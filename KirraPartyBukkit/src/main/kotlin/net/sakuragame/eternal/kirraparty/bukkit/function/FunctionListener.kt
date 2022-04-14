package net.sakuragame.eternal.kirraparty.bukkit.function

import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty
import net.sakuragame.eternal.kirraparty.bukkit.party.PartyAPI
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityRegainHealthEvent

object FunctionListener {

    @EventHandler
    fun e(e: EntityRegainHealthEvent) {
        val player = e.entity as? Player ?: return
        if (!PartyAPI.hasParty(player)) {
            return
        }
        DCoreParty.updateDragonCoreVars(player)
    }

    @EventHandler
    fun e(e: EntityDamageEvent) {
        val player = e.entity as? Player ?: return
        if (!PartyAPI.hasParty(player)) {
            return
        }
        DCoreParty.updateDragonCoreVars(player)
    }
}