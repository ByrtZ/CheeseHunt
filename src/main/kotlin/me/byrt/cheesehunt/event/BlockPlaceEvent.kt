package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.command.ModifierOptions
import me.byrt.cheesehunt.manager.RoundState
import me.byrt.cheesehunt.manager.Teams

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.*
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

@Suppress("unused")
class BlockPlaceEvent : Listener {
    @EventHandler
    private fun onBlockPlace(e : BlockPlaceEvent) {
        if(Main.getGame().getBuildMode() && e.player.isOp) {
            e.isCancelled = false
        } else {
            if(e.block.type == Material.SPONGE && Main.getGame().getRoundState() == RoundState.ROUND_ONE && e.blockAgainst.type != Material.BARRIER) {
                playerPlaceCheese(e.block.location, e.player)
                e.isCancelled = false
            } else {
                e.isCancelled = true
            }
        }
    }

    private fun playerPlaceCheese(eventLocation : Location, eventPlayer : Player) {
        cheesePlacedFirework(eventLocation, eventPlayer)
        Bukkit.getOnlinePlayers().stream().forEach {
                player: Player -> announcePlayerPlacedCheese(player, eventPlayer)
        }
        incrementPlayerPlacedCheese(eventPlayer)
        Main.getGame().getInfoBoardManager().updatePlacedStats()
        checkCheesePlaced()
    }

    private fun announcePlayerPlacedCheese(player : Player, placer : Player) {
        if(Main.getGame().getModifier() != ModifierOptions.BOTTOMLESS_CHEESE) {
            if(player == placer) {
                placer.sendMessage(Component.text("[")
                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                    .append(Component.text("] "))
                    .append(Component.text("You placed a piece of cheese!").color(NamedTextColor.GREEN))
                )
                placer.playSound(placer.location, "entity.wandering_trader.yes", 1f, 1f)
            } else {
                if(Main.getGame().getTeamManager().getPlayerTeam(placer.uniqueId) == Teams.RED) {
                    player.sendMessage(Component.text("[")
                        .append(Component.text("▶").color(NamedTextColor.YELLOW))
                        .append(Component.text("] "))
                        .append(Component.text(placer.name).color(NamedTextColor.RED))
                        .append(Component.text(" placed a piece of cheese.")).color(NamedTextColor.WHITE)
                    )
                } else if(Main.getGame().getTeamManager().getPlayerTeam(placer.uniqueId) == Teams.BLUE) {
                    player.sendMessage(Component.text("[")
                        .append(Component.text("▶").color(NamedTextColor.YELLOW))
                        .append(Component.text("] "))
                        .append(Component.text(placer.name).color(NamedTextColor.BLUE))
                        .append(Component.text(" placed a piece of cheese.")).color(NamedTextColor.WHITE)
                    )
                }

                if(Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.RED && Main.getGame().getTeamManager().getPlayerTeam(placer.uniqueId) == Teams.RED || Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.BLUE && Main.getGame().getTeamManager().getPlayerTeam(placer.uniqueId) == Teams.BLUE) {
                    player.playSound(player.location, "entity.wandering_trader.yes", 1f, 1f)
                } else if(Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.RED && Main.getGame().getTeamManager().getPlayerTeam(placer.uniqueId) == Teams.BLUE || Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.BLUE && Main.getGame().getTeamManager().getPlayerTeam(placer.uniqueId) == Teams.RED) {
                    player.playSound(player.location, "entity.wandering_trader.no", 1f, 1f)
                }
            }
        }
    }

    private fun incrementPlayerPlacedCheese(player : Player) {
        when(Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId)) {
            Teams.RED -> {
                Main.getGame().getCheeseManager().incrementCheesePlaced(Teams.RED)
            }
            Teams.BLUE -> {
                Main.getGame().getCheeseManager().incrementCheesePlaced(Teams.BLUE)
            }
            Teams.SPECTATOR -> {
                Main.getPlugin().logger.info("[INCREMENTING ERROR] ${player.name} was not on team ${Teams.SPECTATOR} when they placed cheese.")
            }
        }
    }

    private fun checkCheesePlaced() {
        if(Main.getGame().getModifier() != ModifierOptions.BOTTOMLESS_CHEESE) {
            if(!Main.getGame().getCheeseManager().hasRedFinishedPlacing() && Main.getGame().getTeamManager().getRedTeam().size.times(4) == Main.getGame().getCheeseManager().getRedCheesePlaced()) {
                Main.getGame().getCheeseManager().setRedFinishedPlacing(true)
                for(player in Bukkit.getOnlinePlayers()) {
                    player.sendMessage(Component.text("[")
                        .append(Component.text("▶").color(NamedTextColor.YELLOW))
                        .append(Component.text("] "))
                        .append(Component.text("Red team placed all their cheese!").color(NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true))
                    )
                }
                if(Main.getGame().getCheeseManager().hasRedFinishedPlacing() && Main.getGame().getCheeseManager().hasBlueFinishedPlacing()) {
                    for(player in Bukkit.getOnlinePlayers()) {
                        player.sendMessage(Component.text("[")
                            .append(Component.text("▶").color(NamedTextColor.YELLOW))
                            .append(Component.text("] "))
                            .append(Component.text("All cheese has been placed, get ready to cheese hunt!").color(NamedTextColor.AQUA).decoration(TextDecoration.BOLD, true))
                        )
                    }
                    Main.getGame().getGameCountdownTask().setTimeLeft(0)
                }
            }
            if(!Main.getGame().getCheeseManager().hasBlueFinishedPlacing() && Main.getGame().getTeamManager().getBlueTeam().size.times(4) == Main.getGame().getCheeseManager().getBlueCheesePlaced()) {
                Main.getGame().getCheeseManager().setBlueFinishedPlacing(true)
                for(player in Bukkit.getOnlinePlayers()) {
                    player.sendMessage(Component.text("[")
                        .append(Component.text("▶").color(NamedTextColor.YELLOW))
                        .append(Component.text("] "))
                        .append(Component.text("Blue team placed all their cheese!").color(NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true))
                    )
                }
                if(Main.getGame().getCheeseManager().hasRedFinishedPlacing() && Main.getGame().getCheeseManager().hasBlueFinishedPlacing()) {
                    for(player in Bukkit.getOnlinePlayers()) {
                        player.sendMessage(Component.text("[")
                            .append(Component.text("▶").color(NamedTextColor.YELLOW))
                            .append(Component.text("] "))
                            .append(Component.text("All cheese has been placed, get ready to cheese hunt!").color(NamedTextColor.AQUA).decoration(TextDecoration.BOLD, true))
                        )
                    }
                    Main.getGame().getGameCountdownTask().setTimeLeft(0)
                }
            }
        }
    }

    private fun cheesePlacedFirework(blockLoc : Location, player : Player) {
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
}