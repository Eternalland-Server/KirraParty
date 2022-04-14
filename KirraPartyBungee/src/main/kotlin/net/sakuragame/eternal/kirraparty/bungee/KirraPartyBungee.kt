package net.sakuragame.eternal.kirraparty.bungee

import net.sakuragame.serversystems.manage.proxy.api.ProxyManagerAPI
import taboolib.common.platform.Plugin
import taboolib.platform.BungeePlugin

@Suppress("SpellCheckingInspection")
object KirraPartyBungee : Plugin() {

    val redisManager by lazy {
        ProxyManagerAPI.getRedisManager()!!
    }

    val redisConn by lazy {
        redisManager.standaloneConn!!
    }

    val plugin by lazy {
        BungeePlugin.getInstance()
    }
}