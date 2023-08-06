package dev.byrt.cheesehunt.game

import dev.byrt.cheesehunt.Main
import dev.byrt.cheesehunt.manager.*
import dev.byrt.cheesehunt.queue.*
import dev.byrt.cheesehunt.state.*
import dev.byrt.cheesehunt.task.*
import dev.byrt.cheesehunt.util.*

import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title

import org.bukkit.Bukkit

import java.time.Duration

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
    val scoreManager = ScoreManager(this)
    val statsManager = StatisticsManager(this)
    val configManager = ConfigManager(this)
    val whitelistManager = WhitelistManager(this)
    val mapManager = MapManager(this)
    val cooldownManager = CooldownManager(this)
    val queue = Queue(this)
    val queueVisuals = QueueVisuals(this)
    val queueTask = QueueTask(this)

    val respawnTask = RespawnTask(this)
    val gameTask = GameTask(this)
    val musicTask = MusicTask(this)
    val winShowTask = WinShowTask(this)

    val dev = Dev(this)

    private var buildMode = false

    fun startGame() {
        if(gameManager.getGameState() == GameState.IDLE) {
            gameManager.nextState()
        } else {
            dev.parseDevMessage("Unable to start, as game is already running.", DevStatus.SEVERE)
        }
    }

    fun stopGame() {
        if(gameManager.getGameState() == GameState.IDLE) {
            dev.parseDevMessage("Unable to stop, as no game is running.", DevStatus.SEVERE)
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
        queueVisuals.spawnQueueNPC()
    }

    fun cleanUp() {
        queueVisuals.removeQueueNPC()
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
        queue.deleteQueue()
        queueVisuals.removeQueueNPC()
        queueVisuals.spawnQueueNPC()
        queueVisuals.setAllQueueInvisible()
        queueVisuals.updateQueueStatus()

        for(player in Bukkit.getOnlinePlayers()) {
            player.showTitle(Title.title(Component.text("\uD000"), Component.text(""), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(3), Duration.ofSeconds(1))))
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