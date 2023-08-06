package dev.byrt.cheesehunt.game

import dev.byrt.cheesehunt.Main
import dev.byrt.cheesehunt.state.Sounds
import dev.byrt.cheesehunt.state.TimerState
import dev.byrt.cheesehunt.task.Music
import dev.byrt.cheesehunt.util.DevStatus

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title

import org.bukkit.Bukkit
import org.bukkit.SoundCategory

import java.time.Duration

class GameManager(private val game : Game) {
    private var gameState = GameState.IDLE
    private var overtimeActive = true

    fun nextState() {
        when(this.gameState) {
            GameState.IDLE -> { setGameState(GameState.STARTING) }
            GameState.STARTING -> { setGameState(GameState.IN_GAME) }
            GameState.IN_GAME -> {
                if(overtimeActive) {
                    setGameState(GameState.OVERTIME)
                } else if(game.roundManager.getRoundState().ordinal + 1 >= game.roundManager.getTotalRounds()) {
                    setGameState(GameState.GAME_END)
                } else {
                    setGameState(GameState.ROUND_END)
                }
            }
            GameState.ROUND_END -> { setGameState(GameState.STARTING) }
            GameState.GAME_END -> { setGameState(GameState.IDLE) }
            GameState.OVERTIME -> {
                if(game.roundManager.getRoundState().ordinal + 1 >= game.roundManager.getTotalRounds()) {
                    setGameState(GameState.GAME_END)
                } else {
                    setGameState(GameState.ROUND_END)
                }
            }
        }
    }

    fun setGameState(newState : GameState) {
        if(newState == gameState) return
        game.dev.parseDevMessage("Game state updated from $gameState to $newState.", DevStatus.INFO)
        this.gameState = newState
        when(this.gameState) {
            GameState.IDLE -> {
                game.reload()
                game.gameTask.cancelGameTask()
            }
            GameState.STARTING -> {
                game.setBuildMode(false)
                game.timerManager.setTimerState(TimerState.ACTIVE)
                game.gameTask.setTimeLeft(80, null)
                game.gameTask.gameLoop()
                starting()
            }
            GameState.IN_GAME -> {
                game.timerManager.setTimerState(TimerState.ACTIVE)
                game.gameTask.setTimeLeft(720, null)
                startRound()
            }
            GameState.ROUND_END -> {
                game.timerManager.setTimerState(TimerState.ACTIVE)
                game.gameTask.setTimeLeft(10, null)
                roundEnd()
            }
            GameState.GAME_END -> {
                game.timerManager.setTimerState(TimerState.ACTIVE)
                game.gameTask.setTimeLeft(30, null)
                gameEnd()
            }
            GameState.OVERTIME -> {
                game.timerManager.setTimerState(TimerState.ACTIVE)
                game.gameTask.setTimeLeft(60, null)
                startOvertime()
            }
        }
    }

    private fun startRound() {
        for(player in Bukkit.getOnlinePlayers()) {
            player.playSound(player.location, Sounds.Round.ROUND_END_PLING, 1f, 1f)
            player.playSound(player.location, Sounds.Round.ROUND_END_PLING, 1f, 2f)
            game.musicTask.startMusicLoop(player, game.plugin, Music.MAIN)
            player.resetTitle()
        }
        game.playerManager.giveItemsToPlayers()
        game.playerManager.setPlayersAdventure()
        game.blockManager.removeBarriers()
    }

