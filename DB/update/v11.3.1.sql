SET FOREIGN_KEY_CHECKS=0;

ALTER TABLE `wk_crm_field` ADD COLUMN `style_percent` int(3) NULL DEFAULT 50 COMMENT '样式百分比%';
ALTER TABLE `wk_crm_field` ADD COLUMN `precisions` int(2) NULL COMMENT '精度，允许的最大小数位' AFTER `style_percent`;
ALTER TABLE `wk_crm_field` ADD COLUMN `form_position` varchar(10) NULL COMMENT '表单定位 坐标格式： 1,1' AFTER `precisions`;
ALTER TABLE `wk_crm_field` ADD COLUMN `max_num_restrict` varchar(20) NULL COMMENT '限制的最大数值' AFTER `form_position`;
ALTER TABLE `wk_crm_field` ADD COLUMN `min_num_restrict` varchar(20) NULL COMMENT '限制的最小数值' AFTER `max_num_restrict`;

ALTER TABLE `wk_oa_examine_field` ADD COLUMN `style_percent` int(3) NULL DEFAULT 50 COMMENT '样式百分比%';
ALTER TABLE `wk_oa_examine_field` ADD COLUMN `precisions` int(2) NULL COMMENT '精度，允许的最大小数位' AFTER `style_percent`;
ALTER TABLE `wk_oa_examine_field` ADD COLUMN `form_position` varchar(10) NULL COMMENT '表单定位 坐标格式： 1,1' AFTER `precisions`;
ALTER TABLE `wk_oa_examine_field` ADD COLUMN `max_num_restrict` varchar(20) NULL COMMENT '限制的最大数值' AFTER `form_position`;
ALTER TABLE `wk_oa_examine_field` ADD COLUMN `min_num_restrict` varchar(20) NULL COMMENT '限制的最小数值' AFTER `max_num_restrict`;

ALTER TABLE `wk_crm_field` MODIFY COLUMN `default_value` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '默认值' AFTER `max_length`;

ALTER TABLE `wk_oa_examine_field` MODIFY COLUMN `default_value` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '默认值' AFTER `max_length`;

ALTER TABLE `wk_admin_user` ADD COLUMN `is_del` int(1) NOT NULL DEFAULT 0 COMMENT '是否删除 0 未删除 1 已删除';

ALTER TABLE `wk_admin_dept` ADD COLUMN `owner_user_id` bigint(20) NULL COMMENT '部门负责人' AFTER `remark`;

INSERT INTO `wk_admin_menu`(`menu_id`, `parent_id`, `menu_name`, `realm`, `realm_url`, `realm_module`, `menu_type`, `sort`, `status`, `remarks`) VALUES (928, 440, '导入', 'excelimport', '/crmInstrument/importRecordList', NULL, 3, 5, 1, NULL);
INSERT INTO `wk_admin_menu`(`menu_id`, `parent_id`, `menu_name`, `realm`, `realm_url`, `realm_module`, `menu_type`, `sort`, `status`, `remarks`) VALUES (929, 440, '导出', 'excelexport', '/crmInstrument/exportRecordList', NULL, 3, 6, 1, NULL);



UPDATE `wk_admin_menu` SET `parent_id` = 440, `menu_name` = '查看', `realm` = 'read', `realm_url` = '/crmInstrument/queryRecordList', `realm_module` = NULL, `menu_type` = 3, `sort` = 1, `status` = 1, `remarks` = NULL WHERE `menu_id` = 441;
UPDATE `wk_admin_menu` SET `parent_id` = 440, `menu_name` = '新建', `realm` = 'save', `realm_url` = '/crmActivity/addCrmActivityRecord', `realm_module` = NULL, `menu_type` = 3, `sort` = 2, `status` = 1, `remarks` = NULL WHERE `menu_id` = 442;
UPDATE `wk_admin_menu` SET `parent_id` = 440, `menu_name` = '编辑', `realm` = 'update', `realm_url` = '/crmActivity/updateActivityRecord', `realm_module` = NULL, `menu_type` = 3, `sort` = 3, `status` = 1, `remarks` = NULL WHERE `menu_id` = 443;
UPDATE `wk_admin_menu` SET `parent_id` = 440, `menu_name` = '删除', `realm` = 'delete', `realm_url` = '/crmActivity/deleteCrmActivityRecord/*', `realm_module` = NULL, `menu_type` = 3, `sort` = 4, `status` = 1, `remarks` = NULL WHERE `menu_id` = 444;


ALTER TABLE `wk_oa_examine_field` 
MODIFY COLUMN `operating` int(1) NULL DEFAULT 255 COMMENT '是否可以删除修改' AFTER `options`;

