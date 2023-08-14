package dev.byrt.cheesehunt.manager

import dev.byrt.cheesehunt.game.Game
import dev.byrt.cheesehunt.state.Sounds

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.Bukkit
import org.bukkit.entity.Player

import java.util.*

class StatisticsManager(private val game : Game) {
    private var playerCheesePickedUp = mutableMapOf<UUID, Int>()
    private var playerCheeseDropped = mutableMapOf<UUID, Int>()
    private var playerEliminations = mutableMapOf<UUID, Int>()
    private var playerDeaths = mutableMapOf<UUID, Int>()
    private var playerKillStreaks = mutableMapOf<UUID, Int>()

    fun updateStatistic(uuid : UUID, stat : Statistic) {
        when(stat) {
            Statistic.CHEESE_PICKED_UP -> {
                playerCheesePickedUp.putIfAbsent(uuid, 0)
                playerCheesePickedUp[uuid] = (playerCheesePickedUp[uuid]?.plus(1)) as Int
            }
            Statistic.CHEESE_DROPPED -> {
                playerCheeseDropped.putIfAbsent(uuid, 0)
                playerCheeseDropped[uuid] = (playerCheeseDropped[uuid]?.plus(1)) as Int
            }
            Statistic.ELIMINATIONS -> {
                playerEliminations.putIfAbsent(uuid, 0)
                playerEliminations[uuid] = (playerEliminations[uuid]?.plus(1)) as Int
            }
            Statistic.DEATHS -> {
                playerDeaths.putIfAbsent(uuid, 0)
                playerDeaths[uuid] = (playerDeaths[uuid]?.plus(1)) as Int
            }
            Statistic.KILL_STREAKS -> {
                playerKillStreaks.putIfAbsent(uuid, 0)

                if(game.respawnTask.getRespawnLoopMap().containsKey(uuid)) {
                    playerKillStreaks[uuid] = 0
                } else {
                    playerKillStreaks[uuid] = (playerKillStreaks[uuid]?.plus(1)) as Int
                    killStreak(Bukkit.getPlayer(uuid)!!, uuid)
                }
            }
        }
    }

    private fun killStreak(player : Player, uuid : UUID) {
        if(playerKillStreaks[uuid]!! in 2..4) {
            for(allPlayers in Bukkit.getOnlinePlayers()) {
                if(allPlayers == player) {
                    player.sendMessage(Component.text("[")
                        .append(Component.text("▶").color(NamedTextColor.YELLOW))
                        .append(Component.text("] "))
                        .append(Component.text("You are on a ${playerKillStreaks[uuid]} kill streak!", TextColor.fromHexString("#ed4440"))))
                    player.playSound(player.location, Sounds.Score.KILL_STREAK, 0.5f, 1.0f + playerKillStreaks[uuid]!! * 0.1f)
                } else {
                    allPlayers.sendMessage(Component.text("[")
                        .append(Component.text("▶").color(NamedTextColor.YELLOW))
                        .append(Component.text("] "))
                        .append(Component.text(player.name, game.teamManager.getTeamNamedTextColor(player)))
                        .append(Component.text(" is on a ${playerKillStreaks[uuid]} kill streak!", TextColor.fromHexString("#ed4440"))))
                }
            }
        } else {
            if(playerKillStreaks[uuid]!! >= 5) {
                for(allPlayers in Bukkit.getOnlinePlayers()) {
                    if(allPlayers == player) {
                        player.sendMessage(Component.text("[")
                            .append(Component.text("▶").color(NamedTextColor.YELLOW))
                            .append(Component.text("] "))
                            .append(Component.text("You are on a", TextColor.fromHexString("#ed4440")))
                            .append(Component.text(" RAMPAGE!", TextColor.fromHexString("#cc1100"), TextDecoration.BOLD)))
                        player.playSound(player.location, Sounds.Score.KILL_STREAK, 0.5f, 1.5f)
                        player.playSound(player.location, Sounds.Score.KILL_STREAK_RAMPAGE, 0.85f, 2f)
                    } else {
                        allPlayers.sendMessage(Component.text("[")
                            .append(Component.text("▶").color(NamedTextColor.YELLOW))
                            .append(Component.text("] "))
                            .append(Component.text(player.name, game.teamManager.getTeamNamedTextColor(player)))
                            .append(Component.text(" is on a", TextColor.fromHexString("#ed4440")))
                            .append(Component.text(" RAMPAGE!", TextColor.fromHexString("#cc1100"), TextDecoration.BOLD)))
                    }
                }
            }
        }
    }

