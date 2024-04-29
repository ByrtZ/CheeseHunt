package me.lucyydotp.cheeselib.sys

import me.lucyydotp.cheeselib.inject.context
import me.lucyydotp.cheeselib.module.Module
import me.lucyydotp.cheeselib.module.ModuleHolder
import net.kyori.adventure.extra.kotlin.plus
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Server
import org.bukkit.entity.Player

public class AdminMessages(
    parent: ModuleHolder,
    private val playerSelector: (Player) -> Boolean,
) : Module(parent) {

    private companion object {
        private val DEV_PREFIX = "\uD001 "
    }

    private val server: Server by context()

    fun sendDevMessage(message: String, style: Style) =
        sendDevMessage(Component.text(message, style))

    fun sendDevMessage(message: Component) {
        val component = Component.text(DEV_PREFIX) + message

        server.onlinePlayers
            .filter(playerSelector)
            .forEach {
                it.sendMessage(component)
            }
    }

}

object AdminMessageStyles {
    val CLIENT_BRAND_INFO = Style.style(TextColor.fromHexString("#db0060"))
    val INFO = Style.style(NamedTextColor.DARK_GRAY)
    val INFO_SUCCESS = Style.style(NamedTextColor.GREEN)
    val INFO_FAIL = Style.style(NamedTextColor.RED)
    val WARNING = Style.style(NamedTextColor.GOLD)
    val SEVERE = Style.style(NamedTextColor.RED)
    val ERROR = Style.style(NamedTextColor.DARK_RED)
}

