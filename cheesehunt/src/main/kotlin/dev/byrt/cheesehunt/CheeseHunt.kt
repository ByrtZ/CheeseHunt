package dev.byrt.cheesehunt

import dev.byrt.cheesehunt.game.Game
import dev.byrt.cheesehunt.manager.Maps
import me.lucyydotp.cheeselib.game.nameformat.CustomNameTags
import me.lucyydotp.cheeselib.game.nameformat.NameFormatter
import me.lucyydotp.cheeselib.inject.GlobalInjectionContext
import me.lucyydotp.cheeselib.inject.bind
import me.lucyydotp.cheeselib.inject.context
import me.lucyydotp.cheeselib.module.ModuleHolder
import me.lucyydotp.cheeselib.module.ParentModule
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.messaging.Messenger
import org.incendo.interfaces.paper.PaperInterfaceListeners
import org.reflections.Reflections
import org.slf4j.Logger
import java.util.function.Consumer

class CheeseHunt(parent: ModuleHolder) : ParentModule(parent) {

    val logger: Logger by context()
    val plugin: Plugin by context()
    val server: Server by context()

    val game: Game

    init {
        GlobalInjectionContext.bind(NameFormatter(this).registerAsChild())
        game = Game(this).registerAsChild()
        CustomNameTags(this).registerAsChild()

        onEnable {
            setupEventListeners()
            setupConfigs()
            setupPluginMessageListener()
            setupInterfaces()
            game.setup()
        }

        onDisable {
            game.cleanUp()
        }
    }

    @Deprecated("Move event listeners to their own modules")
    private fun setupEventListeners() {
        logger.info("Setting up event listeners...")
        val reflections = Reflections("dev.byrt.cheesehunt.event")
        val listeners = reflections.getSubTypesOf(Listener::class.java)

        listeners.forEach(Consumer { listener: Class<out Listener> ->
            try {
                val instance = listener.getConstructor().newInstance()
                server.pluginManager.registerEvents(instance, plugin)
            } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        )
    }

    private fun setupPluginMessageListener() {
        // FIXME(lucy): no-op
//        logger.info("Setting up plugin message channels...")
//        messenger = Bukkit.getMessenger()
//        messenger.registerIncomingPluginChannel(this, "minecraft:brand", PluginMessenger())
    }

    private fun setupInterfaces() {
        logger.info("Setting up interfaces...")
        PaperInterfaceListeners.install(plugin)
    }

    private fun setupConfigs() {
        logger.info("Setting up configurations...")
        game.configManager.setup()
        game.mapManager.setCurrentMap(null, Maps.REFORGED)
    }

    companion object {
        fun getPlugin(): CheeseHuntPlugin { return Bukkit.getPluginManager().getPlugin("CheeseHunt") as CheeseHuntPlugin }
        @Deprecated("Use contextual DI")
        fun getGame(): Game = getPlugin().module.game
        @Deprecated("Use contextual DI")
        fun getMessenger(): Messenger { TODO() }
    }
}
