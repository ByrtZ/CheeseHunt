package me.byrt.cheesehunt.task

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.*
import me.byrt.cheesehunt.state.GameState
import me.byrt.cheesehunt.state.RoundState
import me.byrt.cheesehunt.state.TimerState

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title

import org.bukkit.Bukkit
import org.bukkit.SoundCategory
import org.bukkit.scheduler.BukkitRunnable

import java.time.Duration

class GameCountdownTask(private var game: Game) {
    private var gameRunnableList = mutableMapOf<Int, BukkitRunnable>()
    private var currentGameTaskId = 0
    private var timeLeft = 0
    private var previousTimeLeft = 0
    private var displayTime: String = "00:00"
    private var previousDisplayTime: String = "00:00"
    private var setTimerTimeLeft: String? = null

    fun gameLoop() {
        val gameRunnable = object : BukkitRunnable() {
            override fun run() {
                // Formatting variables
                previousTimeLeft = timeLeft + 1
                displayTime = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60)
                previousDisplayTime = String.format("%02d:%02d", previousTimeLeft / 60, previousTimeLeft % 60)

                // Update scoreboard timer
                game.getInfoBoardManager().updateScoreboardTimer(displayTime, previousDisplayTime, game.getGameState())

                // Game/round starting front end
                if (game.getGameState() == GameState.STARTING && game.getTimerState() == TimerState.ACTIVE) {
                    if(game.getRoundState() == RoundState.ROUND_ONE) { // Game tutorial
                        if(timeLeft == 75) {
                            for(player in Bukkit.getOnlinePlayers()) {
                                player.showTitle(Title.title(
                                    Component.text("Cheese Hunt").color(NamedTextColor.YELLOW),
                                    Component.text(""),
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
                                    .append(Component.text("   Welcome to Cheese Hunt!\n\n").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.STRIKETHROUGH, false)
                                        .append(Component.text("       Here's how to play...").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, true).decoration(TextDecoration.BOLD, false)
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
                                        .append(Component.text(" • Cheese Hunt is a game.\n").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, false)
                                            .append(Component.text(" • Insert\n").color(NamedTextColor.WHITE)
                                                .append(Component.text(" • tutorial\n").color(NamedTextColor.WHITE)
                                                    .append(Component.text(" • here.\n\n\n").color(NamedTextColor.WHITE)
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
                                    .append(Component.text(" Items:\n").color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.STRIKETHROUGH, false)
                                        .append(Component.text(" • yea.\n").color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false)
                                            .append(Component.text(" • made you look.\n").color(NamedTextColor.WHITE)
                                                .append(Component.text("\n\n\n")
                                                    .append(Component.text("-----------------------------------------------------").color(NamedTextColor.GREEN).decoration(TextDecoration.STRIKETHROUGH, true)
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
                                    .append(Component.text(" Win Criteria:\n").color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.STRIKETHROUGH, false)
                                        .append(Component.text(" • git gud ig.\n\n").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, false)
                                            .append(Component.text(" • lmao.").color(NamedTextColor.RED)
                                                .append(Component.text("\n\n\n")
                                                    .append(Component.text("-----------------------------------------------------").color(NamedTextColor.GREEN).decoration(TextDecoration.STRIKETHROUGH, true)
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
                                    .append(Component.text(" Starting soon:\n\n").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.STRIKETHROUGH, false)
                                        .append(Component.text("        Standby for the game to begin...\n\n").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true)
                                            .append(Component.text(" • May the best Cheese hiders and Cheese hunters win.").color(NamedTextColor.GOLD)
                                                .append(Component.text("\n\n\n\n")
                                                    .append(Component.text("-----------------------------------------------------").color(NamedTextColor.GREEN).decoration(TextDecoration.STRIKETHROUGH, true)
                                                    )
                                                )
                                            )
                                        )
                                    )
                                )
                            }
                        }
                        if (timeLeft in 4..10) {
                            for (player in Bukkit.getOnlinePlayers()) {
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
                        if (timeLeft == 3 || timeLeft == 2 || timeLeft == 1) {
                            for (player in Bukkit.getOnlinePlayers()) {
                                player.playSound(player.location, Sounds.Timer.STARTING_TICK_FINAL, 1f, 1f)
                                if (timeLeft == 3) {
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
                                if (timeLeft == 2) {
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
                                if (timeLeft == 1) {
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
                    if (timeLeft <= 0) {
                        for (player in Bukkit.getOnlinePlayers()) {
                            player.playSound(player.location, Sounds.Round.ROUND_END_PLING, 1f, 1f)
                            player.playSound(player.location, Sounds.Round.ROUND_END_PLING, 1f, 2f)
                            Main.getGame().getMusicTask().startMusicLoop(player, Main.getPlugin(), Music.MAIN)
                            player.resetTitle()
                        }
                        game.setGameState(GameState.IN_GAME)
                    }
                }

                // IN_GAME state
                if (game.getGameState() == GameState.IN_GAME && game.getTimerState() == TimerState.ACTIVE) {
                    if(timeLeft % 180 == 0) {
                        if(timeLeft != 0) {
                            game.getBlockManager().placeFullCheeseSquare()
                            for(player in Bukkit.getOnlinePlayers()) {
                                player.sendMessage(Component.text("[")
                                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                                    .append(Component.text("] "))
                                    .append(Component.text("A cheese payload has been dropped in the center.", NamedTextColor.AQUA, TextDecoration.BOLD))
                                )
                            }
                        }
                        if(timeLeft != 720) {
                            for(player in Bukkit.getOnlinePlayers()) {
                                player.sendMessage(Component.text("[")
                                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                                    .append(Component.text("] "))
                                    .append(Component.text("Cheese in team bases has been counted!", NamedTextColor.AQUA, TextDecoration.BOLD))
                                )
                            }
                            game.getCheeseManager().countCheeseInBases()
                        }
                    }
                    if(timeLeft == 31) {
                        for(player in Bukkit.getOnlinePlayers()) {
                            player.playSound(player.location, Sounds.Music.OVERTIME_INTRO_MUSIC, 1f, 1f)
                        }
                    }
                    if (timeLeft in 11..30 || timeLeft % 60 == 0) {
                        for (player in Bukkit.getOnlinePlayers()) {
                            player.playSound(player.location, Sounds.Timer.CLOCK_TICK, 0.75f, 1f)
                        }
                        if (timeLeft == 28) {
                            for (player in Bukkit.getOnlinePlayers()) {
                                Main.getGame().getMusicTask().stopMusicLoop(player, Music.MAIN)
                                Main.getGame().getMusicTask().startMusicLoop(player, Main.getPlugin(), Music.OVERTIME)
                            }
                        }
                    }
                    if (timeLeft in 0..10) {
                        for (player in Bukkit.getOnlinePlayers()) {
                            player.playSound(player.location, Sounds.Timer.CLOCK_TICK_HIGH, 0.75f, 2f)
                        }
                    }
                }

                // Round ending front end
                if (timeLeft <= 0 && game.getGameState() == GameState.IN_GAME && game.getTimerState() == TimerState.ACTIVE) {
                    if (game.getRoundState() == RoundState.ROUND_ONE) {
                        for (player in Bukkit.getOnlinePlayers()) {
                            player.playSound(player.location, Sounds.GameOver.GAME_OVER_PLING, 1f, 1f)
                            player.playSound(player.location, Sounds.GameOver.GAME_OVER_PLING, 1f, 2f)
                            player.playSound(player.location, Sounds.GameOver.GAME_OVER_EFFECT_1, 1f, 1f)
                            player.playSound(player.location, Sounds.GameOver.GAME_OVER_EFFECT_2, 1f, 1f)
                            Main.getGame().getMusicTask().stopMusicLoop(player, Music.MAIN)
                            Main.getGame().getMusicTask().stopMusicLoop(player, Music.OVERTIME)
                            player.playSound(player.location, Sounds.Music.GAME_OVER_MUSIC, SoundCategory.VOICE, 0.85f, 1f)
                            player.showTitle(Title.title(
                                Component.text("Game Over!", NamedTextColor.RED, TextDecoration.BOLD),
                                Component.text(""),
                                Title.Times.times(
                                    Duration.ofSeconds(0),
                                    Duration.ofSeconds(4),
                                    Duration.ofSeconds(1)
                                    )
                                )
                            )
                            if(game.getCheeseManager().playerHasCheese(player)) {
                                game.getCheeseManager().setPlayerHasCheese(player, false)
                            }
                            game.getPlayerManager().setPlayersAdventure()
                            if(!game.getTeamManager().isSpectator(player.uniqueId)) {
                                player.allowFlight = true
                                player.isFlying = true
                            }
                        }
                        game.setGameState(GameState.GAME_END)
                    }
                }

                // Game end cycle
                if (game.getGameState() == GameState.GAME_END && game.getTimerState() == TimerState.ACTIVE) {
                    if(timeLeft == 25) {
                        for(player in Bukkit.getOnlinePlayers()) {
                            player.sendMessage(Component.text("\nTeam Placements:", NamedTextColor.WHITE, TextDecoration.BOLD))
                        }
                    }
                    if(timeLeft == 23) {
                        for(player in Bukkit.getOnlinePlayers()) {
                            player.sendMessage("Red scored: ${game.getScoreManager().getRedScore()} coins.")
                            player.sendMessage("Blue scored: ${game.getScoreManager().getBlueScore()} coins.")
                        }
                        if(game.getScoreManager().getRedScore() > game.getScoreManager().getBlueScore()) {
                            game.getTeamManager().redWinGame()
                        }
                        if(game.getScoreManager().getRedScore() < game.getScoreManager().getBlueScore()) {
                            game.getTeamManager().blueWinGame()
                        }
                        if(game.getScoreManager().getRedScore() == game.getScoreManager().getBlueScore()) {
                            game.getTeamManager().noWinGame()
                        }
                    }
                    if(timeLeft == 18) {
                        for(player in Bukkit.getOnlinePlayers()) {
                            player.sendMessage(Component.text("\nIndividual Cheese Collections:", NamedTextColor.WHITE, TextDecoration.BOLD))
                        }
                    }
                    if(timeLeft == 16) {
                        val sortedCollectedCheeseMap = game.getCheeseManager().getSortedCollectedCheeseMap()
                        var i = 1
                        sortedCollectedCheeseMap.forEach { (uuid, cheeseCollected) ->
                            for(player in Bukkit.getOnlinePlayers()) {
                                if(game.getTeamManager().isInRedTeam(uuid)) {
                                    player.sendMessage(Component.text("$i. ")
                                        .append(Component.text("${Bukkit.getPlayer(uuid)?.player?.name}", NamedTextColor.RED)
                                        .append(Component.text(" collected $cheeseCollected cheese.", NamedTextColor.WHITE))))
                                }
                                if(game.getTeamManager().isInBlueTeam(uuid)) {
                                    player.sendMessage(Component.text("$i. ")
                                        .append(Component.text("${Bukkit.getPlayer(uuid)?.player?.name}", NamedTextColor.BLUE)
                                        .append(Component.text(" collected $cheeseCollected cheese.", NamedTextColor.WHITE))))
                                }
                            }
                            i++
                        }
                    }
                    if (timeLeft <= 0) {
                        game.getPlayerManager().clearAllItems()
                        game.getPlayerManager().teleportPlayersToSpawn()
                        game.setTimerState(TimerState.INACTIVE)
                        game.getBlockManager().resetBarriers()
                        game.getPlayerManager().removeAllArrows()
                        cancelGameTask()
                    }
                }

                // Decrement timer by 1 if timer is active
                if (game.getTimerState() == TimerState.ACTIVE) {
                    timeLeft--
                }
            }
        }
        gameRunnable.runTaskTimer(Main.getPlugin(), 0, 20)
        currentGameTaskId = gameRunnable.taskId
        gameRunnableList[gameRunnable.taskId] = gameRunnable
    }

    private fun cancelGameTask() {
        gameRunnableList.remove(currentGameTaskId)?.cancel()
    }

    fun pauseCountdownTask() {
        game.setTimerState(TimerState.PAUSED)
    }

    fun resumeCountdownTask() {
        game.setTimerState(TimerState.ACTIVE)
    }

    fun setTimeLeft(setTimeLeft: Int) {
        setTimerTimeLeft = String.format("%02d:%02d", setTimeLeft / 60, setTimeLeft % 60)
        game.getInfoBoardManager().updateScoreboardTimer(setTimerTimeLeft!!, displayTime, game.getGameState())
        timeLeft = setTimeLeft
    }

    fun getTimeLeft() : Int {
        return timeLeft
    }

    fun resetVars() {
        timeLeft = 0
        previousTimeLeft = 0
        displayTime = "00:00"
        previousDisplayTime = "00:00"
        setTimerTimeLeft = null
    }
}