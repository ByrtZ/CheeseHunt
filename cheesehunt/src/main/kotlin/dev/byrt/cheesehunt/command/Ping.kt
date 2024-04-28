package dev.byrt.cheesehunt.command

import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod

import dev.byrt.cheesehunt.state.Sounds
import me.lucyydotp.cheeselib.module.Module
import me.lucyydotp.cheeselib.module.ModuleHolder
import me.lucyydotp.cheeselib.module.installCommands

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.entity.Player

@Suppress("unused")
class Ping(parent: ModuleHolder) : Module(parent) {

    init {
        installCommands()
    }

    private val pingSound: Sound = Sound.sound(Key.key(Sounds.Command.PING), Sound.Source.MASTER, 1f, 1f)
    @CommandMethod("ping")
    @CommandDescription("Returns the executing player's ping")
    fun ping(sender : Player) {
        sender.playSound(pingSound)
        sender.sendMessage(Component.text("Ping: ")
            .color(NamedTextColor.AQUA)
            .decoration(TextDecoration.BOLD, true)
            .append(Component.text("${sender.ping}ms")
                .color(NamedTextColor.WHITE)
                .decoration(TextDecoration.BOLD, false)))
    }
}
