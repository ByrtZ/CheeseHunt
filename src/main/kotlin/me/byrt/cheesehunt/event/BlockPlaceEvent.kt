package me.byrt.cheesehunt.event

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

@Suppress("unused")
class BlockPlaceEvent : Listener {
    @EventHandler
    private fun onBlockPlace(e : BlockPlaceEvent) {
        //e.isCancelled = true
    }
}