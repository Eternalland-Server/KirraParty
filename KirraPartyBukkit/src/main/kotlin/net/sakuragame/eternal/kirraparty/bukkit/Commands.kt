package net.sakuragame.eternal.kirraparty.bukkit

import net.sakuragame.eternal.kirraparty.bukkit.party.Party
import net.sakuragame.eternal.kirraparty.bukkit.party.PartyAPI
import net.sakuragame.serversystems.manage.api.user.UserInfo
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.*
import taboolib.expansion.createHelper
import taboolib.module.chat.colored
import java.util.UUID

@Suppress("SpellCheckingInspection")
@CommandHeader(name = "KirraParty", aliases = ["kparty"])
object Commands {

    @CommandBody
    val main = mainCommand {
        createHelper()
    }
    
    @CommandBody(permissionDefault = PermissionDefault.TRUE)
    val test = subCommand {
        execute<CommandSender> { sender, _, argument ->
            sender.sendMessage("Yikes")
        }
    }

    @CommandBody
    val list = subCommand {
        dynamic(commit = "type") {
            execute<CommandSender> { sender, _, argument ->
                when (argument.lowercase()) {
                    "local" -> sender.sendPartyInfoMessage(true)
                    "all" -> sender.sendPartyInfoMessage(false)
                    else -> {
                        Bukkit.dispatchCommand(sender, "kparty list")
                        return@execute
                    }
                }
            }
        }
        execute<CommandSender> { sender, _, _ ->
            arrayOf(
                "&c[System] 执行错误.".colored(),
                "&c[System] &7LOCAL - 查看本服的队伍数据.".colored(),
                "&c[System] &7ALL - 查看所有服务器的队伍数据.".colored()
            ).forEach {
                sender.sendMessage(it)
            }
        }
    }

    @CommandBody
    val reload = subCommand {
        execute<CommandSender> { sender, _, _ ->
            KirraPartyBukkit.reload()
            sender.sendMessage("&c[System] &7Success".colored())
        }
    }

    private fun CommandSender.sendPartyInfoMessage(all: Boolean) {
        when (all) {
            false -> sendPartyInfoMessage(this, parties = PartyAPI.parties)
            true -> sendPartyInfoMessage(this, parties = PartyAPI.getWholeDataFromRedis())
        }
    }

    private fun sendPartyInfoMessage(sender: CommandSender, parties: List<Party>) {
        parties.forEach {
            val leaderInfo = getInfo(it, sender, it.leaderUUID) ?: return@forEach
            sender.sendMessage("&c[System] &7${leaderInfo.username} 的队伍 (编号: ${it.uid})".colored())
            sender.sendMessage("&c[System] &7创建时间: ${it.createdTime.parseFormattedString()}".colored())
            if (it.memberUUIDs.isEmpty()) {
                sender.sendMessage("         &7该队伍没有成员.".colored())
                return@forEach
            }
            sender.sendMessage("         &7队伍成员: ".colored())
            it.memberUUIDs.forEach memberForeach@{ memberUUID ->
                val memberInfo = getInfo(it, sender, memberUUID) ?: return@memberForeach
                sender.sendMessage("          &7- ${memberInfo.username}".colored())
            }
        }
    }

    private fun getInfo(party: Party, sender: CommandSender, uuid: UUID): UserInfo? {
        val playerInfo = uuid.getPlayerInfo()
        if (playerInfo == null) {
            sender.sendMessage("&c[System] &7在获取 ${party.uid} 队伍时出现了一个错误. &f&o(#100)".colored())
            return null
        }
        return playerInfo
    }
}