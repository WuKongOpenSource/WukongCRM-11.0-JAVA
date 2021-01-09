package com.kakarote.examine.entity.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author JiaS
 * @date 2020/12/18
 */
@Data
public class ExamineFlowDataVO {

    @ApiModelProperty("审核流程ID")
    private Integer flowId;

    @ApiModelProperty("0 条件 1 指定成员 2 主管 3 角色 4 发起人自选 5 连续多级主管")
    private Integer examineType;

    @ApiModelProperty("选择范围，只有发起人自选需要 1 全公司 2 指定成员 3 指定角色 ")
    private Integer rangeType;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty(value = "审批找不到用户或者条件均不满足时怎么处理 1 自动通过 2 管理员审批")
    private Integer examineErrorHandling;

    @ApiModelProperty(value = "直属上级级别 1 代表直属上级 2 代表 直属上级的上级")
    private Integer parentLevel;

    @ApiModelProperty(value = "角色ID")
    private Integer roleId;

    @ApiModelProperty("多人审批类型 1 依次审批 2 会签 3 或签")
    private Integer type;

    @ApiModelProperty("选择类型，只有发起人自选需要 1 自选一人 2 自选多人")
    private Integer chooseType;

    @ApiModelProperty("用户列表")
    private List<Map<String,Object>> userList;

    private Integer examineStatus;
}
