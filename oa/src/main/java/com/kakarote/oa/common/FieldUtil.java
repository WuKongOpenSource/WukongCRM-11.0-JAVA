package com.kakarote.oa.common;

import com.kakarote.oa.entity.PO.OaExamineField;

import java.util.List;

/**
 * @author wyq
 */
public class FieldUtil {
    private List<OaExamineField> recordList;



    public List<OaExamineField> getRecordList(){
        return recordList;
    }

    public FieldUtil(){}

    public FieldUtil(List<OaExamineField> recordList) {
        this.recordList = recordList;
    }

    /**
     *
     * @param fieldName  字段名
     * @param name  中文名
     * @param formType  类型
     * @param settingArr 选项
     * @param isNull 是否必填 0否 1 是
     * @param isUnique 是否验证 0否 1 是
     * @return
     */
    public FieldUtil oaFieldAdd(String fieldName, String name, String formType, List<String> settingArr, Integer isNull,Integer isUnique,Object value,String defaultValue,Integer operating,Integer fieldType){
        OaExamineField record = new OaExamineField();
        record.setFieldName(fieldName);
        record.setName(name);
        record.setMaxLength(0);
        record.setIsUnique(isUnique);
        record.setIsNull(isNull);
        record.setOperating(operating);
        record.setFieldType(fieldType);
        record.setFormType(formType);
        record.setSetting(settingArr);
        record.setValue(value);
        record.setDefaultValue(defaultValue);
        recordList.add(record);
        return this;
    }

}
