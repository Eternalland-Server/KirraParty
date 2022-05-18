package net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore

import com.taylorswiftcn.megumi.uifactory.event.comp.UIFCompClickEvent
import net.sakuragame.eternal.dragoncore.api.KeyPressEvent
import net.sakuragame.eternal.dragoncore.api.event.YamlSendToPlayerEvent
import net.sakuragame.eternal.dragoncore.config.FolderType
import net.sakuragame.eternal.dragoncore.network.PacketSender
import net.sakuragame.eternal.justmessage.api.MessageAPI
import net.sakuragame.eternal.justmessage.api.common.NotifyBox
import net.sakuragame.eternal.justmessage.api.event.input.InputBoxCancelEvent
import net.sakuragame.eternal.justmessage.api.event.input.InputBoxConfirmEvent
import net.sakuragame.eternal.justmessage.api.event.notify.NotifyBoxCancelEvent
import net.sakuragame.eternal.justmessage.api.event.notify.NotifyBoxConfirmEvent
import net.sakuragame.eternal.kirraparty.bukkit.KirraPartyBukkit
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty.baffle
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty.buttonSound
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty.cancelSound
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty.confirmSound
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty.openChangeLeaderNotifyBox
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty.openDisbandNotifyBox
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty.openInviteInputBox
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty.openKickNotifyBox
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty.openLeftNotifyBox
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty.openMainUI
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty.partyCreateNotifyBoxKey
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty.partyDisbandNotifyBoxKey
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty.partyGuiTitle
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty.partyInviteInputBoxKey
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty.partyKickNotifyBoxKey
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty.partyLeftNotifyBoxKey
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty.partyPromoteNotifyBoxKey
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty.updateDragonCoreVars
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.screen.PartyScreen.leftStatusHud
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.screen.PartyScreen.mainUI
import net.sakuragame.eternal.kirraparty.bukkit.forceParsePlayer
import net.sakuragame.eternal.kirraparty.bukkit.party.Party
import net.sakuragame.eternal.kirraparty.bukkit.party.PartyAPI
import net.sakuragame.eternal.kirraparty.bukkit.party.PartyPosition
import org.bukkit.Bukkit
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit
import taboolib.platform.util.asLangText
import java.util.concurrent.atomic.AtomicBoolean

@Suppress("SpellCheckingInspection", "DuplicatedCode")
object FunctionPartyListener {

    /**
     * 当玩家按下队伍按键.
     *
     * @param e 事件.
     */
    @SubscribeEvent
    fun e(e: KeyPressEvent) {
        if (e.key != DCoreParty.triggerKey) return
        val player = e.player
        if (!baffle.hasNext(player.name)) {
            return
        }
        baffle.next(player.name)
        if (!KirraPartyBukkit.conf.getBoolean("allowed-create-party")) {
            MessageAPI.sendActionTip(player, player.asLangText("message-disallow-create-party"))
            return
        }
        if (PartyAPI.hasParty(player)) {
            DCoreParty.open(player)
            return
        }
        player.playSound(player.location, buttonSound, 1f, 1f)
        NotifyBox(partyCreateNotifyBoxKey, partyGuiTitle, listOf("", "是否创建队伍?", "")).apply {
            open(player, false)
        }
    }

    @SubscribeEvent
    fun onClickMainUI(e: UIFCompClickEvent) {
        if (e.screenID != mainUI.id) return
        val compID = e.compID
        val player = e.player
        player.closeInventory()
        // invite button.
        if (compID.startsWith("invite")) openInviteInputBox(player)
        // disband button.
        if (compID == "member1_button") openDisbandNotifyBox(player)
        // promote button.
        if (compID.endsWith("left_button")) openChangeLeaderNotifyBox(player, compID)
        // kick button.
        if (compID.endsWith("right_button")) openKickNotifyBox(player, compID)
    }

    @SubscribeEvent
    fun e(e: YamlSendToPlayerEvent) {
        val player = e.player
        submit(async = true, delay = 10L) {
            if (PartyAPI.hasParty(player)) {
                PacketSender.sendYaml(player, FolderType.Gui, leftStatusHud.id, leftStatusHud.build(player))
            }
        }
    }

    @SubscribeEvent
    fun onClickLeftStatusHud(e: UIFCompClickEvent) {
        if (e.screenID != leftStatusHud.id) return
        val player = e.player
        val party = PartyAPI.getParty(player) ?: return
        player.closeInventory()
        when (e.compID) {
            "team_invite" -> openInviteInputBox(player)
            "team_setting" -> DCoreParty.open(player)
            "team_quit" -> if (party.getPosition(player.uniqueId) == PartyPosition.LEADER) {
                openDisbandNotifyBox(player)
            } else {
                openLeftNotifyBox(player)
            }
        }
    }

