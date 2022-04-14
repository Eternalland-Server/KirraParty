package net.sakuragame.eternal.kirraparty.bukkit.event.compat

import taboolib.platform.type.BukkitProxyEvent

/**
 * KirraParty
 * net.sakuragame.eternal.kirraparty.bukkit.event.compat.PartyInvitePlayerEvent
 *
 * @author kirraObj
 * @since 2022/1/6 19:33
 */
class PartyInvitePlayerEvent(val playerUUID: String, val message: String) : BukkitProxyEvent()