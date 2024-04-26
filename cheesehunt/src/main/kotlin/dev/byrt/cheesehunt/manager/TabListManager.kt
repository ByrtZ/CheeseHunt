package dev.byrt.cheesehunt.manager

import dev.byrt.cheesehunt.game.Game
import dev.byrt.cheesehunt.game.GameState
import dev.byrt.cheesehunt.state.Teams

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.Bukkit
import org.bukkit.entity.Player

import kotlin.collections.ArrayList
import kotlin.random.Random

class TabListManager(private var game : Game) {
    private var cheesePuns = ArrayList<String>()
    private val baseHeader = Component.text("\nCheese Hunt", NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true).append(Component.text("\n\n               An MCC Tester classic meme, brought to you by ", NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false).append(Component.text("Byrt", NamedTextColor.RED)).append(Component.text(".               \n", NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false)))
    private var currentHeader = Component.text("")
    private var blankLines = ""
    private val baseAdminFooter = Component.text("\n\nOnline Admins:\n", NamedTextColor.DARK_RED).decoration(TextDecoration.BOLD, false)
    private var footer = Component.text("")

    fun buildBase() {
        for(i in 0..200) { blankLines += "\n" }
        currentHeader = baseHeader.append(Component.text("\nNo teams available.", NamedTextColor.GRAY)).append(baseAdminFooter).append(game.teamManager.getAdminNames()).append(assignRandomPun()).append(Component.text(blankLines))
    }

    fun updateAllTabList() {
        val red = game.teamManager.getRedTeam()
        val blue = game.teamManager.getBlueTeam()
        val redScore = game.scoreManager.getRedScore()
        val blueScore = game.scoreManager.getBlueScore()
        val adminFooter = baseAdminFooter.append(game.teamManager.getAdminNames())
        if(red.isEmpty() && blue.isEmpty()) {
            currentHeader = baseHeader.append(Component.text("\nNo teams available.", NamedTextColor.GRAY).decoration(TextDecoration.BOLD, false)).append(adminFooter).append(assignRandomPun()).append(Component.text(blankLines))
        }
        if(red.isNotEmpty() && blue.isNotEmpty() && game.scoreManager.getRedScore() > game.scoreManager.getBlueScore()) {
            currentHeader = baseHeader
                .append(Component.text("\n1. ", NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false).append(Component.text("Red Team\n", NamedTextColor.RED)).append(game.teamManager.getPlayerNames(Teams.RED)).append(Component.text("     ${redScore}c", NamedTextColor.WHITE))
                .append(Component.text("\n\n2. ", NamedTextColor.WHITE).append(Component.text("Blue Team\n", NamedTextColor.BLUE)).append(game.teamManager.getPlayerNames(Teams.BLUE)).append(Component.text("     ${blueScore}c", NamedTextColor.WHITE)).append(adminFooter).append(assignRandomPun()).append(Component.text(blankLines))))
        }
        if(red.isNotEmpty() && blue.isNotEmpty() && game.scoreManager.getRedScore() < game.scoreManager.getBlueScore()) {
            currentHeader = baseHeader
                .append(Component.text("\n1. ", NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false).append(Component.text("Blue Team\n", NamedTextColor.BLUE)).append(game.teamManager.getPlayerNames(Teams.BLUE)).append(Component.text("     ${blueScore}c", NamedTextColor.WHITE))
                    .append(Component.text("\n\n2. ", NamedTextColor.WHITE).append(Component.text("Red Team\n", NamedTextColor.RED)).append(game.teamManager.getPlayerNames(Teams.RED)).append(Component.text("     ${redScore}c", NamedTextColor.WHITE)).append(adminFooter).append(assignRandomPun()).append(Component.text(blankLines))))
        }
        if(red.isNotEmpty() && blue.isNotEmpty() && game.scoreManager.getRedScore() == game.scoreManager.getBlueScore() && game.gameManager.getGameState() != GameState.IDLE) {
            currentHeader = baseHeader
                .append(Component.text("\n1. ", NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false).append(Component.text("Red Team\n", NamedTextColor.RED)).append(game.teamManager.getPlayerNames(Teams.RED)).append(Component.text("     ${redScore}c", NamedTextColor.WHITE))
                    .append(Component.text("\n\n1. ", NamedTextColor.WHITE).append(Component.text("Blue Team\n", NamedTextColor.BLUE)).append(game.teamManager.getPlayerNames(Teams.BLUE)).append(Component.text("     ${blueScore}c", NamedTextColor.WHITE)).append(adminFooter).append(assignRandomPun()).append(Component.text(blankLines))))
        }
        if(red.isNotEmpty() && blue.isNotEmpty() && game.gameManager.getGameState() == GameState.IDLE) {
            currentHeader = baseHeader
                .append(Component.text("\n1. ", NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false).append(Component.text("Red Team\n", NamedTextColor.RED)).append(game.teamManager.getPlayerNames(Teams.RED)).append(Component.text("     ${redScore}c", NamedTextColor.WHITE))
                    .append(Component.text("\n\n2. ", NamedTextColor.WHITE).append(Component.text("Blue Team\n", NamedTextColor.BLUE)).append(game.teamManager.getPlayerNames(Teams.BLUE)).append(Component.text("     ${blueScore}c", NamedTextColor.WHITE)).append(adminFooter).append(assignRandomPun()).append(Component.text(blankLines))))
        }
        if(red.isNotEmpty() && blue.isEmpty()) {
            currentHeader = baseHeader
                .append(Component.text("\n1. ", NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false).append(Component.text("Red Team\n", NamedTextColor.RED)).append(game.teamManager.getPlayerNames(Teams.RED)).append(Component.text("     ${redScore}c", NamedTextColor.WHITE)).append(adminFooter).append(assignRandomPun()).append(Component.text(blankLines)))
        }
        if(blue.isNotEmpty() && red.isEmpty()) {
            currentHeader = baseHeader
                .append(Component.text("\n1. ", NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false).append(Component.text("Blue Team\n", NamedTextColor.BLUE)).append(game.teamManager.getPlayerNames(Teams.BLUE)).append(Component.text("     ${blueScore}c", NamedTextColor.WHITE)).append(adminFooter).append(assignRandomPun()).append(Component.text(blankLines)))
        }
        for(player in Bukkit.getOnlinePlayers()) {
            sendTabList(player)
        }
    }

