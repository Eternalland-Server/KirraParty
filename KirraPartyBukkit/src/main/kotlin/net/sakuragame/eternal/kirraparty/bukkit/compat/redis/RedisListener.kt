package net.sakuragame.eternal.kirraparty.bukkit.compat.redis

import net.sakuragame.eternal.kirraparty.bukkit.party.PartyAPI
import net.sakuragame.serversystems.manage.api.redis.RedisMessageListener
import java.util.*

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