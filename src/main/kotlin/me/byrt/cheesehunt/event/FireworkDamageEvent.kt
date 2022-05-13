package me.byrt.cheesehunt.event

import org.bukkit.entity.Firework
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

@Suppress("unused")
class FireworkDamageEvent : Listener {
    @EventHandler
    private fun cancelFireworkDamage(e : EntityDamageByEntityEvent) {
        if(e.damager is Firework) {
            e.isCancelled = true
        }
    }
}