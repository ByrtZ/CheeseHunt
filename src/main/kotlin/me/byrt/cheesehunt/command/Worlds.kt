package me.byrt.cheesehunt.command

import me.byrt.cheesehunt.Main

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.WorldCreator
import org.bukkit.entity.Player

@Suppress("unused")
class Worlds : BaseCommand {
    @CommandMethod("world <option> <world>")
    @CommandDescription("Puts the specified player on the specified team.")
    @CommandPermission("cheesehunt.worlds")
    fun setTeam(sender : Player, @Argument("option") option : WorldOptions, @Argument("world") world : WorldsList) {
        when(option) {
            WorldOptions.OPEN -> {
                when(world) {
                    WorldsList.CHEESE -> {
                        sender.sendMessage(Component.text("You cannot modify the state of this world.").color(NamedTextColor.RED))
                    }
                    WorldsList.ORIGINAL -> {
                        try {
                            if(!Bukkit.getWorlds().contains(Bukkit.getWorld("original"))) {
                                sender.sendMessage(Component.text("Attempting to open $world...").color(NamedTextColor.GRAY))
                                Main.getPlugin().server.createWorld(WorldCreator.name("original"))
                                sender.sendMessage(Component.text("Successfully opened $world.").color(NamedTextColor.GREEN))
                            }
                        } catch(e : Exception) {
                            sender.sendMessage(Component.text("Unable to open $world.").color(NamedTextColor.RED))
                            e.printStackTrace()
                        }
                    }
                }
            }
            WorldOptions.CLOSE -> {
                when(world) {
                    WorldsList.CHEESE -> {
                        sender.sendMessage(Component.text("You cannot modify the state of this world.").color(NamedTextColor.RED))
                    }
                    WorldsList.ORIGINAL -> {
                        try {
                            if(Bukkit.getWorlds().contains(Bukkit.getWorld("original"))) {
                                sender.sendMessage(Component.text("Attempting to close $world...").color(NamedTextColor.GRAY))
                                for(player in Bukkit.getOnlinePlayers()) {
                                    if(player.world == Bukkit.getWorld("original")) {
                                        player.teleport(Location(Bukkit.getWorld("Cheese"), 0.5, -52.0 ,0.5, 0.0f, 0.0f))
                                    }
                                }
                                Main.getPlugin().server.unloadWorld("original", true)
                                sender.sendMessage(Component.text("Successfully closed $world.").color(NamedTextColor.GREEN))
                            }
                        } catch(e : Exception) {
                            sender.sendMessage(Component.text("Unable to close $world.").color(NamedTextColor.RED))
                            e.printStackTrace()
                        }
                    }
                }
            }
            WorldOptions.JOIN -> {
                when(world) {
                    WorldsList.CHEESE -> {
                        sender.sendMessage(Component.text("Attempting to join ${world}...").color(NamedTextColor.GRAY))
                        sender.player?.teleport(Location(Bukkit.getWorld("Cheese"), 0.5, -52.0 ,0.5, 0.0f, 0.0f))
                        sender.sendMessage(Component.text("Successfully joined $world.").color(NamedTextColor.GREEN))
                    }
                    WorldsList.ORIGINAL -> {
                        try {
                            sender.sendMessage(Component.text("Attempting to join ${world}...").color(NamedTextColor.GRAY))
                            sender.player?.teleport(Location(Bukkit.getWorld("original"), 90.5, 63.0, 197.5, 0.0f, 0.0f))
                            sender.sendMessage(Component.text("Successfully joined $world.").color(NamedTextColor.GREEN))
                        } catch(e : Exception) {
                            sender.sendMessage(Component.text("Unable to join $world.").color(NamedTextColor.RED))
                            e.printStackTrace()
                        }
                    }
                }

            }
        }
    }
}

enum class WorldOptions {
    OPEN,
    CLOSE,
    JOIN
}

enum class WorldsList {
    CHEESE,
    ORIGINAL
}