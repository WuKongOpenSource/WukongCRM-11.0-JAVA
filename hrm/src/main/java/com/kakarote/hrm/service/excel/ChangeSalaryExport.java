package com.kakarote.hrm.service.excel;

import cn.hutool.core.date.DateException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import com.kakarote.core.common.cache.AdminCacheKey;
import com.kakarote.core.exception.CrmException;
import com.kakarote.hrm.constant.EmployeeStatusEnum;
import com.kakarote.hrm.constant.SalaryChangeReasonEnum;
import com.kakarote.hrm.entity.BO.SetChangeSalaryRecordBO;
import com.kakarote.hrm.entity.BO.UploadExcelBO;
import com.kakarote.hrm.entity.PO.HrmEmployee;
import com.kakarote.hrm.entity.VO.ChangeSalaryOptionVO;
import com.kakarote.hrm.entity.VO.ChangeSalaryRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class ChangeSalaryExport extends ExcelImport {

    public ChangeSalaryExport(UploadExcelBO uploadExcelBO, MultipartFile file) {
        uploadExcelBO.setFilePath(getFilePath(file));
        super.uploadExcelBO = uploadExcelBO;
    }

    @Override
    public void importExcel() {
        List<ChangeSalaryOptionVO> salaryOptions = archivesService.queryChangeSalaryExcelExportOption().stream().map(option->{
            ChangeSalaryOptionVO changeSalaryOptionVO = new ChangeSalaryOptionVO();
            changeSalaryOptionVO.setName(option.getName());
            changeSalaryOptionVO.setCode(option.getCode());
            return changeSalaryOptionVO;
        }).collect(Collectors.toList());
        List<ChangeSalaryOptionVO> proSalaryOptions = salaryOptions.stream().filter(option -> option.getCode() >= 10100 && option.getCode() <= 10103).map(option->{
            ChangeSalaryOptionVO changeSalaryOptionVO = new ChangeSalaryOptionVO();
            changeSalaryOptionVO.setName(option.getName());
            changeSalaryOptionVO.setCode(option.getCode());
            return changeSalaryOptionVO;
        }).collect(Collectors.toList());
        ExcelUtil.readBySax(uploadExcelBO.getFilePath(), 0, (int sheetIndex, int rowIndex, List<Object> rowList) -> {
            try {
                if (this.num.incrementAndGet() > 10001) {
                    rowList.add(0, "最多同时导入10000条数据");
                    errorList.add(rowList);
                    return;
                }
                redis.setex(AdminCacheKey.UPLOAD_EXCEL_MESSAGE_PREFIX + getUploadExcelBO().getMessageId().toString(), UPLOAD_EXCEL_EXIST_TIME, Math.max(num.get() - 2, 0));
                if (rowIndex == 6) {
                    rowList.add(0, "错误原因");
                    errorList.add(rowList);
                }
                if (rowIndex < 7) {
                    return;
                }
                SetChangeSalaryRecordBO setChangeSalaryRecordBO = new SetChangeSalaryRecordBO();
                String jobNumber = Optional.ofNullable(rowList.get(1)).map(String::valueOf).orElse("");
                HrmEmployee employee;
                if (StrUtil.isEmpty(jobNumber)) {
                    rowList.add(0, "请填写工号");
                    errorList.add(rowList);
                    return;
                } else {
                    Optional<HrmEmployee> employeeOpt = employeeService.lambdaQuery().select(HrmEmployee::getEmployeeId, HrmEmployee::getStatus).eq(HrmEmployee::getJobNumber, jobNumber).oneOpt();
                    if (!employeeOpt.isPresent()) {
                        rowList.add(0, "工号对应的员工不存在");
                        errorList.add(rowList);
                        return;
                    }
                    employee = employeeOpt.get();
                }
                setChangeSalaryRecordBO.setEmployeeId(employee.getEmployeeId());
                String enableDateStr = Optional.ofNullable(rowList.get(4)).map(String::valueOf).orElse("");
                if (StrUtil.isEmpty(enableDateStr)) {
                    rowList.add(0, "请填生效时间");
                    errorList.add(rowList);
                    return;
                } else {
                    try {
                        setChangeSalaryRecordBO.setEnableDate(parseDate(enableDateStr));
                    } catch (DateException e) {
                        rowList.add(0, "填生效时间格式不正确.(example:2020-01-01;2020.01.01;2020/01/01)");
                        errorList.add(rowList);
                        return;
                    }
                }
                String changeReasonStr = Optional.ofNullable(rowList.get(5)).map(String::valueOf).orElse("");
                if (StrUtil.isEmpty(changeReasonStr)) {
                    rowList.add(0, "请填调薪原因");
                    errorList.add(rowList);
                    return;
                } else {
                    int changeReason = SalaryChangeReasonEnum.valueOfType(changeReasonStr);
                    if (changeReason == -1) {
                        rowList.add(0, "调薪原因填写不正确");
                        errorList.add(rowList);
                        return;
                    } else {
                        setChangeSalaryRecordBO.setChangeReason(changeReason);
                    }
                }

                if (employee.getStatus().equals(EmployeeStatusEnum.TRY_OUT.getValue())) {
                    ChangeSalaryRecordVO proSalary = new ChangeSalaryRecordVO();
                    BigDecimal proBeforeSum = new BigDecimal(0);
                    BigDecimal proAfterSum = new BigDecimal(0);
                    List<ChangeSalaryOptionVO> proOldSalary = new ArrayList<>();
                    List<ChangeSalaryOptionVO> proNewSalary = new ArrayList<>();
                    for (int i = 0; i < proSalaryOptions.size(); i++) {
                        ChangeSalaryOptionVO option = proSalaryOptions.get(i);
                        ChangeSalaryOptionVO oldOption = new ChangeSalaryOptionVO();
                        oldOption.setName(option.getName());
                        oldOption.setCode(option.getCode());
                        int index = i * 2 + 6;
                        if (rowList.size()>index){
                            String oldValueStr = getValue(rowList.get(index));
                            String[] oldValueSplit = oldValueStr.split(",");
                            if (oldValueSplit[0].length() > 7 || (oldValueSplit.length == 2 && oldValueSplit[1].length() > 2)) {
                                rowList.add(0, "工资数据格式不正确");
                                errorList.add(rowList);
                                return;
                            }
                            oldOption.setValue(oldValueStr);
                            proOldSalary.add(oldOption);
                            BigDecimal oldValue = new BigDecimal(oldValueStr);
                            proBeforeSum = proBeforeSum.add(oldValue);
                        }else {
                            oldOption.setValue("0");
                            proOldSalary.add(oldOption);
                            BigDecimal oldValue = new BigDecimal(0);
                            proBeforeSum = proBeforeSum.add(oldValue);
                        }
                        ChangeSalaryOptionVO newOption = new ChangeSalaryOptionVO();
                        newOption.setName(option.getName());
                        newOption.setCode(option.getCode());
                        if (rowList.size()>index+1) {
                            String newValueStr = getValue(rowList.get(index+1));
                            String[] newValueSplit = newValueStr.split(",");
                            if (newValueSplit[0].length() > 7 || (newValueSplit.length == 2 && newValueSplit[1].length() > 2)) {
                                rowList.add(0, "工资数据格式不正确");
                                errorList.add(rowList);
                                return;
                            }
                            newOption.setValue(newValueStr);
                            proNewSalary.add(newOption);
                            proAfterSum = proAfterSum.add(new BigDecimal(newValueStr));
                        }else {
                            newOption.setValue("0");
                            proNewSalary.add(newOption);
                            proAfterSum = proAfterSum.add(new BigDecimal(0));
                        }
                    }
                    proSalary.setOldSalary(proOldSalary);
                    proSalary.setNewSalary(proNewSalary);
                    setChangeSalaryRecordBO.setProSalary(proSalary);
                    setChangeSalaryRecordBO.setProBeforeSum(proBeforeSum.toString());
                    setChangeSalaryRecordBO.setProAfterSum(proAfterSum.toString());
                }
                ChangeSalaryRecordVO salary = new ChangeSalaryRecordVO();
                BigDecimal beforeSum = new BigDecimal(0);
                BigDecimal afterSum = new BigDecimal(0);
                List<ChangeSalaryOptionVO> oldSalary = new ArrayList<>();
                List<ChangeSalaryOptionVO> newSalary = new ArrayList<>();
                for (int i = 0; i < salaryOptions.size(); i++) {
                    ChangeSalaryOptionVO option = salaryOptions.get(i);
                    ChangeSalaryOptionVO oldOption = new ChangeSalaryOptionVO();
                    oldOption.setName(option.getName());
                    oldOption.setCode(option.getCode());
                    int index = i * 2 + 12;
                    if (rowList.size()>index){
                        String oldValueStr = getValue(rowList.get(i * 2 + 12));
                        String[] oldValueSplit = oldValueStr.split(",");
                        if (oldValueSplit[0].length() > 7 || (oldValueSplit.length == 2 && oldValueSplit[1].length() > 2)) {
                            rowList.add(0, "工资数据格式不正确");
                            errorList.add(rowList);
                            return;
                        }
                        oldOption.setValue(oldValueStr);
                        oldSalary.add(oldOption);
                        BigDecimal oldValue = new BigDecimal(oldValueStr);
                        beforeSum = beforeSum.add(oldValue);
                    }else {
                        oldOption.setValue("0");
                        oldSalary.add(oldOption);
                        BigDecimal oldValue = new BigDecimal(0);
                        beforeSum = beforeSum.add(oldValue);
                    }
                    ChangeSalaryOptionVO newOption = new ChangeSalaryOptionVO();
                    newOption.setName(option.getName());
                    newOption.setCode(option.getCode());
                    if (rowList.size()>index+1){
                        String newValueStr = getValue(rowList.get(index+1));
                        String[] newValueSplit = newValueStr.split(",");
                        if (newValueSplit[0].length() > 7 || (newValueSplit.length == 2 && newValueSplit[1].length() > 2)) {
                            rowList.add(0, "工资数据格式不正确");
                            errorList.add(rowList);
                            return;
                        }
                        newOption.setValue(newValueStr);
                        newSalary.add(newOption);
                        afterSum = afterSum.add(new BigDecimal(newValueStr));
                    }else {
                        newOption.setValue("0");
                        newSalary.add(newOption);
                        afterSum = afterSum.add(new BigDecimal(0));
                    }
                }
                salary.setOldSalary(oldSalary);
                salary.setNewSalary(newSalary);
                setChangeSalaryRecordBO.setSalary(salary);
                setChangeSalaryRecordBO.setBeforeSum(beforeSum.toString());
                setChangeSalaryRecordBO.setAfterSum(afterSum.toString());
                setChangeSalaryRecordBO.setRemarks(Optional.ofNullable(rowList.get(salaryOptions.size() + 7)).map(String::valueOf).orElse("0"));
                archivesService.setChangeSalaryRecord(setChangeSalaryRecordBO);
                updateNum.incrementAndGet();
            } catch (CrmException e) {
                rowList.add(0, e.getMsg());
                errorList.add(rowList);
            } catch (Exception e) {
                log.error("导入异常",e);
                rowList.add(0, "未知异常");
                errorList.add(rowList);
            }
        });
    }
}
