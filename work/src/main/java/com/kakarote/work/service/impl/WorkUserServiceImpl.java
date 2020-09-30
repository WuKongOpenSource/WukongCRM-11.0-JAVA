package com.kakarote.work.service.impl;

import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.work.entity.PO.WorkUser;
import com.kakarote.work.mapper.WorkUserMapper;
import com.kakarote.work.service.IWorkUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 项目成员表 服务实现类
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
@Service
public class WorkUserServiceImpl extends BaseServiceImpl<WorkUserMapper, WorkUser> implements IWorkUserService {

    /**
     * 查询角色ID
     *
     * @param userId 用户ID
     * @param workId 项目ID
     * @return data
     */
    @Override
    public Integer getRoleId(Long userId, Integer workId) {
        WorkUser workUser = lambdaQuery().eq(WorkUser::getUserId, userId).eq(WorkUser::getWorkId, workId).last(" limit 1").one();
        return workUser != null ? workUser.getRoleId() : null;
    }
}
