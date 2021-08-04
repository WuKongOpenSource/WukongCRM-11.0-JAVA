package com.kakarote.hrm.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 系统薪资项
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_hrm_salary_option")
@ApiModel(value="HrmSalaryOption对象", description="系统薪资项")
public class HrmSalaryOption implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "option_id", type = IdType.AUTO)
    private Integer optionId;

    @ApiModelProperty(value = "薪资项编码")
    private Integer code;

    @ApiModelProperty(value = "薪资项父编码")
    private Integer parentCode;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "是否固定 0 否 1 是")
    private Integer isFixed;

    @ApiModelProperty(value = "是否加项 0 减 1 加")
    private Integer isPlus;

    @ApiModelProperty(value = "是否计税 0 否 1 是")
    private Integer isTax;

    @ApiModelProperty(value = "是否展示 0 否 1 是")
    private Integer isShow;

    @ApiModelProperty("是否参与薪资计算 0 否 1 是")
    private Integer isCompute;

    @ApiModelProperty("是否开启 0 否 1 是")
    private Integer isOpen;





}
