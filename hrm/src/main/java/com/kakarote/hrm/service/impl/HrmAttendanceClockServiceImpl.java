package com.kakarote.hrm.service.impl;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.hrm.common.EmployeeHolder;
import com.kakarote.hrm.constant.MenuIdConstant;
import com.kakarote.hrm.entity.BO.QueryAttendancePageBO;
import com.kakarote.hrm.entity.BO.QueryNotesStatusBO;
import com.kakarote.hrm.entity.PO.HrmAttendanceClock;
import com.kakarote.hrm.entity.VO.QueryAttendancePageVO;
import com.kakarote.hrm.mapper.HrmAttendanceClockMapper;
import com.kakarote.hrm.service.IHrmAttendanceClockService;
import com.kakarote.hrm.utils.EmployeeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 打卡记录表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-12-07
 */
@Service
public class HrmAttendanceClockServiceImpl extends BaseServiceImpl<HrmAttendanceClockMapper, HrmAttendanceClock> implements IHrmAttendanceClockService {

    @Autowired
    private EmployeeUtil employeeUtil;

    @Override
    public void addOrUpdate(HrmAttendanceClock attendanceClock) {
        attendanceClock.setAttendanceTime(new Date());
        attendanceClock.setClockEmployeeId(EmployeeHolder.getEmployeeId());
        saveOrUpdate(attendanceClock);
    }

    @Override
    public BasePage<QueryAttendancePageVO> queryPageList(QueryAttendancePageBO attendancePageBO) {
        Collection<Integer> employeeIds = employeeUtil.queryDataAuthEmpIdByMenuId(MenuIdConstant.ATTENDANCE_MENU_ID);
        return getBaseMapper().queryPageList(attendancePageBO.parse(),attendancePageBO,employeeIds);
    }

    @Override
    public List<HrmAttendanceClock> queryClockListByTime(Date time, Collection<Integer> employeeIds) {
        return getBaseMapper().queryClockListByTime(time,employeeIds);
    }

    @Override
    public Set<String> queryClockStatusList(QueryNotesStatusBO queryNotesStatusBO, Collection<Integer> employeeIds) {
        return getBaseMapper().queryClockStatusList(queryNotesStatusBO,employeeIds);
    }

    @Override
    public BasePage<QueryAttendancePageVO> queryMyPageList(PageEntity pageEntity) {
        return getBaseMapper().queryMyPageList(pageEntity.parse(),EmployeeHolder.getEmployeeId());
    }
}
