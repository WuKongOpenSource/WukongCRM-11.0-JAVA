package com.kakarote.admin.service.impl;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.google.gson.JsonObject;
import com.kakarote.admin.common.AdminCodeEnum;
import com.kakarote.admin.entity.BO.AdminLanguagePackBO;
import com.kakarote.admin.entity.PO.AdminConfig;
import com.kakarote.admin.entity.PO.AdminLanguagePack;
import com.kakarote.admin.entity.PO.AdminUserConfig;
import com.kakarote.admin.entity.VO.AdminLanguagePackVO;
import com.kakarote.admin.mapper.AdminLanguagePackMapper;
import com.kakarote.admin.service.IAdminConfigService;
import com.kakarote.admin.service.IAdminLanguagePackService;
import com.kakarote.admin.service.IAdminUserConfigService;
import com.kakarote.core.common.R;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * 语言包表 服务实现类
 * </p>
 *
 * @author zmj
 * @since 2020-12-01
 */
@Service
public class AdminLanguagePackServiceImpl extends BaseServiceImpl<AdminLanguagePackMapper, AdminLanguagePack> implements IAdminLanguagePackService {

    @Autowired
    private AdminFileService adminFileService;

    @Autowired
    private IAdminUserConfigService adminUserConfigService;

    @Autowired
    private IAdminConfigService adminConfigService;

    private static final String DEFLAUT_LANGUAGE_PACK = "DeflautLanguagePack";
    private static final String DEFLAUT_LANGUAGE_PACK_DESCRIBE = "设置默认语言包";
    private static final String LANGUAGE_PACK_CHINESE = "中文";
    private static final int SYSTEM = 0;
    private static final int USER = 1;

    @Override
    public BasePage<AdminLanguagePackVO> queryLanguagePackList(AdminLanguagePackBO adminLanguagePackBO, Integer systemOrUser) {
        BasePage<AdminLanguagePackVO> languagePackBasePage = getBaseMapper().queryLanguagePackList(adminLanguagePackBO.parse());
        JSONObject deflautLanguagePack = queryDeflautLanguagePackSetting(systemOrUser);
        Integer languagePackId = deflautLanguagePack.getInteger("languagePackId");
        if (languagePackId != null) {
            languagePackBasePage.getList().forEach(adminLanguagePackVO -> {
                if(adminLanguagePackVO.getLanguagePackId().equals(languagePackId)){
                    adminLanguagePackVO.setDefaultLanguage(1);
                }
            });
        }
        return languagePackBasePage;
    }

    @Override
    public Result addOrUpdateLanguagePack(MultipartFile file, AdminLanguagePackBO adminLanguagePackBO) {

        AdminLanguagePack languagePack = getById(adminLanguagePackBO.getLanguagePackId());
        int count = 0;
        if (languagePack != null) {
            count = lambdaQuery().ne(AdminLanguagePack::getLanguagePackId, adminLanguagePackBO.getLanguagePackId()).
                    eq(AdminLanguagePack::getLanguagePackName, adminLanguagePackBO.getLanguagePackName()).count();
        }else{
            count = lambdaQuery().eq(AdminLanguagePack::getLanguagePackName, adminLanguagePackBO.getLanguagePackName()).count();
        }
        if (count > 0){
            throw new CrmException(AdminCodeEnum.ADMIN_LANGUAGE_PACK_NAME_ERROR);
        }

        String filePath = getFilePath(file);
        AtomicReference<Integer> num = new AtomicReference<>(0);
        StringBuilder contextStr = new StringBuilder();

        JsonObject jsonObject = new JsonObject();
        ExcelUtil.readBySax(filePath, 0, (int sheetIndex, int rowIndex, List<Object> rowList) -> {
            if (rowIndex > 0) {
                if (rowIndex>1) {
                    contextStr.append(",");
                };
                num.getAndSet(num.get() + 1);
                String filedName = rowList.get(0).toString().trim();
                String filedCustom = "";
                if (rowList.size() >2){
                    filedCustom = rowList.get(2).toString().trim();
                }
                if (filedName.contains(".")) {
                    String[] filedNameArr = filedName.split("\\.");
                    if (jsonObject.has(filedNameArr[0])){
                        jsonObject.getAsJsonObject(filedNameArr[0]).addProperty(filedNameArr[1], filedCustom);
                    }else{
                        JsonObject jsonObject1 = new JsonObject();
                        jsonObject1.addProperty(filedNameArr[1], filedCustom);
                        jsonObject.add(filedNameArr[0], jsonObject1);
                    }
                }else{
                    jsonObject.addProperty(filedName, filedCustom);
                }
            }
        });

        AdminLanguagePack adminLanguagePackPO = new AdminLanguagePack();
        adminLanguagePackPO.setLanguagePackName(adminLanguagePackBO.getLanguagePackName());
        if( languagePack != null){
            adminLanguagePackPO.setLanguagePackId(languagePack.getLanguagePackId());
        }else{
            adminLanguagePackPO.setCreateTime(new Date());
        }
        adminLanguagePackPO.setLanguagePackContext(jsonObject.toString());
        adminLanguagePackPO.setCreateUserId(UserUtil.getUserId());
        saveOrUpdate(adminLanguagePackPO);
        return R.ok();
    }

