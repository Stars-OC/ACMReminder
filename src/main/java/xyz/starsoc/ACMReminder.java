package xyz.starsoc;

import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import xyz.starsoc.acm.core.CodeForces;

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
        getLogger().info("ACMReminder 加载成功!");
    }
}