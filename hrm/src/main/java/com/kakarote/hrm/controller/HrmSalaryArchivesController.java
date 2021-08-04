package com.kakarote.hrm.controller;


import cn.hutool.core.collection.CollUtil;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SubModelType;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.SysLog;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.hrm.common.log.HrmSalaryArchivesLog;
import com.kakarote.hrm.entity.BO.*;
import com.kakarote.hrm.entity.DTO.ExcelTemplateOption;
import com.kakarote.hrm.entity.PO.HrmSalaryArchivesOption;
import com.kakarote.hrm.entity.VO.*;
import com.kakarote.hrm.service.IHrmSalaryArchivesOptionService;
import com.kakarote.hrm.service.IHrmSalaryArchivesService;
import com.kakarote.hrm.utils.SalaryExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 薪资档案表 前端控制器
 * </p>
 *
 * @author hmb
 * @since 2020-11-05
 */
@RestController
@RequestMapping("/hrmSalaryArchives")
@Api(tags = "薪资档案")
@SysLog(subModel = SubModelType.HRM_SALARY_FILE,logClass = HrmSalaryArchivesLog.class)
public class HrmSalaryArchivesController {

    @Autowired
    private IHrmSalaryArchivesService salaryArchivesService;

    @Autowired
    private IHrmSalaryArchivesOptionService archivesOptionService;

    @PostMapping("/querySalaryArchivesList")
    @ApiOperation("查询薪资档案列表")
    public Result<BasePage<QuerySalaryArchivesListVO>> querySalaryArchivesList(@RequestBody QuerySalaryArchivesListBO querySalaryArchivesListBO) {
        BasePage<QuerySalaryArchivesListVO> page = salaryArchivesService.querySalaryArchivesList(querySalaryArchivesListBO);
        return Result.ok(page);
    }

    @PostMapping("/querySalaryArchivesById/{employeeId}")
    @ApiOperation("查询薪资档案信息")
    public Result<QuerySalaryArchivesByIdVO> querySalaryArchivesById(@PathVariable Integer employeeId) {
        QuerySalaryArchivesByIdVO querySalaryArchivesByIdVO = salaryArchivesService.querySalaryArchivesById(employeeId);
        return Result.ok(querySalaryArchivesByIdVO);
    }

    @PostMapping("/queryChangeRecordList/{employeeId}")
    @ApiOperation("查询调薪记录列表")
    public Result<List<QueryChangeRecordListVO>> queryChangeRecordList(@PathVariable Integer employeeId) {
        List<QueryChangeRecordListVO> list = salaryArchivesService.queryChangeRecordList(employeeId);
        return Result.ok(list);
    }


    @PostMapping("/setFixSalaryRecord")
    @ApiOperation("单个定薪")
    @SysLogHandler(behavior = BehaviorEnum.SAVE)
    public Result setFixSalaryRecord(@RequestBody SetFixSalaryRecordBO setFixSalaryRecordBO) {
        salaryArchivesService.setFixSalaryRecord(setFixSalaryRecordBO);
        return Result.ok();
    }

    @PostMapping("/queryFixSalaryRecordById/{id}")
    @ApiOperation("查询定薪记录详情")
    public Result<FixSalaryRecordDetailVO> queryFixSalaryRecordById(@PathVariable Integer id) {
        FixSalaryRecordDetailVO data = salaryArchivesService.queryFixSalaryRecordById(id);
        return Result.ok(data);
    }

    @PostMapping("/queryChangeOptionByTemplateId")
    @ApiOperation("查询调薪项的值(单个调薪使用)")
    public Result<QueryChangeOptionValueVO> queryChangeOptionValue(@RequestBody QueryChangeOptionValueBO changeOptionValueBO) {
        QueryChangeOptionValueVO data = salaryArchivesService.queryChangeOptionValue(changeOptionValueBO);
        return Result.ok(data);
    }

    @PostMapping("/setChangeSalaryRecord")
    @ApiOperation("单个调薪")
    public Result setChangeSalaryRecord(@RequestBody SetChangeSalaryRecordBO setChangeSalaryRecordBO) {
        salaryArchivesService.setChangeSalaryRecord(setChangeSalaryRecordBO);
        return Result.ok();
    }

    @PostMapping("/queryChangeSalaryRecordById/{id}")
    @ApiOperation("查询调薪记录详情")
    public Result<ChangeSalaryRecordDetailVO> queryChangeSalaryRecordById(@PathVariable Integer id) {
        ChangeSalaryRecordDetailVO data = salaryArchivesService.queryChangeSalaryRecordById(id);
        return Result.ok(data);
    }

