package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.state.GameState
import me.byrt.cheesehunt.task.Music
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerResourcePackStatusEvent
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status
import java.time.Duration

@Suppress("unused")
class PlayerResourcePackLoadEvent : Listener {
    @EventHandler
    private fun onPackLoad(e : PlayerResourcePackStatusEvent) {
        if(Main.getGame().gameManager.getGameState() == GameState.IN_GAME) {
            if(e.status == Status.SUCCESSFULLY_LOADED) {
                if(Main.getGame().gameTask.getTimeLeft() >= 29) {
                    Main.getGame().musicTask.startMusicLoop(e.player, Main.getPlugin(), Music.MAIN)
                } else {
                    Main.getGame().musicTask.startMusicLoop(e.player, Main.getPlugin(), Music.OVERTIME)
                }
            }
        }
        e.player.showTitle(Title.title(Component.text("\uD000"), Component.text(""), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(1))))
    }
}