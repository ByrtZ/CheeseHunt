package me.lucyydotp.cheesehunt.networked

import me.lucyydotp.cheesehunt.networked.format.PermissionGroupNameFormat
import me.lucyydotp.cheeselib.module.ModuleHolder
import me.lucyydotp.cheeselib.module.ParentModule

class NetworkedModule(parent: ModuleHolder) : ParentModule(parent) {
    init {
        PermissionGroupNameFormat(this).registerAsChild()
    }
}
