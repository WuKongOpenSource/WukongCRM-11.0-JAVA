package com.kakarote.crm.controller;


import com.kakarote.core.common.Result;
import com.kakarote.core.common.SubModelType;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.common.log.SysLogHandler;
import com.kakarote.core.feign.admin.entity.AdminConfig;
import com.kakarote.crm.entity.VO.CrmNumberSettingVO;
import com.kakarote.crm.service.ICrmNumberSettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 系统自动生成编号设置表 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-28
 */
@RestController
@RequestMapping("/crmNumberSetting")
@Api(tags = "自动编号模块")
public class CrmNumberSettingController {

    @Autowired
    private ICrmNumberSettingService crmNumberSettingService;

    /**
     * 查询自动编号设置
     */
    @ApiOperation(value = "查询自动编号设置")
    @PostMapping("/queryNumberSetting")
    public Result<List<CrmNumberSettingVO>> queryNumberSetting() {
        List<CrmNumberSettingVO> settingVOS = crmNumberSettingService.queryNumberSetting();
        return Result.ok(settingVOS);
    }

    /**
     * 查询发票自动编号设置
     */
    @ApiOperation(value = "查询发票自动编号设置")
    @PostMapping("/queryInvoiceNumberSetting")
    public Result<AdminConfig> queryInvoiceNumberSetting() {
        AdminConfig adminConfig = crmNumberSettingService.queryInvoiceNumberSetting();
        return Result.ok(adminConfig);
    }

    @ApiOperation(value = "设置发票自动编号设置")
    @PostMapping("/setNumberSetting")
    @SysLogHandler(applicationName = "admin",subModel = SubModelType.ADMIN_CUSTOMER_MANAGEMENT,behavior = BehaviorEnum.UPDATE,object = "修改编号规则设置",detail = "修改编号规则设置")
    public Result setNumberSetting(@RequestBody @Valid List<CrmNumberSettingVO> numberSettingList) {
        crmNumberSettingService.setNumberSetting(numberSettingList);
        return Result.ok();
    }
}

