package me.lucyydotp.cheeselib.command

import cloud.commandframework.annotations.*
import cloud.commandframework.annotations.suggestions.Suggestions
import cloud.commandframework.context.CommandContext
import me.lucyydotp.cheeselib.game.TeamManager
import me.lucyydotp.cheeselib.game.TeamMeta
import me.lucyydotp.cheeselib.inject.context
import me.lucyydotp.cheeselib.module.Module
import me.lucyydotp.cheeselib.module.ModuleHolder
import me.lucyydotp.cheeselib.module.installCommands
import net.kyori.adventure.extra.kotlin.text
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Suppress("unused")
class TeamsCommands<T>(parent: ModuleHolder) : Module(parent) where T : TeamMeta, T : Enum<T> {

//    private val shuffleStartSound: Sound = Sound.sound(Key.key(Sounds.Command.SHUFFLE_START), Sound.Source.MASTER, 1f, 1f)
//    private val shuffleCompleteSound: Sound = Sound.sound(Key.key(Sounds.Command.SHUFFLE_COMPLETE), Sound.Source.MASTER, 1f, 2f)
//    private val shuffleFailSound: Sound = Sound.sound(Key.key(Sounds.Command.SHUFFLE_FAIL), Sound.Source.MASTER, 1f, 0f)

    init {
        installCommands()
    }

    private val teamManager: TeamManager<T> by context()

    @Suggestions("teams")
    fun suggestTeams(context: CommandContext<CommandSender>, input: String)= teamManager.teams.map(Enum<*>::name)

    // TODO(lucy): check game state
    private val canAlterTeams = true

    @CommandMethod("teams set <player> <team>")
    @CommandDescription("Puts the specified player on the specified team.")
    @CommandPermission("cheesehunt.jointeam")
    fun <T> setTeam(
        sender: Player,
        @Argument("player") player: Player,
        @Argument("team", suggestions = "teams") teamName: String,
    ) {
        if (!canAlterTeams) {
            sender.sendMessage(Component.text("Unable to modify teams in this state.", NamedTextColor.RED))
            return
        }

        val team = teamManager.teams.first { it.name.lowercase() == teamName.lowercase() }
        val currentTeam = teamManager.getTeam(player)

        if (team == currentTeam) {
            sender.sendMessage(
                text {
                    color(NamedTextColor.RED)
                    append(Component.text("This player is already on "))
                    append(team.displayName)
                    append(Component.text("."))
                }
            )
            return
        }

        teamManager.setTeam(player, team)

//        CheeseHunt.getGame().dev.parseDevMessage(
//            "Team Update: ${player.name} is now on ${
//                team.toString().lowercase()
//            } team.", DevStatus.INFO
//        )
    }

