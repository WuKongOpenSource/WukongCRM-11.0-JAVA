package com.kakarote.hrm.service;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.BO.*;
import com.kakarote.hrm.entity.DTO.ExcelTemplateOption;
import com.kakarote.hrm.entity.PO.HrmSalaryArchives;
import com.kakarote.hrm.entity.PO.HrmSalaryArchivesOption;
import com.kakarote.hrm.entity.VO.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 薪资档案表 服务类
 * </p>
 *
 * @author hmb
 * @since 2020-11-05
 */
public interface IHrmSalaryArchivesService extends BaseService<HrmSalaryArchives> {

    /**
     * 查询薪资档案列表
     * @param querySalaryArchivesListBO
     * @return
     */
    BasePage<QuerySalaryArchivesListVO> querySalaryArchivesList(QuerySalaryArchivesListBO querySalaryArchivesListBO);

    /**
     * 单个定薪
     * @param setFixSalaryRecordBO
     */
    void setFixSalaryRecord(SetFixSalaryRecordBO setFixSalaryRecordBO);

    /**
     * 查询薪资档案信息
     * @param employeeId
     * @return
     */
    QuerySalaryArchivesByIdVO querySalaryArchivesById(Integer employeeId);

    /**
     * 查询调薪记录列表
     * @param employeeId
     * @return
     */
    List<QueryChangeRecordListVO> queryChangeRecordList(Integer employeeId);

    /**
     * 查询定薪记录详情
     * @param id
     * @return
     */
    FixSalaryRecordDetailVO queryFixSalaryRecordById(Integer id);

    /**
     * 单个调薪
     * @param setChangeSalaryRecordBO
     */
    void setChangeSalaryRecord(SetChangeSalaryRecordBO setChangeSalaryRecordBO);

    /**
     * 查询调薪记录详情
     * @param id
     * @return
     */
    ChangeSalaryRecordDetailVO queryChangeSalaryRecordById(Integer id);

    /**
     * 取消调薪
     * @param id
     */
    void cancelChangeSalary(Integer id);

    /**
     * 删除调薪
     * @param id
     */
    void deleteChangeSalary(Integer id);

    /**
     * 查询调薪项的值(单个调薪使用)
     * @param changeOptionValueBO
     * @return
     */
    QueryChangeOptionValueVO queryChangeOptionValue(QueryChangeOptionValueBO changeOptionValueBO);

    /**
     * 查询批量调薪项
     * @return
     */
    List<ChangeSalaryOptionVO> queryBatchChangeOption();

    /**
     * 批量调薪
     * @param batchChangeSalaryRecordBO
     * @return
     */
    Map<String,Integer> batchChangeSalaryRecord(BatchChangeSalaryRecordBO batchChangeSalaryRecordBO);


    /**
     * 查询定薪导入工资项
     * @return
     */
    List<ExcelTemplateOption> queryFixSalaryExcelExportOption();


    /**
     * 查询调薪导出项
     * @return
     */
    List<ExcelTemplateOption> queryChangeSalaryExcelExportOption();

    /**
     * 导入定薪
     * @param multipartFile
     * @return
     */
    Long exportFixSalaryRecord(MultipartFile multipartFile);

    /**
     * 导入调薪
     * @param multipartFile
     * @return
     */
    Long exportChangeSalaryRecord(MultipartFile multipartFile);

    /**
     * 查询薪资档案项(生成薪资使用)
     * @param employeeId
     * @param year
     * @param month
     * @return
     */
    List<HrmSalaryArchivesOption> querySalaryArchivesOption(Integer employeeId, int year, int month);
}
