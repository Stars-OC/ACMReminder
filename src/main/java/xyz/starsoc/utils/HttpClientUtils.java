package xyz.starsoc.utils;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.starsoc.ACMReminder;
import xyz.starsoc.file.Config;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class HttpClientUtils {

    public static final HttpClientUtils INSTANCE = new HttpClientUtils();

    private Logger logger = LoggerFactory.getLogger("HttpClientUtils");

    private static final Config config = Config.INSTANCE;

    private OkHttpClient client(){
        return new OkHttpClient().newBuilder()
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(config.getProxyHost(), config.getProxyPort())))
                .build();
    }

    public String getJson(String url){
        Request request = new Request.Builder().url(url).build();
        Call call = client().newCall(request);
        try {
            return call.execute().body().string();
        } catch (IOException e) {
            logger.error("{} 获取json数据失败",url);
        }
        return null;
    }
}
