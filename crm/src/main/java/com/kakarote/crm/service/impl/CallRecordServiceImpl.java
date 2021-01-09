package com.kakarote.crm.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.feign.crm.entity.BiParams;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.servlet.upload.FileServiceFactory;
import com.kakarote.core.servlet.upload.UploadEntity;
import com.kakarote.core.servlet.upload.UploadFileEnum;
import com.kakarote.core.utils.BaseUtil;
import com.kakarote.core.utils.BiTimeUtil;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.constant.CrmCodeEnum;
import com.kakarote.crm.entity.BO.CallRecordBO;
import com.kakarote.crm.entity.PO.CallRecord;
import com.kakarote.crm.entity.PO.CrmContacts;
import com.kakarote.crm.entity.PO.CrmCustomer;
import com.kakarote.crm.entity.PO.CrmLeads;
import com.kakarote.crm.mapper.CallRecordMapper;
import com.kakarote.crm.mapper.CrmCustomerMapper;
import com.kakarote.crm.service.ICallRecordService;
import com.kakarote.crm.service.ICrmContactsService;
import com.kakarote.crm.service.ICrmCustomerService;
import com.kakarote.crm.service.ICrmLeadsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 通话记录管理
 * @author Ian
 */
@Service
public class CallRecordServiceImpl extends BaseServiceImpl<CallRecordMapper, CallRecord> implements ICallRecordService {

    @Autowired
    private ICrmCustomerService crmCustomerService;
    @Autowired
    private ICrmContactsService crmContactsService;
    @Autowired
    private ICrmLeadsService crmLeadsService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminFileService adminFileService;

    @Autowired
    private CrmCustomerMapper crmCustomerMapper;


    /**
     * 添加通话记录
     * @return
     */
    @Override
    public CallRecord saveRecord(CallRecord callRecord){
        if(callRecord.getCallRecordId() != null){
            //暂不支持修改
            throw new CrmException(CrmCodeEnum.CRM_CALL_DATA_UPDATE_ERROR);
        }
        String model = null;
        Integer modelId = null;
        CrmCustomer customer = this.getCrmCustomerInfo(callRecord.getNumber(),false);
        if (customer != null){
            model = "customer";
            modelId = customer.getCustomerId();
        }else {
            CrmContacts crmContacts = this.getCrmContactsInfo(callRecord.getNumber(),false);
            if (crmContacts != null){
                model = "contacts";
                modelId = crmContacts.getContactsId();
            }else {
                CrmLeads crmLeads = this.getCrmLeadsInfo(callRecord.getNumber(),false);
                if (crmLeads != null){
                    model = "leads";
                    modelId = crmLeads.getLeadsId();
                }
            }
        }
        callRecord.setModelId(modelId);
        callRecord.setModel(model);
        //通话时长 等于 通话结束时间减去接通时间
        if (callRecord.getEndTime() != null &&  callRecord.getAnswerTime() != null) {
            callRecord.setTalkTime(Long.valueOf(DateUtil.between(callRecord.getEndTime(),callRecord.getAnswerTime(), DateUnit.SECOND)).intValue());
        }
        //摘机时长 等于 接通时间时间减去拨打时间
        if (callRecord.getAnswerTime() != null &&  callRecord.getStartTime() != null) {
            callRecord.setDialTime(Long.valueOf(DateUtil.between(callRecord.getAnswerTime(),callRecord.getStartTime(), DateUnit.SECOND)).intValue());
        }
        if (callRecord.getOwnerUserId() == null){
            callRecord.setOwnerUserId(UserUtil.getUserId());
        }
        CallRecord record = baseMapper.queryRecord(callRecord.getNumber(), callRecord.getStartTime(),callRecord.getOwnerUserId());
        if (record != null){
            return record;
        }
        this.save(callRecord);
        return callRecord;
    }



    /**
     * 获取客户
     * @date 2020/8/27 15:07
     * @param number
     * @param isLike
     * @return com.kakarote.crm.entity.PO.CrmCustomer
     **/
    private CrmCustomer getCrmCustomerInfo(String number, boolean isLike){
        LambdaQueryWrapper<CrmCustomer> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.ne(CrmCustomer::getStatus, 3);
        if (isLike){
            lambdaQueryWrapper.and(wrapper -> wrapper.like(CrmCustomer::getMobile, number).or().like(CrmCustomer::getTelephone, number));
        }else {
            lambdaQueryWrapper.and(wrapper -> wrapper.eq(CrmCustomer::getMobile, number).or().eq(CrmCustomer::getTelephone, number));
        }
        lambdaQueryWrapper.last(" limit 1");
        return crmCustomerService.getOne(lambdaQueryWrapper);
    }

