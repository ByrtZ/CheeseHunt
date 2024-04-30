package dev.byrt.cheesehunt.standalone.visuals

import dev.byrt.cheesehunt.game.Game
import dev.byrt.cheesehunt.game.GameManager
import dev.byrt.cheesehunt.game.GameState
import dev.byrt.cheesehunt.game.GameTask
import dev.byrt.cheesehunt.manager.MapManager
import dev.byrt.cheesehunt.manager.ScoreManager
import dev.byrt.cheesehunt.state.RoundState
import dev.byrt.cheesehunt.state.Rounds
import dev.byrt.cheesehunt.state.Teams
import me.lucyydotp.cheeselib.inject.injectInScope
import me.lucyydotp.cheeselib.module.ModuleHolder
import me.lucyydotp.cheeselib.module.ParentModule
import me.lucyydotp.cheeselib.sys.scoreboard.Board
import net.kyori.adventure.extra.kotlin.plus
import net.kyori.adventure.extra.kotlin.text
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import java.text.DecimalFormat

class InfoBoardManager(parent: ModuleHolder) : ParentModule(parent) {

    private val board = Board(
        this,
        Component.text("Byrt's Server", NamedTextColor.YELLOW, TextDecoration.BOLD)
    ).registerAsChild()

    private val gameManager: GameManager by injectInScope(Game::class)
    private val gameTask: GameTask by injectInScope(Game::class)
    private val mapManager: MapManager by injectInScope(Game::class)
    private val roundManager: Rounds by injectInScope(Game::class)
    private val scoreManager: ScoreManager by injectInScope(Game::class)

    private val multiplierNumberFormat = DecimalFormat("##0.0")

    private var gameMapInfo by board.section(defaultLines = gameMapInfo())
    private var timer by board.section(defaultLines = timer())
    private val _newline1 = board.section(defaultLines = listOf(Board.Line(Component.text(" ".repeat(35)))))

    private var multiplier by board.section(defaultLines = multiplier())
    private var teamScores by board.section(defaultLines = teamScores())
    private val _newline2 = board.section(defaultLines = listOf(Board.Line(Component.empty())))

    init {
        listen(gameManager.onStateChange) {
            timer = timer()
        }

        listen(gameTask.onTimerChange) {
            timer = timer()
        }

        listen(scoreManager.onScoreChange) {
            teamScores = teamScores()
        }

        listen(scoreManager.onMultiplierChange) {
            multiplier = multiplier()
        }

        listen(mapManager.onChange) {
            gameMapInfo = gameMapInfo()
        }
    }

    private fun withColoredPrefix(prefix: String, message: String, color: TextColor) = text {
        append(Component.text("$prefix: ", color, TextDecoration.BOLD))
        append(Component.text(message))
    }

    private fun gameMapInfo() = listOf(
        Board.Line(withColoredPrefix("Game", "Cheese Hunt", NamedTextColor.AQUA)),
        Board.Line(withColoredPrefix("Map", mapManager.getCurrentMap().mapName, NamedTextColor.AQUA))
    )

    private val formattedTimeLeft: String
        get() {
            val timeLeft = gameTask.getTimeLeft()
            return String.format("%02d:%02d", timeLeft / 60, timeLeft % 60)
        }

    private fun timer() = when (gameManager.getGameState()) {
        GameState.IDLE -> withColoredPrefix("Game status", "Waiting...", NamedTextColor.RED)
        GameState.STARTING -> withColoredPrefix(
            if (roundManager.getRoundState() == RoundState.ONE) "Game begins" else "Round begins",
            formattedTimeLeft,
            NamedTextColor.RED
        )

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
        append(Component.text("x" + multiplierNumberFormat.format(scoreManager.getMultiplier()), NamedTextColor.YELLOW))
        append(Component.text(")"))
    }.let {
        listOf(Board.Line(it))
    }

    private fun teamScores() = listOf(
        Teams.RED to scoreManager.getRedScore(),
        Teams.BLUE to scoreManager.getBlueScore(),
    ).sortedByDescending(Pair<Teams, Int>::second)
        .mapIndexed { index, (team, score) ->
            Board.Line(
                Component.text(" ${index + 1}. ") + team.displayName,
                Component.text("${score}c  ")
            )
        }
}
