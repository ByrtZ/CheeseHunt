package dev.byrt.cheesehunt.task

import dev.byrt.cheesehunt.Main
import dev.byrt.cheesehunt.game.Game
import dev.byrt.cheesehunt.state.Sounds
import dev.byrt.cheesehunt.state.Teams
import dev.byrt.cheesehunt.game.GameState

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

class RespawnTask(private val game: Game) {
    private val respawnLoopMap = mutableMapOf<UUID, BukkitRunnable>()
    fun startRespawnLoop(player : Player, plugin : Plugin, team : Teams) {
        val bukkitRunnable = object: BukkitRunnable() {
            var respawnTimer = 6
            override fun run() {
                respawnTimer--
                if(game.gameManager.getGameState() == GameState.IN_GAME || game.gameManager.getGameState() == GameState.OVERTIME) {
                    if(respawnTimer > 0) {
                        player.playSound(player.location, Sounds.Respawn.RESPAWN_TIMER, 1f, 2f)
                        player.showTitle(Title.title(
                                Component.text("Respawning in...", NamedTextColor.YELLOW),
                                Component.text("►$respawnTimer◄", NamedTextColor.WHITE, TextDecoration.BOLD),
                                Title.Times.times(Duration.ZERO, Duration.ofSeconds(2), Duration.ZERO)
                            )
                        )
                    } else {
                        player.playSound(player.location, Sounds.Respawn.RESPAWN, 1000f, 1f)
                        player.resetTitle()
                    }
                }
                if(respawnTimer == 0) {
                    when(team) {
                        Teams.RED -> {
                            player.teleport(game.locationManager.getRedSpawns()[game.locationManager.getRedSpawnCounter()])
                            game.locationManager.incrementSpawnCounter(Teams.RED)
                        }
                        Teams.BLUE -> {
                            player.teleport(game.locationManager.getBlueSpawns()[game.locationManager.getBlueSpawnCounter()])
                            game.locationManager.incrementSpawnCounter(Teams.BLUE)
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
        game.itemManager.givePlayerTeamBoots(player, Main.getGame().teamManager.getPlayerTeam(player.uniqueId))
        if(game.gameManager.getGameState() == GameState.OVERTIME) {
            game.itemManager.givePlayerPickaxe(player)
        } else {
            game.itemManager.givePlayerKit(player)
        }
        player.gameMode = GameMode.ADVENTURE
    }

    fun getRespawnLoopMap() : Map<UUID, BukkitRunnable> {
        return respawnLoopMap
    }
}