    /**
     * 获取联系人
     * @date 2020/8/27 15:07
     * @param number
     * @param isLike
     * @return com.kakarote.crm.entity.PO.CrmContacts
     **/
    private CrmContacts getCrmContactsInfo(String number, boolean isLike){
        LambdaQueryWrapper<CrmContacts> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (isLike){
            lambdaQueryWrapper.like(CrmContacts::getMobile, number).or().like(CrmContacts::getTelephone, number);
        }else {
            lambdaQueryWrapper.eq(CrmContacts::getMobile, number).or().eq(CrmContacts::getTelephone, number);
        }
        lambdaQueryWrapper.last(" limit 1");
        return crmContactsService.getOne(lambdaQueryWrapper);
    }

    /**
     * 获取线索
     * @date 2020/8/27 15:07
     * @param number
     * @param isLike
     * @return com.kakarote.crm.entity.PO.CrmLeads
     **/
    private CrmLeads getCrmLeadsInfo(String number, boolean isLike){
        LambdaQueryWrapper<CrmLeads> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (isLike){
            lambdaQueryWrapper.like(CrmLeads::getMobile, number).or().like(CrmLeads::getTelephone, number);
        }else {
            lambdaQueryWrapper.eq(CrmLeads::getMobile, number).or().eq(CrmLeads::getTelephone, number);
        }
        lambdaQueryWrapper.last(" limit 1");
        return crmLeadsService.getOne(lambdaQueryWrapper);
    }

    /**
     * 查询通话记录列表
     * @param callRecordBO
     * @return
     */
    @Override
    public BasePage<JSONObject> pageCallRecordList(CallRecordBO callRecordBO){
        BasePage<JSONObject> page = new BasePage<>(callRecordBO.getPage(), callRecordBO.getLimit());
        Long talkTime = callRecordBO.getTalkTime();
        String talkTimeCondition = callRecordBO.getTalkTimeCondition();
        if (talkTime != null && StrUtil.isEmpty(talkTimeCondition)){
            throw new CrmException(CrmCodeEnum.CRM_CALL_DATA_QUERY_ERROR,"刷选条件");
        }
        Integer menuId = adminService.queryMenuId("bi", "call", "index").getData();
        callRecordBO.setMenuId(menuId);
        BiTimeUtil.BiTimeEntity record = BiTimeUtil.analyzeType(callRecordBO);
        List<Long> userIds = record.getUserIds();
        if (userIds.isEmpty()){
            return page;
        }
        return baseMapper.pageCallRecordList(page,record.getSqlDateFormat(),userIds,talkTime,
                talkTimeCondition,record.getBeginTime(),record.getFinalTime());
    }

    /**
     * 上传文件
     * @param file 文件
     * @param id id
     * @param prefix
     * @return
     */
    @Override
    public boolean upload(MultipartFile file, String id, String prefix){
        if (StrUtil.isEmpty(id)) {
            throw new CrmException(CrmCodeEnum.CRM_CALL_DATA_QUERY_ERROR,"请求的数据格式");
        }
        //查询通话记录
        CallRecord record = baseMapper.selectById(id);
        if(record == null){
            throw new CrmException(CrmCodeEnum.CRM_DATA_DELETED,"此通话记录");
        }
        String batchId = StrUtil.isNotEmpty(record.getBatchId()) ? record.getBatchId() : IdUtil.simpleUUID();
        UploadEntity entity;
        if (file != null) {
            entity = new UploadEntity(BaseUtil.getNextId() + "", file.getOriginalFilename(), file.getSize(), batchId,"0");
            try {
                entity = FileServiceFactory.build().uploadFile(file.getInputStream(), entity);
            } catch (IOException e) {
                throw new CrmException(CrmCodeEnum.CRM_CALL_UPLOAD_ERROR);
            }
        }else {
            entity = new UploadEntity();
        }
        CallRecord callRecord = new CallRecord();
        callRecord.setCallRecordId(Integer.valueOf(id));
        callRecord.setFilePath(entity.getPath());
        callRecord.setFileName(entity.getName());
        callRecord.setCallUpload(UploadFileEnum.LOCAL.getConfig().equals(entity.getType()) ? entity.getType() : 0);
        callRecord.setSize(Optional.ofNullable(entity.getSize()).orElse(0L).intValue());
        callRecord.setUpdateTime(DateUtil.date());
        callRecord.setBatchId(batchId);
        return baseMapper.updateById(callRecord) > 0 ;
    }

