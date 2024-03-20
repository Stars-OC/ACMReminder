package xyz.starsoc.file

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

object Message : AutoSavePluginConfig("message") {

    val help : String by value("=====ACMRanking 帮助=====" +
            "\n!(！)acm 排行榜 查看今日冲分榜" +
            "\n!(！)acm 昨日排行榜 查看昨日冲分榜")
}