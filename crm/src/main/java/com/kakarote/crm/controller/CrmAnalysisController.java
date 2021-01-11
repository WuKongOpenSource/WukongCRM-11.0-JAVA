package com.kakarote.crm.controller;

import com.kakarote.core.common.ApiExplain;
import com.kakarote.core.common.Result;
import com.kakarote.crm.service.ICrmCommonService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JiaS
 * @date 2020/9/16
 */
@RestController
@RequestMapping("/crmAnalysis")
@Api(tags = "crm分析")
public class CrmAnalysisController {

    @Autowired
    private ICrmCommonService crmCommonService;

    @PostMapping("/initCrmData")
    @ApiExplain("初始化crm数据")
    public Result<Boolean> initCrmData() {
        return Result.ok(crmCommonService.initCrmData());
    }

}
