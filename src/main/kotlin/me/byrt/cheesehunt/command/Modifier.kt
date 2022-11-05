package me.byrt.cheesehunt.command

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.GameState
import me.byrt.cheesehunt.manager.RoundState

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.title.Title

import org.bukkit.Bukkit
import org.bukkit.entity.Player

import java.time.Duration

@Suppress("unused")
class Modifier : BaseCommand {
    private val modifierEnabledSound: Sound = Sound.sound(Key.key("entity.wither.spawn"), Sound.Source.MASTER, 1f, 1f)
    private val modifierDisabledSound: Sound = Sound.sound(Key.key("entity.wither.death"), Sound.Source.MASTER, 1f, 1f)
    @CommandMethod("modifier <modifier>")
    @CommandDescription("Allows the enabling and disabling of modifiers.")
    @CommandPermission("cheesehunt.modifier")
    fun timer(sender : Player, @Argument("modifier") option : ModifierOptions) {
        if(Main.getGame().getGameState() != GameState.STARTING) {
            sender.sendMessage(Component.text("You can't change the modifier right now.").color(NamedTextColor.RED))
        } else {
            when(option) {
                ModifierOptions.NONE -> {
                    if(Main.getGame().getGameState() == GameState.STARTING && Main.getGame().getRoundState() == RoundState.ROUND_ONE) {
                        if(Main.getGame().getModifier() != ModifierOptions.NONE) {
                            sender.sendMessage(Component.text("Active modifier changed to $option!").color(NamedTextColor.GREEN))
                            Main.getGame().setModifier(ModifierOptions.NONE)
                            Main.getGame().getInfoBoardManager().updateModifier()
                            for(player in Bukkit.getOnlinePlayers()) {
                                player.playSound(modifierDisabledSound)
                                player.sendMessage(Component.text("\n⚠ All active modifiers have been disabled ⚠\n").color(NamedTextColor.RED))
                                player.showTitle(
                                    Title.title(
                                        Component.text(""),
                                        Component.text("⚠ All active modifiers have been disabled ⚠").color(NamedTextColor.RED),
                                        Title.Times.times(
                                            Duration.ofSeconds(1),
                                            Duration.ofSeconds(2),
                                            Duration.ofSeconds(1)
                                        )
                                    )
                                )
                            }
                        } else {
                            sender.sendMessage(Component.text("You cannot set the current modifier to none, as there is no modifier active.").color(NamedTextColor.RED))
                        }
                    } else {
                        sender.sendMessage(Component.text("You can only change modifiers when a game is about to begin.").color(NamedTextColor.RED))
                    }
                }
                ModifierOptions.BOTTOMLESS_CHEESE -> {
                    if(Main.getGame().getGameState() == GameState.STARTING && Main.getGame().getRoundState() == RoundState.ROUND_ONE) {
                        if(Main.getGame().getModifier() != ModifierOptions.BOTTOMLESS_CHEESE) {
                            sender.sendMessage(Component.text("Active modifier changed to $option!").color(NamedTextColor.GREEN))
                            Main.getGame().setModifier(ModifierOptions.BOTTOMLESS_CHEESE)
                            Main.getGame().getInfoBoardManager().updateModifier()
                            for(player in Bukkit.getOnlinePlayers()) {
                                player.playSound(modifierEnabledSound)
                                player.sendMessage(Component.text("\n⚠ Modifier: ").color(NamedTextColor.GOLD)
                                    .append(Component.text("Bottomless Cheese ↴").color(NamedTextColor.YELLOW)
                                    .append(Component.text("\nRound 1: Your hotbar is filled with delicious cheese.\nRound 2: Your Cheese Collector has super speed.\n").color(TextColor.fromHexString("#F9F37F")))))
                                player.showTitle(
                                    Title.title(
                                        Component.text("Modifier").color(NamedTextColor.GOLD),
                                        Component.text("⚠ Bottomless Cheese ⚠").color(NamedTextColor.YELLOW),
                                        Title.Times.times(
                                            Duration.ofSeconds(1),
                                            Duration.ofSeconds(2),
                                            Duration.ofSeconds(1)
                                        )
                                    )
                                )
                            }
                        } else {
                            sender.sendMessage(Component.text("This modifier is already active!").color(NamedTextColor.RED))
                        }
                    } else {
                        sender.sendMessage(Component.text("You can only change this modifier before the game begins.").color(NamedTextColor.RED))
                    }
                }
                ModifierOptions.IMPOSTOR -> {
                    if(Main.getGame().getGameState() == GameState.STARTING && Main.getGame().getRoundState() == RoundState.ROUND_TWO) {
                        if(Main.getGame().getModifier() != ModifierOptions.IMPOSTOR) {
                            if(Main.getGame().getTeamManager().getRedTeam().size >=2 && Main.getGame().getTeamManager().getBlueTeam().size >= 2) {
                                sender.sendMessage(Component.text("Active modifier changed to $option!").color(NamedTextColor.GREEN))
                                Main.getGame().setModifier(ModifierOptions.IMPOSTOR)
                                Main.getGame().getInfoBoardManager().updateModifier()
                                for(player in Bukkit.getOnlinePlayers()) {
                                    player.playSound(modifierEnabledSound)
                                    player.sendMessage(Component.text("\n⚠ Modifier: ").color(NamedTextColor.GOLD)
                                        .append(Component.text("Impostor ↴").color(NamedTextColor.YELLOW)
                                        .append(Component.text("\nRound 1: Nothing is different!\nRound 2: One player on your team is swapped to the enemy team's map where they can punch enemy players.\n").color(TextColor.fromHexString("#F9F37F")))))
                                    player.showTitle(
                                        Title.title(
                                            Component.text("Modifier").color(NamedTextColor.GOLD),
                                            Component.text("⚠ Impostor ⚠").color(NamedTextColor.YELLOW),
                                            Title.Times.times(
                                                Duration.ofSeconds(1),
                                                Duration.ofSeconds(2),
                                                Duration.ofSeconds(1)
                                            )
                                        )
                                    )
                                }
                            } else {
                                sender.sendMessage(Component.text("There aren't enough players to enable this modifier.").color(NamedTextColor.RED))
                            }
                        } else {
                            sender.sendMessage(Component.text("This modifier is already active!").color(NamedTextColor.RED))
                        }
                    } else {
                        sender.sendMessage(Component.text("You can only enable this modifier before round two begins.").color(NamedTextColor.RED))
                    }
                }
            }
        }
    }
}

enum class ModifierOptions {
    BOTTOMLESS_CHEESE,
    IMPOSTOR,
    NONE
}