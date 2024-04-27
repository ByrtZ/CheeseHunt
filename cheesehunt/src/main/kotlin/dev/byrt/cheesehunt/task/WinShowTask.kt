package dev.byrt.cheesehunt.task

import dev.byrt.cheesehunt.CheeseHunt
import dev.byrt.cheesehunt.game.Game
import dev.byrt.cheesehunt.state.Teams
import me.lucyydotp.cheeselib.inject.inject

import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Location
import org.bukkit.entity.Firework
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

class WinShowTask(private val game : Game) {
    private val winShowLoopMap = mutableMapOf<Int, BukkitRunnable>()
    private var taskID = 0

    fun startWinShowLoop(plugin : CheeseHunt, winTeam : Teams) {
        val bukkitRunnable = object : BukkitRunnable() {
            var winShowTimer = 20
            var winShowTimerSecs = 22
            override fun run() {
                winShowTimer--
                if(winShowTimer <= 0) {
                    winShowTimerSecs--
                    winShowTimer = 20
                }
                if(winShowTimer % 5 == 0) {
                    val randomLoc = game.locationManager.getRandomWinShowLoc()
                    when(winTeam) {
                        Teams.RED -> { randomFirework(randomLoc, Color.RED) }
                        Teams.BLUE -> { randomFirework(randomLoc, Color.BLUE) }
                        else -> { stopWinShowLoop() }
                    }
                }

                if(winShowTimerSecs == 0) {
                    stopWinShowLoop()
                }
            }
        }
        // FIXME(lucy): this is horrid. don't do that
        bukkitRunnable.runTaskTimer(plugin.inject<Plugin>(), 0L, 1L)
        taskID = bukkitRunnable.taskId
        winShowLoopMap[bukkitRunnable.taskId] = bukkitRunnable
    }

    fun stopWinShowLoop() {
        winShowLoopMap.remove(taskID)?.cancel()
    }

    private fun randomFirework(location : Location, colour : Color) {
        val f: Firework = location.world.spawn(location, Firework::class.java)
        val fm = f.fireworkMeta
        fm.addEffect(
            FireworkEffect.builder()
                .flicker(false)
                .trail(false)
                .with(FireworkEffect.Type.BALL_LARGE)
                .withColor(colour)
                .build()
        )
        fm.power = 0
        f.fireworkMeta = fm
        f.ticksToDetonate = 1
    }
}
