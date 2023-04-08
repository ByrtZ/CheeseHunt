package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.GameState

import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

@Suppress("unused")
class PlayerDamageEvent : Listener {
    @EventHandler
    private fun playerDamagePlayer(e : EntityDamageByEntityEvent) {
        if(Main.getGame().getGameState() != GameState.IN_GAME) {
            e.isCancelled = true
        } else {
            if(e.damager is Firework) {
                e.isCancelled = true
            }
            if(e.entity is Player && e.damager is Player) {
                val player = e.entity as Player
                val damager = e.damager as Player
                e.isCancelled = (Main.getGame().getTeamManager().isInRedTeam(player.uniqueId) && Main.getGame().getTeamManager().isInRedTeam(damager.uniqueId) || Main.getGame().getTeamManager().isInBlueTeam(player.uniqueId) && Main.getGame().getTeamManager().isInBlueTeam(damager.uniqueId))
            }
        }
    }
}