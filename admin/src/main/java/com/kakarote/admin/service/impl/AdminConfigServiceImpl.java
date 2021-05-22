package com.kakarote.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.admin.common.AdminCodeEnum;
import com.kakarote.admin.common.AdminConst;
import com.kakarote.admin.common.AdminModuleEnum;
import com.kakarote.admin.common.AuthPasswordUtil;
import com.kakarote.admin.entity.BO.AdminCompanyBO;
import com.kakarote.admin.entity.BO.AdminInitDataBO;
import com.kakarote.admin.entity.BO.LogWelcomeSpeechBO;
import com.kakarote.admin.entity.PO.AdminAttention;
import com.kakarote.admin.entity.PO.AdminConfig;
import com.kakarote.admin.entity.PO.AdminUser;
import com.kakarote.admin.entity.PO.AdminUserRole;
import com.kakarote.admin.entity.VO.AdminUserVO;
import com.kakarote.admin.entity.VO.ModuleSettingVO;
import com.kakarote.admin.mapper.AdminConfigMapper;
import com.kakarote.admin.service.*;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.common.cache.AdminCacheKey;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.crm.service.CrmAnalysisService;
import com.kakarote.core.feign.jxc.service.JxcExamineService;
import com.kakarote.core.feign.km.KmService;
import com.kakarote.core.feign.oa.OaService;
import com.kakarote.core.feign.work.WorkService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.BaseUtil;
import com.kakarote.core.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户规则 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
@Service
@Slf4j
public class AdminConfigServiceImpl extends BaseServiceImpl<AdminConfigMapper, AdminConfig> implements IAdminConfigService {

    @Autowired
    private IAdminRoleService adminRoleService;
    @Autowired
    private IAdminUserService adminUserService;

    private static String logConfigName = "logWelcomeSpeech";


    @Autowired
    private IAdminAttentionService adminAttentionService;
    @Autowired
    private IAdminUserRoleService adminUserRoleService;
    @Autowired
    private CrmAnalysisService crmAnalysisService;
    @Autowired
    private WorkService workService;
    @Autowired
    private OaService oaService;
    @Autowired
    private KmService kmService;
    @Autowired
    private JxcExamineService jxcExamineService;

    /**
     * 通过name查询系统配置
     *
     * @param names names
     * @return adminConfig
     */
    @Override
    public List<AdminConfig> queryConfigListByName(Object... names) {
        return query().in("name", names).list();
    }

    /**
     * 设置企业配置
     *
     */
    @Override
    public void setAdminConfig(AdminCompanyBO adminCompanyBO) {
        String companyKey = "companyInfo";
        AdminConfig adminConfig = lambdaQuery().eq(AdminConfig::getName, companyKey).last(" limit 1").one();
        if (adminConfig == null) {
            adminConfig = new AdminConfig();
            adminConfig.setName(companyKey);
            adminConfig.setStatus(1);
            adminConfig.setDescription("企业LOGO配置");
        }
        adminConfig.setValue(JSON.toJSONString(adminCompanyBO));
        saveOrUpdate(adminConfig);
    }

    /**
     * 查询企业配置
     *
     * @return adminCompanyBO
     */
    @Override
    public AdminCompanyBO queryAdminConfig() {
        String companyKey = "companyInfo";
        AdminConfig adminConfig = lambdaQuery().eq(AdminConfig::getName, companyKey).last(" limit 1").one();
        AdminCompanyBO adminCompanyBO = new AdminCompanyBO();
        if (adminConfig != null) {
            adminCompanyBO = JSON.parseObject(adminConfig.getValue(), AdminCompanyBO.class);
        }
        return adminCompanyBO;
    }

    /**
     * 查询模块设置
     *
     * @return data
     */
    @Override
    public List<ModuleSettingVO> queryModuleSetting() {
        List<AdminConfig> adminConfigList = queryConfigListByName((Object[]) AdminModuleEnum.getValues());
        List<ModuleSettingVO> moduleSettingList = new ArrayList<>();
        adminConfigList.forEach(adminConfig -> {
            ModuleSettingVO moduleSettingVO = new ModuleSettingVO();
            moduleSettingVO.setSettingId(adminConfig.getSettingId());
            moduleSettingVO.setModule(adminConfig.getName());
            moduleSettingVO.setStatus(adminConfig.getStatus());
            moduleSettingVO.setType(adminConfig.getValue());
            moduleSettingVO.setName(adminConfig.getDescription());
            moduleSettingList.add(moduleSettingVO);
        });
        return moduleSettingList;
    }

