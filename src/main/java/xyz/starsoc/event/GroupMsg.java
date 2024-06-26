package xyz.starsoc.event;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.BotOnlineEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.PlainText;
import org.jetbrains.annotations.NotNull;
import xyz.starsoc.ACMReminder;
import xyz.starsoc.acm.core.CodeForces;
import xyz.starsoc.acm.thread.CodeForcesThread;
import xyz.starsoc.file.Config;
import xyz.starsoc.file.Message;

import java.util.Set;

public class GroupMsg extends SimpleListenerHost {

    private final Config config = Config.INSTANCE;

    private final Message message = Message.INSTANCE;

    private final CodeForces codeForces = CodeForces.INSTANCE;

    private final Set<Long> groupList = config.getEnableGroup();
    @EventHandler
    public void onMessage(@NotNull GroupMessageEvent event) {// 可以抛出任何异常, 将在 handleException 处理

        Group group = event.getGroup();
        long groupId = group.getId();
        if (!groupList.contains(groupId)){
            return;
        }

        String plain = event.getMessage().get(PlainText.Key).contentToString();
        if (!(plain.startsWith("!acm") || plain.startsWith("！acm"))){
            return;
        }

        String help = message.getHelp();

        String[] command = plain.split(" ");

        if (command.length == 1){
            group.sendMessage(help);
        }

        switch (command[1]){
            case "help":
                group.sendMessage(help);
                return;
            case "rank":
                group.sendMessage(codeForces.getRank(groupId));
                return;
            case "contests":
                group.sendMessage(codeForces.getContests());
                return;
            case "user":
                String msg = "[";
                for (String user : codeForces.getUserList(groupId)) {
                    msg += user + " ";
                }
                msg += "]";
                group.sendMessage(msg);
                return;
            case "reload":
                ACMReminder.INSTANCE.reload();
                group.sendMessage("ACMReminder 重载成功");
                return;
        }

        if (command.length == 2){
            group.sendMessage("参数不足，请检查");
            return;
        }

        switch (command[1]){
            case "add":
                group.sendMessage(codeForces.add(groupId,command[2]));
                return;
            case "remove":
                group.sendMessage(codeForces.remove(groupId,command[2]));
                return;
            case "info":
                group.sendMessage(codeForces.getUserInfo(groupId,command[2]));
                return;
            case "update":
                group.sendMessage("正在更新信息，请稍候");
                switch (command[2]){
                    case "contest":
                        codeForces.updateContests();
                        group.sendMessage("更新竞赛信息成功 \n" + codeForces.getContests());
                        break;
                    case "user":
                        codeForces.updateUserRating(groupId);
                        group.sendMessage("更新用户信息成功 \n" + codeForces.updateRank(groupId));
                        break;
                }
                return;
            case "status":
                group.sendMessage(codeForces.getUserStatus(groupId,command[2]));
                return;
        }

    }

    @EventHandler
    public void onBotOnline(@NotNull BotOnlineEvent event) throws Exception {
        if (event.getBot().isOnline()){
            // 启动CodeForces论坛数据更新线程
            new CodeForcesThread().run();
        }
    }
}
