package net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore

import com.taylorswiftcn.megumi.uifactory.generate.ui.UIFactory
import net.sakuragame.eternal.dragoncore.network.PacketSender
import net.sakuragame.eternal.justmessage.api.common.InputBox
import net.sakuragame.eternal.justmessage.api.common.NotifyBox
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.screen.PartyScreen
import net.sakuragame.eternal.kirraparty.bukkit.forceParsePlayer
import net.sakuragame.eternal.kirraparty.bukkit.parsePlayer
import net.sakuragame.eternal.kirraparty.bukkit.party.PartyAPI
import org.bukkit.Sound
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit
import taboolib.common5.Baffle
import taboolib.module.chat.colored
import java.util.concurrent.TimeUnit

/**
 * KirraParty
 * net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.FunctionDCListener
 *
 * @author kirraObj
 * @since 2022/1/6 19:39
 */
@Suppress("SpellCheckingInspection")
object DCoreParty {

    val baffle by lazy {
        Baffle.of(1, TimeUnit.SECONDS)
    }

    val partyGuiTitle = "&6&l组队".colored()

    const val triggerKey = "I"

    const val partyDisbandNotifyBoxKey = "KirraParty_DisbandNotifyBox"
    const val partyPromoteNotifyBoxKey = "KirraParty_PromoteNotifyBox"
    const val partyCreateNotifyBoxKey = "KirraParty_IfCreateNotifyBox"
    const val partyKickNotifyBoxKey = "KirraParty_KickNotifyBox"
    const val partyLeftNotifyBoxKey = "KirraParty_LeftNotifyBox"

    const val partyInviteInputBoxKey = "KirraParty_InviteInputBox"

    val openMenuSound = Sound.BLOCK_ENDERCHEST_OPEN
    val buttonSound = Sound.UI_BUTTON_CLICK
    val confirmSound = Sound.BLOCK_NOTE_PLING
    val cancelSound = Sound.BLOCK_NOTE_BASS

    fun open(player: Player) {
        UIFactory.open(player, PartyScreen.mainUI)
        updateDragonCoreVars(player)
    }

    fun updateDragonCoreVars(player: Player) {
        submit(async = true, delay = 2) {
            val variableMap = when (PartyAPI.hasParty(player)) {
                true -> mutableMapOf<String, String>().also {
                    val party = PartyAPI.getParty(player)!!
                    it["kparty_available"] = "true"

                    it["kparty_uid"] = party.uid

                    it["kparty_latest_message"] = ""

                    val leaderUUID = party.leaderUUID.toString()
                    val leaderPlayer = party.leaderUUID.get().forceParsePlayer()
                    it["kparty_model_leader"] = leaderUUID
                    it["kparty_name_leader"] = leaderPlayer.name

                    it["kparty_leader_health_percent"] = (leaderPlayer.health / leaderPlayer.maxHealth).toString()

                    val member1Player = party.getMemberByOrder(1)?.parsePlayer()
                    it["kparty_model_member1"] = party.getMemberByOrder(1)?.toString() ?: ""
                    it["kparty_name_member1"] = party.getMemberByOrder(1)?.forceParsePlayer()?.name ?: ""

                    it["kparty_member1_health_percent"] = if (member1Player != null) {
                        (member1Player.health / member1Player.maxHealth).toString()
                    } else {
                        ""
                    }

                    val member2Player = party.getMemberByOrder(2)?.parsePlayer()
                    it["kparty_model_member2"] = party.getMemberByOrder(2)?.toString() ?: ""
                    it["kparty_name_member2"] = party.getMemberByOrder(2)?.forceParsePlayer()?.name ?: ""

                    it["kparty_member_2_health_percent"] = if (member2Player != null) {
                        (member2Player.health / member2Player.maxHealth).toString()
                    } else {
                        ""
                    }

                    val member3Player = party.getMemberByOrder(2)?.parsePlayer()
                    it["kparty_name_member3"] = party.getMemberByOrder(3)?.forceParsePlayer()?.name ?: ""
                    it["kparty_model_member3"] = party.getMemberByOrder(3)?.toString() ?: ""

                    it["kparty_member_3_health_percent"] = if (member3Player != null) {
                        (member3Player.health / member3Player.maxHealth).toString()
                    } else {
                        ""
                    }

                    it["kparty_visible_member1"] = (party.memberUUIDs.size >= 1).toString()
                    it["kparty_visible_member2"] = (party.memberUUIDs.size >= 2).toString()
                    it["kparty_visible_member3"] = (party.memberUUIDs.size >= 3).toString()

                    it["kparty_member_size"] = "小队人数: (${party.getWholeMembers().size}/4)"
                }
                else -> mutableMapOf<String, String>().also {
                    it["kparty_available"] = "false"
                    it["kparty_visible_member1"] = "false"
                    it["kparty_visible_member2"] = "false"
                    it["kparty_visible_member3"] = "false"
                }
            }
            PacketSender.sendSyncPlaceholder(player, variableMap)
        }
    }

    fun openKickNotifyBox(player: Player, compID: String): NotifyBox {
        return NotifyBox("$partyKickNotifyBoxKey;$compID", partyGuiTitle, listOf("", "是否踢出该玩家?", "")).apply {
            open(player, false)
        }
    }

    fun openChangeLeaderNotifyBox(player: Player, compID: String): NotifyBox {
        return NotifyBox("$partyPromoteNotifyBoxKey;$compID", partyGuiTitle, listOf("", "是否变更该玩家为当前队伍队长?", "")).apply {
            open(player, false)
        }
    }

    fun openLeftNotifyBox(player: Player): NotifyBox {
        return NotifyBox(partyLeftNotifyBoxKey, partyGuiTitle, listOf("", "是否退出该队伍?", "")).apply {
            open(player, false)
        }
    }

    fun openDisbandNotifyBox(player: Player): NotifyBox {
        return NotifyBox(partyDisbandNotifyBoxKey, partyGuiTitle, listOf("", "是否解散队伍?", "")).apply {
            open(player, false)
        }
    }

    fun openInviteInputBox(player: Player): InputBox {
        return InputBox(partyInviteInputBoxKey, partyGuiTitle, "请输入被邀请玩家的游戏名称").also {
            it.setConfirmText("邀请")
            it.setCancelText("取消")
            it.open(player, false)
        }
    }
}