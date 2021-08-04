package com.kakarote.hrm.entity.VO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class QueryHistorySalaryDetailVO {

    @TableId(value = "s_record_id", type = IdType.AUTO)
    private Integer sRecordId;

    @ApiModelProperty("报表标题")
    private String title;

    @ApiModelProperty(value = "年份")
    private Integer year;

    @ApiModelProperty(value = "月份")
    private Integer month;

    @ApiModelProperty(value = "计薪人数")
    private Integer num;

    @ApiModelProperty("计薪开始时间")
    private Date startTime;

    @ApiModelProperty("计薪结束日期")
    private Date endTime;

    @ApiModelProperty(value = "审批记录id")
    private Integer examineRecordId;

    @ApiModelProperty("状态状态 0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交")
    private Integer checkStatus;

    @ApiModelProperty(value = "创建id")
    private Long createUserId;

    @ApiModelProperty(value = "个人社保")
    private BigDecimal personalInsuranceAmount;

    @ApiModelProperty(value = "个人公积金")
    private BigDecimal personalProvidentFundAmount;

    @ApiModelProperty(value = "企业社保")
    private BigDecimal corporateInsuranceAmount;

    @ApiModelProperty(value = "企业公积金")
    private BigDecimal corporateProvidentFundAmount;

    @ApiModelProperty(value = "预计应发工资")
    private BigDecimal expectedPaySalary;

    @ApiModelProperty(value = "个人所得税")
    private BigDecimal personalTax;

    @ApiModelProperty(value = "预计实发工资")
    private BigDecimal realPaySalary;

    @ApiModelProperty("状态0 未审核 1 审核通过（归档） 2 待审核  3 审核中 4 审核拒绝 5 已撤回")
    private Integer status;

    @ApiModelProperty("薪资项表头")
    private List<SalaryOptionHeadVO> salaryOptionHeadList;

    @ApiModelProperty("薪资项分页数据")
    private BasePage<QuerySalaryPageListVO> pageData;

}
