package dev.byrt.cheesehunt.command

import dev.byrt.cheesehunt.CheeseHunt
import dev.byrt.cheesehunt.game.GameState
import dev.byrt.cheesehunt.state.Teams
import dev.byrt.cheesehunt.util.DevStatus
import dev.byrt.cheesehunt.manager.PowerUpItem
import dev.byrt.cheesehunt.manager.ItemRarity
import dev.byrt.cheesehunt.manager.ItemType

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

@Suppress("unused")
class DebugCommands : BaseCommand {
    @CommandMethod("debug next_phase")
    @CommandDescription("Debug command to set game to its next phase.")
    @CommandPermission("cheesehunt.debug")
    fun debugNextPhase(sender : Player) {
        CheeseHunt.getGame().dev.parseDevMessage("Game pushed to next phase by ${sender.name}.", DevStatus.WARNING)
        CheeseHunt.getGame().gameManager.nextState()
    }

    @CommandMethod("debug force_state <state>")
    @CommandDescription("Debug command to force set game state.")
    @CommandPermission("cheesehunt.debug")
    fun debugForceState(sender : Player, @Argument("state") state : GameState) {
        CheeseHunt.getGame().dev.parseDevMessage("Game state updated to $state by ${sender.name}.", DevStatus.SEVERE)
        CheeseHunt.getGame().gameManager.forceState(state)
    }

    @CommandMethod("debug stop_respawn <player>")
    @CommandDescription("Debug command to force stop respawn loops.")
    @CommandPermission("cheesehunt.debug")
    fun debugStopRespawn(sender : Player, @Argument("player") player : Player) {
        CheeseHunt.getGame().dev.parseDevMessage("${player.name}'s respawn loop force stopped by ${sender.name}.", DevStatus.SEVERE)
        CheeseHunt.getGame().respawnTask.stopRespawnLoop(player)
    }

    @CommandMethod("debug win_show <team>")
    @CommandDescription("Debug command to test the game win show.")
    @CommandPermission("cheesehunt.debug")
    fun debugWinShow(sender : Player, @Argument("team") team : Teams) {
        CheeseHunt.getGame().dev.parseDevMessage("Win show ran for $team team by ${sender.name}.", DevStatus.INFO)
        CheeseHunt.getGame().winShowTask.stopWinShowLoop()
        CheeseHunt.getGame().winShowTask.startWinShowLoop(CheeseHunt.getPlugin(), team)
    }

    @CommandMethod("debug cheese give <player>")
    @CommandDescription("Debug command to give players cheese.")
    @CommandPermission("cheesehunt.debug")
    fun debugGiveCheese(sender : Player, @Argument("player") player: Player) {
        CheeseHunt.getGame().dev.parseDevMessage("${sender.name} gave ${player.name} cheese.", DevStatus.WARNING)
        CheeseHunt.getGame().cheeseManager.playerPickupCheese(player, Location(player.world, 1000.0, 319.0, 1000.0))
    }

    @CommandMethod("debug cheese remove <player>")
    @CommandDescription("Debug command to remove player's cheese.")
    @CommandPermission("cheesehunt.debug")
    fun debugRemoveCheese(sender : Player, @Argument("player") player: Player) {
        CheeseHunt.getGame().dev.parseDevMessage("${sender.name} removed ${player.name}'s cheese.", DevStatus.WARNING)
        CheeseHunt.getGame().cheeseManager.playerDropCheese(player)
    }

    @CommandMethod("debug cheese place square")
    @CommandDescription("Debug command to place a cheese square.")
    @CommandPermission("cheesehunt.debug")
    fun debugPlaceCheeseDrop(sender : Player) {
        CheeseHunt.getGame().dev.parseDevMessage("${sender.name} placed a cheese drop.", DevStatus.WARNING)
        CheeseHunt.getGame().blockManager.placeCheeseSquare()
    }

    @CommandMethod("debug cheese place cube")
    @CommandDescription("Debug command to place a cheese cube.")
    @CommandPermission("cheesehunt.debug")
    fun debugPlaceCheeseCube(sender : Player) {
        CheeseHunt.getGame().dev.parseDevMessage("${sender.name} placed a cheese cube.", DevStatus.WARNING)
        CheeseHunt.getGame().blockManager.placeCheeseCube()
    }


    @CommandMethod("debug skull <player>")
    @CommandDescription("Debug command to test Noxesium's player heads in UI.")
    @CommandPermission("cheesehunt.debug")
    fun debugTestSkulls(sender : Player, @Argument("player") player: Player) {
        sender.sendMessage(Component.text("\uD001 ", NamedTextColor.WHITE).append(Component.text("This is ${player.name}'s head:", NamedTextColor.YELLOW).append(Component.score("%NCPH%${player.uniqueId},false,0,0,1.0", "").color(NamedTextColor.WHITE)).append(Component.text("!", NamedTextColor.YELLOW))))
    }

