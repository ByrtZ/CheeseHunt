package dev.byrt.cheesehunt.event

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockFadeEvent

@Suppress("unused")
class BlockPerishEvent : Listener {
    @EventHandler
    private fun onBlockPerish(e : BlockFadeEvent) {
        e.isCancelled = true
    }
}