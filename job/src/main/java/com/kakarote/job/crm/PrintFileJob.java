package com.kakarote.job.crm;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.kakarote.core.utils.BaseUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PrintFileJob {

    @XxlJob("PrintFileJob")
    public ReturnT<String> printFileJobHandler(String param){
        String path= BaseUtil.UPLOAD_PATH + "print/" + DateUtil.format(DateUtil.offsetDay(new Date(),-2),"yyyyMMdd");
        XxlJobLogger.log("---------准备删除文件，当前路径："+path+"---------");
        boolean b= FileUtil.del(path);
        XxlJobLogger.log("---------删除文件"+(b?"成功":"失败")+"，当前路径："+path+"---------");
        return b?ReturnT.SUCCESS:ReturnT.SUCCESS;
    }
}
