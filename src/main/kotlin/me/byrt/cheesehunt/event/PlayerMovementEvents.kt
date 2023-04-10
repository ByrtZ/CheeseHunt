package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.state.GameState

import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

@Suppress("unused")
class PlayerMovementEvents : Listener {
    @EventHandler
    private fun onLeaveMap(e : PlayerMoveEvent) {
        if(Main.getGame().getGameState() == GameState.IN_GAME) {
            if(e.player.location.block.type == Material.STRUCTURE_VOID && e.player.gameMode != GameMode.SPECTATOR) {
                e.player.damage(20.0)
            }
            if(Main.getGame().getCheeseManager().playerHasCheese(e.player)) {
                e.player.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 10, 3))
            }
        }
    }
}