package xyz.starsoc.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import xyz.starsoc.pojo.codeforces.CFContests;
import xyz.starsoc.pojo.codeforces.CFRespond;

import java.lang.reflect.Type;

public class GsonUtils {

    public static final GsonUtils INSTANCE = new GsonUtils();

    public String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public <T> T fromJson(String json, Class<T> type) {
        return new Gson().fromJson(json, type);
    }

    public <T> T fromJson(String json, Type type) {
        return new Gson().fromJson(json, type);
    }


}
