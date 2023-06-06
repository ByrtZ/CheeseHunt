package me.byrt.cheesehunt.event

import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityExplodeEvent

@Suppress("unused")
class TNTExplodeEvent : Listener {
    @EventHandler
    private fun onTNTExplode(e : EntityExplodeEvent) {
        if(e.entity is TNTPrimed) {
            e.blockList().clear()
        }
    }
}