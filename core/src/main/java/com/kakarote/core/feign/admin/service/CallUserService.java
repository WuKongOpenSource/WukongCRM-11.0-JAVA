package com.kakarote.core.feign.admin.service;

import com.kakarote.core.common.Result;
import com.kakarote.core.feign.admin.entity.CallUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Ian
 * @date 2020/8/28
 */
@FeignClient(name = "admin",contextId = "call")
public interface CallUserService {

    /**
    * 员工坐席授权
    * @date 2020/8/28 14:19
    * @param callUser
    * @return
    **/
    @PostMapping("/adminUserHisTable/authorize")
    Result<Boolean> authorize(@RequestBody CallUser callUser);


    /**
     * 判断用户是否为坐席
     * @date 2020/8/28 14:19
     * @return
     **/
    @PostMapping("/adminUserHisTable/checkAuth")
    Result<Integer> checkAuth();

}
