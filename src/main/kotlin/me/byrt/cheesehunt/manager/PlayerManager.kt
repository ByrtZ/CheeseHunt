package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.state.RoundState
import me.byrt.cheesehunt.state.Teams

import org.bukkit.*
import org.bukkit.entity.Arrow
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

    fun giveItemsToPlayers() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player -> player.gameMode == GameMode.ADVENTURE }
            .forEach { player: Player -> giveItems(player) }
    }

    private fun giveItems(player: Player) {
        when(game.getRoundState()) {
            RoundState.ROUND_ONE -> {
                if(game.getTeamManager().getPlayerTeam(player.uniqueId) != Teams.SPECTATOR) {
                    game.getItemManager().givePlayerKit(player)
                }
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

    fun teleportPlayersToGame() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player? -> player?.let {
            Main.getGame().getTeamManager().getPlayerTeam(it.uniqueId) } != Teams.SPECTATOR}
                .forEach{ player: Player -> teleportPlayers(player) }
    }

    private fun teleportPlayers(player : Player) {
        if(Main.getGame().getTeamManager().isInRedTeam(player.uniqueId)) {
            if(Main.getGame().getRoundState() == RoundState.ROUND_ONE) {
                for(redPlayerUUID in Main.getGame().getTeamManager().getRedTeam()) {
                    val redPlayer = Bukkit.getPlayer(redPlayerUUID)
                    redPlayer!!.teleport(Main.getGame().getLocationManager().getRedSpawns()[Main.getGame().getLocationManager().getRedSpawnCounter()])
                    Main.getGame().getLocationManager().incrementSpawnCounter(Teams.RED)
                }
            } else {
                Main.getPlugin().logger.info("[TELEPORTING ERROR] Something weird happened when trying to teleport players")
            }
        } else if(Main.getGame().getTeamManager().isInBlueTeam(player.uniqueId)) {
            if(Main.getGame().getRoundState() == RoundState.ROUND_ONE) {
                for(bluePlayerUUID in Main.getGame().getTeamManager().getBlueTeam()) {
                    val bluePlayer = Bukkit.getPlayer(bluePlayerUUID)
                    bluePlayer!!.teleport(Main.getGame().getLocationManager().getBlueSpawns()[Main.getGame().getLocationManager().getBlueSpawnCounter()])
                    Main.getGame().getLocationManager().incrementSpawnCounter(Teams.BLUE)
                }
            } else {
                Main.getPlugin().logger.info("[TELEPORTING ERROR] Something weird happened when trying to teleport players")
            }
        } else {
            Main.getPlugin().logger.info("[TELEPORTING ERROR] Something weird happened when trying to teleport players")
        }
    }

    fun teleportSpectatorsToArena() {
        for(player in Bukkit.getOnlinePlayers()) {
            if(game.getTeamManager().getPlayerTeam(player.uniqueId) == Teams.SPECTATOR) {
                player.teleport(game.getLocationManager().getArenaCentre())
            }
        }
    }

    fun teleportPlayersToSpawn() {
        for(player in Bukkit.getOnlinePlayers()) {
            player.teleport(Main.getGame().getLocationManager().getSpawn())
        }
    }

    fun setSpectatorsGameMode() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player? -> player?.let {
            Main.getGame().getTeamManager().getPlayerTeam(it.uniqueId) } == Teams.SPECTATOR}
            .forEach{ player: Player -> player.gameMode = GameMode.SPECTATOR }
    }

    fun setPlayersAdventure() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player? -> player?.let {
            Main.getGame().getTeamManager().getPlayerTeam(it.uniqueId) } != Teams.SPECTATOR}
            .forEach{ player: Player -> player.gameMode = GameMode.ADVENTURE }
    }

    fun setAllAdventure() {
        for(player in Bukkit.getOnlinePlayers()) {
            player.gameMode = GameMode.ADVENTURE
        }
    }

    fun removeAllArrows() {
        for(arrow in Bukkit.getWorld("Cheese")?.getEntitiesByClass(Arrow::class.java)!!) {
            arrow.remove()
        }
    }

    fun resetPlayers() {
        clearAllItems()
        setPlayersNotFlying()
        setAllAdventure()
        teleportPlayersToSpawn()
    }
}