package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.util.DevStatus

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent

@Suppress("unused")
class AttemptJoinEvent : Listener {
    @EventHandler
    private fun onAttemptLogin(e : AsyncPlayerPreLoginEvent) {
        if(!Bukkit.getWhitelistedPlayers().contains(Bukkit.getOfflinePlayer(e.uniqueId))) {
            Main.getGame().dev.parseDevMessage("Player '${e.name}' (${e.uniqueId}) attempted to join but is not whitelisted.", DevStatus.SEVERE)
        }
    }
}