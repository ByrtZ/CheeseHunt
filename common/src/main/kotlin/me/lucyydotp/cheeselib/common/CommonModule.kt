package me.lucyydotp.cheeselib.common

import me.lucyydotp.cheeselib.inject.GlobalInjectionContext
import me.lucyydotp.cheeselib.inject.bind
import me.lucyydotp.cheeselib.module.ParentModule
import org.bukkit.Server

class CommonModule(parent: CommonPlugin) : ParentModule(parent) {

    init {
        GlobalInjectionContext.bind<Server>(parent.server)
    }
}
