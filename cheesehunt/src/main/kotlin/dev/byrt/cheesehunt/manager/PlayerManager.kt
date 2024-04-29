package dev.byrt.cheesehunt.manager

import dev.byrt.cheesehunt.game.Game
import dev.byrt.cheesehunt.state.Teams
import me.lucyydotp.cheeselib.util.teleportWithPassengers
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class PlayerManager(private var game: Game) {
    fun setPlayersNotFlying() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player -> player.allowFlight }.forEach { player: Player ->
            disableFlightPlayers(player)
        }
    }

    private fun disableFlightPlayers(player: Player) {
        if (player.gameMode == GameMode.ADVENTURE || player.gameMode == GameMode.SURVIVAL) {
            player.allowFlight = false
            player.isFlying = false
        }
    }

    fun setPlayersFlying() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player -> !player.allowFlight }.forEach { player: Player ->
            enableFlightPlayers(player)
        }
    }

    private fun enableFlightPlayers(player: Player) {
        if (player.gameMode == GameMode.ADVENTURE || player.gameMode == GameMode.SURVIVAL) {
            player.velocity = Vector(0.0, 0.5, 0.0)
            player.allowFlight = true
            player.isFlying = true
        }
    }

    fun giveItemsToPlayers() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player -> player.gameMode == GameMode.ADVENTURE }
            .forEach { player: Player -> giveItems(player) }
    }

    private fun giveItems(player: Player) {
        if (game.teams.getTeam(player) != null) {
            game.itemManager.givePlayerKit(player)
        }
    }

    fun clearCheese() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player -> player.gameMode == GameMode.ADVENTURE }
            .forEach { player: Player -> clearSpongeItems(player) }
    }

    private fun clearSpongeItems(player: Player) {
        player.inventory.remove(Material.SPONGE)
    }

    fun clearWeapons() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player -> player.gameMode == GameMode.ADVENTURE }
            .forEach { player: Player -> clearWeaponry(player) }
    }

    private fun clearWeaponry(player: Player) {
        player.inventory.remove(Material.STONE_SWORD)
        player.inventory.remove(Material.BOW)
        player.inventory.remove(Material.ARROW)
        player.inventory.setItemInOffHand(null)
        for (item in PowerUpItem.values()) {
            player.inventory.remove(item.material)
        }
    }

    fun clearNonCheeseItems() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player -> player.gameMode == GameMode.ADVENTURE }
            .forEach { player: Player -> clearNonCheese(player) }
    }

    private fun clearNonCheese(player: Player) {
        player.inventory.remove(Material.STONE_SWORD)
        player.inventory.remove(Material.BOW)
        player.inventory.remove(Material.ARROW)
        player.inventory.remove(Material.WOODEN_PICKAXE)
        player.inventory.remove(Material.RED_DYE)
        player.inventory.setItemInOffHand(null)
        for (item in PowerUpItem.values()) {
            player.inventory.remove(item.material)
        }
    }

    fun clearQueueItem(player: Player) {
        player.inventory.remove(Material.RED_DYE)
        player.inventory.setItemInOffHand(null)
    }

    private fun clearAllItems() {
        for (player in Bukkit.getOnlinePlayers()) {
            player.inventory.clear()
        }
    }

    fun clearAllPlayersEffects() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player -> player.gameMode == GameMode.ADVENTURE }
            .forEach { player: Player -> clearPotionEffects(player) }
    }

    fun clearPotionEffects(player: Player) {
        for (effect in player.activePotionEffects) {
            player.removePotionEffect(effect.type)
        }
    }

    fun teleportPlayersToGame() {
        Bukkit.getOnlinePlayers()
            .groupBy(game.teams::getTeam)
            .forEach { (team, players) ->
                val spawns = when (team) {
                    Teams.RED -> game.locationManager.getRedSpawns()
                    Teams.BLUE -> game.locationManager.getBlueSpawns()
                    else -> return@forEach
                }
                players.forEachIndexed { i, player ->
                    player.teleportWithPassengers(spawns[i])
                }
            }
    }

    fun teleportSpectatorsToArena() {
        for (player in Bukkit.getOnlinePlayers()) {
            if (game.teams.getTeam(player) == null) {
                player.teleportWithPassengers(game.locationManager.getArenaCentre())
            }
        }
    }

    private fun teleportPlayersToSpawn() {
        for (player in Bukkit.getOnlinePlayers()) {
            player.teleportWithPassengers(game.locationManager.getSpawn())
        }
    }

    fun setSpectatorsGameMode() {
        Bukkit.getOnlinePlayers().forEach {
            if (game.teams.getTeam(it) == null) {
                it.gameMode = GameMode.SPECTATOR
            }
        }
    }

    fun setPlayersAdventure() {
        Bukkit.getOnlinePlayers().forEach {
            if (game.teams.getTeam(it) != null) {
                it.gameMode = GameMode.ADVENTURE
            }
        }
    }

    private fun setAllAdventure() {
        for (player in Bukkit.getOnlinePlayers()) {
            player.gameMode = GameMode.ADVENTURE
        }
    }

    fun resetPlayers() {
        clearAllItems()
        setPlayersNotFlying()
        setAllAdventure()
        teleportPlayersToSpawn()
    }
}
