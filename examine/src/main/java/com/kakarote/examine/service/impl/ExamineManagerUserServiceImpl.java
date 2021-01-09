package com.kakarote.examine.service.impl;

import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.examine.entity.PO.ExamineManagerUser;
import com.kakarote.examine.mapper.ExamineManagerUserMapper;
import com.kakarote.examine.service.IExamineManagerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 审批管理员设置表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-11-13
 */
@Service
public class ExamineManagerUserServiceImpl extends BaseServiceImpl<ExamineManagerUserMapper, ExamineManagerUser> implements IExamineManagerUserService {

    @Autowired
    private AdminService adminService;

    /**
     * 通过审批ID查询审批管理员
     *
     * @param examineId 审批ID
     * @return data
     */
    @Override
    public List<Long> queryExamineUserByPage(Long examineId) {
        List<ExamineManagerUser> managerUsers = lambdaQuery().eq(ExamineManagerUser::getExamineId, examineId).orderByAsc(ExamineManagerUser::getSort).list();
        return managerUsers.stream().map(ExamineManagerUser::getUserId).collect(Collectors.toList());
    }


    @Override
    public List<Long> queryExamineUser(Long examineId) {
        List<ExamineManagerUser> managerUsers = lambdaQuery().eq(ExamineManagerUser::getExamineId, examineId).orderByAsc(ExamineManagerUser::getSort).list();
        List<Long> userIds = managerUsers.stream().map(ExamineManagerUser::getUserId).collect(Collectors.toList());
        List<Long> userIdList = adminService.queryNormalUserByIds(userIds).getData();
        if (userIdList.size() == 0) {
            userIdList.add(UserUtil.getSuperUser());
        }
        return userIdList;
    }
}
