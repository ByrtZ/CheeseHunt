package me.byrt.cheesehunt.command

import cloud.commandframework.annotations.*

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.Maps
import me.byrt.cheesehunt.state.GameState

import net.kyori.adventure.text.Component

import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player

@Suppress("unused")
class MapCommands : BaseCommand {
    @CommandMethod("map set <map>")
    @CommandDescription("Changes the map to be played.")
    @CommandPermission("cheesehunt.map.set")
    @Confirmation
    fun setMap(sender : Player, @Argument("map") map : Maps) {
        sender.sendMessage(Component.text("Command currently unavailable.", NamedTextColor.RED))
        /*if(Main.getGame().getGameState() == GameState.IDLE) {
            try {
                Main.getGame().getMapManager().setCurrentMap(sender, map)
            } catch (e : Exception) {
                sender.sendMessage(Component.text("Unable to switch map to $map.", NamedTextColor.RED))
            }
        } else {
            sender.sendMessage(Component.text("Unable to switch map to $map as the game is not idle.", NamedTextColor.RED))
        }*/
    }

    @CommandMethod("map create <name>")
    @CommandDescription("Creates a new map with no data.")
    @CommandPermission("cheesehunt.map.create")
    @Confirmation
    fun createNewMap(sender : Player, @Argument("name") mapName : String) {
        sender.sendMessage(Component.text("Command currently unavailable.", NamedTextColor.RED))
        /*if(Main.getGame().getGameState() == GameState.IDLE) {
            try {
                Main.getGame().getMapManager().createMap(sender, mapName)
            } catch (e : Exception) {
                sender.sendMessage(Component.text("Unable to create map named $mapName.", NamedTextColor.RED))
            }
        } else {
            sender.sendMessage(Component.text("Unable to switch map to $mapName as the game is not idle.", NamedTextColor.RED))
        }*/
    }





    // switch map command
    // create/delete/modify map data
    /*
    * /map create <name>
    * /map delete <name>
    * /mapdata add <mapName> <location: <x,y,z>, [yaw, pitch]> <type: red, blue, cheese_point etc.>
    * /mapdata remove <mapName> <type: red, blue, cheese_point etc.>
    * /mapdata get <mapName> <type>
    * */
}