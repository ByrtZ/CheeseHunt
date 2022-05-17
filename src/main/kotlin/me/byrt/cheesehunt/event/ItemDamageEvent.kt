package me.byrt.cheesehunt.event

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemDamageEvent

@Suppress("unused")
class ItemDamageEvent : Listener {
    @EventHandler
    private fun onItemDamage(e : PlayerItemDamageEvent) {
        e.isCancelled = true
    }
}