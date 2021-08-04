package com.kakarote.hrm.service;

import com.kakarote.core.common.FieldEnum;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.constant.LabelGroupEnum;
import com.kakarote.hrm.entity.BO.*;
import com.kakarote.hrm.entity.PO.HrmEmployeeContactsData;
import com.kakarote.hrm.entity.PO.HrmEmployeeData;
import com.kakarote.hrm.entity.PO.HrmEmployeeField;
import com.kakarote.hrm.entity.VO.EmployeeArchivesFieldVO;
import com.kakarote.hrm.entity.VO.EmployeeHeadFieldVO;
import com.kakarote.hrm.entity.VO.FiledListVO;
import com.kakarote.hrm.entity.VO.HrmModelFiledVO;

import java.util.List;

/**
 * <p>
 * 自定义字段表 服务类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
public interface IHrmEmployeeFieldService extends BaseService<HrmEmployeeField> {

    /**
     *根据 label_group 查询员工字段
     * @return
     */
    List<HrmEmployeeField> queryInformationFieldByLabelGroup(LabelGroupEnum labelGroup);

    /**
     * 查询表头信息
     * @return
     */
    List<EmployeeHeadFieldVO> queryListHeads();

    /**
     * 批量修改字段表头配置
     * @param updateFieldConfigBOList
     */
    void updateFieldConfig(List<UpdateFieldConfigBO> updateFieldConfigBOList);


    /**
     * 查询后台配置自定义字段列表
     * @return
     */
    List<FiledListVO> queryFields();

    /**
     * 查询后台配置自定义字段列表
     * @param label
     * @return
     */
    List<List<HrmEmployeeField>> queryFieldByLabel(Integer label);

    /**
     * 保存后台自定义字段
     * @param addEmployeeFieldBO
     */
    void saveField(AddEmployeeFieldBO addEmployeeFieldBO);

    /**
     * 保存员工自定义字段
     * @param fieldList
     * @param labelGroupEnum
     * @param employeeId
     */
    void saveEmployeeField(List<HrmEmployeeData> fieldList, LabelGroupEnum labelGroupEnum, Integer employeeId);

    /**
     * 保存联系人自定义字段
     * @param fieldList
     * @param contactPerson
     * @param contactsId
     */
    void saveEmployeeContactsField(List<HrmEmployeeContactsData> fieldList, LabelGroupEnum contactPerson, Integer contactsId);

    /**
     * 验证字段唯一
     * @param verifyUniqueBO
     */
    VerifyUniqueBO verifyUnique(VerifyUniqueBO verifyUniqueBO);

    /**
     * 修改字段宽度
     * @param updateFieldWidthBO
     */
    void updateFieldWidth(UpdateFieldWidthBO updateFieldWidthBO);

    /**
     * 查询员工档案设置字段列表
     * @return
     */
    List<EmployeeArchivesFieldVO> queryEmployeeArchivesField();

    /**
     * 修改员工档案字段
     * @param archivesFields
     */
    void setEmployeeArchivesField(List<EmployeeArchivesFieldVO> archivesFields);

    /**
     * 发送填写档案信息
     * @param writeArchivesBO
     */
    void sendWriteArchives(SendWriteArchivesBO writeArchivesBO);

    /**
     * 根据员工状态查询字段
     * @param entryStatus
     * @return
     */
    List<HrmModelFiledVO> queryField(Integer entryStatus);
    /**
     * 格式化数据
     * @param record data
     * @param typeEnum type
     */
    public void recordToFormType(HrmModelFiledVO record, FieldEnum typeEnum);
    /**
     * @param label         标签
     * @param fieldType 字段类型
     * @param existNameList 已存在的标签
     * @return fieldName
     */
    public String getNextFieldName(Integer label, Integer fieldType, List<String> existNameList, Integer depth);

    /**
     * 将符合条件的字段值转换成str
     * */
    String convertObjectValueToString(Integer type,Object value,String defaultValue);

    /**
     * 判断自定义字段类型是否符合
     * */
    boolean equalsByType(Object type);

    /**
     * 判断自定义字段类型是否符合
     * */
    boolean equalsByType(Object type, FieldEnum... fieldEnums);
    /**
     * 转换数据库值得格式供前端使用
     * */
    Object convertValueByFormType(Object value, FieldEnum typeEnum);
}
