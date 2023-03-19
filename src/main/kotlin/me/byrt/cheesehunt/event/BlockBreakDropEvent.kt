package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.GameState
import me.byrt.cheesehunt.manager.RoundState
import me.byrt.cheesehunt.manager.Teams

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockDropItemEvent

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
        Bukkit.getOnlinePlayers().stream().forEach {
                player: Player -> announcePlayerCollectedCheese(player, eventPlayer)
        }
        Main.getGame().getCheeseManager().cheeseFirework(eventLocation, eventPlayer)
        Main.getGame().getCheeseManager().incrementCheeseCollected(eventPlayer)
        Main.getGame().getInfoBoardManager().updateCollectedStats()
        Main.getGame().getCheeseManager().checkCheeseCollected()
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

    @EventHandler
    private fun onBlockItemDrop(e : BlockDropItemEvent) {
        e.isCancelled = true
    }
}