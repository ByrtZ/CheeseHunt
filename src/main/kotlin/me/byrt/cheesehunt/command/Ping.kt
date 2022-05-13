package me.byrt.cheesehunt.command

import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.entity.Player

@Suppress("unused")
class Ping : BaseCommand {
    private val pingSound: Sound = Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.MASTER, 1f, 1f)

    @CommandMethod("ping")
    @CommandDescription("Returns the executing player's ping")
    fun start(sender : Player) {
        sender.playSound(pingSound)
        sender.sendMessage(Component.text("Ping: ")
            .color(NamedTextColor.AQUA)
            .decoration(TextDecoration.BOLD, true)
            .append(Component.text("${sender.player?.ping}ms")
                .color(NamedTextColor.WHITE)
                .decoration(TextDecoration.BOLD, false)))
    }
}