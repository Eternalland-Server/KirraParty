package net.sakuragame.eternal.kirraparty.bukkit.compat.redis

import com.google.gson.Gson
import net.sakuragame.eternal.kirraparty.bukkit.KirraPartyBukkit
import net.sakuragame.eternal.kirraparty.bukkit.event.compat.PartyInvitePlayerEvent
import net.sakuragame.eternal.kirraparty.bukkit.event.compat.PartyMemberTransferEvent
import taboolib.common.platform.event.SubscribeEvent

object FunctionRedisEventListener {

    @SubscribeEvent
    fun e(e: PartyMemberTransferEvent) {
        KirraPartyBukkit.redisManager.publishAsync("KirraParty",
            "main",
            "transfer-player",
            e.leaderUUID.toString(),
            Gson().toJson(e.memberUUIDs.map { it.toString() }))
    }

    @SubscribeEvent
    fun e(e: PartyInvitePlayerEvent) {
        KirraPartyBukkit.redisManager.publishAsync("KirraParty", "main", "invite-request", e.playerUUID, e.message)
    }
}