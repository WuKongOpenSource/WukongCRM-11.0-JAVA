package com.kakarote.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.kakarote.admin.entity.BO.AdminMessageQueryBO;
import com.kakarote.admin.entity.PO.AdminMessage;
import com.kakarote.admin.entity.VO.AdminMessageVO;
import com.kakarote.admin.mapper.AdminMessageMapper;
import com.kakarote.admin.service.IAdminMessageService;
import com.kakarote.core.common.Const;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessageBO;
import com.kakarote.core.feign.admin.entity.AdminMessageEnum;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 系统消息表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
@Service
public class AdminMessageServiceImpl extends BaseServiceImpl<AdminMessageMapper, AdminMessage> implements IAdminMessageService {

    /**
     * 新增或修改消息
     *
     * @param message message
     */
    @Override
    public Long saveOrUpdateMessage(com.kakarote.core.feign.admin.entity.AdminMessage message) {
        AdminMessage adminMessage = BeanUtil.copyProperties(message, AdminMessage.class);
        adminMessage.setCreateTime(new Date());
        saveOrUpdate(adminMessage);
        return adminMessage.getMessageId();
    }

    /**
     * 查询消息列表
     *
     * @param adminMessageBO 搜索对象
     * @return data
     */
    @Override
    public BasePage<AdminMessage> queryList(AdminMessageQueryBO adminMessageBO) {
        adminMessageBO.setUserId(UserUtil.getUserId());
        BasePage<AdminMessage> adminMessageBasePage = getBaseMapper().queryList(adminMessageBO.parse(), adminMessageBO);
        if (Arrays.asList(14, 16, 18, 20).contains(adminMessageBO.getType())) {
            adminMessageBasePage.getList().forEach(data -> {
                List<String> splitTrim = StrUtil.splitTrim(data.getContent(), Const.SEPARATOR);
                data.setContent(splitTrim.size() > 0 ? splitTrim.get(0) : "0");
                if (StrUtil.isEmpty(data.getTitle())) {
                    data.setTitle("0");
                }
            });
        }
        return adminMessageBasePage;
    }

    /**
     * 查询未读消息数量
     *
     * @return data
     */
    @Override
    public AdminMessageVO queryUnreadCount() {
        return getBaseMapper().queryUnreadCount(UserUtil.getUserId());
    }

