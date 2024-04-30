package dev.byrt.cheesehunt.game

import dev.byrt.cheesehunt.CheeseHunt
import dev.byrt.cheesehunt.command.BuildMode
import dev.byrt.cheesehunt.event.PlayerDeathEvent
import dev.byrt.cheesehunt.event.PlayerMovementEvent
import dev.byrt.cheesehunt.event.PlayerPotionEffectEvent
import dev.byrt.cheesehunt.interfaces.*
import dev.byrt.cheesehunt.manager.*
import dev.byrt.cheesehunt.queue.*
import dev.byrt.cheesehunt.state.*
import dev.byrt.cheesehunt.task.*
import me.lucyydotp.cheeselib.inject.bind
import me.lucyydotp.cheeselib.inject.bindGlobally
import me.lucyydotp.cheeselib.inject.context
import me.lucyydotp.cheeselib.module.ParentModule
import me.lucyydotp.cheeselib.sys.AdminMessageStyles
import me.lucyydotp.cheeselib.sys.AdminMessages

import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title

import org.bukkit.Bukkit

import java.time.Duration
import me.lucyydotp.cheeselib.sys.TeamManager as CommonTeamManager

class Game(val plugin : CheeseHunt) : ParentModule(plugin) {
    val gameManager = GameManager(this, this).registerAsChild().also(::bind)
    val roundManager = Rounds(this).also(::bind)
    val timerManager = Timer(this).registerAsChild().also(::bind)
    val playerManager = PlayerManager(this).also(::bind)
    val teams = CommonTeamManager(this, Teams::class).registerAsChild().bindGlobally()
    val itemManager = ItemManager(this).also(::bind)
    val blockManager = BlockManager(this)
    val cheeseManager = CheeseManager(this).registerAsChild().also(::bind)
    val tabListManager = TabListManager(this).registerAsChild()
    val locationManager by lazy { LocationManager(this) }
    val scoreManager = ScoreManager(this, this).also(::bind)
    val statsManager = StatisticsManager(this).also(::bind)
    val configManager = ConfigManager(this)
    val mapManager = MapManager(this).also(::bind)
    val cooldownManager = CooldownManager(this)
    val queue = Queue(this, this)
    val queueVisuals = QueueVisuals(this)
    val queueTask = QueueTask(this)

    val interfaceManager = InterfaceManager(this)

    val respawnTask = RespawnTask(this).also(::bind)
    val gameTask = GameTask(this, this).also(::bind)
    val musicTask = MusicTask(this)
    val winShowTask = WinShowTask(this)

    val buildMode = BuildMode(this).registerAsChild()

    private val adminMessages: AdminMessages by context()

    init {
        PlayerDeathEvent(this).registerAsChild()
        PlayerMovementEvent(this).registerAsChild()
        PlayerPotionEffectEvent(this).registerAsChild()
    }

    fun startGame() {
        if(gameManager.getGameState() == GameState.IDLE) {
            gameManager.nextState()
        } else {
            adminMessages.sendDevMessage("Unable to start, as game is already running.", AdminMessageStyles.SEVERE)
        }
    }

    fun stopGame() {
        if(gameManager.getGameState() == GameState.IDLE) {
            adminMessages.sendDevMessage("Unable to stop, as no game is running.", AdminMessageStyles.SEVERE)
        } else {
            gameManager.changeGameState(GameState.GAME_END)
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
        configManager.saveMapConfig()
    }

    fun reload() {
        gameManager.changeGameState(GameState.IDLE)
        roundManager.setRoundState(RoundState.ONE)
        timerManager.setTimerState(TimerState.INACTIVE)
        gameTask.resetVars()
        blockManager.resetAllBlocks()
        playerManager.resetPlayers()
        locationManager.resetSpawnCounters()
        scoreManager.resetScores()
        statsManager.resetStatistics()
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
