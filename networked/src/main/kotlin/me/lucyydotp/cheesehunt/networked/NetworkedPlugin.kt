package me.lucyydotp.cheesehunt.networked

import me.lucyydotp.cheeselib.plugin.ModuleEntrypointPlugin

class NetworkedPlugin : ModuleEntrypointPlugin<NetworkedModule>() {
    override val module: NetworkedModule = NetworkedModule(this)
}
