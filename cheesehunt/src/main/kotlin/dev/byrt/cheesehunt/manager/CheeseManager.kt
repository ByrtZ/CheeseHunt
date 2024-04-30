package dev.byrt.cheesehunt.manager

import dev.byrt.cheesehunt.CheeseHunt
import dev.byrt.cheesehunt.state.Sounds
import dev.byrt.cheesehunt.state.Teams
import me.lucyydotp.cheeselib.sys.TeamManager
import me.lucyydotp.cheeselib.sys.nameformat.NameFormatter
import me.lucyydotp.cheeselib.inject.context
import me.lucyydotp.cheeselib.module.Module
import me.lucyydotp.cheeselib.module.ModuleHolder

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title

import org.bukkit.*
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

import java.time.Duration
import java.util.*

class CheeseManager(parent: ModuleHolder): Module(parent) {
    private val totalCheesePickedUp = EnumMap<Teams, Int>(Teams::class.java)
    private val playerHasCheese = mutableListOf<UUID>()
    private val playersWithCheeseLoopMap = mutableMapOf<UUID, BukkitRunnable>()

    private val itemManager: ItemManager by context()
    private val nameFormatter: NameFormatter by context()
    private val scoreManager: ScoreManager by context()
    private val statsManager: StatisticsManager by context()
    private val teamManager: TeamManager<Teams> by context()

    private fun incrementCheeseCollected(player : Player) {
        val team = teamManager.getTeam(player) ?: return
        statsManager.updateStatistic(player.uniqueId, Statistic.CHEESE_PICKED_UP)
        totalCheesePickedUp[team] = (totalCheesePickedUp[team] ?: 0) + 1
    }

    fun countCheeseInBases(isSilent : Boolean) {
        var redCheeseEarned = 0
        var blueCheeseEarned = 0

        for(x in 952..956) {
            for(z in 998..1002) {
                if(Bukkit.getWorld("Cheese")?.getBlockAt(x, 0, z)?.type == Material.SPONGE) {
                    redCheeseEarned++
                    CheeseHunt.getPlugin().logger.info("Cheese claimed at $x, 0, $z by the RED team.")
                    Bukkit.getWorld("Cheese")?.getBlockAt(x, 0, z)?.type = Material.AIR
                }
            }
        }
        for(x in 1044..1048) {
            for(z in 998..1002) {
                if(Bukkit.getWorld("Cheese")?.getBlockAt(x, 0, z)?.type == Material.SPONGE) {
                    blueCheeseEarned++
                    CheeseHunt.getPlugin().logger.info("Cheese claimed at $x, 0, $z by the BLUE team.")
                    Bukkit.getWorld("Cheese")?.getBlockAt(x, 0, z)?.type = Material.AIR
                }
            }
        }

        if(redCheeseEarned > 0) {
            scoreManager.modifyScore(
                redCheeseEarned * 15 * scoreManager.getMultiplier(),
                ScoreMode.ADD,
                Teams.RED
            )
        }
        if(blueCheeseEarned > 0) {
            scoreManager.modifyScore(
                blueCheeseEarned * 15 * scoreManager.getMultiplier(),
                ScoreMode.ADD,
                Teams.BLUE
            )
        }

        for(player in Bukkit.getOnlinePlayers()) {
            if(redCheeseEarned > 0) {
                if(teamManager.getTeam(player) == Teams.RED) {
                    player.playSound(player.location, Sounds.Score.CLAIM_CHEESE, 1f, 1f)
                    player.sendMessage(Component.text("[+${redCheeseEarned * 15 * scoreManager.getMultiplier()} ").append(Component.text("coins", NamedTextColor.GOLD)).append(Component.text("] ", NamedTextColor.WHITE)).append(Component.text("Your team earned coins by claiming $redCheeseEarned cheese!", NamedTextColor.GREEN)))
                    teamFireworks(player, Teams.RED)
                }
            }
            if(blueCheeseEarned > 0) {
                if(teamManager.getTeam(player) == Teams.BLUE) {
                    player.playSound(player.location, Sounds.Score.CLAIM_CHEESE, 1f, 1f)
                    player.sendMessage(Component.text("[+${blueCheeseEarned * 15 * scoreManager.getMultiplier()} ").append(Component.text("coins", NamedTextColor.GOLD)).append(Component.text("] ", NamedTextColor.WHITE)).append(Component.text("Your team earned coins by claiming $blueCheeseEarned cheese!", NamedTextColor.GREEN)))
                    teamFireworks(player, Teams.BLUE)
                }
            }

            if(!isSilent) {
                player.playSound(player.location, Sounds.Alert.GENERAL_ALERT, 1f, 1f)
                player.sendMessage(
                    Component.text("[")
                        .append(Component.text("▶").color(NamedTextColor.YELLOW))
                        .append(Component.text("] "))
                        .append(Component.text("Cheese in team bases have been counted!", NamedTextColor.AQUA, TextDecoration.BOLD)
                    )
                )
            }
        }
    }

