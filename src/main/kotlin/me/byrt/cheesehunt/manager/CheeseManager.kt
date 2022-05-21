package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.Main

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.inventory.ItemStack


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

    fun incrementCheesePlaced(team : Team) {
        when(team) {
            Team.RED -> {
                redTotalCheesePlaced += 1
            }
            Team.BLUE -> {
                blueTotalCheesePlaced += 1
            }
            Team.SPECTATOR -> {
                Main.getPlugin().logger.info("[INCREMENTING ERROR] Game attempted to increment cheese placed for spectators.")
            }
        }
    }

    fun incrementCheeseCollected(team : Team) {
        when(team) {
            Team.RED -> {
                redTotalCheeseCollected += 1
            }
            Team.BLUE -> {
                blueTotalCheeseCollected += 1
            }
            Team.SPECTATOR -> {
                Main.getPlugin().logger.info("[INCREMENTING ERROR] Game attempted to increment cheese collected for specators.")
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
                    }
                }
            }
        }
    }

    fun clearUnmarkedCheeseMarkers() {
        Main.getPlugin().logger.info("Removing all marked cheese markers and uncollected cheese blocks.")
        for(marker in Bukkit.getWorld("Cheese")?.getEntitiesByClass(ArmorStand::class.java)!!) {
            Main.getPlugin().logger.info("Marker removed.")
            marker.remove()
        }
        for(cheese in uncollectedCheese) {
            Main.getPlugin().logger.info("Cheese block removed.")
            cheese.block.type = Material.AIR
        }
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

    fun hasRedFinishedPlacing() : Boolean {
        return redFinishedPlacing
    }

    fun setRedFinishedPlacing(state : Boolean) {
        redFinishedPlacing = state
    }

    fun hasBlueFinishedPlacing() : Boolean {
        return blueFinishedPlacing
    }

    fun setBlueFinishedPlacing(state : Boolean) {
        blueFinishedPlacing = state
    }

    fun hasRedFinishedCollecting() : Boolean {
        return redFinishedCollecting
    }

    fun setRedFinishedCollecting(state : Boolean) {
        redFinishedCollecting = state
    }

    fun hasBlueFinishedCollecting() : Boolean {
        return blueFinishedCollecting
    }

    fun setBlueFinishedCollecting(state : Boolean) {
        blueFinishedCollecting = state
    }
}