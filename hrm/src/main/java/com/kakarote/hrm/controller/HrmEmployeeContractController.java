package com.kakarote.hrm.controller;


import com.kakarote.core.common.Result;
import com.kakarote.hrm.entity.PO.HrmEmployeeContract;
import com.kakarote.hrm.entity.VO.ContractInformationVO;
import com.kakarote.hrm.service.IHrmEmployeeContractService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 员工合同 前端控制器
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@RestController
@RequestMapping("/hrmEmployeeContract")
@Api(tags = "员工管理-员工合同接口")
public class HrmEmployeeContractController {

    @Autowired
    private IHrmEmployeeContractService employeeContractService;
    /**
     * 合同信息
     */
    @PostMapping("/contractInformation/{employeeId}")
    @ApiOperation("合同基本信息")
    public Result<List<ContractInformationVO>> contractInformation(@PathVariable("employeeId") Integer employeeId){
        List<ContractInformationVO> contractInformationVOList = employeeContractService.contractInformation(employeeId);
        return Result.ok(contractInformationVOList);
    }

    @PostMapping("/addContract")
    @ApiOperation("/添加合同")
    public Result addContract(@RequestBody HrmEmployeeContract employeeContract){
        employeeContractService.addOrUpdateContract(employeeContract);
        return Result.ok();
    }

    @PostMapping("/setContract")
    @ApiOperation("/修改合同")
    public Result setContract(@RequestBody HrmEmployeeContract employeeContract){
        employeeContractService.addOrUpdateContract(employeeContract);
        return Result.ok();
    }

    @PostMapping("/deleteContract/{contractId}")
    @ApiOperation("删除合同")
    public Result deleteContract(@PathVariable Integer contractId){
        employeeContractService.deleteContract(contractId);
        return Result.ok();
    }
}