    @SubscribeEvent
    fun onConfirmCreate(e: NotifyBoxConfirmEvent) {
        if (e.key != partyCreateNotifyBoxKey) return
        val player = e.player
        PartyAPI.createParty(player)
        MessageAPI.sendActionTip(player, player.asLangText("message-player-succ-create-party"))
        submit(delay = 2L, async = true) {
            openMainUI(player)
        }
    }

    @SubscribeEvent
    fun onConfirmInvite(e: InputBoxConfirmEvent) {
        if (e.key != partyInviteInputBoxKey) return
        val player = e.player
        val party = PartyAPI.getParty(player) ?: return
        val content = e.content
        if (player.name == content) {
            MessageAPI.sendActionTip(player, player.asLangText("message-player-cannot-invite-self"))
            return
        }
        val isSucc = party.sendInviteRequest(player, content)
        e.player.playSound(e.player.location, confirmSound, 1f, 1f)
        if (isSucc) {
            MessageAPI.sendActionTip(player, player.asLangText("message-player-succ-sent-invite-request", e.content))
        } else {
            MessageAPI.sendActionTip(player, player.asLangText("message-player-not-found"))
        }
        player.closeInventory()
    }

    @SubscribeEvent
    fun onConfirmDisband(e: NotifyBoxConfirmEvent) {
        if (e.key != partyDisbandNotifyBoxKey) return
        val player = e.player
        val party = PartyAPI.getParty(player) ?: return
        party.disband()
        MessageAPI.sendActionTip(player, player.asLangText("message-player-succ-disband"))
    }

    @SubscribeEvent
    fun onConfirmLeft(e: NotifyBoxConfirmEvent) {
        if (e.key != partyLeftNotifyBoxKey) return
        val player = e.player
        val party = PartyAPI.getParty(player) ?: return
        party.removeMember(player.uniqueId)
        MessageAPI.sendActionTip(player, player.asLangText("message-player-succ-left"))
    }

    @SubscribeEvent
    fun onConfirmPromote(e: NotifyBoxConfirmEvent) {
        if (!e.key.startsWith(partyPromoteNotifyBoxKey)) return
        val player = e.player
        val party = PartyAPI.getParty(player) ?: return
        val playerUUID = getPlayerUUIDByData(e.key, party) ?: return
        party.changeLeader(playerUUID)
        MessageAPI.sendActionTip(player, player.asLangText("message-player-succ-set-leader", Bukkit.getPlayer(playerUUID).displayName))
    }

    @SubscribeEvent
    fun onConfirmKick(e: NotifyBoxConfirmEvent) {
        if (!e.key.startsWith(partyKickNotifyBoxKey)) return
        val player = e.player
        val party = PartyAPI.getParty(player) ?: return
        val playerUUID = getPlayerUUIDByData(e.key, party) ?: return
        party.removeMember(playerUUID)
        MessageAPI.sendActionTip(player, player.asLangText("command-party-succ-kick"))
    }

    @SubscribeEvent
    fun onNotifyConfirm(e: NotifyBoxConfirmEvent) {
        val bool = AtomicBoolean(false)
        when (e.key) {
            partyLeftNotifyBoxKey, partyPromoteNotifyBoxKey, partyDisbandNotifyBoxKey, partyCreateNotifyBoxKey, partyKickNotifyBoxKey -> {
                bool.set(true)
            }
        }
        if (e.key.startsWith(partyKickNotifyBoxKey) || e.key.startsWith(partyPromoteNotifyBoxKey)) bool.set(true)
        if (bool.get()) {
            e.player.playSound(e.player.location, confirmSound, 1f, 1f)
            e.player.closeInventory()
            updateDragonCoreVars(e.player)
        }
    }

    @SubscribeEvent
    fun onInputCancel(e: InputBoxCancelEvent) {
        when (e.key) {
            partyInviteInputBoxKey -> {
                e.player.playSound(e.player.location, cancelSound, 1f, 1f)
                updateDragonCoreVars(e.player)
            }
        }
    }

    @SubscribeEvent
    fun onNotifyCancel(e: NotifyBoxCancelEvent) {
        val bool = AtomicBoolean(false)
        when (e.key) {
            partyLeftNotifyBoxKey, partyPromoteNotifyBoxKey, partyDisbandNotifyBoxKey, partyCreateNotifyBoxKey, partyKickNotifyBoxKey -> {
                bool.set(true)
            }
        }
        if (e.key.startsWith(partyKickNotifyBoxKey) || e.key.startsWith(partyPromoteNotifyBoxKey)) bool.set(true)
        if (bool.get()) {
            e.player.playSound(e.player.location, cancelSound, 1f, 1f)
            e.player.closeInventory()
            updateDragonCoreVars(e.player)
        }
    }

    private fun getPlayerUUIDByData(key: String, party: Party) = when (key.split(";")[1]) {
        "member2_left_button" -> party.getMemberByOrder(1)!!.forceParsePlayer().uniqueId
        "member3_left_button" -> party.getMemberByOrder(2)!!.forceParsePlayer().uniqueId
        "member4_left_button" -> party.getMemberByOrder(3)!!.forceParsePlayer().uniqueId
        else -> null
    }
}