package com.kakarote.authorization.common;

import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.kakarote.core.feign.admin.entity.LoginLogEntity;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.reflect.Method;
import java.util.Date;
@Slf4j
@Component
public class LoginLogUtil {

    public LoginLogEntity getLogEntity(HttpServletRequest request){
        String ipAddress = ServletUtil.getClientIP(request);
        LoginLogEntity loginLogEntity = new LoginLogEntity();
        loginLogEntity.setAuthResult(1);
        loginLogEntity.setLoginTime(new Date());
        loginLogEntity.setIpAddress(ipAddress);
        String ipCityInfo = getIpCityInfo1(ipAddress);
        log.info("===> ipCityInfo  : {}",ipCityInfo);
        if (ipCityInfo != null) {
            if (ipCityInfo.contains("内网IP")) {
                loginLogEntity.setLoginAddress("内网");
            } else {
                try {
                    String[] addressArr = ipCityInfo.split("\\|");
                    loginLogEntity.setLoginAddress(addressArr[0] + addressArr[2] + addressArr[3] + addressArr[4]);
                } catch (Exception e) {
                    log.error("ip解析异常:{}",e.getMessage());
                }
            }
        }
        UserAgent userAgent = UserAgentUtil.parse(request.getHeader("user-agent"));
        loginLogEntity.setDeviceType(userAgent.isMobile() ? "移动端" : "网页");
        loginLogEntity.setCore(userAgent.getBrowser().toString() + userAgent.getVersion());
        loginLogEntity.setPlatform(userAgent.getOs().toString());
        return loginLogEntity;
    }
    private String getIpCityInfo1(String ip){
        log.info("===> Login IP : {}",ip);
        String dbPath = LoginLogUtil.class.getClassLoader().getResource("").getPath() + "ip2region/ip2region.db";
        log.info("===> dbPath  : {}",dbPath);
        File file = new File(dbPath);
        if (!file.exists()) {
            return null;
        }
        try {
            DbConfig config = new DbConfig();
            DbSearcher searcher = new DbSearcher(config, dbPath);
            Method method = searcher.getClass().getMethod("btreeSearch", String.class);
            if (!Util.isIpAddress(ip)) {
                return null;
            }
            DataBlock dataBlock  = (DataBlock) method.invoke(searcher, ip);
            return dataBlock.getRegion();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
