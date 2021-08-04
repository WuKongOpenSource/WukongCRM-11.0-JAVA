package com.kakarote.hrm.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 工资条模板项
 * </p>
 *
 * @author hmb
 * @since 2020-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_salary_slip_template_option")
@ApiModel(value="HrmSalarySlipTemplateOption对象", description="工资条模板项")
public class HrmSalarySlipTemplateOption implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "模板id")
    private Integer templateId;

    @ApiModelProperty(value = "薪资项名称")
    private String name;

    @ApiModelProperty(value = "选项类型 1 分类 2 薪资项")
    private Integer type;

    @ApiModelProperty(value = "薪资项code")
    private Integer code;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "父级分类id")
    private Integer pid;

    @ApiModelProperty(value = "是否隐藏 0 否 1 是")
    private Integer isHide;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(exist = false)
    private List<HrmSalarySlipTemplateOption> optionList;


}
