package dev.byrt.cheesehunt.manager

import dev.byrt.cheesehunt.Main
import dev.byrt.cheesehunt.game.Game

import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration

import java.io.File
import java.io.IOException

class ConfigManager(private val game : Game) {
    private lateinit var mapConfig : File
    private lateinit var mapFileConfig : FileConfiguration

    fun setup() {
        Main.getPlugin().config.options().copyDefaults()
        Main.getPlugin().saveDefaultConfig()
        setupMapConfig()
    }

    private fun setupMapConfig() {
        mapConfig = File(Bukkit.getServer().pluginManager.getPlugin("CheeseHunt")!!.dataFolder, "map.yml")
        if (!mapConfig.exists()) {
            try {
                mapConfig.createNewFile()
            } catch (e : IOException) {
                // no.
            }
        }
        mapFileConfig = YamlConfiguration.loadConfiguration(mapConfig)
    }

    fun getMapData(data : String, map : Maps) { //TODO: MAP SYSTEM
        when(data) {
            "centre" -> {

            }
            "redSpawn" -> {

            }
            "blueSpawn" -> {

            } else -> {
                Main.getPlugin().logger.warning("No $data data for $map.")
            }
        }
    }

    fun saveMapConfig() {
        try {
            mapFileConfig.save(mapConfig)
        } catch (e: IOException) {
            Main.getPlugin().logger.severe("Unable to save map configuration, printing stack trace:\n${e.printStackTrace()}")
        }
    }


    private fun reloadMapConfig() {
        mapFileConfig = YamlConfiguration.loadConfiguration(mapConfig)
    }

    fun saveReloadMapConfig() {
        saveMapConfig()
        reloadMapConfig()
    }

    fun getMapConfig(): FileConfiguration {
        return mapFileConfig
    }
}
