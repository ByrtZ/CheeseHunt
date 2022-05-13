package me.byrt.cheesehunt.event

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

@Suppress("unused")
class BlockBreakEvent : Listener {
    @EventHandler
    private fun onBlockBreak(e : BlockBreakEvent) {
        e.isCancelled = true
    }
}