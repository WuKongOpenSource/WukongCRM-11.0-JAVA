package com.kakarote.hrm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kakarote.core.common.DataAuthEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.feign.admin.entity.AdminMessageEnum;
import com.kakarote.core.feign.admin.service.AdminMessageService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.TransferUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.hrm.common.EmployeeHolder;
import com.kakarote.hrm.common.HrmCodeEnum;
import com.kakarote.hrm.constant.IsEnum;
import com.kakarote.hrm.constant.MenuIdConstant;
import com.kakarote.hrm.entity.BO.QueryInsurancePageListBO;
import com.kakarote.hrm.entity.BO.QueryInsuranceRecordListBO;
import com.kakarote.hrm.entity.PO.*;
import com.kakarote.hrm.entity.VO.QueryInsurancePageListVO;
import com.kakarote.hrm.entity.VO.QueryInsuranceRecordListVO;
import com.kakarote.hrm.mapper.HrmInsuranceMonthRecordMapper;
import com.kakarote.hrm.mapper.HrmInsuranceSchemeMapper;
import com.kakarote.hrm.service.*;
import com.kakarote.hrm.service.actionrecord.impl.insuranceActionRecordServiceImpl;
import com.kakarote.hrm.utils.EmployeeCacheUtil;
import com.kakarote.hrm.utils.EmployeeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 每月社保记录 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
@Service
public class HrmInsuranceMonthRecordServiceImpl extends BaseServiceImpl<HrmInsuranceMonthRecordMapper, HrmInsuranceMonthRecord> implements IHrmInsuranceMonthRecordService {

    @Autowired
    private HrmInsuranceMonthRecordMapper insuranceMonthRecordMapper;

    @Autowired
    private IHrmInsuranceMonthEmpRecordService monthEmpRecordService;

    @Autowired
    private IHrmInsuranceMonthEmpProjectRecordService monthEmpProjectRecordService;

    @Autowired
    private HrmInsuranceSchemeMapper insuranceSchemeMapper;

    @Autowired
    private IHrmInsuranceProjectService insuranceProjectService;

    @Autowired
    private IHrmSalaryConfigService salaryConfigService;

    @Resource
    private insuranceActionRecordServiceImpl insuranceActionRecordService;

    @Autowired
    private EmployeeUtil employeeUtil;

    @Autowired
    private AdminService adminService;




    @Override
    public Integer computeInsuranceData() {
        HrmSalaryConfig salaryConfig = salaryConfigService.getOne(Wrappers.emptyWrapper());
        if (salaryConfig == null){
            throw new CrmException(HrmCodeEnum.NO_INITIAL_CONFIGURATION);
        }
        String socialSecurityMonth = salaryConfig.getSocialSecurityStartMonth();
        DateTime dateTime = DateUtil.parse(socialSecurityMonth, "yyyy-MM");
        int month = dateTime.month()+1;
        int year = dateTime.year();
        //查询社保上月记录,如果有就往后推一个月,如果没有就去薪资配置计薪月
        Optional<HrmInsuranceMonthRecord> lastMonthRecord = lambdaQuery().orderByDesc(HrmInsuranceMonthRecord::getCreateTime).last("limit 1").oneOpt();
        if (lastMonthRecord.isPresent()){
            HrmInsuranceMonthRecord insuranceMonthRecord = lastMonthRecord.get();
            DateTime date = DateUtil.offsetMonth(DateUtil.parse(insuranceMonthRecord.getYear() + "-" + insuranceMonthRecord.getMonth(), "yy-MM"), 1);
            month = date.month()+1;
            year = date.year();
            List<Integer> empRecordIds = insuranceMonthRecordMapper.queryDeleteEmpRecordIds(insuranceMonthRecord.getIRecordId());
            if (CollUtil.isNotEmpty(empRecordIds)){
                monthEmpProjectRecordService.lambdaUpdate().in(HrmInsuranceMonthEmpProjectRecord::getIEmpRecordId,empRecordIds).remove();
                monthEmpRecordService.lambdaUpdate().in(HrmInsuranceMonthEmpRecord::getIEmpRecordId,empRecordIds).remove();
            }
            insuranceMonthRecord.setStatus(IsEnum.YES.getValue());
            updateById(insuranceMonthRecord);
        }
        List<Map<String,Integer>> employeeIds = insuranceMonthRecordMapper.queryInsuranceEmployee();
        HrmInsuranceMonthRecord hrmInsuranceMonthRecord = new HrmInsuranceMonthRecord();
        hrmInsuranceMonthRecord.setTitle(month+"月社保报表");
        hrmInsuranceMonthRecord.setYear(year);
        hrmInsuranceMonthRecord.setMonth(month);
        hrmInsuranceMonthRecord.setNum(employeeIds.size());
        save(hrmInsuranceMonthRecord);
        insuranceActionRecordService.computeInsuranceDataLog(hrmInsuranceMonthRecord);
        int finalYear = year;
        int finalMonth = month;
        employeeIds.forEach(employeeMap->{
            Integer employeeId = employeeMap.get("employeeId");
            Integer schemeId = employeeMap.get("schemeId");
            Map<String, Object> stringObjectMap = insuranceSchemeMapper.queryInsuranceSchemeCountById(schemeId);
            HrmInsuranceMonthEmpRecord insuranceMonthEmpRecord = new HrmInsuranceMonthEmpRecord();
            BeanUtil.fillBeanWithMap(stringObjectMap,insuranceMonthEmpRecord,true);
            insuranceMonthEmpRecord.setIRecordId(hrmInsuranceMonthRecord.getIRecordId());
            insuranceMonthEmpRecord.setEmployeeId(employeeId);
            insuranceMonthEmpRecord.setSchemeId(schemeId);
            insuranceMonthEmpRecord.setYear(finalYear);
            insuranceMonthEmpRecord.setMonth(finalMonth);
            monthEmpRecordService.save(insuranceMonthEmpRecord);
            //发送通知
            AdminMessage adminMessage = new AdminMessage();
            adminMessage.setCreateUser(UserUtil.getUserId());
            adminMessage.setCreateTime(DateUtil.formatDateTime(new Date()));
            adminMessage.setRecipientUser(EmployeeCacheUtil.getUserId(employeeId));
            adminMessage.setLabel(8);
            adminMessage.setType(AdminMessageEnum.HRM_EMPLOYEE_INSURANCE.getType());
            adminMessage.setTitle(finalYear+"年"+finalMonth+"月社保");
            ApplicationContextHolder.getBean(AdminMessageService.class).save(adminMessage);
            List<HrmInsuranceProject> insuranceProjectList = insuranceProjectService.lambdaQuery().eq(HrmInsuranceProject::getSchemeId, schemeId).list();
            List<HrmInsuranceMonthEmpProjectRecord> monthEmpProjectRecordList = TransferUtil.transferList(insuranceProjectList, HrmInsuranceMonthEmpProjectRecord.class);
            monthEmpProjectRecordList.forEach(monthEmpProjectRecord->{
                monthEmpProjectRecord.setIEmpRecordId(insuranceMonthEmpRecord.getIEmpRecordId());
            });
            monthEmpProjectRecordService.saveBatch(monthEmpProjectRecordList);
        });
        return year;
    }

