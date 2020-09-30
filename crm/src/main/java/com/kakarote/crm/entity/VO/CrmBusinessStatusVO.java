package com.kakarote.crm.entity.VO;

import com.kakarote.crm.entity.PO.CrmBusinessStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author zhangzhiwei
 *
 */
@Data
@ToString
@ApiModel("crm商机状态VO")
public class CrmBusinessStatusVO {

    @ApiModelProperty("商机ID")
    public Integer businessId;

    @ApiModelProperty("商机状态ID")
    public Integer statusId;

    @ApiModelProperty("1赢单2输单3无效")
    public Integer isEnd;

    @ApiModelProperty("状态备注")
    public String statusRemark;

    public List<CrmBusinessStatus> statusList;
}
