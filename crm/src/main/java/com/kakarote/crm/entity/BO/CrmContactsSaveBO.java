package com.kakarote.crm.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author zhangzhiwei
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
@ApiModel("crm联系人保存对象")
public class CrmContactsSaveBO extends CrmModelSaveBO {
    @ApiModelProperty("商机ID")
    private Integer businessId;
}
