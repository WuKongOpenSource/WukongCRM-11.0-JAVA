package com.kakarote.core.feign.examine.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author JiaS
 * @date 2020/12/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamineConditionDataBO {
    @ApiModelProperty("对应类型")
    private Integer label;

    @ApiModelProperty("对应类型ID")
    private Integer typeId;

    @ApiModelProperty("OA审批使用 值为新审批流id")
    private Integer categoryId;

    @ApiModelProperty("所需字段")
    private List<String> fieldList;

    @ApiModelProperty("审核状态 0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交 6 创建 7 已删除 8 作废")
    private Integer checkStatus;
}
