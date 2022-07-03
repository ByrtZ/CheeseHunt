package me.byrt.cheesehunt.command

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.Teams

import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.entity.Player

@Suppress("unused")
class AllTeamsList : BaseCommand {
    @CommandMethod("allteamslist")
    @CommandDescription("Allows the executing player to see the array of all teams.")
    @CommandPermission("cheesehunt.allteamslist")
    fun teamList(sender : Player) {
        sender.sendMessage(Component.text("\nDISPLAYING ${Teams.RED} TEAM UUIDS:").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true))
        sender.sendMessage(Component.text("${Main.getGame().getTeamManager().getRedTeam()}\n"))
        sender.sendMessage(Component.text("\nDISPLAYING ${Teams.BLUE} TEAM UUIDS:").color(NamedTextColor.BLUE).decoration(TextDecoration.BOLD, true))
        sender.sendMessage(Component.text("${Main.getGame().getTeamManager().getBlueTeam()}\n"))
        sender.sendMessage(Component.text("\nDISPLAYING ${Teams.SPECTATOR} TEAM UUIDS:").decoration(TextDecoration.BOLD, true))
        sender.sendMessage(Component.text("${Main.getGame().getTeamManager().getSpectators()}\n"))
    }
}