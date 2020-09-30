package com.kakarote.oa.service;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.oa.entity.PO.OaAnnouncement;

/**
 * <p>
 * 公告表 服务类
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
public interface IOaAnnouncementService extends BaseService<OaAnnouncement> {
    /**
     * 添加公告
     * @param oaAnnouncement
     */
    public void addOaAnnouncement(OaAnnouncement oaAnnouncement);

    /**
     * 编辑公告
     * @param oaAnnouncement
     */
    public void setOaAnnouncement(OaAnnouncement oaAnnouncement);

    /**
     * 删除公告
     */
    public void delete(Integer announcementId);

    /**
     * 查询公告
     */
    public OaAnnouncement queryById(Integer announcementId);

    /**
     * 查询公共列表
     * @return data
     */
    public BasePage<OaAnnouncement> queryList(PageEntity entity,Integer type);
}
