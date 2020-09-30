package com.kakarote.crm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.entity.PO.CrmFieldConfig;

import java.util.List;

/**
 * <p>
 * 字段配置表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-19
 */
public interface ICrmFieldConfigService extends BaseService<CrmFieldConfig> {


    /**
     * @param label         标签
     * @param fieldType 字段类型
     * @param existNameList 已存在的标签
     * @param isCreateField 是否创建
     * @return fieldName
     */
    public String getNextFieldName(Integer label, Integer fieldType, List<String> existNameList, Integer depth,boolean isCreateField);
}
