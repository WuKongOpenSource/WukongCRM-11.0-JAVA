package com.kakarote.crm.controller;


import cn.hutool.core.util.StrUtil;
import com.kakarote.core.common.*;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.SysLog;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.common.AuthUtil;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.common.log.CrmContactsLog;
import com.kakarote.crm.constant.CrmAuthEnum;
import com.kakarote.crm.constant.CrmCodeEnum;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.*;
import com.kakarote.crm.entity.PO.CrmContacts;
import com.kakarote.crm.entity.VO.CrmInfoNumVO;
import com.kakarote.crm.entity.VO.CrmMembersSelectVO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.service.CrmUploadExcelService;
import com.kakarote.crm.service.ICrmContactsService;
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
 * 联系人表 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
@RestController
@RequestMapping("/crmContacts")
@Api(tags = "联系人模块接口")
@SysLog(subModel = SubModelType.CRM_CONTACTS,logClass = CrmContactsLog.class)
public class CrmContactsController {

    @Autowired
    private ICrmContactsService crmContactsService;

    @Autowired
    private ICrmTeamMembersService teamMembersService;

    @PostMapping("/queryById/{contactsId}")
    @ApiOperation("根据ID查询")
    public Result<CrmModel> queryById(@PathVariable("contactsId") @ApiParam(name = "id", value = "id") Integer contactsId) {
        Integer number = crmContactsService.lambdaQuery().eq(CrmContacts::getContactsId,contactsId).count();
        if (number == 0){
            throw new CrmException(CrmCodeEnum.CRM_DATA_DELETED,"联系人");
        }
        CrmModel model = crmContactsService.queryById(contactsId);
        return R.ok(model);
    }

    @PostMapping("/deleteByIds")
    @ApiOperation("根据ID删除数据")
    @SysLogHandler(behavior = BehaviorEnum.DELETE)
    public Result deleteByIds(@ApiParam(name = "ids", value = "id列表") @RequestBody List<Integer> ids) {
        crmContactsService.deleteByIds(ids);
        return R.ok();
    }

    @PostMapping("/queryPageList")
    @ApiOperation("查询列表页数据")
    public Result<BasePage<Map<String, Object>>> queryPageList(@RequestBody CrmSearchBO search) {
        search.setPageType(1);
        search.setSearch(search.getSearch().trim());
        BasePage<Map<String, Object>> mapBasePage = crmContactsService.queryPageList(search);
        return R.ok(mapBasePage);
    }
    @PostMapping("/field")
    @ApiOperation("查询新增所需字段")
    public Result<List> queryContactsField(@RequestParam(value = "type",required = false) String type) {
        if (StrUtil.isNotEmpty(type)) {
            return R.ok(crmContactsService.queryField(null));
        }
        return R.ok(crmContactsService.queryFormPositionField(null));
    }

    @PostMapping("/field/{id}")
    @ApiOperation("查询修改数据所需信息")
    public Result<List> queryField(@PathVariable("id") @ApiParam(name = "id", value = "id") Integer id,
                                   @RequestParam(value = "type",required = false) String type) {
        if (StrUtil.isNotEmpty(type)) {
            return R.ok(crmContactsService.queryField(id));
        }
        return R.ok(crmContactsService.queryFormPositionField(id));
    }

