package me.lucyydotp.cheeselib.common

import cloud.commandframework.CommandManager
import cloud.commandframework.annotations.AnnotationParser
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.extra.confirmation.CommandConfirmationManager
import cloud.commandframework.meta.CommandMeta
import cloud.commandframework.meta.SimpleCommandMeta
import cloud.commandframework.paper.PaperCommandManager
import me.lucyydotp.cheeselib.command.TeamsCommands
import me.lucyydotp.cheeselib.inject.GlobalInjectionContext
import me.lucyydotp.cheeselib.inject.bind
import me.lucyydotp.cheeselib.inject.context
import me.lucyydotp.cheeselib.module.ModuleHolder
import me.lucyydotp.cheeselib.module.ParentModule
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin
import java.util.concurrent.TimeUnit

class Commands(parent: ModuleHolder) : ParentModule(parent) {

    private val plugin: Plugin by context()

    init {
        onEnable {
            setupCommandManager()
            TeamsCommands<Nothing>(this).registerAsChild().enable()
        }
    }

    private fun setupCommandManager() {

        val commandManager: PaperCommandManager<CommandSender> =
            PaperCommandManager.createNative(
                plugin,
                CommandExecutionCoordinator.simpleCoordinator()
            )

        commandManager.registerAsynchronousCompletions()
        commandManager.registerBrigadier()

        val confirmationManager: CommandConfirmationManager<CommandSender> = CommandConfirmationManager(
            30L, TimeUnit.SECONDS,
            { context ->
                context.commandContext.sender.sendMessage(
                    Component.text("Confirm command ", NamedTextColor.RED).append(
                        Component.text("'/${context.command}' ", NamedTextColor.GREEN)
                    ).append(Component.text("by running ", NamedTextColor.RED)).append(
                        Component.text("'/confirm' ", NamedTextColor.YELLOW)
                    ).append(Component.text("to execute.", NamedTextColor.RED))
                )
            },
            { sender ->
                sender.sendMessage(
                    Component.text(
                        "You do not have any pending commands.",
                        NamedTextColor.RED
                    )
                )
            }
        )
        confirmationManager.registerConfirmationProcessor(commandManager)

        commandManager.command(
            commandManager.commandBuilder("confirm")
                .meta(CommandMeta.DESCRIPTION, "Confirm a pending command.")
                .handler(confirmationManager.createConfirmationExecutionHandler())
                .permission("cheesehunt.confirm")
        )


        commandManager.commandSuggestionProcessor { context, strings ->
            val input = context.inputQueue.peek()?.lowercase() ?: ""
            strings.filter { it.lowercase().startsWith(input) }
        }

        val annotationParser = AnnotationParser(
            commandManager,
            CommandSender::class.java
        ) { SimpleCommandMeta.empty() }

        GlobalInjectionContext.bind(annotationParser)
        GlobalInjectionContext.bind<CommandManager<*>>(commandManager)
    }
}
