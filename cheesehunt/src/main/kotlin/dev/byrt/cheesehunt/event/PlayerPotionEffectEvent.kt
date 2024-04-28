package dev.byrt.cheesehunt.event

import dev.byrt.cheesehunt.game.GameManager
import dev.byrt.cheesehunt.game.GameState
import dev.byrt.cheesehunt.manager.ItemManager
import dev.byrt.cheesehunt.state.Teams
import me.lucyydotp.cheeselib.game.TeamManager
import me.lucyydotp.cheeselib.inject.context
import me.lucyydotp.cheeselib.module.Module
import me.lucyydotp.cheeselib.module.ModuleHolder
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.potion.PotionEffectType

@Suppress("unused")
class PlayerPotionEffectEvent(parent: ModuleHolder) : Module(parent), Listener {

    private val gameManager: GameManager by context()
    private val itemManager: ItemManager by context()
    private val teamManager: TeamManager<Teams> by context()

    @EventHandler
    private fun onLoseInvis(e : EntityPotionEffectEvent) {
        if(gameManager.getGameState() != GameState.IDLE && e.action == EntityPotionEffectEvent.Action.REMOVED || e.action == EntityPotionEffectEvent.Action.CLEARED && e.oldEffect?.equals(PotionEffectType.INVISIBILITY) == true && e.entity is Player) {
            val player = e.entity as Player
            itemManager.givePlayerTeamBoots(player, teamManager.getTeam(player) ?: return)
            // TODO(lucy): spectators need boots too
        }
    }
}
