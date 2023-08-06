package dev.byrt.cheesehunt.event

import dev.byrt.cheesehunt.Main
import dev.byrt.cheesehunt.manager.PowerUpItem
import dev.byrt.cheesehunt.game.GameState

import org.bukkit.Material
import org.bukkit.block.data.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

@Suppress("unused")
class PlayerInteractionsEvent : Listener {
    @EventHandler
    private fun onInteract(e : PlayerInteractEvent) {
        if(Main.getGame().gameManager.getGameState() != GameState.IDLE) {
            // Cheese Mining
            if(e.action.isLeftClick && e.clickedBlock?.type == Material.SPONGE && e.player.inventory.itemInMainHand.type == Material.WOODEN_PICKAXE) {
                e.player.addPotionEffect(PotionEffect(PotionEffectType.SLOW_DIGGING, Int.MAX_VALUE, 0, false, false))
            }
            // Item Usage
            if(Main.getGame().gameManager.getGameState() == GameState.IN_GAME && e.action.isRightClick && e.player.inventory.itemInMainHand.type == Material.TNT) {
                Main.getGame().itemManager.useItem(PowerUpItem.THROWABLE_TNT, e.player)
            }
            if(Main.getGame().gameManager.getGameState() == GameState.IN_GAME && e.action.isRightClick && e.player.inventory.itemInMainHand.type == Material.GRAY_DYE) {
                Main.getGame().itemManager.useItem(PowerUpItem.INVISIBILITY_CHARM, e.player)
            }
            if(Main.getGame().gameManager.getGameState() == GameState.IN_GAME && e.action.isRightClick && e.player.inventory.itemInMainHand.type == Material.LIGHT_BLUE_DYE) {
                Main.getGame().itemManager.useItem(PowerUpItem.SPEED_CHARM, e.player)
            }
            if(Main.getGame().gameManager.getGameState() == GameState.IN_GAME && e.action.isRightClick && e.player.inventory.itemInMainHand.type == Material.FEATHER) {
                Main.getGame().itemManager.useItem(PowerUpItem.ESCAPE_CHARM, e.player)
            }
            if(Main.getGame().gameManager.getGameState() == GameState.IN_GAME && e.action.isRightClick && e.player.inventory.itemInMainHand.type == Material.ORANGE_DYE) {
                Main.getGame().itemManager.useItem(PowerUpItem.HASTE_CHARM, e.player)
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