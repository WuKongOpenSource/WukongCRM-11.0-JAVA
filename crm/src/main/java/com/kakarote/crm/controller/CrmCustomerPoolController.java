package com.kakarote.crm.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.*;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.SysLog;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.common.log.CrmCustomerPoolLog;
import com.kakarote.crm.constant.CrmCodeEnum;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.CrmCustomerPoolBO;
import com.kakarote.crm.entity.BO.CrmSearchBO;
import com.kakarote.crm.entity.BO.UploadExcelBO;
import com.kakarote.crm.entity.PO.CrmCustomerPool;
import com.kakarote.crm.entity.PO.CrmCustomerPoolFieldSort;
import com.kakarote.crm.entity.VO.CrmCustomerPoolVO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.service.CrmUploadExcelService;
import com.kakarote.crm.service.ICrmCustomerPoolService;
import com.kakarote.crm.service.ICrmCustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 公海表 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-29
 */
@RestController
@RequestMapping("/crmCustomerPool")
@Api(tags = "公海控制器")
@Slf4j
@SysLog(logClass = CrmCustomerPoolLog.class)
public class CrmCustomerPoolController {

    @Autowired
    private ICrmCustomerPoolService crmCustomerPoolService;

    @Autowired
    private ICrmCustomerService customerService;

    @Autowired
    private CrmUploadExcelService uploadExcelService;

    @ApiOperation("查看公海列表页")
    @PostMapping("/queryPageList")
    public Result<BasePage<Map<String, Object>>> queryPageList(@RequestBody CrmSearchBO crmSearchBO) {
        BasePage<Map<String, Object>> basePage = crmCustomerPoolService.queryPageList(crmSearchBO);
        return Result.ok(basePage);
    }

    @ApiOperation("查询公海列表配置")
    @PostMapping("/queryPoolSettingList")
    public Result<BasePage<CrmCustomerPoolVO>> queryPoolSettingList(@RequestBody PageEntity entity) {
        BasePage<CrmCustomerPoolVO> poolVOBasePage = crmCustomerPoolService.queryPoolSettingList(entity);
        return Result.ok(poolVOBasePage);
    }

    @ApiOperation("公海选择列表")
    @PostMapping("/queryPoolNameList")
    public Result<List<CrmCustomerPool>> queryPoolNameList() {
        List<CrmCustomerPool> crmCustomerPools = crmCustomerPoolService.queryPoolNameList();
        return Result.ok(crmCustomerPools);
    }

    @ApiOperation("修改公海状态")
    @PostMapping("/changeStatus")
    @SysLogHandler(applicationName = "admin",subModel = SubModelType.ADMIN_CUSTOMER_MANAGEMENT,behavior = BehaviorEnum.UPDATE)
    public Result changeStatus(@RequestParam("poolId") Integer poolId, @RequestParam("status") Integer status) {
        crmCustomerPoolService.changeStatus(poolId, status);
        return Result.ok();
    }

    @ApiOperation("根据ID查询公海信息")
    @PostMapping("/queryPoolById")
    public Result<CrmCustomerPoolVO> queryPoolById(@RequestParam("poolId") Integer poolId) {
        CrmCustomerPoolVO customerPoolVO = crmCustomerPoolService.queryPoolById(poolId);
        return Result.ok(customerPoolVO);
    }

    @ApiOperation("获取公海默认字段")
    @PostMapping("/queryPoolField")
    public Result<List<CrmModelFiledVO>> queryPoolField() {
        List<CrmModelFiledVO> filedVOS = crmCustomerPoolService.queryPoolField();
        return Result.ok(filedVOS);
    }

    @PostMapping("/downloadExcel")
    @ApiOperation("下载导入模板")
    public void downloadExcel(HttpServletResponse response) throws IOException {
        customerService.downloadExcel(true,response);
    }

    @PostMapping("/uploadExcel")
    @ApiOperation("导入客户")
    public Result<Long> uploadExcel(@RequestParam("file") MultipartFile file, @RequestParam("repeatHandling") Integer repeatHandling, @RequestParam("poolId") Integer poolId) {
        UploadExcelBO uploadExcelBO = new UploadExcelBO();
        uploadExcelBO.setUserInfo(UserUtil.getUser());
        uploadExcelBO.setCrmEnum(CrmEnum.CUSTOMER);
        uploadExcelBO.setPoolId(poolId);
        uploadExcelBO.setRepeatHandling(repeatHandling);
        Long messageId = uploadExcelService.uploadExcel(file, uploadExcelBO);
        return R.ok(messageId);
    }


    @ApiOperation("公海全部导出")
    @PostMapping("/allExportExcel")
    public void allExportExcel(@RequestBody CrmSearchBO search, HttpServletResponse response) throws IOException {
        search.setPageType(0);
        BasePage<Map<String, Object>> basePage = crmCustomerPoolService.queryPageList(search);
        export(basePage.getList(), search.getPoolId(), response);
    }

    @ApiOperation("公海批量导出")
    @PostMapping("/batchExportExcel")
    public void batchExportExcel(@RequestBody JSONObject jsonObject, HttpServletResponse response) throws IOException {
        Integer poolId = jsonObject.getInteger("poolId");
        String ids = jsonObject.getString("ids");
        CrmSearchBO search = new CrmSearchBO();
        search.setPoolId(poolId);
        search.setPageType(0);
        search.setLabel(CrmEnum.CUSTOMER.getType());
        CrmSearchBO.Search entity = new CrmSearchBO.Search();
        entity.setFormType(FieldEnum.TEXT.getFormType());
        entity.setSearchEnum(CrmSearchBO.FieldSearchEnum.ID);
        entity.setValues(StrUtil.splitTrim(ids, Const.SEPARATOR));
        search.getSearchList().add(entity);
        search.setPageType(0);
        BasePage<Map<String, Object>> basePage = crmCustomerPoolService.queryPageList(search);
        export(basePage.getList(), search.getPoolId(), response);
    }


