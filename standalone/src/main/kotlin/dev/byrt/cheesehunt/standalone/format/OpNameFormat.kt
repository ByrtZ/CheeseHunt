package dev.byrt.cheesehunt.standalone.format

import me.lucyydotp.cheeselib.game.nameformat.NameFormatter
import me.lucyydotp.cheeselib.inject.context
import me.lucyydotp.cheeselib.module.Module
import me.lucyydotp.cheeselib.module.ModuleHolder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

/**
 * Sets the op and/or spectator badge for players.
 */
class OpNameFormat(parent: ModuleHolder) : Module(parent) {
    private val nameFormatter by context<NameFormatter>()

    init {
        listen(nameFormatter.format, 50) {
            if (it.player.isOp) {
                it.prefixes += Component.text("\uD002 ")
                it.usernameStyle = it.usernameStyle.color(NamedTextColor.DARK_RED)
            } else {
                it.prefixes += Component.text("\uD003 ")
                it.usernameStyle = it.usernameStyle.color(NamedTextColor.GRAY)
            }
        }
    }
}
