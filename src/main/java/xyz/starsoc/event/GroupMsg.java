package xyz.starsoc.event;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.PlainText;
import org.jetbrains.annotations.NotNull;
import xyz.starsoc.acm.core.CodeForces;
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
                group.sendMessage(codeForces.getRank());
                return;
            case "contests":
                group.sendMessage(codeForces.getContests());
        }

        if (command.length == 2){
            group.sendMessage("参数不足，请检查");
            return;
        }

        switch (command[1]){
            case "add":
                group.sendMessage(codeForces.add(command[2]));
                break;
            case "remove":
                group.sendMessage(codeForces.remove(command[2]));
                break;
        }


    }
}
