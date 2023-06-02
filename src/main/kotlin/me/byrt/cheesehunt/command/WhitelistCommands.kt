package me.byrt.cheesehunt.command

import cloud.commandframework.annotations.*

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.state.Sounds
import me.byrt.cheesehunt.manager.WhitelistGroup
import me.byrt.cheesehunt.state.DevStatus

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title

import org.bukkit.Bukkit
import org.bukkit.entity.Player

import java.time.Duration
import java.util.*

@Suppress("unused")
class WhitelistCommands : BaseCommand {
    private val whitelistStartSound: Sound = Sound.sound(Key.key(Sounds.Command.WHITELIST_START), Sound.Source.MASTER, 1f, 1f)
    private val whitelistCompleteSound: Sound = Sound.sound(Key.key(Sounds.Command.WHITELIST_COMPLETE), Sound.Source.MASTER, 1f, 2f)
    private val whitelistFailSound: Sound = Sound.sound(Key.key(Sounds.Command.WHITELIST_FAIL), Sound.Source.MASTER, 1f, 0f)

    @CommandMethod("whitelist set <group>")
    @CommandDescription("Sets whitelisted players to the specified group.")
    @CommandPermission("cheesehunt.whitelist.setgroup")
    @Confirmation
    fun whitelistGroup(sender : Player, @Argument("group") group : WhitelistGroup) {
        whitelistStartDisplay(sender, group)
        try {
            Main.getGame().whitelistManager.setWhitelist(group)
            whitelistCompleteDisplay(sender, group)
        } catch (e : Exception) {
            whitelistFailDisplay(sender, group)
        }
    }

