package me.byrt.cheesehunt.command

import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.*
import me.byrt.cheesehunt.state.GameState
import me.byrt.cheesehunt.state.RoundState
import me.byrt.cheesehunt.state.Teams
import me.byrt.cheesehunt.state.TimerState

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title

import org.bukkit.Bukkit
import org.bukkit.entity.Player

import java.time.Duration

@Suppress("unused")
class ReloadGame : BaseCommand {
    private val reloadStartSound: Sound = Sound.sound(Key.key(Sounds.Command.SHUFFLE_START), Sound.Source.MASTER, 1f, 1f)
    private val reloadCompleteSound: Sound = Sound.sound(Key.key(Sounds.Command.SHUFFLE_COMPLETE), Sound.Source.MASTER, 1f, 2f)
    @CommandMethod("reloadgame")
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