package me.lucyydotp.cheeselib.common

import me.lucyydotp.cheeselib.inject.GlobalInjectionContext
import me.lucyydotp.cheeselib.inject.bind
import me.lucyydotp.cheeselib.module.ParentModule
import org.bukkit.Server

class CommonModule(private val plugin: CommonPlugin) : ParentModule(plugin) {

    init {
        GlobalInjectionContext.bind<Server>(plugin.server)
        Commands(this).registerAsChild()
    }
}
