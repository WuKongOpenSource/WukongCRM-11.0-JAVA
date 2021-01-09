package com.kakarote.admin.service;

import com.kakarote.admin.entity.BO.AdminMessageQueryBO;
import com.kakarote.admin.entity.PO.AdminMessage;
import com.kakarote.admin.entity.VO.AdminMessageVO;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessageBO;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 系统消息表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
public interface IAdminMessageService extends BaseService<AdminMessage> {

    /**
     * 新增或修改消息
     *
     * @param message message
     */
    public Long saveOrUpdateMessage(com.kakarote.core.feign.admin.entity.AdminMessage message);

    /**
     * 查询消息列表
     * @param adminMessageBO 搜索对象
     * @return data
     */
    public BasePage<AdminMessage> queryList(AdminMessageQueryBO adminMessageBO);

    /**
     * 查询未读消息数量
     * @return data
     */
    public AdminMessageVO queryUnreadCount();

    /**
     * 新增消息
     *
     * @param adminMessageBO message
     */
    public void addMessage(AdminMessageBO adminMessageBO);

    void deleteEventMessage(Integer eventId);

    void deleteById(Integer messageId);

    void deleteByLabel(Integer label);
}
