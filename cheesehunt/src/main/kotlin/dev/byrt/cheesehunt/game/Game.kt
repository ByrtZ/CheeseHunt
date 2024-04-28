package dev.byrt.cheesehunt.game

import dev.byrt.cheesehunt.CheeseHunt
import dev.byrt.cheesehunt.command.BuildMode
import dev.byrt.cheesehunt.interfaces.*
import dev.byrt.cheesehunt.manager.*
import dev.byrt.cheesehunt.queue.*
import dev.byrt.cheesehunt.state.*
import dev.byrt.cheesehunt.task.*
import dev.byrt.cheesehunt.util.*
import me.lucyydotp.cheeselib.inject.GlobalInjectionContext
import me.lucyydotp.cheeselib.inject.bind
import me.lucyydotp.cheeselib.module.ParentModule

import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title

import org.bukkit.Bukkit

import java.time.Duration
import me.lucyydotp.cheeselib.game.TeamManager as CommonTeamManager

class Game(val plugin : CheeseHunt) : ParentModule(plugin) {
    val gameManager = GameManager(this).also(::bind)
    val roundManager = Rounds(this)
    val timerManager = Timer(this)
    val playerManager = PlayerManager(this)
    val teams = CommonTeamManager(this, Teams::class).registerAsChild().also(GlobalInjectionContext::bind)
    val itemManager = ItemManager(this).also(::bind)
    val blockManager = BlockManager(this)
    val cheeseManager = CheeseManager(this).registerAsChild()
    val tabListManager = TabListManager(this).registerAsChild()
    val locationManager by lazy { LocationManager(this) }
    val scoreManager = ScoreManager(this).also(::bind)
    val statsManager = StatisticsManager(this).also(::bind)
    val configManager = ConfigManager(this)
    val mapManager = MapManager(this)
    val cooldownManager = CooldownManager(this)
    val queue = Queue(this)
    val queueVisuals = QueueVisuals(this)
    val queueTask = QueueTask(this)
    val infoBoardManager = InfoBoardManager(this).registerAsChild().also(::bind)

    val interfaceManager = InterfaceManager(this)

    val respawnTask = RespawnTask(this)
    val gameTask = GameTask(this)
    val musicTask = MusicTask(this)
    val winShowTask = WinShowTask(this)

    val buildMode = BuildMode(this).registerAsChild()

    // FIXME(lucy): move this up to parent module
    val dev = parent.bind(Dev(this))

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
        locationManager.populateSpawns()
        locationManager.populateWinShowArea()
        queueVisuals.spawnQueueNPC()
    }

    fun cleanUp() {
        blockManager.resetAllBlocks()
        queueVisuals.removeQueueNPC()
        infoBoardManager.destroyScoreboard()
        configManager.saveMapConfig()
    }

    fun reload() {
        gameManager.setGameState(GameState.IDLE)
        roundManager.setRoundState(RoundState.ONE)
        timerManager.setTimerState(TimerState.INACTIVE)
        gameTask.resetVars()
        blockManager.resetAllBlocks()
        playerManager.resetPlayers()
        locationManager.resetSpawnCounters()
        scoreManager.resetScores()
        statsManager.resetStatistics()
        infoBoardManager.destroyScoreboard()
        queue.setQueueState(QueueState.IDLE)
        queueVisuals.removeQueueNPC()
        queueVisuals.spawnQueueNPC()
        queueVisuals.setAllQueueInvisible()

        for(player in Bukkit.getOnlinePlayers()) {
            player.showTitle(Title.title(Component.text("\uD000"), Component.text(""), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(3), Duration.ofSeconds(1))))
        }
    }

    @Deprecated("Use new BuildMode module")
    fun setBuildMode(mode : Boolean) {
        this.buildMode.buildModeEnabled = mode
    }

    @Deprecated("Use new BuildMode module")
    fun getBuildMode() : Boolean {
        return this.buildMode.buildModeEnabled
    }
}
