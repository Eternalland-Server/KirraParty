package net.sakuragame.eternal.kirraparty.bukkit.event.compat

import taboolib.platform.type.BukkitProxyEvent
import java.util.*

/**
 * KirraParty
 * net.sakuragame.eternal.kirraparty.bukkit.event.compat.PartyMemberTransferEvent
 *
 * @author kirraObj
 * @since 2022/1/6 19:35
 */
class PartyMemberTransferEvent(val leaderUUID: UUID, val memberUUIDs: List<UUID>) : BukkitProxyEvent()