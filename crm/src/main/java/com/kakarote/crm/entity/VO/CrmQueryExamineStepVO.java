package com.kakarote.crm.entity.VO;

import com.kakarote.crm.entity.PO.CrmExamine;
import com.kakarote.crm.entity.PO.CrmExamineStep;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("审批返回对象")
public class CrmQueryExamineStepVO extends CrmExamine {

    @ApiModelProperty(value = "审批用户")
    private Long examineUser;

    @ApiModelProperty(value = "审批用户名称")
    private String examineUserName;

    @ApiModelProperty(value = "审批步骤")
    private List<CrmExamineStep> stepList;
}
