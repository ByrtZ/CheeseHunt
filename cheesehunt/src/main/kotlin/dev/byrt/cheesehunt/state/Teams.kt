package dev.byrt.cheesehunt.state

import me.lucyydotp.cheeselib.sys.TeamMeta
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor

enum class Teams(
    override val displayName: Component,
    override val nameColor: TextColor,
    override val icon: Component,
) : TeamMeta {
    RED(
        Component.text("Red Team", NamedTextColor.RED),
        NamedTextColor.RED,
        Component.text("\uD004")
    ),
    BLUE(
        Component.text("Blue Team", NamedTextColor.BLUE),
        NamedTextColor.BLUE,
        Component.text("\uD005")
    ),
}
