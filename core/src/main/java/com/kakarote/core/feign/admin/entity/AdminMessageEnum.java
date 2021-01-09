package com.kakarote.core.feign.admin.entity;


/**
 * 消息通知枚举类
 */
public enum AdminMessageEnum {

    /**
     * 消息通知枚举类
     */
    NULL(0,0,"NULL"),
    OA_TASK_ALLOCATION(1,1,"分配给我的任务"),
    OA_TASK_JOIN(2,1,"我参与的任务"),
    OA_TASK_OVER(3,1,"任务结束通知"),
    OA_LOG_REPLY(4,2,"日志回复提醒"),
    OA_LOG_SEND(5,2,"日志发送提醒"),
    OA_EXAMINE_REJECT(6,3,"OA审批拒绝提醒"),
    OA_EXAMINE_PASS(7,3,"OA审批通过提醒"),
    OA_NOTICE_MESSAGE(8,4,"公告通知提醒"),
    OA_EVENT_MESSAGE(9,5,"日程通知"),
    CRM_CONTRACT_REJECT(10,6,"合同拒绝通知"),
    CRM_CONTRACT_PASS(11,6,"合同全部通过通知"),
    CRM_RECEIVABLES_REJECT(12,6,"回款拒绝通知"),
    CRM_RECEIVABLES_PASS(13,6,"回款通过通知"),
    CRM_CUSTOMER_IMPORT(14,6,"导入客户通知"),
    CRM_CUSTOMER_IMPORT_CANCEL(15,6,"导入客户取消通知"),
    CRM_CONTACTS_IMPORT(16,6,"导入联系人通知"),
    CRM_CONTACTS_IMPORT_CANCEL(17,6,"导入联系人取消通知"),
    CRM_LEADS_IMPORT(18,6,"导入线索通知"),
    CRM_LEADS_IMPORT_CANCEL(19,6,"导入线索取消通知"),
    CRM_PRODUCT_IMPORT(20,6,"导入产品通知"),
    CRM_PRODUCT_IMPORT_CANCEL(21,6,"导入产品取消通知"),
    CRM_BUSINESS_USER(22,6,"商机团队成员通知"),
    CRM_CUSTOMER_USER(23,6,"客户团队成员通知"),
    CRM_CONTRACT_USER(24,6,"合同团队成员通知"),
    OA_EXAMINE_NOTICE(25,3,"OA待审核审批提醒"),
    CRM_CONTRACT_EXAMINE(26,6,"合同待审核审批提醒"),
    CRM_RECEIVABLES_EXAMINE(27,6,"回款待审核审批提醒"),
    CRM_BUSINESS_TEAM_EXIT(28, 6, "商机团队成员退出提醒"),
    CRM_CUSTOMER_TEAM_EXIT(29, 6, "客户团队成员退出提醒"),
    CRM_CONTRACT_TEAM_EXIT(30, 6, "合同团队成员退出提醒"),
    CRM_BUSINESS_REMOVE_TEAM(31, 6, "移除商机团队成员提醒"),
    CRM_CUSTOMER_REMOVE_TEAM(32, 6, "移除客户团队成员提醒"),
    CRM_CONTRACT_REMOVE_TEAM(33, 6, "移除合同团队成员提醒"),
    OA_COMMENT_REPLY(34, 2, "评论回复提醒"),
    CRM_INVOICE_REJECT(35, 6, "发票拒绝通知"),
    CRM_INVOICE_PASS(36, 6, "发票通过通知"),
    CRM_INVOICE_EXAMINE(37, 6, "发票待审核审批提醒"),

    KM_DOCUMENT_SHARE_OPEN(41, 7, "知识库文档开启分享"),
    KM_DOCUMENT_SHARE_CLOSE(42, 7, "知识库文档关闭分享"),
    //人资
    HRM_EMPLOYEE_IMPORT(50, 8, "人资员工导入通知"),
    HRM_SEND_SLIP(80, 8, "人资发送工资条通知"),
    HRM_FIX_SALARY_IMPORT(81, 8, "人资导入定薪通知"),
    HRM_CHANGE_SALARY_IMPORT(82, 8, "人资导入调薪通知"),
    HRM_WRITE_ARCHIVES(83, 8, "邀请填写档案信息"),
    HRM_EMPLOYEE_SALARY_PASS(84, 8, "人资薪资通过通知"),
    HRM_EMPLOYEE_SALARY_REJECT(85, 8, "人资薪资拒绝通知"),
    HRM_EMPLOYEE_SALARY_EXAMINE(86, 8, "人资薪资待审核提醒"),
    //JXC
    JXC_PURCHASE_EXAMINE(53,9,"采购订单待审核审批提醒"),
    JXC_PURCHASE_REJECT(54,9,"采购订单拒绝通知"),
    JXC_PURCHASE_PASS(55,9,"采购订单通过通知"),
    JXC_RETREAT_EXAMINE(56,9,"采购退货单待审核审批提醒"),
    JXC_RETREAT_REJECT(57,9,"采购退货单拒绝通知"),
    JXC_RETREAT_PASS(58,9,"采购退货单通过通知"),
    JXC_SALE_EXAMINE(59,9,"销售订单待审核审批提醒"),
    JXC_SALE_REJECT(60,9,"销售订单拒绝通知"),
    JXC_SALE_PASS(61,9,"销售订单通过通知"),
    JXC_SALE_RETURN_EXAMINE(62,9,"销售退货单待审核审批提醒"),
    JXC_SALE_RETURN_REJECT(63,9,"销售退货单拒绝通知"),
    JXC_SALE_RETURN_PASS(64,9,"销售退货单通过通知"),
    JXC_PAYMENT_EXAMINE(65,9,"付款单待审核审批提醒"),
    JXC_PAYMENT_REJECT(66,9,"付款单待审拒绝通知"),
    JXC_PAYMENT_PASS(67,9,"付款单待审通过通知"),
    JXC_COLLECTION_EXAMINE(68,9,"回款单待审核审批提醒"),
    JXC_COLLECTION_REJECT(69,9,"回款单拒绝通知"),
    JXC_COLLECTION_PASS(70,9,"回款单通过通知"),
    JXC_INVENTORY_EXAMINE(71,9,"盘点待审核审批提醒"),
    JXC_INVENTORY_REJECT(72,9,"盘点拒绝通知"),
    JXC_INVENTORY_PASS(73,9,"盘点通过通知"),
    JXC_ALLOCATION_EXAMINE(74,9,"调拨待审核审批提醒"),
    JXC_ALLOCATION_REJECT(75,9,"调拨拒绝通知"),
    JXC_ALLOCATION_PASS(76,9,"调拨通过通知")
    ;

    AdminMessageEnum(Integer type, Integer label, String remarks){
        this.type=type;
        this.label=label;
        this.remarks=remarks;
    }
    private final int  type;
    private final int  label;
    private final String remarks;

    public int getType() {
        return type;
    }

    public String getRemarks() {
        return remarks;
    }

    public int getLabel() {
        return label;
    }

    public static AdminMessageEnum parse(int type) {
        for (AdminMessageEnum Enum : AdminMessageEnum.values()) {
            if (Enum.getType()==type) {
                return Enum;
            }
        }
        return NULL;
    }
}