    /**
     * 录音下载
     * @return
     */
    @Override
    public void download(String id, HttpServletResponse response){
        if (StrUtil.isEmpty(id)) {
            throw new CrmException(CrmCodeEnum.CRM_CALL_DATA_QUERY_ERROR,"请求的数据格式");
        }
        //查询通话记录
        CallRecord callRecord = baseMapper.selectById(id);
        if(callRecord == null){
            throw new CrmException(CrmCodeEnum.CRM_DATA_DELETED,"此通话记录");
        }
        if (StrUtil.isEmpty(callRecord.getFilePath())){
            throw new CrmException(CrmCodeEnum.CRM_CALL_DOWNLOAD_ERROR);
        }
        if (Objects.equals(UploadFileEnum.LOCAL.getConfig(), callRecord.getCallUpload())) {
            ServletUtil.write(response, FileUtil.file(callRecord.getFilePath()));
            return;
        }
        UploadEntity entity = new UploadEntity(callRecord.getCallRecordId() + "", callRecord.getFileName(), callRecord.getSize().longValue(), callRecord.getBatchId(),"0");
        entity.setPath(callRecord.getFilePath());
        InputStream inputStream = FileServiceFactory.build().downFile(entity);
        if (inputStream != null) {
            final String contentType = ObjectUtil.defaultIfNull(FileUtil.getMimeType(callRecord.getFileName()), "application/octet-stream");
            ServletUtil.write(response, inputStream, contentType, callRecord.getFileName());
        }
    }


    /**
     * 搜索呼入电话 是否存在记录
     * @param search
     * @return
     */
    @Override
    public JSONObject searchPhone(String search){
        JSONObject jsonObject = new JSONObject();
        //返回呼入信息信息
        if(StrUtil.isEmpty(search)){
            throw new CrmException(CrmCodeEnum.CRM_CALL_DATA_QUERY_ERROR,"条件");
        }
        //排查客户中是否存在当前呼入电话记录
        CrmCustomer crmCustomer = this.getCrmCustomerInfo(search,true);
        if(crmCustomer != null){
            jsonObject.put("model","customer");
            jsonObject.put("customer_id",crmCustomer.getCustomerId());
            jsonObject.put("name",crmCustomer.getCustomerName());
            jsonObject.put("owner_user_id_info", UserCacheUtil.getUserName(crmCustomer.getOwnerUserId()));
            return jsonObject;
        }

        //排查线索中是否存在当前呼入电话记录
        CrmLeads leadsRecord = this.getCrmLeadsInfo(search,true);
        if(leadsRecord != null){
            jsonObject.put("model","leads");
            jsonObject.put("leads_id",leadsRecord.getLeadsId());
            jsonObject.put("name",leadsRecord.getLeadsName());
            jsonObject.put("owner_user_id_info", UserCacheUtil.getUserName(leadsRecord.getOwnerUserId()));
            return jsonObject;
        }

        //排查联系人中是否有呼入电话记录
        CrmContacts contactsRecord = this.getCrmContactsInfo(search,true);
        if(contactsRecord!=null){
            jsonObject.put("model","contacts");
            jsonObject.put("contacts_id",contactsRecord.getContactsId());
            jsonObject.put("name",contactsRecord.getName());
            jsonObject.put("owner_user_id_info", UserCacheUtil.getUserName(contactsRecord.getOwnerUserId()));
            return jsonObject;
        }

        return jsonObject;
    }

