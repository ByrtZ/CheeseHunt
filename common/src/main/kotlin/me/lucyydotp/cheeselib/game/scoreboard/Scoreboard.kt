package me.lucyydotp.cheeselib.game.scoreboard

import io.papermc.paper.adventure.PaperAdventure
import me.lucyydotp.cheeselib.inject.context
import me.lucyydotp.cheeselib.module.Module
import me.lucyydotp.cheeselib.module.ModuleHolder
import me.lucyydotp.cheeselib.module.installBukkitListeners
import me.lucyydotp.cheeselib.util.nms
import net.minecraft.network.chat.numbers.BlankFormat
import net.minecraft.network.chat.numbers.FixedFormat
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBundlePacket
import net.minecraft.network.protocol.game.ClientboundResetScorePacket
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket
import net.minecraft.network.protocol.game.ClientboundSetScorePacket
import net.minecraft.world.scores.DisplaySlot
import net.minecraft.world.scores.Objective
import net.minecraft.world.scores.criteria.ObjectiveCriteria
import org.bukkit.Server
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.slf4j.Logger
import java.util.UUID
import kotlin.properties.Delegates
import kotlin.reflect.KProperty
import net.kyori.adventure.text.Component as KyoriComponent

/**
 * A packet-based scoreboard. This class is not thread-safe.
 */
class Board(parent: ModuleHolder, title: KyoriComponent = KyoriComponent.empty()) : Module(parent), Listener {

    private companion object {
        private val NAME_PREFIX = "cheeselib_"
        private val SECTION_SCORE_INTERVAL = 64
    }


    inner class Section(val ordinal: Int, lines: List<Line>) {
        var lines: List<Line> = lines
            set(value) {
                val lastSize = field.size
                field = value
                updateSection(this, lastSize)
            }

        operator fun getValue(thisRef: Any?, property: KProperty<*>) = lines

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: List<Line>) {
            lines = value
        }
    }

    data class Line(val text: KyoriComponent, val score: KyoriComponent? = null)

    init {
        installBukkitListeners()

        onEnable {
            server.onlinePlayers.forEach {
                createFor(it, true)
            }
        }

        onDisable {
            server.onlinePlayers.forEach(::removeFor)
        }
    }

    /**
     * The scoreboard's title.
     */
    var title: KyoriComponent by Delegates.observable(title) { _, _, newValue ->
        fakeObjective.displayName = PaperAdventure.asVanilla(newValue)
        if (!isEnabled) return@observable
        server.onlinePlayers.forEach {
            createFor(it, false)
        }
    }

    private val server: Server by context()

    // Scoreboard packets aren't records and can't be constructed manually,
    // so use a fake objective to create them instead.
    private val fakeObjective = Objective(
        null,
        NAME_PREFIX + UUID.randomUUID(),
        ObjectiveCriteria.DUMMY,
        PaperAdventure.asVanilla(title),
        ObjectiveCriteria.RenderType.INTEGER,
        true,
        BlankFormat()
    );

    private val sections = mutableListOf<Section>()

    fun section(ordinal: Int = sections.size, defaultLines: List<Line> = listOf()) =
        sections.getOrNull(ordinal) ?: Section(ordinal, defaultLines).also {
            sections.add(ordinal, it)
        }

    private fun createFor(player: Player, creating: Boolean) {
        player.nms.connection.send(
            ClientboundSetObjectivePacket(
                fakeObjective, if (creating) 0 else 2
            )
        )

        if (creating) {
            player.nms.connection.send(
                ClientboundSetDisplayObjectivePacket(
                    DisplaySlot.SIDEBAR,
                    fakeObjective,
                )
            )

            player.nms.connection.send(
                ClientboundBundlePacket(sections.flatMap { sectionToPackets(it, 0) })
            )

        }
    }

    private fun removeFor(player: Player) {
        player.nms.connection.send(
            ClientboundSetObjectivePacket(fakeObjective, 1)
        )
    }

    private fun sectionToPackets(section: Section, lastSize: Int): List<Packet<ClientGamePacketListener>> {
        val lines = section.lines
        val sectionOrdinal = (sections.size - section.ordinal - 1) * SECTION_SCORE_INTERVAL

        return section.lines.mapIndexed { i, line ->
            ClientboundSetScorePacket(
                UUID(0, (sectionOrdinal + SECTION_SCORE_INTERVAL - i).toLong()).toString(),
                fakeObjective.name,
                sectionOrdinal + SECTION_SCORE_INTERVAL - i,
                PaperAdventure.asVanilla(line.text),
                line.score?.let { FixedFormat(PaperAdventure.asVanilla(it)) },
            )
        } + (0..(lastSize - lines.size).coerceAtLeast(0)).map {
            ClientboundResetScorePacket(
                UUID(0, (sectionOrdinal + lines.size + SECTION_SCORE_INTERVAL - it).toLong()).toString(),
                fakeObjective.name,
            )
        }
    }


    private fun updateSection(section: Section, lastSize: Int) {
        if (!this.isEnabled) return

        val bundle = ClientboundBundlePacket(sectionToPackets(section, lastSize))

        server.onlinePlayers.forEach {
            it.nms.connection.send(bundle)
        }
    }

    private val logger by context<Logger>()

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        logger.info("Sending scoreboard to ${event.player.name}")
        createFor(event.player, true)
    }
}
