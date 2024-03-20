package xyz.starsoc.acm.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.starsoc.acm.core.CodeForces;
import xyz.starsoc.file.Config;
import xyz.starsoc.pojo.codeforces.CFContests;
import xyz.starsoc.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CodeForcesThread {

    private Logger logger = LoggerFactory.getLogger("CodeForcesThread");

    private static final Config config = Config.INSTANCE;

    private static final Utils utils = Utils.INSTANCE;

    private static final CodeForces codeForces = CodeForces.INSTANCE;

    private static final HashMap<Long, CFContests> CONTESTS_TIME = CodeForces.CONTESTS_TIME;

    private boolean timeVerify = true;
    private int times = -1;

    private boolean init = true;
    /**
     * 运行该任务，定期执行一系列操作。
     * 该任务首先检查是否需要初始化，如果需要则进行初始化，并确保只初始化一次。
     * 接着，获取当前时间并进行格式化，用于后续的时间判断和矫正。
     * 如果开启了时间验证且当前时间为午夜00:00，则进行时间矫正，并记录日志。
     * 根据配置的提前时间，检查是否有比赛即将开始，并发送相应通知。
     * 每分钟检查是否为整点，如果是，则更新比赛信息和排名。
     */
    public void run() {

        Runnable runnable = () -> {

            // 初始化逻辑，确保只初始化一次
            if (init){
                codeForces.init();
                init = false;
            }

            // 获取当前时间并进行格式化处理
            Date date = new Date();
            long dateTime = date.getTime() / 1000 ;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            String time = simpleDateFormat.format(date);

            // 时间矫正逻辑，确保时间准确
            if (timeVerify && "00".equals(time.split(":")[1])){
                timeVerify = false;
                times = -1;
                logger.info("矫正时间成功");
            }

            // 计算检查时间，查看是否有比赛即将开始
            long checkTime = dateTime + config.getBeforeTime() * 60L;
            if (CONTESTS_TIME.containsKey(checkTime)){
                codeForces.sendContestWillBegin(checkTime);
            }

            // 每分钟执行一次的逻辑，包括在整点时更新比赛信息和排名
            if (times%60 == 0){
                if (utils.time.isMidnight(dateTime)){
                    codeForces.updateContests();
                    codeForces.updateRank();
                    codeForces.updateUserRating();
                    logger.info("更新比赛信息/排名信息成功");
                }
            }



        };
        // 使用单线程定时任务执行器，以20秒初始延迟，每分钟执行一次
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable, 20, 60, TimeUnit.SECONDS);
    }

}
