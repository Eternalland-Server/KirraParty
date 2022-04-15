package net.sakuragame.eternal.kirraparty.bukkit.compat.redis

import net.sakuragame.eternal.kirraparty.bukkit.KirraPartyBukkit
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

object RedisManager {

    @Awake(LifeCycle.ACTIVE)
    fun i() {
        KirraPartyBukkit.redisManager.subscribe("KirraParty")
        KirraPartyBukkit.redisManager.registerListener(RedisListener)
    }
}