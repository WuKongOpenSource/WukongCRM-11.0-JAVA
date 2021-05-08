package com.kakarote.crm.entity.PO;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * crm团队成员表

 * </p>
 *
 * @author zhangzhiwei
 * @since 2021-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_crm_team_members")
@ApiModel(value="CrmTeamMembers对象", description="crm团队成员表")
public class CrmTeamMembers implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "类型，同crm类型")
    private Integer type;

    @ApiModelProperty(value = "对应类型主键ID")
    private Integer typeId;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "团队成员类型 1 只读 2 读写 3 负责人")
    private Integer power;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "过期时间")
    private Date expiresTime;


}