    @CommandMethod("debug spawn_item <item>")
    @CommandDescription("Debug command to test item spawns.")
    @CommandPermission("cheesehunt.debug")
    fun debugTestSkulls(sender : Player, @Argument("item") powerUpItem: PowerUpItem) {
        CheeseHunt.getGame().dev.parseDevMessage("$powerUpItem items spawned on map sides by ${sender.name}.", DevStatus.WARNING)
        CheeseHunt.getGame().itemManager.spawnSideItems(powerUpItem)
    }

    @CommandMethod("queue data")
    @CommandDescription("Debug command to get queue data.")
    @CommandPermission("cheesehunt.debug")
    fun debugQueueData(sender : Player) {
        sender.sendMessage(
            Component.text("Queue Data\n", NamedTextColor.GOLD).append(
                Component.text("Queue", NamedTextColor.YELLOW).append(
                    Component.text(": ${CheeseHunt.getGame().queue.getQueue()}\n", NamedTextColor.WHITE).append(
                        Component.text("State", NamedTextColor.RED).append(
                            Component.text(": ${CheeseHunt.getGame().queue.getQueueState()}", NamedTextColor.WHITE)
                        )
                    )
                )
            )
        )
    }

    @CommandMethod("queue join")
    @CommandDescription("Debug command for queues.")
    @CommandPermission("cheesehunt.debug")
    fun debugJoinQueue(sender : Player) {
        CheeseHunt.getGame().queue.joinQueue(sender)
    }

    @CommandMethod("queue leave")
    @CommandDescription("Debug command for queues.")
    @CommandPermission("cheesehunt.debug")
    fun debugLeaveQueue(sender : Player) {
        CheeseHunt.getGame().queue.leaveQueue(sender)
    }

    @CommandMethod("queue force join <player>")
    @CommandDescription("Debug command for queues.")
    @CommandPermission("cheesehunt.debug")
    fun debugForceJoinQueue(sender : Player, @Argument("player") player : Player) {
        CheeseHunt.getGame().dev.parseDevMessage("${sender.name} pushed ${player.name} into the Queue.", DevStatus.INFO)
        CheeseHunt.getGame().queue.joinQueue(player)
    }

    @CommandMethod("queue force leave <player>")
    @CommandDescription("Debug command for queues.")
    @CommandPermission("cheesehunt.debug")
    fun debugForceLeaveQueue(sender : Player, @Argument("player") player : Player) {
        CheeseHunt.getGame().dev.parseDevMessage("${sender.name} threw ${player.name} out of the Queue.", DevStatus.INFO)
        CheeseHunt.getGame().queue.leaveQueue(player)
    }

    @CommandMethod("debug test interfaces")
    @CommandDescription("Debug command for interface testing.")
    @CommandPermission("cheesehunt.debug")
    fun debugTestInterface(sender : Player) {
        CheeseHunt.getGame().interfaceManager.testInterface(sender)
    }

    @CommandMethod("debug test rarities")
    @CommandDescription("Debug command for item rarity testing.")
    @CommandPermission("cheesehunt.debug")
    fun debugTestRarities(sender : Player) {
        for(rarity in ItemRarity.values()) {
            val rarityTestItem = ItemStack(Material.STICK, 1)
            val rarityTestItemMeta = rarityTestItem.itemMeta
            rarityTestItemMeta.displayName(Component.text("Test Item").color(TextColor.fromHexString(rarity.rarityColour)).decoration(
                TextDecoration.ITALIC, false))
            val rarityTestItemLore = listOf(
                Component.text(rarity.rarityGlyph, NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
                Component.text("Debug item.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
            )
            rarityTestItemMeta.lore(rarityTestItemLore)
            rarityTestItem.itemMeta = rarityTestItemMeta
            sender.inventory.addItem(ItemStack(rarityTestItem))
        }
    }

    @CommandMethod("debug test types")
    @CommandDescription("Debug command for item type testing.")
    @CommandPermission("cheesehunt.debug")
    fun debugTestTypes(sender : Player) {
        for(type in ItemType.values()) {
            val typeTestItem = ItemStack(Material.STICK, 1)
            val typeTestItemMeta = typeTestItem.itemMeta
            typeTestItemMeta.displayName(Component.text("Test Item").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false))
            val typeTestItemLore = listOf(
                Component.text(type.typeGlyph, NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
                Component.text("Debug item.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
            )
            typeTestItemMeta.lore(typeTestItemLore)
            typeTestItem.itemMeta = typeTestItemMeta
            sender.inventory.addItem(ItemStack(typeTestItem))
        }
    }
}
