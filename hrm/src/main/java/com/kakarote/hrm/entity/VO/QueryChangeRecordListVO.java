package com.kakarote.hrm.entity.VO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryChangeRecordListVO {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "员工id")
    private Integer employeeId;

    @ApiModelProperty(value = "记录类型 1 定薪 2 调薪")
    private Integer recordType;

    @ApiModelProperty(value = "调薪原因 1 入职核定 2 转正 3 晋升 4 调动 5 年中调薪 6 年度调薪 7 特别调薪 8 其他")
    private Integer changeReason;

    @ApiModelProperty(value = "生效时间")
    private String enableDate;

    @ApiModelProperty(value = "调整前总工资")
    private String beforeTotal;

    @ApiModelProperty(value = "调整后总工资")
    private String afterTotal;

    @ApiModelProperty(value = "状态 0 未生效 1 已生效 2 已取消")
    private Integer status;

    @ApiModelProperty("员工状态")
    private Integer employeeStatus;

    @ApiModelProperty(value = "备注")
    private String remarks;
}
