package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.Main

import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration

import java.io.File
import java.io.IOException

@Suppress("unused")
class ConfigManager(private val game : Game) {
    private lateinit var whitelistConfig : File
    private lateinit var whitelistFileConfig : FileConfiguration

    fun setup() {
        Main.getPlugin().config.options().copyDefaults()
        Main.getPlugin().saveDefaultConfig()
        setupWhitelistConfig()
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

    fun getWhitelistConfig(): FileConfiguration {
        return whitelistFileConfig
    }

    fun saveWhitelistConfig() {
        try {
            whitelistFileConfig.save(whitelistConfig)
        } catch (e: IOException) {
            Main.getPlugin().logger.severe("Couldn't save file.")
        }
    }

    fun reloadWhitelistConfig() {
        whitelistFileConfig = YamlConfiguration.loadConfiguration(whitelistConfig)
    }
}