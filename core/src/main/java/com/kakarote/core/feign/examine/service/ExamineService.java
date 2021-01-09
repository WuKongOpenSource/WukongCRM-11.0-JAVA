package com.kakarote.core.feign.examine.service;

import com.kakarote.core.common.Result;
import com.kakarote.core.feign.examine.entity.ExamineInfoVo;
import com.kakarote.core.feign.examine.entity.ExamineRecordLog;
import com.kakarote.core.feign.examine.entity.ExamineRecordReturnVO;
import com.kakarote.core.feign.examine.entity.ExamineRecordSaveBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author JiaS
 * @date 2020/12/17
 */
@FeignClient(name = "examine",contextId = "record")
public interface ExamineService {

    @PostMapping("/examineRecord/addExamineRecord")
    public Result<ExamineRecordReturnVO> addExamineRecord(@RequestBody ExamineRecordSaveBO examineRecordSaveBO);

    @PostMapping("/examines/queryNormalExamine")
    public Result<List<ExamineInfoVo>> queryNormalExamine(@RequestParam("label") Integer label);

    @PostMapping("/examineRecord/queryExamineLogById")
    public Result<ExamineRecordLog> queryExamineLogById(@RequestParam("examineLogId") Integer examineLogId);

    @PostMapping("/examineRecord/queryExamineRecordInfo")
    public Result<ExamineRecordReturnVO> queryExamineRecordInfo(@RequestParam("recordId") Integer recordId);

    @PostMapping("/examines/queryExamineById")
    public Result<ExamineInfoVo> queryExamineById(@RequestParam("examineId") Long examineId);

    @PostMapping("/examineRecord/deleteExamineRecord")
    public Result<Boolean> deleteExamineRecord(@RequestParam("recordId") Integer recordId);

    @PostMapping("/examineRecord/updateExamineRecord")
    public Result<Boolean> updateExamineRecord(@RequestParam("recordId") Integer recordId,@RequestParam("examineStatus") Integer examineStatus);

    @PostMapping("/examineWaiting/queryOaExamineIdList")
    public Result<List<Integer>> queryOaExamineIdList(@RequestParam("status")Integer status,@RequestParam("categoryId")Integer categoryId);

    @PostMapping("/examineWaiting/queryCrmExamineIdList")
    public Result<List<Integer>> queryCrmExamineIdList(@RequestParam("label")Integer label,@RequestParam("status")Integer status);

    @PostMapping("/examineRecord/deleteExamineRecordAndLog")
    public Result<Boolean> deleteExamineRecordAndLog(@RequestParam("label") Integer label);

}
