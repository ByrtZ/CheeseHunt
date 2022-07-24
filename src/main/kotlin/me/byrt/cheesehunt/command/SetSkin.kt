//TODO: Not working at all, temporary command for now. Only usable by Byrt.
package me.byrt.cheesehunt.command

import me.byrt.cheesehunt.Main

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.Bukkit
import org.bukkit.entity.Player

import java.io.InputStreamReader
import java.net.URL

import com.google.gson.JsonObject
import com.google.gson.JsonParser

@Suppress("unused")
class SetSkin : BaseCommand {
    @CommandMethod("setskin <player> <skin>")
    @CommandDescription("Allows skin modification.")
    @CommandPermission("cheesehunt.setskin")
    fun skin(sender : Player, @Argument("player") player : Player, @Argument("skin") skin : String) {
        if(sender.name == "Byrt"){
            try {
                sender.sendMessage(Component.text("You changed ${player.name}'s skin to ${skin}'s skin!").color(NamedTextColor.GREEN))
                val newPlayerProfile = player.playerProfile
                val newTextureProperty = getTextureProperty(skin)

                sender.sendMessage(Component.text("$skin's UUID is ${Bukkit.getPlayerUniqueId(skin)}").color(NamedTextColor.RED))
                sender.sendMessage(Component.text("Base64 value for $skin's skin: $newTextureProperty").color(NamedTextColor.GOLD))

                newPlayerProfile.textures.skin = URL("http://textures.minecraft.net/texture/${newTextureProperty}")
                player.playerProfile = newPlayerProfile
                reloadPlayer(player)
            } catch(e : Exception) {
                sender.sendMessage(Component.text("An error occurred when attempting to change a player's skin.").color(NamedTextColor.RED))
                e.printStackTrace()
            }
        } else {
            sender.sendMessage(Component.text("You do not have permission to execute this command.").color(NamedTextColor.RED))
        }
    }

    private fun getTextureProperty(skinName: String): String {
        val url = URL("https://api.mojang.com/users/profiles/minecraft/$skinName")
        val reader = InputStreamReader(url.openStream())
        val skinUUID = JsonParser.parseReader(reader).asJsonObject["id"].asString
        val url2 = URL("https://sessionserver.mojang.com/session/minecraft/profile/${skinUUID}?unsigned=false")
        val read = InputStreamReader(url2.openStream())
        val textureProperty: JsonObject = JsonParser.parseReader(read).asJsonObject["properties"].asJsonArray[0].asJsonObject
        return textureProperty.get("value").asString
    }

    private fun reloadPlayer(playerToReload : Player) {
        for (p in Bukkit.getOnlinePlayers()) {
            Bukkit.getScheduler().runTaskLater(Main.getPlugin(), Runnable { Bukkit.getOnlinePlayers().forEach { _ -> p.hidePlayer(Main.getPlugin(), playerToReload) } }, 0L)
            Bukkit.getScheduler().runTaskLater(Main.getPlugin(), Runnable { Bukkit.getOnlinePlayers().forEach { _ -> p.showPlayer(Main.getPlugin(), playerToReload) } }, 5L)
        }
    }
}