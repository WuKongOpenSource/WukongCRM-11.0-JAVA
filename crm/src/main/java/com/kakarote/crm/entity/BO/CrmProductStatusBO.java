package com.kakarote.crm.entity.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;
/**
 * @author zhangzhiwei
 * 产品状态BO
 */
@Data
@ToString
@ApiModel(value = "产品状态BO")
public class CrmProductStatusBO {
    @ApiModelProperty("ID列表")
    @NotNull
    private List<Integer> ids;

    @ApiModelProperty("状态")
    @NotNull
    private Integer status;
}
