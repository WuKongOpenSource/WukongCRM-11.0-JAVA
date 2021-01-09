package com.kakarote.crm.common;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.log.BehaviorEnum;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.feign.admin.entity.SimpleDept;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.BaseUtil;
import com.kakarote.core.utils.TagUtil;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.constant.FieldEnum;
import com.kakarote.crm.entity.PO.CrmActionRecord;
import com.kakarote.crm.entity.PO.CrmCustomer;
import com.kakarote.crm.entity.PO.CrmMarketing;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Component
public class ActionRecordUtil {

    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(1, 20, 5L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(2048), new ThreadPoolExecutor.AbortPolicy());

    @Autowired
    private AdminService adminService;


    public static class ActionRecordTask implements Runnable {
        private static final Integer BATCH_NUMBER = 1;
        private static volatile List<CrmActionRecord> SQL_LIST = new CopyOnWriteArrayList<>();
        private UserInfo userInfo;

        public ActionRecordTask(CrmActionRecord actionRecord) {
            if (actionRecord != null) {
                SQL_LIST.add(actionRecord);
            }
            userInfo = UserUtil.getUser();
        }

        @Override
        public void run() {
            if (SQL_LIST.size() >= BATCH_NUMBER) {
                List<CrmActionRecord> list = new ArrayList<>(SQL_LIST);
                //底层已经做过size为0的判断，此处不再限制
                try {
                    UserUtil.setUser(userInfo);
                    ApplicationContextHolder.getBean(ICrmActionRecordService.class).saveBatch(list, BATCH_NUMBER);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    UserUtil.removeUser();
                    SQL_LIST.clear();
                }
            }
        }
    }


    public String getDetailByFormTypeAndValue(JSONObject record,String value){
        if (record == null){
            return "";
        }
        FieldEnum fieldEnum = FieldEnum.parse(record.getString("formType"));
        String oldFieldValue = this.parseValueByFieldEnum(value,fieldEnum);
        String newFieldValue = this.parseValueByFieldEnum(record.getString("value"),fieldEnum);
        String oldValue = StrUtil.isEmpty(oldFieldValue) ? "空" : oldFieldValue;
        String newValue = StrUtil.isEmpty(newFieldValue) ? "空" : newFieldValue;
        return  "将" + record.getString("name") + " 由" + oldValue + "修改为" + newValue + "。";
    }



    private String parseValueByFieldEnum(String value, FieldEnum fieldEnum){
        if(StrUtil.isEmpty(value)){
            return null;
        }
        if (Arrays.asList(FieldEnum.USER,FieldEnum.SINGLE_USER).contains(fieldEnum)) {
            List<SimpleUser> simpleUsers = adminService.queryUserByIds(TagUtil.toLongSet(value)).getData();
            value = simpleUsers.stream().map(SimpleUser::getRealname).collect(Collectors.joining(Const.SEPARATOR));
        } else if (FieldEnum.STRUCTURE.equals(fieldEnum)) {
            List<SimpleDept> simpleDepts = adminService.queryDeptByIds(TagUtil.toSet(value)).getData();
            value = simpleDepts.stream().map(SimpleDept::getName).collect(Collectors.joining(Const.SEPARATOR));
        } else if (FieldEnum.FILE.equals(fieldEnum)) {
            AdminFileService adminFileService = ApplicationContextHolder.getBean(AdminFileService.class);
            List<FileEntity> fileEntities = adminFileService.queryFileList(value).getData();
            value = fileEntities.stream().map(FileEntity::getName).collect(Collectors.joining(Const.SEPARATOR));
        }
        return value;
    }

    /**
     * 属性kv
     */
    private static Map<Integer, Dict> propertiesMap = new HashMap<>();

