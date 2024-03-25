package xyz.starsoc;

import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import xyz.starsoc.acm.core.CodeForces;
import xyz.starsoc.acm.thread.CodeForcesThread;
import xyz.starsoc.event.GroupMsg;
import xyz.starsoc.file.Config;
import xyz.starsoc.file.Message;
import xyz.starsoc.file.Redis;

public final class ACMReminder extends JavaPlugin{
    public static final ACMReminder INSTANCE=new ACMReminder();

    private ACMReminder(){
        super(new JvmPluginDescriptionBuilder("xyz.starsoc.acmreminder","0.1.0")
                .name("ACMReminder")
                .author("Clusters_stars")
                .build());
    }

    /**
     * 当插件启用时调用此方法。
     * 该方法不接受参数，也不返回任何值。
     * 主要完成以下功能：
     * 1. 重新加载配置；
     * 2. 注册群消息监听器；
     * 3. 启动CodeForces线程；
     * 4. 记录加载成功的日志信息。
     */
    @Override
    public void onEnable(){
        // 重新加载配置
        reload();
        // 注册全局事件监听器，用于处理群消息
        GlobalEventChannel.INSTANCE.registerListenerHost(new GroupMsg());
        // 记录插件加载成功的日志
        getLogger().info("ACMReminder 加载成功!");
    }

    public void reload(){
        reloadPluginConfig(Config.INSTANCE);
        reloadPluginConfig(Message.INSTANCE);
        reloadPluginConfig(Redis.INSTANCE);
    }
}