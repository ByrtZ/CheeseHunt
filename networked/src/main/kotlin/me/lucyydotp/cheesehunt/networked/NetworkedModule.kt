package me.lucyydotp.cheesehunt.networked

import me.lucyydotp.cheesehunt.networked.format.PermissionGroupNameFormat
import me.lucyydotp.cheeselib.inject.bindGlobally
import me.lucyydotp.cheeselib.module.ModuleHolder
import me.lucyydotp.cheeselib.module.ParentModule
import me.lucyydotp.cheeselib.sys.AdminMessages

class NetworkedModule(parent: ModuleHolder) : ParentModule(parent) {
    init {
        PermissionGroupNameFormat(this).registerAsChild()
        AdminMessages(this) { it.hasPermission("cheesehunt.dev") }.registerAsChild().bindGlobally()
    }
}
