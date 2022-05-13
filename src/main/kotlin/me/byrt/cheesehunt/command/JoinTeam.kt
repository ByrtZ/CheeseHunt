package me.byrt.cheesehunt.command

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.Team

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.entity.Player

@Suppress("unused")
class JoinTeam : BaseCommand {
    @CommandMethod("jointeam <team> <player>")
    @CommandDescription("Puts the specified player on the specified team.")
    fun joinTeam(sender : Player, @Argument("team") team : Team, @Argument("player") player : Player) {
        if(team == Team.SPECTATOR) {
            sender.sendMessage(Component.text("This team is temporarily disabled.").color(NamedTextColor.RED))
        } else {
            if(Main.getGame()?.getTeamManager()?.getPlayerTeam(player.uniqueId) == Team.RED && team == Team.RED || Main.getGame()?.getTeamManager()?.getPlayerTeam(player.uniqueId) == Team.BLUE && team == Team.BLUE) {
                sender.sendMessage(Component.text("This player is already on team $team.").color(NamedTextColor.RED))
            }
            else {
                sender.sendMessage(Component.text("Attempting to add ${player.name} to $team...").color(NamedTextColor.GRAY))
                sender.sendMessage(Component.text("[")
                    .append(Component.text("TEAM UPDATE").color(NamedTextColor.YELLOW))
                    .append(Component.text("] "))
                    .append(Component.text("${player.name} added to ${team}.").color(NamedTextColor.WHITE)))
                Main.getGame()?.getTeamManager()?.addToTeam(player.uniqueId, team)
                Main.getGame()?.getItemManager()?.playerJoinTeamEquip(player, team)
            }
        }
    }
}