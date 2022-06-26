package me.byrt.cheesehunt.command

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.GameMode
import org.bukkit.entity.Player

@Suppress("unused")
class Gamemode : BaseCommand {
    @CommandMethod("gm <mode>")
    @CommandDescription("Puts the executing player into the specified gamemode.")
    @CommandPermission("cheesehunt.gm")
    fun gm(sender : Player, @Argument("mode") mode : Int?) {
        when(mode) {
            0 -> {
                sender.gameMode = GameMode.SURVIVAL
                sender.sendMessage(Component.text("You are now in Survival mode.").color(NamedTextColor.YELLOW))
            }
            1 -> {
                sender.gameMode = GameMode.CREATIVE
                sender.sendMessage(Component.text("You are now in Creative mode.").color(NamedTextColor.YELLOW))
            }
            2 -> {
                sender.gameMode = GameMode.ADVENTURE
                sender.sendMessage(Component.text("You are now in Adventure mode.").color(NamedTextColor.YELLOW))
            }
            3 -> {
                sender.gameMode = GameMode.SPECTATOR
                sender.sendMessage(Component.text("You are now in Spectator mode.").color(NamedTextColor.YELLOW))
            } else -> {
            sender.sendMessage(Component.text("This gamemode does not exist!").color(NamedTextColor.RED))
            }
        }
    }
}