package com.kakarote.crm.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.FieldEnum;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.common.cache.CrmCacheKey;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.SimpleDept;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.redis.Redis;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.BaseUtil;
import com.kakarote.core.utils.UserCacheUtil;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.constant.CrmCodeEnum;
import com.kakarote.crm.constant.CrmEnum;
import com.kakarote.crm.entity.BO.CrmPrintTemplateBO;
import com.kakarote.crm.entity.PO.CrmPrintRecord;
import com.kakarote.crm.entity.PO.CrmPrintTemplate;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;
import com.kakarote.crm.entity.VO.CrmPrintFieldVO;
import com.kakarote.crm.mapper.CrmPrintTemplateMapper;
import com.kakarote.crm.service.*;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 打印模板表 服务实现类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */
@Service
public class CrmPrintTemplateServiceImpl extends BaseServiceImpl<CrmPrintTemplateMapper, CrmPrintTemplate> implements ICrmPrintTemplateService {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ICrmFieldService crmFieldService;

    @Autowired
    private Redis redis;

    @Autowired
    private ICrmPrintRecordService crmPrintRecordService;

    /**
     * 分页查询打印模板列表
     *
     * @param templateBO search
     * @return data
     */
    @Override
    public BasePage<CrmPrintTemplate> queryPrintTemplateList(CrmPrintTemplateBO templateBO) {
        LambdaQueryChainWrapper<CrmPrintTemplate> wrapper = lambdaQuery();
        if (templateBO.getType() != null) {
            wrapper.eq(CrmPrintTemplate::getType, templateBO.getType());
        }
        BasePage<CrmPrintTemplate> page = wrapper.page(templateBO.parse());
        page.getList().forEach(template -> {
            template.setCreateUserName(UserCacheUtil.getUserName(template.getCreateUserId()));
        });
        return page;
    }

    /**
     * 删除打印模板
     *
     * @param templateId templateId
     */

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deletePrintTemplate(Integer templateId) {
        removeById(templateId);
        getBaseMapper().removePrintRecord(templateId);
    }

