package dev.byrt.cheesehunt.event

import dev.byrt.cheesehunt.CheeseHunt
import dev.byrt.cheesehunt.game.GameState

import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent

@Suppress("unused")
class DamageEvent : Listener {
    @EventHandler
    private fun onDamage(e : EntityDamageEvent) {
        if(e.entity is Player && e.cause == EntityDamageEvent.DamageCause.SUFFOCATION || e.cause == EntityDamageEvent.DamageCause.VOID) {
            e.isCancelled = true
        }
    }

    @EventHandler
    private fun tntDamageItem(e : EntityDeathEvent) {
        if(CheeseHunt.getGame().gameManager.getGameState() == GameState.IN_GAME || CheeseHunt.getGame().gameManager.getGameState() == GameState.OVERTIME) {
            if(e.entity is Item) {
                val item = e.entity
                val lastDamageCause = item.lastDamageCause
                val damageCause = lastDamageCause?.cause

                if(damageCause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
                    e.isCancelled = true
                }
            }
        }
    }
}
