package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.GameState
import me.byrt.cheesehunt.manager.Teams

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
        if(Main.getGame().getMusicLooper()) {
            Main.getGame().getInfoBoardManager().hideScoreboard()
            e.player.teleport(Location(Bukkit.getWorld("Cheese"), -99.5, -21.0, -110.5, 0.0f, 0.0f))
            e.joinMessage(null)
        } else {
            Main.getGame().getInfoBoardManager().showScoreboard()
            e.player.teleport(Location(Bukkit.getWorld("Cheese"), 0.5, -52.0 ,0.5, 0.0f, 0.0f))
            e.joinMessage(Component.text("${e.player.name} joined the game.").color(TextColor.fromHexString("#ffff00")))
        }
        e.player.inventory.clear()
        e.player.sendPlayerListHeaderAndFooter(Main.getGame().getTabListManager().getTabHeader(), Main.getGame().getTabListManager().getTabFooter())

        Main.getGame().getTeamManager().addToTeam(e.player, e.player.uniqueId, Teams.SPECTATOR)

        if(e.player.isOp) {
            Main.getGame().getTeamManager().addToAdminDisplay(e.player.uniqueId)
        }
        if(Main.getGame().getGameState() == GameState.IDLE) {
            e.player.gameMode = GameMode.ADVENTURE
        } else {
            e.player.gameMode = GameMode.SPECTATOR
        }
    }

    @EventHandler
    private fun onPlayerQuit(e : PlayerQuitEvent) {
        Main.getGame().getTeamManager().getPlayerTeam(e.player.uniqueId).let { Main.getGame().getTeamManager().removeFromTeam(e.player, e.player.uniqueId, it)}
        if(Main.getGame().getMusicLooper()) {
            Main.getGame().getMusicLoop().removeFromMusicLoop(e.player)
            e.quitMessage(null)
        } else {
            e.quitMessage(Component.text("${e.player.name} left the game.").color(TextColor.fromHexString("#ffff00")))
        }
    }
}