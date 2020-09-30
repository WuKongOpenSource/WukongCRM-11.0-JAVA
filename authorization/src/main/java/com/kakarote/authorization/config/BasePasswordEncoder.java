package com.kakarote.authorization.config;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.kakarote.authorization.common.AuthPasswordUtil;
import com.kakarote.authorization.entity.AuthorizationUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author z
 * 密码校验
 */
@Slf4j
public class BasePasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence password) {
        return password.toString();
    }

    @Override
    public boolean matches(CharSequence password, String userJson) {
        if (StrUtil.isEmpty(userJson)) {
            return false;
        }
        try {
            AuthorizationUser user = JSON.parseObject(userJson, AuthorizationUser.class);
            return AuthPasswordUtil.verify(user.getUsername() + password.toString(), user.getSalt(), user.getPassword());
        } catch (Exception e) {
            log.warn("JSON转换错误，str:{}", userJson);
        }
        return false;
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return false;
    }
}
