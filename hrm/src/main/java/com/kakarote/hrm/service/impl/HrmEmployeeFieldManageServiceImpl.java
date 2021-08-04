package com.kakarote.hrm.service.impl;

import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.hrm.entity.BO.QueryEmployFieldManageBO;
import com.kakarote.hrm.entity.PO.HrmEmployeeFieldManage;
import com.kakarote.hrm.entity.VO.EmployeeFieldManageVO;
import com.kakarote.hrm.mapper.HrmEmployeeFieldManageMapper;
import com.kakarote.hrm.service.IHrmEmployeeFieldManageService;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * <p>
 * 自定义字段管理表 服务实现类
 * </p>
 *
 * @author guomenghao
 * @since 2021-04-14
 */
@Service
public class HrmEmployeeFieldManageServiceImpl extends BaseServiceImpl<HrmEmployeeFieldManageMapper, HrmEmployeeFieldManage> implements IHrmEmployeeFieldManageService {

    @Override
    public List<EmployeeFieldManageVO> queryEmployeeManageField(QueryEmployFieldManageBO queryEmployFieldManageBO) {
        return  getBaseMapper().queryEmployeeManageField(queryEmployFieldManageBO);
    }

    @Override
    public void setEmployeeManageField(List<EmployeeFieldManageVO> manageFields) {
        List<HrmEmployeeFieldManage> employeeManageFields = manageFields.stream().map(field -> {
            HrmEmployeeFieldManage hrmEmployeeFieldManage = new HrmEmployeeFieldManage();
            hrmEmployeeFieldManage.setId(field.getId());
            hrmEmployeeFieldManage.setIsManageVisible(field.getIsManageVisible());
            return hrmEmployeeFieldManage;
        }).collect(toList());
        updateBatchById(employeeManageFields);
    }

    @Override
    public List<HrmEmployeeFieldManage> queryManageField(Integer entryStatus) {
        List<HrmEmployeeFieldManage> employeeFieldManages  = lambdaQuery().eq(HrmEmployeeFieldManage::getEntryStatus,entryStatus).list();
        return employeeFieldManages;
    }
}
