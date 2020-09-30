package com.kakarote.oa.mapper;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import com.kakarote.oa.entity.PO.OaAnnouncement;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 公告表 Mapper 接口
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
public interface OaAnnouncementMapper extends BaseMapper<OaAnnouncement> {
    public BasePage<OaAnnouncement> queryList(BasePage<OaAnnouncement> parse, @Param("type") Integer type, @Param("userId") Long userId, @Param("deptId") Integer deptId);
}
