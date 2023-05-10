package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.state.GameState
import me.byrt.cheesehunt.state.RoundState
import me.byrt.cheesehunt.state.Teams

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scoreboard.*

class InfoBoardManager(private val game : Game) {
    private var scoreboardManager: ScoreboardManager = Bukkit.getScoreboardManager()
    private var scoreboard: Scoreboard = scoreboardManager.mainScoreboard
    private lateinit var cheeseHuntBoard: Objective
    private lateinit var currentGameText: Score
    private lateinit var currentMapText: Score
    private lateinit var currentRoundText: Score
    private lateinit var gameStatusText: Score
    private lateinit var gameScoreText: Score
    private lateinit var redScore: Score
    private lateinit var blueScore: Score
    private lateinit var blankSpaceOne: Score
    private lateinit var blankSpaceTwo: Score

    fun buildScoreboard() {
        constructScoreboardInfo()
        cheeseHuntBoard.displaySlot = DisplaySlot.SIDEBAR
        currentGameText.score = 8
        currentMapText.score = 7
        currentRoundText.score = 6
        gameStatusText.score = 5
        blankSpaceOne.score = 4
        gameScoreText.score = 3
        redScore.score = 2
        blueScore.score = 1
        blankSpaceTwo.score = 0
    }

    fun destroyScoreboard() {
        cheeseHuntBoard.displaySlot = null
        cheeseHuntBoard.unregister()
    }

    private fun constructScoreboardInfo() {
        cheeseHuntBoard = scoreboard.registerNewObjective(
        "cheese_hunt_board",
        Criteria.DUMMY,
        Component.text("Cheese Hunt").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true)
        )

