package net.sakuragame.eternal.kirraparty.bungee

import com.lambdaworks.redis.api.sync.RedisCommands
import net.sakuragame.eternal.kirraparty.bungee.function.FunctionListener
import net.sakuragame.eternal.kirraparty.bungee.function.redis.FunctionRedisListener
import net.sakuragame.serversystems.manage.proxy.api.ProxyManagerAPI
import taboolib.common.platform.Plugin
import taboolib.platform.BungeePlugin

@Suppress("SpellCheckingInspection")
object KirraPartyBungee : Plugin() {

    val redisManager by lazy {
        ProxyManagerAPI.getRedisManager()!!
    }

    val redisConn: RedisCommands<String, String>
        get() = redisManager.pooledConn!!

    val plugin by lazy {
        BungeePlugin.getInstance()
    }

    override fun onDisable() {
        FunctionListener.onClose()
    }

    override fun onEnable() {
        redisManager.subscribe("KirraParty")
        redisManager.registerListener(FunctionRedisListener)
    }
}