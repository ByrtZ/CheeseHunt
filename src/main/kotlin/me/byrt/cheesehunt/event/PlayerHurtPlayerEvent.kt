package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.command.ModifierOptions
import me.byrt.cheesehunt.manager.GameState

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

@Suppress("unused")
class PlayerHurtPlayerEvent : Listener {
    @EventHandler
    private fun playerDamagePlayer(e : EntityDamageByEntityEvent) {
        if(Main.getGame().getModifier() == ModifierOptions.IMPOSTOR && Main.getGame().getGameState() == GameState.IN_GAME) {
            if(e.damager is Player && e.entity is Player) {
                val playerHurt = (e.entity as Player)
                val playerHurtingPlayer = (e.damager as Player)
                if(Main.getGame().getTeamManager().isInRedTeam(playerHurtingPlayer.uniqueId) && Main.getGame().getTeamManager().isInBlueTeam(playerHurt.uniqueId)) {
                    e.damage = 0.01
                    e.isCancelled = false
                } else if(Main.getGame().getTeamManager().isInRedTeam(playerHurt.uniqueId) && Main.getGame().getTeamManager().isInBlueTeam(playerHurtingPlayer.uniqueId)) {
                    e.damage = 0.01
                    e.isCancelled = false
                } else {
                    e.isCancelled = true
                }
            }
        } else {
            e.isCancelled = true
        }
    }
}