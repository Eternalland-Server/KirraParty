package net.sakuragame.eternal.kirraparty.bungee.function.redis

import net.sakuragame.eternal.kirraparty.bungee.parseList
import net.sakuragame.eternal.kirraparty.bungee.parseUUID
import net.sakuragame.serversystems.manage.api.redis.RedisMessageListener
import java.util.*

object FunctionRedisListener : RedisMessageListener(true, "KirraParty"){

    override fun onMessage(serviceName: String, sourceServer: String, channel: String, messages: Array<String>) {
        if (messages.isEmpty()) return
        when (messages[0]) {
            "transfer-player" -> FunctionRedis.doServerTransfer(UUID.fromString(messages[1]), messages[2].parseList().map { UUID.fromString(it) })
            "invite-request" -> FunctionRedis.doInviteRequest(messages[1].parseUUID(), messages[2])
        }
    }
}