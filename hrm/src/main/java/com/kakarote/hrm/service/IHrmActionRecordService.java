package com.kakarote.hrm.service;

import cn.hutool.core.lang.Dict;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.constant.HrmActionBehaviorEnum;
import com.kakarote.hrm.constant.HrmActionTypeEnum;
import com.kakarote.hrm.entity.BO.QueryRecordListBO;
import com.kakarote.hrm.entity.PO.HrmActionRecord;
import com.kakarote.hrm.entity.VO.HrmModelFiledVO;
import com.kakarote.hrm.entity.VO.QueryRecordListVO;

import java.util.List;

/**
 * <p>
 * hrm员工操作记录表 服务类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
public interface IHrmActionRecordService extends BaseService<HrmActionRecord> {
    /**
     * 保存操作记录
     * @param actionTypeEnum  操作对象类型
     * @param behaviorEnum  行为类型
     * @param content   内容
     * @param typeId    类型id
     * @return
     */
    boolean saveRecord(HrmActionTypeEnum actionTypeEnum, HrmActionBehaviorEnum behaviorEnum, List<String> content, Integer typeId);


    /**
     * 查询操作记录列表
     * @param queryRecordListBO
     * @return
     */
    List<QueryRecordListVO> queryRecordList(QueryRecordListBO queryRecordListBO);

    /**
     * 通过字典值查询hrm需要的自定义字段
     * @param kv 字典值
     * @return
     */
    List<HrmModelFiledVO> queryFieldValue(Dict kv);
}
