package me.byrt.cheesehunt.queue

import me.byrt.cheesehunt.game.Game
import me.byrt.cheesehunt.game.GameState
import me.byrt.cheesehunt.state.Sounds
import me.byrt.cheesehunt.util.DevStatus

import net.kyori.adventure.audience.Audience

import org.bukkit.Bukkit
import org.bukkit.entity.Player

import java.util.UUID

class Queue(private val game : Game) {
    private var queue = mutableSetOf<UUID>()
    private var queueState = QueueState.IDLE

    fun joinQueue(player : Player) {
        if(queue.contains(player.uniqueId)) return
        if(queue.size > MAX_PLAYERS) return
        if(game.gameManager.getGameState() != GameState.IDLE) return

        if(queue.isEmpty()) {
            setQueueState(QueueState.AWAITING_PLAYERS)
        }

        queue.add(player.uniqueId)
        game.queueVisuals.updateQueueStatus()
        game.queueVisuals.setQueueVisible()
        player.playSound(player.location, Sounds.Queue.QUEUE_JOIN, 1.0f, 1.5f)
        game.dev.parseDevMessage("${player.name} joined the queue (Queue: ${queue}).", DevStatus.INFO)

        if(queue.size >= MIN_PLAYERS && !game.queueTask.getQueueActive()) {
            setQueueState(QueueState.SENDING_PLAYERS_TO_GAME)
        }
    }

    fun leaveQueue(player : Player) {
        if(queue.contains(player.uniqueId)) {
            queue.remove(player.uniqueId)
            player.playSound(player.location, Sounds.Queue.QUEUE_LEAVE, 1.0f, 1.0f)
            game.dev.parseDevMessage("${player.name} left the queue (Queue: ${queue})", DevStatus.INFO)
            game.queueVisuals.setQueueInvisible(player)

            if(queue.isEmpty()) {
                setQueueState(QueueState.IDLE)
            }

            game.queueVisuals.updateQueueStatus()
        } else {
            return
        }
    }

    fun deleteQueue() {
        queue.clear()
    }

    fun queuedAudience() : Audience {
        return Audience.audience(Bukkit.getOnlinePlayers().filter { p : Player -> queue.contains(p.uniqueId) })
    }

    fun queuedPlayers() : Collection<Player> {
        return Bukkit.getOnlinePlayers().filter { p : Player -> queue.contains(p.uniqueId) }
    }

    fun getQueue() : Set<UUID> {
        return queue
    }

    fun getQueueState() : QueueState {
        return queueState
    }

    fun setQueueState(newState : QueueState) {
        if(newState != queueState) {
            game.dev.parseDevMessage("Queue State updated to $newState from $queueState.", DevStatus.INFO)
            queueState = newState
            game.queueVisuals.updateQueueStatus()
        }
    }

    companion object {
        const val MAX_PLAYERS = 32
        const val MIN_PLAYERS = 4
    }
}