    @PostMapping("/cancelChangeSalary/{id}")
    @ApiOperation("取消调薪")
    public Result cancelChangeSalary(@PathVariable Integer id) {
        salaryArchivesService.cancelChangeSalary(id);
        return Result.ok();
    }

    @PostMapping("/deleteChangeSalary/{id}")
    @ApiOperation("删除调薪")
    public Result deleteChangeSalary(@PathVariable Integer id) {
        salaryArchivesService.deleteChangeSalary(id);
        return Result.ok();
    }

    @PostMapping("/queryBatchChangeOption")
    @ApiOperation("查询批量调薪项")
    public Result<List<ChangeSalaryOptionVO>> queryBatchChangeOption() {
        List<ChangeSalaryOptionVO> list = salaryArchivesService.queryBatchChangeOption();
        return Result.ok(list);
    }

    @PostMapping("/batchChangeSalaryRecord")
    @ApiOperation("批量调薪")
    @SysLogHandler(behavior = BehaviorEnum.SAVE,object = "批量调薪",detail = "批量调薪")
    public Result<Map<String, Integer>> batchChangeSalaryRecord(@RequestBody BatchChangeSalaryRecordBO batchChangeSalaryRecordBO) {
        Map<String, Integer> result = salaryArchivesService.batchChangeSalaryRecord(batchChangeSalaryRecordBO);
        return Result.ok(result);
    }

