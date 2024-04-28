package dev.byrt.cheesehunt.manager

import dev.byrt.cheesehunt.CheeseHunt
import dev.byrt.cheesehunt.game.Game
import dev.byrt.cheesehunt.state.Sounds
import dev.byrt.cheesehunt.state.Teams
import dev.byrt.cheesehunt.util.DevStatus

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title

import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Location
import org.bukkit.entity.Firework
import org.bukkit.entity.Player

import java.time.Duration

import kotlin.random.Random

class ScoreManager(private val game : Game) {
    private var redScore = 0
    private var blueScore = 0
    private var multiplier = 1
    private var multiplierMinute = 0

    fun teamPlacements() {
        for(player in Bukkit.getOnlinePlayers()) {
            player.sendMessage(Component.text("Red Team", NamedTextColor.RED, TextDecoration.BOLD)
                .append(Component.text(": $redScore ", NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false))
                .append(Component.text("coins", NamedTextColor.GOLD))
                .append(Component.text(".", NamedTextColor.WHITE)))
            player.sendMessage(Component.text("Blue Team", NamedTextColor.BLUE, TextDecoration.BOLD)
                .append(Component.text(": $blueScore ", NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false))
                .append(Component.text("coins", NamedTextColor.GOLD))
                .append(Component.text(".", NamedTextColor.WHITE)))
        }
        winCheck()
    }

    private fun winCheck() {
        if (redScore == blueScore) {
            Bukkit.getOnlinePlayers().forEach {
                it.playSound(it.location, Sounds.Round.DRAW_ROUND, 1f, 2f)
                it.sendMessage(
                    Component.text("\nNo team won!\n").color(NamedTextColor.YELLOW).decoration(
                        TextDecoration.BOLD, true
                    )
                )
                it.showTitle(
                    Title.title(
                        Component.text("No team won the game!").color(NamedTextColor.YELLOW),
                        Component.text("It was a draw.").color(NamedTextColor.YELLOW),
                        Title.Times.times(
                            Duration.ofSeconds(1),
                            Duration.ofSeconds(5),
                            Duration.ofSeconds(1)
                        )
                    )
                )
            }
            return
        }

        val winningTeam = if (redScore > blueScore) Teams.RED else Teams.BLUE

        for (player in Bukkit.getOnlinePlayers()) {
            val team = game.teams.getTeam(player) ?: continue
            if(team == winningTeam) {
                player.playSound(player.location, Sounds.Round.WIN_ROUND, 1f, 1f)
                game.cheeseManager.teamFireworks(player, Teams.BLUE)
                player.sendMessage(Component.text("\nYour team won the game!\n").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true))
                player.showTitle(
                    Title.title(
                        Component.text("Your team won!").color(NamedTextColor.GREEN),
                        Component.text("Well done!").color(NamedTextColor.GREEN),
                        Title.Times.times(
                            Duration.ofSeconds(1),
                            Duration.ofSeconds(5),
                            Duration.ofSeconds(1)
                        )
                    )
                )
            } else {
                player.playSound(player.location, Sounds.Round.LOSE_ROUND, 1f, 1f)
                player.sendMessage(Component.text("\nYour team lost the game!\n").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true))
                player.showTitle(
                    Title.title(
                        Component.text("Your team lost!").color(NamedTextColor.RED),
                        Component.text("Better luck next time.").color(NamedTextColor.RED),
                        Title.Times.times(
                            Duration.ofSeconds(1),
                            Duration.ofSeconds(5),
                            Duration.ofSeconds(1)
                        )
                    )
                )
            }
        }
    }

    fun modifyScore(score : Int, mode : ScoreMode, team : Teams) {
        when(mode) {
            ScoreMode.ADD -> {
                if(team == Teams.RED) {
                    redScore += score
                }
                if(team == Teams.BLUE) {
                    blueScore += score
                }
            }
            ScoreMode.SUB -> {
                if(team == Teams.RED) redScore -= score
                if(team == Teams.BLUE) blueScore -= score
            }
        }
        game.tabListManager.updateAllTabList()
    }

    fun setMultiplier(newMultiplier : Int) {
        multiplier = newMultiplier
        game.infoBoardManager.updateScoreboardMultiplier()
        if(newMultiplier == 1) {
            for(player in Bukkit.getOnlinePlayers()) {
                player.playSound(player.location, Sounds.Score.MULTIPLIER_RESET, 1f, 1f)
                player.sendMessage(
                    Component.text("[")
                        .append(Component.text("▶").color(NamedTextColor.GOLD))
                        .append(Component.text("] "))
                        .append(Component.text("Coin multiplier has returned to ", NamedTextColor.GREEN, TextDecoration.BOLD))
                        .append(Component.text("x$multiplier.0", NamedTextColor.YELLOW, TextDecoration.BOLD))
                        .append(Component.text(".", NamedTextColor.GREEN, TextDecoration.BOLD))
                )
            }
        } else {
            for(player in Bukkit.getOnlinePlayers()) {
                player.playSound(player.location, Sounds.Score.NEW_MULTIPLIER, 0.5f, 2f)
                player.sendMessage(
                    Component.text("[")
                        .append(Component.text("▶").color(NamedTextColor.GOLD))
                        .append(Component.text("] "))
                        .append(Component.text("Coin multiplier is now ", NamedTextColor.YELLOW, TextDecoration.BOLD))
                        .append(Component.text("x$multiplier.0 ", NamedTextColor.GOLD, TextDecoration.BOLD))
                        .append(Component.text("for 1 minute!", NamedTextColor.YELLOW, TextDecoration.BOLD))
                )
                player.showTitle(Title.title(Component.text("MULTIPLIER MINUTE", NamedTextColor.GOLD, TextDecoration.BOLD), Component.text("x${multiplier}.0 coins for 1 minute").color(NamedTextColor.YELLOW), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(5), Duration.ofSeconds(1))))
                multiplierMinuteFirework(player)
            }
        }
    }

    private fun multiplierMinuteFirework(player : Player) {
        val playerLoc = Location(player.world, player.location.x, player.location.y + 20.0, player.location.z)
        val f: Firework = player.world.spawn(playerLoc, Firework::class.java)
        val fm = f.fireworkMeta
        fm.addEffect(
            FireworkEffect.builder()
                .flicker(true)
                .trail(false)
                .with(FireworkEffect.Type.BALL_LARGE)
                .withColor(Color.YELLOW, Color.ORANGE)
                .withFade(Color.YELLOW, Color.ORANGE)
                .build()
        )
        fm.power = 0
        f.fireworkMeta = fm
        f.ticksToDetonate = 1
    }

    fun getMultiplier() : Int {
        return multiplier
    }

    fun setNewRandomMultiplierMinute() {
        multiplierMinute = Random.nextInt(1, 10)
        game.dev.parseDevMessage("Multiplier minute will occur at $multiplierMinute.5 minutes remaining.", DevStatus.INFO)
    }

    fun getMultiplierMinute() : Int {
        return multiplierMinute
    }

    fun getRedScore() : Int {
        return redScore
    }

    fun getBlueScore() : Int {
        return blueScore
    }

    fun getScore(team: Teams) = when (team) {
        Teams.RED -> getRedScore()
        Teams.BLUE -> getBlueScore()
    }

    fun resetScores() {
        redScore = 0
        blueScore = 0
        multiplier = 1
        multiplierMinute = 0
    }
}

enum class ScoreMode {
    ADD,
    SUB
}
