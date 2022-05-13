package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.Main

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.Bukkit

import java.util.*

class TeamManager(game : Game) {
    private var redTeam = ArrayList<UUID>()
    private var blueTeam = ArrayList<UUID>()
    private var spectators = ArrayList<UUID>()

    fun addToTeam(uuid : UUID, team : Team) {
        when(team) {
            Team.RED -> {
                if(blueTeam.contains(uuid)) { removeFromTeam(uuid, Team.BLUE) }
                redTeam.add(uuid)
                Bukkit.getServer().getPlayer(uuid)?.sendMessage(Component.text("You are now on the ")
                    .color(NamedTextColor.WHITE)
                    .append(Component.text("Red Team")
                        .color(NamedTextColor.RED))
                        .append(Component.text(".")))
            }
            Team.BLUE -> {
                if(redTeam.contains(uuid)) { removeFromTeam(uuid, Team.RED) }
                blueTeam.add(uuid)
                Bukkit.getServer().getPlayer(uuid)?.sendMessage(Component.text("You are now on the ")
                    .color(NamedTextColor.WHITE)
                    .append(Component.text("Blue Team")
                        .color(NamedTextColor.BLUE))
                    .append(Component.text(".")))
            }
            else -> {
                Main.getPlugin().logger.info("[TEAM ERROR] Something weird happened when attempting to add someone to a team.")
            }
        }
    }

    fun removeFromTeam(uuid : UUID, team : Team) {
        when(team) {
            Team.RED -> {
                redTeam.remove(uuid)
                Bukkit.getServer().getPlayer(uuid)?.sendMessage(Component.text("You are no longer on ")
                    .color(NamedTextColor.WHITE)
                    .append(Component.text("Red Team")
                        .color(NamedTextColor.RED))
                    .append(Component.text(".")))
            }
            Team.BLUE -> {
                blueTeam.remove(uuid)
                Bukkit.getServer().getPlayer(uuid)?.sendMessage(Component.text("You are no longer on ")
                    .color(NamedTextColor.WHITE)
                    .append(Component.text("Blue Team")
                        .color(NamedTextColor.BLUE))
                    .append(Component.text(".")))
            }
            else -> {
                Main.getPlugin().logger.info("[TEAM ERROR] Something weird happened when attempting to remove someone from a team.")
            }
        }
    }

    fun getPlayerTeam(uuid : UUID): Team {
        return if(redTeam.contains(uuid)) {
            Team.RED
        } else if(blueTeam.contains(uuid)) {
            Team.BLUE
        } else {
            Team.SPECTATOR
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
}