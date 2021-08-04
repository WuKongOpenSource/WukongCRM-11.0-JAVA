package com.kakarote.hrm.service.impl;


import cn.hutool.core.date.DateUtil;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.feign.admin.entity.AdminMessageEnum;
import com.kakarote.core.feign.admin.service.AdminMessageService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.hrm.entity.BO.*;
import com.kakarote.hrm.entity.PO.*;
import com.kakarote.hrm.entity.VO.QuerySendDetailListVO;
import com.kakarote.hrm.entity.VO.QuerySendRecordListVO;
import com.kakarote.hrm.entity.VO.SlipEmployeeVO;
import com.kakarote.hrm.mapper.HrmSalarySlipRecordMapper;
import com.kakarote.hrm.service.*;
import com.kakarote.hrm.utils.EmployeeCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 发工资条记录 服务实现类
 * </p>
 *
 * @author hmb
 * @since 2020-11-03
 */
@Service
public class HrmSalarySlipRecordServiceImpl extends BaseServiceImpl<HrmSalarySlipRecordMapper, HrmSalarySlipRecord> implements IHrmSalarySlipRecordService {


    @Autowired
    private IHrmSalaryMonthRecordService salaryMonthRecordService;

    @Autowired
    private IHrmSalaryMonthEmpRecordService salaryMonthEmpRecordService;

    @Autowired
    private IHrmSalaryMonthOptionValueService salaryMonthOptionValueService;

    @Autowired
    private HrmSalarySlipRecordMapper slipRecordMapper;

    @Autowired
    private IHrmSalarySlipOptionService salarySlipOptionService;

    @Resource
    private ThreadPoolTaskExecutor hrmThreadPoolExecutor;

    @Autowired
    private AdminMessageService adminMessageService;

    @Autowired
    private IHrmSalarySlipService salarySlipService;

