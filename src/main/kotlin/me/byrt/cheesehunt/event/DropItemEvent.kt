package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

@Suppress("unused")
class DropItemEvent : Listener {
    @EventHandler
    private fun dropItem(e : PlayerDropItemEvent) {
        e.isCancelled = !Main.getGame().getBuildMode()
    }
}