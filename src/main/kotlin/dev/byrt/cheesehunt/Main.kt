package dev.byrt.cheesehunt

import dev.byrt.cheesehunt.game.Game
import dev.byrt.cheesehunt.manager.Maps
import dev.byrt.cheesehunt.manager.WhitelistGroup
import dev.byrt.cheesehunt.plugin.PluginMessenger

import io.papermc.paper.command.brigadier.CommandSourceStack

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.messaging.Messenger

import org.incendo.cloud.annotations.AnnotationParser
import org.incendo.cloud.description.CommandDescription
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.paper.PaperCommandManager
import org.incendo.cloud.processors.cache.SimpleCache
import org.incendo.cloud.processors.confirmation.ConfirmationConfiguration
import org.incendo.cloud.processors.confirmation.ConfirmationManager
import org.incendo.cloud.processors.confirmation.annotation.ConfirmationBuilderModifier

import org.reflections.Reflections

import java.lang.Exception
import java.time.Duration
import java.util.function.Consumer

private lateinit var game : Game
private lateinit var messenger : Messenger

@Suppress("unstableApiUsage")
class Main : JavaPlugin() {
    private lateinit var commandManager: PaperCommandManager<CommandSourceStack>
    private lateinit var annotationParser: AnnotationParser<CommandSourceStack>
    override fun onEnable() {
        logger.info("Starting Cheese Hunt plugin...")
        game = Game(this)
        game.setup()
        setupCommands()
        setupEventListeners()
        setupConfigs()
        setupPluginMessageListener()
    }

    override fun onDisable() {
        logger.info("Cleaning up Cheese Hunt plugin...")
        game.cleanUp()
    }

    private fun setupCommands() {
        logger.info("Registering commands.")
        commandManager = PaperCommandManager.builder()
            .executionCoordinator(ExecutionCoordinator.simpleCoordinator())
            .buildOnEnable(this)

        annotationParser = AnnotationParser(commandManager, CommandSourceStack::class.java)
        annotationParser.parseContainers()

        setupCommandConfirmation()
    }

    private fun setupCommandConfirmation() {
        logger.info("Setting up command confirmation.")
        val confirmationConfig = ConfirmationConfiguration.builder<CommandSourceStack>()
            .cache(SimpleCache.of())
            .noPendingCommandNotifier { css ->
                css.sender.sendMessage(
                    Component.text(
                        "You do not have any pending commands.",
                        NamedTextColor.RED
                    )
                ) }
            .confirmationRequiredNotifier { css, ctx ->
                css.sender.sendMessage(
                    Component.text("Confirm command ", NamedTextColor.RED).append(
                        Component.text("'/${ctx.commandContext()}' ", NamedTextColor.GREEN)
                    ).append(Component.text("by running ", NamedTextColor.RED)).append(
                        Component.text("'/confirm' ", NamedTextColor.YELLOW)
                    ).append(Component.text("to execute.", NamedTextColor.RED))
                ) }
            .expiration(Duration.ofSeconds(30))
            .build()

        val confirmationManager = ConfirmationManager.confirmationManager(confirmationConfig)
        commandManager.registerCommandPostProcessor(confirmationManager.createPostprocessor())

        commandManager.command(
            commandManager.commandBuilder("confirm")
                .handler(confirmationManager.createExecutionHandler())
                .commandDescription(CommandDescription.commandDescription("Confirm a pending command."))
                .permission("cheesehunt.confirm")
                .build()
        )
        ConfirmationBuilderModifier.install(annotationParser)
    }

    private fun setupEventListeners() {
        logger.info("Registering events.")
        val reflections = Reflections("dev.byrt.cheesehunt.event")
        val listeners = reflections.getSubTypesOf(Listener::class.java)

        listeners.forEach(Consumer { listener : Class<out Listener> ->
                try {
                    val instance = listener.getConstructor().newInstance()
                    server.pluginManager.registerEvents(instance, this)
                } catch (e : Exception) {
                    e.printStackTrace()
                }
            }
        )
    }

    private fun setupPluginMessageListener() {
        logger.info("Setting up plugin message channels...")
        messenger = Bukkit.getMessenger()
        messenger.registerIncomingPluginChannel(this, "minecraft:brand", PluginMessenger())
    }

    private fun setupConfigs() {
        logger.info("Setting up configurations...")
        game.configManager.setup()
        game.whitelistManager.setWhitelist(WhitelistGroup.ADMIN)
        game.mapManager.setCurrentMap(null, Maps.REFORGED)
    }

    companion object {
        fun getPlugin(): Plugin { return Bukkit.getPluginManager().getPlugin("CheeseHunt") as Plugin }
        fun getGame(): Game { return game }
        fun getMessenger(): Messenger { return messenger }
    }
}