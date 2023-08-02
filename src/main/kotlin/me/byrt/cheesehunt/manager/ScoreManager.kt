package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.game.Game
import me.byrt.cheesehunt.state.Sounds
import me.byrt.cheesehunt.state.Teams
import me.byrt.cheesehunt.util.DevStatus

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

@Suppress("unused")
class ScoreManager(private val game : Game) {
    private var redScore = 0
    private var blueScore = 0
    private var previousRedScore = 0
    private var previousBlueScore = 0
    private var placements = ArrayList<Teams>()
    private var previousPlacements = ArrayList<Teams>()
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
        if(redScore > blueScore) {
            game.teamManager.redWinGame()
            game.winShowTask.startWinShowLoop(game.plugin, Teams.RED)
        }
        if(redScore < blueScore) {
            game.teamManager.blueWinGame()
            game.winShowTask.startWinShowLoop(game.plugin, Teams.BLUE)
        }
        if(redScore == blueScore) {
            game.teamManager.noWinGame()
        }
    }

    fun calcPlacements() : ArrayList<Teams>? {
        previousPlacements = placements
        placements.clear()
        if(redScore > blueScore) {
            placements.add(0, Teams.RED)
            placements.add(1, Teams.BLUE)
            return placements
        }
        if(blueScore > redScore) {
            placements.add(0, Teams.BLUE)
            placements.add(1, Teams.RED)
            return placements
        }
        return null
    }

    fun modifyScore(score : Int, mode : ScoreMode, team : Teams) {
        when(mode) {
            ScoreMode.ADD -> {
                if(team == Teams.RED) {
                    previousRedScore = redScore
                    redScore += score
                }
                if(team == Teams.BLUE) {
                    previousBlueScore = blueScore
                    blueScore += score
                }
            }
            ScoreMode.SUB -> {
                if(team == Teams.RED) redScore -= score
                if(team == Teams.BLUE) blueScore -= score
            }
        }
    }

    fun setMultiplier(newMultiplier : Int) {
        game.infoBoardManager.updateScoreboardMultiplier(multiplier, newMultiplier)
        multiplier = newMultiplier
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

    fun getPreviousRedScore() : Int {
        return previousRedScore
    }

    fun getPreviousBlueScore() : Int {
        return previousBlueScore
    }

    fun resetScores() {
        redScore = 0
        blueScore = 0
        previousRedScore = 0
        previousBlueScore = 0
        multiplier = 1
        multiplierMinute = 0
        placements.clear()
        previousPlacements.clear()
    }
}

enum class ScoreMode {
    ADD,
    SUB
}