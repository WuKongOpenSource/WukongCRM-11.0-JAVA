package com.kakarote.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.feign.admin.service.AdminMessageService;
import com.kakarote.core.feign.examine.service.ExamineService;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.core.utils.BaseUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.entity.PO.*;
import com.kakarote.crm.service.*;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author JiaS
 * @date 2020/11/13
 */
@Slf4j
@Service
public class CrmCommonServiceImpl implements ICrmCommonService {

    @Autowired
    private ICrmActivityService crmActivityService;

    @Autowired
    private ICrmActivityRelationService crmActivityRelationService;

    @Autowired
    private ICrmBusinessService crmBusinessService;
    @Autowired
    private ICrmBusinessChangeService crmBusinessChangeService;
    @Autowired
    private ICrmBusinessDataService crmBusinessDataService;
    @Autowired
    private ICrmBusinessProductService crmBusinessProductService;
    @Autowired
    private ICrmBusinessUserStarService crmBusinessUserStarService;

    @Autowired
    private ICrmContactsService crmContactsService;
    @Autowired
    private ICrmContactsBusinessService crmContactsBusinessService;
    @Autowired
    private ICrmContactsDataService crmContactsDataService;
    @Autowired
    private ICrmContactsUserStarService crmContactsUserStarService;

    @Autowired
    private ICrmContractService crmContractService;
    @Autowired
    private ICrmContractDataService crmContractDataService;
    @Autowired
    private ICrmContractProductService crmContractProductService;

    @Autowired
    private ICrmCustomerService crmCustomerService;
    @Autowired
    private ICrmCustomerDataService crmCustomerDataService;
    @Autowired
    private ICrmCustomerSettingService crmCustomerSettingService;
    @Autowired
    private ICrmCustomerSettingUserService crmCustomerSettingUserService;
    @Autowired
    private ICrmCustomerUserStarService crmCustomerUserStarService;

    @Autowired
    private ICrmExamineLogService crmExamineLogService;
    @Autowired
    private ICrmExamineRecordService crmExamineRecordService;
    @Autowired
    private ICrmExamineStepService crmExamineStepService;

    @Autowired
    private ICrmFieldSortService crmFieldSortService;

    @Autowired
    private ICrmInvoiceService crmInvoiceService;
    @Autowired
    private ICrmInvoiceInfoService crmInvoiceInfoService;

    @Autowired
    private ICrmLeadsService crmLeadsService;
    @Autowired
    private ICrmLeadsDataService crmLeadsDataService;
    @Autowired
    private ICrmLeadsUserStarService crmLeadsUserStarService;

    @Autowired
    private ICrmNumberSettingService crmNumberSettingService;

    @Autowired
    private ICrmOwnerRecordService crmOwnerRecordService;

    @Autowired
    private ICrmPrintRecordService crmPrintRecordService;

    @Autowired
    private ICrmProductService crmProductService;
    @Autowired
    private ICrmProductDataService crmProductDataService;
    @Autowired
    private ICrmProductDetailImgService crmProductDetailImgService;
    @Autowired
    private ICrmProductUserService crmProductUserService;

    @Autowired
    private ICrmReceivablesService crmReceivablesService;
    @Autowired
    private ICrmReceivablesDataService crmReceivablesDataService;
    @Autowired
    private ICrmReceivablesPlanService crmReceivablesPlanService;

    @Autowired
    private ICrmReturnVisitService crmReturnVisitService;
    @Autowired
    private ICrmReturnVisitDataService crmReturnVisitDataService;

    @Autowired
    private ICrmActionRecordService crmActionRecordService;
    @Autowired
    private ICrmBackLogDealService crmBackLogDealService;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private AdminFileService adminFileService;

    @Autowired
    private ICrmCustomerPoolRelationService crmCustomerPoolRelationService;

    @Autowired
    private ICrmCustomerPoolFieldSettingService crmCustomerPoolFieldSettingService;

    @Autowired
    private ICrmCustomerPoolFieldSortService crmCustomerPoolFieldSortService;

    @Autowired
    private ICrmCustomerPoolFieldStyleService crmCustomerPoolFieldStyleService;

    @Autowired
    private ICrmCustomerPoolRuleService crmCustomerPoolRuleService;

    @Autowired
    private AdminMessageService adminMessageService;

    @Autowired
    private ICrmMarketingService crmMarketingService;

    @Autowired
    private ICrmMarketingInfoService crmMarketingInfoService;

    @Autowired
    private ICrmMarketingFieldService crmMarketingFieldService;

    @Autowired
    private ICrmMarketingFormService crmMarketingFormService;

