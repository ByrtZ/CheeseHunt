package me.byrt.cheesehunt.task

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.manager.*

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.SoundCategory
import org.bukkit.scheduler.BukkitRunnable

import java.time.Duration

class GameCountdownTask(private var game: Game) : BukkitRunnable() {
    private var timeLeft = 0
    private var previousTimeLeft = 0
    private var displayTime: String = "00:00"
    private var previousDisplayTime: String = "00:00"
    private var setTimerTimeLeft: String? = null

    override fun run() {
        // Formatting variables
        previousTimeLeft = timeLeft + 1
        displayTime = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60)
        previousDisplayTime = String.format("%02d:%02d", previousTimeLeft / 60, previousTimeLeft % 60)

        // Update scoreboard timer
        game.getInfoBoardManager().updateScoreboardTimer(displayTime, previousDisplayTime, game.getGameState())

        // Game/round starting front end
        if (game.getGameState() == GameState.STARTING && game.getTimerState() == TimerState.ACTIVE) {
            if(game.getRoundState() == RoundState.ROUND_ONE) { // Game tutorial and round one starting titles
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
                        player.playSound(player.location, "entity.item.pickup", 1f, 1f)
                        player.sendMessage(Component.text("-----------------------------------------------------\n\n").color(NamedTextColor.GREEN).decoration(TextDecoration.STRIKETHROUGH, true)
                            .append(Component.text(" Welcome to Cheese Hunt!\n\n").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.STRIKETHROUGH, false)
                            .append(Component.text(" Here's how to play...").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, true).decoration(TextDecoration.BOLD, false)
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
                        player.playSound(player.location, "entity.item.pickup", 1f, 1f)
                        player.sendMessage(Component.text("-----------------------------------------------------").color(NamedTextColor.GREEN).decoration(TextDecoration.STRIKETHROUGH, true)
                            .append(Component.text(" Rounds:\n").color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.STRIKETHROUGH, false)
                            .append(Component.text(" • Cheese Hunt is a two round game.\n").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, false)
                            .append(Component.text(" • Round 1 consists of you and your team hiding Cheese.\n").color(NamedTextColor.WHITE)
                            .append(Component.text(" • Round 2 consists of you and your team hunting enemy Cheese.\n").color(NamedTextColor.WHITE)
                            .append(Component.text(" • You switch to the enemy map to hunt Cheese in Round 2.\n\n\n").color(NamedTextColor.WHITE)
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
                        player.playSound(player.location, "entity.item.pickup", 1f, 1f)
                        player.sendMessage(Component.text("-----------------------------------------------------").color(NamedTextColor.GREEN).decoration(TextDecoration.STRIKETHROUGH, true)
                            .append(Component.text(" Items:\n").color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.STRIKETHROUGH, false)
                            .append(Component.text(" • You will be given 4 pieces of Cheese to hide in your team's map during Round 1.\n").color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false)
                            .append(Component.text(" • You will be given a Cheese Harvester 1000 to hunt Cheese in the enemy's map during Round 2.\n").color(NamedTextColor.WHITE)
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
                        player.playSound(player.location, "entity.item.pickup", 1f, 1f)
                        player.sendMessage(Component.text("-----------------------------------------------------").color(NamedTextColor.GREEN).decoration(TextDecoration.STRIKETHROUGH, true)
                            .append(Component.text(" Win Criteria:\n").color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.STRIKETHROUGH, false)
                            .append(Component.text(" • In order to win, you must hunt the Cheese faster in the enemy's map, than the enemy can in your team's map.\n\n").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, false)
                            .append(Component.text(" • If both teams fail to collect all Cheese in their maps, the team that has collected the most Cheese will win.").color(NamedTextColor.RED)
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
                        player.playSound(player.location, "entity.item.pickup", 1f, 1f)
                        player.sendMessage(Component.text("-----------------------------------------------------").color(NamedTextColor.GREEN).decoration(TextDecoration.STRIKETHROUGH, true)
                        .append(Component.text(" Starting soon:\n\n").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true).decoration(TextDecoration.STRIKETHROUGH, false)
                        .append(Component.text(" • Standby for the game to begin...\n\n").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, false)
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
                            Component.text("Hiding starts in").color(NamedTextColor.AQUA),
                            Component.text("►$timeLeft◄").decoration(TextDecoration.BOLD, true),
                            Title.Times.times(
                                Duration.ofSeconds(0),
                                Duration.ofSeconds(5),
                                Duration.ofSeconds(0)
                                )
                            )
                        )
                        player.playSound(player.location, "block.note_block.bass", 0.75f, 1f)
                    }
                }
                if (timeLeft == 3 || timeLeft == 2 || timeLeft == 1) {
                    for (player in Bukkit.getOnlinePlayers()) {
                        player.playSound(player.location, "block.note_block.pling", 1f, 1f)
                        if (timeLeft == 3) {
                            player.showTitle(Title.title(
                                Component.text("Hiding starts in").color(NamedTextColor.AQUA),
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
                                Component.text("Hiding starts in").color(NamedTextColor.AQUA),
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
                                Component.text("Hiding starts in").color(NamedTextColor.AQUA),
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
            } else if(game.getRoundState() == RoundState.ROUND_TWO) { // Round two starting titles
                if (timeLeft in 4..10) {
                    for (player in Bukkit.getOnlinePlayers()) {
                        player.showTitle(Title.title(
                            Component.text("Hunting starts in").color(NamedTextColor.AQUA),
                            Component.text("►$timeLeft◄").decoration(TextDecoration.BOLD, true),
                            Title.Times.times(
                                Duration.ofSeconds(0),
                                Duration.ofSeconds(5),
                                Duration.ofSeconds(0)
                                )
                            )
                        )
                        player.playSound(player.location, "block.note_block.bass", 0.75f, 1f)
                    }
                }
                if (timeLeft == 3 || timeLeft == 2 || timeLeft == 1) {
                    for (player in Bukkit.getOnlinePlayers()) {
                        player.playSound(player.location, "block.note_block.pling", 1f, 1f)
                        if (timeLeft == 3) {
                            player.showTitle(Title.title(
                                Component.text("Hunting starts in").color(NamedTextColor.AQUA),
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
                                Component.text("Hunting starts in").color(NamedTextColor.AQUA),
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
                                Component.text("Hunting starts in").color(NamedTextColor.AQUA),
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
                    player.playSound(player.location, "block.note_block.pling", 1f, 1f)
                    player.playSound(player.location, "block.note_block.pling", 1f, 2f)
                    player.playSound(player.location, "mcc.rocket_spleef",  SoundCategory.VOICE, 0.75f, 1f)
                    if(!game.getTeamManager().isSpectator(player.uniqueId)) {
                        player.gameMode = GameMode.SURVIVAL
                    }
                    player.resetTitle()
                }
                game.setGameState(GameState.IN_GAME)
            }
        }

        // <=30s remaining
        if (game.getGameState() == GameState.IN_GAME && game.getTimerState() == TimerState.ACTIVE) {
            if (timeLeft in 11..30 || timeLeft % 60 == 0) {
                for (player in Bukkit.getOnlinePlayers()) {
                    player.playSound(player.location, "block.note_block.bass", 0.75f, 1f)
                }
                if (timeLeft == 28) {
                    for (player in Bukkit.getOnlinePlayers()) {
                        player.playSound(player.location, "mcc.overtime", SoundCategory.VOICE, 0.75f, 1f)
                    }
                }
                if (timeLeft == 27) {
                    for (player in Bukkit.getOnlinePlayers()) {
                        player.stopSound("mcc.rocket_spleef", SoundCategory.VOICE)
                    }
                }
            }
            if (timeLeft in 0..10) {
                for (player in Bukkit.getOnlinePlayers()) {
                    player.playSound(player.location, "block.note_block.bass", 0.75f, 2f)
                }
            }
        }

        // Round ending front end
        if (timeLeft <= 0 && game.getGameState() == GameState.IN_GAME && game.getTimerState() == TimerState.ACTIVE) {
            if (game.getRoundState() == RoundState.ROUND_TWO) {
                for (player in Bukkit.getOnlinePlayers()) {
                    player.playSound(player.location, "block.note_block.pling", 1f, 1f)
                    player.playSound(player.location, "block.note_block.pling", 1f, 2f)
                    player.stopSound("mcc.rocket_spleef", SoundCategory.VOICE)
                    player.stopSound("mcc.overtime", SoundCategory.VOICE)
                    player.playSound(player.location, "mcc.overovertime", SoundCategory.VOICE, 0.75f, 1f)
                    player.showTitle(Title.title(
                        Component.text("Game Over!").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true),
                        Component.text(""),
                        Title.Times.times(
                            Duration.ofSeconds(0),
                            Duration.ofSeconds(4),
                            Duration.ofSeconds(1)
                            )
                        )
                    )
                    if(!game.getTeamManager().isSpectator(player.uniqueId)) {
                        player.gameMode = GameMode.ADVENTURE
                        player.allowFlight = true
                        player.isFlying = true
                    }
                }
                game.setGameState(GameState.GAME_END)
            } else {
                for (player in Bukkit.getOnlinePlayers()) {
                    player.playSound(player.location, "block.note_block.pling", 1f, 1f)
                    player.playSound(player.location, "block.note_block.pling", 1f, 2f)
                    player.stopSound("mcc.rocket_spleef", SoundCategory.VOICE)
                    player.stopSound("mcc.overtime", SoundCategory.VOICE)
                    player.showTitle(Title.title(
                        Component.text("Round Over!").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true),
                        Component.text(""),
                        Title.Times.times(
                            Duration.ofSeconds(0),
                            Duration.ofSeconds(4),
                            Duration.ofSeconds(1)
                            )
                        )
                    )
                    if(!game.getTeamManager().isSpectator(player.uniqueId)) {
                        player.gameMode = GameMode.ADVENTURE
                        player.allowFlight = true
                        player.isFlying = true
                    }
                }
                game.setGameState(GameState.ROUND_END)
            }
        }

        // Increment round if round one ended
        if (game.getGameState() == GameState.ROUND_END && game.getTimerState() == TimerState.ACTIVE) {
            if (timeLeft <= 0) {
                if (game.getRoundState() != RoundState.ROUND_TWO) {
                    when (game.getRoundState()) {
                        RoundState.ROUND_ONE -> {
                            game.setRoundState(RoundState.ROUND_TWO)
                            game.setGameState(GameState.STARTING)
                        }
                        RoundState.ROUND_TWO -> {
                            Main.getPlugin().logger.info("[COUNTDOWN TASK ERROR] Something weird happened with the round end statements")
                        }
                    }
                }
            }
        }

        // Game end cycle
        if (game.getGameState() == GameState.GAME_END && game.getTimerState() == TimerState.ACTIVE) {
            if(timeLeft == 80) {
                if(!game.getCheeseManager().hasRedFinishedCollecting() || !game.getCheeseManager().hasBlueFinishedCollecting()) {
                    for(player in Bukkit.getOnlinePlayers()) {
                        player.sendMessage(Component.text("The game ended with lost cheese...").color(NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true))
                        player.sendMessage(Component.text("Now showing all uncollected cheese.").color(NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true))
                    }
                    game.getCheeseManager().markUncollectedCheese()
                }
            }
            if(timeLeft == 70) {
                for(player in Bukkit.getOnlinePlayers()) {
                    player.sendMessage(Component.text("\nTeam Cheese Collections:").decoration(TextDecoration.BOLD, true))
                }
            }
            if(timeLeft == 68) {
                for(player in Bukkit.getOnlinePlayers()) {
                    player.sendMessage(Component.text("Red Team ").color(NamedTextColor.RED)
                        .append(Component.text("collected ${game.getCheeseManager().getRedCheeseCollected()}/${game.getCheeseManager().getBlueCheesePlaced()} cheese.").color(NamedTextColor.WHITE))
                    )
                    player.sendMessage(Component.text("Blue Team ").color(NamedTextColor.BLUE)
                        .append(Component.text("collected ${game.getCheeseManager().getBlueCheeseCollected()}/${game.getCheeseManager().getRedCheesePlaced()} cheese.").color(NamedTextColor.WHITE))
                    )
                }
            }
            if(timeLeft == 67) {
                for(player in Bukkit.getOnlinePlayers()) {
                    if(game.getCheeseManager().getRedCheeseCollected() > game.getCheeseManager().getBlueCheeseCollected() && !game.getCheeseManager().hasRedFinishedCollecting() && !game.getCheeseManager().hasBlueFinishedCollecting()) {
                        if(Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.RED) {
                            player.playSound(player.location, "ui.toast.challenge_complete", 1f, 1f)
                            game.getCheeseManager().teamWinFireworks(player, Teams.RED)
                            player.sendMessage(Component.text("\nYour team won the game!\n").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true))
                            player.showTitle(
                                Title.title(
                                    Component.text("Your team won!").color(NamedTextColor.GREEN),
                                    Component.text("Well done!").color(NamedTextColor.GREEN),
                                    Title.Times.times(
                                        Duration.ofSeconds(1),
                                        Duration.ofSeconds(5),
                                        Duration.ofSeconds(1)
                                    )
                                )
                            )
                        }
                        if(Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.BLUE) {
                            player.playSound(player.location, "entity.ender_dragon.growl", 1f, 1f)
                            player.sendMessage(Component.text("\nYour team lost the game!\n").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true))
                            player.showTitle(
                                Title.title(
                                    Component.text("Your team lost!").color(NamedTextColor.RED),
                                    Component.text("Git gud!").color(NamedTextColor.RED),
                                    Title.Times.times(
                                        Duration.ofSeconds(1),
                                        Duration.ofSeconds(5),
                                        Duration.ofSeconds(1)
                                    )
                                )
                            )
                        }
                    } else if(game.getCheeseManager().getRedCheeseCollected() < game.getCheeseManager().getBlueCheeseCollected() && !game.getCheeseManager().hasRedFinishedCollecting() && !game.getCheeseManager().hasBlueFinishedCollecting()) {
                        if(Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.BLUE) {
                            player.playSound(player.location, "ui.toast.challenge_complete", 1f, 1f)
                            game.getCheeseManager().teamWinFireworks(player, Teams.BLUE)
                            player.sendMessage(Component.text("\nYour team won the game!\n").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true))
                            player.showTitle(
                                Title.title(
                                    Component.text("Your team won!").color(NamedTextColor.GREEN),
                                    Component.text("Well done!").color(NamedTextColor.GREEN),
                                    Title.Times.times(
                                        Duration.ofSeconds(1),
                                        Duration.ofSeconds(5),
                                        Duration.ofSeconds(1)
                                    )
                                )
                            )
                        }
                        if(Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.RED) {
                            player.playSound(player.location, "entity.ender_dragon.growl", 1f, 1f)
                            player.sendMessage(Component.text("\nYour team lost the game!\n").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true))
                            player.showTitle(
                                Title.title(
                                    Component.text("Your team lost!").color(NamedTextColor.RED),
                                    Component.text("Git gud!").color(NamedTextColor.RED),
                                    Title.Times.times(
                                        Duration.ofSeconds(1),
                                        Duration.ofSeconds(5),
                                        Duration.ofSeconds(1)
                                    )
                                )
                            )
                        }
                    } else if(game.getCheeseManager().getRedCheeseCollected() == game.getCheeseManager().getBlueCheeseCollected() && !game.getCheeseManager().hasRedFinishedCollecting() && !game.getCheeseManager().hasBlueFinishedCollecting()) {
                        if(Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.RED || Main.getGame().getTeamManager().getPlayerTeam(player.uniqueId) == Teams.BLUE) {
                            player.playSound(player.location, "entity.wither.spawn", 1f, 2f)
                            player.sendMessage(Component.text("\nNo team won!\n").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true))
                            player.showTitle(
                                Title.title(
                                    Component.text("No team won the game!").color(NamedTextColor.YELLOW),
                                    Component.text("It was a draw.").color(NamedTextColor.YELLOW),
                                    Title.Times.times(
                                        Duration.ofSeconds(1),
                                        Duration.ofSeconds(5),
                                        Duration.ofSeconds(1)
                                    )
                                )
                            )
                        }
                    }
                }
            }
            if(timeLeft == 62) {
                for(player in Bukkit.getOnlinePlayers()) {
                    player.sendMessage(Component.text("\nIndividual Cheese Collections:").decoration(TextDecoration.BOLD, true))
                }
            }
            if(timeLeft == 60) {
                val sortedCollectedCheeseMap = game.getCheeseManager().getSortedCollectedCheeseMap()
                var i = 1
                sortedCollectedCheeseMap.forEach { (uuid, cheeseCollected) ->
                    for(player in Bukkit.getOnlinePlayers()) {
                        if(game.getTeamManager().isInRedTeam(uuid)) {
                            player.sendMessage(Component.text("$i. ")
                                .append(Component.text("${Bukkit.getPlayer(uuid)?.player?.name}").color(NamedTextColor.RED))
                                .append(Component.text(" collected $cheeseCollected cheese.").color(NamedTextColor.WHITE))
                            )
                        }
                        if(game.getTeamManager().isInBlueTeam(uuid)) {
                            player.sendMessage(Component.text("$i. ")
                                .append(Component.text("${Bukkit.getPlayer(uuid)?.player?.name}").color(NamedTextColor.BLUE))
                                .append(Component.text(" collected $cheeseCollected cheese.").color(NamedTextColor.WHITE))
                            )
                        }
                    }
                    i++
                }
            }
            if (timeLeft <= 0) {
                for (player in Bukkit.getOnlinePlayers()) { player.sendMessage(Component.text("\nThank you for playing!\nThe server will be restarting shortly.\n").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true)) }
                game.getBlockManager().resetBarriers()
                game.getPlayerManager().clearAllItems()
                game.getPlayerManager().teleportPlayersToSpawn()
                game.setTimerState(TimerState.INACTIVE)
                if(!game.getCheeseManager().hasRedFinishedCollecting() || !game.getCheeseManager().hasBlueFinishedCollecting()) {
                    game.getCheeseManager().clearUnmarkedCheeseMarkers()
                }
                this.cancel()
            }
        }

        // Decrement timer by 1 if timer is active
        if (game.getTimerState() == TimerState.ACTIVE) {
            timeLeft--
        }
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
}