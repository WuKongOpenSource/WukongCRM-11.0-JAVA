package com.kakarote.work.entity.BO;

import com.kakarote.work.entity.PO.WorkUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wyq
 */
@Data
@ApiModel("设置项目成员角色参数")
public class SetWorkOwnerRoleBO {
    @ApiModelProperty("项目id")
    private Integer workId;

    @ApiModelProperty("成员角色列表")
    private List<WorkUser> list;
}