    static {
        propertiesMap.put(CrmEnum.LEADS.getType(), Dict.create().set("leadsName", "线索名称").set("address", "地址").set("mobile", "手机").set("nextTime", "下次联系时间").set("remark", "备注").set("email", "邮箱").set("telephone", "电话"));
        propertiesMap.put(CrmEnum.CUSTOMER.getType(), Dict.create().set("customerName", "客户名称").set("address", "省市区").set("location", "详细地址").set("mobile", "手机").set("nextTime", "下次联系时间").set("remark", "备注").set("telephone", "电话").set("website", "网址"));
        propertiesMap.put(CrmEnum.CONTACTS.getType(), Dict.create().set("name", "姓名").set("customerId", "客户姓名").set("mobile", "手机").set("nextTime", "下次联系时间").set("remark", "备注").set("telephone", "电话").set("email", "电子邮箱").set("post", "职务").set("address", "地址"));
        propertiesMap.put(CrmEnum.BUSINESS.getType(), Dict.create().set("businessName", "商机名称").set("customerId", "客户姓名").set("money", "商机金额").set("dealDate", "预计成交日期").set("remark", "备注").set("typeId", "商机状态组").set("statusId", "商机阶段").set("totalPrice", "总金额").set("discountRate", "整单折扣（%）"));
        propertiesMap.put(CrmEnum.CONTRACT.getType(), Dict.create().set("num", "合同编号").set("name", "合同名称").set("customerId", "客户名称").set("contactsId", "客户签约人").set("businessId", "商机名称").set("orderDate", "下单时间").set("money", "合同金额").set("startTime", "合同开始时间").set("endTime", "合同结束时间").set("companyUserId", "公司签约人").set("remark", "备注").set("totalPrice", "总金额").set("discountRate", "整单折扣（%）"));
        propertiesMap.put(CrmEnum.RECEIVABLES.getType(), Dict.create().set("number", "回款编号").set("customerId", "客户姓名").set("contractId", "合同编号").set("returnTime", "回款日期").set("money", "回款金额").set("planId", "期数").set("remark", "备注"));
        propertiesMap.put(CrmEnum.PRODUCT.getType(), Dict.create().set("name", "产品名称").set("categoryId", "产品类型").set("num", "产品编码").set("price", "价格").set("description", "产品描述"));
        propertiesMap.put(CrmEnum.MARKETING.getType(), Dict.create().set("marketingName", "活动名称").set("crmType", "关联对象").set("relationUserId", "参与人员").set("marketingType", "活动类型").set("startTime", "开始时间").set("endTime", "截止时间").set("browse", "浏览数").set("submitNum", "提交数").set("marketingMoney", "活动预算").set("address", "活动地址").set("synopsis", "活动简介"));
        propertiesMap.put(CrmEnum.RETURN_VISIT.getType(), Dict.create().set("visitNumber", "回访编号").set("visitTime", "回访时间").set("ownerUserId", "回访人").set("customerId", "客户名称").set("contractId", "合同编号").set("contactsId", "联系人"));
        propertiesMap.put(CrmEnum.INVOICE.getType(), Dict.create().set("invoiceApplyNumber", "发票申请编号").set("customerId", "客户名称").set("contractId", "合同编号").set("invoiceMoney", "开票金额").set("invoiceDate", "开票日期").set("invoiceType", "开票类型").set("remark", "备注").set("titleType", "抬头类型").set("invoiceTitle", "开票抬头").set("taxNumber", "纳税识别号").set("depositBank", "开户银行").set("depositAccount", "开户账户").set("depositAddress", "开票地址").set("contactsName", "联系人名称").set("contactsTelephone", "联系方式").set("contactsAddress", "邮寄地址"));
    }


    private List<String> textList = new ArrayList<>();

    /**
     * 更新记录
     *
     * @param oldObj  之前对象
     * @param newObj  新对象
     * @param crmEnum 类型
     */
    @SuppressWarnings("unchecked")
    public void updateRecord(Map<String, Object> oldObj, Map<String, Object> newObj, CrmEnum crmEnum, String name, Integer actionId) {
        try {
            CrmActionRecord crmActionRecord = new CrmActionRecord();
            crmActionRecord.setCreateUserId(UserUtil.getUserId());
            crmActionRecord.setCreateTime(new Date());
            searchChange(textList, oldObj, newObj, crmEnum.getType());
            crmActionRecord.setTypes(crmEnum.getType());
            crmActionRecord.setActionId(actionId);
            crmActionRecord.setContent(JSON.toJSONString(textList));
            crmActionRecord.setIpAddress("127.0.0.1");
            crmActionRecord.setDetail(StrUtil.join("", textList));
            crmActionRecord.setBehavior(BehaviorEnum.UPDATE.getType());
            crmActionRecord.setObject(name);
            if (textList.size() > 0) {
                ActionRecordTask actionRecordTask = new ActionRecordTask(crmActionRecord);
                THREAD_POOL.execute(actionRecordTask);
            }
        } finally {
            textList.clear();
        }

    }

