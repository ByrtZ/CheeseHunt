package dev.byrt.cheesehunt.event

import dev.byrt.cheesehunt.CheeseHunt
import dev.byrt.cheesehunt.game.GameManager
import dev.byrt.cheesehunt.game.GameState
import dev.byrt.cheesehunt.manager.CheeseManager
import dev.byrt.cheesehunt.manager.ItemManager
import dev.byrt.cheesehunt.manager.PlayerManager
import dev.byrt.cheesehunt.manager.PowerUpItem
import dev.byrt.cheesehunt.manager.ScoreManager
import dev.byrt.cheesehunt.manager.ScoreMode
import dev.byrt.cheesehunt.manager.Statistic
import dev.byrt.cheesehunt.manager.StatisticsManager
import dev.byrt.cheesehunt.state.Sounds
import dev.byrt.cheesehunt.state.Teams
import dev.byrt.cheesehunt.task.RespawnTask
import me.lucyydotp.cheeselib.sys.TeamManager
import me.lucyydotp.cheeselib.sys.nameformat.NameFormatter
import me.lucyydotp.cheeselib.inject.context
import me.lucyydotp.cheeselib.module.Module
import me.lucyydotp.cheeselib.module.ModuleHolder
import me.lucyydotp.cheeselib.module.installBukkitListeners
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import java.time.Duration

@Suppress("unused")
class PlayerDeathEvent(parent: ModuleHolder) : Module(parent), Listener {
    private val cheeseManager: CheeseManager by context()
    private val gameManager: GameManager by context()
    private val itemManager: ItemManager by context()
    private val nameFormatter: NameFormatter by context()
    private val playerManager: PlayerManager by context()
    private val respawnTask: RespawnTask by context()
    private val scoreManager: ScoreManager by context()
    private val statsManager: StatisticsManager by context()
    private val teamManager: TeamManager<Teams> by context()

    init {
        installBukkitListeners()
    }

    @EventHandler
    private fun onDeath(e: PlayerDeathEvent) {
        if (gameManager.getGameState() == GameState.IDLE || gameManager.getGameState() == GameState.STARTING || gameManager.getGameState() == GameState.ROUND_END || gameManager.getGameState() == GameState.GAME_END) {
            e.isCancelled = true
        } else {
            val playerDied = e.player
            if (playerDied.inventory.itemInOffHand.type == Material.TOTEM_OF_UNDYING) {
                itemManager.useItem(PowerUpItem.RESURRECTION_CHARM, playerDied)
            } else {
                death(playerDied)
                if (e.player.killer is Player && playerDied != e.player.killer) {
                    val killer = e.player.killer!!
                    eliminationDisplay(killer, playerDied)
                } else {
                    if (e.player.location.block.type == Material.STRUCTURE_VOID) {
                        if (cheeseManager.playerHasCheese(e.player)) {
                            cheeseManager.playerDropCheese(e.player)
                        }
                        voidEliminationDisplay(playerDied)
                    }
                    if (e.player.killer is TNTPrimed || playerDied == e.player.killer) {
                        tntEliminationDisplay(playerDied)
                    }
                }
            }
            e.isCancelled = true
        }
    }

    private fun death(player: Player) {
        player.gameMode = GameMode.SPECTATOR
        player.inventory.clear()
        playerManager.clearPotionEffects(player)
        statsManager.updateStatistic(player.uniqueId, Statistic.DEATHS)

        val team = teamManager.getTeam(player) ?: return

        cheeseManager.teamFireworks(player, team)
        respawnTask.startRespawnLoop(player, CheeseHunt.getPlugin(), team)
        statsManager.updateStatistic(player.uniqueId, Statistic.KILL_STREAKS)
    }

    private fun eliminationDisplay(player: Player, playerKilled: Player) {
        val team = teamManager.getTeam(player) ?: return
        for (allPlayer in Bukkit.getOnlinePlayers()) {
            if (allPlayer != player) {
                allPlayer.sendMessage(
                    nameFormatter.format(playerKilled)
                        .append(Component.text(" was eliminated by ", NamedTextColor.WHITE))
                        .append(nameFormatter.format(player))
                )
            } else {
                allPlayer.sendMessage(
                    Component.text("[+${5 * scoreManager.getMultiplier()} ").append(
                        Component.text("coins", NamedTextColor.GOLD)
                            .append(Component.text("] You eliminated ", NamedTextColor.WHITE))
                            .append(nameFormatter.format(playerKilled))
                            .append(Component.text("!", NamedTextColor.WHITE))
                    )
                )
            }
        }
        scoreManager.modifyScore(
            5 * scoreManager.getMultiplier(),
            ScoreMode.ADD,
            team
        )
        statsManager.updateStatistic(player.uniqueId, Statistic.ELIMINATIONS)
        statsManager.updateStatistic(player.uniqueId, Statistic.KILL_STREAKS)
        player.playSound(player.location, Sounds.Score.ELIMINATION, 1f, 1.25f)
        player.showTitle(
            Title.title(
                Component.text(""),
                Component.text("[").append(
                    Component.text("⚔", NamedTextColor.GREEN).append(Component.text("] ", NamedTextColor.WHITE)).append(
                        nameFormatter.format(playerKilled)
                    )
                ),
                Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ofSeconds(1))
            )
        )
    }

    private fun voidEliminationDisplay(playerKilled: Player) {
        for (player in Bukkit.getOnlinePlayers()) {
            player.sendMessage(
                nameFormatter.format(playerKilled)
                    .append(Component.text(" tried to escape the island...", NamedTextColor.WHITE))
            )
        }
    }

    private fun tntEliminationDisplay(playerKilled: Player) {
        for (player in Bukkit.getOnlinePlayers()) {
            player.sendMessage(
                nameFormatter.format(playerKilled).append(Component.text(" blew up.", NamedTextColor.WHITE))
            )
        }
    }
}
