package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.state.Teams

@Suppress("unused")
class ScoreManager(private val game : Game) {
    private var redScore = 0
    private var blueScore = 0
    private var previousRedScore = 0
    private var previousBlueScore = 0
    private var placements = ArrayList<Teams>()
    private var previousPlacements = ArrayList<Teams>()

    fun calcPlacements() : ArrayList<Teams>? {
        previousPlacements = placements
        placements.clear()
        if(redScore > blueScore) {
            placements.add(0, Teams.RED)
            placements.add(1, Teams.BLUE)
            return placements
        }
        if(blueScore > redScore) {
            placements.add(0, Teams.BLUE)
            placements.add(1, Teams.RED)
            return placements
        }
        return null
    }

    fun modifyScore(score : Int, mode : ScoreMode, team : Teams) {
        when(mode) {
            ScoreMode.ADD -> {
                if(team == Teams.RED) {
                    previousRedScore = redScore
                    redScore += score
                }
                if(team == Teams.BLUE) {
                    previousBlueScore = blueScore
                    blueScore += score
                }
            }
            ScoreMode.SUB -> {
                if(team == Teams.RED) redScore -= score
                if(team == Teams.BLUE) blueScore -= score
            }
        }
    }

    fun getRedScore() : Int {
        return redScore
    }

    fun getBlueScore() : Int {
        return blueScore
    }

    fun getPreviousRedScore() : Int {
        return previousRedScore
    }

    fun getPreviousBlueScore() : Int {
        return previousBlueScore
    }

    fun resetScores() {
        redScore = 0
        blueScore = 0
        previousRedScore = 0
        previousBlueScore = 0
    }
}

enum class ScoreMode {
    ADD,
    SUB
}