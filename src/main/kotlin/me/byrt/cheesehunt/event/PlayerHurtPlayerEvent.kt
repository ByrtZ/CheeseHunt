package me.byrt.cheesehunt.event

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

@Suppress("unused")
class PlayerHurtPlayerEvent : Listener {
    @EventHandler
    private fun playerDamagePlayer(e : EntityDamageByEntityEvent) {
            e.isCancelled = true
    }
}