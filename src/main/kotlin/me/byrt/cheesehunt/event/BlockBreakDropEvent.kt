package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.GameState
import me.byrt.cheesehunt.manager.RoundState
import me.byrt.cheesehunt.manager.Teams

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title

import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockDropItemEvent
import org.bukkit.inventory.ItemStack

import java.time.Duration

@Suppress("unused")
class BlockBreakDropEvent : Listener {
    @EventHandler
    private fun onBlockBreak(e : BlockBreakEvent) {
        if(e.block.type == Material.SPONGE && Main.getGame().getRoundState() == RoundState.ROUND_TWO && Main.getGame().getGameState() == GameState.IN_GAME) {
            Bukkit.getOnlinePlayers().stream().forEach {
                    player: Player -> announcePlayerCollectedCheese(player, e.player, e.block.location)
            }
            e.isCancelled = false
            incrementPlayerCollectedCheese(e.player)
            checkCheeseCollected()
        } else {
            e.isCancelled = true
        }
    }

    private fun announcePlayerCollectedCheese(player : Player, collector : Player, blockLoc : Location) {
        if(player == collector) {
            collector.inventory.addItem(ItemStack(Material.SPONGE, 1))
            collector.world.spawnParticle(Particle.SPELL_MOB, blockLoc.x + 0.5, blockLoc.y + 1, blockLoc.z + 0.5, 150, 1.0, 1.0, 1.0, 0.15)
            collector.sendMessage(Component.text("You collected a piece of cheese!").color(NamedTextColor.YELLOW))
            collector.playSound(collector.location, "entity.player.levelup", 1f, 1.5f)
        } else {
            player.sendMessage(Component.text("${collector.name} collected a piece of cheese!").color(NamedTextColor.YELLOW))
            if(Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.RED && Main.getGame().getTeamManager().getPlayerTeam(collector.uniqueId) == Teams.RED || Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.BLUE && Main.getGame().getTeamManager().getPlayerTeam(collector.uniqueId) == Teams.BLUE) {
                player.playSound(player.location, "entity.player.levelup", 1f, 1.5f)
            } else if(Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.RED && Main.getGame().getTeamManager().getPlayerTeam(collector.uniqueId) == Teams.BLUE || Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.BLUE && Main.getGame().getTeamManager().getPlayerTeam(collector.uniqueId) == Teams.RED) {
                player.playSound(player.location, "entity.wandering_trader.no", 1f, 1f)
            }
        }
    }

