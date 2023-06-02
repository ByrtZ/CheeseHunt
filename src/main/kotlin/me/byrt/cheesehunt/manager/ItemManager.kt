package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.state.Teams

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import com.destroystokyo.paper.Namespaced

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta

import java.util.*

@Suppress("unused")
class ItemManager(private val game : Game) {
    fun givePlayerTeamBoots(player : Player, team : Teams) {
        when(team) {
            Teams.RED -> {
                val teamABoots = ItemStack(Material.LEATHER_BOOTS)
                val teamABootsMeta: ItemMeta = teamABoots.itemMeta
                teamABootsMeta.displayName(Component.text("Red Team Boots").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
                teamABootsMeta.isUnbreakable = true
                teamABootsMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES)
                teamABoots.itemMeta = teamABootsMeta
                val lm: LeatherArmorMeta = teamABoots.itemMeta as LeatherArmorMeta
                lm.setColor(Color.RED)
                teamABoots.itemMeta = lm
                player.inventory.boots = teamABoots
            }
            Teams.BLUE -> {
                val teamBBoots = ItemStack(Material.LEATHER_BOOTS)
                val teamBBootsMeta: ItemMeta = teamBBoots.itemMeta
                teamBBootsMeta.displayName(Component.text("Blue Team Boots").color(NamedTextColor.BLUE).decoration(TextDecoration.ITALIC, false))
                teamBBootsMeta.isUnbreakable = true
                teamBBootsMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES)
                teamBBoots.itemMeta = teamBBootsMeta
                val lm: LeatherArmorMeta = teamBBoots.itemMeta as LeatherArmorMeta
                lm.setColor(Color.BLUE)
                teamBBoots.itemMeta = lm
                player.inventory.boots = teamBBoots
            }
            Teams.SPECTATOR -> {
                val spectatorBoots = ItemStack(Material.LEATHER_BOOTS)
                val spectatorBootsMeta: ItemMeta = spectatorBoots.itemMeta
                spectatorBootsMeta.displayName(Component.text("Spectator Boots").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false))
                spectatorBootsMeta.isUnbreakable = true
                spectatorBootsMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES)
                spectatorBoots.itemMeta = spectatorBootsMeta
                val lm: LeatherArmorMeta = spectatorBoots.itemMeta as LeatherArmorMeta
                if(player.isOp) {
                    lm.setColor(Color.fromRGB(170, 0, 0))
                } else {
                    lm.setColor(Color.GRAY)
                }
                spectatorBoots.itemMeta = lm
                player.inventory.boots = spectatorBoots
            }
        }
    }

    fun givePlayerKit(player : Player) {
        val mainWeapon = ItemStack(Material.STONE_SWORD, 1)
        val mainWeaponMeta: ItemMeta = mainWeapon.itemMeta
        mainWeaponMeta.displayName(Component.text("Combat Sword").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false))
        mainWeaponMeta.isUnbreakable = true
        mainWeaponMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
        mainWeapon.itemMeta = mainWeaponMeta
        player.inventory.addItem(mainWeapon)

        val subWeapon = ItemStack(Material.BOW, 1)
        val subWeaponMeta: ItemMeta = subWeapon.itemMeta
        subWeaponMeta.displayName(Component.text("Combat Bow").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false))
        subWeaponMeta.isUnbreakable = true
        subWeaponMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
        subWeapon.itemMeta = subWeaponMeta
        player.inventory.addItem(subWeapon)

        val cheeseCollector = ItemStack(Material.WOODEN_PICKAXE, 1)
        val cheeseCollectorMeta: ItemMeta = cheeseCollector.itemMeta
        cheeseCollectorMeta.displayName(Component.text("Cheese Collector").color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false))
        cheeseCollectorMeta.isUnbreakable = true
        cheeseCollectorMeta.setDestroyableKeys(Collections.singletonList(Material.SPONGE.key) as Collection<Namespaced>)
        cheeseCollectorMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS)
        cheeseCollector.itemMeta = cheeseCollectorMeta
        player.inventory.addItem(cheeseCollector)

        player.inventory.addItem(ItemStack(Material.ARROW, 3))
    }

    fun givePlayerPickaxe(player : Player) {
        val cheeseCollector = ItemStack(Material.WOODEN_PICKAXE, 1)
        val cheeseCollectorMeta: ItemMeta = cheeseCollector.itemMeta
        cheeseCollectorMeta.displayName(Component.text("Cheese Collector").color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false))
        cheeseCollectorMeta.isUnbreakable = true
        cheeseCollectorMeta.setDestroyableKeys(Collections.singletonList(Material.SPONGE.key) as Collection<Namespaced>)
        cheeseCollectorMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS)
        cheeseCollector.itemMeta = cheeseCollectorMeta
        player.inventory.addItem(cheeseCollector)
    }

    fun getCheeseItem(team : Teams) : ItemStack {
        val cheese = ItemStack(Material.SPONGE, 1)
        val cheeseMeta: ItemMeta = cheese.itemMeta
        cheeseMeta.displayName(Component.text("Cheese").color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false))
        when(team) {
            Teams.RED -> {
                cheeseMeta.setPlaceableKeys(Collections.singletonList(Material.RED_WOOL.key) as Collection<Namespaced>)
            }
            Teams.BLUE -> {
                cheeseMeta.setPlaceableKeys(Collections.singletonList(Material.BLUE_WOOL.key) as Collection<Namespaced>)
            } else -> {
                //no.
            }
        }
        cheeseMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON)
        cheese.itemMeta = cheeseMeta
        return cheese
    }
}