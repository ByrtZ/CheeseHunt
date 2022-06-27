package me.byrt.cheesehunt.command

import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission
import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.Teams
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player

@Suppress("unused")
class AutoTeam : BaseCommand {
    @CommandMethod("autoteam")
    @CommandDescription("Automatically assigns everyone online to a team")
    @CommandPermission("cheesehunt.autoteam")
    fun autoTeam(sender : Player) {
        var i = 0
        Main.getPlugin().server.onlinePlayers.shuffled().forEach {
            if (Main.getGame().getTeamManager().isSpectator(it.uniqueId)) {
                sender.sendMessage(Component.text("Skipped adding ${it.name} as they are spectating.").color(NamedTextColor.GREEN))
                return
            }

            if (i % 2 == 0) {
                Main.getGame().getTeamManager().addToTeam(it.uniqueId, Teams.BLUE)
            } else {
                Main.getGame().getTeamManager().addToTeam(it.uniqueId, Teams.RED)
            }

            sender.sendMessage(Component.text("Automatically put ${it.name} on a team.").color(NamedTextColor.GREEN))
            i++
        }
    }
}
