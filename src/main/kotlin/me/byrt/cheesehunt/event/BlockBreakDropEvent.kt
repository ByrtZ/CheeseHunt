package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.GameState
import me.byrt.cheesehunt.manager.RoundState
import me.byrt.cheesehunt.manager.Team

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockDropItemEvent
import org.bukkit.inventory.ItemStack

@Suppress("unused")
class BlockBreakDropEvent : Listener {
    @EventHandler
    private fun onBlockBreak(e : BlockBreakEvent) {
        if(e.block.type == Material.SPONGE && Main.getGame()?.getRoundState() == RoundState.ROUND_TWO && Main.getGame()?.getGameState() == GameState.IN_GAME) {
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
            collector.world.spawnParticle(Particle.END_ROD, blockLoc.x + 0.5, blockLoc.y + 1, blockLoc.z + 0.5, 50, 0.0, 0.0, 0.0, 0.15)
            collector.sendMessage(Component.text("You collected a piece of cheese!").color(NamedTextColor.YELLOW))
            collector.playSound(collector.location, "scoreacquired", 1f, 1f)
        } else {
            player.sendMessage(Component.text("${collector.name} collected a piece of cheese!").color(NamedTextColor.YELLOW))
            if(Main.getGame()?.getTeamManager()?.getPlayerTeam(player.uniqueId) == Team.RED && Main.getGame()?.getTeamManager()?.getPlayerTeam(collector.uniqueId) == Team.RED || Main.getGame()?.getTeamManager()?.getPlayerTeam(player.uniqueId) == Team.BLUE && Main.getGame()?.getTeamManager()?.getPlayerTeam(collector.uniqueId) == Team.BLUE) {
                player.playSound(collector.location, "scoreacquired", 1f, 1f)
            } else if(Main.getGame()?.getTeamManager()?.getPlayerTeam(player.uniqueId) == Team.RED && Main.getGame()?.getTeamManager()?.getPlayerTeam(collector.uniqueId) == Team.BLUE || Main.getGame()?.getTeamManager()?.getPlayerTeam(player.uniqueId) == Team.BLUE && Main.getGame()?.getTeamManager()?.getPlayerTeam(collector.uniqueId) == Team.RED) {
                player.playSound(collector.location, "enemy_complete", 1f, 1f)
            }
        }
    }

    private fun incrementPlayerCollectedCheese(player : Player) {
        when(Main.getGame()?.getTeamManager()?.getPlayerTeam(player.uniqueId)) {
            Team.RED -> {
                Main.getGame()?.getCheeseManager()?.incrementCheeseCollected(Team.RED)
            }
            Team.BLUE -> {
                Main.getGame()?.getCheeseManager()?.incrementCheeseCollected(Team.BLUE)
            }
            Team.SPECTATOR -> {
                Main.getPlugin().logger.info("[INCREMENTING ERROR] ${player.name} was not on team ${Team.SPECTATOR} when they collected cheese.")
            }
            null -> {
                Main.getPlugin().logger.info("[INCREMENTING ERROR] ${player.name} was not on a team when they collected cheese.")
            }
        }
    }

    private fun checkCheeseCollected() {
        if(Main.getGame()?.getCheeseManager()?.hasRedFinishedCollecting() == false && Main.getGame()?.getCheeseManager()?.getBlueCheesePlaced() == Main.getGame()?.getCheeseManager()?.getRedCheeseCollected()) {
            Main.getGame()?.getCheeseManager()?.setRedFinishedCollecting(true)
            for(player in Bukkit.getOnlinePlayers()) {
                player.sendMessage(Component.text("Red team have finished collecting all their cheese!"))
                if(Main.getGame()?.getTeamManager()?.getPlayerTeam(player.uniqueId) == Team.RED) {
                    player.playSound(player.location, "bigscoreacquired", 1f, 1f)
                    if(Main.getGame()?.getCheeseManager()?.hasBlueFinishedCollecting() != true) {
                        player.sendMessage(Component.text("Your team won the game!").color(NamedTextColor.GREEN))
                    }
                }
                if(Main.getGame()?.getTeamManager()?.getPlayerTeam(player.uniqueId) == Team.BLUE && Main.getGame()?.getCheeseManager()?.hasBlueFinishedCollecting() == false) {
                    player.playSound(player.location, "teameliminated", 1f, 1f)
                    player.sendMessage(Component.text("Your team lost the game!").color(NamedTextColor.RED))
                }
            }
            if(Main.getGame()?.getCheeseManager()?.hasRedFinishedCollecting() == true && Main.getGame()?.getCheeseManager()?.hasBlueFinishedCollecting() == true) {
                for(player in Bukkit.getOnlinePlayers()) { player.sendMessage(Component.text("All teams have finished collecting their cheese!")) }
                Main.getGame()?.getGameCountdownTask()?.setTimeLeft(0)
            }
        }
        if(Main.getGame()?.getCheeseManager()?.hasBlueFinishedCollecting() == false && Main.getGame()?.getCheeseManager()?.getRedCheesePlaced() == Main.getGame()?.getCheeseManager()?.getBlueCheeseCollected()) {
            Main.getGame()?.getCheeseManager()?.setBlueFinishedCollecting(true)
            for(player in Bukkit.getOnlinePlayers()) {
                player.sendMessage(Component.text("Blue team have finished collecting all their cheese!"))
                if(Main.getGame()?.getTeamManager()?.getPlayerTeam(player.uniqueId) == Team.RED && Main.getGame()?.getCheeseManager()?.hasRedFinishedCollecting() == false) {
                    player.playSound(player.location, "teameliminated", 1f, 1f)
                    player.sendMessage(Component.text("Your team lost the game!").color(NamedTextColor.RED))
                }
                if(Main.getGame()?.getTeamManager()?.getPlayerTeam(player.uniqueId) == Team.BLUE) {
                    player.playSound(player.location, "bigscoreacquired", 1f, 1f)
                    if(Main.getGame()?.getCheeseManager()?.hasRedFinishedCollecting() != true) {
                        player.sendMessage(Component.text("Your team won the game!").color(NamedTextColor.GREEN))
                    }
                }
            }
            if(Main.getGame()?.getCheeseManager()?.hasRedFinishedCollecting() == true && Main.getGame()?.getCheeseManager()?.hasBlueFinishedCollecting() == true) {
                for(player in Bukkit.getOnlinePlayers()) { player.sendMessage(Component.text("All teams have finished collecting their cheese!")) }
                Main.getGame()?.getGameCountdownTask()?.setTimeLeft(0)
            }
        }
    }

    @EventHandler
    private fun onBlockItemDrop(e : BlockDropItemEvent) {
        e.isCancelled = true
    }
}