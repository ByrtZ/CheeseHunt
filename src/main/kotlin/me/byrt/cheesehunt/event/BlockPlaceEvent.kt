package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.game.GameState

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

@Suppress("unused")
class BlockPlaceEvent : Listener {
    @EventHandler
    private fun onBlockPlace(e : BlockPlaceEvent) {
        e.isCancelled = !(Main.getGame().getBuildMode() && e.player.isOp && Main.getGame().gameManager.getGameState() == GameState.IDLE)

        if(e.block.type == Material.SPONGE && (e.blockAgainst.type == Material.RED_WOOL || e.blockAgainst.type == Material.BLUE_WOOL) && (Main.getGame().gameManager.getGameState() == GameState.IN_GAME || Main.getGame().gameManager.getGameState() == GameState.OVERTIME)) {
            Main.getGame().cheeseManager.setPlayerHasCheese(e.player, false)
            e.player.sendMessage(Component.text("You placed a piece of cheese in your base!", NamedTextColor.GREEN))
            e.player.inventory.remove(Material.SPONGE)

            if(Main.getGame().gameManager.getGameState() == GameState.OVERTIME) {
                Main.getGame().cheeseManager.countCheeseInBases(true)
            }
            e.isCancelled = false
        }
    }
}