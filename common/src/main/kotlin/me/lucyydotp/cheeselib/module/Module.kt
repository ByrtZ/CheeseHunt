package me.lucyydotp.cheeselib.module

import me.lucyydotp.cheeselib.inject.InjectionSite
import org.bukkit.Bukkit

/**
 * A component that can be enabled and disabled.
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

    /**
     * Defines an action to run every time the module is enabled.
     */
    fun onEnable(action: () -> Unit) {
        enableActions.add(action)
        if (isEnabled) action()
    }

    /**
     * Defines an action to run every time the module is disabled.
     */
    fun onDisable(action: () -> Unit) {
        disableActions.add(action)
    }

    /** Listens for an event while the module is active. */
    fun <T : Any> listen(eventEmitter: EventEmitter<T>, priority: Int = 100, handler: (T) -> Unit) {
        var subscription: EventEmitter.Subscription<T>? = null
        onEnable {
            subscription = eventEmitter.subscribe(handler, priority)
        }
        onDisable {
            subscription?.unsubscribe()
        }
    }

    /**
     * The module's current state.
     */
    var state: State = State.Disabled
        private set

    val isEnabled: Boolean
        get() = state == State.Enabled

    internal fun enable() {
        require(state == State.Disabled) { "Cannot enable an already enabled module" }
        Bukkit.getLogger().info("Enabling module ${this::class.simpleName}")
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
