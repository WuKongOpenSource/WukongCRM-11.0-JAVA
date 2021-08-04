package com.kakarote.hrm.entity.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FixSalaryRecordDetailVO extends SalaryRecordHeadVO{

    private Integer id;

    @ApiModelProperty(value = "状态 0 未生效 1 已生效 2 已取消")
    private Integer status;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty(value = "调薪原因 0 入职定薪 1 入职核定 2 转正 3 晋升 4 调动 5 年中调薪 6 年度调薪 7 特别调薪 8 其他")
    private Integer changeReason;

    @ApiModelProperty("试用期工资")
    private List<ChangeSalaryOptionVO> proSalary;

    @ApiModelProperty("转正后工资")
    private List<ChangeSalaryOptionVO> salary;

    @ApiModelProperty("试用期总计")
    private String proSum;

    @ApiModelProperty("转正后总计")
    private String sum;

    @ApiModelProperty("是否可以更新")
    private Boolean isUpdate;


}
