package dev.byrt.cheesehunt

import dev.byrt.cheesehunt.command.BaseCommand
import dev.byrt.cheesehunt.game.Game
import dev.byrt.cheesehunt.manager.Maps
import dev.byrt.cheesehunt.plugin.PluginMessenger

import cloud.commandframework.annotations.AnnotationParser
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.extra.confirmation.CommandConfirmationManager
import cloud.commandframework.meta.CommandMeta
import cloud.commandframework.meta.SimpleCommandMeta
import cloud.commandframework.paper.PaperCommandManager

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.messaging.Messenger
import org.incendo.interfaces.paper.PaperInterfaceListeners

import org.reflections.Reflections

import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

private lateinit var game : Game
private lateinit var messenger : Messenger

class Main : JavaPlugin() {
    override fun onEnable() {
        logger.info("Starting Cheese Hunt plugin...")
        game = Game(this)
        game.setup()
        setupCommands()
        setupEventListeners()
        setupConfigs()
        setupPluginMessageListener()
        setupInterfaces()
    }

    override fun onDisable() {
        logger.info("Cleaning up Cheese Hunt plugin...")
        game.cleanUp()
    }

    private fun setupCommands() {
        logger.info("Setting up commands...")
        val commandManager: PaperCommandManager<CommandSender> = try {
            PaperCommandManager.createNative(
                this,
                CommandExecutionCoordinator.simpleCoordinator()
            )
        } catch (e: Exception) {
            logger.severe("Failed to initialize the command manager.")
            server.pluginManager.disablePlugin(this)
            return
        }

        commandManager.registerAsynchronousCompletions()
        commandManager.registerBrigadier()
        setupCommandConfirmation(commandManager)

        // Thanks broccolai <3 https://github.com/broccolai/tickets/commit/e8c227abc298d1a34094708a24601d006ec25937
        commandManager.commandSuggestionProcessor { context, strings ->
            var input: String = if (context.inputQueue.isEmpty()) {
                ""
            } else {
                context.inputQueue.peek()
            }
            input = input.lowercase(Locale.getDefault())
            val suggestions: MutableList<String> = LinkedList()
            for (suggestion in strings) {
                val suggestionLower = suggestion.lowercase(Locale.getDefault())
                if (suggestionLower.startsWith(input)) {
                    suggestions.add(suggestion)
                }
            }
            suggestions
        }

        val reflections = Reflections("dev.byrt.cheesehunt.command")
        val commands = reflections.getSubTypesOf(BaseCommand::class.java)

        val annotationParser = AnnotationParser(
            commandManager,
            CommandSender::class.java
        ) { SimpleCommandMeta.empty() }

        commands.forEach { command ->
            run {
                val instance = command.getConstructor().newInstance()
                annotationParser.parse(instance)
            }
        }
    }

    private fun setupCommandConfirmation(commandManager : PaperCommandManager<CommandSender>) {
        logger.info("Setting up command confirmation...")
        try {
            val confirmationManager : CommandConfirmationManager<CommandSender> = CommandConfirmationManager(
                30L, TimeUnit.SECONDS,
                { context -> context.commandContext.sender.sendMessage(Component.text("Confirm command ", NamedTextColor.RED).append(Component.text("'/${context.command}' ", NamedTextColor.GREEN)).append(Component.text("by running ", NamedTextColor.RED)).append(Component.text("'/confirm' ", NamedTextColor.YELLOW)).append(Component.text("to execute.", NamedTextColor.RED))) },
                { sender -> sender.sendMessage(Component.text("You do not have any pending commands.", NamedTextColor.RED)) }
            )
            confirmationManager.registerConfirmationProcessor(commandManager)

            commandManager.command(commandManager.commandBuilder("confirm")
                .meta(CommandMeta.DESCRIPTION, "Confirm a pending command.")
                .handler(confirmationManager.createConfirmationExecutionHandler())
                .permission("cheesehunt.confirm"))

        } catch (e : Exception) {
            logger.severe("Failed to initialize command confirmation manager.")
            return
        }
    }

    private fun setupEventListeners() {
        logger.info("Setting up event listeners...")
        val reflections = Reflections("dev.byrt.cheesehunt.event")
        val listeners = reflections.getSubTypesOf(Listener::class.java)

        listeners.forEach(Consumer { listener: Class<out Listener> ->
            try {
                val instance = listener.getConstructor().newInstance()
                server.pluginManager.registerEvents(instance, this)
            } catch (e: java.lang.Exception) {
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

    private fun setupInterfaces() {
        logger.info("Setting up interfaces...")
        PaperInterfaceListeners.install(this)
    }

    private fun setupConfigs() {
        logger.info("Setting up configurations...")
        game.configManager.setup()
        game.mapManager.setCurrentMap(null, Maps.REFORGED)
    }

    companion object {
        fun getPlugin(): Plugin { return Bukkit.getPluginManager().getPlugin("CheeseHunt") as Plugin }
        fun getGame(): Game { return game }
        fun getMessenger(): Messenger { return messenger }
    }
}