package dev.byrt.cheesehunt.event

import io.papermc.paper.event.block.BlockBreakBlockEvent

import dev.byrt.cheesehunt.CheeseHunt
import dev.byrt.cheesehunt.game.GameState

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
            if(CheeseHunt.getGame().gameManager.getGameState() != GameState.IN_GAME || CheeseHunt.getGame().gameManager.getGameState() != GameState.OVERTIME) {
                e.isCancelled = !(CheeseHunt.getGame().getBuildMode() && e.player.isOp)
            }
            if(CheeseHunt.getGame().gameManager.getGameState()  == GameState.IN_GAME || CheeseHunt.getGame().gameManager.getGameState() == GameState.OVERTIME) {
                if(e.block.type == Material.SPONGE && !CheeseHunt.getGame().cheeseManager.playerHasCheese(e.player)) {
                    CheeseHunt.getGame().cheeseManager.playerPickupCheese(e.player, e.block.location)
                    e.isCancelled = false
                } else {
                    if(e.block.type == Material.SPONGE) {
                        e.player.sendMessage(Component.text("[")
                            .append(Component.text("▶").color(NamedTextColor.YELLOW))
                            .append(Component.text("] "))
                            .append(Component.text("You already have a piece of cheese, don't be greedy!", NamedTextColor.RED)))
                    }
                    e.isCancelled = true
                }
            }
        }
    }

    @EventHandler
    private fun onAbortBlockBreak(e : BlockDamageAbortEvent) {
        if(CheeseHunt.getGame().gameManager.getGameState() != GameState.IDLE && e.block.type == Material.SPONGE && e.itemInHand.type == Material.WOODEN_PICKAXE) {
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
