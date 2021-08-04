package com.kakarote.hrm.service;

import com.kakarote.hrm.entity.BO.UploadExcelBO;
import org.springframework.web.multipart.MultipartFile;

public interface HrmUploadExcelService {

    /**
     * 导入excel
     */
    public Long uploadExcel(MultipartFile file, UploadExcelBO uploadExcelBO);



}
