package com.kakarote.admin.entity.VO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author zhangzhiwei
 * 菜单相关VO
 */
@Data
@ToString
@ApiModel("角色列表返回")
public class AdminMenuVO {
    @ApiModelProperty(value = "菜单ID")
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Integer menuId;

    @ApiModelProperty(value = "上级菜单ID")
    private Integer parentId;

    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    @ApiModelProperty(value = "权限标识")
    private String realm;

    @ApiModelProperty(value = "菜单类型  1目录 2 菜单 3 按钮 4特殊")
    private Integer menuType;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "子菜单")
    private List<AdminMenuVO> childMenu;

}
