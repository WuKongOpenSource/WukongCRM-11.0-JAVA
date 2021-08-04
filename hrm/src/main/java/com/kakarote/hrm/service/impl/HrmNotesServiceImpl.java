package com.kakarote.hrm.service.impl;

import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.hrm.common.EmployeeHolder;
import com.kakarote.hrm.entity.BO.QueryNotesStatusBO;
import com.kakarote.hrm.entity.PO.HrmNotes;
import com.kakarote.hrm.mapper.HrmNotesMapper;
import com.kakarote.hrm.service.IHrmNotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 备忘 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-13
 */
@Service
public class HrmNotesServiceImpl extends BaseServiceImpl<HrmNotesMapper, HrmNotes> implements IHrmNotesService {

    @Autowired
    private HrmNotesMapper notesMapper;

    @Override
    public void addNotes(HrmNotes notes) {
        notes.setEmployeeId(EmployeeHolder.getEmployeeId());
        save(notes);
    }

    @Override
    public List<HrmNotes> queryNoteListByTime(Date time, Collection<Integer> employeeIds) {
        return notesMapper.queryNoteListByTime(time,employeeIds);
    }

    @Override
    public Set<String> queryNoteStatusList(QueryNotesStatusBO queryNotesStatusBO, Collection<Integer> employeeIds) {
        return notesMapper.queryNoteStatusList(queryNotesStatusBO,employeeIds);
    }
}
