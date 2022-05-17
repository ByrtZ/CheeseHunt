package me.byrt.cheesehunt.event

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

@Suppress("unused")
class MagmaDamageEvent : Listener {
    @EventHandler
    private fun onMagmaDamage(e : EntityDamageEvent) {
        if(e.cause == EntityDamageEvent.DamageCause.HOT_FLOOR) {
            e.isCancelled = true
        }
    }
}