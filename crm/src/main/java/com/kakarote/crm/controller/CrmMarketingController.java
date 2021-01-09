package com.kakarote.crm.controller;


import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.ParamAspect;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SubModelType;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.SysLog;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.common.log.CrmMarketingLog;
import com.kakarote.crm.entity.BO.CrmCensusBO;
import com.kakarote.crm.entity.BO.CrmMarketingPageBO;
import com.kakarote.crm.entity.BO.CrmModelSaveBO;
import com.kakarote.crm.entity.BO.CrmSyncDataBO;
import com.kakarote.crm.entity.PO.CrmMarketing;
import com.kakarote.crm.entity.PO.CrmMarketingForm;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.mapper.CrmMarketingMapper;
import com.kakarote.crm.service.ICrmMarketingFormService;
import com.kakarote.crm.service.ICrmMarketingService;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.kakarote.core.utils.BaseUtil.getResponse;

/**
 * <p>
 * 营销表 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-08-12
 */
@RestController
@RequestMapping("/crmMarketing")
@SysLog(subModel = SubModelType.CRM_MARKETING,logClass = CrmMarketingLog.class)
public class CrmMarketingController {

    @Autowired
    private ICrmMarketingService crmMarketingService;

    @Autowired
    private ICrmMarketingFormService crmMarketingFormService;

