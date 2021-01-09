package com.kakarote.core.utils;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.IdUtil;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.redis.Redis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;

/**
 * @author z
 * 一些通用方法
 */
public class BaseUtil {

    private static final Snowflake SNOWFLAKE = IdUtil.getSnowflake(1, 1);

    /**
     * 获取redis
     *
     * @return redis
     */
    public static Redis getRedis() {
        return UserCacheUtil.ME.redis;
    }

    /**
     * 获取当前年月的字符串
     *
     * @return yyyyMMdd
     */
    public static String getDate() {
        return DateUtil.format(new Date(), "yyyyMMdd");
    }

    /**
     * 获取request对象
     *
     * @return request
     */

    public static HttpServletRequest getRequest() {
        return Optional.ofNullable(UserUtil.getUser()).map(UserInfo::getRequest).orElse(null);
    }

    /**
     * 获取long类型的id，雪花算法
     * @return id
     */
    public static Long getNextId(){
        return SNOWFLAKE.nextId();
    }


    /**
     * 获取response对象
     *
     * @return response
     */
    public static HttpServletResponse getResponse() {
        return UserUtil.getUser().getResponse();
    }

    /**
     * 默认的上传文件路径
     */
    public final static String UPLOAD_PATH = BaseUtil.isWindows() ? "D:\\upload\\" : "/usr/local/upload/";

    /**
     * 获取当前是否是windows系统
     * @return true代表为真
     */
    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    public static String getIp(){
        return "127.0.0.1";
    }

    public static boolean isTime(String str){
        if (Validator.isMactchRegex(Validator.BIRTHDAY, str)) {
            Matcher matcher = Validator.BIRTHDAY.matcher(str);
            if (matcher.find()) {
                int year = Integer.parseInt(matcher.group(1));
                int month = Integer.parseInt(matcher.group(3));
                int day = Integer.parseInt(matcher.group(5));
                // 验证月
                if (month < 1 || month > 12) {
                    return false;
                }

                // 验证日
                if (day < 1 || day > 31) {
                    return false;
                }
                // 检查几个特殊月的最大天数
                if (day == 31 && (month == 4 || month == 6 || month == 9 || month == 11)) {
                    return false;
                }
                if (month == 2) {
                    // 在2月，非闰年最大28，闰年最大29
                    return day < 29 || (day == 29 && DateUtil.isLeapYear(year));
                }
                return true;
            }
        }
        return false;
    }
}
