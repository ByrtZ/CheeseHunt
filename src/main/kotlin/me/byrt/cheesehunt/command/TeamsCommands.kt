package me.byrt.cheesehunt.command

import cloud.commandframework.annotations.*

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.game.GameState
import me.byrt.cheesehunt.state.*
import me.byrt.cheesehunt.state.Sounds
import me.byrt.cheesehunt.util.DevStatus

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
        if(Main.getGame().gameManager.getGameState() == GameState.IDLE) {
            if(Main.getGame().teamManager.getPlayerTeam(player.uniqueId) == Teams.RED && team == Teams.RED || Main.getGame().teamManager.getPlayerTeam(player.uniqueId) == Teams.BLUE && team == Teams.BLUE || Main.getGame().teamManager.getPlayerTeam(player.uniqueId) == Teams.SPECTATOR && team == Teams.SPECTATOR) {
                sender.sendMessage(Component.text("This player is already on ${team.toString().lowercase()} team.").color(NamedTextColor.RED))
            } else {
                Main.getGame().teamManager.addToTeam(player, player.uniqueId, team)
                Main.getGame().dev.parseDevMessage("Team Update: ${player.name} is now on ${team.toString().lowercase()} team.", DevStatus.INFO)
            }
        } else {
            sender.sendMessage(Component.text("Unable to modify teams in this state.", NamedTextColor.RED))
        }
    }

    @CommandMethod("teams shuffle")
    @CommandDescription("Automatically assigns everyone online to a team.")
    @CommandPermission("cheesehunt.autoteam")
    fun autoTeam(sender : Player, @Flag("ignoreAdmins") doesIgnoreAdmins: Boolean) {
        if(Main.getGame().gameManager.getGameState() == GameState.IDLE) {
            shuffleStartDisplay(sender)
            if(!doesIgnoreAdmins) {
                shuffle(Main.getPlugin().server.onlinePlayers)
                Main.getGame().dev.parseDevMessage("All online players successfully split into teams by ${sender.name}.", DevStatus.INFO)
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
                        Main.getGame().dev.parseDevMessage("All online non-admin players successfully split into teams by ${sender.name}.", DevStatus.INFO)
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
            Main.getGame().teamManager.removeFromTeam(it, it.uniqueId, Main.getGame().teamManager.getPlayerTeam(it.uniqueId))
            if (i % 2 == 0) {
                Main.getGame().teamManager.addToTeam(it, it.uniqueId, Teams.RED)
            } else {
                Main.getGame().teamManager.addToTeam(it, it.uniqueId, Teams.BLUE)
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
                sender.sendMessage(Component.text("${Main.getGame().teamManager.getRedTeam()}"))
            }
            TeamsListOptions.BLUE -> {
                sender.sendMessage(Component.text("DISPLAYING BLUE TEAM UUIDS:").color(NamedTextColor.BLUE).decoration(TextDecoration.BOLD, true))
                sender.sendMessage(Component.text("${Main.getGame().teamManager.getBlueTeam()}"))
            }
            TeamsListOptions.SPECTATOR -> {
                sender.sendMessage(Component.text("DISPLAYING SPECTATOR TEAM UUIDS:").decoration(TextDecoration.BOLD, true))
                sender.sendMessage(Component.text("${Main.getGame().teamManager.getSpectators()}"))
            }
            TeamsListOptions.ALL -> {
                sender.sendMessage(Component.text("\nDISPLAYING RED TEAM UUIDS:").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true))
                sender.sendMessage(Component.text("${Main.getGame().teamManager.getRedTeam()}\n"))
                sender.sendMessage(Component.text("\nDISPLAYING BLUE TEAM UUIDS:").color(NamedTextColor.BLUE).decoration(TextDecoration.BOLD, true))
                sender.sendMessage(Component.text("${Main.getGame().teamManager.getBlueTeam()}\n"))
                sender.sendMessage(Component.text("\nDISPLAYING SPECTATOR TEAM UUIDS:").decoration(TextDecoration.BOLD, true))
                sender.sendMessage(Component.text("${Main.getGame().teamManager.getSpectators()}\n"))
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