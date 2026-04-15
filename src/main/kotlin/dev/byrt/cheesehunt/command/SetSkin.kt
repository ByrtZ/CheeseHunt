package dev.byrt.cheesehunt.command

import dev.byrt.cheesehunt.Main
import dev.byrt.cheesehunt.util.DevStatus

import com.destroystokyo.paper.profile.PlayerProfile
import com.destroystokyo.paper.profile.ProfileProperty

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.papermc.paper.command.brigadier.CommandSourceStack

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer

import java.io.InputStreamReader

import java.net.URL

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class SetSkin {
    @Command("setskin <player> <skin>")
    @CommandDescription("Allows skin modification.")
    @Permission("cheesehunt.setskin")
    fun skin(css: CommandSourceStack, @Argument("player") player : Player, @Argument("skin") skin : String) {
        val sender = css.sender as Player
        try {
            sender.sendMessage(Component.text("Attempting to change ${player.name}'s skin to ${skin}'s skin...").color(NamedTextColor.GRAY))
            setPlayerSkin(player, skin)
            Main.getGame().dev.parseDevMessage("${player.name}'s skin updated to ${skin}'s skin by ${sender.name}.", DevStatus.INFO)
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