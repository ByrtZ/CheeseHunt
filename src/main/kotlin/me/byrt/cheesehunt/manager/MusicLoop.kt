package me.byrt.cheesehunt.manager

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

import java.util.*

@Suppress("unused")
class MusicLoop(private val game : Game) {
    private val musicLoopMap = mutableMapOf<UUID, BukkitRunnable>()

    fun startMusicLoop(player : Player, plugin : Plugin) {
        val bukkitRunnable = object: BukkitRunnable() {
            var musicTimer = 0
            override fun run() {
                if(musicTimer == 0) {
                    player.playSound(player.location, "event.downtime.loop", 1f, 1f)
                }
                if(musicTimer == 191) {
                    musicTimer = -1
                }
                musicTimer++
            }
        }
        bukkitRunnable.runTaskTimer(plugin, 0L, 20L)
        musicLoopMap[player.uniqueId] = bukkitRunnable
    }

    fun removeFromMusicLoop(player : Player) {
        musicLoopMap.remove(player.uniqueId)?.cancel()
    }
}