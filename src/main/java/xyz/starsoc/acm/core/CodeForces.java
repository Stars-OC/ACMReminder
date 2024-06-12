package xyz.starsoc.acm.core;

import com.google.gson.reflect.TypeToken;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.PlainText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPooled;
import xyz.starsoc.file.Config;
import xyz.starsoc.pojo.codeforces.CFContests;
import xyz.starsoc.pojo.codeforces.CFRespond;
import xyz.starsoc.pojo.codeforces.CFUserRating;
import xyz.starsoc.pojo.codeforces.UserInfo;
import xyz.starsoc.utils.RedisUtils;
import xyz.starsoc.utils.Utils;

import java.lang.reflect.Type;
import java.util.*;


public class CodeForces {

    public static final CodeForces INSTANCE = new CodeForces();

    public static final HashMap<Long,CFContests> CONTESTS_TIME = new HashMap<>();

    public static final Set<Group> groupList = new HashSet<>();

    private final Logger logger = LoggerFactory.getLogger("CodeForces");

    private static final Utils utils = Utils.INSTANCE;

    private static final Config config = Config.INSTANCE;

    private static final String CONTEST_LIST = "https://codeforces.com/api/contest.list?gym=false";
    private static final String USER_RATING = "https://codeforces.com/api/user.rating?handle=";

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
        if (respond != null && !respond.getStatus().equals("OK")) return;
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


    /**
     * 获取并更新CodeForces用户的排名信息。
     * 使用Redis存储的用户信息，计算排名，并将更新后的排名信息返回。
     *
     * @return 包含所有用户排名信息的字符串，格式为多行文本。
     */
    public String updateRank(long groupId) {
        // 从Redis获取连接
        JedisPooled redis = utils.redis.redis();
        // 定义CodeForces用户信息的Redis键名
        String key = RedisUtils.REDIS_CODEFORCES_USER_INFO + groupId;
        // 从Redis获取所有用户的信息（以JSON格式存储）
        Map<String, String> map = redis.hgetAll(key);
        int place = 0;
        // 根据用户数量初始化UserInfo数组
        UserInfo[] userInfos = new UserInfo[map.size()];
        // 将JSON字符串转换为UserInfo对象，并填充数组
        for (String json : map.values()) {
            UserInfo userInfo = utils.gson.fromJson(json, UserInfo.class);
            userInfos[place] = userInfo;
            ++place;
        }
        // 根据用户rating进行排序
        Arrays.sort(userInfos, (o1, o2) -> o2.getRating() - o1.getRating());
        // 更新用户排名
        for (int i = 0; i < userInfos.length; i++) {
            userInfos[i].setRank(i+1);
        }
        // 准备更新后的用户信息Map，以供Redis存储使用
        HashMap<String, String> hashMap = new HashMap<>();
        // 初始化返回信息的StringBuilder
        StringBuilder message = new StringBuilder("---- CodeForces Rank ----\n");
        // 构建返回的排名信息字符串，并更新Redis中的用户信息
        for (UserInfo userInfo : userInfos) {
           hashMap.put(userInfo.getUsername(),utils.gson.toJson(userInfo));
           message.append(userInfo.getUsername()).append(" (").append(userInfo.getRating()).append(")  排名： ").append(userInfo.getRank()).append("\n");
        }
        // 更新Redis中的用户信息，并关闭Redis连接
        redis.hset(key,hashMap);
        redis.close();
        // 打印调试信息，表示获取排名完成
        utils.debug("获取Codeforces Rank完成");

        // 返回构建好的排名信息字符串
        return message.toString();
    }


