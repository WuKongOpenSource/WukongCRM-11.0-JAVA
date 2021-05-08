package com.kakarote.crm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.CrmMemberSaveBO;
import com.kakarote.crm.entity.PO.CrmTeamMembers;
import com.kakarote.crm.entity.VO.CrmMembersSelectVO;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * crm团队成员表
 * 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2021-04-07
 */
public interface ICrmTeamMembersService extends BaseService<CrmTeamMembers> {
    /**
     * 获取团队成员
     *
     * @param crmEnum     对应类型
     * @param typeId      对应类型ID
     * @param ownerUserId 负责人ID
     * @return data
     */
    public List<CrmMembersSelectVO> getMembers(CrmEnum crmEnum, Integer typeId, Long ownerUserId);

    /**
     * 添加团队成员
     *
     * @param crmEnum         对应类型
     * @param crmMemberSaveBO data
     */
    public void addMember(CrmEnum crmEnum, CrmMemberSaveBO crmMemberSaveBO);

    /**
     * 删除团队成员
     *
     * @param crmEnum         对应类型
     * @param crmMemberSaveBO data
     */
    public void deleteMember(CrmEnum crmEnum, CrmMemberSaveBO crmMemberSaveBO);

    /**
     * 退出团队
     *
     * @param crmEnum 对应类型
     * @param typeId  对应类型ID
     */
    public void exitTeam(CrmEnum crmEnum, Integer typeId);

    /**
     * 添加单条团队成员数据
     *
     * @param crmEnum 对应类型
     * @param typeId  对应类型ID
     * @param userId  用户ID
     * @param power   读写类型
     */
    public void addSingleMember(CrmEnum crmEnum, Integer typeId, Long userId, Integer power, Date expiresTime, String name);


    /**
     * 查询团队成员数量
     * @param crmEnum 对应类型
     * @param typeId  对应类型ID
     * @param ownerUserId  用户ID
     */
    public Integer queryMemberCount(CrmEnum crmEnum, Integer typeId, Long ownerUserId);

    /**
     * 删除过期的团队成员数据
     */
    public void removeOverdueTeamMembers();
}
