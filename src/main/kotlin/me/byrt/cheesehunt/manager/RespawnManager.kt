package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.Main

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title

import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

import java.time.Duration
import java.util.*

@Suppress("unused")
class RespawnManager(private val game: Game) {
    private val respawnLoopMap = mutableMapOf<UUID, BukkitRunnable>()

    fun startRespawnLoop(player : Player, plugin : Plugin, team : Teams) {
        val bukkitRunnable = object: BukkitRunnable() {
            var respawnTimer = 5
            override fun run() {
                respawnTimer--
                player.playSound(player.location, Sounds.Respawn.RESPAWN_TIMER, 1f, 2f)
                player.showTitle(
                    Title.title(
                    Component.text("Respawning in...", NamedTextColor.YELLOW),
                    Component.text("►$respawnTimer◄", NamedTextColor.WHITE, TextDecoration.BOLD),
                        Title.Times.times(Duration.ZERO, Duration.ofSeconds(2), Duration.ZERO)
                    )
                )
                if(respawnTimer == 0) {
                    when(team) {
                        Teams.RED -> {
                            player.teleport(game.getLocationManager().getRedSpawns()[game.getLocationManager().getRedSpawnCounter()])
                            game.getLocationManager().incrementSpawnCounter(Teams.RED)
                        }
                        Teams.BLUE -> {
                            player.teleport(game.getLocationManager().getBlueSpawns()[game.getLocationManager().getBlueSpawnCounter()])
                            game.getLocationManager().incrementSpawnCounter(Teams.BLUE)
                        } else -> {
                            //no.
                        }
                    }
                    stopRespawnLoop(player)
                }
            }
        }
        bukkitRunnable.runTaskTimer(plugin, 0L, 20L)
        respawnLoopMap[player.uniqueId] = bukkitRunnable
    }

    fun stopRespawnLoop(player : Player) {
        respawnLoopMap.remove(player.uniqueId)?.cancel()
        game.getItemManager().givePlayerKit(player)
        game.getItemManager().playerJoinTeamEquip(player, Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId))
        player.gameMode = GameMode.ADVENTURE
        player.resetTitle()
    }
}