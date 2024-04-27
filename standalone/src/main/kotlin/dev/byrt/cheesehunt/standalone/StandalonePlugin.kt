package dev.byrt.cheesehunt.standalone

import me.lucyydotp.cheeselib.inject.CascadingInjectionContext
import me.lucyydotp.cheeselib.inject.GlobalInjectionContext
import me.lucyydotp.cheeselib.inject.InjectionContext
import me.lucyydotp.cheeselib.plugin.ModuleEntrypointPlugin

class StandalonePlugin : ModuleEntrypointPlugin<StandaloneModule>(),
    InjectionContext by CascadingInjectionContext(GlobalInjectionContext) {
    override val module = StandaloneModule(this)

}
