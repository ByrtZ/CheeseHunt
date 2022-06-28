package me.byrt.cheesehunt.command

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.Teams

import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.entity.Player

@Suppress("unused")
class AutoTeam : BaseCommand {
    @CommandMethod("autoteam")
    @CommandDescription("Automatically assigns everyone online to a team.")
    @CommandPermission("cheesehunt.autoteam")
    fun autoTeam(sender : Player) {
        var i = 0
        Main.getPlugin().server.onlinePlayers.shuffled().forEach {
            Main.getGame().getTeamManager().removeFromTeam(it.uniqueId, Main.getGame().getTeamManager().getPlayerTeam(it.uniqueId))
            if (i % 2 == 0) {
                Main.getGame().getTeamManager().addToTeam(it.uniqueId, Teams.RED)
            } else {
                Main.getGame().getTeamManager().addToTeam(it.uniqueId, Teams.BLUE)
            }
            i++
        }
        sender.sendMessage(Component.text("Successfully split all online players into teams.").color(NamedTextColor.GREEN))
    }
}
