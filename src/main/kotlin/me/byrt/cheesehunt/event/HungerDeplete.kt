package me.byrt.cheesehunt.event

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.FoodLevelChangeEvent

@Suppress("unused")
class HungerDeplete : Listener {
    @EventHandler
    private fun onHungerDeplete(e : FoodLevelChangeEvent) {
        e.entity.foodLevel = 20
        e.isCancelled = true
    }
}