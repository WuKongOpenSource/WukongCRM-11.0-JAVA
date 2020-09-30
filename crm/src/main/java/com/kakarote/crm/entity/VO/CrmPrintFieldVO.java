package com.kakarote.crm.entity.VO;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("查询字段列表")
public class CrmPrintFieldVO {

    private List<CrmModelFiledVO> business;

    private List<CrmModelFiledVO> contract;

    private List<CrmModelFiledVO> contacts;

    private List<CrmModelFiledVO> receivables;

    private List<CrmModelFiledVO> customer;

    private List<CrmModelFiledVO> product;
}
