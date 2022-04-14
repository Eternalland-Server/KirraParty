package net.sakuragame.eternal.kirraparty.bungee

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.md_5.bungee.api.ChatColor
import java.util.*

val GSON = Gson()

fun printToConsole(message: String) = KirraPartyBungee.plugin.logger.fine(message)

fun List<String>.parseSingle() = GSON.toJson(this)!!

fun String.parseList(): List<String> {
    return GSON.fromJson(this, object : TypeToken<List<String>>() {}.type)
}

fun String.parseUUID() = UUID.fromString(this)!!

fun String.colored() = ChatColor.translateAlternateColorCodes('&', this)!!