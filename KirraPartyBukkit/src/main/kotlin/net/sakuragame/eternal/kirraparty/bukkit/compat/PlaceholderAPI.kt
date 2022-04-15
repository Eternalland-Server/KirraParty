package net.sakuragame.eternal.kirraparty.bukkit.compat

import net.sakuragame.eternal.kirraparty.bukkit.parseFormattedString
import net.sakuragame.eternal.kirraparty.bukkit.party.PartyAPI
import net.sakuragame.serversystems.manage.client.api.ClientManagerAPI
import org.bukkit.entity.Player
import taboolib.platform.compat.PlaceholderExpansion

/**
 * KirraParty
 * net.sakuragame.eternal.kirraparty.bukkit.compat.PlaceholderAPI
 *
 * @author kirraObj
 * @since 2022/1/6 19:24
 */
@Suppress("SpellCheckingInspection")
object PlaceholderAPI : PlaceholderExpansion {

    override val identifier = "KirraParty".lowercase()

    override fun onPlaceholderRequest(player: Player?, args: String): String {
        if (player == null) return "__"
        val party = PartyAPI.getParty(player) ?: return "__"
        return when (args.lowercase()) {
            "uid" -> party.uid
            "leader" -> ClientManagerAPI.getUserName(party.leaderUUID.get())!!
            "member_1" -> (if (party.memberUUIDs.size >= 1) ClientManagerAPI.getUserName(party.memberUUIDs[0]) else "无")!!
            "member_2" -> (if (party.memberUUIDs.size >= 2) ClientManagerAPI.getUserName(party.memberUUIDs[1]) else "无")!!
            "member_3" -> (if (party.memberUUIDs.size >= 3) ClientManagerAPI.getUserName(party.memberUUIDs[2]) else "无")!!
            "created_time" -> party.createdTime.parseFormattedString()
            else -> "__"
        }
    }
}