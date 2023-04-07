package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.*
import org.bukkit.event.player.PlayerSwapHandItemsEvent

@Suppress("unused")
class PlayerInventoryInteractEvent : Listener {
    @EventHandler
    private fun onInventoryClick(e : InventoryClickEvent) {
        e.isCancelled = !(Main.getGame().getBuildMode() && e.whoClicked.isOp)
    }

    @EventHandler
    private fun onInventoryMove(e : InventoryMoveItemEvent) {
        e.isCancelled = !Main.getGame().getBuildMode()
    }

    @EventHandler
    private fun onInventoryDrag(e : InventoryDragEvent) {
        e.isCancelled = !(Main.getGame().getBuildMode() && e.whoClicked.isOp)
    }

    @EventHandler
    private fun onInventory(e : InventoryPickupItemEvent) {
        e.isCancelled = !Main.getGame().getBuildMode()
    }

    @EventHandler
    private fun onOffhand(e : PlayerSwapHandItemsEvent) {
        e.isCancelled = !Main.getGame().getBuildMode()
    }
}