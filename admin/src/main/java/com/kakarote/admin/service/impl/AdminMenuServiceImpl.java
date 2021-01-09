package com.kakarote.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.kakarote.admin.common.AdminRoleTypeEnum;
import com.kakarote.admin.entity.PO.AdminMenu;
import com.kakarote.admin.entity.VO.AdminMenuVO;
import com.kakarote.admin.mapper.AdminMenuMapper;
import com.kakarote.admin.service.IAdminMenuService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.RecursionUtil;
import com.kakarote.core.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台菜单表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
@Service
public class AdminMenuServiceImpl extends BaseServiceImpl<AdminMenuMapper, AdminMenu> implements IAdminMenuService {

    /**
     * 查询用户所拥有的菜单权限
     *
     * @param userId 用户列表
     * @return 菜单权限的并集
     */
    @Override
    public List<AdminMenu> queryMenuList(Long userId) {
        if (UserUtil.isAdmin()) {
            return query().list();
        }
        return getBaseMapper().queryMenuList(userId);
    }

    /**
     * 查询公海菜单权限
     *
     * @param userId 用户ID
     * @param deptId 部门ID
     * @return data
     */
    @Override
    public Map<String, Long> queryPoolReadAuth(Long userId, Integer deptId) {
        return getBaseMapper().queryPoolReadAuth(userId, deptId);
    }

    /**
     * 根据类型查询菜单
     * @param typeEnum type
     * @return data
     */
    @Override
    public JSONObject getMenuListByType(AdminRoleTypeEnum typeEnum) {
        JSONObject object = new JSONObject();
        String realm;
        switch (typeEnum) {
            case MANAGER:
                realm = "manage";
                break;
            case CUSTOMER_MANAGER: {
                realm = "crm";
                AdminMenuVO bi = queryMenuListByRealm("bi");
                List<AdminMenuVO> biList = getMenuList(bi.getMenuId(), "oa","jxc");
                bi.setChildMenu(biList);
                object.put("bi", bi);
                break;
            }
            case WORK:
                realm = "work";
                break;
            case OA: {
                realm = "oa";
                AdminMenuVO bi = queryMenuListByRealm("bi");
                String[] realmArr = new String[]{"achievement", "business", "customer", "contract", "product", "portrait", "ranking", "call","jxc"};
                List<AdminMenuVO> biList = getMenuList(bi.getMenuId(),realmArr);
                bi.setChildMenu(biList);
                object.put("bi", bi);
                break;
            }
            case PROJECT:
                realm = "project";
                break;
            case HRM:
                realm = "hrm";
                break;
            case JXC:
                AdminMenuVO bi = queryMenuListByRealm("bi");
                String[] realmArr = new String[]{"achievement", "business", "customer", "contract", "product", "portrait", "ranking", "call","oa"};
                List<AdminMenuVO> biList = getMenuList(bi.getMenuId(),realmArr);
                bi.setChildMenu(biList);
                object.put("bi", bi);
                realm = "jxc";
                break;
            default:
                realm = "";
                break;
        }
        AdminMenuVO data = queryMenuListByRealm(realm);
        List<AdminMenuVO> menuList = getMenuList(data.getMenuId());
        data.setChildMenu(menuList);
        object.put("data",data);
        return object;
    }

    /**
     * 通过parentId和realm查询菜单
     *
     * @return 菜单列表
     * @author zhangzhiwei
     */
    private AdminMenuVO queryMenuListByRealm(String realm) {
        AdminMenu adminMenu = lambdaQuery().eq(AdminMenu::getParentId, 0).eq(AdminMenu::getRealm, realm).one();
        return BeanUtil.copyProperties(adminMenu, AdminMenuVO.class);
    }

    private List<AdminMenuVO> getMenuList(Integer parentId, String... notRealm) {
        LambdaQueryChainWrapper<AdminMenu> chainWrapper = lambdaQuery();
        if (notRealm.length > 0) {
            chainWrapper.notIn(AdminMenu::getRealm, Arrays.asList(notRealm));
        }
        chainWrapper.orderByAsc(AdminMenu::getSort);
        List<AdminMenu> list = chainWrapper.list();
        return RecursionUtil.getChildListTree(list, "parentId", parentId, "menuId", "childMenu", AdminMenuVO.class);
    }

    @Override
    public Integer queryMenuId(String realm1, String realm2, String realm3) {
        return getBaseMapper().queryMenuId(realm1,realm2,realm3);
    }
}
