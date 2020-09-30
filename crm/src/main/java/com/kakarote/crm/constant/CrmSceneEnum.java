package com.kakarote.crm.constant;

/**
 * @author zhangzhiwei
 * 场景枚举
 */
public enum CrmSceneEnum {
    /**
     * 全部
     */
    ALL,
    /**
     * 自己负责的
     */
    SELF,
    /**
     * 下属负责的
     */
    CHILD,
    /**
     * 关注的
     */
    STAR,
    /**
     * 已转化的线索
     */
    TRANSFORM,
    ;
    CrmSceneEnum(){

    }
    public String getName(){
        return name().toLowerCase();
    }
}
