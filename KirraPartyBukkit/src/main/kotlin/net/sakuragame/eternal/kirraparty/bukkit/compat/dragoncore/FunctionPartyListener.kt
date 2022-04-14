package net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore

import com.taylorswiftcn.megumi.uifactory.event.comp.UIFCompClickEvent
import net.sakuragame.eternal.dragoncore.api.KeyPressEvent
import net.sakuragame.eternal.justmessage.api.MessageAPI
import net.sakuragame.eternal.justmessage.api.common.NotifyBox
import net.sakuragame.eternal.kirraparty.bukkit.KirraPartyBukkit
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty.baffle
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty.buttonSound
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty.openChangeLeaderNotifyBox
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty.openDisbandNotifyBox
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty.openInviteInputBox
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty.openKickNotifyBox
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty.partyCreateNotifyBoxKey
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty.partyGuiTitle
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.screen.PartyScreen.mainUI
import net.sakuragame.eternal.kirraparty.bukkit.party.PartyAPI
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.util.asLangText

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
        if (!baffle.hasNext(player.name)) return
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
}