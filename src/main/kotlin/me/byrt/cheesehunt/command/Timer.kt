package me.byrt.cheesehunt.command

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.GameState
import me.byrt.cheesehunt.manager.TimerState

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandMethod

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.entity.Player

@Suppress("unused")
class Timer : BaseCommand {
    @CommandMethod("timer <setting> [seconds]")
    fun timer(sender : Player, @Argument("setting") option : TimerOptions, @Argument("seconds") time : Int?) {
        when(option) {
            TimerOptions.SET -> {
                if(Main.getGame()?.getGameState() == GameState.IDLE) {
                    sender.sendMessage(Component.text("You cannot set the timer when there is no game active.").color(NamedTextColor.RED))
                } else {
                    if(time == null || time <= 0) {
                        sender.sendMessage(Component.text("You cannot set the timer to zero, less than zero or leave the argument blank!").color(NamedTextColor.RED))
                    } else {
                        sender.sendMessage(Component.text("You set the timer to $time seconds.").color(NamedTextColor.GREEN))
                        Main.getGame()?.getGameCountdownTask()?.setTimeLeft(time)
                    }
                }
            }
            TimerOptions.PAUSE -> {
                if(Main.getGame()?.getTimerState() != TimerState.ACTIVE) {
                    sender.sendMessage(Component.text("You cannot pause the timer when the timer is not active!").color(NamedTextColor.RED))
                } else {
                    Main.getGame()?.getGameCountdownTask()?.pauseCountdownTask()
                    sender.sendMessage(Component.text("You updated the timer state to ${Main.getGame()?.getTimerState()}").color(NamedTextColor.GRAY))
                }
            }
            TimerOptions.RESUME -> {
                if(Main.getGame()?.getTimerState() != TimerState.PAUSED) {
                    sender.sendMessage(Component.text("You cannot resume the timer when it is already active!").color(NamedTextColor.RED))
                } else {
                    Main.getGame()?.getGameCountdownTask()?.resumeCountdownTask()
                    sender.sendMessage(Component.text("You updated the timer state to ${Main.getGame()?.getTimerState()}").color(NamedTextColor.GRAY))
                }
            }
            TimerOptions.SKIP -> {
                if(Main.getGame()?.getTimerState() != TimerState.INACTIVE) {
                    sender.sendMessage(Component.text("You skipped the current timer.").color(NamedTextColor.GRAY))
                    Main.getGame()?.getGameCountdownTask()?.setTimeLeft(1)
                } else {
                    sender.sendMessage(Component.text("You cannot skip the timer during this state!").color(NamedTextColor.RED))
                }
            }
        }
    }
}

enum class TimerOptions {
    SET,
    PAUSE,
    RESUME,
    SKIP
}