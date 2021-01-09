package com.kakarote.core.servlet.upload;

import cn.hutool.core.io.IoUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.kakarote.core.utils.BaseUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

/**
 * @author zhangzhiwei
 * oss上传文件
 */
public class OssFileServiceImpl implements FileService {

    private UploadConfig.OssConfig config;

    private static final String JOIN_STR = "-";

    private OSS clint;

    public OssFileServiceImpl(UploadConfig.OssConfig config) {
        this.config = config;
        this.clint = new OSSClientBuilder().build(config.getEndpoint(), config.getAccessKeyId(), config.getAccessKeySecret());
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
        entity.setType(UploadFileEnum.ALI_OSS.getConfig());
        if ("1".equals(entity.getIsPublic())) {
            entity.setPath(config.getPublicUrl()+key);
        }else {
            entity.setPath(key);
        }
        try {
            clint.putObject(config.getBucketName().get(entity.getIsPublic()), key, inputStream);
            return entity;
        } finally {
            clint.shutdown();
        }
    }

    /**
     * 删除文件
     *
     * @param entity 参数对象
     * @return result
     */
    @Override
    public void deleteFile(UploadEntity entity) {
        String key = entity.getFileId() + JOIN_STR + entity.getName();
        try {
            clint.deleteObject(config.getBucketName().get(entity.getIsPublic()), key);
        } finally {
            clint.shutdown();
        }
    }

    @Override
    public void deleteFileByUrl(String url) {
        String key = url.replace(config.getPublicUrl(), "");
        try {
            clint.deleteObject(config.getBucketName().get("1"), key);
        } finally {
            clint.shutdown();
        }
    }

    /**
     * 重命名文件
     *
     * @param entity   参数对象
     * @param fileName 文件名称
     * @return result
     */
    @Override
    public void renameFile(UploadEntity entity, String fileName) {
        String key = entity.getFileId() + JOIN_STR;
        clint.copyObject(config.getBucketName().get(entity.getIsPublic()), key + entity.getName(), config.getBucketName().get(entity.getIsPublic()), key + fileName);
        deleteFile(entity);
    }

    /**
     * 获取文件
     *
     * @param entity 参数对象
     * @return 文件流，可能为空
     */
    @Override
    public InputStream downFile(UploadEntity entity) {
        OSSObject object = null;
        try {
            object = clint.getObject(config.getBucketName().get(entity.getIsPublic()), entity.getPath());
            byte[] bytes = IoUtil.readBytes(object.getObjectContent());
            return new ByteArrayInputStream(bytes);
        } finally {
            clint.shutdown();
        }
    }

    @Override
    public void downFileByUrl(String url,File file) {
        String key = url.replace(config.getPublicUrl(), "");
        try {
            clint.getObject(new GetObjectRequest(config.getBucketName().get("1"),key),file);
        } finally {
            clint.shutdown();
        }
    }
}
