package dev.byrt.cheesehunt.manager

import dev.byrt.cheesehunt.game.Game
import dev.byrt.cheesehunt.game.GameState
import dev.byrt.cheesehunt.state.RoundState
import dev.byrt.cheesehunt.state.Teams
import me.lucyydotp.cheeselib.sys.scoreboard.Board
import me.lucyydotp.cheeselib.inject.context
import me.lucyydotp.cheeselib.module.ParentModule
import net.kyori.adventure.extra.kotlin.plus
import net.kyori.adventure.extra.kotlin.text
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import java.text.DecimalFormat

class InfoBoardManager(private val game: Game) : ParentModule(game) {

    private val board = Board(
        this,
        Component.text("Byrt's Server", NamedTextColor.YELLOW, TextDecoration.BOLD)
    ).registerAsChild()

    private val mapManager: MapManager by context()
    private val multiplierNumberFormat = DecimalFormat("##0.0")

    private var gameMapInfo by board.section(defaultLines = gameMapInfo())
    private var timer by board.section(defaultLines = timer())
    private val _newline1 = board.section(defaultLines = listOf(Board.Line(Component.text(" ".repeat(35)))))

    private var multiplier by board.section(defaultLines = multiplier())
    private var teamScores by board.section(defaultLines = teamScores())
    private val _newline2 = board.section(defaultLines = listOf(Board.Line(Component.empty())))

    fun destroyScoreboard() {
        // TODO(lucy): no-op
//        cheeseHuntBoard.displaySlot = null
//        cheeseHuntBoard.unregister()
    }

    private fun withColoredPrefix(prefix: String, message: String, color: TextColor) = text {
        append(Component.text("$prefix: ", color, TextDecoration.BOLD))
        append(Component.text(message))
    }

    private fun gameMapInfo() = listOf(
        Board.Line(withColoredPrefix("Game", "Cheese Hunt", NamedTextColor.AQUA)),
        // FIXME(lucy): make mapManager injectable
        Board.Line(withColoredPrefix("Map", /*mapManager.getCurrentMap().mapName*/"Reforged", NamedTextColor.AQUA))
    )

    private val formattedTimeLeft: String get() {
        val timeLeft = game.gameTask.getTimeLeft()
        return String.format("%02d:%02d", timeLeft / 60, timeLeft % 60)
    }

    private fun timer() = when (game.gameManager.getGameState()) {
        GameState.IDLE -> withColoredPrefix("Game status", "Waiting...", NamedTextColor.RED)
        GameState.STARTING -> withColoredPrefix(if (game.roundManager.getRoundState() == RoundState.ONE) "Game begins" else "Round begins", formattedTimeLeft, NamedTextColor.RED)
        GameState.IN_GAME -> withColoredPrefix("Time left", formattedTimeLeft, NamedTextColor.RED)
        GameState.OVERTIME -> withColoredPrefix("OVERTIME", formattedTimeLeft, NamedTextColor.RED)
        GameState.ROUND_END -> withColoredPrefix("Next round", formattedTimeLeft, NamedTextColor.RED)
        GameState.GAME_END -> withColoredPrefix("Game ending", formattedTimeLeft, NamedTextColor.RED)
    }.let {
        listOf(Board.Line(it))
    }


    private fun multiplier() = text {
        append(Component.text("Game Coins: ", NamedTextColor.AQUA, TextDecoration.BOLD))
        append(Component.text("("))
        append(Component.text("x" + multiplierNumberFormat.format(game.scoreManager.getMultiplier()), NamedTextColor.YELLOW))
        append(Component.text(")"))
    }.let {
        listOf(Board.Line(it))
    }

    private fun teamScores() = listOf(
        Teams.RED to game.scoreManager.getRedScore(),
        Teams.BLUE to game.scoreManager.getBlueScore(),
    ).sortedByDescending(Pair<Teams, Int>::second)
        .mapIndexed { index, (team, score) ->
            Board.Line(
                Component.text(" ${index + 1}. ") + team.displayName,
                Component.text("${score}c  ")
            )
        }

    fun updateScoreboardScores() {
        teamScores = teamScores()
    }

    fun updateScoreboardTimer() {
        timer = timer()
    }

    fun updateScoreboardMultiplier() {
        multiplier = multiplier()
    }

    fun updateCurrentMap() {
        gameMapInfo = gameMapInfo()
    }
}
