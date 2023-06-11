package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.state.GameState
import me.byrt.cheesehunt.state.Teams

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPotionEffectEvent

@Suppress("unused")
class PlayerPotionEffectEvent : Listener {
    @EventHandler
    private fun onLoseInvis(e : EntityPotionEffectEvent) {
        if(Main.getGame().gameManager.getGameState() != GameState.IDLE && e.action == EntityPotionEffectEvent.Action.REMOVED && e.entity is Player) {
            val player = e.entity as Player
            when(Main.getGame().teamManager.getPlayerTeam(player.uniqueId)) {
                Teams.RED -> { Main.getGame().itemManager.givePlayerTeamBoots(player, Teams.RED) }
                Teams.BLUE -> { Main.getGame().itemManager.givePlayerTeamBoots(player, Teams.BLUE) }
                Teams.SPECTATOR -> { Main.getGame().itemManager.givePlayerTeamBoots(player, Teams.SPECTATOR) }
            }
        }
    }
}