package xyz.starsoc;

import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import xyz.starsoc.acm.core.CodeForces;
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

    @Override
    public void onEnable(){
//        System.out.println(CodeForces.INSTANCE.getUserRating("ZhangSonCow"));
        reload();
        GlobalEventChannel.INSTANCE.registerListenerHost(new GroupMsg());
        getLogger().info("ACMReminder 加载成功!");
    }

    public void reload(){
        reloadPluginConfig(Config.INSTANCE);
        reloadPluginConfig(Message.INSTANCE);
        reloadPluginConfig(Redis.INSTANCE);
    }
}