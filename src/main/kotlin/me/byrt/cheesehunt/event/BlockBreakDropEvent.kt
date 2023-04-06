package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main

import org.bukkit.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockDropItemEvent

@Suppress("unused")
class BlockBreakDropEvent : Listener {
    @EventHandler
    private fun onBlockBreak(e : BlockBreakEvent) {
        e.isCancelled = !(Main.getGame().getBuildMode() && e.player.isOp)
    }

    @EventHandler
    private fun onBlockItemDrop(e : BlockDropItemEvent) {
        e.isCancelled = true
    }
}