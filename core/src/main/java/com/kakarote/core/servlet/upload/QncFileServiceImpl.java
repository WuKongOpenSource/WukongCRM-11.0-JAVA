package com.kakarote.core.servlet.upload;

import cn.hutool.core.io.FileUtil;
import com.kakarote.core.utils.BaseUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author JiaS
 * @date 2020/10/16
 */
@Slf4j
public class QncFileServiceImpl implements FileService {

    private UploadConfig.QncConfig config;

    private static final String JOIN_STR = "-";

    private Auth clint;

    public QncFileServiceImpl(UploadConfig.QncConfig config) {
        this.config = config;
        this.clint = Auth.create(config.getAccessKey(), config.getSecretKey());
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
            //构造一个 Region 对象的配置类,自动判断区域
            Configuration cfg = new Configuration(Region.autoRegion());
            UploadManager uploadManager = new UploadManager(cfg);
            String upToken = clint.uploadToken(config.getBucketName().get(entity.getIsPublic()));
            uploadManager.put(inputStream,key,upToken,null, null);
            return entity;
        } catch (QiniuException e) {
            log.error("文件【{}】上传到七牛云失败！",key);
            log.error("失败的原因可能是：{}",e.response.toString());
            return null;
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
            Configuration cfg = new Configuration(Region.autoRegion());
            BucketManager bucketManager = new BucketManager(clint, cfg);
            bucketManager.delete(config.getBucketName().get(entity.getIsPublic()), key);
        } catch (QiniuException e) {
            log.error("七牛云文件【{}】删除失败！",key);
            log.error("失败的原因可能是：{}",e.response.toString());
        }
    }

    @Override
    public void deleteFileByUrl(String url) {
        String key = url.replace(config.getPublicUrl(), "");
        try {
            Configuration cfg = new Configuration(Region.autoRegion());
            BucketManager bucketManager = new BucketManager(clint, cfg);
            bucketManager.delete(config.getBucketName().get("1"), key);
        } catch (QiniuException e) {
            log.error("七牛云文件【{}】删除失败！",key);
            log.error("失败的原因可能是：{}",e.response.toString());
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
        try {
            Configuration cfg = new Configuration(Region.autoRegion());
            BucketManager bucketManager = new BucketManager(clint, cfg);
            bucketManager.move(config.getBucketName().get(entity.getIsPublic()), key + entity.getName(), config.getBucketName().get(entity.getIsPublic()), key + fileName);
        } catch (QiniuException e) {
            log.error("七牛云文件【{}】重命名失败！",key);
            log.error("失败的原因可能是：{}",e.response.toString());
        }
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
        InputStream inputStream = null;
        try {
            // 获取下载输入流
            String encodedFileName = URLEncoder.encode(entity.getPath(), "utf-8").replace("+", "%20");
            String finalUrl= String.format("%s/%s", config.getBucketName().get(entity.getIsPublic()), encodedFileName);
            if (!"1".equals(entity.getIsPublic())) {
                finalUrl = clint.privateDownloadUrl(finalUrl);
            }
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(finalUrl).build();
            okhttp3.Response resp = client.newCall(request).execute();
            if (resp.isSuccessful()) {
                ResponseBody body = resp.body();
                if (body != null){
                    inputStream = body.byteStream();
                }
            }
            return inputStream;
        } catch (UnsupportedEncodingException e) {
            log.error("文件【{}】进行URLEncoder编码失败！",entity.getPath());
        } catch (IOException e) {
            log.error("七牛云文件【{}】下载失败！",entity.getPath());
        }
        return null;
    }

    @Override
    public void downFileByUrl(String url,File file) {
        InputStream inputStream = null;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            okhttp3.Response resp = client.newCall(request).execute();
            if (resp.isSuccessful()) {
                ResponseBody body = resp.body();
                if (body != null){
                    inputStream = body.byteStream();
                }
            }
            FileUtil.writeFromStream(inputStream,file);
        } catch (UnsupportedEncodingException e) {
            log.error("文件【{}】进行URLEncoder编码失败！",e.getMessage());
        } catch (IOException e) {
            log.error("七牛云文件【{}】下载失败！",e.getMessage());
        }
    }
}
