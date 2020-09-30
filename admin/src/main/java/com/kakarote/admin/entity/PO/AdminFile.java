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
 * 附件表
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_admin_file")
@ApiModel(value="AdminFile对象", description="附件表")
public class AdminFile implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "file_id", type = IdType.ASSIGN_ID)
    private Long fileId;

    @ApiModelProperty(value = "附件名称")
    private String name;

    @ApiModelProperty(value = "附件大小（字节）")
    private Long size;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "文件真实路径")
    private String path;

    @ApiModelProperty(value = "文件类型,file,img")
    private String fileType;

    @ApiModelProperty(value = "1 本地 2 阿里云oss")
    private Integer type;

    @ApiModelProperty(value = "来源 0 默认 1 admin 2 crm 3 work 4 oa 5 进销存 6 hrm")
    private Integer source;

    @ApiModelProperty(value = "1 公有访问 0 私有访问")
    private Integer isPublic;

    @ApiModelProperty(value = "批次id")
    private String batchId;



}
