package com.kakarote.hrm.service;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.PO.HrmConfig;

import java.util.List;

/**
 * <p>
 * 人力资源配置表 服务类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-13
 */
public interface IHrmConfigService extends BaseService<HrmConfig> {

    void addOrUpdate(HrmConfig hrmConfig);

    List<HrmConfig> queryListByType(Integer type);
}
