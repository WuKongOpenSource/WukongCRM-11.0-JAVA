package com.kakarote.core.servlet.upload;

/**
 * @author zhangzhiwei
 * 上传文件方式枚举
 */

public enum UploadFileEnum {
    /**
     * 本地上传
     */
    LOCAL(1),
    /**
     * 阿里云OSS
     */
    ALI_OSS(2);

    private UploadFileEnum(Integer config) {
        this.config = config;
    }

    private Integer config;

    public Integer getConfig(){
        return this.config;
    }
}
