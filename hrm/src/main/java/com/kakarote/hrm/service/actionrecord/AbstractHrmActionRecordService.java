package com.kakarote.hrm.service.actionrecord;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.kakarote.hrm.constant.LabelGroupEnum;
import com.kakarote.hrm.entity.PO.HrmDept;
import com.kakarote.hrm.entity.PO.HrmEmployee;
import com.kakarote.hrm.service.IHrmActionRecordService;
import com.kakarote.hrm.service.IHrmDeptService;
import com.kakarote.hrm.service.IHrmEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

public abstract class AbstractHrmActionRecordService {

    @Autowired
    protected IHrmActionRecordService actionRecordService;

    @Autowired
    private IHrmEmployeeService employeeService;

    @Autowired
    private IHrmDeptService hrmDeptService;

    /**
     * 实体类编辑通用处理
     *
     * @return
     */
    protected List<String> entityCommonUpdateRecord(LabelGroupEnum labelGroupEnum, Dict properties, Map<String, Object> oldColumns, Map<String, Object> newColumns) {
        List<String> contentList = new ArrayList<>();
        String defaultValue = "空";
        for (String oldFieldKey : oldColumns.keySet()) {
            if (!properties.containsKey(oldFieldKey)) {
                continue;
            }
            Object oldValueObj = oldColumns.get(oldFieldKey);
            if (newColumns.containsKey(oldFieldKey)) {
                Object newValueObj = newColumns.get(oldFieldKey);
                String oldValue;
                String newValue;
                //转换value
                if (newValueObj instanceof Date || oldValueObj instanceof Date) {
                    oldValue = DateUtil.formatDateTime(Convert.toDate(oldValueObj));
                    newValue = DateUtil.formatDateTime(Convert.toDate(newValueObj));
                }else if (newValueObj instanceof BigDecimal || oldValueObj instanceof BigDecimal) {
                    oldValue = Convert.toBigDecimal(oldValueObj,new BigDecimal(0)).setScale(2, BigDecimal.ROUND_UP).toString();
                    newValue = Convert.toBigDecimal(newValueObj,new BigDecimal(0)).setScale(2, BigDecimal.ROUND_UP).toString();
                }else {
                    oldValue = Convert.toStr(oldValueObj);
                    newValue = Convert.toStr(newValueObj);
                }
                if (StrUtil.isEmpty(oldValue)) {
                    oldValue = defaultValue;
                }
                if (StrUtil.isEmpty(newValue)) {
                    newValue = defaultValue;
                }
                if (!Objects.equals(oldValue,newValue)) {
                    contentList.add(compare(labelGroupEnum, properties, oldFieldKey, oldValue, newValue));
                }
            }
        }
        return contentList;
    }

    /**
     * 比较返回content
     *
     * @param newFieldKey 字段名称
     * @param oldValue    老值
     * @param newValue    新值
     * @return content
     */
    protected String compare(LabelGroupEnum labelGroupEnum, Dict properties, String newFieldKey, String oldValue, String newValue) {
        return "将" + properties.getStr(newFieldKey) + "由" + oldValue + "改为" + newValue;
    }

    /**
     * 员工比较
     *
     * @param fieldName
     * @param oldValue
     * @param newValue
     * @return
     */
    protected String employeeCompare(String fieldName, String oldValue, String newValue) {
        HrmEmployee oldEmployee = employeeService.getById(oldValue);
        HrmEmployee newEmployee = employeeService.getById(newValue);
        String oldDesc = "无";
        String newDesc = "无";
        if (oldEmployee != null){
            oldDesc = oldEmployee.getEmployeeName();
        }
        if (newEmployee != null){
            newDesc = newEmployee.getEmployeeName();
        }
        return "将" + fieldName + "由" + oldDesc + "改为" + newDesc;
    }

    /**
     * 人力资源部门比较
     *
     * @param fieldName
     * @param oldValue
     * @param newValue
     * @return
     */
    protected String hrmDeptCompare(String fieldName, String oldValue, String newValue) {
        HrmDept oldDept = hrmDeptService.getById(oldValue);
        HrmDept newDept = hrmDeptService.getById(newValue);
        String oldDesc = "无";
        String newDesc = "无";
        if (oldDept != null){
            oldDesc = oldDept.getName();
        }
        if (newDept != null){
            newDesc = newDept.getName();
        }
        return "将" + fieldName + "由" + oldDesc + "改为" + newDesc;
    }

}
