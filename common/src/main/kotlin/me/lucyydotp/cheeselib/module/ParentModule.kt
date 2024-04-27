package me.lucyydotp.cheeselib.module

import me.lucyydotp.cheeselib.inject.CascadingInjectionContext
import me.lucyydotp.cheeselib.inject.InjectionContext

/**
 * A module that can hold child modules.
 * Child module states are kept in sync with this module's state. When this module is enabled:
 *
 * - The parent module moves from [Module.State.Disabled] to [Module.State.Enabling]
 * - Each child module is enabled, in no defined order, such that it reaches [Module.State.Enabled]
 * - The parent module moves to [Module.State.Enabled]
 *
 * A similar process happens on disable.
 *
 * Parent modules also implement [InjectionContext], which defers to the parent if no value is bound to a type.
 */
open class ParentModule(parent: ModuleHolder) :
    Module(parent),
    InjectionContext by CascadingInjectionContext(parent),
    ModuleHolder {

    private val _modules = mutableSetOf<Module>()
    override val modules: Set<Module>
        get() = _modules

    init {
        onEnable {
            _modules.forEach(Module::enable)
        }

        onDisable {
            _modules.forEach(Module::disable)
        }
    }

    /**
     * Registers a module as a child of this module, enabling it if this module is already enabled.
     * @throws IllegalArgumentException if [module] has a parent that is not this module, or is already enabled
     * @return [module]
     */
    protected fun <T : Module> register(module: T) = module.also {
        require(module.parent === this) { "Module is registered to a different parent" }
        require(module.state == State.Disabled) { "Cannot register an already enabled module" }
        if (this.isEnabled) module.enable()
        _modules += module
    }

    /**
     * Unregisters a module as a child of this module, disabling it if it's enabled.
     * @throws IllegalArgumentException if the module has a different parent
     */
    protected fun unregister(module: Module) {
        require(module.parent === this && module in modules) { "Cannot unregister a module that isn't registered" }
        _modules -= module
        if (module.isEnabled) module.disable()
    }

    protected fun <T : Module> T.registerAsChild() = register(this)
}
