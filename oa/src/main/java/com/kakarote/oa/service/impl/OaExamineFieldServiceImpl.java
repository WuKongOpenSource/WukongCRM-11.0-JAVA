package com.kakarote.oa.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.FieldEnum;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.examine.entity.ExamineInfoVo;
import com.kakarote.core.feign.examine.service.ExamineService;
import com.kakarote.core.field.FieldService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.oa.common.OaCodeEnum;
import com.kakarote.oa.entity.BO.ExamineFieldBO;
import com.kakarote.oa.entity.PO.OaExamineData;
import com.kakarote.oa.entity.PO.OaExamineField;
import com.kakarote.oa.entity.PO.OaExamineFieldExtend;
import com.kakarote.oa.mapper.OaExamineFieldMapper;
import com.kakarote.oa.service.IOaExamineDataService;
import com.kakarote.oa.service.IOaExamineFieldExtendService;
import com.kakarote.oa.service.IOaExamineFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 自定义字段表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-22
 */
@Service
public class OaExamineFieldServiceImpl extends BaseServiceImpl<OaExamineFieldMapper, OaExamineField> implements IOaExamineFieldService {

    @Autowired
    private FieldService fieldService;

    @Autowired
    private IOaExamineDataService examineDataService;

    @Autowired
    private ExamineService examineService;

    @Autowired
    private IOaExamineFieldExtendService oaExamineFieldExtendService;

    @Override
    public List<OaExamineField> queryField(Integer id) {
        List<OaExamineField> list = lambdaQuery().eq(OaExamineField::getExamineCategoryId, id).orderByAsc(OaExamineField::getSorting).list();
        ExamineInfoVo examineInfoVo = examineService.queryExamineById(Long.valueOf(id)).getData();
        Integer oaType = Optional.ofNullable(examineInfoVo.getOaType()).orElse(0);
        if (ListUtil.toList(1,2,3,4,5,6).contains(oaType)){
            for (int i = 0; i < list.size(); i++) {
                OaExamineField field = list.get(i);
                if (Objects.equals(oaType,2) && "审批内容".equals(field.getName())){
                    field.setName("请假事由");
                }
                field.setFormPosition(i + ",0");
                field.setStylePercent(100);
            }
        }
        recordToFormType(list);
        return list;
    }

    @Override
    public List<List<OaExamineField>> queryFormPositionField(Integer id) {
        List<OaExamineField> list = this.queryField(id);
        return fieldService.convertFormPositionFieldList(list,OaExamineField::getXAxis,OaExamineField::getYAxis, OaExamineField::getSorting);
    }

