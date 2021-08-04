package com.kakarote.hrm.service;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.constant.TaxType;
import com.kakarote.hrm.entity.BO.*;
import com.kakarote.hrm.entity.PO.HrmSalaryMonthRecord;
import com.kakarote.hrm.entity.VO.QueryHistorySalaryDetailVO;
import com.kakarote.hrm.entity.VO.QueryHistorySalaryListVO;
import com.kakarote.hrm.entity.VO.QuerySalaryPageListVO;
import com.kakarote.hrm.entity.VO.SalaryOptionHeadVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 每月薪资记录 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
public interface IHrmSalaryMonthRecordService extends BaseService<HrmSalaryMonthRecord> {
    /**
     * 查询薪资项表头
     * @return
     */
    List<SalaryOptionHeadVO> querySalaryOptionHead();

    /**
     * 查询计薪人员数量
     * @param type
     * @param taxType
     * @return
     */
    List<Map<String, Object>> queryPaySalaryEmployeeListByType(Integer type, TaxType taxType);

    /**
     * 核算薪资数据
     * @param sRecordId
     * @param isSyncInsuranceData
     * @param attendanceFile
     * @param additionalDeductionFile
     * @param cumulativeTaxOfLastMonthFile
     */
    void computeSalaryData(Integer sRecordId, Boolean isSyncInsuranceData, MultipartFile attendanceFile, MultipartFile additionalDeductionFile, MultipartFile cumulativeTaxOfLastMonthFile);

    /**
     * 查询薪资列表
     * @param querySalaryPageListBO
     * @return
     */
    BasePage<QuerySalaryPageListVO> querySalaryPageList(QuerySalaryPageListBO querySalaryPageListBO);

    /**
     * 在线修改薪资
     * @param updateSalaryBOList
     */
    void updateSalary(List<UpdateSalaryBO> updateSalaryBOList);

    /**
     * 计算薪资统计数据
     */
    HrmSalaryMonthRecord computeSalaryCount(HrmSalaryMonthRecord lastSalaryMonthRecord);

    /**
     * 查询历史薪资列表
     * @param queryHistorySalaryListBO
     * @return
     */
    BasePage<QueryHistorySalaryListVO> queryHistorySalaryList(QueryHistorySalaryListBO queryHistorySalaryListBO);

    /**
     * 查询历史薪资详情
     * @param queryHistorySalaryDetailBO
     * @return
     */
    QueryHistorySalaryDetailVO queryHistorySalaryDetail(QueryHistorySalaryDetailBO queryHistorySalaryDetailBO);

    /**
     * 提交审核
     * @param submitExamineBO
     */
    void submitExamine(SubmitExamineBO submitExamineBO);

    /**
     * 创建下月薪资表
     */
    void addNextMonthSalary();


    /**
     * 查询计薪列表员工异动数量
     * @return
     */
    Map<Integer, Long> queryEmployeeChangeNum();

    /**
     * 查询最新的薪资记录
     * @return
     */
    HrmSalaryMonthRecord queryLastSalaryMonthRecord();

    /**
     * 修改审核状态
     * @param sRecordId
     * @param checkStatus
     */
    void updateCheckStatus(Integer sRecordId, Integer checkStatus);

    /**
     * 查询未计薪人员列表
     * @return
     */
    List<Map<String, Object>> queryNoPaySalaryEmployee();

    /**
     * 查询薪资项合计
     * @param sRecordId
     * @return
     */
    List<Map<String,Object>> querySalaryOptionCount(String sRecordId);

    /**
     * 删除薪资记录
     * @param sRecordId
     */
    void deleteSalary(Integer sRecordId);


}
