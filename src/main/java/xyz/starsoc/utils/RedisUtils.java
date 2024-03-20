package xyz.starsoc.utils;

import redis.clients.jedis.JedisPooled;
import xyz.starsoc.file.Redis;

public class RedisUtils {
    private final Redis redis = Redis.INSTANCE;

    private final static GsonUtils gson = GsonUtils.INSTANCE;

    public static final RedisUtils INSTANCE = new RedisUtils();

    public static final String REDIS_CODEFORCES = "codeforces-";

    public static final String REDIS_CODEFORCES_USER = REDIS_CODEFORCES + "user-";

    public static final String REDIS_CODEFORCES_USER_INFO = REDIS_CODEFORCES_USER + "info";

    public static final String REDIS_CODEFORCES_USER_RATING = REDIS_CODEFORCES_USER + "rating";

    public static final String REDIS_CODEFORCES_USER_STATUS = REDIS_CODEFORCES_USER + "status";

    public JedisPooled redis(){

        JedisPooled pool = null;
        String username = redis.getUsername();
        String password = redis.getPassword();
        String host = redis.getHost();
        int port = redis.getPort();

        if (username == "" || password == ""){
            pool = new JedisPooled(host, port);
        }else {
            pool = new JedisPooled(host, port, username, password);
        }

        return pool;
    }

    public void set(String key, String value){
        JedisPooled pooled = redis();
        pooled.set(key, value);
        pooled.close();
    }

    public String get(String key){
        JedisPooled pooled = redis();
        String value = pooled.get(key);
        pooled.close();
        return value;
    }

    public void del(String key){
        JedisPooled pooled = redis();
        pooled.del(key);
        pooled.close();
    }

    public void set(String key, String value, int time){
        JedisPooled pooled = redis();
        pooled.setex(key, time, value);
        pooled.close();
    }

    public void setObject(String key, Object value){
        String json = gson.toJson(value);
        set(key, json);
    }

    public <T> T getObject(String key, Class<T> clazz){
        String json = get(key);
        return gson.fromJson(json, clazz);
    }

    public void setObject(String key, Object value, int time){
        String json = gson.toJson(value);
        set(key, json,time);
    }

    public void setHash(String key, String field, String value){
        JedisPooled pooled = redis();
        pooled.hset(key, field, value);
        pooled.close();
    }

    public String getHash(String key, String field){
        JedisPooled pooled = redis();
        String value = pooled.hget(key, field);
        pooled.close();
        return value;
    }

    public void delHash(String key, String field){
        JedisPooled pooled = redis();
        pooled.hdel(key, field);
        pooled.close();
    }

    public void setHash(String key, String field, String value, int time){
        JedisPooled pooled = redis();
        pooled.hset(key, field, value);
        pooled.expire(key, time);
        pooled.close();
    }

    public void setObjectHash(String key, String field, Object value){
        String json = gson.toJson(value);
        setHash(key, field, json);
    }

    public <T> T getObjectHash(String key, String field, Class<T> clazz){
        String json = getHash(key, field);
        return gson.fromJson(json, clazz);
    }

    public void setObjectHash(String key, String field, Object value, int time){
        String json = gson.toJson(value);
        setHash(key, field, json, time);
    }




}
