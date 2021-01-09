package com.kakarote.core.common.log;

public enum BehaviorEnum {

    /**
     * 操作记录行为
     */
    SAVE(1, "新建"),
    UPDATE(2, "编辑"),
    DELETE(3, "删除"),
    CHANGE_OWNER(4, "转移"),
    TRANSFER(5, "转化"),
    EXCEL_IMPORT(6, "导入"),
    EXCEL_EXPORT(7, "导出"),
    PUT_IN_POOL(8, "放入公海"),
    RECEIVE(9, "领取"),
    DISTRIBUTE(10, "分配"),
    LOCK(11, "锁定"),
    UNLOCK(12, "解锁"),
    CHANGE_DEAL_STATUS(13, "更改成交状态"),
    ADD_MEMBER(14, "添加团队成员"),
    UPDATE_MEMBER(15, "编辑团队成员"),
    REMOVE_MEMBER(16, "移除团队成员"),
    EXIT_MEMBER(17, "退出团队"),
    UPLOAD(18, "上传"),
    UPDATE_BUSINESS_STATUS(19, "编辑商机阶段"),
    SUBMIT_EXAMINE(20, "提交审核"),
    CANCEL_EXAMINE(21, "作废合同"),
    PUT_ON_SALE(22, "上架"),
    PUT_OFF_SALE(23, "下架"),
    START(24, "启用"),
    STOP(25, "停用"),
    FOLLOW_UP(26, "新建跟进记录"),
    PASS_EXAMINE(27, "通过审批"),
    REJECT_EXAMINE(28, "驳回审批"),
    RECHECK_EXAMINE(29, "撤回审批"),
    ARCHIVE(30, "归档"),
    RESTORE(31, "恢复"),
    EXIT(32, "退出"),
    ACTIVE(33, "激活"),
    CLEAN(34, "彻底删除"),
    FORBID(35, "禁用"),
    RESET(36, "重置"),
    COPY(37, "复制"),
    RELATE(38, "关联"),
    UNBINDING(39, "解绑"),
    NULL(0, "null");

    private int type;
    private String name;

    BehaviorEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static BehaviorEnum parse(int type) {
        for (BehaviorEnum Enum : BehaviorEnum.values()) {
            if (Enum.getType() == type) {
                return Enum;
            }
        }
        return NULL;
    }
}
