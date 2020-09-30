package com.kakarote.authorization.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author z
 */
@Data
public class AuthorizationUserInfo implements Serializable {
    /**
     * 认证用户列表
     */
    private List<AuthorizationUser> authorizationUserList = new ArrayList<>();

    public void addAuthorizationUser(AuthorizationUser user){
        this.authorizationUserList.add(user);
    }
}
