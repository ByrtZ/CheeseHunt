package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.GameState

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

@Suppress("unused")
class PlayerHurtPlayerEvent : Listener {
    @EventHandler
    private fun playerDamagePlayer(e : EntityDamageByEntityEvent) {
        if(Main.getGame()?.getGameState() != GameState.IN_GAME) {
            e.isCancelled = true
        } else {
            if(e.entity is Player && e.damager is Player) {
                if(Main.getGame()?.getTeamManager()?.isInRedTeam(e.entity.uniqueId) == true && Main.getGame()?.getTeamManager()?.isInRedTeam(e.damager.uniqueId) == true) {
                    e.isCancelled = true
                }
                if(Main.getGame()?.getTeamManager()?.isInBlueTeam(e.entity.uniqueId) == true && Main.getGame()?.getTeamManager()?.isInBlueTeam(e.damager.uniqueId) == true) {
                    e.isCancelled = true
                }
            }

            if(Main.getGame()?.getTeamManager()?.isSpectator(e.entity.uniqueId) == true) {
                e.isCancelled = true
            }
        }
    }
}