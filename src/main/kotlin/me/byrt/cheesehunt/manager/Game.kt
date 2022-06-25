package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.task.GameCountdownTask

@Suppress("unused")
class Game(private val plugin : Main) {
    private var gameState : GameState = GameState.IDLE
    private var roundState : RoundState = RoundState.ROUND_ONE
    private var timerState : TimerState = TimerState.INACTIVE
    private val playerManager = PlayerManager(this)
    private val teamManager = TeamManager(this)
    private val itemManager = ItemManager(this)
    private val blockManager = BlockManager(this)
    private val cheeseManager = CheeseManager(this)
    private val infoBoardManager = InfoBoardManager(this)
    private val gameCountdownTask = GameCountdownTask(this)

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
                    //TODO: GAME TUTORIAL
                }
                roundStarting()
            }
            GameState.IN_GAME -> {
                setTimerState(TimerState.ACTIVE)
                when(roundState) {
                    RoundState.ROUND_ONE -> {
                        gameCountdownTask.setTimeLeft(120)
                    }
                    RoundState.ROUND_TWO -> {
                        gameCountdownTask.setTimeLeft(180)
                    }
                }
                startRound()
            }
            GameState.ROUND_END -> {
                setTimerState(TimerState.ACTIVE)
                gameCountdownTask.setTimeLeft(20)
            }
            GameState.GAME_END -> {
                setTimerState(TimerState.ACTIVE)
                gameCountdownTask.setTimeLeft(90)
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

    fun getBlockManager(): BlockManager {
        return this.blockManager
    }

    fun getCheeseManager(): CheeseManager {
        return this.cheeseManager
    }

    fun getInfoBoardManager(): InfoBoardManager {
        return this.infoBoardManager
    }

    fun getGameCountdownTask(): GameCountdownTask {
        return this.gameCountdownTask
    }

    private fun roundStarting() {
        playerManager.setSpectatorsGameMode()
        playerManager.setPlayersNotFlying()
        playerManager.clearCheese()
        playerManager.teleportPlayersToGame()
    }

    private fun startRound() {
        playerManager.giveItemsToPlayers()
        blockManager.removeBarriers()
    }

    fun cleanUp() {
        teamManager.destroyDisplayTeams()
        infoBoardManager.destroyScoreboard()
    }
}