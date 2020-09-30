package com.kakarote.gateway.service;

import com.kakarote.core.entity.UpdateConfigEntity;

public interface ProjectUpdateService {

    /**
     * 更新指定项目的版本
     * @param entity 配置文件
     */
    public void updateVersion(UpdateConfigEntity entity);
}