    @Override
    public BasePage<SlipEmployeeVO> querySlipEmployeePageList(QuerySlipEmployeePageListBO slipEmployeePageListBO) {
        HrmSalaryMonthRecord salaryMonthRecord = salaryMonthRecordService.queryLastSalaryMonthRecord();
        BasePage<SlipEmployeeVO> page = slipRecordMapper.querySlipEmployeePageList(slipEmployeePageListBO.parse(),salaryMonthRecord.getSRecordId(),slipEmployeePageListBO);
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendSalarySlip(SendSalarySlipBO sendSalarySlipBO) {
        HrmSalaryMonthRecord salaryMonthRecord = salaryMonthRecordService.queryLastSalaryMonthRecord();
        List<Integer> sEmpRecordIds;
        if (sendSalarySlipBO.getIsAll()){
            sEmpRecordIds = slipRecordMapper.querySlipEmployeeIds(salaryMonthRecord.getSRecordId(),sendSalarySlipBO);
        }else {
            sEmpRecordIds = sendSalarySlipBO.getSEmpRecordIds();
        }
        List<HrmSalarySlipTemplateOption> slipTemplateOption = sendSalarySlipBO.getSlipTemplateOption();
        Set<Integer> optionCodeList = new TreeSet<>();
        optionCodeList.add(240101);
        slipTemplateOption.forEach(option->
                optionCodeList.addAll(option.getOptionList().stream().map(HrmSalarySlipTemplateOption::getCode).collect(Collectors.toList())));
        List<HrmSalaryMonthOptionValue> valueList = salaryMonthOptionValueService.lambdaQuery().select(HrmSalaryMonthOptionValue::getSEmpRecordId, HrmSalaryMonthOptionValue::getCode, HrmSalaryMonthOptionValue::getValue)
                .in(HrmSalaryMonthOptionValue::getCode, optionCodeList).in(HrmSalaryMonthOptionValue::getSEmpRecordId, sEmpRecordIds).list();
        Map<Integer, Map<Integer, String>> empValueMap = valueList.stream().collect(Collectors.groupingBy(HrmSalaryMonthOptionValue::getSEmpRecordId, Collectors.toMap(HrmSalaryMonthOptionValue::getCode, HrmSalaryMonthOptionValue::getValue)));
        HrmSalarySlipRecord salarySlipRecord = new HrmSalarySlipRecord();
        salarySlipRecord.setSRecordId(salaryMonthRecord.getSRecordId());
        salarySlipRecord.setSalaryNum(salaryMonthRecord.getNum());
        salarySlipRecord.setPayNum(sEmpRecordIds.size());
        salarySlipRecord.setYear(salaryMonthRecord.getYear());
        salarySlipRecord.setMonth(salaryMonthRecord.getMonth());
        save(salarySlipRecord);
        Long userId = UserUtil.getUserId();
        for (Integer sEmpRecordId : sEmpRecordIds) {
//            hrmThreadPoolExecutor.execute(()->{
                Map<Integer, String> codeValueMap = empValueMap.get(sEmpRecordId);
                HrmSalarySlip hrmSalarySlip = new HrmSalarySlip();
                hrmSalarySlip.setRecordId(salarySlipRecord.getId());
                hrmSalarySlip.setSEmpRecordId(sEmpRecordId);
                Integer employeeId = salaryMonthEmpRecordService.lambdaQuery().select(HrmSalaryMonthEmpRecord::getEmployeeId).eq(HrmSalaryMonthEmpRecord::getSEmpRecordId, sEmpRecordId).one().getEmployeeId();
                hrmSalarySlip.setEmployeeId(employeeId);
                hrmSalarySlip.setYear(salaryMonthRecord.getYear());
                hrmSalarySlip.setMonth(salaryMonthRecord.getMonth());
                hrmSalarySlip.setRealSalary(Objects.isNull(codeValueMap.get(240101))?new BigDecimal(0):new BigDecimal(codeValueMap.get(240101)));
                hrmSalarySlip.setCreateUserId(userId);
                salarySlipService.save(hrmSalarySlip);
                List<HrmSalarySlipOption> batchSaveSlip = new ArrayList<>();
                for (int i = 0; i < slipTemplateOption.size(); i++) {
                    HrmSalarySlipTemplateOption categoryOptionTemplate = slipTemplateOption.get(i);
                    HrmSalarySlipOption categoryOption = new HrmSalarySlipOption();
                    categoryOption.setSlipId(hrmSalarySlip.getId());
                    categoryOption.setName(categoryOptionTemplate.getName());
                    categoryOption.setType(categoryOptionTemplate.getType());
                    categoryOption.setCode(categoryOptionTemplate.getCode());
                    categoryOption.setValue("");
                    categoryOption.setRemark(categoryOptionTemplate.getRemark());
                    categoryOption.setPid(0);
                    categoryOption.setSort(i+1);
                    categoryOption.setCreateUserId(userId);
                    salarySlipOptionService.save(categoryOption);
                    List<HrmSalarySlipTemplateOption> optionList = categoryOptionTemplate.getOptionList();
                    for (int j = 0; j < optionList.size(); j++) {
                        HrmSalarySlipTemplateOption optionTemplate = optionList.get(j);
                        String value = Objects.isNull(codeValueMap.get(optionTemplate.getCode())) ? "0" : codeValueMap.get(optionTemplate.getCode());
                        if (sendSalarySlipBO.getHideEmpty() == 1 && new BigDecimal(value).equals(BigDecimal.ZERO)){
                            continue;
                        }
                        HrmSalarySlipOption option = new HrmSalarySlipOption();
                        option.setSlipId(hrmSalarySlip.getId());
                        option.setName(optionTemplate.getName());
                        option.setType(optionTemplate.getType());
                        option.setCode(optionTemplate.getCode());
                        option.setValue(value);
                        option.setPid(categoryOption.getId());
                        option.setRemark(optionTemplate.getRemark());
                        option.setSort(j+1);
                        option.setCreateUserId(userId);
                        batchSaveSlip.add(option);
                    }
                }
                salarySlipOptionService.saveBatch(batchSaveSlip,batchSaveSlip.size());
                AdminMessage adminMessage = new AdminMessage();
                AdminMessageEnum adminMessageEnum = AdminMessageEnum.HRM_SEND_SLIP;
                adminMessage.setTitle(adminMessageEnum.getRemarks());
                adminMessage.setContent(hrmSalarySlip.getYear()+","+hrmSalarySlip.getMonth());
                adminMessage.setLabel(adminMessageEnum.getLabel());
                adminMessage.setType(adminMessageEnum.getType());
                adminMessage.setTypeId(hrmSalarySlip.getId());
                adminMessage.setCreateUser(userId);
                adminMessage.setRecipientUser(EmployeeCacheUtil.getUserId(employeeId));
                adminMessage.setCreateTime(DateUtil.formatDateTime(new Date()));
                adminMessageService.save(adminMessage);
//            });
        }
    }

    @Override
    public BasePage<QuerySendRecordListVO> querySendRecordList(QuerySendRecordListBO querySendRecordListBO) {
        BasePage<QuerySendRecordListVO> page = slipRecordMapper.querySendRecordList(querySendRecordListBO.parse(), querySendRecordListBO);
        page.getList().forEach(record->{
            record.setCreateUserName(UserCacheUtil.getUserName(record.getCreateUserId()));
        });
        return page;
    }

    @Override
    public BasePage<QuerySendDetailListVO> querySendDetailList(QuerySendDetailListBO querySendRecordListBO) {
        return slipRecordMapper.querySendDetailList(querySendRecordListBO.parse(),querySendRecordListBO);
    }

    @Override
    public void setSlipRemarks(SetSlipRemarksBO setSlipRemarksBO) {
        salarySlipService.lambdaUpdate().set(HrmSalarySlip::getRemarks,setSlipRemarksBO.getRemarks())
                .in(HrmSalarySlip::getId,setSlipRemarksBO.getIds()).update();
    }

    @Override
    public List<HrmSalarySlipOption> querySlipDetail(Integer id) {
        List<HrmSalarySlipOption> list = salarySlipOptionService.lambdaQuery().eq(HrmSalarySlipOption::getSlipId, id)
                .orderByAsc(HrmSalarySlipOption::getSort).list();
        Map<Integer, List<HrmSalarySlipOption>> optionListMap = list.stream().filter(option -> option.getPid() != 0).collect(Collectors.groupingBy(HrmSalarySlipOption::getPid));
        return list.stream().filter(option -> option.getPid() == 0).peek(option -> option.setOptionList(optionListMap.get(option.getId()))).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSendRecord(String id) {
        List<Integer> slipIds = salarySlipService.lambdaQuery().select(HrmSalarySlip::getId).eq(HrmSalarySlip::getRecordId, id).list()
                .stream().map(HrmSalarySlip::getId).collect(Collectors.toList());
        salarySlipOptionService.lambdaUpdate().in(HrmSalarySlipOption::getSlipId,slipIds).remove();
        salarySlipService.removeByIds(slipIds);
        removeById(id);
    }
}
