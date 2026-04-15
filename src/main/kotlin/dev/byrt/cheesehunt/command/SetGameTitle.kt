package dev.byrt.cheesehunt.command

import dev.byrt.cheesehunt.Main
import dev.byrt.cheesehunt.game.GameState
import io.papermc.paper.command.brigadier.CommandSourceStack

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class SetGameTitle {
    @Command("setgametitle <text>")
    @CommandDescription("Sets the new game's subtitle to the specified string.")
    @Permission("cheesehunt.setgametitle")
    fun setGameTitle(css: CommandSourceStack, @Argument("text") text : Array<String>) {
        val sender = css.sender as Player
        if(Main.getGame().gameManager.getGameState() == GameState.IDLE) {
            val newSubtitle = text.joinToString(" ")
            if(newSubtitle == "reset") {
                Main.getGame().gameTask.setGameSubtitle(newSubtitle)
                sender.sendMessage(Component.text("Successfully reset the next game title.", NamedTextColor.GREEN))
            } else {
                Main.getGame().gameTask.setGameSubtitle(newSubtitle)
                sender.sendMessage(Component.text("Successfully set the next game title to show ", NamedTextColor.GREEN).append(Component.text("'$newSubtitle' ", NamedTextColor.YELLOW)).append(Component.text("when the game begins.", NamedTextColor.GREEN)))
            }
        } else {
            sender.sendMessage(Component.text("Unable to modify game title in this state.", NamedTextColor.RED))
        }
    }
}