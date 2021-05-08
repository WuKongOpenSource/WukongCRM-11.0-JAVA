package com.kakarote.examine.service;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.ExamineField;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.examine.entity.BO.ExaminePageBO;
import com.kakarote.examine.entity.BO.ExaminePreviewBO;
import com.kakarote.examine.entity.BO.ExamineSaveBO;
import com.kakarote.examine.entity.PO.Examine;
import com.kakarote.examine.entity.VO.*;

import java.util.List;

/**
 * <p>
 * 审批表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-11-13
 */
public interface IExamineService extends BaseService<Examine> {

    /**
     * 查询可供设置的自定义字段列表
     * @param label 字段类型
     * @param categoryId 审批分类，只有OA审批需要
     * @return data
     */
    public List<ExamineField> queryField(Integer label, Integer categoryId);

    /**
     * 查询审批列表
     * @param examinePageBo 分页对象
     * @return data
     */
    public BasePage<ExamineVO> queryList(ExaminePageBO examinePageBo);

    /**
     * 保存审批对象，审批对象不可修改，修改是新增一个再把原来的停用
     * @param examineSaveBO data
     */
    public Examine addExamine(ExamineSaveBO examineSaveBO);

    /**
     * 通过label查询可用审批流
     * @param label 类型
     * @return data
     */
    public Examine queryExamineByLabel(Integer label);

    /**
     * 修改审批状态
     *
     * @param examineId 审批ID
     * @param status    1 正常 2 停用 3 删除
     */
    public Examine updateStatus(Long examineId, Integer status,boolean isRequest);


    /**
     * 预览指定类型的审批流程树
     * @param label 类型
     */
    public List<ExamineFlowVO> previewExamineFlow(Integer label);

    /**
     * 查询指定的审批流程树
     * @param examineId 审批ID
     */
    public List<ExamineFlowVO> queryExamineFlow(Long examineId);

    /**
     * 查询指定的审批流包含的条件字段数据
     * @param label 审批类型
     * @param recordId 审批记录ID
     * @param examineId 审批ID
     */
    public List<ExamineFlowConditionDataVO> previewFiledName(Integer label,Integer recordId,Long examineId);

    /**
     * 预览对应的审批流程
     * @param examinePreviewBO
     */
    public ExaminePreviewVO previewExamineFlow(ExaminePreviewBO examinePreviewBO);

    /**
     * 查询办公审批数据列表
     * @param examinePageBo
     */
    BasePage<com.kakarote.core.feign.oa.entity.ExamineVO> queryOaExamineList(ExaminePageBO examinePageBo);

    /**
     * 查询客户管理审批数据列表
     * @param examinePageBo
     */
    BasePage<ExamineRecordInfoVO> queryCrmExamineList(ExaminePageBO examinePageBo);

    /**
     * 查询办公符合条件的审批数据ID
     * @param status  状态
     * @param categoryId 办公审批流ID
     */
    List<Integer> queryOaExamineIdList(Integer status, Integer categoryId);

    /**
     * 查询客户管理符合条件的审批数据ID
     * @param label 审批类型
     * @param status  状态
     */
    List<Integer> queryCrmExamineIdList(Integer label,Integer status);


    /**
     * 过滤用户列表 - 去除非正常用户，最后找不到人返回审批流管理员
     * @param userIds 用户ID集合
     * @param examineId  审批流ID
     */
    public List<Long> handleUserList(List<Long> userIds,Long examineId);
}