    fun playerPickupCheese(player : Player, blockBreakLocation : Location) {
        if(player.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) {
            player.removePotionEffect(PotionEffectType.SLOW_DIGGING)
        }
        startHasCheeseLoop(player)
        player.inventory.addItem(itemManager.getCheeseItem(teamManager.getTeam(player) ?: return))
        collectCheeseFirework(blockBreakLocation, player)
        incrementCheeseCollected(player)
        playerHasCheese.add(player.uniqueId)
        for(allPlayers in Bukkit.getOnlinePlayers()) {
            if(allPlayers != player) {
                allPlayers.sendMessage(Component.text("[")
                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                    .append(Component.text("] "))
                    .append(nameFormatter.format(player))
                    .append(Component.text(" picked up a piece of cheese."))
                )
            } else {
                allPlayers.sendMessage(Component.text("[")
                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                    .append(Component.text("] "))
                    .append(Component.text("You picked up a piece of cheese!", NamedTextColor.GREEN)))
                allPlayers.showTitle(Title.title(Component.text(""), Component.text("Cheese picked up!", NamedTextColor.GREEN), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(1))))
                allPlayers.playSound(allPlayers.location, Sounds.Score.COLLECT_CHEESE, 1f, 1f)
            }
        }
    }

    fun playerDropCheese(player : Player) {
        player.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 30, 1, false, false))
        player.addPotionEffect(PotionEffect(PotionEffectType.DARKNESS, 40, 255, false, false))
        for(allPlayers in Bukkit.getOnlinePlayers()) {
            if(allPlayers != player) {
                allPlayers.sendMessage(Component.text("[")
                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                    .append(Component.text("] "))
                    .append(nameFormatter.format(player))
                    .append(Component.text(" lost a piece of cheese."))
                )
            } else {
                allPlayers.sendMessage(Component.text("[")
                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                    .append(Component.text("] "))
                    .append(Component.text("You lost the piece of cheese you were holding!", NamedTextColor.RED)))
                allPlayers.showTitle(Title.title(Component.text(""), Component.text("Cheese dropped!", NamedTextColor.RED), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(1))))
                allPlayers.playSound(allPlayers.location, Sounds.Score.LOSE_CHEESE_PRIMARY, 1f, 0f)
                allPlayers.playSound(allPlayers.location, Sounds.Score.LOSE_CHEESE_SECONDARY, 1f, 1f)
            }
        }
        player.inventory.remove(Material.SPONGE)
        statsManager.updateStatistic(player.uniqueId, Statistic.CHEESE_DROPPED)
        if(playerHasCheese.contains(player.uniqueId)) {
            playerHasCheese.remove(player.uniqueId)
        }
    }

    fun playerHasCheese(player : Player) : Boolean {
        return playerHasCheese.contains(player.uniqueId)
    }

    fun setPlayerHasCheese(player : Player, boolean : Boolean) {
        if(!boolean) {
            playerHasCheese.remove(player.uniqueId)
            stopHasCheeseLoop(player)
            player.inventory.remove(Material.SPONGE)
            player.removePotionEffect(PotionEffectType.SLOW)
            player.removePotionEffect(PotionEffectType.DARKNESS)
            player.removePotionEffect(PotionEffectType.SLOW_DIGGING)
        }
    }

    private fun startHasCheeseLoop(player : Player) {
        val bukkitRunnable = object: BukkitRunnable() {
            var hasCheeseTimer = 0
            override fun run() {
                if(playerHasCheese(player)) {
                    holdingCheeseFirework(player)
                    player.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 10, 255, false, false))
                    if(hasCheeseTimer % 2 == 0) {
                        player.sendActionBar(Component.text("⚠ You have a piece of cheese! ⚠", NamedTextColor.RED))
                    } else {
                        player.sendActionBar(Component.text("⚠ You have a piece of cheese! ⚠", NamedTextColor.DARK_RED))
                    }
                } else {
                    player.removePotionEffect(PotionEffectType.GLOWING)
                    stopHasCheeseLoop(player)
                }
                hasCheeseTimer++
            }
        }
        bukkitRunnable.runTaskTimer(CheeseHunt.getPlugin(), 0L, 20L)
        playersWithCheeseLoopMap[player.uniqueId] = bukkitRunnable
    }

    fun stopHasCheeseLoop(player : Player) {
        playersWithCheeseLoopMap.remove(player.uniqueId)?.cancel()
        player.removePotionEffect(PotionEffectType.GLOWING)
        player.sendActionBar(Component.text(" "))
    }

    fun teamFireworks(player : Player, teams : Teams) {
        when(teams) {
            Teams.RED -> {
                val playerLoc = Location(player.world, player.location.x, player.location.y + 1.0, player.location.z)
                val f: Firework = player.world.spawn(playerLoc, Firework::class.java)
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
                val playerLoc = Location(player.world, player.location.x, player.location.y + 1.0, player.location.z)
                val f: Firework = player.world.spawn(playerLoc, Firework::class.java)
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

    private fun collectCheeseFirework(blockLoc : Location, player : Player) {
        val blockToSpawnFireworkLoc = Location(blockLoc.world, blockLoc.x + 0.5, blockLoc.y + 2.0, blockLoc.z + 0.5)
        val f: Firework = player.world.spawn(blockToSpawnFireworkLoc, Firework::class.java)
        val fm = f.fireworkMeta
        fm.addEffect(
            FireworkEffect.builder()
                .flicker(true)
                .trail(false)
                .with(FireworkEffect.Type.BALL)
                .withColor(Color.ORANGE)
                .withFade(Color.YELLOW)
                .build()
        )
        fm.power = 0
        f.fireworkMeta = fm
        f.ticksToDetonate = 1
    }

    private fun holdingCheeseFirework(player : Player) {
        val playerLoc = Location(player.world, player.location.x, player.location.y + 3.0, player.location.z)
        val f: Firework = player.world.spawn(playerLoc, Firework::class.java)
        val fm = f.fireworkMeta
        fm.addEffect(
            FireworkEffect.builder()
                .flicker(false)
                .trail(false)
                .with(FireworkEffect.Type.BURST)
                .withColor(Color.RED)
                .build()
        )
        fm.power = 0
        f.fireworkMeta = fm
        f.ticksToDetonate = 1
    }
}
