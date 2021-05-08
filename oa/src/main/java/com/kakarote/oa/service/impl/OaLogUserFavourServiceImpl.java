package com.kakarote.oa.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminMessageBO;
import com.kakarote.core.feign.admin.entity.AdminMessageEnum;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminMessageService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.oa.common.OaCodeEnum;
import com.kakarote.oa.entity.PO.OaLog;
import com.kakarote.oa.entity.PO.OaLogUserFavour;
import com.kakarote.oa.mapper.OaLogUserFavourMapper;
import com.kakarote.oa.service.IOaLogService;
import com.kakarote.oa.service.IOaLogUserFavourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户点赞日志关系表  服务实现类
 * </p>
 *
 * @author JiaS
 * @since 2021-03-02
 */
@Service
public class OaLogUserFavourServiceImpl extends BaseServiceImpl<OaLogUserFavourMapper, OaLogUserFavour> implements IOaLogUserFavourService {

    @Autowired
    private IOaLogUserFavourService oaLogUserFavourService;

    @Autowired
    private IOaLogService oaLogService;

    @Autowired
    private AdminService adminService;

    @Override
    public List<SimpleUser> queryFavourLogUserList(Integer logId) {
        List<OaLogUserFavour> logUserFavours = lambdaQuery().select(OaLogUserFavour::getUserId).eq(OaLogUserFavour::getLogId, logId).list();
        if (CollUtil.isNotEmpty(logUserFavours)){
            List<Long> userIds = logUserFavours.stream().map(OaLogUserFavour::getUserId).collect(Collectors.toList());
            return adminService.queryUserByIds(userIds).getData();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean queryUserWhetherFavourLog(Integer logId) {
        Integer count = lambdaQuery().eq(OaLogUserFavour::getUserId, UserUtil.getUserId()).eq(OaLogUserFavour::getLogId, logId).count();
        return count > 0;
    }

    @Override
    public JSONObject userFavourOrCancel(boolean isFavour, Integer logId) {
        if (isFavour) {
            if (!this.queryUserWhetherFavourLog(logId)){
                OaLog oaLog = oaLogService.getById(logId);
                if(oaLog == null){
                    throw new CrmException(OaCodeEnum.LOG_ALREADY_DELETE);
                }
                AdminMessageBO adminMessageBO = new AdminMessageBO();
                adminMessageBO.setUserId(UserUtil.getUserId());
                adminMessageBO.setTitle(DateUtil.formatDate(oaLog.getCreateTime()));
                adminMessageBO.setContent("");
                adminMessageBO.setTypeId(oaLog.getLogId());
                adminMessageBO.setMessageType(AdminMessageEnum.OA_LOG_FAVOUR.getType());
                adminMessageBO.setIds(Collections.singletonList(oaLog.getCreateUserId()));
                ApplicationContextHolder.getBean(AdminMessageService.class).sendMessage(adminMessageBO);
                OaLogUserFavour oaLogUserFavour = new OaLogUserFavour();
                oaLogUserFavour.setLogId(logId);
                oaLogUserFavour.setUserId(UserUtil.getUserId());
                save(oaLogUserFavour);
            }
        }else {
            lambdaUpdate().eq(OaLogUserFavour::getLogId,logId).eq(OaLogUserFavour::getUserId,UserUtil.getUserId()).remove();
        }
        JSONObject object = new JSONObject();
        object.put("favourUser",oaLogUserFavourService.queryFavourLogUserList(logId));
        object.put("isFavour",isFavour);
        return object;
    }
}
