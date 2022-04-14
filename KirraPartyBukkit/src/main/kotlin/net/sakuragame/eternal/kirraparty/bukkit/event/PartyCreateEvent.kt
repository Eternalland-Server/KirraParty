package net.sakuragame.eternal.kirraparty.bukkit.event

import taboolib.platform.type.BukkitProxyEvent
import java.util.*

/**
 * KirraParty
 * net.sakuragame.eternal.kirraparty.bukkit.event.PartyCreateEvent
 *
 * @author kirraObj
 * @since 2022/1/6 17:07
 */
class PartyCreateEvent(val leaderUUID: UUID, val uid: String) : BukkitProxyEvent()