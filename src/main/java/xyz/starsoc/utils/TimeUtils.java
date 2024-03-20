package xyz.starsoc.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {
    public static final TimeUtils INSTANCE = new TimeUtils();

    public String timestampToString(long time) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    public String timeToFormat(long time) {
        int day = (int) (time / 86400);
        int hour = (int) ((time % 86400) / 3600);
        int minute = (int) ((time % 86400 % 3600) / 60);
        int second = (int) (time % 86400 % 3600 % 60);
        String result = "";
        if (day > 0) {
            result += day + "天";
        }
        if (hour > 0) {
            result += hour + "小时";
        }
        if (minute > 0) {
            result += minute + "分钟";
        }
        result += second + "秒";
        return result;
    }

    public  boolean isMidnight(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int millisecond = calendar.get(Calendar.MILLISECOND);

        // 检查是否为凌晨（00:00:00.000）
        return (hour == 0 && minute == 0 && second == 0 && millisecond == 0);
    }
}
