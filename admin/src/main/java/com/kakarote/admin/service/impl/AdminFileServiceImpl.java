package com.kakarote.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.kakarote.admin.entity.BO.AdminDeleteByBatchIdBO;
import com.kakarote.admin.entity.BO.RenameFileBO;
import com.kakarote.admin.entity.PO.AdminFile;
import com.kakarote.admin.mapper.AdminFileMapper;
import com.kakarote.admin.service.IAdminFileService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.servlet.upload.FileServiceFactory;
import com.kakarote.core.servlet.upload.UploadEntity;
import com.kakarote.core.utils.BaseUtil;
import com.kakarote.core.utils.UserCacheUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 附件表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
@Service
public class AdminFileServiceImpl extends BaseServiceImpl<AdminFileMapper, AdminFile> implements IAdminFileService {

    /**
     * 上传文件
     *
     * @param file    文件对象
     * @param batchId batchId
     * @param type    type
     * @return entity
     */
    @Override
    public UploadEntity upload(MultipartFile file, String batchId, String type, String isPublic) throws IOException {
        if (StrUtil.isEmpty(batchId)) {
            batchId = IdUtil.simpleUUID();
        }
        UploadEntity entity = new UploadEntity(BaseUtil.getNextId() + "", file.getOriginalFilename(), file.getSize(), batchId, isPublic);
        entity = FileServiceFactory.build().uploadFile(file.getInputStream(), entity);
        AdminFile adminFile = new AdminFile();
        adminFile.setFileId(Long.valueOf(entity.getFileId()));
        adminFile.setName(entity.getName());
        adminFile.setSize(entity.getSize());
        adminFile.setPath(entity.getPath());
        adminFile.setBatchId(batchId);
        if (StrUtil.isEmpty(type)) {
            type = "file";
        }
        adminFile.setFileType(type);
        adminFile.setType(entity.getType());
        adminFile.setIsPublic(Integer.valueOf(isPublic));
        save(adminFile);
        return entity;
    }

    /**
     * 获取文件列表
     *
     * @param batchId batchId
     * @return data
     */
    @Override
    public List<FileEntity> queryFileList(String batchId) {
        if (StrUtil.isEmpty(batchId)) {
            return new ArrayList<>();
        }
        List<AdminFile> list = lambdaQuery().eq(AdminFile::getBatchId, batchId).list();
        List<FileEntity> entityList = new ArrayList<>();
        parseFileEntity(list, entityList);
        return entityList;
    }

    /**
     * 获取文件列表
     *
     * @param batchIdList batchIdList
     * @return data
     */
    @Override
    public List<FileEntity> queryFileList(List<String> batchIdList) {
        if (batchIdList.size() == 0) {
            return new ArrayList<>();
        }
        List<AdminFile> list = lambdaQuery().in(AdminFile::getBatchId, batchIdList).list();
        List<FileEntity> entityList = new ArrayList<>();
        parseFileEntity(list, entityList);
        return entityList;
    }

    private void parseFileEntity(List<AdminFile> list, List<FileEntity> entityList) {
        list.forEach(adminFile -> {
            FileEntity entity = new FileEntity();
            entity.setIsPublic(adminFile.getIsPublic().toString());
            entity.setPath(adminFile.getPath());
            entity.setFileId(adminFile.getFileId().toString());
            entity.setName(adminFile.getName());
            entity.setCreateTime(adminFile.getCreateTime());
            entity.setCreateUserName(UserCacheUtil.getUserName(adminFile.getCreateUserId()));
            entity.setCreateUserId(adminFile.getCreateUserId());
            entity.setSize(adminFile.getSize());
            entity.setFileType(adminFile.getFileType());
            entity.setBatchId(adminFile.getBatchId());
            entityList.add(entity);
        });
    }

    /**
     * 通过文件ID查询
     *
     * @param fileId 文件ID
     * @return data
     */
    @Override
    public FileEntity queryById(Long fileId) {
        AdminFile adminFile = getById(fileId);
        FileEntity entity = new FileEntity();
        if (adminFile != null) {
            entity.setIsPublic(adminFile.getIsPublic().toString());
            entity.setPath(adminFile.getPath());
            entity.setFileId(adminFile.getFileId().toString());
            entity.setName(adminFile.getName());
            entity.setCreateTime(adminFile.getCreateTime());
            entity.setCreateUserName(UserCacheUtil.getUserName(adminFile.getCreateUserId()));
            entity.setSize(adminFile.getSize());
            entity.setBatchId(adminFile.getBatchId());
            entity.setFileType(adminFile.getFileType());
        }
        return entity;
    }

    /**
     * 获取单个文件
     *
     * @param batchId batchId
     * @return data
     */
    @Override
    public FileEntity queryOneByBatchId(String batchId) {
        FileEntity entity = new FileEntity();
        AdminFile adminFile = lambdaQuery().eq(AdminFile::getBatchId, batchId).last("limit 1").one();
        if (adminFile != null) {
            entity.setIsPublic(adminFile.getIsPublic().toString());
            entity.setPath(adminFile.getPath());
            entity.setFileId(adminFile.getFileId().toString());
            entity.setName(adminFile.getName());
            entity.setCreateTime(adminFile.getCreateTime());
            entity.setCreateUserName(UserCacheUtil.getUserName(adminFile.getCreateUserId()));
            entity.setSize(adminFile.getSize());
            entity.setFileType(adminFile.getFileType());
            entity.setBatchId(adminFile.getBatchId());
        }
        return entity;
    }

