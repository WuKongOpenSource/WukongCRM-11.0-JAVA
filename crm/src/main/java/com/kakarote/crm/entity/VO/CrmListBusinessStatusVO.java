package com.kakarote.crm.entity.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author zhangzhiwei
 *
 */
@Data
@ToString
@ApiModel("crm商机状态VO")
public class CrmListBusinessStatusVO {

    @ApiModelProperty("1赢单2输单3无效")
    public Integer isEnd;

    @ApiModelProperty("状态备注")
    public String statusName;

    @ApiModelProperty("当前进度")
    public Integer currentProgress;

    @ApiModelProperty("总进度")
    public Integer totalProgress;

}
