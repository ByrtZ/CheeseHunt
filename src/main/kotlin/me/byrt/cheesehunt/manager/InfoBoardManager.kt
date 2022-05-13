package me.byrt.cheesehunt.manager

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.*

class InfoBoardManager(private var game : Game) {
    private var currentGameText: Score? = null
    private var currentMapText: Score? = null
    private var currentRoundText: Score? = null
    private var gameStatusText: Score? = null
    private var blankSpaceOne: Score? = null
    private var blankSpaceTwo: Score? = null
    private var teamAInfo: Score? = null
    private var teamBInfo: Score? = null
    private var scoreboardManager: ScoreboardManager? = null
    private var scoreboard: Scoreboard? = null
    private var cheeseHuntBoard: Objective? = null

    fun buildScoreboard() {
        scoreboardManager = Bukkit.getScoreboardManager()
        scoreboard = scoreboardManager!!.mainScoreboard
        cheeseHuntBoard = scoreboard!!.registerNewObjective(
            "cheese_hunt_board",
            "dummy",
            Component.text("Cheese Hunt").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true)
        )

        cheeseHuntBoard!!.displaySlot = DisplaySlot.SIDEBAR
        currentGameText = cheeseHuntBoard!!.getScore(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "Game: " + ChatColor.RESET + "Cheese Hunt")
        currentGameText!!.score = 7
        currentMapText = cheeseHuntBoard!!.getScore(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "Map: " + ChatColor.RESET + "Classic")
        currentMapText!!.score = 6
        currentRoundText = cheeseHuntBoard!!.getScore(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "None")
        currentRoundText!!.score = 5
        gameStatusText = cheeseHuntBoard!!.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game status: " + ChatColor.RESET + "Waiting...")
        gameStatusText!!.score = 4
        blankSpaceOne = cheeseHuntBoard!!.getScore("§")
        blankSpaceOne!!.score = 3
        teamAInfo = cheeseHuntBoard!!.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Red Team" + ChatColor.RESET + " - 0 Coins")
        teamAInfo!!.score = 2
        teamBInfo = cheeseHuntBoard!!.getScore(ChatColor.BLUE.toString() + ChatColor.BOLD + "Blue Team" + ChatColor.RESET + " - 0 Coins")
        teamBInfo!!.score = 1
        blankSpaceTwo = cheeseHuntBoard!!.getScore("§§")
        blankSpaceTwo!!.score = 0
    }

    fun destroyScoreboard() {
        cheeseHuntBoard?.unregister()
    }

    fun showScoreboard(player : Player) {
        player.scoreboard = scoreboard!!
    }

    fun updateScoreboardTimer(displayTime : String, previousDisplayTime : String, gameState : GameState) {
        if (gameState === GameState.STARTING) {
            cheeseHuntBoard!!.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game status: " + ChatColor.RESET + "Waiting...")
            cheeseHuntBoard!!.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Time left: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard!!.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Next round: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard!!.scoreboard!!.resetScores(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "None")
            cheeseHuntBoard!!.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game ending: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard!!.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game status: " + ChatColor.RESET + "TIMER PAUSED")
            cheeseHuntBoard!!.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game begins: " + ChatColor.RESET + "" + previousDisplayTime)

            if (game.getRoundState() === RoundState.ROUND_ONE) {
                gameStatusText = cheeseHuntBoard!!.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game begins: " + ChatColor.RESET + "" + displayTime)
                gameStatusText!!.score = 4
                cheeseHuntBoard!!.scoreboard?.resetScores(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "None")
                cheeseHuntBoard!!.scoreboard?.resetScores(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "2/2")
                currentRoundText = cheeseHuntBoard!!.getScore(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "1/2")
                currentRoundText!!.score = 5
            } else {
                cheeseHuntBoard!!.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Round begins: " + ChatColor.RESET + previousDisplayTime)
                gameStatusText = cheeseHuntBoard!!.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Round begins: " + ChatColor.RESET + "" + displayTime)
                gameStatusText!!.score = 4

                if (game.getRoundState() === RoundState.ROUND_TWO) {
                    cheeseHuntBoard!!.scoreboard?.resetScores(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "1/2")
                    currentRoundText = cheeseHuntBoard!!.getScore(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "2/2")
                    currentRoundText!!.score = 5
                }
            }
        }
        if (gameState === GameState.IN_GAME) {
            cheeseHuntBoard?.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game status: " + ChatColor.RESET + "Waiting...")
            cheeseHuntBoard?.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game status: " + ChatColor.RESET + "TIMER PAUSED")
            cheeseHuntBoard?.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game begins: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard?.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Round begins: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard?.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Time left: " + ChatColor.RESET + "" + previousDisplayTime)
            gameStatusText = cheeseHuntBoard?.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Time left: " + ChatColor.RESET + "" + displayTime)
            gameStatusText?.score = 4
        }
        if (gameState === GameState.ROUND_END) {
            cheeseHuntBoard?.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Time left: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard?.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Time left: " + ChatColor.RESET + previousDisplayTime)
            cheeseHuntBoard?.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game status: " + ChatColor.RESET + "TIMER PAUSED")
            cheeseHuntBoard?.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Next round: " + ChatColor.RESET + previousDisplayTime)
            gameStatusText = cheeseHuntBoard?.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Next round: " + ChatColor.RESET + "" + displayTime)
            gameStatusText!!.score = 4
        }
        if (gameState === GameState.GAME_END) {
            cheeseHuntBoard?.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Time left: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard?.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Next round: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard?.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Round begins: " + ChatColor.RESET + "00:00")
            cheeseHuntBoard?.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game status: " + ChatColor.RESET + "TIMER PAUSED")
            cheeseHuntBoard?.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game ending: " + ChatColor.RESET + previousDisplayTime)

            gameStatusText = if (displayTime == "00:00") {
                cheeseHuntBoard?.getScore(ChatColor.GREEN.toString() + "" + ChatColor.BOLD + "Event over!")
            } else {
                cheeseHuntBoard?.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game ending: " + ChatColor.RESET + "" + displayTime)
            }
            gameStatusText!!.score = 4
        }
    }
}