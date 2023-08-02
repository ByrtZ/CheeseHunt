package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.game.Game

import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration

import java.io.File
import java.io.IOException

@Suppress("unused")
class ConfigManager(private val game : Game) {
    private lateinit var whitelistConfig : File
    private lateinit var whitelistFileConfig : FileConfiguration
    private lateinit var mapConfig : File
    private lateinit var mapFileConfig : FileConfiguration

    fun setup() {
        Main.getPlugin().config.options().copyDefaults()
        Main.getPlugin().saveDefaultConfig()
        setupWhitelistConfig()
        setupMapConfig()
    }

    private fun setupWhitelistConfig() {
        whitelistConfig = File(Bukkit.getServer().pluginManager.getPlugin("CheeseHunt")!!.dataFolder, "whitelist.yml")
        if (!whitelistConfig.exists()) {
            try {
                whitelistConfig.createNewFile()
            } catch (e : IOException) {
                // no.
            }
        }
       whitelistFileConfig = YamlConfiguration.loadConfiguration(whitelistConfig)
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

    fun saveWhitelistConfig() {
        try {
            whitelistFileConfig.save(whitelistConfig)
        } catch (e: IOException) {
            Main.getPlugin().logger.severe("Unable to save whitelist configuration, printing stack trace:\n${e.printStackTrace()}")
        }
    }

    private fun reloadMapConfig() {
        mapFileConfig = YamlConfiguration.loadConfiguration(mapConfig)
    }

    fun reloadWhitelistConfig() {
        whitelistFileConfig = YamlConfiguration.loadConfiguration(whitelistConfig)
    }

    fun saveReloadMapConfig() {
        saveMapConfig()
        reloadMapConfig()
    }

    fun getWhitelistConfig(): FileConfiguration {
        return whitelistFileConfig
    }

    fun getMapConfig(): FileConfiguration {
        return mapFileConfig
    }
}