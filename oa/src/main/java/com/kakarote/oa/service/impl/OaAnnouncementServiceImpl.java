package com.kakarote.oa.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.kakarote.core.common.Const;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminMessageBO;
import com.kakarote.core.feign.admin.entity.AdminMessageEnum;
import com.kakarote.core.feign.admin.entity.SimpleDept;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminMessageService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.SeparatorUtil;
import com.kakarote.core.utils.TagUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.oa.common.OaCodeEnum;
import com.kakarote.oa.entity.PO.OaAnnouncement;
import com.kakarote.oa.mapper.OaAnnouncementMapper;
import com.kakarote.oa.service.IOaAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 公告表 服务实现类
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
@Service
public class OaAnnouncementServiceImpl extends BaseServiceImpl<OaAnnouncementMapper, OaAnnouncement> implements IOaAnnouncementService {

    @Autowired
    private AdminService adminService;

    @Override
    public void addOaAnnouncement(OaAnnouncement oaAnnouncement) {
        oaAnnouncement.setDeptIds(SeparatorUtil.fromString(oaAnnouncement.getDeptIds()));
        oaAnnouncement.setOwnerUserIds(SeparatorUtil.fromString(oaAnnouncement.getOwnerUserIds()));
        oaAnnouncement.setCreateTime(DateUtil.date());
        oaAnnouncement.setUpdateTime(DateUtil.date());
        save(oaAnnouncement);
        List<Long> ids = new ArrayList<>();
        if (StrUtil.isAllEmpty(oaAnnouncement.getOwnerUserIds(), oaAnnouncement.getDeptIds())) {
            ids.addAll(adminService.queryUserList().getData());
        } else {
            ids.addAll(StrUtil.splitTrim(oaAnnouncement.getOwnerUserIds(), Const.SEPARATOR).stream().map(Long::valueOf).collect(Collectors.toList()));
            List<Integer> deptIds = StrUtil.splitTrim(oaAnnouncement.getDeptIds(), Const.SEPARATOR).stream().map(Integer::valueOf).collect(Collectors.toList());
            if (deptIds.size() > 0) {
                ids.addAll(adminService.queryUserByDeptIds(deptIds).getData());
            }
            ids.add(UserUtil.getUserId());
        }
        if (ids.size() > 0) {
            AdminMessageBO adminMessageBO = new AdminMessageBO();
            adminMessageBO.setUserId(0L);
            adminMessageBO.setMessageType(AdminMessageEnum.OA_NOTICE_MESSAGE.getType());
            adminMessageBO.setTitle(oaAnnouncement.getTitle());
            adminMessageBO.setTypeId(oaAnnouncement.getAnnouncementId());
            adminMessageBO.setIds(ids);
            ApplicationContextHolder.getBean(AdminMessageService.class).sendMessage(adminMessageBO);
        }
    }

    @Override
    public void setOaAnnouncement(OaAnnouncement oaAnnouncement) {
        oaAnnouncement.setUpdateTime(DateUtil.date());
        updateById(oaAnnouncement);
    }

    @Override
    public void delete(Integer announcementId) {
        removeById(announcementId);
    }

    @Override
    public OaAnnouncement queryById(Integer announcementId) {
        OaAnnouncement announcement = getById(announcementId);
        if (announcement == null){
            throw new CrmException(OaCodeEnum.ANNOUNCEMENT_ALREADY_DELETE);
        }
        List<SimpleDept> deptList = adminService.queryDeptByIds(TagUtil.toSet(announcement.getDeptIds())).getData();
        announcement.setDeptList(deptList);
        List<SimpleUser> userList = adminService.queryUserByIds(TagUtil.toLongSet(announcement.getOwnerUserIds())).getData();
        announcement.setOwnerUserList(userList);
        return announcement;
    }

    /**
     * 查询公共列表
     *
     * @return data
     */
    @Override
    public BasePage<OaAnnouncement> queryList(PageEntity entity, Integer type) {
        UserInfo userInfo = UserUtil.getUser();
        return getBaseMapper().queryList(entity.parse(), type, userInfo.getUserId(), userInfo.getDeptId());
    }
}
