package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main

import net.kyori.adventure.text.Component

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

@Suppress("unused")
class JoinQuitEvent : Listener {
    @EventHandler
    private fun onPlayerJoin(e : PlayerJoinEvent) {
        Main.getGame()?.getInfoBoardManager()?.showScoreboard(e.player)
        e.joinMessage(Component.text("${e.player.name} joined the game."))
    }
    @EventHandler
    private fun onPlayerQuit(e : PlayerQuitEvent) {
        Main.getGame()?.getTeamManager()?.getPlayerTeam(e.player.uniqueId)?.let {
            Main.getGame()?.getTeamManager()?.removeFromTeam(e.player.uniqueId, it)}
        e.quitMessage(Component.text("${e.player.name} left the game."))
    }
}