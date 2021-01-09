package com.kakarote.core.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import com.kakarote.core.common.Const;
import com.kakarote.core.entity.UserExtraInfo;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.redis.Redis;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * @author z
 * 用户操作相关方法
 */
@Slf4j
public class UserUtil {

    private static ThreadLocal<UserInfo> threadLocal = new ThreadLocal<>();


    public static UserInfo getUser() {
        return threadLocal.get();
    }

    public static Long getUserId() {
        UserInfo user = getUser();
        return Optional.ofNullable(user).orElse(new UserInfo()).getUserId();
    }


    public static void setUser(UserInfo adminUser) {
        threadLocal.set(adminUser);
    }

    public static UserInfo setUser(Long userId) {
        UserInfo userInfo = UserCacheUtil.getUserInfo(userId);
        setUser(userInfo);
        return userInfo;
    }


    public static void removeUser() {
        threadLocal.remove();
    }

    /**
     * 验证签名是否正确
     *
     * @param key  key
     * @param salt 盐
     * @param sign 签名
     * @return 是否正确 true为正确
     */
    public static boolean verify(String key, String salt, String sign) {
        return sign.equals(sign(key, salt));
    }

    /**
     * 签名数据
     *
     * @param key  key
     * @param salt 盐
     * @return 加密后的字符串
     */
    public static String sign(String key, String salt) {
        return SecureUtil.md5(key.concat("erp").concat(salt));
    }

    public static void userExpire(String token) {
        Redis redis = BaseUtil.getRedis();
        if (redis.exists(token)) {
            Integer time = Const.MAX_USER_EXIST_TIME;
            redis.expire(token, time);
            redis.expire(Const.USER_ADMIN_TOKEN + getUserId(), time);
            redis.expire(Const.USER_MOBILE_TOKEN + getUserId(), time);
        }
    }

    /**
     * @param token    用户token
     * @param userInfo 用户登录信息
     * @param type     type 1 PC登录 2 mobile登录
     */
    public static void userToken(String token, UserInfo userInfo, Integer type) {
        userExit(userInfo.getUserId(), type, 1);
        Redis redis = BaseUtil.getRedis();
        String userToken = (Objects.equals(2, type) ? Const.USER_MOBILE_TOKEN : Const.USER_ADMIN_TOKEN) + userInfo.getUserId();
        redis.setex(token, Const.MAX_USER_EXIST_TIME, userInfo);
        redis.setex(userToken, Const.MAX_USER_EXIST_TIME, token);
        Cookie cookie = new Cookie(Const.TOKEN_NAME, token);
        Long second = DateUtil.betweenMs(new Date(), DateUtil.parseDate("2030-01-01"))/1000L;
        cookie.setMaxAge(second.intValue());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        BaseUtil.getResponse().addCookie(cookie);
    }

    public static Long getSuperUser() {
        return 14773L;
    }

    public static Integer getSuperRole() {
        return 180162;
    }

    public static boolean isAdmin() {
        UserInfo userInfo = getUser();
        return userInfo.getUserId().equals(userInfo.getSuperUserId()) || userInfo.getRoles().contains(userInfo.getSuperRoleId());
    }

    public static void userExit(Long userId, Integer type) {
        userExit(userId, type, null);
    }

    public static void userExit(Long userId, Integer type, Integer extra) {
        Redis redis = BaseUtil.getRedis();
        String token = null, key = null;
        if (type == null || type == 1) {
            key = Const.USER_ADMIN_TOKEN + userId;
        }
        if (type == null || type == 2) {
            key = Const.USER_MOBILE_TOKEN + userId;
        }
        if (key != null) {
            if (redis.exists(key)) {
                token = redis.get(key);
                redis.del(key);
            }
        }
        //1代表被挤掉提示
        if (Objects.equals(1, extra) && token != null) {
            Long time = redis.ttl(token);
            if (time > 1L) {
                redis.setex(token, time.intValue(), new UserExtraInfo(1, DateUtil.formatDateTime(new Date())));
            }
        } else {
            if (token != null) {
                redis.del(token);
            }
        }
    }

}
