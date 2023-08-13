package dev.byrt.cheesehunt.game

import dev.byrt.cheesehunt.Main
import dev.byrt.cheesehunt.manager.*
import dev.byrt.cheesehunt.state.*
import dev.byrt.cheesehunt.task.Music
import dev.byrt.cheesehunt.util.DevStatus

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title

import org.bukkit.Bukkit
import org.bukkit.SoundCategory
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

import java.time.Duration

class GameTask(private var game : Game) {
    private var gameRunnableList = mutableMapOf<Int, BukkitRunnable>()
    private var currentGameTaskId = 0
    private var timeLeft = 0
    private var previousTimeLeft = 0
    private var displayTime: String = "00:00"
    private var previousDisplayTime: String = "00:00"
    private var gameSubtitle = ""

    fun gameLoop() {
        val gameRunnable = object : BukkitRunnable() {
            override fun run() {
                // Formatting variables
                previousTimeLeft = timeLeft + 1
                displayTime = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60)
                previousDisplayTime = String.format("%02d:%02d", previousTimeLeft / 60, previousTimeLeft % 60)

                // Update scoreboard timer
                game.infoBoardManager.updateScoreboardTimer(displayTime, previousDisplayTime, game.gameManager.getGameState())

                // Game/round starting front end
                if(game.gameManager.getGameState() == GameState.STARTING && game.timerManager.getTimerState() == TimerState.ACTIVE) {
                    if(game.roundManager.getRoundState() == RoundState.ONE) { // Game tutorial
                        if(timeLeft == 75) {
                            for(player in Bukkit.getOnlinePlayers()) {
                                player.showTitle(Title.title(
                                    Component.text("Cheese Hunt").color(NamedTextColor.YELLOW),
                                    Component.text(gameSubtitle).color(NamedTextColor.GOLD),
                                    Title.Times.times(
                                        Duration.ofSeconds(1),
                                        Duration.ofSeconds(4),
                                        Duration.ofSeconds(1)
                                        )
                                    )
                                )
                            }
                        }
                        if(timeLeft == 70) {
                            for(player in Bukkit.getOnlinePlayers()) {
                                player.playSound(player.location, Sounds.Tutorial.TUTORIAL_POP, 1f, 1f)
                                player.sendMessage(Component.text("-----------------------------------------------------\n\n").color(NamedTextColor.GREEN).decoration(TextDecoration.STRIKETHROUGH, true)
                                    .append(Component.text("  Welcome to Cheese Hunt!\n\n").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.STRIKETHROUGH, false)
                                        .append(Component.text("        Here's how to play...").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, true).decoration(TextDecoration.BOLD, false)
                                            .append(Component.text("\n\n\n\n\n")
                                                .append(Component.text("-----------------------------------------------------").color(NamedTextColor.GREEN).decoration(TextDecoration.STRIKETHROUGH, true)
                                                )
                                            )
                                        )
                                    )
                                )
                            }
                        }
                        if(timeLeft == 65) {
                            for(player in Bukkit.getOnlinePlayers()) {
                                player.playSound(player.location, Sounds.Tutorial.TUTORIAL_POP, 1f, 1f)
                                player.sendMessage(Component.text("-----------------------------------------------------").color(NamedTextColor.GREEN).decoration(TextDecoration.STRIKETHROUGH, true)
                                    .append(Component.text(" Game:\n").color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.STRIKETHROUGH, false)
                                        .append(Component.text(" • Cheese Hunt is a team based, PvP and collection game.\n").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, false)
                                            .append(Component.text(" • Your goal is to collect Cheese and place it in your team's base.\n").color(NamedTextColor.WHITE)
                                                .append(Component.text(" • Cheese drops are placed in the centre every 2 minutes.\n").color(NamedTextColor.WHITE)
                                                    .append(Component.text(" • Cheese is counted in team bases every 3 minutes.\n\n\n").color(NamedTextColor.WHITE)
                                                        .append(Component.text("-----------------------------------------------------").color(NamedTextColor.GREEN).decoration(TextDecoration.STRIKETHROUGH, true)
                                                        )
                                                    )
                                                )
                                            )
                                        )
                                    )
                                )
                            }
                        }
                        if(timeLeft == 55) {
                            for(player in Bukkit.getOnlinePlayers()) {
                                player.playSound(player.location, Sounds.Tutorial.TUTORIAL_POP, 1f, 1f)
                                player.sendMessage(Component.text("-----------------------------------------------------").color(NamedTextColor.GREEN).decoration(TextDecoration.STRIKETHROUGH, true)
                                    .append(Component.text(" Combat & Items:\n").color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.STRIKETHROUGH, false)
                                        .append(Component.text(" • Players are armed with melee and ranged weaponry.\n").color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false)
                                            .append(Component.text(" • A pickaxe is also given specifically for cheese grabbing.\n").color(NamedTextColor.WHITE)
                                                .append(Component.text(" • Arrows do not regenerate and are only granted on respawn.\n").color(NamedTextColor.RED)
                                                    .append(Component.text(" • Special Items spawn on each side of the map periodically.\n").color(NamedTextColor.YELLOW)
                                                        .append(Component.text(" • These items include throwable TNT and various effect granting charms.\n", NamedTextColor.LIGHT_PURPLE)
                                                            .append(Component.text("-----------------------------------------------------").color(NamedTextColor.GREEN).decoration(TextDecoration.STRIKETHROUGH, true)
                                                            )
                                                        )
                                                    )
                                                )
                                            )
                                        )
                                    )
                                )
                            }
                        }
                        if(timeLeft == 40) {
                            for(player in Bukkit.getOnlinePlayers()) {
                                player.playSound(player.location, Sounds.Tutorial.TUTORIAL_POP, 1f, 1f)
                                player.sendMessage(Component.text("-----------------------------------------------------").color(NamedTextColor.GREEN).decoration(TextDecoration.STRIKETHROUGH, true)
                                    .append(Component.text(" Win Criteria & Extra Information:\n").color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.STRIKETHROUGH, false)
                                        .append(Component.text(" • The team with the most coins at the end of the game will win.\n").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, false)
                                            .append(Component.text(" • A multiplier minute occurs for one minute randomly during the game.\n").color(NamedTextColor.YELLOW)
                                                .append(Component.text(" • Overtime occurs at the end of the game where a final Cheese Drop is put into play and PvP is disabled.\n").color(NamedTextColor.RED)
                                                    .append(Component.text(" • Game Music is on the Voice/Speech slider.\n").color(NamedTextColor.LIGHT_PURPLE)
                                                        .append(Component.text("-----------------------------------------------------").color(NamedTextColor.GREEN).decoration(TextDecoration.STRIKETHROUGH, true)
                                                        )
                                                    )
                                                )
                                            )
                                        )
                                    )
                                )
                            }
                        }
                        if(timeLeft == 25) {
                            for(player in Bukkit.getOnlinePlayers()) {
                                player.playSound(player.location, Sounds.Tutorial.TUTORIAL_POP, 1f, 1f)
                                player.sendMessage(Component.text("-----------------------------------------------------").color(NamedTextColor.GREEN).decoration(TextDecoration.STRIKETHROUGH, true)
                                    .append(Component.text(" Starting soon:\n\n").color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.STRIKETHROUGH, false)
                                        .append(Component.text("      Standby for the game to begin...\n\n").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, false).decoration(TextDecoration.ITALIC, true)
                                            .append(Component.text("\n\n\n")
                                                .append(Component.text("-----------------------------------------------------").color(NamedTextColor.GREEN).decoration(TextDecoration.STRIKETHROUGH, true).decoration(TextDecoration.ITALIC, false)
                                                )
                                            )
                                        )
                                    )
                                )
                            }
                        }
                        if(timeLeft in 4..10) {
                            for(player in Bukkit.getOnlinePlayers()) {
                                player.showTitle(Title.title(
                                    Component.text("Starting in").color(NamedTextColor.AQUA),
                                    Component.text("►$timeLeft◄").decoration(TextDecoration.BOLD, true),
                                    Title.Times.times(
                                        Duration.ofSeconds(0),
                                        Duration.ofSeconds(5),
                                        Duration.ofSeconds(0)
                                    )
                                )
                                )
                                player.playSound(player.location, Sounds.Timer.STARTING_TICK, 0.75f, 1f)
                            }
                        }
                        if(timeLeft == 3 || timeLeft == 2 || timeLeft == 1) {
                            for(player in Bukkit.getOnlinePlayers()) {
                                player.playSound(player.location, Sounds.Timer.STARTING_TICK_FINAL, 1f, 1f)
                                if(timeLeft == 3) {
                                    player.showTitle(Title.title(
                                        Component.text("Starting in").color(NamedTextColor.AQUA),
                                        Component.text("►$timeLeft◄").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true),
                                        Title.Times.times(
                                            Duration.ofSeconds(0),
                                            Duration.ofSeconds(5),
                                            Duration.ofSeconds(0)
                                        )
                                    )
                                    )
                                }
                                if(timeLeft == 2) {
                                    player.showTitle(Title.title(
                                        Component.text("Starting in").color(NamedTextColor.AQUA),
                                        Component.text("►$timeLeft◄").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true),
                                        Title.Times.times(
                                            Duration.ofSeconds(0),
                                            Duration.ofSeconds(5),
                                            Duration.ofSeconds(0)
                                        )
                                    )
                                    )
                                }
                                if(timeLeft == 1) {
                                    player.showTitle(Title.title(
                                        Component.text("Starting in").color(NamedTextColor.AQUA),
                                        Component.text("►$timeLeft◄").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true),
                                        Title.Times.times(
                                            Duration.ofSeconds(0),
                                            Duration.ofSeconds(5),
                                            Duration.ofSeconds(0)
                                        )
                                    )
                                    )
                                }
                            }
                        }
                    }
                    if(timeLeft <= 0) {
                        game.gameManager.nextState()
                    }
                }

                // IN_GAME state
                if(game.gameManager.getGameState() == GameState.IN_GAME && game.timerManager.getTimerState() == TimerState.ACTIVE) {
                    if(timeLeft % 120 == 0) {
                        if(timeLeft != 0) {
                            game.blockManager.placeCheeseSquare()
                        }
                    }
                    if(timeLeft % 180 == 0) {
                        if(timeLeft != 720) {
                            game.cheeseManager.countCheeseInBases(false)
                        }
                    }
                    if(timeLeft % 25 == 0) {
                        if(timeLeft != 0) {
                            if(timeLeft != 720) {
                                game.itemManager.spawnSideItems(game.itemManager.getRandomItem())
                            }
                        }
                    }
                    if(timeLeft == 120) {
                        game.blockManager.removeBlastDoors()
                    }
                    if(timeLeft == 31) {
                        for(player in Bukkit.getOnlinePlayers()) {
                            player.playSound(player.location, Sounds.Music.OVERTIME_INTRO_MUSIC, SoundCategory.VOICE, 1f, 1f)
                        }
                    }
                    if(timeLeft in 11..30 || timeLeft % 60 == 0) {
                        for(player in Bukkit.getOnlinePlayers()) {
                            player.playSound(player.location, Sounds.Timer.CLOCK_TICK, 0.75f, 1f)
                        }
                        if(timeLeft == 28) {
                            for(player in Bukkit.getOnlinePlayers()) {
                                Main.getGame().musicTask.stopMusicLoop(player, Music.MAIN)
                                Main.getGame().musicTask.startMusicLoop(player, Main.getPlugin(), Music.OVERTIME)
                            }
                        }
                    }
                    if(timeLeft in 0..10) {
                        for(player in Bukkit.getOnlinePlayers()) {
                            player.playSound(player.location, Sounds.Timer.CLOCK_TICK_HIGH, 0.75f, 2f)
                        }
                    }
                    if(timeLeft == game.scoreManager.getMultiplierMinute() * 60 + 30) {
                        game.scoreManager.setMultiplier(2)
                    }
                    if(timeLeft == game.scoreManager.getMultiplierMinute() * 60 - 30) {
                        game.scoreManager.setMultiplier(1)
                    }
                }

                // Overtime
                if(timeLeft <= 0 && game.gameManager.getGameState() == GameState.IN_GAME && game.timerManager.getTimerState() == TimerState.ACTIVE && game.gameManager.isOvertimeActive()) {
                    game.gameManager.nextState()
                }

                if(game.gameManager.getGameState() == GameState.OVERTIME && game.timerManager.getTimerState() == TimerState.ACTIVE) {
                    if(timeLeft in 11..30 || timeLeft % 60 == 0) {
                        for(player in Bukkit.getOnlinePlayers()) {
                            player.playSound(player.location, Sounds.Timer.CLOCK_TICK, 0.75f, 1f)
                        }
                    }
                    if(timeLeft in 0..10) {
                        for(player in Bukkit.getOnlinePlayers()) {
                            player.playSound(player.location, Sounds.Timer.CLOCK_TICK_HIGH, 0.75f, 2f)
                        }
                    }
                    if(timeLeft <= 0) {
                        game.cheeseManager.countCheeseInBases(false)
                    }
                }

                // Round ending front end
                if(timeLeft <= 0 && game.gameManager.getGameState() == GameState.IN_GAME && game.timerManager.getTimerState() == TimerState.ACTIVE && !game.gameManager.isOvertimeActive() || timeLeft <= 0 && game.gameManager.getGameState() == GameState.OVERTIME && game.timerManager.getTimerState() == TimerState.ACTIVE) {
                    game.gameManager.nextState()
                }

                // Game end cycle
                if(game.gameManager.getGameState() == GameState.GAME_END && game.timerManager.getTimerState() == TimerState.ACTIVE) {
                    if(timeLeft == 25) {
                        for(player in Bukkit.getOnlinePlayers()) {
                            player.sendMessage(Component.text("\nTeam Placements:", NamedTextColor.WHITE, TextDecoration.BOLD))
                        }
                    }
                    if(timeLeft == 23) {
                        game.scoreManager.teamPlacements()
                    }
                    if(timeLeft == 20) {
                        for(player in Bukkit.getOnlinePlayers()) {
                            player.sendMessage(Component.text("\nCheese Collected:", NamedTextColor.WHITE, TextDecoration.BOLD))
                        }
                    }
                    if(timeLeft == 18) {
                        game.statsManager.statsBreakdown(Statistic.CHEESE_PICKED_UP)
                    }
                    if(timeLeft == 15) {
                        for(player in Bukkit.getOnlinePlayers()) {
                            player.sendMessage(Component.text("\nCheese Lost:", NamedTextColor.WHITE, TextDecoration.BOLD))
                        }
                    }
                    if(timeLeft == 13) {
                        game.statsManager.statsBreakdown(Statistic.CHEESE_DROPPED)
                    }
                    if(timeLeft == 10) {
                        for(player in Bukkit.getOnlinePlayers()) {
                            player.sendMessage(Component.text("\nEliminations:", NamedTextColor.WHITE, TextDecoration.BOLD))
                        }
                    }
                    if(timeLeft == 8) {
                        game.statsManager.statsBreakdown(Statistic.ELIMINATIONS)
                    }
                    if(timeLeft == 5) {
                        for(player in Bukkit.getOnlinePlayers()) {
                            player.sendMessage(Component.text("\nDeaths:", NamedTextColor.WHITE, TextDecoration.BOLD))
                        }
                    }
                    if(timeLeft == 3) {
                        game.statsManager.statsBreakdown(Statistic.DEATHS)
                    }
                    if(timeLeft <= 0) {
                        game.gameManager.nextState()
                        cancelGameTask()
                    }
                }
                // Decrement timer by 1 if timer is active //TODO: Possibly move to top of class?
                if(game.timerManager.getTimerState() == TimerState.ACTIVE) {
                    timeLeft--
                }
            }
        }
        gameRunnable.runTaskTimer(Main.getPlugin(), 0L, 20L)
        currentGameTaskId = gameRunnable.taskId
        gameRunnableList[gameRunnable.taskId] = gameRunnable
    }

    fun cancelGameTask() {
        gameRunnableList.remove(currentGameTaskId)?.cancel()
    }

    fun setTimeLeft(setTimeLeft: Int, sender : Player?) {
        val setTimerTimeLeft = String.format("%02d:%02d", setTimeLeft / 60, setTimeLeft % 60)
        game.infoBoardManager.updateScoreboardTimer(setTimerTimeLeft, displayTime, game.gameManager.getGameState())
        timeLeft = setTimeLeft
        if(sender != null) {
            Main.getGame().dev.parseDevMessage("Timer updated to $timeLeft seconds by ${sender.name}.", DevStatus.INFO)
        } else {
            Main.getGame().dev.parseDevMessage("Timer updated to $timeLeft seconds.", DevStatus.INFO)
        }
    }

    fun getTimeLeft() : Int {
        return timeLeft
    }

    fun setGameSubtitle(newSubtitle : String) {
        gameSubtitle = newSubtitle
    }

    fun resetVars() {
        timeLeft = 0
        previousTimeLeft = 0
        displayTime = "00:00"
        previousDisplayTime = "00:00"
        gameSubtitle = ""
    }
}