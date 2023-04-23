package me.byrt.cheesehunt.command

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.state.GameState

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.entity.Player

@Suppress("unused")
class SetGameTitle : BaseCommand {
    @CommandMethod("setgametitle <text>")
    @CommandDescription("Sets the new game's subtitle to the specified string.")
    @CommandPermission("cheesehunt.setgametitle")
    fun setGameTitle(sender : Player, @Argument("text") text : Array<String>) {
        if(Main.getGame().getGameState() == GameState.IDLE) {
            val newSubtitle = text.joinToString(" ")
            if(newSubtitle == "reset") {
                Main.getGame().getGameCountdownTask().setGameSubtitle(newSubtitle)
                sender.sendMessage(Component.text("Successfully reset the next game title.", NamedTextColor.GREEN))
            } else {
                Main.getGame().getGameCountdownTask().setGameSubtitle(newSubtitle)
                sender.sendMessage(Component.text("Successfully set the next game title to show ", NamedTextColor.GREEN).append(Component.text("'$newSubtitle' ", NamedTextColor.YELLOW)).append(Component.text("when the game begins.", NamedTextColor.GREEN)))
            }
        } else {
            sender.sendMessage(Component.text("Unable to modify game title in this state.", NamedTextColor.RED))
        }
    }
}