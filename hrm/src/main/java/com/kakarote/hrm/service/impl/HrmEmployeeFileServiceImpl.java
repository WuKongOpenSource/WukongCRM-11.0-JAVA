package com.kakarote.hrm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.hrm.constant.HrmActionBehaviorEnum;
import com.kakarote.hrm.entity.BO.AddFileBO;
import com.kakarote.hrm.entity.BO.QueryFileBySubTypeBO;
import com.kakarote.hrm.entity.PO.HrmEmployeeFile;
import com.kakarote.hrm.mapper.HrmEmployeeFileMapper;
import com.kakarote.hrm.service.IHrmEmployeeFileService;
import com.kakarote.hrm.service.actionrecord.impl.EmployeeActionRecordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 员工附件表 服务实现类
 * </p>
 *
 * @author huangmingbo
 * @since 2020-05-12
 */
@Service
public class HrmEmployeeFileServiceImpl extends BaseServiceImpl<HrmEmployeeFileMapper, HrmEmployeeFile> implements IHrmEmployeeFileService {

    @Autowired
    private HrmEmployeeFileMapper employeeFileMapper;

    @Autowired
    private AdminFileService adminFileService;

    @Resource
    private EmployeeActionRecordServiceImpl employeeActionRecordService;

    @Override
    public Map<String, Object> queryFileNum(Integer employeeId) {
        return employeeFileMapper.queryFileNum(employeeId);
    }

    @Override
    public List<HrmEmployeeFile> queryFileBySubType(QueryFileBySubTypeBO queryFileBySubTypeBO) {
        List<HrmEmployeeFile> list = lambdaQuery().eq(HrmEmployeeFile::getEmployeeId, queryFileBySubTypeBO.getEmployeeId())
                .eq(HrmEmployeeFile::getSubType, queryFileBySubTypeBO.getSubType())
                .list();
        list.forEach(employeeFile->{
            employeeFile.setFile(adminFileService.queryById(Long.valueOf(employeeFile.getFileId())).getData());
        });
        return list;
    }

    @Override
    public void addFile(AddFileBO addFileBO) {
        HrmEmployeeFile employeeFile = BeanUtil.copyProperties(addFileBO, HrmEmployeeFile.class);
        save(employeeFile);
        employeeActionRecordService.addFileRecord(employeeFile, HrmActionBehaviorEnum.ADD);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFile(Integer employeeFileId) {
        HrmEmployeeFile employeeFile = getById(employeeFileId);
        Long fileId = Long.valueOf(employeeFile.getFileId());
        adminFileService.delete(fileId);
        removeById(employeeFileId);
        employeeActionRecordService.addFileRecord(employeeFile, HrmActionBehaviorEnum.DELETE);
    }
}
