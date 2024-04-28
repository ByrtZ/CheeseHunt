package me.lucyydotp.cheeselib.common

import me.lucyydotp.cheeselib.plugin.ModuleEntrypointPlugin

class CommonPlugin : ModuleEntrypointPlugin<CommonModule>() {
    override val module: CommonModule = CommonModule(this)
}
