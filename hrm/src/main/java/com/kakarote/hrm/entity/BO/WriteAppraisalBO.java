package com.kakarote.hrm.entity.BO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class WriteAppraisalBO {
    
    @ApiModelProperty("员工考核id")
    private Integer employeeAppraisalId;

    @ApiModelProperty("考核项")
    private List<SegListBean> segList;
    @ApiModelProperty(value = "是否为草稿 0否 1是")
    private Integer isDraft;

    @Getter
    @Setter
    public static class SegListBean {

        @ApiModelProperty("模板考核项id(新添加的是0)")
        private Integer tempSegId;
        @ApiModelProperty("考核项名称")
        private String segName;
        @ApiModelProperty("考核项值")
        private String value;
        @ApiModelProperty("是否固定 0 否 1 是")
        private Integer isFixed;
        @ApiModelProperty("权重")
        private BigDecimal weight;
        @ApiModelProperty("考核子项")
        private List<ItemsBean> items;
    }

    @Getter
    @Setter
    public static class ItemsBean {
        @ApiModelProperty("模板考核子项项id(新添加的是0)")
        private Integer tempItemId;
        @ApiModelProperty("考核项名称")
        private String itemName;
        @ApiModelProperty("考核项值")
        private String value;
    }
}
