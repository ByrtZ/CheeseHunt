package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.state.Teams

@Suppress("unused")
class ScoreManager(private val game : Game) {
    private var redScore = 0
    private var blueScore = 0

    fun getRedScore() : Int {
        return redScore
    }

    fun getBlueScore() : Int {
        return blueScore
    }

    fun modifyScore(score : Int, mode : ScoreMode, team : Teams) {
        when(mode) {
            ScoreMode.ADD -> {
                if(team == Teams.RED) redScore += score
                if(team == Teams.BLUE) blueScore += score
            }
            ScoreMode.SUB -> {
                if(team == Teams.RED) redScore -= score
                if(team == Teams.BLUE) blueScore -= score
            }
        }
    }
}

enum class ScoreMode {
    ADD,
    SUB
}