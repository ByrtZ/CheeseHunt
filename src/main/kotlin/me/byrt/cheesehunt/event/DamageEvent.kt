package me.byrt.cheesehunt.event

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

@Suppress("unused")
class DamageEvent : Listener {
    @EventHandler
    private fun onDamage(e : EntityDamageEvent) {
        if(e.cause == EntityDamageEvent.DamageCause.SUFFOCATION || e.cause == EntityDamageEvent.DamageCause.VOID) {
            e.isCancelled = true
        }
    }
}