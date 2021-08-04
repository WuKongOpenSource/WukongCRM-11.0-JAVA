package com.kakarote.hrm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.common.cache.AdminCacheKey;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.feign.admin.entity.AdminMessageEnum;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.redis.Redis;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.hrm.common.EmployeeHolder;
import com.kakarote.hrm.common.HrmCodeEnum;
import com.kakarote.hrm.constant.*;
import com.kakarote.hrm.entity.BO.*;
import com.kakarote.hrm.entity.DTO.ExcelTemplateOption;
import com.kakarote.hrm.entity.PO.*;
import com.kakarote.hrm.entity.VO.*;
import com.kakarote.hrm.mapper.HrmSalaryArchivesMapper;
import com.kakarote.hrm.service.*;
import com.kakarote.hrm.service.excel.ChangeSalaryExport;
import com.kakarote.hrm.service.excel.ExcelImport;
import com.kakarote.hrm.service.excel.FixSalaryExport;
import com.kakarote.hrm.utils.EmployeeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.kakarote.hrm.service.impl.HrmUploadExcelServiceImpl.UPLOAD_EXCEL_EXIST_TIME;

/**
 * <p>
 * 薪资档案表 服务实现类
 * </p>
 *
 * @author hmb
 * @since 2020-11-05
 */
@Service
public class HrmSalaryArchivesServiceImpl extends BaseServiceImpl<HrmSalaryArchivesMapper, HrmSalaryArchives> implements IHrmSalaryArchivesService {

    @Autowired
    private HrmSalaryArchivesMapper salaryArchivesMapper;

    @Autowired
    private IHrmSalaryChangeRecordService salaryChangeRecordService;

    @Autowired
    private IHrmEmployeeService employeeService;

    @Autowired
    private IHrmSalaryChangeTemplateService salaryChangeTemplateService;

    @Autowired
    private IHrmSalaryArchivesOptionService salaryArchivesOptionService;

    @Autowired
    private IHrmDeptService deptService;

    @Resource
    private ThreadPoolTaskExecutor hrmThreadPoolExecutor;

    @Autowired
    private Redis redis;

    @Autowired
    private AdminService adminService;

    @Autowired
    private EmployeeUtil employeeUtil;


