package com.kakarote.hrm.entity.BO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.hrm.entity.VO.ChangeSalaryOptionVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class BatchChangeSalaryRecordBO {

    @ApiModelProperty("员工id")
    private List<Integer> employeeIds;

    @ApiModelProperty("部门id")
    private List<Integer> deptIds;

    @ApiModelProperty("类型 1 按比例调薪 2 按金额调薪")
    private Integer type;

    @ApiModelProperty("薪资项")
    private List<ChangeSalaryOptionVO> salaryOptions;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty(value = "调薪原因 0 入职定薪 1 入职核定 2 转正 3 晋升 4 调动 5 年中调薪 6 年度调薪 7 特别调薪 8 其他")
    private Integer changeReason;

    @ApiModelProperty(value = "生效时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date enableDate;



}
