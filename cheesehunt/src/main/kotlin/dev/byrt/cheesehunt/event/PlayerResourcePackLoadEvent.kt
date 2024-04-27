package dev.byrt.cheesehunt.event

import dev.byrt.cheesehunt.CheeseHunt
import dev.byrt.cheesehunt.game.GameState
import dev.byrt.cheesehunt.task.Music

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
        if(e.status == Status.SUCCESSFULLY_LOADED) {
//            e.player.showTitle(Title.title(Component.text("\uD000"), Component.text(""), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(1))))

            if(CheeseHunt.getGame().gameManager.getGameState() == GameState.IN_GAME) {
                if(CheeseHunt.getGame().gameTask.getTimeLeft() >= 29) {
                    CheeseHunt.getGame().musicTask.startMusicLoop(e.player, CheeseHunt.getPlugin(), Music.MAIN)
                } else {
                    CheeseHunt.getGame().musicTask.startMusicLoop(e.player, CheeseHunt.getPlugin(), Music.OVERTIME)
                }
            }

            if(CheeseHunt.getGame().gameManager.getGameState() == GameState.OVERTIME) {
                CheeseHunt.getGame().musicTask.startMusicLoop(e.player, CheeseHunt.getPlugin(), Music.OVERTIME)
            }
        }
    }
}
