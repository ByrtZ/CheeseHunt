package dev.byrt.cheesehunt.event

import dev.byrt.cheesehunt.CheeseHunt
import dev.byrt.cheesehunt.game.GameState
import dev.byrt.cheesehunt.state.Sounds

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerAttemptPickupItemEvent
import org.bukkit.event.player.PlayerDropItemEvent

@Suppress("unused")
class PlayerItemEvent : Listener {
    @EventHandler
    private fun dropItem(e : PlayerDropItemEvent) {
        e.isCancelled = !(CheeseHunt.getGame().getBuildMode() && e.player.isOp)
    }

    @EventHandler
    private fun attemptPickupItem(e : PlayerAttemptPickupItemEvent) {
        if(CheeseHunt.getGame().gameManager.getGameState() == GameState.IN_GAME || CheeseHunt.getGame().gameManager.getGameState() == GameState.OVERTIME) {
            if(e.player.inventory.contains(e.item.itemStack.type) || e.player.inventory.itemInOffHand.type == e.item.itemStack.type) {
                e.player.sendActionBar(Component.text("⚠ You cannot hold any more of this item. ⚠", NamedTextColor.RED))
                e.isCancelled = true
            } else {
                e.player.playSound(e.player.location, Sounds.Item.PICKUP_ITEM, 1.0f, 1.0f)
            }
            if(e.player.inventory.itemInOffHand.type == Material.AIR && e.item.itemStack.type == Material.TOTEM_OF_UNDYING) {
                e.player.playSound(e.player.location, Sounds.Item.PICKUP_ITEM, 1.0f, 1.0f)
                e.player.inventory.setItemInOffHand(e.item.itemStack)
                e.item.remove()
                e.isCancelled = true
            }
        }
    }
}
