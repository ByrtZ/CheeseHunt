package me.lucyydotp.cheeselib.module

import me.lucyydotp.cheeselib.inject.InjectionSite

/**
 * A module.
 */
abstract class Module(val parent: ModuleHolder): InjectionSite by parent {

    enum class State {
        Enabled,
        Enabling,
        Disabling,
        Disabled,
    }

    private val enableActions = mutableListOf<() -> Unit>()
    private val disableActions = mutableListOf<() -> Unit>()

    fun onEnable(action: () -> Unit) {
        enableActions.add(action)
    }

    fun onDisable(action: () -> Unit) {
        enableActions.add(action)
    }

    var state: State = State.Disabled
        private set

    val isEnabled: Boolean
        get() = state == State.Enabled

    internal fun enable() {
        require(state == State.Disabled) { "Cannot enable an already enabled module" }
        state = State.Enabling
        enableActions.forEach { it() }
        state = State.Enabled
    }

    internal fun disable() {
        require(isEnabled) { "Cannot disable a module that's not enabled" }
        state = State.Disabling
        disableActions.forEach { it() }
        state = State.Disabled
    }
}
