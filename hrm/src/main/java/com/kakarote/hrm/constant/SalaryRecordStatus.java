package com.kakarote.hrm.constant;

import lombok.Getter;

/**
 * 薪资记录状态
 * @author hmb
 */

@Getter
public enum SalaryRecordStatus {
    //状态  0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交
    WAIT_EXAMINE(0,"待审核"),PASS(1,"审核通过（归档）")
    ,REFUSE(2,"审核拒绝"),UNDER_EXAMINE(3,"审核中"),RECALL(4,"已撤回")
    ,CREATED(5,"新创建,薪资未生成"),HISTORY(10,"历史薪资"),COMPUTE(11,"核算完成");


    SalaryRecordStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }


    private String name;
    private int value;
}
