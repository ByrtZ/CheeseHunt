package dev.byrt.cheesehunt.event

import dev.byrt.cheesehunt.Main
import dev.byrt.cheesehunt.game.GameState
import dev.byrt.cheesehunt.manager.PowerUpItem

import org.bukkit.*
import org.bukkit.block.data.*
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

import java.util.*

@Suppress("unused")
class PlayerInteractionsEvent : Listener {
    @EventHandler
    private fun onInteract(e : PlayerInteractEvent) {
        if(Main.getGame().gameManager.getGameState() != GameState.IDLE) {
            if(e.action.isLeftClick && e.player.inventory.itemInMainHand.type == Material.GOLDEN_SWORD) {
                val entityTarget = findEntityTarget(e.player, 80.0, 0.1)
                if(entityTarget != null && entityTarget is Player) {
                    val tnt = entityTarget.world.spawn(entityTarget.location, TNTPrimed::class.java)
                    tnt.source = e.player
                    tnt.fuseTicks = 0
                }
            }
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
            // Queue leave item interaction
            if(Main.getGame().gameManager.getGameState() == GameState.IDLE && e.action.isRightClick && e.player.inventory.itemInMainHand.type == Material.RED_DYE) {
                Main.getGame().queue.leaveQueue(e.player)
                e.isCancelled = true
            } else {
                e.isCancelled = !(e.player.isOp && Main.getGame().getBuildMode())
            }
        }
    }

    @EventHandler
    private fun onInteract(e : PlayerInteractEntityEvent) {
        if(Main.getGame().gameManager.getGameState() == GameState.IDLE && e.rightClicked.type == EntityType.CHICKEN) {
           if(Main.getGame().cooldownManager.attemptJoinQueue(e.player) && !Main.getGame().queue.getQueue().contains(e.player.uniqueId)) {
               if(e.rightClicked.scoreboardTags.contains("cheesehunt.queue.npc") && !Main.getGame().queue.getQueue().contains(e.player.uniqueId)) {
                   Main.getGame().queue.joinQueue(e.player)
               }
           }
        }
    }

    private fun findEntityTarget(player : Player, range : Double, raySize : Double): Entity? {
        val world  = player.world
        val eyeLocation = player.eyeLocation
        val direction = player.location.direction
        val filter = { entity : Entity -> entity != player }
        val rayTraceResult = world.rayTraceEntities(eyeLocation, direction, range, raySize, filter)
        return rayTraceResult?.hitEntity
    }
}