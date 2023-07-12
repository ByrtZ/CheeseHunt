package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.PowerUpItem
import me.byrt.cheesehunt.state.GameState
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.HeightMap

import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.block.data.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

@Suppress("unused")
class PlayerInteractionsEvent : Listener {
    @EventHandler
    private fun onClickCheese(e : PlayerInteractEvent) {
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
            if(Main.getGame().gameManager.getGameState() == GameState.IN_GAME && e.action.isRightClick && e.player.inventory.itemInMainHand.type == Material.LIME_DYE) {
                if(e.player.world.getHighestBlockAt(e.player.location, HeightMap.WORLD_SURFACE).type != Material.STRUCTURE_VOID && e.player.world.getHighestBlockAt(e.player.location, HeightMap.WORLD_SURFACE) == e.player.location.block.getRelative(BlockFace.DOWN)) {
                    e.player.sendMessage(Component.text("[").append(Component.text("▶", NamedTextColor.YELLOW)).append(Component.text("] ")).append(Component.text("You cannot use an Ascend Charm here.", NamedTextColor.RED)))
                } else {
                    Main.getGame().itemManager.useItem(PowerUpItem.ASCEND_CHARM, e.player)
                }
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