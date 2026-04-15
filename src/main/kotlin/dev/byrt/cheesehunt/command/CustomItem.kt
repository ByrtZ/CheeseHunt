package dev.byrt.cheesehunt.command

import dev.byrt.cheesehunt.manager.ItemRarity
import dev.byrt.cheesehunt.manager.ItemType
import io.papermc.paper.command.brigadier.CommandSourceStack

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class CustomItem {
    @Command("givecustomitem <item>")
    @Permission("cheesehunt.customitem")
    fun customItem(css: CommandSourceStack, @Argument("item") item: CustomItems) {
        when(item) {
            CustomItems.ASPECT_OF_THE_VOID -> {
                createTeleportSpoon(css.sender as Player)
            }
            CustomItems.RAYGUN -> {
                createRayGun(css.sender as Player)
            }
        }
    }

    private fun createTeleportSpoon(player : Player) {
        val teleportSpoon = ItemStack(Material.DIAMOND_SHOVEL)
        val teleportSpoonMeta: ItemMeta = teleportSpoon.itemMeta
        val teleportSpoonRarity = ItemRarity.SPECIAL
        val teleportSpoonType = ItemType.UTILITY
        teleportSpoonMeta.displayName(Component.text("Aspect of the Void").color(TextColor.fromHexString(teleportSpoonRarity.rarityColour)).decoration(TextDecoration.ITALIC, false))
        val teleportSpoonLore = listOf(
            Component.text("${teleportSpoonRarity.rarityGlyph}${teleportSpoonType.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("An out of this world wand that grants the", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("ability to teleport upon interaction.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
        )
        teleportSpoonMeta.lore(teleportSpoonLore)
        teleportSpoonMeta.isUnbreakable = true
        teleportSpoonMeta.addEnchant(Enchantment.MENDING, 1, false)
        teleportSpoonMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
        teleportSpoon.itemMeta = teleportSpoonMeta
        player.inventory.addItem(ItemStack(teleportSpoon))
        player.sendMessage(Component.text("Received custom item ${CustomItems.ASPECT_OF_THE_VOID}.").color(NamedTextColor.GREEN))
    }

    private fun createRayGun(player : Player) {
        val rayGun = ItemStack(Material.GOLDEN_SWORD)
        val rayGunMeta: ItemMeta = rayGun.itemMeta
        val rayGunRarity = ItemRarity.SPECIAL
        val rayGunType = ItemType.WEAPON
        rayGunMeta.displayName(Component.text("Ray-Gun").color(TextColor.fromHexString(rayGunRarity.rarityColour)).decoration(TextDecoration.ITALIC, false))
        val rayGunLore = listOf(
            Component.text("${rayGunRarity.rarityGlyph}${rayGunType.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("You already know what this does.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
        )
        rayGunMeta.lore(rayGunLore)
        rayGunMeta.isUnbreakable = true
        rayGunMeta.addEnchant(Enchantment.MENDING, 1, false)
        rayGunMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
        rayGun.itemMeta = rayGunMeta
        player.inventory.addItem(ItemStack(rayGun))
        player.sendMessage(Component.text("Received custom item ${CustomItems.RAYGUN}.").color(NamedTextColor.GREEN))
    }

    enum class CustomItems {
       ASPECT_OF_THE_VOID,
       RAYGUN
    }
}