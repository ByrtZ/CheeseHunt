package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.command.ModifierOptions
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.*

class InfoBoardManager(private val game : Game) {
    private var scoreboardManager: ScoreboardManager = Bukkit.getScoreboardManager()
    private var scoreboard: Scoreboard = scoreboardManager.mainScoreboard
    private var cheeseHuntBoard: Objective = scoreboard.registerNewObjective(
        "cheese_hunt_board",
        Criteria.DUMMY,
        Component.text("Cheese Hunt").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true)
    )

    private var currentGameText: Score = cheeseHuntBoard.getScore(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "Game: " + ChatColor.RESET + "Cheese Hunt")
    private var currentMapText: Score = cheeseHuntBoard.getScore(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "Map: " + ChatColor.RESET + "Classic")
    private var currentRoundText: Score = cheeseHuntBoard.getScore(ChatColor.GREEN.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "None")
    private var currentModifierText: Score = cheeseHuntBoard.getScore(ChatColor.GOLD.toString() + "" + ChatColor.BOLD + "Modifier: " + ChatColor.DARK_GRAY + "Inactive")
    private var gameStatusText: Score = cheeseHuntBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Game status: " + ChatColor.RESET + "Waiting...")

    private var blankSpaceOne: Score = cheeseHuntBoard.getScore("§")
    private var blankSpaceTwo: Score = cheeseHuntBoard.getScore("§§")
    private var redCheesePlaced: Score = cheeseHuntBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Red Team  - " + ChatColor.RESET + "0/0 placed")
    private var blueCheesePlaced: Score = cheeseHuntBoard.getScore(ChatColor.BLUE.toString() + "" + ChatColor.BOLD + "Blue Team - " + ChatColor.RESET + "0/0 placed")
    private var redCheeseCollected: Score = cheeseHuntBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Red Team  - " + ChatColor.RESET + "0/0 collected")
    private var blueCheeseCollected: Score = cheeseHuntBoard.getScore(ChatColor.BLUE.toString() + "" + ChatColor.BOLD + "Blue Team - " + ChatColor.RESET + "0/0 collected")

    fun buildScoreboard() {
        cheeseHuntBoard.displaySlot = DisplaySlot.SIDEBAR
        currentGameText.score = 8
        currentMapText.score = 7
        currentRoundText.score = 6
        currentModifierText.score = 5
        gameStatusText.score = 4
    }

    fun destroyScoreboard() {
        cheeseHuntBoard.unregister()
    }

    fun showScoreboard(player : Player) {
        player.scoreboard = scoreboard
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
        if(Main.getGame().getModifier() == ModifierOptions.BOTTOMLESS_CHEESE) {
            redCheesePlaced = cheeseHuntBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Red Team  - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getRedCheesePlaced()}/∞ placed")
            cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Red Team  - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getRedCheesePlaced()-1}/∞ placed")
            redCheesePlaced.score = 2
            cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.BLUE.toString() + "" + ChatColor.BOLD + "Blue Team - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getBlueCheesePlaced()-1}/∞ placed")
            blueCheesePlaced = cheeseHuntBoard.getScore(ChatColor.BLUE.toString() + "" + ChatColor.BOLD + "Blue Team - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getBlueCheesePlaced()}/∞ placed")
            blueCheesePlaced.score = 1
        } else {
            cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Red Team  - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getRedCheesePlaced()-1}/${Main.getGame().getTeamManager().getRedTeam().size*4} placed")
            redCheesePlaced = cheeseHuntBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Red Team  - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getRedCheesePlaced()}/${Main.getGame().getTeamManager().getRedTeam().size*4} placed")
            redCheesePlaced.score = 2
            cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.BLUE.toString() + "" + ChatColor.BOLD + "Blue Team - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getBlueCheesePlaced()-1}/${Main.getGame().getTeamManager().getBlueTeam().size*4} placed")
            blueCheesePlaced = cheeseHuntBoard.getScore(ChatColor.BLUE.toString() + "" + ChatColor.BOLD + "Blue Team - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getBlueCheesePlaced()}/${Main.getGame().getTeamManager().getBlueTeam().size*4} placed")
            blueCheesePlaced.score = 1
        }
    }

    fun addCollectedStatsToScoreboard() {
        blankSpaceOne.score = 3
        if(Main.getGame().getModifier() == ModifierOptions.BOTTOMLESS_CHEESE) {
            cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.RED.toString() + ChatColor.BOLD + "Red Team  - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getRedCheesePlaced()}/∞ placed")
            cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.BLUE.toString() + ChatColor.BOLD + "Blue Team - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getBlueCheesePlaced()}/∞ placed")
        } else {
            cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.RED.toString() + ChatColor.BOLD + "Red Team  - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getRedCheesePlaced()}/${Main.getGame().getTeamManager().getRedTeam().size*4} placed")
            cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.BLUE.toString() + ChatColor.BOLD + "Blue Team - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getBlueCheesePlaced()}/${Main.getGame().getTeamManager().getBlueTeam().size*4} placed")
        }
        redCheeseCollected.score = 2
        blueCheeseCollected.score = 1
        blankSpaceTwo.score = 0
    }

    fun updateCollectedStats() {
        cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Red Team  - " + ChatColor.RESET + "0/0 collected")
        cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.BLUE.toString() + "" + ChatColor.BOLD + "Blue Team - " + ChatColor.RESET + "0/0 collected")
        if(Main.getGame().getModifier() == ModifierOptions.BOTTOMLESS_CHEESE) {
            cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Red Team  - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getRedCheeseCollected()-1}/${Main.getGame().getCheeseManager().getBlueCheesePlaced()} collected")
            redCheeseCollected = cheeseHuntBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Red Team  - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getRedCheeseCollected()}/${Main.getGame().getCheeseManager().getBlueCheesePlaced()} collected")
            cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.BLUE.toString() + "" + ChatColor.BOLD + "Blue Team - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getBlueCheeseCollected()-1}/${Main.getGame().getCheeseManager().getRedCheesePlaced()} collected")
            blueCheeseCollected = cheeseHuntBoard.getScore(ChatColor.BLUE.toString() + "" + ChatColor.BOLD + "Blue Team - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getBlueCheeseCollected()}/${Main.getGame().getCheeseManager().getRedCheesePlaced()} collected")
        } else {
            cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Red Team  - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getRedCheeseCollected()-1}/${Main.getGame().getTeamManager().getRedTeam().size*4} collected")
            redCheeseCollected = cheeseHuntBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Red Team  - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getRedCheeseCollected()}/${Main.getGame().getTeamManager().getRedTeam().size*4} collected")
            cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.BLUE.toString() + "" + ChatColor.BOLD + "Blue Team - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getBlueCheeseCollected()-1}/${Main.getGame().getTeamManager().getBlueTeam().size*4} collected")
            blueCheeseCollected = cheeseHuntBoard.getScore(ChatColor.BLUE.toString() + "" + ChatColor.BOLD + "Blue Team - " + ChatColor.RESET + "${Main.getGame().getCheeseManager().getBlueCheeseCollected()}/${Main.getGame().getTeamManager().getBlueTeam().size*4} collected")
        }
        redCheeseCollected.score = 2
        blueCheeseCollected.score = 1
    }

    fun updateModifier() {
        when(Main.getGame().getModifier()) {
            ModifierOptions.NONE -> {
                cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.GOLD.toString() + "" + ChatColor.BOLD + "Modifier: " + ChatColor.WHITE + "Bottomless Cheese")
                cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.GOLD.toString() + "" + ChatColor.BOLD + "Modifier: " + ChatColor.WHITE + "Impostor")
                currentModifierText = cheeseHuntBoard.getScore(ChatColor.GOLD.toString() + "" + ChatColor.BOLD + "Modifier: " + ChatColor.DARK_GRAY + "Inactive")
                currentModifierText.score = 5
            }
            ModifierOptions.BOTTOMLESS_CHEESE -> {
                cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.GOLD.toString() + "" + ChatColor.BOLD + "Modifier: " + ChatColor.DARK_GRAY + "Inactive")
                cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.GOLD.toString() + "" + ChatColor.BOLD + "Modifier: " + ChatColor.WHITE + "Impostor")
                currentModifierText = cheeseHuntBoard.getScore(ChatColor.GOLD.toString() + "" + ChatColor.BOLD + "Modifier: " + ChatColor.WHITE + "Bottomless Cheese")
                currentModifierText.score = 5
            }
            ModifierOptions.IMPOSTOR -> {
                cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.GOLD.toString() + "" + ChatColor.BOLD + "Modifier: " + ChatColor.DARK_GRAY + "Inactive")
                cheeseHuntBoard.scoreboard!!.resetScores(ChatColor.GOLD.toString() + "" + ChatColor.BOLD + "Modifier: " + ChatColor.WHITE + "Bottomless Cheese")
                currentModifierText = cheeseHuntBoard.getScore(ChatColor.GOLD.toString() + "" + ChatColor.BOLD + "Modifier: " + ChatColor.WHITE + "Impostor")
                currentModifierText.score = 5
            }
        }
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
                currentRoundText.score = 6
            } else {
                cheeseHuntBoard.scoreboard?.resetScores(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Round begins: " + ChatColor.RESET + previousDisplayTime)
                gameStatusText = cheeseHuntBoard.getScore(ChatColor.RED.toString() + "" + ChatColor.BOLD + "Round begins: " + ChatColor.RESET + "" + displayTime)
                gameStatusText.score = 4

                if (game.getRoundState() === RoundState.ROUND_TWO) {
                    cheeseHuntBoard.scoreboard?.resetScores(ChatColor.GREEN.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "1/2")
                    currentRoundText = cheeseHuntBoard.getScore(ChatColor.GREEN.toString() + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "2/2")
                    currentRoundText.score = 6
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