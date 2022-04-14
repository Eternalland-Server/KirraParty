package net.sakuragame.eternal.kirraparty.bukkit.compat

import com.google.gson.Gson
import net.sakuragame.eternal.kirraparty.bukkit.KirraPartyBukkit
import net.sakuragame.eternal.kirraparty.bukkit.event.compat.PartyInvitePlayerEvent
import net.sakuragame.eternal.kirraparty.bukkit.event.compat.PartyMemberTransferEvent
import net.sakuragame.eternal.kirraparty.bukkit.party.PartyAPI
import net.sakuragame.serversystems.manage.api.redis.RedisMessageListener
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.event.SubscribeEvent
import java.util.*

@Suppress("SpellCheckingInspection")
object RedisManager {

    @Awake(LifeCycle.ACTIVE)
    fun i() {
        KirraPartyBukkit.redisManager.subscribe("KirraParty")
        KirraPartyBukkit.redisManager.registerListener(RedisListener)
    }

    object RedisListener : RedisMessageListener(false, "KirraParty") {

        override fun onMessage(serviceName: String, sourceServer: String, channel: String, messages: Array<String>) {
            when (messages[0]) {
                "transfer-player-failed" -> {
                    val party = PartyAPI.getPartyByStringUID(messages[2]) ?: return
                    when (messages[1]) {
                        "leader-not-found" -> party.disband()
                        "member-not-found" -> party.removeMember(UUID.fromString(messages[3]))
                    }
                    return
                }
            }
        }
    }

    object EventListener {

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
}