package me.byrt.cheesehunt.manager

import me.byrt.cheesehunt.state.Teams
import me.byrt.cheesehunt.state.Sounds
import me.byrt.cheesehunt.game.Game
import me.byrt.cheesehunt.util.DevStatus

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import com.destroystokyo.paper.Namespaced

import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.entity.*
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

import java.util.*

@Suppress("unused")
class ItemManager(private val game : Game) {
    fun spawnSideItems(powerUpItem : PowerUpItem) {
        clearFloorItems()

        val item = ItemStack(powerUpItem.material, powerUpItem.amount)
        val itemMeta = item.itemMeta
        itemMeta.displayName(powerUpItem.displayName)
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
        mainWeaponMeta.displayName(Component.text("Swiss Sword").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false))
        mainWeaponMeta.isUnbreakable = true
        mainWeaponMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
        mainWeapon.itemMeta = mainWeaponMeta
        player.inventory.addItem(mainWeapon)

        val subWeapon = ItemStack(Material.BOW, 1)
        val subWeaponMeta: ItemMeta = subWeapon.itemMeta
        subWeaponMeta.displayName(Component.text("Stilton Sniper").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false))
        subWeaponMeta.isUnbreakable = true
        subWeaponMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
        subWeapon.itemMeta = subWeaponMeta
        player.inventory.addItem(subWeapon)

        val cheeseCollector = ItemStack(Material.WOODEN_PICKAXE, 1)
        val cheeseCollectorMeta: ItemMeta = cheeseCollector.itemMeta
        cheeseCollectorMeta.displayName(Component.text("Gouda Gatherer").color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false))
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
                player.addPotionEffect(PotionEffect(PotionEffectType.FAST_DIGGING, 160, 4, false, false))
            }
        }
    }

    fun getHighestBlock(world : World, x : Int, z : Int) : Block? {
        var y = 255
        while(y >= -10) {
            game.dev.parseDevMessage("Y: $y, Type: ${Location(world, x.toDouble(), y.toDouble(), z.toDouble()).block.type}", DevStatus.WARNING)
            if(Location(world, x.toDouble(), y.toDouble(), z.toDouble()).block.type == Material.AIR || Location(world, x.toDouble(), y.toDouble(), z.toDouble()).block.type == Material.STRUCTURE_VOID) {
                y--
            } else {
                return Location(world, x.toDouble(), y.toDouble(), z.toDouble()).block
            }
        }
        return null
    }

    private fun escapeToTeamBase(player : Player) {
        when(game.teamManager.getPlayerTeam(player.uniqueId)) {
            Teams.RED -> {
                player.teleport(game.locationManager.getRedEscapeLoc())
                val velocity = Vector(1.1, 1.45, 0.0)
                player.velocity = velocity
            }
            Teams.BLUE -> {
                player.teleport(game.locationManager.getBlueEscapeLoc())
                val velocity = Vector(-1.1, 1.45, 0.0)
                player.velocity = velocity
            }
            Teams.SPECTATOR -> {
                player.teleport(game.locationManager.getArenaCentre())
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

enum class PowerUpItem(val material : Material, val amount : Int, val displayName : Component, val consumeMessage : Component) {
    RESURRECTION_CHARM(Material.TOTEM_OF_UNDYING, 1, Component.text("Resurrection Charm", NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false), Component.text("[").append(Component.text("▶", NamedTextColor.YELLOW)).append(Component.text("] ")).append(Component.text("Your Resurrection charm saved you from death!", NamedTextColor.LIGHT_PURPLE))),
    THROWABLE_TNT(Material.TNT, 2, Component.text("Throwing TNT", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false), Component.text("[").append(Component.text("▶", NamedTextColor.YELLOW)).append(Component.text("] ")).append(Component.text("You used a Throwing TNT!", NamedTextColor.GREEN))),
    INVISIBILITY_CHARM(Material.GRAY_DYE, 1, Component.text("Invisibili-brie Charm", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false), Component.text("[").append(Component.text("▶", NamedTextColor.YELLOW)).append(Component.text("] ")).append(Component.text("Your Invisibili-brie charm granted you invisibility for 10 seconds!", NamedTextColor.GREEN))),
    SPEED_CHARM(Material.LIGHT_BLUE_DYE, 1, Component.text("Parma-zoom Charm", NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false), Component.text("[").append(Component.text("▶", NamedTextColor.YELLOW)).append(Component.text("] ")).append(Component.text("Your Parma-zoom charm granted you speed for 4 seconds!", NamedTextColor.GREEN))),
    ESCAPE_CHARM(Material.FEATHER, 1, Component.text("Escape Charm", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false), Component.text("[").append(Component.text("▶", NamedTextColor.YELLOW)).append(Component.text("] ")).append(Component.text("Your Escape charm sent you back to your base!", NamedTextColor.GREEN))),
    HASTE_CHARM(Material.ORANGE_DYE, 1, Component.text("Hallou-mine Charm", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false), Component.text("[").append(Component.text("▶", NamedTextColor.YELLOW)).append(Component.text("] ")).append(Component.text("Your Hallou-mine charm granted you haste for 8 seconds!", NamedTextColor.GREEN)))
}