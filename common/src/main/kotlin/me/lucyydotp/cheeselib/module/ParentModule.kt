package me.lucyydotp.cheeselib.module

import me.lucyydotp.cheeselib.inject.CascadingInjectionContext
import me.lucyydotp.cheeselib.inject.InjectionContext

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

    protected fun register(module: Module) {
        require(module.parent == this) { "Module is registered to a different parent" }
        module.parent != this
        _modules += module
    }
}
