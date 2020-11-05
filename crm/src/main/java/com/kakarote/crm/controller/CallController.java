package com.kakarote.crm.controller;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.cache.CrmCacheKey;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminConfig;
import com.kakarote.core.feign.admin.entity.CallUser;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.feign.admin.service.CallUserService;
import com.kakarote.core.feign.crm.entity.BiParams;
import com.kakarote.core.redis.Redis;
import com.kakarote.core.utils.BaseUtil;
import com.kakarote.crm.entity.BO.CallRecordBO;
import com.kakarote.crm.entity.PO.CallRecord;
import com.kakarote.crm.service.ICallRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Ian
 * @date 2020/8/27
 */
@RestController
@RequestMapping("/crmCall")
@Api(tags = "呼叫中心接口")
public class CallController {

    @Autowired
    private ICallRecordService callRecordService;
    @Autowired
    private CallUserService callUserService;

    @Autowired
    private Redis redis;

    @Autowired
    private AdminService adminService;


    @PostMapping("/save")
    @ApiOperation("添加通话记录")
    public Result save(@RequestBody CallRecord callRecord) {
        String id = CrmCacheKey.CRM_CALL_CACHE_KEY + callRecord.getNumber();
        if (redis.exists(id)) {
            return R.ok();
        }
        redis.setex(id, 5, 1);
        return R.ok(callRecordService.saveRecord(callRecord));
    }


    @PostMapping("/index")
    @ApiOperation("查询通话记录")
    public Result<BasePage<JSONObject>> index(@RequestBody CallRecordBO callRecordBO) {
        return R.ok(callRecordService.pageCallRecordList(callRecordBO));
    }

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    @ApiOperation("上传文件")
    public Result upload(@RequestParam("file") MultipartFile file, @RequestParam("id") String id) {
        String prefix = BaseUtil.getDate();
        callRecordService.upload(file, id, prefix);
        return R.ok();
    }

    @PostMapping("/download")
    @ApiOperation("录音下载")
    public void download(@RequestParam("id") String id, HttpServletResponse response) {
        callRecordService.download(id, response);
    }

    @PostMapping("/searchPhone")
    @ApiOperation("搜索呼入电话")
    public Result<JSONObject> searchPhone(@RequestParam("search") String search) {
        return R.ok(callRecordService.searchPhone(search));
    }

    @PostMapping("/queryPhoneNumber")
    @ApiOperation("查询可呼出电话")
    public Result<List<JSONObject>> queryPhoneNumber(@RequestParam("model") String model, @RequestParam("modelId") String modelId) {
        return R.ok(callRecordService.queryPhoneNumber(model, modelId));
    }

    @PostMapping("/analysis")
    @ApiOperation("通话记录分析")
    public Result<BasePage<JSONObject>> analysis(@RequestBody BiParams biParams) {
        return R.ok(callRecordService.analysis(biParams));
    }

    @PostMapping("/authorize")
    @ApiOperation("员工呼叫中心授权")
    public Result authorize(@RequestBody CallUser callUser) {
        return R.ok(callUserService.authorize(callUser).getData());
    }

    @PostMapping("/checkAuth")
    @ApiOperation("判断当前用户是否开启呼叫中心")
    public Result<JSONObject> checkAuth() {
        JSONObject jsonObject = new JSONObject();
        //查看用户权限
        AdminConfig adminConfig = adminService.queryFirstConfigByName("call").getData();
        boolean isStart = adminConfig != null && adminConfig.getStatus() == 1;
        jsonObject.put("auth", callUserService.checkAuth().getData() && isStart);
        jsonObject.put("status", isStart ? 1 : 0);
        return R.ok(jsonObject);
    }
}