    @PostMapping("/add")
    @ApiOperation(value = "添加推广")
    @SysLogHandler(behavior = BehaviorEnum.SAVE,object = "#crmMarketing.marketingName",detail = "'创建了市场活动:'+#crmMarketing.marketingName")
    public Result add(@Validated @RequestBody CrmMarketing crmMarketing) {
        crmMarketingService.addOrUpdate(crmMarketing);
        return Result.ok();
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改推广")
    public Result update(@Validated @RequestBody CrmMarketing crmMarketing) {
        crmMarketingService.addOrUpdate(crmMarketing);
        return Result.ok();
    }

    @PostMapping("/queryPageList")
    @ApiOperation(value = "查询推广列表")
    public Result<BasePage<CrmMarketing>> queryPageList(@RequestBody CrmMarketingPageBO crmMarketingPageBO) {
        BasePage<CrmMarketing> page = crmMarketingService.queryPageList(crmMarketingPageBO, null);
        return Result.ok(page);
    }

    @PostMapping("/queryMiNiPageList")
    @ApiOperation(value = "小程序查询推广列表")
    @ParamAspect
    public Result<BasePage<CrmMarketing>> queryMiNiPageList(@RequestBody CrmMarketingPageBO crmMarketingPageBO) {
        UserUtil.setUser(ApplicationContextHolder.getBean(AdminService.class).queryLoginUserInfo(crmMarketingPageBO.getUserId()).getData());
        try {
            BasePage<CrmMarketing> page = crmMarketingService.queryPageList(crmMarketingPageBO, 1);
            return Result.ok(page);
        } finally {
            UserUtil.removeUser();
        }
    }

    /**
     * 推广详情
     *
     * @param marketingId
     */
    @PostMapping("/queryById")
    @ApiOperation(value = "推广详情")
    public Result<JSONObject> queryById(@RequestParam("marketingId") Integer marketingId) {
        JSONObject data = crmMarketingService.queryById(marketingId, null);
        return Result.ok(data);
    }

    @ParamAspect
    @PostMapping("/queryMiNiById")
    @ApiOperation(value = "小程序推广详情")
    public Result<JSONObject> queryMiNiById(@RequestParam("marketingId") Integer marketingId, @RequestParam("userId") Long userId) {
        UserUtil.setUser(ApplicationContextHolder.getBean(AdminService.class).queryLoginUserInfo(userId).getData());
        try {
            JSONObject data = crmMarketingService.queryById(marketingId, null);
            return Result.ok(data);
        } finally {
            UserUtil.removeUser();
        }
    }

    /**
     * 删除推广
     */
    @PostMapping("/deleteByIds")
    @ApiOperation(value = "删除推广")
    public Result deleteByIds(@RequestBody List<Integer> marketingIds) {
        crmMarketingService.deleteByIds(marketingIds);
        return R.ok();
    }

    /**
     * 查询自定义字段
     *
     * @param marketingId
     */
    @PostMapping("/queryField")
    @ApiOperation(value = "查询自定义字段")
    public Result<List<CrmModelFiledVO>> queryField(@RequestParam("marketingId") Integer marketingId) {
        List<CrmModelFiledVO> crmModelFiledVOS = crmMarketingService.queryField(marketingId);
        return Result.ok(crmModelFiledVOS);
    }

    /**
     * 修改状态
     */
    @PostMapping("/updateStatus")
    @ApiOperation(value = "修改状态")
    @SysLogHandler(behavior = BehaviorEnum.UPDATE)
    public Result updateStatus(@RequestParam("marketingIds") String marketingIds, @RequestParam("status") Integer status) {
        crmMarketingService.updateStatus(marketingIds, status);
        return Result.ok();
    }

    @ParamAspect
    @PostMapping("/updateShareNum")
    @ApiOperation(value = "修改分享次数")
    public Result updateShareNum(@RequestParam("marketingId") Integer marketingId, @RequestParam("num") Integer num) {
        crmMarketingService.updateShareNum(marketingId, num);
        return Result.ok();
    }


    @PostMapping("/census")
    @ApiOperation(value = "查询推广客户信息")
    public Result<BasePage<JSONObject>> census(@RequestBody CrmCensusBO crmCensusBO) {
        BasePage<JSONObject> page = crmMarketingService.census(crmCensusBO);
        return Result.ok(page);
    }

    /**
     * 查询自定义字段
     */
    @ParamAspect
    @PostMapping("/queryAddField")
    @ApiOperation("查询自定义字段")
    public Result<JSONObject> queryAddField(@RequestBody JSONObject data) {
        JSONObject jsonObject = crmMarketingService.queryAddField(data.getString("marketingId"));
        return Result.ok(jsonObject);
    }

    /**
     * 保存推广客户信息
     */
    @ParamAspect
    @PostMapping("/saveMarketingInfo")
    @ApiOperation("保存推广客户信息")
    public Result saveMarketingInfo(@RequestBody JSONObject data) {
        try {
            crmMarketingService.saveMarketingInfo(data);
        } finally {
            UserUtil.removeUser();
        }
        return Result.ok();
    }

    @ParamAspect
    @PostMapping("/queryMarketingId")
    @ApiOperation("保存推广客户信息")
    public Result<JSONObject> queryMarketingId(@RequestBody JSONObject data) {
        AES aes = SecureUtil.aes(ICrmMarketingService.BYTES);
        Integer marketingId = Integer.valueOf(aes.decryptStr(data.getString("marketingId")));
        Long currentUserId = Long.valueOf(aes.decryptStr(data.getString("currentUserId")));
        UserUtil.setUser(ApplicationContextHolder.getBean(AdminService.class).queryLoginUserInfo(currentUserId).getData());
        try {
            String device = data.getString("device");
            JSONObject jsonObject = crmMarketingService.queryById(marketingId, device);
            return Result.ok(jsonObject);
        } finally {
            UserUtil.removeUser();
        }
    }

    /**
     * 同步数据
     */
    @PostMapping("/syncData")
    @ApiOperation("保存推广客户信息")
    public Result syncData(@RequestBody CrmSyncDataBO syncDataBO) {
        crmMarketingService.syncData(syncDataBO);
        return Result.ok();
    }

    @PostMapping("/customerExportExcel")
    public void customerExportExcel(@RequestParam("marketingId") Integer marketingId, @RequestParam("status") Integer status) {
        Long userId = UserUtil.getUserId();
        List<Long> userIds = ApplicationContextHolder.getBean(AdminService.class).queryChildUserId(userId).getData();
        userIds.add(userId);
        PageEntity pageEntity = new PageEntity();
        pageEntity.setPageType(0);
        List<JSONObject> fieldList = ApplicationContextHolder.getBean(CrmMarketingMapper.class).census(pageEntity.parse(), marketingId, userIds, status).getList();
        List<CrmModelFiledVO> nameList = crmMarketingService.queryField(marketingId);
        ExcelWriter writer = ExcelUtil.getWriter();
        try {
            nameList.forEach(record -> writer.addHeaderAlias(record.getFieldName(), record.getName()));
            writer.addHeaderAlias("ownerUserName", "负责人");
            String title;
            CrmMarketing marketing = crmMarketingService.getById(marketingId);
            Integer crmType = marketing.getCrmType();
            if (Arrays.asList(ICrmMarketingService.FIXED_CRM_TYPE).contains(crmType)){
                title = "客户信息";
            }else {
                CrmMarketingForm marketingForm = crmMarketingFormService.getById(crmType);
                title = marketingForm.getTitle();
            }
            if (nameList.size() > 1) {
                writer.merge(nameList.size() - 1, title);
            }else {
                writer.merge(1, title);
            }
            List<Map<String, Object>> list = fieldList.stream().map(record -> {
                CrmModelSaveBO jsonObject = JSON.parseObject(record.getString("fieldInfo"), CrmModelSaveBO.class);
                Map<String, Object> entity = jsonObject.getEntity();
                jsonObject.getField().forEach(field -> {
                    entity.put(field.getFieldName(), field.getValue());
                });
                entity.put("ownerUserName", UserCacheUtil.getUserName(record.getLong("ownerUserId")));
                return entity;
            }).collect(Collectors.toList());
            HttpServletResponse response = getResponse();
            writer.setOnlyAlias(true);
            writer.write(list, true);
            writer.setRowHeight(0, 20);
            writer.setRowHeight(1, 20);
            Cell cell = writer.getCell(0, 0);
            for (int i = 0; i < nameList.size(); i++) {
                writer.setColumnWidth(i, 20);
            }
            CellStyle cellStyle = cell.getCellStyle();
            cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = writer.createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 16);
            cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);
            //自定义标题别名
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=customer.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭writer，释放内存
            writer.close();
        }
    }
}

