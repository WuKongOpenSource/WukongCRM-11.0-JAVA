package com.kakarote.hrm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.FieldEnum;
import com.kakarote.core.field.FieldService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.hrm.entity.PO.HrmFieldExtend;
import com.kakarote.hrm.mapper.HrmFieldExtendMapper;
import com.kakarote.hrm.service.IHrmFieldExtendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 自定义字段表 服务实现类
 * </p>
 *
 * @author guomenghao
 * @since 2021-05-07
 */
@Service
public class HrmFieldExtendServiceImpl extends BaseServiceImpl<HrmFieldExtendMapper, HrmFieldExtend> implements IHrmFieldExtendService {
    @Autowired
    private FieldService fieldService;
    @Override
    public List<HrmFieldExtend> queryHrmFieldExtend(Integer parentFieldId) {
        List<HrmFieldExtend> fieldExtends = lambdaQuery().eq(HrmFieldExtend::getParentFieldId, parentFieldId).list();
        fieldExtends.forEach(fieldExtend -> recordToFormType(fieldExtend,FieldEnum.parse(fieldExtend.getType())));
        return fieldExtends;
    }

    @Override
    public boolean saveOrUpdateHrmFieldExtend(List<HrmFieldExtend> hrmFieldExtendList, Integer parentFieldId, boolean isUpdate) {
        if (parentFieldId == null){
            return false;
        }
        if (isUpdate) {
            this.deleteHrmFieldExtend(parentFieldId);
        }
        for (HrmFieldExtend fieldExtend : hrmFieldExtendList) {
            if (ObjectUtil.isEmpty(fieldExtend.getDefaultValue())) {
                fieldExtend.setDefaultValue("");
            } else {
                boolean isNeedHandle = fieldService.equalsByType(fieldExtend.getType(), FieldEnum.AREA, FieldEnum.AREA_POSITION, FieldEnum.CURRENT_POSITION);
                if (isNeedHandle) {
                    fieldExtend.setDefaultValue(JSON.toJSONString(fieldExtend.getDefaultValue()));
                }
                if (fieldExtend.getDefaultValue() instanceof Collection) {
                    fieldExtend.setDefaultValue(StrUtil.join(Const.SEPARATOR, fieldExtend.getDefaultValue()));
                }
            }
            fieldExtend.setId(null);
            fieldExtend.setParentFieldId(parentFieldId);
            save(fieldExtend);
        }
        return true;
    }

    @Override
    public boolean deleteHrmFieldExtend(Integer parentFieldId) {
        return  lambdaUpdate().eq(HrmFieldExtend::getParentFieldId,parentFieldId).remove();
    }

    private void recordToFormType(HrmFieldExtend record, FieldEnum typeEnum) {
        record.setFormType(typeEnum.getFormType());
        switch (typeEnum) {
            case CHECKBOX:
                record.setDefaultValue(StrUtil.splitTrim((CharSequence) record.getDefaultValue(), Const.SEPARATOR));
            case SELECT:
                if(Objects.equals(record.getRemark(),FieldEnum.OPTIONS_TYPE.getFormType())) {
                    LinkedHashMap<String, Object> optionsData = JSON.parseObject(record.getOptions(), LinkedHashMap.class);
                    record.setOptionsData(optionsData);
                    record.setSetting(new ArrayList<>(optionsData.keySet()));
                }else {
                    if (CollUtil.isEmpty(record.getSetting())) {
                        try {
                            String dtStr = Optional.ofNullable(record.getOptions()).orElse("").toString();
                            List<Object> jsonArrayList = JSON.parseObject(dtStr,List.class);
                            record.setSetting(jsonArrayList);
                        } catch (Exception e) {
                            record.setSetting(new ArrayList<>(StrUtil.splitTrim(record.getOptions(), Const.SEPARATOR)));
                        }
                    }
                }
                break;
            case DATE_INTERVAL:
                String dataValueStr = Optional.ofNullable(record.getDefaultValue()).orElse("").toString();
                record.setDefaultValue(StrUtil.split(dataValueStr, Const.SEPARATOR));
                break;
            case USER:
            case STRUCTURE:
                record.setDefaultValue(new ArrayList<>(0));
                break;
            case AREA:
            case AREA_POSITION:
            case CURRENT_POSITION:
                String defaultValue = Optional.ofNullable(record.getDefaultValue()).orElse("").toString();
                record.setDefaultValue(JSON.parse(defaultValue));
                break;
            default:
                record.setSetting(new ArrayList<>());
                break;
        }
    }
}
