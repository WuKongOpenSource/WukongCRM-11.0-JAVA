package com.kakarote.work.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.work.entity.PO.WorkUser;

/**
 * <p>
 * 项目成员表 服务类
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
public interface IWorkUserService extends BaseService<WorkUser> {
    /**
     * 查询角色ID
     * @param userId 用户ID
     * @param workId 项目ID
     * @return data
     */
    public Integer getRoleId(Long userId,Integer workId);
}
