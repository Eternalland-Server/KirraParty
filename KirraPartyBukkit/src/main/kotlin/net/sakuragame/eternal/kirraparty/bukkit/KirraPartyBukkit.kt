package net.sakuragame.eternal.kirraparty.bukkit

import net.sakuragame.serversystems.manage.client.api.ClientManagerAPI
import taboolib.common.platform.Plugin
import taboolib.module.configuration.Configuration
import taboolib.platform.BukkitPlugin
import java.io.File
import java.util.concurrent.TimeUnit

@Suppress("SpellCheckingInspection")
object KirraPartyBukkit : Plugin() {

    lateinit var conf: Configuration
        private set

    val plugin by lazy {
        BukkitPlugin.getInstance()
    }

    val redisManager by lazy {
        ClientManagerAPI.clientPlugin.redisManager!!
    }

    val redisConn by lazy {
        redisManager.standaloneConn.also {
            it.setTimeout(200, TimeUnit.MILLISECONDS)
        }!!
    }

    override fun onEnable() {
        reload()
    }

    fun reload() {
        val file = File(plugin.dataFolder, "config.yml")
        if (!file.exists()) {
            file.createNewFile()
        }
        conf = Configuration.loadFromFile(file)
    }
}