    private void export(List<Map<String, Object>> recordList, Integer poolId, HttpServletResponse response) throws IOException {
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            List<CrmCustomerPoolFieldSort> headList = crmCustomerPoolService.queryPoolListHead(poolId);
            headList.forEach(head -> writer.addHeaderAlias(head.getFieldName(), head.getName()));
            writer.merge(headList.size() - 1, "客户信息");
            if (recordList.size() == 0) {
                Map<String, Object> record = new HashMap<>();
                headList.forEach(head -> record.put(head.getFieldName(), ""));
                recordList.add(record);
            }
            recordList.forEach(record -> {
                record.put("dealStatus", Objects.equals(1, record.get("dealStatus")) ? "已成交" : "未成交");
            });
            writer.setOnlyAlias(true);
            writer.write(recordList, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < headList.size(); i++) {
                writer.setColumnWidth(i, 20);
            }
            Cell cell = writer.getCell(0, 0);
            CellStyle cellStyle = cell.getCellStyle();
            cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = writer.createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 16);
            cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);
            //自定义标题别名
            //response为HttpServletResponse对象
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=customer.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出公海错误：", e);
        }
    }

    @ApiOperation("获取客户级别选项")
    @PostMapping("/queryCustomerLevel")
    public Result<List<String>> queryCustomerLevel() {
        List<String> strings = crmCustomerPoolService.queryCustomerLevel();
        return Result.ok(strings);
    }

    @ApiOperation("设置公海规则")
    @PostMapping("/setCustomerPool")
    @SysLogHandler(applicationName = "admin",subModel = SubModelType.ADMIN_CUSTOMER_MANAGEMENT,behavior = BehaviorEnum.SAVE,object = "#jsonObject[poolName]",detail = "'添加或修改了公海规则:'+#jsonObject[poolName]")
    public Result setCustomerPool(@RequestBody JSONObject jsonObject) {
        crmCustomerPoolService.setCustomerPool(jsonObject);
        return Result.ok();
    }

    @ApiOperation("查询公海字段配置")
    @PostMapping("/queryPoolFieldConfig")
    public Result<JSONObject> queryPoolFieldConfig(@RequestParam("poolId") Integer poolId) {
        JSONObject object = crmCustomerPoolService.queryPoolFieldConfig(poolId);
        return Result.ok(object);
    }

    @ApiOperation("公海字段配置")
    @PostMapping("/poolFieldConfig")
    public Result poolFieldConfig(@RequestBody JSONObject jsonObject) {
        crmCustomerPoolService.poolFieldConfig(jsonObject);
        return Result.ok();
    }

    @ApiOperation("删除公海规则")
    @PostMapping("/deleteCustomerPool")
    @SysLogHandler(applicationName = "admin",subModel = SubModelType.ADMIN_CUSTOMER_MANAGEMENT,behavior = BehaviorEnum.DELETE)
    public Result deleteCustomerPool(@RequestParam("poolId") Integer poolId) {
        crmCustomerPoolService.deleteCustomerPool(poolId);
        return Result.ok();
    }

    @ApiOperation("查询前台公海列表")
    @PostMapping("/queryPoolNameListByAuth")
    public Result queryPoolNameListByAuth() {
        List<CrmCustomerPool> crmCustomerPools = crmCustomerPoolService.queryPoolNameListByAuth();
        return Result.ok(crmCustomerPools);
    }

    @ApiOperation("查询前台公海字段")
    @PostMapping("/queryPoolListHead")
    public Result<List<CrmCustomerPoolFieldSort>> queryPoolListHead(Integer poolId) {
        if (poolId == null) {
            List<CrmCustomerPool> crmCustomerPools = crmCustomerPoolService.queryPoolNameListByAuth();
            if (crmCustomerPools.size() == 0) {
                throw new CrmException(CrmCodeEnum.CRM_CUSTOMER_POOL_NOT_EXIST_ERROR);
            }
            poolId = crmCustomerPools.get(0).getPoolId();
        }
        List<CrmCustomerPoolFieldSort> crmCustomerPoolFieldSorts = crmCustomerPoolService.queryPoolListHead(poolId);
        return Result.ok(crmCustomerPoolFieldSorts);
    }

    @PostMapping("/transfer")
    @ApiOperation("公海客户的转移")
    @SysLogHandler(applicationName = "admin",subModel = SubModelType.ADMIN_CUSTOMER_MANAGEMENT,behavior = BehaviorEnum.UPDATE)
    public Result transfer(@RequestParam("prePoolId") Integer prePoolId, @RequestParam("postPoolId") Integer postPoolId) {
        crmCustomerPoolService.transfer(prePoolId, postPoolId);
        return R.ok();
    }

    @PostMapping("/deleteByIds")
    @ApiOperation("根据ID删除数据")
    public Result deleteByIds(@RequestBody CrmCustomerPoolBO poolBO) {
        crmCustomerPoolService.deleteByIds(poolBO.getIds(), poolBO.getPoolId());
        return R.ok();
    }

    @ApiOperation("查询前台公海权限")
    @PostMapping("/queryAuthByPoolId")
    public Result<JSONObject> queryAuthByPoolId(@RequestParam("poolId") Integer poolId) {
        JSONObject object = crmCustomerPoolService.queryAuthByPoolId(poolId);
        return Result.ok(object);
    }
}

