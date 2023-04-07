package me.byrt.cheesehunt.manager

import org.bukkit.SoundCategory
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

import java.util.*

@Suppress("unused")
class MusicLoop(private val game : Game) {
    private val musicLoopMap = mutableMapOf<UUID, BukkitRunnable>()

    fun startMusicLoop(player : Player, plugin : Plugin, music : Music) {
        val bukkitRunnable = object: BukkitRunnable() {
            var musicTimer = 0
            override fun run() {
                when(music) {
                    Music.MAIN -> {
                        if(musicTimer == 0) {
                            player.playSound(player.location, Music.MAIN.track, SoundCategory.VOICE, 0.75f, 1f)
                        }
                        if(musicTimer == 242) {
                            musicTimer = -1
                        }
                    }
                    Music.OVERTIME -> {
                        if(musicTimer == 0) {
                            player.playSound(player.location, Music.OVERTIME.track, SoundCategory.VOICE, 1f, 1f)
                        }
                        if(musicTimer == 23) {
                            musicTimer = -1
                        }
                    }
                    else -> {
                        //no.
                    }
                }
                musicTimer++
            }
        }
        bukkitRunnable.runTaskTimer(plugin, 0L, 20L)
        musicLoopMap[player.uniqueId] = bukkitRunnable
    }

    fun stopMusicLoop(player : Player, music : Music) {
        player.stopSound(music.track, SoundCategory.VOICE)
        musicLoopMap.remove(player.uniqueId)?.cancel()
    }
}

enum class Music(val track: String) {
    MAIN(Sounds.Music.GAME_MUSIC),
    OVERTIME(Sounds.Music.OVERTIME_MUSIC),
    NULL("")
}