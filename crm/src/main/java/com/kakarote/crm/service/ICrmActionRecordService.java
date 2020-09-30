package com.kakarote.crm.service;

import cn.hutool.core.lang.Dict;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.PO.CrmActionRecord;
import com.kakarote.crm.entity.VO.CrmActionRecordVO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;

import java.util.List;

/**
 * <p>
 * 字段操作记录表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-25
 */
public interface ICrmActionRecordService extends BaseService<CrmActionRecord> {

    /**
     * 删除字段记录类型
     *
     * @param crmEnum 类型
     * @param ids     ids
     */
    public void deleteActionRecord(CrmEnum crmEnum, List<Integer> ids);

    /**
     * 查询自定义欢迎语
     *
     * @return data
     */
    public List<String> queryRecordOptions();

    /**
     * 查询字段操作记录列表
     *
     * @param actionId 类型
     * @param types    type
     * @return data
     */
    public List<CrmActionRecordVO> queryRecordList(Integer actionId, Integer types);

    List<CrmModelFiledVO> queryFieldValue(Dict kv);
}