    /**
     * 查询字段列表
     *
     * @param crmType type
     * @return data
     */
    @Override
    public CrmPrintFieldVO queryFields(Integer crmType) {
        CrmEnum crmEnum = CrmEnum.parse(crmType);
        if (crmEnum == CrmEnum.BUSINESS) {
            List<CrmModelFiledVO> businessList = new ArrayList<>();
            businessList.add(new CrmModelFiledVO("business_name", "商机名称"));
            businessList.add(new CrmModelFiledVO("money", "商机金额"));
            businessList.add(new CrmModelFiledVO("deal_date", "预计成交日期"));
            businessList.add(new CrmModelFiledVO("remark", "备注"));
            businessList.add(new CrmModelFiledVO("type_name", "商机状态组"));
            businessList.add(new CrmModelFiledVO("status_name", "商机阶段"));
            businessList.add(new CrmModelFiledVO("owner_user_name", "负责人"));
            businessList.add(new CrmModelFiledVO("create_user_name", "创建人"));
            businessList.add(new CrmModelFiledVO("create_time", "创建时间"));
            businessList.add(new CrmModelFiledVO("update_time", "更新时间"));
            List<CrmModelFiledVO> businessFieldList = crmFieldService.queryField(crmType);
            Map<String, String> businessMap = businessFieldList.stream().collect(Collectors.toMap(CrmModelFiledVO::getFieldName, CrmModelFiledVO::getName));
            businessList.forEach(productField -> {
                if (businessMap.containsKey(productField.getFieldName())) {
                    productField.setName(businessMap.get(productField.getFieldName()));
                }
            });
            List<CrmModelFiledVO> businessFixedFieldList = businessFieldList.stream().filter(field -> field.getFieldType() == 0).collect(Collectors.toList());
            businessList.addAll(businessFixedFieldList);
            CrmPrintFieldVO crmPrintFieldVO = new CrmPrintFieldVO();
            crmPrintFieldVO.setBusiness(businessList);
            getProductFields(crmPrintFieldVO);
            getCustomerFields(crmPrintFieldVO);
            return crmPrintFieldVO;
        } else if (crmEnum == CrmEnum.CONTRACT) {
            List<CrmModelFiledVO> contractList = new ArrayList<>();
            contractList.add(new CrmModelFiledVO("num", "合同编号"));
            contractList.add(new CrmModelFiledVO("name", "合同名称"));
            contractList.add(new CrmModelFiledVO("money", "合同金额"));
            contractList.add(new CrmModelFiledVO("business_name", "商机名称"));
            contractList.add(new CrmModelFiledVO("owner_user_name", "负责人"));
            contractList.add(new CrmModelFiledVO("order_date", "下单时间"));
            contractList.add(new CrmModelFiledVO("start_time", "合同开始时间"));
            contractList.add(new CrmModelFiledVO("end_time", "合同结束时间"));
            contractList.add(new CrmModelFiledVO("contacts_name", "客户签约人"));
            contractList.add(new CrmModelFiledVO("company_user_name", "公司签约人"));
            contractList.add(new CrmModelFiledVO("remark", "备注"));
            contractList.add(new CrmModelFiledVO("create_user_name", "创建人"));
            contractList.add(new CrmModelFiledVO("create_time", "创建时间"));
            contractList.add(new CrmModelFiledVO("update_time", "更新时间"));
            contractList.add(new CrmModelFiledVO("received_money", "已收款金额"));
            contractList.add(new CrmModelFiledVO("unreceived_money", "未收款金额"));
            List<CrmModelFiledVO> contractFieldList = crmFieldService.queryField(crmType);
            Map<String, String> contractMap = contractFieldList.stream().collect(Collectors.toMap(CrmModelFiledVO::getFieldName, CrmModelFiledVO::getName));
            contractFieldList.forEach(productField -> {
                if (contractMap.containsKey(productField.getFieldName())) {
                    productField.setName(contractMap.get(productField.getFieldName()));
                }
            });
            List<CrmModelFiledVO> contractFixedFieldList = contractFieldList.stream().filter(field -> field.getFieldType() == 0).collect(Collectors.toList());
            contractList.addAll(contractFixedFieldList);
            CrmPrintFieldVO crmPrintFieldVO = new CrmPrintFieldVO();
            crmPrintFieldVO.setContract(contractList);
            getProductFields(crmPrintFieldVO);
            getCustomerFields(crmPrintFieldVO);
            List<CrmModelFiledVO> contactsList = new ArrayList<>();
            contactsList.add(new CrmModelFiledVO("name", "姓名"));
            contactsList.add(new CrmModelFiledVO("sex", "性别"));
            contactsList.add(new CrmModelFiledVO("policymakers", "是否关键决策人"));
            contactsList.add(new CrmModelFiledVO("customer_name", "客户名称"));
            contactsList.add(new CrmModelFiledVO("mobile", "手机"));
            contactsList.add(new CrmModelFiledVO("telephone", "电话"));
            contactsList.add(new CrmModelFiledVO("email", "邮箱"));
            contactsList.add(new CrmModelFiledVO("post", "职务"));
            contactsList.add(new CrmModelFiledVO("address", "地址"));
            contactsList.add(new CrmModelFiledVO("remark", "备注"));
            List<CrmModelFiledVO> contactsFieldList = crmFieldService.queryField(3);
            Map<String, String> contactsMap = contactsFieldList.stream().collect(Collectors.toMap(CrmModelFiledVO::getFieldName, CrmModelFiledVO::getName));
            contactsList.forEach(contactsField -> {
                if (contactsMap.containsKey(contactsField.getFieldName())) {
                    contactsField.setName(contactsMap.get(contactsField.getFieldName()));
                }
            });
            List<CrmModelFiledVO> contactsFixedFieldList = contactsFieldList.stream().filter(field -> field.getFieldType() == 0).collect(Collectors.toList());
            contactsList.addAll(contactsFixedFieldList);
            crmPrintFieldVO.setContacts(contactsList);
            return crmPrintFieldVO;
        } else if (crmEnum == CrmEnum.RECEIVABLES) {
            List<CrmModelFiledVO> receivablesList = new ArrayList<>();
            receivablesList.add(new CrmModelFiledVO("number", "回款编号"));
            receivablesList.add(new CrmModelFiledVO("customer_name", "客户名称"));
            receivablesList.add(new CrmModelFiledVO("plan_num", "期数"));
            receivablesList.add(new CrmModelFiledVO("return_time", "回款日期"));
            receivablesList.add(new CrmModelFiledVO("money", "回款金额"));
            receivablesList.add(new CrmModelFiledVO("return_type", "回款方式"));
            receivablesList.add(new CrmModelFiledVO("owner_user_name", "负责人"));
            receivablesList.add(new CrmModelFiledVO("remark", "备注"));
            receivablesList.add(new CrmModelFiledVO("create_user_name", "创建人"));
            receivablesList.add(new CrmModelFiledVO("create_time", "创建时间"));
            receivablesList.add(new CrmModelFiledVO("update_time", "更新时间"));
            List<CrmModelFiledVO> receivablesFieldList = crmFieldService.queryField(crmType);
            Map<String, String> receivablesMap = receivablesFieldList.stream().collect(Collectors.toMap(CrmModelFiledVO::getFieldName, CrmModelFiledVO::getName));
            receivablesList.forEach(receivablesField -> {
                if (receivablesMap.containsKey(receivablesField.getFieldName())) {
                    receivablesField.setName(receivablesMap.get(receivablesField.getFieldName()));
                }
            });
            List<CrmModelFiledVO> receivablesFixedFieldList = receivablesFieldList.stream().filter(field -> field.getFieldType() == 0).collect(Collectors.toList());
            receivablesList.addAll(receivablesFixedFieldList);
            CrmPrintFieldVO crmPrintFieldVO = new CrmPrintFieldVO();
            crmPrintFieldVO.setReceivables(receivablesList);
            List<CrmModelFiledVO> contractList = new ArrayList<>();
            contractList.add(new CrmModelFiledVO("num", "合同编号"));
            contractList.add(new CrmModelFiledVO("name", "合同名称"));
            contractList.add(new CrmModelFiledVO("money", "合同金额"));
            contractList.add(new CrmModelFiledVO("order_date", "下单时间"));
            contractList.add(new CrmModelFiledVO("start_time", "合同开始时间"));
            contractList.add(new CrmModelFiledVO("end_time", "合同结束时间"));
            contractList.add(new CrmModelFiledVO("contacts_name", "客户签约人"));
            contractList.add(new CrmModelFiledVO("company_user_name", "公司签约人"));
            contractList.add(new CrmModelFiledVO("remark", "备注"));
            List<CrmModelFiledVO> contractFieldList = crmFieldService.queryField(6);
            Map<String, String> contractMap = contractFieldList.stream().collect(Collectors.toMap(CrmModelFiledVO::getFieldName, CrmModelFiledVO::getName));
            contractList.forEach(productField -> {
                if (contractMap.containsKey(productField.getFieldName())) {
                    productField.setName(contractMap.get(productField.getFieldName()));
                }
            });
            List<CrmModelFiledVO> contractFixedFieldList = contractFieldList.stream().filter(field -> field.getFieldType() == 0).collect(Collectors.toList());
            contractList.addAll(contractFixedFieldList);
            crmPrintFieldVO.setContract(contractList);
            return crmPrintFieldVO;
        }
        throw new CrmException(SystemCodeEnum.SYSTEM_NO_VALID);
    }

