package dev.byrt.cheesehunt

import me.lucyydotp.cheeselib.plugin.ModuleEntrypointPlugin


class CheeseHuntPlugin : ModuleEntrypointPlugin<CheeseHunt>() {
    override val module: CheeseHunt = CheeseHunt(this)

    public companion object {
        private lateinit var instance: CheeseHuntPlugin

        @Deprecated("Inject through contextual DI.")
        public fun getInstance(): CheeseHuntPlugin = instance
    }
}
