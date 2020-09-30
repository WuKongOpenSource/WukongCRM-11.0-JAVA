package com.kakarote.work.controller;


import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.work.service.IWorkOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 项目排序表 前端控制器
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
@RestController
@RequestMapping("/workOrder")
@Api(tags = "项目排序")
public class WorkOrderController {
    @Autowired
    private IWorkOrderService workOrderService;

    @PostMapping("/updateWorkOrder")
    @ApiOperation("修改项目排序")
    public Result updateWorkOrder(@RequestBody List<Integer> workIdList){
        workOrderService.updateWorkOrder(workIdList);
        return R.ok();
    }
}