    /**
     * 新增消息
     *
     * @param adminMessageBO message
     */
    @Override
    public void addMessage(AdminMessageBO adminMessageBO) {
        if (adminMessageBO.getIds().size() == 0) {
            return;
        }
        AdminMessageEnum messageEnum = AdminMessageEnum.parse(adminMessageBO.getMessageType());
        switch (messageEnum) {
            case OA_TASK_ALLOCATION:
            case OA_TASK_JOIN:
            case OA_TASK_OVER:
                addOaTaskMessage(messageEnum, adminMessageBO.getTitle(), adminMessageBO.getTypeId(), adminMessageBO.getUserId(), adminMessageBO.getIds());
                break;
            case OA_LOG_SEND:
                addOaLogSendMessage(messageEnum, adminMessageBO.getTypeId(), adminMessageBO.getUserId(), adminMessageBO.getIds());
                break;
            case OA_LOG_REPLY:
                addOaLogReplyMessage(messageEnum, adminMessageBO.getTitle(), adminMessageBO.getContent(), adminMessageBO.getTypeId(), adminMessageBO.getUserId(), adminMessageBO.getIds());
                break;
            case OA_COMMENT_REPLY:
                addOaLogReplyMessage(messageEnum, adminMessageBO.getTitle(), adminMessageBO.getContent(), adminMessageBO.getTypeId(), adminMessageBO.getUserId(), adminMessageBO.getIds());
                break;
            case OA_EXAMINE_REJECT:
            case OA_EXAMINE_PASS:
                addOaLogReplyMessage(messageEnum, adminMessageBO.getTitle(), adminMessageBO.getContent(), adminMessageBO.getTypeId(), adminMessageBO.getUserId(), adminMessageBO.getIds());
                break;
            case OA_NOTICE_MESSAGE:
                addOaNoticeMessage(messageEnum, adminMessageBO.getTitle(), adminMessageBO.getTypeId(), adminMessageBO.getUserId(), adminMessageBO.getIds());
                break;
            case OA_EVENT_MESSAGE:
                addOaEventMessage(messageEnum, adminMessageBO.getTitle(), adminMessageBO.getTypeId(), adminMessageBO.getUserId(), adminMessageBO.getIds());
                break;
            case CRM_CONTRACT_PASS:
            case CRM_CONTRACT_REJECT:
            case CRM_RECEIVABLES_PASS:
            case CRM_RECEIVABLES_REJECT:
            case CRM_INVOICE_PASS:
            case CRM_INVOICE_REJECT:
                addCrmExamineMessage(messageEnum, adminMessageBO.getContent(), adminMessageBO.getTitle(), adminMessageBO.getTypeId(), adminMessageBO.getUserId(), adminMessageBO.getIds());
                break;
            case CRM_BUSINESS_USER:
            case CRM_CONTRACT_USER:
            case CRM_CUSTOMER_USER:
            case CRM_BUSINESS_TEAM_EXIT:
            case CRM_CUSTOMER_TEAM_EXIT:
            case CRM_CONTRACT_TEAM_EXIT:
            case CRM_BUSINESS_REMOVE_TEAM:
            case CRM_CUSTOMER_REMOVE_TEAM:
            case CRM_CONTRACT_REMOVE_TEAM:
                addCrmTeamMessage(messageEnum, adminMessageBO.getTypeId(), adminMessageBO.getTitle(), adminMessageBO.getUserId());
                break;
            case OA_EXAMINE_NOTICE:
                addOaExamineNotice(messageEnum, adminMessageBO.getTitle(), adminMessageBO.getTypeId(), adminMessageBO.getUserId(), adminMessageBO.getIds());
                break;
            case CRM_CONTRACT_EXAMINE:
            case CRM_RECEIVABLES_EXAMINE:
            case CRM_INVOICE_EXAMINE:
                addCrmExamineNotice(messageEnum, adminMessageBO.getTitle(), adminMessageBO.getTypeId(), adminMessageBO.getUserId(), adminMessageBO.getIds());
                break;
            case KM_DOCUMENT_SHARE_OPEN:
            case KM_DOCUMENT_SHARE_CLOSE:
                addKmDocumentShareNotice(messageEnum, adminMessageBO.getTitle(), adminMessageBO.getTypeId(), adminMessageBO.getUserId(), adminMessageBO.getIds());
                break;
            case JXC_PURCHASE_EXAMINE:
            case JXC_RETREAT_EXAMINE:
            case JXC_SALE_EXAMINE:
            case JXC_SALE_RETURN_EXAMINE:
            case JXC_PAYMENT_EXAMINE:
            case JXC_COLLECTION_EXAMINE:
            case JXC_INVENTORY_EXAMINE:
            case JXC_ALLOCATION_EXAMINE:
            case HRM_EMPLOYEE_SALARY_EXAMINE:
                addJXCExamineNotice(messageEnum, adminMessageBO.getTitle(), adminMessageBO.getTypeId(), adminMessageBO.getUserId(), adminMessageBO.getIds());
                break;
            case JXC_PURCHASE_PASS:
            case JXC_RETREAT_REJECT:
            case JXC_RETREAT_PASS:
            case JXC_SALE_REJECT:
            case JXC_SALE_PASS:
            case JXC_SALE_RETURN_REJECT:
            case JXC_SALE_RETURN_PASS:
            case JXC_PAYMENT_REJECT:
            case JXC_PAYMENT_PASS:
            case JXC_COLLECTION_REJECT:
            case JXC_COLLECTION_PASS:
            case JXC_INVENTORY_REJECT:
            case JXC_INVENTORY_PASS:
            case JXC_ALLOCATION_REJECT:
            case JXC_ALLOCATION_PASS:
            case HRM_EMPLOYEE_SALARY_PASS:
            case HRM_EMPLOYEE_SALARY_REJECT:
                addJXCExamineMessage(messageEnum, adminMessageBO.getContent(), adminMessageBO.getTitle(), adminMessageBO.getTypeId(), adminMessageBO.getUserId(), adminMessageBO.getIds());
                break;
            default:
                break;
        }
    }

