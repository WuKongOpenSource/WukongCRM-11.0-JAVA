package com.kakarote.crm.service;

import com.kakarote.core.feign.admin.entity.AdminConfig;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.crm.entity.PO.CrmNumberSetting;
import com.kakarote.crm.entity.VO.CrmNumberSettingVO;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 系统自动生成编号设置表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-28
 */
public interface ICrmNumberSettingService extends BaseService<CrmNumberSetting> {
    /**
     * 通过pid查询规则设置
     * @param pid pid
     * @return data
     */
    public List<CrmNumberSetting> queryListByPid(Integer pid);

    /**
     * 通过pid删除规则设置
     * @param pid pid
     */
    public void deleteByPid(Integer pid);

    /**
     * 生成编号
     * @param config 配置
     * @param date 时间
     * @return num
     */
    public String generateNumber(AdminConfig config, Date date);

    /**
     * 查询自动编号设置
     * @return data
     */
    public List<CrmNumberSettingVO> queryNumberSetting();

    /**
     * 查询发票自动编号设置
     * @return data
     */
    public AdminConfig queryInvoiceNumberSetting();


    /**
     * 保存自动编号设置
     * @param numberSettingList 设置参数
     */
    public void setNumberSetting(List<CrmNumberSettingVO> numberSettingList);
}
