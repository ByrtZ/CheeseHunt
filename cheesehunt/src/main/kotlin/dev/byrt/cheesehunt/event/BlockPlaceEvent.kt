package dev.byrt.cheesehunt.event

import dev.byrt.cheesehunt.CheeseHunt
import dev.byrt.cheesehunt.game.GameState

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
        e.isCancelled = !(CheeseHunt.getGame().getBuildMode() && e.player.isOp && CheeseHunt.getGame().gameManager.getGameState() == GameState.IDLE)
        if(e.block.type == Material.SPONGE && (e.blockAgainst.type == Material.RED_WOOL || e.blockAgainst.type == Material.BLUE_WOOL) && (CheeseHunt.getGame().gameManager.getGameState() == GameState.IN_GAME || CheeseHunt.getGame().gameManager.getGameState() == GameState.OVERTIME)) {
            CheeseHunt.getGame().cheeseManager.setPlayerHasCheese(e.player, false)
            e.player.sendMessage(Component.text("[")
                .append(Component.text("▶").color(NamedTextColor.YELLOW))
                .append(Component.text("] "))
                .append(Component.text("You placed a piece of cheese in your base!", NamedTextColor.GREEN)))
            e.player.inventory.remove(Material.SPONGE)

            if(CheeseHunt.getGame().gameManager.getGameState() == GameState.OVERTIME) {
                CheeseHunt.getGame().cheeseManager.countCheeseInBases(true)
            }
            e.isCancelled = false
        }
    }
}
