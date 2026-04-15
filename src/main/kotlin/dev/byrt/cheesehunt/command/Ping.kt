package dev.byrt.cheesehunt.command

import dev.byrt.cheesehunt.state.Sounds
import io.papermc.paper.command.brigadier.CommandSourceStack

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.processing.CommandContainer

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class Ping {
    private val pingSound: Sound = Sound.sound(Key.key(Sounds.Command.PING), Sound.Source.MASTER, 1f, 1f)
    @Command("ping")
    @CommandDescription("Returns the executing player's ping")
    fun ping(css: CommandSourceStack) {
        val sender = css.sender as Player
        sender.playSound(pingSound)
        sender.sendMessage(Component.text("Ping: ")
            .color(NamedTextColor.AQUA)
            .decoration(TextDecoration.BOLD, true)
            .append(Component.text("${sender.ping}ms")
                .color(NamedTextColor.WHITE)
                .decoration(TextDecoration.BOLD, false)))
    }
}