    @Override
    public BasePage<QuerySalaryArchivesListVO> querySalaryArchivesList(QuerySalaryArchivesListBO querySalaryArchivesListBO) {
        Collection<Integer> employeeIds = employeeUtil.queryDataAuthEmpIdByMenuId(MenuIdConstant.SALARY_MENU_ID);
        BasePage<QuerySalaryArchivesListVO> page = salaryArchivesMapper.querySalaryArchivesList(querySalaryArchivesListBO.parse(), querySalaryArchivesListBO,employeeIds);
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setFixSalaryRecord(SetFixSalaryRecordBO setFixSalaryRecordBO) {
        Integer employeeId = setFixSalaryRecordBO.getEmployeeId();
        Optional<HrmSalaryChangeRecord> fixSalaryRecordOpt = salaryChangeRecordService.lambdaQuery().eq(HrmSalaryChangeRecord::getEmployeeId, employeeId).eq(HrmSalaryChangeRecord::getRecordType, SalaryChangeRecordTypeEnum.FIX_SALARY.getValue())
                .oneOpt();
        if (fixSalaryRecordOpt.isPresent()) {
            boolean isUpdate = salaryChangeRecordService.lambdaQuery().eq(HrmSalaryChangeRecord::getEmployeeId, employeeId).eq(HrmSalaryChangeRecord::getRecordType, SalaryChangeRecordTypeEnum.CHANGE_SALARY.getValue())
                    .ne(HrmSalaryChangeRecord::getStatus, SalaryChangeRecordStatusEnum.CANCEL.getValue()).count() > 0;
            if (isUpdate) {
                throw new CrmException(HrmCodeEnum.CHANGE_SALARY_NOT_FIX_SALARY);
            }
            //如果是更新,需要删除之前的数据重新生成
            lambdaUpdate().eq(HrmSalaryArchives::getEmployeeId, employeeId).remove();
            salaryArchivesOptionService.lambdaUpdate().eq(HrmSalaryArchivesOption::getEmployeeId, employeeId).remove();
        }
        HrmEmployee employee = employeeService.getById(employeeId);
        HrmSalaryArchives hrmSalaryArchives = new HrmSalaryArchives();
        hrmSalaryArchives.setEmployeeId(employeeId);
        hrmSalaryArchives.setChangeData(new Date());
        hrmSalaryArchives.setChangeReason(SalaryChangeReasonEnum.ENTRY_FIX_SALARY.getValue());
        hrmSalaryArchives.setRemarks(setFixSalaryRecordBO.getRemarks());
        hrmSalaryArchives.setChangeType(SalaryChangeTypeEnum.HAS_FIX_SALARY.getValue());
        //保存薪资项
        List<HrmSalaryArchivesOption> proArchivesOptionList = setFixSalaryRecordBO.getProSalary().stream().map(option -> {
            HrmSalaryArchivesOption hrmSalaryArchivesOption = new HrmSalaryArchivesOption();
            hrmSalaryArchivesOption.setEmployeeId(employeeId);
            hrmSalaryArchivesOption.setCode(option.getCode());
            hrmSalaryArchivesOption.setName(option.getName());
            hrmSalaryArchivesOption.setValue(option.getValue());
            hrmSalaryArchivesOption.setIsPro(1);
            return hrmSalaryArchivesOption;
        }).collect(Collectors.toList());
        salaryArchivesOptionService.saveBatch(proArchivesOptionList);
        List<HrmSalaryArchivesOption> archivesOptionList = setFixSalaryRecordBO.getSalary().stream().map(option -> {
            HrmSalaryArchivesOption hrmSalaryArchivesOption = new HrmSalaryArchivesOption();
            hrmSalaryArchivesOption.setEmployeeId(employeeId);
            hrmSalaryArchivesOption.setCode(option.getCode());
            hrmSalaryArchivesOption.setName(option.getName());
            hrmSalaryArchivesOption.setValue(option.getValue());
            hrmSalaryArchivesOption.setIsPro(0);
            return hrmSalaryArchivesOption;
        }).collect(Collectors.toList());
        salaryArchivesOptionService.saveBatch(archivesOptionList);
        save(hrmSalaryArchives);
        HrmSalaryChangeRecord salaryChangeRecord = new HrmSalaryChangeRecord();
        salaryChangeRecord.setId(fixSalaryRecordOpt.map(HrmSalaryChangeRecord::getId).orElse(null));
        salaryChangeRecord.setEmployeeId(employeeId);
        salaryChangeRecord.setEmployeeStatus(employee.getStatus());
        salaryChangeRecord.setRecordType(SalaryChangeRecordTypeEnum.FIX_SALARY.getValue());
        salaryChangeRecord.setChangeReason(hrmSalaryArchives.getChangeReason());
        salaryChangeRecord.setEnableDate(new Date());
        salaryChangeRecord.setProAfterSum(setFixSalaryRecordBO.getProSum());
        salaryChangeRecord.setProSalary(JSON.toJSONString(setFixSalaryRecordBO.getProSalary()));
        salaryChangeRecord.setAfterSum(setFixSalaryRecordBO.getSum());
        salaryChangeRecord.setSalary(JSON.toJSONString(setFixSalaryRecordBO.getSalary()));
        salaryChangeRecord.setStatus(SalaryChangeRecordStatusEnum.HAS_TAKE_EFFECT.getValue());
        salaryChangeRecord.setRemarks(setFixSalaryRecordBO.getRemarks());
        if (employee.getStatus().equals(EmployeeStatusEnum.TRY_OUT.getValue())){
            salaryChangeRecord.setAfterTotal(setFixSalaryRecordBO.getProSum());
        }else {
            salaryChangeRecord.setAfterTotal(setFixSalaryRecordBO.getSum());
        }
        salaryChangeRecordService.saveOrUpdate(salaryChangeRecord);
    }

    @Override
    public QuerySalaryArchivesByIdVO querySalaryArchivesById(Integer employeeId) {
        QuerySalaryArchivesByIdVO querySalaryArchivesByIdVO = new QuerySalaryArchivesByIdVO();
        HrmSalaryArchives archives = lambdaQuery().eq(HrmSalaryArchives::getEmployeeId, employeeId).one();
        if (archives == null) {
            return querySalaryArchivesByIdVO;
        }
        querySalaryArchivesByIdVO.setEmployeeId(employeeId);
        List<ChangeSalaryOptionVO> changeSalaryOptions = salaryChangeTemplateService.queryChangeSalaryOption();
        HrmEmployee employee = employeeService.getById(employeeId);
        Map<Integer, String> map;
        if (employee.getStatus().equals(EmployeeStatusEnum.TRY_OUT.getValue())) {
            map = salaryArchivesOptionService.lambdaQuery().eq(HrmSalaryArchivesOption::getEmployeeId, employeeId).eq(HrmSalaryArchivesOption::getIsPro, 1).list()
                    .stream().collect(Collectors.toMap(HrmSalaryArchivesOption::getCode, HrmSalaryArchivesOption::getValue));
        } else {
            map = salaryArchivesOptionService.lambdaQuery().eq(HrmSalaryArchivesOption::getEmployeeId, employeeId).eq(HrmSalaryArchivesOption::getIsPro, 0).list()
                    .stream().collect(Collectors.toMap(HrmSalaryArchivesOption::getCode, HrmSalaryArchivesOption::getValue));
        }
        BigDecimal total = new BigDecimal(0);
        for (ChangeSalaryOptionVO option : changeSalaryOptions) {
            String value = Objects.isNull(map.get(option.getCode())) ? "0" : map.get(option.getCode());
            option.setValue(value);
            total = total.add(new BigDecimal(value));
        }
        querySalaryArchivesByIdVO.setTotal(total.toString());
        querySalaryArchivesByIdVO.setSalaryOptions(changeSalaryOptions);
        return querySalaryArchivesByIdVO;
    }

    @Override
    public List<QueryChangeRecordListVO> queryChangeRecordList(Integer employeeId) {
        return salaryArchivesMapper.queryChangeRecordList(employeeId);
    }

    @Override
    public FixSalaryRecordDetailVO queryFixSalaryRecordById(Integer id) {
        HrmSalaryChangeRecord salaryChangeRecord = salaryChangeRecordService.getById(id);
        HrmEmployee employee = employeeService.getById(salaryChangeRecord.getEmployeeId());
        FixSalaryRecordDetailVO fixSalaryRecordDetailVO = BeanUtil.copyProperties(employee, FixSalaryRecordDetailVO.class);
        HrmDept dept = deptService.getById(employee.getDeptId());
        fixSalaryRecordDetailVO.setDeptName(Optional.ofNullable(dept).map(HrmDept::getName).orElse(""));
        fixSalaryRecordDetailVO.setEmployeeStatus(employee.getStatus());
        fixSalaryRecordDetailVO.setRemarks(salaryChangeRecord.getRemarks());
        fixSalaryRecordDetailVO.setProSalary(JSON.parseArray(salaryChangeRecord.getProSalary(), ChangeSalaryOptionVO.class));
        fixSalaryRecordDetailVO.setSalary(JSON.parseArray(salaryChangeRecord.getSalary(), ChangeSalaryOptionVO.class));
        fixSalaryRecordDetailVO.setStatus(salaryChangeRecord.getStatus());
        fixSalaryRecordDetailVO.setProSum(salaryChangeRecord.getProAfterSum());
        fixSalaryRecordDetailVO.setSum(salaryChangeRecord.getAfterSum());
        fixSalaryRecordDetailVO.setChangeReason(salaryChangeRecord.getChangeReason());
        //1、没有未生效、已生效的调薪时，可编辑定薪；
        //2、存在未生效、已生效的调薪时，隐藏定薪的编辑入口，除非调薪取消或删除；
        if (!EmployeeHolder.isHrmAdmin()){
            fixSalaryRecordDetailVO.setIsUpdate(false);
        }else {
            boolean isUpdate = salaryChangeRecordService.lambdaQuery().eq(HrmSalaryChangeRecord::getEmployeeId, employee.getEmployeeId()).eq(HrmSalaryChangeRecord::getRecordType, SalaryChangeRecordTypeEnum.CHANGE_SALARY.getValue())
                    .ne(HrmSalaryChangeRecord::getStatus, SalaryChangeRecordStatusEnum.CANCEL.getValue()).count() > 0;
            fixSalaryRecordDetailVO.setIsUpdate(!isUpdate);
        }
        return fixSalaryRecordDetailVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setChangeSalaryRecord(SetChangeSalaryRecordBO setChangeSalaryRecordBO) {
        //员工当前存在正在进行中的调薪任务，无法再次新增调薪任务
        boolean isChange = salaryChangeRecordService.lambdaQuery().eq(HrmSalaryChangeRecord::getEmployeeId, setChangeSalaryRecordBO.getEmployeeId()).eq(HrmSalaryChangeRecord::getRecordType, SalaryChangeRecordTypeEnum.CHANGE_SALARY.getValue())
                .eq(HrmSalaryChangeRecord::getStatus, SalaryChangeRecordStatusEnum.NOT_TAKE_EFFECT.getValue()).ne(setChangeSalaryRecordBO.getId() != null, HrmSalaryChangeRecord::getId, setChangeSalaryRecordBO.getId())
                .count() > 0;
        if (isChange) {
            throw new CrmException(HrmCodeEnum.EXIST_CHANGE_RECORD);
        }
        HrmEmployee employee = employeeService.getById(setChangeSalaryRecordBO.getEmployeeId());
        HrmSalaryChangeRecord salaryChangeRecord = BeanUtil.copyProperties(setChangeSalaryRecordBO, HrmSalaryChangeRecord.class);
        salaryChangeRecord.setProSalary(JSON.toJSONString(setChangeSalaryRecordBO.getProSalary()));
        salaryChangeRecord.setSalary(JSON.toJSONString(setChangeSalaryRecordBO.getSalary()));
        salaryChangeRecord.setStatus(SalaryChangeRecordStatusEnum.NOT_TAKE_EFFECT.getValue());
        salaryChangeRecord.setEmployeeStatus(employee.getStatus());
        salaryChangeRecord.setRecordType(SalaryChangeRecordTypeEnum.CHANGE_SALARY.getValue());
        salaryChangeRecord.setChangeReason(setChangeSalaryRecordBO.getChangeReason());
        BigDecimal beforeTotal = new BigDecimal(0);
        BigDecimal afterTotal = new BigDecimal(0);
        List<HrmSalaryArchivesOption> salaryArchivesOptions;
        List<ChangeSalaryOptionVO> newSalaryOptions;
        Map<Integer, String> codeValueMap;
        if (employee.getStatus().equals(EmployeeStatusEnum.TRY_OUT.getValue())){
            salaryArchivesOptions = salaryArchivesOptionService.lambdaQuery().eq(HrmSalaryArchivesOption::getEmployeeId,employee.getEmployeeId())
                    .eq(HrmSalaryArchivesOption::getIsPro,1).list();
            newSalaryOptions = setChangeSalaryRecordBO.getProSalary().getNewSalary();
        }else {
            salaryArchivesOptions = salaryArchivesOptionService.lambdaQuery().eq(HrmSalaryArchivesOption::getEmployeeId,employee.getEmployeeId())
                    .eq(HrmSalaryArchivesOption::getIsPro,0).list();
            newSalaryOptions = setChangeSalaryRecordBO.getSalary().getNewSalary();
        }
        List<Integer> newCodeList = newSalaryOptions.stream().map(ChangeSalaryOptionVO::getCode).collect(Collectors.toList());
        for (HrmSalaryArchivesOption option : salaryArchivesOptions) {
            beforeTotal = beforeTotal.add(new BigDecimal(option.getValue()));
        }
        List<HrmSalaryArchivesOption> oldOptions = salaryArchivesOptions.stream().filter(option -> !newCodeList.contains(option.getCode())).collect(Collectors.toList());
        for (HrmSalaryArchivesOption option : oldOptions) {
            afterTotal = afterTotal.add(new BigDecimal(option.getValue()));
        }
        for (ChangeSalaryOptionVO newOption : newSalaryOptions) {
            afterTotal = afterTotal.add(new BigDecimal(newOption.getValue()));
        }
        salaryChangeRecord.setAfterTotal(afterTotal.toString());
        salaryChangeRecord.setBeforeTotal(beforeTotal.toString());
        salaryChangeRecordService.saveOrUpdate(salaryChangeRecord);
    }

    @Override
    public ChangeSalaryRecordDetailVO queryChangeSalaryRecordById(Integer id) {
        HrmSalaryChangeRecord salaryChangeRecord = salaryChangeRecordService.getById(id);
        HrmEmployee employee = employeeService.getById(salaryChangeRecord.getEmployeeId());
        ChangeSalaryRecordDetailVO changeSalaryRecordDetailVO = BeanUtil.copyProperties(employee, ChangeSalaryRecordDetailVO.class);
        HrmDept dept = deptService.getById(employee.getDeptId());
        changeSalaryRecordDetailVO.setDeptName(Optional.ofNullable(dept).map(HrmDept::getName).orElse(""));
        changeSalaryRecordDetailVO.setEmployeeStatus(employee.getStatus());

        changeSalaryRecordDetailVO.setProSalary(JSON.parseObject(salaryChangeRecord.getProSalary(), ChangeSalaryRecordVO.class));
        changeSalaryRecordDetailVO.setProBeforeSum(salaryChangeRecord.getProBeforeSum());
        changeSalaryRecordDetailVO.setProAfterSum(salaryChangeRecord.getProAfterSum());
        changeSalaryRecordDetailVO.setSalary(JSON.parseObject(salaryChangeRecord.getSalary(), ChangeSalaryRecordVO.class));
        changeSalaryRecordDetailVO.setBeforeSum(salaryChangeRecord.getBeforeSum());
        changeSalaryRecordDetailVO.setAfterSum(salaryChangeRecord.getAfterSum());
        changeSalaryRecordDetailVO.setChangeReason(salaryChangeRecord.getChangeReason());
        changeSalaryRecordDetailVO.setRemarks(salaryChangeRecord.getRemarks());
        changeSalaryRecordDetailVO.setEnableDate(salaryChangeRecord.getEnableDate());
        changeSalaryRecordDetailVO.setStatus(salaryChangeRecord.getStatus());
        /**
         * 控制 编辑，删除，取消按钮
         * 已生效：三个都隐藏
         * 未生效：可编辑，删除，取消
         * 已取消：可编辑，删除
         */
        if (!EmployeeHolder.isHrmAdmin()){
            changeSalaryRecordDetailVO.setIsCancel(false);
            changeSalaryRecordDetailVO.setIsUpdate(false);
            changeSalaryRecordDetailVO.setIsDelete(false);
        }else {
            if (salaryChangeRecord.getStatus().equals(SalaryChangeRecordStatusEnum.HAS_TAKE_EFFECT.getValue())) {
                changeSalaryRecordDetailVO.setIsCancel(false);
                changeSalaryRecordDetailVO.setIsUpdate(false);
                changeSalaryRecordDetailVO.setIsDelete(false);
            } else if (salaryChangeRecord.getStatus().equals(SalaryChangeRecordStatusEnum.NOT_TAKE_EFFECT.getValue())) {
                changeSalaryRecordDetailVO.setIsCancel(true);
                changeSalaryRecordDetailVO.setIsUpdate(true);
                changeSalaryRecordDetailVO.setIsDelete(true);
            } else {
                changeSalaryRecordDetailVO.setIsCancel(false);
                changeSalaryRecordDetailVO.setIsUpdate(true);
                changeSalaryRecordDetailVO.setIsDelete(true);
            }
        }
        return changeSalaryRecordDetailVO;
    }

    @Override
    public void cancelChangeSalary(Integer id) {
        HrmSalaryChangeRecord salaryChangeRecord = salaryChangeRecordService.getById(id);
        if (!salaryChangeRecord.getStatus().equals(SalaryChangeRecordStatusEnum.NOT_TAKE_EFFECT.getValue())) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
        }
        salaryChangeRecordService.lambdaUpdate().set(HrmSalaryChangeRecord::getStatus, SalaryChangeRecordStatusEnum.CANCEL.getValue())
                .eq(HrmSalaryChangeRecord::getId, id)
                .update();
    }

    @Override
    public void deleteChangeSalary(Integer id) {
        HrmSalaryChangeRecord salaryChangeRecord = salaryChangeRecordService.getById(id);
        if (salaryChangeRecord.getStatus().equals(SalaryChangeRecordStatusEnum.HAS_TAKE_EFFECT.getValue())) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
        }
        salaryChangeRecordService.removeById(id);
    }

