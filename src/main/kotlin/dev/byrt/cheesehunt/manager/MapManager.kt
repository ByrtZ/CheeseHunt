package dev.byrt.cheesehunt.manager

import dev.byrt.cheesehunt.game.Game
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.entity.Player

@Suppress("unused")
class MapManager(private val game : Game) {
    private var currentMap = Maps.REFORGED

    fun setCurrentMap(sender : Player?, map : Maps) {
        if(map == currentMap) {
            sender?.sendMessage(Component.text("Unable to set current map to $map as it is already active.", NamedTextColor.RED))
        } else {
            game.infoBoardManager.updateCurrentMap(currentMap, map)
            when(map) {
                Maps.REFORGED -> {
                    currentMap = map
                }
            }
            sender?.sendMessage(Component.text("Current map is now set to $map.", NamedTextColor.GREEN))
        }
    }

    fun getCurrentMap() : Maps {
        return currentMap
    }

    fun createMap(sender : Player, newMapName : String) {
        if(Maps.valueOf(newMapName).mapName.isBlank()) {
            game.configManager.getMapConfig().set("map.$newMapName", null)
            sender.sendMessage(Component.text("Map successfully created with name $newMapName, but must be added to the plugin before it is playable.", NamedTextColor.GREEN))
            game.configManager.saveReloadMapConfig()
        } else {
            sender.sendMessage(Component.text("Map with name $newMapName already exists.", NamedTextColor.RED))
        }
    }

    fun addMapData() {

    }

    fun modifyMapData() {

    }

    fun deleteMapData() {

    }
}

enum class Maps(val mapName : String) {
    REFORGED("Reforged")
}