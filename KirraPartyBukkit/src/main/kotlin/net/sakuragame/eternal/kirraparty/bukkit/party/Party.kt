package net.sakuragame.eternal.kirraparty.bukkit.party

import net.sakuragame.eternal.kirraparty.bukkit.KirraPartyBukkit
import net.sakuragame.eternal.kirraparty.bukkit.event.PartyAddMemberEvent
import net.sakuragame.eternal.kirraparty.bukkit.event.PartyChangeLeaderEvent
import net.sakuragame.eternal.kirraparty.bukkit.event.PartyDisbandEvent
import net.sakuragame.eternal.kirraparty.bukkit.event.PartyKickMemberEvent
import net.sakuragame.eternal.kirraparty.bukkit.event.compat.PartyInvitePlayerEvent
import net.sakuragame.eternal.kirraparty.bukkit.event.compat.PartyMemberTransferEvent
import net.sakuragame.eternal.kirraparty.bukkit.getRandomPartyUID
import net.sakuragame.kirracore.bukkit.util.Lang
import net.sakuragame.serversystems.manage.client.api.ClientManagerAPI
import org.bukkit.Bukkit
import org.bukkit.Bukkit.getConsoleSender
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit
import taboolib.module.chat.colored
import taboolib.platform.util.asLangText
import java.util.*
import java.util.concurrent.atomic.AtomicReference

data class Party(
    val isSync: Boolean = false,
    val uid: String = getRandomPartyUID(),
    val leaderUUID: AtomicReference<UUID>,
    val memberUUIDs: MutableList<UUID>,
    val createdTime: Long,
) {

    init {
        if (!isSync) {
            KirraPartyBukkit.redisConn.async().hset("savedParty", leaderUUID.toString(), uid)
            KirraPartyBukkit.redisConn.async().set("createdTime:$uid", createdTime.toString())
        }
    }

    fun sendInviteRequest(sender: Player, name: String): Boolean {
        if (name.isEmpty()) return false
        val invitedPlayerUUID = ClientManagerAPI.getUserUUID(name) ?: return false
        PartyAPI.getWholeDataFromRedis().forEach {
            if (it.memberUUIDs.contains(invitedPlayerUUID)) {
                Lang.sendMessage(sender, sender.asLangText("message-player-already-in-party"))
                return false
            }
        }
        PartyInvitePlayerEvent(invitedPlayerUUID.toString(), sender.asLangText("message-invite-by-player", sender.name))
        KirraPartyBukkit.redisConn.async().hset("inviteRequests", invitedPlayerUUID.toString(), uid)
        return true
    }

    fun getPosition(uuid: UUID): PartyPosition {
        if (leaderUUID.get() == uuid) return PartyPosition.LEADER
        return PartyPosition.MEMBER
    }

    fun getWholeMembers() = mutableListOf<UUID>().apply {
        add(leaderUUID.get())
        addAll(memberUUIDs)
    }

    fun addMember(playerUUID: UUID) {
        val playerName = ClientManagerAPI.getUserName(playerUUID) ?: return
        sendMessage(getConsoleSender().asLangText("message-auto-joined-team", playerName))

        memberUUIDs += playerUUID

        KirraPartyBukkit.redisConn.async().lpush("memberOf:$uid", playerUUID.toString())
        PartyAddMemberEvent(uid, playerUUID).call()
    }

    fun removeMember(playerUUID: UUID) {
        val playerName = ClientManagerAPI.getUserName(playerUUID) ?: return
        sendMessage(getConsoleSender().asLangText("message-auto-kicked-member", playerName))

        memberUUIDs -= playerUUID

        KirraPartyBukkit.redisConn.async().lrem("memberOf:$uid", 1, playerUUID.toString())
        PartyKickMemberEvent(uid, playerUUID).call()
    }

    fun changeLeader(newLeaderUUID: UUID) {
        val playerName = ClientManagerAPI.getUserName(newLeaderUUID) ?: return
        val oldLeaderUUID = leaderUUID.get()

        PartyChangeLeaderEvent(uid, oldLeaderUUID, newLeaderUUID).call()
        sendMessage(getConsoleSender().asLangText("message-leader-has-changed", playerName))

        leaderUUID.set(newLeaderUUID)

        removeMember(newLeaderUUID)
        addMember(oldLeaderUUID)

        KirraPartyBukkit.redisConn.async().hset("savedParty", newLeaderUUID.toString(), uid)
    }

    fun getMemberByOrder(order: Int): UUID? {
        if (memberUUIDs.size < order) return null
        return memberUUIDs[order - 1]
    }

    fun doSummon() {
        if (memberUUIDs.isEmpty()) {
            return
        }
        submit(delay = 5L) {
            PartyMemberTransferEvent(leaderUUID.get(), memberUUIDs).call()
        }
    }

    fun sendMessage(message: String) {
        getWholeMembers().forEach {
            val player = Bukkit.getPlayer(it) ?: return@forEach
            player.sendMessage(message.colored())
        }
    }

    fun disband() {
        PartyDisbandEvent(uid).call()
        removeLocal()
        KirraPartyBukkit.redisConn.async().hdel("savedParty", leaderUUID.toString())
        KirraPartyBukkit.redisConn.async().del("createdTime:$uid")
        KirraPartyBukkit.redisConn.async().del("memberOf:$uid")
    }

    fun removeLocal() = PartyAPI.parties.remove(this)
}
