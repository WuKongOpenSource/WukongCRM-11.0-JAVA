package com.kakarote.crm.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.util.TypeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminMessageBO;
import com.kakarote.core.feign.admin.entity.AdminMessageEnum;
import com.kakarote.core.feign.admin.service.AdminMessageService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.common.ActionRecordUtil;
import com.kakarote.crm.common.AuthUtil;
import com.kakarote.crm.constant.CrmAuthEnum;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.CrmMemberSaveBO;
import com.kakarote.crm.entity.PO.*;
import com.kakarote.crm.entity.VO.CrmMembersSelectVO;
import com.kakarote.crm.mapper.CrmTeamMembersMapper;
import com.kakarote.crm.service.*;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * crm团队成员表
 * 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2021-04-07
 */
@Service
@Slf4j
public class CrmTeamMembersServiceImpl extends BaseServiceImpl<CrmTeamMembersMapper, CrmTeamMembers> implements ICrmTeamMembersService {

    @Autowired
    private ActionRecordUtil actionRecordUtil;

    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    /**
     * 获取团队成员
     *
     * @param crmEnum     对应类型
     * @param typeId      对应类型ID
     * @param ownerUserId 负责人ID
     * @return data
     */
    @Override
    public List<CrmMembersSelectVO> getMembers(CrmEnum crmEnum, Integer typeId, Long ownerUserId) {
        List<CrmMembersSelectVO> selectVOS = new ArrayList<>();
        if (ownerUserId != null) {
            List<Long> authUserList = AuthUtil.queryAuthUserList(crmEnum, CrmAuthEnum.READ);
            Integer num = lambdaQuery().eq(CrmTeamMembers::getType,crmEnum.getType()).eq(CrmTeamMembers::getTypeId,typeId).eq(CrmTeamMembers::getUserId,UserUtil.getUserId()).count();
            if (!authUserList.contains(ownerUserId) && num == 0) {
                throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
            }
            UserInfo userInfo = UserCacheUtil.getUserInfo(ownerUserId);
            CrmMembersSelectVO selectVO = new CrmMembersSelectVO();
            selectVO.setUserId(userInfo.getUserId());
            selectVO.setPower(3);
            selectVO.setDeptName(userInfo.getDeptName());
            selectVO.setRealname(userInfo.getRealname());
            selectVO.setExpiresTime(null);
            selectVOS.add(selectVO);
        }
        List<CrmTeamMembers> teamMembers = lambdaQuery().eq(CrmTeamMembers::getType, crmEnum.getType()).eq(CrmTeamMembers::getTypeId, typeId).list();
        for (CrmTeamMembers teamMember : teamMembers) {
            if (Objects.equals(teamMember.getUserId(), ownerUserId)) {
                continue;
            }
            CrmMembersSelectVO selectVO = new CrmMembersSelectVO();
            UserInfo userInfo = UserCacheUtil.getUserInfo(teamMember.getUserId());
            selectVO.setUserId(teamMember.getUserId());
            selectVO.setPower(teamMember.getPower());
            selectVO.setDeptName(userInfo.getDeptName());
            selectVO.setRealname(userInfo.getRealname());
            selectVO.setExpiresTime(DateUtil.formatDate(teamMember.getExpiresTime()));
            selectVOS.add(selectVO);
        }
        return selectVOS;
    }

