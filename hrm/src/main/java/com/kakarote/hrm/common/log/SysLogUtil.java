package com.kakarote.hrm.common.log;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.kakarote.hrm.service.IHrmDeptService;
import com.kakarote.hrm.service.IHrmEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class SysLogUtil {

    private static Map<String, Dict> propertiesMap = new HashMap<>();

    @Autowired
    private IHrmDeptService deptService;

    @Autowired
    private IHrmEmployeeService employeeService;

    static {
        propertiesMap.put("dept", Dict.create().set("pid", "上级组织").set("deptType", "部门类型")
                .set("name", "组织名称").set("code", "组织编号").set("mainEmployeeId", "组织负责人").set("leaderEmployeeId", "分管领导"));
        propertiesMap.put("",Dict.create().set("candidateName", "候选人名称").set("mobile", "手机").set("sex", "性别").set("age", "年龄")
                .set("postId", "职位").set("workTime", "工作年限").set("education", "学历").set("graduateSchool", "毕业院校").set("latestWorkPlace", "最近工作单位")
                .set("channelId", "招聘渠道").set("remark", "备注"));
    }

    public List<String> updateRecord(Map<String, Object> oldColumns, Map<String, Object> newColumns, String type) {
        Dict properties = propertiesMap.get(type);
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
                } else if (newValueObj instanceof BigDecimal || oldValueObj instanceof BigDecimal) {
                    oldValue = Convert.toBigDecimal(oldValueObj, new BigDecimal(0)).setScale(2, BigDecimal.ROUND_UP).toString();
                    newValue = Convert.toBigDecimal(newValueObj, new BigDecimal(0)).setScale(2, BigDecimal.ROUND_UP).toString();
                } else {
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
                    if ("dept".equals(type)) {
                        if ("pid".equals(oldFieldKey)) {
                            if (!"空".equals(oldValue)) {
                                oldValue = deptService.getById(Integer.parseInt(oldValue)).getName();
                            }
                            if (!"空".equals(newValue)) {
                                newValue = deptService.getById(Integer.parseInt(newValue)).getName();
                            }
                        } else if ("deptType".equals(oldFieldKey)) {
                            if (!"空".equals(oldValue)) {
                                if (Integer.parseInt(oldValue) == 1) {
                                    oldValue = "公司";
                                } else {
                                    oldValue = "部门";
                                }
                            }
                            if (!"空".equals(newValue)) {
                                if (Integer.parseInt(newValue) == 1) {
                                    newValue = "公司";
                                } else {
                                    newValue = "部门";
                                }
                            }
                        }else if ("mainEmployeeId".equals(oldFieldKey) || "leaderEmployeeId".equals(oldFieldKey)){
                            if (!"空".equals(oldValue)) {
                                oldValue = employeeService.getById(Integer.parseInt(oldValue)).getEmployeeName();
                            }
                            if (!"空".equals(newValue)) {
                                newValue = employeeService.getById(Integer.parseInt(newValue)).getEmployeeName();
                            }
                        }
                    }
                    String detail = "将" + properties.getStr(oldFieldKey) + "由" + oldValue + "改为" + newValue;
                    contentList.add(detail);
                }
            }
        }
        return contentList;
    }
}
