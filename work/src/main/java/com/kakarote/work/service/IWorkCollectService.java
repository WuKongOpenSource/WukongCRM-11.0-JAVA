package com.kakarote.work.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.work.entity.PO.WorkCollect;

/**
 * <p>
 * 项目收藏表 服务类
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
public interface IWorkCollectService extends BaseService<WorkCollect> {

    public void collect(Integer workId);
}