    private fun sendTabList(player : Player) {
        player.sendPlayerListHeaderAndFooter(currentHeader, footer)
    }

    private fun assignRandomPun() : Component {
        val randomIndex = Random.nextInt(cheesePuns.size)
        val randomPun = cheesePuns[randomIndex]
        return Component.text(randomPun).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, true).decoration(TextDecoration.BOLD, false)
    }

    fun populateCheesePuns() {
        cheesePuns.add("\n\n'When life gives you cheese, you make a minigame with it'\n\n")
        cheesePuns.add("\n\n'I don't have any gouda puns to put here'\n\n")
        cheesePuns.add("\n\n'Do you brie-lieve in life after love?'\n\n")
        cheesePuns.add("\n\n'I'd make a cheesy joke, but I gouda say, it'd brie a bit too forced'\n\n")
        cheesePuns.add("\n\n'You cheddar not say any cheese jokes!'\n\n")
        cheesePuns.add("\n\n'Made you look: Cheese Edition'\n\n")
        cheesePuns.add("\n\n'Nothing gets cheddar than this'\n\n")
        cheesePuns.add("\n\n'Anything you can do, I can do feta'\n\n")
        cheesePuns.add("\n\n'You think you’re feta than me?'\n\n")
        cheesePuns.add("\n\n'Hello, is it brie you're looking for?'\n\n")
        cheesePuns.add("\n\n'This might sound cheesy, but I think you're really grate'\n\n")
        cheesePuns.add("\n\n'Damn! You're looking mozzar-hella good today!'\n\n")
        cheesePuns.add("\n\n'Hey, I camembert the last time we saw each other'\n\n")
        cheesePuns.add("\n\n'After the cheese factory exploded, all that was left was de-brie'\n\n")
        cheesePuns.add("\n\n'Sweet dreams are made of cheese'\n\n")
        cheesePuns.add("\n\n'I brought my Cheese rifle, it's time to go hunting'\n\n")
        cheesePuns.add("\n\n'I swiss you the best!'\n\n")
        cheesePuns.add("\n\n'Cheddar days are coming...'\n\n")
        cheesePuns.add("\n\n'Sweet dreams are made of cheese,\nWho am I to disa-brie,\nI cheddar the world by the feta cheese,\nEverybody's looking for stilton.'\n\n")
    }
}