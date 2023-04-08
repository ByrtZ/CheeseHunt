package me.byrt.cheesehunt.manager

import org.bukkit.Bukkit.getWorld
import org.bukkit.Material

class BlockManager(private val game : Game) {
    fun removeBarriers() {
        when(game.getRoundState()) {
            RoundState.ROUND_ONE -> {
                for (z in 999..1001) { // Removing red barrier
                    for (y in 3..5) {
                        getWorld("Cheese")?.getBlockAt(950, y, z)?.type = Material.AIR
                    }
                }
                for (z in 999..1001) { // Removing blue barrier
                    for (y in 3..5) {
                        getWorld("Cheese")?.getBlockAt(1050, y, z)?.type = Material.AIR
                    }
                }
            }
        }
    }

    fun resetBarriers() {
        for (z in 999..1001) { // Resetting red barrier
            for (y in 3..5) {
                getWorld("Cheese")?.getBlockAt(950, y, z)?.type = Material.BARRIER
            }
        }
        for (z in 999..1001) { // Resetting blue barrier
            for (y in 3..5) {
                getWorld("Cheese")?.getBlockAt(1050, y, z)?.type = Material.BARRIER
            }
        }
    }
}