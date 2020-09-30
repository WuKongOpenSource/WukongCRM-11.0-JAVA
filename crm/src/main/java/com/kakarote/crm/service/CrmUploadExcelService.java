package com.kakarote.crm.service;

import com.kakarote.crm.entity.BO.UploadExcelBO;
import org.springframework.web.multipart.MultipartFile;

public interface CrmUploadExcelService {

    /**
     * 导入excel
     */
    public Long uploadExcel(MultipartFile file, UploadExcelBO uploadExcelBO);


}
