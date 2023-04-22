package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.state.*
import me.byrt.cheesehunt.task.*

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
    private val soundManager = Sounds(this)
    private val locationManager = LocationManager(this)
    private val respawnTask = RespawnTask(this)
    private val gameCountdownTask = GameCountdownTask(this)
    private val scoreManager = ScoreManager(this)
    private val statsManager = StatisticsManager(this)
    private val musicTask = MusicTask(this)
    private val configManager = ConfigManager(this)
    private val whitelistManager = WhitelistManager(this)

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
                }
                roundStarting()
            }
            GameState.IN_GAME -> {
                setTimerState(TimerState.ACTIVE)
                gameCountdownTask.setTimeLeft(720)
                startRound()
            }
            GameState.ROUND_END -> {
                setTimerState(TimerState.ACTIVE)
                gameCountdownTask.setTimeLeft(20)
            }
            GameState.GAME_END -> {
                setTimerState(TimerState.ACTIVE)
                teamManager.showDisplayTeamNames()
                gameCountdownTask.setTimeLeft(30)
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

    fun getSoundManager(): Sounds {
        return this.soundManager
    }

    fun getLocationManager(): LocationManager {
        return this.locationManager
    }

    fun getRespawnTask(): RespawnTask {
        return this.respawnTask
    }

    fun getGameCountdownTask(): GameCountdownTask {
        return this.gameCountdownTask
    }

    fun getScoreManager(): ScoreManager {
        return this.scoreManager
    }

    fun getStatsManager() : StatisticsManager {
        return this.statsManager
    }

    fun getMusicTask(): MusicTask {
        return this.musicTask
    }

    fun getConfigManager(): ConfigManager {
        return this.configManager
    }

    fun getWhitelistManager() : WhitelistManager {
        return this.whitelistManager
    }

    private fun roundStarting() {
        playerManager.setSpectatorsGameMode()
        playerManager.setPlayersNotFlying()
        playerManager.clearCheese()
        playerManager.teleportPlayersToGame()
        playerManager.setPlayersAdventure()
        playerManager.teleportSpectatorsToArena()
        blockManager.resetAllBlocks()
        teamManager.hideDisplayTeamNames()
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

    fun cleanUp() {
        teamManager.destroyDisplayTeams()
        infoBoardManager.destroyScoreboard()
        configManager.saveWhitelistConfig()
    }
}