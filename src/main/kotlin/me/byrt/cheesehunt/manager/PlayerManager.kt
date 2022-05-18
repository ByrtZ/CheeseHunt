package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.Main

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class PlayerManager(private var game : Game) {
    private val redRoundOneSpawn = Location(Main.getPlugin().server.getWorld("Cheese"), 17.0, -43.0, 38.5, 0.0f, 0.0f)
    private val blueRoundOneSpawn = Location(Main.getPlugin().server.getWorld("Cheese"), -63.0, -43.0, 38.5, 0.0f, 0.0f)
    private val redRoundTwoSpawn = Location(Main.getPlugin().server.getWorld("Cheese"), -63.0, -52.0, 77.5, 180.0f, 0.0f)
    private val blueRoundTwoSpawn = Location(Main.getPlugin().server.getWorld("Cheese"), 17.0, -52.0, 77.5, 180.0f, 0.0f)

    fun setPlayersNotFlying() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player -> player.allowFlight }.forEach {
                player: Player -> disableFlightPlayers(player)
        }
    }

    private fun disableFlightPlayers(player: Player) {
        if (player.gameMode == GameMode.ADVENTURE || player.gameMode == GameMode.SURVIVAL) {
            player.allowFlight = false
            player.isFlying = false
        }
    }

    fun giveItemsToPlayers() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player -> player.gameMode == GameMode.SURVIVAL }
            .forEach { player: Player -> giveItems(player) }
    }

    private fun giveItems(player: Player) {
        when(game.getRoundState()) {
            RoundState.ROUND_ONE -> {
                player.inventory.addItem(ItemStack(Material.SPONGE, 4))
            }
            RoundState.ROUND_TWO -> {
                player.inventory.addItem(ItemStack(Material.IRON_HOE, 1))
            }
        }
    }

    fun clearCheese() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player -> player.gameMode == GameMode.ADVENTURE }
            .forEach { player: Player -> clearSpongeItems(player) }
    }

    private fun clearSpongeItems(player : Player) {
        player.inventory.remove(Material.SPONGE)
    }

    fun clearAllItems() {
        for(player in Bukkit.getOnlinePlayers()) {
            player.inventory.clear()
        }
    }

    fun teleportAllPlayers() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player? -> player?.let {
            Main.getGame()?.getTeamManager()?.getPlayerTeam(it.uniqueId) } != Team.SPECTATOR}
                .forEach{ player: Player -> teleportPlayers(player) }
    }

    private fun teleportPlayers(player : Player) {
        if(Main.getGame()?.getTeamManager()?.isInRedTeam(player.uniqueId) == true) {
            if(Main.getGame()?.getRoundState() == RoundState.ROUND_ONE) {
                player.teleport(redRoundOneSpawn)
            } else if(Main.getGame()?.getRoundState() == RoundState.ROUND_TWO) {
                player.teleport(redRoundTwoSpawn)
            } else {
                Main.getPlugin().logger.info("[TELEPORTING ERROR] Something weird happened when trying to teleport players")
            }
        } else if(Main.getGame()?.getTeamManager()?.isInBlueTeam(player.uniqueId) == true) {
            if(Main.getGame()?.getRoundState() == RoundState.ROUND_ONE) {
                player.teleport(blueRoundOneSpawn)
            } else if(Main.getGame()?.getRoundState() == RoundState.ROUND_TWO) {
                player.teleport(blueRoundTwoSpawn)
            } else {
                Main.getPlugin().logger.info("[TELEPORTING ERROR] Something weird happened when trying to teleport players")
            }
        } else {
            Main.getPlugin().logger.info("[TELEPORTING ERROR] Something weird happened when trying to teleport players")
        }
    }
}