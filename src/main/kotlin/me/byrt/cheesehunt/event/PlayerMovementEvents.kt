package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.state.GameState
import me.byrt.cheesehunt.state.Teams

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
}