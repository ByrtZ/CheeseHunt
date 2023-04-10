package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.state.GameState
import me.byrt.cheesehunt.task.Music

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerResourcePackStatusEvent
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status

@Suppress("unused")
class PlayerResourcePackLoadEvent : Listener {
    @EventHandler
    private fun onPackLoad(e : PlayerResourcePackStatusEvent) {
        if(Main.getGame().getGameState() == GameState.IN_GAME) {
            if(e.status == Status.SUCCESSFULLY_LOADED) {
                if(Main.getGame().getGameCountdownTask().getTimeLeft() >= 29) {
                    Main.getGame().getMusicTask().startMusicLoop(e.player, Main.getPlugin(), Music.MAIN)
                } else {
                    Main.getGame().getMusicTask().startMusicLoop(e.player, Main.getPlugin(), Music.OVERTIME)
                }
            }
        }
    }
}