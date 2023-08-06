package dev.byrt.cheesehunt.manager

import dev.byrt.cheesehunt.Main
import dev.byrt.cheesehunt.game.Game
import dev.byrt.cheesehunt.state.Teams

import org.bukkit.Location

import kotlin.random.Random

@Suppress("unused")
class LocationManager(private val game : Game) {
    private var redSpawns = ArrayList<Location>()
    private var redSpawnCounter = 0
    private var blueSpawns = ArrayList<Location>()
    private var blueSpawnCounter = 0

    private var winShowArea = ArrayList<Location>()

    private val spawn = Location(Main.getPlugin().server.getWorld("Cheese"), 0.5, -52.0 ,0.5, 0.0f, 0.0f)
    private val arenaCentre = Location(Main.getPlugin().server.getWorld("Cheese"), 1000.5, 0.0, 1000.5, 0.0f, 0.0f)

    private val redEscapeLoc = Location(Main.getPlugin().server.getWorld("Cheese"), 961.5, 7.0, 1000.5, -90.0f, 0.0f)
    private val blueEscapeLoc = Location(Main.getPlugin().server.getWorld("Cheese"), 1039.5, 7.0, 1000.5, 90.0f, 0.0f)

    private val itemSpawnLeftRed = Location(Main.getPlugin().server.getWorld("Cheese"), 1000.5, 1.0, 968.5, 90.0f, 0.0f)
    private val itemSpawnLeftBlue = Location(Main.getPlugin().server.getWorld("Cheese"), 1000.5, 1.0, 1032.5, 90.0f, 0.0f)

    fun populateSpawns() {
        redSpawns.add(Location(Main.getPlugin().server.getWorld("Cheese"), 949.5, 3.0, 999.5, -90.0f, 0.0f))
        redSpawns.add(Location(Main.getPlugin().server.getWorld("Cheese"), 949.5, 3.0, 1001.5, -90.0f, 0.0f))
        redSpawns.add(Location(Main.getPlugin().server.getWorld("Cheese"), 947.5, 3.0, 999.5, -90.0f, 0.0f))
        redSpawns.add(Location(Main.getPlugin().server.getWorld("Cheese"), 947.5, 3.0, 1001.5, -90.0f, 0.0f))

        blueSpawns.add(Location(Main.getPlugin().server.getWorld("Cheese"), 1051.5, 3.0, 1001.5, 90.0f, 0.0f))
        blueSpawns.add(Location(Main.getPlugin().server.getWorld("Cheese"), 1051.5, 3.0, 999.5, 90.0f, 0.0f))
        blueSpawns.add(Location(Main.getPlugin().server.getWorld("Cheese"), 1053.5, 3.0, 1001.5, 90.0f, 0.0f))
        blueSpawns.add(Location(Main.getPlugin().server.getWorld("Cheese"), 1053.5, 3.0, 999.5, 90.0f, 0.0f))
    }

    fun populateWinShowArea() {
        game.plugin.logger.info("Drawing game win show area.")
        for(x in 941..1059) {
            for(y in 12..42) {
                for(z in 960..1040) {
                    game.plugin.server.getWorld("Cheese")?.getBlockAt(x, y, z)?.location?.let { winShowArea.add(it) }
                }
            }
        }
        game.plugin.logger.info("Win show area plot complete.")
    }

    fun incrementSpawnCounter(team : Teams) {
        when(team) {
            Teams.RED -> {
                if(redSpawnCounter >= redSpawnCounter - 1) {
                    redSpawnCounter = 0
                } else {
                    redSpawnCounter++
                }
            }
            Teams.BLUE -> {
                if(blueSpawnCounter >= blueSpawnCounter - 1) {
                    blueSpawnCounter = 0
                } else {
                    blueSpawnCounter++
                }
            } else -> {
                Main.getPlugin().logger.severe("An error occurred when attempting to increment a spawn location counter.")
            }
        }
    }

    fun getRedSpawns() : ArrayList<Location> {
        return redSpawns
    }

    private fun setRedSpawnCounter(counter : Int) {
        redSpawnCounter = counter
    }

    fun getRedSpawnCounter() : Int {
        return redSpawnCounter
    }

    fun getBlueSpawns() : ArrayList<Location> {
        return blueSpawns
    }

    private fun setBlueSpawnCounter(counter : Int) {
        blueSpawnCounter = counter
    }

    fun getBlueSpawnCounter() : Int {
        return blueSpawnCounter
    }

    fun resetSpawnCounters() {
        redSpawnCounter = 0
        blueSpawnCounter = 0
    }

    fun getRandomWinShowLoc() : Location {
        val random = Random.nextInt(winShowArea.size - 1)
        return winShowArea[random]
    }

    fun getWinShowArea() : ArrayList<Location> {
        return winShowArea
    }

    fun getSpawn() : Location {
        return spawn
    }

    fun getArenaCentre() : Location {
        return arenaCentre
    }

    fun getRedEscapeLoc() : Location {
        return redEscapeLoc
    }

    fun getBlueEscapeLoc() : Location {
        return blueEscapeLoc
    }

    fun getRedItemSpawn() : Location {
        return itemSpawnLeftRed
    }

    fun getBlueItemSpawn() : Location {
        return itemSpawnLeftBlue
    }
}