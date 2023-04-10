package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.state.GameState

import org.bukkit.Material
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

@Suppress("unused")
class CheeseInteractEvent : Listener {
    private fun playerInteract(e : PlayerInteractEvent) {
        if(Main.getGame().getGameState() == GameState.IN_GAME) {
            if(e.clickedBlock?.type == Material.SPONGE && e.player.itemInHand.type == Material.WOODEN_PICKAXE) {
                e.player.sendMessage("adding mining fatigue")
                e.player.addPotionEffect(PotionEffect(PotionEffectType.SLOW_DIGGING, 2147483646, 1))
            }
        }
    }
}