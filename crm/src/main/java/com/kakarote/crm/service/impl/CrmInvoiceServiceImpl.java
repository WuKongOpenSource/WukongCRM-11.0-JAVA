package com.kakarote.crm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.AdminConfig;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.common.ActionRecordUtil;
import com.kakarote.crm.common.AuthUtil;
import com.kakarote.crm.common.ParamsUtil;
import com.kakarote.crm.constant.CrmCodeEnum;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.CrmExamineData;
import com.kakarote.crm.entity.BO.CrmSearchBO;
import com.kakarote.crm.entity.PO.CrmExamineLog;
import com.kakarote.crm.entity.PO.CrmInvoice;
import com.kakarote.crm.entity.PO.CrmInvoiceInfo;
import com.kakarote.crm.mapper.CrmInvoiceMapper;
import com.kakarote.crm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-07-06
 */
@Service
public class CrmInvoiceServiceImpl extends BaseServiceImpl<CrmInvoiceMapper, CrmInvoice> implements ICrmInvoiceService {
    @Autowired
    private AdminService adminService;
    @Autowired
    private ICrmNumberSettingService crmNumberSettingService;
    @Autowired
    private ICrmExamineService crmExamineService;
    @Autowired
    private ICrmExamineRecordService crmExamineRecordService;
    @Autowired
    private ICrmExamineLogService crmExamineLogService;

    @Autowired
    private ICrmInvoiceInfoService crmInvoiceInfoService;

    @Autowired
    private ActionRecordUtil actionRecordUtil;

