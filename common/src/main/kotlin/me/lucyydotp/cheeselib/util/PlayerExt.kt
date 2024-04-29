package me.lucyydotp.cheeselib.util

import io.papermc.paper.entity.TeleportFlag
import net.minecraft.server.level.ServerPlayer
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerTeleportEvent

/**
 * The player as a Minecraft [ServerPlayer].
 */
val Player.nms: ServerPlayer get() = (this as CraftPlayer).handle

fun Player.teleportWithPassengers(location: Location) {
    teleportAsync(location, PlayerTeleportEvent.TeleportCause.PLUGIN, TeleportFlag.EntityState.RETAIN_PASSENGERS)
}
