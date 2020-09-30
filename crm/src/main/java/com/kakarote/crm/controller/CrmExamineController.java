package com.kakarote.crm.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.crm.entity.BO.CrmMyExamineBO;
import com.kakarote.crm.entity.BO.CrmQueryExamineStepBO;
import com.kakarote.crm.entity.BO.CrmSaveExamineBO;
import com.kakarote.crm.entity.PO.CrmExamine;
import com.kakarote.crm.entity.VO.CrmQueryAllExamineVO;
import com.kakarote.crm.entity.VO.CrmQueryExamineStepVO;
import com.kakarote.crm.service.ICrmExamineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 审批流程表 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-28
 */
@RestController
@RequestMapping("/crmExamine")
@Api(tags = "审批模块")
public class CrmExamineController {

    @Autowired
    private ICrmExamineService examineService;


    @PostMapping("/saveExamine")
    @ApiOperation("添加审批流程")
    public Result saveExamine(@RequestBody CrmSaveExamineBO crmSaveExamineBO){
        examineService.saveExamine(crmSaveExamineBO);
        return Result.ok();
    }


    @PostMapping("/queryAllExamine")
    @ApiOperation("查询所有未删除审批流程")
    public Result<BasePage<CrmQueryAllExamineVO>> queryAllExamine(@RequestBody PageEntity pageEntity){
        BasePage<CrmQueryAllExamineVO> page = examineService.queryAllExamine(pageEntity);
        return Result.ok(page);
    }


    @PostMapping("/queryExamineById/{examineId}")
    @ApiOperation("根据id查询审批流程 examineId 审批流程id")
    public Result<CrmQueryAllExamineVO> queryExamineById(@PathVariable String examineId){
        CrmQueryAllExamineVO examineVO = examineService.queryExamineById(examineId);
        return Result.ok(examineVO);
    }

    /**
     * 停用或删除审批流程
     * examineId 审批流程id
     * status 审批状态 1启用 0禁用 2 删除
     */
    @PostMapping("/updateStatus")
    @ApiOperation("停用或删除审批流程")
    public Result updateStatus(@RequestBody CrmExamine crmExamine){
        examineService.updateStatus(crmExamine);
        return Result.ok();
    }

    /**
     * 查询当前启用审核流程步骤
     * categoryType 1 合同 2 回款
     */
    @PostMapping("/queryExamineStep")
    @ApiOperation("查询当前启用审核流程步骤")
    public Result<CrmQueryExamineStepVO> queryExamineStep(@RequestBody CrmQueryExamineStepBO queryExamineStepBO){
        CrmQueryExamineStepVO examineStepVO = examineService.queryExamineStep(queryExamineStepBO);
        return Result.ok(examineStepVO);
    }

    @PostMapping("/queryExamineStepIsExist")
    @ApiOperation("查询当前启用审核流程是否存在")
    public Result<Boolean> queryExamineStepIsExist(@RequestParam("categoryType") Integer categoryType){
        Boolean isExist = examineService.queryExamineStepByType(categoryType);
        return Result.ok(isExist);
    }

    @PostMapping("/myExamine")
    @ApiOperation("办公审批列表接口")
    public Result<BasePage<JSONObject>> myExamine(@RequestBody CrmMyExamineBO crmMyExamineBO){
        BasePage<JSONObject> page = examineService.myExamine(crmMyExamineBO);
        return Result.ok(page);
    }
}

