package me.byrt.cheesehunt.task

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.Game
import me.byrt.cheesehunt.manager.Sounds
import me.byrt.cheesehunt.state.Teams
import me.byrt.cheesehunt.state.GameState

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
class RespawnTask(private val game: Game) {
    private val respawnLoopMap = mutableMapOf<UUID, BukkitRunnable>()

    fun startRespawnLoop(player : Player, plugin : Plugin, team : Teams) {
        val bukkitRunnable = object: BukkitRunnable() {
            var respawnTimer = 6
            override fun run() {
                respawnTimer--
                if(game.getGameState() == GameState.IN_GAME) {
                    if(respawnTimer > 0) {
                        player.playSound(player.location, Sounds.Respawn.RESPAWN_TIMER, 1f, 2f)
                    } else {
                        player.playSound(player.location, Sounds.Respawn.RESPAWN, 1000f, 1f)
                    }
                    player.showTitle(
                        Title.title(
                            Component.text("Respawning in...", NamedTextColor.YELLOW),
                            Component.text("►$respawnTimer◄", NamedTextColor.WHITE, TextDecoration.BOLD),
                            Title.Times.times(Duration.ZERO, Duration.ofSeconds(2), Duration.ZERO)
                        )
                    )
                }
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
        game.getItemManager().givePlayerTeamBoots(player, Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId))
        player.gameMode = GameMode.ADVENTURE
        player.resetTitle()
    }
}