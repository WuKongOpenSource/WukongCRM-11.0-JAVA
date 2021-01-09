package com.kakarote.core.servlet.upload;


import java.io.File;
import java.io.InputStream;

/**
 * @author z
 * 文件上传接口
 */
public interface FileService {

    /**
     * 上传文件
     * @param inputStream 文件流
     * @param entity 参数对象
     * @return result
     */
    public UploadEntity uploadFile(InputStream inputStream, UploadEntity entity);

    /**
     * 删除文件
     * @param entity 参数对象
     */
    public void deleteFile(UploadEntity entity);

    /**
     * 通过url删除文件(邮件需要)
     * @param url url
     */
    void deleteFileByUrl(String url);

    /**
     * 重命名文件
     * @param entity 参数对象
     * @param fileName 文件名称
     */
    public void renameFile(UploadEntity entity,String fileName);

    /**
     * 获取文件
     * @param entity 参数对象
     * @return 文件流，可能为空
     */
    public InputStream downFile(UploadEntity entity);

    void downFileByUrl(String url,File file);

}