ALTER TABLE `wk_crm_field` 
MODIFY COLUMN `operating` int(1) NULL DEFAULT 255 COMMENT '是否可以删除修改' AFTER `options`;
UPDATE `wk_crm_field` SET `style_percent` = 50;

UPDATE `wk_crm_field` SET `operating` = 255 WHERE field_type='0';
UPDATE `wk_crm_field` SET `operating` = 189 WHERE label='1' and field_name='leads_name';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='1' and field_name='source';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='1' and field_name='mobile';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='1' and field_name='telephone';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='1' and field_name='email';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='1' and field_name='industry';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='1' and field_name='level';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='1' and field_name='address';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='1' and field_name='remark';
UPDATE `wk_crm_field` SET `operating` = 63 WHERE label='1' and field_name='next_time';


UPDATE `wk_crm_field` SET `operating` = 189 WHERE label='2' and field_name='customer_name';
UPDATE `wk_crm_field` SET `operating` = 63 WHERE label='2' and field_name='level';
UPDATE `wk_crm_field` SET `operating` = 63 WHERE label='2' and field_name='next_time';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='2' and field_name='industry';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='2' and field_name='source';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='2' and field_name='mobile';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='2' and field_name='website';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='2' and field_name='email';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='2' and field_name='remark';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='2' and field_name='telephone';




UPDATE `wk_crm_field` SET `operating` = 181 WHERE label='3' and field_name='name';
UPDATE `wk_crm_field` SET `operating` = 159 WHERE label='3' and field_name='customer_id';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='3' and field_name='post';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='3' and field_name='sex';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='3' and field_name='mobile';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='3' and field_name='telephone';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='3' and field_name='email';
UPDATE `wk_crm_field` SET `operating` = 190 WHERE label='3' and field_name='policymakers';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='3' and field_name='address';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='3' and field_name='next_time';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='3' and field_name='remark';

UPDATE `wk_crm_field` SET `operating` = 177 WHERE label='4' and field_name='name';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='4' and field_name='unit';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='4' and field_name='num';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='4' and field_name='description';
UPDATE `wk_crm_field` SET `operating` = 181 WHERE label='4' and field_name='price';


UPDATE `wk_crm_field` SET `operating` = 181 WHERE label='5' and field_name='business_name';
UPDATE `wk_crm_field` SET `operating` = 149 WHERE label='5' and field_name='customer_id';
UPDATE `wk_crm_field` SET `operating` = 189 WHERE label='5' and field_name='money';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='5' and field_name='deal_date';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='5' and field_name='remark';


UPDATE `wk_crm_field` SET `operating` = 177 WHERE label='6' and field_name='num';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='6' and field_name='name';
UPDATE `wk_crm_field` SET `operating` = 149 WHERE label='6' and field_name='customer_id';
UPDATE `wk_crm_field` SET `operating` = 159 WHERE label='6' and field_name='business_id';
UPDATE `wk_crm_field` SET `operating` = 181 WHERE label='6' and field_name='order_date';
UPDATE `wk_crm_field` SET `operating` = 189 WHERE label='6' and field_name='money';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='6' and field_name='start_time';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='6' and field_name='end_time';
UPDATE `wk_crm_field` SET `operating` = 159 WHERE label='6' and field_name='contacts_id';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='6' and field_name='company_user_id';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='6' and field_name='remark';

UPDATE `wk_crm_field` SET `operating` = 177 WHERE label='7' and field_name='number';
UPDATE `wk_crm_field` SET `operating` = 159 WHERE label='7' and field_name='contract_id';
UPDATE `wk_crm_field` SET `operating` = 149 WHERE label='7' and field_name='customer_id';
UPDATE `wk_crm_field` SET `operating` = 181 WHERE label='7' and field_name='return_time';
UPDATE `wk_crm_field` SET `operating` = 181 WHERE label='7' and field_name='money';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='7' and field_name='return_type';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='7' and field_name='remark';

UPDATE `wk_crm_field` SET `operating` = 177 WHERE label='17' and field_name='visit_number';
UPDATE `wk_crm_field` SET `operating` = 181 WHERE label='17' and field_name='visit_time';
UPDATE `wk_crm_field` SET `operating` = 149 WHERE label='17' and field_name='owner_user_id';
UPDATE `wk_crm_field` SET `operating` = 149 WHERE label='17' and field_name='customer_id';
UPDATE `wk_crm_field` SET `operating` = 159 WHERE label='17' and field_name='contacts_id';
UPDATE `wk_crm_field` SET `operating` = 159 WHERE label='17' and field_name='contract_id';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='17' and field_name='return_visit_type';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='17' and field_name='satisficing';
UPDATE `wk_crm_field` SET `operating` = 191 WHERE label='17' and field_name='flied_itvzix';

SET FOREIGN_KEY_CHECKS=1;