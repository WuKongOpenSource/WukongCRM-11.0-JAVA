package com.kakarote.core.servlet.upload;

import cn.hutool.core.io.FileUtil;
import com.kakarote.core.utils.BaseUtil;

import java.io.File;
import java.io.InputStream;

/**
 * @author zhangzhiwei
 * 本地上传文件
 */
public class LocalFileServiceImpl implements FileService {

    private UploadConfig.LocalConfig config;

    private static final String JOIN_STR = "-";

    LocalFileServiceImpl(UploadConfig.LocalConfig config) {
        this.config = config;
    }

    /**
     * 上传文件
     *
     * @param inputStream 文件流
     * @param entity      参数对象
     * @return result
     */
    @Override
    public UploadEntity uploadFile(InputStream inputStream, UploadEntity entity) {
        String key = BaseUtil.getDate() + "/" + entity.getFileId() + JOIN_STR + entity.getName();
        entity.setType(UploadFileEnum.LOCAL.getConfig());
        File file = FileUtil.writeFromStream(inputStream, config.getUploadPath().get(entity.getIsPublic()) + "/" + key);
        if ("1".equals(entity.getIsPublic())) {
            entity.setPath(config.getPublicUrl()+key);
        }else {
            entity.setPath(file.getAbsolutePath());
        }
        return entity;
    }

    /**
     * 删除文件
     *
     * @param entity 参数对象
     */
    @Override
    public void deleteFile(UploadEntity entity) {
        FileUtil.del(entity.getPath());
    }

    @Override
    public void deleteFileByUrl(String url) {
        String path = config.getUploadPath().get("1")+"/"+url.replace(config.getPublicUrl(), "");
        FileUtil.del(path);
    }

    /**
     * 重命名文件
     *
     * @param entity   参数对象
     * @param fileName 文件名称
     */
    @Override
    public void renameFile(UploadEntity entity, String fileName) {

    }

    /**
     * 获取文件
     *
     * @param entity 参数对象
     * @return 文件流，可能为空
     */
    @Override
    public InputStream downFile(UploadEntity entity) {
        return null;
    }

    @Override
    public void downFileByUrl(String url, File file) {

    }
}
