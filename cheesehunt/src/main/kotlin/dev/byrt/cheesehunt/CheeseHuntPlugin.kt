package dev.byrt.cheesehunt

import me.lucyydotp.cheeselib.inject.CascadingInjectionContext
import me.lucyydotp.cheeselib.inject.GlobalInjectionContext
import me.lucyydotp.cheeselib.inject.InjectionContext
import me.lucyydotp.cheeselib.inject.bind
import me.lucyydotp.cheeselib.plugin.ModuleEntrypointPlugin
import org.slf4j.Logger


class CheeseHuntPlugin : ModuleEntrypointPlugin<CheeseHunt>(), InjectionContext by CascadingInjectionContext(GlobalInjectionContext) {

    init {
        bind<Logger>(slF4JLogger)
    }

    override val module: CheeseHunt = CheeseHunt(this)

    public companion object {
        private lateinit var instance: CheeseHuntPlugin

        @Deprecated("Inject through contextual DI.")
        public fun getInstance(): CheeseHuntPlugin = instance
    }
}
