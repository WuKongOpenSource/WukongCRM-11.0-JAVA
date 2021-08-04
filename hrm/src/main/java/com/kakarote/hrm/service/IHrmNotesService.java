package com.kakarote.hrm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.BO.QueryNotesStatusBO;
import com.kakarote.hrm.entity.PO.HrmNotes;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 备忘 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-13
 */
public interface IHrmNotesService extends BaseService<HrmNotes> {

    /**
     * 添加备忘
     * @param notes
     */
    void addNotes(HrmNotes notes);

    /**
     * 查询备忘录列表
     * @param time
     * @param employeeIds
     * @return
     */
    List<HrmNotes> queryNoteListByTime(Date time, Collection<Integer> employeeIds);

    /**
     * 查询备忘录状态
     * @param queryNotesStatusBO
     * @param employeeIds
     * @return
     */
    Set<String> queryNoteStatusList(QueryNotesStatusBO queryNotesStatusBO, Collection<Integer> employeeIds);
}
