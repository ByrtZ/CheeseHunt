package me.byrt.cheesehunt.command

import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission

import me.byrt.cheesehunt.Main

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Color
import org.bukkit.Location

import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

import java.util.*
import kotlin.math.cos
import kotlin.math.sin

@Suppress("unused")
class Particle : BaseCommand {
    @CommandMethod("particletest start tornado")
    @CommandDescription("Spawns a tornado particle.")
    @CommandPermission("cheesehunt.particle.test")
    fun testParticleStart(sender : Player) {
        if(sender.name != "Byrt") {
            sender.sendMessage(Component.text("No funny test particles for you.", NamedTextColor.RED))
        } else {
            sender.sendMessage(Component.text("Spawned new particle tornado.", NamedTextColor.GREEN))
            particleLoop(sender.location, Main.getPlugin())
        }
    }

    private fun particleLoop(location : Location, plugin : Plugin) {
        val bukkitRunnable = object : BukkitRunnable() {
            var time = 0
            override fun run() {
                if(time >= 30) {
                    cancel()
                } else {
                    startParticle(location, plugin)
                    time++
                }
            }
        }
        bukkitRunnable.runTaskTimer(plugin, 0L, 15L)
    }

    private fun startParticle( location : Location, plugin : Plugin) {
        val bukkitRunnable = object : BukkitRunnable() {
            var loc = location.clone()
            var radius : Double = 0.0
            var y : Double = 0.0
            override fun run() {
                val x = radius * cos(y)
                val z = radius * sin(y)
                val dust = Particle.DustOptions(Color.fromRGB(133, 217, 54), 2F)
                loc.world.spawnParticle<Any>(
                    Particle.REDSTONE,
                    loc.x + x,
                    loc.y + y,
                    loc.z + z,
                    1,
                    0.0,
                    0.0,
                    0.0,
                    0.0,
                    dust,
                    true
                )

                y += 0.1
                radius += 0.05

                if(y >= 25) {
                    cancel()
                }
            }
        }
        bukkitRunnable.runTaskTimer(plugin, 0L, 1L)
    }
}