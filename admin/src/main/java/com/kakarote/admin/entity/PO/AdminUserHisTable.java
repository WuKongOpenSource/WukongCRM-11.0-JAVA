package com.kakarote.admin.entity.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 授权坐席
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wk_admin_user_his_table")
@ApiModel(value="AdminUserHisTable对象", description="授权坐席")
public class AdminUserHisTable implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "his_table_id", type = IdType.AUTO)
    private Integer hisTableId;

    private Long userId;

    @ApiModelProperty(value = "0 没有 1 有")
    private Integer hisTable;


    @ApiModelProperty(value = "1.坐席授权 2.设置默认名片 3.关联员工")
    private Integer type;


}
