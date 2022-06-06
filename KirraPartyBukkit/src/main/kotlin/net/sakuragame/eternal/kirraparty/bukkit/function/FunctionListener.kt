package net.sakuragame.eternal.kirraparty.bukkit.function

import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty
import net.sakuragame.eternal.kirraparty.bukkit.party.PartyAPI
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit
import taboolib.platform.util.sendLang

object FunctionListener {

    @SubscribeEvent
    fun e(e: PlayerCommandPreprocessEvent) {
        if (!e.message.equals("/kParty acceptInvite")) {
            return
        }
        e.isCancelled = true
        val player = e.player
        if (!PartyAPI.hasRequest(player)) {
            player.sendLang("command-player-not-have-invite-quest")
            return
        }
        if (PartyAPI.getParty(player) != null) {
            player.sendLang("")
        }
        PartyAPI.doRequest(e.player)
    }

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