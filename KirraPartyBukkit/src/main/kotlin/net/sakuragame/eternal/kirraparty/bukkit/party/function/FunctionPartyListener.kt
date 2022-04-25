package net.sakuragame.eternal.kirraparty.bukkit.party.function

import net.sakuragame.eternal.kirraparty.bukkit.KirraPartyBukkit
import net.sakuragame.eternal.kirraparty.bukkit.party.PartyAPI
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.event.SubscribeEvent

object FunctionPartyListener {

    /**
     * 数据回收.
     */
    @SubscribeEvent
    fun e(e: PlayerQuitEvent) {
        PartyAPI.recycleData(e.player.uniqueId)
    }

    /**
     * 数据回收.
     */
    @SubscribeEvent
    fun e(e: PlayerKickEvent) {
        PartyAPI.recycleData(e.player.uniqueId)
    }

    /**
     * 跨服数据同步.
     */
    @SubscribeEvent
    fun e(e: PlayerJoinEvent) {
        val player = e.player
        // 玩家通过邀请进入服务器.
        val inviteUID = KirraPartyBukkit.redisConn.sync().hget("inviteRequests", player.uniqueId.toString()) ?: return
        val localParty = PartyAPI.getPartyByStringUID(inviteUID)
        if (localParty != null) {
            KirraPartyBukkit.redisConn.async().hdel("inviteRequests", player.uniqueId.toString())
            localParty.addMember(player.uniqueId)
            return
        }
        val uid = KirraPartyBukkit.redisConn.sync().hget("savedParty", player.uniqueId.toString())
        if (uid.isNullOrEmpty()) return
        PartyAPI.parties += PartyAPI.getDataFromRedis(player.uniqueId, uid, true) ?: return
    }
}