    private void getCustomerFields(CrmPrintFieldVO kv) {
        List<CrmModelFiledVO> customerList = new ArrayList<>();
        customerList.add(new CrmModelFiledVO("customer_name", "客户名称"));
        customerList.add(new CrmModelFiledVO("source", "客户名称"));
        customerList.add(new CrmModelFiledVO("industry", "客户名称"));
        customerList.add(new CrmModelFiledVO("level", "客户名称"));
        customerList.add(new CrmModelFiledVO("mobile", "手机"));
        customerList.add(new CrmModelFiledVO("telephone", "电话"));
        customerList.add(new CrmModelFiledVO("website", "网址"));
        customerList.add(new CrmModelFiledVO("email", "邮箱"));
        customerList.add(new CrmModelFiledVO("remark", "备注"));
        customerList.add(new CrmModelFiledVO("detail_address", "详细地址"));
        customerList.add(new CrmModelFiledVO("address", "区域"));
        List<CrmModelFiledVO> customerFieldList = crmFieldService.queryField(2);
        Map<String, String> customerMap = customerFieldList.stream().collect(Collectors.toMap(CrmModelFiledVO::getFieldName, CrmModelFiledVO::getName));
        customerList.forEach(customerField -> {
            if (customerMap.keySet().contains(customerField.getFieldName())) {
                customerField.setName(customerMap.get(customerField.getFieldName()));
            }
        });
        List<CrmModelFiledVO> customerFixedFieldList = customerFieldList.stream().filter(field -> field.getFieldType() == 0).collect(Collectors.toList());
        customerList.addAll(customerFixedFieldList);
        kv.setCustomer(customerList);
    }

    private void getProductFields(CrmPrintFieldVO kv) {
        List<CrmModelFiledVO> productList = new ArrayList<>();
        productList.add(new CrmModelFiledVO("name", "产品名称"));
        productList.add(new CrmModelFiledVO("category_name", "产品类别"));
        productList.add(new CrmModelFiledVO("unit", "单位"));
        productList.add(new CrmModelFiledVO("num", "产品编码"));
        productList.add(new CrmModelFiledVO("price", "标准价格"));
        productList.add(new CrmModelFiledVO("sales_price", "售价"));
        productList.add(new CrmModelFiledVO("sales_num", "数量"));
        productList.add(new CrmModelFiledVO("discount", "折扣"));
        productList.add(new CrmModelFiledVO("subtotal", "合计"));
        productList.add(new CrmModelFiledVO("discount_rate", "整单折扣"));
        productList.add(new CrmModelFiledVO("total_price", "产品总金额"));
        List<CrmModelFiledVO> productFieldList = crmFieldService.queryField(4);
        Map<String, String> productMap = productFieldList.stream().collect(Collectors.toMap(CrmModelFiledVO::getFieldName, CrmModelFiledVO::getName));
        productList.forEach(productField -> {
            if (productMap.containsKey(productField.getFieldName())) {
                productField.setName(productMap.get(productField.getFieldName()));
            }
        });
        List<CrmModelFiledVO> productFixedFieldList = productFieldList.stream().filter(field -> field.getFieldType() == 0).collect(Collectors.toList());
        productList.addAll(productFixedFieldList);
        kv.setProduct(productList);
    }

