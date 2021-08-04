package com.kakarote.hrm.common;

import java.util.HashMap;

/**
 * hrm模块单个的实体类对象
 */
public class HrmModel extends HashMap<String, Object> {
    public HrmModel(){

    }
    public HrmModel(Integer label){
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
}
