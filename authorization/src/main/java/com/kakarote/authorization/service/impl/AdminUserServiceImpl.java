package com.kakarote.authorization.service.impl;

import com.kakarote.authorization.entity.AdminUserStatusBO;
import com.kakarote.authorization.service.AdminUserService;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SystemCodeEnum;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhang
 * 向admin模块请求企业信息
 */
@Component
public class AdminUserServiceImpl implements AdminUserService {


    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 结果信息
     */
    @Override
    public Result findByUsername(String username) {
        return Result.error(SystemCodeEnum.SYSTEM_NO_FOUND);
    }

    /**
     * 通过用户ID查询所属角色
     *
     * @param userId 用户ID
     * @return data
     */
    @Override
    public Result<List<Integer>> queryUserRoleIds(Long userId) {
        return Result.ok(new ArrayList<>());
    }

    @Override
    public Result<List<String>> queryNoAuthMenu(Long userId) {
        return Result.error(SystemCodeEnum.SYSTEM_NO_FOUND);
    }

    @Override
    public Result setUserStatus(AdminUserStatusBO adminUserStatusBO) {
        return Result.ok();
    }
}
