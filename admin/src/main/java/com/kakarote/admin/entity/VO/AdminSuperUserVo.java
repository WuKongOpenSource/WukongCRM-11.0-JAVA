package com.kakarote.admin.entity.VO;

import com.alibaba.fastjson.JSONObject;

public class AdminSuperUserVo extends AdminUserVO {
    private JSONObject serverUserInfo;

    public JSONObject getServerUserInfo() {
        return serverUserInfo;
    }

    public void setServerUserInfo(JSONObject serverUserInfo) {
        this.serverUserInfo = serverUserInfo;
    }
}