    /**
     * 获取即将举行的CodeForces比赛信息。
     * 该方法不接受参数，返回一个包含所有即将举行的比赛名称、类型以及开始时间的字符串。
     * 比赛信息按照比赛开始时间的顺序进行排列。
     *
     * @return 返回一个字符串，该字符串包含所有即将举行的CodeForces比赛的名称、类型和开始时间。
     */
    public String getContests() {
        // 使用StringBuilder初始化消息，添加比赛信息的标题
        StringBuilder message = new StringBuilder("---- CodeForces Contests ----\n");
        // 遍历所有比赛的开始时间，并将比赛名称、类型和开始时间格式化后添加到消息中
        for (CFContests contest : CONTESTS_TIME.values()){
            message.append(contest.getName()).append("(").append(contest.getType()).append(") 将在").append(utils.time.timeToFormat(contest.getStartTimeSeconds() - System.currentTimeMillis()/1000)).append("后开始\n");
        }
        // 返回格式化后的内容
        return message.toString();
    }

    /**
     * 添加用户到Codeforces用户评分数据库
     *
     * @param groupId
     * @param person  Codeforces用户的用户名
     * @return 成功添加用户的提示信息，如果添加失败则返回null
     */
    public String add(long groupId, String person) {
        String key = RedisUtils.REDIS_CODEFORCES_USER_INFO + groupId;
        if (utils.redis.getHash(key,person) != null) return "用户" + person + " 已存在";
        // 获取指定用户的评分信息
        CFUserRating userRating = getUserRating(person);

        if (userRating == null) return "用户" + person + " 不存在";

        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(person);
        userInfo.setRating(userRating.getNewRating());
        userInfo.setUpdateTime(userRating.getRatingUpdateTimeSeconds());
        userInfo.setLastContestName(userRating.getContestName());

        // 将用户评分信息存储到Redis中
        utils.redis.setObjectHash(key, person, userInfo);

        // 更新排名数据
        updateRank(groupId);
        // 返回添加成功的提示信息，包含用户的新评分和上次更新时间
        return "添加 用户 " + person + " 成功，当前分数为 (" + userRating.getNewRating() + ")，上次更新时间 " + utils.time.timestampToString(userRating.getRatingUpdateTimeSeconds());
    }


    /**
     * 获取指定用户的评分信息。
     *
     * @param person 用户的Codeforces用户名。
     * @return CFUserRating 对象，包含用户的评分信息。如果无法获取到信息，则返回null。
     */
    public CFUserRating getUserRating(String person) {
        // 通过HTTP GET请求获取指定用户的评分信息
        String rating = utils.http.getJson(USER_RATING + person);

        // 使用Gson解析返回的JSON数据
        Type type = new TypeToken<CFRespond<CFUserRating>>(){}.getType();
        CFRespond<CFUserRating> respond = utils.gson.fromJson(rating, type);

        // 检查API响应状态，如果不为"OK"则返回null
        if (!respond.getStatus().equals("OK")) return null;

        // 获取用户评分列表，通常只有一个元素，取最后一个（最新）的评分信息
        List<CFUserRating> result = respond.getResult();
        return result.get(result.size() - 1);
    }

    /**
     * 获取用户列表。
     * 该方法从Redis缓存中获取所有用户的ID。
     *
     * @return Set<String> 返回一个包含所有用户ID的集合。
     */
    public Set<String> getUserList(long groupId){
        // 从Redis连接池获取JedisPooled实例
        JedisPooled redis = utils.redis.redis();
        // 从Redis获取用户信息哈希表的键（用户ID）
        String key = RedisUtils.REDIS_CODEFORCES_USER_INFO + groupId;
        Set<String> set = redis.hgetAll(key).keySet();
        // 关闭Redis连接
        redis.close();
        return set;
    }

