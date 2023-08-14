package dev.byrt.cheesehunt.manager

import dev.byrt.cheesehunt.game.Game

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.entity.Player

import java.util.*
import java.util.concurrent.TimeUnit

class CooldownManager(private val game : Game) {
    private var tntCooldown = HashMap<UUID, Long>()
    private val tntCooldownTime = 2500

    private var queueCooldown = HashMap<UUID, Long>()
    private val queueCooldownTime = 1500

    fun attemptUseTNT(player : Player) : Boolean {
        return if(!tntCooldown.containsKey(player.uniqueId) || System.currentTimeMillis() - tntCooldown[player.uniqueId]!! > tntCooldownTime) {
            tntCooldown[player.uniqueId] = System.currentTimeMillis()
            true
        } else {
            val totalTimeLeft = tntCooldownTime - (System.currentTimeMillis() - tntCooldown[player.uniqueId]!!)
            val secondsTimeLeft = TimeUnit.MILLISECONDS.toSeconds(totalTimeLeft)
            player.sendActionBar(Component.text("⚠ You cannot use Throwing TNT for ${secondsTimeLeft + 1}s. ⚠", NamedTextColor.RED))
            false
        }
    }

    fun attemptJoinQueue(player : Player) : Boolean {
        return if(!queueCooldown.containsKey(player.uniqueId) || System.currentTimeMillis() - queueCooldown[player.uniqueId]!! > queueCooldownTime) {
            queueCooldown[player.uniqueId] = System.currentTimeMillis()
            true
        } else {
            false
        }
    }
}