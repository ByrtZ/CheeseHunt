package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.RoundState
import me.byrt.cheesehunt.manager.Teams

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.*
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
        Bukkit.getOnlinePlayers().stream().forEach {
                player: Player -> announcePlayerPlacedCheese(player, eventPlayer)
        }
        Main.getGame().getCheeseManager().cheeseFirework(eventLocation, eventPlayer)
        Main.getGame().getCheeseManager().incrementCheesePlaced(eventPlayer)
        Main.getGame().getInfoBoardManager().updatePlacedStats()
        Main.getGame().getCheeseManager().checkCheesePlaced()
    }

    private fun announcePlayerPlacedCheese(player : Player, placer : Player) {
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