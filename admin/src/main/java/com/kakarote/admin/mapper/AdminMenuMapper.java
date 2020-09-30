package com.kakarote.admin.mapper;

import com.kakarote.admin.entity.PO.AdminMenu;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台菜单表 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
public interface AdminMenuMapper extends BaseMapper<AdminMenu> {

    /**
     * 查询菜单列表
     *
     * @param userId 用户ID
     * @return menus
     */
    public List<AdminMenu> queryMenuList(Long userId);

    /**
     * 查询公海菜单权限
     * @param userId 用户ID
     * @param deptId 部门ID
     * @return data
     */
    public Map<String, Long> queryPoolReadAuth(@Param("userId") Long userId, @Param("deptId") Integer deptId);

    Integer queryMenuId(@Param("realm1") String realm1,@Param("realm2") String realm2,@Param("realm3") String realm3);
}
