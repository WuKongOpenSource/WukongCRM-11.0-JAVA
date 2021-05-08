package com.kakarote.crm.controller;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.*;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.SysLog;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminConfig;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.common.AuthUtil;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.common.log.CrmCustomerLog;
import com.kakarote.crm.constant.CrmAuthEnum;
import com.kakarote.crm.constant.CrmCodeEnum;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.*;
import com.kakarote.crm.entity.PO.CrmContacts;
import com.kakarote.crm.entity.PO.CrmCustomer;
import com.kakarote.crm.entity.PO.CrmCustomerSetting;
import com.kakarote.crm.entity.VO.CrmDataCheckVO;
import com.kakarote.crm.entity.VO.CrmInfoNumVO;
import com.kakarote.crm.entity.VO.CrmMembersSelectVO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.service.CrmUploadExcelService;
import com.kakarote.crm.service.ICrmCustomerService;
import com.kakarote.crm.service.ICrmTeamMembersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户表 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-29
 */
@RestController
@RequestMapping("/crmCustomer")
@Api(tags = "客户模块接口")
@SysLog(subModel = SubModelType.CRM_CUSTOMER, logClass = CrmCustomerLog.class)
public class CrmCustomerController {

    @Autowired
    private ICrmCustomerService crmCustomerService;

    @Autowired
    private CrmUploadExcelService uploadExcelService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ICrmTeamMembersService teamMembersService;

    @PostMapping("/queryPageList")
    @ApiOperation("查询列表页数据")
    public Result<BasePage<Map<String, Object>>> queryPageList(@RequestBody CrmSearchBO search) {
        search.setPageType(1);
        BasePage<Map<String, Object>> mapBasePage = crmCustomerService.queryPageList(search);
        return R.ok(mapBasePage);
    }

    @PostMapping("/add")
    @ApiOperation("保存数据")
    @SysLogHandler(behavior = BehaviorEnum.SAVE, object = "#crmModel.entity[customerName]", detail = "'新增了客户:' + #crmModel.entity[customerName]")
    public Result<Map<String, Object>> add(@RequestBody CrmBusinessSaveBO crmModel) {
        Map<String, Object> map = crmCustomerService.addOrUpdate(crmModel, false, null);
        return R.ok(map);
    }

    @PostMapping("/update")
    @ApiOperation("修改数据")
    @SysLogHandler(behavior = BehaviorEnum.UPDATE)
    public Result<Map<String, Object>> update(@RequestBody CrmBusinessSaveBO crmModel) {
        Map<String, Object> map = crmCustomerService.addOrUpdate(crmModel, false, null);
        return R.ok(map);
    }

    @PostMapping("/queryById/{customerId}")
    @ApiOperation("根据ID查询")
    public Result<CrmModel> queryById(@PathVariable("customerId") @ApiParam(name = "id", value = "id") Integer customerId, Integer poolId) {
        Integer number = crmCustomerService.lambdaQuery().eq(CrmCustomer::getCustomerId, customerId).ne(CrmCustomer::getStatus, 3).count();
        if (number == 0) {
            throw new CrmException(CrmCodeEnum.CRM_DATA_DELETED, "客户");
        }
        boolean auth = AuthUtil.isPoolAuth(customerId, CrmAuthEnum.READ);
        if (auth) {
            CrmModel crmModel = new CrmModel();
            crmModel.put("dataAuth", 0);
            return R.ok(crmModel);
        }
        CrmModel model = crmCustomerService.queryById(customerId, poolId);
        return R.ok(model);
    }

    @PostMapping("/field")
    @ApiOperation("查询新增所需字段")
    public Result<List> queryCustomerField(@RequestParam(value = "type",required = false) String type) {
        if (StrUtil.isNotEmpty(type)) {
            return R.ok(crmCustomerService.queryField(null));
        }
        return R.ok(crmCustomerService.queryFormPositionField(null));
    }