    @PostMapping("/queryBusiness")
    @ApiOperation("联系人下查询商机")
    public Result<BasePage<Map<String, Object>>> queryBusiness(@RequestBody CrmBusinessPageBO businessPageBO) {
        boolean auth = AuthUtil.isCrmAuth(CrmEnum.CONTACTS, businessPageBO.getContactsId(), CrmAuthEnum.READ);
        if (auth) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
        }
        BasePage<Map<String, Object>> page = crmContactsService.queryBusiness(businessPageBO);
        return R.ok(page);
    }

    @PostMapping("/relateBusiness")
    @ApiOperation("联系人关联商机")
    public Result relateBusiness(@RequestBody CrmRelateBusinessBO relateBusinessBO){
        crmContactsService.relateBusiness(relateBusinessBO);
        return R.ok();
    }

    @PostMapping("/unrelateBusiness")
    @ApiOperation("联系人解除关联商机")
    public Result unrelateBusiness(@RequestBody CrmRelateBusinessBO relateBusinessBO){
        crmContactsService.unrelateBusiness(relateBusinessBO);
        return R.ok();
    }

    @PostMapping("/changeOwnerUser")
    @ApiOperation("修改负责人")
    @SysLogHandler(behavior = BehaviorEnum.CHANGE_OWNER)
    public Result changeOwnerUser(@RequestBody CrmChangeOwnerUserBO crmChangeOwnerUserBO){
        crmContactsService.changeOwnerUser(crmChangeOwnerUserBO);
        return R.ok();
    }

    @PostMapping("/batchExportExcel")
    @ApiOperation("选中导出")
    @SysLogHandler(behavior = BehaviorEnum.EXCEL_EXPORT,object = "选中导出",detail = "导出联系人")
    public void batchExportExcel(@RequestBody @ApiParam(name = "ids", value = "id列表") List<Integer> ids, HttpServletResponse response) {
        CrmSearchBO search = new CrmSearchBO();
        search.setPageType(0);
        search.setLabel(CrmEnum.CONTACTS.getType());
        CrmSearchBO.Search entity = new CrmSearchBO.Search();
        entity.setFormType(FieldEnum.TEXT.getFormType());
        entity.setSearchEnum(CrmSearchBO.FieldSearchEnum.ID);
        entity.setValues(ids.stream().map(Object::toString).collect(Collectors.toList()));
        search.getSearchList().add(entity);
        search.setPageType(0);
        crmContactsService.exportExcel(response, search);
    }

    @PostMapping("/allExportExcel")
    @ApiOperation("全部导出")
    @SysLogHandler(behavior = BehaviorEnum.EXCEL_EXPORT,object = "全部导出",detail = "导出联系人")
    public void allExportExcel(@RequestBody CrmSearchBO search, HttpServletResponse response) {
        search.setPageType(0);
        crmContactsService.exportExcel(response, search);
    }

    @PostMapping("/add")
    @ApiOperation("保存数据")
    @SysLogHandler(behavior = BehaviorEnum.SAVE, object = "#crmModel.entity[name]", detail = "'新增了联系人:' + #crmModel.entity[name]")
    public Result add(@RequestBody CrmContactsSaveBO crmModel) {
        crmContactsService.addOrUpdate(crmModel,false);
        return R.ok();
    }

    @PostMapping("/information/{id}")
    @ApiOperation("查询详情页信息")
    public Result<List<CrmModelFiledVO>> information(@PathVariable("id") @ApiParam(name = "id", value = "id") Integer id) {
        List<CrmModelFiledVO> information = crmContactsService.information(id);
        return R.ok(information);
    }

    @PostMapping("/update")
    @ApiOperation("修改数据")
    @SysLogHandler(behavior = BehaviorEnum.UPDATE)
    public Result update(@RequestBody CrmContactsSaveBO crmModel) {
        crmContactsService.addOrUpdate(crmModel,false);
        return R.ok();
    }
    @PostMapping("/downloadExcel")
    @ApiOperation("下载导入模板")
    public void downloadExcel(HttpServletResponse response) throws IOException {
        crmContactsService.downloadExcel(response);
    }

    @PostMapping("/queryFileList")
    @ApiOperation("查询附件列表")
    public Result<List<FileEntity>> queryFileList(@RequestParam("id") @ApiParam(name = "id", value = "id") Integer id) {
        List<FileEntity> fileEntities = crmContactsService.queryFileList(id);
        return R.ok(fileEntities);
    }

    @PostMapping("/num")
    @ApiOperation("详情页数量展示")
    public Result<CrmInfoNumVO> num(@RequestParam("id") @ApiParam(name = "id", value = "id") Integer id) {
        CrmInfoNumVO infoNumVO = crmContactsService.num(id);
        return R.ok(infoNumVO);
    }

    @PostMapping("/star/{id}")
    @ApiOperation("联系人标星")
    public Result star(@PathVariable("id") @ApiParam(name = "id", value = "id") Integer id) {
        crmContactsService.star(id);
        return R.ok();
    }

    @PostMapping("/querySimpleEntity")
    @ApiExplain("查询简单的联系人对象")
    public Result<List<SimpleCrmEntity>> querySimpleEntity(@RequestBody List<Integer> ids) {
        List<SimpleCrmEntity> crmEntities = crmContactsService.querySimpleEntity(ids);
        return R.ok(crmEntities);
    }

    @PostMapping("/uploadExcel")
    @ApiOperation("导入联系人")
    @SysLogHandler(behavior = BehaviorEnum.EXCEL_IMPORT,object = "导入联系人",detail = "导入联系人")
    public Result<Long> uploadExcel(@RequestParam("file") MultipartFile file, @RequestParam("repeatHandling") Integer repeatHandling) {
        UploadExcelBO uploadExcelBO = new UploadExcelBO();
        uploadExcelBO.setUserInfo(UserUtil.getUser());
        uploadExcelBO.setCrmEnum(CrmEnum.CONTACTS);
        uploadExcelBO.setPoolId(null);
        uploadExcelBO.setRepeatHandling(repeatHandling);
        Long messageId = ApplicationContextHolder.getBean(CrmUploadExcelService.class).uploadExcel(file, uploadExcelBO);
        return R.ok(messageId);
    }

    @PostMapping("/updateInformation")
    @ApiOperation("基本信息保存修改")
    @SysLogHandler(behavior = BehaviorEnum.UPDATE)
    public Result updateInformation(@RequestBody CrmUpdateInformationBO updateInformationBO) {
        crmContactsService.updateInformation(updateInformationBO);
        return R.ok();
    }

    @PostMapping("/getMembers/{contactsId}")
    @ApiOperation("获取团队成员")
    public Result<List<CrmMembersSelectVO>> getMembers(@PathVariable("contactsId") @ApiParam("联系人ID") Integer contractId) {
        CrmEnum crmEnum = CrmEnum.CONTACTS;
        CrmContacts contacts = crmContactsService.getById(contractId);
        if (contacts == null) {
            throw new CrmException(CrmCodeEnum.CRM_DATA_DELETED, crmEnum.getRemarks());
        }
        List<CrmMembersSelectVO> members = teamMembersService.getMembers(crmEnum,contractId,contacts.getOwnerUserId());
        return R.ok(members);
    }

    @PostMapping("/addMembers")
    @ApiOperation("新增团队成员")
    @SysLogHandler(behavior = BehaviorEnum.ADD_MEMBER)
    public Result addMembers(@RequestBody CrmMemberSaveBO crmMemberSaveBO) {
        teamMembersService.addMember(CrmEnum.CONTACTS,crmMemberSaveBO);
        return R.ok();
    }

    @PostMapping("/updateMembers")
    @ApiOperation("新增团队成员")
    @SysLogHandler(behavior = BehaviorEnum.ADD_MEMBER)
    public Result updateMembers(@RequestBody CrmMemberSaveBO crmMemberSaveBO) {
        teamMembersService.addMember(CrmEnum.CONTACTS,crmMemberSaveBO);
        return R.ok();
    }

    @PostMapping("/deleteMembers")
    @ApiOperation("删除团队成员")
    @SysLogHandler
    public Result deleteMembers(@RequestBody CrmMemberSaveBO crmMemberSaveBO) {
        teamMembersService.deleteMember(CrmEnum.CONTACTS,crmMemberSaveBO);
        return R.ok();
    }

    @PostMapping("/exitTeam/{contactsId}")
    @ApiOperation("退出团队")
    @SysLogHandler
    public Result exitTeam(@PathVariable("contactsId") @ApiParam("联系人ID") Integer contactsId) {
        teamMembersService.exitTeam(CrmEnum.CONTACTS,contactsId);
        return R.ok();
    }
}