    /**
     * 添加任务消息
     *
     * @author zhangzhiwei
     */
    private void addOaTaskMessage(AdminMessageEnum messageEnum, String name, Integer typeId, Long userId, List<Long> ids) {
        List<AdminMessage> messageList = new ArrayList<>();
        ids.forEach(id -> {
            if (userId.equals(id)) {
                return;
            }
            AdminMessage adminMessage = new AdminMessage();
            adminMessage.setCreateTime(new Date());
            adminMessage.setCreateUser(userId);
            adminMessage.setType(messageEnum.getType());
            adminMessage.setLabel(messageEnum.getLabel());
            adminMessage.setTitle(name);
            adminMessage.setRecipientUser(id);
            adminMessage.setTypeId(typeId);
            messageList.add(adminMessage);
        });
        saveBatch(messageList);
    }

    /**
     * 添加日志发送消息
     *
     * @author zhangzhiwei
     */
    private void addOaLogSendMessage(AdminMessageEnum messageEnum, Integer typeId, Long userId, List<Long> ids) {
        List<AdminMessage> messageList = new ArrayList<>();
        ids.forEach(id -> {
            AdminMessage adminMessage = new AdminMessage();
            adminMessage.setCreateTime(new Date());
            adminMessage.setCreateUser(userId);
            adminMessage.setType(messageEnum.getType());
            adminMessage.setLabel(messageEnum.getLabel());
            adminMessage.setTitle(DateUtil.today());
            adminMessage.setRecipientUser(id);
            adminMessage.setTypeId(typeId);
            messageList.add(adminMessage);
        });
        saveBatch(messageList);
    }

    /**
     * 添加日志回复消息
     *
     * @author zhangzhiwei
     */
    private void addOaLogReplyMessage(AdminMessageEnum messageEnum, String title, String content, Integer typeId, Long userId, List<Long> ids) {
        AdminMessage adminMessage = new AdminMessage();
        adminMessage.setCreateTime(new Date());
        adminMessage.setCreateUser(userId);
        adminMessage.setType(messageEnum.getType());
        adminMessage.setLabel(messageEnum.getLabel());
        adminMessage.setContent(content);
        adminMessage.setTitle(title);
        adminMessage.setRecipientUser(ids.get(0));
        adminMessage.setTypeId(typeId);
        save(adminMessage);
    }


    private void addKmDocumentShareNotice(AdminMessageEnum messageEnum, String title, Integer typeId, Long userId, List<Long> ids) {
        List<AdminMessage> adminMessageList = new ArrayList<>();
        for (Long shareUserId : ids) {
            AdminMessage adminMessage = new AdminMessage();
            adminMessage.setCreateTime(new Date());
            adminMessage.setCreateUser(userId);
            adminMessage.setType(messageEnum.getType());
            adminMessage.setLabel(messageEnum.getLabel());
            adminMessage.setTitle(title);
            adminMessage.setRecipientUser(shareUserId);
            adminMessage.setTypeId(typeId);
            adminMessageList.add(adminMessage);
        }
        saveBatch(adminMessageList);
    }

    /**
     * 添加oa待审批提醒
     *
     * @author wyq
     */
    private void addOaExamineNotice(AdminMessageEnum messageEnum, String title, Integer typeId, Long userId, List<Long> ids) {
        AdminMessage adminMessage = new AdminMessage();
        adminMessage.setCreateTime(new Date());
        adminMessage.setCreateUser(userId);
        adminMessage.setType(messageEnum.getType());
        adminMessage.setLabel(messageEnum.getLabel());
        adminMessage.setTitle(title);
        adminMessage.setRecipientUser(ids.get(0));
        adminMessage.setTypeId(typeId);
        save(adminMessage);
    }

    /**
     * 添加OA公告消息
     *
     * @author zhangzhiwei
     */
    private void addOaNoticeMessage(AdminMessageEnum messageEnum, String title, Integer typeId, Long userId, List<Long> ids) {
        List<AdminMessage> messageList = new ArrayList<>();
        ids.forEach(id -> {
            AdminMessage adminMessage = new AdminMessage();
            adminMessage.setCreateTime(new Date());
            adminMessage.setCreateUser(userId);
            adminMessage.setType(messageEnum.getType());
            adminMessage.setLabel(messageEnum.getLabel());
            adminMessage.setTitle(title);
            adminMessage.setRecipientUser(id);
            adminMessage.setTypeId(typeId);
            messageList.add(adminMessage);
        });
        saveBatch(messageList);
    }

