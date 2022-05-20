package me.byrt.cheesehunt.event

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.*

@Suppress("unused")
class PlayerInventoryInteractEvent : Listener {
    @EventHandler
    private fun onInventoryClick(e : InventoryClickEvent) {
        e.isCancelled = true
    }

    @EventHandler
    private fun onInventoryMove(e : InventoryMoveItemEvent) {
        e.isCancelled = true
    }

    @EventHandler
    private fun onInventoryDrag(e : InventoryDragEvent) {
        e.isCancelled = true
    }

    @EventHandler
    private fun onInventory(e : InventoryPickupItemEvent) {
        e.isCancelled = true
    }
}