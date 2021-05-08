package com.kakarote.crm.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.FieldEnum;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.PO.CrmField;
import com.kakarote.crm.entity.PO.CrmFieldSort;
import com.kakarote.crm.entity.VO.CrmFieldSortVO;
import com.kakarote.crm.mapper.CrmFieldSortMapper;
import com.kakarote.crm.service.ICrmFieldService;
import com.kakarote.crm.service.ICrmFieldSortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 字段排序表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-19
 */
@Service
public class CrmFieldSortServiceImpl extends BaseServiceImpl<CrmFieldSortMapper, CrmFieldSort> implements ICrmFieldSortService {

    @Autowired
    private ICrmFieldService crmFieldService;

    /**
     * 查询模块字段列表
     *
     * @param label label
     * @return data
     */
    @Override
    public List<CrmFieldSortVO> queryListHead(Integer label) {
        Long userId = UserUtil.getUserId();
        QueryWrapper<CrmFieldSort> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("label", label);
        int number = count(wrapper);
        if (number == 0) {
            saveUserFieldSort(label, userId);
        }
        return getBaseMapper().queryListHead(label, userId);
    }

    @Override
    public List<CrmFieldSort> queryAllFieldSortList(Integer label,Long userId) {
        List<CrmField> crmFieldList = crmFieldService.list(label, false);
        CrmEnum crmEnum = CrmEnum.parse(label);
        switch (crmEnum) {
            case CUSTOMER:
                crmFieldList.add(new CrmField("status", "锁定状态", FieldEnum.NUMBER));
                crmFieldList.add(new CrmField("dealStatus", "成交状态", FieldEnum.SELECT));
                crmFieldList.add(new CrmField("lastTime", "最后跟进时间", FieldEnum.DATETIME));
                crmFieldList.add(new CrmField("lastContent", "最后跟进记录", FieldEnum.TEXTAREA));
                crmFieldList.add(new CrmField("detailAddress", "详细地址", FieldEnum.TEXT));
                crmFieldList.add(new CrmField("address", "地区定位", FieldEnum.MAP_ADDRESS));
                crmFieldList.add(new CrmField("poolDay", "距进入公海天数", FieldEnum.NUMBER));
                crmFieldList.add(new CrmField("ownerUserName", "负责人", FieldEnum.TEXT));
                crmFieldList.add(new CrmField("teamMemberIds", "相关团队", FieldEnum.USER));
                break;
            case BUSINESS:
                crmFieldList.add(new CrmField("typeName", "商机状态组", FieldEnum.SELECT));
                crmFieldList.add(new CrmField("statusName", "商机阶段", FieldEnum.SELECT));
                crmFieldList.add(new CrmField("lastTime", "最后跟进时间", FieldEnum.DATETIME));
                crmFieldList.add(new CrmField("ownerUserName", "负责人", FieldEnum.TEXT));
                crmFieldList.add(new CrmField("teamMemberIds", "相关团队", FieldEnum.USER));
                break;
            case CONTRACT:
                crmFieldList.add(new CrmField("checkStatus", "审核状态", FieldEnum.NUMBER));
                crmFieldList.add(new CrmField("receivedMoney", "已收款金额", FieldEnum.FLOATNUMBER));
                crmFieldList.add(new CrmField("unreceivedMoney", "未收款金额", FieldEnum.FLOATNUMBER));
                crmFieldList.add(new CrmField("lastTime", "最后跟进时间", FieldEnum.DATETIME));
                crmFieldList.add(new CrmField("ownerUserName", "负责人", FieldEnum.TEXT));
                crmFieldList.add(new CrmField("teamMemberIds", "相关团队", FieldEnum.USER));
                break;
            case RECEIVABLES:
                crmFieldList.add(new CrmField("checkStatus", "审核状态", FieldEnum.NUMBER));
                crmFieldList.add(new CrmField("contractMoney", "合同金额", FieldEnum.FLOATNUMBER));
                crmFieldList.add(new CrmField("ownerUserName", "负责人", FieldEnum.TEXT));
                crmFieldList.add(new CrmField("teamMemberIds", "相关团队", FieldEnum.USER));
                break;
            case LEADS:
                crmFieldList.add(new CrmField("lastTime", "最后跟进时间", FieldEnum.DATE));
                crmFieldList.add(new CrmField("lastContent", "最后跟进记录", FieldEnum.TEXTAREA));
                crmFieldList.add(new CrmField("ownerUserName", "负责人", FieldEnum.TEXT));
                break;
            case PRODUCT:
                crmFieldList.add(new CrmField("status", "是否上下架", FieldEnum.NUMBER));
                crmFieldList.add(new CrmField("ownerUserName", "负责人", FieldEnum.TEXT));
                break;
            case CONTACTS:
                crmFieldList.add(new CrmField("lastTime", "最后跟进时间", FieldEnum.DATETIME));
                crmFieldList.add(new CrmField("ownerUserName", "负责人", FieldEnum.TEXT));
                crmFieldList.add(new CrmField("teamMemberIds", "相关团队", FieldEnum.USER));
                break;
            case CUSTOMER_POOL:
                crmFieldList.add(new CrmField("lastContent", "最后跟进记录", FieldEnum.TEXTAREA));
                crmFieldList.add(new CrmField("ownerUserName", "负责人", FieldEnum.TEXT));
                break;
            case RETURN_VISIT:
                break;
            case INVOICE:
                crmFieldList.add(new CrmField("ownerUserName", "负责人", FieldEnum.TEXT));
                crmFieldList.add(new CrmField("checkStatus", "审核状态", FieldEnum.NUMBER));
                crmFieldList.add(new CrmField("invoiceStatus", "开票状态", FieldEnum.NUMBER));
                crmFieldList.add(new CrmField("invoiceNumber", "发票号码", FieldEnum.TEXT));
                crmFieldList.add(new CrmField("realInvoiceDate", "实际开票日期", FieldEnum.DATE));
                crmFieldList.add(new CrmField("logisticsNumber", "物流单号", FieldEnum.TEXT));
            default:
                break;
        }
        crmFieldList.add(new CrmField("updateTime", "更新时间", FieldEnum.DATETIME));
        crmFieldList.add(new CrmField("createTime", "创建时间", FieldEnum.DATETIME));
        crmFieldList.add(new CrmField("createUserName", "创建人", FieldEnum.TEXT));

        List<CrmFieldSort> fieldSortList = new ArrayList<>();
        for (int i = 0; i < crmFieldList.size(); i++) {
            CrmFieldSort fieldSort = new CrmFieldSort();
            fieldSort.setFieldId(crmFieldList.get(i).getFieldId());
            fieldSort.setFieldName(parseFieldName(crmFieldList.get(i).getFieldName()));
            fieldSort.setName(crmFieldList.get(i).getName());
            fieldSort.setSort(i);
            fieldSort.setUserId(userId);
            fieldSort.setStyle(100);
            fieldSort.setIsHide(0);
            fieldSort.setLabel(label);
            fieldSort.setType(crmFieldList.get(i).getType());
            fieldSortList.add(fieldSort);
        }
        return fieldSortList;
    }

    /**
     * 保存用户排序
     *
     * @param label  label
     * @param userId 用户ID
     */
    private void saveUserFieldSort(Integer label, Long userId) {
        List<CrmFieldSort> fieldSortList = queryAllFieldSortList(label,userId);
        saveBatch(fieldSortList, Const.BATCH_SAVE_SIZE);
    }

    private String parseFieldName(String fieldName) {
        if ("contract_id".equals(fieldName) || "plan_id".equals(fieldName)) {
            fieldName = fieldName.substring(0, fieldName.lastIndexOf("_id")).concat("_num");
        }
        if (fieldName.endsWith("_id")) {
            fieldName = fieldName.substring(0, fieldName.lastIndexOf("_id")).concat("_name");
        }
        return StrUtil.toCamelCase(fieldName);
    }
}
