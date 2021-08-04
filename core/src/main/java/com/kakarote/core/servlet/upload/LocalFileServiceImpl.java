package com.kakarote.core.servlet.upload;

import cn.hutool.core.io.FileUtil;
import com.kakarote.core.utils.BaseUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

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
            entity.setPath(config.getPublicUrl() + key);
        } else {
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
        String path = config.getUploadPath().get("1") + "/" + url.replace(config.getPublicUrl(), "");
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
        try {
            URL urlTmp = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlTmp.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //得到输入流
            InputStream inputStream = conn.getInputStream();
            //获取自己数组
            byte[] getData = readInputStream(inputStream);
            //文件保存位置
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(getData);
            fos.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}
