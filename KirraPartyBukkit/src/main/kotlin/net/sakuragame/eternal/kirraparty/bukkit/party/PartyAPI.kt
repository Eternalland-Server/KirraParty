package net.sakuragame.eternal.kirraparty.bukkit.party

import com.google.common.util.concurrent.Atomics
import net.sakuragame.eternal.kirraparty.bukkit.KirraPartyBukkit
import net.sakuragame.eternal.kirraparty.bukkit.event.PartyCreateEvent
import net.sakuragame.eternal.kirraparty.bukkit.getCurrentTimes
import net.sakuragame.eternal.kirraparty.bukkit.getRandomPartyUID
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.atomic.AtomicReference

@Suppress("MemberVisibilityCanBePrivate")
object PartyAPI {

    val parties = mutableListOf<Party>()

    fun hasParty(player: Player) = getPartyByPlayerUUID(player.uniqueId) != null

    fun getParty(player: Player) = getPartyByPlayerUUID(player.uniqueId)

    fun getPartyByPlayerUUID(playerUUID: UUID) = parties.find { it.getWholeMembers().contains(playerUUID) }

    fun getPartyByStringUID(uid: String) = parties.find { it.uid == uid }

    fun createParty(player: Player) = createParty(player.uniqueId)

    /**
     * 创建队伍.
     * @param isSync 是否为转移数据.
     */
    fun createParty(leaderUUID: UUID, isSync: Boolean = false) {
        val partyUID = getRandomPartyUID()
        parties += Party(isSync = isSync, partyUID, Atomics.newReference(leaderUUID), mutableListOf(), getCurrentTimes())
        PartyCreateEvent(leaderUUID, partyUID).call()
    }

    /**
     * 回收数据.
     * 包括执行根据职位来自动解散队伍 / 移除成员等操作.
     */
    fun recycleData(playerUUID: UUID) {
        val party = getPartyByPlayerUUID(playerUUID) ?: return
        when (party.getPosition(playerUUID)) {
            PartyPosition.LEADER -> party.removeLocal()
            PartyPosition.MEMBER -> party.removeMember(playerUUID)
        }
    }

    /**
     * 从 Redis 里获取玩家的组队数据.
     * 这里只检测队长的数据, 因为在设计上非队长传到别的服务器是直接算退出队伍的.
     */
    fun getDataFromRedis(leaderUUID: UUID, originUID: String = "", doSummon: Boolean = false): Party? {
        val uid = if (originUID.isEmpty()) {
            originUID
        } else {
            KirraPartyBukkit.redisConn.sync().hget("savedParty", leaderUUID.toString()) ?: return null
        }
        val createTime = KirraPartyBukkit.redisConn.sync().get("createdTime:$uid").toLongOrNull() ?: return null
        val memberUUIDs = KirraPartyBukkit.redisConn.sync().lrange("memberOf:$uid", 0, -1).map { UUID.fromString(it) } as ArrayList<UUID>
        return Party(true, uid, AtomicReference(leaderUUID), memberUUIDs, createTime).also {
            if (doSummon) {
                it.doSummon()
            }
        }
    }

    /**
     * 从数据库中获取所有玩家数据.
     */
    fun getWholeDataFromRedis(): List<Party> {
        return mutableListOf<Party>().also { partyList ->
            KirraPartyBukkit.redisConn.sync().hgetall("savedParty").forEach { (index, value) ->
                val leaderUUID = Atomics.newReference<UUID>().also {
                    it.set(UUID.fromString(index))
                }
                val createdTime = KirraPartyBukkit.redisConn.sync().get("createdTime:$value").toLong()
                val memberUUIDList = KirraPartyBukkit.redisConn.sync().lrange("memberOf:$value", 0, -1).map { UUID.fromString(it) } as ArrayList<UUID>
                partyList += Party(true, value, leaderUUID, memberUUIDList, createdTime)
            }
        }
    }
}