package dev.byrt.cheesehunt.standalone

import dev.byrt.cheesehunt.standalone.format.OpNameFormat
import me.lucyydotp.cheeselib.module.ModuleHolder
import me.lucyydotp.cheeselib.module.ParentModule

class StandaloneModule(parent: ModuleHolder) : ParentModule(parent) {
    init {
        OpNameFormat(this).registerAsChild()
    }
}
