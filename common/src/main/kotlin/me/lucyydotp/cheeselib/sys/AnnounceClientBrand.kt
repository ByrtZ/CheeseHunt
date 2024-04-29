package me.lucyydotp.cheeselib.sys

import me.lucyydotp.cheeselib.inject.context
import me.lucyydotp.cheeselib.module.Module
import me.lucyydotp.cheeselib.module.ModuleHolder
import net.kyori.adventure.extra.kotlin.text
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.messaging.PluginMessageListener

/**
 * Announces a client's brand to admins on join.
 */
class AnnounceClientBrand(parent: ModuleHolder) : Module(parent), PluginMessageListener {

    val plugin: Plugin by context()
    val devMessages: AdminMessages by context()

    init {
        onEnable {
            plugin.server.messenger.registerIncomingPluginChannel(plugin, "minecraft:brand", this)
        }
    }

    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {

        text {
            style(AdminMessageStyles.CLIENT_BRAND_INFO)
            append(player.name())
            append(Component.text(" joined on ", NamedTextColor.DARK_GRAY))
            append(Component.text(message.decodeToString().substring(1).replaceFirstChar(Char::uppercaseChar)))
            append(Component.text(".", NamedTextColor.DARK_GRAY))
        }.let {
            devMessages.sendDevMessage(it)
        }
    }
}
