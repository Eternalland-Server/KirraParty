package net.sakuragame.eternal.kirraparty.bukkit.function

import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty
import net.sakuragame.eternal.kirraparty.bukkit.party.PartyAPI
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit

object FunctionListener {

    @SubscribeEvent
    fun e(e: EntityRegainHealthEvent) {
        val player = e.entity as? Player ?: return
        if (!PartyAPI.hasParty(player)) {
            return
        }
        DCoreParty.updateDragonCoreVars(player)
    }

    @SubscribeEvent
    fun e(e: EntityDamageEvent) {
        val player = e.entity as? Player ?: return
        if (!PartyAPI.hasParty(player)) {
            return
        }
        DCoreParty.updateDragonCoreVars(player)
    }

    @SubscribeEvent
    fun e(e: PlayerJoinEvent) {
        submit(async = true, delay = 20L) {
            DCoreParty.updateDragonCoreVars(e.player)
        }
    }
}