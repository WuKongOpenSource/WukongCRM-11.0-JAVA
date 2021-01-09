package com.kakarote.core.common;

import lombok.Getter;

@Getter
public enum ModelType {
    ADMIN("admin","后台管理"),
    CRM("crm","客户管理"),
    OA("oa","办公管理"),
    WORK("work","项目管理"),
    HRM("hrm","人力资源管理"),
    JXC("jxc","进销存管理"),
    ;


    ModelType(String modelName, String name) {
        this.modelName = modelName;
        this.name = name;
    }

    private String modelName;

    private String name;

    public static String valueOfName(String modelName){
        for (ModelType modelType : values()) {
            if (modelName.equals(modelType.getModelName())){
                return modelType.getName();
            }
        }
        return "";
    }
}
