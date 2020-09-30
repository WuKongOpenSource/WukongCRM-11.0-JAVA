package com.kakarote.crm.service;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.entity.BO.CrmModelSaveBO;
import com.kakarote.crm.entity.BO.CrmProductStatusBO;
import com.kakarote.crm.entity.BO.CrmSearchBO;
import com.kakarote.crm.entity.BO.CrmUpdateInformationBO;
import com.kakarote.crm.entity.PO.CrmProduct;
import com.kakarote.crm.entity.VO.CrmInfoNumVO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产品表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-26
 */
public interface ICrmProductService extends BaseService<CrmProduct> {
    /**
     * 查询字段配置
     *
     * @param id 主键ID
     * @return data
     */
    public List<CrmModelFiledVO> queryField(Integer id);

    /**
     * 分页查询
     *
     * @param search 搜索添加
     * @return data
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
    public void addOrUpdate(CrmModelSaveBO crmModel,boolean isExcel);

    /**
     * 删除数据
     *
     * @param ids ids
     */
    public void deleteByIds(List<Integer> ids);

    /**
     * 修改负责人
     *
     * @param ids       id列表
     * @param newOwnerUserId 新负责人ID
     */
    public void changeOwnerUser(List<Integer> ids, Long newOwnerUserId);


    /**
     * 下载导入模板
     *
     * @param response 线索id
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
     * 修改产品状态
     * @param productStatus status
     */
    public void updateStatus(CrmProductStatusBO productStatus);

    /**
     * 查询详情页基本信息
     * @param productId id
     * @return data
     */
    public List<CrmModelFiledVO> information(Integer productId);

    /**
     * 查询文件数量
     *
     * @param productId id
     * @return data
     */
    public CrmInfoNumVO num(Integer productId);

    /**
     * 查询文件列表
     * @param productId id
     * @return file
     */
    public List<FileEntity> queryFileList(Integer productId);

    /**
     * 查询产品对象
     * @return list
     */
    public List<SimpleCrmEntity> querySimpleEntity();

    void updateInformation(CrmUpdateInformationBO updateInformationBO);

}
