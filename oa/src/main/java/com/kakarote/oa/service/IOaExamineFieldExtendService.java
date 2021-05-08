package com.kakarote.oa.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.oa.entity.PO.OaExamineFieldExtend;

import java.util.List;

/**
 * <p>
 * 自定义字段表 服务类
 * </p>
 *
 * @author JiaS
 * @since 2021-03-04
 */
public interface IOaExamineFieldExtendService extends BaseService<OaExamineFieldExtend> {

    /**
     * 查询自定义字段扩展表
     * */
    List<OaExamineFieldExtend> queryOaExamineFieldExtend(Integer parentFieldId);


    /**
     * 保存或修改自定义字段扩展表
     * */
    boolean saveOrUpdateOaExamineFieldExtend(List<OaExamineFieldExtend> oaExamineFieldExtendList, Integer parentFieldId,boolean isUpdate);


    /**
     * 删除或添加自定义字段扩展表
     * */
    boolean deleteOaExamineFieldExtend(Integer parentFieldId);
}