    /**
     * 设置模块的禁用启用
     *
     * @param adminConfig data
     */
    @Override
    public void setModuleSetting(AdminConfig adminConfig) {
        updateById(adminConfig);
        //查询企业用户，将企业用户的权限缓存清除，重新计算
        List<AdminUserVO> userList = adminUserService.queryUserList(null).getRecords();
        //清除缓存
        adminRoleService.authInvalidate(userList.stream().map(AdminUserVO::getUserId).collect(Collectors.toList()));
    }

    /**
     * 设置日志欢迎语
     *
     * @param stringList data
     */
    @Override
    public void setLogWelcomeSpeech(List<String> stringList) {
        List<AdminConfig> configList = new ArrayList<>();
        stringList.forEach(str -> {
            AdminConfig config = new AdminConfig();
            config.setName(logConfigName);
            config.setValue(str);
            config.setStatus(1);
            configList.add(config);
        });
        QueryWrapper<AdminConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", logConfigName);
        remove(queryWrapper);
        saveBatch(configList, AdminConst.BATCH_SAVE_SIZE);
    }

    /**
     * 查询日志欢迎语
     *
     * @return data
     */
    @Override
    public List<LogWelcomeSpeechBO> getLogWelcomeSpeechList() {
        List<AdminConfig> adminConfigList = query().select("setting_id", "value").eq("name", logConfigName).list();
        return adminConfigList.stream().map(adminConfig -> BeanUtil.copyProperties(adminConfig, LogWelcomeSpeechBO.class)).collect(Collectors.toList());
    }

    /**
     * 根据名称查询配置信息
     *
     * @param name
     * @return data
     */
    @Override
    public AdminConfig queryConfigByName(String name) {
        return query().in("name", name).last(" limit 1").one();
    }

    /**
     * 查询呼叫中心设置
     *
     * @return data
     */
    @Override
    public ModuleSettingVO queryCallModuleSetting() {
        AdminConfig adminConfig = queryConfigByName(AdminModuleEnum.CALL.getValue());
        ModuleSettingVO moduleSettingVO = new ModuleSettingVO();
        moduleSettingVO.setSettingId(adminConfig.getSettingId());
        moduleSettingVO.setModule(adminConfig.getName());
        moduleSettingVO.setStatus(adminConfig.getStatus());
        moduleSettingVO.setType(adminConfig.getValue());
        moduleSettingVO.setName(adminConfig.getDescription());
        return moduleSettingVO;
    }


    @Override
    public void updateAdminConfig(AdminConfig adminConfig) {
        saveOrUpdate(adminConfig);
    }

    @Override
    public AdminConfig queryFirstConfigByNameAndValue(String name, String value) {
        return lambdaQuery().eq(AdminConfig::getName, name).eq(AdminConfig::getValue, value).one();
    }

    @Override
    public void setMarketing(Integer status) {
        String name = "marketing";
        AdminConfig adminConfig = this.lambdaQuery().eq(AdminConfig::getName, name).last(" limit 1").one();
        if (adminConfig == null) {
            AdminConfig config = new AdminConfig();
            config.setName(name);
            config.setStatus(Objects.equals(1, status) ? 1 : 0);
            config.setDescription("是否开启营销活动");
            this.save(config);
        } else {
            adminConfig.setStatus(Objects.equals(1, status) ? 1 : 0);
            this.updateById(adminConfig);
        }
    }

    @Override
    public Integer queryMarketing() {
        String name = "marketing";
        AdminConfig adminConfig = this.lambdaQuery().eq(AdminConfig::getName, name).last(" limit 1").one();
        if (adminConfig == null) {
            adminConfig = new AdminConfig();
            adminConfig.setName(name);
            adminConfig.setStatus(1);
            adminConfig.setDescription("是否开启营销活动");
            this.save(adminConfig);
        }
        return adminConfig.getStatus();
    }

