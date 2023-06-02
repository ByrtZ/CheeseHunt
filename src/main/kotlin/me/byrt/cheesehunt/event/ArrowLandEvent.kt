package me.byrt.cheesehunt.event

import me.byrt.cheesehunt.Main
import me.byrt.cheesehunt.state.GameState

import org.bukkit.entity.Arrow
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent

@Suppress("unused")
class ArrowLandEvent : Listener {
    @EventHandler
    private fun onArrowLand(e : ProjectileHitEvent) {
        if(Main.getGame().gameManager.getGameState() == GameState.IN_GAME || Main.getGame().gameManager.getGameState() == GameState.OVERTIME) {
            if(e.entity is Arrow && e.hitEntity == null) {
                e.entity.remove()
            }
        }
    }

    /*@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private fun onProjectileHitEvent(e : ProjectileHitEvent) {
        val projectile = e.entity
        val source = projectile.shooter
        val projectileType = projectile.type
        // sets bouncing metadata if projectile is an arrow
        if(projectile is Arrow) {
            projectile.setMetadata("bouncing", FixedMetadataValue(Main.getPlugin(), true))
        }
        if (projectile.hasMetadata("bouncing")) {
            val arrowVelocity = projectile.velocity
            var speed = arrowVelocity.length()
            if (speed < 0.3 || projectileType == EntityType.ARROW && speed < 0.5) {
                return
            }
            val arrowLocation: Location = projectile.location
            val hitBlock: Block = arrowLocation.block
            var blockFace: BlockFace


            val blockIterator = BlockIterator(arrowLocation.world, arrowLocation.toVector(), arrowVelocity, 0.0, 3)
            var previousBlock: Block = hitBlock
            var nextBlock: Block = blockIterator.next()

            // to make sure, that previousBlock and nextBlock are not the same block
            while (blockIterator.hasNext() && (nextBlock.type === Material.AIR || nextBlock.isLiquid || nextBlock == hitBlock)) {
                previousBlock = nextBlock
                nextBlock = blockIterator.next()
            }

            // direction
            blockFace = nextBlock.getFace(previousBlock)!!
            if (blockFace == BlockFace.SELF) {
                blockFace = BlockFace.UP
            }
            var mirrorDirection = Vector(blockFace.modX, blockFace.modY, blockFace.modZ)
            val dotProduct = arrowVelocity.dot(mirrorDirection)
            mirrorDirection = mirrorDirection.multiply(dotProduct).multiply(2.0)

            // reduce projectile speed
            speed *= 0.6
            val newProjectile: Projectile
            if (projectileType == EntityType.ARROW) {
                // spawn with slight spray:
                newProjectile = projectile.world.spawnArrow(
                    arrowLocation,
                    arrowVelocity.subtract(mirrorDirection),
                    speed.toFloat(),
                    4.0f
                )

                // make the arrow pickup-able:
                if (source is Player) {
                    val field: Field
                    try {
                        val entityArrow = newProjectile.javaClass.getMethod("getHandle").invoke(newProjectile)
                        field = entityArrow.javaClass.getDeclaredField("fromPlayer")
                        //field.isAccessible = true;
                        field.set(entityArrow, 1)
                    } catch (e: Exception) {
                        Main.getPlugin().logger.info("Failed to set a bouncing arrow pick-up-able")
                        e.printStackTrace()
                    }
                }
            } else {
                // without spray:
                newProjectile = projectile.world.spawnEntity(arrowLocation, projectile.type) as Projectile
                newProjectile.velocity = arrowVelocity.subtract(mirrorDirection).normalize().multiply(speed)
            }
            newProjectile.shooter = source
            newProjectile.fireTicks = projectile.fireTicks
            newProjectile.setMetadata("bouncing", FixedMetadataValue(Main.getPlugin(), true))

            // remove old arrow
            projectile.remove()
        }
    }*/
}
