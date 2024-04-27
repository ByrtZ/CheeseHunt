package me.lucyydotp.cheeselib.common

import me.lucyydotp.cheeselib.inject.CascadingInjectionContext
import me.lucyydotp.cheeselib.inject.GlobalInjectionContext
import me.lucyydotp.cheeselib.inject.InjectionContext
import me.lucyydotp.cheeselib.plugin.ModuleEntrypointPlugin

class CommonPlugin : ModuleEntrypointPlugin<CommonModule>(),
    InjectionContext by CascadingInjectionContext(GlobalInjectionContext) {
    override val module: CommonModule = CommonModule(this)
}
