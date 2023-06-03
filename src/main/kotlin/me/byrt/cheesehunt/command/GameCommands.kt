package me.byrt.cheesehunt.command

import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission
import cloud.commandframework.annotations.Confirmation

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.state.Sounds
import me.byrt.cheesehunt.state.*
import me.byrt.cheesehunt.util.DevStatus

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title

import org.bukkit.Bukkit
import org.bukkit.entity.Player

import java.time.Duration

@Suppress("unused")
class GameCommands : BaseCommand {
    private val startGameSuccessSound: Sound = Sound.sound(Key.key(Sounds.Start.START_GAME_SUCCESS), Sound.Source.MASTER, 1f, 1f)
    private val startGameFailSound: Sound = Sound.sound(Key.key(Sounds.Start.START_GAME_FAIL), Sound.Source.MASTER, 1f, 0f)
    private val reloadStartSound: Sound = Sound.sound(Key.key(Sounds.Command.SHUFFLE_START), Sound.Source.MASTER, 1f, 1f)
    private val reloadCompleteSound: Sound = Sound.sound(Key.key(Sounds.Command.SHUFFLE_COMPLETE), Sound.Source.MASTER, 1f, 2f)

    @CommandMethod("game start")
    @CommandDescription("Starts a game of Cheese Hunt.")
    @CommandPermission("cheesehunt.startgame")
    @Confirmation
    fun start(sender : Player) {
        if(Main.getGame().gameManager.getGameState() == GameState.IDLE) {
            if(Main.getGame().teamManager.getRedTeam().size >= 1 && Main.getGame().teamManager.getBlueTeam().size >= 1) {
                Main.getGame().dev.parseDevMessage("${sender.name} started a Cheese Hunt game!", DevStatus.INFO_SUCCESS)
                Main.getGame().startGame()
                for(player in Bukkit.getOnlinePlayers()) {
                    player.sendMessage(Component.text("\nA Cheese Hunt game is starting!\n").color(NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true))
                    player.playSound(startGameSuccessSound)
                }
                Main.getGame().scoreManager.setNewRandomMultiplierMinute()
            } else {
                sender.sendMessage(Component.text("There are not enough players on teams to start a Cheese Hunt game.").color(NamedTextColor.RED))
                sender.playSound(startGameFailSound)
            }
        } else if(Main.getGame().gameManager.getGameState() == GameState.GAME_END) {
            sender.sendMessage(Component.text("A restart is required for a new Cheese Hunt game.").color(NamedTextColor.RED))
            sender.playSound(startGameFailSound)
        } else {
            sender.sendMessage(Component.text("There is already a Cheese Hunt game running, you numpty!").color(NamedTextColor.RED))
            sender.playSound(startGameFailSound)
        }
    }

    @CommandMethod("game force start")
    @CommandDescription("Force starts the game, may have unintended consequences.")
    @CommandPermission("cheesehunt.force.start")
    @Confirmation
    fun forceStartGame(sender : Player) {
        if(Main.getGame().gameManager.getGameState() == GameState.IDLE) {
            Main.getGame().dev.parseDevMessage("${sender.name} forcefully started a Cheese Hunt game!", DevStatus.INFO_SUCCESS)
            Main.getGame().startGame()
            for(player in Bukkit.getOnlinePlayers()) {
                player.sendMessage(Component.text("\nA Cheese Hunt game is starting!\n").color(NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true))
                player.playSound(startGameSuccessSound)
            }
            Main.getGame().scoreManager.setNewRandomMultiplierMinute()
        } else {
            sender.sendMessage(Component.text("Game already running or restart required.").color(NamedTextColor.RED))
            sender.playSound(startGameFailSound)
        }
    }

    @CommandMethod("game force stop")
    @CommandDescription("Force stops the game, may have unintended consequences.")
    @CommandPermission("cheesehunt.force.stop")
    @Confirmation
    fun forceStopGame(sender : Player) {
        if(Main.getGame().gameManager.getGameState() != GameState.IDLE) {
            if(Main.getGame().gameManager.getGameState() != GameState.GAME_END) {
                Main.getGame().dev.parseDevMessage("${sender.name} force stopped the current Cheese Hunt game.", DevStatus.INFO_SUCCESS)
                Main.getGame().stopGame()
            } else {
                sender.sendMessage(Component.text("Game currently ending.").color(NamedTextColor.RED))
            }
        } else {
            sender.sendMessage(Component.text("Game not running or restart required.").color(NamedTextColor.RED))
            sender.playSound(startGameFailSound)
        }
    }

    @CommandMethod("game reload")
    @CommandDescription("Allows the executing player to reset the game.")
    @CommandPermission("cheesehunt.reloadgame")
    @Confirmation
    fun reloadGame(sender : Player) {
        if(Main.getGame().gameManager.getGameState() == GameState.GAME_END && Main.getGame().timerManager.getTimerState() == TimerState.INACTIVE) {
            sender.showTitle(Title.title(Component.text(""), Component.text("Reloading...", NamedTextColor.RED), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(3), Duration.ofSeconds(1))))
            sender.playSound(reloadStartSound)
            Main.getGame().reload()
            sender.showTitle(Title.title(Component.text(""), Component.text("Game reloaded!", NamedTextColor.GREEN), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(1))))
            sender.playSound(reloadCompleteSound)
            Main.getGame().dev.parseDevMessage("${sender.name} reloaded the game.", DevStatus.INFO)
        } else {
            sender.sendMessage(Component.text("Unable to reload game.", NamedTextColor.RED))
        }
    }

    @CommandMethod("game toggle overtime")
    @CommandDescription("Toggles whether overtime should occur or not.")
    @CommandPermission("cheesehunt.overtime")
    fun overtime(sender : Player) {
        if(Main.getGame().gameManager.getGameState() == GameState.IDLE) {
            if(Main.getGame().gameManager.isOvertimeActive()) {
                Main.getGame().gameManager.setOvertimeState(false)
                Main.getGame().dev.parseDevMessage("Overtime disabled for next game by ${sender.name}.", DevStatus.INFO_FAIL)
                sender.playSound(reloadStartSound)
            } else {
                Main.getGame().gameManager.setOvertimeState(true)
                Main.getGame().dev.parseDevMessage("Overtime enabled for next game by ${sender.name}.", DevStatus.INFO_SUCCESS)
                sender.playSound(reloadCompleteSound)
            }
        } else {
            sender.sendMessage(Component.text("Unable to change overtime toggle in this state.").color(NamedTextColor.RED))
            sender.playSound(startGameFailSound)
        }
    }
}