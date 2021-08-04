package com.kakarote.hrm.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.common.cache.AdminCacheKey;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.feign.admin.entity.AdminMessageEnum;
import com.kakarote.core.feign.admin.service.AdminMessageService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.redis.Redis;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.upload.UploadConfig;
import com.kakarote.core.utils.BaseUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.hrm.common.HrmCodeEnum;
import com.kakarote.hrm.constant.*;
import com.kakarote.hrm.entity.BO.UploadExcelBO;
import com.kakarote.hrm.entity.PO.*;
import com.kakarote.hrm.service.*;
import com.kakarote.hrm.service.actionrecord.impl.EmployeeActionRecordServiceImpl;
import com.kakarote.hrm.utils.EmployeeCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HrmUploadExcelServiceImpl implements HrmUploadExcelService {

    @Autowired
    private AdminService adminService;

    @Autowired
    private Redis redis;

    private String uploadPath = ApplicationContextHolder.getBean(UploadConfig.class).getLocal().getUploadPath().get("0");

    /**
     * 上传excel可查询时间
     */
    protected static final int UPLOAD_EXCEL_EXIST_TIME = 7200;

    @Resource
    private ThreadPoolTaskExecutor hrmThreadPoolExecutor;



    /**
     * 导入excel
     */
    @Override
    public Long uploadExcel(MultipartFile file, UploadExcelBO uploadExcelBO) {
        String filePath = getFilePath(file);
        uploadExcelBO.setFilePath(filePath);
        AdminMessage adminMessage = new AdminMessage();
        adminMessage.setCreateUser(UserUtil.getUserId());
        adminMessage.setCreateTime(DateUtil.formatDateTime(new Date()));
        adminMessage.setRecipientUser(UserUtil.getUserId());
        adminMessage.setLabel(8);
        adminMessage.setType(AdminMessageEnum.HRM_EMPLOYEE_IMPORT.getType());
        Long messageId = adminService.saveOrUpdateMessage(adminMessage).getData();
        uploadExcelBO.setMessageId(messageId);
        uploadExcelBO.setUserInfo(UserUtil.getUser());
        redis.setex(AdminCacheKey.UPLOAD_EXCEL_MESSAGE_PREFIX + messageId.toString(), UPLOAD_EXCEL_EXIST_TIME, 0);
        UploadService uploadService = new EmployeeUploadService(uploadExcelBO);
        hrmThreadPoolExecutor.execute(uploadService);
        return messageId;
    }

    /**
     * 获取临时文件路径,用完记得删除
     *
     * @param file file
     * @return path
     */
    private String getFilePath(MultipartFile file) {
        String dirPath = FileUtil.getTmpDirPath();
        try {
            InputStream inputStream = file.getInputStream();
            File fromStream = FileUtil.writeFromStream(inputStream, dirPath + "/" + IdUtil.simpleUUID() + file.getOriginalFilename());
            return fromStream.getAbsolutePath();
        } catch (IOException e) {
            throw new CrmException(SystemCodeEnum.SYSTEM_UPLOAD_FILE_ERROR);
        }
    }

    public abstract class UploadService implements Runnable {

        protected List<List<Object>> errorList = new CopyOnWriteArrayList<>();

        protected List<HrmEmployeeField> fieldList = new CopyOnWriteArrayList<>();

        protected List<HrmEmployeeField> fixedFieldList = new CopyOnWriteArrayList<>();

        protected List<HrmEmployeeField> uniqueList = new CopyOnWriteArrayList<>();

        protected volatile AtomicInteger num = new AtomicInteger(0);

        protected volatile AtomicInteger updateNum = new AtomicInteger(0);

        protected List<Integer> isNullList = new CopyOnWriteArrayList<>();

        protected volatile boolean templateErr = false;

        protected volatile JSONObject kv = new JSONObject();

        public abstract void importExcel();

        public abstract UploadExcelBO getUploadExcelBO();

        @Override
        public abstract void run();


        /**
         * 查询导入顺序
         *
         * @param rowList 行数据
         * @author zhangzhiwei
         */
        protected void queryExcelHead(List<Object> rowList) {
            fieldList = ApplicationContextHolder.getBean(IHrmEmployeeService.class).downloadExcelFiled();
            HashMap<String, String> nameMap = new HashMap<>();
            HashMap<String, Integer> isNullMap = new HashMap<>();
            fieldList.forEach(filed -> {
                if (Objects.equals(1, filed.getIsFixed())) {
                    fixedFieldList.add(filed);
                }
                if (Objects.equals(1, filed.getIsUnique())) {
                    uniqueList.add(filed);
                }
                boolean isNull = Objects.equals(1, filed.getIsNull());
                String name = filed.getName() + (isNull ? "(*)" : "");
                if (filed.getType().equals(FieldTypeEnum.DATE.getValue())) {
                    name += "-例:[2020年10月1日]";
                }
                if ("dept_id".equals(filed.getFieldName())) {
                    name += "-(组织编码)";
                }
                nameMap.put(name, filed.getFieldName());
                isNullMap.put(name, filed.getIsNull());
            });
            List<String> nameList = new ArrayList<>(nameMap.keySet());
            if (nameList.size() != rowList.size() || !nameList.containsAll(rowList)) {
                templateErr = true;
            } else {
                for (int i = 0; i < rowList.size(); i++) {
                    kv.put(nameMap.get(rowList.get(i).toString()), i);
                    if (Objects.equals(1,isNullMap.get(rowList.get(i).toString()))) {
                        isNullList.add(i);
                    }
                }
            }
            rowList.add(0, "错误原因");
            errorList.add(rowList);
        }
    }


    public class EmployeeUploadService extends UploadService {

        private UploadExcelBO uploadExcelBO;

        private EmployeeUploadService(UploadExcelBO uploadExcelBO) {
            this.uploadExcelBO = uploadExcelBO;
        }

        @Override
        public void importExcel() {
            ExcelUtil.readBySax(uploadExcelBO.getFilePath(), 0, (int sheetIndex, int rowIndex, List<Object> rowList) -> {
                int num = this.num.incrementAndGet();
                redis.setex(AdminCacheKey.UPLOAD_EXCEL_MESSAGE_PREFIX + getUploadExcelBO().getMessageId().toString(), UPLOAD_EXCEL_EXIST_TIME, Math.max(num - 2, 0));
                if (rowList.size() < kv.entrySet().size()) {
                    for (int i = rowList.size() - 1; i < kv.entrySet().size(); i++) {
                        rowList.add(null);
                    }
                }
                if (this.num.get() > 10001) {
                    rowList.add(0, "最多同时导入10000条数据");
                    errorList.add(rowList);
                    return;
                }
                if (rowIndex > 1) {
                    if (templateErr) {
                        rowList.add(0, "请使用最新的模板");
                        errorList.add(rowList);
                    } else {
                        try {
                            for (Integer index : isNullList) {
                                if (ObjectUtil.isEmpty(rowList.get(index))) {
                                    throw new CrmException(HrmCodeEnum.REQUIRED_FIELDS_ARE_NOT_FILLED);
                                }
                            }
                            Map<Integer, List<HrmEmployeeField>> labelGroupMap = fieldList.stream().peek(field -> {
                                String fieldName = field.getFieldName();
                                Object object = rowList.get(kv.getInteger(fieldName));
                                Object value;
                                if (field.getType() == FieldTypeEnum.DATE.getValue() && object instanceof Long) {
                                    value = org.apache.poi.ss.usermodel.DateUtil.getJavaDate((Long) object);
                                } else if (field.getType() == FieldTypeEnum.NUMBER.getValue()) {
                                    value = Convert.toInt(object);
                                } else if ("dept_id".equals(field.getFieldName())) {
                                    if (ObjectUtil.isNotEmpty(object)) {
                                        Optional<HrmDept> hrmDeptOpt = ApplicationContextHolder.getBean(IHrmDeptService.class).lambdaQuery().eq(HrmDept::getCode, object).oneOpt();
                                        value = hrmDeptOpt.map(HrmDept::getDeptId).orElse(null);
                                        if (ObjectUtil.isEmpty(value)) {
                                            throw new CrmException(500, "部门不存在");
                                        }
                                    } else {
                                        value = null;
                                    }
                                } else if ("channel_id".equals(field.getFieldName())) {
                                    if (ObjectUtil.isNotNull(object)) {
                                        Optional<HrmRecruitChannel> hrmDeptOpt = ApplicationContextHolder.getBean(IHrmRecruitChannelService.class).lambdaQuery()
                                                .eq(HrmRecruitChannel::getValue, object).oneOpt();
                                        value = hrmDeptOpt.map(HrmRecruitChannel::getChannelId).orElse(null);
                                    } else {
                                        value = null;
                                    }
                                } else {
                                    value = Convert.toStr(object);
                                }
                                field.setValue(value);
                            }).collect(Collectors.groupingBy(HrmEmployeeField::getLabelGroup));
                            //只有个人信息个岗位信息值自定义字段,需要验证字段唯一性
                            List<HrmEmployeeField> employeeFields = labelGroupMap.get(LabelGroupEnum.PERSONAL.getValue());
                            employeeFields.addAll(labelGroupMap.get(LabelGroupEnum.POST.getValue()));
                            List<HrmEmployeeField> uniqueList = new ArrayList<>();
                            for (HrmEmployeeField employeeField : employeeFields) {
                                if (employeeField.getIsUnique() == IsEnum.YES.getValue()) {
                                    uniqueList.add(employeeField);
                                }
                            }
                            if (uniqueList.size() > 0) {
                                Integer uniqueCount = ApplicationContextHolder.getBean(IHrmEmployeeService.class).queryFieldValueNoDelete(uniqueList);
                                if (uniqueCount > 0) {
                                    if (Objects.equals(2, uploadExcelBO.getRepeatHandling())) {
                                        return;
                                    }
                                    if (uniqueCount > 1) {
                                        throw new CrmException(HrmCodeEnum.DATA_IS_DUPLICATED_WITH_MULTIPLE_UNIQUE_FIELDS);
                                    }
                                }
                            }
                            List<HrmEmployeeField> salaryCardFields = labelGroupMap.get(LabelGroupEnum.SALARY_CARD.getValue());
                            List<HrmEmployeeField> socialSecurityFields = labelGroupMap.get(LabelGroupEnum.SOCIAL_SECURITY.getValue());
                            JSONObject employee = new JSONObject();
                            JSONObject salaryCard = new JSONObject();
                            JSONObject socialSecurityInfo = new JSONObject();
                            List<HrmEmployeeData> employeeDataList = new ArrayList<>();
                            employeeFields.forEach(field -> {
                                Object value = field.getValue();
                                if (field.getType() == FieldTypeEnum.SELECT.getValue() && field.getComponentType() == ComponentType.NO.getValue()) {
                                    List<Map<String, String>> list = JSON.parseObject(field.getOptions(), List.class);
                                    for (Map<String, String> map : list) {
                                        if (map.get("name").equals(value)) {
                                            value = map.get("value");
                                        }
                                    }
                                }
                                if (field.getIsFixed() == IsEnum.YES.getValue()) {
                                    employee.put(field.getFieldName(), value);
                                } else {
                                    HrmEmployeeData hrmEmployeeData = new HrmEmployeeData();
                                    hrmEmployeeData.setFieldId(field.getFieldId());
                                    hrmEmployeeData.setLabelGroup(field.getLabelGroup());
                                    hrmEmployeeData.setName(field.getName());
                                    hrmEmployeeData.setFieldValue((String) value);
                                    hrmEmployeeData.setFieldValueDesc((String) field.getValue());
                                    employeeDataList.add(hrmEmployeeData);
                                }
                            });
                            salaryCardFields.forEach(field -> {
                                salaryCard.put(field.getFieldName(), field.getValue());
                            });
                            socialSecurityFields.forEach(field -> {
                                socialSecurityInfo.put(field.getFieldName(), field.getValue());
                            });
                            HrmEmployee employee1 = employee.toJavaObject(HrmEmployee.class);
                            if (employee1.getIdType() != null && employee1.getIdType() == IdTypeEnum.ID_CARD.
                                    getValue() && StrUtil.isNotEmpty(employee1.getIdNumber())) {
                                String idNumber = employee1.getIdNumber();
                                if (!IdcardUtil.isValidCard(idNumber)) {
                                    throw new CrmException(HrmCodeEnum.IDENTITY_INFORMATION_IS_ILLEGAL, idNumber);
                                }
                                employee1.setDateOfBirth(IdcardUtil.getBirthDate(idNumber));
                                employee1.setAge(IdcardUtil.getAgeByIdCard(idNumber));
                            }
                            Integer employeeId = ApplicationContextHolder.getBean(IHrmEmployeeService.class).lambdaQuery().eq(HrmEmployee::getJobNumber, employee1.getJobNumber())
                                    .eq(HrmEmployee::getMobile, employee1.getMobile())
                                    .eq(HrmEmployee::getIsDel, 0)
                                    .oneOpt().map(HrmEmployee::getEmployeeId).orElse(null);
                            Integer repeatHandling = getUploadExcelBO().getRepeatHandling();
                            if (employeeId != null) {
                                if (repeatHandling == 2) {
                                    return;
                                } else {
                                    employee1.setEmployeeId(employeeId);
                                    updateNum.incrementAndGet();
                                }
                            }
                            if (StrUtil.isNotEmpty(employee1.getMobile())) {
                                boolean mobile = Validator.isMobile(employee1.getMobile());
                                if (!mobile) {
                                    throw new CrmException(HrmCodeEnum.PHONE_NUMBER_ERROR, employee1.getMobile());
                                }
                                LambdaQueryChainWrapper<HrmEmployee> wrapper = ApplicationContextHolder.getBean(IHrmEmployeeService.class).lambdaQuery()
                                        .eq(HrmEmployee::getMobile, employee1.getMobile()).eq(HrmEmployee::getIsDel, 0);
                                Integer count;
                                if (employee1.getEmployeeId() == null) {
                                    count = wrapper.count();
                                } else {
                                    count = wrapper.ne(HrmEmployee::getEmployeeId, employee1.getEmployeeId()).count();
                                }
                                if (count > 0) {
                                    throw new CrmException(HrmCodeEnum.PHONE_NUMBER_ALREADY_EXISTS, employee1.getMobile());
                                }
                            }
                            if (StrUtil.isNotEmpty(employee1.getJobNumber())) {
                                String jobNumber = employee1.getJobNumber();
                                LambdaQueryChainWrapper<HrmEmployee> wrapper = ApplicationContextHolder.getBean(IHrmEmployeeService.class).lambdaQuery().eq(HrmEmployee::getIsDel, 0).eq(HrmEmployee::getJobNumber, jobNumber);
                                Integer count;
                                if (employee1.getEmployeeId() == null) {
                                    count = wrapper.count();
                                } else {
                                    count = wrapper.ne(HrmEmployee::getEmployeeId, employee1.getEmployeeId()).count();
                                }
                                if (count > 0) {
                                    throw new CrmException(HrmCodeEnum.JOB_NUMBER_EXISTED, jobNumber);
                                }
                            }
                            employee1.setEntryStatus(EmployeeEntryStatus.IN.getValue());
                            if (employee1.getEmploymentForms().equals(EmploymentFormsEnum.OFFICIAL.getValue())) {
                                employee1.setStatus(EmployeeStatusEnum.OFFICIAL.getValue());
                            } else {
                                employee1.setStatus(EmployeeStatusEnum.INTERNSHIP.getValue());
                            }
                            ApplicationContextHolder.getBean(IHrmEmployeeService.class).saveOrUpdate(employee1);
                            ApplicationContextHolder.getBean(EmployeeActionRecordServiceImpl.class).addOrDeleteRecord(HrmActionBehaviorEnum.ADD, LabelGroupEnum.PERSONAL, employee1.getEmployeeId());
                            employeeDataList.forEach(data -> {
                                data.setEmployeeId(employee1.getEmployeeId());
                            });
                            ApplicationContextHolder.getBean(IHrmEmployeeDataService.class).lambdaUpdate().eq(HrmEmployeeData::getEmployeeId, employee1.getEmployeeId()).remove();
                            ApplicationContextHolder.getBean(IHrmEmployeeDataService.class).saveBatch(employeeDataList, employeeDataList.size());
                            HrmEmployeeSalaryCard hrmEmployeeSalaryCard = salaryCard.toJavaObject(HrmEmployeeSalaryCard.class);
                            hrmEmployeeSalaryCard.setEmployeeId(employee1.getEmployeeId());
                            ApplicationContextHolder.getBean(IHrmEmployeeSalaryCardService.class).save(hrmEmployeeSalaryCard);
                            HrmEmployeeSocialSecurityInfo socialSecurityInfo1 = socialSecurityInfo.toJavaObject(HrmEmployeeSocialSecurityInfo.class);
                            socialSecurityInfo1.setEmployeeId(employee1.getEmployeeId());
                            ApplicationContextHolder.getBean(IHrmEmployeeSocialSecurityService.class).save(socialSecurityInfo1);
                            //发送通知
                            AdminMessage adminMessage = new AdminMessage();
                            adminMessage.setCreateUser(UserUtil.getUserId());
                            adminMessage.setCreateTime(DateUtil.formatDateTime(new Date()));
                            adminMessage.setRecipientUser(EmployeeCacheUtil.getUserId(employee1.getEmployeeId()));
                            adminMessage.setLabel(8);
                            adminMessage.setType(AdminMessageEnum.HRM_EMPLOYEE_OPEN.getType());
                            ApplicationContextHolder.getBean(AdminMessageService.class).save(adminMessage);
                        } catch (CrmException e) {
                            log.error("导入数据异常:", e);
                            rowList.add(0, e.getMsg());
                            errorList.add(rowList);
                        } catch (JSONException e) {
                            log.error("导入数据异常:", e);
                            rowList.add(0, "数据格式异常,请使用模板规定格式");
                            errorList.add(rowList);
                        } catch (Exception ex) {
                            log.error("导入数据异常:", ex);
                            rowList.add(0, "导入异常");
                            errorList.add(rowList);
                        }
                    }
                } else if (rowIndex == 1) {
                    queryExcelHead(rowList);
                } else {
                    errorList.add(0, rowList);
                }
            });
        }

        @Override
        public UploadExcelBO getUploadExcelBO() {
            return uploadExcelBO;
        }

        @Override
        public void run() {
            boolean exists = redis.exists(AdminCacheKey.UPLOAD_EXCEL_MESSAGE_PREFIX + uploadExcelBO.getMessageId());
            if (!exists) {
                return;
            }
            try {
                UserUtil.setUser(getUploadExcelBO().getUserInfo());
                importExcel();
            } catch (Exception e) {
                log.error("导入出现错误", e);
            } finally {
                redis.del(AdminCacheKey.UPLOAD_EXCEL_MESSAGE_PREFIX + uploadExcelBO.getMessageId());
                UserUtil.removeUser();
            }
            AdminMessage adminMessage = adminService.getMessageById(getUploadExcelBO().getMessageId()).getData();
            adminMessage.setTitle(String.valueOf(num.get() - 2));
            adminMessage.setContent((errorList.size() - 2) + "," + updateNum.get());
            if (errorList.size() > 2) {
                File file = new File(uploadPath + "excel/" + BaseUtil.getDate() + "/" + IdUtil.simpleUUID() + ".xlsx");
                BigExcelWriter writer = ExcelUtil.getBigWriter(file);
                CellStyle textStyle = writer.getWorkbook().createCellStyle();
                DataFormat format = writer.getWorkbook().createDataFormat();
                textStyle.setDataFormat(format.getFormat("@"));
                for (int i = 0; i < errorList.get(1).size(); i++) {
                    writer.setColumnWidth(i, 20);
                    writer.getSheet().setDefaultColumnStyle(i, textStyle);
                }
                writer.merge(errorList.get(1).size() - 1, errorList.remove(0).get(0));
                writer.setDefaultRowHeight(20);
                Cell cell = writer.getCell(0, 0);
                CellStyle cellStyle = cell.getCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                Font font = writer.createFont();
                font.setBold(true);
                font.setFontHeightInPoints((short) 16);
                cellStyle.setFont(font);
                cell.setCellStyle(cellStyle);
                // 一次性写出内容，使用默认样式
                writer.write(errorList);
                // 关闭writer，释放内存
                writer.close();
                adminMessage.setTypeId(null);
                //将错误信息的excel保存七天 604800 七天的秒数
                redis.setex(AdminCacheKey.UPLOAD_EXCEL_MESSAGE_PREFIX + "file:" + adminMessage.getMessageId().toString(), 604800, file.getAbsolutePath());
            }
            adminService.saveOrUpdateMessage(adminMessage);
            FileUtil.del(getUploadExcelBO().getFilePath());
        }
    }

}