    private fun incrementPlayerCollectedCheese(player : Player) {
        when(Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId)) {
            Teams.RED -> {
                Main.getGame().getCheeseManager().incrementCheeseCollected(Teams.RED)
                Main.getGame().getCheeseManager().updateCollectedCheese(player.uniqueId)
            }
            Teams.BLUE -> {
                Main.getGame().getCheeseManager().incrementCheeseCollected(Teams.BLUE)
                Main.getGame().getCheeseManager().updateCollectedCheese(player.uniqueId)
            }
            Teams.SPECTATOR -> {
                Main.getPlugin().logger.info("[INCREMENTING ERROR] ${player.name} was on team ${Teams.SPECTATOR} when they collected cheese.")
            }
        }
    }

    private fun checkCheeseCollected() {
        if(!Main.getGame().getCheeseManager().hasRedFinishedCollecting() && Main.getGame().getCheeseManager().getBlueCheesePlaced() == Main.getGame().getCheeseManager().getRedCheeseCollected()) {
            Main.getGame().getCheeseManager().setRedFinishedCollecting(true)
            for(player in Bukkit.getOnlinePlayers()) {
                player.sendMessage(Component.text("Red team have finished collecting all their cheese!").color(NamedTextColor.GREEN))
                if(Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.RED) {
                    player.playSound(player.location, "ui.toast.challenge_complete", 1f, 1f)
                    if(!Main.getGame().getCheeseManager().hasBlueFinishedCollecting()) {
                        player.sendMessage(Component.text("Your team won the game!").color(NamedTextColor.GREEN))
                        player.showTitle(
                            Title.title(
                            Component.text("Your team won the game!").color(NamedTextColor.GREEN),
                            Component.text("Well done!").color(NamedTextColor.GREEN),
                            Title.Times.times(
                                Duration.ofSeconds(1),
                                Duration.ofSeconds(5),
                                Duration.ofSeconds(1)
                                )
                            )
                        )
                    }
                }
                if(Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.BLUE && !Main.getGame().getCheeseManager().hasBlueFinishedCollecting()) {
                    player.playSound(player.location, "entity.ender_dragon.growl", 1f, 1f)
                    player.sendMessage(Component.text("Your team lost the game!").color(NamedTextColor.RED))
                    player.showTitle(
                        Title.title(
                            Component.text("Your team lost the game!").color(NamedTextColor.RED),
                            Component.text("Git gud!").color(NamedTextColor.RED),
                            Title.Times.times(
                                Duration.ofSeconds(1),
                                Duration.ofSeconds(5),
                                Duration.ofSeconds(1)
                            )
                        )
                    )
                }
            }
            if(Main.getGame().getCheeseManager().hasRedFinishedCollecting() && Main.getGame().getCheeseManager().hasBlueFinishedCollecting()) {
                for(player in Bukkit.getOnlinePlayers()) { player.sendMessage(Component.text("All teams have finished collecting their cheese!").color(NamedTextColor.GREEN)) }
                Main.getGame().getGameCountdownTask().setTimeLeft(0)
            }
        }
        if(!Main.getGame().getCheeseManager().hasBlueFinishedCollecting() && Main.getGame().getCheeseManager().getRedCheesePlaced() == Main.getGame().getCheeseManager().getBlueCheeseCollected()) {
            Main.getGame().getCheeseManager().setBlueFinishedCollecting(true)
            for(player in Bukkit.getOnlinePlayers()) {
                player.sendMessage(Component.text("Blue team have finished collecting all their cheese!").color(NamedTextColor.GREEN))
                if(Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.RED && !Main.getGame().getCheeseManager().hasRedFinishedCollecting()) {
                    player.playSound(player.location, "entity.ender_dragon.growl", 1f, 1f)
                    player.sendMessage(Component.text("Your team lost the game!").color(NamedTextColor.RED))
                    player.showTitle(
                        Title.title(
                            Component.text("Your team lost the game!").color(NamedTextColor.RED),
                            Component.text("Git gud!").color(NamedTextColor.RED),
                            Title.Times.times(
                                Duration.ofSeconds(1),
                                Duration.ofSeconds(5),
                                Duration.ofSeconds(1)
                            )
                        )
                    )
                }
                if(Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.BLUE) {
                    player.playSound(player.location, "ui.toast.challenge_complete", 1f, 1f)
                    if(!Main.getGame().getCheeseManager().hasRedFinishedCollecting()) {
                        player.sendMessage(Component.text("Your team won the game!").color(NamedTextColor.GREEN))
                        player.showTitle(
                            Title.title(
                                Component.text("Your team won the game!").color(NamedTextColor.GREEN),
                                Component.text("Well done!").color(NamedTextColor.GREEN),
                                Title.Times.times(
                                    Duration.ofSeconds(1),
                                    Duration.ofSeconds(5),
                                    Duration.ofSeconds(1)
                                )
                            )
                        )
                    }
                }
            }
            if(Main.getGame().getCheeseManager().hasRedFinishedCollecting() && Main.getGame().getCheeseManager().hasBlueFinishedCollecting()) {
                for(player in Bukkit.getOnlinePlayers()) { player.sendMessage(Component.text("All teams have finished collecting their cheese!").color(NamedTextColor.GREEN)) }
                Main.getGame().getGameCountdownTask().setTimeLeft(0)
            }
        }
    }

    @EventHandler
    private fun onBlockItemDrop(e : BlockDropItemEvent) {
        e.isCancelled = true
    }
}