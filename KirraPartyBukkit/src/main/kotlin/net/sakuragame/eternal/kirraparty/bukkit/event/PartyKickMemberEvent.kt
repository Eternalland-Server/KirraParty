package net.sakuragame.eternal.kirraparty.bukkit.event

import taboolib.platform.type.BukkitProxyEvent
import java.util.*

/**
 * KirraParty
 * net.sakuragame.eternal.kirraparty.bukkit.event.PartyKickMemberEvent
 *
 * @author kirraObj
 * @since 2022/1/6 18:39
 */
class PartyKickMemberEvent(val uid: String, val playerUUID: UUID) : BukkitProxyEvent()