    @Override
    public QueryChangeOptionValueVO queryChangeOptionValue(QueryChangeOptionValueBO changeOptionValueBO) {
        HrmSalaryChangeTemplate changeTemplate = salaryChangeTemplateService.getById(changeOptionValueBO.getTemplateId());
        List<ChangeSalaryOptionVO> changeSalaryOptions = JSON.parseArray(changeTemplate.getValue(), ChangeSalaryOptionVO.class);
        List<HrmSalaryArchivesOption> archivesOptions = salaryArchivesOptionService.lambdaQuery().eq(HrmSalaryArchivesOption::getEmployeeId, changeOptionValueBO.getEmployeeId()).list();
        Map<Integer, Map<Integer, String>> isProMap = archivesOptions.stream().collect(Collectors.groupingBy(HrmSalaryArchivesOption::getIsPro, Collectors.toMap(HrmSalaryArchivesOption::getCode, HrmSalaryArchivesOption::getValue)));
        Map<Integer, String> proCodeValueMap = isProMap.get(1);
        Map<Integer, String> codeValueMap = isProMap.get(0);
        List<ChangeSalaryOptionVO> proSalary = changeSalaryOptions.stream().map(option -> {
            String value = Objects.isNull(proCodeValueMap.get(option.getCode())) ? "" : proCodeValueMap.get(option.getCode());
            return new ChangeSalaryOptionVO(option.getName(),option.getCode(),value);
        }).collect(Collectors.toList());
        List<ChangeSalaryOptionVO> salary = changeSalaryOptions.stream().map(option -> {
            String value = Objects.isNull(codeValueMap.get(option.getCode())) ? "" : codeValueMap.get(option.getCode());
            return new ChangeSalaryOptionVO(option.getName(),option.getCode(),value);
        }).collect(Collectors.toList());
        QueryChangeOptionValueVO data = new QueryChangeOptionValueVO();
        data.setProSalary(proSalary);
        data.setSalary(salary);
        return data;
    }

