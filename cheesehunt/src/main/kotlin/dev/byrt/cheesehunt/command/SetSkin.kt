package dev.byrt.cheesehunt.command

import dev.byrt.cheesehunt.CheeseHunt
import dev.byrt.cheesehunt.util.DevStatus

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission

import com.destroystokyo.paper.profile.PlayerProfile
import com.destroystokyo.paper.profile.ProfileProperty

import com.google.gson.JsonObject
import com.google.gson.JsonParser

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.Bukkit
import org.bukkit.entity.Player

import java.io.InputStreamReader

import java.net.URL

@Suppress("unused")
class SetSkin : BaseCommand {
    @CommandMethod("setskin <player> <skin>")
    @CommandDescription("Allows skin modification.")
    @CommandPermission("cheesehunt.setskin")
    fun skin(sender : Player, @Argument("player") player : Player, @Argument("skin") skin : String) {
        try {
            sender.sendMessage(Component.text("Attempting to change ${player.name}'s skin to ${skin}'s skin...").color(NamedTextColor.GRAY))
            setPlayerSkin(player, skin)
            CheeseHunt.getGame().dev.parseDevMessage("${player.name}'s skin updated to ${skin}'s skin by ${sender.name}.", DevStatus.INFO)
        } catch(e : Exception) {
            sender.sendMessage(Component.text("An error occurred when attempting to change a player's skin.").color(NamedTextColor.RED))
            e.printStackTrace()
        }
    }

    private fun setPlayerSkin(playerToChange : Player, playerSkinToGrabName : String) {
        try {
            val uuid = URL("https://api.mojang.com/users/profiles/minecraft/${playerSkinToGrabName}")
            val userReader = InputStreamReader(uuid.openStream())
            val id = JsonParser.parseReader(userReader).asJsonObject.get("id").asString
            val profile: PlayerProfile = Bukkit.createProfile(playerToChange.uniqueId, id)
            val mojang = URL("https://sessionserver.mojang.com/session/minecraft/profile/$id?unsigned=false")
            val reader = InputStreamReader(mojang.openStream())
            val textureProperty : JsonObject = JsonParser.parseReader(reader).asJsonObject.get("properties").asJsonArray.get(0).asJsonObject
            if (textureProperty.get("value").asString != null && textureProperty.get("signature").asString != null) {
                val texture = textureProperty.get("value").asString
                val signature = textureProperty.get("signature").asString
                profile.clearProperties()
                profile.setProperty(ProfileProperty("textures", texture, signature))
                playerToChange.playerProfile = profile
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
