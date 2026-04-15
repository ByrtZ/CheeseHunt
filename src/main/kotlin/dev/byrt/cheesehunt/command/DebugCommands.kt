package dev.byrt.cheesehunt.command

import dev.byrt.cheesehunt.Main
import dev.byrt.cheesehunt.game.GameState
import dev.byrt.cheesehunt.state.Teams
import dev.byrt.cheesehunt.util.DevStatus
import dev.byrt.cheesehunt.manager.PowerUpItem
import dev.byrt.cheesehunt.manager.ItemRarity
import dev.byrt.cheesehunt.manager.ItemType
import io.papermc.paper.command.brigadier.CommandSourceStack

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class DebugCommands {
    @Command("debug next_phase")
    @CommandDescription("Debug command to set game to its next phase.")
    @Permission("cheesehunt.debug")
    fun debugNextPhase(css: CommandSourceStack) {
        Main.getGame().dev.parseDevMessage("Game pushed to next phase by ${css.sender.name}.", DevStatus.WARNING)
        Main.getGame().gameManager.nextState()
    }

    @Command("debug force_state <state>")
    @CommandDescription("Debug command to force set game state.")
    @Permission("cheesehunt.debug")
    fun debugForceState(css: CommandSourceStack, @Argument("state") state : GameState) {
        Main.getGame().dev.parseDevMessage("Game state updated to $state by ${css.sender.name}.", DevStatus.SEVERE)
        Main.getGame().gameManager.forceState(state)
    }

    @Command("debug stop_respawn <player>")
    @CommandDescription("Debug command to force stop respawn loops.")
    @Permission("cheesehunt.debug")
    fun debugStopRespawn(css: CommandSourceStack, @Argument("player") player : Player) {
        Main.getGame().dev.parseDevMessage("${player.name}'s respawn loop force stopped by ${css.sender.name}.", DevStatus.SEVERE)
        Main.getGame().respawnTask.stopRespawnLoop(player)
    }

    @Command("debug win_show <team>")
    @CommandDescription("Debug command to test the game win show.")
    @Permission("cheesehunt.debug")
    fun debugWinShow(css: CommandSourceStack, @Argument("team") team : Teams) {
        Main.getGame().dev.parseDevMessage("Win show ran for $team team by ${css.sender.name}.", DevStatus.INFO)
        Main.getGame().winShowTask.stopWinShowLoop()
        Main.getGame().winShowTask.startWinShowLoop(Main.getPlugin(), team)
    }

    @Command("debug cheese give <player>")
    @CommandDescription("Debug command to give players cheese.")
    @Permission("cheesehunt.debug")
    fun debugGiveCheese(css: CommandSourceStack, @Argument("player") player: Player) {
        Main.getGame().dev.parseDevMessage("${css.sender.name} gave ${player.name} cheese.", DevStatus.WARNING)
        Main.getGame().cheeseManager.playerPickupCheese(player, Location(player.world, 1000.0, 319.0, 1000.0))
    }

    @Command("debug cheese remove <player>")
    @CommandDescription("Debug command to remove player's cheese.")
    @Permission("cheesehunt.debug")
    fun debugRemoveCheese(css: CommandSourceStack, @Argument("player") player: Player) {
        Main.getGame().dev.parseDevMessage("${css.sender.name} removed ${player.name}'s cheese.", DevStatus.WARNING)
        Main.getGame().cheeseManager.playerDropCheese(player)
    }

    @Command("debug cheese place square")
    @CommandDescription("Debug command to place a cheese square.")
    @Permission("cheesehunt.debug")
    fun debugPlaceCheeseDrop(css: CommandSourceStack) {
        Main.getGame().dev.parseDevMessage("${css.sender.name} placed a cheese drop.", DevStatus.WARNING)
        Main.getGame().blockManager.placeCheeseSquare()
    }

    @Command("debug cheese place cube")
    @CommandDescription("Debug command to place a cheese cube.")
    @Permission("cheesehunt.debug")
    fun debugPlaceCheeseCube(css: CommandSourceStack) {
        Main.getGame().dev.parseDevMessage("${css.sender.name} placed a cheese cube.", DevStatus.WARNING)
        Main.getGame().blockManager.placeCheeseCube()
    }


    @Command("debug skull <player>")
    @CommandDescription("Debug command to test Noxesium's player heads in UI.")
    @Permission("cheesehunt.debug")
    fun debugTestSkulls(css: CommandSourceStack, @Argument("player") player: Player) {
        css.sender.sendMessage(Component.text("\uD001 ", NamedTextColor.WHITE).append(Component.text("This is ${player.name}'s head:", NamedTextColor.YELLOW).append(Component.score("%NCPH%${player.uniqueId},false,0,0,1.0", "").color(NamedTextColor.WHITE)).append(Component.text("!", NamedTextColor.YELLOW))))
    }

    @Command("debug spawn_item <item>")
    @CommandDescription("Debug command to test item spawns.")
    @Permission("cheesehunt.debug")
    fun debugTestSkulls(css: CommandSourceStack, @Argument("item") powerUpItem: PowerUpItem) {
        Main.getGame().dev.parseDevMessage("$powerUpItem items spawned on map sides by ${css.sender.name}.", DevStatus.WARNING)
        Main.getGame().itemManager.spawnSideItems(powerUpItem)
    }

    @Command("queue data")
    @CommandDescription("Debug command to get queue data.")
    @Permission("cheesehunt.debug")
    fun debugQueueData(css: CommandSourceStack) {
        css.sender.sendMessage(
            Component.text("Queue Data\n", NamedTextColor.GOLD).append(
                Component.text("Queue", NamedTextColor.YELLOW).append(
                    Component.text(": ${Main.getGame().queue.getQueue()}\n", NamedTextColor.WHITE).append(
                        Component.text("State", NamedTextColor.RED).append(
                            Component.text(": ${Main.getGame().queue.getQueueState()}", NamedTextColor.WHITE)
                        )
                    )
                )
            )
        )
    }

    @Command("queue join")
    @CommandDescription("Debug command for queues.")
    @Permission("cheesehunt.debug")
    fun debugJoinQueue(css: CommandSourceStack) {
        Main.getGame().queue.joinQueue(css.sender as Player)
    }

    @Command("queue leave")
    @CommandDescription("Debug command for queues.")
    @Permission("cheesehunt.debug")
    fun debugLeaveQueue(css: CommandSourceStack) {
        Main.getGame().queue.leaveQueue(css.sender as Player)
    }

    @Command("queue force join <player>")
    @CommandDescription("Debug command for queues.")
    @Permission("cheesehunt.debug")
    fun debugForceJoinQueue(css: CommandSourceStack, @Argument("player") player : Player) {
        Main.getGame().dev.parseDevMessage("${css.sender.name} pushed ${player.name} into the Queue.", DevStatus.INFO)
        Main.getGame().queue.joinQueue(player)
    }

    @Command("queue force leave <player>")
    @CommandDescription("Debug command for queues.")
    @Permission("cheesehunt.debug")
    fun debugForceLeaveQueue(css: CommandSourceStack, @Argument("player") player : Player) {
        Main.getGame().dev.parseDevMessage("${css.sender.name} threw ${player.name} out of the Queue.", DevStatus.INFO)
        Main.getGame().queue.leaveQueue(player)
    }

    @Command("debug test rarities")
    @CommandDescription("Debug command for item rarity testing.")
    @Permission("cheesehunt.debug")
    fun debugTestRarities(css: CommandSourceStack) {
        val player = css.sender as Player
        for(rarity in ItemRarity.entries) {
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
            player.inventory.addItem(ItemStack(rarityTestItem))
        }
    }

    @Command("debug test types")
    @CommandDescription("Debug command for item type testing.")
    @Permission("cheesehunt.debug")
    fun debugTestTypes(css: CommandSourceStack) {
        val player = css.sender as Player
        for(type in ItemType.entries) {
            val typeTestItem = ItemStack(Material.STICK, 1)
            val typeTestItemMeta = typeTestItem.itemMeta
            typeTestItemMeta.displayName(Component.text("Test Item").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false))
            val typeTestItemLore = listOf(
                Component.text(type.typeGlyph, NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
                Component.text("Debug item.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
            )
            typeTestItemMeta.lore(typeTestItemLore)
            typeTestItem.itemMeta = typeTestItemMeta
            player.inventory.addItem(ItemStack(typeTestItem))
        }
    }
}