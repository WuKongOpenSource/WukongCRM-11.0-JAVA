package com.kakarote.hrm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.PO.HrmEmployeeContract;
import com.kakarote.hrm.entity.VO.ContractInformationVO;

import java.util.List;

/**
 * <p>
 * 员工合同 服务类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
public interface IHrmEmployeeContractService extends BaseService<HrmEmployeeContract> {

    /**
     * 合同基本信息
     * @param employeeId
     * @return
     */
    List<ContractInformationVO> contractInformation(Integer employeeId);

    /**
     * 添加或者合同
     * @param employeeContract
     */
    void addOrUpdateContract(HrmEmployeeContract employeeContract);

    /**
     * 删除合同
     * @param contractId
     */
    void deleteContract(Integer contractId);

    /**
     * 查询合同到期的员工id
     * @return
     */
    List<Integer> queryToExpireContractCount();

}
