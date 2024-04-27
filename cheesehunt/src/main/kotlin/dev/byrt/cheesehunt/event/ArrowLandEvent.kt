package dev.byrt.cheesehunt.event

import dev.byrt.cheesehunt.CheeseHunt
import dev.byrt.cheesehunt.game.GameState

import org.bukkit.entity.Arrow
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent

@Suppress("unused")
class ArrowLandEvent : Listener {
    @EventHandler
    private fun onArrowLand(e : ProjectileHitEvent) {
        if(CheeseHunt.getGame().gameManager.getGameState() == GameState.IN_GAME || CheeseHunt.getGame().gameManager.getGameState() == GameState.OVERTIME) {
            if(e.entity is Arrow && e.hitEntity == null) {
                e.entity.remove()
            }
        }
    }
}
