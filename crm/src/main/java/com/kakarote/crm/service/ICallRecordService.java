package com.kakarote.crm.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.BiParams;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.entity.BO.CallRecordBO;
import com.kakarote.crm.entity.PO.CallRecord;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 通话记录管理
 * @author Ian
 */
public interface ICallRecordService extends BaseService<CallRecord> {
    
    /**
     * 添加通话记录
     * @return
     */
    CallRecord saveRecord(CallRecord callRecord);

    /**
     * 查询通话记录列表
     * @param callRecordBO
     * @return
     */
    BasePage<JSONObject> pageCallRecordList(CallRecordBO callRecordBO);
    /**
     * 上传文件
     * @param file 文件
     * @param id id
     * @param prefix
     * @return
     */
    boolean upload(MultipartFile file, String id, String prefix);
    /**
     * 录音下载
     * @return
     */
    void download(String id, HttpServletResponse response);

    /**
     * 搜索呼入电话 是否存在记录
     * @param search
     * @return
     */
    JSONObject searchPhone(String search);
    /**
     * 查询可呼叫的电话
     * @return
     */
    List<JSONObject> queryPhoneNumber(String model, String modelId);


    /**
     * 通话记录分析
     *  year：本年度；lastYear：上年度；quarter：本季度；lastQuarter：上季度；month：本月；自定义时间：如start_time：2019-04-19；end_time：2019-04-22
     * user_id 	否 	int 用户id
     * @return
     */
    BasePage<JSONObject> analysis(BiParams biParams);
}
