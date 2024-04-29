package dev.byrt.cheesehunt.command

import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission
import dev.byrt.cheesehunt.game.GameManager
import dev.byrt.cheesehunt.game.GameState
import dev.byrt.cheesehunt.state.Sounds
import me.lucyydotp.cheeselib.inject.context
import me.lucyydotp.cheeselib.module.Module
import me.lucyydotp.cheeselib.module.ModuleHolder
import me.lucyydotp.cheeselib.module.installCommands
import me.lucyydotp.cheeselib.sys.AdminMessageStyles
import me.lucyydotp.cheeselib.sys.AdminMessages
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class BuildMode(parent: ModuleHolder) : Module(parent) {
    private val buildToggleSuccessSound: Sound =
        Sound.sound(Key.key(Sounds.Command.BUILDMODE_SUCCESS), Sound.Source.MASTER, 1f, 1f)
    private val buildToggleFailSound: Sound =
        Sound.sound(Key.key(Sounds.Command.BUILDMODE_FAIL), Sound.Source.MASTER, 1f, 0f)

    private val adminMessages: AdminMessages by context()
    private val gameManager: GameManager by context()

    var buildModeEnabled: Boolean = false

    init {
        installCommands()
    }

    @CommandMethod("buildmode")
    @CommandDescription("Toggles the ability to build for operators while not in game.")
    @CommandPermission("cheesehunt.buildmode")
    fun start(sender: Player) {
        if (gameManager.getGameState() == GameState.IDLE) {
            if (buildModeEnabled) {
                for (player in Bukkit.getOnlinePlayers()) {
                    if (player.isOp) {
                        player.playSound(buildToggleSuccessSound)
                    }
                }
               adminMessages.sendDevMessage("Building disabled by ${sender.name}.", AdminMessageStyles.INFO_FAIL)
                buildModeEnabled = false
            } else {
                for (player in Bukkit.getOnlinePlayers()) {
                    if (player.isOp) {
                        player.playSound(buildToggleSuccessSound)
                    }
                }
                adminMessages.sendDevMessage("Building enabled by ${sender.name}.", AdminMessageStyles.INFO_SUCCESS)
                buildModeEnabled = true
            }
        } else {
            sender.sendMessage(
                Component.text("You can only toggle Build Mode when the game is idle.").color(NamedTextColor.RED)
            )
            sender.playSound(buildToggleFailSound)
        }
    }
}
