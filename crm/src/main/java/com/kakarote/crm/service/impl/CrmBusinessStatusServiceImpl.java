package com.kakarote.crm.service.impl;

import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.crm.entity.PO.CrmBusinessStatus;
import com.kakarote.crm.mapper.CrmBusinessStatusMapper;
import com.kakarote.crm.service.ICrmBusinessStatusService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商机状态 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
@Service
public class CrmBusinessStatusServiceImpl extends BaseServiceImpl<CrmBusinessStatusMapper, CrmBusinessStatus> implements ICrmBusinessStatusService {

    @Override
    public String getBusinessStatusName(int statusId) {
        return lambdaQuery().select(CrmBusinessStatus::getName).eq(CrmBusinessStatus::getStatusId,statusId).oneOpt()
                .map(CrmBusinessStatus::getName).orElse("");
    }
}
