package dev.byrt.cheesehunt.event

import com.destroystokyo.paper.event.server.PaperServerListPingEvent

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Suppress("unused")
class ServerListEvent : Listener {
    @EventHandler
    private fun onServerListPing(e : PaperServerListPingEvent) {
        e.setHidePlayers(false)
        e.version = "Byrtrium v1.0.0"
    }
}