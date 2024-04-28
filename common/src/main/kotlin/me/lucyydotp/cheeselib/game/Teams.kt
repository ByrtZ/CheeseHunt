package me.lucyydotp.cheeselib.game

import com.google.common.collect.MultimapBuilder
import me.lucyydotp.cheeselib.module.EventEmitter
import me.lucyydotp.cheeselib.game.nameformat.NameFormatter
import me.lucyydotp.cheeselib.inject.context
import me.lucyydotp.cheeselib.module.Module
import me.lucyydotp.cheeselib.module.ModuleHolder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player
import java.util.UUID
import kotlin.reflect.KClass

interface TeamMeta {
    val name: String
    val displayName: Component
    val nameColor: TextColor
    val icon: Component
}

data class TeamChangeEvent(val player: Player, val oldTeam: TeamMeta?, val newTeam: TeamMeta?)

/**
 * Manages teams for players.
 * @param T an enum that implements [TeamMeta], dictating the available teams
 */
class TeamManager<T>(
    parent: ModuleHolder,
    teamClazz: KClass<T>,
) : Module(parent) where T : Enum<T>, T : TeamMeta {

    val onTeamChange = EventEmitter<TeamChangeEvent>()

    val teams = (teamClazz.java.enumConstants as Array<T>).toList()

    private val teamMembers = MultimapBuilder
        .enumKeys(teamClazz.java)
        .hashSetValues()
        .build<T, UUID>()

    private val nameFormatter: NameFormatter by context()

    init {
        listen(nameFormatter.format) {
            getTeam(it.player)?.let { t ->
                it.usernameStyle = it.usernameStyle.color(t.nameColor)
                it.prefixes = listOf(t.icon)
            }
        }
    }

    /**
     * Gets the player's current team, or null if they're a spectator.
     */
    fun getTeam(player: Player) = teams.firstOrNull {
        teamMembers.containsEntry(it, player.uniqueId)
    }

    /**
     * Gets every player on a team.
     */
    fun playersOnTeam(team: T) = teamMembers[team]

    /**
     * Sets the player's team, emitting a [TeamChangeEvent] if it changed.
     */
    fun setTeam(player: Player, team: T?) {
        val currentTeam = getTeam(player)
        if (team == currentTeam) return

        teamMembers.remove(currentTeam, player.uniqueId)
        teamMembers.put(team, player.uniqueId)

        onTeamChange.emit(TeamChangeEvent(player, currentTeam, team))
        nameFormatter.format(player, force = true)
    }
}
