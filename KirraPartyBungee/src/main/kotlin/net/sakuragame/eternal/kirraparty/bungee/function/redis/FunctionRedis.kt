package net.sakuragame.eternal.kirraparty.bungee.function.redis

import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.sakuragame.eternal.kirraparty.bungee.KirraPartyBungee
import java.util.*

@Suppress("SpellCheckingInspection")
object FunctionRedis {

    fun doServerTransfer(leaderUUID: UUID, memberUUIDList: List<UUID>) {
        val leader = KirraPartyBungee.plugin.proxy.getPlayer(leaderUUID)
        if (leader == null) {
            KirraPartyBungee.redisManager.publishAsync("KirraParty", "main", "transfer-player-failed", getParty(leaderUUID), "leader-not-found", leaderUUID.toString())
        }
        memberUUIDList.forEach {
            KirraPartyBungee.plugin.proxy.getPlayer(it).connect(leader.server.info) { isSucc, _ ->
                if (!isSucc) {
                    KirraPartyBungee.redisManager.publishAsync("KirraParty", "main", "transfer-player-failed", getParty(leaderUUID), "member-not-found", it.toString())
                }
            }
        }
    }

    fun doInviteRequest(uuid: UUID, rawMessage: String) {
        val player = KirraPartyBungee.plugin.proxy.getPlayer(uuid)
        val message = TextComponent(rawMessage).also {
            it.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, arrayOf(TextComponent("&7点击加入该队伍.")))
            it.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kParty acceptInvite")

        }
        player.sendMessage(message)
    }

    private fun getParty(leaderUUID: UUID) = KirraPartyBungee.redisConn.hget("savedParty", leaderUUID.toString()) ?: ""
}