    public void addRecord(Integer actionId, CrmEnum crmEnum, String name) {
        CrmActionRecord crmActionRecord = new CrmActionRecord();
        crmActionRecord.setCreateUserId(UserUtil.getUserId());
        crmActionRecord.setCreateTime(new Date());
        crmActionRecord.setTypes(crmEnum.getType());
        crmActionRecord.setActionId(actionId);
        ArrayList<String> strings = new ArrayList<>();
        strings.add("新建了" + crmEnum.getRemarks());
        crmActionRecord.setContent(JSON.toJSONString(strings));
        crmActionRecord.setIpAddress("127.0.0.1");
        crmActionRecord.setDetail("新建了" + crmEnum.getRemarks() + "：" + name);
        crmActionRecord.setBehavior(BehaviorEnum.SAVE.getType());
        crmActionRecord.setObject(name);
        ActionRecordTask actionRecordTask = new ActionRecordTask(crmActionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }

    public void updateRecord(List<CrmModelFiledVO> newFieldList, Dict kv) {
        textList.clear();
        if (newFieldList == null) {
            return;
        }
        List<CrmModelFiledVO> oldFieldList = ApplicationContextHolder.getBean(ICrmActionRecordService.class).queryFieldValue(kv);
        newFieldList.forEach(newField -> {
            for (CrmModelFiledVO oldField : oldFieldList) {
                String oldFieldValue;
                String newFieldValue;
                if (ObjectUtil.isEmpty(oldField.getValue()) && ObjectUtil.isEmpty(newField.getValue())) {
                    continue;
                }
                if (ObjectUtil.isEmpty(oldField.getValue())) {
                    oldFieldValue = "空";
                } else {
                    oldFieldValue = oldField.getValue().toString();
                }
                if (ObjectUtil.isEmpty(newField.getValue())) {
                    newFieldValue = "空";
                } else {
                    newFieldValue = newField.getValue().toString();
                }
                if (oldField.getName().equals(newField.getName()) && !oldFieldValue.equals(newFieldValue)) {
                    textList.add("将" + oldField.getName() + " 由" + oldFieldValue + "修改为" + newFieldValue + "。");
                }
            }
        });
    }

    private void searchChange(List<String> textList, Map<String, Object> oldObj, Map<String, Object> newObj, Integer crmTypes) {
        for (String oldKey : oldObj.keySet()) {
            for (String newKey : newObj.keySet()) {
                if (propertiesMap.get(crmTypes).containsKey(oldKey)) {
                    Object oldValue = oldObj.get(oldKey);
                    Object newValue = newObj.get(newKey);
                    if (oldValue instanceof Date) {
                        oldValue = DateUtil.formatDateTime((Date) oldValue);
                    }
                    if (newValue instanceof Date) {
                        newValue = DateUtil.formatDateTime((Date) newValue);
                    }
                    if (ObjectUtil.isEmpty(oldValue) || ("address".equals(oldKey) && ",,".equals(oldValue))) {
                        oldValue = "空";
                    }
                    if (ObjectUtil.isEmpty(newValue) || ("address".equals(newKey) && ",,".equals(newValue))) {
                        newValue = "空";
                    }
                    if (oldValue instanceof BigDecimal || newValue instanceof BigDecimal) {
                        oldValue = Convert.toBigDecimal(oldValue, new BigDecimal(0)).setScale(2, BigDecimal.ROUND_UP).toString();
                        newValue = Convert.toBigDecimal(newValue, new BigDecimal(0)).setScale(2, BigDecimal.ROUND_UP).toString();
                    }
                    if (newKey.equals(oldKey) && !oldValue.equals(newValue)) {
                        switch (oldKey) {
                            case "companyUserId":
                                if (!"空".equals(newValue)) {
                                    newValue = UserCacheUtil.getUserName(Long.valueOf(newValue.toString()));
                                }
                                if (!"空".equals(oldValue)) {
                                    oldValue = UserCacheUtil.getUserName(Long.valueOf(oldValue.toString()));
                                }
                                break;
                            case "customerId":
                                if (!"空".equals(newValue)) {
                                    newValue = ApplicationContextHolder.getBean(ICrmCustomerService.class).getCustomerName(Integer.valueOf(newValue.toString()));
                                }
                                if (!"空".equals(oldValue)) {
                                    oldValue = ApplicationContextHolder.getBean(ICrmCustomerService.class).getCustomerName(Integer.valueOf(oldValue.toString()));
                                }
                                break;
                            case "businessId":
                                if (!"空".equals(newValue)) {
                                    newValue = ApplicationContextHolder.getBean(ICrmBusinessService.class).getBusinessName(Integer.parseInt(newValue.toString()));
                                }
                                if (!"空".equals(oldValue)) {
                                    oldValue = ApplicationContextHolder.getBean(ICrmBusinessService.class).getBusinessName(Integer.parseInt(oldValue.toString()));
                                }
                                break;
                            case "contractId":
                                if (!"空".equals(newValue)) {
                                    newValue = ApplicationContextHolder.getBean(ICrmContractService.class).getContractName(Integer.parseInt(newValue.toString()));
                                }
                                if (!"空".equals(oldValue)) {
                                    oldValue = ApplicationContextHolder.getBean(ICrmContractService.class).getContractName(Integer.parseInt(oldValue.toString()));
                                }
                                break;
                            case "contactsId":
                                if (!"空".equals(newValue)) {
                                    newValue = ApplicationContextHolder.getBean(ICrmContactsService.class).getContactsName(Integer.parseInt(newValue.toString()));
                                }
                                if (!"空".equals(oldValue)) {
                                    oldValue = ApplicationContextHolder.getBean(ICrmContactsService.class).getContactsName(Integer.parseInt(oldValue.toString()));
                                }
                                break;
                            case "typeId":
                                if (!"空".equals(newValue)) {
                                    newValue = ApplicationContextHolder.getBean(ICrmBusinessTypeService.class).getBusinessTypeName(Integer.parseInt(newValue.toString()));
                                }
                                if (!"空".equals(oldValue)) {
                                    oldValue = ApplicationContextHolder.getBean(ICrmBusinessTypeService.class).getBusinessTypeName(Integer.parseInt(oldValue.toString()));
                                }
                                break;
                            case "statusId":
                                if (!"空".equals(newValue)) {
                                    newValue = ApplicationContextHolder.getBean(ICrmBusinessStatusService.class).getBusinessStatusName(Integer.parseInt(newValue.toString()));
                                }
                                if (!"空".equals(oldValue)) {
                                    oldValue = ApplicationContextHolder.getBean(ICrmBusinessStatusService.class).getBusinessStatusName(Integer.parseInt(oldValue.toString()));
                                }
                                break;
                            case "planId":
                                if (!"空".equals(newValue)) {
                                    newValue = ApplicationContextHolder.getBean(ICrmReceivablesPlanService.class).getReceivablesPlanNum(Integer.parseInt(newValue.toString()));
                                }
                                if (!"空".equals(oldValue)) {
                                    oldValue = ApplicationContextHolder.getBean(ICrmReceivablesPlanService.class).getReceivablesPlanNum(Integer.parseInt(oldValue.toString()));
                                }
                                break;
                            case "categoryId":
                                if (!"空".equals(newValue)) {
                                    newValue = ApplicationContextHolder.getBean(ICrmProductCategoryService.class).getProductCategoryName(Integer.parseInt(newValue.toString()));
                                }
                                if (!"空".equals(oldValue)) {
                                    oldValue = ApplicationContextHolder.getBean(ICrmProductCategoryService.class).getProductCategoryName(Integer.parseInt(oldValue.toString()));
                                }
                                break;
                            case "crmType":
                                if (!"空".equals(newValue)) {
                                    newValue = newValue.equals(1) ? "线索" : "客户";
                                }
                                if (!"空".equals(oldValue)) {
                                    oldValue = oldValue.equals(1) ? "线索" : "客户";
                                }
                                break;
                            case "relationUserId":
                                if (!"空".equals(newValue)) {
                                    List<SimpleUser> newList = ApplicationContextHolder.getBean(AdminService.class).queryUserByIds(TagUtil.toLongSet((String) newValue)).getData();
                                    newValue = newList.stream().map(SimpleUser::getRealname).collect(Collectors.joining(","));
                                }
                                if (!"空".equals(oldValue)) {
                                    List<SimpleUser> oldList = ApplicationContextHolder.getBean(AdminService.class).queryUserByIds(TagUtil.toLongSet((String) oldValue)).getData();
                                    oldValue = oldList.stream().map(SimpleUser::getRealname).collect(Collectors.joining(","));
                                }
                                break;
                            default:
                                break;
                        }

                        if (ObjectUtil.isEmpty(oldValue)) {
                            oldValue = "空";
                        }
                        if (ObjectUtil.isEmpty(newValue)) {
                            newValue = "空";
                        }
                        textList.add("将" + propertiesMap.get(crmTypes).get(oldKey) + " 由" + oldValue + "修改为" + newValue + "。");
                    }
                }
            }
        }
    }

    public void publicContentRecord(CrmEnum crmEnum, BehaviorEnum behaviorEnum, Integer actionId, String name, String detail) {
        CrmActionRecord actionRecord = new CrmActionRecord();
        actionRecord.setCreateUserId(UserUtil.getUserId());
        actionRecord.setCreateTime(new Date());
        actionRecord.setIpAddress(BaseUtil.getIp());
        actionRecord.setTypes(crmEnum.getType());
        actionRecord.setBehavior(behaviorEnum.getType());
        actionRecord.setActionId(actionId);
        actionRecord.setContent("[\"" + detail + "\"]");
        actionRecord.setDetail(detail);
        actionRecord.setObject(name);
        ActionRecordTask actionRecordTask = new ActionRecordTask(actionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }


    /**
     * 添加转移记录
     *
     * @param actionId
     */
    public void addConversionRecord(Integer actionId, CrmEnum crmEnum, Long userId, String name) {
        String userName = UserCacheUtil.getUserName(userId);
        CrmActionRecord crmActionRecord = new CrmActionRecord();
        crmActionRecord.setCreateUserId(UserUtil.getUserId());
        crmActionRecord.setCreateTime(new Date());
        crmActionRecord.setTypes(crmEnum.getType());
        crmActionRecord.setActionId(actionId);
        ArrayList<String> strings = new ArrayList<>();
        strings.add("将" + crmEnum.getRemarks() + "转移给：" + userName);
        crmActionRecord.setContent(JSON.toJSONString(strings));
        crmActionRecord.setIpAddress(BaseUtil.getIp());
//        String name = Db.queryStr("select " + crmEnum.getTableNameField() + " from " + crmEnum.getTableName() + " where " + crmEnum.getTableId() + " = ?", actionId);
        crmActionRecord.setDetail("将" + crmEnum.getRemarks() + "：" + name + "转移给：" + userName);
        crmActionRecord.setBehavior(BehaviorEnum.CHANGE_OWNER.getType());
        crmActionRecord.setObject(name);
        ActionRecordTask actionRecordTask = new ActionRecordTask(crmActionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }

    @Autowired
    private ICrmCustomerService crmCustomerService;

    /**
     * 添加(锁定/解锁)记录
     */
    public void addIsLockRecord(List<String> ids, CrmEnum crmEnum, Integer isLock) {
        CrmActionRecord crmActionRecord = new CrmActionRecord();
        crmActionRecord.setCreateUserId(UserUtil.getUserId());
        crmActionRecord.setCreateTime(new Date());
        crmActionRecord.setTypes(crmEnum.getType());
        crmActionRecord.setIpAddress(BaseUtil.getIp());
        ArrayList<String> strings = new ArrayList<>();
        if (isLock == 2) {
            strings.add("将客户锁定。");
            crmActionRecord.setBehavior(BehaviorEnum.LOCK.getType());
        } else {
            strings.add("将客户解锁。");
            crmActionRecord.setBehavior(BehaviorEnum.UNLOCK.getType());
        }
        crmActionRecord.setContent(JSON.toJSONString(strings));
        for (String actionId : ids) {
            String name = crmCustomerService.lambdaQuery().select(CrmCustomer::getCustomerName).eq(CrmCustomer::getCustomerId, actionId).one().getCustomerName();
//            String name = Db.queryStr("select " + crmEnum.getTableNameField() + " from " + crmEnum.getTableName() + " where " + crmEnum.getTableId() + " = ?", actionId);
            String detail;
            if (isLock == 2) {
                detail = "将客户：" + name + "锁定";
            } else {
                detail = "将客户：" + name + "解锁";
            }
            crmActionRecord.setDetail(detail);
            crmActionRecord.setId(null);
            crmActionRecord.setActionId(Integer.valueOf(actionId));
            crmActionRecord.setObject(name);
            ActionRecordTask actionRecordTask = new ActionRecordTask(crmActionRecord);
            THREAD_POOL.execute(actionRecordTask);
        }
    }

    /**
     * 线索转化客户
     *
     * @param actionId
     */
    public void addConversionCustomerRecord(Integer actionId, CrmEnum crmEnum, String name) {
        CrmActionRecord crmActionRecord = new CrmActionRecord();
        crmActionRecord.setCreateUserId(UserUtil.getUserId());
        crmActionRecord.setCreateTime(new Date());
        crmActionRecord.setTypes(crmEnum.getType());
        crmActionRecord.setActionId(actionId);
        ArrayList<String> strings = new ArrayList<>();
        strings.add("将线索\"" + name + "\"转化为客户");
        crmActionRecord.setContent(JSON.toJSONString(strings));
        crmActionRecord.setIpAddress(BaseUtil.getIp());
        crmActionRecord.setDetail(strings.get(0));
        crmActionRecord.setBehavior(BehaviorEnum.TRANSFER.getType());
        crmActionRecord.setObject(name);
        ActionRecordTask actionRecordTask = new ActionRecordTask(crmActionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }

    /**
     * 放入公海
     */
    public void addPutIntoTheOpenSeaRecord(Integer actionId, CrmEnum crmEnum, String name) {
        CrmActionRecord crmActionRecord = new CrmActionRecord();
        crmActionRecord.setCreateUserId(UserUtil.getUserId());
        crmActionRecord.setCreateTime(new Date());
        crmActionRecord.setTypes(crmEnum.getType());
        ArrayList<String> strings = new ArrayList<>();
        strings.add("将客户放入公海");
        crmActionRecord.setContent(JSON.toJSONString(strings));
        crmActionRecord.setIpAddress(BaseUtil.getIp());
        crmActionRecord.setBehavior(BehaviorEnum.PUT_IN_POOL.getType());
        crmActionRecord.setDetail("将客户：" + name + "放入公海");
        crmActionRecord.setId(null);
        crmActionRecord.setActionId(actionId);
        crmActionRecord.setObject(name);
        ActionRecordTask actionRecordTask = new ActionRecordTask(crmActionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }


    /**
     * 添加分配客户记录
     */
    public void addDistributionRecord(Integer actionId, CrmEnum crmEnum, Long userId, String name) {

        ArrayList<String> strings = new ArrayList<>();
        CrmActionRecord crmActionRecord = new CrmActionRecord();
        crmActionRecord.setCreateUserId(UserUtil.getUserId());
        crmActionRecord.setCreateTime(new Date());
        crmActionRecord.setTypes(crmEnum.getType());
        crmActionRecord.setActionId(actionId);
        if (userId == null) {
            //领取
            strings.add("领取了客户");
            crmActionRecord.setDetail("领取了客户：" + name);
            crmActionRecord.setBehavior(BehaviorEnum.RECEIVE.getType());
        } else {
            String userName = UserCacheUtil.getUserName(userId);
            //管理员分配
            strings.add("将客户分配给：" + userName);
            crmActionRecord.setDetail("将客户：" + name + "分配给：" + userName);
            crmActionRecord.setBehavior(BehaviorEnum.DISTRIBUTE.getType());
        }
        crmActionRecord.setContent(JSON.toJSONString(strings));
        crmActionRecord.setIpAddress(BaseUtil.getIp());
        crmActionRecord.setObject(name);
        ActionRecordTask actionRecordTask = new ActionRecordTask(crmActionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }

    public void addDeleteActionRecord(CrmEnum crmEnum, String name, Integer actionId) {
        CrmActionRecord actionRecord = new CrmActionRecord();
        actionRecord.setCreateUserId(UserUtil.getUserId());
        actionRecord.setCreateTime(new Date());
        actionRecord.setIpAddress(BaseUtil.getIp());
        actionRecord.setTypes(crmEnum.getType());
        actionRecord.setBehavior(BehaviorEnum.DELETE.getType());
        actionRecord.setActionId(Integer.valueOf(actionId));
        actionRecord.setDetail("删除了" + crmEnum.getRemarks() + "：" + name);
        actionRecord.setObject(name);
        ActionRecordTask actionRecordTask = new ActionRecordTask(actionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }

    public void addMemberActionRecord(CrmEnum crmEnum, Integer actionId, Long userId, String name) {
        CrmActionRecord actionRecord = new CrmActionRecord();
        actionRecord.setCreateUserId(UserUtil.getUserId());
        actionRecord.setCreateTime(new Date());
        actionRecord.setIpAddress(BaseUtil.getIp());
        actionRecord.setTypes(crmEnum.getType());
        actionRecord.setBehavior(BehaviorEnum.ADD_MEMBER.getType());
        actionRecord.setActionId(actionId);
        String userName = UserCacheUtil.getUserName(userId);
        actionRecord.setDetail("给" + crmEnum.getRemarks() + "：" + name + "添加了团队成员：" + userName);
        actionRecord.setObject(name);
        ActionRecordTask actionRecordTask = new ActionRecordTask(actionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }

    public void addDeleteMemberActionRecord(CrmEnum crmEnum, Integer actionId, Long userId, boolean isSelf, String name) {
        CrmActionRecord actionRecord = new CrmActionRecord();
        actionRecord.setCreateUserId(UserUtil.getUserId());
        actionRecord.setCreateTime(new Date());
        actionRecord.setIpAddress(BaseUtil.getIp());
        actionRecord.setTypes(crmEnum.getType());
        actionRecord.setActionId(actionId);
//        String name = Db.queryStr("select " + crmEnum.getTableNameField() + " from " + crmEnum.getTableName() + " where " + crmEnum.getTableId() + " = ?", actionId);
        if (isSelf) {
            actionRecord.setBehavior(BehaviorEnum.EXIT_MEMBER.getType());
            actionRecord.setDetail("退出了" + crmEnum.getRemarks() + "：" + name + "的团队成员");
        } else {
            actionRecord.setBehavior(BehaviorEnum.REMOVE_MEMBER.getType());
            String userName = UserCacheUtil.getUserName(userId);
            actionRecord.setDetail("移除了" + crmEnum.getRemarks() + "：" + name + "的团队成员：" + userName);
        }
        actionRecord.setObject(name);
        ActionRecordTask actionRecordTask = new ActionRecordTask(actionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }

    public void addExportActionRecord(CrmEnum crmEnum, Integer number) {
        CrmActionRecord actionRecord = new CrmActionRecord();
        actionRecord.setCreateUserId(UserUtil.getUserId());
        actionRecord.setCreateTime(new Date());
        actionRecord.setIpAddress(BaseUtil.getIp());
        actionRecord.setTypes(crmEnum.getType());
        actionRecord.setBehavior(BehaviorEnum.EXCEL_EXPORT.getType());
        actionRecord.setDetail("导出了" + number + "条" + crmEnum.getRemarks());
        ActionRecordTask actionRecordTask = new ActionRecordTask(actionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }

    public void addFollowupActionRecord(Integer crmType, Integer actionId, String name) {
        CrmEnum crmEnum = CrmEnum.parse(crmType);
        CrmActionRecord actionRecord = new CrmActionRecord();
        actionRecord.setCreateUserId(UserUtil.getUserId());
        actionRecord.setCreateTime(new Date());
        actionRecord.setIpAddress(BaseUtil.getIp());
        actionRecord.setTypes(crmEnum.getType());
        actionRecord.setActionId(actionId);
        actionRecord.setBehavior(BehaviorEnum.FOLLOW_UP.getType());
//        String name = Db.queryStr("select " + crmEnum.getTableNameField() + " from " + crmEnum.getTableName() + " where " + crmEnum.getTableId() + " = ?", actionId);
        actionRecord.setDetail("给" + crmEnum.getRemarks() + "：" + name + "新建了跟进记录");
        actionRecord.setObject(name);
        ActionRecordTask actionRecordTask = new ActionRecordTask(actionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }

    public void addCancelActionRecord(CrmEnum crmEnum, Integer actionId, String name) {
        CrmActionRecord actionRecord = new CrmActionRecord();
        actionRecord.setCreateUserId(UserUtil.getUserId());
        actionRecord.setCreateTime(new Date());
        actionRecord.setIpAddress(BaseUtil.getIp());
        actionRecord.setTypes(crmEnum.getType());
        actionRecord.setBehavior(BehaviorEnum.CANCEL_EXAMINE.getType());
        actionRecord.setActionId(actionId);
//        String name = Db.queryStr("select " + crmEnum.getTableNameField() + " from " + crmEnum.getTableName() + " where " + crmEnum.getTableId() + " = ?", actionId);
        actionRecord.setDetail("将" + crmEnum.getRemarks() + "：" + name + "作废");
        actionRecord.setObject(name);
        ActionRecordTask actionRecordTask = new ActionRecordTask(actionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }

    public void addObjectSaveRecord(CrmEnum crmEnum, Integer actionId, String name) {
        CrmActionRecord actionRecord = new CrmActionRecord();
        actionRecord.setCreateUserId(UserUtil.getUserId());
        actionRecord.setCreateTime(new Date());
        actionRecord.setIpAddress(BaseUtil.getIp());
        actionRecord.setTypes(crmEnum.getType());
        actionRecord.setBehavior(BehaviorEnum.SAVE.getType());
        actionRecord.setActionId(actionId);
//        String name = Db.queryStr("select " + crmEnum.getTableNameField() + " from " + crmEnum.getTableName() + " where " + crmEnum.getTableId() + " = ?", actionId);
        actionRecord.setDetail("新建了" + crmEnum.getRemarks() + "：" + name);
        actionRecord.setObject(name);
        ActionRecordTask actionRecordTask = new ActionRecordTask(actionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }

    public void addMarketingUpdateStatusRecord(String[] ids, CrmEnum crmEnum, Integer status) {
        CrmActionRecord crmActionRecord = new CrmActionRecord();
        crmActionRecord.setCreateUserId(UserUtil.getUserId());
        crmActionRecord.setCreateTime(new Date());
        crmActionRecord.setTypes(crmEnum.getType());
        crmActionRecord.setIpAddress(BaseUtil.getIp());
        if (status == 1) {
            crmActionRecord.setBehavior(BehaviorEnum.START.getType());
        } else {
            crmActionRecord.setBehavior(BehaviorEnum.STOP.getType());
        }
        for (String actionId : ids) {
            CrmMarketing marketing = ApplicationContextHolder.getBean(ICrmMarketingService.class).getById(actionId);
            String name = marketing.getMarketingName();
            String detail;
            if (status == 1) {
                detail = "将活动：" + name + "启用";
            } else {
                detail = "将活动：" + name + "停用";
            }
            crmActionRecord.setDetail(detail);
            crmActionRecord.setId(null);
            crmActionRecord.setActionId(Integer.valueOf(actionId));
            crmActionRecord.setObject(name);
            ActionRecordTask actionRecordTask = new ActionRecordTask(crmActionRecord);
            THREAD_POOL.execute(actionRecordTask);
        }
    }

    public void addOaLogSaveRecord(CrmEnum crmEnum, Integer actionId) {
        CrmActionRecord actionRecord = new CrmActionRecord();
        actionRecord.setCreateUserId(UserUtil.getUserId());
        actionRecord.setCreateTime(new Date());
        actionRecord.setIpAddress(BaseUtil.getIp());
        actionRecord.setTypes(crmEnum.getType());
        actionRecord.setBehavior(BehaviorEnum.SAVE.getType());
        actionRecord.setActionId(actionId);
        actionRecord.setDetail("新建了" + crmEnum.getRemarks() + "：" + DateUtil.formatDate(new Date()));
        actionRecord.setObject(DateUtil.formatDate(new Date()));
        ActionRecordTask actionRecordTask = new ActionRecordTask(actionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }

    public void addOaLogUpdateRecord(CrmEnum crmEnum, Integer actionId, String date) {
        CrmActionRecord actionRecord = new CrmActionRecord();
        actionRecord.setCreateUserId(UserUtil.getUserId());
        actionRecord.setCreateTime(new Date());
        actionRecord.setIpAddress(BaseUtil.getIp());
        actionRecord.setTypes(crmEnum.getType());
        actionRecord.setBehavior(BehaviorEnum.UPDATE.getType());
        actionRecord.setActionId(actionId);
        actionRecord.setDetail("编辑了" + crmEnum.getRemarks() + "：" + date);
        actionRecord.setObject(date);
        ActionRecordTask actionRecordTask = new ActionRecordTask(actionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }

    public void addOaExamineActionRecord(CrmEnum crmEnum, Integer actionId, BehaviorEnum behaviorEnum, String content) {
        CrmActionRecord actionRecord = new CrmActionRecord();
        actionRecord.setCreateUserId(UserUtil.getUserId());
        actionRecord.setCreateTime(new Date());
        actionRecord.setIpAddress(BaseUtil.getIp());
        actionRecord.setTypes(crmEnum.getType());
        actionRecord.setBehavior(behaviorEnum.getType());
        actionRecord.setActionId(actionId);
        if (content.length() > 20) {
            content = content.substring(0, 20) + "...";
        }
        String prefix = "";
        switch (behaviorEnum) {
            case SAVE:
                prefix = "新建了";
                break;
            case UPDATE:
                prefix = "编辑了";
                break;
            case RECHECK_EXAMINE:
                prefix = "撤回了";
                break;
            case PASS_EXAMINE:
                prefix = "通过了";
                break;
            case REJECT_EXAMINE:
                prefix = "驳回了";
                break;
            case DELETE:
                prefix = "删除了";
                break;
            default:
                break;
        }
        actionRecord.setDetail(prefix + crmEnum.getRemarks() + "：" + content);
        actionRecord.setObject(content);
        ActionRecordTask actionRecordTask = new ActionRecordTask(actionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }

    public void addCrmExamineActionRecord(CrmEnum crmEnum, Integer actionId, BehaviorEnum behaviorEnum, String number) {
        CrmActionRecord actionRecord = new CrmActionRecord();
        actionRecord.setCreateUserId(UserUtil.getUserId());
        actionRecord.setCreateTime(new Date());
        actionRecord.setIpAddress(BaseUtil.getIp());
        actionRecord.setTypes(crmEnum.getType());
        actionRecord.setBehavior(behaviorEnum.getType());
        actionRecord.setActionId(actionId);
//        String number = Db.queryStr("select " + crmEnum.getTableNameField() + " from " + crmEnum.getTableName() + " where examine_record_id = ?", actionId);
        String prefix = "";
        switch (behaviorEnum) {
            case SUBMIT_EXAMINE:
                prefix = "提交了";
                break;
            case RECHECK_EXAMINE:
                prefix = "撤回了";
                break;
            case PASS_EXAMINE:
                prefix = "通过了";
                break;
            case REJECT_EXAMINE:
                prefix = "驳回了";
                break;
            default:
                break;
        }
        actionRecord.setDetail(prefix + crmEnum.getRemarks() + "：" + number);
        actionRecord.setObject(number);
        ActionRecordTask actionRecordTask = new ActionRecordTask(actionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }

    /**
     * 通用模板，无需特殊处理的操作记录适用
     *
     * @param crmEnum
     * @param actionId
     * @param behaviorEnum
     */
    public void addObjectActionRecord(CrmEnum crmEnum, Integer actionId, BehaviorEnum behaviorEnum, String name) {
        CrmActionRecord actionRecord = new CrmActionRecord();
        actionRecord.setCreateUserId(UserUtil.getUserId());
        actionRecord.setCreateTime(new Date());
        actionRecord.setIpAddress(BaseUtil.getIp());
        actionRecord.setTypes(crmEnum.getType());
        actionRecord.setBehavior(behaviorEnum.getType());
        actionRecord.setActionId(actionId);
        String detail;
        switch (behaviorEnum) {
            case CANCEL_EXAMINE:
                detail = "将" + crmEnum.getRemarks() + "：" + name + "作废";
                break;
            case FOLLOW_UP:
                detail = "给" + crmEnum.getRemarks() + "：" + name + "新建了跟进记录";
                break;
            default:
                detail = behaviorEnum.getName() + "了" + crmEnum.getRemarks() + "：" + name;
                break;
        }
        actionRecord.setDetail(detail);
        actionRecord.setObject(name);
        ActionRecordTask actionRecordTask = new ActionRecordTask(actionRecord);
        THREAD_POOL.execute(actionRecordTask);
    }
}
