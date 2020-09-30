package com.kakarote.admin.entity.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangzhiwei
 * 简单的用户对象
 */
@Data
@ApiModel("用户对象")
public class HrmSimpleUserVO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("头像")
    private String img;

    @ApiModelProperty("昵称")
    private String realname;

    @ApiModelProperty("岗位")
    private String post;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("性别")
    private Integer sex;
}
