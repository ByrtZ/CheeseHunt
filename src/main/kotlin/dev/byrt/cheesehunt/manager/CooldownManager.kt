package dev.byrt.cheesehunt.manager

import dev.byrt.cheesehunt.game.Game
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.entity.Player

import java.util.*
import java.util.concurrent.TimeUnit

@Suppress("unused")
class CooldownManager(private val game : Game) {
    private var tntCooldown = HashMap<UUID, Long>()
    private val tntUseCooldown = 2500

    fun attemptUseTNT(player : Player) : Boolean {
        return if(!tntCooldown.containsKey(player.uniqueId) || System.currentTimeMillis() - tntCooldown[player.uniqueId]!! > tntUseCooldown) {
            tntCooldown[player.uniqueId] = System.currentTimeMillis()
            true
        } else {
            val totalTimeLeft = tntUseCooldown - (System.currentTimeMillis() - tntCooldown[player.uniqueId]!!)
            val secondsTimeLeft = TimeUnit.MILLISECONDS.toSeconds(totalTimeLeft)
            player.sendActionBar(Component.text("⚠ You cannot use Throwing TNT for ${secondsTimeLeft + 1}s. ⚠", NamedTextColor.RED))
            false
        }
    }
}