package me.byrt.cheesehunt.command

import cloud.commandframework.annotations.*

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.state.*
import me.byrt.cheesehunt.state.Sounds

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title

import org.bukkit.Bukkit
import org.bukkit.entity.Player

import java.time.Duration
import java.util.*

@Suppress("unused")
class TeamsCommands : BaseCommand {
    private val shuffleStartSound: Sound = Sound.sound(Key.key(Sounds.Command.SHUFFLE_START), Sound.Source.MASTER, 1f, 1f)
    private val shuffleCompleteSound: Sound = Sound.sound(Key.key(Sounds.Command.SHUFFLE_COMPLETE), Sound.Source.MASTER, 1f, 2f)
    private val shuffleFailSound: Sound = Sound.sound(Key.key(Sounds.Command.SHUFFLE_FAIL), Sound.Source.MASTER, 1f, 0f)
    @CommandMethod("teams set <player> <team>")
    @CommandDescription("Puts the specified player on the specified team.")
    @CommandPermission("cheesehunt.jointeam")
    fun setTeam(sender : Player, @Argument("player") player : Player, @Argument("team") team : Teams) {
        if(Main.getGame().getGameState() == GameState.IDLE) {
            if(Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.RED && team == Teams.RED || Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.BLUE && team == Teams.BLUE || Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.SPECTATOR && team == Teams.SPECTATOR) {
                sender.sendMessage(Component.text("This player is already on ${team.toString().lowercase()} team.").color(NamedTextColor.RED))
            } else {
                sender.sendMessage(Component.text("Attempting to add ${player.name} to ${team.toString().lowercase()} team...").color(NamedTextColor.GRAY))
                sender.sendMessage(Component.text("Successfully added ${player.name} to ${team.toString().lowercase()} team.").color(NamedTextColor.GREEN))
                Main.getGame().getTeamManager().addToTeam(player, player.uniqueId, team)
            }
        } else {
            sender.sendMessage(Component.text("Unable to modify teams in this state.", NamedTextColor.RED))
        }
    }

    @CommandMethod("teams shuffle")
    @CommandDescription("Automatically assigns everyone online to a team.")
    @CommandPermission("cheesehunt.autoteam")
    fun autoTeam(sender : Player, @Flag("ignoreAdmins") doesIgnoreAdmins: Boolean) {
        if(Main.getGame().getGameState() == GameState.IDLE) {
            shuffleStartDisplay(sender)
            if(!doesIgnoreAdmins) {
                shuffle(Main.getPlugin().server.onlinePlayers)
                sender.sendMessage(Component.text("Successfully split all online players into teams.").color(NamedTextColor.GREEN))
                shuffleCompleteDisplay(sender)
            } else {
                try {
                    val nonAdmins = mutableListOf<Player>()
                    for(player in Bukkit.getOnlinePlayers()) {
                        if(!player.isOp) {
                            nonAdmins.add(player)
                        }
                    }
                    if(nonAdmins.isEmpty()) {
                        sender.sendMessage(Component.text("Unable to shuffle teams due to no non-admin players online.").color(NamedTextColor.RED))
                        shuffleFailDisplay(sender)
                    } else {
                        shuffle(nonAdmins)
                        sender.sendMessage(Component.text("Successfully split all online non-admin players into teams.").color(NamedTextColor.GREEN))
                        shuffleCompleteDisplay(sender)
                    }
                } catch(e : Exception) {
                    sender.sendMessage(Component.text("Unable to shuffle teams as an error occurred.").color(NamedTextColor.RED))
                    shuffleFailDisplay(sender)
                }
            }
        } else {
            sender.sendMessage(Component.text("Unable to modify teams in this state.", NamedTextColor.RED))
        }
    }

    private fun shuffle(players : Collection<Player>) {
        var i = 0
        players.shuffled().forEach {
            Main.getGame().getTeamManager().removeFromTeam(it, it.uniqueId, Main.getGame().getTeamManager().getPlayerTeam(it.uniqueId))
            if (i % 2 == 0) {
                Main.getGame().getTeamManager().addToTeam(it, it.uniqueId, Teams.RED)
            } else {
                Main.getGame().getTeamManager().addToTeam(it, it.uniqueId, Teams.BLUE)
            }
            i++
        }
    }

    private fun shuffleStartDisplay(player : Player) {
        player.showTitle(Title.title(Component.text(""), Component.text("Shuffling teams...").color(NamedTextColor.RED), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(3), Duration.ofSeconds(1))))
        player.playSound(shuffleStartSound)
    }

    private fun shuffleCompleteDisplay(player : Player) {
        player.showTitle(Title.title(Component.text(""), Component.text("Teams shuffled randomly!").color(NamedTextColor.GREEN), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(1))))
        player.playSound(shuffleCompleteSound)
    }

    private fun shuffleFailDisplay(player : Player) {
        player.showTitle(Title.title(Component.text(""), Component.text("Team shuffling failed.").color(NamedTextColor.RED), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(1))))
        player.playSound(shuffleFailSound)
    }

    @CommandMethod("teams list <option>")
    @CommandDescription("Allows the executing player to see the array of the specified team.")
    @CommandPermission("cheesehunt.teamlist")
    fun teamList(sender : Player, @Argument("option") option : TeamsListOptions) {
        when(option) {
            TeamsListOptions.RED -> {
                sender.sendMessage(Component.text("DISPLAYING RED TEAM UUIDS:").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true))
                sender.sendMessage(Component.text("${Main.getGame().getTeamManager().getRedTeam()}"))
            }
            TeamsListOptions.BLUE -> {
                sender.sendMessage(Component.text("DISPLAYING BLUE TEAM UUIDS:").color(NamedTextColor.BLUE).decoration(TextDecoration.BOLD, true))
                sender.sendMessage(Component.text("${Main.getGame().getTeamManager().getBlueTeam()}"))
            }
            TeamsListOptions.SPECTATOR -> {
                sender.sendMessage(Component.text("DISPLAYING SPECTATOR TEAM UUIDS:").decoration(TextDecoration.BOLD, true))
                sender.sendMessage(Component.text("${Main.getGame().getTeamManager().getSpectators()}"))
            }
            TeamsListOptions.ALL -> {
                sender.sendMessage(Component.text("\nDISPLAYING RED TEAM UUIDS:").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true))
                sender.sendMessage(Component.text("${Main.getGame().getTeamManager().getRedTeam()}\n"))
                sender.sendMessage(Component.text("\nDISPLAYING BLUE TEAM UUIDS:").color(NamedTextColor.BLUE).decoration(TextDecoration.BOLD, true))
                sender.sendMessage(Component.text("${Main.getGame().getTeamManager().getBlueTeam()}\n"))
                sender.sendMessage(Component.text("\nDISPLAYING SPECTATOR TEAM UUIDS:").decoration(TextDecoration.BOLD, true))
                sender.sendMessage(Component.text("${Main.getGame().getTeamManager().getSpectators()}\n"))
            }
        }
    }
}

enum class TeamsListOptions {
    SPECTATOR,
    RED,
    BLUE,
    ALL
}