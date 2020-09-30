package com.kakarote.admin.entity.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author zhangzhiwei
 * 云平台账号配置
 */
@ToString
@Data
@ApiModel("云平台账号配置")
public class CloudConfigVO {

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("可用人数")
    private Integer allNum;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("已使用人数")
    private Integer usingNum;
}
