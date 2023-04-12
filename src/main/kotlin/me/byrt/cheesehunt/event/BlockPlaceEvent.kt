package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.state.GameState

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
        e.isCancelled = !(Main.getGame().getBuildMode() && e.player.isOp && Main.getGame().getGameState() == GameState.IDLE)
        if(e.block.type == Material.SPONGE && e.blockAgainst.type == Material.RED_WOOL || e.blockAgainst.type == Material.BLUE_WOOL && Main.getGame().getGameState() == GameState.IN_GAME) {
            Main.getGame().getCheeseManager().setPlayerHasCheese(e.player, false)
            e.player.sendMessage(Component.text("You placed a piece of cheese in your base!", NamedTextColor.GREEN))
            e.player.inventory.remove(Material.SPONGE)
            e.isCancelled = false
        }
    }
}