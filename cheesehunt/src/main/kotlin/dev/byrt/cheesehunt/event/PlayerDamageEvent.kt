package dev.byrt.cheesehunt.event

import dev.byrt.cheesehunt.CheeseHunt
import dev.byrt.cheesehunt.game.GameState

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
        if(CheeseHunt.getGame().gameManager.getGameState() == GameState.IN_GAME) {
            if(e.entity is Player && e.damager is Player || e.damager is Arrow || e.damager is TNTPrimed) {
                val player = e.entity as Player
                if(CheeseHunt.getGame().cheeseManager.playerHasCheese(player)) {
                    CheeseHunt.getGame().cheeseManager.playerDropCheese(player)
                }
                if(e.damager is Arrow) {
                    e.damager.remove()
                }
                e.isCancelled = false
            }
            if(e.entity is Player && e.damager is Player) {
                val teamManager = CheeseHunt.getGame().teams
                e.isCancelled = teamManager.getTeam(e.entity as Player) == teamManager.getTeam(e.damager as Player)
            }
        } else {
            if(CheeseHunt.getGame().gameManager.getGameState() == GameState.OVERTIME) {
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
