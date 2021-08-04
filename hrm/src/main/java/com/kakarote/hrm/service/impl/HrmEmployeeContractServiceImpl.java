package com.kakarote.hrm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.kakarote.core.common.Result;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.TransferUtil;
import com.kakarote.hrm.constant.HrmActionBehaviorEnum;
import com.kakarote.hrm.constant.LabelGroupEnum;
import com.kakarote.hrm.entity.PO.HrmEmployeeContract;
import com.kakarote.hrm.entity.VO.ContractInformationVO;
import com.kakarote.hrm.mapper.HrmEmployeeContractMapper;
import com.kakarote.hrm.service.IHrmEmployeeContractService;
import com.kakarote.hrm.service.actionrecord.impl.EmployeeActionRecordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 员工合同 服务实现类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Service
public class HrmEmployeeContractServiceImpl extends BaseServiceImpl<HrmEmployeeContractMapper, HrmEmployeeContract> implements IHrmEmployeeContractService {

    @Autowired
    private AdminFileService adminFileService;

    @Resource
    private EmployeeActionRecordServiceImpl employeeActionRecordService;

    @Override
    public List<ContractInformationVO> contractInformation(Integer employeeId) {
        List<HrmEmployeeContract> contractList = lambdaQuery().eq(HrmEmployeeContract::getEmployeeId, employeeId).orderByAsc(HrmEmployeeContract::getSort).list();
        List<ContractInformationVO> contractInformationVOList = TransferUtil.transferList(contractList, ContractInformationVO.class);
        contractInformationVOList.forEach(contractInformationVO -> {
            Result<List<FileEntity>> listResult = adminFileService.queryFileList(contractInformationVO.getBatchId());
            contractInformationVO.setFileList(listResult.getData());
        });
        return contractInformationVOList;
    }

    @Override
    public void addOrUpdateContract(HrmEmployeeContract employeeContract) {
        if (employeeContract.getContractId() == null) {
            employeeActionRecordService.addOrDeleteRecord(HrmActionBehaviorEnum.ADD, LabelGroupEnum.CONTRACT, employeeContract.getEmployeeId());
        } else {
            HrmEmployeeContract old = getById(employeeContract.getContractId());
            employeeActionRecordService.entityUpdateRecord(LabelGroupEnum.CONTRACT, BeanUtil.beanToMap(old), BeanUtil.beanToMap(employeeContract), employeeContract.getEmployeeId());
        }
        saveOrUpdate(employeeContract);
    }

    @Override
    public void deleteContract(Integer contractId) {
        HrmEmployeeContract contract = getById(contractId);
        employeeActionRecordService.addOrDeleteRecord(HrmActionBehaviorEnum.DELETE, LabelGroupEnum.CONTRACT, contract.getEmployeeId());
        removeById(contractId);
    }

    @Override
    public List<Integer> queryToExpireContractCount() {
        return getBaseMapper().queryToExpireContractCount();
    }
}
