package me.byrt.cheesehunt.event

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

@Suppress("unused")
class DamageEvent : Listener {
    @EventHandler
    private fun onDamage(e : EntityDamageEvent) {
        if(e.cause == EntityDamageEvent.DamageCause.HOT_FLOOR
            || e.cause == EntityDamageEvent.DamageCause.SUFFOCATION
            || e.cause == EntityDamageEvent.DamageCause.CRAMMING
            || e.cause == EntityDamageEvent.DamageCause.LAVA
            || e.cause == EntityDamageEvent.DamageCause.FIRE
            || e.cause == EntityDamageEvent.DamageCause.FIRE_TICK
            || e.cause == EntityDamageEvent.DamageCause.CRAMMING
            || e.cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION
            || e.cause == EntityDamageEvent.DamageCause.LIGHTNING
            || e.cause == EntityDamageEvent.DamageCause.WITHER
            || e.cause == EntityDamageEvent.DamageCause.VOID
            || e.cause == EntityDamageEvent.DamageCause.DRAGON_BREATH
            || e.cause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION
            || e.cause == EntityDamageEvent.DamageCause.PROJECTILE
            || e.cause == EntityDamageEvent.DamageCause.POISON
            || e.cause == EntityDamageEvent.DamageCause.CUSTOM) {
            e.isCancelled = true
        }
    }
}