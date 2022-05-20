package me.byrt.cheesehunt.manager

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta

@Suppress("unused")
class ItemManager(private val game : Game) {
    fun playerJoinTeamEquip(player : Player, team : Team) {
        when(team) {
            Team.RED -> {
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
            Team.BLUE -> {
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
            Team.SPECTATOR -> {
                val spectatorBoots = ItemStack(Material.LEATHER_BOOTS)
                val spectatorBootsMeta: ItemMeta = spectatorBoots.itemMeta
                spectatorBootsMeta.displayName(Component.text("Spectator Boots").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false))
                spectatorBootsMeta.isUnbreakable = true
                spectatorBootsMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES)
                spectatorBoots.itemMeta = spectatorBootsMeta
                val lm: LeatherArmorMeta = spectatorBoots.itemMeta as LeatherArmorMeta
                lm.setColor(Color.GRAY)
                spectatorBoots.itemMeta = lm
                player.inventory.boots = spectatorBoots
            }
        }
    }
}