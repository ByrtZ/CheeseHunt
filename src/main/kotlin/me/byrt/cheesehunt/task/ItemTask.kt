package me.byrt.cheesehunt.task

import me.byrt.cheesehunt.manager.Game

import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

class ItemTask(private val game : Game) {
    private val ascendMap = mutableMapOf<Int, BukkitRunnable>()
    private var taskID = 0
    fun startAscendTask(player : Player, ascendStand : ArmorStand, plugin : Plugin) {
        val bukkitRunnable = object : BukkitRunnable() {
            val destination = player.location.world.getHighestBlockAt(player.location)
            override fun run() {
                if(player.location.blockY == destination.y + 1) {
                    player.removePotionEffect(PotionEffectType.LEVITATION)
                    ascendStand.remove()
                    stopAscendTask()
                } else {
                    player.addPotionEffect(PotionEffect(PotionEffectType.LEVITATION, 20, 255, false, false))
                    player.teleport(Location(ascendStand.world, ascendStand.location.x, ascendStand.location.y, ascendStand.location.z, player.location.yaw, player.location.pitch))
                    ascendStand.teleport(Location(ascendStand.world, ascendStand.location.x, ascendStand.location.y + 0.35, ascendStand.location.z))
                }
            }
        }
        bukkitRunnable.runTaskTimer(plugin, 0L, 1L)
        ascendMap[bukkitRunnable.taskId] = bukkitRunnable
        taskID = bukkitRunnable.taskId
    }

    fun stopAscendTask() {
        ascendMap.remove(taskID)?.cancel()
    }
}