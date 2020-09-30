package com.kakarote.admin.controller;


import cn.hutool.core.util.StrUtil;
import com.kakarote.admin.entity.BO.AdminDeleteByBatchIdBO;
import com.kakarote.admin.entity.BO.RenameFileBO;
import com.kakarote.admin.service.IAdminFileService;
import com.kakarote.core.common.ApiExplain;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.servlet.LoginFromCookie;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.servlet.upload.UploadEntity;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 附件表 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
@RestController
@RequestMapping("/adminFile")
@Api(tags = "文件操作相关接口")
public class AdminFileController {

    @Autowired
    private IAdminFileService adminFileService;

    @PostMapping("/upload")
    @ApiOperation("上传文件")
    public Result<UploadEntity> upload(@RequestParam("file")
                                       @ApiParam("文件") MultipartFile file,
                                       @ApiParam("batchId") String batchId,
                                       @ApiParam("文件类型") String type,
                                       @RequestParam(value = "isPublic",required = false)String isPublic) throws IOException {
        if (StrUtil.isEmpty(isPublic)){
            isPublic = "0";
        }
        UploadEntity entity = adminFileService.upload(file, batchId, type,isPublic);
        return Result.ok(entity);
    }

    @RequestMapping(value = "/queryFileList/{batchId}", method = RequestMethod.POST)
    @ApiOperation(value = "通过批次ID查询文件列表", httpMethod = "POST")
    public Result<List<FileEntity>> queryFileList(@NotNull @PathVariable String batchId) {
        List<FileEntity> entityList = adminFileService.queryFileList(batchId);
        return Result.ok(entityList);
    }

    @RequestMapping(value = "/queryById/{fileId}", method = RequestMethod.POST)
    @ApiOperation(value = "通过ID查询文件", httpMethod = "POST")
    public Result<FileEntity> queryById(@NotNull @PathVariable @ApiParam("文件ID") Long fileId) {
        FileEntity fileEntity = adminFileService.queryById(fileId);
        return Result.ok(fileEntity);
    }

    @RequestMapping(value = "/queryByIds", method = RequestMethod.POST)
    @ApiOperation(value = "通过ID查询文件", httpMethod = "POST")
    public Result<List<FileEntity>> queryByIds(@RequestBody Collection<Long> fileIds) {
        List<FileEntity> fileEntitys = adminFileService.queryByIds(fileIds);
        return Result.ok(fileEntitys);
    }

    @RequestMapping(value = "/queryOneByBatchId/{batchId}", method = RequestMethod.POST)
    @ApiOperation(value = "通过批次ID查询单个文件", httpMethod = "POST")
    public Result<FileEntity> queryOneByBatchId(@NotNull @PathVariable @ApiParam("batchId") String batchId) {
        FileEntity fileEntity = adminFileService.queryOneByBatchId(batchId);
        return Result.ok(fileEntity);
    }

    @RequestMapping(value = "/deleteById/{fileId}", method = RequestMethod.POST)
    @ApiOperation(value = "通过ID删除文件", httpMethod = "POST")
    public Result deleteById(@NotNull @PathVariable @ApiParam("文件ID") Object fileId) {
        if (fileId instanceof Number) {
            adminFileService.deleteById(((Number) fileId).longValue());
        } else if (fileId instanceof String) {
            if(((String) fileId).length() == 19){
                adminFileService.deleteById(Long.valueOf(fileId.toString()));
            }else {
                AdminDeleteByBatchIdBO bo = new AdminDeleteByBatchIdBO();
                bo.setBatchId((String) fileId);
                adminFileService.deleteByBatchId(bo);
            }
        }

        return Result.ok();
    }

    @RequestMapping(value = "/deleteByBatchId", method = RequestMethod.POST)
    @ApiOperation(value = "通过批次ID和文件类型删除文件", httpMethod = "POST")
    public Result deleteByBatchId(@RequestBody AdminDeleteByBatchIdBO deleteByBatchIdBO) {
        adminFileService.deleteByBatchId(deleteByBatchIdBO);
        return Result.ok();
    }

    @RequestMapping(value = "/deleteByBatchIds", method = RequestMethod.POST)
    @ApiOperation(value = "通过批次ID删除文件", httpMethod = "POST")
    public Result deleteByBatchId(@RequestBody @ApiParam("batchId") List<String> batchId) {
        adminFileService.deleteByBatchId(batchId);
        return Result.ok();
    }

    @RequestMapping(value = "/down/{fileId}")
    @ApiOperation(value = "下载文件接口", httpMethod = "POST")
    @LoginFromCookie
    public void down(@PathVariable("fileId") @ApiParam("文件ID") Long fileId, HttpServletResponse response) {
        if (UserUtil.getUser() == null) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
        }
        adminFileService.down(response, fileId);
    }

    @PostMapping(value = "/renameFileById")
    @ApiOperation(value = "修改附件名称", httpMethod = "POST")
    public Result renameFileById(@RequestBody RenameFileBO renameFileBO) {
        adminFileService.renameFileById(renameFileBO);
        return Result.ok();
    }

    @PostMapping(value = "/queryNum")
    @ApiExplain("查询文件数量")
    public Result<Integer> queryNum(@RequestBody List<String> batchId) {
        Integer num = adminFileService.queryNum(batchId);
        return Result.ok(num);
    }

    @PostMapping(value = "/queryFileList")
    @ApiExplain("查询文件")
    public Result<List<FileEntity>> queryFileList(@RequestBody List<String> batchIdList) {
        List<FileEntity> fileEntities = adminFileService.queryFileList(batchIdList);
        return Result.ok(fileEntities);
    }


    @PostMapping(value = "/copyJxcImg")
    @ApiExplain("copy进销存产品详情图")
    public Result<String> copyJxcImg(@RequestParam(value = "batchId") String batchId) {
        String newBatchId = adminFileService.copyJxcImg(batchId);
        return Result.ok(newBatchId);
    }

    @PostMapping(value = "/saveBatchFileEntity")
    @ApiExplain("批量保存附件(查询附件id,修改batchId)")
    public Result saveBatchFileEntity(@RequestParam(value = "adminFileIdList") List<String> adminFileIdList,
                                      @RequestParam(value = "batchId") String batchId) {
        adminFileService.saveBatchFileEntity(adminFileIdList, batchId);
        return Result.ok();
    }
}

