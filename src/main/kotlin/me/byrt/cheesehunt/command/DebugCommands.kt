package me.byrt.cheesehunt.command

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.game.GameState
import me.byrt.cheesehunt.state.Teams
import me.byrt.cheesehunt.util.DevStatus
import me.byrt.cheesehunt.manager.PowerUpItem

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player

@Suppress("unused")
class DebugCommands : BaseCommand {
    @CommandMethod("debug next_phase")
    @CommandDescription("Debug command to set game to its next phase.")
    @CommandPermission("cheesehunt.debug")
    fun debugNextPhase(sender : Player) {
        Main.getGame().dev.parseDevMessage("Game pushed to next phase by ${sender.name}.", DevStatus.WARNING)
        Main.getGame().gameManager.nextState()
    }

    @CommandMethod("debug force_state <state>")
    @CommandDescription("Debug command to force set game state.")
    @CommandPermission("cheesehunt.debug")
    fun debugForceState(sender : Player, @Argument("state") state : GameState) {
        Main.getGame().dev.parseDevMessage("Game state updated to $state by ${sender.name}.", DevStatus.SEVERE)
        Main.getGame().gameManager.forceState(state)
    }

    @CommandMethod("debug stop_respawn <player>")
    @CommandDescription("Debug command to force stop respawn loops.")
    @CommandPermission("cheesehunt.debug")
    fun debugStopRespawn(sender : Player, @Argument("player") player : Player) {
        Main.getGame().dev.parseDevMessage("${player.name}'s respawn loop force stopped by ${sender.name}.", DevStatus.SEVERE)
        Main.getGame().respawnTask.stopRespawnLoop(player)
    }

    @CommandMethod("debug win_show <team>")
    @CommandDescription("Debug command to test the game win show.")
    @CommandPermission("cheesehunt.debug")
    fun debugWinShow(sender : Player, @Argument("team") team : Teams) {
        Main.getGame().dev.parseDevMessage("Win show ran for $team team by ${sender.name}.", DevStatus.INFO)
        Main.getGame().winShowTask.stopWinShowLoop()
        Main.getGame().winShowTask.startWinShowLoop(Main.getPlugin(), team)
    }

    @CommandMethod("debug cheese give <player>")
    @CommandDescription("Debug command to give players cheese.")
    @CommandPermission("cheesehunt.debug")
    fun debugGiveCheese(sender : Player, @Argument("player") player: Player) {
        Main.getGame().dev.parseDevMessage("${sender.name} gave ${player.name} cheese.", DevStatus.WARNING)
        Main.getGame().cheeseManager.playerPickupCheese(player, Location(player.world, 1000.0, 319.0, 1000.0))
    }

    @CommandMethod("debug cheese remove <player>")
    @CommandDescription("Debug command to remove player's cheese.")
    @CommandPermission("cheesehunt.debug")
    fun debugRemoveCheese(sender : Player, @Argument("player") player: Player) {
        Main.getGame().dev.parseDevMessage("${sender.name} removed ${player.name}'s cheese.", DevStatus.WARNING)
        Main.getGame().cheeseManager.playerDropCheese(player)
    }

    @CommandMethod("debug cheese place square")
    @CommandDescription("Debug command to place a cheese square.")
    @CommandPermission("cheesehunt.debug")
    fun debugPlaceCheeseDrop(sender : Player) {
        Main.getGame().dev.parseDevMessage("${sender.name} placed a cheese drop.", DevStatus.WARNING)
        Main.getGame().blockManager.placeCheeseSquare()
    }

    @CommandMethod("debug cheese place cube")
    @CommandDescription("Debug command to place a cheese cube.")
    @CommandPermission("cheesehunt.debug")
    fun debugPlaceCheeseCube(sender : Player) {
        Main.getGame().dev.parseDevMessage("${sender.name} placed a cheese cube.", DevStatus.WARNING)
        Main.getGame().blockManager.placeCheeseCube()
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
        Main.getGame().dev.parseDevMessage("$powerUpItem items spawned on map sides by ${sender.name}.", DevStatus.WARNING)
        Main.getGame().itemManager.spawnSideItems(powerUpItem)
    }

    @CommandMethod("queue join")
    @CommandDescription("Debug command to test queues.")
    fun debugJoinQueue(sender : Player) {
        Main.getGame().queue.joinQueue(sender)
    }

    @CommandMethod("queue leave")
    @CommandDescription("Debug command to test queues.")
    fun debugLeaveQueue(sender : Player) {
        Main.getGame().queue.leaveQueue(sender)
    }
}