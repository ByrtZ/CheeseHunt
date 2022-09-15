package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.GameState
import me.byrt.cheesehunt.manager.Teams

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

@Suppress("unused")
class JoinQuitEvent : Listener {
    private val header = Component.text("Cheese Hunt").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true).append(Component.text("\nAn MCC Tester classic meme, brought to you by ").color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false).append(Component.text("Byrt").color(NamedTextColor.RED)).append(Component.text(".").color(NamedTextColor.GRAY).decoration(TextDecoration.BOLD, false)))
    private val footer = Component.text("'When life gives you cheese, you make a minigame with it'").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, true)

    @EventHandler
    private fun onPlayerJoin(e : PlayerJoinEvent) {
        Main.getGame().getInfoBoardManager().showScoreboard(e.player)
        e.joinMessage(Component.text("${e.player.name} joined the game.").color(TextColor.fromHexString("#ffff00")))
        e.player.teleport(Location(e.player.world, 0.5, -52.0 ,0.5, 0.0f, 0.0f))
        e.player.inventory.clear()
        e.player.sendPlayerListHeaderAndFooter(header, footer)

        Main.getGame().getTeamManager().addToTeam(e.player.uniqueId, Teams.SPECTATOR)
        Main.getGame().getItemManager().playerJoinTeamEquip(e.player, Teams.SPECTATOR)
        if(e.player.isOp) {
            Main.getGame().getTeamManager().addToAdminDisplay(e.player.uniqueId)
        }
        if(Main.getGame().getGameState() == GameState.IDLE) {
            e.player.gameMode = GameMode.ADVENTURE
        }
        if(Main.getGame().getGameState() != GameState.IDLE) {
            e.player.gameMode = GameMode.SPECTATOR
        }
    }
    @EventHandler
    private fun onPlayerQuit(e : PlayerQuitEvent) {
        Main.getGame().getTeamManager().getPlayerTeam(e.player.uniqueId).let { Main.getGame().getTeamManager().removeFromTeam(e.player.uniqueId, it)}
        e.quitMessage(Component.text("${e.player.name} left the game.").color(TextColor.fromHexString("#ffff00")))
    }
}