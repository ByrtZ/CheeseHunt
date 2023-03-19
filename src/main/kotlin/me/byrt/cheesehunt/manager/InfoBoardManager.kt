package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.Main

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
    private lateinit var blankSpaceOne: Score
    private lateinit var blankSpaceTwo: Score
    private lateinit var redCheesePlaced: Score
    private lateinit var blueCheesePlaced: Score
    private lateinit var redCheeseCollected: Score
    private lateinit var blueCheeseCollected: Score

    fun buildScoreboard() {
        constructScoreboardInfo()
        cheeseHuntBoard.displaySlot = DisplaySlot.SIDEBAR
        currentGameText.score = 7
        currentMapText.score = 6
        currentRoundText.score = 5
        gameStatusText.score = 4
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
        currentMapText = cheeseHuntBoard.getScore(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "Map: " + ChatColor.RESET + "Classic")
        currentRoundText = cheeseHuntBoard.getScore(ChatColor.GREEN.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "None")
        gameStatusText = cheeseHuntBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game status: " + ChatColor.RESET + "Waiting...")

        blankSpaceOne = cheeseHuntBoard.getScore("§")
        blankSpaceTwo = cheeseHuntBoard.getScore("§§")
        redCheesePlaced = cheeseHuntBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Red Team  - " + ChatColor.RESET + "0/0 placed")
        blueCheesePlaced = cheeseHuntBoard.getScore(ChatColor.BLUE.toString() + "" + ChatColor.BOLD + "Blue Team - " + ChatColor.RESET + "0/0 placed")
        redCheeseCollected = cheeseHuntBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Red Team  - " + ChatColor.RESET + "0/0 collected")
        blueCheeseCollected = cheeseHuntBoard.getScore(ChatColor.BLUE.toString() + "" + ChatColor.BOLD + "Blue Team - " + ChatColor.RESET + "0/0 collected")
    }

    fun showScoreboard() {
        cheeseHuntBoard.displaySlot = DisplaySlot.SIDEBAR
    }

    fun hideScoreboard() {
        cheeseHuntBoard.displaySlot = null
    }

    fun addPlacedStatsToScoreboard() {
        blankSpaceOne.score = 3
        redCheesePlaced.score = 2
        blueCheesePlaced.score = 1
        blankSpaceTwo.score = 0
    }

    fun updatePlacedStats() {
        cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Red Team  - " + ChatColor.RESET + "0/0 placed")
        cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.BLUE.toString() + "" + ChatColor.BOLD + "Blue Team - " + ChatColor.RESET + "0/0 placed")
        cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Red Team  - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getRedCheesePlaced()-1}/${Main.getGame().getTeamManager().getRedTeam().size*4} placed")
        redCheesePlaced = cheeseHuntBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Red Team  - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getRedCheesePlaced()}/${Main.getGame().getTeamManager().getRedTeam().size*4} placed")
        redCheesePlaced.score = 2
        cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.BLUE.toString() + "" + ChatColor.BOLD + "Blue Team - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getBlueCheesePlaced()-1}/${Main.getGame().getTeamManager().getBlueTeam().size*4} placed")
        blueCheesePlaced = cheeseHuntBoard.getScore(ChatColor.BLUE.toString() + "" + ChatColor.BOLD + "Blue Team - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getBlueCheesePlaced()}/${Main.getGame().getTeamManager().getBlueTeam().size*4} placed")
        blueCheesePlaced.score = 1
    }

    fun addCollectedStatsToScoreboard() {
        blankSpaceOne.score = 3
        cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.RED.toString() + ChatColor.BOLD + "Red Team  - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getRedCheesePlaced()}/${Main.getGame().getTeamManager().getRedTeam().size*4} placed")
        cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.BLUE.toString() + ChatColor.BOLD + "Blue Team - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getBlueCheesePlaced()}/${Main.getGame().getTeamManager().getBlueTeam().size*4} placed")
        redCheeseCollected.score = 2
        blueCheeseCollected.score = 1
        blankSpaceTwo.score = 0
    }

    fun updateCollectedStats() {
        cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Red Team  - " + ChatColor.RESET + "0/0 collected")
        cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.BLUE.toString() + "" + ChatColor.BOLD + "Blue Team - " + ChatColor.RESET + "0/0 collected")
        cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Red Team  - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getRedCheeseCollected()-1}/${Main.getGame().getCheeseManager().getBlueCheesePlaced()} collected")
        redCheeseCollected = cheeseHuntBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Red Team  - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getRedCheeseCollected()}/${Main.getGame().getCheeseManager().getBlueCheesePlaced()} collected")
        cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.BLUE.toString() + "" + ChatColor.BOLD + "Blue Team - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getBlueCheeseCollected()-1}/${Main.getGame().getCheeseManager().getRedCheesePlaced()} collected")
        blueCheeseCollected = cheeseHuntBoard.getScore(ChatColor.BLUE.toString() + "" + ChatColor.BOLD + "Blue Team - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getBlueCheeseCollected()}/${Main.getGame().getCheeseManager().getRedCheesePlaced()} collected")
        redCheeseCollected.score = 2
        blueCheeseCollected.score = 1
    }

    fun updateScoreboardTimer(displayTime : String, previousDisplayTime : String, gameState : GameState) {
        if (gameState === GameState.STARTING) {
            cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game status: " + ChatColor.RESET + "Waiting...")
            cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Time left: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Next round: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.GREEN.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "None")
            cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game ending: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game status: " + ChatColor.RESET + "TIMER PAUSED")
            cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game begins: " + ChatColor.RESET + "" + previousDisplayTime)

            if (game.getRoundState() === RoundState.ROUND_ONE) {
                gameStatusText = cheeseHuntBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game begins: " + ChatColor.RESET + "" + displayTime)
                gameStatusText.score = 4
                cheeseHuntBoard.scoreboard?.resetScores(ChatColor.GREEN.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "None")
                cheeseHuntBoard.scoreboard?.resetScores(ChatColor.GREEN.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "2/2")
                currentRoundText = cheeseHuntBoard.getScore(ChatColor.GREEN.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "1/2")
                currentRoundText.score = 5
            } else {
                cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Round begins: " + ChatColor.RESET + previousDisplayTime)
                gameStatusText = cheeseHuntBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Round begins: " + ChatColor.RESET + "" + displayTime)
                gameStatusText.score = 4

                if (game.getRoundState() === RoundState.ROUND_TWO) {
                    cheeseHuntBoard.scoreboard?.resetScores(ChatColor.GREEN.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "1/2")
                    currentRoundText = cheeseHuntBoard.getScore(ChatColor.GREEN.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "2/2")
                    currentRoundText.score = 5
                }
            }
        }
        if (gameState === GameState.IN_GAME) {
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game status: " + ChatColor.RESET + "Waiting...")
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game status: " + ChatColor.RESET + "TIMER PAUSED")
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game begins: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Round begins: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Time left: " + ChatColor.RESET + "" + previousDisplayTime)
            gameStatusText = cheeseHuntBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Time left: " + ChatColor.RESET + "" + displayTime)
            gameStatusText.score = 4
        }
        if (gameState === GameState.ROUND_END) {
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Time left: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Time left: " + ChatColor.RESET + previousDisplayTime)
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game status: " + ChatColor.RESET + "TIMER PAUSED")
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Next round: " + ChatColor.RESET + previousDisplayTime)
            gameStatusText = cheeseHuntBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Next round: " + ChatColor.RESET + "" + displayTime)
            gameStatusText.score = 4
        }
        if (gameState === GameState.GAME_END) {
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Time left: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Next round: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Round begins: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game status: " + ChatColor.RESET + "TIMER PAUSED")
            cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game ending: " + ChatColor.RESET + previousDisplayTime)

            gameStatusText = if (displayTime == "00:00") {
                cheeseHuntBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game over!")
            } else {
                cheeseHuntBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game ending: " + ChatColor.RESET + "" + displayTime)
            }
            gameStatusText.score = 4
        }
    }
}