package me.byrt.cheesehunt.command

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.Team

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.entity.Player

@Suppress("unused")
class TeamList : BaseCommand {
    @CommandMethod("teamlist <team>")
    @CommandDescription("[TEMPORARY] Allows the executing player to see the array of the specified team")
    fun teamList(sender : Player, @Argument("team") team : Team) {
        when(team) {
            Team.RED -> {
                sender.sendMessage(Component.text("DISPLAYING $team TEAM UUIDS:").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true))
                sender.sendMessage(Component.text("${Main.getGame()?.getTeamManager()?.getRedTeam()}"))
            }
            Team.BLUE -> {
                sender.sendMessage(Component.text("DISPLAYING $team TEAM UUIDS:").color(NamedTextColor.BLUE).decoration(TextDecoration.BOLD, true))
                sender.sendMessage(Component.text("${Main.getGame()?.getTeamManager()?.getBlueTeam()}"))
            }
            Team.SPECTATOR -> {
                sender.sendMessage(Component.text("This team is temporarily disabled.").color(NamedTextColor.RED))
                /*sender.sendMessage(Component.text("DISPLAYING TEAM $team").color(NamedTextColor.GRAY).decoration(TextDecoration.BOLD, true))
                sender.sendMessage(Component.text("${Main.getGame()?.getTeamManager()?.getSpectators()}"))*/
            }
        }
    }
}