package me.byrt.cheesehunt.manager

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.Bukkit

import java.util.*

@Suppress("unused")
class StatisticsManager(private val game : Game) {
    private var playerCheesePickedUp = mutableMapOf<UUID, Int>()
    private var playerCheeseDropped = mutableMapOf<UUID, Int>()
    private var playerEliminations = mutableMapOf<UUID, Int>()
    private var playerDeaths = mutableMapOf<UUID, Int>()

    fun incrementStat(uuid : UUID, stat : Statistic) {
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
        }
    }

    private fun getUnsortedMap(stat : Statistic) : MutableMap<UUID, Int> {
        return when(stat) {
            Statistic.CHEESE_PICKED_UP -> {
                playerCheesePickedUp
            }
            Statistic.CHEESE_DROPPED -> {
                playerCheeseDropped
            }
            Statistic.ELIMINATIONS -> {
                playerEliminations
            }
            Statistic.DEATHS -> {
                playerDeaths
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
        }
    }

    fun statsBreakdown(stat : Statistic) {
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
                                .append(Component.text(" eliminated players $eliminations times.", NamedTextColor.WHITE))))
                        }
                        if(game.teamManager.isInBlueTeam(uuid)) {
                            player.sendMessage(Component.text("$i. ")
                                .append(Component.text("${Bukkit.getPlayer(uuid)?.player?.name}", NamedTextColor.BLUE)
                                .append(Component.text(" eliminated players $eliminations times.", NamedTextColor.WHITE))))
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
                                .append(Component.text(" died $deaths times.", NamedTextColor.WHITE))))
                        }
                        if(game.teamManager.isInBlueTeam(uuid)) {
                            player.sendMessage(Component.text("$i. ")
                                .append(Component.text("${Bukkit.getPlayer(uuid)?.player?.name}", NamedTextColor.BLUE)
                                .append(Component.text(" died $deaths times.", NamedTextColor.WHITE))))
                        }
                    }
                    i++
                }
            }
        }
    }

    fun resetStats() {
        playerCheesePickedUp.clear()
        playerCheeseDropped.clear()
        playerEliminations.clear()
        playerDeaths.clear()
    }
}

enum class Statistic {
    CHEESE_PICKED_UP,
    CHEESE_DROPPED,
    ELIMINATIONS,
    DEATHS
}