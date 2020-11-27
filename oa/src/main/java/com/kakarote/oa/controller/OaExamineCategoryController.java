package com.kakarote.oa.controller;


import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.oa.entity.BO.SetExamineCategoryBO;
import com.kakarote.oa.entity.BO.UpdateCategoryStatus;
import com.kakarote.oa.entity.PO.OaExamineCategory;
import com.kakarote.oa.entity.PO.OaExamineSort;
import com.kakarote.oa.entity.VO.OaExamineCategoryVO;
import com.kakarote.oa.service.IOaExamineCategoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 审批类型表 前端控制器
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
@RestController
@RequestMapping("/oaExamineCategory")
public class OaExamineCategoryController {

    @Autowired
    private IOaExamineCategoryService examineCategoryService;


    @ApiOperation("设置审批类型")
    @PostMapping("/setExamineCategory")
    public Result<Map<String,Integer>> setExamineCategory(@RequestBody SetExamineCategoryBO setExamineCategoryBO){
        Map<String, Integer> map = examineCategoryService.setExamineCategory(setExamineCategoryBO);
        return Result.ok(map);
    }

    @ApiOperation("查询审批类型列表")
    @PostMapping("/queryExamineCategoryList")
    public Result<BasePage<OaExamineCategoryVO>> queryExamineCategoryList(PageEntity pageEntity) {
        BasePage<OaExamineCategoryVO> page = examineCategoryService.queryExamineCategoryList(pageEntity);
        return Result.ok(page);
    }


    @ApiOperation("查询审批类型列表")
    @PostMapping("/queryAllExamineCategoryList")
    public Result<List<OaExamineCategory>> queryAllExamineCategoryList() {
        List<OaExamineCategory> examineCategoryList = examineCategoryService.queryAllExamineCategoryList();
        return Result.ok(examineCategoryList);
    }

    @ApiOperation("自定义审批类型排序")
    @PostMapping("/saveOrUpdateOaExamineSort")
    public Result saveOrUpdateOaExamineSort(@RequestBody List<OaExamineSort> oaExamineSortList) {
        examineCategoryService.saveOrUpdateOaExamineSort(oaExamineSortList);
        return Result.ok();
    }


    @ApiOperation("删除审批类型")
    @PostMapping("/deleteExamineCategory")
    public Result deleteExamineCategory(@RequestParam("id") Integer id) {
        examineCategoryService.deleteExamineCategory(id);
        return Result.ok();
    }

    /**
     * 启用/禁用
     */
    @ApiOperation("删除审批类型")
    @PostMapping("/updateStatus")
    public Result updateStatus(@RequestBody UpdateCategoryStatus updateCategoryStatus) {
        examineCategoryService.updateStatus(updateCategoryStatus);
        return Result.ok();
    }


}

