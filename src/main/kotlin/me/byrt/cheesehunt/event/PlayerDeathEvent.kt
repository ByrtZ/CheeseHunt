package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.ScoreMode
import me.byrt.cheesehunt.state.GameState
import me.byrt.cheesehunt.manager.Sounds
import me.byrt.cheesehunt.state.Teams

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title

import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

import java.time.Duration

@Suppress("unused")
class PlayerDeathEvent : Listener {
    @EventHandler
    private fun onDeath(e : PlayerDeathEvent) {
        if(Main.getGame().getGameState() != GameState.IN_GAME) {
            e.isCancelled = true
        } else {
            val playerDied = e.player
            death(playerDied)
            if(e.player.killer is Player) {
                val killer = e.player.killer!!
                eliminationDisplay(killer, playerDied)
            } else {
                if(e.player.location.block.type == Material.STRUCTURE_VOID) {
                    if(Main.getGame().getCheeseManager().playerHasCheese(e.player)) Main.getGame().getCheeseManager().playerDropCheese(e.player)
                    voidEliminationDisplay(playerDied)
                }
            }
            e.isCancelled = true
        }
    }

    private fun death(player : Player) {
        player.gameMode = GameMode.SPECTATOR
        player.inventory.clear()
        if(Main.getGame().getTeamManager().isInRedTeam(player.uniqueId)) {
            Main.getGame().getCheeseManager().teamFireworks(player, Teams.RED)
            Main.getGame().getRespawnTask().startRespawnLoop(player, Main.getPlugin(), Teams.RED)
        } else if(Main.getGame().getTeamManager().isInBlueTeam(player.uniqueId)) {
            Main.getGame().getCheeseManager().teamFireworks(player, Teams.BLUE)
            Main.getGame().getRespawnTask().startRespawnLoop(player, Main.getPlugin(), Teams.BLUE)
        }
    }

    private fun eliminationDisplay(player : Player, playerKilled : Player) {
        for(allPlayer in Bukkit.getOnlinePlayers()) {
            if(allPlayer != player) {
                allPlayer.sendMessage(Component.text(playerKilled.name, Main.getGame().getTeamManager().getTeamColour(playerKilled)).append(Component.text(" was eliminated by ", NamedTextColor.WHITE).append(Component.text(player.name, Main.getGame().getTeamManager().getTeamColour(player)).append(Component.text(".", NamedTextColor.WHITE)))))
            } else {
                allPlayer.sendMessage(Component.text("[+5 ").append(Component.text("coins", NamedTextColor.GOLD).append(Component.text("] You eliminated ", NamedTextColor.WHITE)).append(Component.text(playerKilled.name, Main.getGame().getTeamManager().getTeamColour(playerKilled))).append(Component.text("!", NamedTextColor.WHITE))))
            }
        }
        Main.getGame().getScoreManager().modifyScore(5, ScoreMode.ADD, Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId))
        Main.getGame().getInfoBoardManager().updateScoreboardScores()
        player.playSound(player.location, Sounds.Score.ELIMINATION, 1f, 1.25f)
        player.showTitle(
            Title.title(
                Component.text(""),
                Component.text("[").append(Component.text("⚔", NamedTextColor.GREEN).append(Component.text("] ", NamedTextColor.WHITE)).append(Component.text(playerKilled.name, Main.getGame().getTeamManager().getTeamColour(playerKilled)))),
                Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ofSeconds(1))
            )
        )
    }

    private fun voidEliminationDisplay(playerKilled : Player) {
        for(player in Bukkit.getOnlinePlayers()) {
            player.sendMessage(Component.text(playerKilled.name, Main.getGame().getTeamManager().getTeamColour(playerKilled)).append(Component.text(" tried to escape the island... ", NamedTextColor.WHITE)))
        }
    }
}