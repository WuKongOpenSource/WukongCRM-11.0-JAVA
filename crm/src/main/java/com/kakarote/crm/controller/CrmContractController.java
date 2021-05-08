package com.kakarote.crm.controller;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.*;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.SysLog;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminConfig;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.feign.crm.entity.BiParams;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.crm.common.AuthUtil;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.common.log.CrmContractLog;
import com.kakarote.crm.constant.CrmAuthEnum;
import com.kakarote.crm.constant.CrmCodeEnum;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.*;
import com.kakarote.crm.entity.PO.CrmContract;
import com.kakarote.crm.entity.PO.CrmReceivablesPlan;
import com.kakarote.crm.entity.VO.CrmInfoNumVO;
import com.kakarote.crm.entity.VO.CrmMembersSelectVO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.service.ICrmBackLogDealService;
import com.kakarote.crm.service.ICrmContractService;
import com.kakarote.crm.service.ICrmTeamMembersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 合同表 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
@RestController
@RequestMapping("/crmContract")
@Api(tags = "合同模块接口")
@SysLog(subModel = SubModelType.CRM_CONTRACT, logClass = CrmContractLog.class)
public class CrmContractController {

    @Autowired
    private ICrmContractService crmContractService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ICrmTeamMembersService teamMembersService;

    @PostMapping("/queryPageList")
    @ApiOperation("查询列表页数据")
    public Result<BasePage<Map<String, Object>>> queryPageList(@RequestBody CrmSearchBO search) {
        search.setPageType(1);
        BasePage<Map<String, Object>> mapBasePage = crmContractService.queryPageList(search);
        return R.ok(mapBasePage);
    }


    @PostMapping("/queryById/{contractId}")
    @ApiOperation("根据ID查询")
    public Result<CrmModel> queryById(@PathVariable("contractId") @ApiParam(name = "id", value = "id") Integer contractId) {
        Integer number = crmContractService.lambdaQuery().eq(CrmContract::getContractId, contractId).ne(CrmContract::getCheckStatus, 7).count();
        if (number == 0) {
            throw new CrmException(CrmCodeEnum.CRM_DATA_DELETED, "合同");
        }
        CrmModel model = crmContractService.queryById(contractId);
        return R.ok(model);
    }


    @PostMapping("/deleteByIds")
    @ApiOperation("根据ID删除数据")
    @SysLogHandler(behavior = BehaviorEnum.DELETE)
    public Result deleteByIds(@ApiParam(name = "ids", value = "id列表") @RequestBody List<Integer> ids) {
        crmContractService.deleteByIds(ids);
        return R.ok();
    }

    @PostMapping("/field")
    @ApiOperation("查询新增所需字段")
    public Result<List> queryContractField(@RequestParam(value = "type",required = false) String type) {
        if (StrUtil.isNotEmpty(type)) {
            return R.ok(crmContractService.queryField(null));
        }
        return R.ok(crmContractService.queryFormPositionField(null));
    }

    @PostMapping("/field/{id}")
    @ApiOperation("查询修改数据所需信息")
    public Result<List> queryField(@PathVariable("id") @ApiParam(name = "id", value = "id") Integer id,
                                   @RequestParam(value = "type",required = false) String type) {
        if (StrUtil.isNotEmpty(type)) {
            return R.ok(crmContractService.queryField(id));
        }
        return R.ok(crmContractService.queryFormPositionField(id));
    }

    @PostMapping("/changeOwnerUser")
    @ApiOperation("修改合同负责人")
    @SysLogHandler(behavior = BehaviorEnum.CHANGE_OWNER)
    public Result changeOwnerUser(@RequestBody CrmChangeOwnerUserBO crmChangeOwnerUserBO) {
        crmContractService.changeOwnerUser(crmChangeOwnerUserBO);
        return R.ok();
    }

    @PostMapping("/add")
    @ApiOperation("保存数据")
    @SysLogHandler(behavior = BehaviorEnum.SAVE, object = "#crmModel.entity[name]", detail = "'新增了合同:' + #crmModel.entity[name]")
    public Result add(@RequestBody CrmContractSaveBO crmModel) {
        crmContractService.addOrUpdate(crmModel);
        return R.ok();
    }

    @PostMapping("/update")
    @ApiOperation("修改数据")
    @SysLogHandler(behavior = BehaviorEnum.UPDATE)
    public Result update(@RequestBody CrmContractSaveBO crmModel) {
        crmContractService.addOrUpdate(crmModel);
        return R.ok();
    }


    @PostMapping("/getMembers/{contractId}")
    @ApiOperation("获取团队成员")
    public Result<List<CrmMembersSelectVO>> getMembers(@PathVariable("contractId") @ApiParam("合同ID") Integer contractId) {
        CrmEnum crmEnum = CrmEnum.CONTRACT;
        CrmContract contract = crmContractService.getById(contractId);
        if (contract == null) {
            throw new CrmException(CrmCodeEnum.CRM_DATA_DELETED, crmEnum.getRemarks());
        }
        List<CrmMembersSelectVO> members = teamMembersService.getMembers(crmEnum,contractId,contract.getOwnerUserId());
        return R.ok(members);
    }

