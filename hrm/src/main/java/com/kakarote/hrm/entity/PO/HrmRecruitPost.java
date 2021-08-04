package com.kakarote.hrm.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.kakarote.core.common.RangeValidated;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 招聘职位表
 * </p>
 *
 * @author huangmingbo
 * @since 2020-06-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_recruit_post")
@ApiModel(value="HrmRecruitPost对象", description="招聘职位表")
@RangeValidated(minFieldName = {"minSalary","minAge"},maxFieldName = {"maxSalary","maxAge"})
public class HrmRecruitPost implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "职位id")
    @TableId(value = "post_id", type = IdType.AUTO)
    private Integer postId;

    @ApiModelProperty(value = "职位名称")
    private String postName;

    @ApiModelProperty(value = "部门id")
    private Integer deptId;

    @ApiModelProperty(value = "工作性质 1 全职 2实习 3兼职")
    private Integer jobNature;

    @ApiModelProperty(value = "工作城市")
    private String city;

    @ApiModelProperty(value = "招聘人数")
    private Integer recruitNum;

    @ApiModelProperty(value = "招聘原因")
    private String reason;

    @ApiModelProperty(value = "工作经验 1不限 2一年以内 3一至三年 4三至五年 5五至十年 6十年以上")
    private Integer workTime;

    @ApiModelProperty(value = "学历要求 1不限 2高中及以上 3大专及以上 4本科及以上 5硕士及以上 6博士")
    private Integer educationRequire;

    @ApiModelProperty(value = "开始薪资 -1 面议")
    @TableField(value = "min_salary", updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal minSalary;

    @ApiModelProperty(value = "结束薪资 -1 面议")
    @TableField(value = "max_salary", updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal maxSalary;

    @ApiModelProperty(value = "薪资单位 1 元/年 2 元/月")
    private Integer salaryUnit;

    @ApiModelProperty(value = "最小年龄 -1 不限")
    @TableField(value = "min_age", updateStrategy = FieldStrategy.IGNORED)
    private Integer minAge;

    @ApiModelProperty(value = "最大年龄 -1 不限")
    @TableField(value = "max_age", updateStrategy = FieldStrategy.IGNORED)
    private Integer maxAge;

    @ApiModelProperty(value = "最迟到岗时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date latestEntryTime;

    @ApiModelProperty(value = "负责人id")
    private Integer ownerEmployeeId;

    @ApiModelProperty(value = "面试官")
    private String interviewEmployeeIds;

    @ApiModelProperty(value = "职位描述")
    private String description;

    @ApiModelProperty(value = "紧急程度 1紧急 2 一般")
    private Integer emergencyLevel;

    @ApiModelProperty(value = "职位类型")
    private Integer postTypeId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "批次id")
    private String batchId;

    @ApiModelProperty(value = "0 停止  1 启用")
    private Integer status;



    @TableField(exist = false)
    private String deptName;


}
