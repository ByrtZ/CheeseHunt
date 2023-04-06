package me.byrt.cheesehunt.command

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.GameState
import me.byrt.cheesehunt.manager.Teams

import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title

import org.bukkit.entity.Player

import java.time.Duration

@Suppress("unused")
class AutoTeam : BaseCommand {
    private val shuffleStartSound: Sound = Sound.sound(Key.key("block.note_block.flute"), Sound.Source.MASTER, 1f, 1f)
    private val shuffleCompleteSound: Sound = Sound.sound(Key.key("block.note_block.flute"), Sound.Source.MASTER, 1f, 2f)
    @CommandMethod("autoteam")
    @CommandDescription("Automatically assigns everyone online to a team.")
    @CommandPermission("cheesehunt.autoteam")
    fun autoTeam(sender : Player) {
        if(Main.getGame().getGameState() == GameState.IDLE) {
            sender.showTitle(Title.title(Component.text(""), Component.text("Shuffling teams...").color(NamedTextColor.RED), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(3), Duration.ofSeconds(1))))
            sender.playSound(shuffleStartSound)
            var i = 0
            Main.getPlugin().server.onlinePlayers.shuffled().forEach {
                Main.getGame().getTeamManager().removeFromTeam(it, it.uniqueId, Main.getGame().getTeamManager().getPlayerTeam(it.uniqueId))
                if (i % 2 == 0) {
                    Main.getGame().getTeamManager().addToTeam(it, it.uniqueId, Teams.RED)
                } else {
                    Main.getGame().getTeamManager().addToTeam(it, it.uniqueId, Teams.BLUE)
                }
                i++
            }
            sender.sendMessage(Component.text("Successfully split all online players into teams.").color(NamedTextColor.GREEN))
            sender.showTitle(Title.title(Component.text(""), Component.text("Teams shuffled randomly!").color(NamedTextColor.GREEN), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(1))))
            sender.playSound(shuffleCompleteSound)
        } else {
            sender.sendMessage(Component.text("Unable to modify teams in this state.", NamedTextColor.RED))
        }
    }
}