    @Autowired
    private ExamineService examineService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean initCrmData(){
        if (!UserUtil.isAdmin()){
            if (this.verifyInitAuth()) {
                throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
            }
        }
        log.info("开始初始化客户管理模块数据！");
        crmActivityService.lambdaUpdate().remove();

        this.deleteFile(crmBusinessService,CrmBusiness::getBatchId,CrmBusiness::getBatchId);
        crmBusinessService.lambdaUpdate().remove();
        crmBusinessChangeService.lambdaUpdate().remove();
        crmBusinessDataService.lambdaUpdate().remove();
        crmBusinessProductService.lambdaUpdate().remove();
        crmBusinessUserStarService.lambdaUpdate().remove();

        this.deleteFile(crmContactsService,CrmContacts::getBatchId,CrmContacts::getBatchId);
        crmContactsService.lambdaUpdate().remove();
        crmContactsDataService.lambdaUpdate().remove();
        crmContactsBusinessService.lambdaUpdate().remove();
        crmContactsUserStarService.lambdaUpdate().remove();

        this.deleteFile(crmContractService,CrmContract::getBatchId,CrmContract::getBatchId);
        crmContractService.lambdaUpdate().remove();
        crmContractDataService.lambdaUpdate().remove();
        crmContractProductService.lambdaUpdate().remove();

        this.deleteFile(crmCustomerService,CrmCustomer::getBatchId,CrmCustomer::getBatchId);
        crmCustomerService.lambdaUpdate().remove();
        crmCustomerDataService.lambdaUpdate().remove();
        crmCustomerSettingService.lambdaUpdate().remove();
        crmCustomerSettingUserService.lambdaUpdate().remove();
        crmCustomerUserStarService.lambdaUpdate().remove();

        crmExamineLogService.lambdaUpdate().remove();
        crmExamineRecordService.lambdaUpdate().remove();
        crmExamineStepService.lambdaUpdate().remove();

        crmFieldSortService.lambdaUpdate().remove();

        crmInvoiceService.lambdaUpdate().remove();
        crmInvoiceInfoService.lambdaUpdate().remove();

        this.deleteFile(crmLeadsService,CrmLeads::getBatchId,CrmLeads::getBatchId);
        crmLeadsService.lambdaUpdate().remove();
        crmLeadsDataService.lambdaUpdate().remove();
        crmLeadsUserStarService.lambdaUpdate().remove();


        crmOwnerRecordService.lambdaUpdate().remove();

        crmPrintRecordService.lambdaUpdate().remove();

        this.deleteFile(crmProductService,CrmProduct::getBatchId,CrmProduct::getBatchId);
        crmProductService.lambdaUpdate().remove();
        crmProductDataService.lambdaUpdate().remove();
        crmProductDetailImgService.lambdaUpdate().remove();
        crmProductUserService.lambdaUpdate().remove();

        crmReceivablesService.lambdaUpdate().remove();
        crmReceivablesDataService.lambdaUpdate().remove();
        crmReceivablesPlanService.lambdaUpdate().remove();

        crmReturnVisitService.lambdaUpdate().remove();
        crmReturnVisitDataService.lambdaUpdate().remove();

        crmCustomerPoolRelationService.lambdaUpdate().remove();
        crmCustomerPoolFieldSettingService.lambdaUpdate().remove();
        crmCustomerPoolFieldSortService.lambdaUpdate().remove();
        crmCustomerPoolFieldStyleService.lambdaUpdate().remove();
        crmCustomerPoolRuleService.lambdaUpdate().remove();

        crmMarketingService.lambdaUpdate().remove();
        crmMarketingInfoService.lambdaUpdate().remove();
        crmMarketingFieldService.lambdaUpdate().remove();
        crmMarketingFormService.lambdaUpdate().remove();

        adminMessageService.deleteByLabel(6);

        crmActionRecordService.lambdaUpdate().remove();
        crmBackLogDealService.lambdaUpdate().remove();

        examineService.deleteExamineRecordAndLog(1);
        examineService.deleteExamineRecordAndLog(2);
        examineService.deleteExamineRecordAndLog(3);
        log.info("客户管理模块数据初始化成功。开始更新es数据！");
        return this.deleteByQuery(elasticsearchRestTemplate.getClient()) >= 0;
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
    private <T> void deleteFile(BaseService<T> baseService, SFunction<T,String> resultColumn,Function<T,String> mapper){
        LambdaQueryWrapper<T> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(resultColumn);
        List<T> list = baseService.list(lambdaQueryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            List<String> batchIds = list.stream().map(mapper).collect(Collectors.toList());
            batchIds = batchIds.stream().distinct().collect(Collectors.toList());
            adminFileService.delete(batchIds);
        }
    }


    /**
     * 清除es数据
     * @date 2020/11/13 13:35
     * @param client
     * @return long
     **/
    private long deleteByQuery(RestHighLevelClient client) {
        //参数为索引名，可以不指定，可以一个，可以多个
        DeleteByQueryRequest request = new DeleteByQueryRequest("_all");
        // 更新时版本冲突
        request.setConflicts("proceed");
        request.setSize(1000);
        // 刷新索引
        request.setRefresh(true);
        try {
            long size = 1000,updateNum = 0;
            while (size > 0) {
                BulkByScrollResponse response = client.deleteByQuery(request, RequestOptions.DEFAULT);
                size = response.getStatus().getUpdated();
                updateNum += size;
            }
            log.info("es数据更新成功。本次更新清除了{}条无用数据！", updateNum);
            return updateNum;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
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
