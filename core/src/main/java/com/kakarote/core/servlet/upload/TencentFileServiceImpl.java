package com.kakarote.core.servlet.upload;

import com.kakarote.core.utils.BaseUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 腾讯云
 * @author JiaS
 * @date 2020/10/16
 */
@Slf4j
public class TencentFileServiceImpl implements FileService {

    private UploadConfig.CosConfig config;

    private static final String JOIN_STR = "-";

    private COSClient clint;

    public TencentFileServiceImpl(UploadConfig.CosConfig config) {
        this.config = config;
        // 1 初始化用户身份信息（secretId, secretKey）。
        COSCredentials cred = new BasicCOSCredentials(config.getSecretId(), config.getSecretKey());
        // 2 设置 bucket 的区域。
        Region region = new Region(config.getRegion());
        ClientConfig clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端。
        this.clint = new COSClient(cred, clientConfig);
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
        entity.setType(UploadFileEnum.ALI_COS.getConfig());
        if ("1".equals(entity.getIsPublic())) {
            entity.setPath(config.getPublicUrl()+key);
        }else {
            entity.setPath(key);
        }
        try {
            File targetFile = new File(key);
            FileUtils.copyInputStreamToFile(inputStream, targetFile);
            PutObjectRequest putObjectRequest = new PutObjectRequest(config.getBucketName().get(entity.getIsPublic()), key, targetFile);
            clint.putObject(putObjectRequest);
            FileUtils.deleteDirectory(targetFile);
            return entity;
        } catch (IOException e) {
            log.error("生成临时文件【{}】失败！",key);
            return null;
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
        COSObjectInputStream cosObjectInput = null;
        try {
            // 获取下载输入流
            GetObjectRequest getObjectRequest = new GetObjectRequest(config.getBucketName().get(entity.getIsPublic()), entity.getPath());
            COSObject cosObject = clint.getObject(getObjectRequest);
            cosObjectInput = cosObject.getObjectContent();
            return cosObjectInput;
        } finally {
            clint.shutdown();
        }
    }

    @Override
    public void downFileByUrl(String url,File file) {
        String key = url.replace(config.getPublicUrl(), "");
        try {
            GetObjectRequest getObjectRequest = new GetObjectRequest(config.getBucketName().get("1"), key);
            clint.getObject(getObjectRequest, file);
        } finally {
            clint.shutdown();
        }
    }
}
