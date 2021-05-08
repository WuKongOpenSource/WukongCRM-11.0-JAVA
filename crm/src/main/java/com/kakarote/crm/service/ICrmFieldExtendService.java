package com.kakarote.crm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.entity.PO.CrmFieldExtend;

import java.util.List;


/**
 * <p>
 * 自定义字段表 服务类
 * </p>
 *
 * @author JiaS
 * @since 2021-03-04
 */
public interface ICrmFieldExtendService extends BaseService<CrmFieldExtend> {

    /**
     * 查询自定义字段扩展表
     * */
    List<CrmFieldExtend> queryCrmFieldExtend(Integer parentFieldId);


    /**
     * 保存或修改自定义字段扩展表
     * */
    boolean saveOrUpdateCrmFieldExtend(List<CrmFieldExtend> crmFieldExtendList, Integer parentFieldId,boolean isUpdate);


    /**
     * 删除或添加自定义字段扩展表
     * */
    boolean deleteCrmFieldExtend(Integer parentFieldId);




}
