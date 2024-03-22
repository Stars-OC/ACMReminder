package xyz.starsoc.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {
    public static final TimeUtils INSTANCE = new TimeUtils();

    /**
     * 将时间戳转换为字符串格式的日期时间。
     *
     * @param time 时间戳，单位为毫秒。
     * @return 格式化后的日期时间字符串，格式为"yyyy-MM-dd HH:mm:ss"。
     */
    public String timestampToString(long time) {
        time *= 1000;
        // 将时间戳转换为Date对象
        Date date = new Date(time);
        // 创建SimpleDateFormat对象，并指定日期时间格式
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 使用指定格式将Date对象转换为字符串
        return format.format(date);
    }

    /**
     * 将时间戳转换为格式化的字符串表达
     *
     * @param time 输入的时间戳，单位为秒
     * @return 格式化后的时间字符串，格式为"天小时分钟秒"，根据时间戳的实际情况可能不包含所有的单位
     */
    public String timeToFormat(long time) {
        // 计算天数
        int day = (int) (time / 86400);
        // 计算小时数
        int hour = (int) ((time % 86400) / 3600);
        // 计算分钟数
        int minute = (int) ((time % 86400 % 3600) / 60);
        // 计算秒数
        int second = (int) (time % 86400 % 3600 % 60);
        String result = "";
        // 根据天数是否大于0，添加天数到结果字符串
        if (day > 0) {
            result += day + "天";
        }
        // 根据小时数是否大于0，添加小时数到结果字符串
        if (hour > 0) {
            result += hour + "小时";
        }
        // 根据分钟数是否大于0，添加分钟数到结果字符串
        if (minute > 0) {
            result += minute + "分钟";
        }
        // 添加秒数到结果字符串
        result += second + "秒";
        return result;
    }

    /**
     * 判断给定的时间戳是否表示午夜（00:00:00.000）。
     *
     * @param timestamp 表示特定时间的长整型时间戳。
     * @return 如果给定的时间戳表示的是午夜，则返回true；否则返回false。
     */
    public boolean isMidnight(long timestamp) {
        // 获取当前时间的日历实例，并将时间戳设置到该日历实例上
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        // 获取小时、分钟、秒和毫秒
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

//        System.out.println("hour:" + hour + " minute:" + minute + " second:" + second + " millisecond:" + millisecond);
        // 检查是否为凌晨（00:00:00.000）
        return (hour == 0 && minute == 0 && second == 0);
    }

}
