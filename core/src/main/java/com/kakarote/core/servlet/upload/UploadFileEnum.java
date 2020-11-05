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
    ALI_OSS(2),
    /**
     * 腾讯云COS
     */
    ALI_COS(3),
    /**
     * 七牛云QNC
     */
    ALI_QNC(4);

    private UploadFileEnum(Integer config) {
        this.config = config;
    }

    private Integer config;

    public Integer getConfig(){
        return this.config;
    }
}
