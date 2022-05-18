package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.Main

class CheeseManager(private var game : Game) {
    private var redTotalCheesePlaced = 0
    private var blueTotalCheesePlaced = 0
    private var redTotalCheeseCollected = 0
    private var blueTotalCheeseCollected = 0
    private var redFinishedPlacing = false
    private var blueFinishedPlacing = false
    private var redFinishedCollecting = false
    private var blueFinishedCollecting = false

    fun incrementCheesePlaced(team : Team) {
        when(team) {
            Team.RED -> {
                redTotalCheesePlaced += 1
            }
            Team.BLUE -> {
                blueTotalCheesePlaced += 1
            }
            Team.SPECTATOR -> {
                Main.getPlugin().logger.info("[INCREMENTING ERROR] Game attempted to increment cheese placed for spectators.")
            }
        }
    }

    fun incrementCheeseCollected(team : Team) {
        when(team) {
            Team.RED -> {
                redTotalCheeseCollected += 1
            }
            Team.BLUE -> {
                blueTotalCheeseCollected += 1
            }
            Team.SPECTATOR -> {
                Main.getPlugin().logger.info("[INCREMENTING ERROR] Game attempted to increment cheese collected for specators.")
            }
        }
    }

    fun getRedCheesePlaced() : Int {
        return redTotalCheesePlaced
    }

    fun getBlueCheesePlaced() : Int {
        return blueTotalCheesePlaced
    }

    fun getRedCheeseCollected() : Int {
        return redTotalCheeseCollected
    }

    fun getBlueCheeseCollected() : Int {
        return blueTotalCheeseCollected
    }

    fun hasRedFinishedPlacing() : Boolean {
        return redFinishedPlacing
    }

    fun setRedFinishedPlacing(state : Boolean) {
        redFinishedPlacing = state
    }

    fun hasBlueFinishedPlacing() : Boolean {
        return blueFinishedPlacing
    }

    fun setBlueFinishedPlacing(state : Boolean) {
        blueFinishedPlacing = state
    }

    fun hasRedFinishedCollecting() : Boolean {
        return redFinishedCollecting
    }

    fun setRedFinishedCollecting(state : Boolean) {
        redFinishedCollecting = state
    }

    fun hasBlueFinishedCollecting() : Boolean {
        return blueFinishedCollecting
    }

    fun setBlueFinishedCollecting(state : Boolean) {
        blueFinishedCollecting = state
    }
}