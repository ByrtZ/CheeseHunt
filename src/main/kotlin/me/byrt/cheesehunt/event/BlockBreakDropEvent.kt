package me.byrt.cheesehunt.event

import io.papermc.paper.event.block.BlockBreakBlockEvent
import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.state.GameState

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockDamageAbortEvent
import org.bukkit.event.block.BlockDropItemEvent
import org.bukkit.potion.PotionEffectType

@Suppress("unused")
class BlockBreakDropEvent : Listener {
    @EventHandler
    private fun onBlockBreak(e : BlockBreakEvent) {
        if(e.player.inventory.itemInMainHand.type == Material.DEBUG_STICK) {
            e.isCancelled = true
        } else {
            if(Main.getGame().getGameState() != GameState.IN_GAME) {
                e.isCancelled = !(Main.getGame().getBuildMode() && e.player.isOp)
            }
            if(Main.getGame().getGameState() == GameState.IN_GAME) {
                if(e.block.type == Material.SPONGE && !Main.getGame().getCheeseManager().playerHasCheese(e.player)) {
                    Main.getGame().getCheeseManager().playerPickupCheese(e.player, e.block.location)
                    e.isCancelled = false
                } else {
                    e.player.sendMessage(Component.text("You already have a piece of cheese, don't be greedy!", NamedTextColor.RED))
                    e.isCancelled = true
                }
            }
        }
    }

    @EventHandler
    private fun onAbortBlockBreak(e : BlockDamageAbortEvent) {
        if(Main.getGame().getGameState() == GameState.IN_GAME && e.block.type == Material.SPONGE && e.itemInHand.type == Material.WOODEN_PICKAXE) {
            e.player.removePotionEffect(PotionEffectType.SLOW_DIGGING)
        }
    }

    @EventHandler
    private fun onBlockItemDrop(e : BlockDropItemEvent) {
        e.isCancelled = true
    }

    @EventHandler
    private fun onBlockBreakBlock(e : BlockBreakBlockEvent) {
        e.drops.clear()
    }
}