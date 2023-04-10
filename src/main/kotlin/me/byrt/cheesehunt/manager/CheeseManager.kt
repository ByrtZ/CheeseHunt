package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.state.Teams

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.*
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

@Suppress("unused")
class CheeseManager(private val game : Game) {
    private var redCheeseEarned = 0
    private var blueCheeseEarned = 0
    private var tempRedCheeseEarned = 0
    private var tempBlueCheeseEarned = 0
    private var redTotalCheeseCollected = 0
    private var blueTotalCheeseCollected = 0
    private var playerCollectedCheese = mutableMapOf<UUID, Int>()
    private var playerHasCheese = mutableListOf<UUID>()

    private fun incrementCheeseCollected(player : Player) {
        when(Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId)) {
            Teams.RED -> {
                redTotalCheeseCollected += 1
                updateCollectedCheese(player.uniqueId)
            }
            Teams.BLUE -> {
                blueTotalCheeseCollected += 1
                updateCollectedCheese(player.uniqueId)
            }
            Teams.SPECTATOR -> {
                Main.getPlugin().logger.info("[INCREMENTING ERROR] ${player.name} was on team ${Teams.SPECTATOR} when they collected cheese.")
            }
        }
    }

    private fun updateCollectedCheese(uuid : UUID) {
        playerCollectedCheese.putIfAbsent(uuid, 0)
        playerCollectedCheese[uuid] = (playerCollectedCheese[uuid]?.plus(1)) as Int
    }

    fun countCheeseInBases() {
        tempRedCheeseEarned = 0
        tempBlueCheeseEarned = 0
        for (x in 952..956) {
            for (z in 998..1002) {
                if(Bukkit.getWorld("Cheese")?.getBlockAt(x, 0, z)?.type == Material.SPONGE) {
                    redCheeseEarned++
                    tempRedCheeseEarned++
                    Main.getPlugin().logger.info("Cheese collected at $x, 0, $z by the RED team.")
                    Bukkit.getWorld("Cheese")?.getBlockAt(x, 0, z)?.type = Material.AIR
                }
            }
        }
        for (x in 1044..1048) {
            for (z in 998..1002) {
                if(Bukkit.getWorld("Cheese")?.getBlockAt(x, 0, z)?.type == Material.SPONGE) {
                    blueCheeseEarned++
                    tempBlueCheeseEarned++
                    Main.getPlugin().logger.info("Cheese collected at $x, 0, $z by the BLUE team.")
                    Bukkit.getWorld("Cheese")?.getBlockAt(x, 0, z)?.type = Material.AIR
                }
            }
        }
        for(player in Bukkit.getOnlinePlayers()) {
            if(tempRedCheeseEarned > 0) {
                player.sendMessage(Component.text("Red Team ", NamedTextColor.RED).append(Component.text("earned ", NamedTextColor.WHITE)).append(Component.text("${tempRedCheeseEarned * 15} ", NamedTextColor.GOLD)).append(Component.text("coins by claiming ", NamedTextColor.WHITE)).append(Component.text("$tempRedCheeseEarned cheese!", NamedTextColor.WHITE)))
                if(Main.getGame().getTeamManager().isInRedTeam(player.uniqueId)) {
                    player.playSound(player.location, Sounds.Elimination.ELIMINATION, 1f, 1f)
                }
            }
            if(tempBlueCheeseEarned > 0) {
                player.sendMessage(Component.text("Blue Team ", NamedTextColor.BLUE).append(Component.text("earned ", NamedTextColor.WHITE)).append(Component.text("${tempBlueCheeseEarned * 15} ", NamedTextColor.GOLD)).append(Component.text("coins by claiming ", NamedTextColor.WHITE)).append(Component.text("$tempBlueCheeseEarned cheese!", NamedTextColor.WHITE)))
                if(Main.getGame().getTeamManager().isInBlueTeam(player.uniqueId)) {
                    player.playSound(player.location, Sounds.Elimination.ELIMINATION, 1f, 1f)
                }
            }
        }
    }

    fun getUnsortedCheeseCollectedMap() : MutableMap<UUID, Int> {
        return playerCollectedCheese
    }

    fun getSortedCollectedCheeseMap(): Map<UUID, Int> {
        return playerCollectedCheese.toList().sortedBy { (_, int) -> int }.reversed().toMap()
    }

    fun playerPickupCheese(player : Player, blockBreakLocation : Location) {
        if(player.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) {
            player.removePotionEffect(PotionEffectType.SLOW_DIGGING)
        }
        player.inventory.addItem(game.getItemManager().getCheeseItem(Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId)))
        cheeseFirework(blockBreakLocation, player)
        incrementCheeseCollected(player)
        playerHasCheese.add(player.uniqueId)
        for(allPlayers in Bukkit.getOnlinePlayers()) {
            if(allPlayers != player) {
                allPlayers.sendMessage(Component.text("${player.name} picked up a piece of cheese!"))
            } else {
                allPlayers.sendMessage(Component.text("You picked up a piece of cheese!", NamedTextColor.GREEN))
            }
        }
    }

    fun playerDropCheese(player : Player) {
        player.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 40, 255, false, false))
        player.addPotionEffect(PotionEffect(PotionEffectType.DARKNESS, 40, 255, false, false))
        for(allPlayers in Bukkit.getOnlinePlayers()) {
            if(allPlayers != player) {
                allPlayers.sendMessage(Component.text("${player.name} lost a piece of cheese."))
            } else {
                allPlayers.sendMessage(Component.text("You lost your piece of cheese.", NamedTextColor.RED))
            }
        }
        player.inventory.remove(Material.SPONGE)
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
            player.removePotionEffect(PotionEffectType.SLOW)
            player.removePotionEffect(PotionEffectType.DARKNESS)
            player.removePotionEffect(PotionEffectType.SLOW_DIGGING)
        }
    }

    fun getRedCheeseCollectedPercentage(): Double {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.FLOOR
        return df.format(redTotalCheeseCollected.toDouble() / blueCheeseEarned.toDouble() * 100).toDouble()
    }

    fun getBlueCheeseCollectedPercentage(): Double {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.FLOOR
        return df.format(blueTotalCheeseCollected.toDouble() / redCheeseEarned.toDouble() * 100).toDouble()
    }

    fun getRedCheesePlaced() : Int {
        return redCheeseEarned
    }

    fun getBlueCheesePlaced() : Int {
        return blueCheeseEarned
    }

    fun getRedCheeseCollected() : Int {
        return redTotalCheeseCollected
    }

    fun getBlueCheeseCollected() : Int {
        return blueTotalCheeseCollected
    }

    fun resetVars() {
        redCheeseEarned = 0
        blueCheeseEarned = 0
        tempRedCheeseEarned = 0
        tempBlueCheeseEarned = 0
        redTotalCheeseCollected = 0
        blueTotalCheeseCollected = 0
        playerCollectedCheese.clear()
    }

    fun teamWinFireworks(player : Player, teams : Teams) {
        when(teams) {
            Teams.RED -> {
                val playerLoc = Location(player.world, player.location.x, player.location.y + 1, player.location.z)
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
                val playerLoc = Location(player.world, player.location.x, player.location.y + 1, player.location.z)
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

    private fun cheeseFirework(blockLoc : Location, player : Player) {
        val blockToSpawnFireworkLoc = Location(blockLoc.world, blockLoc.x + 0.5, blockLoc.y + 2.0, blockLoc.z + 0.5)
        val f: Firework = player.world.spawn(blockToSpawnFireworkLoc, Firework::class.java)
        val fm = f.fireworkMeta
        fm.addEffect(
            FireworkEffect.builder()
                .flicker(true)
                .trail(false)
                .with(FireworkEffect.Type.BURST)
                .withColor(Color.ORANGE)
                .withFade(Color.YELLOW)
                .build()
        )
        fm.power = 0
        f.fireworkMeta = fm
        f.ticksToDetonate = 1
    }
}