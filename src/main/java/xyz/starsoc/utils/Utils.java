package xyz.starsoc.utils;

import xyz.starsoc.file.Config;

import java.util.logging.Logger;

public class Utils {

    public static final Utils INSTANCE = new Utils();

    public Logger logger = Logger.getLogger("Utils-Debug");

    public  GsonUtils gson = GsonUtils.INSTANCE;
    public  HttpClientUtils http = HttpClientUtils.INSTANCE;

    public  RedisUtils redis = RedisUtils.INSTANCE;

    public TimeUtils time = TimeUtils.INSTANCE;

    public void debug(String msg){
        if (Config.INSTANCE.getDebug()) logger.info(msg);
    }

}
