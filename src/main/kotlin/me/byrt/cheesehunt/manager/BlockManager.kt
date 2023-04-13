package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.state.RoundState

import org.bukkit.Bukkit.getWorld
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Directional

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

    private fun resetBarriers() {
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

    fun removeBlastDoors() {
        for(x in 990..991) {
            for(y in 0..2) {
                for(z in 999..1001) {
                    getWorld("Cheese")?.getBlockAt(x, y, z)?.type = Material.AIR
                }
            }
        }

        for(x in 1010 downTo 1009) {
            for(y in 0..2) {
                for(z in 999..1001) {
                    getWorld("Cheese")?.getBlockAt(x, y, z)?.type = Material.AIR
                }
            }
        }
    }

    private fun resetBlastDoors() {
        getWorld("Cheese")?.getBlockAt(990, 0, 1000)?.type = Material.TRIPWIRE_HOOK
        val tripwireHook1 = getWorld("Cheese")?.getBlockAt(990, 0, 1000)
        val tripwireHookData1 = getWorld("Cheese")?.getBlockAt(990, 0, 1000)?.blockData
        if(tripwireHookData1 is Directional) {
            tripwireHookData1.facing = BlockFace.WEST
            tripwireHook1?.blockData = tripwireHookData1
        }
        getWorld("Cheese")?.getBlockAt(1010, 0, 1000)?.type = Material.TRIPWIRE_HOOK
        val tripwireHook2 = getWorld("Cheese")?.getBlockAt(1010, 0, 1000)
        val tripwireHookData2 = getWorld("Cheese")?.getBlockAt(1010, 0, 1000)?.blockData
        if(tripwireHookData2 is Directional) {
            tripwireHookData2.facing = BlockFace.EAST
            tripwireHook2?.blockData = tripwireHookData2
        }
        for(y in 0..2) {
            for(z in 999..1001) {
                getWorld("Cheese")?.getBlockAt(991, y, z)?.type = Material.NETHERITE_BLOCK
            }
        }
        for(y in 0..2) {
            for(z in 999..1001) {
                getWorld("Cheese")?.getBlockAt(1009, y, z)?.type = Material.NETHERITE_BLOCK
            }
        }
    }

    fun placeFullCheeseSquare() {
        for(x in 999..1001) {
            for(z in 999..1001) {
                getWorld("Cheese")?.getBlockAt(x, 0, z)?.type = Material.SPONGE
            }
        }
    }

    fun resetAllBlocks() {
        resetBarriers()
        resetBlastDoors()
    }
}