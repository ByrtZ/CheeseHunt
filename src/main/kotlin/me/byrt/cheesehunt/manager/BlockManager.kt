package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.state.Sounds
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Bukkit.getWorld
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Directional

@Suppress("unused")
class BlockManager(private val game : Game) {
    fun removeBarriers() {
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
        for(player in Bukkit.getOnlinePlayers()) {
            player.playSound(player.location, Sounds.Alert.BLAST_DOORS_OPEN, 1f, 1f)
            player.sendMessage(
                Component.text("[")
                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                    .append(Component.text("] "))
                    .append(Component.text("Doors to the center are now open, 2 minutes remain.", NamedTextColor.RED, TextDecoration.BOLD)
                )
            )
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

    fun placeCheeseSquare() {
        for(x in 999..1001) {
            for(z in 999..1001) {
                getWorld("Cheese")?.getBlockAt(x, 0, z)?.type = Material.SPONGE
            }
        }

        for(player in Bukkit.getOnlinePlayers()) {
            player.playSound(player.location, Sounds.Alert.GENERAL_ALERT, 1f, 1f)
            player.sendMessage(
                Component.text("[")
                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                    .append(Component.text("] "))
                    .append(Component.text("A cheese payload has been dropped in the center.", NamedTextColor.AQUA, TextDecoration.BOLD)
                )
            )
        }
    }

    fun placeCheeseCube() {
        for(x in 999..1001) {
            for(y in 0..2) {
                for(z in 999..1001) {
                    getWorld("Cheese")?.getBlockAt(x, y, z)?.type = Material.SPONGE
                }
            }
        }

        for(player in Bukkit.getOnlinePlayers()) {
            player.playSound(player.location, Sounds.Alert.GENERAL_ALERT, 1f, 1f)
            player.sendMessage(
                Component.text("[")
                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                    .append(Component.text("] "))
                    .append(Component.text("A large cheese payload has been dropped in the center.", NamedTextColor.AQUA, TextDecoration.BOLD)
                )
            )
        }
    }

    private fun resetCheeseDrop() {
        for(x in 999..1001) {
            for(y in 0..2) {
                for(z in 999..1001) {
                    getWorld("Cheese")?.getBlockAt(x, y, z)?.type = Material.AIR
                }
            }
        }
    }

    fun resetAllBlocks() {
        resetBarriers()
        resetBlastDoors()
        resetCheeseDrop()
    }
}