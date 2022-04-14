package net.sakuragame.eternal.kirraparty.bukkit

import net.sakuragame.eternal.kirraparty.bukkit.KirraPartyBukkit.redisConn
import net.sakuragame.serversystems.manage.client.api.ClientManagerAPI
import org.apache.commons.lang.time.DateFormatUtils
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.text.DecimalFormat
import java.util.*

private val uidRange = (0..9999)
private val decimalFormat = DecimalFormat("0000")

private fun Int.parseToUIDFormat() = decimalFormat.format(this)

fun UUID.forceParsePlayer() = Bukkit.getPlayer(this)!!

fun UUID.parsePlayer(): Player? = Bukkit.getPlayer(this)

fun UUID.getPlayerInfo() = ClientManagerAPI.getUserInfo(this)

fun getCurrentTimes() = System.currentTimeMillis()

fun getExistsUID() = redisConn.sync().hgetall("savedParty").values.map { it.toInt() }

fun getRandomPartyUID(): String {
    val existsUID = getExistsUID()
    return uidRange.first { !existsUID.contains(it) }.parseToUIDFormat()
}

fun Long.parseFormattedString() = DateFormatUtils.format(this, "MM月dd日 HH时mm分.")!!