package com.kakarote.crm.common;

import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.service.ICrmFieldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InitEsIndexRunner implements ApplicationRunner {

    @Autowired
    private ElasticsearchRestTemplate restTemplate;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (CrmEnum value : CrmEnum.values()) {
            if (!value.equals(CrmEnum.RECEIVABLES_PLAN) && !value.equals(CrmEnum.CUSTOMER_POOL)
                    && !value.equals(CrmEnum.MARKETING) && !value.equals(CrmEnum.INVOICE)
                    &&!restTemplate.indexExists(value.getIndex())){
                ApplicationContextHolder.getBean(ICrmFieldService.class).initData(value.getType());
                log.info("es {} index init success!",value.getIndex());
            }
        }
    }
}
