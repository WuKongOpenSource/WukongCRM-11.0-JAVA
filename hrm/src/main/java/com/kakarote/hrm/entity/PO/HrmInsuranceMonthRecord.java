package com.kakarote.hrm.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 每月社保记录
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_insurance_month_record")
@ApiModel(value="HrmInsuranceMonthRecord对象", description="每月社保记录")
public class HrmInsuranceMonthRecord implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "i_record_id", type = IdType.AUTO)
    private Integer iRecordId;

    @ApiModelProperty("报表标题")
    private String title;

    @ApiModelProperty(value = "年份")
    private Integer year;

    @ApiModelProperty(value = "月份")
    private Integer month;

    @ApiModelProperty(value = "参保人数")
    private Integer num;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;




}
