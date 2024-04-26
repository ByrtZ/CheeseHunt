package dev.byrt.cheesehunt.state

import dev.byrt.cheesehunt.game.Game

class Rounds(private val game : Game) {
    private var roundState = RoundState.ONE
    private var totalRounds = 1

    fun nextRound() {
        when(roundState) {
            RoundState.ONE -> { setRoundState(RoundState.TWO) }
            RoundState.TWO -> { setRoundState(RoundState.THREE) }
            RoundState.THREE -> { setRoundState(RoundState.FOUR) }
            RoundState.FOUR -> { setRoundState(RoundState.FIVE) }
            RoundState.FIVE -> { game.plugin.logger.warning("Attempted to increment past Round 5.") }
        }
    }

    fun setRoundState(newRound : RoundState) {
        if(newRound == roundState) return
    }

    fun getRoundState() : RoundState {
        return roundState
    }

    fun getTotalRounds() : Int {
        return totalRounds
    }
}

enum class RoundState {
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE
}