package xyz.starsoc.acm.core;

import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.starsoc.pojo.codeforces.CFContests;
import xyz.starsoc.pojo.codeforces.CFRespond;
import xyz.starsoc.pojo.codeforces.CFUserRating;
import xyz.starsoc.pojo.codeforces.UserInfo;
import xyz.starsoc.utils.RedisUtils;
import xyz.starsoc.utils.Utils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;


public class CodeForces {

    public static final CodeForces INSTANCE = new CodeForces();

    public static final HashMap<Long,CFContests> CONTESTS_TIME = new HashMap<>();

    private Logger logger = LoggerFactory.getLogger("CodeForces");

    private static final Utils utils = Utils.INSTANCE;

    private static final String CONTEST_LIST = "https://codeforces.com/api/contest.list?gym=false";

    /**
     * 更新竞赛列表信息。该方法会从指定接口获取竞赛列表的JSON数据，然后将这些数据解析成CFRespond<CFContests>对象。
     * 如果获取数据成功且竞赛处于"BEFORE"阶段（即竞赛还未开始），则将这些竞赛信息存储到Redis中。
     *
     * 该方法不接受任何参数。
     * 该方法没有返回值。
     */
    public void updateContests(){
        logger.info("开始更新竞赛列表信息");
        CONTESTS_TIME.clear();
        // 从指定URL获取JSON数据
        String json = utils.http.getJson(CONTEST_LIST);
        // 使用TypeToken来处理泛型的转换
        Type type = new TypeToken<CFRespond<CFContests>>(){}.getType();
        // 将JSON数据解析成CFRespond<CFContests>对象
        CFRespond<CFContests> respond = utils.gson.fromJson(json, type);
        // 如果响应状态不为"OK"，则不进行任何操作
        if (!respond.getStatus().equals("OK")) return;
        // 获取竞赛列表
        List<CFContests> result = respond.getResult();
        // 遍历竞赛列表，只处理还未开始的竞赛
        for (CFContests contest : result) {
            if (!contest.getPhase().equals("BEFORE")) continue;
            // 将还未开始的竞赛信息存储到时间列表中
            CONTESTS_TIME.put(contest.getStartTimeSeconds(),contest);
        }
        utils.debug("更新竞赛列表信息完成");
    }


    public String getRank() {
        return null;
    }

    public String getContests() {
        StringBuilder message = new StringBuilder("---- CodeForces Contests ----\n");
        for (CFContests contest : CONTESTS_TIME.values()){
            message.append(contest.getName()).append("(").append(contest.getType()).append(") 将在").append(contest.getStartTimeSeconds()).append("s后开始\n");
        }
        return message.toString();
    }

    /**
     * 添加用户到Codeforces用户评分数据库
     * @param person Codeforces用户的用户名
     * @return 成功添加用户的提示信息，如果添加失败则返回null
     */
    public String add(String person) {
        // 获取指定用户的评分信息
        CFUserRating userRating = getUserRating(person);

        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(person);
        userInfo.setRating(userRating.getNewRating());
        userInfo.setUpdateTime(userRating.getRatingUpdateTimeSeconds());
        userInfo.setLastContestName(userRating.getContestName());

        // 将用户评分信息存储到Redis中
        utils.redis.setObjectHash(RedisUtils.REDIS_CODEFORCES_USER_INFO, person, userInfo);

        // 返回添加成功的提示信息，包含用户的新评分和上次更新时间
        return "添加 用户" + person + " 成功，当前分数为 (" + userRating.getNewRating() + ")，上次更新时间 " + userRating.getRatingUpdateTimeSeconds();
    }


    /**
     * 获取指定用户的评分信息。
     *
     * @param person 用户的Codeforces用户名。
     * @return CFUserRating 对象，包含用户的评分信息。如果无法获取到信息，则返回null。
     */
    public CFUserRating getUserRating(String person) {
        // 通过HTTP GET请求获取指定用户的评分信息
        String rating = utils.http.getJson("https://codeforces.com/api/user.rating?handles=" + person);

        // 使用Gson解析返回的JSON数据
        Type type = new TypeToken<CFRespond<CFUserRating>>(){}.getType();
        CFRespond<CFUserRating> respond = utils.gson.fromJson(rating, type);

        // 检查API响应状态，如果不为"OK"则返回null
        if (!respond.getStatus().equals("OK")) return null;

        // 获取用户评分列表，通常只有一个元素，取最后一个（最新）的评分信息
        List<CFUserRating> result = respond.getResult();
        return result.get(result.size() - 1);
    }



    public String remove(String person) {
        return null;
    }
}
