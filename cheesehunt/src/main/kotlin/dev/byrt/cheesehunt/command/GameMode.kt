package dev.byrt.cheesehunt.command

import dev.byrt.cheesehunt.CheeseHunt

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission
import me.lucyydotp.cheeselib.inject.context
import me.lucyydotp.cheeselib.module.Module
import me.lucyydotp.cheeselib.module.ModuleHolder
import me.lucyydotp.cheeselib.module.installCommands
import me.lucyydotp.cheeselib.sys.AdminMessageStyles
import me.lucyydotp.cheeselib.sys.AdminMessages

import org.bukkit.GameMode
import org.bukkit.entity.Player

@Suppress("unused")
class GameMode(parent: ModuleHolder) : Module(parent) {

    init {
        installCommands()
    }

    private val adminMessages: AdminMessages by context()

    @CommandMethod("gm <mode>")
    @CommandDescription("Puts the executing player into the specified gamemode.")
    @CommandPermission("cheesehunt.gm")
    fun gm(sender : Player, @Argument("mode") mode : PlayerGameModes) {
        when(mode) {
            PlayerGameModes.S -> {
                if(sender.gameMode != GameMode.SURVIVAL) {
                    sender.gameMode = GameMode.SURVIVAL
                    adminMessages.sendDevMessage("${sender.name} set own game mode to Survival mode.", AdminMessageStyles.INFO)
                }
            }
            PlayerGameModes.C -> {
                if(sender.gameMode != GameMode.CREATIVE) {
                    sender.gameMode = GameMode.CREATIVE
                    adminMessages.sendDevMessage("${sender.name} set own game mode to Creative mode.", AdminMessageStyles.INFO)
                }
            }
            PlayerGameModes.A -> {
                if(sender.gameMode != GameMode.ADVENTURE) {
                    sender.gameMode = GameMode.ADVENTURE
                    adminMessages.sendDevMessage("${sender.name} set own game mode to Adventure mode.", AdminMessageStyles.INFO)
                }
            }
            PlayerGameModes.SP -> {
                if(sender.gameMode != GameMode.SPECTATOR) {
                    sender.gameMode = GameMode.SPECTATOR
                    adminMessages.sendDevMessage("${sender.name} set own game mode to Spectator mode.", AdminMessageStyles.INFO)
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
