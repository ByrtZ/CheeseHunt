package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.GameState
import me.byrt.cheesehunt.manager.Sounds
import me.byrt.cheesehunt.manager.Teams

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title

import org.bukkit.*
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

import java.time.Duration

@Suppress("unused")
class PlayerDeathEvent : Listener {
    @EventHandler
    private fun onDeath(e : PlayerDeathEvent) {
        if(Main.getGame().getGameState() != GameState.IN_GAME) {
            e.isCancelled = true
        } else {
            val playerDied = e.player
            val killer = e.player.killer!!
            death(playerDied)
            eliminationDisplay(killer, playerDied)
            e.isCancelled = true
        }
    }

    private fun death(player : Player) {
        player.gameMode = GameMode.SPECTATOR
        player.inventory.clear()
        if(Main.getGame().getTeamManager().isInRedTeam(player.uniqueId)) {
            deathFirework(player.location, Teams.RED)
            Main.getGame().getRespawnManager().startRespawnLoop(player, Main.getPlugin(), Teams.RED)
        } else if(Main.getGame().getTeamManager().isInBlueTeam(player.uniqueId)) {
            deathFirework(player.location, Teams.BLUE)
            Main.getGame().getRespawnManager().startRespawnLoop(player, Main.getPlugin(), Teams.BLUE)
        }
    }

    private fun deathFirework(location : Location, team : Teams) {
        when(team) {
            Teams.RED -> {
                val spawnLoc = Location(location.world, location.x, location.y + 1, location.z)
                val f: Firework = spawnLoc.world.spawn(spawnLoc, Firework::class.java)
                val fm = f.fireworkMeta
                fm.addEffect(
                    FireworkEffect.builder()
                        .flicker(false)
                        .trail(false)
                        .with(FireworkEffect.Type.BALL)
                        .withColor(Color.RED)
                        .build()
                )
                fm.power = 0
                f.fireworkMeta = fm
                f.ticksToDetonate = 1
            }
            Teams.BLUE -> {
                val spawnLoc = Location(location.world, location.x, location.y + 1, location.z)
                val f: Firework = spawnLoc.world.spawn(spawnLoc, Firework::class.java)
                val fm = f.fireworkMeta
                fm.addEffect(
                    FireworkEffect.builder()
                        .flicker(false)
                        .trail(false)
                        .with(FireworkEffect.Type.BALL)
                        .withColor(Color.BLUE)
                        .build()
                )
                fm.power = 0
                f.fireworkMeta = fm
                f.ticksToDetonate = 1
            }
            else -> {
                // This is literally impossible to reach :)
            }
        }
    }

    private fun eliminationDisplay(player : Player, playerKilled : Player) {
        for(allPlayer in Bukkit.getOnlinePlayers()) {
            if(allPlayer != player) {
                allPlayer.sendMessage(Component.text(playerKilled.name, Main.getGame().getTeamManager().getTeamColour(playerKilled)).append(Component.text(" was eliminated by ", NamedTextColor.WHITE).append(Component.text(player.name, Main.getGame().getTeamManager().getTeamColour(player)))))
            } else {
                allPlayer.sendMessage(Component.text("[+5] You eliminated ").append(Component.text(playerKilled.name, Main.getGame().getTeamManager().getTeamColour(playerKilled))).append(Component.text("!")))
            }
        }
        player.playSound(player.location, Sounds.Elimination.ELIMINATION, 1f, 1.25f)
        player.showTitle(
            Title.title(
                Component.text(""),
                Component.text("[").append(Component.text("⚔", NamedTextColor.GREEN).append(Component.text("] ")).append(Component.text(playerKilled.name, Main.getGame().getTeamManager().getTeamColour(playerKilled)))),
                Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ofSeconds(1))
            )
        )
    }
}