    /**
     * 更新用户评分信息。
     * 该方法遍历用户列表，获取每个用户的评分信息，并将这些信息更新到Redis中。
     * 这个过程中，会忽略那些没有评分信息的用户。
     */
    public void updateUserRating(long groupId){
        // 获取用户列表
        Set<String> userList = getUserList(groupId);
        String key = RedisUtils.REDIS_CODEFORCES_USER_INFO + groupId;
        // 遍历用户列表
        for (String username : userList){
            // 尝试获取用户的评分信息
            CFUserRating userRating = getUserRating(username);
            // 如果用户没有评分信息，则跳过该用户
            if (userRating == null) continue;

            // 准备用户信息，包括用户名、新的评分、评分更新时间以及最后一次比赛名称
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(username);
            userInfo.setRating(userRating.getNewRating());
            userInfo.setUpdateTime(userRating.getRatingUpdateTimeSeconds());
            userInfo.setLastContestName(userRating.getContestName());

            // 将用户信息更新到Redis中

            utils.redis.setObjectHash(key, username, userInfo);
        }
    }

    /**
     * 更新所有用户评分。
     * 此方法遍历配置中启用的用户组，并对每个用户组调用更新用户评分的方法。
     * 注意：此方法不接受参数，也不返回任何值。
     */
    public void updateAllUserRating(){
        // 遍历配置中启用的用户组，对每个组更新用户评分
        for (Long groupId : config.getEnableGroup()) {
            updateUserRating(groupId);
            updateRank(groupId);
        }
    }

    public void sendAllGroupRank(StringBuilder message){

        for (Group group : groupList) {
            String newMessage = message.toString();
            newMessage += getRank(group.getId());
            group.sendMessage(newMessage);
        }
    }



    /**
     * 从Redis中删除指定用户的个人信息。
     *
     * @param groupId
     * @param person  要删除的用户名称。
     * @return 返回一个字符串，表示删除操作的结果信息。
     */
    public String remove(long groupId, String person) {
        // 从Redis中删除指定用户的个人信息
        String key = RedisUtils.REDIS_CODEFORCES_USER_INFO + groupId;
        utils.redis.delHash(key,person);
        // 更新排名数据
        updateRank(groupId);
        return "删除 用户" + person + " 成功";
    }

    /**
     * 获取CodeForces用户的排名信息。
     *
     * 该方法首先从Redis获取所有用户的信息，然后将这些信息转换为用户排名的字符串表示。
     * 如果没有用户信息，则返回提示信息"当前没有用户"。
     *
     * @return 返回一个包含所有用户排名信息的字符串，如果没有用户，则返回"当前没有用户"。
     */
    public String getRank(long groupId) {
        JedisPooled redis = utils.redis.redis(); // 获取Redis客户端
        String key = RedisUtils.REDIS_CODEFORCES_USER_INFO + groupId;
        Map<String, String> hashMap = redis.hgetAll(key); // 从Redis获取所有用户的信息
        if (hashMap.size() == 0) {
            return "当前没有用户";
        }
        StringBuilder message = new StringBuilder("---- CodeForces Rank ----\n"); // 初始化用于构建排名信息的StringBuilder
        int place = 0;
        UserInfo[] userInfos = new UserInfo[hashMap.size()];
        // 将JSON字符串转换为UserInfo对象，并填充数组
        for (String json : hashMap.values()) {
            UserInfo userInfo = utils.gson.fromJson(json, UserInfo.class);
            userInfos[place] = userInfo;
            ++place;
        }
        // 根据用户rating进行排序
        Arrays.sort(userInfos, (o1, o2) -> o2.getRating() - o1.getRating());
        // 遍历用户信息，将用户信息转换为排名信息并添加到message中
        for (UserInfo userInfo : userInfos) {
            message.append(userInfo.getUsername()).append(" (").append(userInfo.getRating()).append(")  排名： ").append(userInfo.getRank()).append("\n");
        }
        return message.toString(); // 返回构建完成的排名信息字符串
    }