    @PostMapping("/downLoadFixTemplate")
    @ApiOperation("下载定薪模板")
    public void downLoadFixTemplate(@RequestBody QuerySalaryArchivesListBO querySalaryArchivesListBO, HttpServletResponse response) throws IOException {
        List<ExcelTemplateOption> templateOptionList = salaryArchivesService.queryFixSalaryExcelExportOption();
        querySalaryArchivesListBO.setPageType(0);
        querySalaryArchivesListBO.setStatus(11);
        List<QuerySalaryArchivesListVO> employeeList = salaryArchivesService.querySalaryArchivesList(querySalaryArchivesListBO).getList();
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("fixTemplate.xlsx");
             XSSFWorkbook wb = new XSSFWorkbook(inputStream);
             ServletOutputStream out = response.getOutputStream()) {
            int colNum = 7;
            int rowNum = 7;
            XSSFSheet sheet = wb.getSheetAt(0);
            XSSFRow row5 = sheet.getRow(5);
            XSSFRow row6 = sheet.getRow(6);
            for (ExcelTemplateOption categoryOption : templateOptionList) {
                List<ExcelTemplateOption> optionList = categoryOption.getOptionList();
                if (CollUtil.isEmpty(optionList)){
                    continue;
                }
                if (optionList.size() > 1) {
                    sheet.addMergedRegion(new CellRangeAddress(row5.getRowNum(), row5.getRowNum(), colNum, colNum + optionList.size() - 1));
                }
                SalaryExcelUtil.createHeadCell(wb, row5, colNum, categoryOption.getName());
                for (int i = 0; i < optionList.size(); i++) {
                    ExcelTemplateOption option = optionList.get(i);
                    SalaryExcelUtil.createHeadCell(wb, row6, colNum + i, option.getName());
                }
                colNum += optionList.size();
            }
            sheet.addMergedRegion(new CellRangeAddress(row5.getRowNum(), row6.getRowNum(), colNum, colNum));
            SalaryExcelUtil.createHeadCell(wb, row5, colNum, "备注");
            for (int i = 0; i < employeeList.size(); i++) {
                QuerySalaryArchivesListVO employeeArchives = employeeList.get(i);
                XSSFRow row = sheet.createRow(rowNum + i);
                SalaryExcelUtil.createBodyCell(wb, row, 0, employeeArchives.getEmployeeName());
                SalaryExcelUtil.createBodyCell(wb, row, 1, employeeArchives.getJobNumber());
                SalaryExcelUtil.createBodyCell(wb, row, 2, employeeArchives.getDeptName());
                SalaryExcelUtil.createBodyCell(wb, row, 3, employeeArchives.getPost());
            }
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=fixSalaryTemplate.xlsx");
            wb.write(out);
        }
    }


    @PostMapping("/exportFixSalaryRecord")
    @ApiOperation("导入定薪")
    @SysLogHandler(behavior = BehaviorEnum.SAVE,object = "导入定薪",detail = "导入定薪")
    public Result<Long> exportFixSalaryRecord(@RequestParam("file") MultipartFile multipartFile) {
        Long messageId = salaryArchivesService.exportFixSalaryRecord(multipartFile);
        return Result.ok(messageId);
    }


    @PostMapping("/downLoadChangeTemplate")
    @ApiOperation("下载调薪模板")
    public void downLoadChangeTemplate(@RequestBody QuerySalaryArchivesListBO querySalaryArchivesListBO, HttpServletResponse response) throws IOException {
        List<ExcelTemplateOption> templateOptionList = salaryArchivesService.queryChangeSalaryExcelExportOption();
        querySalaryArchivesListBO.setPageType(0);
        querySalaryArchivesListBO.setStatus(11);
        List<QuerySalaryArchivesListVO> employeeList = salaryArchivesService.querySalaryArchivesList(querySalaryArchivesListBO).getList();
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("changeTemplate.xlsx");
             XSSFWorkbook wb = new XSSFWorkbook(inputStream);
             ServletOutputStream out = response.getOutputStream()) {
            int colNum = 12;
            int rowNum = 7;
            XSSFSheet sheet = wb.getSheetAt(0);
            XSSFRow row5 = sheet.getRow(5);
            XSSFRow row6 = sheet.getRow(6);
            Map<Integer, Integer> codeIndexMap = new HashMap<>();
            for (ExcelTemplateOption categoryOption : templateOptionList) {
                sheet.addMergedRegion(new CellRangeAddress(row5.getRowNum(), row5.getRowNum(), colNum, colNum + 1));
                SalaryExcelUtil.createHeadCell(wb, row5, colNum, categoryOption.getName());
                SalaryExcelUtil.createHeadCell(wb, row6, colNum, "调整前");
                SalaryExcelUtil.createHeadCell(wb, row6, colNum + 1, "调整后");
                codeIndexMap.put(categoryOption.getCode(), colNum);
                colNum += 2;
            }
            sheet.addMergedRegion(new CellRangeAddress(row5.getRowNum(), row6.getRowNum(), colNum, colNum));
            SalaryExcelUtil.createHeadCell(wb, row5, colNum, "备注");
            for (int i = 0; i < employeeList.size(); i++) {
                QuerySalaryArchivesListVO employeeArchives = employeeList.get(i);
                XSSFRow row = sheet.createRow(rowNum + i);
                SalaryExcelUtil.createBodyCell(wb, row, 0, employeeArchives.getEmployeeName());
                SalaryExcelUtil.createBodyCell(wb, row, 1, employeeArchives.getJobNumber());
                SalaryExcelUtil.createBodyCell(wb, row, 2, employeeArchives.getDeptName());
                SalaryExcelUtil.createBodyCell(wb, row, 3, employeeArchives.getPost());
                Map<Integer, Map<Integer, String>> collect = archivesOptionService.lambdaQuery().eq(HrmSalaryArchivesOption::getEmployeeId, employeeArchives.getEmployeeId()).list()
                        .stream().collect(Collectors.groupingBy(HrmSalaryArchivesOption::getIsPro, Collectors.toMap(HrmSalaryArchivesOption::getCode, HrmSalaryArchivesOption::getValue)));
                Map<Integer, String> proCodeValueMap = collect.get(1);
                Map<Integer, String> codeValueMap = collect.get(0);
                SalaryExcelUtil.createBodyCell(wb, row, 6, getValue(proCodeValueMap, 10101));
                SalaryExcelUtil.createBodyCell(wb, row, 8, getValue(proCodeValueMap, 10102));
                SalaryExcelUtil.createBodyCell(wb, row, 10, getValue(proCodeValueMap, 10103));
                codeIndexMap.forEach((code, index) -> {
                    SalaryExcelUtil.createBodyCell(wb, row, index, getValue(codeValueMap, code));
                });
            }
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=changeSalaryTemplate.xlsx");
            wb.write(out);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CrmException(SystemCodeEnum.SYSTEM_ERROR);
        }
    }

    @PostMapping("/exportChangeSalaryRecord")
    @ApiOperation("导入调薪")
    @SysLogHandler(behavior = BehaviorEnum.SAVE,object = "导入调薪",detail = "导入调薪")
    public Result<Long> exportChangeSalaryRecord(@RequestParam("file") MultipartFile multipartFile) {
        Long messageId = salaryArchivesService.exportChangeSalaryRecord(multipartFile);
        return Result.ok(messageId);
    }

    private String getValue(Map<Integer, String> codeValueMap, Integer code) {
        if (CollUtil.isEmpty(codeValueMap)) {
            return "0";
        }
        return Optional.ofNullable(codeValueMap.get(code)).orElse("0");
    }


}

