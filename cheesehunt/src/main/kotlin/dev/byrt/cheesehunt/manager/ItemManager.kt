package dev.byrt.cheesehunt.manager

import dev.byrt.cheesehunt.state.Teams
import dev.byrt.cheesehunt.state.Sounds
import dev.byrt.cheesehunt.game.Game

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.format.TextColor

import com.destroystokyo.paper.Namespaced
import me.lucyydotp.cheeselib.util.teleportWithPassengers

import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

import java.util.*

class ItemManager(private val game : Game) {
    fun spawnSideItems(powerUpItem : PowerUpItem) {
        clearFloorItems()

        val item = ItemStack(powerUpItem.material, powerUpItem.amount)
        val itemMeta = item.itemMeta
        itemMeta.displayName(powerUpItem.displayName)
        itemMeta.lore(powerUpItem.displayLore)
        item.itemMeta = itemMeta

        val dropItemRed = game.locationManager.getRedItemSpawn().world.dropItem(game.locationManager.getRedItemSpawn(), item)
        dropItemRed.velocity = Vector(0.0, 0.25, 0.0)
        dropItemRed.location.world.spawnParticle(Particle.END_ROD, dropItemRed.location, 25, 0.0, 0.0, 0.0, 0.05)
        dropItemRed.customName(powerUpItem.displayName)
        dropItemRed.isCustomNameVisible = true
        dropItemRed.isGlowing = true

        val dropItemBlue = game.locationManager.getBlueItemSpawn().world.dropItem(game.locationManager.getBlueItemSpawn(), item)
        dropItemBlue.velocity = Vector(0.0, 0.25, 0.0)
        dropItemBlue.location.world.spawnParticle(Particle.END_ROD, dropItemBlue.location, 25, 0.0, 0.0, 0.0, 0.05)
        dropItemBlue.customName(powerUpItem.displayName)
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

    fun givePlayerTeamBoots(player : Player, team : Teams?) {
        when(team) {
            Teams.RED -> {
                val teamABoots = ItemStack(Material.LEATHER_BOOTS)
                val teamABootsMeta: ItemMeta = teamABoots.itemMeta
                teamABootsMeta.displayName(Component.text("Red Team Boots").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
                val teamABootsRarity = ItemRarity.COMMON
                val teamABootsType = ItemType.ARMOUR
                val teamABootsLore = listOf(
                    Component.text("${teamABootsRarity.rarityGlyph}${teamABootsType.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
                    Component.text("A snazzy pair of Red Team's boots.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
                )
                teamABootsMeta.lore(teamABootsLore)
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
                val teamBBootsRarity = ItemRarity.COMMON
                val teamBBootsType = ItemType.ARMOUR
                val teamBBootsLore = listOf(
                    Component.text("${teamBBootsRarity.rarityGlyph}${teamBBootsType.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
                    Component.text("A snazzy pair of Blue Team's boots.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
                )
                teamBBootsMeta.lore(teamBBootsLore)
                teamBBootsMeta.isUnbreakable = true
                teamBBootsMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES)
                teamBBoots.itemMeta = teamBBootsMeta
                val lm: LeatherArmorMeta = teamBBoots.itemMeta as LeatherArmorMeta
                lm.setColor(Color.BLUE)
                teamBBoots.itemMeta = lm
                player.inventory.boots = teamBBoots
            }
            // TODO: admin boots
            else -> {
                val spectatorBoots = ItemStack(Material.LEATHER_BOOTS)
                val spectatorBootsMeta = spectatorBoots.itemMeta
                spectatorBootsMeta.isUnbreakable = true
                spectatorBootsMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES)
                if(player.isOp) {
                    val spectatorBootsRarity = ItemRarity.SPECIAL
                    val spectatorBootsType = ItemType.ARMOUR
                    spectatorBootsMeta.displayName(Component.text("Admin Boots").color(TextColor.fromHexString(spectatorBootsRarity.rarityColour)).decoration(TextDecoration.ITALIC, false))
                    val spectatorBootsLore = listOf(
                        Component.text("${spectatorBootsRarity.rarityGlyph}${spectatorBootsType.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
                        Component.text("A very special pair of Admin boots.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
                    )
                    spectatorBootsMeta.lore(spectatorBootsLore)
                    spectatorBoots.itemMeta = spectatorBootsMeta
                    val lm: LeatherArmorMeta = spectatorBoots.itemMeta as LeatherArmorMeta
                    lm.setColor(Color.fromRGB(170, 0, 0))
                    spectatorBoots.itemMeta = lm
                } else {
                    val spectatorBootsRarity = ItemRarity.COMMON
                    val spectatorBootsType = ItemType.ARMOUR
                    spectatorBootsMeta.displayName(Component.text("Spectator Boots").color(TextColor.fromHexString(spectatorBootsRarity.rarityColour)).decoration(TextDecoration.ITALIC, false))
                    val spectatorBootsLore = listOf(
                        Component.text("${spectatorBootsRarity.rarityGlyph}${spectatorBootsType.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
                        Component.text("A boring old pair of Spectator boots.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
                    )
                    spectatorBootsMeta.lore(spectatorBootsLore)
                    spectatorBoots.itemMeta = spectatorBootsMeta
                    val lm: LeatherArmorMeta = spectatorBoots.itemMeta as LeatherArmorMeta
                    lm.setColor(Color.GRAY)
                    spectatorBoots.itemMeta = lm
                }
                player.inventory.boots = spectatorBoots
            }
        }
    }

    fun givePlayerKit(player : Player) {
        val mainWeapon = ItemStack(Material.STONE_SWORD, 1)
        val mainWeaponMeta: ItemMeta = mainWeapon.itemMeta
        val mainWeaponRarity = ItemRarity.COMMON
        val mainWeaponType = ItemType.WEAPON
        mainWeaponMeta.displayName(Component.text("Swiss Sword").color(TextColor.fromHexString(mainWeaponRarity.rarityColour)).decoration(TextDecoration.ITALIC, false))
        val mainWeaponLore = listOf(
            Component.text("${mainWeaponRarity.rarityGlyph}${mainWeaponType.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("A standard melee weapon.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
        )
        mainWeaponMeta.lore(mainWeaponLore)
        mainWeaponMeta.isUnbreakable = true
        mainWeaponMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
        mainWeapon.itemMeta = mainWeaponMeta
        player.inventory.addItem(mainWeapon)

        val subWeapon = ItemStack(Material.BOW, 1)
        val subWeaponMeta = subWeapon.itemMeta
        val subWeaponRarity = ItemRarity.COMMON
        val subWeaponType = ItemType.WEAPON
        subWeaponMeta.displayName(Component.text("Stilton Sniper").color(TextColor.fromHexString(subWeaponRarity.rarityColour)).decoration(TextDecoration.ITALIC, false))
        val subWeaponLore = listOf(
            Component.text("${subWeaponRarity.rarityGlyph}${subWeaponType.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("A standard ranged weapon.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
        )
        subWeaponMeta.lore(subWeaponLore)
        subWeaponMeta.isUnbreakable = true
        subWeaponMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
        subWeapon.itemMeta = subWeaponMeta
        player.inventory.addItem(subWeapon)

        givePlayerPickaxe(player)

        val subWeaponAmmo = ItemStack(Material.ARROW, 3)
        val subWeaponAmmoMeta = subWeaponAmmo.itemMeta
        val subWeaponAmmoRarity = ItemRarity.COMMON
        val subWeaponAmmoType = ItemType.CONSUMABLE
        subWeaponAmmoMeta.displayName(Component.text("Arrow").color(TextColor.fromHexString(subWeaponAmmoRarity.rarityColour)).decoration(TextDecoration.ITALIC, false))
        val subWeaponAmmoLore = listOf(
            Component.text("${subWeaponAmmoRarity.rarityGlyph}${subWeaponAmmoType.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("A regular flint-tipped arrow to", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("be used with a bow or crossbow.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
        )
        subWeaponAmmoMeta.lore(subWeaponAmmoLore)
        subWeaponAmmo.itemMeta = subWeaponAmmoMeta
        player.inventory.addItem(ItemStack(subWeaponAmmo))
    }

    fun givePlayerPickaxe(player : Player) {
        val cheeseCollector = ItemStack(Material.WOODEN_PICKAXE, 1)
        val cheeseCollectorMeta: ItemMeta = cheeseCollector.itemMeta
        val cheeseCollectorRarity = ItemRarity.COMMON
        val cheeseCollectorType = ItemType.TOOL
        cheeseCollectorMeta.displayName(Component.text("Gouda Gatherer", TextColor.fromHexString(cheeseCollectorRarity.rarityColour)).decoration(TextDecoration.ITALIC, false))
        val cheeseCollectorLore = listOf(
            Component.text("${cheeseCollectorRarity.rarityGlyph}${cheeseCollectorType.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("Perfect for cheese farming!", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
        )
        cheeseCollectorMeta.lore(cheeseCollectorLore)
        cheeseCollectorMeta.isUnbreakable = true
        cheeseCollectorMeta.setDestroyableKeys(Collections.singletonList(Material.SPONGE.key) as Collection<Namespaced>)
        cheeseCollectorMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS)
        cheeseCollector.itemMeta = cheeseCollectorMeta
        player.inventory.addItem(cheeseCollector)
    }

    fun getCheeseItem(team : Teams) : ItemStack {
        val cheese = ItemStack(Material.SPONGE, 1)
        val cheeseMeta: ItemMeta = cheese.itemMeta
        val cheeseRarity = ItemRarity.LEGENDARY
        cheeseMeta.displayName(Component.text("Cheese", TextColor.fromHexString(cheeseRarity.rarityColour)).decoration(TextDecoration.ITALIC, false))
        val cheeseLore = listOf(
            Component.text(cheeseRarity.rarityGlyph, NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("A delicious chunk of cheese.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
        )
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
        cheeseMeta.lore(cheeseLore)
        cheese.itemMeta = cheeseMeta
        return cheese
    }

    fun getQueueItem() : ItemStack {
        val queueLeaveItem = ItemStack(Material.RED_DYE, 1)
        val queueLeaveItemMeta: ItemMeta = queueLeaveItem.itemMeta
        queueLeaveItemMeta.displayName(Component.text("Leave Queue").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
        queueLeaveItemMeta.lore(Collections.singletonList(Component.text("Right-Click to leave the queue.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)) as MutableList<out Component>)
        queueLeaveItem.itemMeta = queueLeaveItemMeta
        return queueLeaveItem
    }

    fun clearFloorItems() {
        for(item in game.locationManager.getSpawn().world.getEntitiesByClass(Item::class.java)) {
            item.remove()
        }
    }

    fun useItem(item : PowerUpItem, player : Player) {
        if(item != PowerUpItem.THROWABLE_TNT) {
            decrementItemInHand(player, item)
            player.playSound(player.location, Sounds.Item.USE_ITEM, 1.0f, 1.0f)
            player.sendMessage(item.consumeMessage)
        }

        when(item) {
            PowerUpItem.RESURRECTION_CHARM -> {
                player.playEffect(EntityEffect.TOTEM_RESURRECT)
                player.health = 8.0
                player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 60, 2,false, false))
                resurrectFirework(player)
                escapeToTeamBase(player)
                player.playSound(player.location, Sounds.Respawn.RESPAWN, 1.0f, 1.0f)
            }
            PowerUpItem.THROWABLE_TNT -> {
                if(game.cooldownManager.attemptUseTNT(player)) {
                    decrementItemInHand(player, item)
                    player.playSound(player.location, Sounds.Item.USE_ITEM, 1.0f, 1.0f)
                    player.sendMessage(item.consumeMessage)
                    val tnt = player.world.spawn(player.eyeLocation, TNTPrimed::class.java)
                    tnt.source = player
                    val tntVelocity = player.location.direction.multiply(1.4)
                    tnt.velocity = tntVelocity
                }
            }
            PowerUpItem.INVISIBILITY_CHARM -> {
                player.inventory.boots = null
                player.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 200, 0, false, false))
            }
            PowerUpItem.SPEED_CHARM -> {
                player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 80, 2, false, false))
            }
            PowerUpItem.ESCAPE_CHARM -> {
                escapeToTeamBase(player)
                player.playSound(player.location, Sounds.Item.ESCAPE, 1.0f, 1.0f)
            }
            PowerUpItem.HASTE_CHARM -> {
                player.addPotionEffect(PotionEffect(PotionEffectType.FAST_DIGGING, 160, 9, false, false))
            }
        }
    }

    private fun escapeToTeamBase(player : Player) {
        when(game.teams.getTeam(player)) {
            Teams.RED -> {
                player.teleportWithPassengers(game.locationManager.getRedEscapeLoc())
                val velocity = Vector(1.1, 1.45, 0.0)
                player.velocity = velocity
            }
            Teams.BLUE -> {
                player.teleportWithPassengers(game.locationManager.getBlueEscapeLoc())
                val velocity = Vector(-1.1, 1.45, 0.0)
                player.velocity = velocity
            }
            else -> {
                player.teleportWithPassengers(game.locationManager.getArenaCentre())
            }
        }
    }

    private fun resurrectFirework(player : Player) {
        val playerLoc = Location(player.world, player.location.x, player.location.y + 1.0, player.location.z)
        val f: Firework = player.world.spawn(playerLoc, Firework::class.java)
        val fm = f.fireworkMeta
        fm.addEffect(
            FireworkEffect.builder()
                .flicker(false)
                .trail(false)
                .with(FireworkEffect.Type.BALL)
                .withColor(Color.fromRGB(255, 85, 255))
                .build()
        )
        fm.power = 0
        f.fireworkMeta = fm
        f.ticksToDetonate = 1
    }

    private fun decrementItemInHand(player : Player, item : PowerUpItem) {
        if(item == PowerUpItem.RESURRECTION_CHARM) {
            player.inventory.setItemInOffHand(null)
        } else {
            if(player.inventory.itemInMainHand.amount > 1) {
                player.inventory.itemInMainHand.amount--
            } else {
                player.inventory.setItemInMainHand(null)
            }
        }
    }

    fun getRandomItem() : PowerUpItem {
        val random = Random()
        return PowerUpItem.values()[random.nextInt(PowerUpItem.values().size)]
    }
}

enum class PowerUpItem(val material : Material, val amount : Int, val displayName : Component, val displayLore : List<Component>, val consumeMessage : Component) {
    RESURRECTION_CHARM(
        Material.TOTEM_OF_UNDYING,
        1,
        Component.text("Resurrection Charm", TextColor.fromHexString(ItemRarity.LEGENDARY.rarityColour)).decoration(TextDecoration.ITALIC, false),
        listOf(
            Component.text("${ItemRarity.LEGENDARY.rarityGlyph}${ItemType.CONSUMABLE.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("A totem that deeply connects", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("with its user to grant them", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("a second life.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
        ),
        Component.text("[").append(Component.text("▶", NamedTextColor.YELLOW)).append(Component.text("] ")).append(Component.text("Your Resurrection charm saved you from death!", NamedTextColor.LIGHT_PURPLE))
    ),

    THROWABLE_TNT(
        Material.TNT,
        2,
        Component.text("Throwing TNT", TextColor.fromHexString(ItemRarity.EPIC.rarityColour)).decoration(TextDecoration.ITALIC, false),
        listOf(
            Component.text("${ItemRarity.EPIC.rarityGlyph}${ItemType.UTILITY.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("An explosive that can be thrown", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("by right-clicking.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
        ),
        Component.text("[").append(Component.text("▶", NamedTextColor.YELLOW)).append(Component.text("] ")).append(Component.text("You used a Throwing TNT!", NamedTextColor.GREEN))
    ),

    INVISIBILITY_CHARM(
        Material.GRAY_DYE,
        1,
        Component.text("Invisibili-brie Charm", TextColor.fromHexString(ItemRarity.RARE.rarityColour)).decoration(TextDecoration.ITALIC, false),
        listOf(
            Component.text("${ItemRarity.RARE.rarityGlyph}${ItemType.CONSUMABLE.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("A charm that grants the user", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("full temporary invisibility when consumed.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
        ),
        Component.text("[").append(Component.text("▶", NamedTextColor.YELLOW)).append(Component.text("] ")).append(Component.text("Your Invisibili-brie charm granted you invisibility for 10 seconds!", NamedTextColor.GREEN))
    ),

    SPEED_CHARM(
        Material.LIGHT_BLUE_DYE,
        1,
        Component.text("Parma-zoom Charm", TextColor.fromHexString(ItemRarity.RARE.rarityColour)).decoration(TextDecoration.ITALIC, false),
        listOf(
            Component.text("${ItemRarity.RARE.rarityGlyph}${ItemType.CONSUMABLE.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("A charm that grants the user", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("a temporary speed boost when consumed.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
        ),
        Component.text("[").append(Component.text("▶", NamedTextColor.YELLOW)).append(Component.text("] ")).append(Component.text("Your Parma-zoom charm granted you speed for 4 seconds!", NamedTextColor.GREEN))
    ),

    ESCAPE_CHARM(
        Material.FEATHER,
        1,
        Component.text("Escape Charm", TextColor.fromHexString(ItemRarity.EPIC.rarityColour)).decoration(TextDecoration.ITALIC, false),
        listOf(
            Component.text("${ItemRarity.EPIC.rarityGlyph}${ItemType.CONSUMABLE.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("A charm that allows the user", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("to escape to their team's base.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
        ),
        Component.text("[").append(Component.text("▶", NamedTextColor.YELLOW)).append(Component.text("] ")).append(Component.text("Your Escape charm sent you back to your base!", NamedTextColor.GREEN))
    ),

    HASTE_CHARM(
        Material.ORANGE_DYE,
        1,
        Component.text("Hallou-mine Charm", TextColor.fromHexString(ItemRarity.UNCOMMON.rarityColour)).decoration(TextDecoration.ITALIC, false),
        listOf(
            Component.text("${ItemRarity.UNCOMMON.rarityGlyph}${ItemType.CONSUMABLE.typeGlyph}", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("A charm that grants the user a", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            Component.text("temporary mining speed boost when consumed.", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
        ),
        Component.text("[").append(Component.text("▶", NamedTextColor.YELLOW)).append(Component.text("] ")).append(Component.text("Your Hallou-mine charm granted you haste for 8 seconds!", NamedTextColor.GREEN))
    )
}

enum class ItemRarity(val rarityName : String, val rarityColour : String, val rarityGlyph : String) {
    COMMON("Common", "#ffffff", "\uF001"),
    UNCOMMON("Uncommon", "#0ed145", "\uF002"),
    RARE("Rare", "#00a8f3", "\uF003"),
    EPIC("Epic", "#b83dba", "\uF004"),
    LEGENDARY("Legendary", "#ff7f27", "\uF005"),
    MYTHIC("Mythic", "#ff3374", "\uF006"),
    SPECIAL("Special", "#ec1c24", "\uF007"),
    UNREAL("Unreal", "#8666e6", "\uF008")
}

enum class ItemType(val typeName : String, val typeGlyph : String) {
    ARMOUR("Armour", "\uF009"),
    CONSUMABLE("Consumable", "\uF010"),
    TOOL("Tool", "\uF011"),
    UTILITY("Utility", "\uF012"),
    WEAPON("Weapon", "\uF013")
}
