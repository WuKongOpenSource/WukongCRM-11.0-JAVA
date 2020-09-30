package com.kakarote.core.feign.admin.service;

import com.kakarote.core.common.ApiExplain;
import com.kakarote.core.common.Result;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.servlet.upload.UploadEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

/**
 * @author zhangzhiwei
 * 文件操作相关接口
 */
@FeignClient(name = "admin", contextId = "file")
@Component
public interface AdminFileService {
    /**
     * 根据batchId查询文件列表
     *
     * @param batchId batchId
     * @return data
     */
    @RequestMapping(value = "/adminFile/queryFileList/{batchId}", method = RequestMethod.POST)
    public Result<List<FileEntity>> queryFileList(@PathVariable("batchId") String batchId);

    /**
     * 根据fileId查询文件
     *
     * @param fileId 文件ID
     * @return data
     */
    @RequestMapping(value = "/adminFile/queryById/{fileId}", method = RequestMethod.POST)
    public Result<FileEntity> queryById(@PathVariable("fileId") Long fileId);

    @RequestMapping(value = "/adminFile/queryByIds", method = RequestMethod.POST)
    public Result<List<FileEntity>> queryByIds(@RequestBody Collection<Long> fileIds);

    /**
     * 根据batchId查询单个文件
     *
     * @param batchId batchId
     * @return data
     */
    @RequestMapping(value = "/adminFile/queryOneByBatchId/{batchId}", method = RequestMethod.POST)
    public Result<FileEntity> queryOne(@PathVariable("batchId") String batchId);

    /**
     * 根据fileId删除文件
     *
     * @param fileId 文件ID
     * @return data
     */
    @RequestMapping(value = "/adminFile/deleteById/{fileId}", method = RequestMethod.POST)
    public Result delete(@PathVariable("fileId") Object fileId);

    /**
     * 根据batchId删除文件
     *
     * @param batchId batchId
     * @return data
     */
    @RequestMapping(value = "/adminFile/deleteByBatchIds", method = RequestMethod.POST)
    public Result delete(@RequestBody List<String> batchId);

    @PostMapping(value = "/adminFile/queryNum")
    public Result<Integer> queryNum(@RequestBody List<String> batchId);

    @PostMapping(value = "/adminFile/queryFileList")
    public Result<List<FileEntity>> queryFileList(@RequestBody List<String> batchIdList);

    @PostMapping(value = "/adminFile/copyJxcImg")
    @ApiExplain("copy进销存产品详情图")
    Result<String> copyJxcImg(@RequestParam(value = "batchId") String batchId);


    @PostMapping(value = "/adminFile/saveBatchFileEntity")
    @ApiExplain("批量保存附件(查询附件id,修改batchId)")
    void saveBatchFileEntity(@RequestParam(value = "adminFileIdList") List<String> adminFileIdList,
                             @RequestParam(value = "batchId") String batchId);

    @PostMapping(value = "/adminFile/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiExplain("上传文件")
    public Result<UploadEntity> upload(@RequestPart("file") MultipartFile file,
                                       @RequestParam("batchId") String batchId,
                                       @RequestParam("type") String type);
}
