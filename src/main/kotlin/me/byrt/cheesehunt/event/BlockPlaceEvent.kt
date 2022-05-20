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
        if(e.block.type == Material.SPONGE && Main.getGame().getRoundState() == RoundState.ROUND_ONE && e.blockAgainst.type != Material.BARRIER) {
            cheesePlacedFirework(e.block.location, e.player)
            Bukkit.getOnlinePlayers().stream().forEach {
                    player: Player -> announcePlayerPlacedCheese(player, e.player)
            }
            incrementPlayerPlacedCheese(e.player)
            checkCheesePlaced()
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
            player.sendMessage(Component.text("${placer.name} placed a piece of cheese!").color(NamedTextColor.YELLOW))
            if(Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Team.RED && Main.getGame().getTeamManager().getPlayerTeam(placer.uniqueId) == Team.RED || Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Team.BLUE && Main.getGame().getTeamManager().getPlayerTeam(placer.uniqueId) == Team.BLUE) {
                player.playSound(placer.location, "objectivecomplete", 1f, 1f)
            } else if(Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Team.RED && Main.getGame().getTeamManager().getPlayerTeam(placer.uniqueId) == Team.BLUE || Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Team.BLUE && Main.getGame().getTeamManager().getPlayerTeam(placer.uniqueId) == Team.RED) {
                player.playSound(placer.location, "enemy_complete", 1f, 1f)
            }
        }
    }

    private fun incrementPlayerPlacedCheese(player : Player) {
        when(Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId)) {
            Team.RED -> {
                Main.getGame().getCheeseManager().incrementCheesePlaced(Team.RED)
            }
            Team.BLUE -> {
                Main.getGame().getCheeseManager().incrementCheesePlaced(Team.BLUE)
            }
            Team.SPECTATOR -> {
                Main.getPlugin().logger.info("[INCREMENTING ERROR] ${player.name} was not on team ${Team.SPECTATOR} when they placed cheese.")
            }
        }
    }

    private fun checkCheesePlaced() {
        if(!Main.getGame().getCheeseManager().hasRedFinishedPlacing() && Main.getGame().getTeamManager().getRedTeam().size.times(4) == Main.getGame().getCheeseManager().getRedCheesePlaced()) {
            Main.getGame().getCheeseManager().setRedFinishedPlacing(true)
            for(player in Bukkit.getOnlinePlayers()) { player.sendMessage(Component.text("Red team have placed all their cheese!")) }
            if(Main.getGame().getCheeseManager().hasRedFinishedPlacing() && Main.getGame().getCheeseManager().hasBlueFinishedPlacing()) {
                for(player in Bukkit.getOnlinePlayers()) { player.sendMessage(Component.text("All teams have placed their cheese!")) }
                Main.getGame().getGameCountdownTask().setTimeLeft(0)
            }
        }
        if(!Main.getGame().getCheeseManager().hasBlueFinishedPlacing() && Main.getGame().getTeamManager().getBlueTeam().size.times(4) == Main.getGame().getCheeseManager().getBlueCheesePlaced()) {
            Main.getGame().getCheeseManager().setBlueFinishedPlacing(true)
            for(player in Bukkit.getOnlinePlayers()) { player.sendMessage(Component.text("Blue team have placed all their cheese!")) }
            if(Main.getGame().getCheeseManager().hasRedFinishedPlacing() && Main.getGame().getCheeseManager().hasBlueFinishedPlacing()) {
                for(player in Bukkit.getOnlinePlayers()) { player.sendMessage(Component.text("All teams have placed their cheese!")) }
                Main.getGame().getGameCountdownTask().setTimeLeft(0)
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