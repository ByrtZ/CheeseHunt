package dev.byrt.cheesehunt.standalone

import me.lucyydotp.cheeselib.plugin.ModuleEntrypointPlugin

class StandalonePlugin : ModuleEntrypointPlugin<StandaloneModule>() {
    override val module = StandaloneModule(this)
}
