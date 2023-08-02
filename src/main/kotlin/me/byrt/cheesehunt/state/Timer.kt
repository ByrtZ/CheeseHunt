package me.byrt.cheesehunt.state

import me.byrt.cheesehunt.game.Game

@Suppress("unused")
class Timer(private val game : Game) {
    private var timerState = TimerState.INACTIVE

    fun setTimerState(newState : TimerState) {
        if(newState == this.timerState) return
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