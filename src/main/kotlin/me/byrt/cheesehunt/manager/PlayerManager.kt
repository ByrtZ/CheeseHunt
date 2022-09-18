package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.Main

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.*
import org.bukkit.entity.Player

import java.util.UUID

import kotlin.random.Random

class PlayerManager(private var game : Game) {
    private val redRoundOneSpawn = Location(Main.getPlugin().server.getWorld("Cheese"), 17.0, -43.0, 38.5, 0.0f, 0.0f)
    private val blueRoundOneSpawn = Location(Main.getPlugin().server.getWorld("Cheese"), -63.0, -43.0, 38.5, 0.0f, 0.0f)
    private val redRoundTwoSpawn = Location(Main.getPlugin().server.getWorld("Cheese"), -63.0, -52.0, 77.5, 180.0f, 0.0f)
    private val blueRoundTwoSpawn = Location(Main.getPlugin().server.getWorld("Cheese"), 17.0, -52.0, 77.5, 180.0f, 0.0f)
    private val spawn = Location(Main.getPlugin().server.getWorld("Cheese"), 0.5, -52.0 ,0.5, 0.0f, 0.0f)
    private var randomRedImpostor: UUID? = null
    private var randomBlueImpostor: UUID? = null

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
                if(game.getTeamManager().getPlayerTeam(player.uniqueId) != Teams.SPECTATOR) {
                    game.getItemManager().givePlayerCheese(player)
                }
            }
            RoundState.ROUND_TWO -> {
                if(game.getTeamManager().getPlayerTeam(player.uniqueId) != Teams.SPECTATOR) {
                    game.getItemManager().givePlayerHoe(player)
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
                player.teleport(redRoundOneSpawn)
            } else if(Main.getGame().getRoundState() == RoundState.ROUND_TWO) {
                player.teleport(redRoundTwoSpawn)
            } else {
                Main.getPlugin().logger.info("[TELEPORTING ERROR] Something weird happened when trying to teleport players")
            }
        } else if(Main.getGame().getTeamManager().isInBlueTeam(player.uniqueId)) {
            if(Main.getGame().getRoundState() == RoundState.ROUND_ONE) {
                player.teleport(blueRoundOneSpawn)
            } else if(Main.getGame().getRoundState() == RoundState.ROUND_TWO) {
                player.teleport(blueRoundTwoSpawn)
            } else {
                Main.getPlugin().logger.info("[TELEPORTING ERROR] Something weird happened when trying to teleport players")
            }
        } else {
            Main.getPlugin().logger.info("[TELEPORTING ERROR] Something weird happened when trying to teleport players")
        }
    }

    fun teleportPlayersToSpawn() {
        for(player in Bukkit.getOnlinePlayers()) {
            player.teleport(spawn)
        }
    }

    fun setSpectatorsGameMode() {
        Bukkit.getOnlinePlayers().stream().filter { player: Player? -> player?.let {
            Main.getGame().getTeamManager().getPlayerTeam(it.uniqueId) } == Teams.SPECTATOR}
            .forEach{ player: Player -> player.gameMode = GameMode.SPECTATOR }
    }

    fun assignRandomImpostors() {
        val randomRedIndex = Random.nextInt(Main.getGame().getTeamManager().getRedTeam().size)
        randomRedImpostor = Main.getGame().getTeamManager().getRedTeam()[randomRedIndex]
        Bukkit.getPlayer(randomRedImpostor!!)?.sendMessage(Component.text("\n⚠ You are your team's Impostor! Get ready to cause some crime.\n").color(NamedTextColor.RED))

        val randomBlueIndex = Random.nextInt(Main.getGame().getTeamManager().getBlueTeam().size)
        randomBlueImpostor = Main.getGame().getTeamManager().getBlueTeam()[randomBlueIndex]
        Bukkit.getPlayer(randomBlueImpostor!!)?.sendMessage(Component.text("\n⚠ You are your team's Impostor! Get ready to cause some crime.\n").color(NamedTextColor.RED))

        val redPlayerLoc = Bukkit.getPlayer(randomRedImpostor!!)?.location
        val bluePlayerLoc = Bukkit.getPlayer(randomBlueImpostor!!)?.location
        bluePlayerLoc?.let { Bukkit.getPlayer(randomRedImpostor!!)?.teleport(it) }
        redPlayerLoc?.let { Bukkit.getPlayer(randomBlueImpostor!!)?.teleport(it) }

        for(player in Bukkit.getOnlinePlayers()) {
            player.sendMessage(Component.text("\n⚠ One player from each team has been swapped to the other side, there are impostors among us! ⚠\n").color(NamedTextColor.RED))
            player.playSound(player.location, "entity.witch.ambient", SoundCategory.HOSTILE, 1f, 1f)
        }
    }

    fun getRedImpostor(): UUID? {
        return randomRedImpostor
    }

    fun getBlueImpostor(): UUID? {
        return randomBlueImpostor
    }
}