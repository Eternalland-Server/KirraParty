package net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.screen

import com.taylorswiftcn.megumi.uifactory.generate.function.Statements
import com.taylorswiftcn.megumi.uifactory.generate.type.ActionType
import com.taylorswiftcn.megumi.uifactory.generate.type.MatchType
import com.taylorswiftcn.megumi.uifactory.generate.ui.component.base.EntityViewComp
import com.taylorswiftcn.megumi.uifactory.generate.ui.component.base.LabelComp
import com.taylorswiftcn.megumi.uifactory.generate.ui.component.base.TextureComp
import com.taylorswiftcn.megumi.uifactory.generate.ui.screen.ScreenUI
import net.sakuragame.eternal.dragoncore.api.CoreAPI
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty.buttonSound
import net.sakuragame.eternal.kirraparty.bukkit.compat.dragoncore.DCoreParty.openMenuSound
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

@Suppress("SpellCheckingInspection", "SpellCheckingInspection")
object PartyScreen {

    lateinit var leftStatusHud: ScreenUI
    lateinit var mainUI: ScreenUI

    @Awake(LifeCycle.ACTIVE)
    fun i() {
        CoreAPI.registerKey(DCoreParty.triggerKey)
        initLeftStatus()
        initMainUI()
    }

    private fun initLeftStatus() {
        leftStatusHud = ScreenUI("kparty_leftStatusHud")
            .setMatch(MatchType.HUD)
            .addComponent(TextureComp("body", "ui/party/hud_title.png")
                .setXY("0", "h*0.2")
                .setWidth("132")
                .setHeight("22")
                .setAlpha(0.5)
                .setVisible("(func.PlaceholderAPI_Get('kparty_available') == true)")
            )
            .addComponent(LabelComp("team_info", "")
                .setTexts("func.PlaceholderAPI_Get('kparty_member_size')")
                .setXY("body.x+3", "body.y+7")
                .setVisible("body.visible")
            )
            .addComponent(TextureComp("team_setting", "ui/party/team_setting.png")
                .setXY("body.x+78", "body.y+5")
                .setWidth("12")
                .setHeight("12")
                .setVisible("(func.PlaceholderAPI_Get('kparty_available') == true)) && (func.PlaceholderAPI_Get('kparty_name_leader') == func.Player_Get_Name))")
                .addAction(ActionType.Left_Click, buttonSound)
                .addAction(ActionType.Left_Click)
            )
            .addComponent(TextureComp("team_invite", "ui/party/invite_member.png")
                .setXY("body.x+96", "body.y+5")
                .setWidth("12")
                .setHeight("12")
                .setVisible("(func.PlaceholderAPI_Get('kparty_available') == true)) && (func.PlaceholderAPI_Get('kparty_name_leader') == func.Player_Get_Name))")
                .addAction(ActionType.Left_Click, buttonSound)
                .addAction(ActionType.Left_Click)
            )
            .addComponent(TextureComp("team_quit", "ui/party/team_quit.png")
                .setXY("body.x+112", "body.y+5")
                .setWidth("12")
                .setHeight("12")
                .setVisible("body.visible")
                .addAction(ActionType.Left_Click)
                .addAction(ActionType.Left_Click, buttonSound)
            )
            .addComponent(TextureComp("team_content1", "ui/party/hud_bg.png")
                .setXY("body.x", "body.y+22")
                .setWidth("132")
                .setHeight("18")
                .setAlpha(0.4)
                .setVisible("body.visible")
            )
            .addComponent(TextureComp("captain_tag", "ui/party/captain_icon.png")
                .setXY("team_content1.x+3", "team_content1.y+4")
                .setWidth("11")
                .setHeight("10")
                .setVisible("body.visible")
            )
            .addComponent(LabelComp("member1_name", "")
                .setTexts("func.PlaceholderAPI_Get('kparty_name_leader')")
                .setXY("team_content1.x+16", "team_content1.y+4")
                .setLimitX("team_content1.x+16")
                .setLimitY("team_content1.y+4")
                .setLimitHeight("team_content1.x+41")
                .setLimitWidth("team_content1.y+14")
                .setVisible("body.visible")
            )
            .addComponent(TextureComp("member1_health_bg", "ui/party/health_bg.png")
                .setXY("team_content1.x+60", "team_content1.y+4")
                .setWidth("67")
                .setHeight("10")
                .setVisible("body.visible")
            )
            .addComponent(TextureComp("member1_health_bar", "ui/party/health_bar.png")
                .setXY("team_content1.x+62", "team_content1.y+6")
                .setWidth("63 * func.PlaceholderAPI_Get('kparty_leader_health_percent')")
                .setHeight("6")
                .setVisible("body.visible")
            )
            .addComponent(TextureComp("team_content2", "ui/party/hud_bg.png")
                .setXY("team_content1.x", "team_content1.y+18")
                .setWidth("132")
                .setHeight("18")
                .setAlpha(0.4)
                .setVisible("func.PlaceholderAPI_Get('kparty_visible_member1')"))
            .addComponent(LabelComp("member2_name", "")
                .setTexts("func.PlaceholderAPI_Get('kparty_name_member1')")
                .setXY("team_content2.x+16", "team_content2.y+4")
                .setLimitX("team_content2.x+16")
                .setLimitY("team_content2.y+4")
                .setLimitHeight("team_content2.x+41")
                .setLimitWidth("team_content2.y+14")
                .setVisible("team_content2.visible")
            )
            .addComponent(TextureComp("member2_health_bg", "ui/party/health_bg.png")
                .setXY("team_content2.x+60", "team_content2.y+4")
                .setWidth("67")
                .setHeight("10")
                .setVisible("team_content2.visible"))
            .addComponent(TextureComp("member2_health_bar", "ui/party/health_bar.png")
                .setXY("team_content2.x+62", "team_content2.y+6")
                .setWidth("63 * func.PlaceholderAPI_Get('kparty_member1_health_percent')")
                .setHeight("6")
                .setVisible("team_content2.visible")
            )
            .addComponent(TextureComp("team_content3", "ui/party/hud_bg.png")
                .setXY("team_content1.x", "team_content2.y+18")
                .setWidth("132")
                .setHeight("18")
                .setAlpha(0.4)
                .setVisible("func.PlaceholderAPI_Get('kparty_visible_member2')")
            )
            .addComponent(LabelComp("member3_name", "")
                .setTexts("func.PlaceholderAPI_Get('kparty_name_member2')")
                .setXY("team_content3.x+16", "team_content3.y+4")
                .setLimitX("team_content3.x+16")
                .setLimitY("team_content3.y+4")
                .setLimitHeight("team_content3.x+41")
                .setLimitWidth("team_content3.y+14")
                .setVisible("team_content3.visible")
            )
            .addComponent(TextureComp("member3_health_bg", "ui/party/health_bg.png")
                .setXY("team_content3.x+60", "team_content3.y+4")
                .setWidth("67")
                .setHeight("10")
                .setVisible("team_content3.visible")
            )
            .addComponent(TextureComp("member3_health_bar", "ui/party/health_bar.png")
                .setXY("team_content3.x+62", "team_content3.y+6")
                .setWidth("63 * func.PlaceholderAPI_Get('kparty_member2_health_percent')")
                .setHeight("6")
                .setVisible("team_content3.visible")
            )
            .addComponent(TextureComp("team_content4", "ui/party/hud_bg.png")
                .setXY("team_content1.x", "team_content3.y+18")
                .setWidth("132")
                .setHeight("18")
                .setAlpha(0.4)
                .setVisible("func.PlaceholderAPI_Get('kparty_visible_member4')")
            )
            .addComponent(LabelComp("member4_name", "")
                .setTexts("func.PlaceholderAPI_Get('kparty_name_member3')")
                .setXY("team_content4.x+16", "team_content4.y+4")
                .setLimitX("team_content4.x+16")
                .setLimitY("team_content4.y+4")
                .setLimitHeight("team_content4.x+41")
                .setLimitWidth("team_content4.y+14")
                .setVisible("team_content4.visible")
            )
            .addComponent(TextureComp("member4_health_bg", "ui/party/health_bg.png")
                .setXY("team_content4.x+60", "team_content4.y+4")
                .setWidth("67")
                .setHeight("10")
                .setVisible("team_content4.visible"))
            .addComponent(TextureComp("member4_health_bar", "ui/party/health_bar.png")
                .setXY("team_content4.x+62", "team_content4.y+6")
                .setWidth("63 * func.PlaceholderAPI_Get('kparty_member3_health_percent')")
                .setHeight("6")
                .setVisible("team_content4.visible")
            )
    }

