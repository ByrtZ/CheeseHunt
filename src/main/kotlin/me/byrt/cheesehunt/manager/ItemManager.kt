package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.command.ModifierOptions
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta

@Suppress("unused")
class ItemManager(private val game : Game) {
    fun playerJoinTeamEquip(player : Player, team : Teams) {
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
                lm.setColor(Color.GRAY)
                spectatorBoots.itemMeta = lm
                player.inventory.boots = spectatorBoots
            }
        }
    }

    fun givePlayerCheese(player : Player) {
        if(game.getRoundState() == RoundState.ROUND_ONE) {
            if(game.getModifier() == ModifierOptions.BOTTOMLESS_CHEESE) {
                val cheese = ItemStack(Material.SPONGE, 576)
                val cheeseMeta: ItemMeta = cheese.itemMeta
                cheeseMeta.displayName(Component.text("Cheese").color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false))
                cheese.itemMeta = cheeseMeta
                player.inventory.addItem(cheese)
            } else {
                val cheese = ItemStack(Material.SPONGE, 4)
                val cheeseMeta: ItemMeta = cheese.itemMeta
                cheeseMeta.displayName(Component.text("Cheese").color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false))
                cheese.itemMeta = cheeseMeta
                player.inventory.addItem(cheese)
            }
        }
        if(game.getRoundState() == RoundState.ROUND_TWO) {
            val cheese = ItemStack(Material.SPONGE, 1)
            val cheeseMeta: ItemMeta = cheese.itemMeta
            cheeseMeta.displayName(Component.text("Cheese").color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false))
            cheese.itemMeta = cheeseMeta
            player.inventory.addItem(cheese)
        }
    }

    fun givePlayerHoe(player : Player) {
        if(game.getModifier() == ModifierOptions.BOTTOMLESS_CHEESE) {
            val cheeseHarvester = ItemStack(Material.STONE_HOE)
            val cheeseHarvesterMeta: ItemMeta = cheeseHarvester.itemMeta
            cheeseHarvesterMeta.displayName(Component.text("Cheese Harvester 2000").color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false))
            cheeseHarvesterMeta.isUnbreakable = true
            cheeseHarvesterMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
            cheeseHarvesterMeta.addEnchant(Enchantment.DIG_SPEED, 5, true)
            cheeseHarvester.itemMeta = cheeseHarvesterMeta
            player.inventory.addItem(cheeseHarvester)
        } else {
            val cheeseHarvester = ItemStack(Material.STONE_HOE)
            val cheeseHarvesterMeta: ItemMeta = cheeseHarvester.itemMeta
            cheeseHarvesterMeta.displayName(Component.text("Cheese Harvester 1000").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false))
            cheeseHarvesterMeta.isUnbreakable = true
            cheeseHarvesterMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
            cheeseHarvester.itemMeta = cheeseHarvesterMeta
            player.inventory.addItem(cheeseHarvester)
        }
    }
}