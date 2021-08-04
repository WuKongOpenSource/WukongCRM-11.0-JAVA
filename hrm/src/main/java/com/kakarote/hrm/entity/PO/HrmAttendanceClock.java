package com.kakarote.hrm.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 打卡记录表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-12-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_attendance_clock")
@ApiModel(value="HrmAttendanceClock对象", description="打卡记录表")
public class HrmAttendanceClock implements Serializable {

    private static final long serialVersionUID=1L;

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

    @ApiModelProperty("员工名称")
    @TableField(exist = false)
    private String employeeName;


}
