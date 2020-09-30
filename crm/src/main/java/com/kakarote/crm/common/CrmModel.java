package com.kakarote.crm.common;

import java.util.HashMap;

/**
 * @author zhangzhiwei
 * crm模块单个的实体类对象
 */
public class CrmModel extends HashMap<String, Object> {

    public CrmModel() {

    }

    public CrmModel(Integer label) {
        this.label = label;
    }

    private transient Integer label;

    private transient Integer id;

    public Integer getLabel() {
        return label;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLabel(Integer label) {
        this.label = label;
    }

    public Long getOwnerUserId() {
        return (Long) get("ownerUserId");
    }

    public void setOwnerUserId(Long ownerUserId) {
        put("ownerUserId",ownerUserId);
    }

    public String getOwnerUserName() {
        return (String) get("ownerUserName");
    }

    public void setOwnerUserName(String ownerUserName) {
        put("ownerUserName",ownerUserName);
    }

    public String getBatchId(){
        return (String) get("batchId");
    }
}
