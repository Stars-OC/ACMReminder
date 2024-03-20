package xyz.starsoc.file

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value
import xyz.starsoc.file.Config.provideDelegate

object Config : AutoSavePluginConfig("config") {
    val bot : Long by value()
    @ValueDescription("最高权限")
    val master : Long by value()
    val debug : Boolean by value(false)
    val enableGroup : Set<Long> by value()
    val proxyHost by value("127.0.0.1")
    val proxyPort by value(10809)

    val beforeTime by value(30)
}