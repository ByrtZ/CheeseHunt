package me.byrt.cheesehunt.command

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.state.GameState

import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission
import me.byrt.cheesehunt.state.Sounds

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.Bukkit
import org.bukkit.entity.Player

@Suppress("unused")
class Buildmode : BaseCommand {
    private val buildToggleSuccessSound: Sound = Sound.sound(Key.key(Sounds.Command.BUILDMODE_SUCCESS), Sound.Source.MASTER, 1f, 1f)
    private val buildToggleFailSound: Sound = Sound.sound(Key.key(Sounds.Command.BUILDMODE_FAIL), Sound.Source.MASTER, 1f, 0f)
    @CommandMethod("buildmode")
    @CommandDescription("Toggles the ability to build for operators while not in game.")
    @CommandPermission("cheesehunt.buildmode")
    fun start(sender : Player) {
        if(Main.getGame().getGameState() == GameState.IDLE) {
            if(Main.getGame().getBuildMode()) {
                for(player in Bukkit.getOnlinePlayers()) {
                    if(player.isOp) {
                        player.sendMessage(Component.text("Building has been disabled by ${sender.name}.").color(NamedTextColor.RED))
                        player.playSound(buildToggleSuccessSound)
                    }
                    Main.getGame().setBuildMode(false)
                }
            } else {
                for(player in Bukkit.getOnlinePlayers()) {
                    if(player.isOp) {
                        player.sendMessage(Component.text("Building has been enabled by ${sender.name}.").color(NamedTextColor.GREEN))
                        player.playSound(buildToggleSuccessSound)
                    }
                    Main.getGame().setBuildMode(true)
                }
            }
        } else {
            sender.sendMessage(Component.text("You can only toggle Build Mode when the game is idle.").color(NamedTextColor.RED))
            sender.playSound(buildToggleFailSound)
        }
    }
}