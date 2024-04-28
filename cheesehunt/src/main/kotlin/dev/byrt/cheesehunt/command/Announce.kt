package dev.byrt.cheesehunt.command

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission
import dev.byrt.cheesehunt.state.Sounds
import dev.byrt.cheesehunt.util.Dev
import dev.byrt.cheesehunt.util.DevStatus
import me.lucyydotp.cheeselib.inject.context
import me.lucyydotp.cheeselib.module.Module
import me.lucyydotp.cheeselib.module.ModuleHolder
import me.lucyydotp.cheeselib.module.installCommands
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class Announce(parent: ModuleHolder) : Module(parent) {

    private val dev: Dev by context()
    private val mm = MiniMessage.miniMessage()

    init {
        installCommands()
    }

    @CommandMethod("announce <text>")
    @CommandDescription("Puts a formatted announcement message in chat.")
    @CommandPermission("cheesehunt.announce")
    fun announce(sender : Player, @Argument("text") text: Array<String>) {
        dev.parseDevMessage("Announcement sent by ${sender.name}.", DevStatus.INFO)
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
