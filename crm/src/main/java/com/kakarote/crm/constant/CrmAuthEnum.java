package com.kakarote.crm.constant;


/**
 * crm模块操作权限枚举
 */
public enum CrmAuthEnum {

    /**
     * 新增
     */
    ADD(1),
    /**
     * 编辑
     */
    EDIT(2),
    /**
     * 列表
     */
    LIST(3),
    /**
     * 详情
     */
    READ(4),
    /**
     * 删除
     */
    DELETE(5),
    ;


    private CrmAuthEnum(int value) {
        this.value = value;
    }

    private int value;

    /**
     * 根据功能和权限获取菜单ID，在CRM模块中菜单ID是完全固定的常量
     * 9 线索
     * 17 新建 18 修改 19 列表 20 详情 23 删除
     * 10 客户
     * 26 新建 27 修改 28 列表 29 详情 32 删除
     * 11 联系人
     * 40 新建 41 修改 42 列表 43 详情 44 删除
     * 12 商机
     * 46 新建 47 修改 48 列表 49 详情 50 删除
     * 13 合同
     * 53 新建 54 修改 55 列表 56 详情 57 删除
     * 14 回款
     * 60 新建 61 修改 62 列表 63 详情 64 删除
     * 15 产品
     * 65 新建 66 修改 67 列表 68 详情 211 删除
     * 440 跟进记录
     * 442 新建 443 修改 441 列表 441 详情 444 删除
     *
     * @param crmEnum label
     * @return menuId
     */
    public Integer getMenuId(CrmEnum crmEnum) {
        /*
            跟进记录
         */
        if(crmEnum == null){
            switch (this){
                case ADD: return  442;
                case EDIT: return  443;
                case LIST:
                case READ: return  441;
                case DELETE: return  444;
            }
        }
        int start = 0;
        switch (crmEnum) {
            case LEADS:
                start = 16;
            case CUSTOMER: {
                if (crmEnum == CrmEnum.CUSTOMER) {
                    start = 25;
                }
                if (this == DELETE) {
                    return start + value + 2;
                } else {
                    return start + value;
                }
            }
            case CONTACTS:
                    start = 39;
            case PRODUCT:
                if (CrmEnum.PRODUCT == crmEnum) {
                    start = 64;
                }
            case BUSINESS:
                if (CrmEnum.BUSINESS == crmEnum) {
                    start = 45;
                }
            case CONTRACT:
                if (CrmEnum.CONTRACT == crmEnum) {
                    start = 52;
                }
            case RECEIVABLES:
                if (CrmEnum.RECEIVABLES == crmEnum) {
                    start = 59;
                }
                if(CrmEnum.PRODUCT == crmEnum && this == DELETE){
                    return 211;
                }
            case INVOICE:
                if (CrmEnum.INVOICE == crmEnum) {
                    start = 420;
                }
                return start + value;
        }
        return 0;
    }
}