    private fun starting() {
        for(player in Bukkit.getOnlinePlayers()) {
            player.showTitle(Title.title(Component.text("\uD000"), Component.text(""), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(3), Duration.ofSeconds(1))))
        }
        game.playerManager.setSpectatorsGameMode()
        game.playerManager.setPlayersNotFlying()
        game.playerManager.clearCheese()
        game.playerManager.teleportPlayersToGame()
        game.playerManager.teleportSpectatorsToArena()
        game.playerManager.setPlayersAdventure()
        game.blockManager.resetAllBlocks()
        game.teamManager.hideDisplayTeamNames()
        game.itemManager.clearFloorItems()
        game.scoreManager.setNewRandomMultiplierMinute()
    }

    private fun startOvertime() {
        game.playerManager.clearWeapons()
        game.blockManager.placeCheeseCube()
        game.itemManager.clearFloorItems()
        for(player in Bukkit.getOnlinePlayers()) {
            player.playSound(player.location, Sounds.Alert.OVERTIME_ALERT, 0.35f, 1.25f)
            player.showTitle(Title.title(
                Component.text("OVERTIME!", NamedTextColor.RED, TextDecoration.BOLD),
                Component.text("Hunt all the Cheese!"),
                Title.Times.times(
                    Duration.ofSeconds(0),
                    Duration.ofSeconds(3),
                    Duration.ofSeconds(1)
                    )
                )
            )
            player.sendMessage(
                Component.text("\n[")
                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                    .append(Component.text("] "))
                    .append(Component.text("OVERTIME: ", NamedTextColor.RED, TextDecoration.BOLD))
                    .append(Component.text("You can now ", NamedTextColor.WHITE))
                    .append(Component.text("ONLY", NamedTextColor.WHITE, TextDecoration.BOLD))
                    .append(Component.text(" gather Cheese, 1 minute remains!\n")
                )
            )
        }
    }

    private fun gameEnd() {
        for (player in Bukkit.getOnlinePlayers()) {
            player.playSound(player.location, Sounds.GameOver.GAME_OVER_PLING, 1f, 1f)
            player.playSound(player.location, Sounds.GameOver.GAME_OVER_PLING, 1f, 2f)
            player.playSound(player.location, Sounds.GameOver.GAME_OVER_EFFECT_1, 1f, 1f)
            player.playSound(player.location, Sounds.GameOver.GAME_OVER_EFFECT_2, 1f, 1f)
            Main.getGame().musicTask.stopMusicLoop(player, Music.MAIN)
            Main.getGame().musicTask.stopMusicLoop(player, Music.OVERTIME)
            player.playSound(player.location, Sounds.Music.GAME_OVER_MUSIC, SoundCategory.VOICE, 0.85f, 1f)
            player.showTitle(Title.title(
                Component.text("Game Over!", NamedTextColor.RED, TextDecoration.BOLD),
                Component.text(""),
                Title.Times.times(
                    Duration.ofSeconds(0),
                    Duration.ofSeconds(4),
                    Duration.ofSeconds(1)
                    )
                )
            )
            if(game.cheeseManager.playerHasCheese(player)) {
                game.cheeseManager.setPlayerHasCheese(player, false)
            }
            game.playerManager.setPlayersAdventure()
            game.playerManager.setPlayersFlying()
            game.playerManager.clearNonCheeseItems()
            game.playerManager.clearAllPlayersEffects()
            game.itemManager.clearFloorItems()
        }
    }

    private fun roundEnd() {
        for(player in Bukkit.getOnlinePlayers()) {
            player.playSound(player.location, Sounds.GameOver.GAME_OVER_PLING, 1f, 1f)
            player.playSound(player.location, Sounds.GameOver.GAME_OVER_PLING, 1f, 2f)
            player.playSound(player.location, Sounds.GameOver.GAME_OVER_EFFECT_1, 1f, 1f)
            player.playSound(player.location, Sounds.GameOver.GAME_OVER_EFFECT_2, 1f, 1f)
            game.musicTask.stopMusicLoop(player, Music.MAIN)
            game.musicTask.stopMusicLoop(player, Music.OVERTIME)
            player.playSound(player.location, Sounds.Music.GAME_OVER_MUSIC, SoundCategory.VOICE, 0.85f, 1f)
            player.showTitle(
                Title.title(
                    Component.text("Round Over!", NamedTextColor.RED, TextDecoration.BOLD),
                    Component.text(""),
                    Title.Times.times(
                        Duration.ofSeconds(0),
                        Duration.ofSeconds(4),
                        Duration.ofSeconds(1)
                    )
                )
            )
        }
        game.playerManager.setPlayersAdventure()
        game.playerManager.setPlayersFlying()
        game.playerManager.clearNonCheeseItems()
        game.itemManager.clearFloorItems()
    }

    fun getGameState() : GameState {
        return this.gameState
    }

    fun setOvertimeState(isActive : Boolean) {
        this.overtimeActive = isActive
    }

    fun isOvertimeActive() : Boolean {
        return this.overtimeActive
    }

    // Dangerous method call, can cause unresolvable issues.
    fun forceState(forcedState : GameState) {
        setGameState(forcedState)
    }
}
enum class GameState {
    IDLE,
    STARTING,
    IN_GAME,
    ROUND_END,
    GAME_END,
    OVERTIME
}