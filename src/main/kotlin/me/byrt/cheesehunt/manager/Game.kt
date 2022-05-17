package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.task.GameCountdownTask

class Game(private var plugin : Main) {
    private var gameState : GameState = GameState.IDLE
    private var roundState : RoundState = RoundState.ROUND_ONE
    private var timerState : TimerState = TimerState.INACTIVE
    private var playerManager = PlayerManager(this)
    private var teamManager = TeamManager(this)
    private var itemManager = ItemManager(this)
    private var infoBoardManager = InfoBoardManager(this)
    private var gameCountdownTask = GameCountdownTask(this)

    fun setGameState(newState : GameState) {
        this.gameState = newState
        when(this.gameState) {
            GameState.IDLE -> {
                setTimerState(TimerState.INACTIVE)
                setRoundState(RoundState.ROUND_ONE)
            }
            GameState.STARTING -> {
                setTimerState(TimerState.ACTIVE)
                gameCountdownTask.setTimeLeft(20)
                if(roundState == RoundState.ROUND_ONE) {
                    gameCountdownTask.runTaskTimer(plugin, 0, 20)
                }
                roundStarting()
            }
            GameState.IN_GAME -> {
                setTimerState(TimerState.ACTIVE)
                gameCountdownTask.setTimeLeft(240)
                startRound()
            }
            GameState.ROUND_END -> {
                setTimerState(TimerState.ACTIVE)
                gameCountdownTask.setTimeLeft(20)
            }
            GameState.GAME_END -> {
                setTimerState(TimerState.ACTIVE)
                gameCountdownTask.setTimeLeft(35)
            }
        }
    }

    fun getGameState(): GameState {
        return this.gameState
    }

    fun getRoundState(): RoundState {
        return this.roundState
    }

    fun setRoundState(roundState : RoundState) {
        this.roundState = roundState
    }

    fun getTimerState(): TimerState {
        return this.timerState
    }

    fun setTimerState(timerState : TimerState) {
        this.timerState = timerState
    }

    fun getPlayerManager() : PlayerManager {
        return this.playerManager
    }

    fun getTeamManager() : TeamManager {
        return this.teamManager
    }

    fun getItemManager(): ItemManager {
        return this.itemManager
    }

    fun getInfoBoardManager(): InfoBoardManager {
        return this.infoBoardManager
    }

    fun getGameCountdownTask(): GameCountdownTask {
        return this.gameCountdownTask
    }

    private fun roundStarting() {
        playerManager.setPlayersNotFlying()
        playerManager.clearCheese()
        playerManager.teleportAllPlayers()
    }

    private fun startRound() {
        playerManager.giveItemsToPlayers()
    }

    fun cleanUp() {
        playerManager.clearAllItems()
        infoBoardManager.destroyScoreboard()
    }
}