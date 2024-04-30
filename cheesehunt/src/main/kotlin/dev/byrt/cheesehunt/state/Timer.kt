package dev.byrt.cheesehunt.state

import dev.byrt.cheesehunt.game.GameManager
import dev.byrt.cheesehunt.game.GameState
import me.lucyydotp.cheeselib.inject.context
import me.lucyydotp.cheeselib.module.Module
import me.lucyydotp.cheeselib.module.ModuleHolder
import me.lucyydotp.cheeselib.sys.AdminMessageStyles
import me.lucyydotp.cheeselib.sys.AdminMessages

class Timer(parent : ModuleHolder) : Module(parent) {
    private val adminMessages: AdminMessages by context()
    private val gameManager: GameManager by context()

    private var timerState = TimerState.INACTIVE

    init {
        listen(gameManager.onStateChange) {
            setTimerState(when(it) {
                GameState.IDLE -> TimerState.INACTIVE
                else -> TimerState.ACTIVE
            })
        }
    }

    fun setTimerState(newState : TimerState) {
        if(newState == timerState) return
        adminMessages.sendDevMessage("Timer is now $newState", AdminMessageStyles.INFO)
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
