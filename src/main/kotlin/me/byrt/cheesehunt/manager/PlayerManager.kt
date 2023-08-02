package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.game.Game
import me.byrt.cheesehunt.state.Teams

import org.bukkit.*
import org.bukkit.entity.Player

class PlayerManager(private var game : Game) {
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

    fun setPlayersFlying() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player -> !player.allowFlight }.forEach {
                player: Player -> enableFlightPlayers(player)
        }
    }

    private fun enableFlightPlayers(player: Player) {
        if (player.gameMode == GameMode.ADVENTURE || player.gameMode == GameMode.SURVIVAL) {
            player.allowFlight = true
            player.isFlying = true
        }
    }

    fun giveItemsToPlayers() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player -> player.gameMode == GameMode.ADVENTURE }
            .forEach { player: Player -> giveItems(player) }
    }

    private fun giveItems(player: Player) {
        if(game.teamManager.getPlayerTeam(player.uniqueId) != Teams.SPECTATOR) {
            game.itemManager.givePlayerKit(player)
        }
    }

    fun clearCheese() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player -> player.gameMode == GameMode.ADVENTURE }
            .forEach { player: Player -> clearSpongeItems(player) }
    }

    private fun clearSpongeItems(player : Player) {
        player.inventory.remove(Material.SPONGE)
    }

    fun clearWeapons() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player -> player.gameMode == GameMode.ADVENTURE }
            .forEach { player: Player -> clearWeaponry(player) }
    }

    private fun clearWeaponry(player : Player) {
        player.inventory.remove(Material.STONE_SWORD)
        player.inventory.remove(Material.BOW)
        player.inventory.remove(Material.ARROW)
        player.inventory.setItemInOffHand(null)
        for(item in PowerUpItem.values()) {
            player.inventory.remove(item.material)
        }
    }

    fun clearNonCheeseItems() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player -> player.gameMode == GameMode.ADVENTURE }
            .forEach { player: Player -> clearNonCheese(player) }
    }

    private fun clearNonCheese(player : Player) {
        player.inventory.remove(Material.STONE_SWORD)
        player.inventory.remove(Material.BOW)
        player.inventory.remove(Material.ARROW)
        player.inventory.remove(Material.WOODEN_PICKAXE)
        player.inventory.setItemInOffHand(null)
        for(item in PowerUpItem.values()) {
            player.inventory.remove(item.material)
        }
    }

    private fun clearAllItems() {
        for(player in Bukkit.getOnlinePlayers()) {
            player.inventory.clear()
        }
    }

    fun clearAllPlayersEffects() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player -> player.gameMode == GameMode.ADVENTURE }
            .forEach { player: Player -> clearPotionEffects(player) }
    }

    fun clearPotionEffects(player : Player) {
        for(effect in player.activePotionEffects) {
            player.removePotionEffect(effect.type)
        }
    }

    fun teleportPlayersToGame() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player? -> player?.let {
            Main.getGame().teamManager.getPlayerTeam(it.uniqueId) } != Teams.SPECTATOR}
                .forEach{ player: Player -> teleportPlayers(player) }
    }

    private fun teleportPlayers(player : Player) {
        if(Main.getGame().teamManager.isInRedTeam(player.uniqueId)) {
                for(redPlayerUUID in Main.getGame().teamManager.getRedTeam()) {
                    val redPlayer = Bukkit.getPlayer(redPlayerUUID)
                    redPlayer!!.teleport(Main.getGame().locationManager.getRedSpawns()[Main.getGame().locationManager.getRedSpawnCounter()])
                    Main.getGame().locationManager.incrementSpawnCounter(Teams.RED)
                }
        }
        if(Main.getGame().teamManager.isInBlueTeam(player.uniqueId)) {
            for(bluePlayerUUID in Main.getGame().teamManager.getBlueTeam()) {
                val bluePlayer = Bukkit.getPlayer(bluePlayerUUID)
                bluePlayer!!.teleport(Main.getGame().locationManager.getBlueSpawns()[Main.getGame().locationManager.getBlueSpawnCounter()])
                Main.getGame().locationManager.incrementSpawnCounter(Teams.BLUE)
            }
        }
    }

    fun teleportSpectatorsToArena() {
        for(player in Bukkit.getOnlinePlayers()) {
            if(game.teamManager.getPlayerTeam(player.uniqueId) == Teams.SPECTATOR) {
                player.teleport(game.locationManager.getArenaCentre())
            }
        }
    }

    private fun teleportPlayersToSpawn() {
        for(player in Bukkit.getOnlinePlayers()) {
            player.teleport(Main.getGame().locationManager.getSpawn())
        }
    }

    fun setSpectatorsGameMode() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player? -> player?.let {
            Main.getGame().teamManager.getPlayerTeam(it.uniqueId) } == Teams.SPECTATOR}
            .forEach{ player: Player -> player.gameMode = GameMode.SPECTATOR }
    }

    fun setPlayersAdventure() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player? -> player?.let {
            Main.getGame().teamManager.getPlayerTeam(it.uniqueId) } != Teams.SPECTATOR}
            .forEach{ player: Player -> player.gameMode = GameMode.ADVENTURE }
    }

    private fun setAllAdventure() {
        for(player in Bukkit.getOnlinePlayers()) {
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