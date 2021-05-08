package com.kakarote.crm.controller;


import cn.hutool.core.util.StrUtil;
import com.kakarote.core.common.ApiExplain;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SubModelType;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.SysLog;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminConfig;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.common.log.CrmReturnVisitLog;
import com.kakarote.crm.entity.BO.CrmBusinessSaveBO;
import com.kakarote.crm.entity.BO.CrmSearchBO;
import com.kakarote.crm.entity.BO.CrmUpdateInformationBO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.service.ICrmReturnVisitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 回访
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-06
 */
@RestController
@RequestMapping("/crmReturnVisit")
@Api(tags = "回访模块")
@SysLog(subModel = SubModelType.CRM_RETURN_VISIT,logClass = CrmReturnVisitLog.class)
public class CrmReturnVisitController {

    @Autowired
    private ICrmReturnVisitService crmReturnVisitService;

    @PostMapping("/queryPageList")
    @ApiOperation("查询列表页数据")
    public Result<BasePage<Map<String, Object>>> queryPageList(@RequestBody CrmSearchBO search) {
        search.setPageType(1);
        BasePage<Map<String, Object>> mapBasePage = crmReturnVisitService.queryPageList(search);
        return R.ok(mapBasePage);
    }

    @PostMapping("/querySimpleEntity")
    @ApiExplain("查询简单的客户对象")
    public Result<List<SimpleCrmEntity>> querySimpleEntity(@RequestBody List<Integer> ids) {
        List<SimpleCrmEntity> crmEntities = crmReturnVisitService.querySimpleEntity(ids);
        return R.ok(crmEntities);
    }

    @PostMapping("/add")
    @ApiOperation("添加")
    @SysLogHandler(behavior = BehaviorEnum.SAVE,object = "#crmModel.entity[visitNumber]")
    public Result add(@RequestBody CrmBusinessSaveBO crmModel) {
        crmReturnVisitService.addOrUpdate(crmModel);
        return R.ok();
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    @SysLogHandler(behavior = BehaviorEnum.UPDATE)
    public Result update(@RequestBody CrmBusinessSaveBO crmModel) {
        crmReturnVisitService.addOrUpdate(crmModel);
        return R.ok();
    }


    @PostMapping("/queryById/{visitId}")
    @ApiOperation("根据ID查询")
    public Result<CrmModel> queryById(@PathVariable("visitId") @ApiParam(name = "id", value = "id") Integer visitId) {
        CrmModel model = crmReturnVisitService.queryById(visitId);
        return R.ok(model);
    }

    @PostMapping("/field")
    @ApiOperation("查询新增所需字段")
    public Result<List> queryReturnVisitField(@RequestParam(value = "type",required = false) String type) {
        if (StrUtil.isNotEmpty(type)) {
            return R.ok(crmReturnVisitService.queryField(null));
        }
        return R.ok(crmReturnVisitService.queryFormPositionField(null));
    }

    @PostMapping("/field/{id}")
    @ApiOperation("查询修改数据所需信息")
    public Result<List> queryField(@PathVariable("id") @ApiParam(name = "id", value = "id") Integer id,
                                   @RequestParam(value = "type",required = false) String type) {
        if (StrUtil.isNotEmpty(type)) {
            return R.ok(crmReturnVisitService.queryField(id));
        }
        return R.ok(crmReturnVisitService.queryFormPositionField(id));
    }
    /**
     * 查询详情页基本信息
     *
     * @param visitId id
     * @return data
     */
    @PostMapping("/information/{id}")
    @ApiOperation("查询详情页信息")
    public Result<List<CrmModelFiledVO>> information(@PathVariable("id") @ApiParam(name = "id", value = "id") Integer visitId) {
        List<CrmModelFiledVO> information = crmReturnVisitService.information(visitId);
        return R.ok(information);
    }



    @PostMapping("/queryFileList")
    @ApiOperation("查询附件列表")
    public Result<List<FileEntity>> queryFileList(@RequestParam("id") @ApiParam(name = "id", value = "id") Integer id) {
        List<FileEntity> fileEntities = crmReturnVisitService.queryFileList(id);
        return R.ok(fileEntities);
    }

    @PostMapping("/deleteByIds")
    @ApiOperation("删除回访数据")
    @SysLogHandler(behavior = BehaviorEnum.DELETE)
    public Result deleteByIds(@ApiParam(name = "ids", value = "id列表") @RequestBody List<Integer> ids) {
        crmReturnVisitService.deleteByIds(ids);
        return R.ok();
    }

    @PostMapping("/queryReturnVisitRemindConfig")
    @ApiOperation("查询客户回访提醒设置")
    public Result queryReturnVisitRemindConfig() {
        AdminConfig adminConfig = crmReturnVisitService.queryReturnVisitRemindConfig();
        return R.ok(adminConfig);
    }

    @PostMapping("/updateReturnVisitRemindConfig")
    @ApiOperation("修改客户回访提醒设置")
    @SysLogHandler(applicationName = "admin",subModel = SubModelType.ADMIN_CUSTOMER_MANAGEMENT,behavior = BehaviorEnum.UPDATE,object = "修改客户回访提醒设置",detail = "修改客户回访提醒设置")
    public Result updateReturnVisitRemindConfig(Integer value, @RequestParam("status") Integer status) {
        crmReturnVisitService.updateReturnVisitRemindConfig(status,value);
        return R.ok();
    }

    @PostMapping("/updateInformation")
    @ApiOperation("基本信息保存修改")
    @SysLogHandler(behavior = BehaviorEnum.UPDATE)
    public Result updateInformation(@RequestBody CrmUpdateInformationBO updateInformationBO) {
        crmReturnVisitService.updateInformation(updateInformationBO);
        return R.ok();
    }
}