    /**
     * 发送即将开始的竞赛提醒
     *
     * @param checkTime 检查的时间点，单位为毫秒（通常为当前时间）
     * 该方法会查找在该时间点即将开始的竞赛，并向所有群组发送提醒信息。
     * 提醒信息包含竞赛的名称、类型和开始时间。
     */
    public void sendContestWillBegin(long checkTime) {
        // 根据检查时间获取对应的竞赛信息
        CFContests contests = CONTESTS_TIME.get(checkTime);

        if (contests != null && !contests.getStarted()) {
            contests.setStarted(true);
            utils.debug("发送比赛即将开始通知成功");
            // 发送提醒信息至所有群组
            for (Group group : groupList){
                // 构造并发送包含竞赛详情的提醒信息
                group.sendMessage(new PlainText("CodeForces比赛 " + contests.getName() + "("+ contests.getType() +") 将在 " + utils.time.timeToFormat(contests.getStartTimeSeconds() - System.currentTimeMillis()/1000) + " 开始"));
            }
        }
    }

    /**
     * 初始化函数
     * 该函数不接受参数，也不返回任何值。
     * 主要功能是根据配置信息初始化并加载指定的群组。
     */
    public void init(){
        utils.debug("init");
        // 尝试获取Bot实例，如果配置中没有指定Bot，则返回null
        Bot bot = Bot.getInstanceOrNull(config.getBot());
        if (bot == null){
            utils.debug("Bot is null"); // 如果Bot实例为null，则在日志中记录信息并返回
            return;
        }
        // 遍历配置中启用的群组ID列表
        for(Long groupId : config.getEnableGroup()){
            // 尝试根据群组ID获取群组实例，如果群组不存在则抛出异常
            Group group = bot.getGroupOrFail(groupId);
            groupList.add(group); // 将获取到的群组实例添加到群组列表中
        }

        // 更新竞赛信息
        updateContests();
        updateAllUserRating();

        utils.debug("init end");
    }

    public String getUserStatus(long groupId, String person) {
        return null;
    }

    public String getUserInfo(long groupId, String person) {
        String info = RedisUtils.REDIS_CODEFORCES_USER_INFO + groupId;
        UserInfo userInfo = utils.redis.getObjectHash(info, person, UserInfo.class);
        if (userInfo == null) {
            return "暂无用户 " + person + " 的数据";
        }

        return "用户 " + userInfo.getUsername() + " (" + userInfo.getRating() + ") 当前位于本群No." + userInfo.getRank() + " 位" +
                "\n上次比赛为 " + userInfo.getLastContestName() + "，更新时间：" + utils.time.timestampToString(userInfo.getUpdateTime());
    }

    /**
     * 结束指定时间的比赛并进行相关更新。
     * 该方法首先判断比赛是否已经结束，如果是，则移除该比赛，更新比赛列表，并重新计算所有参赛用户的评级。
     *
     * @param contestTime 比赛的开始时间戳，单位为秒。
     * @return 如果成功结束比赛并进行了相关更新，则返回true；如果比赛尚未结束，则返回false。
     */
    public boolean endContest(long contestTime) {
        // 根据比赛时间获取比赛对象
        CFContests contests = CONTESTS_TIME.get(contestTime);
        // 获取当前时间戳，单位为秒
        long nowTime = System.currentTimeMillis() / 1000;
        // 计算比赛的结束时间戳，单位为秒
        long endTime = contests.getDurationSeconds() + contestTime;
        if (nowTime > endTime){
            // 如果当前时间大于比赛结束时间，则认为比赛已经结束，进行后续处理
            endContestHandel(contests);
            // 移除指定时间的比赛
            CONTESTS_TIME.remove(contestTime);
            // 更新比赛列表
//            getUserRating()
            return true;
        }
        // 如果当前时间未超过比赛结束时间，则不进行处理，返回false
        return false;
    }

    private void endContestHandel(CFContests contests){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateContests();
                updateAllUserRating();
                logger.info("比赛 " + contests.getName() + " 已结束");
                StringBuilder message = new StringBuilder("比赛 " + contests.getName() + " 已结束 \n");
                sendAllGroupRank(message);
                timer.cancel(); //执行完毕停止定时器
            }
            // 5秒后执行结束相关信息
        }, 5000);
    }

}
