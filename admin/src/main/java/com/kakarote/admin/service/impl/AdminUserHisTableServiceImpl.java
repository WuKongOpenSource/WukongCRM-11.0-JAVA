package com.kakarote.admin.service.impl;

import com.kakarote.admin.common.AdminCodeEnum;
import com.kakarote.admin.entity.PO.AdminConfig;
import com.kakarote.admin.entity.PO.AdminUser;
import com.kakarote.admin.entity.PO.AdminUserHisTable;
import com.kakarote.admin.mapper.AdminUserHisTableMapper;
import com.kakarote.admin.service.IAdminConfigService;
import com.kakarote.admin.service.IAdminUserHisTableService;
import com.kakarote.admin.service.IAdminUserService;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 授权坐席 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
@Service
public class AdminUserHisTableServiceImpl extends BaseServiceImpl<AdminUserHisTableMapper, AdminUserHisTable> implements IAdminUserHisTableService {

    @Autowired
    private IAdminConfigService adminConfigService;
    @Autowired
    private IAdminUserService adminUserService;

    /**
     * 员工坐席授权
     */
    @Override
    public boolean authorize(List<Long> userIds, Integer status){
        AdminConfig adminConfig = adminConfigService.lambdaQuery()
                .eq(AdminConfig::getName,"call").last(" limit 1").one();
        if(adminConfig == null || adminConfig.getStatus() != 1){
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
        }
        if (userIds.size() == 0 ){
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
        }
        AdminUserHisTable userHisTable;
        for (Long userId: userIds) {
            userHisTable = this.lambdaQuery()
                    .eq(AdminUserHisTable::getUserId,userId).eq(AdminUserHisTable::getType,1).last(" limit 1").one();
            if (userHisTable == null){
                userHisTable = new AdminUserHisTable();
            }
            userHisTable.setType(1);
            userHisTable.setUserId(userId);
            userHisTable.setHisTable(status);
            if (userHisTable.getHisTableId() == null && status == 1){
                this.save(userHisTable);
            }else if (userHisTable.getHisTableId() != null){
                this.updateById(userHisTable);
            }
        }
        return true;
    }


    /**
     * 判断用户是否为坐席
     * @return
     */
    @Override
    public boolean checkAuth(){
        Long userId= UserUtil.getUserId();
        AdminUser adminUser = adminUserService.getById(userId);
        if(adminUser==null){
            throw new CrmException(AdminCodeEnum.ADMIN_USER_NOT_EXIST_ERROR);
        }
        AdminUserHisTable userHisTable = this.lambdaQuery()
                .eq(AdminUserHisTable::getUserId,userId).eq(AdminUserHisTable::getType,1).last(" limit 1").one();
        return userHisTable != null && userHisTable.getHisTable() != 0;
    }
}
