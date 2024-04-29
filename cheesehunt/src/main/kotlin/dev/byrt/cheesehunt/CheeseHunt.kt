package dev.byrt.cheesehunt

import dev.byrt.cheesehunt.command.Announce
import dev.byrt.cheesehunt.command.CustomItem
import dev.byrt.cheesehunt.command.DebugCommands
import dev.byrt.cheesehunt.command.GameCommands
import dev.byrt.cheesehunt.command.GameMode
import dev.byrt.cheesehunt.command.MapCommands
import dev.byrt.cheesehunt.command.Particle
import dev.byrt.cheesehunt.command.Ping
import dev.byrt.cheesehunt.command.SetGameTitle
import dev.byrt.cheesehunt.command.SetSkin
import dev.byrt.cheesehunt.command.Timer
import dev.byrt.cheesehunt.command.Worlds
import dev.byrt.cheesehunt.event.AdvancementsRecipesEvent
import dev.byrt.cheesehunt.event.ArrowLandEvent
import dev.byrt.cheesehunt.event.BlockBreakDropEvent
import dev.byrt.cheesehunt.event.BlockPerishEvent
import dev.byrt.cheesehunt.event.BlockPlaceEvent
import dev.byrt.cheesehunt.event.DamageEvent
import dev.byrt.cheesehunt.event.HungerDepleteEvent
import dev.byrt.cheesehunt.event.ItemDamageEvent
import dev.byrt.cheesehunt.event.JoinQuitEvent
import dev.byrt.cheesehunt.event.PlayerDamageEvent
import dev.byrt.cheesehunt.event.PlayerDeathEvent
import dev.byrt.cheesehunt.event.PlayerInteractionsEvent
import dev.byrt.cheesehunt.event.PlayerInventoryInteractEvent
import dev.byrt.cheesehunt.event.PlayerItemEvent
import dev.byrt.cheesehunt.event.PlayerMovementEvent
import dev.byrt.cheesehunt.event.PlayerPotionEffectEvent
import dev.byrt.cheesehunt.event.PlayerResourcePackLoadEvent
import dev.byrt.cheesehunt.event.PlayerResurrectEvent
import dev.byrt.cheesehunt.event.ServerListEvent
import dev.byrt.cheesehunt.event.TNTExplodeEvent
import dev.byrt.cheesehunt.event.UseCustomItemEvent
import dev.byrt.cheesehunt.game.Game
import dev.byrt.cheesehunt.manager.Maps
import me.lucyydotp.cheeselib.common.Commands
import me.lucyydotp.cheeselib.sys.nameformat.CustomNameTags
import me.lucyydotp.cheeselib.sys.nameformat.NameFormatter
import me.lucyydotp.cheeselib.inject.GlobalInjectionContext
import me.lucyydotp.cheeselib.inject.bind
import me.lucyydotp.cheeselib.inject.context
import me.lucyydotp.cheeselib.module.ModuleHolder
import me.lucyydotp.cheeselib.module.ParentModule
import me.lucyydotp.cheeselib.sys.AnnounceClientBrand
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.messaging.Messenger
import org.incendo.interfaces.paper.PaperInterfaceListeners
import org.slf4j.Logger

class CheeseHunt(parent: ModuleHolder) : ParentModule(parent) {

    val logger: Logger by context()
    val plugin: Plugin by context()
    val server: Server by context()

    val game: Game

    init {
        GlobalInjectionContext.bind(NameFormatter(this).registerAsChild())
        Commands(this).registerAsChild()

        game = Game(this).registerAsChild()
        CustomNameTags(this).registerAsChild()
        AnnounceClientBrand(this).registerAsChild()

        // Module-based event listeners
        PlayerDeathEvent(this).registerAsChild()
        PlayerMovementEvent(this).registerAsChild()
        PlayerPotionEffectEvent(this).registerAsChild()

        // Commands
        Announce(this).registerAsChild()
        // BuildMode is registered in Game
        CustomItem(this).registerAsChild()
        DebugCommands(this).registerAsChild()
        GameCommands(this).registerAsChild()
        GameMode(this).registerAsChild()
        MapCommands(this).registerAsChild()
        Particle(this).registerAsChild()
        Ping(this).registerAsChild()
        SetGameTitle(this).registerAsChild()
        SetSkin(this).registerAsChild()
        Timer(this).registerAsChild()
        Worlds(this).registerAsChild()

        onEnable {
            // Bukkit-style event listeners
            listOf(
                AdvancementsRecipesEvent(),
                ArrowLandEvent(),
                BlockBreakDropEvent(),
                BlockPerishEvent(),
                BlockPlaceEvent(),
                DamageEvent(),
                HungerDepleteEvent(),
                ItemDamageEvent(),
                JoinQuitEvent(),
                PlayerDamageEvent(),
                PlayerInteractionsEvent(),
                PlayerInventoryInteractEvent(),
                PlayerItemEvent(),
                PlayerResourcePackLoadEvent(),
                PlayerResurrectEvent(),
                ServerListEvent(),
                TNTExplodeEvent(),
                UseCustomItemEvent(),
            ).forEach {
                server.pluginManager.registerEvents(it, plugin)
            }

            setupConfigs()
            setupInterfaces()
            game.setup()
        }

        onDisable {
            game.cleanUp()
        }
    }

    private fun setupInterfaces() {
        logger.info("Setting up interfaces...")
        PaperInterfaceListeners.install(plugin)
    }

    private fun setupConfigs() {
        logger.info("Setting up configurations...")
        game.configManager.setup()
        game.mapManager.setCurrentMap(null, Maps.REFORGED)
    }

    companion object {
        fun getPlugin(): CheeseHuntPlugin {
            return Bukkit.getPluginManager().getPlugin("CheeseHunt") as CheeseHuntPlugin
        }

        @Deprecated("Use contextual DI")
        fun getGame(): Game = getPlugin().module.game

        @Deprecated("Use contextual DI")
        fun getMessenger(): Messenger {
            TODO()
        }
    }
}
