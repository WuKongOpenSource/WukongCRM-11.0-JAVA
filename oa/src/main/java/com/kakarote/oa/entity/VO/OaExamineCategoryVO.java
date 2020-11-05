package com.kakarote.oa.entity.VO;

import com.kakarote.core.feign.admin.entity.SimpleDept;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.oa.entity.PO.OaExamineStep;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author JiaS
 * @date 2020/9/25
 */
@Data
public class OaExamineCategoryVO {

    private Integer categoryId;

    @ApiModelProperty(value = "名称")
    private String title;

    @ApiModelProperty(value = "描述")
    private String remarks;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "1 普通审批 2 请假审批 3 出差审批 4 加班审批 5 差旅报销 6 借款申请 0 自定义审批")
    private Integer type;

    @ApiModelProperty(value = "创建人ID")
    private Long createUserId;

    @ApiModelProperty(value = "1启用，0禁用")
    private Integer status;

    @ApiModelProperty(value = "1为系统类型，不能删除")
    private Integer isSys;

    @ApiModelProperty(value = "1固定2自选")
    private Integer examineType;

    private List<SimpleUser> userList;

    private List<SimpleDept> deptList;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "1已删除")
    private Integer isDeleted;

    @ApiModelProperty(value = "删除时间")
    private Date deleteTime;

    @ApiModelProperty(value = "删除人ID")
    private Long deleteUserId;

    private List<OaExamineStep> stepList;



}
