package com.kakarote.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.admin.entity.BO.*;
import com.kakarote.admin.entity.PO.AdminUser;
import com.kakarote.admin.entity.VO.AdminUserVO;
import com.kakarote.admin.entity.VO.HrmSimpleUserVO;
import com.kakarote.admin.entity.VO.UserBookVO;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.servlet.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
public interface IAdminUserService extends BaseService<AdminUser> {

    /**
     * 通过用户名查询用户
     * @param username
     * @return
     */
    public List<Map<String,Object>> findByUsername(String username);

    /**
     * 查询企业下所有用户
     * @param adminUserBO 业务对象
     * @return ids
     */
    public BasePage<AdminUserVO> queryUserList(AdminUserBO adminUserBO);

    /**
     * 查询该用户下级的用户
     * @param userId 用户ID 0代表全部
     * @return data
     */
    public List<Long> queryChildUserId(Long userId);

    /**
     * 修改用户
     * @param adminUserVO data
     */
    public void setUser(AdminUserVO adminUserVO);

    /**
     * 新增用户
     * @param adminUserVO data
     */
    public void addUser(AdminUserVO adminUserVO);

    /**
     * 修改用户信息
     * @param adminUser
     */
    public void updateUser(AdminUser adminUser);

    /**
     * 修改用户账号功能
     *
     * @param id       用户ID
     * @param username 新的用户名
     * @param password 新的密码
     * @return 操作状态
     */
    public Integer usernameEdit(Integer id, String username, String password);


    /**
     * excel导入员工
     * @param file file
     */
    public JSONObject excelImport(MultipartFile file);

    /**
     * 设置状态
     * @param adminUserStatusBO status
     */
    public void setUserStatus(AdminUserStatusBO adminUserStatusBO);

    /**
     * 重置密码
     * @param adminUserStatusBO status
     */
    public void resetPassword(AdminUserStatusBO adminUserStatusBO);

    /**
     * 根据用户ID查询角色ID
     * @param userId userId
     * @return ids
     */
    public List<Integer> queryUserRoleIds(Long userId);

    /**
     * 通讯录查询
     * @param userBookBO data
     */
    public BasePage<UserBookVO> queryListName(UserBookBO userBookBO);

    /**
     * 切换关注状态
     * @param userId 用户ID
     * @return data
     */
    public void attention(Long userId);

    /**
     * 根据用户ID获取用户名称
     * @param userId 用户ID
     * @return data
     */
    public String getNameByUserId(Long userId);

    /**
     * 根据ids查询用户信息
     *
     * @param ids id列表
     * @return data
     */
    public List<SimpleUser> queryUserByIds(List<Long> ids);

    public List<Long> queryNormalUserByIds(List<Long> ids);
    /**
     * 根据部门ids查询用户列表
     *
     * @param ids id列表
     * @return data
     */
    public List<Long> queryUserByDeptIds(List<Integer> ids);

    void hrmAddUser(HrmAddUserBO hrmAddUserBO);

    DeptUserListVO queryDeptUserList(Integer deptId,boolean isAllUser);

    Set<HrmSimpleUserVO> queryDeptUserListByHrm(DeptUserListByHrmBO deptUserListByHrmBO);

    List<Long> queryUserIdByRealName(List<String> realNames);

    UserInfo queryLoginUserInfo(Long userId);


    /**
     * 查询当前系统有没有初始化
     * @return data
     */
    public Integer querySystemStatus();

    /**
     * 系统用户初始化
     */
    public void initUser(SystemUserBO systemUserBO);


    /**
     * 查询所有员工
     * @return
     */
    public List<UserInfo> queryAllUserInfoList();
}
