package dev.byrt.cheesehunt.event

import com.destroystokyo.paper.event.player.PlayerJumpEvent

import dev.byrt.cheesehunt.Main
import dev.byrt.cheesehunt.game.GameState
import dev.byrt.cheesehunt.state.Sounds
import dev.byrt.cheesehunt.state.Teams

import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

@Suppress("unused")
class PlayerMovementEvent : Listener {
    @EventHandler
    private fun onMove(e : PlayerMoveEvent) {
        when(e.from.block.getRelative(BlockFace.DOWN).type) {
            Material.YELLOW_CONCRETE_POWDER -> {
                e.player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 60, 2, false, false))
                e.player.playSound(e.player.location, Sounds.Movement.USE_SPEED_PAD, 0.35f, 1.0f)
            }
            Material.LIME_CONCRETE_POWDER -> {
                e.player.addPotionEffect(PotionEffect(PotionEffectType.JUMP, 5, 7, false, false))
            }
            else -> {}
        }

        if(Main.getGame().gameManager.getGameState() == GameState.IN_GAME || Main.getGame().gameManager.getGameState() == GameState.OVERTIME) {
            if(e.player.location.block.type == Material.STRUCTURE_VOID && e.player.gameMode != GameMode.SPECTATOR && Main.getGame().teamManager.getPlayerTeam(e.player.uniqueId) != Teams.SPECTATOR) {
                e.player.damage(0.1)
                e.player.health = 0.0
            }
            if(Main.getGame().cheeseManager.playerHasCheese(e.player)) {
                e.player.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 20, 3, false, false))
            }
        }
    }

    @EventHandler
    private fun onJump(e : PlayerJumpEvent) {
        when(e.from.block.getRelative(BlockFace.DOWN).type) {
            Material.RED_CONCRETE_POWDER -> {
                e.player.velocity = e.player.location.direction.multiply(1.15)
                e.player.velocity = Vector(e.player.velocity.x, 1.85, e.player.velocity.z)
                e.player.playSound(e.player.location, Sounds.Movement.USE_LAUNCH_PAD, 0.35f, 1.0f)
            }
            Material.ORANGE_CONCRETE_POWDER -> {
                e.player.velocity = e.player.location.direction.multiply(1.45)
                e.player.velocity = Vector(e.player.velocity.x, 2.25, e.player.velocity.z)
                e.player.playSound(e.player.location, Sounds.Movement.USE_LAUNCH_PAD, 0.35f, 1.0f)
            }
            Material.LIME_CONCRETE_POWDER -> {
                e.player.playSound(e.player.location, Sounds.Movement.USE_JUMP_PAD, 0.35f, 1.0f)
            }
            else -> {}
        }
    }
}