package me.byrt.cheesehunt.manager

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

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
                player.sendActionBar(Component.text("Music Loop Timer: ${String.format("%02d:%02d", musicTimer / 60, musicTimer % 60)}").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true))
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