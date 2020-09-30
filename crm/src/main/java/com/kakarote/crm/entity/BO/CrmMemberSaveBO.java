package com.kakarote.crm.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author Administrator
 */
@Data
@ToString
@ApiModel("crm首要联系人BO对象")
public class CrmMemberSaveBO {

    @ApiModelProperty("id")
    private List<Integer> ids;

    @ApiModelProperty("成员ids")
    private List<Long> memberIds;

    @ApiModelProperty("权限（1.只读2.读写）")
    private Integer power;

    @ApiModelProperty("变更类型 1、联系人 2、商机 3、合同")
    private List<Integer> changeType;
}
