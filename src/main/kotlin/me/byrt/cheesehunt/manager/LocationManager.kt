package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.state.Teams

import org.bukkit.Location

@Suppress("unused")
class LocationManager(private var game : Game) {
    private var redSpawns = ArrayList<Location>()
    private var redSpawnCounter = 0
    private var blueSpawns = ArrayList<Location>()
    private var blueSpawnCounter = 0
    private val spawn = Location(Main.getPlugin().server.getWorld("Cheese"), 0.5, -52.0 ,0.5, 0.0f, 0.0f)
    private val arenaCentre = Location(Main.getPlugin().server.getWorld("Cheese"), 1000.5, 0.0, 1000.5, 0.0f, 0.0f)

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

    fun incrementSpawnCounter(team : Teams) {
        when(team) {
            Teams.RED -> {
                if(redSpawnCounter >= 3) {
                    redSpawnCounter = 0
                } else {
                    redSpawnCounter++
                }
            }
            Teams.BLUE -> {
                if(blueSpawnCounter >= 3) {
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

    fun getSpawn() : Location {
        return spawn
    }

    fun getArenaCentre() : Location {
        return arenaCentre
    }
}