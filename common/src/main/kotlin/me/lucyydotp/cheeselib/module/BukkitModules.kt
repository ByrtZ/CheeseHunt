package me.lucyydotp.cheeselib.module

import me.lucyydotp.cheeselib.inject.inject
import org.bukkit.Server
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

/**
 * Sets up this module to listen to Bukkit events while enabled.
 */
public fun <T> T.installBukkitListeners() where T : Module, T : Listener {
    onEnable {
        inject<Server>().pluginManager.registerEvents(this, inject<Plugin>())
    }

    onDisable {
        HandlerList.unregisterAll(this)
    }
}
