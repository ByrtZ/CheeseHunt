package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.game.GameState

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.entity.Arrow
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

@Suppress("unused")
class PlayerDamageEvent : Listener {
    @EventHandler
    private fun playerDamagePlayer(e : EntityDamageByEntityEvent) {
        if(Main.getGame().gameManager.getGameState() == GameState.IN_GAME) {
            if(e.entity is Player && e.damager is Player || e.damager is Arrow || e.damager is TNTPrimed) {
                val player = e.entity as Player
                if(Main.getGame().cheeseManager.playerHasCheese(player)) {
                    Main.getGame().cheeseManager.playerDropCheese(player)
                }
                if(e.damager is Arrow) {
                    e.damager.remove()
                }
                e.isCancelled = false
            }
            if(e.entity is Player && e.damager is Player) {
                val player = e.entity as Player
                val damager = e.damager as Player
                e.isCancelled =
                    (Main.getGame().teamManager.isInRedTeam(player.uniqueId) && Main.getGame().teamManager.isInRedTeam(damager.uniqueId)
                     || Main.getGame().teamManager.isInBlueTeam(player.uniqueId) && Main.getGame().teamManager.isInBlueTeam(damager.uniqueId))
            }
        } else {
            if(Main.getGame().gameManager.getGameState() == GameState.OVERTIME) {
                if(e.damager is Player) {
                    e.damager.sendMessage(Component.text("You cannot hurt players during overtime, go get the Cheese!", NamedTextColor.RED))
                }
            }
            e.isCancelled = true
        }
    }

    @EventHandler
    private fun onFireworkDamage(e : EntityDamageByEntityEvent) {
        if(e.damager is Firework) e.isCancelled = true
    }
}