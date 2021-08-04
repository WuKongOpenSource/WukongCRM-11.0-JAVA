package com.kakarote.hrm.service;

import com.kakarote.core.servlet.BaseService;
import com.kakarote.hrm.entity.BO.DeleteLeaveInformationBO;
import com.kakarote.hrm.entity.BO.UpdateInformationBO;
import com.kakarote.hrm.entity.PO.HrmEmployeeCertificate;
import com.kakarote.hrm.entity.PO.HrmEmployeeQuitInfo;
import com.kakarote.hrm.entity.VO.PostInformationVO;

/**
 * <p>
 * 员工证书 服务类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
public interface IHrmEmployeePostService extends BaseService<HrmEmployeeCertificate> {

    /**
     * 岗位信息
     * @param employeeId
     * @return
     */
    PostInformationVO postInformation(Integer employeeId);

    /**
     * 修改岗位信息
     * @param updateInformationBO
     */
    void updatePostInformation(UpdateInformationBO updateInformationBO);

    /**
     * 办理离职
     * @param quitInfo
     */
    void addOrUpdateLeaveInformation(HrmEmployeeQuitInfo quitInfo);

    /**
     * 取消离职
     * @param deleteLeaveInformationBO
     */
    void deleteLeaveInformation(DeleteLeaveInformationBO deleteLeaveInformationBO);

    /**
     * 岗位档案信息
     * @return
     */
    PostInformationVO postArchives();

}
