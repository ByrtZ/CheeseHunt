package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.state.Teams

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title

import org.bukkit.*
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

import java.time.Duration
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
    private var playersWithCheeseLoopMap = mutableMapOf<UUID, BukkitRunnable>()

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
                    Main.getPlugin().logger.info("Cheese claimed at $x, 0, $z by the RED team.")
                    Bukkit.getWorld("Cheese")?.getBlockAt(x, 0, z)?.type = Material.AIR
                }
            }
        }
        for (x in 1044..1048) {
            for (z in 998..1002) {
                if(Bukkit.getWorld("Cheese")?.getBlockAt(x, 0, z)?.type == Material.SPONGE) {
                    blueCheeseEarned++
                    tempBlueCheeseEarned++
                    Main.getPlugin().logger.info("Cheese claimed at $x, 0, $z by the BLUE team.")
                    Bukkit.getWorld("Cheese")?.getBlockAt(x, 0, z)?.type = Material.AIR
                }
            }
        }
        game.getScoreManager().modifyScore(tempRedCheeseEarned * 15, ScoreMode.ADD, Teams.RED)
        game.getScoreManager().modifyScore(tempBlueCheeseEarned * 15, ScoreMode.ADD, Teams.BLUE)
        Main.getGame().getInfoBoardManager().updateScoreboardScores()
        for(player in Bukkit.getOnlinePlayers()) {
            if(tempRedCheeseEarned > 0) {
                if(Main.getGame().getTeamManager().isInRedTeam(player.uniqueId)) {
                    player.playSound(player.location, Sounds.Score.CLAIM_CHEESE, 1f, 1f)
                    player.sendMessage(Component.text("[+${tempRedCheeseEarned * 15} ").append(Component.text("coins", NamedTextColor.GOLD)).append(Component.text("] ", NamedTextColor.WHITE)).append(Component.text("Your team earned coins by claiming $tempRedCheeseEarned cheese!", NamedTextColor.GREEN)))
                    teamFireworks(player, Teams.RED)
                }
            }
            if(tempBlueCheeseEarned > 0) {
                if(Main.getGame().getTeamManager().isInBlueTeam(player.uniqueId)) {
                    player.playSound(player.location, Sounds.Score.CLAIM_CHEESE, 1f, 1f)
                    player.sendMessage(Component.text("[+${tempBlueCheeseEarned * 15} ").append(Component.text("coins", NamedTextColor.GOLD)).append(Component.text("] ", NamedTextColor.WHITE)).append(Component.text("Your team earned coins by claiming $tempBlueCheeseEarned cheese!", NamedTextColor.GREEN)))
                    teamFireworks(player, Teams.BLUE)
                }
            }
        }
    }

    fun playerPickupCheese(player : Player, blockBreakLocation : Location) {
        if(player.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) {
            player.removePotionEffect(PotionEffectType.SLOW_DIGGING)
        }
        startHasCheeseLoop(player)
        player.inventory.addItem(game.getItemManager().getCheeseItem(Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId)))
        collectCheeseFirework(blockBreakLocation, player)
        incrementCheeseCollected(player)
        playerHasCheese.add(player.uniqueId)
        for(allPlayers in Bukkit.getOnlinePlayers()) {
            if(allPlayers != player) {
                allPlayers.sendMessage(Component.text("[")
                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                    .append(Component.text("] "))
                    .append(Component.text(player.name, game.getTeamManager().getTeamColour(player)))
                    .append(Component.text(" picked up a piece of cheese!"))
                )
            } else {
                allPlayers.sendMessage(Component.text("You picked up a piece of cheese!", NamedTextColor.GREEN))
                allPlayers.showTitle(Title.title(Component.text(""), Component.text("Cheese picked up!", NamedTextColor.GREEN), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(1))))
                allPlayers.playSound(allPlayers.location, Sounds.Score.COLLECT_CHEESE, 1f, 1f)
            }
        }
    }

    fun playerDropCheese(player : Player) {
        player.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 40, 255, false, false))
        player.addPotionEffect(PotionEffect(PotionEffectType.DARKNESS, 40, 255, false, false))
        for(allPlayers in Bukkit.getOnlinePlayers()) {
            if(allPlayers != player) {
                allPlayers.sendMessage(Component.text("[")
                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                    .append(Component.text("] "))
                    .append(Component.text(player.name, game.getTeamManager().getTeamColour(player)))
                    .append(Component.text(" lost a piece of cheese."))
                )
            } else {
                allPlayers.sendMessage(Component.text("You lost the piece of cheese you were holding.", NamedTextColor.RED))
                allPlayers.showTitle(Title.title(Component.text(""), Component.text("Cheese dropped!", NamedTextColor.RED), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(1))))
                allPlayers.playSound(allPlayers.location, Sounds.Score.LOSE_CHEESE_PRIMARY, 1f, 0f)
                allPlayers.playSound(allPlayers.location, Sounds.Score.LOSE_CHEESE_SECONDARY, 1f, 1f)
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
                } else {
                    player.removePotionEffect(PotionEffectType.GLOWING)
                    stopHasCheeseLoop(player)
                }
                hasCheeseTimer++
            }
        }
        bukkitRunnable.runTaskTimer(Main.getPlugin(), 0L, 20L)
        playersWithCheeseLoopMap[player.uniqueId] = bukkitRunnable
    }

    fun stopHasCheeseLoop(player : Player) {
        playersWithCheeseLoopMap.remove(player.uniqueId)?.cancel()
        player.removePotionEffect(PotionEffectType.GLOWING)
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

    fun getUnsortedCheeseCollectedMap() : MutableMap<UUID, Int> {
        return playerCollectedCheese
    }

    fun getSortedCollectedCheeseMap(): Map<UUID, Int> {
        return playerCollectedCheese.toList().sortedBy { (_, int) -> int }.reversed().toMap()
    }
}