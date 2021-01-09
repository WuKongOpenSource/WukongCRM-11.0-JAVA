package com.kakarote.core.feign.admin.service;

import com.kakarote.core.common.Result;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.feign.admin.entity.AdminMessageBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "admin",contextId = "message")
public interface AdminMessageService {

    @PostMapping("/adminMessage/save")
    public Result<AdminMessage> save(@RequestBody AdminMessage adminMessage);

    @PostMapping("/adminMessage/update")
    public Result<AdminMessage> update(@RequestBody AdminMessage adminMessage);

    @PostMapping("/adminMessage/getById/{messageId}")
    public Result<AdminMessage> getById(@PathVariable Long messageId);

    @PostMapping("/adminMessage/sendMessage")
    public Result<AdminMessage> sendMessage(@RequestBody AdminMessageBO adminMessageBO);

    @PostMapping("/adminMessage/deleteEventMessage")
    public Result deleteEventMessage(@RequestParam("eventId")Integer eventId);

    @PostMapping("/adminMessage/deleteByLabel")
    public Result deleteByLabel(@RequestParam("label") Integer label);
}
