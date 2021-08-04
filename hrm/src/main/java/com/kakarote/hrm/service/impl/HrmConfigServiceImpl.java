package com.kakarote.hrm.service.impl;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.hrm.entity.PO.HrmConfig;
import com.kakarote.hrm.mapper.HrmConfigMapper;
import com.kakarote.hrm.service.IHrmConfigService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 人力资源配置表 服务实现类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-13
 */
@Service
public class HrmConfigServiceImpl extends BaseServiceImpl<HrmConfigMapper, HrmConfig> implements IHrmConfigService {
    @Override
    public void addOrUpdate(HrmConfig hrmConfig) {
        saveOrUpdate(hrmConfig);
    }

    @Override
    public List<HrmConfig> queryListByType(Integer type) {
        return lambdaQuery().eq(HrmConfig::getType,type).orderByAsc(HrmConfig::getCreateTime).list();
    }
}
