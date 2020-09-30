package com.kakarote.crm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.entity.PO.CrmCustomerSetting;

/**
 * <p>
 * 员工拥有以及锁定客户数限制 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-29
 */
public interface ICrmCustomerSettingService extends BaseService<CrmCustomerSetting> {
    /**
     * 查询客户拥有客户数限制
     * @param type 类型 1 拥有客户数限制 2 锁定客户数限制
     * @param userId 用户ID
     * @param offset 增加量 默认1
     * @return 是否可增加
     */
    public boolean queryCustomerSettingNum(int type, Long userId, int offset);

    /**
     * 查询客户拥有客户数限制
     * @param type 类型 1 拥有客户数限制 2 锁定客户数限制
     * @param userId 用户ID
     * @return 是否可增加
     */
    public boolean queryCustomerSettingNum(int type, Long userId);
}
