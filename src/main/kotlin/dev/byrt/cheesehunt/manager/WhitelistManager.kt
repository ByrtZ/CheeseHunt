package dev.byrt.cheesehunt.manager

import dev.byrt.cheesehunt.Main
import dev.byrt.cheesehunt.game.Game

import org.bukkit.Bukkit

class WhitelistManager(private val game : Game) {
    private var currentWhitelistedGroup = WhitelistGroup.ADMIN

    fun setWhitelist(group : WhitelistGroup) {
        try {
            whitelistGroup(group)
        } catch(e : Exception) {
            Main.getPlugin().logger.severe("Error when attempting to set the whitelist group to $group.")
        }
    }

    private fun whitelistGroup(group : WhitelistGroup) {
        clearServerWhitelist()
        Main.getPlugin().server.reloadWhitelist()
        Main.getPlugin().server.setWhitelist(true)
        currentWhitelistedGroup = group
        when(group) {
            WhitelistGroup.ADMIN -> {
                for(player in game.configManager.getWhitelistConfig().getStringList("group.admin")) {
                    Bukkit.getOfflinePlayer(player).isWhitelisted = true
                    Main.getPlugin().logger.info("Whitelisted $player as they are in the $group group.")
                }
            }
            WhitelistGroup.CLEAR -> {
                // nothing else, just emptiness.
            }
            WhitelistGroup.OFF -> {
                Main.getPlugin().server.setWhitelist(false)
            } else -> {
                for(player in game.configManager.getWhitelistConfig().getStringList("group.admin").plus(game.configManager.getWhitelistConfig().getStringList("group.${group.toString().lowercase()}"))) {
                    Bukkit.getOfflinePlayer(player).isWhitelisted = true
                    Main.getPlugin().logger.info("Whitelisted $player as they are in the $group group or inherit it.")
                }
            }
        }
        Main.getPlugin().server.reloadWhitelist()
    }

    fun getWhitelistedGroup() : WhitelistGroup {
        return currentWhitelistedGroup
    }

    private fun clearServerWhitelist() {
        for(player in Main.getPlugin().server.whitelistedPlayers) {
            player.isWhitelisted = false
        }
    }

    fun tempWhitelistPlayer(player : String) {
        Bukkit.getOfflinePlayer(player).isWhitelisted = true
    }

    fun removeTempWhitelistPlayer(player : String) {
        Bukkit.getOfflinePlayer(player).isWhitelisted = false
    }
}

@Suppress("unused")
enum class WhitelistGroup {
    ADMIN,
    BOT_TESTING,
    GUOCO,
    LISA,
    NOXCREW_MODS,
    OFF,
    CLEAR
}