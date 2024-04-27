package dev.byrt.cheesehunt.command

import dev.byrt.cheesehunt.CheeseHunt
import dev.byrt.cheesehunt.util.DevStatus

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission

import org.bukkit.GameMode
import org.bukkit.entity.Player

@Suppress("unused")
class GameMode : BaseCommand {
    @CommandMethod("gm <mode>")
    @CommandDescription("Puts the executing player into the specified gamemode.")
    @CommandPermission("cheesehunt.gm")
    fun gm(sender : Player, @Argument("mode") mode : PlayerGameModes) {
        when(mode) {
            PlayerGameModes.S -> {
                if(sender.gameMode != GameMode.SURVIVAL) {
                    sender.gameMode = GameMode.SURVIVAL
                    CheeseHunt.getGame().dev.parseDevMessage("${sender.name} set own game mode to Survival mode.", DevStatus.INFO)
                }
            }
            PlayerGameModes.C -> {
                if(sender.gameMode != GameMode.CREATIVE) {
                    sender.gameMode = GameMode.CREATIVE
                    CheeseHunt.getGame().dev.parseDevMessage("${sender.name} set own game mode to Creative mode.", DevStatus.INFO)
                }
            }
            PlayerGameModes.A -> {
                if(sender.gameMode != GameMode.ADVENTURE) {
                    sender.gameMode = GameMode.ADVENTURE
                    CheeseHunt.getGame().dev.parseDevMessage("${sender.name} set own game mode to Adventure mode.", DevStatus.INFO)
                }
            }
            PlayerGameModes.SP -> {
                if(sender.gameMode != GameMode.SPECTATOR) {
                    sender.gameMode = GameMode.SPECTATOR
                    CheeseHunt.getGame().dev.parseDevMessage("${sender.name} set own game mode to Spectator mode.", DevStatus.INFO)
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
