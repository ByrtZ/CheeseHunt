package dev.byrt.cheesehunt.event

import dev.byrt.cheesehunt.CheeseHunt
import dev.byrt.cheesehunt.manager.ScoreMode
import dev.byrt.cheesehunt.manager.PowerUpItem
import dev.byrt.cheesehunt.game.GameState
import dev.byrt.cheesehunt.state.Sounds
import dev.byrt.cheesehunt.manager.Statistic
import dev.byrt.cheesehunt.state.Teams

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title

import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

import java.time.Duration

@Suppress("unused")
class PlayerDeathEvent : Listener {
    @EventHandler
    private fun onDeath(e : PlayerDeathEvent) {
        if(CheeseHunt.getGame().gameManager.getGameState() == GameState.IDLE || CheeseHunt.getGame().gameManager.getGameState() == GameState.STARTING || CheeseHunt.getGame().gameManager.getGameState() == GameState.ROUND_END || CheeseHunt.getGame().gameManager.getGameState() == GameState.GAME_END) {
            e.isCancelled = true
        } else {
            val playerDied = e.player
            if(playerDied.inventory.itemInOffHand.type == Material.TOTEM_OF_UNDYING) {
                CheeseHunt.getGame().itemManager.useItem(PowerUpItem.RESURRECTION_CHARM, playerDied)
            } else {
                death(playerDied)
                if(e.player.killer is Player && playerDied != e.player.killer) {
                    val killer = e.player.killer!!
                    eliminationDisplay(killer, playerDied)
                } else {
                    if(e.player.location.block.type == Material.STRUCTURE_VOID) {
                        if(CheeseHunt.getGame().cheeseManager.playerHasCheese(e.player)) {
                            CheeseHunt.getGame().cheeseManager.playerDropCheese(e.player)
                        }
                        voidEliminationDisplay(playerDied)
                    }
                    if(e.player.killer is TNTPrimed || playerDied == e.player.killer) {
                        tntEliminationDisplay(playerDied)
                    }
                }
            }
            e.isCancelled = true
        }
    }

    private fun death(player : Player) {
        player.gameMode = GameMode.SPECTATOR
        player.inventory.clear()
        CheeseHunt.getGame().playerManager.clearPotionEffects(player)
        CheeseHunt.getGame().statsManager.updateStatistic(player.uniqueId, Statistic.DEATHS)
        if(CheeseHunt.getGame().teamManager.isInRedTeam(player.uniqueId)) {
            CheeseHunt.getGame().cheeseManager.teamFireworks(player, Teams.RED)
            CheeseHunt.getGame().respawnTask.startRespawnLoop(player, CheeseHunt.getPlugin(), Teams.RED)
            CheeseHunt.getGame().statsManager.updateStatistic(player.uniqueId, Statistic.KILL_STREAKS)
        } else if(CheeseHunt.getGame().teamManager.isInBlueTeam(player.uniqueId)) {
            CheeseHunt.getGame().cheeseManager.teamFireworks(player, Teams.BLUE)
            CheeseHunt.getGame().respawnTask.startRespawnLoop(player, CheeseHunt.getPlugin(), Teams.BLUE)
            CheeseHunt.getGame().statsManager.updateStatistic(player.uniqueId, Statistic.KILL_STREAKS)
        }
    }

    private fun eliminationDisplay(player : Player, playerKilled : Player) {
        for(allPlayer in Bukkit.getOnlinePlayers()) {
            if(allPlayer != player) {
                allPlayer.sendMessage(Component.text(playerKilled.name, CheeseHunt.getGame().teamManager.getTeamNamedTextColor(playerKilled)).append(Component.text(" was eliminated by ", NamedTextColor.WHITE).append(Component.text(player.name, CheeseHunt.getGame().teamManager.getTeamNamedTextColor(player)).append(Component.text(".", NamedTextColor.WHITE)))))
            } else {
                allPlayer.sendMessage(Component.text("[+${5 * CheeseHunt.getGame().scoreManager.getMultiplier()} ").append(Component.text("coins", NamedTextColor.GOLD).append(Component.text("] You eliminated ", NamedTextColor.WHITE)).append(Component.text(playerKilled.name, CheeseHunt.getGame().teamManager.getTeamNamedTextColor(playerKilled))).append(Component.text("!", NamedTextColor.WHITE))))
            }
        }
        CheeseHunt.getGame().scoreManager.modifyScore(5 * CheeseHunt.getGame().scoreManager.getMultiplier(), ScoreMode.ADD, CheeseHunt.getGame().teamManager.getPlayerTeam(player.uniqueId))
        CheeseHunt.getGame().infoBoardManager.updateScoreboardScores()
        CheeseHunt.getGame().statsManager.updateStatistic(player.uniqueId, Statistic.ELIMINATIONS)
        CheeseHunt.getGame().statsManager.updateStatistic(player.uniqueId, Statistic.KILL_STREAKS)
        player.playSound(player.location, Sounds.Score.ELIMINATION, 1f, 1.25f)
        player.showTitle(Title.title(Component.text(""), Component.text("[").append(Component.text("⚔", NamedTextColor.GREEN).append(Component.text("] ", NamedTextColor.WHITE)).append(Component.text(playerKilled.name, CheeseHunt.getGame().teamManager.getTeamNamedTextColor(playerKilled)))), Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ofSeconds(1))))
    }

    private fun voidEliminationDisplay(playerKilled : Player) {
        for(player in Bukkit.getOnlinePlayers()) {
            player.sendMessage(Component.text(playerKilled.name, CheeseHunt.getGame().teamManager.getTeamNamedTextColor(playerKilled)).append(Component.text(" tried to escape the island...", NamedTextColor.WHITE)))
        }
    }

    private fun tntEliminationDisplay(playerKilled : Player) {
        for(player in Bukkit.getOnlinePlayers()) {
            player.sendMessage(Component.text(playerKilled.name, CheeseHunt.getGame().teamManager.getTeamNamedTextColor(playerKilled)).append(Component.text(" blew up.", NamedTextColor.WHITE)))
        }
    }
}
