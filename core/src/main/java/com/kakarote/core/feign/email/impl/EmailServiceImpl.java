package com.kakarote.core.feign.email.impl;

import com.kakarote.core.common.Result;
import com.kakarote.core.feign.email.EmailService;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService {
    /**
     * 获取邮箱ID
     *
     * @param userId 用户ID
     * @return 邮箱ID
     */
    @Override
    public Result<Integer> getEmailId(Long userId) {
        return Result.ok(null);
    }
}
