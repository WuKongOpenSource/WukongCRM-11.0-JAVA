package com.kakarote.oa.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.FieldEnum;
import com.kakarote.core.field.FieldService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.oa.entity.PO.OaExamineFieldExtend;
import com.kakarote.oa.mapper.OaExamineFieldExtendMapper;
import com.kakarote.oa.service.IOaExamineFieldExtendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 自定义字段表 服务实现类
 * </p>
 *
 * @author JiaS
 * @since 2021-03-04
 */
@Service
public class OaExamineFieldExtendServiceImpl extends BaseServiceImpl<OaExamineFieldExtendMapper, OaExamineFieldExtend> implements IOaExamineFieldExtendService {

    @Autowired
    private FieldService fieldService;

    @Override
    public List<OaExamineFieldExtend> queryOaExamineFieldExtend(Integer parentFieldId) {
        List<OaExamineFieldExtend> fieldExtends = lambdaQuery().eq(OaExamineFieldExtend::getParentFieldId, parentFieldId).list();
        fieldExtends.forEach(fieldExtend -> recordToFormType(fieldExtend, FieldEnum.parse(fieldExtend.getType())));
        return fieldExtends;
    }

    @Override
    public boolean saveOrUpdateOaExamineFieldExtend(List<OaExamineFieldExtend> oaExamineFieldExtendList, Integer parentFieldId, boolean isUpdate) {
        if (parentFieldId == null){
            return false;
        }
        if (isUpdate) {
            this.deleteOaExamineFieldExtend(parentFieldId);
        }
        for (OaExamineFieldExtend fieldExtend : oaExamineFieldExtendList) {
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
    public boolean deleteOaExamineFieldExtend(Integer parentFieldId) {
        return lambdaUpdate().eq(OaExamineFieldExtend::getParentFieldId,parentFieldId).remove();
    }


    private void recordToFormType(OaExamineFieldExtend record, FieldEnum typeEnum) {
        record.setFormType(typeEnum.getFormType());
        switch (typeEnum) {
            case CHECKBOX:
                record.setDefaultValue(StrUtil.splitTrim((CharSequence) record.getDefaultValue(), Const.SEPARATOR));
            case SELECT:
                if(Objects.equals(record.getRemark(),FieldEnum.OPTIONS_TYPE.getFormType())) {
                    JSONObject optionsData = JSON.parseObject(record.getOptions());
                    record.setOptionsData(optionsData);
                    record.setSetting(new ArrayList<>(optionsData.keySet()));
                }else {
                    record.setSetting(StrUtil.splitTrim(record.getOptions(), Const.SEPARATOR));
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
