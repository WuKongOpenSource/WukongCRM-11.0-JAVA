package com.kakarote.admin.entity.VO;

public class AdminSuperUserVo extends AdminUserVO {

    public AdminSuperUserVo() {
    }

    private String adminToken;

    private boolean isInitToken = false;

    public String getAdminToken() {
        return adminToken;
    }

    public void setAdminToken(String adminToken) {
        this.adminToken = adminToken;
    }

    public boolean isInitToken() {
        return isInitToken;
    }

    public void setInitToken(boolean initToken) {
        isInitToken = initToken;
    }
}
