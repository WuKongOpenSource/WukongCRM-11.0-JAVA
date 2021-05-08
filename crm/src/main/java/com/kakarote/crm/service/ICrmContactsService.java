package com.kakarote.crm.service;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.entity.BO.*;
import com.kakarote.crm.entity.PO.CrmContacts;
import com.kakarote.crm.entity.VO.CrmInfoNumVO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 联系人表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
public interface ICrmContactsService extends BaseService<CrmContacts> {
    /**
     * 查询字段配置
     *
     * @param id 主键ID
     * @return data
     */
    public List<CrmModelFiledVO> queryField(Integer id);
    public List<List<CrmModelFiledVO>> queryFormPositionField(Integer id);
    /**
     * 查询所有数据
     *
     * @param search
     * @param isExcel true:查询全部数据  false:查询分页数据
     * @return
     */
    public BasePage<Map<String, Object>> queryPageList(@RequestBody CrmSearchBO search);

    /**
     * 查询字段配置
     *
     * @param id 主键ID
     * @return data
     */
    public CrmModel queryById(Integer id);

    /**
     * 保存或新增信息
     *
     * @param crmModel model
     */
    public void addOrUpdate(CrmContactsSaveBO crmModel,boolean isExcel);

    /**
     * 删除联系人数据
     *
     * @param ids ids
     */
    public void deleteByIds(List<Integer> ids);

    /**
     * 修改联系人负责人
     *
     * @param crmChangeOwnerUserBO  负责人变更BO
     */
    public void changeOwnerUser(CrmChangeOwnerUserBO crmChangeOwnerUserBO);


    /**
     * 下载导入模板
     *
     * @param response 联系人id
     * @throws IOException exception
     */
    public void downloadExcel(HttpServletResponse response) throws IOException;

    /**
     * 全部导出
     *
     * @param response resp
     * @param search   搜索对象
     */
    public void exportExcel(HttpServletResponse response, CrmSearchBO search);

    /**
     * 标星
     * @param contactsId 联系人id
     */
    public void star(Integer contactsId);

    /**
     * 查询详情信息
     * @param contactsId 联系人id
     * @return data
     */
    public List<CrmModelFiledVO> information(Integer contactsId);

    /**
     * 查询文件数量
     *
     * @param contactsId id
     * @return data
     */
    public CrmInfoNumVO num(Integer contactsId);

    /**
     * 查询文件列表
     * @param contactsId id
     * @return file
     */
    public List<FileEntity> queryFileList(Integer contactsId);

    public List<SimpleCrmEntity> querySimpleEntity(List<Integer> ids);

    String getContactsName(int contactsId);

    void updateInformation(CrmUpdateInformationBO updateInformationBO);

    BasePage<Map<String,Object>> queryBusiness(CrmBusinessPageBO businessPageBO);

    void relateBusiness(CrmRelateBusinessBO relateBusinessBO);

    void unrelateBusiness(CrmRelateBusinessBO relateBusinessBO);
}
