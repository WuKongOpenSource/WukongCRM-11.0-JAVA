package com.kakarote.hrm.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.FieldEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.constant.LabelGroupEnum;
import com.kakarote.hrm.entity.BO.*;
import com.kakarote.hrm.entity.PO.*;
import com.kakarote.hrm.entity.VO.*;

import java.util.*;

/**
 * <p>
 * 员工表 服务类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
public interface IHrmEmployeeService extends BaseService<HrmEmployee> {

    /**
     * 新建员工
     * @param employeeVO
     */
    void add(AddEmployeeBO employeeVO);

    /**
     * 查询所用员工(表单选择使用)
     * @return
     */
    List<SimpleHrmEmployeeVO> queryAllEmployeeList();

    /**
     * 转换员工信息
     * @param employee
     * @return
     */
    SimpleHrmEmployeeVO transferSimpleEmp(HrmEmployee employee);

    /**
     * 个人基本信息
     * @param employeeId
     * @return
     */
    PersonalInformationVO personalInformation(Integer employeeId);

    /**
     * 个人档案信息
     * @return
     */
    PersonalInformationVO personalArchives();

    /**
     * 查询员工详情
     * @param employeeId
     * @return
     */
    HrmEmployee queryById(Integer employeeId);

    /**
     * 转换基本信息
     */
    List<InformationFieldVO> transferInformation(JSONObject model, LabelGroupEnum labelGroupEnum, List<HrmEmployeeData> fieldValueList);

    /**
     * 转换基本信息
     */
    List<InformationFieldVO> transferInformation(JSONObject model, List<HrmEmployeeField> employeeFields, List<HrmEmployeeData> fieldValueList);

    /**
     * 修改基本信息
     * @param updateInformationBO
     */
    void updateInformation(UpdateInformationBO updateInformationBO);

    /**
     * 修改通讯信息
     * @param updateInformationBO
     */
    void updateCommunication(UpdateInformationBO updateInformationBO);

    /**
     * 添加活修改教育经历
     * @param educationExperience
     */
    void addOrUpdateEduExperience(HrmEmployeeEducationExperience educationExperience);

    /**
     * 删除教育经历
     * @param educationId
     */
    void deleteEduExperience(Integer educationId);

    /**
     * 添加修改工作经历
     * @param workExperience
     */
    void addOrUpdateWorkExperience(HrmEmployeeWorkExperience workExperience);

    /**
     * 删除工作经历
     * @param workExpId
     */
    void deleteWorkExperience(Integer workExpId);

    /**
     * 添加或修改证书
     * @param certificate
     */
    void addOrUpdateCertificate(HrmEmployeeCertificate certificate);

    /**
     * 删除证书
     * @param certificateId
     */
    void deleteCertificate(Integer certificateId);

    /**
     * 添加修改培训经历
     * @param trainingExperience
     */
    void addOrUpdateTrainingExperience(HrmEmployeeTrainingExperience trainingExperience);

    /**
     * 删除培训经历
     * @param trainingId
     */
    void deleteTrainingExperience(Integer trainingId);

    /**
     * 查询联系人添加字段
     * @return
     */
    List<HrmEmployeeField> queryContactsAddField();

    /**
     * 添加修改联系人
     * @param updateInformationBO
     */
    void addOrUpdateContacts(UpdateInformationBO updateInformationBO);

    /**
     * 删除联系人
     * @param contractsId
     */
    void deleteContacts(Integer contractsId);

    /**
     * 删除员工
     * @param employeeIds
     */
    void deleteByIds(List<Integer> employeeIds);

    /**
     * 转正
     * @param hrmEmployeeChangeRecord
     */
    void change(HrmEmployeeChangeRecord hrmEmployeeChangeRecord);

    /**
     * 修改社保方案
     * @param updateInsuranceSchemeBO
     */
    void updateInsuranceScheme(UpdateInsuranceSchemeBO updateInsuranceSchemeBO);

    /**
     * 分页查询员工列表
     * @param employeePageListBO
     * @return
     */
    BasePage<Map<String,Object>> queryPageList(QueryEmployeePageListBO employeePageListBO);

    /**
     * 查询员工列表
     * @param employeeIds
     * @return
     */
    List<SimpleHrmEmployeeVO> querySimpleEmployeeList(Collection<Integer> employeeIds);

    /**
     * 查询员工数量统计
     * @return
     */
    Map<Integer,Long> queryEmployeeStatusNum();


    /**
     * 在入职
     * @param employeeBO
     */
    void againOnboarding(AddEmployeeFieldManageBO employeeBO);

    /**
     * 通过手机号查询员工信息
     * @param mobile
     * @return
     */
    EmployeeInfo queryEmployeeInfoByMobile(String mobile);

    /**
     * 确认入职
     * @param employeeBO
     */
    void confirmEntry(AddEmployeeFieldManageBO employeeBO);

    /**
     * 获取导入模板字段列表
     * @return
     */
    List<HrmEmployeeField> downloadExcelFiled();


    /**
     * 导出员工列表
     * @param employeePageListBO
     * @return
     */
    List<Map<String, Object>> export(QueryEmployeePageListBO employeePageListBO);

    /**
     * 字段唯一验证
     * @param uniqueList
     * @return
     */
    Integer queryFieldValueNoDelete(List<HrmEmployeeField> uniqueList);

    /**
     * 根据月份查询待入职员工id
     * @param year
     * @param month
     * @return
     */
    List<Integer> queryToInByMonth(int year, int month);

    /**
     * 根绝月份查询待离职员工id
     * @param year
     * @param month
     * @return
     */
    List<Integer> queryToLeaveByMonth(int year, int month);

    /**
     * 查询待转正数量
     * @return
     */
    List<Integer> queryToCorrectCount();


    /**
     * 查询过生日员工
     * @param time
     * @param employeeIds
     * @return
     */
    List<HrmEmployee> queryBirthdayListByTime(Date time, Collection<Integer> employeeIds);

    /**
     * 查询入职员工
     * @param time
     * @param employeeIds
     * @return
     */
    List<HrmEmployee> queryEntryEmpListByTime(Date time, Collection<Integer> employeeIds);

    /**
     * 查询转正员工
     * @param time
     * @param employeeIds
     * @return
     */
    List<HrmEmployee> queryBecomeEmpListByTime(Date time, Collection<Integer> employeeIds);

    /**
     * 查询离职员工
     * @param time
     * @param employeeIds
     * @return
     */
    List<HrmEmployee> queryLeaveEmpListByTime(Date time, Collection<Integer> employeeIds);


    /**
     * 查询生日员工
     * @return
     */
    List<Integer> queryBirthdayEmp();

    /**
     * 查询在职员工
     * @return
     */
    List<SimpleHrmEmployeeVO> queryInEmployeeList();


    /**
     * 查询部门员工列表
     * @param deptId
     * @return
     */
    DeptEmployeeListVO queryDeptEmployeeList(Integer deptId);


    /**
     * 从系统用户添加员工
     * @param employeeBOS
     */
    void adminAddEmployee(List<AddEmployeeBO> employeeBOS);

    /**
     * 查询部门用户列表(hrm添加员工使用)
     * @param deptUserListByUserBO
     * @return
     */
    Set<SimpleHrmEmployeeVO> queryDeptUserListByUser(DeptUserListByUserBO deptUserListByUserBO);

    /**
     * 过滤删除员工
     * @param employeeIds
     * @return
     */
    Set<Integer> filterDeleteEmployeeIds(Set<Integer> employeeIds);

    List<SimpleHrmEmployeeVO> queryAllSimpleEmployeeList(Collection<Integer> employeeIds);

    /**
     * 查询下级员工
     * @param employeeIds
     * @return
     */
    Set<Integer> queryChildEmployeeId(List<Integer> employeeIds);

    /**
     * 查询员工入职时间
     * @param queryNotesStatusBO
     * @param employeeIds
     * @return
     */
    Set<String> queryEntryStatusList(QueryNotesStatusBO queryNotesStatusBO, Collection<Integer> employeeIds);

    /**
     * 查询员工转正时间
     * @param queryNotesStatusBO
     * @param employeeIds
     * @return
     */
    Set<String> queryBecomeStatusList(QueryNotesStatusBO queryNotesStatusBO, Collection<Integer> employeeIds);

    /**
     * 查询员工离职时间
     * @param queryNotesStatusBO
     * @param employeeIds
     * @return
     */
    Set<String> queryLeaveStatusList(QueryNotesStatusBO queryNotesStatusBO, Collection<Integer> employeeIds);

    /**
     * 查询新增所需要字段
     * @param entryStatus
     * @return
     */
    List<HrmModelFiledVO> queryEmployeeField(Integer entryStatus);

    /**
     * 保存自定义员工字段
     * @param addEmployeeFieldManageBO
     */
    void addEmployeeField(AddEmployeeFieldManageBO addEmployeeFieldManageBO);

    /**
     * 格式化数据
     * @param record data
     * @param typeEnum type
     */
    public void recordToFormType(InformationFieldVO record, FieldEnum typeEnum);

    /**
     * 查询员工信息
     * @param employeeIds
     * @return
     */
    SimpleHrmEmployeeVO querySimpleEmployee(Integer employeeIds);
}
