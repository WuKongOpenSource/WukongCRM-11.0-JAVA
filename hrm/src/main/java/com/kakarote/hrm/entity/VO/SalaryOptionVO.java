package com.kakarote.hrm.entity.VO;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 系统薪资项
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
@Data
public class SalaryOptionVO implements Serializable {

    private static final long serialVersionUID=1L;

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


    @ApiModelProperty("薪资子项")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<SalaryOptionVO> children;




}