    /**
     * 查询可呼叫的电话
     * @return
     */
    @Override
    public List<JSONObject> queryPhoneNumber(String model, String modelId){
        if(StrUtil.isEmpty(model) || StrUtil.isEmpty(modelId)){
            throw new CrmException(CrmCodeEnum.CRM_CALL_DATA_QUERY_ERROR,"条件");
        }
        List<JSONObject> recordList=new ArrayList<>();
        //当前选项为客户时 查询客户负责的所有联系人
        switch (model) {
            case "customer":
                CrmModel crmModel = Optional.ofNullable(crmCustomerMapper.queryById(Integer.valueOf(modelId), UserUtil.getUserId())).orElse(new CrmModel());
                if (crmModel.get("mobile") != null && !"".equals(crmModel.get("mobile"))) {
                    JSONObject data=new JSONObject();
                    data.put("name", "客户-" + crmModel.get("customerName"));
                    data.put("phoneNumber", crmModel.get("mobile"));
                    data.put("model", "customer");
                    data.put("model_id", modelId);
                    recordList.add(data);
                }
                if (crmModel.get("telephone") != null && !"".equals(crmModel.get("telephone"))) {
                    JSONObject data=new JSONObject();
                    data.put("name","客户-" + crmModel.get("customerName"));
                    data.put("phoneNumber", crmModel.get("telephone"));
                    data.put("model", "customer");
                    data.put("model_id", modelId);
                    recordList.add(data);
                }
                //查询客户 负责的联系人
                List<JSONObject> contactsList = baseMapper.queryContactsByCustomerId(Integer.valueOf(modelId));
                if (!contactsList.isEmpty()) {
                    for (JSONObject record : contactsList) {
                        if (record.get("mobile") != null && !"".equals(record.get("mobile"))) {
                            JSONObject data=new JSONObject();
                            data.put("name", "联系人-" + record.get("name"));
                            data.put("phoneNumber", record.get("mobile"));
                            data.put("model", "customer");
                            data.put("model_id", modelId);
                            recordList.add(data);
                        }
                        if (record.get("telephone") != null && !"".equals(record.get("telephone"))) {
                            JSONObject data=new JSONObject();
                            data.put("name", "联系人-" + record.get("name"));
                            data.put("phoneNumber", record.get("telephone"));
                            data.put("model", "customer");
                            data.put("model_id", modelId);
                            recordList.add(data);
                        }
                    }
                }
                break;
            case "leads": {
                //线索
                CrmLeads crmLeads = Optional.ofNullable(crmLeadsService.getById(modelId)).orElse(new CrmLeads());
                if (StrUtil.isNotEmpty(crmLeads.getMobile())) {
                    JSONObject data=new JSONObject();
                    data.put("name", "线索-" + crmLeads.getLeadsName());
                    data.put("phoneNumber", crmLeads.getMobile());
                    data.put("model", "leads");
                    data.put("model_id", modelId);
                    recordList.add(data);
                }
                if (StrUtil.isNotEmpty(crmLeads.getTelephone())) {
                    JSONObject data = new JSONObject();
                    data.put("name", "线索-" + crmLeads.getLeadsName());
                    data.put("phoneNumber", crmLeads.getTelephone());
                    data.put("model", "leads");
                    data.put("model_id", modelId);
                    recordList.add(data);
                }
                //获取线索自定义的手机号码 字段
                List<JSONObject> fieldList = baseMapper.searchFieldValueByLeadsId(Integer.valueOf(modelId));
                if (!fieldList.isEmpty()) {
                    for (JSONObject record : fieldList) {
                        JSONObject data = new JSONObject();
                        data.put("name", "线索-" + record.get("leadsName"));
                        data.put("phoneNumber", record.get("value"));
                        data.put("model", "leads");
                        data.put("model_id", modelId);
                        recordList.add(data);
                    }
                }
                break;
            }
            case "contacts": {
                //联系人
                CrmContacts crmContacts = Optional.ofNullable(crmContactsService.getById(modelId)).orElse(new CrmContacts());
                if (StrUtil.isNotEmpty(crmContacts.getMobile())) {
                    JSONObject data = new JSONObject();
                    data.put("name", "联系人-" + crmContacts.getName());
                    data.put("phoneNumber", crmContacts.getMobile());
                    data.put("model", "contacts");
                    data.put("model_id", modelId);
                    recordList.add(data);
                }
                if (StrUtil.isNotEmpty(crmContacts.getTelephone())) {
                    JSONObject data = new JSONObject();
                    data.put("name", "联系人-" + crmContacts.getName());
                    data.put("phoneNumber", crmContacts.getTelephone());
                    data.put("model", "contacts");
                    data.put("model_id", modelId);
                    recordList.add(data);
                }

                //获取联系人自定义的手机号码字段
                List<JSONObject> fieldList = baseMapper.searchFieldValueByContactsId(Integer.valueOf(modelId));
                if (!fieldList.isEmpty()) {
                    for (JSONObject record : fieldList) {
                        JSONObject data = new JSONObject();
                        data.put("name", "联系人-" + record.get("name"));
                        data.put("phoneNumber", record.get("value"));
                        data.put("model", "contacts");
                        data.put("model_id", modelId);
                        recordList.add(data);
                    }
                }
                break;
            }
            default:
                break;
        }
        return recordList;
    }


    /**
     * 通话记录分析
     *  year：本年度；lastYear：上年度；quarter：本季度；lastQuarter：上季度；month：本月；自定义时间：如start_time：2019-04-19；end_time：2019-04-22
     * user_id 	否 	int 用户id
     * @return
     */
    @Override
    public BasePage<JSONObject> analysis(BiParams biParams){
        BasePage<JSONObject> page = new BasePage<>(biParams.getPage(), biParams.getLimit());
        Integer menuId = adminService.queryMenuId("bi", "call", "analysis").getData();
        biParams.setMenuId(menuId);
        BiTimeUtil.BiTimeEntity record = BiTimeUtil.analyzeType(biParams);
        List<Long> userIds = record.getUserIds();
        if (userIds.isEmpty()){
            return page;
        }
        BasePage<JSONObject> recordPage = baseMapper.analysis(page,userIds,record.getSqlDateFormat(),record.getBeginTime(),record.getFinalTime());
        recordPage.getList().forEach(recordObj -> {
            recordObj.put("user_info",adminService.getUserInfo(recordObj.getLong("ownerUserId")).getData());
        });
        return recordPage;
    }
}
