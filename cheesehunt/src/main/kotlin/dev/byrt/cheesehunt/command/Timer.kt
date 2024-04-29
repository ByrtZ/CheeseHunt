package dev.byrt.cheesehunt.command

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission
import dev.byrt.cheesehunt.CheeseHunt
import dev.byrt.cheesehunt.game.GameState
import dev.byrt.cheesehunt.state.TimerState
import me.lucyydotp.cheeselib.inject.context
import me.lucyydotp.cheeselib.module.Module
import me.lucyydotp.cheeselib.module.ModuleHolder
import me.lucyydotp.cheeselib.module.installCommands
import me.lucyydotp.cheeselib.sys.AdminMessageStyles
import me.lucyydotp.cheeselib.sys.AdminMessages
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player

@Suppress("unused")
class Timer(parent: ModuleHolder) : Module(parent) {

    init {
        installCommands()
    }

    private val adminMessages: AdminMessages by context()

    @CommandMethod("timer <setting> [seconds]")
    @CommandDescription("Allows timer modification.")
    @CommandPermission("cheesehunt.timer")
    fun timer(sender: Player, @Argument("setting") option: TimerOptions, @Argument("seconds") time: Int?) {
        if (CheeseHunt.getGame().timerManager.getTimerState() == TimerState.INACTIVE) {
            sender.sendMessage(Component.text("Unable to manipulate timer when inactive.").color(NamedTextColor.RED))
        } else {
            when (option) {
                TimerOptions.SET -> {
                    if (CheeseHunt.getGame().gameManager.getGameState() == GameState.IDLE) {
                        sender.sendMessage(
                            Component.text("Unable to set timer when no game active.").color(NamedTextColor.RED)
                        )
                    } else {
                        if (time == null || time <= 0) {
                            sender.sendMessage(
                                Component.text("Unable to set timer to zero, less than zero or null.")
                                    .color(NamedTextColor.RED)
                            )
                        } else {
                            CheeseHunt.getGame().gameTask.setTimeLeft(time, sender)
                        }
                    }
                }

                TimerOptions.PAUSE -> {
                    if (CheeseHunt.getGame().timerManager.getTimerState() != TimerState.ACTIVE) {
                        sender.sendMessage(
                            Component.text("Unable to pause timer when inactive.").color(NamedTextColor.RED)
                        )
                    } else {
                        CheeseHunt.getGame().timerManager.setTimerState(TimerState.PAUSED)
                        adminMessages.sendDevMessage(
                            "Timer state updated to ${CheeseHunt.getGame().timerManager.getTimerState()} by ${sender.name}.",
                            AdminMessageStyles.INFO
                        )
                    }
                }

                TimerOptions.RESUME -> {
                    if (CheeseHunt.getGame().timerManager.getTimerState() != TimerState.PAUSED) {
                        sender.sendMessage(
                            Component.text("Unable to resume timer when already active.").color(NamedTextColor.RED)
                        )
                    } else {
                        CheeseHunt.getGame().timerManager.setTimerState(TimerState.ACTIVE)
                        adminMessages.sendDevMessage(
                            "Timer state updated to ${CheeseHunt.getGame().timerManager.getTimerState()} by ${sender.name}.",
                            AdminMessageStyles.INFO
                        )
                    }
                }

                TimerOptions.SKIP -> {
                    if (CheeseHunt.getGame().timerManager.getTimerState() != TimerState.INACTIVE) {
                        CheeseHunt.getGame().gameTask.setTimeLeft(1, sender)
                    } else {
                        sender.sendMessage(
                            Component.text("Unable to skip timer during this state.").color(NamedTextColor.RED)
                        )
                    }
                }
            }
        }
    }

    @CommandMethod("p")
    @CommandDescription("Allows quick timer pausing.")
    @CommandPermission("cheesehunt.timer.pause")
    fun quickPause(sender: Player) {
        if (CheeseHunt.getGame().timerManager.getTimerState() != TimerState.ACTIVE) {
            sender.sendMessage(
                Component.text("Unable to pause timer when timer is not active.").color(NamedTextColor.RED)
            )
        } else {
            CheeseHunt.getGame().timerManager.setTimerState(TimerState.PAUSED)
            adminMessages.sendDevMessage(
                "Timer state updated to ${CheeseHunt.getGame().timerManager.getTimerState()} by ${sender.name}.",
                AdminMessageStyles.INFO
            )
        }
    }

    @CommandMethod("s")
    @CommandDescription("Allows quick timer resuming.")
    @CommandPermission("cheesehunt.timer.resume")
    fun quickResume(sender: Player) {
        if (CheeseHunt.getGame().timerManager.getTimerState() != TimerState.PAUSED) {
            sender.sendMessage(Component.text("Unable to resume timer when already active.").color(NamedTextColor.RED))
        } else {
            CheeseHunt.getGame().timerManager.setTimerState(TimerState.ACTIVE)
            adminMessages.sendDevMessage(
                "Timer state updated to ${CheeseHunt.getGame().timerManager.getTimerState()} by ${sender.name}.",
                AdminMessageStyles.INFO
            )
        }
    }
}

enum class TimerOptions {
    SET,
    PAUSE,
    RESUME,
    SKIP
}
