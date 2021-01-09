package com.kakarote.crm.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.feign.crm.entity.ExamineField;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.constant.FieldEnum;
import com.kakarote.crm.entity.BO.*;
import com.kakarote.crm.entity.PO.CrmField;
import com.kakarote.crm.entity.VO.CrmFieldSortVO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;

import java.util.List;

/**
 * <p>
 * 自定义字段表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-19
 */
public interface ICrmFieldService extends BaseService<CrmField> {

    /**
     * 查询自定义字段列表
     * @return data
     */
    public List<CrmFieldsBO> queryFields();

    /**
     * 保存自定义字段列表
     *
     * @param crmFieldBO data
     */
    public void saveField(CrmFieldBO crmFieldBO);

    /**
     * 保存自定义字段列表
     * @param label label
     * @param isQueryHide 是否查询隐藏字段
     * @return data
     */
    public List<CrmField> list(Integer label, boolean isQueryHide);

    /**
     * 查询模块字段列表
     *
     * @param label label
     * @return data
     */
    public List<CrmFieldSortVO> queryListHead(Integer label);

    /**
     * 修改字段宽度
     * @param fieldStyle data
     */
    public void setFieldStyle(CrmFieldStyleBO fieldStyle);

    /**
     * 修改字段配置
     * @param fieldSort data
     */
    public void setFieldConfig(CrmFieldSortBO fieldSort);

    /**
     * 查询字段配置
     * @param label 类型
     */
    public JSONObject queryFieldConfig(Integer label);

    /**
     * 查询字段配置
     * @param crmModel data
     * @return data
     */
    public List<CrmModelFiledVO> queryField(CrmModel crmModel);

    /**
     * 查询字段配置
     * @param type 类型
     * @return data
     */
//  TODO  @Cached(name = CrmConst.FIELD_SORT_CACHE_NAME)  暂时先注释(后台修改自定义字段这边没有同步更新,导致es保存映射出错)
    public List<CrmModelFiledVO> queryField(Integer type);

    /**
     * 验证数据是否已经存在
     * @param verifyBO verify
     * @return data
     */
    public CrmFieldVerifyBO verify(CrmFieldVerifyBO verifyBO);

    /**
     * 查询类型是文件的自定义字段
     * @return data
     */
    public List<CrmField> queryFileField();

    /**
     * 初始化数据
     * @param type type
     * @return data
     */
    public Integer initData(Integer type);

    /**
     * 格式化数据
     * @param record data
     * @param typeEnum type
     */
    public void recordToFormType(CrmModelFiledVO record, FieldEnum typeEnum);

    /**
     * 查询客户自定义字段是否重复
     * @param name
     * @param value
     * @return
     */
    public long queryCustomerFieldDuplicateByNoFixed(String name, Object value);

    /**
     * 查询客户固定字段是否重复
     * @param name
     * @param value
     * @return
     */
    Integer queryCustomerFieldDuplicateByFixed(String name, Object value);

    void setPoolFieldStyle(CrmFieldStyleBO fieldStyle);

    void changeEsIndex(List<Integer> labels);

    public List<ExamineField> queryExamineField(Integer label);

}
