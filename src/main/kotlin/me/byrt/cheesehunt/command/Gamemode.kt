package me.byrt.cheesehunt.command

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.GameMode
import org.bukkit.entity.Player
import java.util.*

@Suppress("unused")
class Gamemode : BaseCommand {
    @CommandMethod("gm <mode>")
    @CommandDescription("Puts the executing player into the specified gamemode.")
    @CommandPermission("cheesehunt.gm")
    fun gm(sender : Player, @Argument("mode") mode : PlayerGameModes) {
        when(mode) {
            PlayerGameModes.S -> {
                sender.gameMode = GameMode.SURVIVAL
                sender.sendMessage(Component.text("You are now in Survival mode.", NamedTextColor.YELLOW))
            }
            PlayerGameModes.C -> {
                sender.gameMode = GameMode.CREATIVE
                sender.sendMessage(Component.text("You are now in Creative mode.", NamedTextColor.YELLOW))
            }
            PlayerGameModes.A -> {
                sender.gameMode = GameMode.ADVENTURE
                sender.sendMessage(Component.text("You are now in Adventure mode.", NamedTextColor.YELLOW))
            }
            PlayerGameModes.SP -> {
                sender.gameMode = GameMode.SPECTATOR
                sender.sendMessage(Component.text("You are now in Spectator mode.", NamedTextColor.YELLOW))
            }
        }
    }
}

enum class PlayerGameModes {
    S,
    C,
    A,
    SP
}