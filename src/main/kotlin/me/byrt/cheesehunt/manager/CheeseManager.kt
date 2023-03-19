package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.Main

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.*
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

import kotlin.collections.ArrayList

@Suppress("unused")
class CheeseManager(private val game : Game) {
    private var redTotalCheesePlaced = 0
    private var blueTotalCheesePlaced = 0
    private var redTotalCheeseCollected = 0
    private var blueTotalCheeseCollected = 0
    private var redFinishedPlacing = false
    private var blueFinishedPlacing = false
    private var redFinishedCollecting = false
    private var blueFinishedCollecting = false
    private var uncollectedCheese = ArrayList<Location>()
    private var playerCollectedCheese = mutableMapOf<UUID, Int>()

    fun incrementCheesePlaced(player : Player) {
        when(Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId)) {
            Teams.RED -> {
                redTotalCheesePlaced += 1
            }
            Teams.BLUE -> {
                blueTotalCheesePlaced += 1
            }
            Teams.SPECTATOR -> {
                Main.getPlugin().logger.info("[INCREMENTING ERROR] Game attempted to increment cheese placed for spectators.")
            }
        }
    }

    fun incrementCheeseCollected(player : Player) {
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

    fun markUncollectedCheese() {
        for (x in 0..32) { // Scan Red arena for uncollected cheese and mark them
            for (y in -53..-39) {
                for(z in 36..73) {
                    if(Bukkit.getWorld("Cheese")?.getBlockAt(x, y, z)?.type == Material.SPONGE) {
                        Main.getPlugin().logger.info("[RED ARENA: UNCOLLECTED CHEESE] Found at $x, $y, $z")
                        uncollectedCheese.add(Location(Bukkit.getWorld("Cheese"), x.toDouble(), y.toDouble(), z.toDouble()))
                        val cheeseMarker : ArmorStand? = Bukkit.getWorld("Cheese")?.spawn(
                            Location(Bukkit.getWorld("Cheese"), x.toDouble() + 0.5, y.toDouble() - 1.2, z.toDouble() + 0.5),
                            ArmorStand::class.java
                        )
                        cheeseMarker?.setGravity(false)
                        cheeseMarker?.isInvisible = true
                        cheeseMarker?.isGlowing = true
                        cheeseMarker?.isMarker = true
                        cheeseMarker?.equipment?.helmet = ItemStack(Material.SPONGE)
                        cheeseMarker?.scoreboardTags?.add("uncollectedCheese")
                        if (cheeseMarker != null) {
                            game.getTeamManager().getUncollectedCheeseDisplayTeam().addEntity(cheeseMarker)
                        }
                    }
                }
            }
        }

        for (x in -79..-48) { // Scan Blue arena for uncollected cheese and mark them
            for (y in -53..-39) {
                for(z in 36..73) {
                    if(Bukkit.getWorld("Cheese")?.getBlockAt(x, y, z)?.type == Material.SPONGE) {
                        Main.getPlugin().logger.info("[BLUE ARENA: UNCOLLECTED CHEESE] Found at $x, $y, $z")
                        uncollectedCheese.add(Location(Bukkit.getWorld("Cheese"), x.toDouble(), y.toDouble(), z.toDouble()))
                        val cheeseMarker : ArmorStand? = Bukkit.getWorld("Cheese")?.spawn(
                            Location(Bukkit.getWorld("Cheese"), x.toDouble() + 0.5, y.toDouble() - 1.2, z.toDouble() + 0.5),
                            ArmorStand::class.java
                        )
                        cheeseMarker?.setGravity(false)
                        cheeseMarker?.isInvisible = true
                        cheeseMarker?.isGlowing = true
                        cheeseMarker?.isMarker = true
                        cheeseMarker?.equipment?.helmet = ItemStack(Material.SPONGE)
                        cheeseMarker?.scoreboardTags?.add("uncollectedCheese")
                        if (cheeseMarker != null) {
                            game.getTeamManager().getUncollectedCheeseDisplayTeam().addEntity(cheeseMarker)
                        }
                    }
                }
            }
        }
    }

    fun clearUnmarkedCheeseMarkers() {
        Main.getPlugin().logger.info("[INFO] Removing all marked cheese markers and uncollected cheese blocks.")
        for(marker in Bukkit.getWorld("Cheese")?.getEntitiesByClass(ArmorStand::class.java)!!) {
            Main.getPlugin().logger.info("[INFO] Marker removed.")
            marker.remove()
        }
        for(cheese in uncollectedCheese) {
            Main.getPlugin().logger.info("[INFO] Cheese block removed.")
            cheese.block.type = Material.AIR
        }
    }

    private fun updateCollectedCheese(uuid : UUID) {
        playerCollectedCheese.putIfAbsent(uuid, 0)
        playerCollectedCheese[uuid] = (playerCollectedCheese[uuid]?.plus(1)) as Int
    }

    fun getUnsortedCheeseCollectedMap() : MutableMap<UUID, Int> {
        return playerCollectedCheese
    }

    fun getSortedCollectedCheeseMap(): Map<UUID, Int> {
        return playerCollectedCheese.toList().sortedBy { (_, int) -> int }.reversed().toMap()
    }

    fun checkCheesePlaced() {
        if(!hasRedFinishedPlacing() && Main.getGame().getTeamManager().getRedTeam().size.times(4) == getRedCheesePlaced()) {
            setRedFinishedPlacing(true)
            for(player in Bukkit.getOnlinePlayers()) {
                player.sendMessage(Component.text("[")
                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                    .append(Component.text("] "))
                    .append(Component.text("Red team has placed all their cheese.").color(NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true))
                )
            }
            if(hasRedFinishedPlacing() && hasBlueFinishedPlacing()) {
                for(player in Bukkit.getOnlinePlayers()) {
                    player.sendMessage(Component.text("[")
                        .append(Component.text("▶").color(NamedTextColor.YELLOW))
                        .append(Component.text("] "))
                        .append(Component.text("All cheese has been placed, get ready to hunt!").color(NamedTextColor.AQUA).decoration(TextDecoration.BOLD, true))
                    )
                }
                Main.getGame().getGameCountdownTask().setTimeLeft(0)
            }
        }
        if(!hasBlueFinishedPlacing() && Main.getGame().getTeamManager().getBlueTeam().size.times(4) == getBlueCheesePlaced()) {
            setBlueFinishedPlacing(true)
            for(player in Bukkit.getOnlinePlayers()) {
                player.sendMessage(Component.text("[")
                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                    .append(Component.text("] "))
                    .append(Component.text("Blue team has placed all their cheese.").color(NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true))
                )
            }
            if(hasRedFinishedPlacing() && hasBlueFinishedPlacing()) {
                for(player in Bukkit.getOnlinePlayers()) {
                    player.sendMessage(Component.text("[")
                        .append(Component.text("▶").color(NamedTextColor.YELLOW))
                        .append(Component.text("] "))
                        .append(Component.text("All cheese has been placed, get ready to hunt!").color(NamedTextColor.AQUA).decoration(TextDecoration.BOLD, true))
                    )
                }
                Main.getGame().getGameCountdownTask().setTimeLeft(0)
            }
        }
    }

    fun checkCheeseCollected() {
        if(!hasRedFinishedCollecting() && getBlueCheesePlaced() == getRedCheeseCollected()) {
            setRedFinishedCollecting(true)
            for(player in Bukkit.getOnlinePlayers()) {
                player.sendMessage(
                    Component.text("[")
                        .append(Component.text("▶").color(NamedTextColor.YELLOW))
                        .append(Component.text("] "))
                        .append(
                            Component.text("Red team finished collecting their cheese.").color(NamedTextColor.GOLD)
                                .decoration(TextDecoration.BOLD, true)
                    )
                )
            }
            if(!blueFinishedCollecting) {
                Main.getGame().getTeamManager().redWinGame()
            }

            if(hasRedFinishedCollecting() && hasBlueFinishedCollecting()) {
                for(player in Bukkit.getOnlinePlayers()) {
                    player.sendMessage(
                        Component.text("[")
                        .append(Component.text("▶").color(NamedTextColor.YELLOW))
                        .append(Component.text("] "))
                        .append(
                            Component.text("All cheese has been collected.").color(NamedTextColor.AQUA).decoration(
                                TextDecoration.BOLD, true))
                    )
                }
                Main.getGame().getGameCountdownTask().setTimeLeft(0)
            }
        }
        if(!hasBlueFinishedCollecting() && getRedCheesePlaced() == getBlueCheeseCollected()) {
            setBlueFinishedCollecting(true)
            for(player in Bukkit.getOnlinePlayers()) {
                player.sendMessage(
                    Component.text("[")
                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                    .append(Component.text("] "))
                    .append(
                        Component.text("Blue team finished collecting their cheese.").color(NamedTextColor.GOLD).decoration(
                            TextDecoration.BOLD, true))
                )
            }
            if(!redFinishedCollecting) {
                Main.getGame().getTeamManager().blueWinGame()
            }

            if(hasRedFinishedCollecting() && hasBlueFinishedCollecting()) {
                for(player in Bukkit.getOnlinePlayers()) {
                    player.sendMessage(
                        Component.text("[")
                        .append(Component.text("▶").color(NamedTextColor.YELLOW))
                        .append(Component.text("] "))
                        .append(
                            Component.text("All cheese has been collected.").color(NamedTextColor.AQUA).decoration(
                                TextDecoration.BOLD, true))
                    )
                }
                Main.getGame().getGameCountdownTask().setTimeLeft(0)
            }
        }
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

    fun cheeseFirework(blockLoc : Location, player : Player) {
        val blockToSpawnFireworkLoc = Location(blockLoc.world, blockLoc.x + 0.5, blockLoc.y + 2.0, blockLoc.z + 0.5)
        val f: Firework = player.world.spawn(blockToSpawnFireworkLoc, Firework::class.java)
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
        f.ticksToDetonate = 1
    }

    fun getRedCheeseCollectedPercentage(): Double {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.FLOOR
        return df.format(redTotalCheeseCollected.toDouble() / blueTotalCheesePlaced.toDouble() * 100).toDouble()
    }

    fun getBlueCheeseCollectedPercentage(): Double {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.FLOOR
        return df.format(blueTotalCheeseCollected.toDouble() / redTotalCheesePlaced.toDouble() * 100).toDouble()
    }

    fun getRedCheesePlaced() : Int {
        return redTotalCheesePlaced
    }

    fun getBlueCheesePlaced() : Int {
        return blueTotalCheesePlaced
    }

    fun getRedCheeseCollected() : Int {
        return redTotalCheeseCollected
    }

    fun getBlueCheeseCollected() : Int {
        return blueTotalCheeseCollected
    }

    private fun hasRedFinishedPlacing() : Boolean {
        return redFinishedPlacing
    }

    private fun setRedFinishedPlacing(state : Boolean) {
        redFinishedPlacing = state
    }

    private fun hasBlueFinishedPlacing() : Boolean {
        return blueFinishedPlacing
    }

    private fun setBlueFinishedPlacing(state : Boolean) {
        blueFinishedPlacing = state
    }

    fun hasRedFinishedCollecting() : Boolean {
        return redFinishedCollecting
    }

    private fun setRedFinishedCollecting(state : Boolean) {
        redFinishedCollecting = state
    }

    fun hasBlueFinishedCollecting() : Boolean {
        return blueFinishedCollecting
    }

    private fun setBlueFinishedCollecting(state : Boolean) {
        blueFinishedCollecting = state
    }
}