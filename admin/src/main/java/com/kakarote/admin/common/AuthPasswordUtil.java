package com.kakarote.admin.common;

import cn.hutool.crypto.SecureUtil;

/**
 * @author z
 * 密码加密工具
 */
public class AuthPasswordUtil {

    /**
     * 验证签名是否正确
     *
     * @param key  key
     * @param salt 盐
     * @param sign 签名
     * @return 是否正确 true为正确
     */
    public static boolean verify(String key, String salt, String sign) {
        return sign.equals(sign(key, salt))||sign.equals(signP(key,salt))||sign.equals(signP2(key,salt));
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

    /**
     * 签名数据
     * PHP端签名
     *
     * @param key  key
     * @param salt 盐
     * @return 加密后的字符串
     */
    private static String signP(String key, String salt) {
        String username=key.substring(0,11);
        String password=key.substring(11);
        return SecureUtil.md5(SecureUtil.md5(SecureUtil.sha1(username.concat(password)))+SecureUtil.md5(password.concat(salt)));
    }

    private static String signP2(String key, String salt) {
        String username=key.substring(0,11);
        String password=key.substring(11);
        return SecureUtil.md5(SecureUtil.sha1(password)+SecureUtil.md5(password.concat(salt)));
    }
}
