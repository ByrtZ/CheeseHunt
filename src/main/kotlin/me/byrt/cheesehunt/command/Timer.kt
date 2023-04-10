package me.byrt.cheesehunt.command

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.state.GameState
import me.byrt.cheesehunt.state.TimerState

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
        if(Main.getGame().getTimerState() == TimerState.INACTIVE) {
            sender.sendMessage(Component.text("Unable to manipulate timer when inactive.").color(NamedTextColor.RED))
        } else {
            when(option) {
                TimerOptions.SET -> {
                    if(Main.getGame().getGameState() == GameState.IDLE) {
                        sender.sendMessage(Component.text("Unable to set timer when no game active.").color(NamedTextColor.RED))
                    } else {
                        if(time == null || time <= 0) {
                            sender.sendMessage(Component.text("Unable to set timer to zero, less than zero or null.").color(NamedTextColor.RED))
                        } else {
                            sender.sendMessage(Component.text("Timer set to $time seconds.").color(NamedTextColor.GREEN))
                            Main.getGame().getGameCountdownTask().setTimeLeft(time)
                        }
                    }
                }
                TimerOptions.PAUSE -> {
                    if(Main.getGame().getTimerState() != TimerState.ACTIVE) {
                        sender.sendMessage(Component.text("Unable to pause timer when inactive.").color(NamedTextColor.RED))
                    } else {
                        Main.getGame().getGameCountdownTask().pauseCountdownTask()
                        sender.sendMessage(Component.text("Updated timer state to ${Main.getGame().getTimerState()}.").color(NamedTextColor.GRAY))
                    }
                }
                TimerOptions.RESUME -> {
                    if(Main.getGame().getTimerState() != TimerState.PAUSED) {
                        sender.sendMessage(Component.text("Unable to resume timer when already active.").color(NamedTextColor.RED))
                    } else {
                        Main.getGame().getGameCountdownTask().resumeCountdownTask()
                        sender.sendMessage(Component.text("Updated timer state to ${Main.getGame().getTimerState()}.").color(NamedTextColor.GRAY))
                    }
                }
                TimerOptions.SKIP -> {
                    if(Main.getGame().getTimerState() != TimerState.INACTIVE) {
                        sender.sendMessage(Component.text("Skipped current timer.").color(NamedTextColor.GRAY))
                        Main.getGame().getGameCountdownTask().setTimeLeft(1)
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
        if(Main.getGame().getTimerState() != TimerState.ACTIVE) {
            sender.sendMessage(Component.text("Unable to pause timer when timer is not active.").color(NamedTextColor.RED))
        } else {
            Main.getGame().getGameCountdownTask().pauseCountdownTask()
            sender.sendMessage(Component.text("Updated timer state to ${Main.getGame().getTimerState()}.").color(NamedTextColor.GRAY))
        }
    }

    @CommandMethod("s")
    @CommandDescription("Allows quick timer resuming.")
    @CommandPermission("cheesehunt.timer.resume")
    fun quickResume(sender : Player) {
        if(Main.getGame().getTimerState() != TimerState.PAUSED) {
            sender.sendMessage(Component.text("Unable to resume timer when already active.").color(NamedTextColor.RED))
        } else {
            Main.getGame().getGameCountdownTask().resumeCountdownTask()
            sender.sendMessage(Component.text("Updated timer state to ${Main.getGame().getTimerState()}.").color(NamedTextColor.GRAY))
        }
    }
}

enum class TimerOptions {
    SET,
    PAUSE,
    RESUME,
    SKIP
}