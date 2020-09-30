package com.kakarote.core.servlet.upload;

import com.kakarote.core.servlet.ApplicationContextHolder;

/**
 * @author zhang
 */
public class FileServiceFactory {

    public static FileService build() {
        UploadConfig uploadConfig = ApplicationContextHolder.getBean(UploadConfig.class);
        Integer config = uploadConfig.getConfig();

        if (config.equals(UploadFileEnum.ALI_OSS.getConfig())) {
            return new OssFileServiceImpl(uploadConfig.getOss());
        } else if (config.equals(UploadFileEnum.LOCAL.getConfig())) {
            return new LocalFileServiceImpl(uploadConfig.getLocal());
        }
        return new LocalFileServiceImpl(uploadConfig.getLocal());
    }

}
