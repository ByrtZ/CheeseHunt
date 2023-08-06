package dev.byrt.cheesehunt.queue

import dev.byrt.cheesehunt.game.Game
import dev.byrt.cheesehunt.state.Sounds

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title

import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

import java.time.Duration

class QueueTask(private val game : Game) {
    private val queueTaskMap = mutableMapOf<Int, BukkitRunnable>()
    private var queueTaskID = 0
    private var queueActive = false

    fun startQueueTask(plugin : Plugin) {
        queueActive = true
        val queueRunnable = object : BukkitRunnable() {
            var queueTimerSeconds = 11
            override fun run() {
                if(game.queue.getQueue().size >= Queue.MIN_PLAYERS) {
                    queueTimerSeconds--
                    if(queueTimerSeconds in 0..10) {
                        game.queue.queuedAudience().playSound(Sound.sound(Key.key(Sounds.Queue.QUEUE_TICK), Sound.Source.MASTER, 1f, 1f))
                    }
                    if(queueTimerSeconds == 10) {
                        game.queue.queuedAudience().playSound(Sound.sound(Key.key(Sounds.Queue.QUEUE_FIND_GAME), Sound.Source.MASTER, 0.5f, 1f))
                    }
                    if(queueTimerSeconds > 3) {
                        game.queueVisuals.getQueueStatus().name(
                            Component.text("GAME FOUND! ").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true)
                                .append(Component.text("Teleporting ").color(TextColor.fromHexString("#ffff00")).decoration(TextDecoration.BOLD, false))
                                .append(Component.text("(${game.queue.getQueue().size}/${Queue.MAX_PLAYERS}) ", NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false))
                                .append(Component.text("in $queueTimerSeconds!").color(TextColor.fromHexString("#ffff00")).decoration(TextDecoration.BOLD, false)))
                    }
                    if(queueTimerSeconds in 1..3){
                        game.queueVisuals.getQueueStatus().name(Component.text("Teleporting in $queueTimerSeconds!").color(TextColor.fromHexString("#ffff00")))
                    }
                    if(queueTimerSeconds == 0) {
                        game.queueVisuals.getQueueStatus().name(Component.text("Teleported!").color(TextColor.fromHexString("#ffff00")))
                        game.queue.queuedAudience().playSound(Sound.sound(Key.key(Sounds.Queue.QUEUE_TELEPORT), Sound.Source.MASTER, 0.75f, 1f))
                        game.queue.queuedAudience().showTitle(Title.title(Component.text("\uD000"), Component.text(""), Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1))))
                    }
                    if(queueTimerSeconds <= -2) {
                        game.queueVisuals.setAllQueueInvisible()
                        stopQueueTask(this, true)
                    }
                } else {
                    game.queue.setQueueState(QueueState.AWAITING_PLAYERS)
                    stopQueueTask(this, false)
                }
            }
        }
        queueRunnable.runTaskTimer(plugin, 0L, 20L)
        queueTaskID = queueRunnable.taskId
        queueTaskMap[queueRunnable.taskId] = queueRunnable
    }

    fun stopQueueTask(queueRunnable : BukkitRunnable, isGameStarting : Boolean) {
        queueTaskMap.remove(queueRunnable.taskId)?.cancel()
        queueActive = false

        if(isGameStarting) {
            game.teamManager.shuffle(game.queue.queuedPlayers())
            game.queue.deleteQueue()
            game.queue.setQueueState(QueueState.NO_GAME_AVAILABLE)
        }
    }
    fun getQueueActive() : Boolean {
        return queueActive
    }
}