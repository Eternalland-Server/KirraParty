package net.sakuragame.eternal.kirraparty.bukkit.event

import taboolib.platform.type.BukkitProxyEvent
import java.util.*

/**
 * KirraParty
 * net.sakuragame.eternal.kirraparty.bukkit.event.PartyAddMemberEvent
 *
 * @author kirraObj
 * @since 2022/1/6 18:37
 */
class PartyAddMemberEvent(val uid: String, val memberUUID: UUID) : BukkitProxyEvent()