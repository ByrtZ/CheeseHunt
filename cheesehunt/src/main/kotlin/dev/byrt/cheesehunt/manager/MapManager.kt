package dev.byrt.cheesehunt.manager

import dev.byrt.cheesehunt.game.Game
import me.lucyydotp.cheeselib.module.EventEmitter
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.entity.Player

/**
 * Emitted when the map changes.
 */
data class MapChangeEvent(
    /**
     * The map that the game was on.
     */
    val oldMap: Maps,
    /**
     * The map that the game is now on.
     */
    val newMap: Maps,
)

class MapManager(private val game : Game) {
    val onChange = EventEmitter<MapChangeEvent>()
    private var currentMap = Maps.REFORGED

    fun setCurrentMap(sender : Player?, map : Maps) {
        if(map == currentMap) {
            sender?.sendMessage(Component.text("Unable to set current map to $map as it is already active.", NamedTextColor.RED))
        } else {
            val oldMap = currentMap
            when(map) {
                Maps.REFORGED -> {
                    currentMap = map
                }
            }
            onChange.emit(MapChangeEvent(oldMap, map))

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
