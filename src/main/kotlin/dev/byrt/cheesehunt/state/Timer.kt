package dev.byrt.cheesehunt.state

import dev.byrt.cheesehunt.game.Game

class Timer(private val game : Game) {
    private var timerState = TimerState.INACTIVE

    fun setTimerState(newState : TimerState) {
        if(newState == timerState) return
        this.timerState = newState
    }

    fun getTimerState() : TimerState {
        return this.timerState
    }
}

enum class TimerState {
    ACTIVE,
    INACTIVE,
    PAUSED
}