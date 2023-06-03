package me.byrt.cheesehunt.command

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.state.GameState
import me.byrt.cheesehunt.state.TimerState
import me.byrt.cheesehunt.util.DevStatus

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.entity.Player

@Suppress("unused")
class Timer : BaseCommand {
    @CommandMethod("timer <setting> [seconds]")
    @CommandDescription("Allows timer modification.")
    @CommandPermission("cheesehunt.timer")
    fun timer(sender : Player, @Argument("setting") option : TimerOptions, @Argument("seconds") time : Int?) {
        if(Main.getGame().timerManager.getTimerState() == TimerState.INACTIVE) {
            sender.sendMessage(Component.text("Unable to manipulate timer when inactive.").color(NamedTextColor.RED))
        } else {
            when(option) {
                TimerOptions.SET -> {
                    if(Main.getGame().gameManager.getGameState() == GameState.IDLE) {
                        sender.sendMessage(Component.text("Unable to set timer when no game active.").color(NamedTextColor.RED))
                    } else {
                        if(time == null || time <= 0) {
                            sender.sendMessage(Component.text("Unable to set timer to zero, less than zero or null.").color(NamedTextColor.RED))
                        } else {
                            Main.getGame().gameTask.setTimeLeft(time, sender)
                        }
                    }
                }
                TimerOptions.PAUSE -> {
                    if(Main.getGame().timerManager.getTimerState() != TimerState.ACTIVE) {
                        sender.sendMessage(Component.text("Unable to pause timer when inactive.").color(NamedTextColor.RED))
                    } else {
                        Main.getGame().timerManager.setTimerState(TimerState.PAUSED)
                        Main.getGame().dev.parseDevMessage("Timer state updated to ${Main.getGame().timerManager.getTimerState()} by ${sender.name}.", DevStatus.INFO)
                    }
                }
                TimerOptions.RESUME -> {
                    if(Main.getGame().timerManager.getTimerState() != TimerState.PAUSED) {
                        sender.sendMessage(Component.text("Unable to resume timer when already active.").color(NamedTextColor.RED))
                    } else {
                        Main.getGame().timerManager.setTimerState(TimerState.ACTIVE)
                        Main.getGame().dev.parseDevMessage("Timer state updated to ${Main.getGame().timerManager.getTimerState()} by ${sender.name}.", DevStatus.INFO)
                    }
                }
                TimerOptions.SKIP -> {
                    if(Main.getGame().timerManager.getTimerState() != TimerState.INACTIVE) {
                        Main.getGame().gameTask.setTimeLeft(1, sender)
                    } else {
                        sender.sendMessage(Component.text("Unable to skip timer during this state.").color(NamedTextColor.RED))
                    }
                }
            }
        }
    }

    @CommandMethod("p")
    @CommandDescription("Allows quick timer pausing.")
    @CommandPermission("cheesehunt.timer.pause")
    fun quickPause(sender : Player) {
        if(Main.getGame().timerManager.getTimerState() != TimerState.ACTIVE) {
            sender.sendMessage(Component.text("Unable to pause timer when timer is not active.").color(NamedTextColor.RED))
        } else {
            Main.getGame().timerManager.setTimerState(TimerState.PAUSED)
            Main.getGame().dev.parseDevMessage("Timer state updated to ${Main.getGame().timerManager.getTimerState()} by ${sender.name}.", DevStatus.INFO)
        }
    }

    @CommandMethod("s")
    @CommandDescription("Allows quick timer resuming.")
    @CommandPermission("cheesehunt.timer.resume")
    fun quickResume(sender : Player) {
        if(Main.getGame().timerManager.getTimerState() != TimerState.PAUSED) {
            sender.sendMessage(Component.text("Unable to resume timer when already active.").color(NamedTextColor.RED))
        } else {
            Main.getGame().timerManager.setTimerState(TimerState.ACTIVE)
            Main.getGame().dev.parseDevMessage("Timer state updated to ${Main.getGame().timerManager.getTimerState()} by ${sender.name}.", DevStatus.INFO)
        }
    }
}

enum class TimerOptions {
    SET,
    PAUSE,
    RESUME,
    SKIP
}