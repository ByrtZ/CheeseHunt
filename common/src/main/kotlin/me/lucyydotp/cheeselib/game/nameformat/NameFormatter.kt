package me.lucyydotp.cheeselib.game.nameformat

import com.google.common.cache.CacheBuilder
import me.lucyydotp.cheeselib.module.EventEmitter
import me.lucyydotp.cheeselib.module.Module
import me.lucyydotp.cheeselib.module.ModuleHolder
import me.lucyydotp.cheeselib.module.installBukkitListeners
import net.kyori.adventure.extra.kotlin.text
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.Style
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.UUID
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

/**
 * An event emitted when a player's name is formatted.
 */
data class PlayerNameFormattedEvent(
    /**
     * The player.
     */
    val player: Player,

    /**
     * The player's name.
     */
    val name: Component
)

/**
 * Formats player names.
 */
class NameFormatter(parent: ModuleHolder) : Module(parent), Listener {

    private val cache = CacheBuilder.newBuilder()
        .expireAfterAccess(10.minutes.toJavaDuration())
        .build<UUID, Component>()

    val afterFormat = EventEmitter<PlayerNameFormattedEvent>()
    val format = EventEmitter<NameBuilder>()

    init {
        installBukkitListeners()
    }

    data class NameBuilder(
        val player: Player,
        var prefixes: List<Component> = mutableListOf(),
        var usernameStyle: Style = Style.empty(),
        var suffixes: List<Component> = mutableListOf()
    )

    /**
     * Gets a [player]'s formatted name.
     *
     * If not cached, or if [force] is true, the player's name is calculated by dispatching a [NameBuilder] event
     * and combining the results into a component. The player's [display name][Player.displayName] is also set to the result.
     */
    fun format(player: Player, force: Boolean = false): Component {
        if (force) {
            cache.invalidate(player.uniqueId)
        }

        return cache.get(player.uniqueId) {
            val formattedName = NameBuilder(player)

            format.emit(formattedName)

            return@get text {
                append(formattedName.prefixes)
                append(Component.text(player.name, formattedName.usernameStyle))
                append(formattedName.suffixes)
            }.also {
                player.displayName(it)
                afterFormat.emit(PlayerNameFormattedEvent(player, it))
            }
        }
    }

    /**
     * Caches player names on join.
     */
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        format(event.player)
    }

    /**
     * Removes player names on quit.
     */
    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        cache.invalidate(event.player.uniqueId)
    }
}
