package com.kakarote.admin.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wyq
 */
@Data
@ApiModel("通讯录查询")
public class UserBookBO extends PageEntity {

    @ApiModelProperty("搜索关键字")
    private String search;

    @ApiModelProperty("排列顺序 2倒序 其他是正序")
    private Integer initial;

    @ApiModelProperty("部门id")
    private Integer deptId;

    @ApiModelProperty(value = "关注状态,0未关注,1已关注")
    private Integer status;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

}