    @Override
    public BasePage<Map<String, Object>> queryPageList(CrmSearchBO search) {
        StringBuilder conditions = new StringBuilder("  1=1 ");
        Integer sceneId = search.getSceneId();
        if (sceneId != null) {
            if (sceneId == 1) {
                conditions.append(" and a.owner_user_id = ").append(UserUtil.getUserId());
            } else if (sceneId == 2) {
                List<Long> userIdList = adminService.queryChildUserId(UserUtil.getUserId()).getData();
                if (userIdList.size() > 0) {
                    conditions.append(" and a.owner_user_id in (").append(CollectionUtil.join(userIdList, ",")).append(")");
                } else {
                    BasePage<Map<String, Object>> page = new BasePage<Map<String, Object>>();
                    page.setPageNumber(search.getPage());
                    page.setSize(search.getLimit());
                    return page;
                }
            }
        }
        if (search.getSearchList().size() > 0) {
            search.getSearchList().forEach(r -> {
                List<String> values = r.getValues();
                String name = r.getName();
                if (!ParamsUtil.isValid(name)) {
                    throw new CrmException(CrmCodeEnum.CRM_ILLEGAL_CHARACTERS_ERROR);
                }
                values.forEach(value -> {
                    if (StrUtil.isNotEmpty(value) && !ParamsUtil.isValid(value)) {
                        throw new CrmException(CrmCodeEnum.CRM_ILLEGAL_CHARACTERS_ERROR);

                    }
                });
                String s = "a";
                if ("customerName".equals(name)) {
                    s = "b";
                    name = StrUtil.toUnderlineCase(name);
                } else {
                    name = StrUtil.toUnderlineCase(name);
                }
                if (r.getSearchEnum() == CrmSearchBO.FieldSearchEnum.IS_NULL) {
                    //不存在
                    conditions.append(" and ").append(s).append(".").append(name).append(" IS NULL");

                } else if (r.getSearchEnum() == CrmSearchBO.FieldSearchEnum.IS) {
                    //等于
                    conditions.append(" and  ").append(s).append(".").append(name).append(" = '").append(r.getValues().get(0)).append("' ");

                } else if (r.getSearchEnum() == CrmSearchBO.FieldSearchEnum.ID) {
                    //等于
                    conditions.append(" and  ").append(s).append(".invoice_id in (").append(String.join(",", r.getValues())).append(") ");

                } else if (r.getSearchEnum() == CrmSearchBO.FieldSearchEnum.IS_NOT) {
                    //不等于
                    conditions.append(" and  ").append(s).append(".").append(name).append(" != '").append(r.getValues().get(0)).append("' ");

                } else if (r.getSearchEnum() == CrmSearchBO.FieldSearchEnum.CONTAINS) {
                    //包含
                    conditions.append(" and  ").append(s).append(".").append(name).append("  LIKE CONCAT('%','").append(r.getValues().get(0)).append("','%') ");

                } else if (r.getSearchEnum() == CrmSearchBO.FieldSearchEnum.NOT_CONTAINS) {
                    //不包含
                    conditions.append(" and  ").append(s).append(".").append(name).append(" NOT LIKE CONCAT('%','").append(r.getValues().get(0)).append("','%') ");

                } else if (r.getSearchEnum() == CrmSearchBO.FieldSearchEnum.IS_NOT_NULL) {
                    //不为空
                    conditions.append(" and  ").append(s).append(".").append(name).append(" IS NOT NULL ");

                } else if (r.getSearchEnum() == CrmSearchBO.FieldSearchEnum.GT) {
                    //大于
                    conditions.append(" and  ").append(s).append(".").append(name).append(" > '").append(r.getValues().get(0)).append("' ");

                } else if (r.getSearchEnum() == CrmSearchBO.FieldSearchEnum.EGT) {
                    //大于等于
                    conditions.append(" and  ").append(s).append(".").append(name).append(" >= '").append(r.getValues().get(0)).append("' ");

                } else if (r.getSearchEnum() == CrmSearchBO.FieldSearchEnum.LT) {
                    //小于
                    conditions.append(" and  ").append(s).append(".").append(name).append(" < '").append(r.getValues().get(0)).append("' ");

                } else if (r.getSearchEnum() == CrmSearchBO.FieldSearchEnum.ELT) {
                    //小于等于
                    conditions.append(" and  ").append(s).append(".").append(name).append(" <= '").append(r.getValues().get(0)).append("' ");
                } else if (r.getSearchEnum() == CrmSearchBO.FieldSearchEnum.DATE) {
                    //日期筛选
                    conditions.append(" and ").append(s).append(".").append(name).append("  BETWEEN  '").append(r.getValues().get(0)).append("' and '").append(r.getValues().get(1)).append("' ");
                } else if (r.getSearchEnum() == CrmSearchBO.FieldSearchEnum.DATE_TIME) {
                    //日期时间筛选
                    conditions.append("  and ").append(s).append(".").append(name).append("  BETWEEN  '").append(r.getValues().get(0)).append("' and '").append(r.getValues().get(1)).append("' ");
                } else if (r.getSearchEnum().getType() == 13) {
                    //在..之中
                    if (r.getValues().size() > 0) {
                        List<String> list = new ArrayList<String>();
                        r.getValues().forEach(string -> {
                            list.add("'" + string + "'");
                        });
                        String a = String.join(",", list);
                        conditions.append(" and  ").append(s).append(".").append(name).append(" IN(").append(a).append(") ");
                    }
                } else if (r.getSearchEnum().getType() == 14) {
                    //不在..之中
                    if (r.getValues().size() > 0) {
                        List<String> list = new ArrayList<String>();
                        r.getValues().forEach(string -> {
                            list.add("'" + string + "'");
                        });
                        String a = String.join(",", list);
                        conditions.append(" and  ").append(s).append(".").append(name).append(" NOT IN(").append(a).append(") ");
                    }
                } else if (r.getSearchEnum().getType() == 15) {
                    //分组 在..之中
                    if (r.getValues().size() > 0) {
                        conditions.append(" and  FIND_IN_SET('" + r.getValues().get(0) + "'," + s + "." + name + ")");
                    }
                } else if (r.getSearchEnum().getType() == 16) {
                    //分组 不在..之中
                    if (r.getValues().size() > 0) {
                        conditions.append(" and  (NOT FIND_IN_SET('").append(r.getValues().get(0)).append("',").append(s).append(".").append(name).append(") OR ").append(s).append(".").append(name).append(" IS NULL) ");
                    }
                }
            });

        }
        conditions.append(AuthUtil.getCrmAuthSql(CrmEnum.INVOICE, "a", 1));
        return baseMapper.queryPageList(search.parse(), conditions.toString());
    }

