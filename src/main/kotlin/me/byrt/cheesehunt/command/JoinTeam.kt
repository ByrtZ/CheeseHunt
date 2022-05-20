package me.byrt.cheesehunt.command

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.Team

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.entity.Player

@Suppress("unused")
class JoinTeam : BaseCommand {
    @CommandMethod("jointeam <team> <player>")
    @CommandDescription("Puts the specified player on the specified team.")
    @CommandPermission("cheesehunt.jointeam")
    fun joinTeam(sender : Player, @Argument("team") team : Team, @Argument("player") player : Player) {
        if(Main.getGame()?.getTeamManager()?.getPlayerTeam(player.uniqueId) == Team.RED && team == Team.RED || Main.getGame()?.getTeamManager()?.getPlayerTeam(player.uniqueId) == Team.BLUE && team == Team.BLUE || Main.getGame()?.getTeamManager()?.getPlayerTeam(player.uniqueId) == Team.SPECTATOR && team == Team.SPECTATOR) {
            sender.sendMessage(Component.text("This player is already on team $team.").color(NamedTextColor.RED))
        } else {
            sender.sendMessage(Component.text("Attempting to add ${player.name} to $team...").color(NamedTextColor.GRAY))
            sender.sendMessage(Component.text("Successfully added ${player.name} to ${team}.").color(NamedTextColor.GREEN))
            Main.getGame()?.getTeamManager()?.addToTeam(player.uniqueId, team)
            Main.getGame()?.getItemManager()?.playerJoinTeamEquip(player, team)
        }
    }
}