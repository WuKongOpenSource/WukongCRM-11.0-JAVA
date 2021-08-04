package com.kakarote.hrm.controller;


import com.kakarote.core.common.Result;
import com.kakarote.hrm.entity.BO.DeleteLeaveInformationBO;
import com.kakarote.hrm.entity.BO.UpdateInformationBO;
import com.kakarote.hrm.entity.PO.HrmEmployeeQuitInfo;
import com.kakarote.hrm.entity.VO.PostInformationVO;
import com.kakarote.hrm.service.IHrmEmployeePostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 员工岗位 前端控制器
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@RestController
@RequestMapping("/hrmEmployeePost")
@Api(tags = "员工管理-员工岗位接口")
public class HrmEmployeePostController {

    @Autowired
    private IHrmEmployeePostService employeePostService;

    @PostMapping("/postInformation/{employeeId}")
    @ApiOperation("岗位信息")
    public Result<PostInformationVO> postInformation(@PathVariable("employeeId") Integer employeeId){
        PostInformationVO postInformationVO = employeePostService.postInformation(employeeId);
        return Result.ok(postInformationVO);
    }

    @PostMapping("/updatePostInformation")
    @ApiOperation("修改岗位信息")
    public Result updatePostInformation(@RequestBody UpdateInformationBO updateInformationBO){
        employeePostService.updatePostInformation(updateInformationBO);
        return Result.ok();
    }



    @PostMapping("/addLeaveInformation")
    @ApiOperation("办理离职")
    public Result  addLeaveInformation(@RequestBody HrmEmployeeQuitInfo quitInfo){
        employeePostService.addOrUpdateLeaveInformation(quitInfo);
        return Result.ok();
    }

    @PostMapping("/setLeaveInformation")
    @ApiOperation("修改离职信息")
    public Result  setLeaveInformation(@RequestBody HrmEmployeeQuitInfo quitInfo){
        employeePostService.addOrUpdateLeaveInformation(quitInfo);
        return Result.ok();
    }

    @PostMapping("/deleteLeaveInformation")
    @ApiOperation("取消离职")
    public Result  deleteLeaveInformation(@RequestBody DeleteLeaveInformationBO deleteLeaveInformationBO){
        employeePostService.deleteLeaveInformation(deleteLeaveInformationBO);
        return Result.ok();
    }

}

