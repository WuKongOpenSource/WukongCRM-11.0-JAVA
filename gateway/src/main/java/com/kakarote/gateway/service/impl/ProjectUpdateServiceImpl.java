package com.kakarote.gateway.service.impl;

import com.kakarote.core.entity.UpdateConfigEntity;
import com.kakarote.core.projectupdate.UpdateProjectVersionUtil;
import com.kakarote.gateway.service.ProjectUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ProjectUpdateServiceImpl implements ProjectUpdateService {

    @Autowired
    private DiscoveryClient discoveryClient;


    /**
     * 更新指定项目的版本
     *
     * @param entity 配置文件
     */
    @Async
    @Override
    public void updateVersion(UpdateConfigEntity entity) {
        UpdateProjectVersionUtil.updateVersion(entity,discoveryClient);
    }
}
