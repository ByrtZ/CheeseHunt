package me.lucyydotp.cheeselib.plugin

import me.lucyydotp.cheeselib.inject.InjectionContext
import me.lucyydotp.cheeselib.inject.bind
import me.lucyydotp.cheeselib.module.Module
import me.lucyydotp.cheeselib.module.ModuleHolder
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

abstract class ModuleEntrypointPlugin<T> : JavaPlugin(), ModuleHolder where T : Module, T : InjectionContext {
    protected abstract val module: T

    override val modules: Set<Module>
        get() = setOf(module)

    override fun onEnable() {
        module.bind<Plugin>(this)
        module.enable()
    }

    override fun onDisable() {
        module.disable()
    }
}
