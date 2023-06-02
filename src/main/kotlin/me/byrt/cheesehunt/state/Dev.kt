package me.byrt.cheesehunt.state

import me.byrt.cheesehunt.manager.Game

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.Bukkit

@Suppress("unused")
class Dev(private val game : Game) {
    fun parseDevMessage(message : String, status : DevStatus) {
        val parsed = Component.text("\uD001 ", NamedTextColor.WHITE)
            .append(Component.text(message, status.colour))
        sendDevMessage(parsed)
    }

    private fun sendDevMessage(message : Component) {
        for(admin in Bukkit.getOnlinePlayers().filter { player -> player.isOp }) {
            admin.sendMessage(message)
        }
    }
}

@Suppress("unused")
enum class DevStatus(val colour : NamedTextColor) {
    INFO(NamedTextColor.DARK_GRAY),
    INFO_SUCCESS(NamedTextColor.GREEN),
    INFO_FAIL(NamedTextColor.RED),
    WARNING(NamedTextColor.GOLD),
    SEVERE(NamedTextColor.RED),
    ERROR(NamedTextColor.DARK_RED)
}