package dev.byrt.cheesehunt.standalone

import dev.byrt.cheesehunt.standalone.format.OpNameFormat
import dev.byrt.cheesehunt.standalone.visuals.InfoBoardManager
import me.lucyydotp.cheeselib.inject.bindGlobally
import me.lucyydotp.cheeselib.module.ModuleHolder
import me.lucyydotp.cheeselib.module.ParentModule
import me.lucyydotp.cheeselib.sys.AdminMessages
import org.bukkit.entity.Player

class StandaloneModule(parent: ModuleHolder) : ParentModule(parent) {
    init {
        OpNameFormat(this).registerAsChild()
        AdminMessages(this, Player::isOp).registerAsChild().bindGlobally()
        InfoBoardManager(this).registerAsChild()
    }
}