    @Override
    public List<ChangeSalaryOptionVO> queryBatchChangeOption() {
        return baseMapper.queryBatchChangeOption();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String,Integer> batchChangeSalaryRecord(BatchChangeSalaryRecordBO batchChangeSalaryRecordBO) {
        List<Integer> employeeIds = new ArrayList<>(batchChangeSalaryRecordBO.getEmployeeIds());
        if (CollUtil.isNotEmpty(batchChangeSalaryRecordBO.getDeptIds())){
            employeeIds.addAll(employeeService.lambdaQuery().select(HrmEmployee::getEmployeeId).in(HrmEmployee::getDeptId, batchChangeSalaryRecordBO.getDeptIds()).list()
                    .stream().map(HrmEmployee::getEmployeeId).collect(Collectors.toList()));
        }
        int errorNum = 0;
        List<ChangeSalaryOptionVO> changeSalaryOptions = batchChangeSalaryRecordBO.getSalaryOptions();
        List<Integer> changeCodes = changeSalaryOptions.stream().map(ChangeSalaryOptionVO::getCode).collect(Collectors.toList());
        for (Integer employeeId : employeeIds) {
            Integer count = lambdaQuery().eq(HrmSalaryArchives::getEmployeeId, employeeId).count();
            if (count == 0) {
                errorNum++;
                continue;
            }
            HrmEmployee employee = employeeService.getById(employeeId);
            SetChangeSalaryRecordBO setChangeSalaryRecordBO = new SetChangeSalaryRecordBO();
            if (employee.getStatus().equals(EmployeeStatusEnum.TRY_OUT.getValue())){
                Map<Integer, String> codeValueMap = salaryArchivesOptionService.lambdaQuery().eq(HrmSalaryArchivesOption::getEmployeeId, employeeId)
                        .eq(HrmSalaryArchivesOption::getIsPro, 1).in(HrmSalaryArchivesOption::getCode, changeCodes).list()
                        .stream().collect(Collectors.toMap(HrmSalaryArchivesOption::getCode, HrmSalaryArchivesOption::getValue));
                ChangeSalaryRecordVO proSalary = new ChangeSalaryRecordVO();
                BigDecimal proBeforeSum = new BigDecimal(0);
                BigDecimal proAfterSum = new BigDecimal(0);
                List<ChangeSalaryOptionVO> proOldSalary = new ArrayList<>();
                List<ChangeSalaryOptionVO> proNewSalary = new ArrayList<>();
                for (ChangeSalaryOptionVO option : changeSalaryOptions) {
                    ChangeSalaryOptionVO oldOption= new ChangeSalaryOptionVO();
                    oldOption.setName(option.getName());
                    oldOption.setCode(option.getCode());
                    String oldValueStr = Optional.ofNullable(codeValueMap.get(option.getCode())).orElse("0");
                    oldOption.setValue(oldValueStr);
                    proOldSalary.add(oldOption);
                    BigDecimal oldValue = new BigDecimal(oldValueStr);
                    proBeforeSum = proBeforeSum.add(oldValue);
                    ChangeSalaryOptionVO newOption= new ChangeSalaryOptionVO();
                    newOption.setName(option.getName());
                    newOption.setCode(option.getCode());
                    BigDecimal newValue;
                    if (batchChangeSalaryRecordBO.getType() == 1){
                        newValue = oldValue.add(oldValue.multiply(new BigDecimal(option.getValue()).divide(new BigDecimal(100),2,BigDecimal.ROUND_UP)));
                    }else {
                        newValue = oldValue.add(new BigDecimal(option.getValue()));
                    }
                    newOption.setValue(newValue.toString());
                    proNewSalary.add(newOption);
                    proAfterSum = proAfterSum.add(newValue);
                }
                proSalary.setOldSalary(proOldSalary);
                proSalary.setNewSalary(proNewSalary);
                setChangeSalaryRecordBO.setProSalary(proSalary);
                setChangeSalaryRecordBO.setProBeforeSum(proBeforeSum.toString());
                setChangeSalaryRecordBO.setProAfterSum(proAfterSum.toString());
            }
            Map<Integer, String> codeValueMap = salaryArchivesOptionService.lambdaQuery().eq(HrmSalaryArchivesOption::getEmployeeId, employeeId)
                    .eq(HrmSalaryArchivesOption::getIsPro, 0).in(HrmSalaryArchivesOption::getCode, changeCodes).list()
                    .stream().collect(Collectors.toMap(HrmSalaryArchivesOption::getCode, HrmSalaryArchivesOption::getValue));
            ChangeSalaryRecordVO salary = new ChangeSalaryRecordVO();
            BigDecimal beforeSum = new BigDecimal(0);
            BigDecimal afterSum = new BigDecimal(0);
            List<ChangeSalaryOptionVO> oldSalary = new ArrayList<>();
            List<ChangeSalaryOptionVO> newSalary = new ArrayList<>();
            for (ChangeSalaryOptionVO option : changeSalaryOptions) {
                ChangeSalaryOptionVO oldOption= new ChangeSalaryOptionVO();
                oldOption.setName(option.getName());
                oldOption.setCode(option.getCode());
                String oldValueStr = Optional.ofNullable(codeValueMap.get(option.getCode())).orElse("0");
                oldOption.setValue(oldValueStr);
                oldSalary.add(oldOption);
                BigDecimal oldValue = new BigDecimal(oldValueStr);
                beforeSum = beforeSum.add(oldValue);
                ChangeSalaryOptionVO newOption= new ChangeSalaryOptionVO();
                newOption.setName(option.getName());
                newOption.setCode(option.getCode());
                BigDecimal newValue;
                if (batchChangeSalaryRecordBO.getType() == 1){
                    newValue = oldValue.add(oldValue.multiply(new BigDecimal(option.getValue()).divide(new BigDecimal(100),2,BigDecimal.ROUND_UP)));
                }else {
                    newValue = oldValue.add(new BigDecimal(option.getValue()));
                }
                newOption.setValue(newValue.toString());
                newSalary.add(newOption);
                afterSum = afterSum.add(newValue);
            }
            salary.setOldSalary(oldSalary);
            salary.setNewSalary(newSalary);
            setChangeSalaryRecordBO.setEmployeeId(employeeId);
            setChangeSalaryRecordBO.setSalary(salary);
            setChangeSalaryRecordBO.setBeforeSum(beforeSum.toString());
            setChangeSalaryRecordBO.setAfterSum(afterSum.toString());
            setChangeSalaryRecordBO.setRemarks(batchChangeSalaryRecordBO.getRemarks());
            setChangeSalaryRecordBO.setChangeReason(batchChangeSalaryRecordBO.getChangeReason());
            setChangeSalaryRecordBO.setEnableDate(batchChangeSalaryRecordBO.getEnableDate());
            try {
                setChangeSalaryRecord(setChangeSalaryRecordBO);
            }catch (CrmException e){
                errorNum ++;
            }
        }
        Map<String,Integer> result = new HashMap<>(2);
        result.put("errorNum",errorNum);
        result.put("successNum",employeeIds.size()-errorNum);
        return result;
    }

    @Override
    public List<ExcelTemplateOption> queryFixSalaryExcelExportOption() {
        List<ExcelTemplateOption> list = getBaseMapper().queryFixSalaryExcelExportOption();
        Map<Integer, List<ExcelTemplateOption>> collect = list.stream().filter(option -> !option.getParentCode().equals(0)).collect(Collectors.groupingBy(ExcelTemplateOption::getParentCode));
        return list.stream().filter(option -> option.getParentCode().equals(0)).peek(option -> option.setOptionList(collect.get(option.getCode()))).collect(Collectors.toList());
    }

    @Override
    public List<ExcelTemplateOption> queryChangeSalaryExcelExportOption() {
        return getBaseMapper().queryChangeSalaryExcelExportOption();
    }

    @Override
    public Long exportFixSalaryRecord(MultipartFile multipartFile) {
        UploadExcelBO uploadExcelBO = new UploadExcelBO();
        AdminMessage adminMessage = new AdminMessage();
        adminMessage.setCreateUser(UserUtil.getUserId());
        adminMessage.setCreateTime(DateUtil.formatDateTime(new Date()));
        adminMessage.setRecipientUser(UserUtil.getUserId());
        adminMessage.setLabel(8);
        adminMessage.setType(AdminMessageEnum.HRM_FIX_SALARY_IMPORT.getType());
        Long messageId = adminService.saveOrUpdateMessage(adminMessage).getData();
        uploadExcelBO.setMessageId(messageId);
        uploadExcelBO.setUserInfo(UserUtil.getUser());
        redis.setex(AdminCacheKey.UPLOAD_EXCEL_MESSAGE_PREFIX + messageId.toString(), UPLOAD_EXCEL_EXIST_TIME, 0);
        ExcelImport uploadService = new FixSalaryExport(uploadExcelBO,multipartFile);
        hrmThreadPoolExecutor.execute(uploadService);
        return messageId;
    }

    @Override
    public Long exportChangeSalaryRecord(MultipartFile multipartFile) {
        UploadExcelBO uploadExcelBO = new UploadExcelBO();
        AdminMessage adminMessage = new AdminMessage();
        adminMessage.setCreateUser(UserUtil.getUserId());
        adminMessage.setCreateTime(DateUtil.formatDateTime(new Date()));
        adminMessage.setRecipientUser(UserUtil.getUserId());
        adminMessage.setLabel(8);
        adminMessage.setType(AdminMessageEnum.HRM_CHANGE_SALARY_IMPORT.getType());
        Long messageId = adminService.saveOrUpdateMessage(adminMessage).getData();
        uploadExcelBO.setMessageId(messageId);
        uploadExcelBO.setUserInfo(UserUtil.getUser());
        redis.setex(AdminCacheKey.UPLOAD_EXCEL_MESSAGE_PREFIX + messageId.toString(), UPLOAD_EXCEL_EXIST_TIME, 0);
        ExcelImport uploadService = new ChangeSalaryExport(uploadExcelBO,multipartFile);
        hrmThreadPoolExecutor.execute(uploadService);
        return messageId;
    }

    @Override
    public List<HrmSalaryArchivesOption> querySalaryArchivesOption(Integer employeeId, int year, int month) {
        HrmEmployee employee = employeeService.getById(employeeId);
        HrmSalaryArchives salaryArchives = lambdaQuery().eq(HrmSalaryArchives::getEmployeeId, employeeId).one();
        if (Objects.isNull(salaryArchives)){
            return new ArrayList<>();
        }
        Optional<HrmSalaryChangeRecord> salaryChangeRecordOpt = salaryChangeRecordService.lambdaQuery().eq(HrmSalaryChangeRecord::getEmployeeId, employeeId)
                .eq(HrmSalaryChangeRecord::getRecordType, SalaryChangeRecordTypeEnum.CHANGE_SALARY.getValue())
                .eq(HrmSalaryChangeRecord::getStatus, SalaryChangeRecordStatusEnum.NOT_TAKE_EFFECT.getValue())
                .apply("year(enable_date) = {0} and month(enable_date) = {1}", year, month).oneOpt();
        if (salaryChangeRecordOpt.isPresent()){
            HrmSalaryChangeRecord salaryChangeRecord = salaryChangeRecordOpt.get();
            if (StrUtil.isNotEmpty(salaryChangeRecord.getProSalary()) && !"null".equals(salaryChangeRecord.getProSalary())){
                ChangeSalaryRecordVO proSalary = JSON.parseObject(salaryChangeRecord.getProSalary(),ChangeSalaryRecordVO.class);
                List<ChangeSalaryOptionVO> newSalary = proSalary.getNewSalary();
                List<Integer> removeCode = newSalary.stream().map(ChangeSalaryOptionVO::getCode).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(removeCode)){
                    salaryArchivesOptionService.lambdaUpdate().eq(HrmSalaryArchivesOption::getIsPro,1).eq(HrmSalaryArchivesOption::getEmployeeId,employeeId)
                            .in(HrmSalaryArchivesOption::getCode,removeCode)
                            .remove();
                }
                List<HrmSalaryArchivesOption> archivesOptionList = newSalary.stream().map(option -> {
                    HrmSalaryArchivesOption archivesOption = new HrmSalaryArchivesOption();
                    archivesOption.setEmployeeId(employeeId);
                    archivesOption.setIsPro(1);
                    archivesOption.setCode(option.getCode());
                    archivesOption.setName(option.getName());
                    archivesOption.setValue(option.getValue());
                    return archivesOption;
                }).collect(Collectors.toList());
                salaryArchivesOptionService.saveBatch(archivesOptionList);
            }
            if (StrUtil.isNotEmpty(salaryChangeRecord.getSalary())){
                ChangeSalaryRecordVO salary = JSON.parseObject(salaryChangeRecord.getSalary(),ChangeSalaryRecordVO.class);
                List<ChangeSalaryOptionVO> newSalary = salary.getNewSalary();
                List<Integer> removeCode = newSalary.stream().map(ChangeSalaryOptionVO::getCode).collect(Collectors.toList());
                salaryArchivesOptionService.lambdaUpdate().eq(HrmSalaryArchivesOption::getIsPro,0).eq(HrmSalaryArchivesOption::getEmployeeId,employeeId)
                        .in(HrmSalaryArchivesOption::getCode,removeCode)
                        .remove();
                List<HrmSalaryArchivesOption> archivesOptionList = newSalary.stream().map(option -> {
                    HrmSalaryArchivesOption archivesOption = new HrmSalaryArchivesOption();
                    archivesOption.setEmployeeId(employeeId);
                    archivesOption.setIsPro(0);
                    archivesOption.setCode(option.getCode());
                    archivesOption.setName(option.getName());
                    archivesOption.setValue(option.getValue());
                    return archivesOption;
                }).collect(Collectors.toList());
                salaryArchivesOptionService.saveBatch(archivesOptionList);
                salaryChangeRecord.setStatus(SalaryChangeRecordStatusEnum.HAS_TAKE_EFFECT.getValue());
                salaryChangeRecordService.updateById(salaryChangeRecord);
            }
            salaryArchives.setChangeType(2);
            salaryArchives.setChangeReason(salaryChangeRecord.getChangeReason());
            salaryArchives.setRemarks(salaryChangeRecord.getRemarks());
            salaryArchives.setChangeData(salaryChangeRecord.getEnableDate());
            updateById(salaryArchives);
        }
        List<HrmSalaryArchivesOption> salaryArchivesOptions;
        if (employee.getStatus().equals(EmployeeStatusEnum.TRY_OUT.getValue())){
            salaryArchivesOptions = salaryArchivesOptionService.lambdaQuery().eq(HrmSalaryArchivesOption::getEmployeeId,employeeId).eq(HrmSalaryArchivesOption::getIsPro,1).list();
        }else {
            salaryArchivesOptions = salaryArchivesOptionService.lambdaQuery().eq(HrmSalaryArchivesOption::getEmployeeId,employeeId).eq(HrmSalaryArchivesOption::getIsPro,0).list();
        }
        return salaryArchivesOptions;
    }
}
