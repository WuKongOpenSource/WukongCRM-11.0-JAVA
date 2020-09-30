package com.kakarote.crm.service;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminConfig;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.entity.BO.CrmBusinessSaveBO;
import com.kakarote.crm.entity.BO.CrmSearchBO;
import com.kakarote.crm.entity.BO.CrmUpdateInformationBO;
import com.kakarote.crm.entity.PO.CrmReturnVisit;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-06
 */
public interface ICrmReturnVisitService extends BaseService<CrmReturnVisit> {

    BasePage<Map<String, Object>> queryPageList(CrmSearchBO search);

    void addOrUpdate(CrmBusinessSaveBO crmModel);

    CrmModel queryById(Integer visitId);

    List<CrmModelFiledVO> queryField(Integer id);

    List<CrmModelFiledVO> information(Integer visitId);

    List<FileEntity> queryFileList(Integer id);

    void deleteByIds(List<Integer> ids);

    /**
     * 修改回访提醒设置
     * @param status 状态
     * @param value 值
     */
    public void updateReturnVisitRemindConfig(Integer status,Integer value);

    /**
     * 查询回访提醒设置
     * @return data
     */
    public AdminConfig queryReturnVisitRemindConfig();

    public void updateInformation(CrmUpdateInformationBO updateInformationBO);
}
