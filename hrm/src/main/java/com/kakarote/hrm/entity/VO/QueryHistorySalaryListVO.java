package com.kakarote.hrm.entity.VO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class QueryHistorySalaryListVO {

    @TableId(value = "s_record_id", type = IdType.AUTO)
    @ApiModelProperty("历史薪资id")
    private Integer sRecordId;

    @ApiModelProperty("报表标题")
    private String title;

    @ApiModelProperty(value = "计薪人数")
    private Integer num;

    @ApiModelProperty(value = "预计应发工资")
    private BigDecimal expectedPaySalary;

    @ApiModelProperty(value = "个人所得税")
    private BigDecimal personalTax;

    @ApiModelProperty(value = "预计实发工资")
    private BigDecimal realPaySalary;

    @ApiModelProperty(value = "审批记录id")
    private Integer examineRecordId;

    @ApiModelProperty("状态状态 0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交")
    private Integer checkStatus;

    @ApiModelProperty(value = "创建id")
    private Long createUserId;

}