    @Override
    public BasePage<QueryInsuranceRecordListVO> queryInsuranceRecordList(QueryInsuranceRecordListBO recordListBO) {
        Collection<Integer> employeeIds = employeeUtil.queryDataAuthEmpIdByMenuId(MenuIdConstant.INSURANCE_MENU_ID);
        Integer dataAuthType = adminService.queryDataType(UserUtil.getUserId(),MenuIdConstant.INSURANCE_MENU_ID).getData();
        boolean isAll = EmployeeHolder.isHrmAdmin() || dataAuthType.equals(DataAuthEnum.ALL.getValue());
        return insuranceMonthRecordMapper.queryInsuranceRecordList(recordListBO.parse(),recordListBO,employeeIds,isAll);
    }

    @Override
    public BasePage<QueryInsurancePageListVO> queryInsurancePageList(QueryInsurancePageListBO queryInsurancePageListBO) {
        Collection<Integer> employeeIds = employeeUtil.queryDataAuthEmpIdByMenuId(MenuIdConstant.INSURANCE_MENU_ID);
        return insuranceMonthRecordMapper.queryInsurancePageList(queryInsurancePageListBO.parse(),queryInsurancePageListBO,employeeIds);
    }

    @Override
    public QueryInsuranceRecordListVO queryInsuranceRecord(String iRecordId) {
        Collection<Integer> employeeIds = employeeUtil.queryDataAuthEmpIdByMenuId(MenuIdConstant.INSURANCE_MENU_ID);
        return insuranceMonthRecordMapper.queryInsuranceRecord(iRecordId,employeeIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteInsurance(Integer iRecordId) {
        Integer count = lambdaQuery().count();
        if (count == 1){
            throw new CrmException(HrmCodeEnum.INSURANCE_CANNOT_BE_DELETED);
        }
        List<Integer> iEmpRecordIds = monthEmpRecordService.lambdaQuery().select(HrmInsuranceMonthEmpRecord::getIEmpRecordId).eq(HrmInsuranceMonthEmpRecord::getIRecordId, iRecordId).list()
                .stream().map(HrmInsuranceMonthEmpRecord::getIEmpRecordId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(iEmpRecordIds)){
            monthEmpProjectRecordService.lambdaUpdate().in(HrmInsuranceMonthEmpProjectRecord::getIEmpRecordId,iEmpRecordIds).remove();
            monthEmpRecordService.lambdaUpdate().in(HrmInsuranceMonthEmpRecord::getIEmpRecordId,iEmpRecordIds).remove();
        }
        removeById(iRecordId);
        HrmInsuranceMonthRecord monthRecord = lambdaQuery().orderByDesc(HrmInsuranceMonthRecord::getCreateTime).last("limit 1").one();
        monthRecord.setStatus(0);
        updateById(monthRecord);
        insuranceActionRecordService.deleteInsurance(monthRecord);
    }
}
