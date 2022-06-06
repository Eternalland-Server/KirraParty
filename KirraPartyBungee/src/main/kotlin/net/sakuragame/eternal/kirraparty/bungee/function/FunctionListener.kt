package net.sakuragame.eternal.kirraparty.bungee.function

import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.sakuragame.eternal.kirraparty.bungee.KirraPartyBungee
import net.sakuragame.eternal.kirraparty.bungee.printToConsole
import taboolib.common.platform.event.SubscribeEvent

object FunctionListener {

    fun onClose() {
        KirraPartyBungee.redisConn.hgetall("savedParty").keys.forEach {
            KirraPartyBungee.redisConn.hdel("savedParty", it)
            printToConsole("已删除遗留数据: $it (savedParty)")
        }
        KirraPartyBungee.redisConn.hgetall("inviteRequests").keys.forEach {
            KirraPartyBungee.redisConn.hdel("inviteRequests", it)
            printToConsole("已删除遗留数据: $it (inviteRequests)")
        }
    }

    @SubscribeEvent
    fun e(e: PlayerDisconnectEvent) {
        val player = e.player
        KirraPartyBungee.redisConn.hget("savedParty", player.uniqueId.toString()).also {
            if (it == null) return
            KirraPartyBungee.redisConn.hdel("savedParty", player.uniqueId.toString())
            KirraPartyBungee.redisConn.del("createdTime:$it")
            KirraPartyBungee.redisConn.del("memberOf:$it")
            printToConsole("已将 ${player.name} 的队伍删除.")
        }
        KirraPartyBungee.redisConn.hdel("inviteRequests", e.player.uniqueId.toString())
        printToConsole("已将 ${player.name} 的缓存信息删除.")
    }
}