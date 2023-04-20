package me.byrt.cheesehunt.command

import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.Sounds
import me.byrt.cheesehunt.state.*

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
    fun start(sender : Player) {
        if(Main.getGame().getGameState() == GameState.IDLE) {
            if(Main.getGame().getTeamManager().getRedTeam().size >= 1 && Main.getGame().getTeamManager().getBlueTeam().size >= 1) {
                sender.sendMessage(Component.text("Starting Cheese Hunt game!").color(NamedTextColor.GREEN))
                Main.getGame().setGameState(GameState.STARTING)
                for(player in Bukkit.getOnlinePlayers()) {
                    player.sendMessage(Component.text("\nA Cheese Hunt game is starting!\n").color(NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true))
                    player.playSound(startGameSuccessSound)
                }
                Main.getGame().getScoreManager().setNewRandomMultiplierMinute()
            } else {
                sender.sendMessage(Component.text("There are not enough players on teams to start a Cheese Hunt game.").color(NamedTextColor.RED))
                sender.playSound(startGameFailSound)
            }
        } else if(Main.getGame().getGameState() == GameState.GAME_END) {
            sender.sendMessage(Component.text("A restart is required for a new Cheese Hunt game.").color(NamedTextColor.RED))
            sender.playSound(startGameFailSound)
        } else {
            sender.sendMessage(Component.text("There is already a Cheese Hunt game running, you numpty!").color(NamedTextColor.RED))
            sender.playSound(startGameFailSound)
        }
    }

    @CommandMethod("game reload")
    @CommandDescription("Allows the executing player to reset the game.")
    @CommandPermission("cheesehunt.reloadgame")
    fun reloadGame(sender : Player) {
        if(Main.getGame().getGameState() == GameState.GAME_END && Main.getGame().getTimerState() == TimerState.INACTIVE) {
            sender.showTitle(Title.title(Component.text(""), Component.text("Reloading...", NamedTextColor.RED), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(3), Duration.ofSeconds(1))))
            sender.playSound(reloadStartSound)
            Main.getGame().setGameState(GameState.IDLE)
            Main.getGame().setRoundState(RoundState.ROUND_ONE)
            Main.getGame().setTimerState(TimerState.INACTIVE)
            Main.getGame().getCheeseManager().resetVars()
            Main.getGame().getGameCountdownTask().resetVars()
            Main.getGame().getBlockManager().resetAllBlocks()
            Main.getGame().getInfoBoardManager().destroyScoreboard()
            Main.getGame().getInfoBoardManager().buildScoreboard()
            Main.getGame().getPlayerManager().resetPlayers()
            Main.getGame().getLocationManager().resetSpawnCounters()
            Main.getGame().getScoreManager().resetScores()
            Main.getGame().getStatsManager().resetStats()
            for(player in Bukkit.getOnlinePlayers()) {
                Main.getGame().getTeamManager().addToTeam(player, player.uniqueId, Teams.SPECTATOR)
                if(player.isOp) {
                    Main.getGame().getTeamManager().addToAdminDisplay(player.uniqueId)
                }
            }
            sender.showTitle(Title.title(Component.text(""), Component.text("Game reloaded!", NamedTextColor.GREEN), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(1))))
            sender.playSound(reloadCompleteSound)
        } else {
            sender.sendMessage(Component.text("Unable to reload game.", NamedTextColor.RED))
        }
    }
}