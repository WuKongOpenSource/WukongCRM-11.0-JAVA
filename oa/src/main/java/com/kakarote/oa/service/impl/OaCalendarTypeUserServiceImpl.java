package com.kakarote.oa.service.impl;

import com.kakarote.core.common.Result;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.oa.entity.PO.OaCalendarTypeUser;
import com.kakarote.oa.mapper.OaCalendarTypeUserMapper;
import com.kakarote.oa.service.IOaCalendarTypeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户关联日历类型 服务实现类
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
@Service
public class OaCalendarTypeUserServiceImpl extends BaseServiceImpl<OaCalendarTypeUserMapper, OaCalendarTypeUser> implements IOaCalendarTypeUserService {

    @Autowired
    private AdminService adminService;

    @Override
    public void saveSysCalendarType(Integer typeId) {
        Result<List<Long>> listResult = adminService.queryUserList();
        List<Long> userIds = listResult.getData();
        List<OaCalendarTypeUser> oaCalendarTypeUsers = new ArrayList<>();
        userIds.forEach(userId->{
            OaCalendarTypeUser oaCalendarTypeUser = new OaCalendarTypeUser();
            oaCalendarTypeUser.setTypeId(typeId);
            oaCalendarTypeUser.setUserId(userId);
            oaCalendarTypeUsers.add(oaCalendarTypeUser);
        });
        saveBatch(oaCalendarTypeUsers);
    }
}
