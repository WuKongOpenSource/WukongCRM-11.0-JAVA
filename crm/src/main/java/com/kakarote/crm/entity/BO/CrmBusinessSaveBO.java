package com.kakarote.crm.entity.BO;

import com.kakarote.crm.entity.PO.CrmBusinessProduct;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangzhiwei
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
@ApiModel("crm商机保存对象")
public class CrmBusinessSaveBO extends CrmModelSaveBO {
    @ApiModelProperty("商机关联产品列表")
    private List<CrmBusinessProduct> product = new ArrayList<>();

    @ApiModelProperty("联系人ID")
    private Integer contactsId;
}
