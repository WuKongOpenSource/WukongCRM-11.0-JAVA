package com.kakarote.hrm.mapper;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.hrm.entity.BO.QuerySalaryArchivesListBO;
import com.kakarote.hrm.entity.DTO.ExcelTemplateOption;
import com.kakarote.hrm.entity.PO.HrmSalaryArchives;
import com.kakarote.hrm.entity.VO.ChangeSalaryOptionVO;
import com.kakarote.hrm.entity.VO.QueryChangeRecordListVO;
import com.kakarote.hrm.entity.VO.QuerySalaryArchivesListVO;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 薪资档案表 Mapper 接口
 * </p>
 *
 * @author hmb
 * @since 2020-11-05
 */
public interface HrmSalaryArchivesMapper extends BaseMapper<HrmSalaryArchives> {

    BasePage<QuerySalaryArchivesListVO> querySalaryArchivesList(BasePage<QuerySalaryArchivesListVO> parse, @Param("data") QuerySalaryArchivesListBO querySalaryArchivesListBO,
                                                                @Param("employeeIds") Collection<Integer> employeeIds);

    List<QueryChangeRecordListVO> queryChangeRecordList(@Param("employeeId") Integer employeeId);

    List<ChangeSalaryOptionVO> queryBatchChangeOption();

    List<ExcelTemplateOption> queryFixSalaryExcelExportOption();

    List<ExcelTemplateOption> queryChangeSalaryExcelExportOption();

}