    @Override
    @Transactional
    public void saveInvoice(CrmInvoice crmInvoice, Long checkUserId) {
        List<AdminConfig> configList = adminService.queryConfigByName("numberSetting").getData();
        AdminConfig adminConfig = configList.stream().filter(config -> Objects.equals(getLabel().getType().toString(), config.getValue())).collect(Collectors.toList()).get(0);
        if (adminConfig.getStatus() == 1 && StrUtil.isEmpty(crmInvoice.getInvoiceApplyNumber())) {
            String result = crmNumberSettingService.generateNumber(adminConfig, null);
            crmInvoice.setInvoiceApplyNumber(result);
        }
        crmInvoice.setOwnerUserId(UserUtil.getUserId());
        Integer examineCount = crmExamineService.queryCount(CrmEnum.INVOICE);
        CrmExamineData crmExamineData = null;
        if (examineCount > 0) {
            crmExamineData = crmExamineRecordService.saveExamineRecord(3, checkUserId, crmInvoice.getOwnerUserId(), null, crmInvoice.getCheckStatus());
            if (crmExamineData.getStatus() == 0) {
                throw new CrmException(CrmCodeEnum.CRM_CONTRACT_EXAMINE_STEP_ERROR);
            } else if (crmExamineData.getStatus() == 2) {
                throw new CrmException(CrmCodeEnum.CRM_CONTRACT_EXAMINE_USER_ERROR);
            } else {
                crmInvoice.setExamineRecordId(crmExamineData.getRecordId());
            }
            crmInvoice.setCheckStatus(0);
        } else {
            if (crmInvoice.getCheckStatus() == null) {
                crmInvoice.setCheckStatus(1);
            }
        }
        String batchId = StrUtil.isNotEmpty(crmInvoice.getBatchId()) ? crmInvoice.getBatchId() : IdUtil.simpleUUID();
        crmInvoice.setCreateUserId(UserUtil.getUserId()).setCreateTime(new Date()).setUpdateTime(new Date()).setBatchId(batchId);
        save(crmInvoice);
        actionRecordUtil.addRecord(crmInvoice.getInvoiceId(), CrmEnum.INVOICE, crmInvoice.getInvoiceApplyNumber());
        addExamineNotice(crmInvoice, crmExamineData);
    }


    @Override
    @Transactional
    public void updateInvoice(CrmInvoice crmInvoice, Long checkUserId) {
        CrmInvoice oldInvoice = getById(crmInvoice.getInvoiceId());
        if (oldInvoice.getCheckStatus() == 1) {
            throw new CrmException(CrmCodeEnum.CRM_INVOICE_EXAMINE_PASS_ERROR);
        }
        if (oldInvoice.getCheckStatus() != 4 && oldInvoice.getCheckStatus() != 2) {
            throw new CrmException(CrmCodeEnum.CRM_CONTRACT_EDIT_ERROR);
        }
        Integer examineCount = crmExamineService.queryCount(CrmEnum.INVOICE);
        CrmExamineData crmExamineData = null;
        if (examineCount > 0) {
            crmExamineData = crmExamineRecordService.saveExamineRecord(3, checkUserId, oldInvoice.getOwnerUserId(), oldInvoice.getExamineRecordId(), crmInvoice.getCheckStatus());
            if (crmExamineData.getStatus() == 0) {
                throw new CrmException(CrmCodeEnum.CRM_CONTRACT_EXAMINE_STEP_ERROR);
            } else if (crmExamineData.getStatus() == 2) {
                throw new CrmException(CrmCodeEnum.CRM_CONTRACT_EXAMINE_USER_ERROR);
            } else {
                crmInvoice.setExamineRecordId(crmExamineData.getRecordId());
            }
            crmInvoice.setCheckStatus(0);
        } else {
            if (crmInvoice.getCheckStatus() == null) {
                crmInvoice.setCheckStatus(1);
            }
            if (oldInvoice.getExamineRecordId() != null) {
                LambdaUpdateWrapper<CrmExamineLog> lambdaUpdateWrapper = new LambdaUpdateWrapper<CrmExamineLog>();
                lambdaUpdateWrapper.eq(CrmExamineLog::getRecordId, oldInvoice.getExamineRecordId()).set(CrmExamineLog::getIsRecheck, "1");
                crmExamineLogService.update(lambdaUpdateWrapper);
            }
        }
        actionRecordUtil.updateRecord(BeanUtil.beanToMap(oldInvoice), BeanUtil.beanToMap(crmInvoice), CrmEnum.INVOICE, crmInvoice.getInvoiceApplyNumber(), crmInvoice.getInvoiceId());
        crmInvoice.setUpdateTime(new Date());
        updateById(crmInvoice);
        addExamineNotice(crmInvoice, crmExamineData);
    }

    @Override
    public CrmInvoice queryById(Integer invoiceId) {
        return getBaseMapper().queryById(invoiceId);
    }

