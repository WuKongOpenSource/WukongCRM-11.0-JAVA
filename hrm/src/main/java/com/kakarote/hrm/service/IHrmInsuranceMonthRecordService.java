package com.kakarote.hrm.service;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.BO.QueryInsurancePageListBO;
import com.kakarote.hrm.entity.BO.QueryInsuranceRecordListBO;
import com.kakarote.hrm.entity.PO.HrmInsuranceMonthRecord;
import com.kakarote.hrm.entity.VO.QueryInsurancePageListVO;
import com.kakarote.hrm.entity.VO.QueryInsuranceRecordListVO;

/**
 * <p>
 * 每月社保记录 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
public interface IHrmInsuranceMonthRecordService extends BaseService<HrmInsuranceMonthRecord> {


    /**
     * 核算社保数据
     * @return
     */
    Integer computeInsuranceData();

    /**
     * 查询社保统计数据列表
     * @param recordListBO
     * @return
     */
    BasePage<QueryInsuranceRecordListVO> queryInsuranceRecordList(QueryInsuranceRecordListBO recordListBO);

    /**
     * 查询社保数据列表
     * @param queryInsurancePageListBO
     * @return
     */
    BasePage<QueryInsurancePageListVO> queryInsurancePageList(QueryInsurancePageListBO queryInsurancePageListBO);

    /**
     * 查询社保详情统计数据(详情统计)
     * @param iRecordId
     * @return
     */
    QueryInsuranceRecordListVO queryInsuranceRecord(String iRecordId);

    /**
     * 删除社保记录
     * @param iRecordId
     */
    void deleteInsurance(Integer iRecordId);
}
