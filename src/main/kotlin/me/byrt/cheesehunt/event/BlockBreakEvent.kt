package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.RoundState
import me.byrt.cheesehunt.manager.Team

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

@Suppress("unused")
class BlockBreakEvent : Listener {
    @EventHandler
    private fun onBlockBreak(e : BlockBreakEvent) {
        if(e.block.type == Material.SPONGE && Main.getGame()?.getRoundState() == RoundState.ROUND_TWO) {
            e.player.world.spawnParticle(Particle.END_ROD, e.block.location.x + 0.5, e.block.location.y + 1, e.block.location.z + 0.5, 50, 0.0, 0.0, 0.0, 0.15)
            Bukkit.getOnlinePlayers().stream().forEach {
                    player: Player -> announcePlayerCollectedCheese(player, e.player)
            }
            e.isCancelled = false
        } else {
            e.isCancelled = true
        }
    }

    private fun announcePlayerCollectedCheese(player : Player, collector : Player) {
        if(player == collector) {
            collector.sendMessage(Component.text("You collected a piece of cheese!").color(NamedTextColor.YELLOW))
            collector.playSound(collector.location, "scoreacquired", 1f, 1f)
        } else {
            player.sendMessage(Component.text("${collector.name} collected a piece of cheese!"))
            if(Main.getGame()?.getTeamManager()?.getPlayerTeam(player.uniqueId) == Team.RED && Main.getGame()?.getTeamManager()?.getPlayerTeam(collector.uniqueId) == Team.RED || Main.getGame()?.getTeamManager()?.getPlayerTeam(player.uniqueId) == Team.BLUE && Main.getGame()?.getTeamManager()?.getPlayerTeam(collector.uniqueId) == Team.BLUE) {
                player.playSound(collector.location, "scoreacquired", 1f, 1f)
            } else if(Main.getGame()?.getTeamManager()?.getPlayerTeam(player.uniqueId) == Team.RED && Main.getGame()?.getTeamManager()?.getPlayerTeam(collector.uniqueId) == Team.BLUE || Main.getGame()?.getTeamManager()?.getPlayerTeam(player.uniqueId) == Team.BLUE && Main.getGame()?.getTeamManager()?.getPlayerTeam(collector.uniqueId) == Team.RED) {
                player.playSound(collector.location, "enemy_complete", 1f, 1f)
            }
        }
    }
}