package me.byrt.cheesehunt.command

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.Teams

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.entity.Player
import java.util.*

@Suppress("unused")
class SetTeam : BaseCommand {
    @CommandMethod("setteam <team> <player>")
    @CommandDescription("Puts the specified player on the specified team.")
    @CommandPermission("cheesehunt.jointeam")
    fun setTeam(sender : Player, @Argument("team") team : Teams, @Argument("player") player : Player) {
        if(Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.RED && team == Teams.RED || Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.BLUE && team == Teams.BLUE || Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.SPECTATOR && team == Teams.SPECTATOR) {
            sender.sendMessage(Component.text("This player is already on team ${team.toString().lowercase()} team.").color(NamedTextColor.RED))
        } else {
            sender.sendMessage(Component.text("Attempting to add ${player.name} to ${team.toString().lowercase()} team...").color(NamedTextColor.GRAY))
            sender.sendMessage(Component.text("Successfully added ${player.name} to ${team.toString().lowercase()} team.").color(NamedTextColor.GREEN))
            Main.getGame().getTeamManager().addToTeam(player, player.uniqueId, team)
        }
    }
}