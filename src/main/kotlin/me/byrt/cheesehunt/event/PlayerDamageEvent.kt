package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.state.GameState

import org.bukkit.entity.Arrow
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

@Suppress("unused")
class PlayerDamageEvent : Listener {
    @EventHandler
    private fun playerDamagePlayer(e : EntityDamageByEntityEvent) {
        if(Main.getGame().getGameState() == GameState.IN_GAME) {
            if(e.entity is Player && e.damager is Player || e.damager is Arrow) {
                val player = e.entity as Player
                if (Main.getGame().getCheeseManager().playerHasCheese(player)) {
                    Main.getGame().getCheeseManager().playerDropCheese(player)
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
                    (Main.getGame().getTeamManager().isInRedTeam(player.uniqueId) && Main.getGame().getTeamManager().isInRedTeam(damager.uniqueId)
                     || Main.getGame().getTeamManager().isInBlueTeam(player.uniqueId) && Main.getGame().getTeamManager().isInBlueTeam(damager.uniqueId))
            }
        } else {
            e.isCancelled = true
        }
    }

    @EventHandler
    private fun onFireworkDamage(e : EntityDamageByEntityEvent) {
        if(e.damager is Firework) e.isCancelled = true
    }
}