    @PostMapping("/addMembers")
    @ApiOperation("新增团队成员")
    @SysLogHandler(behavior = BehaviorEnum.ADD_MEMBER)
    public Result addMembers(@RequestBody CrmMemberSaveBO crmMemberSaveBO) {
        teamMembersService.addMember(CrmEnum.CONTRACT,crmMemberSaveBO);
        return R.ok();
    }

    @PostMapping("/updateMembers")
    @ApiOperation("新增团队成员")
    @SysLogHandler(behavior = BehaviorEnum.ADD_MEMBER)
    public Result updateMembers(@RequestBody CrmMemberSaveBO crmMemberSaveBO) {
        teamMembersService.addMember(CrmEnum.CONTRACT,crmMemberSaveBO);
        return R.ok();
    }

    @PostMapping("/deleteMembers")
    @ApiOperation("删除团队成员")
    @SysLogHandler
    public Result deleteMembers(@RequestBody CrmMemberSaveBO crmMemberSaveBO) {
        teamMembersService.deleteMember(CrmEnum.CONTRACT,crmMemberSaveBO);
        return R.ok();
    }

    @PostMapping("/exitTeam/{contractId}")
    @ApiOperation("退出团队")
    @SysLogHandler
    public Result exitTeam(@PathVariable("contractId") @ApiParam("合同ID") Integer contractId) {
        teamMembersService.exitTeam(CrmEnum.CONTRACT,contractId);
        return R.ok();
    }

