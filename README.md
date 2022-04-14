# KirraParty

> 作者: @kirraObj (咸蛋)

简单的组队插件, 基于 Kotlin & TabooLib 6 进行开发.

为使用它, 必须配合安装使用 Bungee 环境.

## 指令
```text
  /kirraParty ...

  list (Type) - 查看队伍列表.
  # Type 分为 Local 服务器本地缓存) 与 Redis (Bungee 内所有队伍) 两种.
  create - 创建一个队伍.
  disband - 解散一个队伍.
  invite - 邀请 Bungee 内任意一名玩家进入您的队伍.
  acceptInvite - 同意一个人的队伍邀请.
  setLeader (Player) - 设置一个人为您当前队伍的队长.
  kick (Player) - 踢出您队伍的一名成员.
```

## 开发接口

### 事件

- PartyAddMemberEvent - 当队伍增加成员时触发事件.
- PartyKickMemberEvent - 当队伍踢出一名成员时触发事件.
- PartyChangeLeaderEvent - 当队伍更换队长时触发事件.
- PartyDisbandEvent - 当队伍解散时触发事件.

### PlaceholderAPI 变量

```text
  kirraParty...

  以下的所有变量都必须需要玩家在一个队伍内, 否则一概返回 "__"

  uid - 您当前所在的队伍识别 ID.
  leader - 当前所在队伍队长游戏名.
  member_1, member_2, member_3 - 当前队伍相应位置的成员名, 没有则返回 "无"
  created_time - 队伍创建时间.
```

## 版本更新

### 1.0.0-SNAPSHOT
基本功能完善.

### 0.0.1-SNAPSHOT
新建文件夹.jpg