    private fun whitelistStartDisplay(player : Player, group : WhitelistGroup) {
        player.showTitle(Title.title(Component.text(""), Component.text("Whitelisting $group group...").color(NamedTextColor.RED), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(3), Duration.ofSeconds(1))))
        player.playSound(whitelistStartSound)
    }

    private fun whitelistCompleteDisplay(player : Player, group : WhitelistGroup) {
        player.showTitle(Title.title(Component.text(""), Component.text("Successfully whitelisted $group group!").color(NamedTextColor.GREEN), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(1))))
        player.playSound(whitelistCompleteSound)

        Main.getGame().dev.parseDevMessage("Whitelist group set to $group by ${player.name}.", DevStatus.INFO_SUCCESS)
    }

    private fun whitelistFailDisplay(player : Player, group : WhitelistGroup) {
        player.showTitle(Title.title(Component.text(""), Component.text("Whitelisting $group group failed.").color(NamedTextColor.RED), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(1))))
        player.playSound(whitelistFailSound)
    }

    @CommandMethod("whitelist add <player> <group>")
    @CommandDescription("Adds the specified player to the specified whitelist group.")
    @CommandPermission("cheesehunt.whitelist.add")
    fun addPlayerToWhitelistGroup(sender : Player, @Argument("player") player : String, @Argument("group") group : WhitelistGroup) {
        sender.sendMessage(Component.text("Attempting to add $player to $group whitelist group...", NamedTextColor.GRAY))
        try {
            when(group) {
                WhitelistGroup.OFF -> {
                    sender.sendMessage(Component.text("Unable to modify this group as it is not used to store players.", NamedTextColor.RED))
                }
                WhitelistGroup.CLEAR -> {
                    sender.sendMessage(Component.text("Unable to modify this group as it is not used to store players.", NamedTextColor.RED))
                } else -> {
                    val whitelist: ArrayList<String> = Main.getGame().configManager.getWhitelistConfig().getStringList("group.${group.toString().lowercase()}") as ArrayList<String>
                    whitelist.add(player)
                    Main.getGame().configManager.getWhitelistConfig().set("group.${group.toString().lowercase()}", whitelist)
                    Main.getGame().configManager.saveWhitelistConfig()
                    Main.getGame().dev.parseDevMessage("$player successfully added to $group whitelist group by ${sender.name}.", DevStatus.INFO_SUCCESS)
                }
            }

        } catch (e : Exception) {
            sender.sendMessage(Component.text("Failed to add $player to $group group.", NamedTextColor.RED))
        }
    }

    @CommandMethod("whitelist remove <player> <group>")
    @CommandDescription("Removes the specified player from the specified whitelist group.")
    @CommandPermission("cheesehunt.whitelist.remove")
    fun removePlayerFromWhitelistGroup(sender : Player, @Argument("player") player : String, @Argument("group") group : WhitelistGroup) {
        sender.sendMessage(Component.text("Attempting to remove $player to $group whitelist group...", NamedTextColor.GRAY))
        try {
            when(group) {
                WhitelistGroup.OFF -> {
                    sender.sendMessage(Component.text("Unable to modify this group as it is not used to store players.", NamedTextColor.RED))
                }
                WhitelistGroup.CLEAR -> {
                    sender.sendMessage(Component.text("Unable to modify this group as it is not used to store players.", NamedTextColor.RED))
                } else -> {
                    val whitelist: ArrayList<String> = Main.getGame().configManager.getWhitelistConfig().getStringList("group.${group.toString().lowercase()}") as ArrayList<String>
                    whitelist.remove(player)
                    Main.getGame().configManager.getWhitelistConfig().set("group.${group.toString().lowercase()}", whitelist)
                    Main.getGame().configManager.saveWhitelistConfig()
                    Main.getGame().dev.parseDevMessage("$player successfully removed from $group whitelist group by ${sender.name}.", DevStatus.INFO_SUCCESS)
                }
            }

        } catch (e : Exception) {
            sender.sendMessage(Component.text("Failed to add $player to $group group.", NamedTextColor.RED))
        }
    }

    @CommandMethod("whitelist tempadd <player>")
    @CommandDescription("Temporarily adds the specified player to the server whitelist until removal or restart.")
    @CommandPermission("cheesehunt.whitelist.tempadd")
    fun addPlayerTempWhitelist(sender : Player, @Argument("player") player : String) {
        sender.sendMessage(Component.text("Attempting to temporarily whitelist $player...", NamedTextColor.GRAY))
        try {
            if(!Main.getPlugin().server.whitelistedPlayers.contains(Bukkit.getOfflinePlayer(player))) {
                Main.getGame().whitelistManager.tempWhitelistPlayer(player)
                Main.getGame().dev.parseDevMessage("$player successfully added to temporary whitelist by ${sender.name}.", DevStatus.INFO_SUCCESS)
            } else {
                sender.sendMessage(Component.text("$player is already on whitelist.", NamedTextColor.RED))
            }
        } catch (e : Exception) {
            sender.sendMessage(Component.text("Failed to add $player to whitelist.", NamedTextColor.RED))
        }
    }

    @CommandMethod("whitelist tempremove <player>")
    @CommandDescription("Temporarily remove the specified player to the server whitelist.")
    @CommandPermission("cheesehunt.whitelist.tempremove")
    fun removePlayerTempWhitelist(sender : Player, @Argument("player") player : String) {
        sender.sendMessage(Component.text("Attempting to temporarily unwhitelist $player...", NamedTextColor.GRAY))
        try {
            if(Main.getPlugin().server.whitelistedPlayers.contains(Bukkit.getOfflinePlayer(player))) {
                Main.getGame().whitelistManager.removeTempWhitelistPlayer(player)
                Main.getGame().dev.parseDevMessage("$player successfully removed from temporary whitelist by ${sender.name}.", DevStatus.INFO_SUCCESS)
            } else {
                sender.sendMessage(Component.text("$player is not on whitelist.", NamedTextColor.RED))
            }
        } catch (e : Exception) {
            sender.sendMessage(Component.text("Failed to remove $player from whitelist.", NamedTextColor.RED))
        }
    }

    @CommandMethod("whitelist reload")
    @CommandDescription("Reloads the whitelist.yml file.")
    @CommandPermission("cheesehunt.whitelist.reload")
    @Confirmation
    fun whitelistReload(sender : Player) {
        sender.sendMessage(Component.text("Reloading whitelist configuration...", NamedTextColor.RED))
        try {
            Main.getGame().configManager.saveWhitelistConfig()
            Main.getGame().configManager.reloadWhitelistConfig()
            Main.getGame().dev.parseDevMessage("Whitelist configuration reloaded by ${sender.name}.", DevStatus.INFO)

        } catch (e : Exception) {
            sender.sendMessage(Component.text("Failed to reload whitelist configuration.", NamedTextColor.RED))
        }
    }

    @CommandMethod("whitelist list")
    @CommandDescription("Lists the currently whitelisted players and group.")
    @CommandPermission("cheesehunt.whitelist.list")
    fun whitelistList(sender : Player) {
        try {
            sender.sendMessage(Component.text("Current whitelisted group: ${Main.getGame().whitelistManager.getWhitelistedGroup()}.", NamedTextColor.GOLD)
                .append(Component.text("Players: ${Main.getGame().configManager.getWhitelistConfig().getStringList("group.${Main.getGame().whitelistManager.getWhitelistedGroup().toString().lowercase()}")}", NamedTextColor.YELLOW)))

        } catch (e : Exception) {
            sender.sendMessage(Component.text("Failed to list currently whitelisted players and group.", NamedTextColor.RED))
        }
    }
}