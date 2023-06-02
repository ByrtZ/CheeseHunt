package me.byrt.cheesehunt.command

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.state.GameState
import me.byrt.cheesehunt.state.Teams
import me.byrt.cheesehunt.state.DevStatus

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission

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
        Main.getGame().dev.parseDevMessage("Game state updated to $state by ${sender.name}.", DevStatus.WARNING)
        Main.getGame().gameManager.forceState(state)
    }

    @CommandMethod("debug stop_respawn <player>")
    @CommandDescription("Debug command to force stop respawn loops.")
    @CommandPermission("cheesehunt.debug")
    fun debugStopRespawn(sender : Player, @Argument("player") player : Player) {
        Main.getGame().dev.parseDevMessage("${player.name}'s respawn loop force stopped by ${sender.name}.", DevStatus.WARNING)
        Main.getGame().respawnTask.stopRespawnLoop(player)
    }

    @CommandMethod("debug win_show <team>")
    @CommandDescription("Debug command to test the game win show.")
    @CommandPermission("cheesehunt.debug")
    fun debugWinShow(sender : Player, @Argument("team") team : Teams) {
        Main.getGame().dev.parseDevMessage("Win show ran for $team team by ${sender.name}.", DevStatus.INFO)
        Main.getGame().winShowTask.startWinShowLoop(Main.getPlugin(), team)
    }

    @CommandMethod("debug cheese give <player>")
    @CommandDescription("Debug command to give players cheese.")
    @CommandPermission("cheesehunt.debug")
    fun debugGiveCheese(sender : Player, @Argument("player") player: Player) {
        Main.getGame().dev.parseDevMessage("${sender.name} gave ${player.name} cheese.", DevStatus.WARNING)
        Main.getGame().cheeseManager.setPlayerHasCheese(player, true)
        Main.getGame().cheeseManager.startHasCheeseLoop(player)
        player.inventory.addItem(Main.getGame().itemManager.getCheeseItem(Main.getGame().teamManager.getPlayerTeam(player.uniqueId)))
    }

    @CommandMethod("debug cheese remove <player>")
    @CommandDescription("Debug command to remove player's cheese.")
    @CommandPermission("cheesehunt.debug")
    fun debugRemoveCheese(sender : Player, @Argument("player") player: Player) {
        Main.getGame().dev.parseDevMessage("${sender.name} removed ${player.name}'s cheese.", DevStatus.WARNING)
        Main.getGame().cheeseManager.playerDropCheese(player)
        Main.getGame().cheeseManager.setPlayerHasCheese(player, false)
    }
}