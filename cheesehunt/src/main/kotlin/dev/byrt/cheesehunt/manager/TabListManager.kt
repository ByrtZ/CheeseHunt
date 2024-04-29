package dev.byrt.cheesehunt.manager

import dev.byrt.cheesehunt.state.Teams
import me.lucyydotp.cheeselib.sys.TeamManager
import me.lucyydotp.cheeselib.sys.nameformat.NameFormatter
import me.lucyydotp.cheeselib.inject.context
import me.lucyydotp.cheeselib.module.Module
import me.lucyydotp.cheeselib.module.ModuleHolder
import me.lucyydotp.cheeselib.util.calculateOrdinalRanking
import net.kyori.adventure.extra.kotlin.text
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.GameMode

class TabListManager(parent: ModuleHolder) : Module(parent) {
    private val cheesePuns = listOf(
        "When life gives you cheese, you make a minigame with it",
        "I don't have any gouda puns to put here",
        "Do you brie-lieve in life after love?",
        "I'd make a cheesy joke, but I gouda say, it'd brie a bit too forced",
        "You cheddar not say any cheese jokes!",
        "Made you look: Cheese Edition",
        "Nothing gets cheddar than this",
        "Anything you can do, I can do feta",
        "You think you’re feta than me?",
        "Hello, is it brie you're looking for?",
        "This might sound cheesy, but I think you're really grate",
        "Damn! You're looking mozzar-hella good today!",
        "Hey, I camembert the last time we saw each other",
        "After the cheese factory exploded, all that was left was de-brie",
        "Sweet dreams are made of cheese",
        "I brought my Cheese rifle, it's time to go hunting",
        "I swiss you the best!",
        "Cheddar days are coming...",
        "Sweet dreams are made of cheese,\nWho am I to disa-brie,\nI cheddar the world by the feta cheese,\nEverybody's looking for stilton.",
    )

    private val baseHeader = text {
        append(Component.text(" ".repeat(100)))
        appendNewline()
        append(Component.text("Cheese Hunt", NamedTextColor.YELLOW, TextDecoration.BOLD))
        appendNewline()
        appendNewline()
        append(Component.text("An MCC Tester classic meme, brought to you by "))
        append(Component.text("Byrt", NamedTextColor.RED))
        append(Component.text(","))
        appendNewline()
        append(Component.text("and \"improved\" by Lucy."))
        appendNewline()
        appendNewline()
    }

    private var currentHeader = Component.text("")
        set(value) {
            field = value
            Bukkit.getOnlinePlayers().forEach {
                it.sendPlayerListHeader(value)
            }
        }

    private val blankLines = Component.text("\n".repeat(200))
    private val noTeamsAvailable = Component.text("No teams available.", NamedTextColor.GRAY)
    private val onlineAdmins = Component.text("Online Admins:", NamedTextColor.DARK_RED)


    private val nameFormatter: NameFormatter by context()
    private val scoreManager: ScoreManager by context()
    private val teamManager: TeamManager<Teams> by context()

    init {
        listen(nameFormatter.afterFormat) {
            updateAllTabList()
        }
    }

    private fun buildTab() = text {
        append(baseHeader)

        if (teamManager.teams.all { teamManager.playersOnTeam(it).isEmpty() }) {
            append(noTeamsAvailable)
            appendNewline()
        } else {
            teamManager.teams
                .calculateOrdinalRanking(scoreManager::getScore)
                .forEach { (team, ordinal) ->
                    append(Component.text("$ordinal. "))
                    append(team.displayName)
                    appendNewline()

                    teamManager.playersOnTeam(team)
                        .mapNotNull(Bukkit::getPlayer)
                        .map {
                            Component.text(
                                it.name,
                                if (it.gameMode == GameMode.SPECTATOR) NamedTextColor.GRAY else team.nameColor
                            )
                        }
                        .let(::append)

                    append(Component.text("     ${scoreManager.getScore(team)}c"))
                    appendNewline()
                    appendNewline()
                }
        }
        appendNewline()
        append(onlineAdmins)
        appendNewline()
        append(Bukkit.getOperators()
            .filter { it.isOnline }
            .map { it.name }
            .joinToString(" ")
            .let { Component.text(it, NamedTextColor.DARK_RED) }
        )
        appendNewline()
        appendNewline()

        append(Component.text("'${cheesePuns.random()}'", NamedTextColor.GRAY, TextDecoration.ITALIC))
        append(blankLines)
    }

    fun updateAllTabList() {
        currentHeader = buildTab()
    }

}
