package com.kakarote.examine.service;


import com.kakarote.core.feign.crm.entity.ExamineField;

import java.util.List;
import java.util.Map;

public interface  ExamineModuleService {

    /**
     * 查询审批可用的字段
     * @param label label
     * @param categoryId categoryId
     * @return data
     */
    public List<ExamineField> queryExamineField(Integer label, Integer categoryId);

    /**
     * 修改审核状态
     * @param label 类型
     * @param typeId 对应类型主键ID
     * @param checkStatus 审核状态 0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交 6 创建 7 已删除 8 作废
     */
    public void updateCheckStatus(Integer label,Integer typeId,Integer checkStatus);

    /**
     * 校验当前状态是否可以进行，不可进行直接抛异常即可
     * @param label 类型
     * @param typeId 对应类型ID
     * @param checkStatus 审核状态
     * @param oldCheckStatus 之前的审核状态
     */
    public void checkStatus(Integer label,Integer typeId,Integer checkStatus,Integer oldCheckStatus);

    /**
     * 获取条件审核需要的数据entity
     * @param label 类型
     * @param typeId 对应类型ID
     * @return map
     */
    public Map<String,Object> getConditionMap(Integer label,Integer typeId,Integer recordId);
}
