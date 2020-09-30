package com.kakarote.crm.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.crm.common.ElasticUtil;
import com.kakarote.crm.entity.PO.CrmFieldConfig;
import com.kakarote.crm.mapper.CrmFieldConfigMapper;
import com.kakarote.crm.service.ICrmFieldConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 字段配置表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-19
 */
@Service
@Slf4j
public class CrmFieldConfigServiceImpl extends BaseServiceImpl<CrmFieldConfigMapper, CrmFieldConfig> implements ICrmFieldConfigService {

    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    /**
     * @param label         标签
     * @param fieldType     字段类型
     * @param existNameList 已存在的标签
     * @param isCreateField 是否创建索引字段
     * @return fieldName
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String getNextFieldName(Integer label, Integer fieldType, List<String> existNameList, Integer depth, boolean isCreateField) {
        if (depth < 0) {
            throw new CrmException(SystemCodeEnum.SYSTEM_ERROR);
        }
        QueryWrapper<CrmFieldConfig> configQueryWrapper = new QueryWrapper<>();
        QueryWrapper<CrmFieldConfig> eq = configQueryWrapper.select("field_name").eq("label", label).eq("field_type", parseType(fieldType));
        if (existNameList.size() > 0) {
            eq.notIn("field_name", existNameList);
        }
        List<String> nameList = listObjs(configQueryWrapper, Object::toString);
        if (nameList.size() == 0) {
            String name;
            try {
                name = "flied_" + RandomUtil.randomString(RandomUtil.BASE_CHAR, 6);
                CrmFieldConfig crmFieldConfig = new CrmFieldConfig();
                crmFieldConfig.setFieldType(parseType(fieldType));
                crmFieldConfig.setLabel(label);
                crmFieldConfig.setFieldName(name);
                save(crmFieldConfig);
                if (isCreateField) {
                    ElasticUtil.addField(restTemplate.getClient(), crmFieldConfig, fieldType);
                }
            } catch (Exception e) {
                log.error("保存出现冲突", e);
                name = getNextFieldName(label, fieldType, existNameList, --depth, isCreateField);
            }
            nameList.add(name);
        }
        return nameList.get(0);
    }

    /**
     * 初始化成elastic数据类型
     * @param fieldType 字段类型
     * @return esType 1 keyword 2 date 3 number 4 nested 5 datetime
     */
    private Integer parseType(Integer fieldType) {
        Integer[] nested = new Integer[]{2, 9};
        Integer[] date = new Integer[]{4, 13};
        Integer[] number = new Integer[]{5, 6};
        if (Arrays.asList(nested).contains(fieldType)) {
            return 4;
        }
        if (Arrays.asList(date).contains(fieldType)) {
            if (Objects.equals(4, fieldType)) {
                return 2;
            } else {
                return 5;
            }
        }
        if (Arrays.asList(number).contains(fieldType)) {
            return 3;
        }
        return 1;
    }
}