    @Override
    public void deleteLanguagePackById(Integer id) {
        List<AdminUserConfig> AdminUserConfigs = adminUserConfigService.queryUserConfigByNameAndValue(DEFLAUT_LANGUAGE_PACK, id.toString());
        if (AdminUserConfigs.size() > 0) {
            throw new CrmException(AdminCodeEnum.ADMIN_LANGUAGE_PACK_EXIST_USER_ERROR);
        }
        removeById(id);
    }

    @Override
    public void exportLanguagePackById(Integer id, HttpServletResponse response) {
        AdminLanguagePack adminLanguagePack = getById(id);
        List<Map<String, Object>> dataList = new ArrayList<>();
        String translateContext = adminLanguagePack.getLanguagePackContext();
        AdminLanguagePack chineseLanguagePack = lambdaQuery().eq(AdminLanguagePack::getLanguagePackName, LANGUAGE_PACK_CHINESE).one();
        if (chineseLanguagePack == null) {
            throw new CrmException(AdminCodeEnum.ADMIN_LANGUAGE_PACK_CHOINESE_ERROR);
        }
        Map<String, String> translateMap = getFilePath(translateContext);
        Map<String, String> chineseMap = getFilePath(chineseLanguagePack.getLanguagePackContext());

        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            writer.addHeaderAlias("fileName", "原始字段");
            if(LANGUAGE_PACK_CHINESE.equals(adminLanguagePack.getLanguagePackName())){
                writer.addHeaderAlias("chinese", "字段名称");
            }else{
                writer.addHeaderAlias("chinese", LANGUAGE_PACK_CHINESE);
            }
            writer.addHeaderAlias("translateName", adminLanguagePack.getLanguagePackName());
            translateMap.forEach((k, v) -> {
                Map<String, Object> record = new HashMap<>();
                record.put("fileName", k);
                record.put("chinese", chineseMap.get(k));
                record.put("translateName", v);
                dataList.add(record);
            });

            writer.setOnlyAlias(true);
            writer.write(dataList, true);
            writer.setRowHeight(0, 20);
            for (int i = 0; i < 3; i++) {
                writer.setColumnWidth(i, 30);
            }
            Cell cell = writer.getCell(0, 0);
            CellStyle cellStyle = cell.getCellStyle();
            cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = writer.createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 16);
            cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);
            //自定义标题别名
            //response为HttpServletResponse对象
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=languagePack.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出语言包错误：", e);
        }
    }

    /**
     * 解析json语言包
     */
    private Map<String, String> getFilePath(String languagePackContext) {
        JSONObject jsonObject = JSONObject.parseObject(languagePackContext, Feature.OrderedField);

        HashMap<String, String> fileMap = new LinkedHashMap<>();
        if(jsonObject instanceof JSONObject) {
            for (Map.Entry<String, Object> entry: jsonObject.entrySet()) {
                Object o = entry.getValue();
                if(o instanceof String) {
                    fileMap.put(entry.getKey(), entry.getValue().toString());
                } else if (o instanceof JSONObject) {
                    JSONObject jsonObject2 = (JSONObject) o;
                    for (Map.Entry<String, Object> entry2: jsonObject2.entrySet()) {
                        Object o2 = entry2.getValue();
                        if(o2 instanceof String) {
                            fileMap.put(entry.getKey()+"."+entry2.getKey(), entry2.getValue().toString());
                        }
                    }
                }
            }
        }
        return fileMap;
    }

    @Override
    public String queryLanguagePackContextById(Integer id) {
        return getById(id).getLanguagePackContext();
    }

    @Override
    public void downloadExcel(HttpServletResponse response) {
        List<Map<String, Object>> dataList = new ArrayList<>();
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            writer.addHeaderAlias("file", "原始字段");
            writer.addHeaderAlias("chinese", "中文");
            writer.addHeaderAlias("translate", "英语");
            Map<String, Object> record = new HashMap<>();
            record.put("file", "customer");
            record.put("chinese", "客户");
            record.put("translate", "Customer");
            dataList.add(record);
            writer.setOnlyAlias(true);
            writer.write(dataList, true);
            writer.setRowHeight(0, 20);
            for (int i = 0; i < 3; i++) {
                writer.setColumnWidth(i, 30);
            }
            Cell cell = writer.getCell(0, 0);
            CellStyle cellStyle = cell.getCellStyle();
            cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = writer.createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 16);
            cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);
            //自定义标题别名
            //response为HttpServletResponse对象
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=languagePack_imp.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出语言包错误：", e);
        }
    }

    @Override
    public void updateLanguagePackNameById(AdminLanguagePackBO adminLanguagePackBO) {
        AdminLanguagePack oldAdminLanguagePac = lambdaQuery().ne(AdminLanguagePack::getLanguagePackId, adminLanguagePackBO.getLanguagePackId()).
                eq(AdminLanguagePack::getLanguagePackName, adminLanguagePackBO.getLanguagePackName()).one();
        if(oldAdminLanguagePac != null){
            throw new CrmException(AdminCodeEnum.ADMIN_LANGUAGE_PACK_NAME_ERROR);
        }
        AdminLanguagePack languagePack = getById(adminLanguagePackBO.getLanguagePackId());
        languagePack.setLanguagePackName(adminLanguagePackBO.getLanguagePackName());
        saveOrUpdate(languagePack);
    }

    @Override
    public void setDeflautLanguagePackSetting(Integer id, Integer systemOrUser) {
        //修改默认语言 systemOrUser(0:系统用户， 1:个人用户)
        adminConfigService.queryConfigByName(DEFLAUT_LANGUAGE_PACK);
        if(systemOrUser == SYSTEM){
            AdminConfig adminConfig = adminConfigService.queryConfigByName(DEFLAUT_LANGUAGE_PACK);
            if (adminConfig != null) {
                adminConfig.setValue(id.toString());
                adminConfigService.updateById(adminConfig);
            } else {
                adminConfig = new AdminConfig();
                adminConfig.setStatus(1);
                adminConfig.setName(DEFLAUT_LANGUAGE_PACK);
                adminConfig.setValue(id.toString());
                adminConfig.setDescription(DEFLAUT_LANGUAGE_PACK_DESCRIBE);
                adminConfigService.save(adminConfig);
            }
        }else if(systemOrUser == USER){
            AdminUserConfig userConfig = adminUserConfigService.queryUserConfigByName(DEFLAUT_LANGUAGE_PACK);
            if (userConfig != null) {
                userConfig.setValue(id.toString());
                adminUserConfigService.updateById(userConfig);
            } else {
                userConfig = new AdminUserConfig();
                userConfig.setStatus(1);
                userConfig.setName(DEFLAUT_LANGUAGE_PACK);
                userConfig.setValue(id.toString());
                userConfig.setUserId(UserUtil.getUserId());
                userConfig.setDescription(DEFLAUT_LANGUAGE_PACK_DESCRIBE);
                adminUserConfigService.save(userConfig);
            }
        }
    }

    @Override
    public JSONObject queryDeflautLanguagePackSetting(Integer systemOrUser) {
        //查询默认语言 systemOrUser(0:系统用户， 1:个人用户)
        int languagePackId = 0;
        String languagePackName = null;
        String languagePackContext = null;

        if (systemOrUser == SYSTEM) {
            AdminConfig adminConfig = adminConfigService.queryConfigByName(DEFLAUT_LANGUAGE_PACK);
            if (adminConfig != null) {
                languagePackId = Integer.parseInt(adminConfig.getValue());
                AdminLanguagePack adminLanguagePack = getById(languagePackId);
                languagePackName = adminLanguagePack.getLanguagePackName();
                languagePackContext =  adminLanguagePack.getLanguagePackContext();
            }
        }else if(systemOrUser == USER){
            //个人和系统均有默认语言包，个人优先级高于系统
            AdminUserConfig userConfig = adminUserConfigService.queryUserConfigByName(DEFLAUT_LANGUAGE_PACK);
            if (userConfig != null) {
                languagePackId = Integer.parseInt(userConfig.getValue());
                AdminLanguagePack adminLanguagePack = getById(languagePackId);
                languagePackName = adminLanguagePack.getLanguagePackName();
                languagePackContext =  adminLanguagePack.getLanguagePackContext();
            }else{
                AdminConfig adminConfig = adminConfigService.queryConfigByName(DEFLAUT_LANGUAGE_PACK);
                if (adminConfig != null) {
                    languagePackId = Integer.parseInt(adminConfig.getValue());
                    AdminLanguagePack adminLanguagePack = getById(languagePackId);
                    languagePackName = adminLanguagePack.getLanguagePackName();
                    languagePackContext =  adminLanguagePack.getLanguagePackContext();
                }
            }
        }
        return new JSONObject().fluentPut("languagePackId", languagePackId).fluentPut("languagePackName", languagePackName).fluentPut("languagePackContext", languagePackContext);
    }

    private String getFilePath(MultipartFile file) {
        String dirPath = FileUtil.getTmpDirPath();
        try {
            InputStream inputStream = file.getInputStream();
            File fromStream = FileUtil.writeFromStream(inputStream, dirPath + "/" + IdUtil.simpleUUID() + file.getOriginalFilename());
            return fromStream.getAbsolutePath();
        } catch (IOException e) {
            throw new CrmException(SystemCodeEnum.SYSTEM_UPLOAD_FILE_ERROR);
        }
    }

}