        currentGameText = cheeseHuntBoard.getScore(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "Game: " + ChatColor.RESET + "Cheese Hunt")
        currentMapText = cheeseHuntBoard.getScore(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "Map: " + ChatColor.WHITE + "Reforged")
        currentRoundText = cheeseHuntBoard.getScore(ChatColor.GREEN.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "None")
        gameStatusText = cheeseHuntBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game status: " + ChatColor.RESET + "Waiting...")
        gameScoreText = cheeseHuntBoard.getScore(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "Game Coins: " + ChatColor.RESET + "(" + ChatColor.YELLOW + "x1.0" + ChatColor.RESET + ")")
        redScore = cheeseHuntBoard.getScore(" 1. " + ChatColor.RED.toString() + "Red Team " + ChatColor.RESET + "                 0c")
        blueScore = cheeseHuntBoard.getScore(" 2. " + ChatColor.BLUE.toString() + "Blue Team " + ChatColor.RESET + "                0c")
        blankSpaceOne = cheeseHuntBoard.getScore("§")
        blankSpaceTwo = cheeseHuntBoard.getScore("§§")
    }

    fun showScoreboard() {
        cheeseHuntBoard.displaySlot = DisplaySlot.SIDEBAR
    }

    fun updateScoreboardTimer(displayTime : String, previousDisplayTime : String, gameState : GameState) {
        if (gameState === GameState.STARTING) {
            cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game status: " + ChatColor.RESET + "Waiting...")
            cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Time left: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Next round: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.GREEN.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "None")
            cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game ending: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game begins: " + ChatColor.RESET + "" + previousDisplayTime)
            if (game.getRoundState() === RoundState.ROUND_ONE) {
                gameStatusText = cheeseHuntBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game begins: " + ChatColor.RESET + "" + displayTime)
                gameStatusText.score = 5
                cheeseHuntBoard.scoreboard?.resetScores(ChatColor.GREEN.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "None")
                currentRoundText = cheeseHuntBoard.getScore(ChatColor.GREEN.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "1/1")
                currentRoundText.score = 6
            } else {
                cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Round begins: " + ChatColor.RESET + previousDisplayTime)
                gameStatusText = cheeseHuntBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Round begins: " + ChatColor.RESET + "" + displayTime)
                gameStatusText.score = 5
            }
        }
        if (gameState === GameState.IN_GAME) {
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game status: " + ChatColor.RESET + "Waiting...")
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game begins: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Round begins: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Time left: " + ChatColor.RESET + "" + previousDisplayTime)
            gameStatusText = cheeseHuntBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Time left: " + ChatColor.RESET + "" + displayTime)
            gameStatusText.score = 5
        }
        if (gameState === GameState.OVERTIME) {
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game status: " + ChatColor.RESET + "Waiting...")
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game begins: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Round begins: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Time left: " + ChatColor.RESET + "" + previousDisplayTime)
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "OVERTIME: " + ChatColor.RESET + "" + previousDisplayTime)
            gameStatusText = cheeseHuntBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "OVERTIME: " + ChatColor.RESET + "" + displayTime)
            gameStatusText.score = 5
        }
        if (gameState === GameState.ROUND_END) {
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Time left: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Time left: " + ChatColor.RESET + previousDisplayTime)
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Next round: " + ChatColor.RESET + previousDisplayTime)
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "OVERTIME: " + ChatColor.RESET + "" + previousDisplayTime)
            gameStatusText = cheeseHuntBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Next round: " + ChatColor.RESET + "" + displayTime)
            gameStatusText.score = 5
        }
        if (gameState === GameState.GAME_END) {
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Time left: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Next round: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Round begins: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "OVERTIME: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "OVERTIME: " + ChatColor.RESET + "" + previousDisplayTime)
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game ending: " + ChatColor.RESET + previousDisplayTime)

            gameStatusText = if (displayTime == "00:00") {
                cheeseHuntBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game over!")
            } else {
                cheeseHuntBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game ending: " + ChatColor.RESET + "" + displayTime)
            }
            gameStatusText.score = 5
        }
    }

    fun updateScoreboardScores() {
        cheeseHuntBoard.scoreboard?.resetScores(" 1. " + ChatColor.RED.toString() + "Red Team " + ChatColor.RESET + "                 0c")
        cheeseHuntBoard.scoreboard?.resetScores(" 2. " + ChatColor.BLUE.toString() + "Blue Team " + ChatColor.RESET + "                0c")

        cheeseHuntBoard.scoreboard?.resetScores(" 1. " + ChatColor.RED.toString() + "Red Team " + ChatColor.RESET + "                 ${game.getScoreManager().getPreviousRedScore()}c")
        cheeseHuntBoard.scoreboard?.resetScores(" 2. " + ChatColor.RED.toString() + "Red Team " + ChatColor.RESET + "                 ${game.getScoreManager().getPreviousRedScore()}c")
        cheeseHuntBoard.scoreboard?.resetScores(" 1. " + ChatColor.BLUE.toString() + "Blue Team " + ChatColor.RESET + "                ${game.getScoreManager().getPreviousBlueScore()}c")
        cheeseHuntBoard.scoreboard?.resetScores(" 2. " + ChatColor.BLUE.toString() + "Blue Team " + ChatColor.RESET + "                ${game.getScoreManager().getPreviousBlueScore()}c")

        cheeseHuntBoard.scoreboard?.resetScores(" 1. " + ChatColor.RED.toString() + "Red Team " + ChatColor.RESET + "                 ${game.getScoreManager().getRedScore()}c")
        cheeseHuntBoard.scoreboard?.resetScores(" 2. " + ChatColor.RED.toString() + "Red Team " + ChatColor.RESET + "                 ${game.getScoreManager().getRedScore()}c")
        cheeseHuntBoard.scoreboard?.resetScores(" 1. " + ChatColor.BLUE.toString() + "Blue Team " + ChatColor.RESET + "                ${game.getScoreManager().getBlueScore()}c")
        cheeseHuntBoard.scoreboard?.resetScores(" 2. " + ChatColor.BLUE.toString() + "Blue Team " + ChatColor.RESET + "                ${game.getScoreManager().getBlueScore()}c")
        if(game.getScoreManager().calcPlacements().isNullOrEmpty()) {
            redScore = cheeseHuntBoard.getScore(" 1. " + ChatColor.RED.toString() + "Red Team " + ChatColor.RESET + "                 ${game.getScoreManager().getRedScore()}c")
            redScore.score = 2
            blueScore = cheeseHuntBoard.getScore(" 1. " + ChatColor.BLUE.toString() + "Blue Team " + ChatColor.RESET + "                ${game.getScoreManager().getBlueScore()}c")
            blueScore.score = 1
        }
        if(game.getScoreManager().calcPlacements()?.get(0) == Teams.RED) {
            redScore = cheeseHuntBoard.getScore(" 1. " + ChatColor.RED.toString() + "Red Team " + ChatColor.RESET + "                 ${game.getScoreManager().getRedScore()}c")
            redScore.score = 2
            blueScore = cheeseHuntBoard.getScore(" 2. " + ChatColor.BLUE.toString() + "Blue Team " + ChatColor.RESET + "                ${game.getScoreManager().getBlueScore()}c")
            blueScore.score = 1
        }
        if(game.getScoreManager().calcPlacements()?.get(0) == Teams.BLUE) {
            redScore = cheeseHuntBoard.getScore(" 2. " + ChatColor.RED.toString() + "Red Team " + ChatColor.RESET + "                 ${game.getScoreManager().getRedScore()}c")
            redScore.score = 1
            blueScore = cheeseHuntBoard.getScore(" 1. " + ChatColor.BLUE.toString() + "Blue Team " + ChatColor.RESET + "                ${game.getScoreManager().getBlueScore()}c")
            blueScore.score = 2
        }
    }

    fun updateScoreboardMultiplier(currentMultiplier : Int, newMultiplier : Int) {
        cheeseHuntBoard.scoreboard?.resetScores(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "Game Coins: " + ChatColor.RESET + "(" + ChatColor.YELLOW + "x1.0" + ChatColor.RESET + ")")
        cheeseHuntBoard.scoreboard?.resetScores(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "Game Coins: " + ChatColor.RESET + "(" + ChatColor.YELLOW + "x$currentMultiplier.0" + ChatColor.RESET + ")")
        gameScoreText = cheeseHuntBoard.getScore(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "Game Coins: " + ChatColor.RESET + "(" + ChatColor.YELLOW + "x$newMultiplier.0" + ChatColor.RESET + ")")
        gameScoreText.score = 3
    }

    fun updateCurrentMap(currentMap : Maps, newMap : Maps) {
        cheeseHuntBoard.scoreboard?.resetScores(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "Map: " + ChatColor.GRAY + "UNCONFIGURED")
        cheeseHuntBoard.scoreboard?.resetScores(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "Map: " + ChatColor.RESET + currentMap.mapName)
        currentMapText = cheeseHuntBoard.getScore(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "Map: " + ChatColor.RESET + newMap.mapName)
        currentMapText.score = 7
    }
}