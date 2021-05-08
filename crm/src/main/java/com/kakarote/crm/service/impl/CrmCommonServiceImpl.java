package com.kakarote.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.feign.admin.service.AdminMessageService;
import com.kakarote.core.feign.examine.service.ExamineService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.core.utils.BaseUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.PO.*;
import com.kakarote.crm.service.*;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private ICrmBusinessService crmBusinessService;

    @Autowired
    private ICrmContactsService crmContactsService;

    @Autowired
    private ICrmContractService crmContractService;
   
    @Autowired
    private ICrmCustomerService crmCustomerService;

    @Autowired
    private ICrmFieldSortService crmFieldSortService;

    @Autowired
    private ICrmInvoiceService crmInvoiceService;

    @Autowired
    private ICrmLeadsService crmLeadsService;

    @Autowired
    private ICrmOwnerRecordService crmOwnerRecordService;

    @Autowired
    private ICrmPrintRecordService crmPrintRecordService;

    @Autowired
    private ICrmProductService crmProductService;

    @Autowired
    private ICrmReceivablesService crmReceivablesService;

    @Autowired
    private ICrmReturnVisitService crmReturnVisitService;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private AdminFileService adminFileService;

    @Autowired
    private AdminMessageService adminMessageService;

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
        ApplicationContextHolder.getBean(ICrmActivityService.class).lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmActivityRelationService.class).lambdaUpdate().remove();
        this.deleteFile(crmBusinessService,CrmBusiness::getBatchId,CrmBusiness::getBatchId);
        crmBusinessService.lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmBusinessChangeService.class).lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmBusinessDataService.class).lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmBusinessProductService.class).lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmBusinessUserStarService.class).lambdaUpdate().remove();

        this.deleteFile(crmContactsService,CrmContacts::getBatchId,CrmContacts::getBatchId);
        crmContactsService.lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmContactsDataService.class).lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmContactsBusinessService.class).lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmContactsUserStarService.class).lambdaUpdate().remove();

        this.deleteFile(crmContractService,CrmContract::getBatchId,CrmContract::getBatchId);
        crmContractService.lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmContractDataService.class).lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmContractProductService.class).lambdaUpdate().remove();

        this.deleteFile(crmCustomerService,CrmCustomer::getBatchId,CrmCustomer::getBatchId);
        crmCustomerService.lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmCustomerDataService.class).lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmCustomerSettingService.class).lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmCustomerSettingUserService.class).lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmCustomerUserStarService.class).lambdaUpdate().remove();
        
        crmFieldSortService.lambdaUpdate().remove();

        crmInvoiceService.lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmInvoiceDataService.class).lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmInvoiceInfoService.class).lambdaUpdate().remove();

        this.deleteFile(crmLeadsService,CrmLeads::getBatchId,CrmLeads::getBatchId);
        crmLeadsService.lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmLeadsDataService.class).lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmLeadsUserStarService.class).lambdaUpdate().remove();


        crmOwnerRecordService.lambdaUpdate().remove();
        crmPrintRecordService.lambdaUpdate().remove();

        this.deleteFile(crmProductService,CrmProduct::getBatchId,CrmProduct::getBatchId);
        crmProductService.lambdaUpdate().remove();

        ApplicationContextHolder.getBean(ICrmProductDataService.class).lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmProductDetailImgService.class).lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmProductUserService.class).lambdaUpdate().remove();

        crmReceivablesService.lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmReceivablesDataService.class).lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmReceivablesPlanService.class).lambdaUpdate().remove();

        crmReturnVisitService.lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmReturnVisitDataService.class).lambdaUpdate().remove();

        ApplicationContextHolder.getBean(ICrmCustomerPoolRelationService.class).lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmCustomerPoolFieldSettingService.class).lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmCustomerPoolFieldSortService.class).lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmCustomerPoolFieldStyleService.class).lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmCustomerPoolRuleService.class).lambdaUpdate().remove();

        ApplicationContextHolder.getBean(ICrmMarketingService.class).lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmMarketingInfoService.class).lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmMarketingFieldService.class).lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmMarketingFormService.class).lambdaUpdate().remove();

        adminMessageService.deleteByLabel(6);

        ApplicationContextHolder.getBean(ICrmActionRecordService.class).lambdaUpdate().remove();
        ApplicationContextHolder.getBean(ICrmBackLogDealService.class).lambdaUpdate().remove();

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


    /**
     * 清除es数据
     * @date 2020/11/13 13:35
     * @param client
     * @return long
     **/
    private long deleteByQuery(RestHighLevelClient client) {
        List<String> indexList = new ArrayList<>();
        for (CrmEnum value : CrmEnum.values()) {
            if (!value.equals(CrmEnum.RECEIVABLES_PLAN) && !value.equals(CrmEnum.CUSTOMER_POOL) && !value.equals(CrmEnum.MARKETING)){
                indexList.add(value.getIndex());
            }
        }
        //参数为索引名，可以不指定，可以一个，可以多个
        DeleteByQueryRequest request = new DeleteByQueryRequest(indexList.toArray(new String[0]));
        // 更新时版本冲突
        request.setConflicts("proceed");
        request.setSize(1000);
        // 设置查询条件，第一个参数是字段名，第二个参数是字段的值
        request.setQuery(QueryBuilders.matchAllQuery());
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
