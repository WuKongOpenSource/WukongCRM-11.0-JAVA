package com.kakarote.admin.service;

import com.kakarote.admin.entity.BO.AdminDeleteByBatchIdBO;
import com.kakarote.admin.entity.BO.RenameFileBO;
import com.kakarote.admin.entity.PO.AdminFile;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.servlet.upload.UploadEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 附件表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
public interface IAdminFileService extends BaseService<AdminFile> {

    /**
     * 上传文件
     * @param file 文件对象
     * @param batchId batchId
     * @param type type
     * @return entity
     */
    public UploadEntity upload(MultipartFile file,String batchId, String type,String isPublic) throws IOException;


    /**
     * 获取文件列表
     * @param batchId batchId
     * @return data
     */
    public List<FileEntity> queryFileList(String batchId);

    /**
     * 获取文件列表
     * @param batchIdList batchIdList
     * @return data
     */
    public List<FileEntity> queryFileList(List<String> batchIdList);

    /**
     * 通过文件ID查询
     * @param fileId 文件ID
     * @return data
     */
    public FileEntity queryById(Long fileId);

    /**
     * 获取单个文件
     * @param batchId batchId
     * @return data
     */
    public FileEntity queryOneByBatchId(String batchId);

    /**
     * 通过文件ID删除
     * @param fileId 文件ID
     * @return data
     */
    public void deleteById(Long fileId);

    /**
     * 通过batchId删除
     * @param batchId batchId
     * @return data
     */
    public void deleteByBatchId(List<String> batchId);

    /**
     * 下载文件
     * @param response response
     * @param fileId fileId
     */
    public void down(HttpServletResponse response,Long fileId);

    /**
     * 修改附件名称
     * @param renameFileBO
     */
    void renameFileById(RenameFileBO renameFileBO);

    /**
     * 查询文件数量
     * @param batchId batchId
     * @return num
     */
    public Integer queryNum(List<String> batchId);


    String copyJxcImg(String batchId);

    void saveBatchFileEntity(List<String> adminFileIdList, String batchId);

    List<FileEntity> queryByIds(Collection<Long> fileIds);

    void deleteByBatchId(AdminDeleteByBatchIdBO deleteByBatchIdBO);
}
