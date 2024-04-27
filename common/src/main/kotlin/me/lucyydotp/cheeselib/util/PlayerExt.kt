package me.lucyydotp.cheeselib.util

import net.minecraft.server.level.ServerPlayer
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
import org.bukkit.entity.Player

/**
 * The player as a Minecraft [ServerPlayer].
 */
val Player.nms: ServerPlayer get() = (this as CraftPlayer).handle
