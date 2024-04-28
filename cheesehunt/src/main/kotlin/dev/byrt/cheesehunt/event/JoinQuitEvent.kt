package dev.byrt.cheesehunt.event

import dev.byrt.cheesehunt.CheeseHunt
import dev.byrt.cheesehunt.game.GameState
import dev.byrt.cheesehunt.state.Teams
import dev.byrt.cheesehunt.task.Music

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

@Suppress("unused")
class JoinQuitEvent : Listener {
    @EventHandler
    private fun onPlayerJoin(e : PlayerJoinEvent) {
        e.joinMessage(Component.text("${e.player.name} joined the game.").color(TextColor.fromHexString("#ffff00")))
        e.player.teleport(Location(Bukkit.getWorld("Cheese"), 0.5, -52.0 ,0.5, 0.0f, 0.0f))
        e.player.inventory.clear()
        CheeseHunt.getGame().tabListManager.updateAllTabList()
        if(CheeseHunt.getGame().gameManager.getGameState() == GameState.IDLE) {
            e.player.gameMode = GameMode.ADVENTURE
        } else {
            e.player.gameMode = GameMode.SPECTATOR
        }
    }

    @EventHandler
    private fun onPlayerQuit(e : PlayerQuitEvent) {
        CheeseHunt.getGame().musicTask.stopMusicLoop(e.player, Music.NULL)
        CheeseHunt.getGame().respawnTask.stopRespawnLoop(e.player)
        CheeseHunt.getGame().tabListManager.updateAllTabList()
        if(CheeseHunt.getGame().cheeseManager.playerHasCheese(e.player)) {
            CheeseHunt.getGame().cheeseManager.setPlayerHasCheese(e.player, false)
        }
        if(CheeseHunt.getGame().queue.getQueue().contains(e.player.uniqueId)) {
            CheeseHunt.getGame().queue.leaveQueue(e.player)
        }
        e.quitMessage(Component.text("${e.player.name} left the game.").color(TextColor.fromHexString("#ffff00")))
    }
}
