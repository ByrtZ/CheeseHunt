package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.GameState
import me.byrt.cheesehunt.manager.RoundState
import me.byrt.cheesehunt.manager.Teams

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title

import org.bukkit.*
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockDropItemEvent

import java.time.Duration

@Suppress("unused")
class BlockBreakDropEvent : Listener {
    @EventHandler
    private fun onBlockBreak(e : BlockBreakEvent) {
        if(Main.getGame().getBuildMode() && e.player.isOp) {
            e.isCancelled = false
        } else {
            if(e.block.type == Material.SPONGE && Main.getGame().getRoundState() == RoundState.ROUND_TWO && Main.getGame().getGameState() == GameState.IN_GAME) {
                playerCollectCheese(e.block.location, e.player)
                e.isCancelled = false
            } else {
                e.isCancelled = true
            }
        }
    }

    private fun playerCollectCheese(eventLocation : Location, eventPlayer : Player) {
        cheeseCollectedFirework(eventLocation, eventPlayer)
        Bukkit.getOnlinePlayers().stream().forEach {
                player: Player -> announcePlayerCollectedCheese(player, eventPlayer)
        }
        incrementPlayerCollectedCheese(eventPlayer)
        Main.getGame().getInfoBoardManager().updateCollectedStats()
        checkCheeseCollected()
    }

    private fun announcePlayerCollectedCheese(player : Player, collector : Player) {
        if(player == collector) {
            Main.getGame().getItemManager().givePlayerCheese(collector)
            collector.sendMessage(Component.text("[")
                .append(Component.text("▶").color(NamedTextColor.YELLOW))
                .append(Component.text("] "))
                .append(Component.text("You collected a piece of cheese!").color(NamedTextColor.GREEN))
            )
            collector.playSound(collector.location, "entity.player.levelup", 1f, 1.5f)
        } else {
            if(Main.getGame().getTeamManager().getPlayerTeam(collector.uniqueId) == Teams.RED) {
                player.sendMessage(Component.text("[")
                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                    .append(Component.text("] "))
                    .append(Component.text(collector.name).color(NamedTextColor.RED))
                    .append(Component.text(" collected a piece of cheese.")).color(NamedTextColor.WHITE)
                )
            } else if(Main.getGame().getTeamManager().getPlayerTeam(collector.uniqueId) == Teams.BLUE) {
                player.sendMessage(Component.text("[")
                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                    .append(Component.text("] "))
                    .append(Component.text(collector.name).color(NamedTextColor.BLUE))
                    .append(Component.text(" collected a piece of cheese.")).color(NamedTextColor.WHITE)
                )
            }

            if(Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.RED && Main.getGame().getTeamManager().getPlayerTeam(collector.uniqueId) == Teams.RED || Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.BLUE && Main.getGame().getTeamManager().getPlayerTeam(collector.uniqueId) == Teams.BLUE) {
                player.playSound(player.location, "entity.player.levelup", 1f, 1.5f)
            } else if(Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.RED && Main.getGame().getTeamManager().getPlayerTeam(collector.uniqueId) == Teams.BLUE || Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.BLUE && Main.getGame().getTeamManager().getPlayerTeam(collector.uniqueId) == Teams.RED) {
                player.playSound(player.location, "entity.wandering_trader.no", 1f, 1f)
            }
        }
    }

    private fun cheeseCollectedFirework(blockLoc : Location, player : Player) {
        val blockPlacedLoc = Location(blockLoc.world, blockLoc.x + 0.5, blockLoc.y + 2.0, blockLoc.z + 0.5)
        val f: Firework = player.world.spawn(blockPlacedLoc, Firework::class.java)
        val fm = f.fireworkMeta
        fm.addEffect(
            FireworkEffect.builder()
                .flicker(false)
                .trail(false)
                .with(FireworkEffect.Type.BALL)
                .withColor(Color.ORANGE)
                .withFade(Color.YELLOW)
                .build()
        )
        fm.power = 0
        f.fireworkMeta = fm
        f.detonate()
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
                player.sendMessage(Component.text("[")
                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                    .append(Component.text("] "))
                    .append(Component.text("Red team finished collecting their cheese!").color(NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true))
                )
                if(Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.RED) {
                    player.playSound(player.location, "ui.toast.challenge_complete", 1f, 1f)
                    if(!Main.getGame().getCheeseManager().hasBlueFinishedCollecting()) {
                        Main.getGame().getCheeseManager().teamWinFireworks(player, Teams.RED)
                        player.sendMessage(Component.text("\nYour team won the game!\n").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true))
                        player.showTitle(
                            Title.title(
                            Component.text("Your team won!").color(NamedTextColor.GREEN),
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
                    player.sendMessage(Component.text("\nYour team lost the game!\n").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true))
                    player.showTitle(
                        Title.title(
                            Component.text("Your team lost!").color(NamedTextColor.RED),
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
                for(player in Bukkit.getOnlinePlayers()) {
                    player.sendMessage(Component.text("\n[")
                        .append(Component.text("▶").color(NamedTextColor.YELLOW))
                        .append(Component.text("] "))
                        .append(Component.text("All cheese has been collected!\n").color(NamedTextColor.AQUA).decoration(TextDecoration.BOLD, true))
                    )
                }
                Main.getGame().getGameCountdownTask().setTimeLeft(0)
            }
        }
        if(!Main.getGame().getCheeseManager().hasBlueFinishedCollecting() && Main.getGame().getCheeseManager().getRedCheesePlaced() == Main.getGame().getCheeseManager().getBlueCheeseCollected()) {
            Main.getGame().getCheeseManager().setBlueFinishedCollecting(true)
            for(player in Bukkit.getOnlinePlayers()) {
                player.sendMessage(Component.text("[")
                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                    .append(Component.text("] "))
                    .append(Component.text("Blue team finished collecting their cheese!").color(NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true))
                )
                if(Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.RED && !Main.getGame().getCheeseManager().hasRedFinishedCollecting()) {
                    player.playSound(player.location, "entity.ender_dragon.growl", 1f, 1f)
                    player.sendMessage(Component.text("\nYour team lost the game!\n").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true))
                    player.showTitle(
                        Title.title(
                            Component.text("Your team lost!").color(NamedTextColor.RED),
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
                        Main.getGame().getCheeseManager().teamWinFireworks(player, Teams.BLUE)
                        player.sendMessage(Component.text("\nYour team won the game!\n").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true))
                        player.showTitle(
                            Title.title(
                                Component.text("Your team won!").color(NamedTextColor.GREEN),
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
                for(player in Bukkit.getOnlinePlayers()) {
                    player.sendMessage(Component.text("\n[")
                        .append(Component.text("▶").color(NamedTextColor.YELLOW))
                        .append(Component.text("] "))
                        .append(Component.text("All cheese has been collected!\n").color(NamedTextColor.AQUA).decoration(TextDecoration.BOLD, true))
                    )
                }
                Main.getGame().getGameCountdownTask().setTimeLeft(0)
            }
        }
    }

    @EventHandler
    private fun onBlockItemDrop(e : BlockDropItemEvent) {
        e.isCancelled = true
    }
}