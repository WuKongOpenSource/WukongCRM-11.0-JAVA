package com.kakarote.work.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.feign.admin.service.AdminMessageService;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.core.utils.BaseUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.work.entity.PO.Work;
import com.kakarote.work.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author JiaS
 * @date 2020/11/16
 */
@Slf4j
@Service
public class WorkCommonServiceImpl implements IWorkCommonService {

    @Autowired
    private IWorkService workService;
    @Autowired
    private IWorkCollectService workCollectService;
    @Autowired
    private IWorkOrderService workOrderService;

    @Autowired
    private IWorkTaskService workTaskService;
    @Autowired
    private IWorkTaskClassService workTaskClassService;
    @Autowired
    private IWorkTaskCommentService workTaskCommentService;
    @Autowired
    private IWorkTaskLabelService workTaskLabelService;
    @Autowired
    private IWorkTaskLabelOrderService workTaskLabelOrderService;
    @Autowired
    private IWorkTaskLogService workTaskLogService;
    @Autowired
    private IWorkTaskRelationService workTaskRelationService;
    @Autowired
    private IWorkUserService workUserService;

    @Autowired
    private AdminFileService adminFileService;

    @Autowired
    private AdminMessageService adminMessageService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean initWorkData() {
        if (!UserUtil.isAdmin()) {
            if (this.verifyInitAuth()) {
                throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
            }
        }
        log.info("开始初始化项目管理模块数据！");
        this.deleteFile(workService,Work::getBatchId,Work::getBatchId);
        workService.lambdaUpdate().remove();
        workCollectService.lambdaUpdate().remove();
        workOrderService.lambdaUpdate().remove();

        workTaskService.lambdaUpdate().remove();
        workTaskClassService.lambdaUpdate().remove();
        workTaskCommentService.lambdaUpdate().remove();
        workTaskLabelService.lambdaUpdate().remove();
        workTaskLabelOrderService.lambdaUpdate().remove();
        workTaskLogService.lambdaUpdate().remove();
        workTaskRelationService.lambdaUpdate().remove();
        workUserService.lambdaUpdate().remove();

        adminMessageService.deleteByLabel(1);
        log.info("项目管理模块数据初始化完成！");
        return true;
    }



    /**
     * 删除附件
     * @date 2020/11/20 15:41
     * @param baseService
     * @param resultColumn
     * @param queryColumn
     * @param mapper
     * @return void
     **/
    private <T> void deleteFile(BaseService<T> baseService, SFunction<T,String> resultColumn, Function<T,String> mapper){
        LambdaQueryWrapper<T> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(resultColumn);
        List<T> list = baseService.list(lambdaQueryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            List<String> batchIds = list.stream().map(mapper).collect(Collectors.toList());
            batchIds = batchIds.stream().distinct().collect(Collectors.toList());
            adminFileService.delete(batchIds);
        }
    }


    private static final String INIT_AUTH_URL = "/adminConfig/moduleInitData";


    /**
     * 验证非管理员有无权限
     * @date 2020/11/23 10:35
     * @param
     * @return boolean
     **/
    private boolean verifyInitAuth(){
        boolean isNoAuth = false;
        Long userId = UserUtil.getUserId();
        String key = userId.toString();
        List<String> noAuthMenuUrls = BaseUtil.getRedis().get(key);
        if (noAuthMenuUrls != null && noAuthMenuUrls.contains(INIT_AUTH_URL)) {
            isNoAuth = true;
        }
        return isNoAuth;
    }
}
