package com.kakarote.crm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.entity.PO.CrmBusinessStatus;

/**
 * <p>
 * 商机状态 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
public interface ICrmBusinessStatusService extends BaseService<CrmBusinessStatus> {

    String getBusinessStatusName(int statusId);
}
