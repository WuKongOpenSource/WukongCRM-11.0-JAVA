package com.kakarote.core.feign.oa;

import com.kakarote.core.common.Result;
import com.kakarote.core.feign.oa.entity.ExamineVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "oa",contextId = "eventJob")
public interface OaService {
    @PostMapping("/oaEventJob/eventNoticeCron")
    Result eventNoticeCron();

    @PostMapping("/oaExamine/transfer")
    @ApiOperation("转换审批")
    public Result<List<ExamineVO>> transfer(@RequestBody List<ExamineVO> recordList);
}
