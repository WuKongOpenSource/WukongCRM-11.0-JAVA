package com.kakarote.admin.mapper;

import com.kakarote.admin.entity.BO.AdminMessageQueryBO;
import com.kakarote.admin.entity.PO.AdminMessage;
import com.kakarote.admin.entity.VO.AdminMessageVO;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 系统消息表 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
public interface AdminMessageMapper extends BaseMapper<AdminMessage> {
    public BasePage<AdminMessage> queryList(BasePage<AdminMessage> parse, @Param("data") AdminMessageQueryBO adminMessageBO);

    public AdminMessageVO queryUnreadCount(@Param("userId") Long userId);
}
