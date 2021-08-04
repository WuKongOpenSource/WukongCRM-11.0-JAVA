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
import java.util.Date;

/**
 * <p>
 * 员工工作经历
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_employee_work_experience")
@ApiModel(value="HrmEmployeeWorkExperience对象", description="员工工作经历")
@RangeValidated(minFieldName = "workStartTime", maxFieldName = "workEndTime")
public class HrmEmployeeWorkExperience implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "work_exp_id", type = IdType.AUTO)
    private Integer workExpId;

    private Integer employeeId;

    @ApiModelProperty(value = "工作单位")
    private String workUnit;

    @ApiModelProperty(value = "职务")
    private String post;

    @ApiModelProperty(value = "工作开始时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date workStartTime;

    @ApiModelProperty(value = "工作结束时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date workEndTime;

    @ApiModelProperty(value = "离职原因")
    private String leavingReason;

    @ApiModelProperty(value = "证明人")
    private String witness;

    @ApiModelProperty(value = "证明人手机号")
    private String witnessPhone;

    @ApiModelProperty(value = "工作备注")
    private String workRemarks;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "排序")
    private Integer sort;




}