    @Override
    public void updateInvoiceStatus(CrmInvoice crmInvoice) {
        LambdaUpdateWrapper<CrmInvoice> lambdaUpdateWrapper = new LambdaUpdateWrapper<CrmInvoice>();
        lambdaUpdateWrapper.eq(CrmInvoice::getInvoiceId, crmInvoice.getInvoiceId())
                .set(CrmInvoice::getInvoiceStatus, 1)
                .set(CrmInvoice::getInvoiceNumber, crmInvoice.getInvoiceNumber())
                .set(CrmInvoice::getRealInvoiceDate, crmInvoice.getRealInvoiceDate())
                .set(CrmInvoice::getLogisticsNumber, crmInvoice.getLogisticsNumber());
        update(lambdaUpdateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<Integer> ids) {
        for (Integer id : ids) {
            CrmInvoice crmInvoice = getById(id);
            boolean bol = (crmInvoice.getCheckStatus() != 4 && crmInvoice.getCheckStatus() != 5) && (!UserUtil.getUserId().equals(UserUtil.getSuperUser()) && !UserUtil.getUser().getRoles().contains(UserUtil.getSuperRole()));
            if (bol) {
                throw new CrmException(CrmCodeEnum.CAN_ONLY_DELETE_WITHDRAWN_AND_SUBMITTED_EXAMINE);
            }
            ApplicationContextHolder.getBean(AdminFileService.class).delete(Collections.singletonList(crmInvoice.getBatchId()));
            removeById(crmInvoice.getInvoiceId());
        }
    }

    @Override
    public void changeOwnerUser(List<Integer> ids, Long ownerUserId) {
        LambdaUpdateWrapper<CrmInvoice> lambdaUpdateWrapper = new LambdaUpdateWrapper<CrmInvoice>();
        lambdaUpdateWrapper.in(CrmInvoice::getInvoiceId, ids).set(CrmInvoice::getOwnerUserId, ownerUserId);
        update(lambdaUpdateWrapper);
        for (Integer id : ids) {
            CrmInvoice invoice = getById(id);
            actionRecordUtil.addConversionRecord(id, CrmEnum.INVOICE, ownerUserId, invoice.getInvoiceApplyNumber());
        }
    }

    @Override
    public List<FileEntity> queryFileList(Integer id) {
        List<FileEntity> fileEntityList = new ArrayList<>();
        CrmInvoice crmInvoice = getById(id);
        AdminFileService fileService = ApplicationContextHolder.getBean(AdminFileService.class);
        fileService.queryFileList(crmInvoice.getBatchId()).getData().forEach(fileEntity -> {
            fileEntity.setSource("附件上传");
            fileEntity.setReadOnly(0);
            fileEntityList.add(fileEntity);
        });
        return fileEntityList;
    }

    @Override
    public void saveInvoiceInfo(CrmInvoiceInfo crmInvoiceInfo) {
        crmInvoiceInfo.setCreateUserId(UserUtil.getUserId()).setCreateTime(new Date());
        crmInvoiceInfoService.save(crmInvoiceInfo);
    }

    @Override
    public void updateInvoiceInfo(CrmInvoiceInfo crmInvoiceInfo) {
        boolean auth = AuthUtil.isRwAuth(crmInvoiceInfo.getCustomerId(), CrmEnum.CUSTOMER);
        if (auth) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
        }
        crmInvoiceInfoService.updateById(crmInvoiceInfo);
    }

    @Override
    public void deleteInvoiceInfo(Integer infoId) {
        CrmInvoiceInfo invoiceInfo = crmInvoiceInfoService.getById(infoId);
        boolean auth = AuthUtil.isRwAuth(invoiceInfo.getCustomerId(), CrmEnum.CUSTOMER);
        if (auth) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NO_AUTH);
        }
        crmInvoiceInfoService.removeById(infoId);

    }


    /*
   添加系统通知
   */
    private void addExamineNotice(CrmInvoice crmInvoice, CrmExamineData crmExamineData) {

        if (ObjectUtil.equal(crmInvoice.getCheckStatus(), 0)) {
            List<Long> examineLogIdList = crmExamineData.getExamineLogIdList();
            if (CollUtil.isNotEmpty(examineLogIdList)){
                examineLogIdList.forEach(examineLogId -> {
                    CrmExamineLog examineLog = crmExamineLogService.getById(examineLogId);
                    crmExamineRecordService.addMessage(3,1,examineLog, UserUtil.getUserId());
                });
            }
        }
    }

    /**
     * 获取crm列表类型
     *
     * @return data
     */
    public CrmEnum getLabel() {
        return CrmEnum.INVOICE;
    }
}
