package me.lucyydotp.cheeselib.command

import cloud.commandframework.annotations.*
import cloud.commandframework.annotations.suggestions.Suggestions
import cloud.commandframework.context.CommandContext
import me.lucyydotp.cheeselib.inject.context
import me.lucyydotp.cheeselib.module.Module
import me.lucyydotp.cheeselib.module.ModuleHolder
import me.lucyydotp.cheeselib.module.installCommands
import me.lucyydotp.cheeselib.sys.TeamManager
import me.lucyydotp.cheeselib.sys.TeamMeta
import net.kyori.adventure.extra.kotlin.text
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandMethod("teams")
class TeamsCommands<T>(parent: ModuleHolder) : Module(parent) where T : TeamMeta, T : Enum<T> {

//    private val shuffleStartSound: Sound = Sound.sound(Key.key(Sounds.Command.SHUFFLE_START), Sound.Source.MASTER, 1f, 1f)
//    private val shuffleCompleteSound: Sound = Sound.sound(Key.key(Sounds.Command.SHUFFLE_COMPLETE), Sound.Source.MASTER, 1f, 2f)
//    private val shuffleFailSound: Sound = Sound.sound(Key.key(Sounds.Command.SHUFFLE_FAIL), Sound.Source.MASTER, 1f, 0f)

    init {
        installCommands()
    }

    private val teamManager: TeamManager<T> by context()

    @Suggestions("teams")
    fun suggestTeams(context: CommandContext<CommandSender>, input: String) = teamManager.teams.map(Enum<*>::name)

    // TODO(lucy): check game state
    private val canAlterTeams = true

    @CommandMethod("set <player> <team>")
    @CommandDescription("Puts the specified player on the specified team.")
    @CommandPermission("cheesehunt.jointeam")
    fun setTeam(
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

    @CommandMethod("unset <player>")
    @CommandDescription("Removes a player from their current team.")
    @CommandPermission("cheesehunt.jointeam")
    fun unsetTeam(
        sender: Player,
        @Argument("player") player: Player,
    ) {
        if (teamManager.getTeam(sender) == null) {
            sender.sendMessage(Component.text("That player is not on a team.", NamedTextColor.RED))
            return
        }

        teamManager.setTeam(player, null)
    }


    @CommandMethod("shuffle")
    @CommandDescription("Automatically assigns everyone online to a team.")
    @CommandPermission("cheesehunt.autoteam")
    fun autoTeam(sender: Player, @Flag("ignoreAdmins") doesIgnoreAdmins: Boolean) {

        val players = Bukkit.getOnlinePlayers().let {
            if (doesIgnoreAdmins) it.filterNot(Player::isOp) else it
        }

        if (players.isEmpty()) {
            sender.sendMessage(Component.text("There are no players online to shuffle into teams.", NamedTextColor.RED))
            return
        }

        players.shuffled()
            .withIndex()
            .groupBy(keySelector = { it.index % teamManager.teams.size }, valueTransform = IndexedValue<Player>::value)


        sender.sendMessage(Component.text("Shuffled teams.", NamedTextColor.GREEN))
    }

    @CommandMethod("list")
    @CommandDescription("Allows the executing player to see the array of the specified team.")
    @CommandPermission("cheesehunt.teamlist")
    fun teamList(sender: Player) {
        text {
            teamManager.teams.forEach { team ->
                append(team.displayName)
                val players = teamManager.playersOnTeam(team)

                append(Component.text(" (${players.size})", NamedTextColor.GRAY))
                appendNewline()
                if (players.size > 0) {
                    append(
                        players
                            .map(Bukkit::getPlayer).joinToString(", ") { it?.name ?: "Unknown player" }
                            .let(Component::text)
                    )
                    appendNewline()
                }
                append(Component.text("     ", NamedTextColor.GRAY, TextDecoration.STRIKETHROUGH))
                appendNewline()
            }
        }.let(sender::sendMessage)

    }
}
