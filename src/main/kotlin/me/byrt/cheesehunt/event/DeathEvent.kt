package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.GameState
import me.byrt.cheesehunt.manager.Team

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title

import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

import java.time.Duration

@Suppress("unused")
class DeathEvent : Listener {
    @EventHandler
    private fun onPlayerDeath(e : PlayerDeathEvent) {
        if(Main.getGame().getGameState() == GameState.IN_GAME) {
            if(e.entity.killer is Player) {
                if(Main.getGame().getTeamManager().getPlayerTeam(e.player.uniqueId) == Team.RED) {
                    val playerKilledLoc = Location(e.player.location.world, e.player.location.x, e.player.location.y + 2.0, e.player.location.z)
                    val f: Firework = e.player.world.spawn(playerKilledLoc, Firework::class.java)
                    val fm = f.fireworkMeta
                    fm.addEffect(
                        FireworkEffect.builder()
                            .flicker(false)
                            .trail(false)
                            .with(FireworkEffect.Type.BALL)
                            .withColor(Color.RED)
                            .withFade(Color.RED)
                            .build()
                    )
                    fm.power = 0
                    f.fireworkMeta = fm
                    f.detonate()
                }
                if(Main.getGame().getTeamManager().getPlayerTeam(e.player.uniqueId) == Team.BLUE) {
                    val playerKilledLoc = Location(e.player.location.world, e.player.location.x, e.player.location.y + 2.0, e.player.location.z)
                    val f: Firework = e.player.world.spawn(playerKilledLoc, Firework::class.java)
                    val fm = f.fireworkMeta
                    fm.addEffect(
                        FireworkEffect.builder()
                            .flicker(false)
                            .trail(false)
                            .with(FireworkEffect.Type.BALL)
                            .withColor(Color.BLUE)
                            .withFade(Color.BLUE)
                            .build()
                    )
                    fm.power = 0
                    f.fireworkMeta = fm
                    f.detonate()
                }

                e.deathMessage(Component.text("[")
                    .append(Component.text("☠").color(NamedTextColor.RED)
                        .append(Component.text("] ${e.player.name} was killed by ${e.entity.killer?.player?.name}").color(NamedTextColor.WHITE))))

                val killer = e.entity.killer!!.player
                killer?.showTitle(Title.title(
                    Component.text(""),
                    Component.text("[").append(Component.text("⚔").color(NamedTextColor.GREEN).append(Component.text("] ${e.player.name}").color(NamedTextColor.WHITE))),
                    Title.Times.times(Duration.ofSeconds(0),
                                    Duration.ofSeconds(3.5.toLong()),
                                    Duration.ofSeconds(1)
                        )
                    )
                )
                killer?.spawnParticle(Particle.END_ROD, killer.location, 25, 0.0, 0.0, 0.0, 0.15)
                killer?.playSound(killer.location, "scoreacquired", 1f, 1f)
                killer?.sendMessage(Component.text("[+??.?喇] You somehow killed ${e.player.name}!"))
            }
        }
    }
}