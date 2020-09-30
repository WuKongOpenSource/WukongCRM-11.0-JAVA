package com.kakarote.crm.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.crm.entity.PO.CrmCustomer;
import com.kakarote.crm.entity.PO.CrmCustomerSetting;
import com.kakarote.crm.entity.PO.CrmCustomerSettingUser;
import com.kakarote.crm.mapper.CrmCustomerSettingMapper;
import com.kakarote.crm.service.ICrmCustomerService;
import com.kakarote.crm.service.ICrmCustomerSettingService;
import com.kakarote.crm.service.ICrmCustomerSettingUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 员工拥有以及锁定客户数限制 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-29
 */
@Service
public class CrmCustomerSettingServiceImpl extends BaseServiceImpl<CrmCustomerSettingMapper, CrmCustomerSetting> implements ICrmCustomerSettingService {

    @Autowired
    private ICrmCustomerSettingUserService crmCustomerSettingUserService;

    @Autowired
    private ICrmCustomerService crmCustomerService;

    /**
     * 查询客户拥有客户数限制
     *
     * @param type   类型 1 拥有客户数限制 2 锁定客户数限制
     * @param userId 用户ID
     * @param offset 增加量 默认1
     */
    @Override
    public boolean queryCustomerSettingNum(int type, Long userId, int offset) {
        List<CrmCustomerSetting> settingList = lambdaQuery().eq(CrmCustomerSetting::getType, type).orderByAsc(CrmCustomerSetting::getType).list();
        if (settingList.size() > 0) {
            UserInfo userInfo = UserCacheUtil.getUserInfo(userId);
            List<CrmCustomerSettingUser> settingUserList = crmCustomerSettingUserService.lambdaQuery().and(l -> {
                l.eq(CrmCustomerSettingUser::getType, 1);
                l.eq(CrmCustomerSettingUser::getUserId, userInfo.getUserId());
            }).or(l -> {
                l.eq(CrmCustomerSettingUser::getType, 2);
                l.eq(CrmCustomerSettingUser::getDeptId, userInfo.getDeptId());
            }).list();
            if (settingUserList.size() == 0) {
                return true;
            }
            Map<Integer, List<CrmCustomerSettingUser>> collect = settingUserList.stream().collect(Collectors.groupingBy(CrmCustomerSettingUser::getSettingId));
            for (CrmCustomerSetting setting : settingList) {
                if (collect.containsKey(setting.getSettingId())) {
                    LambdaQueryChainWrapper<CrmCustomer> chainWrapper = crmCustomerService.lambdaQuery()
                            .ne(CrmCustomer::getStatus, 3)
                            .eq(CrmCustomer::getOwnerUserId, userId);
                    if (setting.getType() == 1 && setting.getCustomerDeal() == 0) {
                        chainWrapper.eq(CrmCustomer::getDealStatus, 0);
                    }
                    if (setting.getType() == 2) {
                        chainWrapper.eq(CrmCustomer::getStatus, 2);
                    }
                    Integer num = chainWrapper.count();
                    return num + offset <= setting.getCustomerNum();
                }
            }
        }
        return true;
    }

    /**
     * 查询客户拥有客户数限制
     *
     * @param type   类型 1 拥有客户数限制 2 锁定客户数限制
     * @param userId 用户ID
     */
    @Override
    public boolean queryCustomerSettingNum(int type, Long userId) {
        return queryCustomerSettingNum(type, userId, 1);
    }
}
