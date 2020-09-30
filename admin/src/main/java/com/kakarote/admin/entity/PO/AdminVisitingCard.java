package com.kakarote.admin.entity.PO;

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
 * 名片表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_admin_visiting_card")
@ApiModel(value="AdminVisitingCard对象", description="名片表")
public class AdminVisitingCard implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "card_id", type = IdType.AUTO)
    private Integer cardId;

    @ApiModelProperty(value = "名片名称")
    private String cardName;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "关联员工id")
    private Long userId;

    @ApiModelProperty(value = "备注")
    private String remark;


    private String openid;

    @ApiModelProperty(value = "微信号")
    private String wechatNumber;

    @ApiModelProperty(value = "网址")
    private String url;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "简介")
    private String intro;

    @ApiModelProperty(value = "微信小程序码")
    private String weixinImg;

    @ApiModelProperty(value = "海报名片")
    private String officialImg;


}
