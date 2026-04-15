package dev.byrt.cheesehunt.command

import dev.byrt.cheesehunt.Main
import dev.byrt.cheesehunt.util.DevStatus
import dev.byrt.cheesehunt.state.Sounds
import io.papermc.paper.command.brigadier.CommandSourceStack

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class Announce {
    private val mm = MiniMessage.miniMessage()
    @Command("announce <text>")
    @Permission("cheesehunt.announce")
    fun announce(css: CommandSourceStack, @Argument("text") text: Array<String>) {
        Main.getGame().dev.parseDevMessage("Announcement sent by ${css.sender.name}.", DevStatus.INFO)
        val rawAnnounceMessage = text.joinToString(" ")
        for(player in Bukkit.getServer().onlinePlayers) {
            player.playSound(player.location, Sounds.Alert.GENERAL_ALERT, 1.0f, 1.0f)
            player.sendMessage(Component.text("\n-----------------------------------------------------\n\n", NamedTextColor.GREEN, TextDecoration.STRIKETHROUGH))
            player.sendMessage(
                Component.text("[", NamedTextColor.WHITE).decoration(TextDecoration.STRIKETHROUGH, false)
                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                    .append(Component.text("] ", NamedTextColor.WHITE))
                    .append(mm.deserialize(rawAnnounceMessage))
                )
            player.sendMessage(Component.text("\n\n-----------------------------------------------------\n", NamedTextColor.GREEN, TextDecoration.STRIKETHROUGH))
        }
    }
}