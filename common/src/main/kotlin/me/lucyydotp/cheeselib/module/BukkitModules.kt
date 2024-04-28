package me.lucyydotp.cheeselib.module

import cloud.commandframework.annotations.AnnotationParser
import me.lucyydotp.cheeselib.inject.context
import me.lucyydotp.cheeselib.inject.inject
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.slf4j.Logger

/**
 * Sets up this module to listen to Bukkit events while enabled.
 */
fun <T> T.installBukkitListeners() where T : Module, T : Listener {
    onEnable {
        inject<Server>().pluginManager.registerEvents(this, inject<Plugin>())
    }

    onDisable {
        HandlerList.unregisterAll(this)
    }
}

/**
 * Sets up this module to handle cloud annotation commands.
 */
fun Module.installCommands() {
    val annotationParser: AnnotationParser<*> by context()
    val logger: Logger by context()

    onEnable {
        annotationParser.parse(this)
    }

    onDisable {
        if (Bukkit.isStopping()) return@onDisable
        logger.warn("Attempted to unload module ${this::class.simpleName} which has command handlers. This is not supported.")
    }
}
