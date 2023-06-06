package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.ScoreMode
import me.byrt.cheesehunt.state.GameState
import me.byrt.cheesehunt.state.Sounds
import me.byrt.cheesehunt.manager.Statistic
import me.byrt.cheesehunt.state.Teams

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title

import org.bukkit.*
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

import java.time.Duration

@Suppress("unused")
class PlayerDeathEvent : Listener {
    @EventHandler
    private fun onDeath(e : PlayerDeathEvent) {
        if(Main.getGame().gameManager.getGameState() == GameState.IDLE || Main.getGame().gameManager.getGameState() == GameState.STARTING || Main.getGame().gameManager.getGameState() == GameState.ROUND_END || Main.getGame().gameManager.getGameState() == GameState.GAME_END) {
            e.isCancelled = true
        } else {
            val playerDied = e.player

            if(playerDied.inventory.itemInOffHand.type == Material.TOTEM_OF_UNDYING) {
                resurrect(playerDied)
            } else {
                death(playerDied)
                if(e.player.killer is Player) {
                    val killer = e.player.killer!!
                    eliminationDisplay(killer, playerDied)
                } else {
                    if(e.player.location.block.type == Material.STRUCTURE_VOID) {
                        if(Main.getGame().cheeseManager.playerHasCheese(e.player)) {
                            Main.getGame().cheeseManager.playerDropCheese(e.player)
                        }
                        voidEliminationDisplay(playerDied)
                    }
                }
            }
            e.isCancelled = true
        }
    }

    private fun death(player : Player) {
        player.gameMode = GameMode.SPECTATOR
        player.inventory.clear()
        Main.getGame().playerManager.clearPotionEffects(player)
        Main.getGame().statsManager.incrementStat(player.uniqueId, Statistic.DEATHS)
        if(Main.getGame().teamManager.isInRedTeam(player.uniqueId)) {
            Main.getGame().cheeseManager.teamFireworks(player, Teams.RED)
            Main.getGame().respawnTask.startRespawnLoop(player, Main.getPlugin(), Teams.RED)
        } else if(Main.getGame().teamManager.isInBlueTeam(player.uniqueId)) {
            Main.getGame().cheeseManager.teamFireworks(player, Teams.BLUE)
            Main.getGame().respawnTask.startRespawnLoop(player, Main.getPlugin(), Teams.BLUE)
        }
    }

    private fun eliminationDisplay(player : Player, playerKilled : Player) {
        for(allPlayer in Bukkit.getOnlinePlayers()) {
            if(allPlayer != player) {
                allPlayer.sendMessage(Component.text(playerKilled.name, Main.getGame().teamManager.getTeamNamedTextColor(playerKilled)).append(Component.text(" was eliminated by ", NamedTextColor.WHITE).append(Component.text(player.name, Main.getGame().teamManager.getTeamNamedTextColor(player)).append(Component.text(".", NamedTextColor.WHITE)))))
            } else {
                allPlayer.sendMessage(Component.text("[+${5 * Main.getGame().scoreManager.getMultiplier()} ").append(Component.text("coins", NamedTextColor.GOLD).append(Component.text("] You eliminated ", NamedTextColor.WHITE)).append(Component.text(playerKilled.name, Main.getGame().teamManager.getTeamNamedTextColor(playerKilled))).append(Component.text("!", NamedTextColor.WHITE))))
            }
        }
        Main.getGame().scoreManager.modifyScore(5 * Main.getGame().scoreManager.getMultiplier(), ScoreMode.ADD, Main.getGame().teamManager.getPlayerTeam(player.uniqueId))
        Main.getGame().infoBoardManager.updateScoreboardScores()
        Main.getGame().statsManager.incrementStat(player.uniqueId, Statistic.ELIMINATIONS)
        player.playSound(player.location, Sounds.Score.ELIMINATION, 1f, 1.25f)
        player.showTitle(Title.title(Component.text(""), Component.text("[").append(Component.text("⚔", NamedTextColor.GREEN).append(Component.text("] ", NamedTextColor.WHITE)).append(Component.text(playerKilled.name, Main.getGame().teamManager.getTeamNamedTextColor(playerKilled)))), Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ofSeconds(1))))
    }

    private fun voidEliminationDisplay(playerKilled : Player) {
        for(player in Bukkit.getOnlinePlayers()) {
            player.sendMessage(Component.text(playerKilled.name, Main.getGame().teamManager.getTeamNamedTextColor(playerKilled)).append(Component.text(" tried to escape the island... ", NamedTextColor.WHITE)))
        }
    }

    private fun resurrect(player : Player) {
        player.inventory.setItemInOffHand(null)
        player.playSound(player.location, Sounds.Item.USE_ITEM, 1.0f, 1.0f)
        player.playEffect(EntityEffect.TOTEM_RESURRECT)
        player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 60, 1,false, false))
        player.health = 8.0
        resurrectFirework(player)
        when(Main.getGame().teamManager.getPlayerTeam(player.uniqueId)) {
            Teams.RED -> {
                player.teleport(Main.getGame().locationManager.getRedResurrectLoc())
                val velocity = Vector(1.1, 1.45, 0.0)
                player.velocity = velocity
            }
            Teams.BLUE -> {
                player.teleport(Main.getGame().locationManager.getBlueResurrectLoc())
                val velocity = Vector(-1.1, 1.45, 0.0)
                player.velocity = velocity
            }
            Teams.SPECTATOR -> {}
        }
        player.showTitle(Title.title(Component.text(""), Component.text("Your resurrection charm saved you!", NamedTextColor.LIGHT_PURPLE), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(3), Duration.ofSeconds(1))))
        player.playSound(player.location, Sounds.Respawn.RESPAWN, 1.0f, 1.0f)
    }

    private fun resurrectFirework(player : Player) {
        val playerLoc = Location(player.world, player.location.x, player.location.y + 1.0, player.location.z)
        val f: Firework = player.world.spawn(playerLoc, Firework::class.java)
        val fm = f.fireworkMeta
        fm.addEffect(
            FireworkEffect.builder()
                .flicker(false)
                .trail(false)
                .with(FireworkEffect.Type.BALL)
                .withColor(Color.fromRGB(255, 85, 255))
                .build()
        )
        fm.power = 0
        f.fireworkMeta = fm
        f.ticksToDetonate = 1
    }
}