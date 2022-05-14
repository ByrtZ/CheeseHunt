package me.byrt.cheesehunt.manager

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class PlayerManager(private var game : Game) {
    fun setPlayersNotFlying() {
        Bukkit.getOnlinePlayers().stream().filter { obj: Player -> obj.allowFlight }.forEach {
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
        Bukkit.getOnlinePlayers().stream().filter { player: Player? -> player!!.gameMode == GameMode.SURVIVAL }
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
        Bukkit.getOnlinePlayers().stream().filter { player: Player? -> player!!.gameMode == GameMode.ADVENTURE }
            .forEach { player: Player -> clearItems(player) }
    }

    private fun clearItems(player : Player) {
        player.inventory.remove(Material.SPONGE)
    }

    fun clearAllItems() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player? -> player!!.gameMode == GameMode.ADVENTURE }
            .forEach { player: Player -> itemReset(player) }
    }

    private fun itemReset(player : Player) {
        player.inventory.clear()
    }
}