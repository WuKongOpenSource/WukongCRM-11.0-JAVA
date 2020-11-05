package com.kakarote.authorization.entity;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.kakarote.core.entity.UserInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author zhangzhiwei
 * 系统默认用户标识
 */

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "权限认证对象")
public class AuthorizationUser extends UserInfo implements UserDetails {

    /**
     * 短信验证码
     */
    @ApiModelProperty("短信验证码")
    private String smscode;

    @ApiModelProperty("用户密码")
    private String password;

    @ApiModelProperty("salt")
    private String salt;

    /**
     * 登录类型 1 密码登录 2 验证码登录
     */
    @ApiModelProperty(value = "登录类型", allowableValues = "1,2")
    private Integer loginType;

    @ApiModelProperty(value = "类型 1 pc 2 ", allowableValues = "1,2")
    private Integer type = 1;

    private List<UserInfo> userInfoList = new ArrayList<>();

    public AuthorizationUser setUserInfoList(List<Object> objList) {
        objList.forEach(obj -> {
            this.userInfoList.add(BeanUtil.copyProperties(obj, AuthorizationUser.class));
        });
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        getAuthoritiesUrlList().forEach(authoritiesUrl -> {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authoritiesUrl);
            authorityList.add(grantedAuthority);
        });
        return authorityList;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static AuthorizationUser toAuthorizationUser(UserInfo systemUser) {
        return BeanUtil.copyProperties(systemUser, AuthorizationUser.class);
    }

    public UserInfo toUserInfo() {
        return BeanUtil.copyProperties(this, UserInfo.class);
    }

    public String toJSON() {
        return JSON.toJSONString(this);
    }
}
