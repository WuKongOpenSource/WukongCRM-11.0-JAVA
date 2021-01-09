package com.kakarote.core.feign.jxc.service;


import com.kakarote.core.common.Result;
import com.kakarote.core.feign.crm.entity.ExamineField;
import com.kakarote.core.feign.jxc.entity.JxcExamine;
import com.kakarote.core.feign.jxc.entity.JxcState;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


@FeignClient(name = "jxc",contextId = "jxcExamine")
public interface JxcExamineService {
    @PostMapping("/jxcExamine/examine")
    Result examine(@RequestParam("label") Integer label, @RequestParam("state") Integer state, @RequestParam("id") Integer id);

    @PostMapping("/jxcExamine/examineFieldDataMap")
    Result<Map<String, Object>> examineFieldDataMap(@RequestParam("label") Integer label, @RequestParam("id") Integer id);

    @PostMapping("/jxcExamine/examineMessage")
    Result examineMessage(@RequestBody JxcExamine jxcExamine);

    @PostMapping("/jxcExamine/queryJxcById")
    Result<JxcState> queryJxcById(@RequestParam("label") Integer label,@RequestParam("id") Integer id);


    @PostMapping("/jxcExamine/queryExamineField")
    Result<List<ExamineField>> queryExamineField(@RequestParam("label") Integer label);

}
