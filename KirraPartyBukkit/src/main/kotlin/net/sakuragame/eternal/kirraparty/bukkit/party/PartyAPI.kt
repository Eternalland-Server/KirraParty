package net.sakuragame.eternal.kirraparty.bukkit.party

import net.sakuragame.eternal.kirracore.bukkit.KirraCoreBukkitAPI
import net.sakuragame.eternal.kirraparty.bukkit.KirraPartyBukkit
import net.sakuragame.eternal.kirraparty.bukkit.event.PartyCreateEvent
import net.sakuragame.eternal.kirraparty.bukkit.event.compat.PartyMemberTransferEvent
import net.sakuragame.eternal.kirraparty.bukkit.getCurrentTimes
import net.sakuragame.eternal.kirraparty.bukkit.getRandomPartyUID
import org.bukkit.entity.Player
import taboolib.platform.util.sendLang
import java.util.*

@Suppress("MemberVisibilityCanBePrivate", "SpellCheckingInspection")
object PartyAPI {

    val parties = mutableListOf<Party>()

    fun hasParty(player: Player) = getPartyByPlayerUUID(player.uniqueId) != null

    fun getParty(player: Player) = getPartyByPlayerUUID(player.uniqueId)

    fun getPartyByPlayerUUID(playerUUID: UUID) = parties.find { it.getWholeMembers().contains(playerUUID) }

    fun getPartyByStringUID(uid: String) = parties.find { it.uid == uid }

    fun createParty(player: Player) = createParty(player.uniqueId)

    /**
     * 创建队伍.
     * @param isSync 是否为转移数据
     */
    fun createParty(leaderUUID: UUID, isSync: Boolean = false) {
        val partyUID = getRandomPartyUID()
        parties += Party(isSync = isSync, partyUID, leaderUUID, mutableListOf(), getCurrentTimes())
        PartyCreateEvent(leaderUUID, partyUID).call()
    }

    /**
     * 查看玩家是否有邀请组队请求
     *
     * @param player 玩家
     * @return 是否拥有
     */
    fun hasRequest(player: Player): Boolean {
        return KirraPartyBukkit.redisConn.hget("inviteRequests", player.uniqueId.toString()) != null
    }

    /**
     * 执行同意操作
     * @param player 玩家
     */
    fun doRequest(player: Player) {
        val uid = KirraPartyBukkit.redisConn.hget("inviteRequests", player.uniqueId.toString())
        val party = getPartyByStringUID(uid)
        if (party != null) {
            party.addMember(player.uniqueId)
            player.sendLang("message-player-succ-accept", uid)
        } else {
            val redisParty = getWholeDataFromRedis().find { it.uid == uid }
            if (redisParty == null) {
                player.sendLang("message-player-accept-failed")
                return
            }
            KirraPartyBukkit.redisConn.lpush("memberOf:$uid", player.uniqueId.toString())
            val leaderUUID = redisParty.leaderUUID
            val memberUUIDs = redisParty.memberUUIDs.apply {
                add(player.uniqueId)
            }
            PartyMemberTransferEvent(leaderUUID, memberUUIDs).call()
            player.sendLang("message-player-succ-accept", uid)
            player.sendLang("message-invite-summon-trigger")
            KirraCoreBukkitAPI.showDefaultLoadingTitle(player)
        }
    }

    /**
     * 回收数据
     * 包括执行根据职位来自动解散队伍 / 移除成员等操作
     */
    fun recycleData(playerUUID: UUID) {
        val party = getPartyByPlayerUUID(playerUUID) ?: return
        when (party.getPosition(playerUUID)) {
            PartyPosition.LEADER -> party.removeLocal()
            PartyPosition.MEMBER -> party.removeMember(playerUUID)
        }
    }

    /**
     * 从 Redis 里获取玩家的组队数据
     * 这里只检测队长的数据, 因为在设计上非队长传到别的服务器是直接认作退出队伍
     */
    fun getDataFromRedis(leaderUUID: UUID, originUID: String = "", doSummon: Boolean = false): Party? {
        val uid = if (originUID.isEmpty()) {
            originUID
        } else {
            KirraPartyBukkit.redisConn.hget("savedParty", leaderUUID.toString()) ?: return null
        }
        val createTime = KirraPartyBukkit.redisConn.get("createdTime:$uid").toLongOrNull() ?: return null
        val memberUUIDs = KirraPartyBukkit.redisConn.lrange("memberOf:$uid", 0, -1).map { UUID.fromString(it) } as ArrayList<UUID>
        return Party(true, uid, leaderUUID, memberUUIDs, createTime).also {
            if (doSummon) {
                it.doSummon()
            }
        }
    }

    /**
     * 从数据库中获取所有玩家数据
     */
    fun getWholeDataFromRedis(): List<Party> {
        return mutableListOf<Party>().also { partyList ->
            KirraPartyBukkit.redisConn.hgetall("savedParty").forEach { (index, value) ->
                if (index.isNullOrEmpty()) {
                    doRecycle(index)
                    return@forEach
                }
                val leaderUUID = UUID.fromString(index)
                val createdTime = KirraPartyBukkit.redisConn.get("createdTime:$value").toLong()
                val memberUUIDList = KirraPartyBukkit.redisConn.lrange("memberOf:$value", 0, -1).map { UUID.fromString(it) }.toMutableList()
                partyList += Party(true, value, leaderUUID, memberUUIDList, createdTime)
            }
        }
    }

    private fun doRecycle(index: String) {
        KirraPartyBukkit.redisConn.hdel("savedParty", index)
    }
}