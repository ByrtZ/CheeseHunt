package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.state.GameState

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

@Suppress("unused")
class BreakingCheeseEvent : Listener {
    @EventHandler
    private fun onClickCheese(e : PlayerInteractEvent) {
        if(Main.getGame().getGameState() == GameState.IN_GAME) {
            if(e.action == Action.LEFT_CLICK_BLOCK && e.clickedBlock?.type == Material.SPONGE && e.player.inventory.itemInMainHand.type == Material.WOODEN_PICKAXE) {
                e.player.addPotionEffect(PotionEffect(PotionEffectType.SLOW_DIGGING, Int.MAX_VALUE, 0, false, false))
            }
        }
    }
}