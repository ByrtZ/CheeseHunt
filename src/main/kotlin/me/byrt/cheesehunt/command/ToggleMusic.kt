package me.byrt.cheesehunt.command

import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.GameState

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.Bukkit
import org.bukkit.entity.Player

@Suppress("unused")
class ToggleMusic : BaseCommand {
    private val musicToggleSuccessSound: Sound = Sound.sound(Key.key("entity.warden.death"), Sound.Source.MASTER, 1f, 1.75f)
    private val musicToggleFailSound: Sound = Sound.sound(Key.key("entity.enderman.teleport"), Sound.Source.MASTER, 1f, 0f)
    @CommandMethod("togglemusic")
    @CommandDescription("Toggles the looping music.")
    @CommandPermission("cheesehunt.togglemusic")
    fun start(sender : Player) {
        if(Main.getGame().getGameState() == GameState.IDLE) {
            if(Main.getGame().getMusicLooper()) {
                for(player in Bukkit.getOnlinePlayers()) {
                    if(player.isOp) {
                        player.sendMessage(Component.text("Looping music has been disabled by ${sender.name}.\nPlayers joining from now on will not receive a music loop.").color(NamedTextColor.RED))
                        player.playSound(musicToggleSuccessSound)
                    }
                    Main.getGame().getMusicLoop().removeFromMusicLoop(player)
                    player.stopSound("event.downtime.loop")
                }
                Main.getGame().setMusicLooper(false)
            } else {
                for(player in Bukkit.getOnlinePlayers()) {
                    if(player.isOp) {
                        player.sendMessage(Component.text("Looping music has been enabled by ${sender.name}.\nLeave and rejoin the server to start your music loop!").color(NamedTextColor.GREEN))
                        player.playSound(musicToggleSuccessSound)
                    }
                }
                Main.getGame().setMusicLooper(true)
            }
        } else {
            sender.sendMessage(Component.text("You can only toggle Music Looping when the game is idle.").color(NamedTextColor.RED))
            sender.playSound(musicToggleFailSound)
        }
    }
}