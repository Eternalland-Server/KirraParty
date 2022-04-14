package net.sakuragame.eternal.kirraparty.bukkit.event

import taboolib.platform.type.BukkitProxyEvent
import java.util.*

/**
 * KirraParty
 * net.sakuragame.eternal.kirraparty.bukkit.event.PlayerChangeLeaderEvent
 *
 * @author kirraObj
 * @since 2022/1/6 18:41
 */
class PartyChangeLeaderEvent(val uid: String, val oldLeaderUUID: UUID, val newLeaderUUID: UUID) : BukkitProxyEvent()