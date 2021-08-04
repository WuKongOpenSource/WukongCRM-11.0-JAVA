package com.kakarote.hrm.entity.VO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class QueryAttendancePageVO {
    @ApiModelProperty(value = "打卡记录id")
    @TableId(value = "clock_id", type = IdType.AUTO)
    private Integer clockId;

    @ApiModelProperty(value = "打卡人")
    private Integer clockEmployeeId;

    @ApiModelProperty(value = "打卡时间")
    private Date clockTime;

    @ApiModelProperty(value = "打卡类型 1 上班打卡 2 下班打卡")
    private Integer clockType;

    @ApiModelProperty(value = "上班日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date attendanceTime;

    @ApiModelProperty(value = "打卡类型 1手机端打卡 2手工录入")
    private Integer type;

    @ApiModelProperty(value = "考勤地址")
    private String address;

    @ApiModelProperty(value = "精度")
    private String lng;

    @ApiModelProperty(value = "维度")
    private String lat;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "员工名称")
    private String employeeName;

    @ApiModelProperty(value = "工号")
    private String jobNumber;

    @ApiModelProperty(value = "部门")
    private String deptName;

    @ApiModelProperty(value = "岗位")
    private String post;
}
