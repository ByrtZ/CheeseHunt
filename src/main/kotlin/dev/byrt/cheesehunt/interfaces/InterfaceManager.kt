package dev.byrt.cheesehunt.interfaces

import dev.byrt.cheesehunt.game.Game

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

import org.incendo.interfaces.core.arguments.ArgumentKey
import org.incendo.interfaces.core.arguments.HashMapInterfaceArguments
import org.incendo.interfaces.core.click.ClickContext
import org.incendo.interfaces.core.click.ClickHandler
import org.incendo.interfaces.core.transform.Transform
import org.incendo.interfaces.core.view.InterfaceView
import org.incendo.interfaces.paper.PlayerViewer
import org.incendo.interfaces.paper.element.ItemStackElement
import org.incendo.interfaces.paper.pane.ChestPane
import org.incendo.interfaces.paper.transform.PaperTransform
import org.incendo.interfaces.paper.type.ChestInterface

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class InterfaceManager(private val game : Game) {
    private var infoInterface = ChestInterface.builder()
        .rows(1)
        .updates(true, 5)
        .clickHandler(ClickHandler.cancel())
        .addTransform(PaperTransform.chestFill(ItemStackElement.of(ItemStack(Material.BLACK_STAINED_GLASS_PANE))))
        .addTransform(Transform { pane: ChestPane, view: InterfaceView<ChestPane, PlayerViewer> ->
            val time = view.arguments().get(ArgumentKey.of("time", String::class.java))
            val player = view.arguments().get(ArgumentKey.of("player", Player::class.java))
            val clicks = view.arguments().get(ArgumentKey.of("clicks", Int::class.java))

            val itemStack = ItemStack(Material.PAPER)
            val itemMeta = itemStack.itemMeta
            itemMeta.displayName(Component.text("Interfaces!", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))
            val lore: ArrayList<Component> = ArrayList()
            lore.add(Component.text("Subtitle Line", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))
            lore.add(Component.text("Player: ${player.name}", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false))
            lore.add(Component.text("Clicks: $clicks", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
            lore.add(Component.text("Time: $time", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))
            itemMeta.lore(lore)
            itemStack.itemMeta = itemMeta

            pane.element(ItemStackElement.of(itemStack)
            { clickHandler: ClickContext<ChestPane, InventoryClickEvent, PlayerViewer> ->
                val arguments = HashMapInterfaceArguments
                    .with(
                        ArgumentKey.of(
                            "clicks",
                            Int::class.java
                        ), clickHandler.view().arguments().get(
                            ArgumentKey.of(
                                "clicks",
                                Int::class.java
                            )
                        ) + 1
                    )
                    .with(
                        ArgumentKey.of(
                            "player",
                            Player::class.java
                        ),
                        clickHandler.view().arguments().get(ArgumentKey.of("player", Player::class.java))
                    ).build()
                clickHandler.view().backing().open(clickHandler.viewer(), arguments)
            }, 4, 0
            )
        })
        .title(Component.text("Interfaces Demo"))
        .build()

    fun testInterface(player : Player) {
        val dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
        val now = LocalDateTime.now()

        infoInterface.open(PlayerViewer.of(player),
            HashMapInterfaceArguments
                .with(ArgumentKey.of("player", Player::class.java), player)
                .with(ArgumentKey.of("time", String::class.java), dtf.format(now))
                .with(ArgumentKey.of("clicks", Int::class.java), 0)
                .build()
        )
    }
}