    @PostMapping("/field/{id}")
    @ApiOperation("查询修改数据所需信息")
    public Result<List> queryField(@PathVariable("id") @ApiParam(name = "id", value = "id") Integer id,
                                                          @RequestParam(value = "type",required = false) String type) {
        if (StrUtil.isNotEmpty(type)) {
            return R.ok(crmCustomerService.queryField(id));
        }
        return R.ok(crmCustomerService.queryFormPositionField(id));
    }

    @PostMapping("/queryContacts")
    @ApiOperation("查询客户下联系人")
    public Result<BasePage<CrmContacts>> queryContacts(@RequestBody CrmContactsPageBO pageEntity) {
        boolean auth = AuthUtil.isPoolAuth(pageEntity.getCustomerId(), CrmAuthEnum.READ);
        if (auth) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
        }
        BasePage<CrmContacts> contactsBasePage = crmCustomerService.queryContacts(pageEntity);
        return R.ok(contactsBasePage);
    }

    @PostMapping("/deleteByIds")
    @ApiOperation("根据ID删除数据")
    @SysLogHandler(behavior = BehaviorEnum.DELETE)
    public Result deleteByIds(@ApiParam(name = "ids", value = "id列表") @RequestBody List<Integer> ids) {
        crmCustomerService.deleteByIds(ids);
        return R.ok();
    }


    @PostMapping("/detectionDataCanBeDelete")
    @ApiOperation("根据ID删除数据")
    public Result detectionDataCanBeDelete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<Integer> ids) {
        return R.ok(crmCustomerService.detectionDataCanBeDelete(ids));
    }

    @PostMapping("/queryBusiness")
    @ApiOperation("查询客户下商机")
    public Result<BasePage<Map<String, Object>>> queryBusiness(@RequestBody CrmContactsPageBO pageEntity) {
        boolean auth = AuthUtil.isPoolAuth(pageEntity.getCustomerId(),CrmAuthEnum.READ);
        if (auth) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
        }
        BasePage<Map<String, Object>> basePage = crmCustomerService.queryBusiness(pageEntity);
        return R.ok(basePage);
    }

    @PostMapping("/queryContract")
    @ApiOperation("查询客户下合同")
    public Result<BasePage<Map<String, Object>>> queryContract(@RequestBody CrmContactsPageBO pageEntity) {
        boolean auth = AuthUtil.isPoolAuth(pageEntity.getCustomerId(),CrmAuthEnum.READ);
        if (auth) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
        }
        BasePage<Map<String, Object>> basePage = crmCustomerService.queryContract(pageEntity);
        return R.ok(basePage);
    }

    @PostMapping("/queryReceivablesPlan")
    @ApiOperation("根据客户id查询回款计划")
    public Result<BasePage<JSONObject>> queryReceivablesPlan(@RequestBody CrmRelationPageBO crmRelationPageBO) {
        boolean auth = AuthUtil.isPoolAuth(crmRelationPageBO.getCustomerId(),CrmAuthEnum.READ);
        if (auth) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
        }
        BasePage<JSONObject> data = crmCustomerService.queryReceivablesPlan(crmRelationPageBO);
        return R.ok(data);
    }

    @PostMapping("/queryReceivables")
    @ApiOperation("根据客户id查询回款")
    public Result<BasePage<JSONObject>> queryReceivables(@RequestBody CrmRelationPageBO crmRelationPageBO) {
        boolean auth = AuthUtil.isPoolAuth(crmRelationPageBO.getCustomerId(),CrmAuthEnum.READ);
        if (auth) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
        }
        BasePage<JSONObject> data = crmCustomerService.queryReceivables(crmRelationPageBO);
        return R.ok(data);
    }

    @PostMapping("/queryReturnVisit")
    @ApiOperation("根据客户id查询回款")
    public Result<BasePage<JSONObject>> queryReturnVisit(@RequestBody CrmRelationPageBO crmRelationPageBO) {
        boolean auth = AuthUtil.isPoolAuth(crmRelationPageBO.getCustomerId(),CrmAuthEnum.READ);
        if (auth) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
        }
        BasePage<JSONObject> data = crmCustomerService.queryReturnVisit(crmRelationPageBO);
        return R.ok(data);
    }

    @PostMapping("/queryInvoice")
    @ApiOperation("根据客户id查询发票")
    public Result<BasePage<JSONObject>> queryInvoice(@RequestBody CrmRelationPageBO crmRelationPageBO) {
        boolean auth = AuthUtil.isPoolAuth(crmRelationPageBO.getCustomerId(),CrmAuthEnum.READ);
        if (auth) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
        }
        BasePage<JSONObject> data = crmCustomerService.queryInvoice(crmRelationPageBO);
        return R.ok(data);
    }

    @PostMapping("/queryInvoiceInfo")
    @ApiOperation("根据客户id查询发票抬头信息")
    public Result<BasePage<JSONObject>> queryInvoiceInfo(@RequestBody CrmRelationPageBO crmRelationPageBO) {
        boolean auth = AuthUtil.isPoolAuth(crmRelationPageBO.getCustomerId(),CrmAuthEnum.READ);
        if (auth) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
        }
        BasePage<JSONObject> data = crmCustomerService.queryInvoiceInfo(crmRelationPageBO);
        return R.ok(data);
    }

    @PostMapping("/queryCallRecord")
    @ApiOperation("根据客户id查询呼叫记录")
    public Result<BasePage<JSONObject>> queryCallRecord(@RequestBody CrmRelationPageBO crmRelationPageBO) {
        boolean auth = AuthUtil.isPoolAuth(crmRelationPageBO.getCustomerId(),CrmAuthEnum.READ);
        if (auth) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
        }
        BasePage<JSONObject> data = crmCustomerService.queryCallRecord(crmRelationPageBO);
        return R.ok(data);
    }

    @PostMapping("/lock")
    @ApiOperation("锁定或解锁客户")
    @SysLogHandler
    public Result lock(@RequestParam("status") Integer status, @RequestParam("ids") String id) {
        crmCustomerService.lock(status, StrUtil.splitTrim(id, Const.SEPARATOR));
        return R.ok();
    }

    @PostMapping("/setDealStatus")
    @ApiOperation("修改客户成交状态")
    @SysLogHandler(behavior = BehaviorEnum.CHANGE_DEAL_STATUS)
    public Result setDealStatus(@RequestParam("dealStatus") Integer dealStatus, @RequestParam("ids") String id) {
        crmCustomerService.setDealStatus(dealStatus, StrUtil.splitTrim(id, Const.SEPARATOR).stream().map(Integer::valueOf).collect(Collectors.toList()));
        return R.ok();
    }

    @PostMapping("/changeOwnerUser")
    @ApiOperation("修改客户负责人")
    @SysLogHandler(behavior = BehaviorEnum.CHANGE_OWNER)
    public Result changeOwnerUser(@RequestBody CrmChangeOwnerUserBO crmChangeOwnerUserBO) {
        crmCustomerService.changeOwnerUser(crmChangeOwnerUserBO);
        return R.ok();
    }

    @PostMapping("/getMembers/{customerId}")
    @ApiOperation("获取团队成员")
    public Result<List<CrmMembersSelectVO>> getMembers(@PathVariable("customerId") @ApiParam("客户ID") Integer customerId) {
        CrmEnum crmEnum = CrmEnum.CUSTOMER;
        CrmCustomer customer = crmCustomerService.getById(customerId);
        if (customer == null) {
            throw new CrmException(CrmCodeEnum.CRM_DATA_DELETED, crmEnum.getRemarks());
        }
        List<CrmMembersSelectVO> members = teamMembersService.getMembers(crmEnum,customerId,customer.getOwnerUserId());
        return R.ok(members);
    }

    @PostMapping("/addMembers")
    @ApiOperation("新增团队成员")
    @SysLogHandler(behavior = BehaviorEnum.ADD_MEMBER)
    public Result addMembers(@RequestBody CrmMemberSaveBO crmMemberSaveBO) {
        teamMembersService.addMember(CrmEnum.CUSTOMER,crmMemberSaveBO);
        return R.ok();
    }

    @PostMapping("/updateMembers")
    @ApiOperation("新增团队成员")
    @SysLogHandler(behavior = BehaviorEnum.ADD_MEMBER)
    public Result updateMembers(@RequestBody CrmMemberSaveBO crmMemberSaveBO) {
        teamMembersService.addMember(CrmEnum.CUSTOMER,crmMemberSaveBO);
        return R.ok();
    }

    @PostMapping("/deleteMembers")
    @ApiOperation("删除团队成员")
    @SysLogHandler
    public Result deleteMembers(@RequestBody CrmMemberSaveBO crmMemberSaveBO) {
        teamMembersService.deleteMember(CrmEnum.CUSTOMER,crmMemberSaveBO);
        return R.ok();
    }

    @PostMapping("/exitTeam/{customerId}")
    @ApiOperation("删除团队成员")
    @SysLogHandler
    public Result exitTeam(@PathVariable("customerId") @ApiParam("客户ID") Integer customerId) {
        teamMembersService.exitTeam(CrmEnum.CUSTOMER,customerId);
        return R.ok();
    }

    @PostMapping("/getRulesSetting")
    @ApiOperation("查询规则设置")
    public Result<JSONObject> getRulesSetting() {
        AdminConfig dealDay = adminService.queryFirstConfigByName("customerPoolSettingDealDays").getData();
        AdminConfig followupDay = adminService.queryFirstConfigByName("customerPoolSettingFollowupDays").getData();
        AdminConfig type = adminService.queryFirstConfigByName("customerPoolSetting").getData();
        AdminConfig remindConfig = adminService.queryFirstConfigByName("putInPoolRemindDays").getData();

        if (dealDay == null) {
            dealDay = new AdminConfig();
            dealDay.setName("customerPoolSettingDealDays");
            dealDay.setValue("3");
            adminService.updateAdminConfig(dealDay);
        }
        if (followupDay == null) {
            followupDay = new AdminConfig();
            followupDay.setName("customerPoolSettingFollowupDays");
            followupDay.setValue("7");
            adminService.updateAdminConfig(followupDay);
        }
        if (type == null) {
            type = new AdminConfig();
            type.setName("customerPoolSetting");
            type.setStatus(0);
            adminService.updateAdminConfig(type);
        }
        if (remindConfig == null) {
            remindConfig = new AdminConfig();
            remindConfig.setStatus(0);
            remindConfig.setValue("3");
            remindConfig.setName("putInPoolRemindDays");
            adminService.updateAdminConfig(remindConfig);
        }
        AdminConfig config = adminService.queryFirstConfigByName("expiringContractDays").getData();
        if (config == null) {
            config = new AdminConfig();
            config.setStatus(0);
            config.setName("expiringContractDays");
            config.setValue("3");
            config.setDescription("合同到期提醒");
            adminService.updateAdminConfig(config);
        }
        JSONObject object = new JSONObject();
        object.put("dealDay", dealDay.getValue());
        object.put("followupDay", followupDay.getValue());
        object.put("customerConfig", type.getStatus());
        object.put("contractConfig", config.getStatus());
        object.put("contractDay", config.getValue());
        object.put("putInPoolRemindConfig", remindConfig.getStatus());
        object.put("putInPoolRemindDays", remindConfig.getValue());
        return R.ok(object);
    }

    @PostMapping("/batchExportExcel")
    @ApiOperation("选中导出")
    @SysLogHandler(behavior = BehaviorEnum.EXCEL_EXPORT,object = "选中导出",detail = "导出客户")
    public void batchExportExcel(@RequestBody @ApiParam(name = "ids", value = "id列表") List<Integer> ids, HttpServletResponse response) {
        CrmSearchBO search = new CrmSearchBO();
        search.setPageType(0);
        search.setLabel(CrmEnum.CUSTOMER.getType());
        CrmSearchBO.Search entity = new CrmSearchBO.Search();
        entity.setFormType(FieldEnum.TEXT.getFormType());
        entity.setSearchEnum(CrmSearchBO.FieldSearchEnum.ID);
        entity.setValues(ids.stream().map(Object::toString).collect(Collectors.toList()));
        search.getSearchList().add(entity);
        search.setPageType(0);
        crmCustomerService.exportExcel(response, search);
    }

    @PostMapping("/allExportExcel")
    @ApiOperation("全部导出")
    @SysLogHandler(behavior = BehaviorEnum.EXCEL_EXPORT,object = "全部导出",detail = "导出客户")
    public void allExportExcel(@RequestBody CrmSearchBO search, HttpServletResponse response) {
        search.setPageType(0);
        crmCustomerService.exportExcel(response, search);
    }

    @PostMapping("/updateCustomerByIds")
    @ApiOperation("客户放入公海")
    @SysLogHandler(behavior = BehaviorEnum.PUT_IN_POOL)
    public Result updateCustomerByIds(@RequestBody CrmCustomerPoolBO poolBO) {
        crmCustomerService.updateCustomerByIds(poolBO);
        return R.ok();
    }

    @PostMapping("/distributeByIds")
    @ApiOperation("公海分配客户")
    @SysLogHandler(behavior = BehaviorEnum.DISTRIBUTE)
    public Result distributeByIds(@RequestBody CrmCustomerPoolBO poolBO) {
        crmCustomerService.getCustomersByIds(poolBO, 1);
        return R.ok();
    }

    @PostMapping("/receiveByIds")
    @ApiOperation("公海领取客户")
    @SysLogHandler(behavior = BehaviorEnum.RECEIVE)
    public Result receiveByIds(@RequestBody CrmCustomerPoolBO poolBO) {
        crmCustomerService.getCustomersByIds(poolBO, 2);
        return R.ok();
    }

    @PostMapping("/downloadExcel")
    @ApiOperation("下载导入模板")
    public void downloadExcel(HttpServletResponse response) throws IOException {
        crmCustomerService.downloadExcel(false,response);
    }

    @PostMapping("/uploadExcel")
    @ApiOperation("导入客户")
    @SysLogHandler(behavior = BehaviorEnum.EXCEL_IMPORT,object = "导入客户",detail = "导入客户")
    public Result<Long> uploadExcel(@RequestParam("file") MultipartFile file, @RequestParam("repeatHandling") Integer repeatHandling) {
        UploadExcelBO uploadExcelBO = new UploadExcelBO();
        uploadExcelBO.setUserInfo(UserUtil.getUser());
        uploadExcelBO.setCrmEnum(CrmEnum.CUSTOMER);
        uploadExcelBO.setPoolId(null);
        uploadExcelBO.setRepeatHandling(repeatHandling);
        Long messageId = uploadExcelService.uploadExcel(file, uploadExcelBO);
        return R.ok(messageId);
    }

    @PostMapping("/customerSetting")
    @ApiOperation("客户规则设置")
    @SysLogHandler(applicationName = "admin",subModel = SubModelType.ADMIN_CUSTOMER_MANAGEMENT,behavior = BehaviorEnum.UPDATE,object = "修改拥有/锁定客户规则设置",detail = "修改拥有/锁定客户规则设置")
    public Result customerSetting(@RequestBody CrmCustomerSetting customerSetting) {
        crmCustomerService.customerSetting(customerSetting);
        return R.ok();
    }

    @PostMapping("/queryCustomerSetting")
    @ApiOperation("客户数限制查询")
    public Result<BasePage<CrmCustomerSetting>> queryCustomerSetting(PageEntity pageEntity, @RequestParam("type") Integer type) {
        BasePage<CrmCustomerSetting> basePage = crmCustomerService.queryCustomerSetting(pageEntity, type);
        return R.ok(basePage);
    }

    @PostMapping("/deleteCustomerSetting")
    @ApiOperation("删除客户规则设置")
    public Result deleteCustomerSetting(@RequestParam("settingId") Integer settingId) {
        crmCustomerService.deleteCustomerSetting(settingId);
        return R.ok();
    }

    @PostMapping("/setContacts")
    public Result setContacts(@RequestBody CrmFirstContactsBO contactsBO) {
        crmCustomerService.setContacts(contactsBO);
        return R.ok();
    }

    @PostMapping("/dataCheck")
    @ApiOperation("数据查重")
    public Result<List<CrmDataCheckVO>> dataCheck(@RequestBody CrmDataCheckBO dataCheckBO) {
        List<CrmDataCheckVO> crmDataCheckVOS = crmCustomerService.dataCheck(dataCheckBO);
        return Result.ok(crmDataCheckVOS);
    }

    @PostMapping("/queryFileList")
    @ApiOperation("查询附件列表")
    public Result<List<FileEntity>> queryFileList(@RequestParam("id") @ApiParam(name = "id", value = "id") Integer id) {
        List<FileEntity> fileEntities = crmCustomerService.queryFileList(id);
        return R.ok(fileEntities);
    }

    @PostMapping("/num")
    @ApiOperation("详情页数量展示")
    public Result<CrmInfoNumVO> num(@RequestParam("id") @ApiParam(name = "id", value = "id") Integer id) {
        CrmInfoNumVO num = crmCustomerService.num(id);
        return R.ok(num);
    }

    @PostMapping("/nearbyCustomer")
    @ApiOperation("附近的客户")
    public Result<List<JSONObject>> nearbyCustomer(@RequestParam("lng") String lng, @RequestParam("lat") String lat,
                                                   @RequestParam("type") Integer type, @RequestParam("radius") Integer radius,
                                                   @RequestParam(value = "ownerUserId", required = false) Long ownerUserId) {
        List<JSONObject> jsonObjects = crmCustomerService.nearbyCustomer(lng, lat, type, radius, ownerUserId);
        return R.ok(jsonObjects);
    }

    @PostMapping("/star/{id}")
    @ApiOperation("客户标星")
    public Result star(@PathVariable("id") @ApiParam(name = "id", value = "id") Integer id) {
        crmCustomerService.star(id);
        return R.ok();
    }

    /**
     * 查询详情页基本信息
     *
     * @param customerId id
     * @param poolId     公海ID
     * @return data
     */
    @PostMapping("/information/{id}")
    @ApiOperation("查询详情页信息")
    public Result<List<CrmModelFiledVO>> information(@PathVariable("id") @ApiParam(name = "id", value = "id") Integer customerId, @RequestParam(name = "poolId", required = false) @ApiParam(name = "poolId", value = "poolId") Integer poolId) {
        List<CrmModelFiledVO> information = crmCustomerService.information(customerId, poolId);
        return R.ok(information);
    }

    @PostMapping("/querySimpleEntity")
    @ApiExplain("查询简单的客户对象")
    public Result<List<SimpleCrmEntity>> querySimpleEntity(@RequestBody List<Integer> ids) {
        List<SimpleCrmEntity> crmEntities = crmCustomerService.querySimpleEntity(ids);
        return R.ok(crmEntities);
    }

    @PostMapping("/queryByNameCustomerInfo")
    @ApiExplain("根据名字查询like简单的客户对象")
    public Result<List<SimpleCrmEntity>> queryByNameCustomerInfo(@RequestParam("name") String name) {
        List<SimpleCrmEntity> crmEntities = crmCustomerService.queryByNameCustomerInfo(name);
        return R.ok(crmEntities);
    }

    @PostMapping("/queryNameCustomerInfo")
    @ApiExplain("根据名字查询eq简单的客户对象")
    public Result<List<SimpleCrmEntity>> queryNameCustomerInfo(@RequestParam("name") String name) {
        List<SimpleCrmEntity> crmEntities = crmCustomerService.queryNameCustomerInfo(name);
        return R.ok(crmEntities);
    }

    @PostMapping("/updateInformation")
    @ApiOperation("基本信息保存修改")
    @SysLogHandler(behavior = BehaviorEnum.UPDATE)
    public Result updateInformation(@RequestBody CrmUpdateInformationBO updateInformationBO) {
        crmCustomerService.updateInformation(updateInformationBO);
        return R.ok();
    }
}

