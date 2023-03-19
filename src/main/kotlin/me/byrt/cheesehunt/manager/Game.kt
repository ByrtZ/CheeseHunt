package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.task.GameCountdownTask

@Suppress("unused")
class Game(private val plugin : Main) {
    private var gameState : GameState = GameState.IDLE
    private var roundState : RoundState = RoundState.ROUND_ONE
    private var timerState : TimerState = TimerState.INACTIVE
    private var buildMode = false
    private val playerManager = PlayerManager(this)
    private val teamManager = TeamManager(this)
    private val itemManager = ItemManager(this)
    private val blockManager = BlockManager(this)
    private val cheeseManager = CheeseManager(this)
    private val infoBoardManager = InfoBoardManager(this)
    private val tabListManager = TabListManager(this)
    private val gameCountdownTask = GameCountdownTask(this)
    private val musicLoop = MusicLoop(this)
    private var musicLooper = false

    fun setGameState(newState : GameState) {
        this.gameState = newState
        when(this.gameState) {
            GameState.IDLE -> {
                setTimerState(TimerState.INACTIVE)
                setRoundState(RoundState.ROUND_ONE)
            }
            GameState.STARTING -> {
                setTimerState(TimerState.ACTIVE)
                when(this.roundState) {
                    RoundState.ROUND_ONE -> {
                        buildMode = false
                        gameCountdownTask.setTimeLeft(80)
                        gameCountdownTask.gameLoop()
                    }
                    RoundState.ROUND_TWO -> {
                        gameCountdownTask.setTimeLeft(15)
                    }
                }
                roundStarting()
            }
            GameState.IN_GAME -> {
                setTimerState(TimerState.ACTIVE)
                gameCountdownTask.setTimeLeft(120)
                when(this.roundState) {
                    RoundState.ROUND_ONE -> {
                        infoBoardManager.addPlacedStatsToScoreboard()
                        infoBoardManager.updatePlacedStats()
                    }
                    RoundState.ROUND_TWO -> {
                        infoBoardManager.addCollectedStatsToScoreboard()
                        infoBoardManager.updateCollectedStats()
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

    fun getTabListManager(): TabListManager {
        return this.tabListManager
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

    fun setBuildMode(mode : Boolean) {
        this.buildMode = mode
    }

    fun getBuildMode() : Boolean {
        return this.buildMode
    }

    fun getMusicLoop(): MusicLoop {
        return this.musicLoop
    }

    fun setMusicLooper(mode : Boolean) {
        this.musicLooper = mode
    }

    fun getMusicLooper() : Boolean {
        return this.musicLooper
    }

    fun cleanUp() {
        teamManager.destroyDisplayTeams()
        infoBoardManager.destroyScoreboard()
    }
}