package com.kakarote.hrm.entity.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class QuerySalaryArchivesListVO {

    private Integer employeeId;

    @ApiModelProperty(value = "员工姓名")
    private String employeeName;

    @ApiModelProperty(value = "工号")
    private String jobNumber;

    @ApiModelProperty(value = "部门ID")
    private Integer deptId;

    @ApiModelProperty(value = "部门")
    private String deptName;

    @ApiModelProperty(value = "职位")
    private String post;

    @ApiModelProperty(value = "员工状态 1正式 2试用  3实习 4兼职 5劳务 6顾问 7返聘 8外包 ")
    private Integer status;

    @ApiModelProperty(value = "入职时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date entryTime;

    @ApiModelProperty(value = "转正日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date becomeTime;

    @ApiModelProperty(value = "调薪原因 1 入职核定 2 转正 3 晋升 4 调动 5 年中调薪 6 年度调薪 7 特别调薪 8 其他")
    private Integer changeReason;

    @ApiModelProperty(value = "最近调整日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date changeData;

    @ApiModelProperty(value = "调薪状态 0 未定薪 1 已定薪 2 已调薪")
    private Integer changeType;

    @ApiModelProperty(value = "工资合计")
    private String total;
}