    @Override
    public String verifyPassword(AdminInitDataBO adminInitDataBO) {
        Long userId = UserUtil.getUserId();
        AdminUser user = adminUserService.getById(userId);
         String userName = user.getUsername();
         boolean isPass = AuthPasswordUtil.verify(userName + adminInitDataBO.getPassword(), user.getSalt(), user.getPassword());
        if (isPass) {
            String cacheName = AdminCacheKey.TEMPORARY_ACCESS_CODE_CACHE_KEY + userId;
            String value = String.valueOf(RandomUtil.randomInt(100000, 999999));
            BaseUtil.getRedis().setex(cacheName, 600, value);
            return value;
        }
        throw new CrmException(AdminCodeEnum.ADMIN_PASSWORD_INVALID_ERROR);
    }


    @Override
    public boolean moduleInitData(AdminInitDataBO adminInitDataBO) {
        Long userId = UserUtil.getUserId();
        String cacheName = AdminCacheKey.TEMPORARY_ACCESS_CODE_CACHE_KEY + userId;
        String temporaryCode = BaseUtil.getRedis().get(cacheName);
        if (StrUtil.isEmpty(temporaryCode)) {
            //超时未操作
            throw new CrmException(AdminCodeEnum.ADMIN_PASSWORD_EXPIRE_ERROR);
        }
        if (!temporaryCode.equals(adminInitDataBO.getTemporaryCode())) {
            throw new CrmException(AdminCodeEnum.ADMIN_PASSWORD_INVALID_ERROR);
        }
        List<String> modules = adminInitDataBO.getModules();
        if (CollUtil.isNotEmpty(modules)) {
            for (String module : modules) {
                AdminModuleEnum adminModuleEnum = AdminModuleEnum.parse(module);
                if (adminModuleEnum == null) {
                    continue;
                }
                switch (adminModuleEnum) {
                    case TASK_EXAMINE:
                        //oa、work
                        oaService.initOaExamineData();
                        break;
                    case CRM:
                        crmAnalysisService.initCrmData();
                        break;
                    case PROJECT:
                        workService.initWorkData();
                        break;
                    case LOG:
                    case OA:
                        oaService.initOaData();
                        break;
                    case BOOK:
                        if (!UserUtil.isAdmin()) {
                            if (this.verifyInitAuth()) {
                                throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
                            }
                        }
                        log.info("开始初始化通讯录模块数据！");
                        List<AdminUser> adminUsers = adminUserService.lambdaQuery().select(AdminUser::getUserId)
                                .ne(AdminUser::getUserId, UserUtil.getUserId()).list();
                        if (CollUtil.isNotEmpty(adminUsers)){
                            List<Long> userIds = adminUsers.stream().map(AdminUser::getUserId).collect(Collectors.toList());
                            adminAttentionService.lambdaUpdate().in(AdminAttention::getBeUserId,userIds)
                                    .or().in(AdminAttention::getAttentionUserId,userIds)
                                    .remove();
                            adminUserRoleService.lambdaUpdate().in(AdminUserRole::getUserId,userIds)
                                    .remove();
                            adminUserService.removeByIds(userIds);
                        }
                        log.info("通讯录模块数据初始化完成！");
                        break;
                    case CALENDAR:
                        //oa
                        oaService.initCalendarData();
                        break;
                    case KNOWLEDGE:
                        kmService.initKmData();
                        break;
                    case JXC:
                        jxcExamineService.initJxcData();
                        break;
                    case HRM:
                        log.info("人资模块数据初始化暂未提供！");
                        break;
                    default:
                        break;
                }
            }
        }
        return true;
    }


    private static final String INIT_AUTH_URL = "/adminConfig/moduleInitData";


    /**
     * 验证非管理员有无权限
     *
     * @param
     * @return boolean
     * @date 2020/11/23 10:35
     **/
    private boolean verifyInitAuth() {
        boolean isNoAuth = false;
        Long userId = UserUtil.getUserId();
        String key = userId.toString();
        List<String> noAuthMenuUrls = BaseUtil.getRedis().get(key);
        if (noAuthMenuUrls != null && noAuthMenuUrls.contains(INIT_AUTH_URL)) {
            isNoAuth = true;
        }
        return isNoAuth;
    }
}
