package com.kakarote.crm.controller;

import com.kakarote.core.common.Result;
import com.kakarote.crm.service.ICrmAnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    private ICrmAnalysisService crmAnalysisService;

    @PostMapping("/customerStats")
    @ApiOperation("查询跟进方式列表")
    public Result<Boolean> customerStats() {
        boolean run = true;
        while (run) {
            run = !crmAnalysisService.saveCustomerStats();
        }
        return Result.ok(Boolean.TRUE);
    }

}
