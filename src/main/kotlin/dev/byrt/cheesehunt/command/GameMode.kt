package dev.byrt.cheesehunt.command

import dev.byrt.cheesehunt.Main
import dev.byrt.cheesehunt.util.DevStatus
import io.papermc.paper.command.brigadier.CommandSourceStack

import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class GameMode {
    @Command("gm <mode>")
    @CommandDescription("Puts the executing player into the specified gamemode.")
    @Permission("cheesehunt.gm")
    fun gm(css: CommandSourceStack, @Argument("mode") mode : PlayerGameModes) {
        val sender = css.sender as Player
        when(mode) {
            PlayerGameModes.S -> {
                if(sender.gameMode != GameMode.SURVIVAL) {
                    sender.gameMode = GameMode.SURVIVAL
                    Main.getGame().dev.parseDevMessage("${sender.name} set own game mode to Survival mode.", DevStatus.INFO)
                }
            }
            PlayerGameModes.C -> {
                if(sender.gameMode != GameMode.CREATIVE) {
                    sender.gameMode = GameMode.CREATIVE
                    Main.getGame().dev.parseDevMessage("${sender.name} set own game mode to Creative mode.", DevStatus.INFO)
                }
            }
            PlayerGameModes.A -> {
                if(sender.gameMode != GameMode.ADVENTURE) {
                    sender.gameMode = GameMode.ADVENTURE
                    Main.getGame().dev.parseDevMessage("${sender.name} set own game mode to Adventure mode.", DevStatus.INFO)
                }
            }
            PlayerGameModes.SP -> {
                if(sender.gameMode != GameMode.SPECTATOR) {
                    sender.gameMode = GameMode.SPECTATOR
                    Main.getGame().dev.parseDevMessage("${sender.name} set own game mode to Spectator mode.", DevStatus.INFO)
                }
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