package me.lucyydotp.cheesehunt.networked.format

import me.lucyydotp.cheeselib.inject.context
import me.lucyydotp.cheeselib.module.Module
import me.lucyydotp.cheeselib.module.ModuleHolder
import me.lucyydotp.cheeselib.sys.nameformat.NameFormatter
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.luckperms.api.LuckPermsProvider

class PermissionGroupNameFormat(parent: ModuleHolder) : Module(parent) {
    private val nameFormatter by context<NameFormatter>()

    val luckperms by lazy(LuckPermsProvider::get)

    init {
        listen(nameFormatter.format, 150) {
            val lpUser = luckperms.userManager.getUser(it.player.uniqueId) ?: return@listen

            lpUser.cachedData.metaData.getMetaValue("badge")?.let { badge ->
                it.prefixes = buildList {
                        add(Component.text(badge))
                    addAll(it.prefixes)
                }
            }
            lpUser.cachedData.metaData.getMetaValue("color")?.let { badge ->
                it.usernameStyle = it.usernameStyle.colorIfAbsent(TextColor.fromHexString(badge))
            }
        }
    }
}
