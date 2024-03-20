package xyz.starsoc.file

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

object Message : AutoSavePluginConfig("message") {

    val help : String by value("=====ACMReminder 帮助=====" +
            "\n!(！)acm rank 查看排名" +
            "\n!(！)acm contests 查看最近竞赛信息" +
            "\n!(！)acm update contest 强制更新竞赛信息" +
            "\n!(！)acm update user 强制更新用户信息" +
            "\n!(！)acm add 用户 添加用户" +
            "\n!(！)acm remove 用户 删除用户" +
            "\n!(！)acm info 用户 查看用户信息" +
            "\n!(！)acm status 用户 查看用户状态" )

}