package com.kakarote.hrm.controller;

import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.hrm.entity.BO.QueryRecruitPostPageListBO;
import com.kakarote.hrm.entity.BO.UpdateRecruitPostStatusBO;
import com.kakarote.hrm.entity.PO.HrmRecruitPost;
import com.kakarote.hrm.entity.VO.RecruitPostVO;
import com.kakarote.hrm.service.IHrmRecruitPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 招聘职位表 前端控制器
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@RestController
@RequestMapping("/hrmRecruitPost")
@Api(tags = "招聘管理-职位管理")
public class HrmRecruitPostController {

    @Autowired
    private IHrmRecruitPostService recruitPostService;

    @ApiOperation("添加职位")
    @PostMapping("/addRecruitPost")
    public Result addRecruitPost(@Validated @RequestBody HrmRecruitPost recruitPost){
        recruitPostService.addRecruitPost(recruitPost);
        return Result.ok();
    }

    @ApiOperation("修改职位")
    @PostMapping("/setRecruitPost")
    public Result setRecruitPost(@Validated @RequestBody HrmRecruitPost recruitPost){
        recruitPostService.setRecruitPost(recruitPost);
        return Result.ok();
    }

    /**
     * 获取职位详情
     * @param postId
     */
    @ApiOperation("获取职位详情")
    @PostMapping("/queryById/{postId}")
    public Result<RecruitPostVO> queryById(@PathVariable("postId") Integer postId){
        RecruitPostVO recruitPostVO = recruitPostService.queryById(postId);
        if (recruitPostVO.getRecruitNum() != null && recruitPostVO.getRecruitNum() != 0){
            recruitPostVO.setRecruitSchedule((recruitPostVO.getHasEntryNum()*100/recruitPostVO.getRecruitNum())+"");
        }
        return Result.ok(recruitPostVO);
    }

    @ApiOperation("查询职位列表")
    @PostMapping("/queryRecruitPostPageList")
    public Result<BasePage<RecruitPostVO>> queryRecruitPostPageList(@RequestBody QueryRecruitPostPageListBO queryRecruitPostPageListBO){
        BasePage<RecruitPostVO> page = recruitPostService.queryRecruitPostPageList(queryRecruitPostPageListBO);
        page.getList().forEach(recruitPostVO -> {
            if ( recruitPostVO.getRecruitNum() != null && recruitPostVO.getRecruitNum() != 0){
                recruitPostVO.setRecruitSchedule((recruitPostVO.getHasEntryNum()*100/recruitPostVO.getRecruitNum())+"");
            }
        });
        return Result.ok(page);
    }

    @ApiOperation("修改职位状态")
    @PostMapping("/updateRecruitPostStatus")
    public Result updateRecruitPostStatus(@RequestBody UpdateRecruitPostStatusBO updateRecruitPostStatusBO){
        recruitPostService.updateRecruitPostStatus(updateRecruitPostStatusBO);
        return Result.ok();
    }

    @PostMapping("/queryPostStatusNum")
    @ApiOperation("查询每个职位状态的数量")
    public Result<Map<Integer,Long>> queryPostStatusNum(){
        Map<Integer,Long> statusMap = recruitPostService.queryPostStatusNum();
        return Result.ok(statusMap);
    }

    @PostMapping("/queryAllRecruitPostList")
    @ApiOperation("查询所有职位列表(表单使用)")
    public Result<List<HrmRecruitPost>> queryAllRecruitPostList(){
        List<HrmRecruitPost> postList = recruitPostService.queryAllRecruitPostList();
        return Result.ok(postList);
    }
}

