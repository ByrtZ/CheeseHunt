package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.state.GameState
import me.byrt.cheesehunt.task.Music
import me.byrt.cheesehunt.state.Teams

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
        Main.getGame().infoBoardManager.showScoreboard()
        e.player.teleport(Location(Bukkit.getWorld("Cheese"), 0.5, -52.0 ,0.5, 0.0f, 0.0f))
        e.joinMessage(Component.text("${e.player.name} joined the game.").color(TextColor.fromHexString("#ffff00")))

        e.player.inventory.clear()
        e.player.sendPlayerListHeaderAndFooter(Main.getGame().tabListManager.getTabHeader(), Main.getGame().tabListManager.getTabFooter())

        Main.getGame().teamManager.addToTeam(e.player, e.player.uniqueId, Teams.SPECTATOR)

        if(Main.getGame().gameManager.getGameState() == GameState.IDLE) {
            e.player.gameMode = GameMode.ADVENTURE
        } else {
            e.player.gameMode = GameMode.SPECTATOR
        }
    }

    @EventHandler
    private fun onPlayerQuit(e : PlayerQuitEvent) {
        Main.getGame().teamManager.getPlayerTeam(e.player.uniqueId).let { Main.getGame().teamManager.removeFromTeam(e.player, e.player.uniqueId, it)}
        Main.getGame().musicTask.stopMusicLoop(e.player, Music.NULL)
        Main.getGame().respawnTask.stopRespawnLoop(e.player)
        if(Main.getGame().cheeseManager.playerHasCheese(e.player)) {
            Main.getGame().cheeseManager.setPlayerHasCheese(e.player, false)
        }
        e.quitMessage(Component.text("${e.player.name} left the game.").color(TextColor.fromHexString("#ffff00")))
    }
}