    /**
     * 通过文件ID删除
     *
     * @param fileId 文件ID
     * @return data
     */
    @Override
    public void deleteById(Long fileId) {
        AdminFile adminFile = getById(fileId);
        if (adminFile != null) {
            UploadEntity entity = new UploadEntity(adminFile.getFileId() + "", adminFile.getName(), adminFile.getSize(), adminFile.getBatchId(), adminFile.getIsPublic().toString());
            entity.setPath(adminFile.getPath());
            FileServiceFactory.build().deleteFile(entity);
            removeById(fileId);
        }
    }

    /**
     * 通过batchId删除
     *
     * @param batchId batchId
     * @return data
     */
    @Override
    public void deleteByBatchId(List<String> batchId) {
        if (batchId.size() == 0) {
            return;
        }
        List<AdminFile> fileList = lambdaQuery().select(AdminFile::getFileId).in(AdminFile::getBatchId, batchId).list();
        List<Long> fileIdList = fileList.stream().map(AdminFile::getFileId).collect(Collectors.toList());
        fileIdList.forEach(this::deleteById);
    }

    /**
     * 下载文件
     *
     * @param response response
     * @param fileId   fileId
     */
    @Override
    public void down(HttpServletResponse response, Long fileId) {
        AdminFile adminFile = getById(fileId);
        if (adminFile != null) {
            if (Objects.equals(1, adminFile.getType())) {
                ServletUtil.write(response, FileUtil.file(adminFile.getPath()));
                return;
            }
            UploadEntity entity = new UploadEntity(adminFile.getFileId() + "", adminFile.getName(), adminFile.getSize(), adminFile.getBatchId(), "0");
            entity.setPath(adminFile.getPath());
            InputStream inputStream = FileServiceFactory.build().downFile(entity);
            if (inputStream != null) {
                final String contentType = ObjectUtil.defaultIfNull(FileUtil.getMimeType(adminFile.getName()), "application/octet-stream");
                ServletUtil.write(response, inputStream, contentType, adminFile.getName());
            }
        }
    }

    @Override
    public void renameFileById(RenameFileBO renameFileBO) {
        lambdaUpdate().set(AdminFile::getName, renameFileBO.getName())
                .eq(AdminFile::getFileId, renameFileBO.getFileId()).update();
    }

    /**
     * 查询文件数量
     *
     * @param batchId batchId
     * @return num
     */
    @Override
    public Integer queryNum(List<String> batchId) {
        if (batchId.size() == 0) {
            return 0;
        }
        return lambdaQuery().in(AdminFile::getBatchId, batchId).count();
    }


    @Override
    public String copyJxcImg(String batchId) {
        if (StrUtil.isEmpty(batchId)) {
            return "";
        }
        List<AdminFile> fileList = lambdaQuery().eq(AdminFile::getBatchId, batchId).list();
        String newBatchId = IdUtil.simpleUUID();
        for (AdminFile adminFile : fileList) {
            adminFile.setFileId(BaseUtil.getNextId());
            adminFile.setBatchId(newBatchId);
        }
        saveBatch(fileList);
        return newBatchId;
    }

    @Override
    public void saveBatchFileEntity(List<String> adminFileIdList, String batchId) {
        List<AdminFile> fileList = lambdaQuery().in(AdminFile::getFileId, adminFileIdList).list();
        for (AdminFile adminFile : fileList) {
            adminFile.setFileId(BaseUtil.getNextId());
            adminFile.setBatchId(batchId);
        }
        saveBatch(fileList);
    }

    @Override
    public List<FileEntity> queryByIds(Collection<Long> fileIds) {
        if (CollUtil.isEmpty(fileIds)) {
            return new ArrayList<>();
        }
        List<AdminFile> fileList = lambdaQuery().in(AdminFile::getFileId, fileIds).list();
        return fileList.stream().map(adminFile -> {
            FileEntity entity = new FileEntity();
            if (adminFile != null) {
                entity.setIsPublic(adminFile.getIsPublic().toString());
                entity.setPath(adminFile.getPath());
                entity.setFileId(adminFile.getFileId().toString());
                entity.setName(adminFile.getName());
                entity.setCreateTime(adminFile.getCreateTime());
                entity.setCreateUserName(UserCacheUtil.getUserName(adminFile.getCreateUserId()));
                entity.setSize(adminFile.getSize());
                entity.setBatchId(adminFile.getBatchId());
                entity.setFileType(adminFile.getFileType());
            }
            return entity;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteByBatchId(AdminDeleteByBatchIdBO deleteByBatchIdBO) {
        Integer type = deleteByBatchIdBO.getType();
        String fileType = "";
        if (Objects.equals(1, type)) {
            fileType = "file";
        } else if (Objects.equals(2, type)) {
            fileType = "img";
        }
        List<AdminFile> fileList = lambdaQuery().select(AdminFile::getFileId).eq(AdminFile::getBatchId, deleteByBatchIdBO.getBatchId())
                .eq(StrUtil.isNotEmpty(fileType), AdminFile::getFileType, fileType).list();
        List<Long> fileIdList = fileList.stream().map(AdminFile::getFileId).collect(Collectors.toList());
        fileIdList.forEach(this::deleteById);
    }
}
