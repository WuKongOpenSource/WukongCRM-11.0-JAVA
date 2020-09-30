package com.kakarote.oa.entity.BO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class SetExamineCategoryBO {

    @ApiModelProperty("审批类型id")
    private Integer id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("备注")
    private String remarks;

    private String icon;

    private Integer examineType;

    private Set<Long> userIds;

    private Set<Integer> deptIds;

    private List<Step> step;


    @Getter
    @Setter
    public static class Step{
        private Set<Long> checkUserId;

        private Integer stepType;

    }
}
