package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.state.GameState

import org.bukkit.entity.Arrow
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent

@Suppress("unused")
class ArrowLandEvent : Listener {
    private fun onArrowLand(e : ProjectileHitEvent) {
        if(Main.getGame().getGameState() == GameState.IN_GAME) {
            if(e.entity is Arrow) {
                val arrow = e.entity as Arrow
                arrow.remove() // NOT WORKING???
            }
        }
    }
}