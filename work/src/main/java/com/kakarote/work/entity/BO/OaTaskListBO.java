package com.kakarote.work.entity.BO;

import com.kakarote.core.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wyq
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("查询办公任务列表参数")
public class OaTaskListBO extends PageEntity {

    @ApiModelProperty("用户范围 0全部1.我负责的2.我创建的3.我参与的")
    private Integer type;

    @ApiModelProperty("状态 1正在进行 5结束")
    private Integer status;

    @ApiModelProperty("优先级 3高 2中 1低 0无 null全部")
    private Integer priority;

    @ApiModelProperty("截止日期")
    private String dueDate;

    @ApiModelProperty("为空查询我的任务 1.为下属任务 其他为指定人任务")
    private Integer mold;

    @ApiModelProperty("指定人id")
    private Long userId;

    @ApiModelProperty("负责人id")
    private Long mainUserId;

    @ApiModelProperty("任务名称关键字")
    private String search;

    private Boolean isExport;
}