    /**
     * 添加团队成员
     *
     * @param crmEnum         对应类型
     * @param crmMemberSaveBO data
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addMember(CrmEnum crmEnum, CrmMemberSaveBO crmMemberSaveBO) {
        addMember(crmEnum, crmMemberSaveBO, false, new ArrayList<>());
    }

    /**
     * 删除团队成员
     *
     * @param crmEnum         对应类型
     * @param crmMemberSaveBO data
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMember(CrmEnum crmEnum, CrmMemberSaveBO crmMemberSaveBO) {
        for (Integer typeId : crmMemberSaveBO.getIds()) {
            if (crmMemberSaveBO.getChangeType() != null && crmEnum == CrmEnum.CUSTOMER) {
                if (crmMemberSaveBO.getChangeType().contains(1)) {
                    LambdaQueryWrapper<CrmContacts> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(CrmContacts::getCustomerId, typeId);
                    queryWrapper.select(CrmContacts::getContactsId);
                    List<Integer> ids = ApplicationContextHolder.getBean(ICrmContactsService.class).listObjs(queryWrapper, TypeUtils::castToInt);
                    deleteMember(CrmEnum.CONTACTS, new CrmMemberSaveBO(ids, crmMemberSaveBO));
                }
                if (crmMemberSaveBO.getChangeType().contains(2)) {
                    LambdaQueryWrapper<CrmBusiness> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(CrmBusiness::getCustomerId, typeId);
                    queryWrapper.select(CrmBusiness::getBusinessId);
                    List<Integer> ids = ApplicationContextHolder.getBean(ICrmBusinessService.class).listObjs(queryWrapper, TypeUtils::castToInt);
                    deleteMember(CrmEnum.BUSINESS, new CrmMemberSaveBO(ids, crmMemberSaveBO));
                }
                if (crmMemberSaveBO.getChangeType().contains(3)) {
                    LambdaQueryWrapper<CrmContract> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(CrmContract::getCustomerId, typeId);
                    queryWrapper.select(CrmContract::getContractId);
                    List<Integer> ids = ApplicationContextHolder.getBean(ICrmContractService.class).listObjs(queryWrapper, TypeUtils::castToInt);
                    deleteMember(CrmEnum.CONTRACT, new CrmMemberSaveBO(ids, crmMemberSaveBO));
                }
            }
            deleteMembers(crmEnum, typeId, crmMemberSaveBO.getMemberIds());
        }

        updateEsField(crmEnum, crmMemberSaveBO.getIds(), crmMemberSaveBO.getMemberIds(), true);
    }

    /**
     * 退出团队
     *
     * @param crmEnum 对应类型
     * @param typeId  对应类型ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exitTeam(CrmEnum crmEnum, Integer typeId) {
        deleteMembers(crmEnum, typeId, Collections.singletonList(UserUtil.getUserId()));
        updateEsField(crmEnum, Collections.singletonList(typeId), Collections.singletonList(UserUtil.getUserId()), true);
    }

    /**
     * 添加单条团队成员数据
     *
     * @param crmEnum 对应类型
     * @param typeId  对应类型ID
     * @param userId  用户ID
     * @param power   读写类型
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSingleMember(CrmEnum crmEnum, Integer typeId, Long userId, Integer power, Date expiresTime, String name) {
        /*
          添加单条数据前先尝试删除，防止出现多余的数据
         */
        lambdaUpdate()
                .eq(CrmTeamMembers::getUserId,userId)
                .eq(CrmTeamMembers::getTypeId,typeId)
                .eq(CrmTeamMembers::getType,crmEnum.getType())
                .remove();
        CrmTeamMembers crmTeamMembers = new CrmTeamMembers();
        crmTeamMembers.setUserId(userId);
        crmTeamMembers.setTypeId(typeId);
        crmTeamMembers.setType(crmEnum.getType());
        crmTeamMembers.setPower(power);
        crmTeamMembers.setExpiresTime(expiresTime);
        save(crmTeamMembers);
        addTermMessage(crmEnum, typeId, name, userId, 1);
        updateEsField(crmEnum,Collections.singletonList(typeId),Collections.singletonList(userId),false);
    }

    /**
     * 查询团队成员数量
     *
     * @param crmEnum     对应类型
     * @param typeId      对应类型ID
     * @param ownerUserId 用户ID
     */
    @Override
    public Integer queryMemberCount(CrmEnum crmEnum, Integer typeId, Long ownerUserId) {
        Integer count = lambdaQuery()
                .eq(CrmTeamMembers::getType, crmEnum.getType())
                .eq(CrmTeamMembers::getTypeId, typeId).count();
        return ownerUserId != null ? count + 1 : count;
    }

    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;

    @Autowired
    private TransactionDefinition transactionDefinition;

    /**
     * 删除过期的团队成员数据
     */
    @Override
    public void removeOverdueTeamMembers() {
        List<CrmTeamMembers> teamMembers = lambdaQuery().lt(CrmTeamMembers::getExpiresTime, new Date()).list();
        Map<Integer, List<CrmTeamMembers>> listMap = teamMembers.stream().collect(Collectors.groupingBy(CrmTeamMembers::getType));
        listMap.forEach((type,typeIds)->{
            BulkRequest bulkRequest = new BulkRequest();
            Map<Integer, List<CrmTeamMembers>> typeIdMap = typeIds.stream().collect(Collectors.groupingBy(CrmTeamMembers::getTypeId));
            CrmEnum crmEnum = CrmEnum.parse(type);
            typeIdMap.forEach((typeId,memberIds)->{
                UpdateRequest request = new UpdateRequest(crmEnum.getIndex(),"_doc",typeId.toString());
                List<Long> ids = memberIds.stream().map(CrmTeamMembers::getUserId).collect(Collectors.toList());
                request.script(new Script(ScriptType.INLINE, "painless", "ctx._source.teamMemberIds.removeAll(params.value)", Collections.singletonMap("value", ids)));
                bulkRequest.add(request);
            });
            TransactionStatus transactionStatus = null;
            try {
                transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
                lambdaUpdate().in(CrmTeamMembers::getId,typeIds.stream().map(CrmTeamMembers::getId).collect(Collectors.toList())).remove();
                restTemplate.getClient().bulk(bulkRequest,RequestOptions.DEFAULT);
                dataSourceTransactionManager.commit(transactionStatus);
            } catch (IOException e) {
                dataSourceTransactionManager.rollback(transactionStatus);
            }
        });
    }

    private void addMember(CrmEnum crmEnum, CrmMemberSaveBO crmMemberSaveBO, boolean append, List<CrmTeamMembers> teamMembers) {
        if (crmMemberSaveBO.getPower() != 1 && crmMemberSaveBO.getPower() != 2) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
        }
        if (crmMemberSaveBO.getMemberIds().size() == 0) {
            return;
        }
        for (Integer id : crmMemberSaveBO.getIds()) {
            if (AuthUtil.isRwAuth(id, crmEnum, CrmAuthEnum.EDIT)) {
                continue;
            }
            List<Long> memberIds = new ArrayList<>(crmMemberSaveBO.getMemberIds());
            LambdaQueryWrapper<CrmTeamMembers> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(CrmTeamMembers::getUserId).eq(CrmTeamMembers::getType, crmEnum.getType())
                    .eq(CrmTeamMembers::getTypeId, id).in(CrmTeamMembers::getUserId,memberIds);
            List<Long> userIds = listObjs(wrapper, TypeUtils::castToLong);
            if(userIds.size() > 0) {
                lambdaUpdate()
                        .set(CrmTeamMembers::getPower,crmMemberSaveBO.getPower())
                        .set(CrmTeamMembers::getExpiresTime,crmMemberSaveBO.getExpiresTime())
                        .in(CrmTeamMembers::getUserId,userIds).update();
                memberIds.removeAll(userIds);
            }
            Object[] objects = getTypeName(crmEnum, id);
            if (objects.length == 0) {
                continue;
            }
            memberIds.removeIf(memberId -> Objects.equals(objects[0], memberId));
            if(memberIds.size() == 0) {
                continue;
            }
            for (Long memberId : memberIds) {
                CrmTeamMembers crmTeamMembers = new CrmTeamMembers();
                crmTeamMembers.setPower(crmMemberSaveBO.getPower());
                crmTeamMembers.setType(crmEnum.getType());
                crmTeamMembers.setTypeId(id);
                crmTeamMembers.setCreateTime(new Date());
                crmTeamMembers.setExpiresTime(crmMemberSaveBO.getExpiresTime());
                crmTeamMembers.setUserId(memberId);
                teamMembers.add(crmTeamMembers);
                addTermMessage(crmEnum, id, (String) objects[1], memberId, 1);
                actionRecordUtil.addMemberActionRecord(CrmEnum.CUSTOMER, id, memberId, (String) objects[1]);
            }
            updateEsField(crmEnum, Collections.singletonList(id), memberIds, false);
            if (crmMemberSaveBO.getChangeType() != null && crmEnum == CrmEnum.CUSTOMER) {
                if (crmMemberSaveBO.getChangeType().contains(1)) {
                    LambdaQueryWrapper<CrmContacts> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(CrmContacts::getCustomerId, id);
                    queryWrapper.select(CrmContacts::getContactsId);
                    List<Integer> ids = ApplicationContextHolder.getBean(ICrmContactsService.class).listObjs(queryWrapper, TypeUtils::castToInt);
                    addMember(CrmEnum.CONTACTS, new CrmMemberSaveBO(ids, crmMemberSaveBO), true, teamMembers);
                }
                if (crmMemberSaveBO.getChangeType().contains(2)) {
                    LambdaQueryWrapper<CrmBusiness> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(CrmBusiness::getCustomerId, id);
                    queryWrapper.select(CrmBusiness::getBusinessId);
                    List<Integer> ids = ApplicationContextHolder.getBean(ICrmBusinessService.class).listObjs(queryWrapper, TypeUtils::castToInt);
                    addMember(CrmEnum.BUSINESS, new CrmMemberSaveBO(ids, crmMemberSaveBO), true, teamMembers);
                }
                if (crmMemberSaveBO.getChangeType().contains(3)) {
                    LambdaQueryWrapper<CrmContract> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(CrmContract::getCustomerId, id);
                    queryWrapper.select(CrmContract::getContractId);
                    List<Integer> ids = ApplicationContextHolder.getBean(ICrmContractService.class).listObjs(queryWrapper, TypeUtils::castToInt);
                    addMember(CrmEnum.CONTRACT, new CrmMemberSaveBO(ids, crmMemberSaveBO), true, teamMembers);
                }
            }
        }
        if (!append) {
            saveBatch(teamMembers, Const.BATCH_SAVE_SIZE);
        }
    }


    /**
     * 发送通知
     *
     * @param crmEnum 对应crm
     * @param typeId  对应crm类型ID
     * @param title   标题 即对应类型名称
     * @param userId  用户ID
     * @param type    1 新增 2 移除 3 退出
     */
    private void addTermMessage(CrmEnum crmEnum, Integer typeId, String title, Long userId, Integer type) {
        String enumName = "CRM_" + crmEnum.name();

        switch (type) {
            case 1: {
                enumName += "_USER";
                break;
            }
            case 2: {
                enumName += "_REMOVE_TEAM";
                break;
            }
            case 3: {
                enumName += "_TEAM_EXIT";
                break;
            }
            default:
                return;
        }
        AdminMessageEnum adminMessageEnum = AdminMessageEnum.valueOf(enumName);
        AdminMessageBO adminMessageBO = new AdminMessageBO();
        adminMessageBO.setTitle(title);
        adminMessageBO.setTypeId(typeId);
        adminMessageBO.setUserId(UserUtil.getUserId());
        adminMessageBO.setIds(Collections.singletonList(userId));
        adminMessageBO.setMessageType(adminMessageEnum.getType());
        ApplicationContextHolder.getBean(AdminMessageService.class).sendMessage(adminMessageBO);
    }

    private void updateEsField(CrmEnum crmEnum, List<Integer> ids, List<Long> members, boolean isRemove) {
        if (ids.size() == 0 || members.size() == 0) {
            return;
        }
        try {
            for (Integer id : ids) {
                UpdateRequest updateRequest = new UpdateRequest(crmEnum.getIndex(), "_doc", id.toString());
                String script;
                if (isRemove) {
                    script = "if (ctx._source.teamMemberIds== null) {ctx._source.teamMemberIds=[]}else{ctx._source.teamMemberIds.removeAll(params.value)}";
                } else {
                    script = "if (ctx._source.teamMemberIds== null) {ctx._source.teamMemberIds=params.value}else{ctx._source.teamMemberIds.addAll(params.value)}";
                }
                updateRequest.script(new Script(ScriptType.INLINE, "painless", script, Collections.singletonMap("value", members)));
                restTemplate.getClient().update(updateRequest, RequestOptions.DEFAULT);
            }
            restTemplate.refresh(crmEnum.getIndex());
        } catch (Exception ex) {
            log.error("添加团队成员异常:", ex);
        }
    }

    private Object[] getTypeName(CrmEnum crmEnum, Integer typeId) {
        switch (crmEnum) {
            case CUSTOMER: {
                CrmCustomer customer = ApplicationContextHolder.getBean(ICrmCustomerService.class)
                        .lambdaQuery()
                        .select(CrmCustomer::getOwnerUserId, CrmCustomer::getCustomerName)
                        .eq(CrmCustomer::getCustomerId, typeId)
                        .one();
                return new Object[]{customer.getOwnerUserId(), customer.getCustomerName()};
            }
            case CONTRACT: {
                CrmContract contract = ApplicationContextHolder.getBean(ICrmContractService.class)
                        .lambdaQuery()
                        .select(CrmContract::getOwnerUserId, CrmContract::getName)
                        .eq(CrmContract::getContractId, typeId)
                        .one();
                return new Object[]{contract.getOwnerUserId(), contract.getName()};
            }
            case BUSINESS: {
                CrmBusiness business = ApplicationContextHolder.getBean(ICrmBusinessService.class)
                        .lambdaQuery()
                        .select(CrmBusiness::getOwnerUserId, CrmBusiness::getBusinessName)
                        .eq(CrmBusiness::getBusinessId, typeId)
                        .one();
                return new Object[]{business.getOwnerUserId(), business.getBusinessName()};
            }
            case CONTACTS: {
                CrmContacts contacts = ApplicationContextHolder.getBean(ICrmContactsService.class)
                        .lambdaQuery()
                        .select(CrmContacts::getOwnerUserId, CrmContacts::getName)
                        .eq(CrmContacts::getContactsId, typeId)
                        .one();
                return new Object[]{contacts.getOwnerUserId(), contacts.getName()};
            }
            case RECEIVABLES: {
                CrmReceivables receivables = ApplicationContextHolder.getBean(ICrmReceivablesService.class)
                        .lambdaQuery()
                        .select(CrmReceivables::getOwnerUserId, CrmReceivables::getNumber)
                        .eq(CrmReceivables::getReceivablesId, typeId)
                        .one();
                return new Object[]{receivables.getOwnerUserId(), receivables.getNumber()};
            }
            default: {
                return new Object[0];
            }
        }
    }

    private void deleteMembers(CrmEnum crmEnum, Integer typeId, List<Long> memberIds) {
        Object[] objects = getTypeName(crmEnum, typeId);
        if (objects.length == 0) {
            return;
        }
        for (Long memberId : memberIds) {
            Integer count = lambdaQuery()
                    .eq(CrmTeamMembers::getType, crmEnum.getType())
                    .eq(CrmTeamMembers::getTypeId, typeId)
                    .eq(CrmTeamMembers::getUserId, memberId)
                    .count();
            if (count == 0) {
                continue;
            }
            if (!memberId.equals(UserUtil.getUserId())) {
                addTermMessage(crmEnum, typeId, (String) objects[1], memberId, 2);
                actionRecordUtil.addDeleteMemberActionRecord(crmEnum, typeId, memberId, false, (String) objects[1]);
            } else {
                addTermMessage(crmEnum, typeId, (String) objects[1], memberId, 3);
                actionRecordUtil.addDeleteMemberActionRecord(crmEnum, typeId, memberId, true, (String) objects[1]);
            }
        }
        lambdaUpdate()
                .eq(CrmTeamMembers::getType, crmEnum.getType())
                .eq(CrmTeamMembers::getTypeId, typeId)
                .in(CrmTeamMembers::getUserId, memberIds).remove();
    }
}
