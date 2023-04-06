package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main

import org.bukkit.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

@Suppress("unused")
class BlockPlaceEvent : Listener {
    @EventHandler
    private fun onBlockPlace(e : BlockPlaceEvent) {
        e.isCancelled = !(Main.getGame().getBuildMode() && e.player.isOp)
    }
}