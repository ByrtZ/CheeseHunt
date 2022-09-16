package me.byrt.cheesehunt.command

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.GameState

import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit

import org.bukkit.entity.Player

@Suppress("unused")
class StartGame : BaseCommand {
    private val startGameSuccessSound: Sound = Sound.sound(Key.key("block.end_portal.spawn"), Sound.Source.MASTER, 1f, 1f)
    private val startGameFailSound: Sound = Sound.sound(Key.key("entity.enderman.teleport"), Sound.Source.MASTER, 1f, 0f)
    @CommandMethod("startgame")
    @CommandDescription("Starts a game of Cheese Hunt.")
    @CommandPermission("cheesehunt.startgame")
    fun start(sender : Player) {
        if(Main.getGame().getGameState() == GameState.IDLE) {
            if(Main.getGame().getTeamManager().getRedTeam().size >= 1 && Main.getGame().getTeamManager().getBlueTeam().size >= 1) {
                sender.sendMessage(Component.text("Starting Cheese Hunt game!").color(NamedTextColor.GREEN))
                Main.getGame().setGameState(GameState.STARTING)
                for(player in Bukkit.getOnlinePlayers()) {
                    player.sendMessage(Component.text("\nA Cheese Hunt game is starting!\n").color(NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true))
                    player.playSound(startGameSuccessSound)
                }
            } else {
                sender.sendMessage(Component.text("There are not enough players on teams to start a Cheese Hunt game.").color(NamedTextColor.RED))
                sender.playSound(startGameSuccessSound)
            }
        } else if(Main.getGame().getGameState() == GameState.GAME_END) {
            sender.sendMessage(Component.text("A restart is required for a new Cheese Hunt game.").color(NamedTextColor.RED))
            sender.playSound(startGameSuccessSound)
        } else {
            sender.sendMessage(Component.text("There is already a Cheese Hunt game running, you numpty!").color(NamedTextColor.RED))
            sender.playSound(startGameSuccessSound)
        }
    }
}