    @PostMapping("/qureyReceivablesListByContractId")
    @ApiOperation("查询回款列表")
    public Result<BasePage<JSONObject>> queryReceivablesListByContractId(@RequestBody CrmRelationPageBO crmRelationPageBO) {
        boolean auth = AuthUtil.isCrmAuth(CrmEnum.CONTRACT, crmRelationPageBO.getContractId(),CrmAuthEnum.LIST);
        if (auth) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
        }
        BasePage<JSONObject> jsonObjects = crmContractService.queryListByContractId(crmRelationPageBO);
        return R.ok(jsonObjects);
    }

    @PostMapping("/queryProductListByContractId")
    @ApiOperation("查询合同下产品")
    public Result<JSONObject> queryProductListByContractId(@RequestBody CrmRelationPageBO crmRelationPageBO) {
        boolean auth = AuthUtil.isCrmAuth(CrmEnum.CONTRACT, crmRelationPageBO.getContractId(),CrmAuthEnum.READ);
        if (auth) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
        }
        JSONObject page = crmContractService.queryProductListByContractId(crmRelationPageBO);
        return R.ok(page);
    }


    @PostMapping("/queryReturnVisit")
    @ApiOperation("查询合同下产品")
    public Result<BasePage<JSONObject>> queryReturnVisit(@RequestBody CrmRelationPageBO crmRelationPageBO) {
        boolean auth = AuthUtil.isCrmAuth(CrmEnum.CONTRACT, crmRelationPageBO.getContractId(),CrmAuthEnum.READ);
        if (auth) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
        }
        BasePage<JSONObject> page = crmContractService.queryReturnVisit(crmRelationPageBO);
        return R.ok(page);
    }

    @PostMapping("/queryReceivablesPlanListByContractId")
    @ApiOperation("查询合同下回款计划")
    public Result<BasePage<CrmReceivablesPlan>> queryReceivablesPlanListByContractId(@RequestBody CrmRelationPageBO crmRelationPageBO) {
        boolean auth = AuthUtil.isCrmAuth(CrmEnum.CONTRACT, crmRelationPageBO.getContractId(),CrmAuthEnum.READ);
        if (auth) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
        }
        BasePage<CrmReceivablesPlan> receivablesPlanList = crmContractService.queryReceivablesPlanListByContractId(crmRelationPageBO);
        return R.ok(receivablesPlanList);
    }

    @PostMapping("/queryReceivablesPlansByContractId")
    @ApiOperation("查询合同下回款计划")
    public Result<List<CrmReceivablesPlan>> queryReceivablesPlansByContractId(@RequestParam("contractId") Integer contractId, @RequestParam(value = "receivablesId", required = false) Integer receivablesId) {
        List<CrmReceivablesPlan> receivablesPlanList = crmContractService.queryReceivablesPlansByContractId(contractId, receivablesId);
        return R.ok(receivablesPlanList);
    }

    @ApiOperation(value = "查询合同到期提醒设置")
    @PostMapping("/queryContractConfig")
    public Result<AdminConfig> queryContractConfig() {
        AdminConfig config = ApplicationContextHolder.getBean(AdminService.class).queryFirstConfigByName("expiringContractDays").getData();
        if (config == null) {
            config = new AdminConfig();
            config.setStatus(0);
            config.setName("expiringContractDays");
            config.setValue("3");
            config.setDescription("合同到期提醒");
        }
        return R.ok(config);
    }

    @ApiOperation(value = "设置合同到期提醒设置")
    @PostMapping("/setContractConfig")
    @SysLogHandler(applicationName = "admin", subModel = SubModelType.ADMIN_CUSTOMER_MANAGEMENT, behavior = BehaviorEnum.UPDATE, object = "设置合同到期提醒设置", detail = "设置合同到期提醒设置")
    public Result setContractConfig(@RequestParam("status") Integer status, @RequestParam(value = "contractDay", required = false, defaultValue = "0") Integer contractDay) {
        if (status == 1 && contractDay == null) {
            return R.error(CrmCodeEnum.CRM_CONTRACT_CONFIG_ERROR);
        }
        AdminConfig adminConfig = adminService.queryFirstConfigByName("expiringContractDays").getData();
        if (adminConfig == null) {
            adminConfig = new AdminConfig();
        }
        adminConfig.setStatus(status);
        adminConfig.setName("expiringContractDays");
        adminConfig.setValue(contractDay.toString());
        adminConfig.setDescription("合同到期提醒");
        adminService.updateAdminConfig(adminConfig);
        ApplicationContextHolder.getBean(ICrmBackLogDealService.class).removeByMap(new JSONObject().fluentPut("model", 8));
        return R.ok();
    }

    @PostMapping("/contractDiscard")
    @ApiOperation("合同作废")
    @SysLogHandler
    public Result contractDiscard(@RequestParam("contractId") Integer contractId) {
        boolean auth = AuthUtil.isRwAuth(contractId, CrmEnum.CONTRACT,CrmAuthEnum.EDIT);
        if (auth) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
        }
        crmContractService.contractDiscard(contractId);
        return R.ok();
    }

    @PostMapping("/queryFileList")
    @ApiOperation("查询附件列表")
    public Result<List<FileEntity>> queryFileList(@RequestParam("id") @ApiParam(name = "id", value = "id") Integer id) {
        List<FileEntity> fileEntities = crmContractService.queryFileList(id);
        return R.ok(fileEntities);
    }

    @PostMapping("/num")
    @ApiOperation("详情页数量展示")
    public Result<CrmInfoNumVO> num(@RequestParam("id") @ApiParam(name = "id", value = "id") Integer id) {
        CrmInfoNumVO infoNumVO = crmContractService.num(id);
        return R.ok(infoNumVO);
    }


    @PostMapping("/batchExportExcel")
    @ApiOperation("选中导出")
    @SysLogHandler(behavior = BehaviorEnum.EXCEL_EXPORT, object = "合同导出", detail = "选中导出")
    public void batchExportExcel(@RequestBody @ApiParam(name = "ids", value = "id列表") List<Integer> ids, HttpServletResponse response) {
        CrmSearchBO search = new CrmSearchBO();
        search.setPageType(0);
        search.setLabel(CrmEnum.CONTRACT.getType());
        CrmSearchBO.Search entity = new CrmSearchBO.Search();
        entity.setFormType(FieldEnum.TEXT.getFormType());
        entity.setSearchEnum(CrmSearchBO.FieldSearchEnum.ID);
        entity.setValues(ids.stream().map(Object::toString).collect(Collectors.toList()));
        search.getSearchList().add(entity);
        search.setPageType(0);
        crmContractService.exportExcel(response, search);
    }

    @PostMapping("/allExportExcel")
    @ApiOperation("全部导出")
    @SysLogHandler(behavior = BehaviorEnum.EXCEL_EXPORT, object = "合同导出", detail = "全部导出")
    public void allExportExcel(@RequestBody CrmSearchBO search, HttpServletResponse response) {
        search.setPageType(0);
        crmContractService.exportExcel(response, search);
    }

    @PostMapping("/information/{id}")
    @ApiOperation("查询详情页信息")
    public Result<List<CrmModelFiledVO>> information(@PathVariable("id") @ApiParam(name = "id", value = "id") Integer id) {
        List<CrmModelFiledVO> information = crmContractService.information(id);
        return R.ok(information);
    }

    @PostMapping("/querySimpleEntity")
    @ApiExplain("查询简单的合同对象")
    public Result<List<SimpleCrmEntity>> querySimpleEntity(@RequestBody List<Integer> ids) {
        List<SimpleCrmEntity> crmEntities = crmContractService.querySimpleEntity(ids);
        return R.ok(crmEntities);
    }

    @PostMapping("/updateInformation")
    @ApiOperation("基本信息保存修改")
    @SysLogHandler(behavior = BehaviorEnum.UPDATE)
    public Result updateInformation(@RequestBody CrmUpdateInformationBO updateInformationBO) {
        crmContractService.updateInformation(updateInformationBO);
        return R.ok();
    }

    @PostMapping("/queryListByProductId")
    @ApiOperation("查询列表页数据")
    public Result<BasePage<Map<String, Object>>> queryListByProductId(@RequestBody BiParams biParams) {
        BasePage<Map<String, Object>> mapBasePage = crmContractService.queryListByProductId(biParams);
        return R.ok(mapBasePage);
    }
}