    @CommandMethod("teams shuffle")
    @CommandDescription("Automatically assigns everyone online to a team.")
    @CommandPermission("cheesehunt.autoteam")
    fun autoTeam(sender: Player, @Flag("ignoreAdmins") doesIgnoreAdmins: Boolean) {
        TODO()
//        if (CheeseHunt.getGame().gameManager.getGameState() == GameState.IDLE) {
//            shuffleStartDisplay(sender)
//            if (!doesIgnoreAdmins) {
//                shuffle(CheeseHunt.getPlugin().server.onlinePlayers)
//                CheeseHunt.getGame().dev.parseDevMessage(
//                    "All online players successfully split into teams by ${sender.name}.",
//                    DevStatus.INFO
//                )
//                shuffleCompleteDisplay(sender)
//            } else {
//                try {
//                    val nonAdmins = mutableListOf<Player>()
//                    for (player in Bukkit.getOnlinePlayers()) {
//                        if (!player.isOp) {
//                            nonAdmins.add(player)
//                        }
//                    }
//                    if (nonAdmins.isEmpty()) {
//                        sender.sendMessage(
//                            Component.text("Unable to shuffle teams due to no non-admin players online.")
//                                .color(NamedTextColor.RED)
//                        )
//                        shuffleFailDisplay(sender)
//                    } else {
//                        shuffle(nonAdmins)
//                        CheeseHunt.getGame().dev.parseDevMessage(
//                            "All online non-admin players successfully split into teams by ${sender.name}.",
//                            DevStatus.INFO
//                        )
//                        shuffleCompleteDisplay(sender)
//                    }
//                } catch (e: Exception) {
//                    sender.sendMessage(
//                        Component.text("Unable to shuffle teams as an error occurred.").color(NamedTextColor.RED)
//                    )
//                    shuffleFailDisplay(sender)
//                }
//            }
//        } else {
//            sender.sendMessage(Component.text("Unable to modify teams in this state.", NamedTextColor.RED))
//        }
    }
//
//    private fun shuffle(players: Collection<Player>) {
//        var i = 0
//        players.shuffled().forEach {
//            CheeseHunt.getGame().teamManager.removeFromTeam(
//                it,
//                it.uniqueId,
//                CheeseHunt.getGame().teamManager.getPlayerTeam(it.uniqueId)
//            )
//            if (i % 2 == 0) {
//                CheeseHunt.getGame().teamManager.addToTeam(it, it.uniqueId, Teams.RED)
//            } else {
//                CheeseHunt.getGame().teamManager.addToTeam(it, it.uniqueId, Teams.BLUE)
//            }
//            i++
//        }
//    }
//
//    private fun shuffleStartDisplay(player: Player) {
//        player.showTitle(
//            Title.title(
//                Component.text(""),
//                Component.text("Shuffling teams...").color(NamedTextColor.RED),
//                Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(3), Duration.ofSeconds(1))
//            )
//        )
//        player.playSound(shuffleStartSound)
//    }
//
//    private fun shuffleCompleteDisplay(player: Player) {
//        player.showTitle(
//            Title.title(
//                Component.text(""),
//                Component.text("Teams shuffled randomly!").color(NamedTextColor.GREEN),
//                Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(1))
//            )
//        )
//        player.playSound(shuffleCompleteSound)
//    }
//
//    private fun shuffleFailDisplay(player: Player) {
//        player.showTitle(
//            Title.title(
//                Component.text(""),
//                Component.text("Team shuffling failed.").color(NamedTextColor.RED),
//                Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(1))
//            )
//        )
//        player.playSound(shuffleFailSound)
//    }

    @CommandMethod("teams list <option>")
    @CommandDescription("Allows the executing player to see the array of the specified team.")
    @CommandPermission("cheesehunt.teamlist")
    fun teamList(sender: Player, @Argument("option") option: TeamsListOptions) {
        TODO()
//        when (option) {
//            TeamsListOptions.RED -> {
//                sender.sendMessage(
//                    Component.text("DISPLAYING RED TEAM UUIDS:").color(NamedTextColor.RED)
//                        .decoration(TextDecoration.BOLD, true)
//                )
//                sender.sendMessage(Component.text("${CheeseHunt.getGame().teamManager.getRedTeam()}"))
//            }
//
//            TeamsListOptions.BLUE -> {
//                sender.sendMessage(
//                    Component.text("DISPLAYING BLUE TEAM UUIDS:").color(NamedTextColor.BLUE)
//                        .decoration(TextDecoration.BOLD, true)
//                )
//                sender.sendMessage(Component.text("${CheeseHunt.getGame().teamManager.getBlueTeam()}"))
//            }
//
//            TeamsListOptions.SPECTATOR -> {
//                sender.sendMessage(
//                    Component.text("DISPLAYING SPECTATOR TEAM UUIDS:").decoration(TextDecoration.BOLD, true)
//                )
//                sender.sendMessage(Component.text("${CheeseHunt.getGame().teamManager.getSpectators()}"))
//            }
//
//            TeamsListOptions.ALL -> {
//                sender.sendMessage(
//                    Component.text("\nDISPLAYING RED TEAM UUIDS:").color(NamedTextColor.RED)
//                        .decoration(TextDecoration.BOLD, true)
//                )
//                sender.sendMessage(Component.text("${CheeseHunt.getGame().teamManager.getRedTeam()}\n"))
//                sender.sendMessage(
//                    Component.text("\nDISPLAYING BLUE TEAM UUIDS:").color(NamedTextColor.BLUE)
//                        .decoration(TextDecoration.BOLD, true)
//                )
//                sender.sendMessage(Component.text("${CheeseHunt.getGame().teamManager.getBlueTeam()}\n"))
//                sender.sendMessage(
//                    Component.text("\nDISPLAYING SPECTATOR TEAM UUIDS:").decoration(TextDecoration.BOLD, true)
//                )
//                sender.sendMessage(Component.text("${CheeseHunt.getGame().teamManager.getSpectators()}\n"))
//            }
//        }
    }
}

enum class TeamsListOptions {
    SPECTATOR,
    RED,
    BLUE,
    ALL
}