    private fun getSortedMap(stat : Statistic): Map<UUID, Int> {
        return when(stat) {
            Statistic.CHEESE_PICKED_UP -> {
                playerCheesePickedUp.toList().sortedBy { (_, int) -> int }.reversed().toMap()
            }
            Statistic.CHEESE_DROPPED -> {
                playerCheeseDropped.toList().sortedBy { (_, int) -> int }.toMap()
            }
            Statistic.ELIMINATIONS -> {
                playerEliminations.toList().sortedBy { (_, int) -> int }.reversed().toMap()
            }
            Statistic.DEATHS -> {
                playerDeaths.toList().sortedBy { (_, int) -> int }.toMap()
            }
            Statistic.KILL_STREAKS -> {
                playerKillStreaks.toList().sortedBy { (_, int) -> int }.reversed().toMap()
            }
        }
    }

    fun statisticBreakdown(stat : Statistic) {
        val sortedMap = getSortedMap(stat)
        var i = 1
        when(stat) {
            Statistic.CHEESE_PICKED_UP -> {
                sortedMap.forEach { (uuid, cheesePickedUp) ->
                    for(player in Bukkit.getOnlinePlayers()) {
                        if(game.teamManager.isInRedTeam(uuid)) {
                            player.sendMessage(Component.text("$i. ")
                                .append(Component.text("${Bukkit.getPlayer(uuid)?.player?.name}", NamedTextColor.RED)
                                .append(Component.text(" collected $cheesePickedUp cheese.", NamedTextColor.WHITE))))
                        }
                        if(game.teamManager.isInBlueTeam(uuid)) {
                            player.sendMessage(Component.text("$i. ")
                                 .append(Component.text("${Bukkit.getPlayer(uuid)?.player?.name}", NamedTextColor.BLUE)
                                 .append(Component.text(" collected $cheesePickedUp cheese.", NamedTextColor.WHITE))))
                        }
                    }
                    i++
                }
            }
            Statistic.CHEESE_DROPPED -> {
                sortedMap.forEach { (uuid, cheeseDropped) ->
                    for(player in Bukkit.getOnlinePlayers()) {
                        if(game.teamManager.isInRedTeam(uuid)) {
                            player.sendMessage(Component.text("$i. ")
                                 .append(Component.text("${Bukkit.getPlayer(uuid)?.player?.name}", NamedTextColor.RED)
                                 .append(Component.text(" dropped $cheeseDropped cheese.", NamedTextColor.WHITE))))
                        }
                        if(game.teamManager.isInBlueTeam(uuid)) {
                            player.sendMessage(Component.text("$i. ")
                                 .append(Component.text("${Bukkit.getPlayer(uuid)?.player?.name}", NamedTextColor.BLUE)
                                 .append(Component.text(" dropped $cheeseDropped cheese.", NamedTextColor.WHITE))))
                        }
                    }
                    i++
                }
            }
            Statistic.ELIMINATIONS -> {
                sortedMap.forEach { (uuid, eliminations) ->
                    for(player in Bukkit.getOnlinePlayers()) {
                        if(game.teamManager.isInRedTeam(uuid)) {
                            player.sendMessage(Component.text("$i. ")
                                .append(Component.text("${Bukkit.getPlayer(uuid)?.player?.name}", NamedTextColor.RED)
                                .append(Component.text(" eliminated players $eliminations time${if(eliminations > 1) "s" else ""}.", NamedTextColor.WHITE))))
                        }
                        if(game.teamManager.isInBlueTeam(uuid)) {
                            player.sendMessage(Component.text("$i. ")
                                .append(Component.text("${Bukkit.getPlayer(uuid)?.player?.name}", NamedTextColor.BLUE)
                                .append(Component.text(" eliminated players $eliminations time${if(eliminations > 1) "s" else ""}.", NamedTextColor.WHITE))))
                        }
                    }
                    i++
                }
            }
            Statistic.DEATHS -> {
                sortedMap.forEach { (uuid, deaths) ->
                    for(player in Bukkit.getOnlinePlayers()) {
                        if(game.teamManager.isInRedTeam(uuid)) {
                            player.sendMessage(Component.text("$i. ")
                                .append(Component.text("${Bukkit.getPlayer(uuid)?.player?.name}", NamedTextColor.RED)
                                .append(Component.text(" died $deaths time${if(deaths > 1) "s" else ""}.", NamedTextColor.WHITE))))
                        }
                        if(game.teamManager.isInBlueTeam(uuid)) {
                            player.sendMessage(Component.text("$i. ")
                                .append(Component.text("${Bukkit.getPlayer(uuid)?.player?.name}", NamedTextColor.BLUE)
                                .append(Component.text(" died $deaths time${if(deaths > 1) "s" else ""}.", NamedTextColor.WHITE))))
                        }
                    }
                    i++
                }
            }
            Statistic.KILL_STREAKS -> {
                // Unfinished
            }
        }
    }

    fun resetStatistics() {
        playerCheesePickedUp.clear()
        playerCheeseDropped.clear()
        playerEliminations.clear()
        playerDeaths.clear()
        playerKillStreaks.clear()
    }
}

enum class Statistic {
    CHEESE_PICKED_UP,
    CHEESE_DROPPED,
    ELIMINATIONS,
    DEATHS,
    KILL_STREAKS
}