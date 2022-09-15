package me.byrt.cheesehunt.manager

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.Bukkit
import org.bukkit.scoreboard.Team

import java.util.*

@Suppress("unused")
class TeamManager(private val game : Game) {
    private var redTeam = ArrayList<UUID>()
    private var blueTeam = ArrayList<UUID>()
    private var spectators = ArrayList<UUID>()
    private var redDisplayTeam: Team = Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("redDisplay")
    private var blueDisplayTeam: Team = Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("blueDisplay")
    private var uncollectedCheeseDisplayTeam: Team = Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("uncollectedCheeseDisplay")
    private var adminDisplayTeam: Team = Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("admin")

    fun addToTeam(uuid : UUID, team : Teams) {
        when(team) {
            Teams.RED -> {
                if(blueTeam.contains(uuid)) { removeFromTeam(uuid, Teams.BLUE) }
                if(spectators.contains(uuid)) { removeFromTeam(uuid, Teams.SPECTATOR) }
                redTeam.add(uuid)
                redDisplayTeam.addPlayer(Bukkit.getOfflinePlayer(uuid))
                Bukkit.getServer().getPlayer(uuid)?.sendMessage(Component.text("You are now on the ")
                    .color(NamedTextColor.WHITE)
                    .append(Component.text("Red Team")
                        .color(NamedTextColor.RED))
                        .append(Component.text(".")))
            }
            Teams.BLUE -> {
                if(redTeam.contains(uuid)) { removeFromTeam(uuid, Teams.RED) }
                if(spectators.contains(uuid)) { removeFromTeam(uuid, Teams.SPECTATOR) }
                blueTeam.add(uuid)
                blueDisplayTeam.addPlayer(Bukkit.getOfflinePlayer(uuid))
                Bukkit.getServer().getPlayer(uuid)?.sendMessage(Component.text("You are now on the ")
                    .color(NamedTextColor.WHITE)
                    .append(Component.text("Blue Team")
                        .color(NamedTextColor.BLUE))
                    .append(Component.text(".")))
            }
            Teams.SPECTATOR -> {
                if(redTeam.contains(uuid)) { removeFromTeam(uuid, Teams.RED) }
                if(blueTeam.contains(uuid)) { removeFromTeam(uuid, Teams.BLUE) }
                spectators.add(uuid)
                Bukkit.getServer().getPlayer(uuid)?.sendMessage(Component.text("You are now a Spectator."))
            }
        }
    }

    fun removeFromTeam(uuid : UUID, team : Teams) {
        when(team) {
            Teams.RED -> {
                redTeam.remove(uuid)
                redDisplayTeam.removePlayer(Bukkit.getOfflinePlayer(uuid))
                Bukkit.getServer().getPlayer(uuid)?.sendMessage(Component.text("You are no longer on ")
                    .color(NamedTextColor.WHITE)
                    .append(Component.text("Red Team")
                        .color(NamedTextColor.RED))
                    .append(Component.text(".")))
            }
            Teams.BLUE -> {
                blueTeam.remove(uuid)
                blueDisplayTeam.removePlayer(Bukkit.getOfflinePlayer(uuid))
                Bukkit.getServer().getPlayer(uuid)?.sendMessage(Component.text("You are no longer on ")
                    .color(NamedTextColor.WHITE)
                    .append(Component.text("Blue Team")
                        .color(NamedTextColor.BLUE))
                    .append(Component.text(".")))
            }
            Teams.SPECTATOR -> {
                spectators.remove(uuid)
                Bukkit.getServer().getPlayer(uuid)?.sendMessage(Component.text("You are no longer a Spectator."))
            }
        }
    }

    fun addToAdminDisplay(uuid : UUID) {
        adminDisplayTeam.addPlayer(Bukkit.getOfflinePlayer(uuid))
    }

    fun buildDisplayTeams() {
        redDisplayTeam.color(NamedTextColor.RED)
        redDisplayTeam.prefix(Component.text("").color(NamedTextColor.RED))
        redDisplayTeam.suffix(Component.text("").color(NamedTextColor.WHITE))
        redDisplayTeam.displayName(Component.text("Red").color(NamedTextColor.RED))

        blueDisplayTeam.color(NamedTextColor.BLUE)
        blueDisplayTeam.prefix(Component.text("").color(NamedTextColor.BLUE))
        blueDisplayTeam.suffix(Component.text("").color(NamedTextColor.WHITE))
        blueDisplayTeam.displayName(Component.text("Blue").color(NamedTextColor.BLUE))

        uncollectedCheeseDisplayTeam.color(NamedTextColor.GOLD)
        uncollectedCheeseDisplayTeam.prefix(Component.text("").color(NamedTextColor.GOLD))
        uncollectedCheeseDisplayTeam.suffix(Component.text("").color(NamedTextColor.WHITE))
        uncollectedCheeseDisplayTeam.displayName(Component.text("Cheese").color(NamedTextColor.GOLD))

        adminDisplayTeam.color(NamedTextColor.WHITE)
        adminDisplayTeam.prefix(Component.text("⛏ ").color(NamedTextColor.RED)
            .append(Component.text("[").color(NamedTextColor.WHITE))
            .append(Component.text("Admin").color(NamedTextColor.RED))
            .append(Component.text("] ").color(NamedTextColor.WHITE)))
        adminDisplayTeam.suffix(Component.text("").color(NamedTextColor.WHITE))
        adminDisplayTeam.displayName(Component.text("Admin").color(NamedTextColor.RED))
    }

    fun destroyDisplayTeams() {
        redDisplayTeam.unregister()
        blueDisplayTeam.unregister()
        uncollectedCheeseDisplayTeam.unregister()
        adminDisplayTeam.unregister()
    }

    fun getPlayerTeam(uuid : UUID): Teams {
        return if(redTeam.contains(uuid)) {
            Teams.RED
        } else if(blueTeam.contains(uuid)) {
            Teams.BLUE
        } else {
            Teams.SPECTATOR
        }
    }

    fun isInRedTeam(uuid : UUID): Boolean {
        return redTeam.contains(uuid)
    }

    fun isInBlueTeam(uuid : UUID): Boolean {
        return blueTeam.contains(uuid)
    }

    fun isSpectator(uuid : UUID): Boolean {
        return spectators.contains(uuid)
    }

    fun getRedTeam(): ArrayList<UUID> {
        return this.redTeam
    }

    fun getBlueTeam(): ArrayList<UUID> {
        return this.blueTeam
    }

    fun getSpectators(): ArrayList<UUID> {
        return this.spectators
    }

    fun getRedDisplayTeam(): Team {
        return this.redDisplayTeam
    }

    fun getBlueDisplayTeam(): Team {
        return this.blueDisplayTeam
    }

    fun getUncollectedCheeseDisplayTeam(): Team {
        return this.uncollectedCheeseDisplayTeam
    }
}