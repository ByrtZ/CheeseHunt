package me.byrt.cheesehunt

import me.byrt.cheesehunt.command.BaseCommand
import me.byrt.cheesehunt.manager.Game

import cloud.commandframework.annotations.AnnotationParser
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.meta.SimpleCommandMeta
import cloud.commandframework.paper.PaperCommandManager

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

import org.reflections.Reflections

import java.util.*
import java.util.function.Consumer
import java.util.function.Function

private lateinit var game : Game

@Suppress("unused")
class Main : JavaPlugin() {
    override fun onEnable() {
        logger.info("Starting Cheese Hunt plugin...")
        game = Game(this)
        setupCommands()
        setupEventListeners()
        game.getInfoBoardManager().buildScoreboard()
    }

    override fun onDisable() {
        logger.info("Cleaning up...")
        game.cleanUp()
    }

    private fun setupCommands() {
        val commandManager: PaperCommandManager<CommandSender> = try {
            PaperCommandManager(
                this,
                CommandExecutionCoordinator.simpleCoordinator(),
                Function.identity(),
                Function.identity()
            )
        } catch (e: Exception) {
            logger.severe("Failed to initialize the command manager")
            server.pluginManager.disablePlugin(this)
            return
        }

        commandManager.registerAsynchronousCompletions()
        commandManager.registerBrigadier()
        // Thanks broccolai <3 https://github.com/broccolai/tickets/commit/e8c227abc298d1a34094708a24601d006ec25937
        commandManager.setCommandSuggestionProcessor { context, strings ->
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


        val reflections = Reflections("me.byrt.cheesehunt.command")
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

    private fun setupEventListeners() {
        val reflections = Reflections("me.byrt.cheesehunt.event")
        val listeners = reflections.getSubTypesOf(Listener::class.java)

        listeners.forEach(Consumer { listener: Class<out Listener> ->
            try {
                val instance = listener.getConstructor().newInstance()
                server.pluginManager.registerEvents(instance, this)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        })
    }

    companion object {
        fun getPlugin(): Plugin { return Bukkit.getPluginManager().getPlugin("CheeseHunt") as Plugin }
        fun getGame(): Game { return game }
    }
}