    private void recordToFormType(List<OaExamineField> list) {
        for (OaExamineField record : list) {
            FieldEnum typeEnum = FieldEnum.parse(record.getType());
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
                        record.setSetting(new ArrayList<>(StrUtil.splitTrim(record.getOptions(), Const.SEPARATOR)));
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
                case DETAIL_TABLE:
                    record.setFieldExtendList(oaExamineFieldExtendService.queryOaExamineFieldExtend(record.getFieldId()));
                    break;
                default:
                    record.setSetting(new ArrayList<>());
                    break;
            }
        }
    }

    @Override
    public Boolean updateFieldCategoryId(Long newCategoryId,Long oldCategoryId) {
        if (newCategoryId != null && oldCategoryId != null) {
            List<OaExamineField> oaExamineFields = this.lambdaQuery().eq(OaExamineField::getExamineCategoryId, oldCategoryId).list();
            for (OaExamineField oaExamineField : oaExamineFields) {
                oaExamineField.setFieldId(null);
                oaExamineField.setExamineCategoryId(newCategoryId.intValue());
            }
            saveBatch(oaExamineFields,Const.BATCH_SAVE_SIZE);
            return true;
        }
        return false;
    }

    /**
     * 根据batchId查询values
     * @param batchId batchId
     * @return valuesMap
     */
    @Override
    public Map<Integer,String> queryFieldData(String batchId) {
        List<OaExamineData> examineData = examineDataService.lambdaQuery().select(OaExamineData::getFieldId,OaExamineData::getValue).eq(OaExamineData::getBatchId, batchId).list();
        Map<Integer,String> dataMap = new HashMap<>(examineData.size(),1.0f);
        for (OaExamineData data : examineData) {
            dataMap.put(data.getFieldId(),data.getValue());
        }
        return dataMap;
    }

    @Override
    public void transferFieldList(List<OaExamineField> recordList, Integer isDetail) {
        recordList.forEach(record -> {
            Integer dataType = record.getType();
            if (isDetail == 2) {
               if (FieldEnum.CHECKBOX.getType().equals(dataType)) {
                    if (ObjectUtil.isNotEmpty(record.getValue())) {
                        String[] split = StrUtil.split((String) record.getValue(), ",");
                        record.setValue(split);
                    }
                }
            }
            if (ObjectUtil.isNotEmpty(record.getValue())) {
                FieldEnum fieldEnum = FieldEnum.parse(dataType);
                record.setValue(fieldService.convertValueByFormType(record.getValue(),fieldEnum));
            }

        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveField(ExamineFieldBO examineFieldBO) {
        List<OaExamineField> data = examineFieldBO.getData();
        Map<String, List<OaExamineField>> collect = data.stream().collect(Collectors.groupingBy(OaExamineField::getName));
        for (Map.Entry<String, List<OaExamineField>> entry : collect.entrySet()) {
            if (entry.getValue().size() > 1) {
                throw new CrmException(OaCodeEnum.THE_NAME_OF_THE_CUSTOM_FORM_CANNOT_BE_REPEATED);
            }
        }
        Integer categoryId = examineFieldBO.getCategoryId();
        ExamineInfoVo category = examineService.queryExamineById(Long.valueOf(categoryId)).getData();
        if (category != null && ListUtil.toList(1,2,3,4,5,6).contains(category.getOaType())) {
            throw new CrmException(OaCodeEnum.SYSTEM_EXAMINE_CAN_NOT_MODIFY);
        }
        List<Integer> arr = data.stream().filter(oaExamineField -> oaExamineField.getFieldId() != null).map(OaExamineField::getFieldId).collect(Collectors.toList());
        if (arr.size() > 0) {
            getBaseMapper().deleteByChooseId(arr, categoryId);
            getBaseMapper().deleteByFieldValue(arr, categoryId);
            oaExamineFieldExtendService.lambdaUpdate().in(OaExamineFieldExtend::getParentFieldId,arr).remove();
        }
        for (int i = 0; i < data.size(); i++) {
            OaExamineField entity = data.get(i);
            entity.setUpdateTime(DateUtil.date());
            entity.setExamineCategoryId(categoryId);
            entity.setSorting(i);
            if (ObjectUtil.isEmpty(entity.getDefaultValue())) {
                entity.setDefaultValue("");
            }else {
                boolean isNeedHandle = fieldService.equalsByType(entity.getType(),FieldEnum.AREA,FieldEnum.AREA_POSITION,FieldEnum.CURRENT_POSITION);
                if (isNeedHandle) {
                    entity.setDefaultValue(JSON.toJSONString(entity.getDefaultValue()));
                }
                if (entity.getDefaultValue() instanceof Collection) {
                    entity.setDefaultValue(StrUtil.join(Const.SEPARATOR, entity.getDefaultValue()));
                }
            }
            if (entity.getFieldId() != null) {
                if (FieldEnum.DETAIL_TABLE.getType().equals(entity.getType())){
                    oaExamineFieldExtendService.saveOrUpdateOaExamineFieldExtend(entity.getFieldExtendList(),entity.getFieldId(),true);
                }
                updateById(entity);
                examineDataService.lambdaUpdate().set(OaExamineData::getName,entity.getName()).eq(OaExamineData::getFieldId,entity.getFieldId()).update();
            } else {
                entity.setFieldName(getFieldName(entity.getExamineCategoryId()));
                save(entity);
                if (FieldEnum.DETAIL_TABLE.getType().equals(entity.getType())){
                    oaExamineFieldExtendService.saveOrUpdateOaExamineFieldExtend(entity.getFieldExtendList(),entity.getFieldId(),false);
                }
            }
        }
    }


    @Override
    public void saveDefaultField(Long categoryId){
        OaExamineField content = new OaExamineField();
        content.setName("审批事由");
        content.setFieldName("content");
        content.setMaxLength(0);
        content.setType(2);
        content.setIsNull(1);
        content.setUpdateTime(new Date());
        content.setOperating(1);
        content.setFieldType(1);
        content.setExamineCategoryId(categoryId.intValue());
        this.save(content);
        content.setFieldId(null);
        content.setFieldName("remark");
        content.setIsNull(0);
        content.setName("备注");
        this.save(content);
    }

    /**
     * 获取fieldName
     * @param categoryId c
     * @return fieldName
     */
    private String getFieldName(Integer categoryId){
        List<OaExamineField> fields = lambdaQuery().select(OaExamineField::getFieldName).eq(OaExamineField::getExamineCategoryId, categoryId).list();
        List<String> names = fields.stream().map(OaExamineField::getFieldName).collect(Collectors.toList());
        String name = "field_" + RandomUtil.randomString(RandomUtil.BASE_CHAR,6);
        if(names.contains(name)){
            return getFieldName(categoryId);
        }
        return name;
    }
}
