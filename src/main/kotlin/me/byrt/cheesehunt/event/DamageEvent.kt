package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.GameState

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

@Suppress("unused")
class DamageEvent : Listener {
    @EventHandler
    private fun onDamage(e : EntityDamageEvent) {
        e.isCancelled = Main.getGame().getGameState() != GameState.IN_GAME
    }
}