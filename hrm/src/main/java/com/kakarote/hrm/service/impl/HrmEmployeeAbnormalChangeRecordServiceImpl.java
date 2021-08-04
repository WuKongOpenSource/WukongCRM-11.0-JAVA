package com.kakarote.hrm.service.impl;

import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.hrm.constant.AbnormalChangeType;
import com.kakarote.hrm.entity.PO.HrmEmployeeAbnormalChangeRecord;
import com.kakarote.hrm.mapper.HrmEmployeeAbnormalChangeRecordMapper;
import com.kakarote.hrm.service.IHrmEmployeeAbnormalChangeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 员工异常表动记录表（薪资列表统计需要） 服务实现类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-06-08
 */
@Service
public class HrmEmployeeAbnormalChangeRecordServiceImpl extends BaseServiceImpl<HrmEmployeeAbnormalChangeRecordMapper, HrmEmployeeAbnormalChangeRecord> implements IHrmEmployeeAbnormalChangeRecordService {


    @Autowired
    private HrmEmployeeAbnormalChangeRecordMapper abnormalChangeRecordMapper;

    @Override
    public void addAbnormalChangeRecord(Integer employeeId, AbnormalChangeType abnormalChangeType, Date changeTime) {
        if (abnormalChangeType.equals(AbnormalChangeType.RESIGNATION)){
            //离职删除入职
            lambdaUpdate().eq(HrmEmployeeAbnormalChangeRecord::getEmployeeId,employeeId)
                    .eq(HrmEmployeeAbnormalChangeRecord::getType,AbnormalChangeType.NEW_ENTRY.getValue()).remove();
        }else if (abnormalChangeType.equals(AbnormalChangeType.NEW_ENTRY)){
            //删除离职
            lambdaUpdate().eq(HrmEmployeeAbnormalChangeRecord::getEmployeeId,employeeId)
                    .eq(HrmEmployeeAbnormalChangeRecord::getType,AbnormalChangeType.RESIGNATION.getValue()).remove();
        }
        HrmEmployeeAbnormalChangeRecord abnormalChangeRecord = new HrmEmployeeAbnormalChangeRecord();
        abnormalChangeRecord.setType(abnormalChangeType.getValue());
        abnormalChangeRecord.setEmployeeId(employeeId);
        abnormalChangeRecord.setChangeTime(changeTime);
        save(abnormalChangeRecord);
    }

    @Override
    public List<HrmEmployeeAbnormalChangeRecord> queryListByDate(Date startTime, Date endTime, Collection<Integer> employeeIds, Integer type) {
        return abnormalChangeRecordMapper.queryListByDate(startTime,endTime,employeeIds,type);
    }

    @Override
    public List<HrmEmployeeAbnormalChangeRecord> queryListByDate1(int year, int monthValue, Integer type, Collection<Integer> employeeIds) {
        return abnormalChangeRecordMapper.queryListByDate1(year,monthValue,type,employeeIds);
    }
}
