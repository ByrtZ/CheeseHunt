package me.lucyydotp.cheeselib.plugin

import me.lucyydotp.cheeselib.inject.CascadingInjectionContext
import me.lucyydotp.cheeselib.inject.GlobalInjectionContext
import me.lucyydotp.cheeselib.inject.InjectionContext
import me.lucyydotp.cheeselib.inject.bind
import me.lucyydotp.cheeselib.module.Module
import me.lucyydotp.cheeselib.module.ModuleHolder
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

abstract class ModuleEntrypointPlugin<T> :
    JavaPlugin(),
    ModuleHolder,
    InjectionContext by CascadingInjectionContext(GlobalInjectionContext)
        where T : Module, T : InjectionContext {
    abstract val module: T

    init {
        bind<Plugin>(this)
    }

    override val modules: Set<Module>
        get() = setOf(module)

    override fun onEnable() {
        module.enable()
    }

    override fun onDisable() {
        module.disable()
    }
}