    private fun initMainUI() {
        mainUI = ScreenUI("kparty_mainUI")
            .setPressEKeyClose()
            .addComponent(TextureComp("body", "ui/party/background.png")
                .setXY("(w-body.width)/2", "(h-body.height)/2")
                .setWidth("529")
                .setHeight("286")
                .addAction(ActionType.Enter, openMenuSound)
            )
            .addComponent(LabelComp("title", "")
                .setTexts("&6&l??????")
                .setXY("body.x+(body.width-title.width)/2", "body.y+6")
            )
            .addComponent(TextureComp("title_shade", "ui/party/title_shade.png")
                .setXY("body.x+3", "body.y+3")
                .setWidth("523")
                .setHeight("16")
            )
            .addComponent(TextureComp("party_bg", "ui/party/party_background.png")
                .setXY("body.x+6", "body.y+21")
                .setWidth("517")
                .setHeight("218")
                .setAlpha(0.5))
            .addComponent(TextureComp("party_shade", "255,255,255,30")
                .setXY("party_bg.x+2", "party_bg.y+225")
                .setWidth("513")
                .setHeight("20")
            )
            .addComponent(LabelComp("party_msg", "func.PlaceholderAPI_Get('kparty_latest_message')")
                .setXY("party_shade.x+(party_shade.width-party_msg.width)/2", "party_shade.y+6")
            )
            .addComponent(TextureComp("member1_bg", "ui/party/member_background.png")
                .setXY("body.x+19", "body.y+35")
                .setWidth("124")
                .setHeight("165")
                .setAlpha(0.5)
            )
            .addComponent(TextureComp("captain_icon", "ui/party/captain_icon.png")
                .setXY("member1_bg.x+100", "member1_bg.y+10")
                .setWidth("11")
                .setHeight("10")
            )
            .addComponent(EntityViewComp("member1_model", "func.PlaceholderAPI_Get('kparty_model_leader')")
                .setXY("member1_bg.x+(member1_bg.width/2)", "member1_bg.y+140")
                .setScale(2.0)
            )
            .addComponent(TextureComp("member1_shade", "ui/party/member_shade.png")
                .setXY("member1_bg.x+9", "member1_bg.y+7")
                .setWidth("68")
                .setHeight("98"))
            .addComponent(TextureComp("member1_card", "ui/party/card.png")
                .setXY("member1_bg.x+(member1_bg.width-member1_card.width)/2", "member1_bg.y+128")
                .setWidth("101")
                .setHeight("22")
            )
            .addComponent(LabelComp("member1_name", "func.PlaceholderAPI_Get('kparty_name_leader')")
                .setXY("member1_card.x+(member1_card.width-member1_name.width)/2", "member1_card.y+6")
            )
            // party disband component.
            .addComponent(TextureComp("member1_button", "ui/party/button.png")
                .setText("&f??????")
                .setXY("member1_card.x+(member1_card.width-member1_button.width)/2+(51-51*member1_button.scale)/2",
                    "member1_bg.y+166+(20-member1_button.height*member1_button.scale)/2")
                .setWidth("51")
                .setHeight("20")
                .setVisible("(func.PlaceholderAPI_Get('kparty_name_leader') == func.Player_Get_Name))")
                .addAction(ActionType.Leave, "member1_button.texture = 'ui/party/button.png';")
                .addAction(ActionType.Left_Click)
                .addAction(ActionType.Left_Click, buttonSound)
                .addAction(ActionType.Left_Click, Statements()
                    .add("member1_button.texture = 'ui/party/button_press.png';")
                    .add("member1_button.scale = 0.95;")
                    .build())
                .addAction(ActionType.Left_Release, Statements()
                    .add("member1_button.texture = 'ui/party/button.png';")
                    .add("member1_button.scale = 1;")
                    .build())
            )
            .addComponent(TextureComp("member2_bg", "ui/party/unselected_background.png")
                .setXY("member1_bg.x+member1_bg.width-2", "member1_bg.y")
                .setWidth("124")
                .setHeight("165")
                .setAlpha(0.5)
            )
            .addComponent(TextureComp("invite2_member", "ui/party/invite.png")
                .setXY("member2_bg.x+(member2_bg.width-invite2_member.width)/2", "member2_bg.y+(member2_bg.height-invite2_member.height)/2")
                .setWidth("34")
                .setHeight("34")
                .setVisible("(func.PlaceholderAPI_Get('kparty_visible_member1') == false) && (func.PlaceholderAPI_Get('kparty_name_leader') == func.Player_Get_Name))")
                .addAction(ActionType.Left_Click)
                .addAction(ActionType.Left_Click, buttonSound)
                .addAction(ActionType.Left_Release, Statements()
                    .add("invite2_member.texture = 'ui/party/invite.png'")
                    .add("invite2_member.scale = 1;")
                    .build())
            )
            .addComponent(EntityViewComp("member2_model", "func.PlaceholderAPI_Get('kparty_model_member1')")
                .setXY("member2_bg.x+(member2_bg.width/2)", "member2_bg.y+140")
                .setScale(2.0)
                .setVisible("func.PlaceholderAPI_Get('kparty_visible_member1')")
            )
            .addComponent(TextureComp("member2_shade", "ui/party/member_shade.png")
                .setXY("member2_bg.x+9", "member2_bg.y+7")
                .setWidth("68")
                .setHeight("98")
                .setVisible("member2_model.visible")
            )
            .addComponent(TextureComp("member2_card", "ui/party/card.png")
                .setXY("member2_bg.x+(member2_bg.width-member2_card.width)/2", "member2_bg.y+128")
                .setWidth("101")
                .setHeight("22")
                .setVisible("member2_model.visible")
            )
            .addComponent(LabelComp("member2_name", "func.PlaceholderAPI_Get('kparty_name_member1')")
                .setXY("member2_card.x+(member2_card.width-member2_name.width)/2", "member2_card.y+6")
            )
            .addComponent(TextureComp("member2_left_button", "ui/party/button.png")
                .setText("&f??????")
                .setXY("member2_card.x-2+(51-51*member2_left_button.scale)/2", "member2_bg.y+166")
                .setWidth("51")
                .setHeight("20")
                .setVisible("func.PlaceholderAPI_Get('kparty_visible_member1') && (func.PlaceholderAPI_Get('kparty_name_leader') == func.Player_Get_Name))")
                .addAction(ActionType.Leave, "[member2_left_button.texture = 'ui/party/button.png';]")
                .addAction(ActionType.Left_Click)
                .addAction(ActionType.Left_Click, buttonSound)
                .addAction(ActionType.Left_Click, Statements()
                    .add("member1_button.texture = 'ui/party/button_press.png';")
                    .add("member1_button.scale = 0.95;")
                    .build())
                .addAction(ActionType.Left_Release, Statements()
                    .add("member2_left_button.texture = 'ui/party/button.png';")
                    .add("member2_left_button.scale = 1;")
                    .build())
            )
            .addComponent(TextureComp("member2_right_button", "ui/party/button.png")
                .setText("&f??????")
                .setXY("member2_card.x+(member2_card.width-member2_buttonB.width)/2+(51-51*member2_right_button.scale)/2+2", "member2_bg.y+166")
                .setWidth("51")
                .setHeight("20")
                .setVisible("func.PlaceholderAPI_Get('kparty_visible_member1') && (func.PlaceholderAPI_Get('kparty_name_leader') == func.Player_Get_Name))")
                .addAction(ActionType.Leave, "member2_right_button.texture = 'ui/party/button.png';")
                .addAction(ActionType.Left_Click)
                .addAction(ActionType.Left_Click, buttonSound)
                .addAction(ActionType.Left_Click, Statements()
                    .add("member1_button.texture = 'ui/party/button_press.png';")
                    .add("member1_button.scale = 0.95;")
                    .build())
                .addAction(ActionType.Left_Release, Statements()
                    .add("member2_right_button.texture = 'ui/party/button.png';")
                    .add("member2_right_button.scale = 1;")
                    .build())
            )
            .addComponent(TextureComp("member3_bg", "ui/party/unselected_background.png")
                .setXY("member2_bg.x+member2_bg.width-2", "member2_bg.y")
                .setWidth("124")
                .setHeight("165")
                .setAlpha(0.5)
            )
            .addComponent(TextureComp("invite3_member", "ui/party/invite.png")
                .setXY("member3_bg.x+(member3_bg.width-invite3_member.width)/2", "member3_bg.y+(member3_bg.height-invite3_member.height)/2")
                .setWidth("34")
                .setHeight("34")
                .setVisible("true")
                .setVisible("(func.PlaceholderAPI_Get('kparty_visible_member2') == false) && (func.PlaceholderAPI_Get('kparty_name_leader') == func.Player_Get_Name))")
                .addAction(ActionType.Left_Click)
                .addAction(ActionType.Left_Click, buttonSound)
                .addAction(ActionType.Left_Release, Statements()
                    .add("invite3_member.texture = 'ui/party/invite.png'")
                    .add("invite3_member.scale = 1;")
                    .build())
            )
            .addComponent(EntityViewComp("member3_model", "func.PlaceholderAPI_Get('kparty_model_member2')")
                .setXY("member3_bg.x+(member3_bg.width/2)", "member3_bg.y+140")
                .setScale(2.0)
                .setVisible("func.PlaceholderAPI_Get('kparty_visible_member2')")
            )
            .addComponent(TextureComp("member3_shade", "ui/party/member_shade.png")
                .setXY("member3_bg.x+9", "member3_bg.y+7")
                .setWidth("68")
                .setHeight("98")
                .setVisible("member3_model.visible")
            )
            .addComponent(TextureComp("member3_card", "ui/party/card.png")
                .setXY("member3_bg.x+(member3_bg.width-member3_card.width)/2", "member3_bg.y+128")
                .setWidth("101")
                .setHeight("22")
                .setVisible("member3_model.visible")
            )
            .addComponent(LabelComp("member3_name", "func.PlaceholderAPI_Get('kparty_name_member2')")
                .setXY("member3_card.x+(member3_card.width-member3_name.width)/2", "member3_card.y+6")
                .setVisible("member3_card.visible"))
            .addComponent(TextureComp("member3_left_button", "ui/party/button.png")
                .setText("&f??????")
                .setXY("member3_card.x-2+(51-member3_left_button.width*member3_left_button.scale)/2", "member3_bg.y+166")
                .setWidth("51")
                .setHeight("20")
                .setVisible("func.PlaceholderAPI_Get('kparty_visible_member2') && (func.PlaceholderAPI_Get('kparty_name_leader') == func.Player_Get_Name))")
                .addAction(ActionType.Leave, "member3_left_button.texture = 'ui/party/button.png';")
                .addAction(ActionType.Left_Click)
                .addAction(ActionType.Left_Click, buttonSound)
                .addAction(ActionType.Left_Click, Statements()
                    .add("member1_button.texture = 'ui/party/button_press.png';")
                    .add("member1_button.scale = 0.95;")
                    .build())
                .addAction(ActionType.Left_Release, Statements()
                    .add("member3_left_button.texture = 'ui/party/button.png';")
                    .add("member3_left_button.scale = 1;")
                    .build())
            )
            .addComponent(TextureComp("member3_right_button", "ui/party/button.png")
                .setText("&f??????")
                .setXY("member3_card.x+(member3_card.width-member3_buttonB.width)/2+(51-member3_right_button.width*member3_right_button.scale)/2+2",
                    "member3_bg.y+166")
                .setWidth("51")
                .setHeight("20")
                .setVisible("func.PlaceholderAPI_Get('kparty_visible_member2') && (func.PlaceholderAPI_Get('kparty_name_leader') == func.Player_Get_Name))")
                .addAction(ActionType.Leave, "member3_right_button.texture = 'ui/party/button.png';")
                .addAction(ActionType.Left_Click)
                .addAction(ActionType.Left_Click, buttonSound)
                .addAction(ActionType.Left_Click, Statements()
                    .add("member1_button.texture = 'ui/party/button_press.png';")
                    .add("member1_button.scale = 0.95;")
                    .build())
                .addAction(ActionType.Left_Release, Statements()
                    .add("member3_right_button.texture = 'ui/party/button.png';")
                    .add("member3_right_button.scale = 1;")
                    .build())
            )
            .addComponent(TextureComp("member4_bg", "ui/party/unselected_background.png")
                .setXY("member3_bg.x+member3_bg.width-2", "member3_bg.y")
                .setWidth("124")
                .setHeight("165")
                .setAlpha(0.5))
            .addComponent(TextureComp("invite4_member", "ui/party/invite.png")
                .setXY("member4_bg.x+(member4_bg.width-invite4_member.width)/2", "member4_bg.y+(member4_bg.height-invite4_member.height)/2")
                .setWidth("34")
                .setHeight("34")
                .setVisible("(func.PlaceholderAPI_Get('kparty_visible_member3') == false) && (func.PlaceholderAPI_Get('kparty_name_leader') == func.Player_Get_Name))")
                .addAction(ActionType.Left_Click)
                .addAction(ActionType.Left_Click, buttonSound)
                .addAction(ActionType.Left_Release, Statements()
                    .add("invite4_member.texture = 'ui/party/invite.png'")
                    .add("invite4_member.scale = 1;")
                    .build())
            )
            .addComponent(EntityViewComp("member4_model", "func.PlaceholderAPI_Get('kparty_model_member3')")
                .setXY("member4_bg.x+(member4_bg.width/2)", "member4_bg.y+140")
                .setScale(2.0)
                .setVisible("func.PlaceholderAPI_Get('kparty_visible_member3')")
            )
            .addComponent(TextureComp("member4_shade", "ui/party/member_shade.png")
                .setXY("member4_bg.x+9", "member4_bg.y+7")
                .setWidth("68")
                .setHeight("98")
                .setVisible("member4_model.visible")
            )
            .addComponent(TextureComp("member4_card", "ui/party/card.png")
                .setXY("member4_bg.x+(member4_bg.width-member4_card.width)/2", "member4_bg.y+128")
                .setWidth("101")
                .setHeight("22")
                .setVisible("member4_model.visible")
            )
            .addComponent(LabelComp("member4_name", "func.PlaceholderAPI_Get('kparty_name_member3')")
                .setXY("member4_card.x+(member4_card.width-member4_name.width)/2", "member4_card.y+6")
                .setVisible("member4_card.visible")
            )
            .addComponent(TextureComp("member4_left_button", "ui/party/button.png")
                .setText("&f??????")
                .setXY("member4_card.x-2+(51-member4_left_button.width*member4_left_button.scale)/2", "member4_bg.y+166")
                .setWidth("51")
                .setHeight("20")
                .setVisible("func.PlaceholderAPI_Get('kparty_visible_member3') && (func.PlaceholderAPI_Get('kparty_name_leader') == func.Player_Get_Name))")
                .addAction(ActionType.Leave, "member4_left_button.texture = 'ui/party/button.png';")
                .addAction(ActionType.Left_Click)
                .addAction(ActionType.Left_Click, buttonSound)
                .addAction(ActionType.Left_Click, Statements()
                    .add("member1_button.texture = 'ui/party/button_press.png';")
                    .add("member1_button.scale = 0.95;")
                    .build())
                .addAction(ActionType.Left_Release, Statements()
                    .add("member4_left_button.texture = 'ui/party/button.png';")
                    .add("member4_left_button.scale = 1;")
                    .build())
            )
            .addComponent(TextureComp("member4_right_button", "ui/party/button.png")
                .setText("&f??????")
                .setXY("member4_card.x+(member4_card.width-member4_buttonB.width)/2+(51-member4_right_button.width*member4_right_button.scale)/2+2",
                    "member4_bg.y+166")
                .setWidth("51")
                .setHeight("20")
                .setVisible("func.PlaceholderAPI_Get('kparty_visible_member3') && (func.PlaceholderAPI_Get('kparty_name_leader') == func.Player_Get_Name))")
                .addAction(ActionType.Leave, "member4_right_button.texture = 'ui/party/button.png';")
                .addAction(ActionType.Left_Click)
                .addAction(ActionType.Left_Click, buttonSound)
                .addAction(ActionType.Left_Click, Statements()
                    .add("member1_button.texture = 'ui/party/button_press.png';")
                    .add("member1_button.scale = 0.95;")
                    .build())
                .addAction(ActionType.Left_Release, Statements()
                    .add("member4_right_button.texture = 'ui/party/button.png';")
                    .add("member4_right_button.scale = 1;")
                    .build())
            )
    }
}