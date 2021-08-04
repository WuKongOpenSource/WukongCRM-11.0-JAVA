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
 * 员工教育经历
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_employee_education_experience")
@ApiModel(value="HrmEmployeeEducationExperience对象", description="员工教育经历")
@RangeValidated(minFieldName = "admissionTime", maxFieldName = "graduationTime",message = "入学时间大于毕业时间")
public class HrmEmployeeEducationExperience implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "education_id", type = IdType.AUTO)
    private Integer educationId;

    private Integer employeeId;

    @ApiModelProperty(value = "学历 1小学、2初中、3中专、4中职、5中技、6高中、7大专、8本科、9硕士、10博士、11博士后、12其他")
    private Integer education;

    @ApiModelProperty(value = "毕业院校")
    private String graduateSchool;

    @ApiModelProperty(value = "专业")
    private String major;

    @ApiModelProperty(value = "入学时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date admissionTime;

    @ApiModelProperty(value = "毕业时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date graduationTime;

    @ApiModelProperty(value = "教学方式 1 全日制、2成人教育、3远程教育、4自学考试、5其他")
    private Integer teachingMethods;

    @ApiModelProperty(value = "是否第一学历 0 否 1 是")
    private Integer isFirstDegree;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private Integer sort;




}
