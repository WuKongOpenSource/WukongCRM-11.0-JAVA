package com.kakarote.admin.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.kakarote.admin.entity.BO.AdminMessageQueryBO;
import com.kakarote.admin.entity.PO.AdminMessage;
import com.kakarote.admin.entity.VO.AdminMessageVO;
import com.kakarote.admin.service.IAdminMessageService;
import com.kakarote.core.common.ApiExplain;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.cache.AdminCacheKey;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessageBO;
import com.kakarote.core.redis.Redis;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 系统消息表 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
@RestController
@RequestMapping("/adminMessage")
@Api(tags = "系统消息")
@Slf4j
public class AdminMessageController {

    @Autowired
    private IAdminMessageService messageService;

    @Autowired
    private Redis redis;

    @PostMapping("/save")
    public Result<AdminMessage> save(@RequestBody com.kakarote.core.feign.admin.entity.AdminMessage adminMessage) {
        AdminMessage adminMessage1 = BeanUtil.copyProperties(adminMessage, AdminMessage.class);
        if (adminMessage.getCreateTime() != null){
            log.info("saveMessage:{}",adminMessage.getCreateTime());
            adminMessage1.setCreateTime(DateUtil.parseDateTime(adminMessage.getCreateTime()));
        }
        messageService.save(adminMessage1);
        return Result.ok(adminMessage1);
    }

    @PostMapping("/update")
    public Result<AdminMessage> update(@RequestBody com.kakarote.core.feign.admin.entity.AdminMessage adminMessage) {
        AdminMessage adminMessage1 = BeanUtil.copyProperties(adminMessage, AdminMessage.class);
        if (adminMessage.getCreateTime() != null){
            adminMessage1.setCreateTime(DateUtil.parseDateTime(adminMessage.getCreateTime()));
        }
        messageService.updateById(adminMessage1);
        return Result.ok(adminMessage1);
    }

    @PostMapping("/saveOrUpdateMessage")
    public Result<Long> saveOrUpdateMessage(@RequestBody com.kakarote.core.feign.admin.entity.AdminMessage message) {
        Long messageId = messageService.saveOrUpdateMessage(message);
        return Result.ok(messageId);
    }

    @PostMapping("/queryList")
    @ApiOperation("查询消息列表")
    public Result<BasePage<AdminMessage>> queryList(@RequestBody AdminMessageQueryBO adminMessageBO) {
        BasePage<AdminMessage> adminMessageBasePage = messageService.queryList(adminMessageBO);
        return Result.ok(adminMessageBasePage);
    }

    @PostMapping("/readMessage")
    @ApiOperation("单个标记为已读")
    public Result readMessage(@RequestParam("messageId") Long messageId) {
        AdminMessage byId = messageService.getById(messageId);
        if (byId != null) {
            byId.setIsRead(1);
            messageService.updateById(byId);
        }
        return Result.ok();
    }

    @PostMapping("/readAllMessage")
    @ApiOperation("全部标记为已读")
    public Result readAllMessage(Integer label) {
        LambdaUpdateChainWrapper<AdminMessage> wrapper = messageService.lambdaUpdate();
        wrapper.set(AdminMessage::getIsRead, 1);
        wrapper.eq(AdminMessage::getRecipientUser, UserUtil.getUserId());
        if (label != null) {
            wrapper.eq(AdminMessage::getLabel, label);
        }
        wrapper.update();
        return Result.ok();
    }

    @PostMapping("/clear")
    @ApiOperation("删除已读消息")
    public Result clear(Integer label) {
        LambdaUpdateChainWrapper<AdminMessage> wrapper = messageService.lambdaUpdate();
        wrapper.eq(AdminMessage::getIsRead, 1);
        wrapper.eq(AdminMessage::getRecipientUser, UserUtil.getUserId());
        if (label != null) {
            wrapper.eq(AdminMessage::getLabel, label);
        }
        wrapper.remove();
        return Result.ok();
    }

    @PostMapping("/getById/{messageId}")
    public Result<AdminMessage> getById(@PathVariable Long messageId) {
        AdminMessage adminMessage = messageService.getById(messageId);
        return Result.ok(adminMessage);
    }

    @PostMapping("/queryUnreadCount")
    @ApiOperation("查询未读消息")
    public Result<AdminMessageVO> queryUnreadCount() {
        AdminMessageVO messageVO = messageService.queryUnreadCount();
        return Result.ok(messageVO);
    }

    @PostMapping("/queryImportNum")
    @ApiOperation("查询导入数量")
    public Result<Integer> queryImportNum(Long messageId) {
        boolean exists = redis.exists(AdminCacheKey.UPLOAD_EXCEL_MESSAGE_PREFIX + messageId.toString());
        Integer num = null;
        if (exists) {
            num = redis.get(AdminCacheKey.UPLOAD_EXCEL_MESSAGE_PREFIX + messageId.toString());
        }
        return Result.ok(num);
    }

    @PostMapping("/queryImportInfo")
    @ApiOperation("查询导入信息")
    public Result<JSONObject> queryImportInfo(@RequestParam("messageId") Long messageId) {
        AdminMessage adminMessage = messageService.getById(messageId);
        if (adminMessage != null && adminMessage.getContent() != null) {
            String[] content = adminMessage.getContent().split(",");
            JSONObject r = new JSONObject().fluentPut("totalSize", adminMessage.getTitle()).fluentPut("errSize", content[0]);
            r.put("updateSize", content.length > 1 ? content[1] : 0);
            return Result.ok(r);
        } else {
            return Result.ok(new JSONObject().fluentPut("totalSize", 0).fluentPut("errSize", 0).fluentPut("updateSize", 0));
        }
    }

    @PostMapping("/downImportError")
    @ApiOperation("下载错误模板")
    public void downImportError(@RequestParam("messageId") Long messageId, HttpServletResponse response) {
        String str = redis.get(AdminCacheKey.UPLOAD_EXCEL_MESSAGE_PREFIX + "file:" + messageId.toString());
        final boolean exist = FileUtil.exist(str);
        if (exist) {
            ServletUtil.write(response, FileUtil.file(str));
        }
    }

    @PostMapping("/sendMessage")
    @ApiExplain("发送消息")
    public Result sendMessage(@RequestBody AdminMessageBO adminMessageBO) {
        messageService.addMessage(adminMessageBO);
        return Result.ok();
    }

    @PostMapping("/deleteEventMessage")
    @ApiExplain("删除日程消息")
    public Result deleteEventMessage(@RequestParam("eventId")Integer eventId){
        messageService.deleteEventMessage(eventId);
        return Result.ok();
    }

    @PostMapping("/deleteById/{messageId}")
    @ApiOperation("删除通知")
    public Result deleteById(@PathVariable("messageId") Integer messageId) {
        messageService.deleteById(messageId);
        return Result.ok();
    }

    @PostMapping("/deleteByLabel")
    public Result deleteByLabel(@RequestParam("label") Integer label){
        messageService.deleteByLabel(label);
        return Result.ok();
    }
}

