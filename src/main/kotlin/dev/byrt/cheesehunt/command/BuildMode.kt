package dev.byrt.cheesehunt.command

import dev.byrt.cheesehunt.Main
import dev.byrt.cheesehunt.game.GameState
import dev.byrt.cheesehunt.state.Sounds
import dev.byrt.cheesehunt.util.DevStatus
import io.papermc.paper.command.brigadier.CommandSourceStack

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class BuildMode {
    private val buildToggleSuccessSound: Sound = Sound.sound(Key.key(Sounds.Command.BUILDMODE_SUCCESS), Sound.Source.MASTER, 1f, 1f)
    private val buildToggleFailSound: Sound = Sound.sound(Key.key(Sounds.Command.BUILDMODE_FAIL), Sound.Source.MASTER, 1f, 0f)
    @Command("buildmode")
    @Permission("cheesehunt.buildmode")
    fun start(css: CommandSourceStack) {
        if(Main.getGame().gameManager.getGameState() == GameState.IDLE) {
            if(Main.getGame().getBuildMode()) {
                for(player in Bukkit.getOnlinePlayers()) {
                    if(player.isOp) {
                        player.playSound(buildToggleSuccessSound)
                    }
                }
                Main.getGame().dev.parseDevMessage("Building disabled by ${css.sender.name}.", DevStatus.INFO_FAIL)
                Main.getGame().setBuildMode(false)
            } else {
                for(player in Bukkit.getOnlinePlayers()) {
                    if(player.isOp) {
                        player.playSound(buildToggleSuccessSound)
                    }
                }
                Main.getGame().dev.parseDevMessage("Building enabled by ${css.sender.name}.", DevStatus.INFO_SUCCESS)
                Main.getGame().setBuildMode(true)
            }
        } else {
            css.sender.sendMessage(Component.text("You can only toggle Build Mode when the game is idle.").color(NamedTextColor.RED))
            css.sender.playSound(buildToggleFailSound)
        }
    }
}