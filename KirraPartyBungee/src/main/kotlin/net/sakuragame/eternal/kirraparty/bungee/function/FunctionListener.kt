package net.sakuragame.eternal.kirraparty.bungee.function

import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.sakuragame.eternal.kirraparty.bungee.KirraPartyBungee
import net.sakuragame.eternal.kirraparty.bungee.printToConsole
import taboolib.common.platform.event.SubscribeEvent

object FunctionListener {

    @SubscribeEvent
    fun e(e: PlayerDisconnectEvent) {
        val player = e.player
        KirraPartyBungee.redisConn.async().hget("savedParty", player.uniqueId.toString()).thenAccept {
            if (it == null) return@thenAccept
            KirraPartyBungee.redisConn.async().hdel("savedParty", player.uniqueId.toString())
            KirraPartyBungee.redisConn.async().del("createdTime:$it")
            KirraPartyBungee.redisConn.async().del("memberOf:$it")
            printToConsole("已将 ${player.name} 的队伍删除.")
        }
        KirraPartyBungee.redisConn.async().hdel("inviteRequests", e.player.uniqueId.toString())
        printToConsole("已将 ${player.name} 的缓存信息删除.")
    }
}