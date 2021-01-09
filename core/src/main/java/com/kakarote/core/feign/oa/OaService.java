package com.kakarote.core.feign.oa;

import com.kakarote.core.common.Result;
import com.kakarote.core.feign.crm.entity.ExamineField;
import com.kakarote.core.feign.examine.entity.ExamineConditionDataBO;
import com.kakarote.core.feign.oa.entity.ExamineVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "oa",contextId = "eventJob")
public interface OaService {

    @PostMapping("/oaEventJob/eventNoticeCron")
    Result eventNoticeCron();

    @PostMapping("/oaExamine/transfer")
    @ApiOperation("转换审批")
    Result<List<ExamineVO>> transfer(@RequestBody List<ExamineVO> recordList);

    @PostMapping("/oaLog/initOaData")
    Result<Boolean> initOaData();

    @PostMapping("/oaLog/initCalendarData")
    Result<Boolean> initCalendarData();

    @PostMapping("/oaLog/initOaExamineData")
    Result<Boolean> initOaExamineData();

    @PostMapping("/oaExamine/queryExamineField")
    Result<List<ExamineField>> queryExamineField(@RequestParam("categoryId") Integer categoryId);

    @PostMapping("/oaExamine/queryConditionData")
    Result<Map<String, Object>> getDataMapForNewExamine(@RequestBody ExamineConditionDataBO examineConditionDataBO);

    @PostMapping("/oaExamine/updateCheckStatusByNewExamine")
    Result<Boolean> updateCheckStatusByNewExamine(@RequestBody ExamineConditionDataBO examineConditionDataBO);

    @PostMapping("/oaExamine/getOaExamineById")
    Result<ExamineVO> getOaExamineById(@RequestParam("oaExamineId") Integer oaExamineId);

    @PostMapping("/oaExamineField/saveDefaultField")
    Result saveDefaultField(@RequestParam("categoryId") Long categoryId);

    @PostMapping("/oaExamineField/updateFieldCategoryId")
    Result<Boolean> updateFieldCategoryId(@RequestParam("newCategoryId") Long newCategoryId,@RequestParam("oldCategoryId") Long oldCategoryId);
}
