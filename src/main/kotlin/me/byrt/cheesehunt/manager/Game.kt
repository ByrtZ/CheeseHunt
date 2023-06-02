package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.state.*
import me.byrt.cheesehunt.task.*

import org.bukkit.Bukkit

@Suppress("unused")
class Game(val plugin : Main) {
    val gameManager = GameManager(this)
    val roundManager = Rounds(this)
    val timerManager = Timer(this)
    val playerManager = PlayerManager(this)
    val teamManager = TeamManager(this)
    val itemManager = ItemManager(this)
    val blockManager = BlockManager(this)
    val cheeseManager = CheeseManager(this)
    val infoBoardManager = InfoBoardManager(this)
    val tabListManager = TabListManager(this)
    val soundManager = Sounds(this)
    val locationManager = LocationManager(this)
    val respawnTask = RespawnTask(this)
    val gameTask = GameTask(this)
    val musicTask = MusicTask(this)
    val winShowTask = WinShowTask(this)
    val scoreManager = ScoreManager(this)
    val statsManager = StatisticsManager(this)
    val configManager = ConfigManager(this)
    val whitelistManager = WhitelistManager(this)
    val mapManager = MapManager(this)
    val dev = Dev(this)

    private var buildMode = false

    fun startGame() {
        if(gameManager.getGameState() == GameState.IDLE) {
            gameManager.nextState()
        } else {
            plugin.logger.warning("Unable to start, as game is already running.")
        }
    }

    fun stopGame() {
        if(gameManager.getGameState() == GameState.IDLE) {
            plugin.logger.warning("Unable to stop, as no game is running.")
        } else {
            gameManager.setGameState(GameState.GAME_END)
        }
    }

    fun setup() {
        infoBoardManager.buildScoreboard()
        teamManager.buildDisplayTeams()
        locationManager.populateSpawns()
        locationManager.populateWinShowArea()
        tabListManager.populateCheesePuns()
    }

    fun cleanUp() {
        teamManager.destroyDisplayTeams()
        infoBoardManager.destroyScoreboard()
        configManager.saveWhitelistConfig()
        configManager.saveMapConfig()
    }

    fun reload() {
        gameManager.setGameState(GameState.IDLE)
        roundManager.setRoundState(RoundState.ONE)
        timerManager.setTimerState(TimerState.INACTIVE)
        cheeseManager.resetVars()
        gameTask.resetVars()
        blockManager.resetAllBlocks()
        playerManager.resetPlayers()
        locationManager.resetSpawnCounters()
        scoreManager.resetScores()
        statsManager.resetStats()
        infoBoardManager.destroyScoreboard()
        infoBoardManager.buildScoreboard()

        for(player in Bukkit.getOnlinePlayers()) {
            Main.getGame().teamManager.addToTeam(player, player.uniqueId, Teams.SPECTATOR)
            if(player.isOp) {
                Main.getGame().teamManager.addToAdminDisplay(player.uniqueId)
            }
        }
    }

    fun setBuildMode(mode : Boolean) {
        this.buildMode = mode
    }

    fun getBuildMode() : Boolean {
        return this.buildMode
    }
}