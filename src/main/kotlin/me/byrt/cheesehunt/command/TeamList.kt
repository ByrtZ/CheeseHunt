package me.byrt.cheesehunt.command

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.Teams

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.entity.Player

@Suppress("unused")
class TeamList : BaseCommand {
    @CommandMethod("teamlist <team>")
    @CommandDescription("Allows the executing player to see the array of the specified team.")
    @CommandPermission("cheesehunt.teamlist")
    fun teamList(sender : Player, @Argument("team") team : Teams) {
        when(team) {
            Teams.RED -> {
                sender.sendMessage(Component.text("DISPLAYING $team TEAM UUIDS:").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true))
                sender.sendMessage(Component.text("${Main.getGame().getTeamManager().getRedTeam()}"))
            }
            Teams.BLUE -> {
                sender.sendMessage(Component.text("DISPLAYING $team TEAM UUIDS:").color(NamedTextColor.BLUE).decoration(TextDecoration.BOLD, true))
                sender.sendMessage(Component.text("${Main.getGame().getTeamManager().getBlueTeam()}"))
            }
            Teams.SPECTATOR -> {
                sender.sendMessage(Component.text("DISPLAYING $team TEAM UUIDS:").decoration(TextDecoration.BOLD, true))
                sender.sendMessage(Component.text("${Main.getGame().getTeamManager().getSpectators()}"))
            }
        }
    }
}