    /**
     * 添加OA日程消息
     *
     * @author zhangzhiwei
     */
    private void addOaEventMessage(AdminMessageEnum messageEnum, String title, Integer typeId, Long userId, List<Long> ids) {
        List<AdminMessage> messageList = new ArrayList<>();
        ids.forEach(id -> {
            AdminMessage adminMessage = new AdminMessage();
            adminMessage.setCreateTime(new Date());
            adminMessage.setCreateUser(userId);
            adminMessage.setType(messageEnum.getType());
            adminMessage.setLabel(messageEnum.getLabel());
            adminMessage.setTitle(title);
            adminMessage.setRecipientUser(id);
            adminMessage.setTypeId(typeId);
            messageList.add(adminMessage);
        });
        saveBatch(messageList);
    }

    /**
     * 添加CRM审批消息
     *
     * @author zhangzhiwei
     */
    private void addCrmExamineMessage(AdminMessageEnum messageEnum, String content, String title, Integer typeId, Long userId, List<Long> ids) {
        AdminMessage adminMessage = new AdminMessage();
        adminMessage.setCreateTime(new Date());
        adminMessage.setCreateUser(userId);
        adminMessage.setType(messageEnum.getType());
        adminMessage.setLabel(messageEnum.getLabel());
        adminMessage.setContent(content);
        adminMessage.setTitle(title);
        adminMessage.setRecipientUser(ids.get(0));
        adminMessage.setTypeId(typeId);
        save(adminMessage);
    }

    private void addCrmExamineNotice(AdminMessageEnum messageEnum, String title, Integer typeId, Long userId, List<Long> ids) {
        AdminMessage adminMessage = new AdminMessage();
        adminMessage.setCreateTime(new Date());
        adminMessage.setCreateUser(userId);
        adminMessage.setType(messageEnum.getType());
        adminMessage.setLabel(messageEnum.getLabel());
        adminMessage.setTitle(title);
        adminMessage.setRecipientUser(ids.get(0));
        adminMessage.setTypeId(typeId);
        save(adminMessage);
    }

    /**
     * 团队成员提醒
     */
    private void addCrmTeamMessage(AdminMessageEnum messageEnum, Integer typeId, String title, Long userId) {
        AdminMessage adminMessage = new AdminMessage();
        adminMessage.setCreateTime(new Date());
        adminMessage.setCreateUser(UserUtil.getUserId());
        adminMessage.setType(messageEnum.getType());
        adminMessage.setLabel(messageEnum.getLabel());
        adminMessage.setRecipientUser(userId);
        adminMessage.setTypeId(typeId);
        adminMessage.setTitle(title);
        save(adminMessage);
    }


    /**
     * 添加JXC审批消息
     */
    private void addJXCExamineMessage(AdminMessageEnum messageEnum, String content, String title, Integer typeId, Long userId, List<Long> ids) {
        AdminMessage adminMessage = new AdminMessage();
        adminMessage.setCreateTime(new Date());
        adminMessage.setCreateUser(userId);
        adminMessage.setType(messageEnum.getType());
        adminMessage.setLabel(messageEnum.getLabel());
        adminMessage.setContent(content);
        adminMessage.setTitle(title);
        adminMessage.setRecipientUser(ids.get(0));
        adminMessage.setTypeId(typeId);
        save(adminMessage);
    }

    private void addJXCExamineNotice(AdminMessageEnum messageEnum, String title, Integer typeId, Long userId, List<Long> ids) {
        AdminMessage adminMessage = new AdminMessage();
        adminMessage.setCreateTime(new Date());
        adminMessage.setCreateUser(userId);
        adminMessage.setType(messageEnum.getType());
        adminMessage.setLabel(messageEnum.getLabel());
        adminMessage.setTitle(title);
        adminMessage.setRecipientUser(ids.get(0));
        adminMessage.setTypeId(typeId);
        save(adminMessage);
    }

    @Override
    public void deleteEventMessage(Integer eventId) {
        lambdaUpdate().eq(AdminMessage::getLabel,AdminMessageEnum.OA_EVENT_MESSAGE.getLabel())
                .eq(AdminMessage::getType,AdminMessageEnum.OA_EVENT_MESSAGE.getType())
                .apply("create_time > now()").eq(AdminMessage::getTypeId,eventId).remove();
    }

    @Override
    public void deleteById(Integer messageId) {
        removeById(messageId);
    }

    @Override
    public void deleteByLabel(Integer label) {
        lambdaUpdate().eq(AdminMessage::getLabel,label).remove();
    }
}
