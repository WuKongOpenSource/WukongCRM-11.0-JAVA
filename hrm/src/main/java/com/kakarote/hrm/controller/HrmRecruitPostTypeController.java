package com.kakarote.hrm.controller;

import com.kakarote.core.common.Result;
import com.kakarote.hrm.entity.PO.HrmRecruitPostType;
import com.kakarote.hrm.service.IHrmRecruitPostTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 职位类型 服务类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@RequestMapping("/hrmRecruitPostType")
@RestController
@Api(tags = "招聘管理-职位类型")
public class HrmRecruitPostTypeController {


    @Autowired
    private IHrmRecruitPostTypeService recruitPostTypeService;


    @PostMapping("/queryPostType")
    @ApiOperation("查询职位类型")
    public Result<List<HrmRecruitPostType>> queryPostType(){
        List<HrmRecruitPostType> list = recruitPostTypeService.queryPostType();
        return Result.ok(list);
    }

}
