SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `wk_oa_log_user_favour`;
CREATE TABLE `wk_oa_log_user_favour`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `log_id` int(11) NOT NULL COMMENT '日志id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id`, `log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户点赞日志关系表 ' ROW_FORMAT = Dynamic;

ALTER TABLE `wk_crm_field` ADD COLUMN `form_assist_id` int(12) NULL COMMENT '表单辅助id，前端生成' AFTER `min_num_restrict`;

ALTER TABLE `wk_oa_examine_field` ADD COLUMN `form_assist_id` int(12) NULL COMMENT '表单辅助id，前端生成' AFTER `min_num_restrict`;


DROP TABLE IF EXISTS `wk_crm_field_extend`;
CREATE TABLE `wk_crm_field_extend`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_field_id` int(11) NOT NULL COMMENT '对应主字段id',
  `field_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '自定义字段英文标识',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '字段名称',
  `type` int(2) NOT NULL DEFAULT 1 COMMENT '字段类型 1 单行文本 2 多行文本 3 单选 4日期 5 数字 6 小数 7 手机  8 文件 9 多选 10 人员 11 附件 12 部门 13 日期时间 14 邮箱 15客户 16 商机 17 联系人 18 地图 19 产品类型 20 合同 21 回款计划',
  `remark` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段说明',
  `input_tips` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '输入提示',
  `max_length` int(12) NULL DEFAULT NULL COMMENT '最大长度',
  `default_value` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '默认值',
  `is_unique` int(1) NULL DEFAULT 0 COMMENT '是否唯一 1 是 0 否',
  `is_null` int(1) NULL DEFAULT 0 COMMENT '是否必填 1 是 0 否',
  `sorting` int(5) NULL DEFAULT 1 COMMENT '排序 从小到大',
  `options` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '如果类型是选项，此处不能为空，多个选项以，隔开',
  `operating` int(1) NULL DEFAULT 255 COMMENT '是否允许编辑',
  `is_hidden` int(1) NOT NULL DEFAULT 0 COMMENT '是否隐藏  0不隐藏 1隐藏',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后修改时间',
  `field_type` int(2) NOT NULL DEFAULT 0 COMMENT '字段来源  0.自定义 1.原始固定 2原始字段但值存在扩展表中',
  `style_percent` int(3) NULL DEFAULT 50 COMMENT '样式百分比%',
  `precisions` int(2) NULL DEFAULT NULL COMMENT '精度，允许的最大小数位',
  `form_position` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表单定位 坐标格式： 1,1',
  `max_num_restrict` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '限制的最大数值',
  `min_num_restrict` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '限制的最小数值',
  `form_assist_id` int(12) NULL DEFAULT NULL COMMENT '表单辅助id，前端生成',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `update_time`(`update_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12231 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '自定义字段表' ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `wk_oa_examine_field_extend`;
CREATE TABLE `wk_oa_examine_field_extend`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_field_id` int(11) NOT NULL COMMENT '对应主字段id',
  `field_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '自定义字段英文标识',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '字段名称',
  `type` int(2) NOT NULL DEFAULT 1 COMMENT '字段类型 1 单行文本 2 多行文本 3 单选 4日期 5 数字 6 小数 7 手机  8 文件 9 多选 10 人员 11 附件 12 部门 13 日期时间 14 邮箱 15客户 16 商机 17 联系人 18 地图 19 产品类型 20 合同 21 回款计划',
  `remark` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段说明',
  `input_tips` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '输入提示',
  `max_length` int(12) NULL DEFAULT NULL COMMENT '最大长度',
  `default_value` varchar(3000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '默认值',
  `is_unique` int(1) NULL DEFAULT 0 COMMENT '是否唯一 1 是 0 否',
  `is_null` int(1) NULL DEFAULT 0 COMMENT '是否必填 1 是 0 否',
  `sorting` int(5) NULL DEFAULT 1 COMMENT '排序 从小到大',
  `options` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '如果类型是选项，此处不能为空，多个选项以，隔开',
  `operating` int(1) NULL DEFAULT 255 COMMENT '是否可以删除修改',
  `is_hidden` int(1) NOT NULL DEFAULT 0 COMMENT '是否隐藏  0不隐藏 1隐藏',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后修改时间',
  `field_type` int(2) NOT NULL DEFAULT 0 COMMENT '字段来源  0.自定义 1.原始固定 2原始字段但值存在扩展表中',
  `style_percent` int(3) NULL DEFAULT 50 COMMENT '样式百分比%',
  `precisions` int(2) NULL DEFAULT NULL COMMENT '精度，允许的最大小数位',
  `form_position` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表单定位 坐标格式： 1,1',
  `max_num_restrict` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '限制的最大数值',
  `min_num_restrict` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '限制的最小数值',
  `form_assist_id` int(12) NULL DEFAULT NULL COMMENT '表单辅助id，前端生成',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5645 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '自定义字段表' ROW_FORMAT = Dynamic;


DELETE FROM `wk_crm_field` WHERE label ='18';

INSERT INTO `wk_crm_field` (
SELECT NULL AS field_id,'invoice_apply_number' AS field_name,'发票申请编号' AS NAME,1 AS type,18 AS label,NULL AS remark,NULL AS input_tips,NULL AS max_length,'' AS default_value,1 AS is_unique,1 AS is_null,0 AS sorting,NULL AS OPTIONS,176 AS operating,0 AS is_hidden,now() AS update_time,1 AS field_type,NULL AS relevant,50 AS style_percent,NULL AS precisions,'0,0' AS form_position,NULL AS max_num_restrict,NULL AS min_num_restrict,NULL AS form_assist_id FROM wk_crm_field LIMIT 1);

INSERT INTO `wk_crm_field` (
SELECT NULL AS field_id,'customer_id' AS field_name,'客户名称' AS NAME,15 AS type,18 AS label,NULL AS remark,NULL AS input_tips,NULL AS max_length,'' AS default_value,0 AS is_unique,1 AS is_null,1 AS sorting,NULL AS OPTIONS,148 AS operating,0 AS is_hidden,now() AS update_time,1 AS field_type,NULL AS relevant,50 AS style_percent,NULL AS precisions,'0,1' AS form_position,NULL AS max_num_restrict,NULL AS min_num_restrict,NULL AS form_assist_id FROM wk_crm_field LIMIT 1);

INSERT INTO `wk_crm_field` (
SELECT NULL AS field_id,'contract_id' AS field_name,'合同编号' AS NAME,20 AS type,18 AS label,NULL AS remark,NULL AS input_tips,NULL AS max_length,'' AS default_value,0 AS is_unique,1 AS is_null,2 AS sorting,NULL AS OPTIONS,148 AS operating,0 AS is_hidden,now() AS update_time,1 AS field_type,NULL AS relevant,50 AS style_percent,NULL AS precisions,'1,0' AS form_position,NULL AS max_num_restrict,NULL AS min_num_restrict,NULL AS form_assist_id FROM wk_crm_field limit 1);


INSERT INTO `wk_crm_field` (
SELECT NULL AS field_id,'contract_money' AS field_name,'合同金额' AS NAME,6 AS type,18 AS label,NULL AS remark,NULL AS input_tips,NULL AS max_length,'' AS default_value,0 AS is_unique,0 AS is_null,3 AS sorting,NULL AS OPTIONS,144 AS operating,0 AS is_hidden,now() AS update_time,1 AS field_type,NULL AS relevant,50 AS style_percent,2 AS precisions,'1,1' AS form_position,NULL AS max_num_restrict,NULL AS min_num_restrict,NULL AS form_assist_id FROM wk_crm_field limit 1);

INSERT INTO `wk_crm_field` (
SELECT NULL AS field_id,'invoice_money' AS field_name,'开票金额' AS NAME,6 AS type,18 AS label,NULL AS remark,NULL AS input_tips,NULL AS max_length,'' AS default_value,0 AS is_unique,1 AS is_null,4 AS sorting,NULL AS OPTIONS,148 AS operating,0 AS is_hidden,now() AS update_time,1 AS field_type,NULL AS relevant,50 AS style_percent,2 AS precisions,'2,0' AS form_position,NULL AS max_num_restrict,NULL AS min_num_restrict,NULL AS form_assist_id FROM wk_crm_field limit 1);

INSERT INTO `wk_crm_field` (
SELECT NULL AS field_id,'invoice_date' AS field_name,'开票日期' AS NAME,13 AS type,18 AS label,NULL AS remark,NULL AS input_tips,NULL AS max_length,'' AS default_value,0 AS is_unique,0 AS is_null,5 AS sorting,NULL AS OPTIONS,190 AS operating,0 AS is_hidden,now() AS update_time,1 AS field_type,NULL AS relevant,50 AS style_percent,NULL AS precisions,'2,1' AS form_position,NULL AS max_num_restrict,NULL AS min_num_restrict,NULL AS form_assist_id FROM wk_crm_field limit 1);

INSERT INTO `wk_crm_field` (
SELECT NULL AS field_id,'invoice_type' AS field_name,'开票类型' AS NAME,3 AS type,18 AS label,NULL AS remark,NULL AS input_tips,NULL AS max_length,'' AS default_value,0 AS is_unique,1 AS is_null,6 AS sorting,'增值税专用发票,增值税普通发票,国税通用机打发票,地税通用机打发票,收据' AS OPTIONS,158 AS operating,0 AS is_hidden,now() AS update_time,1 AS field_type,NULL AS relevant,50 AS style_percent,NULL AS precisions,'3,0' AS form_position,NULL AS max_num_restrict,NULL AS min_num_restrict,NULL AS form_assist_id FROM wk_crm_field limit 1);

INSERT INTO `wk_crm_field` (
SELECT NULL AS field_id,'remark' AS field_name,'备注' AS NAME,2 AS type,18 AS label,NULL AS remark,NULL AS input_tips,255 AS max_length,'' AS default_value,0 AS is_unique,0 AS is_null,7 AS sorting,null AS OPTIONS,190 AS operating,0 AS is_hidden,now() AS update_time,1 AS field_type,NULL AS relevant,50 AS style_percent,NULL AS precisions,'3,1' AS form_position,NULL AS max_num_restrict,NULL AS min_num_restrict,NULL AS form_assist_id FROM wk_crm_field limit 1);

UPDATE `wk_oa_examine_field` SET `precisions` = 2 WHERE  `field_type` = '1' AND `type` = '6';

UPDATE `wk_crm_field` SET `precisions` = 2 WHERE  `field_type` = '1' AND `type` = '6';

UPDATE `wk_admin_menu` SET `realm_url` = null WHERE `menu_id` = 164;

INSERT INTO `wk_admin_menu` ( `menu_id`, `parent_id`, `menu_name`, `realm`, `realm_url`, `realm_module`, `menu_type`, `sort`, `status`, `remarks` ) VALUES ( 933, 11, '编辑团队成员', 'teamsave', '/crmContacts/addMembers,/crmContacts/updateMembers,/crmContacts/exitTeam', NULL, 3, 1, 1, NULL );

INSERT INTO `wk_admin_menu` ( `menu_id`, `parent_id`, `menu_name`, `realm`, `realm_url`, `realm_module`, `menu_type`, `sort`, `status`, `remarks` ) VALUES ( 934, 14, '编辑团队成员', 'teamsave', '/crmReceivables/addMembers,/crmReceivables/updateMembers,/crmReceivables/exitTeam', NULL, 3, 1, 1, NULL );

INSERT INTO `wk_admin_menu`( `menu_id`,`parent_id`, `menu_name`, `realm`, `menu_type`, `sort`) VALUES (935,166, '角色权限查看', 'read', 3, 8);

INSERT INTO `wk_admin_menu`(`menu_id`, `parent_id`, `menu_name`, `realm`, `realm_url`, `realm_module`, `menu_type`, `sort`, `status`, `remarks`) VALUES (932, 420, '导出', 'excelexport', '/crmInvoice/allExportExcel', NULL, 3, 1, 1, NULL);

UPDATE `wk_crm_field` SET `is_unique` = 0 WHERE label='18' and field_name in('contract_id','customer_id');

DELETE FROM wk_crm_role_field WHERE field_id in (SELECT field_id from wk_crm_field WHERE type='50');


INSERT INTO `wk_km_auth_user`
SELECT NULL AS r_id,auth_id,create_user_id AS user_id,1 AS auth,now() AS create_time,create_user_id AS create_user_id FROM (
SELECT (
SELECT COUNT(1) FROM wk_km_auth_user WHERE auth_id=a.auth_id) AS num,create_user_id,auth_id FROM wk_km_folder AS a) AS a WHERE a.num='0';


INSERT INTO `wk_km_auth_user`
SELECT NULL AS r_id,auth_id,create_user_id AS user_id,1 AS auth,now() AS create_time,create_user_id AS create_user_id FROM (
SELECT (
SELECT COUNT(1) FROM wk_km_auth_user WHERE auth_id=a.auth_id) AS num,create_user_id,auth_id FROM wk_km_document AS a) AS a WHERE a.num='0';

ALTER TABLE `wk_examine` 
ADD COLUMN `examine_init_id` bigint(20) NULL COMMENT '审批初始化Id' AFTER `examine_id`;

UPDATE `wk_examine` SET `examine_init_id` = examine_id;

DELETE FROM `wk_crm_scene` WHERE is_system='1' and name ='我参与的客户';

DELETE FROM `wk_crm_scene` WHERE is_system='1' and name ='我参与的合同';

DELETE FROM `wk_crm_scene` WHERE is_system='1' and name ='我参与的商机';

CREATE TABLE `wk_crm_team_members` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(1) NOT NULL COMMENT '类型，同crm类型',
  `type_id` int(11) NOT NULL COMMENT '对应类型主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `power` int(1) DEFAULT NULL COMMENT '1 只读 2 读写 3 负责人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `expires_time` datetime DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY (`id`),
  KEY `type` (`type`,`type_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=348 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='crm团队成员表';

INSERT INTO `wk_crm_team_members` (`type`,`type_id`,`user_id`,`power`,`create_time`,`expires_time`)
SELECT 2 AS type,a.type_id,user_id,2 AS power,now() AS create_time,NULL AS expires_time FROM (
SELECT a.customer_id AS type_id,substring_index(substring_index(a.rw_user_id,',',b.help_topic_id+1),',',-1) AS user_id FROM wk_crm_customer AS a JOIN mysql.help_topic b ON b.help_topic_id< (length(a.rw_user_id)-length(
REPLACE (a.rw_user_id,',',''))+1) WHERE a.rw_user_id !=',') AS a WHERE a.user_id !='';

INSERT INTO `wk_crm_team_members` (`type`,`type_id`,`user_id`,`power`,`create_time`,`expires_time`)
SELECT 2 AS type,a.type_id,user_id,1 AS power,now() AS create_time,NULL AS expires_time FROM (
SELECT a.customer_id AS type_id,substring_index(substring_index(a.ro_user_id,',',b.help_topic_id+1),',',-1) AS user_id FROM wk_crm_customer AS a JOIN mysql.help_topic b ON b.help_topic_id< (length(a.ro_user_id)-length(
REPLACE (a.ro_user_id,',',''))+1) WHERE a.ro_user_id !=',') AS a WHERE a.user_id !='';

INSERT INTO `wk_crm_team_members` (`type`,`type_id`,`user_id`,`power`,`create_time`,`expires_time`)
SELECT 6 AS type,a.type_id,user_id,2 AS power,now() AS create_time,NULL AS expires_time FROM (
SELECT a.contract_id AS type_id,substring_index(substring_index(a.rw_user_id,',',b.help_topic_id+1),',',-1) AS user_id FROM wk_crm_contract AS a JOIN mysql.help_topic b ON b.help_topic_id< (length(a.rw_user_id)-length(
REPLACE (a.rw_user_id,',',''))+1) WHERE a.rw_user_id !=',') AS a WHERE a.user_id !='';


INSERT INTO `wk_crm_team_members` (`type`,`type_id`,`user_id`,`power`,`create_time`,`expires_time`)
SELECT 6 AS type,a.type_id,user_id,1 AS power,now() AS create_time,NULL AS expires_time FROM (
SELECT a.contract_id AS type_id,substring_index(substring_index(a.ro_user_id,',',b.help_topic_id+1),',',-1) AS user_id FROM wk_crm_contract AS a JOIN mysql.help_topic b ON b.help_topic_id< (length(a.ro_user_id)-length(
REPLACE (a.ro_user_id,',',''))+1) WHERE a.ro_user_id !=',') AS a WHERE a.user_id !='';


INSERT INTO `wk_crm_team_members` (`type`,`type_id`,`user_id`,`power`,`create_time`,`expires_time`)
SELECT 5 AS type,a.type_id,user_id,2 AS power,now() AS create_time,NULL AS expires_time FROM (
SELECT a.business_id AS type_id,substring_index(substring_index(a.rw_user_id,',',b.help_topic_id+1),',',-1) AS user_id FROM wk_crm_business AS a JOIN mysql.help_topic b ON b.help_topic_id< (length(a.rw_user_id)-length(
REPLACE (a.rw_user_id,',',''))+1) WHERE a.rw_user_id !=',') AS a WHERE a.user_id !='';

INSERT INTO `wk_crm_team_members` (`type`,`type_id`,`user_id`,`power`,`create_time`,`expires_time`)
SELECT 5 AS type,a.type_id,user_id,1 AS power,now() AS create_time,NULL AS expires_time FROM (
SELECT a.business_id AS type_id,substring_index(substring_index(a.ro_user_id,',',b.help_topic_id+1),',',-1) AS user_id FROM wk_crm_business AS a JOIN mysql.help_topic b ON b.help_topic_id< (length(a.ro_user_id)-length(
REPLACE (a.ro_user_id,',',''))+1) WHERE a.ro_user_id !=',') AS a WHERE a.user_id !='';

CREATE TABLE `wk_admin_role_auth` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  `auth_role_id` int(11) DEFAULT NULL COMMENT '能查询的角色ID',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `role_type` (`auth_role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5449 DEFAULT CHARSET=utf8 COMMENT='角色表';

CREATE TABLE `wk_crm_invoice_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL COMMENT '字段名称',
  `value` longtext,
  `create_time` datetime NOT NULL,
  `batch_id` varchar(32) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `batch_id` (`batch_id`) USING BTREE,
  KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2250 DEFAULT CHARSET=utf8mb4 COMMENT='发票扩展字段数据表';


INSERT INTO `wk_crm_field_sort` 
SELECT null as id,null as field_id,'teamMemberIds' as field_name,'相关团队' as name,2 as label,10 as type,100 as style,24 as sort,user_id,0 as is_hide FROM wk_crm_field_sort WHERE label ='2' GROUP BY user_id;

INSERT INTO `wk_crm_field_sort` 
SELECT null as id,null as field_id,'teamMemberIds' as field_name,'相关团队' as name,3 as label,10 as type,100 as style,24 as sort,user_id,0 as is_hide FROM wk_crm_field_sort WHERE label ='3' GROUP BY user_id;

INSERT INTO `wk_crm_field_sort` 
SELECT null as id,null as field_id,'teamMemberIds' as field_name,'相关团队' as name,5 as label,10 as type,100 as style,24 as sort,user_id,0 as is_hide FROM wk_crm_field_sort WHERE label ='5' GROUP BY user_id;

INSERT INTO `wk_crm_field_sort` 
SELECT null as id,null as field_id,'teamMemberIds' as field_name,'相关团队' as name,6 as label,10 as type,100 as style,24 as sort,user_id,0 as is_hide FROM wk_crm_field_sort WHERE label ='6' GROUP BY user_id;

INSERT INTO `wk_crm_field_sort` 
SELECT null as id,null as field_id,'teamMemberIds' as field_name,'相关团队' as name,7 as label,10 as type,100 as style,24 as sort,user_id,0 as is_hide FROM wk_crm_field_sort WHERE label ='7' GROUP BY user_id;


ALTER TABLE `wk_crm_business` 
MODIFY COLUMN `ro_user_id` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '只读权限' AFTER `batch_id`,
MODIFY COLUMN `rw_user_id` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '读写权限' AFTER `ro_user_id`;

SET FOREIGN_KEY_CHECKS = 1;
