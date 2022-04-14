package net.sakuragame.eternal.kirraparty.bukkit

import net.sakuragame.serversystems.manage.client.api.ClientManagerAPI
import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import java.util.concurrent.TimeUnit

@Suppress("SpellCheckingInspection")
object KirraPartyBukkit : Plugin() {

    @Config
    lateinit var conf: Configuration
        private set

    val redisManager by lazy {
        ClientManagerAPI.clientPlugin.redisManager!!
    }

    val redisConn by lazy {
        redisManager.standaloneConn.also {
            it.setTimeout(200, TimeUnit.MILLISECONDS)
        }!!
    }
}