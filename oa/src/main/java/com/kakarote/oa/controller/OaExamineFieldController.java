package com.kakarote.oa.controller;


import cn.hutool.core.util.StrUtil;
import com.kakarote.core.common.ApiExplain;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.oa.entity.BO.ExamineFieldBO;
import com.kakarote.oa.service.IOaExamineFieldService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 自定义字段表 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-22
 */
@RestController
@RequestMapping("/oaExamineField")
public class OaExamineFieldController {

    @Autowired
    private IOaExamineFieldService examineFieldService;

    /**
     * @author wyq
     * 查询新增或编辑字段
     */
    @ApiOperation("查询新增或编辑字段")
    @PostMapping("/queryField/{categoryId}")
    public Result<List> queryField(@PathVariable Integer categoryId,
                                                   @RequestParam(value = "type",required = false) String type){
        if (StrUtil.isNotEmpty(type)) {
            return R.ok(examineFieldService.queryField(categoryId));
        }
        return R.ok(examineFieldService.queryFormPositionField(categoryId));
    }

    @ApiOperation("保存自定义字段")
    @PostMapping("/saveField")
    public Result saveField(@RequestBody ExamineFieldBO examineFieldBO){
        examineFieldService.saveField(examineFieldBO);
        return Result.ok();
    }

    @ApiExplain("保存默认字段")
    @PostMapping("/saveDefaultField")
    public Result saveDefaultField(@RequestParam("categoryId") Long categoryId){
        examineFieldService.saveDefaultField(categoryId);
        return Result.ok();
    }

    @ApiOperation("修改自定义字段")
    @PostMapping("/updateFieldCategoryId")
    public Result<Boolean> updateFieldCategoryId(@RequestParam("newCategoryId") Long newCategoryId,@RequestParam("oldCategoryId") Long oldCategoryId){
        return Result.ok(examineFieldService.updateFieldCategoryId(newCategoryId,oldCategoryId));
    }


}