    /**
     * 打印
     *
     * @param templateId templateId
     * @param id         id
     * @return data
     */
    @Override
    public String print(Integer templateId, Integer id) {
        CrmPrintTemplate CrmPrintTemplate = getById(templateId);
        if (StrUtil.isEmpty(CrmPrintTemplate.getContent())) {
            throw new CrmException(CrmCodeEnum.CRM_PRINT_TEMPLATE_NOT_EXIST_ERROR);
        }
        Integer type = CrmPrintTemplate.getType();
        return replaceContent(id, CrmPrintTemplate.getContent(), type);
    }

    /**
     * 预览
     *
     * @param content content
     * @param type    type
     * @return path
     */
    @Override
    public String preview(String content, String type) {
        if (!Arrays.asList("pdf", "word").contains(type)) {
            throw new CrmException(CrmCodeEnum.CRM_PRINT_PRE_VIEW_ERROR);
        }
        if(StrUtil.isEmpty(content)){
            content = "<br/>";
        }
        String slash = BaseUtil.isWindows() ? "\\" : "/";
        String date = DateUtil.format(new Date(), "yyyyMMdd");
        String folderPath = FileUtil.getTmpDirPath() + slash + "print" + slash + date;
        String UUID = IdUtil.simpleUUID();
        String fileName = UUID + ".pdf";
        FileUtil.mkdir(folderPath + slash);
        String path = folderPath + slash + fileName;
        JSONObject object = new JSONObject();
        String html = "<html>\n" +
                "<head>\n" +
                "<style>\n" +
                "/**\n" +
                "* Copyright (c) Tiny Technologies, Inc. All rights reserved.\n" +
                "* Licensed under the LGPL or a commercial license.\n" +
                "* For LGPL see License.txt in the project root for license information.\n" +
                "* For commercial licenses see https://www.tiny.cloud/\n" +
                "*/\n" +
                "body {\n" +
                "  font-family:  simsun, serif,-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;\n" +
                "  line-height: 1.4;\n" +
                "  padding: 60px;\n" +
                "  width: 595px;\n" +
                "  margin: 0 auto;\n" +
                "  border-radius: 4px;\n" +
                "  background: white;\n" +
                "  min-height: 100%;\n" +
                "}\n" +
                "p { margin: 5px 0;\n" +
                "  line-height: 1.5;\n" +
                "}\n" +
                "table {\n" +
                "  border-collapse: collapse;\n" +
                "}\n" +
                "table th,\n" +
                "table td {\n" +
                "  border: 1px solid #ccc;\n" +
                "  padding: 0.4rem;\n" +
                "}\n" +
                "*{\n" +
                "     font-family:  simsun, serif,-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;\n" +
                "}\n" +
                "figure {\n" +
                "  display: table;\n" +
                "  margin: 1rem auto;\n" +
                "}\n" +
                "figure figcaption {\n" +
                "  color: #999;\n" +
                "  display: block;\n" +
                "  margin-top: 0.25rem;\n" +
                "  text-align: center;\n" +
                "}\n" +
                "hr {\n" +
                "  border-color: #ccc;\n" +
                "  border-style: solid;\n" +
                "  border-width: 1px 0 0 0;\n" +
                "}\n" +
                "code {\n" +
                "  background-color: #e8e8e8;\n" +
                "  border-radius: 3px;\n" +
                "  padding: 0.1rem 0.2rem;\n" +
                "}\n" +
                ".mce-content-body:not([dir=rtl]) blockquote {\n" +
                "  border-left: 2px solid #ccc;\n" +
                "  margin-left: 1.5rem;\n" +
                "  padding-left: 1rem;\n" +
                "}\n" +
                ".mce-content-body[dir=rtl] blockquote {\n" +
                "  border-right: 2px solid #ccc;\n" +
                "  margin-right: 1.5rem;\n" +
                "  padding-right: 1rem;\n" +
                "}\n" +
                "\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                content +
                "</body>\n" +
                "</html>";
        if ("pdf".equals(type)) {
            try {
                Document document = new Document(PageSize.A4);
                File file = FileUtil.file(path);
                PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(html.getBytes());
                XMLWorkerHelper.getInstance().parseXHtml(pdfWriter, document, byteArrayInputStream, StandardCharsets.UTF_8,new FontProvider());
                document.close();
                pdfWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("word".equals(type)) {
            ByteArrayInputStream bais = null;
            FileOutputStream ostream = null;
            String filePath = folderPath + slash + UUID + ".doc";
            try {
                bais = IoUtil.toUtf8Stream(html);
                POIFSFileSystem poifs = new POIFSFileSystem();
                DirectoryEntry directoryEntry = poifs.getRoot();
                directoryEntry.createDocument("WordDocument", bais);
                File file = FileUtil.file(filePath);
                object.put("word", file.getAbsolutePath());
                ostream = new FileOutputStream(file);
                poifs.writeFilesystem(ostream);
                bais.close();
                ostream.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bais != null) {
                    try {
                        bais.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (ostream != null) {
                    try {
                        ostream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Document document = null;
            PdfWriter pdfWriter = null;
            try {
                document = new Document(PageSize.A4);
                File file = FileUtil.file(path);
                pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(html.getBytes());
                XMLWorkerHelper.getInstance().parseXHtml(pdfWriter, document, byteArrayInputStream, StandardCharsets.UTF_8,new FontProvider());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (document != null) {
                    document.close();
                }
                if (pdfWriter != null) {
                    pdfWriter.close();
                }
            }
        }
        String uuid = IdUtil.simpleUUID();
        object.put("pdf", path);
        redis.setex(CrmCacheKey.CRM_PRINT_TEMPLATE_CACHE_KEY + uuid, 3600 * 24, object.toJSONString());
        return uuid;
    }

    public class FontProvider extends XMLWorkerFontProvider {
        @Override
        public Font getFont(String fontname, String encoding, boolean embedded, float size, int style, BaseColor color) {
            try {
                BaseFont baseFont = BaseFont.createFont("/fonts/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                return new Font(baseFont,size,style,color);
            } catch (Exception e) {
                log.error("本地字体引用出错",e);
            }
            return super.getFont(fontname, encoding,embedded,size,style,color);
        }
        @Override
        public Font getFont(String fontname, String encoding, float size, int style) {
            try {
                BaseFont baseFont = BaseFont.createFont("/fonts/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                return new Font(baseFont,size,style);
            } catch (Exception e) {
                log.error("本地字体引用出错",e);
            }
            return super.getFont(fontname, encoding,size,style);
        }
    }

    private String replaceContent(Integer id, String content, Integer type) {
        List<String> spanList = ReUtil.findAllGroup0("<span((?!/span>).)*data-wk-tag=\".*?\".*?</span>", content);
        List<String> tableStyleList = ReUtil.findAllGroup0("<table[^(<|>)]*>", content);
        for (String tableStyle : tableStyleList) {
            String newStyle;
            if (tableStyle.contains("float: right") || tableStyle.contains("margin-left: auto; margin-right: auto;")) {
                newStyle = tableStyle.replace(">", " align=\"right\">");
            } else {
                newStyle = tableStyle.replace(">", " align=\"left\">");
            }
            content = content.replaceAll(tableStyle, newStyle);
        }
        List<String> tableList = ReUtil.findAllGroup0("<table.*?data-wk-table-tag=\"table\".*?table>", content);
        Map<String, String> map = new HashMap<>();
        if (type.equals(CrmEnum.BUSINESS.getType())) {
            List<CrmModelFiledVO> businessFieldList = crmFieldService.queryField(5);
            Map<String, Integer> businessFieldMap = businessFieldList.stream().collect(Collectors.toMap(CrmModelFiledVO::getFieldName, CrmModelFiledVO::getType));
            List<CrmModelFiledVO> customerFieldList = crmFieldService.queryField(2);
            Map<String, Integer> customerFieldMap = customerFieldList.stream().collect(Collectors.toMap(CrmModelFiledVO::getFieldName, CrmModelFiledVO::getType));
            CrmModel record = ApplicationContextHolder.getBean(ICrmBusinessService.class).queryById(id);
            CrmModel customer = ApplicationContextHolder.getBean(ICrmCustomerService.class).queryById((Integer) record.get("customerId"),null);
            List<JSONObject> businessProductList = getBaseMapper().queryBusinessProduct(id);
            spanList.forEach(span -> {
                String key = ReUtil.getGroup1("<span.*?data-wk-tag=\"(.*?)\".*?</span>", span);
                String model = ReUtil.getGroup1("^([A-Za-z]*)\\.", key);
                String fieldName = ReUtil.delFirst("^[A-Za-z]*\\.", key);
                fieldName = StrUtil.toCamelCase(fieldName);
                String value = "";
                if ("business".equals(model)) {
                    if (businessFieldMap.containsKey(fieldName)) {
                        Integer formType = businessFieldMap.get(fieldName);
                        getName(record, fieldName, formType);
                    }
                    if (ObjectUtil.isEmpty(record.get(fieldName))) {
                        value = "";
                    } else if (record.get(fieldName) instanceof Date) {
                        value = DateUtil.formatDateTime((Date) record.get(fieldName));
                    } else {
                        value = TypeUtils.castToString(record.get(fieldName));
                    }
                } else if ("customer".equals(model)) {
                    if (customerFieldMap.containsKey(fieldName)) {
                        Integer formType = customerFieldMap.get(fieldName);
                        getName(customer, fieldName, formType);
                    }
                    if (ObjectUtil.isEmpty(customer.get(fieldName))) {
                        value = "";
                    } else if (customer.get(fieldName) instanceof Date) {
                        value = DateUtil.formatDateTime((Date) customer.get(fieldName));
                    } else {
                        value = TypeUtils.castToString(customer.get(fieldName));
                    }
                }
                map.put(key, value);
            });
            content = appendTableContent(content, tableList, businessProductList);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String k = entry.getKey();
                String v = StrUtil.isNotEmpty(entry.getValue()) ? entry.getValue() : "";
                content = content.replaceAll("(<span((?!/span>).)*data-wk-tag=\"" + k + "\".*?)(\\{.*?\\})(</span>)", "$1" + v + "$4");
            }
        } else if (type.equals(CrmEnum.CONTRACT.getType())) {
            List<CrmModelFiledVO> contractFieldList = crmFieldService.queryField(6);
            Map<String, Integer> contractFieldMap = contractFieldList.stream().collect(Collectors.toMap(CrmModelFiledVO::getFieldName, CrmModelFiledVO::getType));
            List<CrmModelFiledVO> customerFieldList = crmFieldService.queryField(2);
            Map<String, Integer> customerFieldMap = customerFieldList.stream().collect(Collectors.toMap(CrmModelFiledVO::getFieldName, CrmModelFiledVO::getType));
            List<CrmModelFiledVO> contactsFieldList = crmFieldService.queryField(3);
            Map<String, Integer> contactsFieldMap = contactsFieldList.stream().collect(Collectors.toMap(CrmModelFiledVO::getFieldName, CrmModelFiledVO::getType));
            CrmModel record = ApplicationContextHolder.getBean(ICrmContractService.class).queryById(id);
            CrmModel customer = ApplicationContextHolder.getBean(ICrmCustomerService.class).queryById((Integer) record.get("customerId"),null);
            CrmModel contacts = new CrmModel();
            if (record.get("contactsId") != null) {
                contacts = ApplicationContextHolder.getBean(ICrmContactsService.class).queryById((Integer) record.get("contactsId"));
            }
            List<JSONObject> contractProductList = getBaseMapper().queryContractProduct(id);
            for (String span : spanList) {
                String key = ReUtil.getGroup1("<span.*?data-wk-tag=\"(.*?)\".*?</span>", span);
                String model = ReUtil.getGroup1("^([A-Za-z]*)\\.", key);
                String fieldName = ReUtil.delFirst("^[A-Za-z]*\\.", key);
                fieldName = StrUtil.toCamelCase(fieldName);
                String value = "";
                if ("contract".equals(model)) {
                    if (contractFieldMap.containsKey(fieldName)) {
                        Integer formType = contractFieldMap.get(fieldName);
                        getName(record, fieldName, formType);
                    }
                    if (ObjectUtil.isEmpty(record.get(fieldName))) {
                        value = "";
                    } else if (record.get(fieldName) instanceof Date) {
                        value = DateUtil.formatDateTime((Date) record.get(fieldName));
                    } else {
                        value = TypeUtils.castToString(record.get(fieldName));
                    }
                } else if ("customer".equals(model)) {
                    if (customerFieldMap.containsKey(fieldName)) {
                        Integer formType = customerFieldMap.get(fieldName);
                        getName(customer, fieldName, formType);
                    }
                    if (ObjectUtil.isEmpty(customer.get(fieldName))) {
                        value = "";
                    } else if (customer.get(fieldName) instanceof Date) {
                        value = DateUtil.formatDateTime((Date) customer.get(fieldName));
                    } else {
                        value = TypeUtils.castToString(customer.get(fieldName));
                    }
                } else if ("contacts".equals(model)) {
                    if (contactsFieldMap.containsKey(fieldName)) {
                        Integer formType = contactsFieldMap.get(fieldName);
                        getName(contacts, fieldName, formType);
                    }
                    if (ObjectUtil.isEmpty(contacts.get(fieldName))) {
                        value = "";
                    } else if (contacts.get(fieldName) instanceof Date) {
                        value = DateUtil.formatDateTime((Date) contacts.get(fieldName));
                    } else {
                        value = TypeUtils.castToString(contacts.get(fieldName));
                    }
                }
                map.put(key, value);
            }
            content = appendTableContent(content, tableList, contractProductList);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String k = entry.getKey();
                String v = StrUtil.isNotEmpty(entry.getValue()) ? entry.getValue() : "";
                content = content.replaceAll("(<span((?!/span>).)*data-wk-tag=\"" + k + "\".*?)(\\{.*?\\})(</span>)", "$1" + v + "$4");
            }
        } else if (type.equals(CrmEnum.RECEIVABLES.getType())) {
            List<CrmModelFiledVO> receivablesFieldList = crmFieldService.queryField(7);
            Map<String, Integer> receivablesFieldMap = receivablesFieldList.stream().collect(Collectors.toMap(CrmModelFiledVO::getFieldName, CrmModelFiledVO::getType));
            List<CrmModelFiledVO> contractFieldList = crmFieldService.queryField(6);
            Map<String, Integer> contractFieldMap = contractFieldList.stream().collect(Collectors.toMap(CrmModelFiledVO::getFieldName, CrmModelFiledVO::getType));
            CrmModel record = ApplicationContextHolder.getBean(ICrmReceivablesService.class).queryById(id);
            CrmModel contract = ApplicationContextHolder.getBean(ICrmContractService.class).queryById((Integer) record.get("contractId"));
            spanList.forEach(span -> {
                String key = ReUtil.getGroup1("<span.*?data-wk-tag=\"(.*?)\".*?</span>", span);
                String model = ReUtil.getGroup1("^([A-Za-z]*)\\.", key);
                String fieldName = ReUtil.delFirst("^[A-Za-z]*\\.", key);
                fieldName = StrUtil.toCamelCase(fieldName);
                String value = "";
                if ("receivables".equals(model)) {
                    if (receivablesFieldMap.containsKey(fieldName)) {
                        Integer formType = receivablesFieldMap.get(fieldName);
                        getName(record, fieldName, formType);
                    }
                    if (ObjectUtil.isEmpty(record.get(fieldName))) {
                        value = "";
                    } else if (record.get(fieldName) instanceof Date) {
                        value = DateUtil.formatDateTime((Date) record.get(fieldName));
                    } else {
                        value = TypeUtils.castToString(record.get(fieldName));
                    }
                } else if ("contract".equals(model)) {
                    if (contractFieldMap.containsKey(fieldName)) {
                        Integer formType = contractFieldMap.get(fieldName);
                        getName(contract, fieldName, formType);
                    }
                    if (ObjectUtil.isEmpty(contract.get(fieldName))) {
                        value = "";
                    } else if (contract.get(fieldName) instanceof Date) {
                        value = DateUtil.formatDateTime((Date) contract.get(fieldName));
                    } else {
                        value = TypeUtils.castToString(contract.get(fieldName));
                    }
                }
                map.put(key, value);
            });
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String k = entry.getKey();
                String v = StrUtil.isNotEmpty(entry.getValue()) ? entry.getValue() : "";
                content = content.replaceAll("(<span((?!/span>).)*data-wk-tag=\"" + k + "\".*?)(\\{.*?\\})(</span>)", "$1" + v + "$4");
            }
        }
        return content;
    }

    private void getName(Map<String, Object> record, String fieldName, Integer formType) {
        Object value = record.get(fieldName);
        if (formType.equals(FieldEnum.USER.getType())) {
            if (value instanceof Long) {
                record.put(fieldName, UserCacheUtil.getUserName((Long) value));
            } else if (value instanceof String) {
                List<SimpleUser> data = adminService.queryUserByIds(StrUtil.splitTrim((CharSequence) value, Const.SEPARATOR).stream().map(Long::valueOf).collect(Collectors.toList())).getData();
                record.put(fieldName, data.stream().map(SimpleUser::getRealname).collect(Collectors.joining(Const.SEPARATOR)));
            }
        } else if (formType.equals(FieldEnum.STRUCTURE.getType())) {
            if (value instanceof Integer) {
                record.put(fieldName, adminService.queryDeptName((Integer) value).getData());
            } else if (value instanceof String) {
                List<SimpleDept> data = adminService.queryDeptByIds(StrUtil.splitTrim((CharSequence) value, Const.SEPARATOR).stream().map(Integer::valueOf).collect(Collectors.toList())).getData();
                record.put(fieldName, data.stream().map(SimpleDept::getName).collect(Collectors.joining(Const.SEPARATOR)));
            }
        } else if (formType.equals(FieldEnum.FILE.getType())) {
            if (ObjectUtil.isNotEmpty(value)){
                List<FileEntity> data = ApplicationContextHolder.getBean(AdminFileService.class).queryFileList((String) value).getData();
                record.put(fieldName, data.stream().map(FileEntity::getName).collect(Collectors.joining(Const.SEPARATOR)));
            }else {
                record.put(fieldName, "");
            }

        }
    }

    private String appendTableContent(String content, List<String> tableList, List<JSONObject> productList) {
        List<CrmModelFiledVO> productFieldList = crmFieldService.queryField(7);
        Map<String, Integer> productFieldMap = productFieldList.stream().collect(Collectors.toMap(CrmModelFiledVO::getFieldName, CrmModelFiledVO::getType));
        for (String table : tableList) {
            String tableMid = ReUtil.getGroup0("<tr((?!header).)*data-wk-table-tr-tag=\"value\">.*?</tr>?", table);
            List<String> spanList = ReUtil.findAllGroup0("<span.*?data-wk-table-value-tag=\".*?\".*?</span>", tableMid);
            StringBuilder tableContent = new StringBuilder();
            ICrmProductService productService = ApplicationContextHolder.getBean(ICrmProductService.class);
            for (JSONObject orderProduct : productList) {
                CrmModel product = productService.queryById(orderProduct.getInteger("productId"));
                orderProduct.putAll(product);
                Map<String, String> productMap = new HashMap<>();
                spanList.forEach(span -> {
                    String tableKey = ReUtil.getGroup1("<span.*?data-wk-table-value-tag=\"(.*?)\".*?</span>", span);
                    String fieldName = ReUtil.delFirst("^[A-Za-z]*\\.", tableKey);
                    fieldName = StrUtil.toCamelCase(fieldName);
                    if (productFieldMap.containsKey(orderProduct.getString(fieldName))) {
                        Integer formType = productFieldMap.get(fieldName);
                        getName(orderProduct, fieldName, formType);
                    }
                    if ("categoryId".equals(fieldName)){
                        fieldName = "categoryName";
                    }
                    String value = orderProduct.getString(fieldName);
                    productMap.put(tableKey, value);
                });
                String newTableMid = tableMid;
                for (Map.Entry<String, String> entry : productMap.entrySet()) {
                    String k = entry.getKey();
                    String v = StrUtil.isNotEmpty(entry.getValue()) ? entry.getValue() : "";
                    newTableMid = newTableMid.replaceAll("(<span((?!/span>).)*data-wk-table-value-tag=\"" + k + "\".*?)(\\{.*?\\})(</span>)", "$1" + v + "$4");
                }
                tableContent.append(newTableMid);
            }
            tableContent = new StringBuilder(table.replaceAll("<tr.*?data-wk-table-tr-tag=\"value\">[\\s\\S]*</tr>?", tableContent.toString()));
            content = content.replace(table, tableContent.toString());
        }
        return content;
    }

    /**
     * 复制模板
     * @param templateId templateId
     */
    @Override
    public void copyTemplate(Integer templateId) {
        CrmPrintTemplate crmPrintTemplate = getById(templateId);
        String name = crmPrintTemplate.getTemplateName();
        String pre = ReUtil.delFirst("[(]\\d+[)]$", name);
        List<String> nameList;
        if (!ReUtil.contains("^[(]\\d+[)]$", name)) {
            nameList = query().like("template_name", pre).list().stream().map(CrmPrintTemplate::getTemplateName).collect(Collectors.toList());

        } else {
            nameList = query().last("  where template_name regexp '^[(]\\d+[)]$'").list().stream().map(CrmPrintTemplate::getTemplateName).collect(Collectors.toList());
        }
        StringBuilder numberSb = new StringBuilder();
        for (String dbName : nameList) {
            String endCode = ReUtil.get("[(]\\d+[)]$", dbName, 0);
            if (endCode != null) {
                numberSb.append(endCode);
            }
        }
        int i = 1;
        if (numberSb.length() == 0) {
            while (numberSb.toString().contains("(" + i + ")")) {
                i++;
            }
        }
        crmPrintTemplate.setTemplateName(pre + "(" + i + ")");
        crmPrintTemplate.setTemplateId(null);
        save(crmPrintTemplate);
    }

    /**
     * 保存打印记录
     * @param crmPrintRecord record
     */
    @Override
    public void savePrintRecord(CrmPrintRecord crmPrintRecord) {
        Integer crmType = getById(crmPrintRecord.getTemplateId()).getType();
        crmPrintRecord.setCrmType(crmType).setCreateTime(new Date()).setCreateUserId(UserUtil.getUserId());
        crmPrintRecordService.save(crmPrintRecord);
    }

    /**
     * 查询打印记录
     * @param crmType crm类型
     * @param typeId 类型ID
     */
    @Override
    public List<CrmPrintRecord> queryPrintRecord(Integer crmType, Integer typeId) {
        List<CrmPrintRecord> crmPrintRecords = getBaseMapper().queryPrintRecord(crmType, typeId);
        crmPrintRecords.forEach(record->{
            record.setCreateUserName(UserCacheUtil.getUserName(record.getCreateUserId()));
        });
        return crmPrintRecords;
    }

    /**
     * 根据ID查询
     * @param recordId recordId
     * @return data
     */
    @Override
    public CrmPrintRecord queryPrintRecordById(Integer recordId) {
        return crmPrintRecordService.getById(recordId);
    }
}
