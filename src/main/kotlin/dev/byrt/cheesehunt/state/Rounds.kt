package dev.byrt.cheesehunt.state

import dev.byrt.cheesehunt.game.Game

@Suppress("unused")
class Rounds(private val game : Game) {
    private var roundState = RoundState.ONE
    private var totalRounds = 1

    fun nextRound() {
        when(roundState) {
            RoundState.ONE -> { setRoundState(RoundState.TWO) }
            RoundState.TWO -> { setRoundState(RoundState.THREE) }
            RoundState.THREE -> { setRoundState(RoundState.FOUR) }
            RoundState.FOUR -> { setRoundState(RoundState.FIVE) }
            RoundState.FIVE -> {}
        }
    }

    fun setRoundState(newRound : RoundState) {
        if(newRound == this.roundState) return
    }

    fun getRoundState() : RoundState {
        return this.roundState
    }

    fun getTotalRounds() : Int {
        return this.totalRounds
    }
}

enum class RoundState {
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE
}