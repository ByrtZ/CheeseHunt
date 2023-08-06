package dev.byrt.cheesehunt.event

import dev.byrt.cheesehunt.Main
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
        if(Main.getGame().gameManager.getGameState() == GameState.IDLE || Main.getGame().gameManager.getGameState() == GameState.STARTING || Main.getGame().gameManager.getGameState() == GameState.ROUND_END || Main.getGame().gameManager.getGameState() == GameState.GAME_END) {
            e.isCancelled = true
        } else {
            val playerDied = e.player

            if(playerDied.inventory.itemInOffHand.type == Material.TOTEM_OF_UNDYING) {
                Main.getGame().itemManager.useItem(PowerUpItem.RESURRECTION_CHARM, playerDied)
            } else {
                death(playerDied)
                if(e.player.killer is Player && playerDied != e.player.killer) {
                    val killer = e.player.killer!!
                    eliminationDisplay(killer, playerDied)
                } else {
                    if(e.player.location.block.type == Material.STRUCTURE_VOID) {
                        if(Main.getGame().cheeseManager.playerHasCheese(e.player)) {
                            Main.getGame().cheeseManager.playerDropCheese(e.player)
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
        Main.getGame().playerManager.clearPotionEffects(player)
        Main.getGame().statsManager.incrementStat(player.uniqueId, Statistic.DEATHS)
        if(Main.getGame().teamManager.isInRedTeam(player.uniqueId)) {
            Main.getGame().cheeseManager.teamFireworks(player, Teams.RED)
            Main.getGame().respawnTask.startRespawnLoop(player, Main.getPlugin(), Teams.RED)
        } else if(Main.getGame().teamManager.isInBlueTeam(player.uniqueId)) {
            Main.getGame().cheeseManager.teamFireworks(player, Teams.BLUE)
            Main.getGame().respawnTask.startRespawnLoop(player, Main.getPlugin(), Teams.BLUE)
        }
    }

    private fun eliminationDisplay(player : Player, playerKilled : Player) {
        for(allPlayer in Bukkit.getOnlinePlayers()) {
            if(allPlayer != player) {
                allPlayer.sendMessage(Component.text(playerKilled.name, Main.getGame().teamManager.getTeamNamedTextColor(playerKilled)).append(Component.text(" was eliminated by ", NamedTextColor.WHITE).append(Component.text(player.name, Main.getGame().teamManager.getTeamNamedTextColor(player)).append(Component.text(".", NamedTextColor.WHITE)))))
            } else {
                allPlayer.sendMessage(Component.text("[+${5 * Main.getGame().scoreManager.getMultiplier()} ").append(Component.text("coins", NamedTextColor.GOLD).append(Component.text("] You eliminated ", NamedTextColor.WHITE)).append(Component.text(playerKilled.name, Main.getGame().teamManager.getTeamNamedTextColor(playerKilled))).append(Component.text("!", NamedTextColor.WHITE))))
            }
        }
        Main.getGame().scoreManager.modifyScore(5 * Main.getGame().scoreManager.getMultiplier(), ScoreMode.ADD, Main.getGame().teamManager.getPlayerTeam(player.uniqueId))
        Main.getGame().infoBoardManager.updateScoreboardScores()
        Main.getGame().statsManager.incrementStat(player.uniqueId, Statistic.ELIMINATIONS)
        player.playSound(player.location, Sounds.Score.ELIMINATION, 1f, 1.25f)
        player.showTitle(Title.title(Component.text(""), Component.text("[").append(Component.text("⚔", NamedTextColor.GREEN).append(Component.text("] ", NamedTextColor.WHITE)).append(Component.text(playerKilled.name, Main.getGame().teamManager.getTeamNamedTextColor(playerKilled)))), Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ofSeconds(1))))
    }

    private fun voidEliminationDisplay(playerKilled : Player) {
        for(player in Bukkit.getOnlinePlayers()) {
            player.sendMessage(Component.text(playerKilled.name, Main.getGame().teamManager.getTeamNamedTextColor(playerKilled)).append(Component.text(" tried to escape the island...", NamedTextColor.WHITE)))
        }
    }

    private fun tntEliminationDisplay(playerKilled : Player) {
        for(player in Bukkit.getOnlinePlayers()) {
            player.sendMessage(Component.text(playerKilled.name, Main.getGame().teamManager.getTeamNamedTextColor(playerKilled)).append(Component.text(" blew up.", NamedTextColor.WHITE)))
        }
    }
}