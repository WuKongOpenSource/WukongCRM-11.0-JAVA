package com.kakarote.hrm.controller;

import com.kakarote.core.common.Result;
import com.kakarote.hrm.entity.BO.SendWriteArchivesBO;
import com.kakarote.hrm.entity.VO.EmployeeArchivesFieldVO;
import com.kakarote.hrm.entity.VO.PersonalInformationVO;
import com.kakarote.hrm.entity.VO.PostInformationVO;
import com.kakarote.hrm.service.IHrmEmployeeFieldService;
import com.kakarote.hrm.service.IHrmEmployeePostService;
import com.kakarote.hrm.service.IHrmEmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hrmEmployeeArchives")
@Api(tags = "员工档案")
@Slf4j
public class HrmEmployeeArchivesController {

    @Autowired
    private IHrmEmployeeFieldService employeeFieldService;

    @Autowired
    private IHrmEmployeePostService employeePostService;

    @Autowired
    private IHrmEmployeeService employeeService;


    @PostMapping("/queryEmployeeArchivesField")
    @ApiOperation("查询员工档案设置字段列表")
    public Result<List<EmployeeArchivesFieldVO>> queryEmployeeArchivesField(){
        List<EmployeeArchivesFieldVO> archivesFields = employeeFieldService.queryEmployeeArchivesField();
        return Result.ok(archivesFields);
    }
    @PostMapping("/setEmployeeArchivesField")
    @ApiOperation("修改员工档案字段")
    public Result setEmployeeArchivesField(@RequestBody List<EmployeeArchivesFieldVO> archivesFields){
        employeeFieldService.setEmployeeArchivesField(archivesFields);
        return Result.ok();
    }

    @PostMapping("/sendWriteArchives")
    @ApiOperation("发送填写档案信息")
    public Result sendWriteArchives(@RequestBody SendWriteArchivesBO writeArchivesBO){
        employeeFieldService.sendWriteArchives(writeArchivesBO);
        return Result.ok();
    }

    @PostMapping("/postArchives")
    @ApiOperation("岗位档案信息")
    public Result<PostInformationVO>  postArchives(){
        PostInformationVO information = employeePostService.postArchives();
        return Result.ok(information);
    }

    @PostMapping("/personalArchives")
    @ApiOperation("个人档案信息")
    public Result<PersonalInformationVO> personalArchives() {
        PersonalInformationVO personalInformationVO = employeeService.personalArchives();
        return Result.ok(personalInformationVO);
    }
}
