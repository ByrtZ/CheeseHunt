package dev.byrt.cheesehunt.queue

import dev.byrt.cheesehunt.game.Game
import dev.byrt.cheesehunt.game.GameState

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.entity.Player

class QueueVisuals(private val game : Game) {
    private var queueBackground = BossBar.bossBar(Component.text("\uD006"), 0.0f, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS)
    private var queueTitle = BossBar.bossBar(Component.text("CHEESE HUNT", NamedTextColor.GOLD, TextDecoration.BOLD), 0.0f, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS)
    private var queueStatus = BossBar.bossBar(Component.text("Inactive Queue").color(TextColor.fromHexString("#ffff00")), 0.0f, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS)

    fun setQueueVisible() {
        val audience = game.queue.queuedAudience()
        queueBackground.addViewer(audience)
        queueTitle.addViewer(audience)
        queueStatus.addViewer(audience)
    }

    fun setQueueInvisible(player : Player) {
        val audience = Audience.audience(player)
        queueBackground.removeViewer(audience)
        queueTitle.removeViewer(audience)
        queueStatus.removeViewer(audience)
    }

    fun setAllQueueInvisible() {
        val audience = game.queue.queuedAudience()
        queueBackground.removeViewer(audience)
        queueTitle.removeViewer(audience)
        queueStatus.removeViewer(audience)
    }

    fun updateQueueStatus() {
        when(game.queue.getQueueState()) {
            QueueState.IDLE -> {
                queueStatus.name(Component.text("Inactive Queue").color(TextColor.fromHexString("#ffff00")))
                game.queue.deleteQueue()
            }
            QueueState.AWAITING_PLAYERS -> {
                queueStatus.name(
                    Component.text("In Queue ").color(TextColor.fromHexString("#ffff00"))
                        .append(Component.text("(${game.queue.getQueue().size}/${Queue.MIN_PLAYERS})", NamedTextColor.WHITE)))
            }
            QueueState.NO_GAME_AVAILABLE -> {
                game.startGame()
                if(game.gameManager.getGameState() != GameState.IDLE) {
                    queueStatus.name(Component.text("Awaiting available game...").color(TextColor.fromHexString("#ffff00")))
                }
            }
            QueueState.SENDING_PLAYERS_TO_GAME -> {
                if(!game.queueTask.getQueueActive()) {
                    game.queueTask.startQueueTask(game.plugin)
                }
            }
        }
    }

    fun getQueueStatus() : BossBar {
        return queueStatus
    }
}