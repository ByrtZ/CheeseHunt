package dev.byrt.cheesehunt.event

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityResurrectEvent

@Suppress("unused")
class PlayerResurrectEvent : Listener {
    @EventHandler
    private fun onResurrect(e : EntityResurrectEvent) {
        if(e.entity is Player) {
            e.isCancelled = true
        }
    }
}