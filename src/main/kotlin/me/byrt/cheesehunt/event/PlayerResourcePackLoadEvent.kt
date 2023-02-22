package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerResourcePackStatusEvent
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status

@Suppress("unused")
class PlayerResourcePackLoadEvent : Listener {
    @EventHandler
    private fun onPackLoad(e : PlayerResourcePackStatusEvent) {
        if(Main.getGame().getMusicLooper()) {
            if(e.status == Status.SUCCESSFULLY_LOADED) {
                Main.getGame().getMusicLoop().startMusicLoop(e.player, Main.getPlugin())
            }
        }
    }
}