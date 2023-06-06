package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.state.GameState
import me.byrt.cheesehunt.state.Sounds

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.Material
import org.bukkit.block.data.*
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

@Suppress("unused")
class PlayerInteractionsEvent : Listener {
    @EventHandler
    private fun onClickCheese(e : PlayerInteractEvent) {
        if(Main.getGame().gameManager.getGameState() != GameState.IDLE) {
            // Cheese Mining
            if(e.action == Action.LEFT_CLICK_BLOCK && e.clickedBlock?.type == Material.SPONGE && e.player.inventory.itemInMainHand.type == Material.WOODEN_PICKAXE) {
                e.player.addPotionEffect(PotionEffect(PotionEffectType.SLOW_DIGGING, Int.MAX_VALUE, 0, false, false))
            }
            // Item Usage
            if(Main.getGame().gameManager.getGameState() == GameState.IN_GAME && e.action.isRightClick && e.player.inventory.itemInMainHand.type == Material.TNT) {
                val player = e.player
                if(Main.getGame().cooldownManager.attemptUseTNT(player)) {
                    if(player.inventory.itemInMainHand.amount > 1) {
                        player.inventory.itemInMainHand.amount--
                    } else {
                        player.inventory.setItemInMainHand(null)
                    }
                    player.inventory.removeItem(ItemStack(player.inventory.itemInMainHand.type, 1))
                    player.playSound(player.location, Sounds.Item.USE_ITEM, 1.0f, 1.0f)
                    player.sendMessage(Component.text("[").append(Component.text("▶", NamedTextColor.YELLOW)).append(Component.text("] ")).append(Component.text("You used a Throwing TNT!", NamedTextColor.GREEN)))
                    val tnt = player.world.spawn(player.location, TNTPrimed::class.java)
                    val tntVelocity = player.location.direction.multiply(1.25)
                    tnt.velocity = tntVelocity
                }
            }
            if(Main.getGame().gameManager.getGameState() == GameState.IN_GAME && e.action.isRightClick && e.player.inventory.itemInMainHand.type == Material.GRAY_DYE) {
                val player = e.player
                if(player.inventory.itemInMainHand.amount > 1) {
                    player.inventory.itemInMainHand.amount--
                } else {
                    player.inventory.setItemInMainHand(null)
                }
                player.playSound(player.location, Sounds.Item.USE_ITEM, 1.0f, 1.0f)
                player.sendMessage(Component.text("[").append(Component.text("▶", NamedTextColor.YELLOW)).append(Component.text("] ")).append(Component.text("Your Invisibili-brie charm granted you invisibility for 10 seconds!", NamedTextColor.GREEN)))
                player.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 200, 0, false, false))
            }
            // Disable block interactions
            if(e.action.isRightClick
                && e.clickedBlock?.blockData is Openable
                || e.clickedBlock?.blockData is Directional
                || e.clickedBlock?.blockData is Orientable
                || e.clickedBlock?.blockData is Rotatable
                || e.clickedBlock?.blockData is Powerable
                || e.clickedBlock?.type == Material.FLOWER_POT
                || e.clickedBlock?.type == Material.BEACON
                || e.clickedBlock?.type?.name?.startsWith("POTTED_") == true) {
                e.isCancelled = true
            }
        } else {
            e.isCancelled = !(e.player.isOp && Main.getGame().getBuildMode())
        }
    }
}