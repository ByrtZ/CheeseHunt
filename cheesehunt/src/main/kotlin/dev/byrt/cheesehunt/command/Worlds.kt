package dev.byrt.cheesehunt.command

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission
import dev.byrt.cheesehunt.CheeseHunt
import me.lucyydotp.cheeselib.module.Module
import me.lucyydotp.cheeselib.module.ModuleHolder
import me.lucyydotp.cheeselib.module.installCommands
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.WorldCreator
import org.bukkit.entity.Player

@Suppress("unused")
class Worlds(parent: ModuleHolder) : Module(parent) {

    init {
        installCommands()
    }

    @CommandMethod("world <option> <world>")
    @CommandDescription("Puts the specified player on the specified team.")
    @CommandPermission("cheesehunt.worlds")
    private fun world(sender: Player, @Argument("option") option: WorldOptions, @Argument("world") world: WorldsList) {
        when (option) {
            WorldOptions.OPEN -> {
                when (world) {
                    WorldsList.CHEESE -> {
                        sender.sendMessage(
                            Component.text("You cannot modify the state of this world.").color(NamedTextColor.RED)
                        )
                    }

                    WorldsList.ORIGINAL -> {
                        try {
                            if (!Bukkit.getWorlds().contains(Bukkit.getWorld("original"))) {
                                sender.sendMessage(
                                    Component.text("Attempting to open $world...").color(NamedTextColor.GRAY)
                                )
                                CheeseHunt.getPlugin().server.createWorld(WorldCreator.name("original"))
                                sender.sendMessage(
                                    Component.text("Successfully opened $world.").color(NamedTextColor.GREEN)
                                )
                            }
                        } catch (e: Exception) {
                            sender.sendMessage(Component.text("Unable to open $world.").color(NamedTextColor.RED))
                            e.printStackTrace()
                        }
                    }

                    WorldsList.VALORANT -> {
                        try {
                            if (!Bukkit.getWorlds().contains(Bukkit.getWorld("Valorant"))) {
                                sender.sendMessage(
                                    Component.text("Attempting to open $world...").color(NamedTextColor.GRAY)
                                )
                                CheeseHunt.getPlugin().server.createWorld(WorldCreator.name("Valorant"))
                                sender.sendMessage(
                                    Component.text("Successfully opened $world.").color(NamedTextColor.GREEN)
                                )
                            }
                        } catch (e: Exception) {
                            sender.sendMessage(Component.text("Unable to open $world.").color(NamedTextColor.RED))
                            e.printStackTrace()
                        }
                    }

                    WorldsList.ELEOS -> {
                        try {
                            if (!Bukkit.getWorlds().contains(Bukkit.getWorld("eleos"))) {
                                sender.sendMessage(
                                    Component.text("Attempting to open $world...").color(NamedTextColor.GRAY)
                                )
                                CheeseHunt.getPlugin().server.createWorld(WorldCreator.name("eleos"))
                                sender.sendMessage(
                                    Component.text("Successfully opened $world.").color(NamedTextColor.GREEN)
                                )
                            }
                        } catch (e: Exception) {
                            sender.sendMessage(Component.text("Unable to open $world.").color(NamedTextColor.RED))
                            e.printStackTrace()
                        }
                    }

                    WorldsList.DEZZER -> {
                        try {
                            if (!Bukkit.getWorlds().contains(Bukkit.getWorld("dezzer"))) {
                                sender.sendMessage(
                                    Component.text("Attempting to open $world...").color(NamedTextColor.GRAY)
                                )
                                CheeseHunt.getPlugin().server.createWorld(WorldCreator.name("dezzer"))
                                sender.sendMessage(
                                    Component.text("Successfully opened $world.").color(NamedTextColor.GREEN)
                                )
                            }
                        } catch (e: Exception) {
                            sender.sendMessage(Component.text("Unable to open $world.").color(NamedTextColor.RED))
                            e.printStackTrace()
                        }
                    }

                    WorldsList.PKW -> {
                        try {
                            if (!Bukkit.getWorlds().contains(Bukkit.getWorld("pkw"))) {
                                sender.sendMessage(
                                    Component.text("Attempting to open $world...").color(NamedTextColor.GRAY)
                                )
                                CheeseHunt.getPlugin().server.createWorld(WorldCreator.name("pkw"))
                                sender.sendMessage(
                                    Component.text("Successfully opened $world.").color(NamedTextColor.GREEN)
                                )
                            }
                        } catch (e: Exception) {
                            sender.sendMessage(Component.text("Unable to open $world.").color(NamedTextColor.RED))
                            e.printStackTrace()
                        }
                    }
                }
            }

            WorldOptions.CLOSE -> {
                when (world) {
                    WorldsList.CHEESE -> {
                        sender.sendMessage(
                            Component.text("You cannot modify the state of this world.").color(NamedTextColor.RED)
                        )
                    }

                    WorldsList.ORIGINAL -> {
                        try {
                            if (Bukkit.getWorlds().contains(Bukkit.getWorld("original"))) {
                                sender.sendMessage(
                                    Component.text("Attempting to close $world...").color(NamedTextColor.GRAY)
                                )
                                for (player in Bukkit.getOnlinePlayers()) {
                                    if (player.world == Bukkit.getWorld("original")) {
                                        player.teleport(
                                            Location(
                                                Bukkit.getWorld("Cheese"),
                                                0.5,
                                                -52.0,
                                                0.5,
                                                0.0f,
                                                0.0f
                                            )
                                        )
                                    }
                                }
                                CheeseHunt.getPlugin().server.unloadWorld("original", true)
                                sender.sendMessage(
                                    Component.text("Successfully closed $world.").color(NamedTextColor.GREEN)
                                )
                            }
                        } catch (e: Exception) {
                            sender.sendMessage(Component.text("Unable to close $world.").color(NamedTextColor.RED))
                            e.printStackTrace()
                        }
                    }

                    WorldsList.VALORANT -> {
                        try {
                            if (Bukkit.getWorlds().contains(Bukkit.getWorld("Valorant"))) {
                                sender.sendMessage(
                                    Component.text("Attempting to close $world...").color(NamedTextColor.GRAY)
                                )
                                for (player in Bukkit.getOnlinePlayers()) {
                                    if (player.world == Bukkit.getWorld("Valorant")) {
                                        player.teleport(
                                            Location(
                                                Bukkit.getWorld("Cheese"),
                                                0.5,
                                                -52.0,
                                                0.5,
                                                0.0f,
                                                0.0f
                                            )
                                        )
                                    }
                                }
                                CheeseHunt.getPlugin().server.unloadWorld("Valorant", true)
                                sender.sendMessage(
                                    Component.text("Successfully closed $world.").color(NamedTextColor.GREEN)
                                )
                            }
                        } catch (e: Exception) {
                            sender.sendMessage(Component.text("Unable to close $world.").color(NamedTextColor.RED))
                            e.printStackTrace()
                        }
                    }

                    WorldsList.ELEOS -> {
                        try {
                            if (Bukkit.getWorlds().contains(Bukkit.getWorld("eleos"))) {
                                sender.sendMessage(
                                    Component.text("Attempting to close $world...").color(NamedTextColor.GRAY)
                                )
                                for (player in Bukkit.getOnlinePlayers()) {
                                    if (player.world == Bukkit.getWorld("eleos")) {
                                        player.teleport(
                                            Location(
                                                Bukkit.getWorld("Cheese"),
                                                0.5,
                                                -52.0,
                                                0.5,
                                                0.0f,
                                                0.0f
                                            )
                                        )
                                    }
                                }
                                CheeseHunt.getPlugin().server.unloadWorld("eleos", true)
                                sender.sendMessage(
                                    Component.text("Successfully closed $world.").color(NamedTextColor.GREEN)
                                )
                            }
                        } catch (e: Exception) {
                            sender.sendMessage(Component.text("Unable to close $world.").color(NamedTextColor.RED))
                            e.printStackTrace()
                        }
                    }

                    WorldsList.DEZZER -> {
                        try {
                            if (Bukkit.getWorlds().contains(Bukkit.getWorld("dezzer"))) {
                                sender.sendMessage(
                                    Component.text("Attempting to close $world...").color(NamedTextColor.GRAY)
                                )
                                for (player in Bukkit.getOnlinePlayers()) {
                                    if (player.world == Bukkit.getWorld("dezzer")) {
                                        player.teleport(
                                            Location(
                                                Bukkit.getWorld("Cheese"),
                                                0.5,
                                                -52.0,
                                                0.5,
                                                0.0f,
                                                0.0f
                                            )
                                        )
                                    }
                                }
                                CheeseHunt.getPlugin().server.unloadWorld("dezzer", true)
                                sender.sendMessage(
                                    Component.text("Successfully closed $world.").color(NamedTextColor.GREEN)
                                )
                            }
                        } catch (e: Exception) {
                            sender.sendMessage(Component.text("Unable to close $world.").color(NamedTextColor.RED))
                            e.printStackTrace()
                        }
                    }

                    WorldsList.PKW -> {
                        try {
                            if (Bukkit.getWorlds().contains(Bukkit.getWorld("pkw"))) {
                                sender.sendMessage(
                                    Component.text("Attempting to close $world...").color(NamedTextColor.GRAY)
                                )
                                for (player in Bukkit.getOnlinePlayers()) {
                                    if (player.world == Bukkit.getWorld("pkw")) {
                                        player.teleport(
                                            Location(
                                                Bukkit.getWorld("Cheese"),
                                                0.5,
                                                -52.0,
                                                0.5,
                                                0.0f,
                                                0.0f
                                            )
                                        )
                                    }
                                }
                                CheeseHunt.getPlugin().server.unloadWorld("pkw", true)
                                sender.sendMessage(
                                    Component.text("Successfully closed $world.").color(NamedTextColor.GREEN)
                                )
                            }
                        } catch (e: Exception) {
                            sender.sendMessage(Component.text("Unable to close $world.").color(NamedTextColor.RED))
                            e.printStackTrace()
                        }
                    }
                }
            }

            WorldOptions.JOIN -> {
                when (world) {
                    WorldsList.CHEESE -> {
                        sender.sendMessage(Component.text("Attempting to join ${world}...").color(NamedTextColor.GRAY))
                        sender.player?.teleport(Location(Bukkit.getWorld("Cheese"), 0.5, -52.0, 0.5, 0.0f, 0.0f))
                        sender.sendMessage(Component.text("Successfully joined $world.").color(NamedTextColor.GREEN))
                    }

                    WorldsList.ORIGINAL -> {
                        try {
                            sender.sendMessage(
                                Component.text("Attempting to join ${world}...").color(NamedTextColor.GRAY)
                            )
                            sender.player?.teleport(
                                Location(
                                    Bukkit.getWorld("original"),
                                    90.5,
                                    63.0,
                                    197.5,
                                    0.0f,
                                    0.0f
                                )
                            )
                            sender.sendMessage(
                                Component.text("Successfully joined $world.").color(NamedTextColor.GREEN)
                            )
                        } catch (e: Exception) {
                            sender.sendMessage(Component.text("Unable to join $world.").color(NamedTextColor.RED))
                            e.printStackTrace()
                        }
                    }

                    WorldsList.VALORANT -> {
                        try {
                            sender.sendMessage(
                                Component.text("Attempting to join ${world}...").color(NamedTextColor.GRAY)
                            )
                            sender.player?.teleport(Location(Bukkit.getWorld("Valorant"), 0.5, 64.0, 0.5, 0.0f, 0.0f))
                            sender.sendMessage(
                                Component.text("Successfully joined $world.").color(NamedTextColor.GREEN)
                            )
                        } catch (e: Exception) {
                            sender.sendMessage(Component.text("Unable to join $world.").color(NamedTextColor.RED))
                            e.printStackTrace()
                        }
                    }

                    WorldsList.ELEOS -> {
                        try {
                            sender.sendMessage(
                                Component.text("Attempting to join ${world}...").color(NamedTextColor.GRAY)
                            )
                            sender.player?.teleport(Location(Bukkit.getWorld("eleos"), 0.5, 64.0, 0.5, 0.0f, 0.0f))
                            sender.sendMessage(
                                Component.text("Successfully joined $world.").color(NamedTextColor.GREEN)
                            )
                        } catch (e: Exception) {
                            sender.sendMessage(Component.text("Unable to join $world.").color(NamedTextColor.RED))
                            e.printStackTrace()
                        }
                    }

                    WorldsList.DEZZER -> {
                        try {
                            sender.sendMessage(
                                Component.text("Attempting to join ${world}...").color(NamedTextColor.GRAY)
                            )
                            sender.player?.teleport(Location(Bukkit.getWorld("dezzer"), 69.5, 65.0, 186.5, 0.0f, 0.0f))
                            sender.sendMessage(
                                Component.text("Successfully joined $world.").color(NamedTextColor.GREEN)
                            )
                        } catch (e: Exception) {
                            sender.sendMessage(Component.text("Unable to join $world.").color(NamedTextColor.RED))
                            e.printStackTrace()
                        }
                    }

                    WorldsList.PKW -> {
                        try {
                            sender.sendMessage(
                                Component.text("Attempting to join ${world}...").color(NamedTextColor.GRAY)
                            )
                            sender.player?.teleport(Location(Bukkit.getWorld("pkw"), 0.5, 128.0, 0.5, 0.0f, 0.0f))
                            sender.sendMessage(
                                Component.text("Successfully joined $world.").color(NamedTextColor.GREEN)
                            )
                        } catch (e: Exception) {
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
    ORIGINAL,
    ELEOS,
    VALORANT,
    DEZZER,
    PKW
}
