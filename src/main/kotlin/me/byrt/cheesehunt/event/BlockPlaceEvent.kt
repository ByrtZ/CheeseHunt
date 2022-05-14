package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.RoundState
import me.byrt.cheesehunt.manager.Team

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

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
        if(e.block.type == Material.SPONGE && Main.getGame()?.getRoundState() == RoundState.ROUND_ONE) {
            val blockPlacedLoc = Location(e.block.location.world, e.block.location.x + 0.5, e.block.location.y + 2.0, e.block.location.z + 0.5)
            val f: Firework = e.player.world.spawn(blockPlacedLoc, Firework::class.java)
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
            Bukkit.getOnlinePlayers().stream().forEach {
                    player: Player -> announcePlayerPlacedCheese(player, e.player)
            }
            e.isCancelled = false
        } else {
            e.isCancelled = true
        }
    }

    private fun announcePlayerPlacedCheese(player : Player, placer : Player) {
        if(player == placer) {
            placer.sendMessage(Component.text("You placed a piece of cheese!").color(NamedTextColor.YELLOW))
            placer.playSound(placer.location, "objectivecomplete", 1f, 1f)
        } else {
            player.sendMessage(Component.text("${placer.name} placed a piece of cheese!"))
            if(Main.getGame()?.getTeamManager()?.getPlayerTeam(player.uniqueId) == Team.RED && Main.getGame()?.getTeamManager()?.getPlayerTeam(placer.uniqueId) == Team.RED || Main.getGame()?.getTeamManager()?.getPlayerTeam(player.uniqueId) == Team.BLUE && Main.getGame()?.getTeamManager()?.getPlayerTeam(placer.uniqueId) == Team.BLUE) {
                player.playSound(placer.location, "objectivecomplete", 1f, 1f)
            } else if(Main.getGame()?.getTeamManager()?.getPlayerTeam(player.uniqueId) == Team.RED && Main.getGame()?.getTeamManager()?.getPlayerTeam(placer.uniqueId) == Team.BLUE || Main.getGame()?.getTeamManager()?.getPlayerTeam(player.uniqueId) == Team.BLUE && Main.getGame()?.getTeamManager()?.getPlayerTeam(placer.uniqueId) == Team.RED) {
                player.playSound(placer.location, "enemy_complete", 1f, 1f)
            }
        }
    }
}