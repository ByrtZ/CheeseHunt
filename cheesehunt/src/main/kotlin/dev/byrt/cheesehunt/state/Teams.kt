package dev.byrt.cheesehunt.state

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

enum class Teams(val teamName: Component) {
    RED(Component.text("Red Team", NamedTextColor.RED)),
    BLUE(Component.text("Blue Team", NamedTextColor.BLUE)),
    SPECTATOR(Component.text("Spectators", NamedTextColor.GRAY)),
}
