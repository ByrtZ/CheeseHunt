package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.state.Teams
import me.byrt.cheesehunt.state.Sounds

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import com.destroystokyo.paper.Namespaced

import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.util.Vector

import java.util.*

@Suppress("unused")
class ItemManager(private val game : Game) {
    fun spawnSideItems(sideItem : SideItem) {
        clearFloorItems()

        val item = ItemStack(sideItem.material, sideItem.amount)
        val itemMeta = item.itemMeta
        itemMeta.displayName(sideItem.displayName)
        item.itemMeta = itemMeta

        val dropItemRed = game.locationManager.getRedItemSpawn().world.dropItem(game.locationManager.getRedItemSpawn(), item)
        dropItemRed.velocity = Vector(0.0, 0.25, 0.0)
        dropItemRed.location.world.spawnParticle(Particle.END_ROD, dropItemRed.location, 25, 0.0, 0.0, 0.0, 0.05)
        dropItemRed.customName(sideItem.displayName)
        dropItemRed.isCustomNameVisible = true
        dropItemRed.isGlowing = true

        val dropItemBlue = game.locationManager.getBlueItemSpawn().world.dropItem(game.locationManager.getBlueItemSpawn(), item)
        dropItemBlue.velocity = Vector(0.0, 0.25, 0.0)
        dropItemBlue.location.world.spawnParticle(Particle.END_ROD, dropItemBlue.location, 25, 0.0, 0.0, 0.0, 0.05)
        dropItemBlue.customName(sideItem.displayName)
        dropItemBlue.isCustomNameVisible = true
        dropItemBlue.isGlowing = true

        for(player in Bukkit.getOnlinePlayers()) {
            player.playSound(player.location, Sounds.Alert.GENERAL_ALERT, 1f, 1f)
            player.sendMessage(
                Component.text("[")
                    .append(Component.text("▶").color(NamedTextColor.YELLOW))
                    .append(Component.text("] "))
                    .append(Component.text("Power-ups have spawned on each side of the map.", NamedTextColor.AQUA, TextDecoration.BOLD)
                )
            )
        }
    }

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

    fun clearFloorItems() {
        for(item in game.locationManager.getSpawn().world.getEntitiesByClass(Item::class.java)) {
            item.remove()
        }
    }

    fun getRandomItem() : SideItem {
        val random = Random()
        return SideItem.values()[random.nextInt(SideItem.values().size)]
    }
}

@Suppress("unused")
enum class SideItem(val material : Material, val amount : Int, val displayName : Component) {
    RESURRECTION_CHARM(Material.TOTEM_OF_UNDYING, 1, Component.text("Charm of Resurrection", NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false)),
    THROWABLE_TNT(Material.TNT, 2, Component.text("Throwing TNT", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)),
    INVISIBILITY_CLOAK(Material.GRAY_DYE, 1, Component.text("Invisibili-brie Charm", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))
}