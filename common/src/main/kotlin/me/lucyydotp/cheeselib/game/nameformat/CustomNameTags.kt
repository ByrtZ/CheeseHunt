package me.lucyydotp.cheeselib.game.nameformat

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent
import me.lucyydotp.cheeselib.inject.context
import me.lucyydotp.cheeselib.module.Module
import me.lucyydotp.cheeselib.module.ModuleHolder
import me.lucyydotp.cheeselib.module.installBukkitListeners
import org.bukkit.GameMode
import org.bukkit.Server
import org.bukkit.entity.Entity
import org.bukkit.entity.Interaction
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerGameModeChangeEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.plugin.Plugin
import java.util.UUID

/**
 * Creates custom nametags for players using interaction entities as passengers.
 * This class expects players to be in teams with nametags turned off already.
 * TODO(lucy): implement team visibility checking, also hide your own nametag
 */
class CustomNameTags(parent: ModuleHolder) : Module(parent), Listener {
    val nameFormatter: NameFormatter by context()
    val server: Server by context()
    val plugin: Plugin by context()

    val entities: MutableMap<UUID, UUID> = mutableMapOf()

    init {
        installBukkitListeners()

        // Update name when it changes
        listen(nameFormatter.afterFormat) {
            it.player.nametagEntity?.customName(it.name)
        }
    }

    /**
     * Spawns a new nametag entity for a player.
     */
    private fun spawnForPlayer(player: Player) {
        // If they already have an entity, don't spawn a new one
        if (player.uniqueId in entities) return

        val entity = player.world.spawn(player.location, Interaction::class.java)
        player.addPassenger(entity)

        entity.customName(nameFormatter.format(player))
        entity.isCustomNameVisible = true
        entity.isPersistent = false
        // Make it nice and tall, so it's big enough to avoid client-side culling.
        // This does come with the unfortunate side effect of making players look like unicorns in f3+b,
        // but we can live with that.
        entity.interactionHeight = 2f
        entity.interactionWidth = 0f

        entities[player.uniqueId] = entity.uniqueId
    }

    /**
     * Removes a player's nametag entity.
     */
    private fun despawnForPlayer(player: Player) {
        player.nametagEntity?.let {
            it.remove()
            entities.remove(player.uniqueId)
        }
    }

    /**
     * The player's nametag entity, if one exists.
     */
    private val Player.nametagEntity: Entity?
        get() = entities[uniqueId]?.let {
            world.getEntity(it)
        }


    @EventHandler
    fun onPlayerRemove(event: EntityRemoveFromWorldEvent) {
        (event.entity as? Player)?.let {
            despawnForPlayer(it)
        }
    }

    @EventHandler
    fun onPlayerSpawn(event: EntityAddToWorldEvent) {
        (event.entity as? Player)?.let {
            server.scheduler.runTask(plugin, Runnable {
                spawnForPlayer(it)
            })
        }
    }

    @EventHandler
    fun onPlayerShift(event: PlayerToggleSneakEvent) {
        val entity = event.player.nametagEntity ?: return
        // Mirror the player's sneaking state.
        entity.isSneaking = event.isSneaking
    }

    @EventHandler
    fun onGameModeChange(event: PlayerGameModeChangeEvent) {
        // Remove the tag in spectator mode.
        if (event.newGameMode == GameMode.SPECTATOR) {
            despawnForPlayer(event.player)
        } else {
            spawnForPlayer(event.player)
        }
    }
}
