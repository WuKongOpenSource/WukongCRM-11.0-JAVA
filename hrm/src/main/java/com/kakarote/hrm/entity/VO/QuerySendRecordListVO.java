package com.kakarote.hrm.entity.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class QuerySendRecordListVO {

    private Integer id;

    @ApiModelProperty(value = "创建人")
    private String createUserName;

    @ApiModelProperty(value = "创建人用户id")
    private Long createUserId;

    @ApiModelProperty(value = "发放时间")
    private Date createTime;

    @ApiModelProperty(value = "薪资表总人数")
    private Integer salaryNum;

    @ApiModelProperty(value = "发放人数")
    private Integer payNum;

    @ApiModelProperty(value = "已查看人数")
    private Integer readNum;

    private Integer year;

    private Integer month;
}
