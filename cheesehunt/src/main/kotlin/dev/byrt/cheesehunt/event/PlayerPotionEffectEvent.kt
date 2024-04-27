package dev.byrt.cheesehunt.event

import dev.byrt.cheesehunt.CheeseHunt
import dev.byrt.cheesehunt.game.GameState
import dev.byrt.cheesehunt.state.Teams

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.potion.PotionEffectType

@Suppress("unused")
class PlayerPotionEffectEvent : Listener {
    @EventHandler
    private fun onLoseInvis(e : EntityPotionEffectEvent) {
        if(CheeseHunt.getGame().gameManager.getGameState() != GameState.IDLE && e.action == EntityPotionEffectEvent.Action.REMOVED || e.action == EntityPotionEffectEvent.Action.CLEARED && e.oldEffect?.equals(PotionEffectType.INVISIBILITY) == true && e.entity is Player) {
            val player = e.entity as Player
            when(CheeseHunt.getGame().teamManager.getPlayerTeam(player.uniqueId)) {
                Teams.RED -> { CheeseHunt.getGame().itemManager.givePlayerTeamBoots(player, Teams.RED) }
                Teams.BLUE -> { CheeseHunt.getGame().itemManager.givePlayerTeamBoots(player, Teams.BLUE) }
                Teams.SPECTATOR -> { CheeseHunt.getGame().itemManager.givePlayerTeamBoots(player, Teams.SPECTATOR) }
            }
        }
    }
}
