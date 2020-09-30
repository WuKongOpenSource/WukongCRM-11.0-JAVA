package com.kakarote.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.admin.common.AdminConst;
import com.kakarote.admin.common.AdminModuleEnum;
import com.kakarote.admin.entity.BO.AdminCompanyBO;
import com.kakarote.admin.entity.BO.LogWelcomeSpeechBO;
import com.kakarote.admin.entity.PO.AdminConfig;
import com.kakarote.admin.entity.VO.AdminUserVO;
import com.kakarote.admin.entity.VO.ModuleSettingVO;
import com.kakarote.admin.mapper.AdminConfigMapper;
import com.kakarote.admin.service.IAdminConfigService;
import com.kakarote.admin.service.IAdminRoleService;
import com.kakarote.admin.service.IAdminUserService;
import com.kakarote.core.servlet.BaseServiceImpl;
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
public class AdminConfigServiceImpl extends BaseServiceImpl<AdminConfigMapper, AdminConfig> implements IAdminConfigService {

    @Autowired
    private IAdminRoleService adminRoleService;
    @Autowired
    private IAdminUserService adminUserService;

    private static String logConfigName = "logWelcomeSpeech";

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
        return query().in("name", name).one();
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
        return lambdaQuery().eq(AdminConfig::getName, name).eq(AdminConfig::getValue,value).one();
    }

    @Override
    public void setMarketing(Integer status) {
        String name = "marketing";
        AdminConfig adminConfig = this.lambdaQuery().eq(AdminConfig::getName,name).last(" limit 1").one();
        if (adminConfig == null) {
            AdminConfig config = new AdminConfig();
            config.setName(name);
            config.setStatus(Objects.equals(1,status) ? 1 : 0);
            config.setDescription("是否开启营销活动");
            this.save(config);
        } else {
            adminConfig.setStatus(Objects.equals(1,status) ? 1 : 0);
            this.updateById(adminConfig);
        }
    }

    @Override
    public Integer queryMarketing() {
        String name = "marketing";
        AdminConfig adminConfig = this.lambdaQuery().eq(AdminConfig::getName,name).last(" limit 1").one();
        if (adminConfig == null) {
            adminConfig = new AdminConfig();
            adminConfig.setName(name);
            adminConfig.setStatus(1);
            adminConfig.setDescription("是否开启营销活动");
            this.save(adminConfig);
        }
        return adminConfig.getStatus();
    }
}
