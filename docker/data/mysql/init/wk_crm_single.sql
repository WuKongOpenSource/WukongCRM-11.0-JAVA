create database wk_crm_single character set utf8mb4 collate utf8mb4_general_ci;
use wk_crm_single;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime(0) NOT NULL,
  `log_modified` datetime(0) NOT NULL,
  `ext` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ux_undo_log`(`xid`, `branch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of undo_log
-- ----------------------------

-- ----------------------------
-- Table structure for wk_admin_attention
-- ----------------------------
DROP TABLE IF EXISTS `wk_admin_attention`;
CREATE TABLE `wk_admin_attention`  (
  `attention_id` int(11) NOT NULL AUTO_INCREMENT,
  `be_user_id` bigint(20) NOT NULL COMMENT '被关注人',
  `attention_user_id` bigint(20) NOT NULL COMMENT '关注人',
  PRIMARY KEY (`attention_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通讯录用户关注表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_admin_attention
-- ----------------------------
INSERT INTO `wk_admin_attention` VALUES (1, 14773, 14773);

-- ----------------------------
-- Table structure for wk_admin_config
-- ----------------------------
DROP TABLE IF EXISTS `wk_admin_config`;
CREATE TABLE `wk_admin_config`  (
  `setting_id` int(9) NOT NULL AUTO_INCREMENT,
  `status` int(1) NOT NULL DEFAULT 0 COMMENT '状态，0:不启用 1 ： 启用',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '设置名称',
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '值',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`setting_id`) USING BTREE,
  INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 262462 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '客户规则' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_admin_config
-- ----------------------------
INSERT INTO `wk_admin_config` VALUES (262433, 1, 'expiringContractDays', '3', '合同到期提醒');
INSERT INTO `wk_admin_config` VALUES (262434, 1, 'putInPoolRemindDays', '2', NULL);
INSERT INTO `wk_admin_config` VALUES (262435, 1, 'taskExamine', '1', '任务审批');
INSERT INTO `wk_admin_config` VALUES (262436, 1, 'log', '1', '日志');
INSERT INTO `wk_admin_config` VALUES (262437, 1, 'book', '1', '通讯录');
INSERT INTO `wk_admin_config` VALUES (262438, 1, 'crm', '1', '客户管理');
INSERT INTO `wk_admin_config` VALUES (262439, 1, 'project', '1', '项目管理');
INSERT INTO `wk_admin_config` VALUES (262440, 1, 'calendar', '1', '日历');
INSERT INTO `wk_admin_config` VALUES (262441, 0, 'email', '2', '邮箱');
INSERT INTO `wk_admin_config` VALUES (262442, 0, 'knowledge', '2', '知识库');
INSERT INTO `wk_admin_config` VALUES (262443, 0, 'hrm', '2', '人力资源管理');
INSERT INTO `wk_admin_config` VALUES (262444, 0, 'jxc', '2', '进销存管理');
INSERT INTO `wk_admin_config` VALUES (262445, 0, 'call', '3', '呼叫中心');
INSERT INTO `wk_admin_config` VALUES (262446, 0, 'followRecordOption', '打电话', '跟进记录选项');
INSERT INTO `wk_admin_config` VALUES (262447, 0, 'followRecordOption', '发邮件', '跟进记录选项');
INSERT INTO `wk_admin_config` VALUES (262448, 0, 'followRecordOption', '发短信', '跟进记录选项');
INSERT INTO `wk_admin_config` VALUES (262449, 0, 'followRecordOption', '见面拜访', '跟进记录选项');
INSERT INTO `wk_admin_config` VALUES (262450, 0, 'followRecordOption', '活动', '跟进记录选项');
INSERT INTO `wk_admin_config` VALUES (262451, 0, 'logWelcomeSpeech', '蓝天是宁静的，空气是清新的，阳光是明媚的', '默认日志欢迎语');
INSERT INTO `wk_admin_config` VALUES (262452, 0, 'logWelcomeSpeech', '人生，最快乐的莫过于奋斗', '默认日志欢迎语');
INSERT INTO `wk_admin_config` VALUES (262453, 0, 'logWelcomeSpeech', '工作一天辛苦了，人生，最快乐的莫过于奋斗', '默认日志欢迎语');
INSERT INTO `wk_admin_config` VALUES (262454, 0, 'pictureSetting', NULL, '外勤签到照片上传设置');
INSERT INTO `wk_admin_config` VALUES (262455, 0, 'returnVisitRemindConfig', '7', '客户回访提醒设置');
INSERT INTO `wk_admin_config` VALUES (262456, 0, 'numberSetting', '6', '自动编号设置');
INSERT INTO `wk_admin_config` VALUES (262457, 0, 'numberSetting', '7', '自动编号设置');
INSERT INTO `wk_admin_config` VALUES (262458, 0, 'numberSetting', '17', '自动编号设置');
INSERT INTO `wk_admin_config` VALUES (262459, 0, 'numberSetting', '18', '自动编号设置');
INSERT INTO `wk_admin_config` VALUES (262460, 1, 'companyInfo', '{\"companyLogo\":\"\",\"companyName\":\"良心企业\"}', '企业LOGO配置');
INSERT INTO `wk_admin_config` VALUES (262461, 1, 'marketing', NULL, '是否开启营销活动');

-- ----------------------------
-- Table structure for wk_admin_dept
-- ----------------------------
DROP TABLE IF EXISTS `wk_admin_dept`;
CREATE TABLE `wk_admin_dept`  (
  `dept_id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) NULL DEFAULT 0 COMMENT '父级ID 顶级部门为0',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部门名称',
  `num` int(4) NULL DEFAULT NULL COMMENT '排序 越大越靠后',
  `remark` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '部门备注',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14853 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_admin_dept
-- ----------------------------
INSERT INTO `wk_admin_dept` VALUES (14852, 0, '全公司', 1, '');

-- ----------------------------
-- Table structure for wk_admin_file
-- ----------------------------
DROP TABLE IF EXISTS `wk_admin_file`;
CREATE TABLE `wk_admin_file`  (
  `file_id` bigint(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '附件名称',
  `size` bigint(20) NOT NULL COMMENT '附件大小（字节）',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件真实路径',
  `file_type` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'file' COMMENT '文件类型,file,img',
  `type` int(1) NULL DEFAULT NULL COMMENT '1 本地 2 阿里云oss',
  `source` int(1) NULL DEFAULT NULL COMMENT '来源 0 默认 1 admin 2 crm 3 work 4 oa 5 进销存 6 hrm',
  `is_public` int(1) NULL DEFAULT 0 COMMENT '1 公有访问 0 私有访问',
  `batch_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次id',
  PRIMARY KEY (`file_id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '附件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_admin_file
-- ----------------------------

-- ----------------------------
-- Table structure for wk_admin_login_log
-- ----------------------------
DROP TABLE IF EXISTS `wk_admin_login_log`;
CREATE TABLE `wk_admin_login_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_user_id` bigint(20) NOT NULL COMMENT '操作人id',
  `login_time` datetime(0) NOT NULL COMMENT '登录时间',
  `ip_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录ip地址',
  `login_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录地点',
  `device_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备类型',
  `core` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '终端内核',
  `platform` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '平台',
  `imei` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IEMI设备号',
  `auth_result` int(2) NULL DEFAULT NULL COMMENT '认证结果 1成功 2失败',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统登录日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_admin_login_log
-- ----------------------------

-- ----------------------------
-- Table structure for wk_admin_menu
-- ----------------------------
DROP TABLE IF EXISTS `wk_admin_menu`;
CREATE TABLE `wk_admin_menu`  (
  `menu_id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id` int(11) UNSIGNED NULL DEFAULT 0 COMMENT '上级菜单ID',
  `menu_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '菜单名称',
  `realm` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '权限标识',
  `realm_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限URL',
  `realm_module` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属模块',
  `menu_type` int(1) NULL DEFAULT NULL COMMENT '菜单类型  1目录 2 菜单 3 按钮 4特殊',
  `sort` int(4) UNSIGNED NULL DEFAULT 0 COMMENT '排序（同级有效）',
  `status` int(4) NULL DEFAULT 1 COMMENT '状态 1 启用 0 禁用',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单说明',
  PRIMARY KEY (`menu_id`) USING BTREE,
  INDEX `menu_id`(`menu_id`) USING BTREE,
  INDEX `parent_id`(`parent_id`) USING BTREE,
  INDEX `realm`(`realm`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 932 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '后台菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_admin_menu
-- ----------------------------
INSERT INTO `wk_admin_menu` VALUES (1, 0, '全部', 'crm', NULL, NULL, 1, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (2, 0, '全部', 'bi', NULL, NULL, 1, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (3, 0, '全部', 'manage', NULL, NULL, 1, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (5, 0, '全部', 'hrm', NULL, NULL, 1, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (6, 0, '全部', 'jxc', '', NULL, 1, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (9, 1, '线索管理', 'leads', NULL, NULL, 1, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (10, 1, '客户管理', 'customer', NULL, NULL, 1, 2, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (11, 1, '联系人管理', 'contacts', NULL, NULL, 1, 3, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (12, 1, '商机管理', 'business', NULL, NULL, 1, 4, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (13, 1, '合同管理', 'contract', NULL, NULL, 1, 5, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (14, 1, '回款管理', 'receivables', NULL, NULL, 1, 6, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (15, 1, '产品管理', 'product', NULL, NULL, 1, 7, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (17, 9, '新建', 'save', '/crmLeads/add', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (18, 9, '编辑', 'update', '/crmLeads/update', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (19, 9, '查看列表', 'index', '/crmLeads/queryPageList', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (20, 9, '查看详情', 'read', '/crmLeads/queryById/*', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (21, 9, '导入', 'excelimport', '/crmLead/downloadExcel', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (22, 9, '导出', 'excelexport', '/crmLead/allExportExcel,/crmLead/batchExportExcel', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (23, 9, '删除', 'delete', '/crmLead/deleteByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (24, 9, '转移', 'transfer', '/crmLeads/changeOwnerUser', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (25, 9, '转化', 'transform', '/crmLead/transfer', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (26, 10, '新建', 'save', '/crmCustomer/add', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (27, 10, '编辑', 'update', '/crmCustomer/update', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (28, 10, '查看列表', 'index', '/crmCustomer/queryPageList', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (29, 10, '查看详情', 'read', '/crmCustomer/queryById/*', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (30, 10, '导入', 'excelimport', '/crmCustomer/uploadExcel', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (31, 10, '导出', 'excelexport', '/crmCustomer/batchExportExcel,/crmCustomer/allExportExcel', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (32, 10, '删除', 'delete', '/crmCustomer/deleteByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (33, 10, '转移', 'transfer', '/crmCustomer/changeOwnerUser', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (34, 10, '放入公海', 'putinpool', '/crmCustomer/updateCustomerByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (35, 10, '锁定/解锁', 'lock', '/crmCustomer/lock', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (36, 10, '编辑团队成员', 'teamsave', '/crmCustomer/addMembers,/crmCustomer/updateMembers,/crmCustomer/exitTeam', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (40, 11, '新建', 'save', '/crmContacts/add', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (41, 11, '编辑', 'update', '/crmContacts/update', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (42, 11, '查看列表', 'index', '/crmContacts/queryPageList', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (43, 11, '查看详情', 'read', '/crmContacts/queryById/*', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (44, 11, '删除', 'delete', '/crmContacts/deleteByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (45, 11, '转移', 'transfer', '/crmContacts/changeOwnerUser', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (46, 12, '新建', 'save', '/crmBusiness/add', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (47, 12, '编辑', 'update', '/crmBusiness/update', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (48, 12, '查看列表', 'index', '/crmBusiness/queryPageList', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (49, 12, '查看详情', 'read', '/crmBusiness/queryById/*', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (50, 12, '删除', 'delete', '/crmBusiness/deleteByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (51, 12, '转移', 'transfer', '/crmBusiness/changeOwnerUser', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (52, 12, '编辑团队成员', 'teamsave', '/crmBusiness/addMembers,/crmBusiness/updateMembers,/crmBusiness/deleteMembers', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (53, 13, '新建', 'save', '/crmContract/add', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (54, 13, '编辑', 'update', '/crmContract/update', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (55, 13, '查看列表', 'index', '/crmContract/queryPageList', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (56, 13, '查看详情', 'read', '/crmContract/queryById', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (57, 13, '删除', 'delete', '/crmContract/deleteByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (58, 13, '转移', 'transfer', '/crmContract/changeOwnerUser', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (59, 13, '编辑团队成员', 'teamsave', '/crmContract/addMembers,/crmContract/updateMembers,/crmContract/deleteMembers', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (60, 14, '新建', 'save', '/crmReceivables/add', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (61, 14, '编辑', 'update', '/crmReceivables/update', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (62, 14, '查看列表', 'index', '/crmReceivables/queryPageList', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (63, 14, '查看详情', 'read', '/crmReceivables/queryById', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (64, 14, '删除', 'delete', '/crmReceivables/deleteByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (65, 15, '新建', 'save', '/crmProduct/add', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (66, 15, '编辑', 'update', '/crmProduct/udpate', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (67, 15, '查看列表', 'index', '/crmProduct/queryPageList', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (68, 15, '查看详情', 'read', '/crmProduct/queryById', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (69, 15, '上架/下架', 'status', '/crmProduct/updateStatus', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (70, 15, '转移', 'transfer', '/crmProduct/changeOwnerUser', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (71, 14, '转移', 'transfer', '/crmReceivables/changeOwnerUser', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (97, 2, '业绩目标完成情况', 'achievement', NULL, NULL, 1, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (98, 2, '销售漏斗', 'business', '/biFunnel/addBusinessAnalyze,/biFunnel/win', NULL, 1, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (99, 2, '员工客户分析', 'customer', NULL, NULL, 1, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (101, 2, '员工业绩分析', 'contract', NULL, NULL, 1, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (102, 97, '查看', 'read', '/bi/taskCompleteStatistics', NULL, 3, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (103, 98, '查看', 'read', '', NULL, 3, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (104, 99, '查看', 'read', '/biCustomer/totalCustomerStats,/biCustomer/totalCustomerTable,/biCustomer/customerRecordStats,/biCustomer/customerRecordStats,/biCustomer/customerRecodCategoryStats,/biCustomer/customerConversionStats,/biCustomer/poolStats,/biCustomer/poolTable,/biCustomer/employeeCycle,/biCustomer/employeeCycleInfo,/biCustomer/customerSatisfactionTable,/biCustomer/productSatisfactionTable', NULL, 3, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (106, 101, '查看', 'read', '/biEmployee/contractNumStats,/biEmployee/contractMoneyStats,/biEmployee/receivablesMoneyStats,/biEmployee/totalContract,/biEmployee/invoiceStats', NULL, 3, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (107, 11, '联系人导出', 'excelexport', '/crmContacts/batchExportExcel,/crmContacts/allExportExcel', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (108, 11, '联系人导入', 'excelimport', '/crmContacts/uploadExcel', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (109, 15, '产品导入', 'excelimport', '/crmProduct/uploadExcel', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (110, 15, '产品导出', 'excelexport', '/crmProduct/batchExportExcel,/crmProduct/allExportExcel', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (117, 2, '产品分析', 'product', NULL, NULL, 1, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (118, 117, '查看', 'read', '/bi/productStatistics,/biRanking/contractProductRanKing', NULL, 3, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (123, 2, '客户画像分析', 'portrait', NULL, NULL, 1, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (124, 123, '查看', 'read', '/biRanking/addressAnalyse,/biRanking/portrait,/biRanking/portraitLevel,/biRanking/portraitSource', NULL, 3, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (125, 2, '排行榜', 'ranking', NULL, NULL, 1, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (126, 125, '查看', 'read', '/biRanking/contractRanKing,/biRanking/receivablesRanKing,/biRanking/contractCountRanKing,/biRanking/productCountRanKing,/biRanking/productCountRanKing,/biRanking/contactsCountRanKing,/biRanking/customerCountRanKing,/biRanking/recordCountRanKing,/biRanking/customerGenjinCountRanKing,/biRanking/customerGenjinCountRanKing', NULL, 3, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (146, 2, '办公分析', 'oa', NULL, NULL, 1, 10, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (147, 146, '查看', 'read', '/biWork/logStatistics,/biWork/examineStatistics', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (148, 0, '全部', 'oa', NULL, NULL, 1, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (149, 0, '全部', 'project', NULL, NULL, 1, 0, 1, '项目管理角色权限');
INSERT INTO `wk_admin_menu` VALUES (150, 148, '通讯录', 'book', NULL, NULL, 1, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (151, 150, '查看', 'read', '/adminUser/queryListName', NULL, 3, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (152, 149, '项目管理', 'projectManage', NULL, NULL, 1, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (153, 152, '新建项目', 'save', '/work/addWork', NULL, 3, 2, 1, 'projectSave');
INSERT INTO `wk_admin_menu` VALUES (160, 3, '企业首页', 'system', NULL, NULL, 1, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (161, 160, '查看', 'read', '', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (162, 160, '编辑', 'update', '/adminConfig/setAdminConfig', NULL, 3, 2, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (163, 3, '应用管理', 'configSet', NULL, NULL, 1, 2, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (164, 163, '查看', 'read', '/adminConfig/queryModuleSetting', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (165, 163, '停用/启用', 'update', '/adminConfig/setModuleSetting', NULL, 3, 2, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (166, 3, '员工与部门管理', 'users', NULL, NULL, 1, 3, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (167, 166, '部门/员工查看', 'read', '', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (168, 166, '员工新建', 'userSave', '/adminUser/addUser', NULL, 3, 2, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (169, 166, '员工禁用/激活', 'userEnables', '/adminUser/setUserStatus', NULL, 3, 3, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (170, 166, '员工操作', 'userUpdate', '/adminUser/setUser', NULL, 3, 4, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (171, 166, '部门新建', 'deptSave', '/adminDept/addDept', NULL, 3, 5, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (172, 166, '部门编辑', 'deptUpdate', '/adminDept/setDept', NULL, 3, 6, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (173, 166, '部门删除', 'deptDelete', '/adminDept/deleteDept', NULL, 3, 7, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (174, 3, '角色权限管理', 'permission', NULL, NULL, 1, 4, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (175, 174, '角色权限设置', 'update', '/adminRole/getRoleByType/*', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (176, 3, '办公审批流', 'oa', NULL, NULL, 1, 6, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (177, 176, '办公审批流管理', 'examine', '/oaExamineCategory/queryExamineCategoryList', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (178, 3, '业务审批流', 'examineFlow', NULL, NULL, 1, 5, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (179, 178, '业务审批流管理', 'update', '/crmExamine/queryAllExamine', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (180, 3, '客户管理设置', 'crm', NULL, NULL, 1, 7, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (181, 180, '自定义字段设置', 'field', '/crmField/queryFields', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (182, 180, '客户公海规则', 'pool', '/crmCustomerPool/queryPoolSettingList', NULL, 3, 2, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (183, 180, '业务参数设置', 'setting', '', NULL, 3, 4, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (184, 180, '业绩目标设置', 'achievement', '/biAchievement/queryAchievementList', NULL, 3, 5, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (185, 3, '项目管理设置', 'work', NULL, NULL, 1, 8, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (186, 185, '项目管理', 'update', '', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (187, 148, '公告', 'announcement', NULL, NULL, 1, 2, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (188, 187, '新建', 'save', '/oaAnnouncement/addAnnouncement', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (189, 187, '编辑', 'update', '/oaAnnouncement/setAnnouncement', NULL, 3, 2, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (190, 187, '删除', 'delete', '/oaAnnouncement/delete', NULL, 3, 3, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (191, 10, '设置成交状态', 'dealStatus', '/crmCustomer/setDealStatus', NULL, 3, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (192, 13, '合同作废', 'discard', '/crmContract/contractDiscard', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (193, 2, '呼叫中心', 'call', NULL, NULL, 1, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (194, 193, '查询通话记录', 'index', NULL, NULL, 3, 2, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (195, 193, '通话记录分析', 'analysis', NULL, NULL, 3, 3, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (200, 1, '市场活动', 'marketing', NULL, NULL, 1, 1, 1, '');
INSERT INTO `wk_admin_menu` VALUES (201, 200, '新建', 'save', '/crmMarketing/add', NULL, 3, 1, 1, '');
INSERT INTO `wk_admin_menu` VALUES (202, 200, '查看列表', 'index', '/crmMarketing/queryPageList', NULL, 3, 1, 1, '');
INSERT INTO `wk_admin_menu` VALUES (204, 200, '编辑', 'update', '/crmMarketing/update', NULL, 3, 1, 1, '');
INSERT INTO `wk_admin_menu` VALUES (205, 200, '删除', 'delete', '/crmMarketing/deleteByIds', NULL, 3, 1, 1, '');
INSERT INTO `wk_admin_menu` VALUES (206, 200, '启用/停用', 'updateStatus', '/crmMarketing/updateStatus', NULL, 3, 1, 1, '');
INSERT INTO `wk_admin_menu` VALUES (207, 200, '查看详情', 'read', '/crmMarketing/queryById', NULL, 3, 1, 1, '');
INSERT INTO `wk_admin_menu` VALUES (208, 13, '导出', 'excelexport', '/crmContract/batchExportExcel,/crmContract/allExportExcel', NULL, 3, 1, 1, '');
INSERT INTO `wk_admin_menu` VALUES (209, 12, '导出', 'excelexport', '/crmBusiness/batchExportExcel,/crmBusiness/allExportExcel', NULL, 3, 1, 1, '');
INSERT INTO `wk_admin_menu` VALUES (211, 15, '删除', 'delete', '/crmProduct/deleteByIds', NULL, 3, 1, 1, '');
INSERT INTO `wk_admin_menu` VALUES (212, 14, '导出', 'excelexport', '/crmReceivables/batchExportExcel,/crmReceivables/allExportExcel', NULL, 3, 1, 1, '');
INSERT INTO `wk_admin_menu` VALUES (213, 1, '外勤', 'outwork', NULL, NULL, 1, 9, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (214, 213, '新建', 'save', NULL, NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (215, 213, '查看', 'read', NULL, NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (216, 213, '删除', 'delete', NULL, NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (217, 213, '设置', 'setting', NULL, NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (218, 10, '附近的客户', 'nearbyCustomer', '/crmCustomer/nearbyCustomer', NULL, 3, 1, 1, '');
INSERT INTO `wk_admin_menu` VALUES (230, 3, '系统日志', 'adminLog', NULL, NULL, 1, 9, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (231, 230, '系统登录日志', 'loginLog', '/system/log/queryLoginLogList', NULL, 3, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (232, 230, '数据操作日志', 'actionRecord', '/system/log/queryActionRecordList', NULL, 3, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (233, 230, '系统操作日志', 'systemLog', '/system/log/querySystemLogList', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (300, 0, '项目管理', 'work', NULL, NULL, 1, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (301, 300, '项目', 'project', NULL, NULL, 1, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (310, 301, '项目设置', 'setWork', '', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (311, 301, '项目导出', 'excelExport', '', NULL, 3, 2, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (312, 301, '新建任务列表', 'saveTaskClass', '', NULL, 3, 3, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (313, 301, '编辑任务列表', 'updateTaskClass', '', NULL, 3, 4, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (314, 301, '移动任务列表', 'updateClassOrder', '', NULL, 3, 5, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (315, 301, '删除任务列表', 'deleteTaskClass', '', NULL, 3, 6, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (316, 301, '新建任务', 'saveTask', '', NULL, 3, 7, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (317, 301, '完成任务', 'setTaskStatus', '', NULL, 3, 8, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (318, 301, '编辑任务标题', 'setTaskTitle', '', NULL, 3, 9, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (319, 301, '编辑任务描述', 'setTaskDescription', '', NULL, 3, 10, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (320, 301, '分配任务', 'setTaskMainUser', '', NULL, 3, 11, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (321, 301, '设置任务时间', 'setTaskTime', '', NULL, 3, 12, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (322, 301, '设置任务标签', 'setTaskLabel', '', NULL, 3, 13, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (323, 301, '添加任务参与人', 'setTaskOwnerUser', '', NULL, 3, 14, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (324, 301, '设置任务优先级', 'setTaskPriority', '', NULL, 3, 15, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (325, 301, '移动任务', 'setTaskOrder', '', NULL, 3, 16, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (326, 301, '归档任务', 'archiveTask', '', NULL, 3, 17, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (327, 301, '删除任务', 'deleteTask', '', NULL, 3, 18, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (328, 301, '彻底删除任务', 'cleanTask', '', NULL, 3, 19, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (329, 301, '任务添加附件', 'uploadTaskFile', '', NULL, 3, 20, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (330, 301, '任务删除附件', 'deleteTaskFile', '', NULL, 3, 21, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (331, 301, '项目导入', 'excelImport', '', NULL, 3, 22, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (332, 301, '新建子任务', 'addChildTask', '', NULL, 3, 23, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (333, 301, '编辑子任务', 'updateChildTask', '', NULL, 3, 24, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (334, 301, '删除子任务', 'deleteChildTask', '', NULL, 3, 25, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (335, 301, '恢复任务', 'restoreTask', '', NULL, 3, 26, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (336, 301, '关联业务', 'saveTaskRelation', '', NULL, 3, 27, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (337, 301, '完成子任务', 'setChildTaskStatus', '', NULL, 3, 28, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (400, 1, '客户回访管理', 'visit', NULL, NULL, 1, 8, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (401, 400, '新建', 'save', '/crmReturnVisit/add', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (402, 400, '编辑', 'update', '/crmReturnVisit/update', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (403, 400, '查看列表', 'index', '/crmReturnVisit/queryPageList', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (404, 400, '查看详情', 'read', '/crmReturnVisit/queryById/*', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (405, 400, '删除', 'delete', '/crmReturnVisit/deleteByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (420, 1, '发票管理', 'invoice', NULL, NULL, 1, 9, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (421, 420, '新建', 'save', '/crmInvoice/save', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (422, 420, '编辑', 'update', '/crmInvoice/update', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (423, 420, '查看列表', 'index', '/crmInvoice/queryPageList', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (424, 420, '查看详情', 'read', '/crmInvoice/queryById/*', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (425, 420, '删除', 'delete', '/crmInvoice/deleteByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (426, 420, '转移', 'transfer', '/crmInvoice/changeOwnerUser', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (427, 420, '标记开票', 'updateInvoiceStatus', '/crmInvoice/updateInvoiceStatus', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (428, 420, '重置开票信息', 'resetInvoiceStatus', '/crmInvoice/resetInvoiceStatus', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (440, 1, '跟进记录管理', 'followRecord', NULL, NULL, 1, 10, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (441, 440, '查看', 'read', '', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (442, 440, '新建', 'save', '/crmActivity/addCrmActivityRecord', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (443, 440, '编辑', 'update', '/crmActivity/updateActivityRecord', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (444, 440, '删除', 'delete', '/crmActivity/deleteCrmActivityRecord/*', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (500, 180, '打印模板设置', 'print', '', NULL, 3, 3, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (501, 12, '打印', 'print', '', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (502, 13, '打印', 'print', '', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (503, 14, '打印', 'print', '', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (601, 6, '供应商', 'supplier', '', NULL, 1, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (602, 601, '新建', 'save', '/jxcSupplier/addOrUpdate/add', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (603, 601, '编辑', 'update', '/jxcSupplier/addOrUpdate/update', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (604, 601, '查看列表', 'index', '/jxcField/queryPageList/2', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (605, 601, '查看详情', 'read', '/jxcField/information/2,/jxcSupplier/queryById/*', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (606, 601, '删除', 'delete', '/jxcSupplier/deleteByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (607, 601, '转移', 'transfer', '/jxcField/transfer/2', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (608, 601, '导出', 'excelexport', NULL, NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (610, 6, '采购订单', 'purchase', '', NULL, 1, 2, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (611, 610, '新建', 'save', '/jxcPurchase/addOrUpdate/add', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (612, 610, '编辑', 'update', '/jxcPurchase/addOrUpdate/update', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (613, 610, '查看列表', 'index', '/jxcField/queryPageList/3', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (614, 610, '查看详情', 'read', '/jxcField/information/3,/jxcPurchase/queryById/*', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (615, 610, '删除', 'delete', '/jxcPurchase/deleteByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (616, 610, '转移', 'transfer', '/jxcField/transfer/3', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (617, 610, '作废', 'setState', '/jxcPurchase/setStateByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (618, 610, '导出', 'excelexport', NULL, NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (620, 6, '采购退货', 'retreat', '', NULL, 1, 3, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (621, 620, '新建', 'save', '/jxcRetreat/addOrUpdate/add', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (622, 620, '编辑', 'update', '/jxcRetreat/addOrUpdate/update', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (623, 620, '查看列表', 'index', '/jxcField/queryPageList/4', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (624, 620, '查看详情', 'read', '/jxcField/information/4,/jxcRetreat/queryById/*', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (625, 620, '删除', 'delete', '/jxcRetreat/deleteByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (626, 620, '转移', 'transfer', '/jxcField/transfer/4', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (627, 620, '作废', 'setState', '/jxcRetreat/setStateByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (628, 620, '导出', 'excelexport', NULL, NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (630, 6, '产品', 'product', '', NULL, 1, 4, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (631, 630, '新建', 'save', '/jxcProduct/addOrUpdate/add', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (632, 630, '编辑', 'update', '/jxcProduct/addOrUpdate/update', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (633, 630, '查看列表', 'index', '/jxcField/queryList/1,/jxcField/queryPageList/1,/jxcWarehouseProduct/queryPageList,/jxcWarehouseProduct/queryList', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (634, 630, '查看详情', 'read', '/jxcField/information/1,/jxcProduct/queryById/*', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (636, 630, '上/下架', 'self', '/jxcProduct/addorUpdateShelf', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (637, 630, '删除', 'delete', '/jxcProduct/deleteByIds', NULL, NULL, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (638, 630, '导出', 'excelexport', NULL, NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (640, 6, '销售订单', 'sale', '', NULL, 1, 5, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (641, 640, '新建', 'save', '/jxcSale/addOrUpdate/add', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (642, 640, '编辑', 'update', '/jxcSale/addOrUpdate/update', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (643, 640, '查看列表', 'index', '/jxcField/queryPageList/5', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (644, 640, '查看详情', 'read', '/jxcField/information/5,/jxcSale/queryById/*', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (645, 640, '删除', 'delete', '/jxcSale/deleteByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (646, 640, '转移', 'transfer', '/jxcField/transfer/5', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (647, 640, '作废', 'setState', '/jxcSale/setStateByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (648, 640, '导出', 'excelexport', NULL, NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (650, 6, '销售退货单', 'salereturn', '', NULL, 1, 6, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (651, 650, '新建', 'save', '/jxcSalereturn/addOrUpdate/add', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (652, 650, '编辑', 'update', '/jxcSalereturn/addOrUpdate/update', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (653, 650, '查看列表', 'index', '/jxcField/queryPageList/6', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (654, 650, '查看详情', 'read', '/jxcField/information/6,/jxcSalereturn/queryById/*', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (655, 650, '删除', 'delete', '/jxcSalereturn/deleteByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (656, 650, '转移', 'transfer', '/jxcField/transfer/6', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (657, 650, '作废', 'setState', '/jxcSalereturn/setStateByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (658, 650, '导出', 'excelexport', NULL, NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (670, 6, '仓库管理', 'warehouse', '', NULL, 1, 7, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (671, 670, '新建', 'save', '/jxcWarehouse/addOrUpdate/add', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (672, 670, '编辑', 'update', '/jxcWarehouse/addOrUpdate/update', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (673, 670, '查看列表', 'index', '/jxcWarehouse/queryPageList', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (675, 670, '删除', 'delete', '/jxcWarehouse/deleteByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (676, 670, '停用/启用', 'spst', '/jxcWarehouse/setTrunByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (677, 670, '导出', 'excelexport', NULL, NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (680, 6, '产品库存', 'warehouseProduct', '', NULL, 1, 8, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (681, 680, '查看列表', 'index', '/jxcWarehouseProduct/queryPageList', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (682, 680, '导出', 'excelexport', NULL, NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (690, 6, '产品入库', 'receipt', '', NULL, 1, 9, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (691, 690, '新建', 'save', '/jxcReceipt/addOrUpdate', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (693, 690, '查看列表', 'index', '/jxcField/queryPageList/7', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (694, 690, '查看详情', 'read', '/jxcField/information/7,/jxcReceipt/queryById/*', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (695, 690, '作废', 'setState', '/jxcReceipt/setStateByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (696, 690, '导出', 'excelexport', NULL, NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (700, 6, '产品出库', 'outbound', '', NULL, 1, 10, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (701, 700, '新建', 'save', '/jxcOutbound/addOrUpdate', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (703, 700, '查看列表', 'index', '/jxcField/queryPageList/8', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (704, 700, '查看详情', 'read', '/jxcField/information/8,/jxcOutbound/queryById/*', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (705, 700, '作废', 'setState', '/jxcOutbound/setStateByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (706, 700, '导出', 'excelexport', NULL, NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (710, 6, '库存调拨', 'allocation', '', NULL, 1, 11, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (711, 710, '新建', 'save', '/jxcAllocation/addOrUpdate/add', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (712, 710, '编辑', 'update', '/jxcAllocation/addOrUpdate/update', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (713, 710, '查看列表', 'index', '/jxcField/queryPageList/12', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (714, 710, '查看详情', 'read', '/jxcField/information/12,/jxcAllocation/queryById/*', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (715, 710, '删除', 'delete', '/jxcAllocation/deleteByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (716, 710, '作废', 'setState', '/jxcAllocation/setStateByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (717, 710, '导出', 'excelexport', NULL, NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (720, 6, '库存盘点', 'inventory', '', NULL, 1, 12, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (721, 720, '新建', 'save', '/jxcInventory/addOrUpdate/add', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (722, 720, '编辑', 'update', '/jxcInventory/addOrUpdate/update', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (723, 720, '查看列表', 'index', '/jxcField/queryPageList/11', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (724, 720, '查看详情', 'read', '/jxcField/information/11,/jxcInventory/queryById/*', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (725, 720, '删除', 'delete', '/jxcInventory/deleteByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (726, 720, '作废', 'setState', '/jxcInventory/setStateByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (727, 720, '盘点入库', 'storage', NULL, NULL, NULL, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (728, 720, '盘点作废', 'invalid', '', NULL, NULL, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (729, 720, '导出', 'excelexport', NULL, NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (730, 6, '出入库明细', 'detailed', '', NULL, 1, 13, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (731, 730, '查看列表', 'index', '/jxcField/queryPageList/13', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (732, 730, '导出', 'excelexport', NULL, NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (740, 6, '回款', 'collection', '', NULL, 1, 14, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (741, 740, '新建', 'save', '/jxcCollection/addOrUpdate/add', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (742, 740, '编辑', 'update', '/jxcCollection/addOrUpdate/update', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (743, 740, '查看列表', 'index', '/jxcField/queryPageList/10', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (744, 740, '查看详情', 'read', '/jxcField/information/10,/jxcCollection/queryById/*', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (745, 740, '删除', 'delete', '/jxcCollection/deleteByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (746, 740, '转移', 'transfer', '/jxcField/transfer/10', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (747, 740, '作废', 'setState', '/jxcCollection/setStateByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (748, 740, '导出', 'excelexport', NULL, NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (750, 6, '付款', 'payment', '', NULL, 1, 15, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (751, 750, '新建', 'save', '/jxcPayment/addOrUpdate/add', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (752, 750, '编辑', 'update', '/jxcPayment/addOrUpdate/update', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (753, 750, '查看列表', 'index', '/jxcField/queryPageList/9', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (754, 750, '查看详情', 'read', '/jxcField/information/9,/jxcPayment/queryById/*', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (755, 750, '删除', 'delete', '/jxcPayment/deleteByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (756, 750, '转移', 'transfer', '/jxcField/transfer/9', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (757, 750, '作废', 'setState', '/jxcPayment/setStateByIds', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (758, 750, '导出', 'excelexport', NULL, NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (760, 2, '全部', 'jxc', '', NULL, 1, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (761, 760, '进销存采购分析', 'purchasingStatistics', '', NULL, 1, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (762, 761, '查看', 'read', '/jxcPurchaseStatistics/purchasingStatistics,/jxcProductPurchaseStatistics/productPurchaseStatistics,/jxcProductPurchaseStatistics/purchaseHeadStatistics,/jxcSupplierPurchaseStatistics/supplierPurchaseStatistics', NULL, 3, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (763, 760, '进销存销售分析', 'saleStatistics', '', NULL, 1, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (764, 763, '查看', 'read', '/jxcSaleStatistics/saleStatistics,/jxcProductSaleStatistics/productSaleStatistics,/jxcProductSaleStatistics/saleHeadStatistics,/jxcCustomerSaleStatistics/customerSaleStatistics,/jxcCustomerSaleStatistics/customerSaleStatistics', NULL, 3, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (765, 760, '进销存产品分析', 'productStatistics', '', NULL, 1, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (766, 765, '查看', 'read', '/jxcProductStatistics/productStatistics,/jxcProductStatistics/productHeadStatistics', NULL, 3, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (800, 5, '员工管理', 'employee', '', NULL, 1, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (801, 800, '新建', 'save', '/hrmEmployee/addEmployee', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (802, 800, '编辑', 'update', '/hrmEmployeePost/updatePostInformation,/hrmEmployee/setEduExperience,/hrmEmployee/addExperience,/hrmEmployee/deleteEduExperience,/hrmEmployee/addWorkExperience,/hrmEmployee/setWorkExperience,/hrmEmployee/deleteWorkExperience,/hrmEmployee/addCertificate,/hrmEmployee/setCertificate,/hrmEmployee/deleteCertificate,/hrmEmployee/addTrainingExperience,/hrmEmployee/setTrainingExperience,/hrmEmployee/deleteTrainingExperience,/hrmEmployee/addContacts,/hrmEmployee/setContacts,/hrmEmployee/deleteContacts,/hrmEmployeeContract/addContract,/hrmEmployeeContract/setContract,/hrmEmployeeContract/deleteContract,/SocialSecurity/setSalaryCard,/SocialSecurity/setSocialSecurity,/hrmEmployeeFile/addFile,/hrmEmployeeFile/deleteFile', NULL, 3, 2, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (803, 800, '查看列表', 'index', '/hrmEmployee/queryPageList', NULL, 3, 3, 1, 'label-92');
INSERT INTO `wk_admin_menu` VALUES (804, 800, '查看详情', 'read', '/hrmEmployee/queryById,/hrmEmployeePost/postInformation,/hrmEmployee/personalInformation,/hrmEmployeeContract/contractInformation,/hrmEmployee/SocialSecurity/salarySocialSecurityInformation,/hrmEmployeeFile/queryFileNum', NULL, 3, 4, 1, 'label-92');
INSERT INTO `wk_admin_menu` VALUES (805, 800, '导入', 'excelimport', '/hrmEmployee/uploadExcel', NULL, 3, 5, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (806, 800, '导出', 'excelexport', '/hrmEmployee/export', NULL, 3, 6, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (807, 800, '删除', 'delete', '/hrmEmployee/deleteByIds', NULL, 3, 7, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (808, 800, '办理转正', 'become', '/hrmEmployee/become', NULL, 3, 8, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (809, 800, '调整部门/岗位', 'changePost', '/hrmEmployee/changePost', NULL, 3, 9, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (810, 800, '晋升/降级', 'promotion', '/hrmEmployee/promotion', NULL, 3, 10, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (811, 800, '办理离职', 'leave', '/hrmEmployeePost/addLeaveInformation', NULL, 3, 11, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (812, 800, '设置参保方案', 'setInsured', '/hrmEmployee/updateInsuranceScheme', NULL, 3, 12, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (813, 800, '再入职', 'againOnboarding', '/hrmEmployee/againOnboarding', NULL, 3, 13, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (814, 800, '确认入职', 'confirmEntry', '/hrmEmployee/confirmEntry', NULL, 3, 13, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (815, 800, '放弃离职', 'cancelLevel', '/hrmEmployeePost/deleteLeaveInformation', NULL, 3, 14, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (830, 5, '组织管理', 'dept', '', NULL, 1, 2, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (831, 830, '新建', 'save', '/hrmDept/addDept', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (832, 830, '编辑', 'update', '/hrmDept/setDept', NULL, 3, 2, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (833, 830, '查看列表', 'index', '', NULL, 3, 3, 1, 'label-92');
INSERT INTO `wk_admin_menu` VALUES (834, 830, '查看详情', 'read', '/hrmDept/queryById', NULL, 3, 4, 1, 'label-92');
INSERT INTO `wk_admin_menu` VALUES (835, 830, '删除', 'delete', '/hrmDept/deleteDeptById', NULL, 3, 5, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (840, 5, '薪资管理', 'salary', '', NULL, 1, 3, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (841, 840, '工资表维护', 'manage', '/hrmSalaryMonthRecord/computeSalaryData,/hrmSalaryMonthRecord/updateSalary,/hrmSalaryMonthRecord/submitExamine,/hrmSalaryMonthRecord/addNextMonthSalary', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (842, 840, '查看薪酬列表', 'index', '/hrmSalaryOption/querySalaryOptionDetail', NULL, 3, 2, 1, 'label-92');
INSERT INTO `wk_admin_menu` VALUES (843, 840, '查看历史工资', 'history', '/hrmSalaryHistoryRecord/queryHistorySalaryList', NULL, 3, 3, 1, 'label-92');
INSERT INTO `wk_admin_menu` VALUES (844, 840, '发放工资条', 'sendSlip', '/hrmSalarySlipRecord/sendSalarySlip', NULL, 3, 4, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (845, 840, '查看发放记录', 'queryRecord', '', NULL, 3, 5, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (846, 840, '查看薪资档案', 'queryArchives', '/hrmSalaryArchives/querySalaryArchivesList', NULL, 3, 6, 1, 'label-92');
INSERT INTO `wk_admin_menu` VALUES (847, 840, '维护薪资档案', 'updateArchives', '/hrmSalaryArchives/setFixSalaryRecord,/hrmSalaryArchives/setChangeSalaryRecord,/deleteChangeSalary/setChangeSalaryRecord,/deleteChangeSalary/batchChangeSalaryRecord,/deleteChangeSalary/exportFixSalaryRecord', NULL, 3, 7, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (850, 5, '社保管理', 'insurance', '', NULL, 1, 4, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (851, 850, '维护社保', 'manage', '/hrmInsuranceMonthRecord/computeInsuranceData,/hrmInsuranceMonthEmpRecord/stop,/hrmInsuranceMonthEmpRecord/updateInsuranceProject', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (852, 850, '查看社保', 'read', '/hrmInsuranceMonthRecord/queryInsuranceRecordList', NULL, 3, 2, 1, 'label-92');
INSERT INTO `wk_admin_menu` VALUES (860, 5, '招聘管理', 'recruit', '', NULL, 1, 5, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (861, 860, '新建候选人', 'save', '/hrmRecruitCandidate/addCandidate', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (862, 860, '查看候选人', 'read', '/hrmRecruitCandidate/queryPageList,/hrmRecruitCandidate/queryById', NULL, 3, 2, 1, 'label-92');
INSERT INTO `wk_admin_menu` VALUES (863, 860, '维护候选人', 'manage', '/hrmRecruitCandidate/setCandidate,/hrmRecruitCandidate/updateCandidateStatus,/hrmRecruitCandidate/updateCandidatePost,/hrmRecruitCandidate/updateCandidateRecruitChannel,/hrmRecruitCandidate/eliminateCandidate', NULL, 3, 3, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (864, 860, '删除候选人', 'delete', '/hrmRecruitCandidate/deleteByIds,/hrmRecruitCandidate/deleteById', NULL, 3, 4, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (865, 860, '新建招聘职位', 'savePost', '/hrmRecruitPost/addRecruitPost', NULL, 3, 5, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (866, 860, '编辑招聘职位', 'updatePost', '/hrmRecruitPost/setRecruitPost', NULL, 3, 6, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (867, 860, '查看招聘职位', 'readPost', '/hrmRecruitPost/queryRecruitPostPageList,/hrmRecruitPost/queryById', NULL, 3, 7, 1, 'label-92');
INSERT INTO `wk_admin_menu` VALUES (868, 860, '停用/启用招聘职位', 'updatePostStatus', '/hrmRecruitPost/updateRecruitPostStatus', NULL, 3, 8, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (880, 5, '绩效管理', 'appraisal', '', NULL, 1, 6, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (881, 880, '新建绩效', 'save', '/hrmRecruitPost/addAppraisal', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (882, 880, '编辑绩效', 'update', '/hrmRecruitPost/setAppraisal', NULL, 3, 2, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (883, 880, '查看绩效', 'read', '', NULL, 3, 3, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (884, 880, '删除绩效', 'delete', '/hrmRecruitPost/delete', NULL, 3, 4, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (885, 880, '终止绩效', 'stop', '/hrmRecruitPoststopAppraisal', NULL, 3, 5, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (886, 880, '查看员工绩效', 'readEmp', '', NULL, 3, 3, 1, 'label-92');
INSERT INTO `wk_admin_menu` VALUES (890, 5, '考勤管理', 'attendance', '', NULL, 1, 6, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (891, 890, '查看打卡记录', 'readClock', '/hrmRecruitPost/addAppraisal', NULL, 3, 1, 1, 'label-92');
INSERT INTO `wk_admin_menu` VALUES (892, 890, '导出打卡记录', 'excelexport', '/hrmAttendanceClock/excelExport', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (900, 3, '人力资源管理', 'hrm', NULL, NULL, 1, 10, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (901, 900, '自定义字段设置', 'field', '/hrmConfig/queryFields', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (902, 900, '薪资设置', 'salary', '/hrmSalaryGroup/querySalaryGroupPageList', NULL, 3, 2, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (903, 900, '社保设置', 'insurance', '', NULL, 3, 2, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (904, 900, '绩效设置', 'appraisal', '', NULL, 3, 2, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (905, 900, '业务参数设置', 'params', '/hrmConfig/queryRecruitChannelList', NULL, 3, 2, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (906, 900, '员工档案设置', 'archives', '', NULL, 3, 2, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (920, 3, '进销存管理', 'jxc', NULL, NULL, 1, 11, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (921, 920, '自定义字段设置', 'field', '/jxcField/queryFields', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (922, 920, '业务参数设置', 'params', '/jxcProductType/queryJxcProductTyp,/jxcNumberSetting/queryNumberSetting', NULL, 3, 1, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (923, 3, '初始化', 'init', NULL, NULL, 1, 12, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (924, 923, '初始化管理', 'initData', '/adminConfig/moduleInitData', NULL, 3, 0, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (926, 180, '市场活动表单设置', 'activityForm', '/crmMarketingForm/page', NULL, 3, 6, 1, NULL);
INSERT INTO `wk_admin_menu` VALUES (927, 301, '管理参与人权限', 'manageTaskOwnerUser', '', NULL, 3, 29, 1, NULL);

-- ----------------------------
-- Table structure for wk_admin_message
-- ----------------------------
DROP TABLE IF EXISTS `wk_admin_message`;
CREATE TABLE `wk_admin_message`  (
  `message_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息标题',
  `content` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容',
  `label` int(2) NULL DEFAULT NULL COMMENT '消息大类 1 任务 2 日志 3 oa审批 4公告 5 日程 6 crm消息 7 知识库 8 人资',
  `type` int(2) NULL DEFAULT NULL COMMENT '消息类型 详见AdminMessageEnum',
  `type_id` int(11) NULL DEFAULT NULL COMMENT '关联ID',
  `create_user` bigint(20) NOT NULL COMMENT '消息创建者 0为系统',
  `recipient_user` bigint(20) NOT NULL COMMENT '接收人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_read` int(1) NULL DEFAULT 0 COMMENT '是否已读 0 未读 1 已读',
  `read_time` datetime(0) NULL DEFAULT NULL COMMENT '已读时间',
  PRIMARY KEY (`message_id`) USING BTREE,
  INDEX `recipient_user`(`recipient_user`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_admin_message
-- ----------------------------

-- ----------------------------
-- Table structure for wk_admin_model_sort
-- ----------------------------
DROP TABLE IF EXISTS `wk_admin_model_sort`;
CREATE TABLE `wk_admin_model_sort`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(4) NOT NULL COMMENT '导航类型 1头部导航 2客户管理左侧导航',
  `model` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模块  1仪表盘 2待办事项 3线索 4客户 5联系人 6商机 7合同 8回款 9发票 10回访 11产品 12市场活动',
  `sort` int(4) NOT NULL COMMENT '排序',
  `is_hidden` int(2) NULL DEFAULT 0 COMMENT '是否隐藏  0不隐藏 1隐藏',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '客户管理导航栏排序表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_admin_model_sort
-- ----------------------------

-- ----------------------------
-- Table structure for wk_admin_official_img
-- ----------------------------
DROP TABLE IF EXISTS `wk_admin_official_img`;
CREATE TABLE `wk_admin_official_img`  (
  `official_img_id` int(11) NOT NULL AUTO_INCREMENT,
  `size` bigint(20) NULL DEFAULT NULL COMMENT '附件大小（字节）',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件真实路径',
  `file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件路径',
  `type` int(1) NULL DEFAULT NULL COMMENT '1.官网设置 2.名片海报',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tactic` int(6) NULL DEFAULT NULL COMMENT '0',
  PRIMARY KEY (`official_img_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '官网图片' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_admin_official_img
-- ----------------------------

-- ----------------------------
-- Table structure for wk_admin_role
-- ----------------------------
DROP TABLE IF EXISTS `wk_admin_role`;
CREATE TABLE `wk_admin_role`  (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `role_type` int(1) NULL DEFAULT NULL COMMENT '0、自定义角色1、管理角色 2、客户管理角色 3、人事角色 4、财务角色 5、项目角色 8、项目自定义角色',
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `status` int(3) NULL DEFAULT 1 COMMENT '1 启用 0 禁用',
  `data_type` int(1) NOT NULL DEFAULT 5 COMMENT '数据权限 1、本人，2、本人及下属，3、本部门，4、本部门及下属部门，5、全部 ',
  `is_hidden` int(1) NOT NULL DEFAULT 1 COMMENT '0 隐藏 1 不隐藏',
  `label` int(2) NULL DEFAULT NULL COMMENT '1 系统项目管理员角色 2 项目管理角色 3 项目编辑角色 4 项目只读角色',
  PRIMARY KEY (`role_id`) USING BTREE,
  INDEX `role_type`(`role_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 180177 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_admin_role
-- ----------------------------
INSERT INTO `wk_admin_role` VALUES (180162, '超级管理员', 1, 'admin', 1, 5, 1, 5);
INSERT INTO `wk_admin_role` VALUES (180163, '系统设置管理员', 1, NULL, 1, 2, 1, 6);
INSERT INTO `wk_admin_role` VALUES (180164, '部门与员工管理员', 1, NULL, 1, 5, 1, 7);
INSERT INTO `wk_admin_role` VALUES (180165, '审批流管理员', 1, NULL, 1, 5, 1, 8);
INSERT INTO `wk_admin_role` VALUES (180166, '工作台管理员', 1, NULL, 1, 5, 1, 9);
INSERT INTO `wk_admin_role` VALUES (180167, '客户管理员', 1, NULL, 1, 5, 1, 10);
INSERT INTO `wk_admin_role` VALUES (180168, '公告管理员', 7, NULL, 1, 5, 1, 11);
INSERT INTO `wk_admin_role` VALUES (180169, '销售经理角色', 2, NULL, 1, 5, 1, NULL);
INSERT INTO `wk_admin_role` VALUES (180170, '销售员角色', 2, NULL, 1, 1, 1, NULL);
INSERT INTO `wk_admin_role` VALUES (180171, '项目管理员', 8, 'project', 1, 5, 1, 1);
INSERT INTO `wk_admin_role` VALUES (180172, '管理', 5, '系统默认权限，包含所在项目所有权限', 1, 5, 0, 2);
INSERT INTO `wk_admin_role` VALUES (180173, '编辑', 5, '成员初始加入时默认享有的权限', 1, 5, 1, 3);
INSERT INTO `wk_admin_role` VALUES (180174, '只读', 5, '项目只读角色', 1, 1, 1, 4);
INSERT INTO `wk_admin_role` VALUES (180175, '上级角色', 9, NULL, 1, 2, 1, 91);

-- ----------------------------
-- Table structure for wk_admin_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `wk_admin_role_menu`;
CREATE TABLE `wk_admin_role_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  INDEX `menu_id`(`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2300834 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色菜单对应关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_admin_role_menu
-- ----------------------------
INSERT INTO `wk_admin_role_menu` VALUES (2300572, 180163, 3);
INSERT INTO `wk_admin_role_menu` VALUES (2300573, 180163, 160);
INSERT INTO `wk_admin_role_menu` VALUES (2300574, 180163, 161);
INSERT INTO `wk_admin_role_menu` VALUES (2300575, 180163, 162);
INSERT INTO `wk_admin_role_menu` VALUES (2300576, 180163, 163);
INSERT INTO `wk_admin_role_menu` VALUES (2300577, 180163, 164);
INSERT INTO `wk_admin_role_menu` VALUES (2300578, 180163, 165);
INSERT INTO `wk_admin_role_menu` VALUES (2300579, 180163, 166);
INSERT INTO `wk_admin_role_menu` VALUES (2300580, 180163, 167);
INSERT INTO `wk_admin_role_menu` VALUES (2300581, 180163, 168);
INSERT INTO `wk_admin_role_menu` VALUES (2300582, 180163, 169);
INSERT INTO `wk_admin_role_menu` VALUES (2300583, 180163, 170);
INSERT INTO `wk_admin_role_menu` VALUES (2300584, 180163, 171);
INSERT INTO `wk_admin_role_menu` VALUES (2300585, 180163, 172);
INSERT INTO `wk_admin_role_menu` VALUES (2300586, 180163, 173);
INSERT INTO `wk_admin_role_menu` VALUES (2300587, 180163, 174);
INSERT INTO `wk_admin_role_menu` VALUES (2300588, 180163, 175);
INSERT INTO `wk_admin_role_menu` VALUES (2300589, 180163, 176);
INSERT INTO `wk_admin_role_menu` VALUES (2300590, 180163, 177);
INSERT INTO `wk_admin_role_menu` VALUES (2300591, 180163, 178);
INSERT INTO `wk_admin_role_menu` VALUES (2300592, 180163, 179);
INSERT INTO `wk_admin_role_menu` VALUES (2300593, 180163, 180);
INSERT INTO `wk_admin_role_menu` VALUES (2300594, 180163, 181);
INSERT INTO `wk_admin_role_menu` VALUES (2300595, 180163, 182);
INSERT INTO `wk_admin_role_menu` VALUES (2300596, 180163, 183);
INSERT INTO `wk_admin_role_menu` VALUES (2300597, 180163, 184);
INSERT INTO `wk_admin_role_menu` VALUES (2300598, 180163, 185);
INSERT INTO `wk_admin_role_menu` VALUES (2300599, 180163, 186);
INSERT INTO `wk_admin_role_menu` VALUES (2300600, 180164, 166);
INSERT INTO `wk_admin_role_menu` VALUES (2300601, 180164, 167);
INSERT INTO `wk_admin_role_menu` VALUES (2300602, 180164, 168);
INSERT INTO `wk_admin_role_menu` VALUES (2300603, 180164, 169);
INSERT INTO `wk_admin_role_menu` VALUES (2300604, 180164, 170);
INSERT INTO `wk_admin_role_menu` VALUES (2300605, 180164, 171);
INSERT INTO `wk_admin_role_menu` VALUES (2300606, 180164, 172);
INSERT INTO `wk_admin_role_menu` VALUES (2300607, 180164, 173);
INSERT INTO `wk_admin_role_menu` VALUES (2300608, 180165, 178);
INSERT INTO `wk_admin_role_menu` VALUES (2300609, 180165, 179);
INSERT INTO `wk_admin_role_menu` VALUES (2300610, 180166, 176);
INSERT INTO `wk_admin_role_menu` VALUES (2300611, 180166, 177);
INSERT INTO `wk_admin_role_menu` VALUES (2300612, 180167, 180);
INSERT INTO `wk_admin_role_menu` VALUES (2300613, 180167, 181);
INSERT INTO `wk_admin_role_menu` VALUES (2300614, 180167, 182);
INSERT INTO `wk_admin_role_menu` VALUES (2300615, 180167, 183);
INSERT INTO `wk_admin_role_menu` VALUES (2300616, 180167, 184);
INSERT INTO `wk_admin_role_menu` VALUES (2300617, 180168, 187);
INSERT INTO `wk_admin_role_menu` VALUES (2300618, 180168, 188);
INSERT INTO `wk_admin_role_menu` VALUES (2300619, 180168, 189);
INSERT INTO `wk_admin_role_menu` VALUES (2300620, 180168, 190);
INSERT INTO `wk_admin_role_menu` VALUES (2300621, 180169, 9);
INSERT INTO `wk_admin_role_menu` VALUES (2300622, 180169, 10);
INSERT INTO `wk_admin_role_menu` VALUES (2300623, 180169, 11);
INSERT INTO `wk_admin_role_menu` VALUES (2300624, 180169, 12);
INSERT INTO `wk_admin_role_menu` VALUES (2300625, 180169, 13);
INSERT INTO `wk_admin_role_menu` VALUES (2300626, 180169, 14);
INSERT INTO `wk_admin_role_menu` VALUES (2300627, 180169, 17);
INSERT INTO `wk_admin_role_menu` VALUES (2300628, 180169, 18);
INSERT INTO `wk_admin_role_menu` VALUES (2300629, 180169, 19);
INSERT INTO `wk_admin_role_menu` VALUES (2300630, 180169, 20);
INSERT INTO `wk_admin_role_menu` VALUES (2300631, 180169, 21);
INSERT INTO `wk_admin_role_menu` VALUES (2300632, 180169, 22);
INSERT INTO `wk_admin_role_menu` VALUES (2300633, 180169, 23);
INSERT INTO `wk_admin_role_menu` VALUES (2300634, 180169, 24);
INSERT INTO `wk_admin_role_menu` VALUES (2300635, 180169, 25);
INSERT INTO `wk_admin_role_menu` VALUES (2300636, 180169, 26);
INSERT INTO `wk_admin_role_menu` VALUES (2300637, 180169, 27);
INSERT INTO `wk_admin_role_menu` VALUES (2300638, 180169, 28);
INSERT INTO `wk_admin_role_menu` VALUES (2300639, 180169, 29);
INSERT INTO `wk_admin_role_menu` VALUES (2300640, 180169, 30);
INSERT INTO `wk_admin_role_menu` VALUES (2300641, 180169, 31);
INSERT INTO `wk_admin_role_menu` VALUES (2300642, 180169, 32);
INSERT INTO `wk_admin_role_menu` VALUES (2300643, 180169, 33);
INSERT INTO `wk_admin_role_menu` VALUES (2300644, 180169, 34);
INSERT INTO `wk_admin_role_menu` VALUES (2300645, 180169, 35);
INSERT INTO `wk_admin_role_menu` VALUES (2300646, 180169, 36);
INSERT INTO `wk_admin_role_menu` VALUES (2300647, 180169, 40);
INSERT INTO `wk_admin_role_menu` VALUES (2300648, 180169, 41);
INSERT INTO `wk_admin_role_menu` VALUES (2300649, 180169, 42);
INSERT INTO `wk_admin_role_menu` VALUES (2300650, 180169, 43);
INSERT INTO `wk_admin_role_menu` VALUES (2300651, 180169, 44);
INSERT INTO `wk_admin_role_menu` VALUES (2300652, 180169, 45);
INSERT INTO `wk_admin_role_menu` VALUES (2300653, 180169, 46);
INSERT INTO `wk_admin_role_menu` VALUES (2300654, 180169, 47);
INSERT INTO `wk_admin_role_menu` VALUES (2300655, 180169, 48);
INSERT INTO `wk_admin_role_menu` VALUES (2300656, 180169, 49);
INSERT INTO `wk_admin_role_menu` VALUES (2300657, 180169, 50);
INSERT INTO `wk_admin_role_menu` VALUES (2300658, 180169, 51);
INSERT INTO `wk_admin_role_menu` VALUES (2300659, 180169, 52);
INSERT INTO `wk_admin_role_menu` VALUES (2300660, 180169, 53);
INSERT INTO `wk_admin_role_menu` VALUES (2300661, 180169, 54);
INSERT INTO `wk_admin_role_menu` VALUES (2300662, 180169, 55);
INSERT INTO `wk_admin_role_menu` VALUES (2300663, 180169, 56);
INSERT INTO `wk_admin_role_menu` VALUES (2300664, 180169, 57);
INSERT INTO `wk_admin_role_menu` VALUES (2300665, 180169, 58);
INSERT INTO `wk_admin_role_menu` VALUES (2300666, 180169, 59);
INSERT INTO `wk_admin_role_menu` VALUES (2300667, 180169, 60);
INSERT INTO `wk_admin_role_menu` VALUES (2300668, 180169, 61);
INSERT INTO `wk_admin_role_menu` VALUES (2300669, 180169, 62);
INSERT INTO `wk_admin_role_menu` VALUES (2300670, 180169, 63);
INSERT INTO `wk_admin_role_menu` VALUES (2300671, 180169, 64);
INSERT INTO `wk_admin_role_menu` VALUES (2300672, 180169, 67);
INSERT INTO `wk_admin_role_menu` VALUES (2300673, 180169, 68);
INSERT INTO `wk_admin_role_menu` VALUES (2300674, 180169, 97);
INSERT INTO `wk_admin_role_menu` VALUES (2300675, 180169, 98);
INSERT INTO `wk_admin_role_menu` VALUES (2300676, 180169, 99);
INSERT INTO `wk_admin_role_menu` VALUES (2300677, 180169, 101);
INSERT INTO `wk_admin_role_menu` VALUES (2300678, 180169, 102);
INSERT INTO `wk_admin_role_menu` VALUES (2300679, 180169, 103);
INSERT INTO `wk_admin_role_menu` VALUES (2300680, 180169, 104);
INSERT INTO `wk_admin_role_menu` VALUES (2300681, 180169, 106);
INSERT INTO `wk_admin_role_menu` VALUES (2300682, 180169, 107);
INSERT INTO `wk_admin_role_menu` VALUES (2300683, 180169, 108);
INSERT INTO `wk_admin_role_menu` VALUES (2300684, 180169, 117);
INSERT INTO `wk_admin_role_menu` VALUES (2300685, 180169, 118);
INSERT INTO `wk_admin_role_menu` VALUES (2300686, 180169, 123);
INSERT INTO `wk_admin_role_menu` VALUES (2300687, 180169, 124);
INSERT INTO `wk_admin_role_menu` VALUES (2300688, 180169, 125);
INSERT INTO `wk_admin_role_menu` VALUES (2300689, 180169, 126);
INSERT INTO `wk_admin_role_menu` VALUES (2300690, 180169, 191);
INSERT INTO `wk_admin_role_menu` VALUES (2300691, 180169, 192);
INSERT INTO `wk_admin_role_menu` VALUES (2300692, 180170, 14);
INSERT INTO `wk_admin_role_menu` VALUES (2300693, 180170, 17);
INSERT INTO `wk_admin_role_menu` VALUES (2300694, 180170, 18);
INSERT INTO `wk_admin_role_menu` VALUES (2300695, 180170, 19);
INSERT INTO `wk_admin_role_menu` VALUES (2300696, 180170, 20);
INSERT INTO `wk_admin_role_menu` VALUES (2300697, 180170, 21);
INSERT INTO `wk_admin_role_menu` VALUES (2300698, 180170, 25);
INSERT INTO `wk_admin_role_menu` VALUES (2300699, 180170, 26);
INSERT INTO `wk_admin_role_menu` VALUES (2300700, 180170, 27);
INSERT INTO `wk_admin_role_menu` VALUES (2300701, 180170, 28);
INSERT INTO `wk_admin_role_menu` VALUES (2300702, 180170, 29);
INSERT INTO `wk_admin_role_menu` VALUES (2300703, 180170, 30);
INSERT INTO `wk_admin_role_menu` VALUES (2300704, 180170, 34);
INSERT INTO `wk_admin_role_menu` VALUES (2300705, 180170, 35);
INSERT INTO `wk_admin_role_menu` VALUES (2300706, 180170, 36);
INSERT INTO `wk_admin_role_menu` VALUES (2300707, 180170, 40);
INSERT INTO `wk_admin_role_menu` VALUES (2300708, 180170, 41);
INSERT INTO `wk_admin_role_menu` VALUES (2300709, 180170, 42);
INSERT INTO `wk_admin_role_menu` VALUES (2300710, 180170, 43);
INSERT INTO `wk_admin_role_menu` VALUES (2300711, 180170, 44);
INSERT INTO `wk_admin_role_menu` VALUES (2300712, 180170, 45);
INSERT INTO `wk_admin_role_menu` VALUES (2300713, 180170, 46);
INSERT INTO `wk_admin_role_menu` VALUES (2300714, 180170, 47);
INSERT INTO `wk_admin_role_menu` VALUES (2300715, 180170, 48);
INSERT INTO `wk_admin_role_menu` VALUES (2300716, 180170, 49);
INSERT INTO `wk_admin_role_menu` VALUES (2300717, 180170, 50);
INSERT INTO `wk_admin_role_menu` VALUES (2300718, 180170, 52);
INSERT INTO `wk_admin_role_menu` VALUES (2300719, 180170, 53);
INSERT INTO `wk_admin_role_menu` VALUES (2300720, 180170, 54);
INSERT INTO `wk_admin_role_menu` VALUES (2300721, 180170, 55);
INSERT INTO `wk_admin_role_menu` VALUES (2300722, 180170, 56);
INSERT INTO `wk_admin_role_menu` VALUES (2300723, 180170, 57);
INSERT INTO `wk_admin_role_menu` VALUES (2300724, 180170, 59);
INSERT INTO `wk_admin_role_menu` VALUES (2300725, 180170, 60);
INSERT INTO `wk_admin_role_menu` VALUES (2300726, 180170, 61);
INSERT INTO `wk_admin_role_menu` VALUES (2300727, 180170, 62);
INSERT INTO `wk_admin_role_menu` VALUES (2300728, 180170, 63);
INSERT INTO `wk_admin_role_menu` VALUES (2300729, 180170, 64);
INSERT INTO `wk_admin_role_menu` VALUES (2300730, 180170, 67);
INSERT INTO `wk_admin_role_menu` VALUES (2300731, 180170, 68);
INSERT INTO `wk_admin_role_menu` VALUES (2300732, 180170, 97);
INSERT INTO `wk_admin_role_menu` VALUES (2300733, 180170, 98);
INSERT INTO `wk_admin_role_menu` VALUES (2300734, 180170, 99);
INSERT INTO `wk_admin_role_menu` VALUES (2300735, 180170, 101);
INSERT INTO `wk_admin_role_menu` VALUES (2300736, 180170, 102);
INSERT INTO `wk_admin_role_menu` VALUES (2300737, 180170, 103);
INSERT INTO `wk_admin_role_menu` VALUES (2300738, 180170, 104);
INSERT INTO `wk_admin_role_menu` VALUES (2300739, 180170, 106);
INSERT INTO `wk_admin_role_menu` VALUES (2300740, 180170, 108);
INSERT INTO `wk_admin_role_menu` VALUES (2300741, 180170, 117);
INSERT INTO `wk_admin_role_menu` VALUES (2300742, 180170, 118);
INSERT INTO `wk_admin_role_menu` VALUES (2300743, 180170, 123);
INSERT INTO `wk_admin_role_menu` VALUES (2300744, 180170, 124);
INSERT INTO `wk_admin_role_menu` VALUES (2300745, 180170, 125);
INSERT INTO `wk_admin_role_menu` VALUES (2300746, 180170, 126);
INSERT INTO `wk_admin_role_menu` VALUES (2300747, 180173, 316);
INSERT INTO `wk_admin_role_menu` VALUES (2300748, 180173, 317);
INSERT INTO `wk_admin_role_menu` VALUES (2300749, 180173, 318);
INSERT INTO `wk_admin_role_menu` VALUES (2300750, 180173, 319);
INSERT INTO `wk_admin_role_menu` VALUES (2300751, 180173, 320);
INSERT INTO `wk_admin_role_menu` VALUES (2300752, 180173, 321);
INSERT INTO `wk_admin_role_menu` VALUES (2300753, 180173, 322);
INSERT INTO `wk_admin_role_menu` VALUES (2300754, 180173, 323);
INSERT INTO `wk_admin_role_menu` VALUES (2300755, 180173, 324);
INSERT INTO `wk_admin_role_menu` VALUES (2300756, 180173, 325);
INSERT INTO `wk_admin_role_menu` VALUES (2300757, 180173, 326);
INSERT INTO `wk_admin_role_menu` VALUES (2300758, 180173, 327);
INSERT INTO `wk_admin_role_menu` VALUES (2300759, 180173, 329);
INSERT INTO `wk_admin_role_menu` VALUES (2300760, 180173, 330);
INSERT INTO `wk_admin_role_menu` VALUES (2300761, 180173, 331);
INSERT INTO `wk_admin_role_menu` VALUES (2300762, 180173, 332);
INSERT INTO `wk_admin_role_menu` VALUES (2300763, 180173, 333);
INSERT INTO `wk_admin_role_menu` VALUES (2300764, 180173, 334);
INSERT INTO `wk_admin_role_menu` VALUES (2300765, 180173, 335);
INSERT INTO `wk_admin_role_menu` VALUES (2300766, 180173, 337);
INSERT INTO `wk_admin_role_menu` VALUES (2300767, 180171, 149);
INSERT INTO `wk_admin_role_menu` VALUES (2300768, 180171, 152);
INSERT INTO `wk_admin_role_menu` VALUES (2300769, 180171, 153);
INSERT INTO `wk_admin_role_menu` VALUES (2300770, 180169, 2);
INSERT INTO `wk_admin_role_menu` VALUES (2300771, 180169, 15);
INSERT INTO `wk_admin_role_menu` VALUES (2300772, 180169, 65);
INSERT INTO `wk_admin_role_menu` VALUES (2300773, 180169, 66);
INSERT INTO `wk_admin_role_menu` VALUES (2300774, 180169, 69);
INSERT INTO `wk_admin_role_menu` VALUES (2300775, 180169, 70);
INSERT INTO `wk_admin_role_menu` VALUES (2300776, 180169, 109);
INSERT INTO `wk_admin_role_menu` VALUES (2300777, 180169, 110);
INSERT INTO `wk_admin_role_menu` VALUES (2300778, 180169, 193);
INSERT INTO `wk_admin_role_menu` VALUES (2300779, 180169, 194);
INSERT INTO `wk_admin_role_menu` VALUES (2300780, 180169, 195);
INSERT INTO `wk_admin_role_menu` VALUES (2300781, 180169, 211);
INSERT INTO `wk_admin_role_menu` VALUES (2300782, 180169, 400);
INSERT INTO `wk_admin_role_menu` VALUES (2300783, 180169, 401);
INSERT INTO `wk_admin_role_menu` VALUES (2300784, 180169, 402);
INSERT INTO `wk_admin_role_menu` VALUES (2300785, 180169, 403);
INSERT INTO `wk_admin_role_menu` VALUES (2300786, 180169, 404);
INSERT INTO `wk_admin_role_menu` VALUES (2300787, 180169, 405);
INSERT INTO `wk_admin_role_menu` VALUES (2300788, 180169, 420);
INSERT INTO `wk_admin_role_menu` VALUES (2300789, 180169, 421);
INSERT INTO `wk_admin_role_menu` VALUES (2300790, 180169, 422);
INSERT INTO `wk_admin_role_menu` VALUES (2300791, 180169, 423);
INSERT INTO `wk_admin_role_menu` VALUES (2300792, 180169, 424);
INSERT INTO `wk_admin_role_menu` VALUES (2300793, 180169, 425);
INSERT INTO `wk_admin_role_menu` VALUES (2300794, 180169, 426);
INSERT INTO `wk_admin_role_menu` VALUES (2300795, 180169, 427);
INSERT INTO `wk_admin_role_menu` VALUES (2300796, 180169, 428);
INSERT INTO `wk_admin_role_menu` VALUES (2300797, 180169, 440);
INSERT INTO `wk_admin_role_menu` VALUES (2300798, 180169, 441);
INSERT INTO `wk_admin_role_menu` VALUES (2300799, 180169, 442);
INSERT INTO `wk_admin_role_menu` VALUES (2300800, 180169, 443);
INSERT INTO `wk_admin_role_menu` VALUES (2300801, 180169, 444);
INSERT INTO `wk_admin_role_menu` VALUES (2300802, 180170, 400);
INSERT INTO `wk_admin_role_menu` VALUES (2300803, 180170, 401);
INSERT INTO `wk_admin_role_menu` VALUES (2300804, 180170, 402);
INSERT INTO `wk_admin_role_menu` VALUES (2300805, 180170, 403);
INSERT INTO `wk_admin_role_menu` VALUES (2300806, 180170, 404);
INSERT INTO `wk_admin_role_menu` VALUES (2300807, 180170, 405);
INSERT INTO `wk_admin_role_menu` VALUES (2300808, 180170, 420);
INSERT INTO `wk_admin_role_menu` VALUES (2300809, 180170, 421);
INSERT INTO `wk_admin_role_menu` VALUES (2300810, 180170, 422);
INSERT INTO `wk_admin_role_menu` VALUES (2300811, 180170, 423);
INSERT INTO `wk_admin_role_menu` VALUES (2300812, 180170, 424);
INSERT INTO `wk_admin_role_menu` VALUES (2300813, 180170, 425);
INSERT INTO `wk_admin_role_menu` VALUES (2300814, 180170, 426);
INSERT INTO `wk_admin_role_menu` VALUES (2300815, 180170, 427);
INSERT INTO `wk_admin_role_menu` VALUES (2300816, 180170, 428);
INSERT INTO `wk_admin_role_menu` VALUES (2300817, 180170, 440);
INSERT INTO `wk_admin_role_menu` VALUES (2300818, 180170, 441);
INSERT INTO `wk_admin_role_menu` VALUES (2300819, 180170, 442);
INSERT INTO `wk_admin_role_menu` VALUES (2300820, 180170, 443);
INSERT INTO `wk_admin_role_menu` VALUES (2300821, 180170, 444);
INSERT INTO `wk_admin_role_menu` VALUES (2300822, 180176, 803);
INSERT INTO `wk_admin_role_menu` VALUES (2300823, 180176, 804);
INSERT INTO `wk_admin_role_menu` VALUES (2300824, 180176, 833);
INSERT INTO `wk_admin_role_menu` VALUES (2300825, 180176, 834);
INSERT INTO `wk_admin_role_menu` VALUES (2300826, 180176, 842);
INSERT INTO `wk_admin_role_menu` VALUES (2300827, 180176, 843);
INSERT INTO `wk_admin_role_menu` VALUES (2300828, 180176, 846);
INSERT INTO `wk_admin_role_menu` VALUES (2300829, 180176, 852);
INSERT INTO `wk_admin_role_menu` VALUES (2300830, 180176, 862);
INSERT INTO `wk_admin_role_menu` VALUES (2300831, 180176, 867);
INSERT INTO `wk_admin_role_menu` VALUES (2300832, 180176, 886);
INSERT INTO `wk_admin_role_menu` VALUES (2300833, 180176, 891);

-- ----------------------------
-- Table structure for wk_admin_system_log
-- ----------------------------
DROP TABLE IF EXISTS `wk_admin_system_log`;
CREATE TABLE `wk_admin_system_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_user_id` int(20) NOT NULL COMMENT '操作人id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `ip_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `types` int(4) NULL DEFAULT NULL COMMENT '模块 1企业首页 2应用管理 3员工和部门管理 4名片小程序管理 5角色权限管理 6审批流（合同/回款） 7审批流（办公） 8项目管理 9客户管理 10系统日志管理 11其他设置',
  `behavior` int(4) NULL DEFAULT NULL COMMENT '行为',
  `object` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作对象',
  `detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作详情',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_admin_system_log
-- ----------------------------

-- ----------------------------
-- Table structure for wk_admin_user
-- ----------------------------
DROP TABLE IF EXISTS `wk_admin_user`;
CREATE TABLE `wk_admin_user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '安全符',
  `img` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `realname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `num` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '员工编号',
  `mobile` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机号',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `sex` int(1) NULL DEFAULT NULL COMMENT '0 未选择 1 男 2 女 ',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门',
  `post` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '岗位',
  `status` int(3) NULL DEFAULT 2 COMMENT '状态,0禁用,1正常,2未激活',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '直属上级ID',
  `last_login_time` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后登录IP 注意兼容IPV6',
  `old_user_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  INDEX `parent_id`(`parent_id`) USING BTREE,
  INDEX `dept_id`(`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_admin_user
-- ----------------------------

-- ----------------------------
-- Table structure for wk_admin_user_config
-- ----------------------------
DROP TABLE IF EXISTS `wk_admin_user_config`;
CREATE TABLE `wk_admin_user_config`  (
  `setting_id` int(9) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `status` int(1) NOT NULL DEFAULT 0 COMMENT '状态，0:不启用 1 ： 启用',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '设置名称',
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '值',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`setting_id`) USING BTREE,
  INDEX `name`(`name`, `user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 114574 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_admin_user_config
-- ----------------------------
INSERT INTO `wk_admin_user_config` VALUES (114563, 14773, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `wk_admin_user_config` VALUES (114564, 14773, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `wk_admin_user_config` VALUES (114565, 14773, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `wk_admin_user_config` VALUES (114566, 14773, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `wk_admin_user_config` VALUES (114567, 14773, 1, 'readNotice', '', '升级日志阅读状态');
INSERT INTO `wk_admin_user_config` VALUES (114568, 14773, 1, 'readNotice', '', '升级日志阅读状态');
INSERT INTO `wk_admin_user_config` VALUES (114569, 14774, 1, 'ActivityPhrase', '电话无人接听', '跟进记录常用语');
INSERT INTO `wk_admin_user_config` VALUES (114570, 14774, 1, 'ActivityPhrase', '客户无意向', '跟进记录常用语');
INSERT INTO `wk_admin_user_config` VALUES (114571, 14774, 1, 'ActivityPhrase', '客户意向度适中，后续继续跟进', '跟进记录常用语');
INSERT INTO `wk_admin_user_config` VALUES (114572, 14774, 1, 'ActivityPhrase', '客户意向度较强，成交几率较大', '跟进记录常用语');
INSERT INTO `wk_admin_user_config` VALUES (114573, 14774, 1, 'readNotice', '', '升级日志阅读状态');

-- ----------------------------
-- Table structure for wk_admin_user_his_table
-- ----------------------------
DROP TABLE IF EXISTS `wk_admin_user_his_table`;
CREATE TABLE `wk_admin_user_his_table`  (
  `his_table_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL,
  `his_table` int(1) NULL DEFAULT NULL COMMENT '0 没有 1 有',
  `type` int(1) NULL DEFAULT 1 COMMENT '1.坐席授权 2.设置默认名片 3.关联员工',
  PRIMARY KEY (`his_table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '授权坐席' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_admin_user_his_table
-- ----------------------------

-- ----------------------------
-- Table structure for wk_admin_user_role
-- ----------------------------
DROP TABLE IF EXISTS `wk_admin_user_role`;
CREATE TABLE `wk_admin_user_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19221 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色对应关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_admin_user_role
-- ----------------------------
INSERT INTO `wk_admin_user_role` VALUES (19219, 14773, 180162);
INSERT INTO `wk_admin_user_role` VALUES (19220, 14774, 180162);

-- ----------------------------
-- Table structure for wk_admin_visiting_card
-- ----------------------------
DROP TABLE IF EXISTS `wk_admin_visiting_card`;
CREATE TABLE `wk_admin_visiting_card`  (
  `card_id` int(11) NOT NULL AUTO_INCREMENT,
  `card_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名片名称',
  `create_user_id` bigint(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `user_id` bigint(11) NULL DEFAULT NULL COMMENT '关联员工id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `openid` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `wechat_number` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信号',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网址',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址',
  `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '简介',
  `weixin_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信小程序码',
  `official_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '海报名片',
  PRIMARY KEY (`card_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '名片表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_admin_visiting_card
-- ----------------------------

-- ----------------------------
-- Table structure for wk_call_record
-- ----------------------------
DROP TABLE IF EXISTS `wk_call_record`;
CREATE TABLE `wk_call_record`  (
  `call_record_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 记录id',
  `number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '电话号码',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始振铃时间',
  `answer_time` datetime(0) NULL DEFAULT NULL COMMENT '接通时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `talk_time` int(10) NULL DEFAULT 0 COMMENT '通话时长（秒）',
  `dial_time` int(10) NULL DEFAULT 0 COMMENT '摘机时长',
  `state` int(2) NULL DEFAULT NULL COMMENT '通话状态 (0未振铃，1未接通，2接通，3呼入未接通)',
  `type` int(2) NULL DEFAULT NULL COMMENT '通话类型 (0呼出，1呼入)',
  `model` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联模块 leads，customer，contacts',
  `model_id` int(11) NULL DEFAULT NULL COMMENT '关联模块ID',
  `file_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '录音文件路径',
  `size` int(10) NULL DEFAULT 0 COMMENT '录音文件大小',
  `file_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件名称',
  `call_upload` tinyint(1) NULL DEFAULT 0 COMMENT '0：CRM服务器; 1：上传至阿里云',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `owner_user_id` bigint(20) NULL DEFAULT NULL COMMENT '负责人ID',
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次',
  PRIMARY KEY (`call_record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通话记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_call_record
-- ----------------------------

-- ----------------------------
-- Table structure for wk_card_weixin_browse
-- ----------------------------
DROP TABLE IF EXISTS `wk_card_weixin_browse`;
CREATE TABLE `wk_card_weixin_browse`  (
  `browse_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '名片表',
  `weixin_leads_id` bigint(20) NULL DEFAULT NULL COMMENT '微信线索表',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `num` int(11) NULL DEFAULT 0 COMMENT '浏览次数',
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`browse_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信浏览名片表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_card_weixin_browse
-- ----------------------------

-- ----------------------------
-- Table structure for wk_card_weixin_leads
-- ----------------------------
DROP TABLE IF EXISTS `wk_card_weixin_leads`;
CREATE TABLE `wk_card_weixin_leads`  (
  `weixin_leads_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `is_transform` int(1) NULL DEFAULT 0 COMMENT '1已转化 0 未转化',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `owner_user_id` bigint(20) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `weixin_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信头像',
  `weixin_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信名称',
  `weixin_number` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信号',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `openid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信唯一标识',
  `sex` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`weixin_leads_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信线索' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_card_weixin_leads
-- ----------------------------

-- ----------------------------
-- Table structure for wk_card_weixin_leads_user
-- ----------------------------
DROP TABLE IF EXISTS `wk_card_weixin_leads_user`;
CREATE TABLE `wk_card_weixin_leads_user`  (
  `weixin_user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '被关注的员工id',
  `weixin_leads_id` bigint(20) NULL DEFAULT NULL COMMENT '微信线索id',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `relevance_user_id` bigint(20) NULL DEFAULT NULL COMMENT '员工id',
  PRIMARY KEY (`weixin_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '名片夹' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_card_weixin_leads_user
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_achievement
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_achievement`;
CREATE TABLE `wk_crm_achievement`  (
  `achievement_id` int(11) NOT NULL AUTO_INCREMENT,
  `obj_id` int(11) NULL DEFAULT NULL COMMENT '对象ID',
  `type` int(2) NULL DEFAULT 0 COMMENT '1公司2部门3员工',
  `year` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '年',
  `january` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '一月',
  `february` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '二月',
  `march` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '三月',
  `april` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '四月',
  `may` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '五月',
  `june` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '六月',
  `july` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '七月',
  `august` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '八月',
  `september` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '九月',
  `october` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '十月',
  `november` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '十一月',
  `december` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '十二月',
  `status` int(2) NULL DEFAULT NULL COMMENT '1销售（目标）2回款（目标）',
  `yeartarget` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '年目标',
  PRIMARY KEY (`achievement_id`) USING BTREE,
  INDEX `obj_id`(`obj_id`, `type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '业绩目标' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_achievement
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_action_record
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_action_record`;
CREATE TABLE `wk_crm_action_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_user_id` bigint(20) NOT NULL COMMENT '操作人ID',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `ip_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `types` int(4) NOT NULL COMMENT '模块类型',
  `action_id` int(11) NULL DEFAULT NULL COMMENT '被操作对象ID',
  `object` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '对象',
  `behavior` int(4) NULL DEFAULT NULL COMMENT '行为',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `detail` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '详情',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `types`(`types`, `action_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '字段操作记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_action_record
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_activity
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_activity`;
CREATE TABLE `wk_crm_activity`  (
  `activity_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '活动id',
  `type` int(1) NULL DEFAULT NULL COMMENT '活动类型 1 跟进记录 2 创建记录 3 商机阶段变更 4 外勤签到',
  `category` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跟进类型',
  `activity_type` int(1) NOT NULL COMMENT '活动类型 1 线索 2 客户 3 联系人 4 产品 5 商机 6 合同 7回款 8日志 9审批 10日程 11任务 12 发邮件',
  `activity_type_id` int(11) NOT NULL COMMENT '活动类型Id',
  `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动内容',
  `business_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联商机',
  `contacts_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联联系人',
  `next_time` datetime(0) NULL DEFAULT NULL COMMENT '下次联系时间',
  `status` int(2) NULL DEFAULT 1 COMMENT '0 删除 1 未删除',
  `lng` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '经度',
  `lat` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '纬度',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '签到地址',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次id',
  PRIMARY KEY (`activity_id`) USING BTREE,
  INDEX `wk_crm_activity_type_activity_type_index`(`type`, `activity_type`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'crm活动表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_activity
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_activity_relation
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_activity_relation`;
CREATE TABLE `wk_crm_activity_relation`  (
  `r_id` int(11) NOT NULL AUTO_INCREMENT,
  `activity_id` int(11) NOT NULL,
  `type` int(1) NOT NULL COMMENT '3 联系人 5 商机',
  `type_id` int(11) NOT NULL,
  PRIMARY KEY (`r_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '活动关联商机联系人表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_activity_relation
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_area
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_area`;
CREATE TABLE `wk_crm_area`  (
  `code_id` int(11) NULL DEFAULT NULL,
  `parent_id` int(11) NULL DEFAULT NULL,
  `city_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '地名表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_area
-- ----------------------------
INSERT INTO `wk_crm_area` VALUES (513222, 513200, '理县');
INSERT INTO `wk_crm_area` VALUES (513233, 513200, '红原县');
INSERT INTO `wk_crm_area` VALUES (513221, 513200, '汶川县');
INSERT INTO `wk_crm_area` VALUES (513232, 513200, '若尔盖县');
INSERT INTO `wk_crm_area` VALUES (513231, 513200, '阿坝县');
INSERT INTO `wk_crm_area` VALUES (513230, 513200, '壤塘县');
INSERT INTO `wk_crm_area` VALUES (513226, 513200, '金川县');
INSERT INTO `wk_crm_area` VALUES (513225, 513200, '九寨沟县');
INSERT INTO `wk_crm_area` VALUES (513224, 513200, '松潘县');
INSERT INTO `wk_crm_area` VALUES (513201, 513200, '马尔康市');
INSERT INTO `wk_crm_area` VALUES (513223, 513200, '茂县');
INSERT INTO `wk_crm_area` VALUES (513228, 513200, '黑水县');
INSERT INTO `wk_crm_area` VALUES (513227, 513200, '小金县');
INSERT INTO `wk_crm_area` VALUES (712899, 712800, '莒光乡');
INSERT INTO `wk_crm_area` VALUES (712896, 712800, '南竿乡');
INSERT INTO `wk_crm_area` VALUES (712897, 712800, '北竿乡');
INSERT INTO `wk_crm_area` VALUES (712898, 712800, '东引乡');
INSERT INTO `wk_crm_area` VALUES (632822, 632800, '都兰县');
INSERT INTO `wk_crm_area` VALUES (632821, 632800, '乌兰县');
INSERT INTO `wk_crm_area` VALUES (632803, 632800, '茫崖市');
INSERT INTO `wk_crm_area` VALUES (632802, 632800, '德令哈市');
INSERT INTO `wk_crm_area` VALUES (632857, 632800, '大柴旦行政委员会');
INSERT INTO `wk_crm_area` VALUES (632801, 632800, '格尔木市');
INSERT INTO `wk_crm_area` VALUES (632823, 632800, '天峻县');
INSERT INTO `wk_crm_area` VALUES (370982, 370900, '新泰市');
INSERT INTO `wk_crm_area` VALUES (370921, 370900, '宁阳县');
INSERT INTO `wk_crm_area` VALUES (370911, 370900, '岱岳区');
INSERT INTO `wk_crm_area` VALUES (370923, 370900, '东平县');
INSERT INTO `wk_crm_area` VALUES (370902, 370900, '泰山区');
INSERT INTO `wk_crm_area` VALUES (370983, 370900, '肥城市');
INSERT INTO `wk_crm_area` VALUES (421127, 421100, '黄梅县');
INSERT INTO `wk_crm_area` VALUES (421126, 421100, '蕲春县');
INSERT INTO `wk_crm_area` VALUES (421125, 421100, '浠水县');
INSERT INTO `wk_crm_area` VALUES (421102, 421100, '黄州区');
INSERT INTO `wk_crm_area` VALUES (421124, 421100, '英山县');
INSERT INTO `wk_crm_area` VALUES (421181, 421100, '麻城市');
INSERT INTO `wk_crm_area` VALUES (421171, 421100, '龙感湖管理区');
INSERT INTO `wk_crm_area` VALUES (421182, 421100, '武穴市');
INSERT INTO `wk_crm_area` VALUES (421123, 421100, '罗田县');
INSERT INTO `wk_crm_area` VALUES (421122, 421100, '红安县');
INSERT INTO `wk_crm_area` VALUES (421121, 421100, '团风县');
INSERT INTO `wk_crm_area` VALUES (330282, 330200, '慈溪市');
INSERT INTO `wk_crm_area` VALUES (330206, 330200, '北仑区');
INSERT INTO `wk_crm_area` VALUES (330281, 330200, '余姚市');
INSERT INTO `wk_crm_area` VALUES (330213, 330200, '奉化区');
INSERT INTO `wk_crm_area` VALUES (330203, 330200, '海曙区');
INSERT INTO `wk_crm_area` VALUES (330225, 330200, '象山县');
INSERT INTO `wk_crm_area` VALUES (330226, 330200, '宁海县');
INSERT INTO `wk_crm_area` VALUES (330205, 330200, '江北区');
INSERT INTO `wk_crm_area` VALUES (330211, 330200, '镇海区');
INSERT INTO `wk_crm_area` VALUES (330212, 330200, '鄞州区');
INSERT INTO `wk_crm_area` VALUES (640381, 640300, '青铜峡市');
INSERT INTO `wk_crm_area` VALUES (640302, 640300, '利通区');
INSERT INTO `wk_crm_area` VALUES (640324, 640300, '同心县');
INSERT INTO `wk_crm_area` VALUES (640323, 640300, '盐池县');
INSERT INTO `wk_crm_area` VALUES (640303, 640300, '红寺堡区');
INSERT INTO `wk_crm_area` VALUES (650500, 650000, '哈密市');
INSERT INTO `wk_crm_area` VALUES (650400, 650000, '吐鲁番市');
INSERT INTO `wk_crm_area` VALUES (650200, 650000, '克拉玛依市');
INSERT INTO `wk_crm_area` VALUES (650100, 650000, '乌鲁木齐市');
INSERT INTO `wk_crm_area` VALUES (652300, 650000, '昌吉回族自治州');
INSERT INTO `wk_crm_area` VALUES (652700, 650000, '博尔塔拉蒙古自治州');
INSERT INTO `wk_crm_area` VALUES (652800, 650000, '巴音郭楞蒙古自治州');
INSERT INTO `wk_crm_area` VALUES (652900, 650000, '阿克苏地区');
INSERT INTO `wk_crm_area` VALUES (654000, 650000, '伊犁哈萨克自治州');
INSERT INTO `wk_crm_area` VALUES (653000, 650000, '克孜勒苏柯尔克孜自治州');
INSERT INTO `wk_crm_area` VALUES (653100, 650000, '喀什地区');
INSERT INTO `wk_crm_area` VALUES (654200, 650000, '塔城地区');
INSERT INTO `wk_crm_area` VALUES (653200, 650000, '和田地区');
INSERT INTO `wk_crm_area` VALUES (654300, 650000, '阿勒泰地区');
INSERT INTO `wk_crm_area` VALUES (659000, 650000, '自治区直辖县级行政区划');
INSERT INTO `wk_crm_area` VALUES (411328, 411300, '唐河县');
INSERT INTO `wk_crm_area` VALUES (411327, 411300, '社旗县');
INSERT INTO `wk_crm_area` VALUES (411329, 411300, '新野县');
INSERT INTO `wk_crm_area` VALUES (411302, 411300, '宛城区');
INSERT INTO `wk_crm_area` VALUES (411324, 411300, '镇平县');
INSERT INTO `wk_crm_area` VALUES (411323, 411300, '西峡县');
INSERT INTO `wk_crm_area` VALUES (411326, 411300, '淅川县');
INSERT INTO `wk_crm_area` VALUES (411303, 411300, '卧龙区');
INSERT INTO `wk_crm_area` VALUES (411325, 411300, '内乡县');
INSERT INTO `wk_crm_area` VALUES (411330, 411300, '桐柏县');
INSERT INTO `wk_crm_area` VALUES (411322, 411300, '方城县');
INSERT INTO `wk_crm_area` VALUES (411321, 411300, '南召县');
INSERT INTO `wk_crm_area` VALUES (411371, 411300, '南阳高新技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (411381, 411300, '邓州市');
INSERT INTO `wk_crm_area` VALUES (411372, 411300, '南阳市城乡一体化示范区');
INSERT INTO `wk_crm_area` VALUES (340700, 340000, '铜陵市');
INSERT INTO `wk_crm_area` VALUES (340800, 340000, '安庆市');
INSERT INTO `wk_crm_area` VALUES (341800, 340000, '宣城市');
INSERT INTO `wk_crm_area` VALUES (340100, 340000, '合肥市');
INSERT INTO `wk_crm_area` VALUES (341200, 340000, '阜阳市');
INSERT INTO `wk_crm_area` VALUES (340200, 340000, '芜湖市');
INSERT INTO `wk_crm_area` VALUES (341300, 340000, '宿州市');
INSERT INTO `wk_crm_area` VALUES (341000, 340000, '黄山市');
INSERT INTO `wk_crm_area` VALUES (341100, 340000, '滁州市');
INSERT INTO `wk_crm_area` VALUES (340500, 340000, '马鞍山市');
INSERT INTO `wk_crm_area` VALUES (341700, 340000, '池州市');
INSERT INTO `wk_crm_area` VALUES (340600, 340000, '淮北市');
INSERT INTO `wk_crm_area` VALUES (341600, 340000, '亳州市');
INSERT INTO `wk_crm_area` VALUES (340300, 340000, '蚌埠市');
INSERT INTO `wk_crm_area` VALUES (341500, 340000, '六安市');
INSERT INTO `wk_crm_area` VALUES (340400, 340000, '淮南市');
INSERT INTO `wk_crm_area` VALUES (810100, 810000, '香港城区');
INSERT INTO `wk_crm_area` VALUES (711777, 711700, '秀水乡');
INSERT INTO `wk_crm_area` VALUES (711799, 711700, '二水乡');
INSERT INTO `wk_crm_area` VALUES (711778, 711700, '鹿港镇');
INSERT INTO `wk_crm_area` VALUES (711779, 711700, '福兴乡');
INSERT INTO `wk_crm_area` VALUES (711784, 711700, '社头乡');
INSERT INTO `wk_crm_area` VALUES (711785, 711700, '永靖乡');
INSERT INTO `wk_crm_area` VALUES (711786, 711700, '埔心乡');
INSERT INTO `wk_crm_area` VALUES (711787, 711700, '溪湖镇');
INSERT INTO `wk_crm_area` VALUES (711780, 711700, '线西乡');
INSERT INTO `wk_crm_area` VALUES (711781, 711700, '和美镇');
INSERT INTO `wk_crm_area` VALUES (711782, 711700, '伸港乡');
INSERT INTO `wk_crm_area` VALUES (711783, 711700, '员林镇');
INSERT INTO `wk_crm_area` VALUES (711788, 711700, '大村乡');
INSERT INTO `wk_crm_area` VALUES (711789, 711700, '埔盐乡');
INSERT INTO `wk_crm_area` VALUES (711790, 711700, '田中镇');
INSERT INTO `wk_crm_area` VALUES (711795, 711700, '竹塘乡');
INSERT INTO `wk_crm_area` VALUES (711774, 711700, '彰化市');
INSERT INTO `wk_crm_area` VALUES (711796, 711700, '二林镇');
INSERT INTO `wk_crm_area` VALUES (711775, 711700, '芬园乡');
INSERT INTO `wk_crm_area` VALUES (711797, 711700, '大城乡');
INSERT INTO `wk_crm_area` VALUES (711776, 711700, '花坛乡');
INSERT INTO `wk_crm_area` VALUES (711798, 711700, '芳苑乡');
INSERT INTO `wk_crm_area` VALUES (711791, 711700, '北斗镇');
INSERT INTO `wk_crm_area` VALUES (711792, 711700, '田尾乡');
INSERT INTO `wk_crm_area` VALUES (711793, 711700, '埤头乡');
INSERT INTO `wk_crm_area` VALUES (711794, 711700, '溪州乡');
INSERT INTO `wk_crm_area` VALUES (650107, 650100, '达坂城区');
INSERT INTO `wk_crm_area` VALUES (650106, 650100, '头屯河区');
INSERT INTO `wk_crm_area` VALUES (650105, 650100, '水磨沟区');
INSERT INTO `wk_crm_area` VALUES (650104, 650100, '新市区');
INSERT INTO `wk_crm_area` VALUES (650103, 650100, '沙依巴克区');
INSERT INTO `wk_crm_area` VALUES (650102, 650100, '天山区');
INSERT INTO `wk_crm_area` VALUES (650121, 650100, '乌鲁木齐县');
INSERT INTO `wk_crm_area` VALUES (650172, 650100, '乌鲁木齐高新技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (650171, 650100, '乌鲁木齐经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (650109, 650100, '米东区');
INSERT INTO `wk_crm_area` VALUES (445303, 445300, '云安区');
INSERT INTO `wk_crm_area` VALUES (445302, 445300, '云城区');
INSERT INTO `wk_crm_area` VALUES (445381, 445300, '罗定市');
INSERT INTO `wk_crm_area` VALUES (445321, 445300, '新兴县');
INSERT INTO `wk_crm_area` VALUES (445322, 445300, '郁南县');
INSERT INTO `wk_crm_area` VALUES (500200, 500000, '县');
INSERT INTO `wk_crm_area` VALUES (500100, 500000, '市辖区');
INSERT INTO `wk_crm_area` VALUES (530921, 530900, '凤庆县');
INSERT INTO `wk_crm_area` VALUES (530922, 530900, '云县');
INSERT INTO `wk_crm_area` VALUES (530927, 530900, '沧源佤族自治县');
INSERT INTO `wk_crm_area` VALUES (530925, 530900, '双江拉祜族佤族布朗族傣族自治县');
INSERT INTO `wk_crm_area` VALUES (530926, 530900, '耿马傣族佤族自治县');
INSERT INTO `wk_crm_area` VALUES (530923, 530900, '永德县');
INSERT INTO `wk_crm_area` VALUES (530902, 530900, '临翔区');
INSERT INTO `wk_crm_area` VALUES (530924, 530900, '镇康县');
INSERT INTO `wk_crm_area` VALUES (150626, 150600, '乌审旗');
INSERT INTO `wk_crm_area` VALUES (150627, 150600, '伊金霍洛旗');
INSERT INTO `wk_crm_area` VALUES (150602, 150600, '东胜区');
INSERT INTO `wk_crm_area` VALUES (150624, 150600, '鄂托克旗');
INSERT INTO `wk_crm_area` VALUES (150603, 150600, '康巴什区');
INSERT INTO `wk_crm_area` VALUES (150625, 150600, '杭锦旗');
INSERT INTO `wk_crm_area` VALUES (150622, 150600, '准格尔旗');
INSERT INTO `wk_crm_area` VALUES (150623, 150600, '鄂托克前旗');
INSERT INTO `wk_crm_area` VALUES (150621, 150600, '达拉特旗');
INSERT INTO `wk_crm_area` VALUES (152923, 152900, '额济纳旗');
INSERT INTO `wk_crm_area` VALUES (152922, 152900, '阿拉善右旗');
INSERT INTO `wk_crm_area` VALUES (152921, 152900, '阿拉善左旗');
INSERT INTO `wk_crm_area` VALUES (152971, 152900, '内蒙古阿拉善经济开发区');
INSERT INTO `wk_crm_area` VALUES (341271, 341200, '阜阳合肥现代产业园区');
INSERT INTO `wk_crm_area` VALUES (341282, 341200, '界首市');
INSERT INTO `wk_crm_area` VALUES (341272, 341200, '阜阳经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (341222, 341200, '太和县');
INSERT INTO `wk_crm_area` VALUES (341221, 341200, '临泉县');
INSERT INTO `wk_crm_area` VALUES (341204, 341200, '颍泉区');
INSERT INTO `wk_crm_area` VALUES (341226, 341200, '颍上县');
INSERT INTO `wk_crm_area` VALUES (341202, 341200, '颍州区');
INSERT INTO `wk_crm_area` VALUES (341203, 341200, '颍东区');
INSERT INTO `wk_crm_area` VALUES (341225, 341200, '阜南县');
INSERT INTO `wk_crm_area` VALUES (620881, 620800, '华亭市');
INSERT INTO `wk_crm_area` VALUES (620826, 620800, '静宁县');
INSERT INTO `wk_crm_area` VALUES (620822, 620800, '灵台县');
INSERT INTO `wk_crm_area` VALUES (620823, 620800, '崇信县');
INSERT INTO `wk_crm_area` VALUES (620802, 620800, '崆峒区');
INSERT INTO `wk_crm_area` VALUES (620825, 620800, '庄浪县');
INSERT INTO `wk_crm_area` VALUES (620821, 620800, '泾川县');
INSERT INTO `wk_crm_area` VALUES (140827, 140800, '垣曲县');
INSERT INTO `wk_crm_area` VALUES (140828, 140800, '夏县');
INSERT INTO `wk_crm_area` VALUES (140829, 140800, '平陆县');
INSERT INTO `wk_crm_area` VALUES (140881, 140800, '永济市');
INSERT INTO `wk_crm_area` VALUES (140882, 140800, '河津市');
INSERT INTO `wk_crm_area` VALUES (140830, 140800, '芮城县');
INSERT INTO `wk_crm_area` VALUES (140821, 140800, '临猗县');
INSERT INTO `wk_crm_area` VALUES (140822, 140800, '万荣县');
INSERT INTO `wk_crm_area` VALUES (140823, 140800, '闻喜县');
INSERT INTO `wk_crm_area` VALUES (140802, 140800, '盐湖区');
INSERT INTO `wk_crm_area` VALUES (140824, 140800, '稷山县');
INSERT INTO `wk_crm_area` VALUES (140825, 140800, '新绛县');
INSERT INTO `wk_crm_area` VALUES (140826, 140800, '绛县');
INSERT INTO `wk_crm_area` VALUES (410106, 410100, '上街区');
INSERT INTO `wk_crm_area` VALUES (410108, 410100, '惠济区');
INSERT INTO `wk_crm_area` VALUES (410103, 410100, '二七区');
INSERT INTO `wk_crm_area` VALUES (410102, 410100, '中原区');
INSERT INTO `wk_crm_area` VALUES (410105, 410100, '金水区');
INSERT INTO `wk_crm_area` VALUES (410104, 410100, '管城回族区');
INSERT INTO `wk_crm_area` VALUES (410181, 410100, '巩义市');
INSERT INTO `wk_crm_area` VALUES (410122, 410100, '中牟县');
INSERT INTO `wk_crm_area` VALUES (410172, 410100, '郑州高新技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (410183, 410100, '新密市');
INSERT INTO `wk_crm_area` VALUES (410171, 410100, '郑州经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (410182, 410100, '荥阳市');
INSERT INTO `wk_crm_area` VALUES (410185, 410100, '登封市');
INSERT INTO `wk_crm_area` VALUES (410173, 410100, '郑州航空港经济综合实验区');
INSERT INTO `wk_crm_area` VALUES (410184, 410100, '新郑市');
INSERT INTO `wk_crm_area` VALUES (710512, 710500, '乌坵乡');
INSERT INTO `wk_crm_area` VALUES (710508, 710500, '金湖镇');
INSERT INTO `wk_crm_area` VALUES (710507, 710500, '金沙镇');
INSERT INTO `wk_crm_area` VALUES (710509, 710500, '金宁乡');
INSERT INTO `wk_crm_area` VALUES (710511, 710500, '烈屿乡');
INSERT INTO `wk_crm_area` VALUES (710510, 710500, '金城镇');
INSERT INTO `wk_crm_area` VALUES (320481, 320400, '溧阳市');
INSERT INTO `wk_crm_area` VALUES (320404, 320400, '钟楼区');
INSERT INTO `wk_crm_area` VALUES (320412, 320400, '武进区');
INSERT INTO `wk_crm_area` VALUES (320402, 320400, '天宁区');
INSERT INTO `wk_crm_area` VALUES (320413, 320400, '金坛区');
INSERT INTO `wk_crm_area` VALUES (320411, 320400, '新北区');
INSERT INTO `wk_crm_area` VALUES (230709, 230700, '金山屯区');
INSERT INTO `wk_crm_area` VALUES (230708, 230700, '美溪区');
INSERT INTO `wk_crm_area` VALUES (230710, 230700, '五营区');
INSERT INTO `wk_crm_area` VALUES (230781, 230700, '铁力市');
INSERT INTO `wk_crm_area` VALUES (230707, 230700, '新青区');
INSERT INTO `wk_crm_area` VALUES (230706, 230700, '翠峦区');
INSERT INTO `wk_crm_area` VALUES (230705, 230700, '西林区');
INSERT INTO `wk_crm_area` VALUES (230716, 230700, '上甘岭区');
INSERT INTO `wk_crm_area` VALUES (230704, 230700, '友好区');
INSERT INTO `wk_crm_area` VALUES (230715, 230700, '红星区');
INSERT INTO `wk_crm_area` VALUES (230703, 230700, '南岔区');
INSERT INTO `wk_crm_area` VALUES (230714, 230700, '乌伊岭区');
INSERT INTO `wk_crm_area` VALUES (230702, 230700, '伊春区');
INSERT INTO `wk_crm_area` VALUES (230713, 230700, '带岭区');
INSERT INTO `wk_crm_area` VALUES (230712, 230700, '汤旺河区');
INSERT INTO `wk_crm_area` VALUES (230711, 230700, '乌马河区');
INSERT INTO `wk_crm_area` VALUES (230722, 230700, '嘉荫县');
INSERT INTO `wk_crm_area` VALUES (110100, 110000, '北京城区');
INSERT INTO `wk_crm_area` VALUES (370881, 370800, '曲阜市');
INSERT INTO `wk_crm_area` VALUES (370871, 370800, '济宁高新技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (370883, 370800, '邹城市');
INSERT INTO `wk_crm_area` VALUES (370826, 370800, '微山县');
INSERT INTO `wk_crm_area` VALUES (370827, 370800, '鱼台县');
INSERT INTO `wk_crm_area` VALUES (370828, 370800, '金乡县');
INSERT INTO `wk_crm_area` VALUES (370829, 370800, '嘉祥县');
INSERT INTO `wk_crm_area` VALUES (370811, 370800, '任城区');
INSERT INTO `wk_crm_area` VALUES (370812, 370800, '兖州区');
INSERT INTO `wk_crm_area` VALUES (370830, 370800, '汶上县');
INSERT INTO `wk_crm_area` VALUES (370831, 370800, '泗水县');
INSERT INTO `wk_crm_area` VALUES (370832, 370800, '梁山县');
INSERT INTO `wk_crm_area` VALUES (632701, 632700, '玉树市');
INSERT INTO `wk_crm_area` VALUES (632723, 632700, '称多县');
INSERT INTO `wk_crm_area` VALUES (632722, 632700, '杂多县');
INSERT INTO `wk_crm_area` VALUES (632726, 632700, '曲麻莱县');
INSERT INTO `wk_crm_area` VALUES (632725, 632700, '囊谦县');
INSERT INTO `wk_crm_area` VALUES (632724, 632700, '治多县');
INSERT INTO `wk_crm_area` VALUES (421003, 421000, '荆州区');
INSERT INTO `wk_crm_area` VALUES (421071, 421000, '荆州经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (421081, 421000, '石首市');
INSERT INTO `wk_crm_area` VALUES (421083, 421000, '洪湖市');
INSERT INTO `wk_crm_area` VALUES (421002, 421000, '沙市区');
INSERT INTO `wk_crm_area` VALUES (421024, 421000, '江陵县');
INSERT INTO `wk_crm_area` VALUES (421023, 421000, '监利县');
INSERT INTO `wk_crm_area` VALUES (421022, 421000, '公安县');
INSERT INTO `wk_crm_area` VALUES (421087, 421000, '松滋市');
INSERT INTO `wk_crm_area` VALUES (330381, 330300, '瑞安市');
INSERT INTO `wk_crm_area` VALUES (330371, 330300, '温州经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (330382, 330300, '乐清市');
INSERT INTO `wk_crm_area` VALUES (330305, 330300, '洞头区');
INSERT INTO `wk_crm_area` VALUES (330327, 330300, '苍南县');
INSERT INTO `wk_crm_area` VALUES (330328, 330300, '文成县');
INSERT INTO `wk_crm_area` VALUES (330329, 330300, '泰顺县');
INSERT INTO `wk_crm_area` VALUES (330302, 330300, '鹿城区');
INSERT INTO `wk_crm_area` VALUES (330324, 330300, '永嘉县');
INSERT INTO `wk_crm_area` VALUES (330303, 330300, '龙湾区');
INSERT INTO `wk_crm_area` VALUES (330304, 330300, '瓯海区');
INSERT INTO `wk_crm_area` VALUES (330326, 330300, '平阳县');
INSERT INTO `wk_crm_area` VALUES (340181, 340100, '巢湖市');
INSERT INTO `wk_crm_area` VALUES (340173, 340100, '合肥新站高新技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (340171, 340100, '合肥高新技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (340172, 340100, '合肥经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (340111, 340100, '包河区');
INSERT INTO `wk_crm_area` VALUES (340122, 340100, '肥东县');
INSERT INTO `wk_crm_area` VALUES (340123, 340100, '肥西县');
INSERT INTO `wk_crm_area` VALUES (340121, 340100, '长丰县');
INSERT INTO `wk_crm_area` VALUES (340104, 340100, '蜀山区');
INSERT INTO `wk_crm_area` VALUES (340102, 340100, '瑶海区');
INSERT INTO `wk_crm_area` VALUES (340124, 340100, '庐江县');
INSERT INTO `wk_crm_area` VALUES (340103, 340100, '庐阳区');
INSERT INTO `wk_crm_area` VALUES (640221, 640200, '平罗县');
INSERT INTO `wk_crm_area` VALUES (640202, 640200, '大武口区');
INSERT INTO `wk_crm_area` VALUES (640205, 640200, '惠农区');
INSERT INTO `wk_crm_area` VALUES (711590, 711500, '苗栗市');
INSERT INTO `wk_crm_area` VALUES (711591, 711500, '造桥乡');
INSERT INTO `wk_crm_area` VALUES (711592, 711500, '头屋乡');
INSERT INTO `wk_crm_area` VALUES (711586, 711500, '狮潭乡');
INSERT INTO `wk_crm_area` VALUES (711597, 711500, '三义乡');
INSERT INTO `wk_crm_area` VALUES (711587, 711500, '后龙镇');
INSERT INTO `wk_crm_area` VALUES (711598, 711500, '西湖乡');
INSERT INTO `wk_crm_area` VALUES (711588, 711500, '通霄镇');
INSERT INTO `wk_crm_area` VALUES (711599, 711500, '卓兰镇');
INSERT INTO `wk_crm_area` VALUES (711589, 711500, '苑里镇');
INSERT INTO `wk_crm_area` VALUES (711582, 711500, '竹南镇');
INSERT INTO `wk_crm_area` VALUES (711593, 711500, '公馆乡');
INSERT INTO `wk_crm_area` VALUES (711583, 711500, '头份镇');
INSERT INTO `wk_crm_area` VALUES (711594, 711500, '大湖乡');
INSERT INTO `wk_crm_area` VALUES (711584, 711500, '三湾乡');
INSERT INTO `wk_crm_area` VALUES (711595, 711500, '泰安乡');
INSERT INTO `wk_crm_area` VALUES (711585, 711500, '南庄乡');
INSERT INTO `wk_crm_area` VALUES (711596, 711500, '铜锣乡');
INSERT INTO `wk_crm_area` VALUES (411281, 411200, '义马市');
INSERT INTO `wk_crm_area` VALUES (411203, 411200, '陕州区');
INSERT INTO `wk_crm_area` VALUES (411202, 411200, '湖滨区');
INSERT INTO `wk_crm_area` VALUES (411224, 411200, '卢氏县');
INSERT INTO `wk_crm_area` VALUES (411221, 411200, '渑池县');
INSERT INTO `wk_crm_area` VALUES (411271, 411200, '河南三门峡经济开发区');
INSERT INTO `wk_crm_area` VALUES (411282, 411200, '灵宝市');
INSERT INTO `wk_crm_area` VALUES (512022, 512000, '乐至县');
INSERT INTO `wk_crm_area` VALUES (512021, 512000, '安岳县');
INSERT INTO `wk_crm_area` VALUES (512002, 512000, '雁江区');
INSERT INTO `wk_crm_area` VALUES (220822, 220800, '通榆县');
INSERT INTO `wk_crm_area` VALUES (220802, 220800, '洮北区');
INSERT INTO `wk_crm_area` VALUES (220881, 220800, '洮南市');
INSERT INTO `wk_crm_area` VALUES (220871, 220800, '吉林白城经济开发区');
INSERT INTO `wk_crm_area` VALUES (220882, 220800, '大安市');
INSERT INTO `wk_crm_area` VALUES (220821, 220800, '镇赉县');
INSERT INTO `wk_crm_area` VALUES (810110, 810100, '荃湾区');
INSERT INTO `wk_crm_area` VALUES (810111, 810100, '屯门区');
INSERT INTO `wk_crm_area` VALUES (810101, 810100, '中西区');
INSERT INTO `wk_crm_area` VALUES (810112, 810100, '元朗区');
INSERT INTO `wk_crm_area` VALUES (810102, 810100, '湾仔区');
INSERT INTO `wk_crm_area` VALUES (810113, 810100, '北区');
INSERT INTO `wk_crm_area` VALUES (810103, 810100, '东区');
INSERT INTO `wk_crm_area` VALUES (810114, 810100, '大埔区');
INSERT INTO `wk_crm_area` VALUES (810104, 810100, '南区');
INSERT INTO `wk_crm_area` VALUES (810115, 810100, '西贡区');
INSERT INTO `wk_crm_area` VALUES (810105, 810100, '油尖旺区');
INSERT INTO `wk_crm_area` VALUES (810116, 810100, '沙田区');
INSERT INTO `wk_crm_area` VALUES (810106, 810100, '深水埗区');
INSERT INTO `wk_crm_area` VALUES (810117, 810100, '葵青区');
INSERT INTO `wk_crm_area` VALUES (810107, 810100, '九龙城区');
INSERT INTO `wk_crm_area` VALUES (810118, 810100, '离岛区');
INSERT INTO `wk_crm_area` VALUES (810108, 810100, '黄大仙区');
INSERT INTO `wk_crm_area` VALUES (810109, 810100, '观塘区');
INSERT INTO `wk_crm_area` VALUES (640181, 640100, '灵武市');
INSERT INTO `wk_crm_area` VALUES (640122, 640100, '贺兰县');
INSERT INTO `wk_crm_area` VALUES (640121, 640100, '永宁县');
INSERT INTO `wk_crm_area` VALUES (640104, 640100, '兴庆区');
INSERT INTO `wk_crm_area` VALUES (640106, 640100, '金凤区');
INSERT INTO `wk_crm_area` VALUES (640105, 640100, '西夏区');
INSERT INTO `wk_crm_area` VALUES (652301, 652300, '昌吉市');
INSERT INTO `wk_crm_area` VALUES (652323, 652300, '呼图壁县');
INSERT INTO `wk_crm_area` VALUES (652302, 652300, '阜康市');
INSERT INTO `wk_crm_area` VALUES (652324, 652300, '玛纳斯县');
INSERT INTO `wk_crm_area` VALUES (652325, 652300, '奇台县');
INSERT INTO `wk_crm_area` VALUES (652327, 652300, '吉木萨尔县');
INSERT INTO `wk_crm_area` VALUES (652328, 652300, '木垒哈萨克自治县');
INSERT INTO `wk_crm_area` VALUES (130972, 130900, '沧州高新技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (130983, 130900, '黄骅市');
INSERT INTO `wk_crm_area` VALUES (130973, 130900, '沧州渤海新区');
INSERT INTO `wk_crm_area` VALUES (130984, 130900, '河间市');
INSERT INTO `wk_crm_area` VALUES (130981, 130900, '泊头市');
INSERT INTO `wk_crm_area` VALUES (130971, 130900, '河北沧州经济开发区');
INSERT INTO `wk_crm_area` VALUES (130982, 130900, '任丘市');
INSERT INTO `wk_crm_area` VALUES (130921, 130900, '沧县');
INSERT INTO `wk_crm_area` VALUES (130922, 130900, '青县');
INSERT INTO `wk_crm_area` VALUES (130930, 130900, '孟村回族自治县');
INSERT INTO `wk_crm_area` VALUES (130903, 130900, '运河区');
INSERT INTO `wk_crm_area` VALUES (130925, 130900, '盐山县');
INSERT INTO `wk_crm_area` VALUES (130926, 130900, '肃宁县');
INSERT INTO `wk_crm_area` VALUES (130923, 130900, '东光县');
INSERT INTO `wk_crm_area` VALUES (130902, 130900, '新华区');
INSERT INTO `wk_crm_area` VALUES (130924, 130900, '海兴县');
INSERT INTO `wk_crm_area` VALUES (130929, 130900, '献县');
INSERT INTO `wk_crm_area` VALUES (130927, 130900, '南皮县');
INSERT INTO `wk_crm_area` VALUES (130928, 130900, '吴桥县');
INSERT INTO `wk_crm_area` VALUES (445203, 445200, '揭东区');
INSERT INTO `wk_crm_area` VALUES (445281, 445200, '普宁市');
INSERT INTO `wk_crm_area` VALUES (445222, 445200, '揭西县');
INSERT INTO `wk_crm_area` VALUES (445202, 445200, '榕城区');
INSERT INTO `wk_crm_area` VALUES (445224, 445200, '惠来县');
INSERT INTO `wk_crm_area` VALUES (230622, 230600, '肇源县');
INSERT INTO `wk_crm_area` VALUES (230621, 230600, '肇州县');
INSERT INTO `wk_crm_area` VALUES (230671, 230600, '大庆高新技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (230606, 230600, '大同区');
INSERT INTO `wk_crm_area` VALUES (230605, 230600, '红岗区');
INSERT INTO `wk_crm_area` VALUES (230604, 230600, '让胡路区');
INSERT INTO `wk_crm_area` VALUES (230603, 230600, '龙凤区');
INSERT INTO `wk_crm_area` VALUES (230602, 230600, '萨尔图区');
INSERT INTO `wk_crm_area` VALUES (230624, 230600, '杜尔伯特蒙古族自治县');
INSERT INTO `wk_crm_area` VALUES (230623, 230600, '林甸县');
INSERT INTO `wk_crm_area` VALUES (150525, 150500, '奈曼旗');
INSERT INTO `wk_crm_area` VALUES (150526, 150500, '扎鲁特旗');
INSERT INTO `wk_crm_area` VALUES (150523, 150500, '开鲁县');
INSERT INTO `wk_crm_area` VALUES (150502, 150500, '科尔沁区');
INSERT INTO `wk_crm_area` VALUES (150524, 150500, '库伦旗');
INSERT INTO `wk_crm_area` VALUES (150521, 150500, '科尔沁左翼中旗');
INSERT INTO `wk_crm_area` VALUES (150522, 150500, '科尔沁左翼后旗');
INSERT INTO `wk_crm_area` VALUES (150581, 150500, '霍林郭勒市');
INSERT INTO `wk_crm_area` VALUES (150571, 150500, '通辽经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (450903, 450900, '福绵区');
INSERT INTO `wk_crm_area` VALUES (450902, 450900, '玉州区');
INSERT INTO `wk_crm_area` VALUES (450924, 450900, '兴业县');
INSERT INTO `wk_crm_area` VALUES (450921, 450900, '容县');
INSERT INTO `wk_crm_area` VALUES (450923, 450900, '博白县');
INSERT INTO `wk_crm_area` VALUES (450922, 450900, '陆川县');
INSERT INTO `wk_crm_area` VALUES (450981, 450900, '北流市');
INSERT INTO `wk_crm_area` VALUES (410800, 410000, '焦作市');
INSERT INTO `wk_crm_area` VALUES (410700, 410000, '新乡市');
INSERT INTO `wk_crm_area` VALUES (410900, 410000, '濮阳市');
INSERT INTO `wk_crm_area` VALUES (410400, 410000, '平顶山市');
INSERT INTO `wk_crm_area` VALUES (411500, 410000, '信阳市');
INSERT INTO `wk_crm_area` VALUES (410300, 410000, '洛阳市');
INSERT INTO `wk_crm_area` VALUES (411400, 410000, '商丘市');
INSERT INTO `wk_crm_area` VALUES (410600, 410000, '鹤壁市');
INSERT INTO `wk_crm_area` VALUES (411700, 410000, '驻马店市');
INSERT INTO `wk_crm_area` VALUES (410500, 410000, '安阳市');
INSERT INTO `wk_crm_area` VALUES (411600, 410000, '周口市');
INSERT INTO `wk_crm_area` VALUES (411100, 410000, '漯河市');
INSERT INTO `wk_crm_area` VALUES (411000, 410000, '许昌市');
INSERT INTO `wk_crm_area` VALUES (410200, 410000, '开封市');
INSERT INTO `wk_crm_area` VALUES (411300, 410000, '南阳市');
INSERT INTO `wk_crm_area` VALUES (410100, 410000, '郑州市');
INSERT INTO `wk_crm_area` VALUES (411200, 410000, '三门峡市');
INSERT INTO `wk_crm_area` VALUES (419000, 410000, '省直辖县级行政区划');
INSERT INTO `wk_crm_area` VALUES (341372, 341300, '宿州经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (341371, 341300, '宿州马鞍山现代产业园区');
INSERT INTO `wk_crm_area` VALUES (341321, 341300, '砀山县');
INSERT INTO `wk_crm_area` VALUES (341322, 341300, '萧县');
INSERT INTO `wk_crm_area` VALUES (341323, 341300, '灵璧县');
INSERT INTO `wk_crm_area` VALUES (341302, 341300, '埇桥区');
INSERT INTO `wk_crm_area` VALUES (341324, 341300, '泗县');
INSERT INTO `wk_crm_area` VALUES (620981, 620900, '玉门市');
INSERT INTO `wk_crm_area` VALUES (620982, 620900, '敦煌市');
INSERT INTO `wk_crm_area` VALUES (620921, 620900, '金塔县');
INSERT INTO `wk_crm_area` VALUES (620922, 620900, '瓜州县');
INSERT INTO `wk_crm_area` VALUES (620923, 620900, '肃北蒙古族自治县');
INSERT INTO `wk_crm_area` VALUES (620902, 620900, '肃州区');
INSERT INTO `wk_crm_area` VALUES (620924, 620900, '阿克塞哈萨克族自治县');
INSERT INTO `wk_crm_area` VALUES (140728, 140700, '平遥县');
INSERT INTO `wk_crm_area` VALUES (140729, 140700, '灵石县');
INSERT INTO `wk_crm_area` VALUES (140781, 140700, '介休市');
INSERT INTO `wk_crm_area` VALUES (140721, 140700, '榆社县');
INSERT INTO `wk_crm_area` VALUES (140722, 140700, '左权县');
INSERT INTO `wk_crm_area` VALUES (140723, 140700, '和顺县');
INSERT INTO `wk_crm_area` VALUES (140702, 140700, '榆次区');
INSERT INTO `wk_crm_area` VALUES (140724, 140700, '昔阳县');
INSERT INTO `wk_crm_area` VALUES (140725, 140700, '寿阳县');
INSERT INTO `wk_crm_area` VALUES (140726, 140700, '太谷县');
INSERT INTO `wk_crm_area` VALUES (140727, 140700, '祁县');
INSERT INTO `wk_crm_area` VALUES (320571, 320500, '苏州工业园区');
INSERT INTO `wk_crm_area` VALUES (320582, 320500, '张家港市');
INSERT INTO `wk_crm_area` VALUES (320583, 320500, '昆山市');
INSERT INTO `wk_crm_area` VALUES (320508, 320500, '姑苏区');
INSERT INTO `wk_crm_area` VALUES (320509, 320500, '吴江区');
INSERT INTO `wk_crm_area` VALUES (320581, 320500, '常熟市');
INSERT INTO `wk_crm_area` VALUES (320506, 320500, '吴中区');
INSERT INTO `wk_crm_area` VALUES (320507, 320500, '相城区');
INSERT INTO `wk_crm_area` VALUES (320505, 320500, '虎丘区');
INSERT INTO `wk_crm_area` VALUES (320585, 320500, '太仓市');
INSERT INTO `wk_crm_area` VALUES (712796, 712700, '望安乡');
INSERT INTO `wk_crm_area` VALUES (712797, 712700, '七美乡');
INSERT INTO `wk_crm_area` VALUES (712798, 712700, '白沙乡');
INSERT INTO `wk_crm_area` VALUES (712799, 712700, '湖西乡');
INSERT INTO `wk_crm_area` VALUES (712794, 712700, '马公市');
INSERT INTO `wk_crm_area` VALUES (712795, 712700, '西屿乡');
INSERT INTO `wk_crm_area` VALUES (710405, 710400, '北区');
INSERT INTO `wk_crm_area` VALUES (710449, 710400, '大甲区');
INSERT INTO `wk_crm_area` VALUES (710404, 710400, '西区');
INSERT INTO `wk_crm_area` VALUES (710448, 710400, '清水区');
INSERT INTO `wk_crm_area` VALUES (710403, 710400, '南区');
INSERT INTO `wk_crm_area` VALUES (710447, 710400, '梧栖区');
INSERT INTO `wk_crm_area` VALUES (710402, 710400, '东区');
INSERT INTO `wk_crm_area` VALUES (710446, 710400, '龙井区');
INSERT INTO `wk_crm_area` VALUES (710408, 710400, '南屯区');
INSERT INTO `wk_crm_area` VALUES (710407, 710400, '西屯区');
INSERT INTO `wk_crm_area` VALUES (710406, 710400, '北屯区');
INSERT INTO `wk_crm_area` VALUES (710451, 710400, '大安区');
INSERT INTO `wk_crm_area` VALUES (710450, 710400, '外埔区');
INSERT INTO `wk_crm_area` VALUES (710434, 710400, '乌日区');
INSERT INTO `wk_crm_area` VALUES (710433, 710400, '雾峰区');
INSERT INTO `wk_crm_area` VALUES (710499, 710400, '其它区');
INSERT INTO `wk_crm_area` VALUES (710432, 710400, '大里区');
INSERT INTO `wk_crm_area` VALUES (710431, 710400, '太平区');
INSERT INTO `wk_crm_area` VALUES (710438, 710400, '东势区');
INSERT INTO `wk_crm_area` VALUES (710437, 710400, '石冈区');
INSERT INTO `wk_crm_area` VALUES (710436, 710400, '后里区');
INSERT INTO `wk_crm_area` VALUES (710435, 710400, '丰原区');
INSERT INTO `wk_crm_area` VALUES (710439, 710400, '和平区');
INSERT INTO `wk_crm_area` VALUES (710441, 710400, '潭子区');
INSERT INTO `wk_crm_area` VALUES (710440, 710400, '新社区');
INSERT INTO `wk_crm_area` VALUES (710401, 710400, '中区');
INSERT INTO `wk_crm_area` VALUES (710445, 710400, '沙鹿区');
INSERT INTO `wk_crm_area` VALUES (710444, 710400, '大肚区');
INSERT INTO `wk_crm_area` VALUES (710443, 710400, '神冈区');
INSERT INTO `wk_crm_area` VALUES (710442, 710400, '大雅区');
INSERT INTO `wk_crm_area` VALUES (433127, 433100, '永顺县');
INSERT INTO `wk_crm_area` VALUES (433125, 433100, '保靖县');
INSERT INTO `wk_crm_area` VALUES (433126, 433100, '古丈县');
INSERT INTO `wk_crm_area` VALUES (433130, 433100, '龙山县');
INSERT INTO `wk_crm_area` VALUES (433172, 433100, '湖南吉首经济开发区');
INSERT INTO `wk_crm_area` VALUES (433173, 433100, '湖南永顺经济开发区');
INSERT INTO `wk_crm_area` VALUES (433101, 433100, '吉首市');
INSERT INTO `wk_crm_area` VALUES (433123, 433100, '凤凰县');
INSERT INTO `wk_crm_area` VALUES (433124, 433100, '花垣县');
INSERT INTO `wk_crm_area` VALUES (433122, 433100, '泸溪县');
INSERT INTO `wk_crm_area` VALUES (540522, 540500, '贡嘎县');
INSERT INTO `wk_crm_area` VALUES (540523, 540500, '桑日县');
INSERT INTO `wk_crm_area` VALUES (540502, 540500, '乃东区');
INSERT INTO `wk_crm_area` VALUES (540524, 540500, '琼结县');
INSERT INTO `wk_crm_area` VALUES (540525, 540500, '曲松县');
INSERT INTO `wk_crm_area` VALUES (540530, 540500, '错那县');
INSERT INTO `wk_crm_area` VALUES (540531, 540500, '浪卡子县');
INSERT INTO `wk_crm_area` VALUES (540521, 540500, '扎囊县');
INSERT INTO `wk_crm_area` VALUES (540526, 540500, '措美县');
INSERT INTO `wk_crm_area` VALUES (540527, 540500, '洛扎县');
INSERT INTO `wk_crm_area` VALUES (540528, 540500, '加查县');
INSERT INTO `wk_crm_area` VALUES (540529, 540500, '隆子县');
INSERT INTO `wk_crm_area` VALUES (710702, 710700, '信义区');
INSERT INTO `wk_crm_area` VALUES (710701, 710700, '仁爱区');
INSERT INTO `wk_crm_area` VALUES (710799, 710700, '其它区');
INSERT INTO `wk_crm_area` VALUES (710706, 710700, '暖暖区');
INSERT INTO `wk_crm_area` VALUES (710705, 710700, '安乐区');
INSERT INTO `wk_crm_area` VALUES (710704, 710700, '中山区');
INSERT INTO `wk_crm_area` VALUES (710703, 710700, '中正区');
INSERT INTO `wk_crm_area` VALUES (710707, 710700, '七堵区');
INSERT INTO `wk_crm_area` VALUES (410404, 410400, '石龙区');
INSERT INTO `wk_crm_area` VALUES (410403, 410400, '卫东区');
INSERT INTO `wk_crm_area` VALUES (410425, 410400, '郏县');
INSERT INTO `wk_crm_area` VALUES (410411, 410400, '湛河区');
INSERT INTO `wk_crm_area` VALUES (410422, 410400, '叶县');
INSERT INTO `wk_crm_area` VALUES (410421, 410400, '宝丰县');
INSERT INTO `wk_crm_area` VALUES (410402, 410400, '新华区');
INSERT INTO `wk_crm_area` VALUES (410423, 410400, '鲁山县');
INSERT INTO `wk_crm_area` VALUES (410472, 410400, '平顶山市新城区');
INSERT INTO `wk_crm_area` VALUES (410471, 410400, '平顶山高新技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (410482, 410400, '汝州市');
INSERT INTO `wk_crm_area` VALUES (410481, 410400, '舞钢市');
INSERT INTO `wk_crm_area` VALUES (330481, 330400, '海宁市');
INSERT INTO `wk_crm_area` VALUES (330482, 330400, '平湖市');
INSERT INTO `wk_crm_area` VALUES (330483, 330400, '桐乡市');
INSERT INTO `wk_crm_area` VALUES (330411, 330400, '秀洲区');
INSERT INTO `wk_crm_area` VALUES (330402, 330400, '南湖区');
INSERT INTO `wk_crm_area` VALUES (330424, 330400, '海盐县');
INSERT INTO `wk_crm_area` VALUES (330421, 330400, '嘉善县');
INSERT INTO `wk_crm_area` VALUES (632624, 632600, '达日县');
INSERT INTO `wk_crm_area` VALUES (632623, 632600, '甘德县');
INSERT INTO `wk_crm_area` VALUES (632622, 632600, '班玛县');
INSERT INTO `wk_crm_area` VALUES (632621, 632600, '玛沁县');
INSERT INTO `wk_crm_area` VALUES (632626, 632600, '玛多县');
INSERT INTO `wk_crm_area` VALUES (632625, 632600, '久治县');
INSERT INTO `wk_crm_area` VALUES (44000004, 442000, '西区街道');
INSERT INTO `wk_crm_area` VALUES (44000103, 442000, '东凤镇');
INSERT INTO `wk_crm_area` VALUES (44000114, 442000, '三乡镇');
INSERT INTO `wk_crm_area` VALUES (44000005, 442000, '南区街道');
INSERT INTO `wk_crm_area` VALUES (44000104, 442000, '东升镇');
INSERT INTO `wk_crm_area` VALUES (44000115, 442000, '板芙镇');
INSERT INTO `wk_crm_area` VALUES (44000006, 442000, '五桂山街道');
INSERT INTO `wk_crm_area` VALUES (44000105, 442000, '古镇镇');
INSERT INTO `wk_crm_area` VALUES (44000116, 442000, '大涌镇');
INSERT INTO `wk_crm_area` VALUES (44000106, 442000, '沙溪镇');
INSERT INTO `wk_crm_area` VALUES (44000117, 442000, '神湾镇 ');
INSERT INTO `wk_crm_area` VALUES (44000110, 442000, '横栏镇');
INSERT INTO `wk_crm_area` VALUES (44000001, 442000, '石岐区街道');
INSERT INTO `wk_crm_area` VALUES (44000100, 442000, '小榄镇');
INSERT INTO `wk_crm_area` VALUES (44000111, 442000, '南头镇');
INSERT INTO `wk_crm_area` VALUES (44000002, 442000, '东区街道');
INSERT INTO `wk_crm_area` VALUES (44000101, 442000, '黄圃镇');
INSERT INTO `wk_crm_area` VALUES (44000112, 442000, '阜沙镇');
INSERT INTO `wk_crm_area` VALUES (44000003, 442000, '火炬开发区街道');
INSERT INTO `wk_crm_area` VALUES (44000102, 442000, '民众镇');
INSERT INTO `wk_crm_area` VALUES (44000113, 442000, '南朗镇');
INSERT INTO `wk_crm_area` VALUES (44000107, 442000, '坦洲镇');
INSERT INTO `wk_crm_area` VALUES (44000108, 442000, '港口镇');
INSERT INTO `wk_crm_area` VALUES (44000109, 442000, '三角镇');
INSERT INTO `wk_crm_area` VALUES (370781, 370700, '青州市');
INSERT INTO `wk_crm_area` VALUES (370782, 370700, '诸城市');
INSERT INTO `wk_crm_area` VALUES (370772, 370700, '潍坊滨海经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (370783, 370700, '寿光市');
INSERT INTO `wk_crm_area` VALUES (370784, 370700, '安丘市');
INSERT INTO `wk_crm_area` VALUES (370702, 370700, '潍城区');
INSERT INTO `wk_crm_area` VALUES (370724, 370700, '临朐县');
INSERT INTO `wk_crm_area` VALUES (370703, 370700, '寒亭区');
INSERT INTO `wk_crm_area` VALUES (370725, 370700, '昌乐县');
INSERT INTO `wk_crm_area` VALUES (370704, 370700, '坊子区');
INSERT INTO `wk_crm_area` VALUES (370785, 370700, '高密市');
INSERT INTO `wk_crm_area` VALUES (370786, 370700, '昌邑市');
INSERT INTO `wk_crm_area` VALUES (370705, 370700, '奎文区');
INSERT INTO `wk_crm_area` VALUES (210903, 210900, '新邱区');
INSERT INTO `wk_crm_area` VALUES (210902, 210900, '海州区');
INSERT INTO `wk_crm_area` VALUES (210911, 210900, '细河区');
INSERT INTO `wk_crm_area` VALUES (210922, 210900, '彰武县');
INSERT INTO `wk_crm_area` VALUES (210905, 210900, '清河门区');
INSERT INTO `wk_crm_area` VALUES (210904, 210900, '太平区');
INSERT INTO `wk_crm_area` VALUES (210921, 210900, '阜新蒙古族自治县');
INSERT INTO `wk_crm_area` VALUES (420203, 420200, '西塞山区');
INSERT INTO `wk_crm_area` VALUES (420202, 420200, '黄石港区');
INSERT INTO `wk_crm_area` VALUES (420222, 420200, '阳新县');
INSERT INTO `wk_crm_area` VALUES (420205, 420200, '铁山区');
INSERT INTO `wk_crm_area` VALUES (420204, 420200, '下陆区');
INSERT INTO `wk_crm_area` VALUES (420281, 420200, '大冶市');
INSERT INTO `wk_crm_area` VALUES (340207, 340200, '鸠江区');
INSERT INTO `wk_crm_area` VALUES (340208, 340200, '三山区');
INSERT INTO `wk_crm_area` VALUES (340272, 340200, '安徽芜湖长江大桥经济开发区');
INSERT INTO `wk_crm_area` VALUES (340271, 340200, '芜湖经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (340221, 340200, '芜湖县');
INSERT INTO `wk_crm_area` VALUES (340222, 340200, '繁昌县');
INSERT INTO `wk_crm_area` VALUES (340203, 340200, '弋江区');
INSERT INTO `wk_crm_area` VALUES (340225, 340200, '无为县');
INSERT INTO `wk_crm_area` VALUES (340223, 340200, '南陵县');
INSERT INTO `wk_crm_area` VALUES (340202, 340200, '镜湖区');
INSERT INTO `wk_crm_area` VALUES (411526, 411500, '潢川县');
INSERT INTO `wk_crm_area` VALUES (411503, 411500, '平桥区');
INSERT INTO `wk_crm_area` VALUES (411525, 411500, '固始县');
INSERT INTO `wk_crm_area` VALUES (411528, 411500, '息县');
INSERT INTO `wk_crm_area` VALUES (411527, 411500, '淮滨县');
INSERT INTO `wk_crm_area` VALUES (411522, 411500, '光山县');
INSERT INTO `wk_crm_area` VALUES (411521, 411500, '罗山县');
INSERT INTO `wk_crm_area` VALUES (411502, 411500, '浉河区');
INSERT INTO `wk_crm_area` VALUES (411524, 411500, '商城县');
INSERT INTO `wk_crm_area` VALUES (411523, 411500, '新县');
INSERT INTO `wk_crm_area` VALUES (411571, 411500, '信阳高新技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (711986, 711900, '中埔乡');
INSERT INTO `wk_crm_area` VALUES (711997, 711900, '溪口乡');
INSERT INTO `wk_crm_area` VALUES (711987, 711900, '大埔乡');
INSERT INTO `wk_crm_area` VALUES (711998, 711900, '义竹乡');
INSERT INTO `wk_crm_area` VALUES (711988, 711900, '水上乡');
INSERT INTO `wk_crm_area` VALUES (711999, 711900, '布袋镇');
INSERT INTO `wk_crm_area` VALUES (711989, 711900, '鹿草乡');
INSERT INTO `wk_crm_area` VALUES (711982, 711900, '番路乡');
INSERT INTO `wk_crm_area` VALUES (711993, 711900, '六脚乡');
INSERT INTO `wk_crm_area` VALUES (711983, 711900, '梅山乡');
INSERT INTO `wk_crm_area` VALUES (711994, 711900, '新港乡');
INSERT INTO `wk_crm_area` VALUES (711984, 711900, '竹崎乡');
INSERT INTO `wk_crm_area` VALUES (711995, 711900, '民雄乡');
INSERT INTO `wk_crm_area` VALUES (711985, 711900, '阿里山乡');
INSERT INTO `wk_crm_area` VALUES (711996, 711900, '大林镇');
INSERT INTO `wk_crm_area` VALUES (711990, 711900, '太保市');
INSERT INTO `wk_crm_area` VALUES (711991, 711900, '朴子市');
INSERT INTO `wk_crm_area` VALUES (711992, 711900, '东石乡');
INSERT INTO `wk_crm_area` VALUES (640100, 640000, '银川市');
INSERT INTO `wk_crm_area` VALUES (640300, 640000, '吴忠市');
INSERT INTO `wk_crm_area` VALUES (640200, 640000, '石嘴山市');
INSERT INTO `wk_crm_area` VALUES (640500, 640000, '中卫市');
INSERT INTO `wk_crm_area` VALUES (640400, 640000, '固原市');
INSERT INTO `wk_crm_area` VALUES (420111, 420100, '洪山区');
INSERT INTO `wk_crm_area` VALUES (420104, 420100, '硚口区');
INSERT INTO `wk_crm_area` VALUES (420115, 420100, '江夏区');
INSERT INTO `wk_crm_area` VALUES (420103, 420100, '江汉区');
INSERT INTO `wk_crm_area` VALUES (420114, 420100, '蔡甸区');
INSERT INTO `wk_crm_area` VALUES (420102, 420100, '江岸区');
INSERT INTO `wk_crm_area` VALUES (420113, 420100, '汉南区');
INSERT INTO `wk_crm_area` VALUES (420112, 420100, '东西湖区');
INSERT INTO `wk_crm_area` VALUES (420107, 420100, '青山区');
INSERT INTO `wk_crm_area` VALUES (420106, 420100, '武昌区');
INSERT INTO `wk_crm_area` VALUES (420117, 420100, '新洲区');
INSERT INTO `wk_crm_area` VALUES (420105, 420100, '汉阳区');
INSERT INTO `wk_crm_area` VALUES (420116, 420100, '黄陂区');
INSERT INTO `wk_crm_area` VALUES (220702, 220700, '宁江区');
INSERT INTO `wk_crm_area` VALUES (220723, 220700, '乾安县');
INSERT INTO `wk_crm_area` VALUES (220771, 220700, '吉林松原经济开发区');
INSERT INTO `wk_crm_area` VALUES (220781, 220700, '扶余市');
INSERT INTO `wk_crm_area` VALUES (220722, 220700, '长岭县');
INSERT INTO `wk_crm_area` VALUES (220721, 220700, '前郭尔罗斯蒙古族自治县');
INSERT INTO `wk_crm_area` VALUES (230523, 230500, '宝清县');
INSERT INTO `wk_crm_area` VALUES (230522, 230500, '友谊县');
INSERT INTO `wk_crm_area` VALUES (230521, 230500, '集贤县');
INSERT INTO `wk_crm_area` VALUES (230506, 230500, '宝山区');
INSERT INTO `wk_crm_area` VALUES (230505, 230500, '四方台区');
INSERT INTO `wk_crm_area` VALUES (230503, 230500, '岭东区');
INSERT INTO `wk_crm_area` VALUES (230502, 230500, '尖山区');
INSERT INTO `wk_crm_area` VALUES (230524, 230500, '饶河县');
INSERT INTO `wk_crm_area` VALUES (150802, 150800, '临河区');
INSERT INTO `wk_crm_area` VALUES (150824, 150800, '乌拉特中旗');
INSERT INTO `wk_crm_area` VALUES (150825, 150800, '乌拉特后旗');
INSERT INTO `wk_crm_area` VALUES (150822, 150800, '磴口县');
INSERT INTO `wk_crm_area` VALUES (150823, 150800, '乌拉特前旗');
INSERT INTO `wk_crm_area` VALUES (150821, 150800, '五原县');
INSERT INTO `wk_crm_area` VALUES (150826, 150800, '杭锦后旗');
INSERT INTO `wk_crm_area` VALUES (421303, 421300, '曾都区');
INSERT INTO `wk_crm_area` VALUES (421381, 421300, '广水市');
INSERT INTO `wk_crm_area` VALUES (421321, 421300, '随县');
INSERT INTO `wk_crm_area` VALUES (530723, 530700, '华坪县');
INSERT INTO `wk_crm_area` VALUES (530702, 530700, '古城区');
INSERT INTO `wk_crm_area` VALUES (530724, 530700, '宁蒗彝族自治县');
INSERT INTO `wk_crm_area` VALUES (530721, 530700, '玉龙纳西族自治县');
INSERT INTO `wk_crm_area` VALUES (530722, 530700, '永胜县');
INSERT INTO `wk_crm_area` VALUES (630222, 630200, '民和回族土族自治县');
INSERT INTO `wk_crm_area` VALUES (630203, 630200, '平安区');
INSERT INTO `wk_crm_area` VALUES (630225, 630200, '循化撒拉族自治县');
INSERT INTO `wk_crm_area` VALUES (630223, 630200, '互助土族自治县');
INSERT INTO `wk_crm_area` VALUES (630202, 630200, '乐都区');
INSERT INTO `wk_crm_area` VALUES (630224, 630200, '化隆回族自治县');
INSERT INTO `wk_crm_area` VALUES (360982, 360900, '樟树市');
INSERT INTO `wk_crm_area` VALUES (360983, 360900, '高安市');
INSERT INTO `wk_crm_area` VALUES (360981, 360900, '丰城市');
INSERT INTO `wk_crm_area` VALUES (360926, 360900, '铜鼓县');
INSERT INTO `wk_crm_area` VALUES (360902, 360900, '袁州区');
INSERT INTO `wk_crm_area` VALUES (360924, 360900, '宜丰县');
INSERT INTO `wk_crm_area` VALUES (360925, 360900, '靖安县');
INSERT INTO `wk_crm_area` VALUES (360922, 360900, '万载县');
INSERT INTO `wk_crm_area` VALUES (360923, 360900, '上高县');
INSERT INTO `wk_crm_area` VALUES (360921, 360900, '奉新县');
INSERT INTO `wk_crm_area` VALUES (320681, 320600, '启东市');
INSERT INTO `wk_crm_area` VALUES (320671, 320600, '南通经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (320682, 320600, '如皋市');
INSERT INTO `wk_crm_area` VALUES (320612, 320600, '通州区');
INSERT INTO `wk_crm_area` VALUES (320623, 320600, '如东县');
INSERT INTO `wk_crm_area` VALUES (320602, 320600, '崇川区');
INSERT INTO `wk_crm_area` VALUES (320611, 320600, '港闸区');
INSERT INTO `wk_crm_area` VALUES (320685, 320600, '海安市');
INSERT INTO `wk_crm_area` VALUES (320684, 320600, '海门市');
INSERT INTO `wk_crm_area` VALUES (431102, 431100, '零陵区');
INSERT INTO `wk_crm_area` VALUES (431124, 431100, '道县');
INSERT INTO `wk_crm_area` VALUES (431123, 431100, '双牌县');
INSERT INTO `wk_crm_area` VALUES (431126, 431100, '宁远县');
INSERT INTO `wk_crm_area` VALUES (431103, 431100, '冷水滩区');
INSERT INTO `wk_crm_area` VALUES (431125, 431100, '江永县');
INSERT INTO `wk_crm_area` VALUES (431128, 431100, '新田县');
INSERT INTO `wk_crm_area` VALUES (431127, 431100, '蓝山县');
INSERT INTO `wk_crm_area` VALUES (431129, 431100, '江华瑶族自治县');
INSERT INTO `wk_crm_area` VALUES (431171, 431100, '永州经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (431173, 431100, '永州市回龙圩管理区');
INSERT INTO `wk_crm_area` VALUES (431172, 431100, '永州市金洞管理区');
INSERT INTO `wk_crm_area` VALUES (431122, 431100, '东安县');
INSERT INTO `wk_crm_area` VALUES (431121, 431100, '祁阳县');
INSERT INTO `wk_crm_area` VALUES (540621, 540600, '嘉黎县');
INSERT INTO `wk_crm_area` VALUES (540622, 540600, '比如县');
INSERT INTO `wk_crm_area` VALUES (540623, 540600, '聂荣县');
INSERT INTO `wk_crm_area` VALUES (540602, 540600, '色尼区');
INSERT INTO `wk_crm_area` VALUES (540624, 540600, '安多县');
INSERT INTO `wk_crm_area` VALUES (540630, 540600, '双湖县');
INSERT INTO `wk_crm_area` VALUES (540629, 540600, '尼玛县');
INSERT INTO `wk_crm_area` VALUES (540625, 540600, '申扎县');
INSERT INTO `wk_crm_area` VALUES (540626, 540600, '索县');
INSERT INTO `wk_crm_area` VALUES (540627, 540600, '班戈县');
INSERT INTO `wk_crm_area` VALUES (540628, 540600, '巴青县');
INSERT INTO `wk_crm_area` VALUES (710614, 710600, '南投市');
INSERT INTO `wk_crm_area` VALUES (710625, 710600, '竹山镇');
INSERT INTO `wk_crm_area` VALUES (710624, 710600, '信义乡');
INSERT INTO `wk_crm_area` VALUES (710623, 710600, '鱼池乡');
INSERT INTO `wk_crm_area` VALUES (710622, 710600, '水里乡');
INSERT INTO `wk_crm_area` VALUES (710618, 710600, '埔里镇');
INSERT INTO `wk_crm_area` VALUES (710617, 710600, '国姓乡');
INSERT INTO `wk_crm_area` VALUES (710616, 710600, '草屯镇');
INSERT INTO `wk_crm_area` VALUES (710615, 710600, '中寮乡');
INSERT INTO `wk_crm_area` VALUES (710626, 710600, '鹿谷乡');
INSERT INTO `wk_crm_area` VALUES (710619, 710600, '仁爱乡');
INSERT INTO `wk_crm_area` VALUES (710621, 710600, '集集镇');
INSERT INTO `wk_crm_area` VALUES (710620, 710600, '名间乡');
INSERT INTO `wk_crm_area` VALUES (410305, 410300, '涧西区');
INSERT INTO `wk_crm_area` VALUES (410327, 410300, '宜阳县');
INSERT INTO `wk_crm_area` VALUES (410304, 410300, '瀍河回族区');
INSERT INTO `wk_crm_area` VALUES (410326, 410300, '汝阳县');
INSERT INTO `wk_crm_area` VALUES (410329, 410300, '伊川县');
INSERT INTO `wk_crm_area` VALUES (410306, 410300, '吉利区');
INSERT INTO `wk_crm_area` VALUES (410328, 410300, '洛宁县');
INSERT INTO `wk_crm_area` VALUES (410323, 410300, '新安县');
INSERT INTO `wk_crm_area` VALUES (410311, 410300, '洛龙区');
INSERT INTO `wk_crm_area` VALUES (410322, 410300, '孟津县');
INSERT INTO `wk_crm_area` VALUES (410303, 410300, '西工区');
INSERT INTO `wk_crm_area` VALUES (410325, 410300, '嵩县');
INSERT INTO `wk_crm_area` VALUES (410302, 410300, '老城区');
INSERT INTO `wk_crm_area` VALUES (410324, 410300, '栾川县');
INSERT INTO `wk_crm_area` VALUES (410381, 410300, '偃师市');
INSERT INTO `wk_crm_area` VALUES (410371, 410300, '洛阳高新技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (370000, 100000, '山东省');
INSERT INTO `wk_crm_area` VALUES (620000, 100000, '甘肃省');
INSERT INTO `wk_crm_area` VALUES (320000, 100000, '江苏省');
INSERT INTO `wk_crm_area` VALUES (110000, 100000, '北京市');
INSERT INTO `wk_crm_area` VALUES (530000, 100000, '云南省');
INSERT INTO `wk_crm_area` VALUES (460000, 100000, '海南省');
INSERT INTO `wk_crm_area` VALUES (330000, 100000, '浙江省');
INSERT INTO `wk_crm_area` VALUES (310000, 100000, '上海市');
INSERT INTO `wk_crm_area` VALUES (120000, 100000, '天津市');
INSERT INTO `wk_crm_area` VALUES (610000, 100000, '陕西省');
INSERT INTO `wk_crm_area` VALUES (650000, 100000, '新疆维吾尔自治区');
INSERT INTO `wk_crm_area` VALUES (520000, 100000, '贵州省');
INSERT INTO `wk_crm_area` VALUES (340000, 100000, '安徽省');
INSERT INTO `wk_crm_area` VALUES (820000, 100000, '澳门特别行政区');
INSERT INTO `wk_crm_area` VALUES (430000, 100000, '湖南省');
INSERT INTO `wk_crm_area` VALUES (130000, 100000, '河北省');
INSERT INTO `wk_crm_area` VALUES (810000, 100000, '香港特别行政区');
INSERT INTO `wk_crm_area` VALUES (210000, 100000, '辽宁省');
INSERT INTO `wk_crm_area` VALUES (510000, 100000, '四川省');
INSERT INTO `wk_crm_area` VALUES (640000, 100000, '宁夏回族自治区');
INSERT INTO `wk_crm_area` VALUES (220000, 100000, '吉林省');
INSERT INTO `wk_crm_area` VALUES (350000, 100000, '福建省');
INSERT INTO `wk_crm_area` VALUES (420000, 100000, '湖北省');
INSERT INTO `wk_crm_area` VALUES (440000, 100000, '广东省');
INSERT INTO `wk_crm_area` VALUES (500000, 100000, '重庆市');
INSERT INTO `wk_crm_area` VALUES (140000, 100000, '山西省');
INSERT INTO `wk_crm_area` VALUES (360000, 100000, '江西省');
INSERT INTO `wk_crm_area` VALUES (230000, 100000, '黑龙江省');
INSERT INTO `wk_crm_area` VALUES (630000, 100000, '青海省');
INSERT INTO `wk_crm_area` VALUES (410000, 100000, '河南省');
INSERT INTO `wk_crm_area` VALUES (710000, 100000, '台湾省');
INSERT INTO `wk_crm_area` VALUES (150000, 100000, '内蒙古自治区');
INSERT INTO `wk_crm_area` VALUES (540000, 100000, '西藏自治区');
INSERT INTO `wk_crm_area` VALUES (450000, 100000, '广西壮族自治区');
INSERT INTO `wk_crm_area` VALUES (330503, 330500, '南浔区');
INSERT INTO `wk_crm_area` VALUES (330521, 330500, '德清县');
INSERT INTO `wk_crm_area` VALUES (330522, 330500, '长兴县');
INSERT INTO `wk_crm_area` VALUES (330523, 330500, '安吉县');
INSERT INTO `wk_crm_area` VALUES (330502, 330500, '吴兴区');
INSERT INTO `wk_crm_area` VALUES (632521, 632500, '共和县');
INSERT INTO `wk_crm_area` VALUES (632525, 632500, '贵南县');
INSERT INTO `wk_crm_area` VALUES (632524, 632500, '兴海县');
INSERT INTO `wk_crm_area` VALUES (632523, 632500, '贵德县');
INSERT INTO `wk_crm_area` VALUES (632522, 632500, '同德县');
INSERT INTO `wk_crm_area` VALUES (421202, 421200, '咸安区');
INSERT INTO `wk_crm_area` VALUES (421224, 421200, '通山县');
INSERT INTO `wk_crm_area` VALUES (421223, 421200, '崇阳县');
INSERT INTO `wk_crm_area` VALUES (421281, 421200, '赤壁市');
INSERT INTO `wk_crm_area` VALUES (421222, 421200, '通城县');
INSERT INTO `wk_crm_area` VALUES (421221, 421200, '嘉鱼县');
INSERT INTO `wk_crm_area` VALUES (370671, 370600, '烟台高新技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (370682, 370600, '莱阳市');
INSERT INTO `wk_crm_area` VALUES (370672, 370600, '烟台经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (370683, 370600, '莱州市');
INSERT INTO `wk_crm_area` VALUES (370684, 370600, '蓬莱市');
INSERT INTO `wk_crm_area` VALUES (370685, 370600, '招远市');
INSERT INTO `wk_crm_area` VALUES (370681, 370600, '龙口市');
INSERT INTO `wk_crm_area` VALUES (370602, 370600, '芝罘区');
INSERT INTO `wk_crm_area` VALUES (370613, 370600, '莱山区');
INSERT INTO `wk_crm_area` VALUES (370686, 370600, '栖霞市');
INSERT INTO `wk_crm_area` VALUES (370687, 370600, '海阳市');
INSERT INTO `wk_crm_area` VALUES (370611, 370600, '福山区');
INSERT INTO `wk_crm_area` VALUES (370612, 370600, '牟平区');
INSERT INTO `wk_crm_area` VALUES (370634, 370600, '长岛县');
INSERT INTO `wk_crm_area` VALUES (654321, 654300, '布尔津县');
INSERT INTO `wk_crm_area` VALUES (654322, 654300, '富蕴县');
INSERT INTO `wk_crm_area` VALUES (654301, 654300, '阿勒泰市');
INSERT INTO `wk_crm_area` VALUES (654323, 654300, '福海县');
INSERT INTO `wk_crm_area` VALUES (654324, 654300, '哈巴河县');
INSERT INTO `wk_crm_area` VALUES (654325, 654300, '青河县');
INSERT INTO `wk_crm_area` VALUES (654326, 654300, '吉木乃县');
INSERT INTO `wk_crm_area` VALUES (210804, 210800, '鲅鱼圈区');
INSERT INTO `wk_crm_area` VALUES (210803, 210800, '西市区');
INSERT INTO `wk_crm_area` VALUES (210802, 210800, '站前区');
INSERT INTO `wk_crm_area` VALUES (210882, 210800, '大石桥市');
INSERT INTO `wk_crm_area` VALUES (210881, 210800, '盖州市');
INSERT INTO `wk_crm_area` VALUES (210811, 210800, '老边区');
INSERT INTO `wk_crm_area` VALUES (419001, 419000, '济源市');
INSERT INTO `wk_crm_area` VALUES (340304, 340300, '禹会区');
INSERT INTO `wk_crm_area` VALUES (340371, 340300, '蚌埠市高新技术开发区');
INSERT INTO `wk_crm_area` VALUES (340372, 340300, '蚌埠市经济开发区');
INSERT INTO `wk_crm_area` VALUES (340321, 340300, '怀远县');
INSERT INTO `wk_crm_area` VALUES (340302, 340300, '龙子湖区');
INSERT INTO `wk_crm_area` VALUES (340303, 340300, '蚌山区');
INSERT INTO `wk_crm_area` VALUES (340311, 340300, '淮上区');
INSERT INTO `wk_crm_area` VALUES (340322, 340300, '五河县');
INSERT INTO `wk_crm_area` VALUES (340323, 340300, '固镇县');
INSERT INTO `wk_crm_area` VALUES (411426, 411400, '夏邑县');
INSERT INTO `wk_crm_area` VALUES (411423, 411400, '宁陵县');
INSERT INTO `wk_crm_area` VALUES (411422, 411400, '睢县');
INSERT INTO `wk_crm_area` VALUES (411403, 411400, '睢阳区');
INSERT INTO `wk_crm_area` VALUES (411425, 411400, '虞城县');
INSERT INTO `wk_crm_area` VALUES (411402, 411400, '梁园区');
INSERT INTO `wk_crm_area` VALUES (411424, 411400, '柘城县');
INSERT INTO `wk_crm_area` VALUES (411421, 411400, '民权县');
INSERT INTO `wk_crm_area` VALUES (411481, 411400, '永城市');
INSERT INTO `wk_crm_area` VALUES (411472, 411400, '河南商丘经济开发区');
INSERT INTO `wk_crm_area` VALUES (411471, 411400, '豫东综合物流产业聚集区');
INSERT INTO `wk_crm_area` VALUES (429000, 420000, '省直辖县级行政区划');
INSERT INTO `wk_crm_area` VALUES (420100, 420000, '武汉市');
INSERT INTO `wk_crm_area` VALUES (421200, 420000, '咸宁市');
INSERT INTO `wk_crm_area` VALUES (421100, 420000, '黄冈市');
INSERT INTO `wk_crm_area` VALUES (421000, 420000, '荆州市');
INSERT INTO `wk_crm_area` VALUES (420500, 420000, '宜昌市');
INSERT INTO `wk_crm_area` VALUES (422800, 420000, '恩施土家族苗族自治州');
INSERT INTO `wk_crm_area` VALUES (420300, 420000, '十堰市');
INSERT INTO `wk_crm_area` VALUES (420200, 420000, '黄石市');
INSERT INTO `wk_crm_area` VALUES (421300, 420000, '随州市');
INSERT INTO `wk_crm_area` VALUES (420900, 420000, '孝感市');
INSERT INTO `wk_crm_area` VALUES (420800, 420000, '荆门市');
INSERT INTO `wk_crm_area` VALUES (420700, 420000, '鄂州市');
INSERT INTO `wk_crm_area` VALUES (420600, 420000, '襄阳市');
INSERT INTO `wk_crm_area` VALUES (220602, 220600, '浑江区');
INSERT INTO `wk_crm_area` VALUES (220605, 220600, '江源区');
INSERT INTO `wk_crm_area` VALUES (220681, 220600, '临江市');
INSERT INTO `wk_crm_area` VALUES (220621, 220600, '抚松县');
INSERT INTO `wk_crm_area` VALUES (220623, 220600, '长白朝鲜族自治县');
INSERT INTO `wk_crm_area` VALUES (220622, 220600, '靖宇县');
INSERT INTO `wk_crm_area` VALUES (530822, 530800, '墨江哈尼族自治县');
INSERT INTO `wk_crm_area` VALUES (530823, 530800, '景东彝族自治县');
INSERT INTO `wk_crm_area` VALUES (530821, 530800, '宁洱哈尼族彝族自治县');
INSERT INTO `wk_crm_area` VALUES (530828, 530800, '澜沧拉祜族自治县');
INSERT INTO `wk_crm_area` VALUES (530829, 530800, '西盟佤族自治县');
INSERT INTO `wk_crm_area` VALUES (530826, 530800, '江城哈尼族彝族自治县');
INSERT INTO `wk_crm_area` VALUES (530827, 530800, '孟连傣族拉祜族佤族自治县');
INSERT INTO `wk_crm_area` VALUES (530802, 530800, '思茅区');
INSERT INTO `wk_crm_area` VALUES (530824, 530800, '景谷傣族彝族自治县');
INSERT INTO `wk_crm_area` VALUES (530825, 530800, '镇沅彝族哈尼族拉祜族自治县');
INSERT INTO `wk_crm_area` VALUES (230402, 230400, '向阳区');
INSERT INTO `wk_crm_area` VALUES (230422, 230400, '绥滨县');
INSERT INTO `wk_crm_area` VALUES (230421, 230400, '萝北县');
INSERT INTO `wk_crm_area` VALUES (230407, 230400, '兴山区');
INSERT INTO `wk_crm_area` VALUES (230406, 230400, '东山区');
INSERT INTO `wk_crm_area` VALUES (230405, 230400, '兴安区');
INSERT INTO `wk_crm_area` VALUES (230404, 230400, '南山区');
INSERT INTO `wk_crm_area` VALUES (230403, 230400, '工农区');
INSERT INTO `wk_crm_area` VALUES (630122, 630100, '湟中县');
INSERT INTO `wk_crm_area` VALUES (630123, 630100, '湟源县');
INSERT INTO `wk_crm_area` VALUES (630121, 630100, '大通回族土族自治县');
INSERT INTO `wk_crm_area` VALUES (630104, 630100, '城西区');
INSERT INTO `wk_crm_area` VALUES (630105, 630100, '城北区');
INSERT INTO `wk_crm_area` VALUES (630102, 630100, '城东区');
INSERT INTO `wk_crm_area` VALUES (630103, 630100, '城中区');
INSERT INTO `wk_crm_area` VALUES (150727, 150700, '新巴尔虎右旗');
INSERT INTO `wk_crm_area` VALUES (150703, 150700, '扎赉诺尔区');
INSERT INTO `wk_crm_area` VALUES (150725, 150700, '陈巴尔虎旗');
INSERT INTO `wk_crm_area` VALUES (150726, 150700, '新巴尔虎左旗');
INSERT INTO `wk_crm_area` VALUES (150723, 150700, '鄂伦春自治旗');
INSERT INTO `wk_crm_area` VALUES (150702, 150700, '海拉尔区');
INSERT INTO `wk_crm_area` VALUES (150724, 150700, '鄂温克族自治旗');
INSERT INTO `wk_crm_area` VALUES (150721, 150700, '阿荣旗');
INSERT INTO `wk_crm_area` VALUES (150722, 150700, '莫力达瓦达斡尔族自治旗');
INSERT INTO `wk_crm_area` VALUES (150785, 150700, '根河市');
INSERT INTO `wk_crm_area` VALUES (150783, 150700, '扎兰屯市');
INSERT INTO `wk_crm_area` VALUES (150784, 150700, '额尔古纳市');
INSERT INTO `wk_crm_area` VALUES (150781, 150700, '满洲里市');
INSERT INTO `wk_crm_area` VALUES (150782, 150700, '牙克石市');
INSERT INTO `wk_crm_area` VALUES (653221, 653200, '和田县');
INSERT INTO `wk_crm_area` VALUES (653222, 653200, '墨玉县');
INSERT INTO `wk_crm_area` VALUES (653201, 653200, '和田市');
INSERT INTO `wk_crm_area` VALUES (653223, 653200, '皮山县');
INSERT INTO `wk_crm_area` VALUES (653224, 653200, '洛浦县');
INSERT INTO `wk_crm_area` VALUES (653225, 653200, '策勒县');
INSERT INTO `wk_crm_area` VALUES (653226, 653200, '于田县');
INSERT INTO `wk_crm_area` VALUES (653227, 653200, '民丰县');
INSERT INTO `wk_crm_area` VALUES (320706, 320700, '海州区');
INSERT INTO `wk_crm_area` VALUES (320707, 320700, '赣榆区');
INSERT INTO `wk_crm_area` VALUES (320724, 320700, '灌南县');
INSERT INTO `wk_crm_area` VALUES (320703, 320700, '连云区');
INSERT INTO `wk_crm_area` VALUES (320722, 320700, '东海县');
INSERT INTO `wk_crm_area` VALUES (320723, 320700, '灌云县');
INSERT INTO `wk_crm_area` VALUES (320771, 320700, '连云港经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (320772, 320700, '连云港高新技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (410205, 410200, '禹王台区');
INSERT INTO `wk_crm_area` VALUES (410202, 410200, '龙亭区');
INSERT INTO `wk_crm_area` VALUES (410212, 410200, '祥符区');
INSERT INTO `wk_crm_area` VALUES (410223, 410200, '尉氏县');
INSERT INTO `wk_crm_area` VALUES (410204, 410200, '鼓楼区');
INSERT INTO `wk_crm_area` VALUES (410203, 410200, '顺河回族区');
INSERT INTO `wk_crm_area` VALUES (410225, 410200, '兰考县');
INSERT INTO `wk_crm_area` VALUES (410222, 410200, '通许县');
INSERT INTO `wk_crm_area` VALUES (410221, 410200, '杞县');
INSERT INTO `wk_crm_area` VALUES (140926, 140900, '静乐县');
INSERT INTO `wk_crm_area` VALUES (140927, 140900, '神池县');
INSERT INTO `wk_crm_area` VALUES (140928, 140900, '五寨县');
INSERT INTO `wk_crm_area` VALUES (140929, 140900, '岢岚县');
INSERT INTO `wk_crm_area` VALUES (140981, 140900, '原平市');
INSERT INTO `wk_crm_area` VALUES (140971, 140900, '五台山风景名胜区');
INSERT INTO `wk_crm_area` VALUES (140930, 140900, '河曲县');
INSERT INTO `wk_crm_area` VALUES (140931, 140900, '保德县');
INSERT INTO `wk_crm_area` VALUES (140921, 140900, '定襄县');
INSERT INTO `wk_crm_area` VALUES (140932, 140900, '偏关县');
INSERT INTO `wk_crm_area` VALUES (140922, 140900, '五台县');
INSERT INTO `wk_crm_area` VALUES (140923, 140900, '代县');
INSERT INTO `wk_crm_area` VALUES (140902, 140900, '忻府区');
INSERT INTO `wk_crm_area` VALUES (140924, 140900, '繁峙县');
INSERT INTO `wk_crm_area` VALUES (140925, 140900, '宁武县');
INSERT INTO `wk_crm_area` VALUES (360829, 360800, '安福县');
INSERT INTO `wk_crm_area` VALUES (360827, 360800, '遂川县');
INSERT INTO `wk_crm_area` VALUES (360828, 360800, '万安县');
INSERT INTO `wk_crm_area` VALUES (360881, 360800, '井冈山市');
INSERT INTO `wk_crm_area` VALUES (360803, 360800, '青原区');
INSERT INTO `wk_crm_area` VALUES (360825, 360800, '永丰县');
INSERT INTO `wk_crm_area` VALUES (360826, 360800, '泰和县');
INSERT INTO `wk_crm_area` VALUES (360823, 360800, '峡江县');
INSERT INTO `wk_crm_area` VALUES (360802, 360800, '吉州区');
INSERT INTO `wk_crm_area` VALUES (360824, 360800, '新干县');
INSERT INTO `wk_crm_area` VALUES (360821, 360800, '吉安县');
INSERT INTO `wk_crm_area` VALUES (360822, 360800, '吉水县');
INSERT INTO `wk_crm_area` VALUES (360830, 360800, '永新县');
INSERT INTO `wk_crm_area` VALUES (232761, 232700, '加格达奇区');
INSERT INTO `wk_crm_area` VALUES (232762, 232700, '松岭区');
INSERT INTO `wk_crm_area` VALUES (232763, 232700, '新林区');
INSERT INTO `wk_crm_area` VALUES (232764, 232700, '呼中区');
INSERT INTO `wk_crm_area` VALUES (232721, 232700, '呼玛县');
INSERT INTO `wk_crm_area` VALUES (232722, 232700, '塔河县');
INSERT INTO `wk_crm_area` VALUES (232701, 232700, '漠河市');
INSERT INTO `wk_crm_area` VALUES (341504, 341500, '叶集区');
INSERT INTO `wk_crm_area` VALUES (341503, 341500, '裕安区');
INSERT INTO `wk_crm_area` VALUES (341525, 341500, '霍山县');
INSERT INTO `wk_crm_area` VALUES (341502, 341500, '金安区');
INSERT INTO `wk_crm_area` VALUES (341524, 341500, '金寨县');
INSERT INTO `wk_crm_area` VALUES (341523, 341500, '舒城县');
INSERT INTO `wk_crm_area` VALUES (341522, 341500, '霍邱县');
INSERT INTO `wk_crm_area` VALUES (431003, 431000, '苏仙区');
INSERT INTO `wk_crm_area` VALUES (431025, 431000, '临武县');
INSERT INTO `wk_crm_area` VALUES (431002, 431000, '北湖区');
INSERT INTO `wk_crm_area` VALUES (431024, 431000, '嘉禾县');
INSERT INTO `wk_crm_area` VALUES (431027, 431000, '桂东县');
INSERT INTO `wk_crm_area` VALUES (431026, 431000, '汝城县');
INSERT INTO `wk_crm_area` VALUES (431028, 431000, '安仁县');
INSERT INTO `wk_crm_area` VALUES (431081, 431000, '资兴市');
INSERT INTO `wk_crm_area` VALUES (431021, 431000, '桂阳县');
INSERT INTO `wk_crm_area` VALUES (431023, 431000, '永兴县');
INSERT INTO `wk_crm_area` VALUES (431022, 431000, '宜章县');
INSERT INTO `wk_crm_area` VALUES (431322, 431300, '新化县');
INSERT INTO `wk_crm_area` VALUES (431321, 431300, '双峰县');
INSERT INTO `wk_crm_area` VALUES (431302, 431300, '娄星区');
INSERT INTO `wk_crm_area` VALUES (431382, 431300, '涟源市');
INSERT INTO `wk_crm_area` VALUES (431381, 431300, '冷水江市');
INSERT INTO `wk_crm_area` VALUES (620321, 620300, '永昌县');
INSERT INTO `wk_crm_area` VALUES (620302, 620300, '金川区');
INSERT INTO `wk_crm_area` VALUES (511322, 511300, '营山县');
INSERT INTO `wk_crm_area` VALUES (511323, 511300, '蓬安县');
INSERT INTO `wk_crm_area` VALUES (511321, 511300, '南部县');
INSERT INTO `wk_crm_area` VALUES (511304, 511300, '嘉陵区');
INSERT INTO `wk_crm_area` VALUES (511302, 511300, '顺庆区');
INSERT INTO `wk_crm_area` VALUES (511324, 511300, '仪陇县');
INSERT INTO `wk_crm_area` VALUES (511303, 511300, '高坪区');
INSERT INTO `wk_crm_area` VALUES (511325, 511300, '西充县');
INSERT INTO `wk_crm_area` VALUES (511381, 511300, '阆中市');
INSERT INTO `wk_crm_area` VALUES (410602, 410600, '鹤山区');
INSERT INTO `wk_crm_area` VALUES (410603, 410600, '山城区');
INSERT INTO `wk_crm_area` VALUES (410611, 410600, '淇滨区');
INSERT INTO `wk_crm_area` VALUES (410622, 410600, '淇县');
INSERT INTO `wk_crm_area` VALUES (410621, 410600, '浚县');
INSERT INTO `wk_crm_area` VALUES (410671, 410600, '鹤壁经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (330681, 330600, '诸暨市');
INSERT INTO `wk_crm_area` VALUES (330602, 330600, '越城区');
INSERT INTO `wk_crm_area` VALUES (330624, 330600, '新昌县');
INSERT INTO `wk_crm_area` VALUES (330603, 330600, '柯桥区');
INSERT INTO `wk_crm_area` VALUES (330604, 330600, '上虞区');
INSERT INTO `wk_crm_area` VALUES (330683, 330600, '嵊州市');
INSERT INTO `wk_crm_area` VALUES (370572, 370500, '东营港经济开发区');
INSERT INTO `wk_crm_area` VALUES (370571, 370500, '东营经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (370503, 370500, '河口区');
INSERT INTO `wk_crm_area` VALUES (370505, 370500, '垦利区');
INSERT INTO `wk_crm_area` VALUES (370522, 370500, '利津县');
INSERT INTO `wk_crm_area` VALUES (370523, 370500, '广饶县');
INSERT INTO `wk_crm_area` VALUES (370502, 370500, '东营区');
INSERT INTO `wk_crm_area` VALUES (210727, 210700, '义县');
INSERT INTO `wk_crm_area` VALUES (210726, 210700, '黑山县');
INSERT INTO `wk_crm_area` VALUES (210703, 210700, '凌河区');
INSERT INTO `wk_crm_area` VALUES (210702, 210700, '古塔区');
INSERT INTO `wk_crm_area` VALUES (210781, 210700, '凌海市');
INSERT INTO `wk_crm_area` VALUES (210782, 210700, '北镇市');
INSERT INTO `wk_crm_area` VALUES (210711, 210700, '太和区');
INSERT INTO `wk_crm_area` VALUES (340405, 340400, '八公山区');
INSERT INTO `wk_crm_area` VALUES (340406, 340400, '潘集区');
INSERT INTO `wk_crm_area` VALUES (340403, 340400, '田家庵区');
INSERT INTO `wk_crm_area` VALUES (340404, 340400, '谢家集区');
INSERT INTO `wk_crm_area` VALUES (340402, 340400, '大通区');
INSERT INTO `wk_crm_area` VALUES (340421, 340400, '凤台县');
INSERT INTO `wk_crm_area` VALUES (340422, 340400, '寿县');
INSERT INTO `wk_crm_area` VALUES (711292, 711200, '罗东镇');
INSERT INTO `wk_crm_area` VALUES (711293, 711200, '三星乡');
INSERT INTO `wk_crm_area` VALUES (711294, 711200, '大同乡');
INSERT INTO `wk_crm_area` VALUES (711295, 711200, '五结乡');
INSERT INTO `wk_crm_area` VALUES (711290, 711200, '壮围乡');
INSERT INTO `wk_crm_area` VALUES (711291, 711200, '员山乡');
INSERT INTO `wk_crm_area` VALUES (711289, 711200, '礁溪乡');
INSERT INTO `wk_crm_area` VALUES (711296, 711200, '冬山乡');
INSERT INTO `wk_crm_area` VALUES (711297, 711200, '苏澳镇');
INSERT INTO `wk_crm_area` VALUES (711287, 711200, '宜兰市');
INSERT INTO `wk_crm_area` VALUES (711298, 711200, '南澳乡');
INSERT INTO `wk_crm_area` VALUES (711288, 711200, '头城镇');
INSERT INTO `wk_crm_area` VALUES (711299, 711200, '钓鱼台');
INSERT INTO `wk_crm_area` VALUES (430211, 430200, '天元区');
INSERT INTO `wk_crm_area` VALUES (430202, 430200, '荷塘区');
INSERT INTO `wk_crm_area` VALUES (430224, 430200, '茶陵县');
INSERT INTO `wk_crm_area` VALUES (430212, 430200, '渌口区');
INSERT INTO `wk_crm_area` VALUES (430223, 430200, '攸县');
INSERT INTO `wk_crm_area` VALUES (430204, 430200, '石峰区');
INSERT INTO `wk_crm_area` VALUES (430203, 430200, '芦淞区');
INSERT INTO `wk_crm_area` VALUES (430225, 430200, '炎陵县');
INSERT INTO `wk_crm_area` VALUES (430271, 430200, '云龙示范区');
INSERT INTO `wk_crm_area` VALUES (430281, 430200, '醴陵市');
INSERT INTO `wk_crm_area` VALUES (350921, 350900, '霞浦县');
INSERT INTO `wk_crm_area` VALUES (350922, 350900, '古田县');
INSERT INTO `wk_crm_area` VALUES (350923, 350900, '屏南县');
INSERT INTO `wk_crm_area` VALUES (350902, 350900, '蕉城区');
INSERT INTO `wk_crm_area` VALUES (350924, 350900, '寿宁县');
INSERT INTO `wk_crm_area` VALUES (350925, 350900, '周宁县');
INSERT INTO `wk_crm_area` VALUES (350926, 350900, '柘荣县');
INSERT INTO `wk_crm_area` VALUES (350981, 350900, '福安市');
INSERT INTO `wk_crm_area` VALUES (350982, 350900, '福鼎市');
INSERT INTO `wk_crm_area` VALUES (650502, 650500, '伊州区');
INSERT INTO `wk_crm_area` VALUES (650522, 650500, '伊吾县');
INSERT INTO `wk_crm_area` VALUES (650521, 650500, '巴里坤哈萨克自治县');
INSERT INTO `wk_crm_area` VALUES (411702, 411700, '驿城区');
INSERT INTO `wk_crm_area` VALUES (411724, 411700, '正阳县');
INSERT INTO `wk_crm_area` VALUES (411723, 411700, '平舆县');
INSERT INTO `wk_crm_area` VALUES (411726, 411700, '泌阳县');
INSERT INTO `wk_crm_area` VALUES (411725, 411700, '确山县');
INSERT INTO `wk_crm_area` VALUES (411722, 411700, '上蔡县');
INSERT INTO `wk_crm_area` VALUES (411721, 411700, '西平县');
INSERT INTO `wk_crm_area` VALUES (411771, 411700, '河南驻马店经济开发区');
INSERT INTO `wk_crm_area` VALUES (411728, 411700, '遂平县');
INSERT INTO `wk_crm_area` VALUES (411727, 411700, '汝南县');
INSERT INTO `wk_crm_area` VALUES (411729, 411700, '新蔡县');
INSERT INTO `wk_crm_area` VALUES (130671, 130600, '保定高新技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (130672, 130600, '保定白沟新城');
INSERT INTO `wk_crm_area` VALUES (130631, 130600, '望都县');
INSERT INTO `wk_crm_area` VALUES (130632, 130600, '安新县');
INSERT INTO `wk_crm_area` VALUES (130630, 130600, '涞源县');
INSERT INTO `wk_crm_area` VALUES (130635, 130600, '蠡县');
INSERT INTO `wk_crm_area` VALUES (130636, 130600, '顺平县');
INSERT INTO `wk_crm_area` VALUES (130633, 130600, '易县');
INSERT INTO `wk_crm_area` VALUES (130634, 130600, '曲阳县');
INSERT INTO `wk_crm_area` VALUES (130637, 130600, '博野县');
INSERT INTO `wk_crm_area` VALUES (130638, 130600, '雄县');
INSERT INTO `wk_crm_area` VALUES (130682, 130600, '定州市');
INSERT INTO `wk_crm_area` VALUES (130683, 130600, '安国市');
INSERT INTO `wk_crm_area` VALUES (130681, 130600, '涿州市');
INSERT INTO `wk_crm_area` VALUES (130684, 130600, '高碑店市');
INSERT INTO `wk_crm_area` VALUES (130602, 130600, '竞秀区');
INSERT INTO `wk_crm_area` VALUES (130624, 130600, '阜平县');
INSERT INTO `wk_crm_area` VALUES (130623, 130600, '涞水县');
INSERT INTO `wk_crm_area` VALUES (130606, 130600, '莲池区');
INSERT INTO `wk_crm_area` VALUES (130628, 130600, '高阳县');
INSERT INTO `wk_crm_area` VALUES (130607, 130600, '满城区');
INSERT INTO `wk_crm_area` VALUES (130629, 130600, '容城县');
INSERT INTO `wk_crm_area` VALUES (130626, 130600, '定兴县');
INSERT INTO `wk_crm_area` VALUES (130627, 130600, '唐县');
INSERT INTO `wk_crm_area` VALUES (130608, 130600, '清苑区');
INSERT INTO `wk_crm_area` VALUES (130609, 130600, '徐水区');
INSERT INTO `wk_crm_area` VALUES (533103, 533100, '芒市');
INSERT INTO `wk_crm_area` VALUES (533102, 533100, '瑞丽市');
INSERT INTO `wk_crm_area` VALUES (533124, 533100, '陇川县');
INSERT INTO `wk_crm_area` VALUES (533123, 533100, '盈江县');
INSERT INTO `wk_crm_area` VALUES (533122, 533100, '梁河县');
INSERT INTO `wk_crm_area` VALUES (220503, 220500, '二道江区');
INSERT INTO `wk_crm_area` VALUES (220582, 220500, '集安市');
INSERT INTO `wk_crm_area` VALUES (220581, 220500, '梅河口市');
INSERT INTO `wk_crm_area` VALUES (220521, 220500, '通化县');
INSERT INTO `wk_crm_area` VALUES (220502, 220500, '东昌区');
INSERT INTO `wk_crm_area` VALUES (220524, 220500, '柳河县');
INSERT INTO `wk_crm_area` VALUES (220523, 220500, '辉南县');
INSERT INTO `wk_crm_area` VALUES (652825, 652800, '且末县');
INSERT INTO `wk_crm_area` VALUES (652826, 652800, '焉耆回族自治县');
INSERT INTO `wk_crm_area` VALUES (652827, 652800, '和静县');
INSERT INTO `wk_crm_area` VALUES (652828, 652800, '和硕县');
INSERT INTO `wk_crm_area` VALUES (652829, 652800, '博湖县');
INSERT INTO `wk_crm_area` VALUES (652871, 652800, '库尔勒经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (652822, 652800, '轮台县');
INSERT INTO `wk_crm_area` VALUES (652801, 652800, '库尔勒市');
INSERT INTO `wk_crm_area` VALUES (652823, 652800, '尉犁县');
INSERT INTO `wk_crm_area` VALUES (652824, 652800, '若羌县');
INSERT INTO `wk_crm_area` VALUES (371772, 371700, '菏泽高新技术开发区');
INSERT INTO `wk_crm_area` VALUES (371771, 371700, '菏泽经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (371728, 371700, '东明县');
INSERT INTO `wk_crm_area` VALUES (371703, 371700, '定陶区');
INSERT INTO `wk_crm_area` VALUES (371725, 371700, '郓城县');
INSERT INTO `wk_crm_area` VALUES (371702, 371700, '牡丹区');
INSERT INTO `wk_crm_area` VALUES (371724, 371700, '巨野县');
INSERT INTO `wk_crm_area` VALUES (371726, 371700, '鄄城县');
INSERT INTO `wk_crm_area` VALUES (371721, 371700, '曹县');
INSERT INTO `wk_crm_area` VALUES (371723, 371700, '成武县');
INSERT INTO `wk_crm_area` VALUES (371722, 371700, '单县');
INSERT INTO `wk_crm_area` VALUES (440900, 440000, '茂名市');
INSERT INTO `wk_crm_area` VALUES (440800, 440000, '湛江市');
INSERT INTO `wk_crm_area` VALUES (441900, 440000, '东莞市');
INSERT INTO `wk_crm_area` VALUES (440700, 440000, '江门市');
INSERT INTO `wk_crm_area` VALUES (441800, 440000, '清远市');
INSERT INTO `wk_crm_area` VALUES (440600, 440000, '佛山市');
INSERT INTO `wk_crm_area` VALUES (441700, 440000, '阳江市');
INSERT INTO `wk_crm_area` VALUES (440500, 440000, '汕头市');
INSERT INTO `wk_crm_area` VALUES (441600, 440000, '河源市');
INSERT INTO `wk_crm_area` VALUES (440400, 440000, '珠海市');
INSERT INTO `wk_crm_area` VALUES (441500, 440000, '汕尾市');
INSERT INTO `wk_crm_area` VALUES (440300, 440000, '深圳市');
INSERT INTO `wk_crm_area` VALUES (441400, 440000, '梅州市');
INSERT INTO `wk_crm_area` VALUES (445200, 440000, '揭阳市');
INSERT INTO `wk_crm_area` VALUES (440200, 440000, '韶关市');
INSERT INTO `wk_crm_area` VALUES (441300, 440000, '惠州市');
INSERT INTO `wk_crm_area` VALUES (445100, 440000, '潮州市');
INSERT INTO `wk_crm_area` VALUES (440100, 440000, '广州市');
INSERT INTO `wk_crm_area` VALUES (441200, 440000, '肇庆市');
INSERT INTO `wk_crm_area` VALUES (442000, 440000, '中山市');
INSERT INTO `wk_crm_area` VALUES (445300, 440000, '云浮市');
INSERT INTO `wk_crm_area` VALUES (610629, 610600, '洛川县');
INSERT INTO `wk_crm_area` VALUES (610627, 610600, '甘泉县');
INSERT INTO `wk_crm_area` VALUES (610628, 610600, '富县');
INSERT INTO `wk_crm_area` VALUES (610603, 610600, '安塞区');
INSERT INTO `wk_crm_area` VALUES (610625, 610600, '志丹县');
INSERT INTO `wk_crm_area` VALUES (610626, 610600, '吴起县');
INSERT INTO `wk_crm_area` VALUES (610623, 610600, '子长县');
INSERT INTO `wk_crm_area` VALUES (610602, 610600, '宝塔区');
INSERT INTO `wk_crm_area` VALUES (610621, 610600, '延长县');
INSERT INTO `wk_crm_area` VALUES (610632, 610600, '黄陵县');
INSERT INTO `wk_crm_area` VALUES (610622, 610600, '延川县');
INSERT INTO `wk_crm_area` VALUES (610630, 610600, '宜川县');
INSERT INTO `wk_crm_area` VALUES (610631, 610600, '黄龙县');
INSERT INTO `wk_crm_area` VALUES (230303, 230300, '恒山区');
INSERT INTO `wk_crm_area` VALUES (230302, 230300, '鸡冠区');
INSERT INTO `wk_crm_area` VALUES (230321, 230300, '鸡东县');
INSERT INTO `wk_crm_area` VALUES (230382, 230300, '密山市');
INSERT INTO `wk_crm_area` VALUES (230381, 230300, '虎林市');
INSERT INTO `wk_crm_area` VALUES (230307, 230300, '麻山区');
INSERT INTO `wk_crm_area` VALUES (230306, 230300, '城子河区');
INSERT INTO `wk_crm_area` VALUES (230305, 230300, '梨树区');
INSERT INTO `wk_crm_area` VALUES (230304, 230300, '滴道区');
INSERT INTO `wk_crm_area` VALUES (140471, 140400, '山西长治高新技术产业园区');
INSERT INTO `wk_crm_area` VALUES (140430, 140400, '沁县');
INSERT INTO `wk_crm_area` VALUES (140431, 140400, '沁源县');
INSERT INTO `wk_crm_area` VALUES (140423, 140400, '襄垣县');
INSERT INTO `wk_crm_area` VALUES (140403, 140400, '潞州区');
INSERT INTO `wk_crm_area` VALUES (140425, 140400, '平顺县');
INSERT INTO `wk_crm_area` VALUES (140404, 140400, '上党区');
INSERT INTO `wk_crm_area` VALUES (140426, 140400, '黎城县');
INSERT INTO `wk_crm_area` VALUES (140405, 140400, '屯留区');
INSERT INTO `wk_crm_area` VALUES (140427, 140400, '壶关县');
INSERT INTO `wk_crm_area` VALUES (140406, 140400, '潞城区');
INSERT INTO `wk_crm_area` VALUES (140428, 140400, '长子县');
INSERT INTO `wk_crm_area` VALUES (140429, 140400, '武乡县');
INSERT INTO `wk_crm_area` VALUES (150206, 150200, '白云鄂博矿区');
INSERT INTO `wk_crm_area` VALUES (150207, 150200, '九原区');
INSERT INTO `wk_crm_area` VALUES (150204, 150200, '青山区');
INSERT INTO `wk_crm_area` VALUES (150205, 150200, '石拐区');
INSERT INTO `wk_crm_area` VALUES (150202, 150200, '东河区');
INSERT INTO `wk_crm_area` VALUES (150203, 150200, '昆都仑区');
INSERT INTO `wk_crm_area` VALUES (150222, 150200, '固阳县');
INSERT INTO `wk_crm_area` VALUES (150223, 150200, '达尔罕茂明安联合旗');
INSERT INTO `wk_crm_area` VALUES (150221, 150200, '土默特右旗');
INSERT INTO `wk_crm_area` VALUES (150271, 150200, '包头稀土高新技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (152529, 152500, '正镶白旗');
INSERT INTO `wk_crm_area` VALUES (152528, 152500, '镶黄旗');
INSERT INTO `wk_crm_area` VALUES (152527, 152500, '太仆寺旗');
INSERT INTO `wk_crm_area` VALUES (152526, 152500, '西乌珠穆沁旗');
INSERT INTO `wk_crm_area` VALUES (152525, 152500, '东乌珠穆沁旗');
INSERT INTO `wk_crm_area` VALUES (152502, 152500, '锡林浩特市');
INSERT INTO `wk_crm_area` VALUES (152524, 152500, '苏尼特右旗');
INSERT INTO `wk_crm_area` VALUES (152501, 152500, '二连浩特市');
INSERT INTO `wk_crm_area` VALUES (152523, 152500, '苏尼特左旗');
INSERT INTO `wk_crm_area` VALUES (152522, 152500, '阿巴嘎旗');
INSERT INTO `wk_crm_area` VALUES (152531, 152500, '多伦县');
INSERT INTO `wk_crm_area` VALUES (152530, 152500, '正蓝旗');
INSERT INTO `wk_crm_area` VALUES (152571, 152500, '乌拉盖管委会');
INSERT INTO `wk_crm_area` VALUES (710108, 710100, '士林区');
INSERT INTO `wk_crm_area` VALUES (710107, 710100, '信义区');
INSERT INTO `wk_crm_area` VALUES (710106, 710100, '万华区');
INSERT INTO `wk_crm_area` VALUES (710105, 710100, '大安区');
INSERT INTO `wk_crm_area` VALUES (710109, 710100, '北投区');
INSERT INTO `wk_crm_area` VALUES (710111, 710100, '南港区');
INSERT INTO `wk_crm_area` VALUES (710199, 710100, '其它区');
INSERT INTO `wk_crm_area` VALUES (710110, 710100, '内湖区');
INSERT INTO `wk_crm_area` VALUES (710104, 710100, '松山区');
INSERT INTO `wk_crm_area` VALUES (710103, 710100, '中山区');
INSERT INTO `wk_crm_area` VALUES (710102, 710100, '大同区');
INSERT INTO `wk_crm_area` VALUES (710101, 710100, '中正区');
INSERT INTO `wk_crm_area` VALUES (710112, 710100, '文山区');
INSERT INTO `wk_crm_area` VALUES (712491, 712400, '新园乡');
INSERT INTO `wk_crm_area` VALUES (712470, 712400, '玛家乡');
INSERT INTO `wk_crm_area` VALUES (712492, 712400, '枋寮乡');
INSERT INTO `wk_crm_area` VALUES (712471, 712400, '九如乡');
INSERT INTO `wk_crm_area` VALUES (712493, 712400, '枋山乡');
INSERT INTO `wk_crm_area` VALUES (712472, 712400, '里港乡');
INSERT INTO `wk_crm_area` VALUES (712494, 712400, '春日乡');
INSERT INTO `wk_crm_area` VALUES (712490, 712400, '佳冬乡');
INSERT INTO `wk_crm_area` VALUES (712477, 712400, '竹田乡');
INSERT INTO `wk_crm_area` VALUES (712499, 712400, '满州乡');
INSERT INTO `wk_crm_area` VALUES (712478, 712400, '内埔乡');
INSERT INTO `wk_crm_area` VALUES (712479, 712400, '万丹乡');
INSERT INTO `wk_crm_area` VALUES (712473, 712400, '高树乡');
INSERT INTO `wk_crm_area` VALUES (712495, 712400, '狮子乡');
INSERT INTO `wk_crm_area` VALUES (712474, 712400, '盐埔乡');
INSERT INTO `wk_crm_area` VALUES (712496, 712400, '车城乡');
INSERT INTO `wk_crm_area` VALUES (712475, 712400, '长治乡');
INSERT INTO `wk_crm_area` VALUES (712497, 712400, '牡丹乡');
INSERT INTO `wk_crm_area` VALUES (712476, 712400, '麟洛乡');
INSERT INTO `wk_crm_area` VALUES (712498, 712400, '恒春镇');
INSERT INTO `wk_crm_area` VALUES (712480, 712400, '潮州镇');
INSERT INTO `wk_crm_area` VALUES (712481, 712400, '泰武乡');
INSERT INTO `wk_crm_area` VALUES (712482, 712400, '来义乡');
INSERT INTO `wk_crm_area` VALUES (712483, 712400, '万峦乡');
INSERT INTO `wk_crm_area` VALUES (712488, 712400, '东港镇');
INSERT INTO `wk_crm_area` VALUES (712467, 712400, '屏东市');
INSERT INTO `wk_crm_area` VALUES (712489, 712400, '琉球乡');
INSERT INTO `wk_crm_area` VALUES (712468, 712400, '三地门乡');
INSERT INTO `wk_crm_area` VALUES (712469, 712400, '雾台乡');
INSERT INTO `wk_crm_area` VALUES (712484, 712400, '莰顶乡');
INSERT INTO `wk_crm_area` VALUES (712485, 712400, '新埤乡');
INSERT INTO `wk_crm_area` VALUES (712486, 712400, '南州乡');
INSERT INTO `wk_crm_area` VALUES (712487, 712400, '林边乡');
INSERT INTO `wk_crm_area` VALUES (320803, 320800, '淮安区');
INSERT INTO `wk_crm_area` VALUES (320804, 320800, '淮阴区');
INSERT INTO `wk_crm_area` VALUES (320826, 320800, '涟水县');
INSERT INTO `wk_crm_area` VALUES (320812, 320800, '清江浦区');
INSERT INTO `wk_crm_area` VALUES (320813, 320800, '洪泽区');
INSERT INTO `wk_crm_area` VALUES (320830, 320800, '盱眙县');
INSERT INTO `wk_crm_area` VALUES (320831, 320800, '金湖县');
INSERT INTO `wk_crm_area` VALUES (320871, 320800, '淮安经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (360728, 360700, '定南县');
INSERT INTO `wk_crm_area` VALUES (360729, 360700, '全南县');
INSERT INTO `wk_crm_area` VALUES (360730, 360700, '宁都县');
INSERT INTO `wk_crm_area` VALUES (360781, 360700, '瑞金市');
INSERT INTO `wk_crm_area` VALUES (360704, 360700, '赣县区');
INSERT INTO `wk_crm_area` VALUES (360726, 360700, '安远县');
INSERT INTO `wk_crm_area` VALUES (360727, 360700, '龙南县');
INSERT INTO `wk_crm_area` VALUES (360702, 360700, '章贡区');
INSERT INTO `wk_crm_area` VALUES (360724, 360700, '上犹县');
INSERT INTO `wk_crm_area` VALUES (360735, 360700, '石城县');
INSERT INTO `wk_crm_area` VALUES (360703, 360700, '南康区');
INSERT INTO `wk_crm_area` VALUES (360725, 360700, '崇义县');
INSERT INTO `wk_crm_area` VALUES (360722, 360700, '信丰县');
INSERT INTO `wk_crm_area` VALUES (360733, 360700, '会昌县');
INSERT INTO `wk_crm_area` VALUES (360723, 360700, '大余县');
INSERT INTO `wk_crm_area` VALUES (360734, 360700, '寻乌县');
INSERT INTO `wk_crm_area` VALUES (360731, 360700, '于都县');
INSERT INTO `wk_crm_area` VALUES (360732, 360700, '兴国县');
INSERT INTO `wk_crm_area` VALUES (620421, 620400, '靖远县');
INSERT INTO `wk_crm_area` VALUES (620422, 620400, '会宁县');
INSERT INTO `wk_crm_area` VALUES (620423, 620400, '景泰县');
INSERT INTO `wk_crm_area` VALUES (620402, 620400, '白银区');
INSERT INTO `wk_crm_area` VALUES (620403, 620400, '平川区');
INSERT INTO `wk_crm_area` VALUES (341602, 341600, '谯城区');
INSERT INTO `wk_crm_area` VALUES (341623, 341600, '利辛县');
INSERT INTO `wk_crm_area` VALUES (341622, 341600, '蒙城县');
INSERT INTO `wk_crm_area` VALUES (341621, 341600, '涡阳县');
INSERT INTO `wk_crm_area` VALUES (431223, 431200, '辰溪县');
INSERT INTO `wk_crm_area` VALUES (431222, 431200, '沅陵县');
INSERT INTO `wk_crm_area` VALUES (431225, 431200, '会同县');
INSERT INTO `wk_crm_area` VALUES (431202, 431200, '鹤城区');
INSERT INTO `wk_crm_area` VALUES (431224, 431200, '溆浦县');
INSERT INTO `wk_crm_area` VALUES (431227, 431200, '新晃侗族自治县');
INSERT INTO `wk_crm_area` VALUES (431226, 431200, '麻阳苗族自治县');
INSERT INTO `wk_crm_area` VALUES (431229, 431200, '靖州苗族侗族自治县');
INSERT INTO `wk_crm_area` VALUES (431228, 431200, '芷江侗族自治县');
INSERT INTO `wk_crm_area` VALUES (431281, 431200, '洪江市');
INSERT INTO `wk_crm_area` VALUES (431271, 431200, '怀化市洪江管理区');
INSERT INTO `wk_crm_area` VALUES (431230, 431200, '通道侗族自治县');
INSERT INTO `wk_crm_area` VALUES (431221, 431200, '中方县');
INSERT INTO `wk_crm_area` VALUES (429021, 429000, '神农架林区');
INSERT INTO `wk_crm_area` VALUES (429006, 429000, '天门市');
INSERT INTO `wk_crm_area` VALUES (429005, 429000, '潜江市');
INSERT INTO `wk_crm_area` VALUES (429004, 429000, '仙桃市');
INSERT INTO `wk_crm_area` VALUES (410503, 410500, '北关区');
INSERT INTO `wk_crm_area` VALUES (410502, 410500, '文峰区');
INSERT INTO `wk_crm_area` VALUES (410505, 410500, '殷都区');
INSERT INTO `wk_crm_area` VALUES (410527, 410500, '内黄县');
INSERT INTO `wk_crm_area` VALUES (410526, 410500, '滑县');
INSERT INTO `wk_crm_area` VALUES (410523, 410500, '汤阴县');
INSERT INTO `wk_crm_area` VALUES (410522, 410500, '安阳县');
INSERT INTO `wk_crm_area` VALUES (410571, 410500, '安阳高新技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (410506, 410500, '龙安区');
INSERT INTO `wk_crm_area` VALUES (410581, 410500, '林州市');
INSERT INTO `wk_crm_area` VALUES (330727, 330700, '磐安县');
INSERT INTO `wk_crm_area` VALUES (330723, 330700, '武义县');
INSERT INTO `wk_crm_area` VALUES (330702, 330700, '婺城区');
INSERT INTO `wk_crm_area` VALUES (330703, 330700, '金东区');
INSERT INTO `wk_crm_area` VALUES (330726, 330700, '浦江县');
INSERT INTO `wk_crm_area` VALUES (330781, 330700, '兰溪市');
INSERT INTO `wk_crm_area` VALUES (330782, 330700, '义乌市');
INSERT INTO `wk_crm_area` VALUES (330783, 330700, '东阳市');
INSERT INTO `wk_crm_area` VALUES (330784, 330700, '永康市');
INSERT INTO `wk_crm_area` VALUES (370481, 370400, '滕州市');
INSERT INTO `wk_crm_area` VALUES (370404, 370400, '峄城区');
INSERT INTO `wk_crm_area` VALUES (370405, 370400, '台儿庄区');
INSERT INTO `wk_crm_area` VALUES (370406, 370400, '山亭区');
INSERT INTO `wk_crm_area` VALUES (370402, 370400, '市中区');
INSERT INTO `wk_crm_area` VALUES (370403, 370400, '薛城区');
INSERT INTO `wk_crm_area` VALUES (420302, 420300, '茅箭区');
INSERT INTO `wk_crm_area` VALUES (420324, 420300, '竹溪县');
INSERT INTO `wk_crm_area` VALUES (420323, 420300, '竹山县');
INSERT INTO `wk_crm_area` VALUES (420322, 420300, '郧西县');
INSERT INTO `wk_crm_area` VALUES (420304, 420300, '郧阳区');
INSERT INTO `wk_crm_area` VALUES (420303, 420300, '张湾区');
INSERT INTO `wk_crm_area` VALUES (420325, 420300, '房县');
INSERT INTO `wk_crm_area` VALUES (420381, 420300, '丹江口市');
INSERT INTO `wk_crm_area` VALUES (522301, 522300, '兴义市');
INSERT INTO `wk_crm_area` VALUES (522323, 522300, '普安县');
INSERT INTO `wk_crm_area` VALUES (522302, 522300, '兴仁市');
INSERT INTO `wk_crm_area` VALUES (522324, 522300, '晴隆县');
INSERT INTO `wk_crm_area` VALUES (522325, 522300, '贞丰县');
INSERT INTO `wk_crm_area` VALUES (522326, 522300, '望谟县');
INSERT INTO `wk_crm_area` VALUES (522327, 522300, '册亨县');
INSERT INTO `wk_crm_area` VALUES (522328, 522300, '安龙县');
INSERT INTO `wk_crm_area` VALUES (522300, 520000, '黔西南布依族苗族自治州');
INSERT INTO `wk_crm_area` VALUES (520200, 520000, '六盘水市');
INSERT INTO `wk_crm_area` VALUES (520300, 520000, '遵义市');
INSERT INTO `wk_crm_area` VALUES (520100, 520000, '贵阳市');
INSERT INTO `wk_crm_area` VALUES (520400, 520000, '安顺市');
INSERT INTO `wk_crm_area` VALUES (522600, 520000, '黔东南苗族侗族自治州');
INSERT INTO `wk_crm_area` VALUES (520500, 520000, '毕节市');
INSERT INTO `wk_crm_area` VALUES (522700, 520000, '黔南布依族苗族自治州');
INSERT INTO `wk_crm_area` VALUES (520600, 520000, '铜仁市');
INSERT INTO `wk_crm_area` VALUES (340504, 340500, '雨山区');
INSERT INTO `wk_crm_area` VALUES (340503, 340500, '花山区');
INSERT INTO `wk_crm_area` VALUES (340506, 340500, '博望区');
INSERT INTO `wk_crm_area` VALUES (340522, 340500, '含山县');
INSERT INTO `wk_crm_area` VALUES (340523, 340500, '和县');
INSERT INTO `wk_crm_area` VALUES (340521, 340500, '当涂县');
INSERT INTO `wk_crm_area` VALUES (210604, 210600, '振安区');
INSERT INTO `wk_crm_area` VALUES (210603, 210600, '振兴区');
INSERT INTO `wk_crm_area` VALUES (210682, 210600, '凤城市');
INSERT INTO `wk_crm_area` VALUES (210681, 210600, '东港市');
INSERT INTO `wk_crm_area` VALUES (210602, 210600, '元宝区');
INSERT INTO `wk_crm_area` VALUES (210624, 210600, '宽甸满族自治县');
INSERT INTO `wk_crm_area` VALUES (430200, 430000, '株洲市');
INSERT INTO `wk_crm_area` VALUES (431300, 430000, '娄底市');
INSERT INTO `wk_crm_area` VALUES (430100, 430000, '长沙市');
INSERT INTO `wk_crm_area` VALUES (431200, 430000, '怀化市');
INSERT INTO `wk_crm_area` VALUES (430400, 430000, '衡阳市');
INSERT INTO `wk_crm_area` VALUES (430300, 430000, '湘潭市');
INSERT INTO `wk_crm_area` VALUES (430600, 430000, '岳阳市');
INSERT INTO `wk_crm_area` VALUES (430500, 430000, '邵阳市');
INSERT INTO `wk_crm_area` VALUES (430800, 430000, '张家界市');
INSERT INTO `wk_crm_area` VALUES (430700, 430000, '常德市');
INSERT INTO `wk_crm_area` VALUES (430900, 430000, '益阳市');
INSERT INTO `wk_crm_area` VALUES (431100, 430000, '永州市');
INSERT INTO `wk_crm_area` VALUES (431000, 430000, '郴州市');
INSERT INTO `wk_crm_area` VALUES (433100, 430000, '湘西土家族苗族自治州');
INSERT INTO `wk_crm_area` VALUES (711150, 711100, '新庄区');
INSERT INTO `wk_crm_area` VALUES (711140, 711100, '新店区');
INSERT INTO `wk_crm_area` VALUES (711151, 711100, '泰山区');
INSERT INTO `wk_crm_area` VALUES (711130, 711100, '万里区');
INSERT INTO `wk_crm_area` VALUES (711141, 711100, '坪林区');
INSERT INTO `wk_crm_area` VALUES (711152, 711100, '林口区');
INSERT INTO `wk_crm_area` VALUES (711146, 711100, '三峡区');
INSERT INTO `wk_crm_area` VALUES (711157, 711100, '三芝区');
INSERT INTO `wk_crm_area` VALUES (711136, 711100, '瑞芳区');
INSERT INTO `wk_crm_area` VALUES (711147, 711100, '树林区');
INSERT INTO `wk_crm_area` VALUES (711137, 711100, '平溪区');
INSERT INTO `wk_crm_area` VALUES (711138, 711100, '双溪区');
INSERT INTO `wk_crm_area` VALUES (711149, 711100, '三重区');
INSERT INTO `wk_crm_area` VALUES (711142, 711100, '乌来区');
INSERT INTO `wk_crm_area` VALUES (711132, 711100, '板桥区');
INSERT INTO `wk_crm_area` VALUES (711143, 711100, '永和区');
INSERT INTO `wk_crm_area` VALUES (711154, 711100, '五股区');
INSERT INTO `wk_crm_area` VALUES (711133, 711100, '汐止区');
INSERT INTO `wk_crm_area` VALUES (711144, 711100, '中和区');
INSERT INTO `wk_crm_area` VALUES (711155, 711100, '八里区');
INSERT INTO `wk_crm_area` VALUES (711134, 711100, '深坑区');
INSERT INTO `wk_crm_area` VALUES (711145, 711100, '土城区');
INSERT INTO `wk_crm_area` VALUES (711156, 711100, '淡水区');
INSERT INTO `wk_crm_area` VALUES (430112, 430100, '望城区');
INSERT INTO `wk_crm_area` VALUES (430111, 430100, '雨花区');
INSERT INTO `wk_crm_area` VALUES (430103, 430100, '天心区');
INSERT INTO `wk_crm_area` VALUES (430102, 430100, '芙蓉区');
INSERT INTO `wk_crm_area` VALUES (430105, 430100, '开福区');
INSERT INTO `wk_crm_area` VALUES (430104, 430100, '岳麓区');
INSERT INTO `wk_crm_area` VALUES (430181, 430100, '浏阳市');
INSERT INTO `wk_crm_area` VALUES (430182, 430100, '宁乡市');
INSERT INTO `wk_crm_area` VALUES (430121, 430100, '长沙县');
INSERT INTO `wk_crm_area` VALUES (350821, 350800, '长汀县');
INSERT INTO `wk_crm_area` VALUES (350823, 350800, '上杭县');
INSERT INTO `wk_crm_area` VALUES (350802, 350800, '新罗区');
INSERT INTO `wk_crm_area` VALUES (350824, 350800, '武平县');
INSERT INTO `wk_crm_area` VALUES (350803, 350800, '永定区');
INSERT INTO `wk_crm_area` VALUES (350825, 350800, '连城县');
INSERT INTO `wk_crm_area` VALUES (350881, 350800, '漳平市');
INSERT INTO `wk_crm_area` VALUES (510184, 510100, '崇州市');
INSERT INTO `wk_crm_area` VALUES (510183, 510100, '邛崃市');
INSERT INTO `wk_crm_area` VALUES (510131, 510100, '蒲江县');
INSERT INTO `wk_crm_area` VALUES (510185, 510100, '简阳市');
INSERT INTO `wk_crm_area` VALUES (510121, 510100, '金堂县');
INSERT INTO `wk_crm_area` VALUES (510132, 510100, '新津县');
INSERT INTO `wk_crm_area` VALUES (510113, 510100, '青白江区');
INSERT INTO `wk_crm_area` VALUES (510112, 510100, '龙泉驿区');
INSERT INTO `wk_crm_area` VALUES (510182, 510100, '彭州市');
INSERT INTO `wk_crm_area` VALUES (510181, 510100, '都江堰市');
INSERT INTO `wk_crm_area` VALUES (510104, 510100, '锦江区');
INSERT INTO `wk_crm_area` VALUES (510115, 510100, '温江区');
INSERT INTO `wk_crm_area` VALUES (510114, 510100, '新都区');
INSERT INTO `wk_crm_area` VALUES (510106, 510100, '金牛区');
INSERT INTO `wk_crm_area` VALUES (510117, 510100, '郫都区');
INSERT INTO `wk_crm_area` VALUES (510105, 510100, '青羊区');
INSERT INTO `wk_crm_area` VALUES (510116, 510100, '双流区');
INSERT INTO `wk_crm_area` VALUES (510108, 510100, '成华区');
INSERT INTO `wk_crm_area` VALUES (510107, 510100, '武侯区');
INSERT INTO `wk_crm_area` VALUES (510129, 510100, '大邑县');
INSERT INTO `wk_crm_area` VALUES (650402, 650400, '高昌区');
INSERT INTO `wk_crm_area` VALUES (650422, 650400, '托克逊县');
INSERT INTO `wk_crm_area` VALUES (650421, 650400, '鄯善县');
INSERT INTO `wk_crm_area` VALUES (130581, 130500, '南宫市');
INSERT INTO `wk_crm_area` VALUES (130571, 130500, '河北邢台经济开发区');
INSERT INTO `wk_crm_area` VALUES (130582, 130500, '沙河市');
INSERT INTO `wk_crm_area` VALUES (130521, 130500, '邢台县');
INSERT INTO `wk_crm_area` VALUES (130532, 130500, '平乡县');
INSERT INTO `wk_crm_area` VALUES (130522, 130500, '临城县');
INSERT INTO `wk_crm_area` VALUES (130533, 130500, '威县');
INSERT INTO `wk_crm_area` VALUES (130530, 130500, '新河县');
INSERT INTO `wk_crm_area` VALUES (130531, 130500, '广宗县');
INSERT INTO `wk_crm_area` VALUES (130503, 130500, '桥西区');
INSERT INTO `wk_crm_area` VALUES (130525, 130500, '隆尧县');
INSERT INTO `wk_crm_area` VALUES (130526, 130500, '任县');
INSERT INTO `wk_crm_area` VALUES (130523, 130500, '内丘县');
INSERT INTO `wk_crm_area` VALUES (130534, 130500, '清河县');
INSERT INTO `wk_crm_area` VALUES (130502, 130500, '桥东区');
INSERT INTO `wk_crm_area` VALUES (130524, 130500, '柏乡县');
INSERT INTO `wk_crm_area` VALUES (130535, 130500, '临西县');
INSERT INTO `wk_crm_area` VALUES (130529, 130500, '巨鹿县');
INSERT INTO `wk_crm_area` VALUES (130527, 130500, '南和县');
INSERT INTO `wk_crm_area` VALUES (130528, 130500, '宁晋县');
INSERT INTO `wk_crm_area` VALUES (411625, 411600, '郸城县');
INSERT INTO `wk_crm_area` VALUES (411602, 411600, '川汇区');
INSERT INTO `wk_crm_area` VALUES (411624, 411600, '沈丘县');
INSERT INTO `wk_crm_area` VALUES (411627, 411600, '太康县');
INSERT INTO `wk_crm_area` VALUES (411626, 411600, '淮阳县');
INSERT INTO `wk_crm_area` VALUES (411621, 411600, '扶沟县');
INSERT INTO `wk_crm_area` VALUES (411623, 411600, '商水县');
INSERT INTO `wk_crm_area` VALUES (411622, 411600, '西华县');
INSERT INTO `wk_crm_area` VALUES (411671, 411600, '河南周口经济开发区');
INSERT INTO `wk_crm_area` VALUES (411628, 411600, '鹿邑县');
INSERT INTO `wk_crm_area` VALUES (411681, 411600, '项城市');
INSERT INTO `wk_crm_area` VALUES (371681, 371600, '邹平市');
INSERT INTO `wk_crm_area` VALUES (371603, 371600, '沾化区');
INSERT INTO `wk_crm_area` VALUES (371625, 371600, '博兴县');
INSERT INTO `wk_crm_area` VALUES (371622, 371600, '阳信县');
INSERT INTO `wk_crm_area` VALUES (371621, 371600, '惠民县');
INSERT INTO `wk_crm_area` VALUES (371602, 371600, '滨城区');
INSERT INTO `wk_crm_area` VALUES (371623, 371600, '无棣县');
INSERT INTO `wk_crm_area` VALUES (610730, 610700, '佛坪县');
INSERT INTO `wk_crm_area` VALUES (610728, 610700, '镇巴县');
INSERT INTO `wk_crm_area` VALUES (610729, 610700, '留坝县');
INSERT INTO `wk_crm_area` VALUES (610726, 610700, '宁强县');
INSERT INTO `wk_crm_area` VALUES (610727, 610700, '略阳县');
INSERT INTO `wk_crm_area` VALUES (610702, 610700, '汉台区');
INSERT INTO `wk_crm_area` VALUES (610724, 610700, '西乡县');
INSERT INTO `wk_crm_area` VALUES (610703, 610700, '南郑区');
INSERT INTO `wk_crm_area` VALUES (610725, 610700, '勉县');
INSERT INTO `wk_crm_area` VALUES (610722, 610700, '城固县');
INSERT INTO `wk_crm_area` VALUES (610723, 610700, '洋县');
INSERT INTO `wk_crm_area` VALUES (652722, 652700, '精河县');
INSERT INTO `wk_crm_area` VALUES (652701, 652700, '博乐市');
INSERT INTO `wk_crm_area` VALUES (652723, 652700, '温泉县');
INSERT INTO `wk_crm_area` VALUES (652702, 652700, '阿拉山口市');
INSERT INTO `wk_crm_area` VALUES (220421, 220400, '东丰县');
INSERT INTO `wk_crm_area` VALUES (220422, 220400, '东辽县');
INSERT INTO `wk_crm_area` VALUES (220403, 220400, '西安区');
INSERT INTO `wk_crm_area` VALUES (220402, 220400, '龙山区');
INSERT INTO `wk_crm_area` VALUES (230281, 230200, '讷河市');
INSERT INTO `wk_crm_area` VALUES (230204, 230200, '铁锋区');
INSERT INTO `wk_crm_area` VALUES (230203, 230200, '建华区');
INSERT INTO `wk_crm_area` VALUES (230225, 230200, '甘南县');
INSERT INTO `wk_crm_area` VALUES (230202, 230200, '龙沙区');
INSERT INTO `wk_crm_area` VALUES (230224, 230200, '泰来县');
INSERT INTO `wk_crm_area` VALUES (230223, 230200, '依安县');
INSERT INTO `wk_crm_area` VALUES (230221, 230200, '龙江县');
INSERT INTO `wk_crm_area` VALUES (230231, 230200, '拜泉县');
INSERT INTO `wk_crm_area` VALUES (230230, 230200, '克东县');
INSERT INTO `wk_crm_area` VALUES (230208, 230200, '梅里斯达斡尔族区');
INSERT INTO `wk_crm_area` VALUES (230207, 230200, '碾子山区');
INSERT INTO `wk_crm_area` VALUES (230229, 230200, '克山县');
INSERT INTO `wk_crm_area` VALUES (230206, 230200, '富拉尔基区');
INSERT INTO `wk_crm_area` VALUES (230205, 230200, '昂昂溪区');
INSERT INTO `wk_crm_area` VALUES (230227, 230200, '富裕县');
INSERT INTO `wk_crm_area` VALUES (140321, 140300, '平定县');
INSERT INTO `wk_crm_area` VALUES (140311, 140300, '郊区');
INSERT INTO `wk_crm_area` VALUES (140322, 140300, '盂县');
INSERT INTO `wk_crm_area` VALUES (140302, 140300, '城区');
INSERT INTO `wk_crm_area` VALUES (140303, 140300, '矿区');
INSERT INTO `wk_crm_area` VALUES (320904, 320900, '大丰区');
INSERT INTO `wk_crm_area` VALUES (320902, 320900, '亭湖区');
INSERT INTO `wk_crm_area` VALUES (320924, 320900, '射阳县');
INSERT INTO `wk_crm_area` VALUES (320903, 320900, '盐都区');
INSERT INTO `wk_crm_area` VALUES (320925, 320900, '建湖县');
INSERT INTO `wk_crm_area` VALUES (320922, 320900, '滨海县');
INSERT INTO `wk_crm_area` VALUES (320923, 320900, '阜宁县');
INSERT INTO `wk_crm_area` VALUES (320921, 320900, '响水县');
INSERT INTO `wk_crm_area` VALUES (320971, 320900, '盐城经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (320981, 320900, '东台市');
INSERT INTO `wk_crm_area` VALUES (710900, 710000, '嘉义市');
INSERT INTO `wk_crm_area` VALUES (710800, 710000, '新竹市');
INSERT INTO `wk_crm_area` VALUES (710700, 710000, '基隆市');
INSERT INTO `wk_crm_area` VALUES (710600, 710000, '南投县');
INSERT INTO `wk_crm_area` VALUES (711700, 710000, '彰化县');
INSERT INTO `wk_crm_area` VALUES (712800, 710000, '连江县');
INSERT INTO `wk_crm_area` VALUES (711900, 710000, '嘉义县');
INSERT INTO `wk_crm_area` VALUES (710100, 710000, '台北市');
INSERT INTO `wk_crm_area` VALUES (711300, 710000, '新竹县');
INSERT INTO `wk_crm_area` VALUES (712400, 710000, '屏东县');
INSERT INTO `wk_crm_area` VALUES (711400, 710000, '桃园县');
INSERT INTO `wk_crm_area` VALUES (712500, 710000, '台东县');
INSERT INTO `wk_crm_area` VALUES (711500, 710000, '苗栗县');
INSERT INTO `wk_crm_area` VALUES (712600, 710000, '花莲县');
INSERT INTO `wk_crm_area` VALUES (712700, 710000, '澎湖县');
INSERT INTO `wk_crm_area` VALUES (710500, 710000, '金门县');
INSERT INTO `wk_crm_area` VALUES (710400, 710000, '台中市');
INSERT INTO `wk_crm_area` VALUES (712100, 710000, '云林县');
INSERT INTO `wk_crm_area` VALUES (710300, 710000, '台南市');
INSERT INTO `wk_crm_area` VALUES (711100, 710000, '新北市');
INSERT INTO `wk_crm_area` VALUES (710200, 710000, '高雄市');
INSERT INTO `wk_crm_area` VALUES (711200, 710000, '宜兰县');
INSERT INTO `wk_crm_area` VALUES (150171, 150100, '呼和浩特金海工业园区');
INSERT INTO `wk_crm_area` VALUES (150105, 150100, '赛罕区');
INSERT INTO `wk_crm_area` VALUES (150103, 150100, '回民区');
INSERT INTO `wk_crm_area` VALUES (150125, 150100, '武川县');
INSERT INTO `wk_crm_area` VALUES (150104, 150100, '玉泉区');
INSERT INTO `wk_crm_area` VALUES (150123, 150100, '和林格尔县');
INSERT INTO `wk_crm_area` VALUES (150102, 150100, '新城区');
INSERT INTO `wk_crm_area` VALUES (150124, 150100, '清水河县');
INSERT INTO `wk_crm_area` VALUES (150121, 150100, '土默特左旗');
INSERT INTO `wk_crm_area` VALUES (150122, 150100, '托克托县');
INSERT INTO `wk_crm_area` VALUES (150172, 150100, '呼和浩特经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (341702, 341700, '贵池区');
INSERT INTO `wk_crm_area` VALUES (341723, 341700, '青阳县');
INSERT INTO `wk_crm_area` VALUES (341722, 341700, '石台县');
INSERT INTO `wk_crm_area` VALUES (341721, 341700, '东至县');
INSERT INTO `wk_crm_area` VALUES (620503, 620500, '麦积区');
INSERT INTO `wk_crm_area` VALUES (620525, 620500, '张家川回族自治县');
INSERT INTO `wk_crm_area` VALUES (620521, 620500, '清水县');
INSERT INTO `wk_crm_area` VALUES (620522, 620500, '秦安县');
INSERT INTO `wk_crm_area` VALUES (620523, 620500, '甘谷县');
INSERT INTO `wk_crm_area` VALUES (620502, 620500, '秦州区');
INSERT INTO `wk_crm_area` VALUES (620524, 620500, '武山县');
INSERT INTO `wk_crm_area` VALUES (360681, 360600, '贵溪市');
INSERT INTO `wk_crm_area` VALUES (360603, 360600, '余江区');
INSERT INTO `wk_crm_area` VALUES (360602, 360600, '月湖区');
INSERT INTO `wk_crm_area` VALUES (410811, 410800, '山阳区');
INSERT INTO `wk_crm_area` VALUES (410822, 410800, '博爱县');
INSERT INTO `wk_crm_area` VALUES (410821, 410800, '修武县');
INSERT INTO `wk_crm_area` VALUES (410802, 410800, '解放区');
INSERT INTO `wk_crm_area` VALUES (410823, 410800, '武陟县');
INSERT INTO `wk_crm_area` VALUES (410883, 410800, '孟州市');
INSERT INTO `wk_crm_area` VALUES (410871, 410800, '焦作城乡一体化示范区');
INSERT INTO `wk_crm_area` VALUES (410882, 410800, '沁阳市');
INSERT INTO `wk_crm_area` VALUES (410804, 410800, '马村区');
INSERT INTO `wk_crm_area` VALUES (410803, 410800, '中站区');
INSERT INTO `wk_crm_area` VALUES (410825, 410800, '温县');
INSERT INTO `wk_crm_area` VALUES (370321, 370300, '桓台县');
INSERT INTO `wk_crm_area` VALUES (370322, 370300, '高青县');
INSERT INTO `wk_crm_area` VALUES (370305, 370300, '临淄区');
INSERT INTO `wk_crm_area` VALUES (370306, 370300, '周村区');
INSERT INTO `wk_crm_area` VALUES (370323, 370300, '沂源县');
INSERT INTO `wk_crm_area` VALUES (370302, 370300, '淄川区');
INSERT INTO `wk_crm_area` VALUES (370303, 370300, '张店区');
INSERT INTO `wk_crm_area` VALUES (370304, 370300, '博山区');
INSERT INTO `wk_crm_area` VALUES (330822, 330800, '常山县');
INSERT INTO `wk_crm_area` VALUES (330802, 330800, '柯城区');
INSERT INTO `wk_crm_area` VALUES (330824, 330800, '开化县');
INSERT INTO `wk_crm_area` VALUES (330803, 330800, '衢江区');
INSERT INTO `wk_crm_area` VALUES (330825, 330800, '龙游县');
INSERT INTO `wk_crm_area` VALUES (330881, 330800, '江山市');
INSERT INTO `wk_crm_area` VALUES (511132, 511100, '峨边彝族自治县');
INSERT INTO `wk_crm_area` VALUES (511102, 511100, '市中区');
INSERT INTO `wk_crm_area` VALUES (511113, 511100, '金口河区');
INSERT INTO `wk_crm_area` VALUES (511124, 511100, '井研县');
INSERT INTO `wk_crm_area` VALUES (511111, 511100, '沙湾区');
INSERT INTO `wk_crm_area` VALUES (511133, 511100, '马边彝族自治县');
INSERT INTO `wk_crm_area` VALUES (511112, 511100, '五通桥区');
INSERT INTO `wk_crm_area` VALUES (511123, 511100, '犍为县');
INSERT INTO `wk_crm_area` VALUES (511129, 511100, '沐川县');
INSERT INTO `wk_crm_area` VALUES (511126, 511100, '夹江县');
INSERT INTO `wk_crm_area` VALUES (511181, 511100, '峨眉山市');
INSERT INTO `wk_crm_area` VALUES (513431, 513400, '昭觉县');
INSERT INTO `wk_crm_area` VALUES (513430, 513400, '金阳县');
INSERT INTO `wk_crm_area` VALUES (513424, 513400, '德昌县');
INSERT INTO `wk_crm_area` VALUES (513435, 513400, '甘洛县');
INSERT INTO `wk_crm_area` VALUES (513401, 513400, '西昌市');
INSERT INTO `wk_crm_area` VALUES (513423, 513400, '盐源县');
INSERT INTO `wk_crm_area` VALUES (513434, 513400, '越西县');
INSERT INTO `wk_crm_area` VALUES (513422, 513400, '木里藏族自治县');
INSERT INTO `wk_crm_area` VALUES (513433, 513400, '冕宁县');
INSERT INTO `wk_crm_area` VALUES (513432, 513400, '喜德县');
INSERT INTO `wk_crm_area` VALUES (513428, 513400, '普格县');
INSERT INTO `wk_crm_area` VALUES (513427, 513400, '宁南县');
INSERT INTO `wk_crm_area` VALUES (513426, 513400, '会东县');
INSERT INTO `wk_crm_area` VALUES (513437, 513400, '雷波县');
INSERT INTO `wk_crm_area` VALUES (513425, 513400, '会理县');
INSERT INTO `wk_crm_area` VALUES (513436, 513400, '美姑县');
INSERT INTO `wk_crm_area` VALUES (513429, 513400, '布拖县');
INSERT INTO `wk_crm_area` VALUES (441303, 441300, '惠阳区');
INSERT INTO `wk_crm_area` VALUES (441302, 441300, '惠城区');
INSERT INTO `wk_crm_area` VALUES (441324, 441300, '龙门县');
INSERT INTO `wk_crm_area` VALUES (441323, 441300, '惠东县');
INSERT INTO `wk_crm_area` VALUES (441322, 441300, '博罗县');
INSERT INTO `wk_crm_area` VALUES (420684, 420600, '宜城市');
INSERT INTO `wk_crm_area` VALUES (420625, 420600, '谷城县');
INSERT INTO `wk_crm_area` VALUES (420602, 420600, '襄城区');
INSERT INTO `wk_crm_area` VALUES (420624, 420600, '南漳县');
INSERT INTO `wk_crm_area` VALUES (420607, 420600, '襄州区');
INSERT INTO `wk_crm_area` VALUES (420606, 420600, '樊城区');
INSERT INTO `wk_crm_area` VALUES (420626, 420600, '保康县');
INSERT INTO `wk_crm_area` VALUES (420683, 420600, '枣阳市');
INSERT INTO `wk_crm_area` VALUES (420682, 420600, '老河口市');
INSERT INTO `wk_crm_area` VALUES (711490, 711400, '杨梅市');
INSERT INTO `wk_crm_area` VALUES (711491, 711400, '新屋乡');
INSERT INTO `wk_crm_area` VALUES (711492, 711400, '观音乡');
INSERT INTO `wk_crm_area` VALUES (711493, 711400, '桃园市');
INSERT INTO `wk_crm_area` VALUES (711487, 711400, '中坜市');
INSERT INTO `wk_crm_area` VALUES (711498, 711400, '大园乡');
INSERT INTO `wk_crm_area` VALUES (711488, 711400, '平镇市');
INSERT INTO `wk_crm_area` VALUES (711499, 711400, '芦竹乡');
INSERT INTO `wk_crm_area` VALUES (711489, 711400, '龙潭乡');
INSERT INTO `wk_crm_area` VALUES (711494, 711400, '龟山乡');
INSERT INTO `wk_crm_area` VALUES (711495, 711400, '八德市');
INSERT INTO `wk_crm_area` VALUES (711496, 711400, '大溪镇');
INSERT INTO `wk_crm_area` VALUES (711497, 711400, '复兴乡');
INSERT INTO `wk_crm_area` VALUES (231281, 231200, '安达市');
INSERT INTO `wk_crm_area` VALUES (231282, 231200, '肇东市');
INSERT INTO `wk_crm_area` VALUES (231283, 231200, '海伦市');
INSERT INTO `wk_crm_area` VALUES (231221, 231200, '望奎县');
INSERT INTO `wk_crm_area` VALUES (231222, 231200, '兰西县');
INSERT INTO `wk_crm_area` VALUES (231223, 231200, '青冈县');
INSERT INTO `wk_crm_area` VALUES (231202, 231200, '北林区');
INSERT INTO `wk_crm_area` VALUES (231224, 231200, '庆安县');
INSERT INTO `wk_crm_area` VALUES (231225, 231200, '明水县');
INSERT INTO `wk_crm_area` VALUES (231226, 231200, '绥棱县');
INSERT INTO `wk_crm_area` VALUES (640522, 640500, '海原县');
INSERT INTO `wk_crm_area` VALUES (640521, 640500, '中宁县');
INSERT INTO `wk_crm_area` VALUES (640502, 640500, '沙坡头区');
INSERT INTO `wk_crm_area` VALUES (451103, 451100, '平桂区');
INSERT INTO `wk_crm_area` VALUES (451122, 451100, '钟山县');
INSERT INTO `wk_crm_area` VALUES (451121, 451100, '昭平县');
INSERT INTO `wk_crm_area` VALUES (451102, 451100, '八步区');
INSERT INTO `wk_crm_area` VALUES (451123, 451100, '富川瑶族自治县');
INSERT INTO `wk_crm_area` VALUES (340603, 340600, '相山区');
INSERT INTO `wk_crm_area` VALUES (340604, 340600, '烈山区');
INSERT INTO `wk_crm_area` VALUES (340602, 340600, '杜集区');
INSERT INTO `wk_crm_area` VALUES (340621, 340600, '濉溪县');
INSERT INTO `wk_crm_area` VALUES (210505, 210500, '南芬区');
INSERT INTO `wk_crm_area` VALUES (210504, 210500, '明山区');
INSERT INTO `wk_crm_area` VALUES (210521, 210500, '本溪满族自治县');
INSERT INTO `wk_crm_area` VALUES (210503, 210500, '溪湖区');
INSERT INTO `wk_crm_area` VALUES (210502, 210500, '平山区');
INSERT INTO `wk_crm_area` VALUES (210522, 210500, '桓仁满族自治县');
INSERT INTO `wk_crm_area` VALUES (350721, 350700, '顺昌县');
INSERT INTO `wk_crm_area` VALUES (350722, 350700, '浦城县');
INSERT INTO `wk_crm_area` VALUES (350723, 350700, '光泽县');
INSERT INTO `wk_crm_area` VALUES (350702, 350700, '延平区');
INSERT INTO `wk_crm_area` VALUES (350724, 350700, '松溪县');
INSERT INTO `wk_crm_area` VALUES (350703, 350700, '建阳区');
INSERT INTO `wk_crm_area` VALUES (350725, 350700, '政和县');
INSERT INTO `wk_crm_area` VALUES (350781, 350700, '邵武市');
INSERT INTO `wk_crm_area` VALUES (350782, 350700, '武夷山市');
INSERT INTO `wk_crm_area` VALUES (350783, 350700, '建瓯市');
INSERT INTO `wk_crm_area` VALUES (430422, 430400, '衡南县');
INSERT INTO `wk_crm_area` VALUES (430421, 430400, '衡阳县');
INSERT INTO `wk_crm_area` VALUES (430424, 430400, '衡东县');
INSERT INTO `wk_crm_area` VALUES (430412, 430400, '南岳区');
INSERT INTO `wk_crm_area` VALUES (430423, 430400, '衡山县');
INSERT INTO `wk_crm_area` VALUES (430426, 430400, '祁东县');
INSERT INTO `wk_crm_area` VALUES (430406, 430400, '雁峰区');
INSERT INTO `wk_crm_area` VALUES (430405, 430400, '珠晖区');
INSERT INTO `wk_crm_area` VALUES (430408, 430400, '蒸湘区');
INSERT INTO `wk_crm_area` VALUES (430407, 430400, '石鼓区');
INSERT INTO `wk_crm_area` VALUES (430471, 430400, '衡阳综合保税区');
INSERT INTO `wk_crm_area` VALUES (430482, 430400, '常宁市');
INSERT INTO `wk_crm_area` VALUES (430481, 430400, '耒阳市');
INSERT INTO `wk_crm_area` VALUES (430473, 430400, '湖南衡阳松木经济开发区');
INSERT INTO `wk_crm_area` VALUES (430472, 430400, '湖南衡阳高新技术产业园区');
INSERT INTO `wk_crm_area` VALUES (512000, 510000, '资阳市');
INSERT INTO `wk_crm_area` VALUES (513200, 510000, '阿坝藏族羌族自治州');
INSERT INTO `wk_crm_area` VALUES (511000, 510000, '内江市');
INSERT INTO `wk_crm_area` VALUES (510100, 510000, '成都市');
INSERT INTO `wk_crm_area` VALUES (511300, 510000, '南充市');
INSERT INTO `wk_crm_area` VALUES (511400, 510000, '眉山市');
INSERT INTO `wk_crm_area` VALUES (510300, 510000, '自贡市');
INSERT INTO `wk_crm_area` VALUES (511100, 510000, '乐山市');
INSERT INTO `wk_crm_area` VALUES (513400, 510000, '凉山彝族自治州');
INSERT INTO `wk_crm_area` VALUES (513300, 510000, '甘孜藏族自治州');
INSERT INTO `wk_crm_area` VALUES (510500, 510000, '泸州市');
INSERT INTO `wk_crm_area` VALUES (511700, 510000, '达州市');
INSERT INTO `wk_crm_area` VALUES (510400, 510000, '攀枝花市');
INSERT INTO `wk_crm_area` VALUES (511800, 510000, '雅安市');
INSERT INTO `wk_crm_area` VALUES (510700, 510000, '绵阳市');
INSERT INTO `wk_crm_area` VALUES (511500, 510000, '宜宾市');
INSERT INTO `wk_crm_area` VALUES (510600, 510000, '德阳市');
INSERT INTO `wk_crm_area` VALUES (511600, 510000, '广安市');
INSERT INTO `wk_crm_area` VALUES (510800, 510000, '广元市');
INSERT INTO `wk_crm_area` VALUES (511900, 510000, '巴中市');
INSERT INTO `wk_crm_area` VALUES (510900, 510000, '遂宁市');
INSERT INTO `wk_crm_area` VALUES (130881, 130800, '平泉市');
INSERT INTO `wk_crm_area` VALUES (130828, 130800, '围场满族蒙古族自治县');
INSERT INTO `wk_crm_area` VALUES (130871, 130800, '承德高新技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (130822, 130800, '兴隆县');
INSERT INTO `wk_crm_area` VALUES (130821, 130800, '承德县');
INSERT INTO `wk_crm_area` VALUES (130804, 130800, '鹰手营子矿区');
INSERT INTO `wk_crm_area` VALUES (130826, 130800, '丰宁满族自治县');
INSERT INTO `wk_crm_area` VALUES (130827, 130800, '宽城满族自治县');
INSERT INTO `wk_crm_area` VALUES (130802, 130800, '双桥区');
INSERT INTO `wk_crm_area` VALUES (130824, 130800, '滦平县');
INSERT INTO `wk_crm_area` VALUES (130803, 130800, '双滦区');
INSERT INTO `wk_crm_area` VALUES (130825, 130800, '隆化县');
INSERT INTO `wk_crm_area` VALUES (220382, 220300, '双辽市');
INSERT INTO `wk_crm_area` VALUES (220381, 220300, '公主岭市');
INSERT INTO `wk_crm_area` VALUES (220322, 220300, '梨树县');
INSERT INTO `wk_crm_area` VALUES (220302, 220300, '铁西区');
INSERT INTO `wk_crm_area` VALUES (220323, 220300, '伊通满族自治县');
INSERT INTO `wk_crm_area` VALUES (220303, 220300, '铁东区');
INSERT INTO `wk_crm_area` VALUES (371521, 371500, '阳谷县');
INSERT INTO `wk_crm_area` VALUES (371581, 371500, '临清市');
INSERT INTO `wk_crm_area` VALUES (371526, 371500, '高唐县');
INSERT INTO `wk_crm_area` VALUES (371523, 371500, '茌平县');
INSERT INTO `wk_crm_area` VALUES (371522, 371500, '莘县');
INSERT INTO `wk_crm_area` VALUES (371525, 371500, '冠县');
INSERT INTO `wk_crm_area` VALUES (371502, 371500, '东昌府区');
INSERT INTO `wk_crm_area` VALUES (371524, 371500, '东阿县');
INSERT INTO `wk_crm_area` VALUES (440282, 440200, '南雄市');
INSERT INTO `wk_crm_area` VALUES (440281, 440200, '乐昌市');
INSERT INTO `wk_crm_area` VALUES (440229, 440200, '翁源县');
INSERT INTO `wk_crm_area` VALUES (440205, 440200, '曲江区');
INSERT INTO `wk_crm_area` VALUES (440204, 440200, '浈江区');
INSERT INTO `wk_crm_area` VALUES (440203, 440200, '武江区');
INSERT INTO `wk_crm_area` VALUES (440224, 440200, '仁化县');
INSERT INTO `wk_crm_area` VALUES (440222, 440200, '始兴县');
INSERT INTO `wk_crm_area` VALUES (440233, 440200, '新丰县');
INSERT INTO `wk_crm_area` VALUES (440232, 440200, '乳源瑶族自治县');
INSERT INTO `wk_crm_area` VALUES (610881, 610800, '神木市');
INSERT INTO `wk_crm_area` VALUES (610829, 610800, '吴堡县');
INSERT INTO `wk_crm_area` VALUES (610827, 610800, '米脂县');
INSERT INTO `wk_crm_area` VALUES (610828, 610800, '佳县');
INSERT INTO `wk_crm_area` VALUES (610803, 610800, '横山区');
INSERT INTO `wk_crm_area` VALUES (610825, 610800, '定边县');
INSERT INTO `wk_crm_area` VALUES (610826, 610800, '绥德县');
INSERT INTO `wk_crm_area` VALUES (610802, 610800, '榆阳区');
INSERT INTO `wk_crm_area` VALUES (610824, 610800, '靖边县');
INSERT INTO `wk_crm_area` VALUES (610822, 610800, '府谷县');
INSERT INTO `wk_crm_area` VALUES (610830, 610800, '清涧县');
INSERT INTO `wk_crm_area` VALUES (610831, 610800, '子洲县');
INSERT INTO `wk_crm_area` VALUES (341825, 341800, '旌德县');
INSERT INTO `wk_crm_area` VALUES (341802, 341800, '宣州区');
INSERT INTO `wk_crm_area` VALUES (341824, 341800, '绩溪县');
INSERT INTO `wk_crm_area` VALUES (341823, 341800, '泾县');
INSERT INTO `wk_crm_area` VALUES (341822, 341800, '广德县');
INSERT INTO `wk_crm_area` VALUES (341871, 341800, '宣城市经济开发区');
INSERT INTO `wk_crm_area` VALUES (341881, 341800, '宁国市');
INSERT INTO `wk_crm_area` VALUES (341821, 341800, '郎溪县');
INSERT INTO `wk_crm_area` VALUES (230184, 230100, '五常市');
INSERT INTO `wk_crm_area` VALUES (230183, 230100, '尚志市');
INSERT INTO `wk_crm_area` VALUES (230127, 230100, '木兰县');
INSERT INTO `wk_crm_area` VALUES (230104, 230100, '道外区');
INSERT INTO `wk_crm_area` VALUES (230126, 230100, '巴彦县');
INSERT INTO `wk_crm_area` VALUES (230103, 230100, '南岗区');
INSERT INTO `wk_crm_area` VALUES (230125, 230100, '宾县');
INSERT INTO `wk_crm_area` VALUES (230102, 230100, '道里区');
INSERT INTO `wk_crm_area` VALUES (230113, 230100, '双城区');
INSERT INTO `wk_crm_area` VALUES (230124, 230100, '方正县');
INSERT INTO `wk_crm_area` VALUES (230112, 230100, '阿城区');
INSERT INTO `wk_crm_area` VALUES (230123, 230100, '依兰县');
INSERT INTO `wk_crm_area` VALUES (230111, 230100, '呼兰区');
INSERT INTO `wk_crm_area` VALUES (230110, 230100, '香坊区');
INSERT INTO `wk_crm_area` VALUES (230109, 230100, '松北区');
INSERT INTO `wk_crm_area` VALUES (230108, 230100, '平房区');
INSERT INTO `wk_crm_area` VALUES (230129, 230100, '延寿县');
INSERT INTO `wk_crm_area` VALUES (230128, 230100, '通河县');
INSERT INTO `wk_crm_area` VALUES (150428, 150400, '喀喇沁旗');
INSERT INTO `wk_crm_area` VALUES (150429, 150400, '宁城县');
INSERT INTO `wk_crm_area` VALUES (150404, 150400, '松山区');
INSERT INTO `wk_crm_area` VALUES (150426, 150400, '翁牛特旗');
INSERT INTO `wk_crm_area` VALUES (150402, 150400, '红山区');
INSERT INTO `wk_crm_area` VALUES (150424, 150400, '林西县');
INSERT INTO `wk_crm_area` VALUES (150403, 150400, '元宝山区');
INSERT INTO `wk_crm_area` VALUES (150425, 150400, '克什克腾旗');
INSERT INTO `wk_crm_area` VALUES (150422, 150400, '巴林左旗');
INSERT INTO `wk_crm_area` VALUES (150423, 150400, '巴林右旗');
INSERT INTO `wk_crm_area` VALUES (150421, 150400, '阿鲁科尔沁旗');
INSERT INTO `wk_crm_area` VALUES (150430, 150400, '敖汉旗');
INSERT INTO `wk_crm_area` VALUES (500231, 500200, '垫江县');
INSERT INTO `wk_crm_area` VALUES (500242, 500200, '酉阳土家族苗族自治县');
INSERT INTO `wk_crm_area` VALUES (500230, 500200, '丰都县');
INSERT INTO `wk_crm_area` VALUES (500241, 500200, '秀山土家族苗族自治县');
INSERT INTO `wk_crm_area` VALUES (500240, 500200, '石柱土家族自治县');
INSERT INTO `wk_crm_area` VALUES (500235, 500200, '云阳县');
INSERT INTO `wk_crm_area` VALUES (500233, 500200, '忠县');
INSERT INTO `wk_crm_area` VALUES (500243, 500200, '彭水苗族土家族自治县');
INSERT INTO `wk_crm_area` VALUES (500238, 500200, '巫溪县');
INSERT INTO `wk_crm_area` VALUES (500237, 500200, '巫山县');
INSERT INTO `wk_crm_area` VALUES (500236, 500200, '奉节县');
INSERT INTO `wk_crm_area` VALUES (500229, 500200, '城口县');
INSERT INTO `wk_crm_area` VALUES (140681, 140600, '怀仁市');
INSERT INTO `wk_crm_area` VALUES (140671, 140600, '山西朔州经济开发区');
INSERT INTO `wk_crm_area` VALUES (140621, 140600, '山阴县');
INSERT INTO `wk_crm_area` VALUES (140622, 140600, '应县');
INSERT INTO `wk_crm_area` VALUES (140623, 140600, '右玉县');
INSERT INTO `wk_crm_area` VALUES (140602, 140600, '朔城区');
INSERT INTO `wk_crm_area` VALUES (140603, 140600, '平鲁区');
INSERT INTO `wk_crm_area` VALUES (712690, 712600, '吉安乡');
INSERT INTO `wk_crm_area` VALUES (712691, 712600, '寿丰乡');
INSERT INTO `wk_crm_area` VALUES (712692, 712600, '凤林镇');
INSERT INTO `wk_crm_area` VALUES (712686, 712600, '花莲市');
INSERT INTO `wk_crm_area` VALUES (712697, 712600, '玉里镇');
INSERT INTO `wk_crm_area` VALUES (712687, 712600, '新城乡');
INSERT INTO `wk_crm_area` VALUES (712698, 712600, '卓溪乡');
INSERT INTO `wk_crm_area` VALUES (712688, 712600, '太鲁阁');
INSERT INTO `wk_crm_area` VALUES (712699, 712600, '富里乡');
INSERT INTO `wk_crm_area` VALUES (712689, 712600, '秀林乡');
INSERT INTO `wk_crm_area` VALUES (712693, 712600, '光复乡');
INSERT INTO `wk_crm_area` VALUES (712694, 712600, '丰滨乡');
INSERT INTO `wk_crm_area` VALUES (712695, 712600, '瑞穗乡');
INSERT INTO `wk_crm_area` VALUES (712696, 712600, '万荣乡');
INSERT INTO `wk_crm_area` VALUES (450800, 450000, '贵港市');
INSERT INTO `wk_crm_area` VALUES (450700, 450000, '钦州市');
INSERT INTO `wk_crm_area` VALUES (450900, 450000, '玉林市');
INSERT INTO `wk_crm_area` VALUES (450400, 450000, '梧州市');
INSERT INTO `wk_crm_area` VALUES (450300, 450000, '桂林市');
INSERT INTO `wk_crm_area` VALUES (451400, 450000, '崇左市');
INSERT INTO `wk_crm_area` VALUES (450600, 450000, '防城港市');
INSERT INTO `wk_crm_area` VALUES (450500, 450000, '北海市');
INSERT INTO `wk_crm_area` VALUES (451100, 450000, '贺州市');
INSERT INTO `wk_crm_area` VALUES (451000, 450000, '百色市');
INSERT INTO `wk_crm_area` VALUES (450200, 450000, '柳州市');
INSERT INTO `wk_crm_area` VALUES (451300, 450000, '来宾市');
INSERT INTO `wk_crm_area` VALUES (450100, 450000, '南宁市');
INSERT INTO `wk_crm_area` VALUES (451200, 450000, '河池市');
INSERT INTO `wk_crm_area` VALUES (659004, 659000, '五家渠市');
INSERT INTO `wk_crm_area` VALUES (659003, 659000, '图木舒克市');
INSERT INTO `wk_crm_area` VALUES (659002, 659000, '阿拉尔市');
INSERT INTO `wk_crm_area` VALUES (659006, 659000, '铁门关市');
INSERT INTO `wk_crm_area` VALUES (659001, 659000, '石河子市');
INSERT INTO `wk_crm_area` VALUES (360521, 360500, '分宜县');
INSERT INTO `wk_crm_area` VALUES (360502, 360500, '渝水区');
INSERT INTO `wk_crm_area` VALUES (620602, 620600, '凉州区');
INSERT INTO `wk_crm_area` VALUES (620621, 620600, '民勤县');
INSERT INTO `wk_crm_area` VALUES (620622, 620600, '古浪县');
INSERT INTO `wk_crm_area` VALUES (620623, 620600, '天祝藏族自治县');
INSERT INTO `wk_crm_area` VALUES (622927, 622900, '积石山保安族东乡族撒拉族自治县');
INSERT INTO `wk_crm_area` VALUES (622924, 622900, '广河县');
INSERT INTO `wk_crm_area` VALUES (622901, 622900, '临夏市');
INSERT INTO `wk_crm_area` VALUES (622923, 622900, '永靖县');
INSERT INTO `wk_crm_area` VALUES (622926, 622900, '东乡族自治县');
INSERT INTO `wk_crm_area` VALUES (622925, 622900, '和政县');
INSERT INTO `wk_crm_area` VALUES (622922, 622900, '康乐县');
INSERT INTO `wk_crm_area` VALUES (622921, 622900, '临夏县');
INSERT INTO `wk_crm_area` VALUES (710306, 710300, '安南区');
INSERT INTO `wk_crm_area` VALUES (710305, 710300, '安平区');
INSERT INTO `wk_crm_area` VALUES (710349, 710300, '官田区');
INSERT INTO `wk_crm_area` VALUES (710304, 710300, '北区');
INSERT INTO `wk_crm_area` VALUES (710348, 710300, '龙崎区');
INSERT INTO `wk_crm_area` VALUES (710303, 710300, '南区');
INSERT INTO `wk_crm_area` VALUES (710347, 710300, '关庙区');
INSERT INTO `wk_crm_area` VALUES (710369, 710300, '安定区');
INSERT INTO `wk_crm_area` VALUES (710353, 710300, '七股区');
INSERT INTO `wk_crm_area` VALUES (710352, 710300, '西港区');
INSERT INTO `wk_crm_area` VALUES (710351, 710300, '佳里区');
INSERT INTO `wk_crm_area` VALUES (710350, 710300, '麻豆区');
INSERT INTO `wk_crm_area` VALUES (710357, 710300, '新营区');
INSERT INTO `wk_crm_area` VALUES (710356, 710300, '北门区');
INSERT INTO `wk_crm_area` VALUES (710355, 710300, '学甲区');
INSERT INTO `wk_crm_area` VALUES (710399, 710300, '其它区');
INSERT INTO `wk_crm_area` VALUES (710354, 710300, '将军区');
INSERT INTO `wk_crm_area` VALUES (710339, 710300, '永康区');
INSERT INTO `wk_crm_area` VALUES (710359, 710300, '白河区');
INSERT INTO `wk_crm_area` VALUES (710358, 710300, '后壁区');
INSERT INTO `wk_crm_area` VALUES (710360, 710300, '东山区');
INSERT INTO `wk_crm_area` VALUES (710342, 710300, '左镇区');
INSERT INTO `wk_crm_area` VALUES (710364, 710300, '盐水区');
INSERT INTO `wk_crm_area` VALUES (710341, 710300, '新化区');
INSERT INTO `wk_crm_area` VALUES (710363, 710300, '柳营区');
INSERT INTO `wk_crm_area` VALUES (710340, 710300, '归仁区');
INSERT INTO `wk_crm_area` VALUES (710362, 710300, '下营区');
INSERT INTO `wk_crm_area` VALUES (710361, 710300, '六甲区');
INSERT INTO `wk_crm_area` VALUES (710302, 710300, '东区');
INSERT INTO `wk_crm_area` VALUES (710346, 710300, '仁德区');
INSERT INTO `wk_crm_area` VALUES (710368, 710300, '新市区');
INSERT INTO `wk_crm_area` VALUES (710301, 710300, '中西区');
INSERT INTO `wk_crm_area` VALUES (710345, 710300, '南化区');
INSERT INTO `wk_crm_area` VALUES (710367, 710300, '山上区');
INSERT INTO `wk_crm_area` VALUES (710344, 710300, '楠西区');
INSERT INTO `wk_crm_area` VALUES (710366, 710300, '大内区');
INSERT INTO `wk_crm_area` VALUES (710343, 710300, '玉井区');
INSERT INTO `wk_crm_area` VALUES (710365, 710300, '善化区');
INSERT INTO `wk_crm_area` VALUES (410711, 410700, '牧野区');
INSERT INTO `wk_crm_area` VALUES (410703, 410700, '卫滨区');
INSERT INTO `wk_crm_area` VALUES (410725, 410700, '原阳县');
INSERT INTO `wk_crm_area` VALUES (410702, 410700, '红旗区');
INSERT INTO `wk_crm_area` VALUES (410724, 410700, '获嘉县');
INSERT INTO `wk_crm_area` VALUES (410773, 410700, '新乡市平原城乡一体化示范区');
INSERT INTO `wk_crm_area` VALUES (410721, 410700, '新乡县');
INSERT INTO `wk_crm_area` VALUES (410727, 410700, '封丘县');
INSERT INTO `wk_crm_area` VALUES (410704, 410700, '凤泉区');
INSERT INTO `wk_crm_area` VALUES (410726, 410700, '延津县');
INSERT INTO `wk_crm_area` VALUES (410728, 410700, '长垣县');
INSERT INTO `wk_crm_area` VALUES (410781, 410700, '卫辉市');
INSERT INTO `wk_crm_area` VALUES (410772, 410700, '新乡经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (410771, 410700, '新乡高新技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (410782, 410700, '辉县市');
INSERT INTO `wk_crm_area` VALUES (511011, 511000, '东兴区');
INSERT INTO `wk_crm_area` VALUES (511025, 511000, '资中县');
INSERT INTO `wk_crm_area` VALUES (511002, 511000, '市中区');
INSERT INTO `wk_crm_area` VALUES (511024, 511000, '威远县');
INSERT INTO `wk_crm_area` VALUES (511083, 511000, '隆昌市');
INSERT INTO `wk_crm_area` VALUES (511071, 511000, '内江经济开发区');
INSERT INTO `wk_crm_area` VALUES (370211, 370200, '黄岛区');
INSERT INTO `wk_crm_area` VALUES (370212, 370200, '崂山区');
INSERT INTO `wk_crm_area` VALUES (370271, 370200, '青岛高新技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (370283, 370200, '平度市');
INSERT INTO `wk_crm_area` VALUES (370285, 370200, '莱西市');
INSERT INTO `wk_crm_area` VALUES (370281, 370200, '胶州市');
INSERT INTO `wk_crm_area` VALUES (370202, 370200, '市南区');
INSERT INTO `wk_crm_area` VALUES (370213, 370200, '李沧区');
INSERT INTO `wk_crm_area` VALUES (370203, 370200, '市北区');
INSERT INTO `wk_crm_area` VALUES (370214, 370200, '城阳区');
INSERT INTO `wk_crm_area` VALUES (370215, 370200, '即墨区');
INSERT INTO `wk_crm_area` VALUES (330903, 330900, '普陀区');
INSERT INTO `wk_crm_area` VALUES (330921, 330900, '岱山县');
INSERT INTO `wk_crm_area` VALUES (330922, 330900, '嵊泗县');
INSERT INTO `wk_crm_area` VALUES (330902, 330900, '定海区');
INSERT INTO `wk_crm_area` VALUES (513332, 513300, '石渠县');
INSERT INTO `wk_crm_area` VALUES (513331, 513300, '白玉县');
INSERT INTO `wk_crm_area` VALUES (513330, 513300, '德格县');
INSERT INTO `wk_crm_area` VALUES (513325, 513300, '雅江县');
INSERT INTO `wk_crm_area` VALUES (513336, 513300, '乡城县');
INSERT INTO `wk_crm_area` VALUES (513324, 513300, '九龙县');
INSERT INTO `wk_crm_area` VALUES (513335, 513300, '巴塘县');
INSERT INTO `wk_crm_area` VALUES (513301, 513300, '康定市');
INSERT INTO `wk_crm_area` VALUES (513323, 513300, '丹巴县');
INSERT INTO `wk_crm_area` VALUES (513334, 513300, '理塘县');
INSERT INTO `wk_crm_area` VALUES (513322, 513300, '泸定县');
INSERT INTO `wk_crm_area` VALUES (513333, 513300, '色达县');
INSERT INTO `wk_crm_area` VALUES (513329, 513300, '新龙县');
INSERT INTO `wk_crm_area` VALUES (513328, 513300, '甘孜县');
INSERT INTO `wk_crm_area` VALUES (513327, 513300, '炉霍县');
INSERT INTO `wk_crm_area` VALUES (513338, 513300, '得荣县');
INSERT INTO `wk_crm_area` VALUES (513326, 513300, '道孚县');
INSERT INTO `wk_crm_area` VALUES (513337, 513300, '稻城县');
INSERT INTO `wk_crm_area` VALUES (441284, 441200, '四会市');
INSERT INTO `wk_crm_area` VALUES (441204, 441200, '高要区');
INSERT INTO `wk_crm_area` VALUES (441226, 441200, '德庆县');
INSERT INTO `wk_crm_area` VALUES (441203, 441200, '鼎湖区');
INSERT INTO `wk_crm_area` VALUES (441225, 441200, '封开县');
INSERT INTO `wk_crm_area` VALUES (441202, 441200, '端州区');
INSERT INTO `wk_crm_area` VALUES (441224, 441200, '怀集县');
INSERT INTO `wk_crm_area` VALUES (441223, 441200, '广宁县');
INSERT INTO `wk_crm_area` VALUES (420583, 420500, '枝江市');
INSERT INTO `wk_crm_area` VALUES (420582, 420500, '当阳市');
INSERT INTO `wk_crm_area` VALUES (420581, 420500, '宜都市');
INSERT INTO `wk_crm_area` VALUES (420504, 420500, '点军区');
INSERT INTO `wk_crm_area` VALUES (420526, 420500, '兴山县');
INSERT INTO `wk_crm_area` VALUES (420503, 420500, '伍家岗区');
INSERT INTO `wk_crm_area` VALUES (420525, 420500, '远安县');
INSERT INTO `wk_crm_area` VALUES (420502, 420500, '西陵区');
INSERT INTO `wk_crm_area` VALUES (420529, 420500, '五峰土家族自治县');
INSERT INTO `wk_crm_area` VALUES (420506, 420500, '夷陵区');
INSERT INTO `wk_crm_area` VALUES (420528, 420500, '长阳土家族自治县');
INSERT INTO `wk_crm_area` VALUES (420505, 420500, '猇亭区');
INSERT INTO `wk_crm_area` VALUES (420527, 420500, '秭归县');
INSERT INTO `wk_crm_area` VALUES (422822, 422800, '建始县');
INSERT INTO `wk_crm_area` VALUES (422801, 422800, '恩施市');
INSERT INTO `wk_crm_area` VALUES (422823, 422800, '巴东县');
INSERT INTO `wk_crm_area` VALUES (422802, 422800, '利川市');
INSERT INTO `wk_crm_area` VALUES (422825, 422800, '宣恩县');
INSERT INTO `wk_crm_area` VALUES (422826, 422800, '咸丰县');
INSERT INTO `wk_crm_area` VALUES (422827, 422800, '来凤县');
INSERT INTO `wk_crm_area` VALUES (422828, 422800, '鹤峰县');
INSERT INTO `wk_crm_area` VALUES (340711, 340700, '郊区');
INSERT INTO `wk_crm_area` VALUES (340722, 340700, '枞阳县');
INSERT INTO `wk_crm_area` VALUES (340706, 340700, '义安区');
INSERT INTO `wk_crm_area` VALUES (340705, 340700, '铜官区');
INSERT INTO `wk_crm_area` VALUES (210411, 210400, '顺城区');
INSERT INTO `wk_crm_area` VALUES (210422, 210400, '新宾满族自治县');
INSERT INTO `wk_crm_area` VALUES (210421, 210400, '抚顺县');
INSERT INTO `wk_crm_area` VALUES (210404, 210400, '望花区');
INSERT INTO `wk_crm_area` VALUES (210403, 210400, '东洲区');
INSERT INTO `wk_crm_area` VALUES (210402, 210400, '新抚区');
INSERT INTO `wk_crm_area` VALUES (210423, 210400, '清原满族自治县');
INSERT INTO `wk_crm_area` VALUES (711391, 711300, '关西镇');
INSERT INTO `wk_crm_area` VALUES (711392, 711300, '芎林乡');
INSERT INTO `wk_crm_area` VALUES (711393, 711300, '宝山乡');
INSERT INTO `wk_crm_area` VALUES (711394, 711300, '竹东镇');
INSERT INTO `wk_crm_area` VALUES (711390, 711300, '新埔镇');
INSERT INTO `wk_crm_area` VALUES (711388, 711300, '湖口乡');
INSERT INTO `wk_crm_area` VALUES (711399, 711300, '峨眉乡');
INSERT INTO `wk_crm_area` VALUES (711389, 711300, '新丰乡');
INSERT INTO `wk_crm_area` VALUES (711395, 711300, '五峰乡');
INSERT INTO `wk_crm_area` VALUES (711396, 711300, '横山乡');
INSERT INTO `wk_crm_area` VALUES (711397, 711300, '尖石乡');
INSERT INTO `wk_crm_area` VALUES (711387, 711300, '竹北市');
INSERT INTO `wk_crm_area` VALUES (711398, 711300, '北埔乡');
INSERT INTO `wk_crm_area` VALUES (231181, 231100, '北安市');
INSERT INTO `wk_crm_area` VALUES (231182, 231100, '五大连池市');
INSERT INTO `wk_crm_area` VALUES (231121, 231100, '嫩江县');
INSERT INTO `wk_crm_area` VALUES (231123, 231100, '逊克县');
INSERT INTO `wk_crm_area` VALUES (231102, 231100, '爱辉区');
INSERT INTO `wk_crm_area` VALUES (231124, 231100, '孙吴县');
INSERT INTO `wk_crm_area` VALUES (640423, 640400, '隆德县');
INSERT INTO `wk_crm_area` VALUES (640422, 640400, '西吉县');
INSERT INTO `wk_crm_area` VALUES (640425, 640400, '彭阳县');
INSERT INTO `wk_crm_area` VALUES (640402, 640400, '原州区');
INSERT INTO `wk_crm_area` VALUES (640424, 640400, '泾源县');
INSERT INTO `wk_crm_area` VALUES (451030, 451000, '西林县');
INSERT INTO `wk_crm_area` VALUES (451021, 451000, '田阳县');
INSERT INTO `wk_crm_area` VALUES (451031, 451000, '隆林各族自治县');
INSERT INTO `wk_crm_area` VALUES (451081, 451000, '靖西市');
INSERT INTO `wk_crm_area` VALUES (451027, 451000, '凌云县');
INSERT INTO `wk_crm_area` VALUES (451026, 451000, '那坡县');
INSERT INTO `wk_crm_area` VALUES (451029, 451000, '田林县');
INSERT INTO `wk_crm_area` VALUES (451028, 451000, '乐业县');
INSERT INTO `wk_crm_area` VALUES (451023, 451000, '平果县');
INSERT INTO `wk_crm_area` VALUES (451022, 451000, '田东县');
INSERT INTO `wk_crm_area` VALUES (451002, 451000, '右江区');
INSERT INTO `wk_crm_area` VALUES (451024, 451000, '德保县');
INSERT INTO `wk_crm_area` VALUES (350622, 350600, '云霄县');
INSERT INTO `wk_crm_area` VALUES (350623, 350600, '漳浦县');
INSERT INTO `wk_crm_area` VALUES (350602, 350600, '芗城区');
INSERT INTO `wk_crm_area` VALUES (350624, 350600, '诏安县');
INSERT INTO `wk_crm_area` VALUES (350603, 350600, '龙文区');
INSERT INTO `wk_crm_area` VALUES (350625, 350600, '长泰县');
INSERT INTO `wk_crm_area` VALUES (350626, 350600, '东山县');
INSERT INTO `wk_crm_area` VALUES (350627, 350600, '南靖县');
INSERT INTO `wk_crm_area` VALUES (350628, 350600, '平和县');
INSERT INTO `wk_crm_area` VALUES (350629, 350600, '华安县');
INSERT INTO `wk_crm_area` VALUES (350681, 350600, '龙海市');
INSERT INTO `wk_crm_area` VALUES (371422, 371400, '宁津县');
INSERT INTO `wk_crm_area` VALUES (371471, 371400, '德州经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (371482, 371400, '禹城市');
INSERT INTO `wk_crm_area` VALUES (371481, 371400, '乐陵市');
INSERT INTO `wk_crm_area` VALUES (371472, 371400, '德州运河经济开发区');
INSERT INTO `wk_crm_area` VALUES (371428, 371400, '武城县');
INSERT INTO `wk_crm_area` VALUES (371427, 371400, '夏津县');
INSERT INTO `wk_crm_area` VALUES (371402, 371400, '德城区');
INSERT INTO `wk_crm_area` VALUES (371424, 371400, '临邑县');
INSERT INTO `wk_crm_area` VALUES (371423, 371400, '庆云县');
INSERT INTO `wk_crm_area` VALUES (371426, 371400, '平原县');
INSERT INTO `wk_crm_area` VALUES (371403, 371400, '陵城区');
INSERT INTO `wk_crm_area` VALUES (371425, 371400, '齐河县');
INSERT INTO `wk_crm_area` VALUES (430321, 430300, '湘潭县');
INSERT INTO `wk_crm_area` VALUES (430302, 430300, '雨湖区');
INSERT INTO `wk_crm_area` VALUES (430304, 430300, '岳塘区');
INSERT INTO `wk_crm_area` VALUES (430381, 430300, '湘乡市');
INSERT INTO `wk_crm_area` VALUES (430372, 430300, '湘潭昭山示范区');
INSERT INTO `wk_crm_area` VALUES (430371, 430300, '湖南湘潭高新技术产业园区');
INSERT INTO `wk_crm_area` VALUES (430382, 430300, '韶山市');
INSERT INTO `wk_crm_area` VALUES (430373, 430300, '湘潭九华示范区');
INSERT INTO `wk_crm_area` VALUES (650205, 650200, '乌尔禾区');
INSERT INTO `wk_crm_area` VALUES (650204, 650200, '白碱滩区');
INSERT INTO `wk_crm_area` VALUES (650203, 650200, '克拉玛依区');
INSERT INTO `wk_crm_area` VALUES (650202, 650200, '独山子区');
INSERT INTO `wk_crm_area` VALUES (610928, 610900, '旬阳县');
INSERT INTO `wk_crm_area` VALUES (610929, 610900, '白河县');
INSERT INTO `wk_crm_area` VALUES (610926, 610900, '平利县');
INSERT INTO `wk_crm_area` VALUES (610927, 610900, '镇坪县');
INSERT INTO `wk_crm_area` VALUES (610902, 610900, '汉滨区');
INSERT INTO `wk_crm_area` VALUES (610924, 610900, '紫阳县');
INSERT INTO `wk_crm_area` VALUES (610925, 610900, '岚皋县');
INSERT INTO `wk_crm_area` VALUES (610922, 610900, '石泉县');
INSERT INTO `wk_crm_area` VALUES (610923, 610900, '宁陕县');
INSERT INTO `wk_crm_area` VALUES (610921, 610900, '汉阴县');
INSERT INTO `wk_crm_area` VALUES (130771, 130700, '张家口市高新技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (130730, 130700, '怀来县');
INSERT INTO `wk_crm_area` VALUES (130731, 130700, '涿鹿县');
INSERT INTO `wk_crm_area` VALUES (130772, 130700, '张家口市察北管理区');
INSERT INTO `wk_crm_area` VALUES (130773, 130700, '张家口市塞北管理区');
INSERT INTO `wk_crm_area` VALUES (130723, 130700, '康保县');
INSERT INTO `wk_crm_area` VALUES (130702, 130700, '桥东区');
INSERT INTO `wk_crm_area` VALUES (130724, 130700, '沽源县');
INSERT INTO `wk_crm_area` VALUES (130732, 130700, '赤城县');
INSERT INTO `wk_crm_area` VALUES (130722, 130700, '张北县');
INSERT INTO `wk_crm_area` VALUES (130705, 130700, '宣化区');
INSERT INTO `wk_crm_area` VALUES (130727, 130700, '阳原县');
INSERT INTO `wk_crm_area` VALUES (130706, 130700, '下花园区');
INSERT INTO `wk_crm_area` VALUES (130728, 130700, '怀安县');
INSERT INTO `wk_crm_area` VALUES (130703, 130700, '桥西区');
INSERT INTO `wk_crm_area` VALUES (130725, 130700, '尚义县');
INSERT INTO `wk_crm_area` VALUES (130726, 130700, '蔚县');
INSERT INTO `wk_crm_area` VALUES (130709, 130700, '崇礼区');
INSERT INTO `wk_crm_area` VALUES (130708, 130700, '万全区');
INSERT INTO `wk_crm_area` VALUES (220281, 220200, '蛟河市');
INSERT INTO `wk_crm_area` VALUES (220272, 220200, '吉林高新技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (220283, 220200, '舒兰市');
INSERT INTO `wk_crm_area` VALUES (220271, 220200, '吉林经济开发区');
INSERT INTO `wk_crm_area` VALUES (220282, 220200, '桦甸市');
INSERT INTO `wk_crm_area` VALUES (220273, 220200, '吉林中国新加坡食品区');
INSERT INTO `wk_crm_area` VALUES (220284, 220200, '磐石市');
INSERT INTO `wk_crm_area` VALUES (220221, 220200, '永吉县');
INSERT INTO `wk_crm_area` VALUES (220211, 220200, '丰满区');
INSERT INTO `wk_crm_area` VALUES (220203, 220200, '龙潭区');
INSERT INTO `wk_crm_area` VALUES (220202, 220200, '昌邑区');
INSERT INTO `wk_crm_area` VALUES (220204, 220200, '船营区');
INSERT INTO `wk_crm_area` VALUES (440118, 440100, '增城区');
INSERT INTO `wk_crm_area` VALUES (440106, 440100, '天河区');
INSERT INTO `wk_crm_area` VALUES (440117, 440100, '从化区');
INSERT INTO `wk_crm_area` VALUES (440105, 440100, '海珠区');
INSERT INTO `wk_crm_area` VALUES (440104, 440100, '越秀区');
INSERT INTO `wk_crm_area` VALUES (440115, 440100, '南沙区');
INSERT INTO `wk_crm_area` VALUES (440103, 440100, '荔湾区');
INSERT INTO `wk_crm_area` VALUES (440114, 440100, '花都区');
INSERT INTO `wk_crm_area` VALUES (440113, 440100, '番禺区');
INSERT INTO `wk_crm_area` VALUES (440112, 440100, '黄埔区');
INSERT INTO `wk_crm_area` VALUES (440111, 440100, '白云区');
INSERT INTO `wk_crm_area` VALUES (360430, 360400, '彭泽县');
INSERT INTO `wk_crm_area` VALUES (360483, 360400, '庐山市');
INSERT INTO `wk_crm_area` VALUES (360481, 360400, '瑞昌市');
INSERT INTO `wk_crm_area` VALUES (360482, 360400, '共青城市');
INSERT INTO `wk_crm_area` VALUES (360429, 360400, '湖口县');
INSERT INTO `wk_crm_area` VALUES (360428, 360400, '都昌县');
INSERT INTO `wk_crm_area` VALUES (360403, 360400, '浔阳区');
INSERT INTO `wk_crm_area` VALUES (360425, 360400, '永修县');
INSERT INTO `wk_crm_area` VALUES (360404, 360400, '柴桑区');
INSERT INTO `wk_crm_area` VALUES (360426, 360400, '德安县');
INSERT INTO `wk_crm_area` VALUES (360423, 360400, '武宁县');
INSERT INTO `wk_crm_area` VALUES (360402, 360400, '濂溪区');
INSERT INTO `wk_crm_area` VALUES (360424, 360400, '修水县');
INSERT INTO `wk_crm_area` VALUES (230600, 230000, '大庆市');
INSERT INTO `wk_crm_area` VALUES (230500, 230000, '双鸭山市');
INSERT INTO `wk_crm_area` VALUES (230400, 230000, '鹤岗市');
INSERT INTO `wk_crm_area` VALUES (230300, 230000, '鸡西市');
INSERT INTO `wk_crm_area` VALUES (230200, 230000, '齐齐哈尔市');
INSERT INTO `wk_crm_area` VALUES (230100, 230000, '哈尔滨市');
INSERT INTO `wk_crm_area` VALUES (231000, 230000, '牡丹江市');
INSERT INTO `wk_crm_area` VALUES (231100, 230000, '黑河市');
INSERT INTO `wk_crm_area` VALUES (231200, 230000, '绥化市');
INSERT INTO `wk_crm_area` VALUES (230900, 230000, '七台河市');
INSERT INTO `wk_crm_area` VALUES (230800, 230000, '佳木斯市');
INSERT INTO `wk_crm_area` VALUES (232700, 230000, '大兴安岭地区');
INSERT INTO `wk_crm_area` VALUES (230700, 230000, '伊春市');
INSERT INTO `wk_crm_area` VALUES (500107, 500100, '九龙坡区');
INSERT INTO `wk_crm_area` VALUES (500106, 500100, '沙坪坝区');
INSERT INTO `wk_crm_area` VALUES (500105, 500100, '江北区');
INSERT INTO `wk_crm_area` VALUES (500104, 500100, '大渡口区');
INSERT INTO `wk_crm_area` VALUES (500109, 500100, '北碚区');
INSERT INTO `wk_crm_area` VALUES (500108, 500100, '南岸区');
INSERT INTO `wk_crm_area` VALUES (500110, 500100, '綦江区');
INSERT INTO `wk_crm_area` VALUES (500154, 500100, '开州区');
INSERT INTO `wk_crm_area` VALUES (500153, 500100, '荣昌区');
INSERT INTO `wk_crm_area` VALUES (500152, 500100, '潼南区');
INSERT INTO `wk_crm_area` VALUES (500151, 500100, '铜梁区');
INSERT INTO `wk_crm_area` VALUES (500114, 500100, '黔江区');
INSERT INTO `wk_crm_area` VALUES (500113, 500100, '巴南区');
INSERT INTO `wk_crm_area` VALUES (500112, 500100, '渝北区');
INSERT INTO `wk_crm_area` VALUES (500156, 500100, '武隆区');
INSERT INTO `wk_crm_area` VALUES (500111, 500100, '大足区');
INSERT INTO `wk_crm_area` VALUES (500155, 500100, '梁平区');
INSERT INTO `wk_crm_area` VALUES (500118, 500100, '永川区');
INSERT INTO `wk_crm_area` VALUES (500117, 500100, '合川区');
INSERT INTO `wk_crm_area` VALUES (500116, 500100, '江津区');
INSERT INTO `wk_crm_area` VALUES (500115, 500100, '长寿区');
INSERT INTO `wk_crm_area` VALUES (500119, 500100, '南川区');
INSERT INTO `wk_crm_area` VALUES (500120, 500100, '璧山区');
INSERT INTO `wk_crm_area` VALUES (500103, 500100, '渝中区');
INSERT INTO `wk_crm_area` VALUES (500102, 500100, '涪陵区');
INSERT INTO `wk_crm_area` VALUES (500101, 500100, '万州区');
INSERT INTO `wk_crm_area` VALUES (140581, 140500, '高平市');
INSERT INTO `wk_crm_area` VALUES (140521, 140500, '沁水县');
INSERT INTO `wk_crm_area` VALUES (140522, 140500, '阳城县');
INSERT INTO `wk_crm_area` VALUES (140502, 140500, '城区');
INSERT INTO `wk_crm_area` VALUES (140524, 140500, '陵川县');
INSERT INTO `wk_crm_area` VALUES (140525, 140500, '泽州县');
INSERT INTO `wk_crm_area` VALUES (150303, 150300, '海南区');
INSERT INTO `wk_crm_area` VALUES (150304, 150300, '乌达区');
INSERT INTO `wk_crm_area` VALUES (150302, 150300, '海勃湾区');
INSERT INTO `wk_crm_area` VALUES (712590, 712500, '关山镇');
INSERT INTO `wk_crm_area` VALUES (712591, 712500, '海端乡');
INSERT INTO `wk_crm_area` VALUES (712592, 712500, '池上乡');
INSERT INTO `wk_crm_area` VALUES (712593, 712500, '东河乡');
INSERT INTO `wk_crm_area` VALUES (712587, 712500, '延平乡');
INSERT INTO `wk_crm_area` VALUES (712598, 712500, '达仁乡');
INSERT INTO `wk_crm_area` VALUES (712588, 712500, '卑南乡');
INSERT INTO `wk_crm_area` VALUES (712599, 712500, '太麻里乡');
INSERT INTO `wk_crm_area` VALUES (712589, 712500, '鹿野乡');
INSERT INTO `wk_crm_area` VALUES (712594, 712500, '成功镇');
INSERT INTO `wk_crm_area` VALUES (712584, 712500, '台东市');
INSERT INTO `wk_crm_area` VALUES (712595, 712500, '长滨乡');
INSERT INTO `wk_crm_area` VALUES (712585, 712500, '绿岛乡');
INSERT INTO `wk_crm_area` VALUES (712596, 712500, '金峰乡');
INSERT INTO `wk_crm_area` VALUES (712586, 712500, '兰屿乡');
INSERT INTO `wk_crm_area` VALUES (712597, 712500, '大武乡');
INSERT INTO `wk_crm_area` VALUES (620723, 620700, '临泽县');
INSERT INTO `wk_crm_area` VALUES (620702, 620700, '甘州区');
INSERT INTO `wk_crm_area` VALUES (620724, 620700, '高台县');
INSERT INTO `wk_crm_area` VALUES (620725, 620700, '山丹县');
INSERT INTO `wk_crm_area` VALUES (620721, 620700, '肃南裕固族自治县');
INSERT INTO `wk_crm_area` VALUES (620722, 620700, '民乐县');
INSERT INTO `wk_crm_area` VALUES (710207, 710200, '前镇区');
INSERT INTO `wk_crm_area` VALUES (710206, 710200, '旗津区');
INSERT INTO `wk_crm_area` VALUES (710205, 710200, '鼓山区');
INSERT INTO `wk_crm_area` VALUES (710249, 710200, '桥头区');
INSERT INTO `wk_crm_area` VALUES (710204, 710200, '盐埕区');
INSERT INTO `wk_crm_area` VALUES (710248, 710200, '燕巢区');
INSERT INTO `wk_crm_area` VALUES (710209, 710200, '左营区');
INSERT INTO `wk_crm_area` VALUES (710208, 710200, '三民区');
INSERT INTO `wk_crm_area` VALUES (710250, 710200, '梓官区');
INSERT INTO `wk_crm_area` VALUES (710210, 710200, '楠梓区');
INSERT INTO `wk_crm_area` VALUES (710254, 710200, '凤山区');
INSERT INTO `wk_crm_area` VALUES (710253, 710200, '湖内区');
INSERT INTO `wk_crm_area` VALUES (710252, 710200, '永安区');
INSERT INTO `wk_crm_area` VALUES (710251, 710200, '弥陀区');
INSERT INTO `wk_crm_area` VALUES (710258, 710200, '大树区');
INSERT INTO `wk_crm_area` VALUES (710257, 710200, '鸟松区');
INSERT INTO `wk_crm_area` VALUES (710256, 710200, '林园区');
INSERT INTO `wk_crm_area` VALUES (710211, 710200, '小港区');
INSERT INTO `wk_crm_area` VALUES (710255, 710200, '大寮区');
INSERT INTO `wk_crm_area` VALUES (710299, 710200, '其它区');
INSERT INTO `wk_crm_area` VALUES (710259, 710200, '旗山区');
INSERT INTO `wk_crm_area` VALUES (710261, 710200, '六龟区');
INSERT INTO `wk_crm_area` VALUES (710260, 710200, '美浓区');
INSERT INTO `wk_crm_area` VALUES (710243, 710200, '大社区');
INSERT INTO `wk_crm_area` VALUES (710265, 710200, '桃源区');
INSERT INTO `wk_crm_area` VALUES (710242, 710200, '仁武区');
INSERT INTO `wk_crm_area` VALUES (710264, 710200, '甲仙区');
INSERT INTO `wk_crm_area` VALUES (710241, 710200, '苓雅区');
INSERT INTO `wk_crm_area` VALUES (710263, 710200, '杉林区');
INSERT INTO `wk_crm_area` VALUES (710262, 710200, '内门区');
INSERT INTO `wk_crm_area` VALUES (710203, 710200, '芩雅区');
INSERT INTO `wk_crm_area` VALUES (710247, 710200, '田寮区');
INSERT INTO `wk_crm_area` VALUES (710202, 710200, '前金区');
INSERT INTO `wk_crm_area` VALUES (710246, 710200, '阿莲区');
INSERT INTO `wk_crm_area` VALUES (710268, 710200, '茄萣区');
INSERT INTO `wk_crm_area` VALUES (710201, 710200, '新兴区');
INSERT INTO `wk_crm_area` VALUES (710245, 710200, '路竹区');
INSERT INTO `wk_crm_area` VALUES (710267, 710200, '茂林区');
INSERT INTO `wk_crm_area` VALUES (710244, 710200, '冈山区');
INSERT INTO `wk_crm_area` VALUES (710266, 710200, '那玛夏区');
INSERT INTO `wk_crm_area` VALUES (370112, 370100, '历城区');
INSERT INTO `wk_crm_area` VALUES (370102, 370100, '历下区');
INSERT INTO `wk_crm_area` VALUES (370113, 370100, '长清区');
INSERT INTO `wk_crm_area` VALUES (370124, 370100, '平阴县');
INSERT INTO `wk_crm_area` VALUES (370171, 370100, '济南高新技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (370103, 370100, '市中区');
INSERT INTO `wk_crm_area` VALUES (370114, 370100, '章丘区');
INSERT INTO `wk_crm_area` VALUES (370104, 370100, '槐荫区');
INSERT INTO `wk_crm_area` VALUES (370115, 370100, '济阳区');
INSERT INTO `wk_crm_area` VALUES (370126, 370100, '商河县');
INSERT INTO `wk_crm_area` VALUES (370105, 370100, '天桥区');
INSERT INTO `wk_crm_area` VALUES (530111, 530100, '官渡区');
INSERT INTO `wk_crm_area` VALUES (530181, 530100, '安宁市');
INSERT INTO `wk_crm_area` VALUES (530129, 530100, '寻甸回族彝族自治县');
INSERT INTO `wk_crm_area` VALUES (530127, 530100, '嵩明县');
INSERT INTO `wk_crm_area` VALUES (530128, 530100, '禄劝彝族苗族自治县');
INSERT INTO `wk_crm_area` VALUES (530103, 530100, '盘龙区');
INSERT INTO `wk_crm_area` VALUES (530114, 530100, '呈贡区');
INSERT INTO `wk_crm_area` VALUES (530125, 530100, '宜良县');
INSERT INTO `wk_crm_area` VALUES (530115, 530100, '晋宁区');
INSERT INTO `wk_crm_area` VALUES (530126, 530100, '石林彝族自治县');
INSERT INTO `wk_crm_area` VALUES (530112, 530100, '西山区');
INSERT INTO `wk_crm_area` VALUES (530102, 530100, '五华区');
INSERT INTO `wk_crm_area` VALUES (530113, 530100, '东川区');
INSERT INTO `wk_crm_area` VALUES (530124, 530100, '富民县');
INSERT INTO `wk_crm_area` VALUES (441581, 441500, '陆丰市');
INSERT INTO `wk_crm_area` VALUES (441502, 441500, '城区');
INSERT INTO `wk_crm_area` VALUES (441523, 441500, '陆河县');
INSERT INTO `wk_crm_area` VALUES (441521, 441500, '海丰县');
INSERT INTO `wk_crm_area` VALUES (610117, 610100, '高陵区');
INSERT INTO `wk_crm_area` VALUES (610118, 610100, '鄠邑区');
INSERT INTO `wk_crm_area` VALUES (610104, 610100, '莲湖区');
INSERT INTO `wk_crm_area` VALUES (610115, 610100, '临潼区');
INSERT INTO `wk_crm_area` VALUES (610116, 610100, '长安区');
INSERT INTO `wk_crm_area` VALUES (610102, 610100, '新城区');
INSERT INTO `wk_crm_area` VALUES (610113, 610100, '雁塔区');
INSERT INTO `wk_crm_area` VALUES (610124, 610100, '周至县');
INSERT INTO `wk_crm_area` VALUES (610103, 610100, '碑林区');
INSERT INTO `wk_crm_area` VALUES (610114, 610100, '阎良区');
INSERT INTO `wk_crm_area` VALUES (610111, 610100, '灞桥区');
INSERT INTO `wk_crm_area` VALUES (610122, 610100, '蓝田县');
INSERT INTO `wk_crm_area` VALUES (610112, 610100, '未央区');
INSERT INTO `wk_crm_area` VALUES (340802, 340800, '迎江区');
INSERT INTO `wk_crm_area` VALUES (340811, 340800, '宜秀区');
INSERT INTO `wk_crm_area` VALUES (340822, 340800, '怀宁县');
INSERT INTO `wk_crm_area` VALUES (340827, 340800, '望江县');
INSERT INTO `wk_crm_area` VALUES (340828, 340800, '岳西县');
INSERT INTO `wk_crm_area` VALUES (340803, 340800, '大观区');
INSERT INTO `wk_crm_area` VALUES (340825, 340800, '太湖县');
INSERT INTO `wk_crm_area` VALUES (340826, 340800, '宿松县');
INSERT INTO `wk_crm_area` VALUES (340881, 340800, '桐城市');
INSERT INTO `wk_crm_area` VALUES (340871, 340800, '安徽安庆经济开发区');
INSERT INTO `wk_crm_area` VALUES (340882, 340800, '潜山市');
INSERT INTO `wk_crm_area` VALUES (420882, 420800, '京山市');
INSERT INTO `wk_crm_area` VALUES (420822, 420800, '沙洋县');
INSERT INTO `wk_crm_area` VALUES (420804, 420800, '掇刀区');
INSERT INTO `wk_crm_area` VALUES (420802, 420800, '东宝区');
INSERT INTO `wk_crm_area` VALUES (420881, 420800, '钟祥市');
INSERT INTO `wk_crm_area` VALUES (231005, 231000, '西安区');
INSERT INTO `wk_crm_area` VALUES (231004, 231000, '爱民区');
INSERT INTO `wk_crm_area` VALUES (231003, 231000, '阳明区');
INSERT INTO `wk_crm_area` VALUES (231025, 231000, '林口县');
INSERT INTO `wk_crm_area` VALUES (231002, 231000, '东安区');
INSERT INTO `wk_crm_area` VALUES (231086, 231000, '东宁市');
INSERT INTO `wk_crm_area` VALUES (231085, 231000, '穆棱市');
INSERT INTO `wk_crm_area` VALUES (231084, 231000, '宁安市');
INSERT INTO `wk_crm_area` VALUES (231083, 231000, '海林市');
INSERT INTO `wk_crm_area` VALUES (231071, 231000, '牡丹江经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (231081, 231000, '绥芬河市');
INSERT INTO `wk_crm_area` VALUES (210381, 210300, '海城市');
INSERT INTO `wk_crm_area` VALUES (210323, 210300, '岫岩满族自治县');
INSERT INTO `wk_crm_area` VALUES (210311, 210300, '千山区');
INSERT INTO `wk_crm_area` VALUES (210321, 210300, '台安县');
INSERT INTO `wk_crm_area` VALUES (210304, 210300, '立山区');
INSERT INTO `wk_crm_area` VALUES (210303, 210300, '铁西区');
INSERT INTO `wk_crm_area` VALUES (210302, 210300, '铁东区');
INSERT INTO `wk_crm_area` VALUES (510681, 510600, '广汉市');
INSERT INTO `wk_crm_area` VALUES (510683, 510600, '绵竹市');
INSERT INTO `wk_crm_area` VALUES (510682, 510600, '什邡市');
INSERT INTO `wk_crm_area` VALUES (510623, 510600, '中江县');
INSERT INTO `wk_crm_area` VALUES (510603, 510600, '旌阳区');
INSERT INTO `wk_crm_area` VALUES (510604, 510600, '罗江区');
INSERT INTO `wk_crm_area` VALUES (451381, 451300, '合山市');
INSERT INTO `wk_crm_area` VALUES (451302, 451300, '兴宾区');
INSERT INTO `wk_crm_area` VALUES (451324, 451300, '金秀瑶族自治县');
INSERT INTO `wk_crm_area` VALUES (451323, 451300, '武宣县');
INSERT INTO `wk_crm_area` VALUES (451322, 451300, '象州县');
INSERT INTO `wk_crm_area` VALUES (451321, 451300, '忻城县');
INSERT INTO `wk_crm_area` VALUES (621123, 621100, '渭源县');
INSERT INTO `wk_crm_area` VALUES (621122, 621100, '陇西县');
INSERT INTO `wk_crm_area` VALUES (621125, 621100, '漳县');
INSERT INTO `wk_crm_area` VALUES (621102, 621100, '安定区');
INSERT INTO `wk_crm_area` VALUES (621124, 621100, '临洮县');
INSERT INTO `wk_crm_area` VALUES (621121, 621100, '通渭县');
INSERT INTO `wk_crm_area` VALUES (621126, 621100, '岷县');
INSERT INTO `wk_crm_area` VALUES (371321, 371300, '沂南县');
INSERT INTO `wk_crm_area` VALUES (371312, 371300, '河东区');
INSERT INTO `wk_crm_area` VALUES (371323, 371300, '沂水县');
INSERT INTO `wk_crm_area` VALUES (371311, 371300, '罗庄区');
INSERT INTO `wk_crm_area` VALUES (371322, 371300, '郯城县');
INSERT INTO `wk_crm_area` VALUES (371372, 371300, '临沂经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (371371, 371300, '临沂高新技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (371373, 371300, '临沂临港经济开发区');
INSERT INTO `wk_crm_area` VALUES (371329, 371300, '临沭县');
INSERT INTO `wk_crm_area` VALUES (371328, 371300, '蒙阴县');
INSERT INTO `wk_crm_area` VALUES (371325, 371300, '费县');
INSERT INTO `wk_crm_area` VALUES (371302, 371300, '兰山区');
INSERT INTO `wk_crm_area` VALUES (371324, 371300, '兰陵县');
INSERT INTO `wk_crm_area` VALUES (371327, 371300, '莒南县');
INSERT INTO `wk_crm_area` VALUES (371326, 371300, '平邑县');
INSERT INTO `wk_crm_area` VALUES (430611, 430600, '君山区');
INSERT INTO `wk_crm_area` VALUES (430621, 430600, '岳阳县');
INSERT INTO `wk_crm_area` VALUES (430602, 430600, '岳阳楼区');
INSERT INTO `wk_crm_area` VALUES (430624, 430600, '湘阴县');
INSERT INTO `wk_crm_area` VALUES (430623, 430600, '华容县');
INSERT INTO `wk_crm_area` VALUES (430626, 430600, '平江县');
INSERT INTO `wk_crm_area` VALUES (430603, 430600, '云溪区');
INSERT INTO `wk_crm_area` VALUES (430671, 430600, '岳阳市屈原管理区');
INSERT INTO `wk_crm_area` VALUES (430682, 430600, '临湘市');
INSERT INTO `wk_crm_area` VALUES (430681, 430600, '汨罗市');
INSERT INTO `wk_crm_area` VALUES (130273, 130200, '唐山高新技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (130284, 130200, '滦州市');
INSERT INTO `wk_crm_area` VALUES (130274, 130200, '河北唐山海港经济开发区');
INSERT INTO `wk_crm_area` VALUES (130202, 130200, '路南区');
INSERT INTO `wk_crm_area` VALUES (130224, 130200, '滦南县');
INSERT INTO `wk_crm_area` VALUES (130203, 130200, '路北区');
INSERT INTO `wk_crm_area` VALUES (130225, 130200, '乐亭县');
INSERT INTO `wk_crm_area` VALUES (130207, 130200, '丰南区');
INSERT INTO `wk_crm_area` VALUES (130229, 130200, '玉田县');
INSERT INTO `wk_crm_area` VALUES (130204, 130200, '古冶区');
INSERT INTO `wk_crm_area` VALUES (130205, 130200, '开平区');
INSERT INTO `wk_crm_area` VALUES (130227, 130200, '迁西县');
INSERT INTO `wk_crm_area` VALUES (130208, 130200, '丰润区');
INSERT INTO `wk_crm_area` VALUES (130209, 130200, '曹妃甸区');
INSERT INTO `wk_crm_area` VALUES (130271, 130200, '唐山市芦台经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (130272, 130200, '唐山市汉沽管理区');
INSERT INTO `wk_crm_area` VALUES (130283, 130200, '迁安市');
INSERT INTO `wk_crm_area` VALUES (130281, 130200, '遵化市');
INSERT INTO `wk_crm_area` VALUES (220122, 220100, '农安县');
INSERT INTO `wk_crm_area` VALUES (220102, 220100, '南关区');
INSERT INTO `wk_crm_area` VALUES (220113, 220100, '九台区');
INSERT INTO `wk_crm_area` VALUES (220112, 220100, '双阳区');
INSERT INTO `wk_crm_area` VALUES (220104, 220100, '朝阳区');
INSERT INTO `wk_crm_area` VALUES (220103, 220100, '宽城区');
INSERT INTO `wk_crm_area` VALUES (220106, 220100, '绿园区');
INSERT INTO `wk_crm_area` VALUES (220105, 220100, '二道区');
INSERT INTO `wk_crm_area` VALUES (220171, 220100, '长春经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (220182, 220100, '榆树市');
INSERT INTO `wk_crm_area` VALUES (220173, 220100, '长春高新技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (220172, 220100, '长春净月高新技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (220183, 220100, '德惠市');
INSERT INTO `wk_crm_area` VALUES (220174, 220100, '长春汽车经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (222406, 222400, '和龙市');
INSERT INTO `wk_crm_area` VALUES (222401, 222400, '延吉市');
INSERT INTO `wk_crm_area` VALUES (222404, 222400, '珲春市');
INSERT INTO `wk_crm_area` VALUES (222426, 222400, '安图县');
INSERT INTO `wk_crm_area` VALUES (222405, 222400, '龙井市');
INSERT INTO `wk_crm_area` VALUES (222402, 222400, '图们市');
INSERT INTO `wk_crm_area` VALUES (222424, 222400, '汪清县');
INSERT INTO `wk_crm_area` VALUES (222403, 222400, '敦化市');
INSERT INTO `wk_crm_area` VALUES (520322, 520300, '桐梓县');
INSERT INTO `wk_crm_area` VALUES (520323, 520300, '绥阳县');
INSERT INTO `wk_crm_area` VALUES (520302, 520300, '红花岗区');
INSERT INTO `wk_crm_area` VALUES (520324, 520300, '正安县');
INSERT INTO `wk_crm_area` VALUES (520303, 520300, '汇川区');
INSERT INTO `wk_crm_area` VALUES (520325, 520300, '道真仡佬族苗族自治县');
INSERT INTO `wk_crm_area` VALUES (520304, 520300, '播州区');
INSERT INTO `wk_crm_area` VALUES (520326, 520300, '务川仡佬族苗族自治县');
INSERT INTO `wk_crm_area` VALUES (520327, 520300, '凤冈县');
INSERT INTO `wk_crm_area` VALUES (520328, 520300, '湄潭县');
INSERT INTO `wk_crm_area` VALUES (520329, 520300, '余庆县');
INSERT INTO `wk_crm_area` VALUES (520381, 520300, '赤水市');
INSERT INTO `wk_crm_area` VALUES (520382, 520300, '仁怀市');
INSERT INTO `wk_crm_area` VALUES (520330, 520300, '习水县');
INSERT INTO `wk_crm_area` VALUES (522629, 522600, '剑河县');
INSERT INTO `wk_crm_area` VALUES (522632, 522600, '榕江县');
INSERT INTO `wk_crm_area` VALUES (522622, 522600, '黄平县');
INSERT INTO `wk_crm_area` VALUES (522633, 522600, '从江县');
INSERT INTO `wk_crm_area` VALUES (522601, 522600, '凯里市');
INSERT INTO `wk_crm_area` VALUES (522623, 522600, '施秉县');
INSERT INTO `wk_crm_area` VALUES (522634, 522600, '雷山县');
INSERT INTO `wk_crm_area` VALUES (522624, 522600, '三穗县');
INSERT INTO `wk_crm_area` VALUES (522635, 522600, '麻江县');
INSERT INTO `wk_crm_area` VALUES (522625, 522600, '镇远县');
INSERT INTO `wk_crm_area` VALUES (522636, 522600, '丹寨县');
INSERT INTO `wk_crm_area` VALUES (522626, 522600, '岑巩县');
INSERT INTO `wk_crm_area` VALUES (522627, 522600, '天柱县');
INSERT INTO `wk_crm_area` VALUES (522628, 522600, '锦屏县');
INSERT INTO `wk_crm_area` VALUES (522630, 522600, '台江县');
INSERT INTO `wk_crm_area` VALUES (522631, 522600, '黎平县');
INSERT INTO `wk_crm_area` VALUES (440404, 440400, '金湾区');
INSERT INTO `wk_crm_area` VALUES (440403, 440400, '斗门区');
INSERT INTO `wk_crm_area` VALUES (440402, 440400, '香洲区');
INSERT INTO `wk_crm_area` VALUES (350502, 350500, '鲤城区');
INSERT INTO `wk_crm_area` VALUES (350524, 350500, '安溪县');
INSERT INTO `wk_crm_area` VALUES (350503, 350500, '丰泽区');
INSERT INTO `wk_crm_area` VALUES (350525, 350500, '永春县');
INSERT INTO `wk_crm_area` VALUES (350504, 350500, '洛江区');
INSERT INTO `wk_crm_area` VALUES (350526, 350500, '德化县');
INSERT INTO `wk_crm_area` VALUES (350505, 350500, '泉港区');
INSERT INTO `wk_crm_area` VALUES (350527, 350500, '金门县');
INSERT INTO `wk_crm_area` VALUES (350581, 350500, '石狮市');
INSERT INTO `wk_crm_area` VALUES (350582, 350500, '晋江市');
INSERT INTO `wk_crm_area` VALUES (350583, 350500, '南安市');
INSERT INTO `wk_crm_area` VALUES (350521, 350500, '惠安县');
INSERT INTO `wk_crm_area` VALUES (360322, 360300, '上栗县');
INSERT INTO `wk_crm_area` VALUES (360323, 360300, '芦溪县');
INSERT INTO `wk_crm_area` VALUES (360321, 360300, '莲花县');
INSERT INTO `wk_crm_area` VALUES (360302, 360300, '安源区');
INSERT INTO `wk_crm_area` VALUES (360313, 360300, '湘东区');
INSERT INTO `wk_crm_area` VALUES (141100, 140000, '吕梁市');
INSERT INTO `wk_crm_area` VALUES (141000, 140000, '临汾市');
INSERT INTO `wk_crm_area` VALUES (140100, 140000, '太原市');
INSERT INTO `wk_crm_area` VALUES (140200, 140000, '大同市');
INSERT INTO `wk_crm_area` VALUES (140300, 140000, '阳泉市');
INSERT INTO `wk_crm_area` VALUES (140400, 140000, '长治市');
INSERT INTO `wk_crm_area` VALUES (140500, 140000, '晋城市');
INSERT INTO `wk_crm_area` VALUES (140600, 140000, '朔州市');
INSERT INTO `wk_crm_area` VALUES (140700, 140000, '晋中市');
INSERT INTO `wk_crm_area` VALUES (140800, 140000, '运城市');
INSERT INTO `wk_crm_area` VALUES (140900, 140000, '忻州市');
INSERT INTO `wk_crm_area` VALUES (211481, 211400, '兴城市');
INSERT INTO `wk_crm_area` VALUES (211422, 211400, '建昌县');
INSERT INTO `wk_crm_area` VALUES (211421, 211400, '绥中县');
INSERT INTO `wk_crm_area` VALUES (211404, 211400, '南票区');
INSERT INTO `wk_crm_area` VALUES (211403, 211400, '龙港区');
INSERT INTO `wk_crm_area` VALUES (211402, 211400, '连山区');
INSERT INTO `wk_crm_area` VALUES (511827, 511800, '宝兴县');
INSERT INTO `wk_crm_area` VALUES (511822, 511800, '荥经县');
INSERT INTO `wk_crm_area` VALUES (511803, 511800, '名山区');
INSERT INTO `wk_crm_area` VALUES (511825, 511800, '天全县');
INSERT INTO `wk_crm_area` VALUES (511826, 511800, '芦山县');
INSERT INTO `wk_crm_area` VALUES (511823, 511800, '汉源县');
INSERT INTO `wk_crm_area` VALUES (511802, 511800, '雨城区');
INSERT INTO `wk_crm_area` VALUES (511824, 511800, '石棉县');
INSERT INTO `wk_crm_area` VALUES (450206, 450200, '柳江区');
INSERT INTO `wk_crm_area` VALUES (450205, 450200, '柳北区');
INSERT INTO `wk_crm_area` VALUES (450202, 450200, '城中区');
INSERT INTO `wk_crm_area` VALUES (450224, 450200, '融安县');
INSERT INTO `wk_crm_area` VALUES (450223, 450200, '鹿寨县');
INSERT INTO `wk_crm_area` VALUES (450204, 450200, '柳南区');
INSERT INTO `wk_crm_area` VALUES (450226, 450200, '三江侗族自治县');
INSERT INTO `wk_crm_area` VALUES (450203, 450200, '鱼峰区');
INSERT INTO `wk_crm_area` VALUES (450225, 450200, '融水苗族自治县');
INSERT INTO `wk_crm_area` VALUES (450222, 450200, '柳城县');
INSERT INTO `wk_crm_area` VALUES (371200, 370000, '莱芜市');
INSERT INTO `wk_crm_area` VALUES (370100, 370000, '济南市');
INSERT INTO `wk_crm_area` VALUES (371100, 370000, '日照市');
INSERT INTO `wk_crm_area` VALUES (370200, 370000, '青岛市');
INSERT INTO `wk_crm_area` VALUES (371400, 370000, '德州市');
INSERT INTO `wk_crm_area` VALUES (370300, 370000, '淄博市');
INSERT INTO `wk_crm_area` VALUES (371300, 370000, '临沂市');
INSERT INTO `wk_crm_area` VALUES (371000, 370000, '威海市');
INSERT INTO `wk_crm_area` VALUES (370800, 370000, '济宁市');
INSERT INTO `wk_crm_area` VALUES (370900, 370000, '泰安市');
INSERT INTO `wk_crm_area` VALUES (370400, 370000, '枣庄市');
INSERT INTO `wk_crm_area` VALUES (371600, 370000, '滨州市');
INSERT INTO `wk_crm_area` VALUES (370500, 370000, '东营市');
INSERT INTO `wk_crm_area` VALUES (371500, 370000, '聊城市');
INSERT INTO `wk_crm_area` VALUES (370600, 370000, '烟台市');
INSERT INTO `wk_crm_area` VALUES (370700, 370000, '潍坊市');
INSERT INTO `wk_crm_area` VALUES (371700, 370000, '菏泽市');
INSERT INTO `wk_crm_area` VALUES (621200, 620000, '陇南市');
INSERT INTO `wk_crm_area` VALUES (620100, 620000, '兰州市');
INSERT INTO `wk_crm_area` VALUES (621100, 620000, '定西市');
INSERT INTO `wk_crm_area` VALUES (620200, 620000, '嘉峪关市');
INSERT INTO `wk_crm_area` VALUES (620300, 620000, '金昌市');
INSERT INTO `wk_crm_area` VALUES (621000, 620000, '庆阳市');
INSERT INTO `wk_crm_area` VALUES (623000, 620000, '甘南藏族自治州');
INSERT INTO `wk_crm_area` VALUES (620800, 620000, '平凉市');
INSERT INTO `wk_crm_area` VALUES (620900, 620000, '酒泉市');
INSERT INTO `wk_crm_area` VALUES (620400, 620000, '白银市');
INSERT INTO `wk_crm_area` VALUES (620500, 620000, '天水市');
INSERT INTO `wk_crm_area` VALUES (620600, 620000, '武威市');
INSERT INTO `wk_crm_area` VALUES (622900, 620000, '临夏回族自治州');
INSERT INTO `wk_crm_area` VALUES (620700, 620000, '张掖市');
INSERT INTO `wk_crm_area` VALUES (410923, 410900, '南乐县');
INSERT INTO `wk_crm_area` VALUES (410922, 410900, '清丰县');
INSERT INTO `wk_crm_area` VALUES (410972, 410900, '濮阳经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (410971, 410900, '河南濮阳工业园区');
INSERT INTO `wk_crm_area` VALUES (410928, 410900, '濮阳县');
INSERT INTO `wk_crm_area` VALUES (410902, 410900, '华龙区');
INSERT INTO `wk_crm_area` VALUES (410927, 410900, '台前县');
INSERT INTO `wk_crm_area` VALUES (410926, 410900, '范县');
INSERT INTO `wk_crm_area` VALUES (469000, 460000, '省直辖县级行政区划');
INSERT INTO `wk_crm_area` VALUES (460100, 460000, '海口市');
INSERT INTO `wk_crm_area` VALUES (460400, 460000, '儋州市');
INSERT INTO `wk_crm_area` VALUES (460300, 460000, '三沙市');
INSERT INTO `wk_crm_area` VALUES (460200, 460000, '三亚市');
INSERT INTO `wk_crm_area` VALUES (441481, 441400, '兴宁市');
INSERT INTO `wk_crm_area` VALUES (441427, 441400, '蕉岭县');
INSERT INTO `wk_crm_area` VALUES (441426, 441400, '平远县');
INSERT INTO `wk_crm_area` VALUES (441403, 441400, '梅县区');
INSERT INTO `wk_crm_area` VALUES (441402, 441400, '梅江区');
INSERT INTO `wk_crm_area` VALUES (441424, 441400, '五华县');
INSERT INTO `wk_crm_area` VALUES (441423, 441400, '丰顺县');
INSERT INTO `wk_crm_area` VALUES (441422, 441400, '大埔县');
INSERT INTO `wk_crm_area` VALUES (141182, 141100, '汾阳市');
INSERT INTO `wk_crm_area` VALUES (141181, 141100, '孝义市');
INSERT INTO `wk_crm_area` VALUES (141130, 141100, '交口县');
INSERT INTO `wk_crm_area` VALUES (141122, 141100, '交城县');
INSERT INTO `wk_crm_area` VALUES (141121, 141100, '文水县');
INSERT INTO `wk_crm_area` VALUES (141102, 141100, '离石区');
INSERT INTO `wk_crm_area` VALUES (141124, 141100, '临县');
INSERT INTO `wk_crm_area` VALUES (141123, 141100, '兴县');
INSERT INTO `wk_crm_area` VALUES (141126, 141100, '石楼县');
INSERT INTO `wk_crm_area` VALUES (141125, 141100, '柳林县');
INSERT INTO `wk_crm_area` VALUES (141128, 141100, '方山县');
INSERT INTO `wk_crm_area` VALUES (141127, 141100, '岚县');
INSERT INTO `wk_crm_area` VALUES (141129, 141100, '中阳县');
INSERT INTO `wk_crm_area` VALUES (610202, 610200, '王益区');
INSERT INTO `wk_crm_area` VALUES (610222, 610200, '宜君县');
INSERT INTO `wk_crm_area` VALUES (610203, 610200, '印台区');
INSERT INTO `wk_crm_area` VALUES (610204, 610200, '耀州区');
INSERT INTO `wk_crm_area` VALUES (420702, 420700, '梁子湖区');
INSERT INTO `wk_crm_area` VALUES (420704, 420700, '鄂城区');
INSERT INTO `wk_crm_area` VALUES (420703, 420700, '华容区');
INSERT INTO `wk_crm_area` VALUES (510522, 510500, '合江县');
INSERT INTO `wk_crm_area` VALUES (510521, 510500, '泸县');
INSERT INTO `wk_crm_area` VALUES (510502, 510500, '江阳区');
INSERT INTO `wk_crm_area` VALUES (510524, 510500, '叙永县');
INSERT INTO `wk_crm_area` VALUES (510504, 510500, '龙马潭区');
INSERT INTO `wk_crm_area` VALUES (510503, 510500, '纳溪区');
INSERT INTO `wk_crm_area` VALUES (510525, 510500, '古蔺县');
INSERT INTO `wk_crm_area` VALUES (210281, 210200, '瓦房店市');
INSERT INTO `wk_crm_area` VALUES (210283, 210200, '庄河市');
INSERT INTO `wk_crm_area` VALUES (210202, 210200, '中山区');
INSERT INTO `wk_crm_area` VALUES (210213, 210200, '金州区');
INSERT INTO `wk_crm_area` VALUES (210224, 210200, '长海县');
INSERT INTO `wk_crm_area` VALUES (210212, 210200, '旅顺口区');
INSERT INTO `wk_crm_area` VALUES (210211, 210200, '甘井子区');
INSERT INTO `wk_crm_area` VALUES (210204, 210200, '沙河口区');
INSERT INTO `wk_crm_area` VALUES (210203, 210200, '西岗区');
INSERT INTO `wk_crm_area` VALUES (210214, 210200, '普兰店区');
INSERT INTO `wk_crm_area` VALUES (451229, 451200, '大化瑶族自治县');
INSERT INTO `wk_crm_area` VALUES (451228, 451200, '都安瑶族自治县');
INSERT INTO `wk_crm_area` VALUES (451203, 451200, '宜州区');
INSERT INTO `wk_crm_area` VALUES (451225, 451200, '罗城仫佬族自治县');
INSERT INTO `wk_crm_area` VALUES (451202, 451200, '金城江区');
INSERT INTO `wk_crm_area` VALUES (451224, 451200, '东兰县');
INSERT INTO `wk_crm_area` VALUES (451227, 451200, '巴马瑶族自治县');
INSERT INTO `wk_crm_area` VALUES (451226, 451200, '环江毛南族自治县');
INSERT INTO `wk_crm_area` VALUES (451221, 451200, '南丹县');
INSERT INTO `wk_crm_area` VALUES (451223, 451200, '凤山县');
INSERT INTO `wk_crm_area` VALUES (451222, 451200, '天峨县');
INSERT INTO `wk_crm_area` VALUES (371202, 371200, '莱城区');
INSERT INTO `wk_crm_area` VALUES (371203, 371200, '钢城区');
INSERT INTO `wk_crm_area` VALUES (621222, 621200, '文县');
INSERT INTO `wk_crm_area` VALUES (621221, 621200, '成县');
INSERT INTO `wk_crm_area` VALUES (621202, 621200, '武都区');
INSERT INTO `wk_crm_area` VALUES (621224, 621200, '康县');
INSERT INTO `wk_crm_area` VALUES (621223, 621200, '宕昌县');
INSERT INTO `wk_crm_area` VALUES (621226, 621200, '礼县');
INSERT INTO `wk_crm_area` VALUES (621225, 621200, '西和县');
INSERT INTO `wk_crm_area` VALUES (621228, 621200, '两当县');
INSERT INTO `wk_crm_area` VALUES (621227, 621200, '徽县');
INSERT INTO `wk_crm_area` VALUES (130110, 130100, '鹿泉区');
INSERT INTO `wk_crm_area` VALUES (130121, 130100, '井陉县');
INSERT INTO `wk_crm_area` VALUES (130132, 130100, '元氏县');
INSERT INTO `wk_crm_area` VALUES (130111, 130100, '栾城区');
INSERT INTO `wk_crm_area` VALUES (130133, 130100, '赵县');
INSERT INTO `wk_crm_area` VALUES (130130, 130100, '无极县');
INSERT INTO `wk_crm_area` VALUES (130131, 130100, '平山县');
INSERT INTO `wk_crm_area` VALUES (130125, 130100, '行唐县');
INSERT INTO `wk_crm_area` VALUES (130104, 130100, '桥西区');
INSERT INTO `wk_crm_area` VALUES (130126, 130100, '灵寿县');
INSERT INTO `wk_crm_area` VALUES (130123, 130100, '正定县');
INSERT INTO `wk_crm_area` VALUES (130102, 130100, '长安区');
INSERT INTO `wk_crm_area` VALUES (130107, 130100, '井陉矿区');
INSERT INTO `wk_crm_area` VALUES (130129, 130100, '赞皇县');
INSERT INTO `wk_crm_area` VALUES (130108, 130100, '裕华区');
INSERT INTO `wk_crm_area` VALUES (130105, 130100, '新华区');
INSERT INTO `wk_crm_area` VALUES (130127, 130100, '高邑县');
INSERT INTO `wk_crm_area` VALUES (130128, 130100, '深泽县');
INSERT INTO `wk_crm_area` VALUES (130109, 130100, '藁城区');
INSERT INTO `wk_crm_area` VALUES (130172, 130100, '石家庄循环化工园区');
INSERT INTO `wk_crm_area` VALUES (130183, 130100, '晋州市');
INSERT INTO `wk_crm_area` VALUES (130184, 130100, '新乐市');
INSERT INTO `wk_crm_area` VALUES (130181, 130100, '辛集市');
INSERT INTO `wk_crm_area` VALUES (130171, 130100, '石家庄高新技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (430521, 430500, '邵东县');
INSERT INTO `wk_crm_area` VALUES (430523, 430500, '邵阳县');
INSERT INTO `wk_crm_area` VALUES (430511, 430500, '北塔区');
INSERT INTO `wk_crm_area` VALUES (430522, 430500, '新邵县');
INSERT INTO `wk_crm_area` VALUES (430503, 430500, '大祥区');
INSERT INTO `wk_crm_area` VALUES (430525, 430500, '洞口县');
INSERT INTO `wk_crm_area` VALUES (430502, 430500, '双清区');
INSERT INTO `wk_crm_area` VALUES (430524, 430500, '隆回县');
INSERT INTO `wk_crm_area` VALUES (430527, 430500, '绥宁县');
INSERT INTO `wk_crm_area` VALUES (430529, 430500, '城步苗族自治县');
INSERT INTO `wk_crm_area` VALUES (430528, 430500, '新宁县');
INSERT INTO `wk_crm_area` VALUES (430581, 430500, '武冈市');
INSERT INTO `wk_crm_area` VALUES (220800, 220000, '白城市');
INSERT INTO `wk_crm_area` VALUES (220100, 220000, '长春市');
INSERT INTO `wk_crm_area` VALUES (222400, 220000, '延边朝鲜族自治州');
INSERT INTO `wk_crm_area` VALUES (220300, 220000, '四平市');
INSERT INTO `wk_crm_area` VALUES (220200, 220000, '吉林市');
INSERT INTO `wk_crm_area` VALUES (220500, 220000, '通化市');
INSERT INTO `wk_crm_area` VALUES (220400, 220000, '辽源市');
INSERT INTO `wk_crm_area` VALUES (220700, 220000, '松原市');
INSERT INTO `wk_crm_area` VALUES (220600, 220000, '白山市');
INSERT INTO `wk_crm_area` VALUES (520422, 520400, '普定县');
INSERT INTO `wk_crm_area` VALUES (520423, 520400, '镇宁布依族苗族自治县');
INSERT INTO `wk_crm_area` VALUES (520402, 520400, '西秀区');
INSERT INTO `wk_crm_area` VALUES (520424, 520400, '关岭布依族苗族自治县');
INSERT INTO `wk_crm_area` VALUES (520403, 520400, '平坝区');
INSERT INTO `wk_crm_area` VALUES (520425, 520400, '紫云苗族布依族自治县');
INSERT INTO `wk_crm_area` VALUES (522728, 522700, '罗甸县');
INSERT INTO `wk_crm_area` VALUES (522729, 522700, '长顺县');
INSERT INTO `wk_crm_area` VALUES (522730, 522700, '龙里县');
INSERT INTO `wk_crm_area` VALUES (522731, 522700, '惠水县');
INSERT INTO `wk_crm_area` VALUES (522732, 522700, '三都水族自治县');
INSERT INTO `wk_crm_area` VALUES (522722, 522700, '荔波县');
INSERT INTO `wk_crm_area` VALUES (522701, 522700, '都匀市');
INSERT INTO `wk_crm_area` VALUES (522723, 522700, '贵定县');
INSERT INTO `wk_crm_area` VALUES (522702, 522700, '福泉市');
INSERT INTO `wk_crm_area` VALUES (522725, 522700, '瓮安县');
INSERT INTO `wk_crm_area` VALUES (522726, 522700, '独山县');
INSERT INTO `wk_crm_area` VALUES (522727, 522700, '平塘县');
INSERT INTO `wk_crm_area` VALUES (440309, 440300, '龙华区');
INSERT INTO `wk_crm_area` VALUES (440308, 440300, '盐田区');
INSERT INTO `wk_crm_area` VALUES (440307, 440300, '龙岗区');
INSERT INTO `wk_crm_area` VALUES (440306, 440300, '宝安区');
INSERT INTO `wk_crm_area` VALUES (440305, 440300, '南山区');
INSERT INTO `wk_crm_area` VALUES (440304, 440300, '福田区');
INSERT INTO `wk_crm_area` VALUES (440303, 440300, '罗湖区');
INSERT INTO `wk_crm_area` VALUES (440311, 440300, '光明区');
INSERT INTO `wk_crm_area` VALUES (440310, 440300, '坪山区');
INSERT INTO `wk_crm_area` VALUES (350402, 350400, '梅列区');
INSERT INTO `wk_crm_area` VALUES (350424, 350400, '宁化县');
INSERT INTO `wk_crm_area` VALUES (350403, 350400, '三元区');
INSERT INTO `wk_crm_area` VALUES (350425, 350400, '大田县');
INSERT INTO `wk_crm_area` VALUES (350426, 350400, '尤溪县');
INSERT INTO `wk_crm_area` VALUES (350427, 350400, '沙县');
INSERT INTO `wk_crm_area` VALUES (350428, 350400, '将乐县');
INSERT INTO `wk_crm_area` VALUES (350429, 350400, '泰宁县');
INSERT INTO `wk_crm_area` VALUES (350481, 350400, '永安市');
INSERT INTO `wk_crm_area` VALUES (350430, 350400, '建宁县');
INSERT INTO `wk_crm_area` VALUES (350421, 350400, '明溪县');
INSERT INTO `wk_crm_area` VALUES (350423, 350400, '清流县');
INSERT INTO `wk_crm_area` VALUES (360202, 360200, '昌江区');
INSERT INTO `wk_crm_area` VALUES (360222, 360200, '浮梁县');
INSERT INTO `wk_crm_area` VALUES (360281, 360200, '乐平市');
INSERT INTO `wk_crm_area` VALUES (360203, 360200, '珠山区');
INSERT INTO `wk_crm_area` VALUES (532531, 532500, '绿春县');
INSERT INTO `wk_crm_area` VALUES (532530, 532500, '金平苗族瑶族傣族自治县');
INSERT INTO `wk_crm_area` VALUES (532528, 532500, '元阳县');
INSERT INTO `wk_crm_area` VALUES (532527, 532500, '泸西县');
INSERT INTO `wk_crm_area` VALUES (532504, 532500, '弥勒市');
INSERT INTO `wk_crm_area` VALUES (532503, 532500, '蒙自市');
INSERT INTO `wk_crm_area` VALUES (532525, 532500, '石屏县');
INSERT INTO `wk_crm_area` VALUES (532502, 532500, '开远市');
INSERT INTO `wk_crm_area` VALUES (532524, 532500, '建水县');
INSERT INTO `wk_crm_area` VALUES (532501, 532500, '个旧市');
INSERT INTO `wk_crm_area` VALUES (532523, 532500, '屏边苗族自治县');
INSERT INTO `wk_crm_area` VALUES (532532, 532500, '河口瑶族自治县');
INSERT INTO `wk_crm_area` VALUES (532529, 532500, '红河县');
INSERT INTO `wk_crm_area` VALUES (511781, 511700, '万源市');
INSERT INTO `wk_crm_area` VALUES (511771, 511700, '达州经济开发区');
INSERT INTO `wk_crm_area` VALUES (511722, 511700, '宣汉县');
INSERT INTO `wk_crm_area` VALUES (511723, 511700, '开江县');
INSERT INTO `wk_crm_area` VALUES (511702, 511700, '通川区');
INSERT INTO `wk_crm_area` VALUES (511724, 511700, '大竹县');
INSERT INTO `wk_crm_area` VALUES (511703, 511700, '达川区');
INSERT INTO `wk_crm_area` VALUES (511725, 511700, '渠县');
INSERT INTO `wk_crm_area` VALUES (211382, 211300, '凌源市');
INSERT INTO `wk_crm_area` VALUES (211381, 211300, '北票市');
INSERT INTO `wk_crm_area` VALUES (211302, 211300, '双塔区');
INSERT INTO `wk_crm_area` VALUES (211324, 211300, '喀喇沁左翼蒙古族自治县');
INSERT INTO `wk_crm_area` VALUES (211322, 211300, '建平县');
INSERT INTO `wk_crm_area` VALUES (211321, 211300, '朝阳县');
INSERT INTO `wk_crm_area` VALUES (211303, 211300, '龙城区');
INSERT INTO `wk_crm_area` VALUES (540500, 540000, '山南市');
INSERT INTO `wk_crm_area` VALUES (540600, 540000, '那曲市');
INSERT INTO `wk_crm_area` VALUES (540100, 540000, '拉萨市');
INSERT INTO `wk_crm_area` VALUES (540200, 540000, '日喀则市');
INSERT INTO `wk_crm_area` VALUES (540300, 540000, '昌都市');
INSERT INTO `wk_crm_area` VALUES (540400, 540000, '林芝市');
INSERT INTO `wk_crm_area` VALUES (542500, 540000, '阿里地区');
INSERT INTO `wk_crm_area` VALUES (450107, 450100, '西乡塘区');
INSERT INTO `wk_crm_area` VALUES (450109, 450100, '邕宁区');
INSERT INTO `wk_crm_area` VALUES (450108, 450100, '良庆区');
INSERT INTO `wk_crm_area` VALUES (450103, 450100, '青秀区');
INSERT INTO `wk_crm_area` VALUES (450125, 450100, '上林县');
INSERT INTO `wk_crm_area` VALUES (450102, 450100, '兴宁区');
INSERT INTO `wk_crm_area` VALUES (450124, 450100, '马山县');
INSERT INTO `wk_crm_area` VALUES (450105, 450100, '江南区');
INSERT INTO `wk_crm_area` VALUES (450127, 450100, '横县');
INSERT INTO `wk_crm_area` VALUES (450126, 450100, '宾阳县');
INSERT INTO `wk_crm_area` VALUES (450110, 450100, '武鸣区');
INSERT INTO `wk_crm_area` VALUES (450123, 450100, '隆安县');
INSERT INTO `wk_crm_area` VALUES (620121, 620100, '永登县');
INSERT INTO `wk_crm_area` VALUES (620111, 620100, '红古区');
INSERT INTO `wk_crm_area` VALUES (620122, 620100, '皋兰县');
INSERT INTO `wk_crm_area` VALUES (620123, 620100, '榆中县');
INSERT INTO `wk_crm_area` VALUES (620102, 620100, '城关区');
INSERT INTO `wk_crm_area` VALUES (620171, 620100, '兰州新区');
INSERT INTO `wk_crm_area` VALUES (620103, 620100, '七里河区');
INSERT INTO `wk_crm_area` VALUES (620104, 620100, '西固区');
INSERT INTO `wk_crm_area` VALUES (620105, 620100, '安宁区');
INSERT INTO `wk_crm_area` VALUES (460108, 460100, '美兰区');
INSERT INTO `wk_crm_area` VALUES (460107, 460100, '琼山区');
INSERT INTO `wk_crm_area` VALUES (460106, 460100, '龙华区');
INSERT INTO `wk_crm_area` VALUES (460105, 460100, '秀英区');
INSERT INTO `wk_crm_area` VALUES (441781, 441700, '阳春市');
INSERT INTO `wk_crm_area` VALUES (441704, 441700, '阳东区');
INSERT INTO `wk_crm_area` VALUES (441702, 441700, '江城区');
INSERT INTO `wk_crm_area` VALUES (441721, 441700, '阳西县');
INSERT INTO `wk_crm_area` VALUES (310100, 310000, '上海城区');
INSERT INTO `wk_crm_area` VALUES (610322, 610300, '凤翔县');
INSERT INTO `wk_crm_area` VALUES (610323, 610300, '岐山县');
INSERT INTO `wk_crm_area` VALUES (610331, 610300, '太白县');
INSERT INTO `wk_crm_area` VALUES (610330, 610300, '凤县');
INSERT INTO `wk_crm_area` VALUES (610328, 610300, '千阳县');
INSERT INTO `wk_crm_area` VALUES (610329, 610300, '麟游县');
INSERT INTO `wk_crm_area` VALUES (610304, 610300, '陈仓区');
INSERT INTO `wk_crm_area` VALUES (610326, 610300, '眉县');
INSERT INTO `wk_crm_area` VALUES (610327, 610300, '陇县');
INSERT INTO `wk_crm_area` VALUES (610302, 610300, '渭滨区');
INSERT INTO `wk_crm_area` VALUES (610324, 610300, '扶风县');
INSERT INTO `wk_crm_area` VALUES (610303, 610300, '金台区');
INSERT INTO `wk_crm_area` VALUES (510421, 510400, '米易县');
INSERT INTO `wk_crm_area` VALUES (510411, 510400, '仁和区');
INSERT INTO `wk_crm_area` VALUES (510422, 510400, '盐边县');
INSERT INTO `wk_crm_area` VALUES (510403, 510400, '西区');
INSERT INTO `wk_crm_area` VALUES (510402, 510400, '东区');
INSERT INTO `wk_crm_area` VALUES (520121, 520100, '开阳县');
INSERT INTO `wk_crm_area` VALUES (520103, 520100, '云岩区');
INSERT INTO `wk_crm_area` VALUES (520122, 520100, '息烽县');
INSERT INTO `wk_crm_area` VALUES (520102, 520100, '南明区');
INSERT INTO `wk_crm_area` VALUES (520123, 520100, '修文县');
INSERT INTO `wk_crm_area` VALUES (520112, 520100, '乌当区');
INSERT INTO `wk_crm_area` VALUES (520113, 520100, '白云区');
INSERT INTO `wk_crm_area` VALUES (520111, 520100, '花溪区');
INSERT INTO `wk_crm_area` VALUES (520115, 520100, '观山湖区');
INSERT INTO `wk_crm_area` VALUES (520181, 520100, '清镇市');
INSERT INTO `wk_crm_area` VALUES (210181, 210100, '新民市');
INSERT INTO `wk_crm_area` VALUES (210103, 210100, '沈河区');
INSERT INTO `wk_crm_area` VALUES (210114, 210100, '于洪区');
INSERT INTO `wk_crm_area` VALUES (210102, 210100, '和平区');
INSERT INTO `wk_crm_area` VALUES (210113, 210100, '沈北新区');
INSERT INTO `wk_crm_area` VALUES (210124, 210100, '法库县');
INSERT INTO `wk_crm_area` VALUES (210112, 210100, '浑南区');
INSERT INTO `wk_crm_area` VALUES (210123, 210100, '康平县');
INSERT INTO `wk_crm_area` VALUES (210111, 210100, '苏家屯区');
INSERT INTO `wk_crm_area` VALUES (210106, 210100, '铁西区');
INSERT INTO `wk_crm_area` VALUES (210105, 210100, '皇姑区');
INSERT INTO `wk_crm_area` VALUES (210104, 210100, '大东区');
INSERT INTO `wk_crm_area` VALUES (210115, 210100, '辽中区');
INSERT INTO `wk_crm_area` VALUES (371122, 371100, '莒县');
INSERT INTO `wk_crm_area` VALUES (371103, 371100, '岚山区');
INSERT INTO `wk_crm_area` VALUES (371102, 371100, '东港区');
INSERT INTO `wk_crm_area` VALUES (371121, 371100, '五莲县');
INSERT INTO `wk_crm_area` VALUES (371171, 371100, '日照经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (130473, 130400, '邯郸冀南新区');
INSERT INTO `wk_crm_area` VALUES (130430, 130400, '邱县');
INSERT INTO `wk_crm_area` VALUES (130471, 130400, '邯郸经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (130433, 130400, '馆陶县');
INSERT INTO `wk_crm_area` VALUES (130423, 130400, '临漳县');
INSERT INTO `wk_crm_area` VALUES (130434, 130400, '魏县');
INSERT INTO `wk_crm_area` VALUES (130431, 130400, '鸡泽县');
INSERT INTO `wk_crm_area` VALUES (130432, 130400, '广平县');
INSERT INTO `wk_crm_area` VALUES (130404, 130400, '复兴区');
INSERT INTO `wk_crm_area` VALUES (130426, 130400, '涉县');
INSERT INTO `wk_crm_area` VALUES (130427, 130400, '磁县');
INSERT INTO `wk_crm_area` VALUES (130402, 130400, '邯山区');
INSERT INTO `wk_crm_area` VALUES (130424, 130400, '成安县');
INSERT INTO `wk_crm_area` VALUES (130435, 130400, '曲周县');
INSERT INTO `wk_crm_area` VALUES (130403, 130400, '丛台区');
INSERT INTO `wk_crm_area` VALUES (130425, 130400, '大名县');
INSERT INTO `wk_crm_area` VALUES (130408, 130400, '永年区');
INSERT INTO `wk_crm_area` VALUES (130406, 130400, '峰峰矿区');
INSERT INTO `wk_crm_area` VALUES (130407, 130400, '肥乡区');
INSERT INTO `wk_crm_area` VALUES (130481, 130400, '武安市');
INSERT INTO `wk_crm_area` VALUES (211100, 210000, '盘锦市');
INSERT INTO `wk_crm_area` VALUES (211000, 210000, '辽阳市');
INSERT INTO `wk_crm_area` VALUES (210900, 210000, '阜新市');
INSERT INTO `wk_crm_area` VALUES (210400, 210000, '抚顺市');
INSERT INTO `wk_crm_area` VALUES (210300, 210000, '鞍山市');
INSERT INTO `wk_crm_area` VALUES (211400, 210000, '葫芦岛市');
INSERT INTO `wk_crm_area` VALUES (210200, 210000, '大连市');
INSERT INTO `wk_crm_area` VALUES (211300, 210000, '朝阳市');
INSERT INTO `wk_crm_area` VALUES (210100, 210000, '沈阳市');
INSERT INTO `wk_crm_area` VALUES (211200, 210000, '铁岭市');
INSERT INTO `wk_crm_area` VALUES (210800, 210000, '营口市');
INSERT INTO `wk_crm_area` VALUES (210700, 210000, '锦州市');
INSERT INTO `wk_crm_area` VALUES (210600, 210000, '丹东市');
INSERT INTO `wk_crm_area` VALUES (210500, 210000, '本溪市');
INSERT INTO `wk_crm_area` VALUES (430811, 430800, '武陵源区');
INSERT INTO `wk_crm_area` VALUES (430822, 430800, '桑植县');
INSERT INTO `wk_crm_area` VALUES (430821, 430800, '慈利县');
INSERT INTO `wk_crm_area` VALUES (430802, 430800, '永定区');
INSERT INTO `wk_crm_area` VALUES (440608, 440600, '高明区');
INSERT INTO `wk_crm_area` VALUES (440607, 440600, '三水区');
INSERT INTO `wk_crm_area` VALUES (440606, 440600, '顺德区');
INSERT INTO `wk_crm_area` VALUES (440605, 440600, '南海区');
INSERT INTO `wk_crm_area` VALUES (440604, 440600, '禅城区');
INSERT INTO `wk_crm_area` VALUES (533325, 533300, '兰坪白族普米族自治县');
INSERT INTO `wk_crm_area` VALUES (533324, 533300, '贡山独龙族怒族自治县');
INSERT INTO `wk_crm_area` VALUES (533301, 533300, '泸水市');
INSERT INTO `wk_crm_area` VALUES (533323, 533300, '福贡县');
INSERT INTO `wk_crm_area` VALUES (350303, 350300, '涵江区');
INSERT INTO `wk_crm_area` VALUES (350304, 350300, '荔城区');
INSERT INTO `wk_crm_area` VALUES (350305, 350300, '秀屿区');
INSERT INTO `wk_crm_area` VALUES (350322, 350300, '仙游县');
INSERT INTO `wk_crm_area` VALUES (350302, 350300, '城厢区');
INSERT INTO `wk_crm_area` VALUES (360102, 360100, '东湖区');
INSERT INTO `wk_crm_area` VALUES (360124, 360100, '进贤县');
INSERT INTO `wk_crm_area` VALUES (360103, 360100, '西湖区');
INSERT INTO `wk_crm_area` VALUES (360111, 360100, '青山湖区');
INSERT INTO `wk_crm_area` VALUES (360112, 360100, '新建区');
INSERT INTO `wk_crm_area` VALUES (360123, 360100, '安义县');
INSERT INTO `wk_crm_area` VALUES (360121, 360100, '南昌县');
INSERT INTO `wk_crm_area` VALUES (360104, 360100, '青云谱区');
INSERT INTO `wk_crm_area` VALUES (360105, 360100, '湾里区');
INSERT INTO `wk_crm_area` VALUES (140221, 140200, '阳高县');
INSERT INTO `wk_crm_area` VALUES (140222, 140200, '天镇县');
INSERT INTO `wk_crm_area` VALUES (140212, 140200, '新荣区');
INSERT INTO `wk_crm_area` VALUES (140223, 140200, '广灵县');
INSERT INTO `wk_crm_area` VALUES (140213, 140200, '平城区');
INSERT INTO `wk_crm_area` VALUES (140224, 140200, '灵丘县');
INSERT INTO `wk_crm_area` VALUES (140214, 140200, '云冈区');
INSERT INTO `wk_crm_area` VALUES (140225, 140200, '浑源县');
INSERT INTO `wk_crm_area` VALUES (140215, 140200, '云州区');
INSERT INTO `wk_crm_area` VALUES (140226, 140200, '左云县');
INSERT INTO `wk_crm_area` VALUES (140271, 140200, '山西大同经济开发区');
INSERT INTO `wk_crm_area` VALUES (450481, 450400, '岑溪市');
INSERT INTO `wk_crm_area` VALUES (450403, 450400, '万秀区');
INSERT INTO `wk_crm_area` VALUES (450406, 450400, '龙圩区');
INSERT INTO `wk_crm_area` VALUES (450405, 450400, '长洲区');
INSERT INTO `wk_crm_area` VALUES (450422, 450400, '藤县');
INSERT INTO `wk_crm_area` VALUES (450421, 450400, '苍梧县');
INSERT INTO `wk_crm_area` VALUES (450423, 450400, '蒙山县');
INSERT INTO `wk_crm_area` VALUES (150800, 150000, '巴彦淖尔市');
INSERT INTO `wk_crm_area` VALUES (150900, 150000, '乌兰察布市');
INSERT INTO `wk_crm_area` VALUES (150600, 150000, '鄂尔多斯市');
INSERT INTO `wk_crm_area` VALUES (152900, 150000, '阿拉善盟');
INSERT INTO `wk_crm_area` VALUES (150700, 150000, '呼伦贝尔市');
INSERT INTO `wk_crm_area` VALUES (150400, 150000, '赤峰市');
INSERT INTO `wk_crm_area` VALUES (150500, 150000, '通辽市');
INSERT INTO `wk_crm_area` VALUES (150200, 150000, '包头市');
INSERT INTO `wk_crm_area` VALUES (152500, 150000, '锡林郭勒盟');
INSERT INTO `wk_crm_area` VALUES (150300, 150000, '乌海市');
INSERT INTO `wk_crm_area` VALUES (150100, 150000, '呼和浩特市');
INSERT INTO `wk_crm_area` VALUES (152200, 150000, '兴安盟');
INSERT INTO `wk_crm_area` VALUES (211282, 211200, '开原市');
INSERT INTO `wk_crm_area` VALUES (211281, 211200, '调兵山市');
INSERT INTO `wk_crm_area` VALUES (211221, 211200, '铁岭县');
INSERT INTO `wk_crm_area` VALUES (211202, 211200, '银州区');
INSERT INTO `wk_crm_area` VALUES (211224, 211200, '昌图县');
INSERT INTO `wk_crm_area` VALUES (211223, 211200, '西丰县');
INSERT INTO `wk_crm_area` VALUES (211204, 211200, '清河区');
INSERT INTO `wk_crm_area` VALUES (511681, 511600, '华蓥市');
INSERT INTO `wk_crm_area` VALUES (511623, 511600, '邻水县');
INSERT INTO `wk_crm_area` VALUES (511602, 511600, '广安区');
INSERT INTO `wk_crm_area` VALUES (511621, 511600, '岳池县');
INSERT INTO `wk_crm_area` VALUES (511622, 511600, '武胜县');
INSERT INTO `wk_crm_area` VALUES (511603, 511600, '前锋区');
INSERT INTO `wk_crm_area` VALUES (62201403, 620200, '长城区');
INSERT INTO `wk_crm_area` VALUES (62201102, 620200, '文殊镇');
INSERT INTO `wk_crm_area` VALUES (62201401, 620200, '雄关区');
INSERT INTO `wk_crm_area` VALUES (62201402, 620200, '镜铁区');
INSERT INTO `wk_crm_area` VALUES (62201100, 620200, '新城镇');
INSERT INTO `wk_crm_area` VALUES (62201101, 620200, '峪泉镇');
INSERT INTO `wk_crm_area` VALUES (211122, 211100, '盘山县');
INSERT INTO `wk_crm_area` VALUES (211104, 211100, '大洼区');
INSERT INTO `wk_crm_area` VALUES (211103, 211100, '兴隆台区');
INSERT INTO `wk_crm_area` VALUES (211102, 211100, '双台子区');
INSERT INTO `wk_crm_area` VALUES (511421, 511400, '仁寿县');
INSERT INTO `wk_crm_area` VALUES (511403, 511400, '彭山区');
INSERT INTO `wk_crm_area` VALUES (511425, 511400, '青神县');
INSERT INTO `wk_crm_area` VALUES (511423, 511400, '洪雅县');
INSERT INTO `wk_crm_area` VALUES (511402, 511400, '东坡区');
INSERT INTO `wk_crm_area` VALUES (511424, 511400, '丹棱县');
INSERT INTO `wk_crm_area` VALUES (532300, 530000, '楚雄彝族自治州');
INSERT INTO `wk_crm_area` VALUES (533400, 530000, '迪庆藏族自治州');
INSERT INTO `wk_crm_area` VALUES (530100, 530000, '昆明市');
INSERT INTO `wk_crm_area` VALUES (533300, 530000, '怒江傈僳族自治州');
INSERT INTO `wk_crm_area` VALUES (533100, 530000, '德宏傣族景颇族自治州');
INSERT INTO `wk_crm_area` VALUES (530800, 530000, '普洱市');
INSERT INTO `wk_crm_area` VALUES (530900, 530000, '临沧市');
INSERT INTO `wk_crm_area` VALUES (530600, 530000, '昭通市');
INSERT INTO `wk_crm_area` VALUES (532900, 530000, '大理白族自治州');
INSERT INTO `wk_crm_area` VALUES (530700, 530000, '丽江市');
INSERT INTO `wk_crm_area` VALUES (532800, 530000, '西双版纳傣族自治州');
INSERT INTO `wk_crm_area` VALUES (530400, 530000, '玉溪市');
INSERT INTO `wk_crm_area` VALUES (530500, 530000, '保山市');
INSERT INTO `wk_crm_area` VALUES (532600, 530000, '文山壮族苗族自治州');
INSERT INTO `wk_crm_area` VALUES (532500, 530000, '红河哈尼族彝族自治州');
INSERT INTO `wk_crm_area` VALUES (530300, 530000, '曲靖市');
INSERT INTO `wk_crm_area` VALUES (532329, 532300, '武定县');
INSERT INTO `wk_crm_area` VALUES (532328, 532300, '元谋县');
INSERT INTO `wk_crm_area` VALUES (532327, 532300, '永仁县');
INSERT INTO `wk_crm_area` VALUES (532326, 532300, '大姚县');
INSERT INTO `wk_crm_area` VALUES (532325, 532300, '姚安县');
INSERT INTO `wk_crm_area` VALUES (532324, 532300, '南华县');
INSERT INTO `wk_crm_area` VALUES (532301, 532300, '楚雄市');
INSERT INTO `wk_crm_area` VALUES (532323, 532300, '牟定县');
INSERT INTO `wk_crm_area` VALUES (532322, 532300, '双柏县');
INSERT INTO `wk_crm_area` VALUES (532331, 532300, '禄丰县');
INSERT INTO `wk_crm_area` VALUES (441625, 441600, '东源县');
INSERT INTO `wk_crm_area` VALUES (441602, 441600, '源城区');
INSERT INTO `wk_crm_area` VALUES (441624, 441600, '和平县');
INSERT INTO `wk_crm_area` VALUES (441623, 441600, '连平县');
INSERT INTO `wk_crm_area` VALUES (441622, 441600, '龙川县');
INSERT INTO `wk_crm_area` VALUES (441621, 441600, '紫金县');
INSERT INTO `wk_crm_area` VALUES (310151, 310100, '崇明区');
INSERT INTO `wk_crm_area` VALUES (310120, 310100, '奉贤区');
INSERT INTO `wk_crm_area` VALUES (310110, 310100, '杨浦区');
INSERT INTO `wk_crm_area` VALUES (310101, 310100, '黄浦区');
INSERT INTO `wk_crm_area` VALUES (310112, 310100, '闵行区');
INSERT INTO `wk_crm_area` VALUES (310113, 310100, '宝山区');
INSERT INTO `wk_crm_area` VALUES (310114, 310100, '嘉定区');
INSERT INTO `wk_crm_area` VALUES (310104, 310100, '徐汇区');
INSERT INTO `wk_crm_area` VALUES (310115, 310100, '浦东新区');
INSERT INTO `wk_crm_area` VALUES (310105, 310100, '长宁区');
INSERT INTO `wk_crm_area` VALUES (310116, 310100, '金山区');
INSERT INTO `wk_crm_area` VALUES (310106, 310100, '静安区');
INSERT INTO `wk_crm_area` VALUES (310117, 310100, '松江区');
INSERT INTO `wk_crm_area` VALUES (310107, 310100, '普陀区');
INSERT INTO `wk_crm_area` VALUES (310118, 310100, '青浦区');
INSERT INTO `wk_crm_area` VALUES (310109, 310100, '虹口区');
INSERT INTO `wk_crm_area` VALUES (460205, 460200, '崖州区');
INSERT INTO `wk_crm_area` VALUES (460204, 460200, '天涯区');
INSERT INTO `wk_crm_area` VALUES (460203, 460200, '吉阳区');
INSERT INTO `wk_crm_area` VALUES (460202, 460200, '海棠区');
INSERT INTO `wk_crm_area` VALUES (610429, 610400, '旬邑县');
INSERT INTO `wk_crm_area` VALUES (610428, 610400, '长武县');
INSERT INTO `wk_crm_area` VALUES (610403, 610400, '杨陵区');
INSERT INTO `wk_crm_area` VALUES (610425, 610400, '礼泉县');
INSERT INTO `wk_crm_area` VALUES (610404, 610400, '渭城区');
INSERT INTO `wk_crm_area` VALUES (610426, 610400, '永寿县');
INSERT INTO `wk_crm_area` VALUES (610423, 610400, '泾阳县');
INSERT INTO `wk_crm_area` VALUES (610402, 610400, '秦都区');
INSERT INTO `wk_crm_area` VALUES (610424, 610400, '乾县');
INSERT INTO `wk_crm_area` VALUES (610422, 610400, '三原县');
INSERT INTO `wk_crm_area` VALUES (610430, 610400, '淳化县');
INSERT INTO `wk_crm_area` VALUES (610431, 610400, '武功县');
INSERT INTO `wk_crm_area` VALUES (610481, 610400, '兴平市');
INSERT INTO `wk_crm_area` VALUES (610482, 610400, '彬州市');
INSERT INTO `wk_crm_area` VALUES (420984, 420900, '汉川市');
INSERT INTO `wk_crm_area` VALUES (420982, 420900, '安陆市');
INSERT INTO `wk_crm_area` VALUES (420981, 420900, '应城市');
INSERT INTO `wk_crm_area` VALUES (420922, 420900, '大悟县');
INSERT INTO `wk_crm_area` VALUES (420921, 420900, '孝昌县');
INSERT INTO `wk_crm_area` VALUES (420902, 420900, '孝南区');
INSERT INTO `wk_crm_area` VALUES (420923, 420900, '云梦县');
INSERT INTO `wk_crm_area` VALUES (520281, 520200, '盘州市');
INSERT INTO `wk_crm_area` VALUES (520221, 520200, '水城县');
INSERT INTO `wk_crm_area` VALUES (520201, 520200, '钟山区');
INSERT INTO `wk_crm_area` VALUES (520203, 520200, '六枝特区');
INSERT INTO `wk_crm_area` VALUES (451481, 451400, '凭祥市');
INSERT INTO `wk_crm_area` VALUES (451423, 451400, '龙州县');
INSERT INTO `wk_crm_area` VALUES (451422, 451400, '宁明县');
INSERT INTO `wk_crm_area` VALUES (451425, 451400, '天等县');
INSERT INTO `wk_crm_area` VALUES (451402, 451400, '江州区');
INSERT INTO `wk_crm_area` VALUES (451424, 451400, '大新县');
INSERT INTO `wk_crm_area` VALUES (451421, 451400, '扶绥县');
INSERT INTO `wk_crm_area` VALUES (321012, 321000, '江都区');
INSERT INTO `wk_crm_area` VALUES (321023, 321000, '宝应县');
INSERT INTO `wk_crm_area` VALUES (321084, 321000, '高邮市');
INSERT INTO `wk_crm_area` VALUES (321071, 321000, '扬州经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (321081, 321000, '仪征市');
INSERT INTO `wk_crm_area` VALUES (321002, 321000, '广陵区');
INSERT INTO `wk_crm_area` VALUES (321003, 321000, '邗江区');
INSERT INTO `wk_crm_area` VALUES (371002, 371000, '环翠区');
INSERT INTO `wk_crm_area` VALUES (371003, 371000, '文登区');
INSERT INTO `wk_crm_area` VALUES (371071, 371000, '威海火炬高技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (371082, 371000, '荣成市');
INSERT INTO `wk_crm_area` VALUES (371073, 371000, '威海临港经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (371072, 371000, '威海经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (371083, 371000, '乳山市');
INSERT INTO `wk_crm_area` VALUES (510311, 510300, '沿滩区');
INSERT INTO `wk_crm_area` VALUES (510322, 510300, '富顺县');
INSERT INTO `wk_crm_area` VALUES (510321, 510300, '荣县');
INSERT INTO `wk_crm_area` VALUES (510302, 510300, '自流井区');
INSERT INTO `wk_crm_area` VALUES (510304, 510300, '大安区');
INSERT INTO `wk_crm_area` VALUES (510303, 510300, '贡井区');
INSERT INTO `wk_crm_area` VALUES (130372, 130300, '北戴河新区');
INSERT INTO `wk_crm_area` VALUES (130302, 130300, '海港区');
INSERT INTO `wk_crm_area` VALUES (130324, 130300, '卢龙县');
INSERT INTO `wk_crm_area` VALUES (130321, 130300, '青龙满族自治县');
INSERT INTO `wk_crm_area` VALUES (130322, 130300, '昌黎县');
INSERT INTO `wk_crm_area` VALUES (130306, 130300, '抚宁区');
INSERT INTO `wk_crm_area` VALUES (130303, 130300, '山海关区');
INSERT INTO `wk_crm_area` VALUES (130304, 130300, '北戴河区');
INSERT INTO `wk_crm_area` VALUES (130371, 130300, '秦皇岛市经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (430771, 430700, '常德市西洞庭管理区');
INSERT INTO `wk_crm_area` VALUES (430721, 430700, '安乡县');
INSERT INTO `wk_crm_area` VALUES (430723, 430700, '澧县');
INSERT INTO `wk_crm_area` VALUES (430722, 430700, '汉寿县');
INSERT INTO `wk_crm_area` VALUES (430703, 430700, '鼎城区');
INSERT INTO `wk_crm_area` VALUES (430725, 430700, '桃源县');
INSERT INTO `wk_crm_area` VALUES (430702, 430700, '武陵区');
INSERT INTO `wk_crm_area` VALUES (430724, 430700, '临澧县');
INSERT INTO `wk_crm_area` VALUES (430726, 430700, '石门县');
INSERT INTO `wk_crm_area` VALUES (430781, 430700, '津市市');
INSERT INTO `wk_crm_area` VALUES (533401, 533400, '香格里拉市');
INSERT INTO `wk_crm_area` VALUES (533423, 533400, '维西傈僳族自治县');
INSERT INTO `wk_crm_area` VALUES (533422, 533400, '德钦县');
INSERT INTO `wk_crm_area` VALUES (440507, 440500, '龙湖区');
INSERT INTO `wk_crm_area` VALUES (440515, 440500, '澄海区');
INSERT INTO `wk_crm_area` VALUES (440514, 440500, '潮南区');
INSERT INTO `wk_crm_area` VALUES (440513, 440500, '潮阳区');
INSERT INTO `wk_crm_area` VALUES (440512, 440500, '濠江区');
INSERT INTO `wk_crm_area` VALUES (440523, 440500, '南澳县');
INSERT INTO `wk_crm_area` VALUES (440511, 440500, '金平区');
INSERT INTO `wk_crm_area` VALUES (350205, 350200, '海沧区');
INSERT INTO `wk_crm_area` VALUES (350206, 350200, '湖里区');
INSERT INTO `wk_crm_area` VALUES (350211, 350200, '集美区');
INSERT INTO `wk_crm_area` VALUES (350212, 350200, '同安区');
INSERT INTO `wk_crm_area` VALUES (350213, 350200, '翔安区');
INSERT INTO `wk_crm_area` VALUES (350203, 350200, '思明区');
INSERT INTO `wk_crm_area` VALUES (610582, 610500, '华阴市');
INSERT INTO `wk_crm_area` VALUES (610581, 610500, '韩城市');
INSERT INTO `wk_crm_area` VALUES (610528, 610500, '富平县');
INSERT INTO `wk_crm_area` VALUES (610526, 610500, '蒲城县');
INSERT INTO `wk_crm_area` VALUES (610527, 610500, '白水县');
INSERT INTO `wk_crm_area` VALUES (610502, 610500, '临渭区');
INSERT INTO `wk_crm_area` VALUES (610524, 610500, '合阳县');
INSERT INTO `wk_crm_area` VALUES (610503, 610500, '华州区');
INSERT INTO `wk_crm_area` VALUES (610525, 610500, '澄城县');
INSERT INTO `wk_crm_area` VALUES (610522, 610500, '潼关县');
INSERT INTO `wk_crm_area` VALUES (610523, 610500, '大荔县');
INSERT INTO `wk_crm_area` VALUES (652924, 652900, '沙雅县');
INSERT INTO `wk_crm_area` VALUES (652925, 652900, '新和县');
INSERT INTO `wk_crm_area` VALUES (652926, 652900, '拜城县');
INSERT INTO `wk_crm_area` VALUES (652927, 652900, '乌什县');
INSERT INTO `wk_crm_area` VALUES (652928, 652900, '阿瓦提县');
INSERT INTO `wk_crm_area` VALUES (652929, 652900, '柯坪县');
INSERT INTO `wk_crm_area` VALUES (652922, 652900, '温宿县');
INSERT INTO `wk_crm_area` VALUES (652901, 652900, '阿克苏市');
INSERT INTO `wk_crm_area` VALUES (652923, 652900, '库车县');
INSERT INTO `wk_crm_area` VALUES (140110, 140100, '晋源区');
INSERT INTO `wk_crm_area` VALUES (140121, 140100, '清徐县');
INSERT INTO `wk_crm_area` VALUES (140122, 140100, '阳曲县');
INSERT INTO `wk_crm_area` VALUES (140123, 140100, '娄烦县');
INSERT INTO `wk_crm_area` VALUES (140105, 140100, '小店区');
INSERT INTO `wk_crm_area` VALUES (140106, 140100, '迎泽区');
INSERT INTO `wk_crm_area` VALUES (140107, 140100, '杏花岭区');
INSERT INTO `wk_crm_area` VALUES (140108, 140100, '尖草坪区');
INSERT INTO `wk_crm_area` VALUES (140109, 140100, '万柏林区');
INSERT INTO `wk_crm_area` VALUES (140181, 140100, '古交市');
INSERT INTO `wk_crm_area` VALUES (140171, 140100, '山西转型综合改革示范区');
INSERT INTO `wk_crm_area` VALUES (360300, 360000, '萍乡市');
INSERT INTO `wk_crm_area` VALUES (360400, 360000, '九江市');
INSERT INTO `wk_crm_area` VALUES (360100, 360000, '南昌市');
INSERT INTO `wk_crm_area` VALUES (360200, 360000, '景德镇市');
INSERT INTO `wk_crm_area` VALUES (361100, 360000, '上饶市');
INSERT INTO `wk_crm_area` VALUES (361000, 360000, '抚州市');
INSERT INTO `wk_crm_area` VALUES (360900, 360000, '宜春市');
INSERT INTO `wk_crm_area` VALUES (360700, 360000, '赣州市');
INSERT INTO `wk_crm_area` VALUES (360800, 360000, '吉安市');
INSERT INTO `wk_crm_area` VALUES (360500, 360000, '新余市');
INSERT INTO `wk_crm_area` VALUES (360600, 360000, '鹰潭市');
INSERT INTO `wk_crm_area` VALUES (450381, 450300, '荔浦市');
INSERT INTO `wk_crm_area` VALUES (450305, 450300, '七星区');
INSERT INTO `wk_crm_area` VALUES (450327, 450300, '灌阳县');
INSERT INTO `wk_crm_area` VALUES (450304, 450300, '象山区');
INSERT INTO `wk_crm_area` VALUES (450326, 450300, '永福县');
INSERT INTO `wk_crm_area` VALUES (450329, 450300, '资源县');
INSERT INTO `wk_crm_area` VALUES (450328, 450300, '龙胜各族自治县');
INSERT INTO `wk_crm_area` VALUES (450312, 450300, '临桂区');
INSERT INTO `wk_crm_area` VALUES (450323, 450300, '灵川县');
INSERT INTO `wk_crm_area` VALUES (450311, 450300, '雁山区');
INSERT INTO `wk_crm_area` VALUES (450303, 450300, '叠彩区');
INSERT INTO `wk_crm_area` VALUES (450325, 450300, '兴安县');
INSERT INTO `wk_crm_area` VALUES (450302, 450300, '秀峰区');
INSERT INTO `wk_crm_area` VALUES (450324, 450300, '全州县');
INSERT INTO `wk_crm_area` VALUES (450330, 450300, '平乐县');
INSERT INTO `wk_crm_area` VALUES (450321, 450300, '阳朔县');
INSERT INTO `wk_crm_area` VALUES (450332, 450300, '恭城瑶族自治县');
INSERT INTO `wk_crm_area` VALUES (511502, 511500, '翠屏区');
INSERT INTO `wk_crm_area` VALUES (511524, 511500, '长宁县');
INSERT INTO `wk_crm_area` VALUES (511503, 511500, '南溪区');
INSERT INTO `wk_crm_area` VALUES (511525, 511500, '高县');
INSERT INTO `wk_crm_area` VALUES (511523, 511500, '江安县');
INSERT INTO `wk_crm_area` VALUES (511528, 511500, '兴文县');
INSERT INTO `wk_crm_area` VALUES (511529, 511500, '屏山县');
INSERT INTO `wk_crm_area` VALUES (511504, 511500, '叙州区');
INSERT INTO `wk_crm_area` VALUES (511526, 511500, '珙县');
INSERT INTO `wk_crm_area` VALUES (511527, 511500, '筠连县');
INSERT INTO `wk_crm_area` VALUES (152202, 152200, '阿尔山市');
INSERT INTO `wk_crm_area` VALUES (152224, 152200, '突泉县');
INSERT INTO `wk_crm_area` VALUES (152201, 152200, '乌兰浩特市');
INSERT INTO `wk_crm_area` VALUES (152223, 152200, '扎赉特旗');
INSERT INTO `wk_crm_area` VALUES (152222, 152200, '科尔沁右翼中旗');
INSERT INTO `wk_crm_area` VALUES (152221, 152200, '科尔沁右翼前旗');
INSERT INTO `wk_crm_area` VALUES (712183, 712100, '土库镇');
INSERT INTO `wk_crm_area` VALUES (712194, 712100, '二仑乡');
INSERT INTO `wk_crm_area` VALUES (712184, 712100, '褒忠乡');
INSERT INTO `wk_crm_area` VALUES (712195, 712100, '北港镇');
INSERT INTO `wk_crm_area` VALUES (712185, 712100, '东势乡');
INSERT INTO `wk_crm_area` VALUES (712196, 712100, '水林乡');
INSERT INTO `wk_crm_area` VALUES (712186, 712100, '台西乡');
INSERT INTO `wk_crm_area` VALUES (712197, 712100, '口湖乡');
INSERT INTO `wk_crm_area` VALUES (712190, 712100, '林内乡');
INSERT INTO `wk_crm_area` VALUES (712180, 712100, '斗南镇');
INSERT INTO `wk_crm_area` VALUES (712191, 712100, '古坑乡');
INSERT INTO `wk_crm_area` VALUES (712181, 712100, '大埤乡');
INSERT INTO `wk_crm_area` VALUES (712192, 712100, '莿桐乡');
INSERT INTO `wk_crm_area` VALUES (712182, 712100, '虎尾镇');
INSERT INTO `wk_crm_area` VALUES (712193, 712100, '西螺镇');
INSERT INTO `wk_crm_area` VALUES (712187, 712100, '仑背乡');
INSERT INTO `wk_crm_area` VALUES (712198, 712100, '四湖乡');
INSERT INTO `wk_crm_area` VALUES (712188, 712100, '麦寮乡');
INSERT INTO `wk_crm_area` VALUES (712199, 712100, '元长乡');
INSERT INTO `wk_crm_area` VALUES (712189, 712100, '斗六市');
INSERT INTO `wk_crm_area` VALUES (710999, 710900, '其它区');
INSERT INTO `wk_crm_area` VALUES (710902, 710900, '西区');
INSERT INTO `wk_crm_area` VALUES (710901, 710900, '东区');
INSERT INTO `wk_crm_area` VALUES (321000, 320000, '扬州市');
INSERT INTO `wk_crm_area` VALUES (321100, 320000, '镇江市');
INSERT INTO `wk_crm_area` VALUES (320900, 320000, '盐城市');
INSERT INTO `wk_crm_area` VALUES (320700, 320000, '连云港市');
INSERT INTO `wk_crm_area` VALUES (320800, 320000, '淮安市');
INSERT INTO `wk_crm_area` VALUES (320500, 320000, '苏州市');
INSERT INTO `wk_crm_area` VALUES (320600, 320000, '南通市');
INSERT INTO `wk_crm_area` VALUES (320300, 320000, '徐州市');
INSERT INTO `wk_crm_area` VALUES (320400, 320000, '常州市');
INSERT INTO `wk_crm_area` VALUES (320100, 320000, '南京市');
INSERT INTO `wk_crm_area` VALUES (321200, 320000, '泰州市');
INSERT INTO `wk_crm_area` VALUES (320200, 320000, '无锡市');
INSERT INTO `wk_crm_area` VALUES (321300, 320000, '宿迁市');
INSERT INTO `wk_crm_area` VALUES (131002, 131000, '安次区');
INSERT INTO `wk_crm_area` VALUES (131024, 131000, '香河县');
INSERT INTO `wk_crm_area` VALUES (131023, 131000, '永清县');
INSERT INTO `wk_crm_area` VALUES (131022, 131000, '固安县');
INSERT INTO `wk_crm_area` VALUES (131028, 131000, '大厂回族自治县');
INSERT INTO `wk_crm_area` VALUES (131026, 131000, '文安县');
INSERT INTO `wk_crm_area` VALUES (131003, 131000, '广阳区');
INSERT INTO `wk_crm_area` VALUES (131025, 131000, '大城县');
INSERT INTO `wk_crm_area` VALUES (131071, 131000, '廊坊经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (131082, 131000, '三河市');
INSERT INTO `wk_crm_area` VALUES (131081, 131000, '霸州市');
INSERT INTO `wk_crm_area` VALUES (211011, 211000, '太子河区');
INSERT INTO `wk_crm_area` VALUES (211021, 211000, '辽阳县');
INSERT INTO `wk_crm_area` VALUES (211005, 211000, '弓长岭区');
INSERT INTO `wk_crm_area` VALUES (211004, 211000, '宏伟区');
INSERT INTO `wk_crm_area` VALUES (211003, 211000, '文圣区');
INSERT INTO `wk_crm_area` VALUES (211002, 211000, '白塔区');
INSERT INTO `wk_crm_area` VALUES (211081, 211000, '灯塔市');
INSERT INTO `wk_crm_area` VALUES (540302, 540300, '卡若区');
INSERT INTO `wk_crm_area` VALUES (540324, 540300, '丁青县');
INSERT INTO `wk_crm_area` VALUES (540325, 540300, '察雅县');
INSERT INTO `wk_crm_area` VALUES (540326, 540300, '八宿县');
INSERT INTO `wk_crm_area` VALUES (540327, 540300, '左贡县');
INSERT INTO `wk_crm_area` VALUES (540321, 540300, '江达县');
INSERT INTO `wk_crm_area` VALUES (540322, 540300, '贡觉县');
INSERT INTO `wk_crm_area` VALUES (540323, 540300, '类乌齐县');
INSERT INTO `wk_crm_area` VALUES (540330, 540300, '边坝县');
INSERT INTO `wk_crm_area` VALUES (540328, 540300, '芒康县');
INSERT INTO `wk_crm_area` VALUES (540329, 540300, '洛隆县');
INSERT INTO `wk_crm_area` VALUES (44900119, 441900, '长安镇');
INSERT INTO `wk_crm_area` VALUES (44900121, 441900, '虎门镇');
INSERT INTO `wk_crm_area` VALUES (44900122, 441900, '厚街镇');
INSERT INTO `wk_crm_area` VALUES (44900101, 441900, '石碣镇');
INSERT INTO `wk_crm_area` VALUES (44900123, 441900, '沙田镇');
INSERT INTO `wk_crm_area` VALUES (44900003, 441900, '东城街道');
INSERT INTO `wk_crm_area` VALUES (44900102, 441900, '石龙镇');
INSERT INTO `wk_crm_area` VALUES (44900124, 441900, '道滘镇');
INSERT INTO `wk_crm_area` VALUES (44900004, 441900, '南城街道');
INSERT INTO `wk_crm_area` VALUES (44900103, 441900, '茶山镇');
INSERT INTO `wk_crm_area` VALUES (44900125, 441900, '洪梅镇');
INSERT INTO `wk_crm_area` VALUES (44900005, 441900, '万江街道');
INSERT INTO `wk_crm_area` VALUES (44900104, 441900, '石排镇');
INSERT INTO `wk_crm_area` VALUES (44900126, 441900, '麻涌镇');
INSERT INTO `wk_crm_area` VALUES (44900401, 441900, '松山湖管委会');
INSERT INTO `wk_crm_area` VALUES (44900006, 441900, '莞城街道');
INSERT INTO `wk_crm_area` VALUES (44900105, 441900, '企石镇');
INSERT INTO `wk_crm_area` VALUES (44900127, 441900, '望牛墩镇');
INSERT INTO `wk_crm_area` VALUES (44900402, 441900, '东莞港');
INSERT INTO `wk_crm_area` VALUES (44900106, 441900, '横沥镇');
INSERT INTO `wk_crm_area` VALUES (44900128, 441900, '中堂镇');
INSERT INTO `wk_crm_area` VALUES (44900403, 441900, '东莞生态园');
INSERT INTO `wk_crm_area` VALUES (44900107, 441900, '桥头镇');
INSERT INTO `wk_crm_area` VALUES (44900129, 441900, '高埗镇');
INSERT INTO `wk_crm_area` VALUES (44900108, 441900, '谢岗镇');
INSERT INTO `wk_crm_area` VALUES (44900109, 441900, '东坑镇');
INSERT INTO `wk_crm_area` VALUES (44900110, 441900, '常平镇');
INSERT INTO `wk_crm_area` VALUES (44900111, 441900, '寮步镇');
INSERT INTO `wk_crm_area` VALUES (44900112, 441900, '樟木头镇');
INSERT INTO `wk_crm_area` VALUES (44900113, 441900, '大朗镇');
INSERT INTO `wk_crm_area` VALUES (44900114, 441900, '黄江镇');
INSERT INTO `wk_crm_area` VALUES (44900115, 441900, '清溪镇');
INSERT INTO `wk_crm_area` VALUES (44900116, 441900, '塘厦镇');
INSERT INTO `wk_crm_area` VALUES (44900117, 441900, '凤岗镇');
INSERT INTO `wk_crm_area` VALUES (44900118, 441900, '大岭山镇');
INSERT INTO `wk_crm_area` VALUES (460323, 460300, '中沙群岛的岛礁及其海域');
INSERT INTO `wk_crm_area` VALUES (460322, 460300, '南沙群岛');
INSERT INTO `wk_crm_area` VALUES (460321, 460300, '西沙群岛');
INSERT INTO `wk_crm_area` VALUES (361126, 361100, '弋阳县');
INSERT INTO `wk_crm_area` VALUES (361103, 361100, '广丰区');
INSERT INTO `wk_crm_area` VALUES (361125, 361100, '横峰县');
INSERT INTO `wk_crm_area` VALUES (361102, 361100, '信州区');
INSERT INTO `wk_crm_area` VALUES (361124, 361100, '铅山县');
INSERT INTO `wk_crm_area` VALUES (361123, 361100, '玉山县');
INSERT INTO `wk_crm_area` VALUES (361121, 361100, '上饶县');
INSERT INTO `wk_crm_area` VALUES (361130, 361100, '婺源县');
INSERT INTO `wk_crm_area` VALUES (361181, 361100, '德兴市');
INSERT INTO `wk_crm_area` VALUES (361129, 361100, '万年县');
INSERT INTO `wk_crm_area` VALUES (361128, 361100, '鄱阳县');
INSERT INTO `wk_crm_area` VALUES (361127, 361100, '余干县');
INSERT INTO `wk_crm_area` VALUES (654221, 654200, '额敏县');
INSERT INTO `wk_crm_area` VALUES (654201, 654200, '塔城市');
INSERT INTO `wk_crm_area` VALUES (654223, 654200, '沙湾县');
INSERT INTO `wk_crm_area` VALUES (654202, 654200, '乌苏市');
INSERT INTO `wk_crm_area` VALUES (654224, 654200, '托里县');
INSERT INTO `wk_crm_area` VALUES (654225, 654200, '裕民县');
INSERT INTO `wk_crm_area` VALUES (654226, 654200, '和布克赛尔蒙古自治县');
INSERT INTO `wk_crm_area` VALUES (120102, 120100, '河东区');
INSERT INTO `wk_crm_area` VALUES (120113, 120100, '北辰区');
INSERT INTO `wk_crm_area` VALUES (120103, 120100, '河西区');
INSERT INTO `wk_crm_area` VALUES (120114, 120100, '武清区');
INSERT INTO `wk_crm_area` VALUES (120104, 120100, '南开区');
INSERT INTO `wk_crm_area` VALUES (120115, 120100, '宝坻区');
INSERT INTO `wk_crm_area` VALUES (120105, 120100, '河北区');
INSERT INTO `wk_crm_area` VALUES (120116, 120100, '滨海新区');
INSERT INTO `wk_crm_area` VALUES (120110, 120100, '东丽区');
INSERT INTO `wk_crm_area` VALUES (120111, 120100, '西青区');
INSERT INTO `wk_crm_area` VALUES (120101, 120100, '和平区');
INSERT INTO `wk_crm_area` VALUES (120112, 120100, '津南区');
INSERT INTO `wk_crm_area` VALUES (120106, 120100, '红桥区');
INSERT INTO `wk_crm_area` VALUES (120117, 120100, '宁河区');
INSERT INTO `wk_crm_area` VALUES (120118, 120100, '静海区');
INSERT INTO `wk_crm_area` VALUES (120119, 120100, '蓟州区');
INSERT INTO `wk_crm_area` VALUES (469007, 469000, '东方市');
INSERT INTO `wk_crm_area` VALUES (469029, 469000, '保亭黎族苗族自治县');
INSERT INTO `wk_crm_area` VALUES (469006, 469000, '万宁市');
INSERT INTO `wk_crm_area` VALUES (469028, 469000, '陵水黎族自治县');
INSERT INTO `wk_crm_area` VALUES (469005, 469000, '文昌市');
INSERT INTO `wk_crm_area` VALUES (469027, 469000, '乐东黎族自治县');
INSERT INTO `wk_crm_area` VALUES (469026, 469000, '昌江黎族自治县');
INSERT INTO `wk_crm_area` VALUES (469025, 469000, '白沙黎族自治县');
INSERT INTO `wk_crm_area` VALUES (469002, 469000, '琼海市');
INSERT INTO `wk_crm_area` VALUES (469024, 469000, '临高县');
INSERT INTO `wk_crm_area` VALUES (469001, 469000, '五指山市');
INSERT INTO `wk_crm_area` VALUES (469023, 469000, '澄迈县');
INSERT INTO `wk_crm_area` VALUES (469022, 469000, '屯昌县');
INSERT INTO `wk_crm_area` VALUES (469021, 469000, '定安县');
INSERT INTO `wk_crm_area` VALUES (469030, 469000, '琼中黎族苗族自治县');
INSERT INTO `wk_crm_area` VALUES (321271, 321200, '泰州医药高新技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (321282, 321200, '靖江市');
INSERT INTO `wk_crm_area` VALUES (321283, 321200, '泰兴市');
INSERT INTO `wk_crm_area` VALUES (321281, 321200, '兴化市');
INSERT INTO `wk_crm_area` VALUES (321204, 321200, '姜堰区');
INSERT INTO `wk_crm_area` VALUES (321202, 321200, '海陵区');
INSERT INTO `wk_crm_area` VALUES (321203, 321200, '高港区');
INSERT INTO `wk_crm_area` VALUES (321111, 321100, '润州区');
INSERT INTO `wk_crm_area` VALUES (321183, 321100, '句容市');
INSERT INTO `wk_crm_area` VALUES (321181, 321100, '丹阳市');
INSERT INTO `wk_crm_area` VALUES (321171, 321100, '镇江新区');
INSERT INTO `wk_crm_area` VALUES (321182, 321100, '扬中市');
INSERT INTO `wk_crm_area` VALUES (321112, 321100, '丹徒区');
INSERT INTO `wk_crm_area` VALUES (321102, 321100, '京口区');
INSERT INTO `wk_crm_area` VALUES (440804, 440800, '坡头区');
INSERT INTO `wk_crm_area` VALUES (440803, 440800, '霞山区');
INSERT INTO `wk_crm_area` VALUES (440825, 440800, '徐闻县');
INSERT INTO `wk_crm_area` VALUES (440802, 440800, '赤坎区');
INSERT INTO `wk_crm_area` VALUES (440823, 440800, '遂溪县');
INSERT INTO `wk_crm_area` VALUES (440811, 440800, '麻章区');
INSERT INTO `wk_crm_area` VALUES (440883, 440800, '吴川市');
INSERT INTO `wk_crm_area` VALUES (440882, 440800, '雷州市');
INSERT INTO `wk_crm_area` VALUES (440881, 440800, '廉江市');
INSERT INTO `wk_crm_area` VALUES (623027, 623000, '夏河县');
INSERT INTO `wk_crm_area` VALUES (623025, 623000, '玛曲县');
INSERT INTO `wk_crm_area` VALUES (623026, 623000, '碌曲县');
INSERT INTO `wk_crm_area` VALUES (623001, 623000, '合作市');
INSERT INTO `wk_crm_area` VALUES (623023, 623000, '舟曲县');
INSERT INTO `wk_crm_area` VALUES (623024, 623000, '迭部县');
INSERT INTO `wk_crm_area` VALUES (623021, 623000, '临潭县');
INSERT INTO `wk_crm_area` VALUES (623022, 623000, '卓尼县');
INSERT INTO `wk_crm_area` VALUES (350105, 350100, '马尾区');
INSERT INTO `wk_crm_area` VALUES (350128, 350100, '平潭县');
INSERT INTO `wk_crm_area` VALUES (350121, 350100, '闽侯县');
INSERT INTO `wk_crm_area` VALUES (350111, 350100, '晋安区');
INSERT INTO `wk_crm_area` VALUES (350122, 350100, '连江县');
INSERT INTO `wk_crm_area` VALUES (350112, 350100, '长乐区');
INSERT INTO `wk_crm_area` VALUES (350123, 350100, '罗源县');
INSERT INTO `wk_crm_area` VALUES (350102, 350100, '鼓楼区');
INSERT INTO `wk_crm_area` VALUES (350124, 350100, '闽清县');
INSERT INTO `wk_crm_area` VALUES (350103, 350100, '台江区');
INSERT INTO `wk_crm_area` VALUES (350125, 350100, '永泰县');
INSERT INTO `wk_crm_area` VALUES (350104, 350100, '仓山区');
INSERT INTO `wk_crm_area` VALUES (350181, 350100, '福清市');
INSERT INTO `wk_crm_area` VALUES (532801, 532800, '景洪市');
INSERT INTO `wk_crm_area` VALUES (532823, 532800, '勐腊县');
INSERT INTO `wk_crm_area` VALUES (532822, 532800, '勐海县');
INSERT INTO `wk_crm_area` VALUES (530523, 530500, '龙陵县');
INSERT INTO `wk_crm_area` VALUES (530502, 530500, '隆阳区');
INSERT INTO `wk_crm_area` VALUES (530524, 530500, '昌宁县');
INSERT INTO `wk_crm_area` VALUES (530521, 530500, '施甸县');
INSERT INTO `wk_crm_area` VALUES (530581, 530500, '腾冲市');
INSERT INTO `wk_crm_area` VALUES (632323, 632300, '泽库县');
INSERT INTO `wk_crm_area` VALUES (632322, 632300, '尖扎县');
INSERT INTO `wk_crm_area` VALUES (632321, 632300, '同仁县');
INSERT INTO `wk_crm_area` VALUES (632324, 632300, '河南蒙古族自治县');
INSERT INTO `wk_crm_area` VALUES (653130, 653100, '巴楚县');
INSERT INTO `wk_crm_area` VALUES (653131, 653100, '塔什库尔干塔吉克自治县');
INSERT INTO `wk_crm_area` VALUES (653121, 653100, '疏附县');
INSERT INTO `wk_crm_area` VALUES (653122, 653100, '疏勒县');
INSERT INTO `wk_crm_area` VALUES (653101, 653100, '喀什市');
INSERT INTO `wk_crm_area` VALUES (653123, 653100, '英吉沙县');
INSERT INTO `wk_crm_area` VALUES (653124, 653100, '泽普县');
INSERT INTO `wk_crm_area` VALUES (653125, 653100, '莎车县');
INSERT INTO `wk_crm_area` VALUES (653126, 653100, '叶城县');
INSERT INTO `wk_crm_area` VALUES (653127, 653100, '麦盖提县');
INSERT INTO `wk_crm_area` VALUES (653128, 653100, '岳普湖县');
INSERT INTO `wk_crm_area` VALUES (653129, 653100, '伽师县');
INSERT INTO `wk_crm_area` VALUES (630100, 630000, '西宁市');
INSERT INTO `wk_crm_area` VALUES (630200, 630000, '海东市');
INSERT INTO `wk_crm_area` VALUES (632300, 630000, '黄南藏族自治州');
INSERT INTO `wk_crm_area` VALUES (632200, 630000, '海北藏族自治州');
INSERT INTO `wk_crm_area` VALUES (632800, 630000, '海西蒙古族藏族自治州');
INSERT INTO `wk_crm_area` VALUES (632700, 630000, '玉树藏族自治州');
INSERT INTO `wk_crm_area` VALUES (632600, 630000, '果洛藏族自治州');
INSERT INTO `wk_crm_area` VALUES (632500, 630000, '海南藏族自治州');
INSERT INTO `wk_crm_area` VALUES (450602, 450600, '港口区');
INSERT INTO `wk_crm_area` VALUES (450603, 450600, '防城区');
INSERT INTO `wk_crm_area` VALUES (450621, 450600, '上思县');
INSERT INTO `wk_crm_area` VALUES (450681, 450600, '东兴市');
INSERT INTO `wk_crm_area` VALUES (710801, 710800, '东区');
INSERT INTO `wk_crm_area` VALUES (710899, 710800, '其它区');
INSERT INTO `wk_crm_area` VALUES (710803, 710800, '香山区');
INSERT INTO `wk_crm_area` VALUES (710802, 710800, '北区');
INSERT INTO `wk_crm_area` VALUES (540423, 540400, '墨脱县');
INSERT INTO `wk_crm_area` VALUES (540402, 540400, '巴宜区');
INSERT INTO `wk_crm_area` VALUES (540424, 540400, '波密县');
INSERT INTO `wk_crm_area` VALUES (540425, 540400, '察隅县');
INSERT INTO `wk_crm_area` VALUES (540426, 540400, '朗县');
INSERT INTO `wk_crm_area` VALUES (540421, 540400, '工布江达县');
INSERT INTO `wk_crm_area` VALUES (540422, 540400, '米林县');
INSERT INTO `wk_crm_area` VALUES (441826, 441800, '连南瑶族自治县');
INSERT INTO `wk_crm_area` VALUES (441803, 441800, '清新区');
INSERT INTO `wk_crm_area` VALUES (441825, 441800, '连山壮族瑶族自治县');
INSERT INTO `wk_crm_area` VALUES (441802, 441800, '清城区');
INSERT INTO `wk_crm_area` VALUES (441823, 441800, '阳山县');
INSERT INTO `wk_crm_area` VALUES (441821, 441800, '佛冈县');
INSERT INTO `wk_crm_area` VALUES (441882, 441800, '连州市');
INSERT INTO `wk_crm_area` VALUES (441881, 441800, '英德市');
INSERT INTO `wk_crm_area` VALUES (46400105, 460400, '兰洋镇');
INSERT INTO `wk_crm_area` VALUES (46400116, 460400, '新州镇');
INSERT INTO `wk_crm_area` VALUES (46400106, 460400, '光村镇');
INSERT INTO `wk_crm_area` VALUES (46400107, 460400, '木棠镇');
INSERT INTO `wk_crm_area` VALUES (46400108, 460400, '海头镇');
INSERT INTO `wk_crm_area` VALUES (46400101, 460400, '和庆镇');
INSERT INTO `wk_crm_area` VALUES (46400112, 460400, '白马井镇');
INSERT INTO `wk_crm_area` VALUES (46400102, 460400, '南丰镇');
INSERT INTO `wk_crm_area` VALUES (46400113, 460400, '中和镇');
INSERT INTO `wk_crm_area` VALUES (46400103, 460400, '大成镇');
INSERT INTO `wk_crm_area` VALUES (46400114, 460400, '排浦镇');
INSERT INTO `wk_crm_area` VALUES (46400499, 460400, '洋浦经济开发区');
INSERT INTO `wk_crm_area` VALUES (46400104, 460400, '雅星镇');
INSERT INTO `wk_crm_area` VALUES (46400115, 460400, '东成镇');
INSERT INTO `wk_crm_area` VALUES (46400500, 460400, '华南热作学院');
INSERT INTO `wk_crm_area` VALUES (46400109, 460400, '峨蔓镇');
INSERT INTO `wk_crm_area` VALUES (46400100, 460400, '那大镇');
INSERT INTO `wk_crm_area` VALUES (46400111, 460400, '王五镇');
INSERT INTO `wk_crm_area` VALUES (331022, 331000, '三门县');
INSERT INTO `wk_crm_area` VALUES (331081, 331000, '温岭市');
INSERT INTO `wk_crm_area` VALUES (331082, 331000, '临海市');
INSERT INTO `wk_crm_area` VALUES (331083, 331000, '玉环市');
INSERT INTO `wk_crm_area` VALUES (331023, 331000, '天台县');
INSERT INTO `wk_crm_area` VALUES (331002, 331000, '椒江区');
INSERT INTO `wk_crm_area` VALUES (331024, 331000, '仙居县');
INSERT INTO `wk_crm_area` VALUES (331003, 331000, '黄岩区');
INSERT INTO `wk_crm_area` VALUES (331004, 331000, '路桥区');
INSERT INTO `wk_crm_area` VALUES (361027, 361000, '金溪县');
INSERT INTO `wk_crm_area` VALUES (361026, 361000, '宜黄县');
INSERT INTO `wk_crm_area` VALUES (361003, 361000, '东乡区');
INSERT INTO `wk_crm_area` VALUES (361025, 361000, '乐安县');
INSERT INTO `wk_crm_area` VALUES (361002, 361000, '临川区');
INSERT INTO `wk_crm_area` VALUES (361024, 361000, '崇仁县');
INSERT INTO `wk_crm_area` VALUES (361023, 361000, '南丰县');
INSERT INTO `wk_crm_area` VALUES (361022, 361000, '黎川县');
INSERT INTO `wk_crm_area` VALUES (361021, 361000, '南城县');
INSERT INTO `wk_crm_area` VALUES (361030, 361000, '广昌县');
INSERT INTO `wk_crm_area` VALUES (361028, 361000, '资溪县');
INSERT INTO `wk_crm_area` VALUES (321371, 321300, '宿迁经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (321323, 321300, '泗阳县');
INSERT INTO `wk_crm_area` VALUES (321302, 321300, '宿城区');
INSERT INTO `wk_crm_area` VALUES (321324, 321300, '泗洪县');
INSERT INTO `wk_crm_area` VALUES (321311, 321300, '宿豫区');
INSERT INTO `wk_crm_area` VALUES (321322, 321300, '沭阳县');
INSERT INTO `wk_crm_area` VALUES (510903, 510900, '船山区');
INSERT INTO `wk_crm_area` VALUES (510904, 510900, '安居区');
INSERT INTO `wk_crm_area` VALUES (510923, 510900, '大英县');
INSERT INTO `wk_crm_area` VALUES (510921, 510900, '蓬溪县');
INSERT INTO `wk_crm_area` VALUES (510922, 510900, '射洪县');
INSERT INTO `wk_crm_area` VALUES (440705, 440700, '新会区');
INSERT INTO `wk_crm_area` VALUES (440704, 440700, '江海区');
INSERT INTO `wk_crm_area` VALUES (440703, 440700, '蓬江区');
INSERT INTO `wk_crm_area` VALUES (440785, 440700, '恩平市');
INSERT INTO `wk_crm_area` VALUES (440784, 440700, '鹤山市');
INSERT INTO `wk_crm_area` VALUES (440783, 440700, '开平市');
INSERT INTO `wk_crm_area` VALUES (440781, 440700, '台山市');
INSERT INTO `wk_crm_area` VALUES (430981, 430900, '沅江市');
INSERT INTO `wk_crm_area` VALUES (430972, 430900, '湖南益阳高新技术产业园区');
INSERT INTO `wk_crm_area` VALUES (430971, 430900, '益阳市大通湖管理区');
INSERT INTO `wk_crm_area` VALUES (430921, 430900, '南县');
INSERT INTO `wk_crm_area` VALUES (430923, 430900, '安化县');
INSERT INTO `wk_crm_area` VALUES (430922, 430900, '桃江县');
INSERT INTO `wk_crm_area` VALUES (430903, 430900, '赫山区');
INSERT INTO `wk_crm_area` VALUES (430902, 430900, '资阳区');
INSERT INTO `wk_crm_area` VALUES (350600, 350000, '漳州市');
INSERT INTO `wk_crm_area` VALUES (350700, 350000, '南平市');
INSERT INTO `wk_crm_area` VALUES (350800, 350000, '龙岩市');
INSERT INTO `wk_crm_area` VALUES (350900, 350000, '宁德市');
INSERT INTO `wk_crm_area` VALUES (350100, 350000, '福州市');
INSERT INTO `wk_crm_area` VALUES (350200, 350000, '厦门市');
INSERT INTO `wk_crm_area` VALUES (350300, 350000, '莆田市');
INSERT INTO `wk_crm_area` VALUES (350400, 350000, '三明市');
INSERT INTO `wk_crm_area` VALUES (350500, 350000, '泉州市');
INSERT INTO `wk_crm_area` VALUES (331121, 331100, '青田县');
INSERT INTO `wk_crm_area` VALUES (331181, 331100, '龙泉市');
INSERT INTO `wk_crm_area` VALUES (331126, 331100, '庆元县');
INSERT INTO `wk_crm_area` VALUES (331127, 331100, '景宁畲族自治县');
INSERT INTO `wk_crm_area` VALUES (331122, 331100, '缙云县');
INSERT INTO `wk_crm_area` VALUES (331123, 331100, '遂昌县');
INSERT INTO `wk_crm_area` VALUES (331102, 331100, '莲都区');
INSERT INTO `wk_crm_area` VALUES (331124, 331100, '松阳县');
INSERT INTO `wk_crm_area` VALUES (331125, 331100, '云和县');
INSERT INTO `wk_crm_area` VALUES (150902, 150900, '集宁区');
INSERT INTO `wk_crm_area` VALUES (150924, 150900, '兴和县');
INSERT INTO `wk_crm_area` VALUES (150923, 150900, '商都县');
INSERT INTO `wk_crm_area` VALUES (150922, 150900, '化德县');
INSERT INTO `wk_crm_area` VALUES (150921, 150900, '卓资县');
INSERT INTO `wk_crm_area` VALUES (150981, 150900, '丰镇市');
INSERT INTO `wk_crm_area` VALUES (150929, 150900, '四子王旗');
INSERT INTO `wk_crm_area` VALUES (150928, 150900, '察哈尔右翼后旗');
INSERT INTO `wk_crm_area` VALUES (150927, 150900, '察哈尔右翼中旗');
INSERT INTO `wk_crm_area` VALUES (150926, 150900, '察哈尔右翼前旗');
INSERT INTO `wk_crm_area` VALUES (150925, 150900, '凉城县');
INSERT INTO `wk_crm_area` VALUES (530602, 530600, '昭阳区');
INSERT INTO `wk_crm_area` VALUES (530624, 530600, '大关县');
INSERT INTO `wk_crm_area` VALUES (530625, 530600, '永善县');
INSERT INTO `wk_crm_area` VALUES (530622, 530600, '巧家县');
INSERT INTO `wk_crm_area` VALUES (530623, 530600, '盐津县');
INSERT INTO `wk_crm_area` VALUES (530621, 530600, '鲁甸县');
INSERT INTO `wk_crm_area` VALUES (530681, 530600, '水富市');
INSERT INTO `wk_crm_area` VALUES (530628, 530600, '彝良县');
INSERT INTO `wk_crm_area` VALUES (530629, 530600, '威信县');
INSERT INTO `wk_crm_area` VALUES (530626, 530600, '绥江县');
INSERT INTO `wk_crm_area` VALUES (530627, 530600, '镇雄县');
INSERT INTO `wk_crm_area` VALUES (532924, 532900, '宾川县');
INSERT INTO `wk_crm_area` VALUES (532901, 532900, '大理市');
INSERT INTO `wk_crm_area` VALUES (532923, 532900, '祥云县');
INSERT INTO `wk_crm_area` VALUES (532922, 532900, '漾濞彝族自治县');
INSERT INTO `wk_crm_area` VALUES (532932, 532900, '鹤庆县');
INSERT INTO `wk_crm_area` VALUES (532931, 532900, '剑川县');
INSERT INTO `wk_crm_area` VALUES (532930, 532900, '洱源县');
INSERT INTO `wk_crm_area` VALUES (532929, 532900, '云龙县');
INSERT INTO `wk_crm_area` VALUES (532928, 532900, '永平县');
INSERT INTO `wk_crm_area` VALUES (532927, 532900, '巍山彝族回族自治县');
INSERT INTO `wk_crm_area` VALUES (532926, 532900, '南涧彝族自治县');
INSERT INTO `wk_crm_area` VALUES (532925, 532900, '弥渡县');
INSERT INTO `wk_crm_area` VALUES (653022, 653000, '阿克陶县');
INSERT INTO `wk_crm_area` VALUES (653001, 653000, '阿图什市');
INSERT INTO `wk_crm_area` VALUES (653023, 653000, '阿合奇县');
INSERT INTO `wk_crm_area` VALUES (653024, 653000, '乌恰县');
INSERT INTO `wk_crm_area` VALUES (611026, 611000, '柞水县');
INSERT INTO `wk_crm_area` VALUES (611025, 611000, '镇安县');
INSERT INTO `wk_crm_area` VALUES (611002, 611000, '商州区');
INSERT INTO `wk_crm_area` VALUES (611024, 611000, '山阳县');
INSERT INTO `wk_crm_area` VALUES (611023, 611000, '商南县');
INSERT INTO `wk_crm_area` VALUES (611022, 611000, '丹凤县');
INSERT INTO `wk_crm_area` VALUES (611021, 611000, '洛南县');
INSERT INTO `wk_crm_area` VALUES (632224, 632200, '刚察县');
INSERT INTO `wk_crm_area` VALUES (632223, 632200, '海晏县');
INSERT INTO `wk_crm_area` VALUES (632222, 632200, '祁连县');
INSERT INTO `wk_crm_area` VALUES (632221, 632200, '门源回族自治县');
INSERT INTO `wk_crm_area` VALUES (450503, 450500, '银海区');
INSERT INTO `wk_crm_area` VALUES (450502, 450500, '海城区');
INSERT INTO `wk_crm_area` VALUES (450521, 450500, '合浦县');
INSERT INTO `wk_crm_area` VALUES (450512, 450500, '铁山港区');
INSERT INTO `wk_crm_area` VALUES (320106, 320100, '鼓楼区');
INSERT INTO `wk_crm_area` VALUES (320117, 320100, '溧水区');
INSERT INTO `wk_crm_area` VALUES (320118, 320100, '高淳区');
INSERT INTO `wk_crm_area` VALUES (320104, 320100, '秦淮区');
INSERT INTO `wk_crm_area` VALUES (320115, 320100, '江宁区');
INSERT INTO `wk_crm_area` VALUES (320105, 320100, '建邺区');
INSERT INTO `wk_crm_area` VALUES (320116, 320100, '六合区');
INSERT INTO `wk_crm_area` VALUES (320102, 320100, '玄武区');
INSERT INTO `wk_crm_area` VALUES (320113, 320100, '栖霞区');
INSERT INTO `wk_crm_area` VALUES (320114, 320100, '雨花台区');
INSERT INTO `wk_crm_area` VALUES (320111, 320100, '浦口区');
INSERT INTO `wk_crm_area` VALUES (540104, 540100, '达孜区');
INSERT INTO `wk_crm_area` VALUES (540127, 540100, '墨竹工卡县');
INSERT INTO `wk_crm_area` VALUES (540122, 540100, '当雄县');
INSERT INTO `wk_crm_area` VALUES (540123, 540100, '尼木县');
INSERT INTO `wk_crm_area` VALUES (540102, 540100, '城关区');
INSERT INTO `wk_crm_area` VALUES (540124, 540100, '曲水县');
INSERT INTO `wk_crm_area` VALUES (540103, 540100, '堆龙德庆区');
INSERT INTO `wk_crm_area` VALUES (540173, 540100, '西藏文化旅游创意园区');
INSERT INTO `wk_crm_area` VALUES (540174, 540100, '达孜工业园区');
INSERT INTO `wk_crm_area` VALUES (540121, 540100, '林周县');
INSERT INTO `wk_crm_area` VALUES (540171, 540100, '格尔木藏青工业园区');
INSERT INTO `wk_crm_area` VALUES (540172, 540100, '拉萨经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (110105, 110100, '朝阳区');
INSERT INTO `wk_crm_area` VALUES (110116, 110100, '怀柔区');
INSERT INTO `wk_crm_area` VALUES (110106, 110100, '丰台区');
INSERT INTO `wk_crm_area` VALUES (110117, 110100, '平谷区');
INSERT INTO `wk_crm_area` VALUES (110114, 110100, '昌平区');
INSERT INTO `wk_crm_area` VALUES (110115, 110100, '大兴区');
INSERT INTO `wk_crm_area` VALUES (110101, 110100, '东城区');
INSERT INTO `wk_crm_area` VALUES (110112, 110100, '通州区');
INSERT INTO `wk_crm_area` VALUES (110102, 110100, '西城区');
INSERT INTO `wk_crm_area` VALUES (110113, 110100, '顺义区');
INSERT INTO `wk_crm_area` VALUES (110111, 110100, '房山区');
INSERT INTO `wk_crm_area` VALUES (110109, 110100, '门头沟区');
INSERT INTO `wk_crm_area` VALUES (110107, 110100, '石景山区');
INSERT INTO `wk_crm_area` VALUES (110118, 110100, '密云区');
INSERT INTO `wk_crm_area` VALUES (110108, 110100, '海淀区');
INSERT INTO `wk_crm_area` VALUES (110119, 110100, '延庆区');
INSERT INTO `wk_crm_area` VALUES (331000, 330000, '台州市');
INSERT INTO `wk_crm_area` VALUES (330800, 330000, '衢州市');
INSERT INTO `wk_crm_area` VALUES (330900, 330000, '舟山市');
INSERT INTO `wk_crm_area` VALUES (330400, 330000, '嘉兴市');
INSERT INTO `wk_crm_area` VALUES (330500, 330000, '湖州市');
INSERT INTO `wk_crm_area` VALUES (330600, 330000, '绍兴市');
INSERT INTO `wk_crm_area` VALUES (330700, 330000, '金华市');
INSERT INTO `wk_crm_area` VALUES (331100, 330000, '丽水市');
INSERT INTO `wk_crm_area` VALUES (330100, 330000, '杭州市');
INSERT INTO `wk_crm_area` VALUES (330200, 330000, '宁波市');
INSERT INTO `wk_crm_area` VALUES (330300, 330000, '温州市');
INSERT INTO `wk_crm_area` VALUES (141081, 141000, '侯马市');
INSERT INTO `wk_crm_area` VALUES (141082, 141000, '霍州市');
INSERT INTO `wk_crm_area` VALUES (141030, 141000, '大宁县');
INSERT INTO `wk_crm_area` VALUES (141021, 141000, '曲沃县');
INSERT INTO `wk_crm_area` VALUES (141032, 141000, '永和县');
INSERT INTO `wk_crm_area` VALUES (141031, 141000, '隰县');
INSERT INTO `wk_crm_area` VALUES (141023, 141000, '襄汾县');
INSERT INTO `wk_crm_area` VALUES (141034, 141000, '汾西县');
INSERT INTO `wk_crm_area` VALUES (141022, 141000, '翼城县');
INSERT INTO `wk_crm_area` VALUES (141033, 141000, '蒲县');
INSERT INTO `wk_crm_area` VALUES (141025, 141000, '古县');
INSERT INTO `wk_crm_area` VALUES (141002, 141000, '尧都区');
INSERT INTO `wk_crm_area` VALUES (141024, 141000, '洪洞县');
INSERT INTO `wk_crm_area` VALUES (141027, 141000, '浮山县');
INSERT INTO `wk_crm_area` VALUES (141026, 141000, '安泽县');
INSERT INTO `wk_crm_area` VALUES (141029, 141000, '乡宁县');
INSERT INTO `wk_crm_area` VALUES (141028, 141000, '吉县');
INSERT INTO `wk_crm_area` VALUES (654021, 654000, '伊宁县');
INSERT INTO `wk_crm_area` VALUES (654022, 654000, '察布查尔锡伯自治县');
INSERT INTO `wk_crm_area` VALUES (654023, 654000, '霍城县');
INSERT INTO `wk_crm_area` VALUES (654002, 654000, '伊宁市');
INSERT INTO `wk_crm_area` VALUES (654024, 654000, '巩留县');
INSERT INTO `wk_crm_area` VALUES (654003, 654000, '奎屯市');
INSERT INTO `wk_crm_area` VALUES (654025, 654000, '新源县');
INSERT INTO `wk_crm_area` VALUES (654004, 654000, '霍尔果斯市');
INSERT INTO `wk_crm_area` VALUES (654026, 654000, '昭苏县');
INSERT INTO `wk_crm_area` VALUES (654027, 654000, '特克斯县');
INSERT INTO `wk_crm_area` VALUES (654028, 654000, '尼勒克县');
INSERT INTO `wk_crm_area` VALUES (411171, 411100, '漯河经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (411104, 411100, '召陵区');
INSERT INTO `wk_crm_area` VALUES (411103, 411100, '郾城区');
INSERT INTO `wk_crm_area` VALUES (411122, 411100, '临颍县');
INSERT INTO `wk_crm_area` VALUES (411121, 411100, '舞阳县');
INSERT INTO `wk_crm_area` VALUES (411102, 411100, '源汇区');
INSERT INTO `wk_crm_area` VALUES (820100, 820000, '澳门城区');
INSERT INTO `wk_crm_area` VALUES (510821, 510800, '旺苍县');
INSERT INTO `wk_crm_area` VALUES (510802, 510800, '利州区');
INSERT INTO `wk_crm_area` VALUES (510824, 510800, '苍溪县');
INSERT INTO `wk_crm_area` VALUES (510811, 510800, '昭化区');
INSERT INTO `wk_crm_area` VALUES (510822, 510800, '青川县');
INSERT INTO `wk_crm_area` VALUES (510812, 510800, '朝天区');
INSERT INTO `wk_crm_area` VALUES (510823, 510800, '剑阁县');
INSERT INTO `wk_crm_area` VALUES (130100, 130000, '石家庄市');
INSERT INTO `wk_crm_area` VALUES (131100, 130000, '衡水市');
INSERT INTO `wk_crm_area` VALUES (131000, 130000, '廊坊市');
INSERT INTO `wk_crm_area` VALUES (130400, 130000, '邯郸市');
INSERT INTO `wk_crm_area` VALUES (130500, 130000, '邢台市');
INSERT INTO `wk_crm_area` VALUES (130200, 130000, '唐山市');
INSERT INTO `wk_crm_area` VALUES (130300, 130000, '秦皇岛市');
INSERT INTO `wk_crm_area` VALUES (130800, 130000, '承德市');
INSERT INTO `wk_crm_area` VALUES (130900, 130000, '沧州市');
INSERT INTO `wk_crm_area` VALUES (130600, 130000, '保定市');
INSERT INTO `wk_crm_area` VALUES (130700, 130000, '张家口市');
INSERT INTO `wk_crm_area` VALUES (520521, 520500, '大方县');
INSERT INTO `wk_crm_area` VALUES (520522, 520500, '黔西县');
INSERT INTO `wk_crm_area` VALUES (520523, 520500, '金沙县');
INSERT INTO `wk_crm_area` VALUES (520502, 520500, '七星关区');
INSERT INTO `wk_crm_area` VALUES (520524, 520500, '织金县');
INSERT INTO `wk_crm_area` VALUES (520525, 520500, '纳雍县');
INSERT INTO `wk_crm_area` VALUES (520526, 520500, '威宁彝族回族苗族自治县');
INSERT INTO `wk_crm_area` VALUES (520527, 520500, '赫章县');
INSERT INTO `wk_crm_area` VALUES (445122, 445100, '饶平县');
INSERT INTO `wk_crm_area` VALUES (445103, 445100, '潮安区');
INSERT INTO `wk_crm_area` VALUES (445102, 445100, '湘桥区');
INSERT INTO `wk_crm_area` VALUES (450804, 450800, '覃塘区');
INSERT INTO `wk_crm_area` VALUES (450803, 450800, '港南区');
INSERT INTO `wk_crm_area` VALUES (450821, 450800, '平南县');
INSERT INTO `wk_crm_area` VALUES (450802, 450800, '港北区');
INSERT INTO `wk_crm_area` VALUES (450881, 450800, '桂平市');
INSERT INTO `wk_crm_area` VALUES (532627, 532600, '广南县');
INSERT INTO `wk_crm_area` VALUES (532626, 532600, '丘北县');
INSERT INTO `wk_crm_area` VALUES (532625, 532600, '马关县');
INSERT INTO `wk_crm_area` VALUES (532624, 532600, '麻栗坡县');
INSERT INTO `wk_crm_area` VALUES (532601, 532600, '文山市');
INSERT INTO `wk_crm_area` VALUES (532623, 532600, '西畴县');
INSERT INTO `wk_crm_area` VALUES (532622, 532600, '砚山县');
INSERT INTO `wk_crm_area` VALUES (532628, 532600, '富宁县');
INSERT INTO `wk_crm_area` VALUES (530303, 530300, '沾益区');
INSERT INTO `wk_crm_area` VALUES (530325, 530300, '富源县');
INSERT INTO `wk_crm_area` VALUES (530304, 530300, '马龙区');
INSERT INTO `wk_crm_area` VALUES (530326, 530300, '会泽县');
INSERT INTO `wk_crm_area` VALUES (530323, 530300, '师宗县');
INSERT INTO `wk_crm_area` VALUES (530302, 530300, '麒麟区');
INSERT INTO `wk_crm_area` VALUES (530324, 530300, '罗平县');
INSERT INTO `wk_crm_area` VALUES (530322, 530300, '陆良县');
INSERT INTO `wk_crm_area` VALUES (530381, 530300, '宣威市');
INSERT INTO `wk_crm_area` VALUES (341021, 341000, '歙县');
INSERT INTO `wk_crm_area` VALUES (341002, 341000, '屯溪区');
INSERT INTO `wk_crm_area` VALUES (341024, 341000, '祁门县');
INSERT INTO `wk_crm_area` VALUES (341003, 341000, '黄山区');
INSERT INTO `wk_crm_area` VALUES (341022, 341000, '休宁县');
INSERT INTO `wk_crm_area` VALUES (341023, 341000, '黟县');
INSERT INTO `wk_crm_area` VALUES (341004, 341000, '徽州区');
INSERT INTO `wk_crm_area` VALUES (230904, 230900, '茄子河区');
INSERT INTO `wk_crm_area` VALUES (230903, 230900, '桃山区');
INSERT INTO `wk_crm_area` VALUES (230902, 230900, '新兴区');
INSERT INTO `wk_crm_area` VALUES (230921, 230900, '勃利县');
INSERT INTO `wk_crm_area` VALUES (320281, 320200, '江阴市');
INSERT INTO `wk_crm_area` VALUES (320282, 320200, '宜兴市');
INSERT INTO `wk_crm_area` VALUES (320205, 320200, '锡山区');
INSERT INTO `wk_crm_area` VALUES (320206, 320200, '惠山区');
INSERT INTO `wk_crm_area` VALUES (320214, 320200, '新吴区');
INSERT INTO `wk_crm_area` VALUES (320213, 320200, '梁溪区');
INSERT INTO `wk_crm_area` VALUES (320211, 320200, '滨湖区');
INSERT INTO `wk_crm_area` VALUES (131123, 131100, '武强县');
INSERT INTO `wk_crm_area` VALUES (131122, 131100, '武邑县');
INSERT INTO `wk_crm_area` VALUES (131121, 131100, '枣强县');
INSERT INTO `wk_crm_area` VALUES (131127, 131100, '景县');
INSERT INTO `wk_crm_area` VALUES (131126, 131100, '故城县');
INSERT INTO `wk_crm_area` VALUES (131103, 131100, '冀州区');
INSERT INTO `wk_crm_area` VALUES (131125, 131100, '安平县');
INSERT INTO `wk_crm_area` VALUES (131102, 131100, '桃城区');
INSERT INTO `wk_crm_area` VALUES (131124, 131100, '饶阳县');
INSERT INTO `wk_crm_area` VALUES (131128, 131100, '阜城县');
INSERT INTO `wk_crm_area` VALUES (131172, 131100, '衡水滨湖新区');
INSERT INTO `wk_crm_area` VALUES (131171, 131100, '河北衡水高新技术产业开发区');
INSERT INTO `wk_crm_area` VALUES (131182, 131100, '深州市');
INSERT INTO `wk_crm_area` VALUES (540225, 540200, '拉孜县');
INSERT INTO `wk_crm_area` VALUES (540236, 540200, '萨嘎县');
INSERT INTO `wk_crm_area` VALUES (540226, 540200, '昂仁县');
INSERT INTO `wk_crm_area` VALUES (540237, 540200, '岗巴县');
INSERT INTO `wk_crm_area` VALUES (540227, 540200, '谢通门县');
INSERT INTO `wk_crm_area` VALUES (540228, 540200, '白朗县');
INSERT INTO `wk_crm_area` VALUES (540221, 540200, '南木林县');
INSERT INTO `wk_crm_area` VALUES (540232, 540200, '仲巴县');
INSERT INTO `wk_crm_area` VALUES (540222, 540200, '江孜县');
INSERT INTO `wk_crm_area` VALUES (540233, 540200, '亚东县');
INSERT INTO `wk_crm_area` VALUES (540223, 540200, '定日县');
INSERT INTO `wk_crm_area` VALUES (540234, 540200, '吉隆县');
INSERT INTO `wk_crm_area` VALUES (540202, 540200, '桑珠孜区');
INSERT INTO `wk_crm_area` VALUES (540224, 540200, '萨迦县');
INSERT INTO `wk_crm_area` VALUES (540235, 540200, '聂拉木县');
INSERT INTO `wk_crm_area` VALUES (540229, 540200, '仁布县');
INSERT INTO `wk_crm_area` VALUES (540230, 540200, '康马县');
INSERT INTO `wk_crm_area` VALUES (540231, 540200, '定结县');
INSERT INTO `wk_crm_area` VALUES (542525, 542500, '革吉县');
INSERT INTO `wk_crm_area` VALUES (542524, 542500, '日土县');
INSERT INTO `wk_crm_area` VALUES (542527, 542500, '措勤县');
INSERT INTO `wk_crm_area` VALUES (542526, 542500, '改则县');
INSERT INTO `wk_crm_area` VALUES (542521, 542500, '普兰县');
INSERT INTO `wk_crm_area` VALUES (542523, 542500, '噶尔县');
INSERT INTO `wk_crm_area` VALUES (542522, 542500, '札达县');
INSERT INTO `wk_crm_area` VALUES (330108, 330100, '滨江区');
INSERT INTO `wk_crm_area` VALUES (330109, 330100, '萧山区');
INSERT INTO `wk_crm_area` VALUES (330103, 330100, '下城区');
INSERT INTO `wk_crm_area` VALUES (330104, 330100, '江干区');
INSERT INTO `wk_crm_area` VALUES (330105, 330100, '拱墅区');
INSERT INTO `wk_crm_area` VALUES (330127, 330100, '淳安县');
INSERT INTO `wk_crm_area` VALUES (330106, 330100, '西湖区');
INSERT INTO `wk_crm_area` VALUES (330110, 330100, '余杭区');
INSERT INTO `wk_crm_area` VALUES (330111, 330100, '富阳区');
INSERT INTO `wk_crm_area` VALUES (330122, 330100, '桐庐县');
INSERT INTO `wk_crm_area` VALUES (330112, 330100, '临安区');
INSERT INTO `wk_crm_area` VALUES (330102, 330100, '上城区');
INSERT INTO `wk_crm_area` VALUES (330182, 330100, '建德市');
INSERT INTO `wk_crm_area` VALUES (120100, 120000, '天津城区');
INSERT INTO `wk_crm_area` VALUES (610300, 610000, '宝鸡市');
INSERT INTO `wk_crm_area` VALUES (610400, 610000, '咸阳市');
INSERT INTO `wk_crm_area` VALUES (610100, 610000, '西安市');
INSERT INTO `wk_crm_area` VALUES (610200, 610000, '铜川市');
INSERT INTO `wk_crm_area` VALUES (611000, 610000, '商洛市');
INSERT INTO `wk_crm_area` VALUES (610900, 610000, '安康市');
INSERT INTO `wk_crm_area` VALUES (610700, 610000, '汉中市');
INSERT INTO `wk_crm_area` VALUES (610800, 610000, '榆林市');
INSERT INTO `wk_crm_area` VALUES (610500, 610000, '渭南市');
INSERT INTO `wk_crm_area` VALUES (610600, 610000, '延安市');
INSERT INTO `wk_crm_area` VALUES (411081, 411000, '禹州市');
INSERT INTO `wk_crm_area` VALUES (411071, 411000, '许昌经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (411082, 411000, '长葛市');
INSERT INTO `wk_crm_area` VALUES (411003, 411000, '建安区');
INSERT INTO `wk_crm_area` VALUES (411025, 411000, '襄城县');
INSERT INTO `wk_crm_area` VALUES (411002, 411000, '魏都区');
INSERT INTO `wk_crm_area` VALUES (411024, 411000, '鄢陵县');
INSERT INTO `wk_crm_area` VALUES (510705, 510700, '安州区');
INSERT INTO `wk_crm_area` VALUES (510727, 510700, '平武县');
INSERT INTO `wk_crm_area` VALUES (510781, 510700, '江油市');
INSERT INTO `wk_crm_area` VALUES (510722, 510700, '三台县');
INSERT INTO `wk_crm_area` VALUES (510723, 510700, '盐亭县');
INSERT INTO `wk_crm_area` VALUES (510704, 510700, '游仙区');
INSERT INTO `wk_crm_area` VALUES (510726, 510700, '北川羌族自治县');
INSERT INTO `wk_crm_area` VALUES (510703, 510700, '涪城区');
INSERT INTO `wk_crm_area` VALUES (510725, 510700, '梓潼县');
INSERT INTO `wk_crm_area` VALUES (820107, 820100, '路凼填海区');
INSERT INTO `wk_crm_area` VALUES (820108, 820100, '圣方济各堂区');
INSERT INTO `wk_crm_area` VALUES (820105, 820100, '风顺堂区');
INSERT INTO `wk_crm_area` VALUES (820106, 820100, '嘉模堂区');
INSERT INTO `wk_crm_area` VALUES (820103, 820100, '望德堂区');
INSERT INTO `wk_crm_area` VALUES (820104, 820100, '大堂区');
INSERT INTO `wk_crm_area` VALUES (820101, 820100, '花地玛堂区');
INSERT INTO `wk_crm_area` VALUES (820102, 820100, '花王堂区');
INSERT INTO `wk_crm_area` VALUES (440904, 440900, '电白区');
INSERT INTO `wk_crm_area` VALUES (440902, 440900, '茂南区');
INSERT INTO `wk_crm_area` VALUES (440983, 440900, '信宜市');
INSERT INTO `wk_crm_area` VALUES (440982, 440900, '化州市');
INSERT INTO `wk_crm_area` VALUES (440981, 440900, '高州市');
INSERT INTO `wk_crm_area` VALUES (621002, 621000, '西峰区');
INSERT INTO `wk_crm_area` VALUES (621024, 621000, '合水县');
INSERT INTO `wk_crm_area` VALUES (621023, 621000, '华池县');
INSERT INTO `wk_crm_area` VALUES (621026, 621000, '宁县');
INSERT INTO `wk_crm_area` VALUES (621025, 621000, '正宁县');
INSERT INTO `wk_crm_area` VALUES (621022, 621000, '环县');
INSERT INTO `wk_crm_area` VALUES (621021, 621000, '庆城县');
INSERT INTO `wk_crm_area` VALUES (621027, 621000, '镇原县');
INSERT INTO `wk_crm_area` VALUES (520627, 520600, '沿河土家族自治县');
INSERT INTO `wk_crm_area` VALUES (520628, 520600, '松桃苗族自治县');
INSERT INTO `wk_crm_area` VALUES (520621, 520600, '江口县');
INSERT INTO `wk_crm_area` VALUES (520622, 520600, '玉屏侗族自治县');
INSERT INTO `wk_crm_area` VALUES (520623, 520600, '石阡县');
INSERT INTO `wk_crm_area` VALUES (520602, 520600, '碧江区');
INSERT INTO `wk_crm_area` VALUES (520624, 520600, '思南县');
INSERT INTO `wk_crm_area` VALUES (520603, 520600, '万山区');
INSERT INTO `wk_crm_area` VALUES (520625, 520600, '印江土家族苗族自治县');
INSERT INTO `wk_crm_area` VALUES (520626, 520600, '德江县');
INSERT INTO `wk_crm_area` VALUES (530426, 530400, '峨山彝族自治县');
INSERT INTO `wk_crm_area` VALUES (530427, 530400, '新平彝族傣族自治县');
INSERT INTO `wk_crm_area` VALUES (530402, 530400, '红塔区');
INSERT INTO `wk_crm_area` VALUES (530424, 530400, '华宁县');
INSERT INTO `wk_crm_area` VALUES (530403, 530400, '江川区');
INSERT INTO `wk_crm_area` VALUES (530425, 530400, '易门县');
INSERT INTO `wk_crm_area` VALUES (530422, 530400, '澄江县');
INSERT INTO `wk_crm_area` VALUES (530423, 530400, '通海县');
INSERT INTO `wk_crm_area` VALUES (530428, 530400, '元江哈尼族彝族傣族自治县');
INSERT INTO `wk_crm_area` VALUES (450722, 450700, '浦北县');
INSERT INTO `wk_crm_area` VALUES (450703, 450700, '钦北区');
INSERT INTO `wk_crm_area` VALUES (450702, 450700, '钦南区');
INSERT INTO `wk_crm_area` VALUES (450721, 450700, '灵山县');
INSERT INTO `wk_crm_area` VALUES (341181, 341100, '天长市');
INSERT INTO `wk_crm_area` VALUES (341171, 341100, '苏滁现代产业园');
INSERT INTO `wk_crm_area` VALUES (341182, 341100, '明光市');
INSERT INTO `wk_crm_area` VALUES (341172, 341100, '滁州经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (341102, 341100, '琅琊区');
INSERT INTO `wk_crm_area` VALUES (341124, 341100, '全椒县');
INSERT INTO `wk_crm_area` VALUES (341122, 341100, '来安县');
INSERT INTO `wk_crm_area` VALUES (341103, 341100, '南谯区');
INSERT INTO `wk_crm_area` VALUES (341125, 341100, '定远县');
INSERT INTO `wk_crm_area` VALUES (341126, 341100, '凤阳县');
INSERT INTO `wk_crm_area` VALUES (320371, 320300, '徐州经济技术开发区');
INSERT INTO `wk_crm_area` VALUES (320382, 320300, '邳州市');
INSERT INTO `wk_crm_area` VALUES (320381, 320300, '新沂市');
INSERT INTO `wk_crm_area` VALUES (320305, 320300, '贾汪区');
INSERT INTO `wk_crm_area` VALUES (320302, 320300, '鼓楼区');
INSERT INTO `wk_crm_area` VALUES (320324, 320300, '睢宁县');
INSERT INTO `wk_crm_area` VALUES (320303, 320300, '云龙区');
INSERT INTO `wk_crm_area` VALUES (320311, 320300, '泉山区');
INSERT INTO `wk_crm_area` VALUES (320322, 320300, '沛县');
INSERT INTO `wk_crm_area` VALUES (320312, 320300, '铜山区');
INSERT INTO `wk_crm_area` VALUES (320321, 320300, '丰县');
INSERT INTO `wk_crm_area` VALUES (230883, 230800, '抚远市');
INSERT INTO `wk_crm_area` VALUES (230882, 230800, '富锦市');
INSERT INTO `wk_crm_area` VALUES (230881, 230800, '同江市');
INSERT INTO `wk_crm_area` VALUES (230828, 230800, '汤原县');
INSERT INTO `wk_crm_area` VALUES (230805, 230800, '东风区');
INSERT INTO `wk_crm_area` VALUES (230804, 230800, '前进区');
INSERT INTO `wk_crm_area` VALUES (230826, 230800, '桦川县');
INSERT INTO `wk_crm_area` VALUES (230803, 230800, '向阳区');
INSERT INTO `wk_crm_area` VALUES (230811, 230800, '郊区');
INSERT INTO `wk_crm_area` VALUES (230822, 230800, '桦南县');
INSERT INTO `wk_crm_area` VALUES (511971, 511900, '巴中经济开发区');
INSERT INTO `wk_crm_area` VALUES (511921, 511900, '通江县');
INSERT INTO `wk_crm_area` VALUES (511902, 511900, '巴州区');
INSERT INTO `wk_crm_area` VALUES (511903, 511900, '恩阳区');
INSERT INTO `wk_crm_area` VALUES (511922, 511900, '南江县');
INSERT INTO `wk_crm_area` VALUES (511923, 511900, '平昌县');

-- ----------------------------
-- Table structure for wk_crm_back_log_deal
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_back_log_deal`;
CREATE TABLE `wk_crm_back_log_deal`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `model` int(2) NOT NULL COMMENT '待办事项模块 1今日需联系客户 2分配给我的线索 3分配给我的客户 4待进入公海的客户 5待审核合同 6待审核回款 7待回款提醒 8即将到期的合同 9待回访合同 10待审核发票',
  `crm_type` int(2) NOT NULL COMMENT '数据模块',
  `type_id` int(11) NOT NULL COMMENT '数据id',
  `pool_id` int(11) NULL DEFAULT NULL COMMENT '公海id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '待办事项标记处理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_back_log_deal
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_business
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_business`;
CREATE TABLE `wk_crm_business`  (
  `business_id` int(11) NOT NULL AUTO_INCREMENT,
  `type_id` int(11) NULL DEFAULT NULL COMMENT '商机状态组',
  `status_id` int(11) NULL DEFAULT NULL COMMENT '销售阶段',
  `next_time` datetime(0) NULL DEFAULT NULL COMMENT '下次联系时间',
  `customer_id` int(11) NULL DEFAULT NULL COMMENT '客户ID',
  `contacts_id` int(11) NULL DEFAULT NULL COMMENT '首要联系人ID',
  `deal_date` datetime(0) NULL DEFAULT NULL COMMENT '预计成交日期',
  `business_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商机名称',
  `money` decimal(18, 2) NULL DEFAULT NULL COMMENT '商机金额',
  `discount_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '整单折扣',
  `total_price` decimal(17, 2) NULL DEFAULT NULL COMMENT '产品总金额',
  `remark` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '备注',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `owner_user_id` bigint(20) NULL DEFAULT NULL COMMENT '负责人ID',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `batch_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次 比如附件批次',
  `ro_user_id` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '只读权限',
  `rw_user_id` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '读写权限',
  `is_end` int(4) NOT NULL DEFAULT 0 COMMENT '1赢单2输单3无效',
  `status_remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `status` int(1) NULL DEFAULT 1 COMMENT '1正常 3  删除',
  `last_time` datetime(0) NULL DEFAULT NULL COMMENT '最后跟进时间',
  `followup` int(1) NULL DEFAULT NULL COMMENT '0 未跟进 1 已跟进',
  PRIMARY KEY (`business_id`) USING BTREE,
  INDEX `owner_user_id`(`owner_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商机表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_business
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_business_change
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_business_change`;
CREATE TABLE `wk_crm_business_change`  (
  `change_id` int(10) NOT NULL AUTO_INCREMENT,
  `business_id` int(10) NOT NULL COMMENT '商机ID',
  `status_id` int(10) NOT NULL COMMENT '阶段ID',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人',
  PRIMARY KEY (`change_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商机阶段变化表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_business_change
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_business_data
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_business_data`;
CREATE TABLE `wk_crm_business_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NOT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商机扩展字段数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_business_data
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_business_product
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_business_product`;
CREATE TABLE `wk_crm_business_product`  (
  `r_id` int(11) NOT NULL AUTO_INCREMENT,
  `business_id` int(11) NOT NULL COMMENT '商机ID',
  `product_id` int(11) NOT NULL COMMENT '产品ID',
  `price` decimal(18, 2) NOT NULL COMMENT '产品单价',
  `sales_price` decimal(18, 2) NOT NULL COMMENT '销售价格',
  `num` decimal(10, 2) NOT NULL COMMENT '数量',
  `discount` int(10) NOT NULL COMMENT '折扣',
  `subtotal` decimal(18, 2) NOT NULL COMMENT '小计（折扣后价格）',
  `unit` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '单位',
  PRIMARY KEY (`r_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商机产品关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_business_product
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_business_status
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_business_status`;
CREATE TABLE `wk_crm_business_status`  (
  `status_id` int(11) NOT NULL AUTO_INCREMENT,
  `type_id` int(11) NOT NULL COMMENT '商机状态类别ID',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标识',
  `rate` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '赢单率',
  `order_num` int(4) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`status_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 47646 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商机状态' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_business_status
-- ----------------------------
INSERT INTO `wk_crm_business_status` VALUES (47643, 12366, '验证客户', '20', 1);
INSERT INTO `wk_crm_business_status` VALUES (47644, 12366, '需求分析', '30', 2);
INSERT INTO `wk_crm_business_status` VALUES (47645, 12366, '方案/报价', '80', 3);

-- ----------------------------
-- Table structure for wk_crm_business_type
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_business_type`;
CREATE TABLE `wk_crm_business_type`  (
  `type_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标识',
  `dept_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部门ID',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '0禁用1启用2删除',
  PRIMARY KEY (`type_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12367 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商机状态组类别' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_business_type
-- ----------------------------
INSERT INTO `wk_crm_business_type` VALUES (12366, '销售流程商机组', '', 3, '2019-05-11 16:25:09', NULL, 1);

-- ----------------------------
-- Table structure for wk_crm_business_user_star
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_business_user_star`;
CREATE TABLE `wk_crm_business_user_star`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `business_id` int(11) NOT NULL COMMENT '客户id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id`, `business_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户商机标星关系表 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_business_user_star
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_contacts
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_contacts`;
CREATE TABLE `wk_crm_contacts`  (
  `contacts_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人名称',
  `next_time` datetime(0) NULL DEFAULT NULL COMMENT '下次联系时间',
  `mobile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '手机',
  `telephone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '电话',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `post` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职务',
  `customer_id` int(11) NOT NULL COMMENT '客户ID',
  `address` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '地址',
  `remark` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '备注',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `owner_user_id` bigint(20) NULL DEFAULT NULL COMMENT '负责人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `batch_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '批次',
  `last_time` datetime(0) NULL DEFAULT NULL COMMENT '最后跟进时间',
  PRIMARY KEY (`contacts_id`) USING BTREE,
  INDEX `owner_user_id`(`owner_user_id`) USING BTREE,
  INDEX `customer_id`(`customer_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '联系人表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_contacts
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_contacts_business
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_contacts_business`;
CREATE TABLE `wk_crm_contacts_business`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `business_id` int(11) NOT NULL,
  `contacts_id` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商机联系人关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_contacts_business
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_contacts_data
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_contacts_data`;
CREATE TABLE `wk_crm_contacts_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NOT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '联系人扩展字段数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_contacts_data
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_contacts_user_star
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_contacts_user_star`;
CREATE TABLE `wk_crm_contacts_user_star`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `contacts_id` int(11) NOT NULL COMMENT '客户id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id`, `contacts_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户联系人标星关系表 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_contacts_user_star
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_contract
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_contract`;
CREATE TABLE `wk_crm_contract`  (
  `contract_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合同名称',
  `customer_id` int(11) NULL DEFAULT NULL COMMENT '客户ID',
  `business_id` int(11) NULL DEFAULT NULL COMMENT '商机ID',
  `check_status` int(4) NOT NULL DEFAULT 0 COMMENT '0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交 6 创建 7 已删除 8 作废',
  `examine_record_id` int(11) NULL DEFAULT NULL COMMENT '审核记录ID',
  `order_date` datetime(0) NULL DEFAULT NULL COMMENT '下单日期',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `owner_user_id` bigint(20) NULL DEFAULT NULL COMMENT '负责人ID',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `num` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合同编号',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `money` decimal(18, 2) NULL DEFAULT NULL COMMENT '合同金额',
  `discount_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '整单折扣',
  `total_price` decimal(17, 2) NULL DEFAULT NULL COMMENT '产品总金额',
  `types` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合同类型',
  `payment_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '付款方式',
  `batch_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '批次 比如附件批次',
  `ro_user_id` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '只读权限',
  `rw_user_id` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '读写权限',
  `contacts_id` int(11) NULL DEFAULT NULL COMMENT '客户签约人（联系人id）',
  `remark` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  `company_user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司签约人',
  `last_time` datetime(0) NULL DEFAULT NULL COMMENT '最后跟进时间',
  `received_money` decimal(17, 2) NULL DEFAULT 0.00,
  `unreceived_money` decimal(17, 2) NULL DEFAULT NULL,
  `old_contract_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`contract_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '合同表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_contract
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_contract_data
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_contract_data`;
CREATE TABLE `wk_crm_contract_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NOT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '合同扩展字段数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_contract_data
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_contract_product
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_contract_product`;
CREATE TABLE `wk_crm_contract_product`  (
  `r_id` int(11) NOT NULL AUTO_INCREMENT,
  `contract_id` int(11) NOT NULL COMMENT '合同ID',
  `product_id` int(11) NOT NULL COMMENT '产品ID',
  `price` decimal(18, 2) NOT NULL COMMENT '产品单价',
  `sales_price` decimal(18, 2) NOT NULL COMMENT '销售价格',
  `num` decimal(10, 2) NOT NULL COMMENT '数量',
  `discount` decimal(18, 4) NOT NULL COMMENT '折扣',
  `subtotal` decimal(18, 2) NOT NULL COMMENT '小计（折扣后价格）',
  `unit` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '单位',
  PRIMARY KEY (`r_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '合同产品关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_contract_product
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_customer
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_customer`;
CREATE TABLE `wk_crm_customer`  (
  `customer_id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '客户名称',
  `followup` int(11) NULL DEFAULT NULL COMMENT '跟进状态 0未跟进1已跟进',
  `is_lock` int(1) NOT NULL DEFAULT 0 COMMENT '1锁定',
  `next_time` datetime(0) NULL DEFAULT NULL COMMENT '下次联系时间',
  `deal_status` int(4) NULL DEFAULT 0 COMMENT '成交状态 0 未成交 1 已成交',
  `deal_time` datetime(0) NULL DEFAULT NULL COMMENT '成交时间',
  `contacts_id` int(11) NULL DEFAULT NULL COMMENT '首要联系人ID',
  `mobile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机',
  `telephone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '电话',
  `website` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '网址',
  `email` varchar(225) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `remark` varchar(3000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '备注',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `owner_user_id` bigint(20) NULL DEFAULT NULL COMMENT '负责人ID',
  `ro_user_id` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '只读权限',
  `rw_user_id` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '读写权限',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '省市区',
  `location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '定位信息',
  `detail_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '详细地址',
  `lng` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '地理位置经度',
  `lat` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '地理位置维度',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `batch_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '批次 比如附件批次',
  `status` int(1) NULL DEFAULT 1 COMMENT '客户状态 1 正常 2锁定 3删除',
  `last_time` datetime(0) NULL DEFAULT NULL COMMENT '最后跟进时间',
  `pool_time` datetime(0) NULL DEFAULT NULL COMMENT '放入公海时间',
  `is_receive` int(1) NULL DEFAULT NULL COMMENT '1 分配 2 领取',
  `last_content` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后一条跟进记录',
  `receive_time` datetime(0) NULL DEFAULT NULL COMMENT '接收到客户时间',
  `pre_owner_user_id` bigint(20) NULL DEFAULT NULL COMMENT '进入公海前负责人id',
  PRIMARY KEY (`customer_id`) USING BTREE,
  INDEX `update_time`(`update_time`) USING BTREE,
  INDEX `owner_user_id`(`owner_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '客户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_customer
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_customer_data
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_customer_data`;
CREATE TABLE `wk_crm_customer_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NOT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '客户扩展字段数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_customer_data
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_customer_pool
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_customer_pool`;
CREATE TABLE `wk_crm_customer_pool`  (
  `pool_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '公海id',
  `pool_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公海名称',
  `admin_user_id` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '管理员 “,”分割',
  `member_user_id` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '公海规则员工成员 “,”分割',
  `member_dept_id` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '公海规则部门成员 “,”分割',
  `status` int(2) NOT NULL DEFAULT 1 COMMENT '状态 0 停用 1启用',
  `pre_owner_setting` int(2) NOT NULL COMMENT '前负责人领取规则 0不限制 1限制',
  `pre_owner_setting_day` int(4) NULL DEFAULT NULL COMMENT '前负责人领取规则限制天数',
  `receive_setting` int(2) NOT NULL COMMENT '是否限制领取频率 0不限制 1限制',
  `receive_num` int(4) NULL DEFAULT NULL COMMENT '领取频率规则',
  `remind_setting` int(2) NOT NULL COMMENT '是否设置提前提醒 0不开启 1开启',
  `remind_day` int(11) NULL DEFAULT NULL COMMENT '提醒规则天数',
  `put_in_rule` int(2) NOT NULL COMMENT '收回规则 0不自动收回 1自动收回',
  `create_user_id` bigint(20) NOT NULL,
  `create_time` datetime(0) NOT NULL,
  PRIMARY KEY (`pool_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34553 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公海表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_customer_pool
-- ----------------------------
INSERT INTO `wk_crm_customer_pool` VALUES (34552, '系统默认公海', '14773', '14773', '', 1, 0, NULL, 0, NULL, 0, NULL, 0, 0, '2019-06-30 18:13:08');

-- ----------------------------
-- Table structure for wk_crm_customer_pool_field_setting
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_customer_pool_field_setting`;
CREATE TABLE `wk_crm_customer_pool_field_setting`  (
  `setting_id` int(11) NOT NULL AUTO_INCREMENT,
  `pool_id` int(11) NOT NULL COMMENT '公海id',
  `field_id` int(11) NULL DEFAULT NULL COMMENT '字段id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段中文名称',
  `field_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `type` int(2) NOT NULL COMMENT '字段类型 1 单行文本 2 多行文本 3 单选 4日期 5 数字 6 小数 7 手机  8 文件 9 多选 10 人员 11 附件 12 部门 13 日期时间 14 邮箱 15客户 16 商机 17 联系人 18 地图 19 产品类型 20 合同 21 回款计划',
  `is_hidden` int(2) NOT NULL DEFAULT 0 COMMENT '是否隐藏 0不隐藏 1隐藏',
  PRIMARY KEY (`setting_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 439856 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公海列表页字段设置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_customer_pool_field_setting
-- ----------------------------
INSERT INTO `wk_crm_customer_pool_field_setting` VALUES (439842, 34552, 1101827, '客户名称', 'customerName', 1, 0);
INSERT INTO `wk_crm_customer_pool_field_setting` VALUES (439843, 34552, 1101829, '手机', 'mobile', 7, 0);
INSERT INTO `wk_crm_customer_pool_field_setting` VALUES (439844, 34552, 1101830, '电话', 'telephone', 1, 0);
INSERT INTO `wk_crm_customer_pool_field_setting` VALUES (439845, 34552, 1101831, '网址', 'website', 1, 0);
INSERT INTO `wk_crm_customer_pool_field_setting` VALUES (439846, 34552, 1101834, '下次联系时间', 'nextTime', 13, 0);
INSERT INTO `wk_crm_customer_pool_field_setting` VALUES (439847, 34552, 1101835, '备注', 'remark', 1, 0);
INSERT INTO `wk_crm_customer_pool_field_setting` VALUES (439848, 34552, 1101833, '客户级别', 'level', 3, 0);
INSERT INTO `wk_crm_customer_pool_field_setting` VALUES (439849, 34552, 1101828, '客户来源', 'source', 3, 0);
INSERT INTO `wk_crm_customer_pool_field_setting` VALUES (439850, 34552, 1101832, '客户行业', 'industry', 3, 0);
INSERT INTO `wk_crm_customer_pool_field_setting` VALUES (439851, 34552, NULL, '成交状态', 'dealStatus', 3, 0);
INSERT INTO `wk_crm_customer_pool_field_setting` VALUES (439852, 34552, NULL, '最后跟进时间', 'lastTime', 4, 0);
INSERT INTO `wk_crm_customer_pool_field_setting` VALUES (439853, 34552, NULL, '更新时间', 'updateTime', 4, 0);
INSERT INTO `wk_crm_customer_pool_field_setting` VALUES (439854, 34552, NULL, '创建时间', 'createTime', 4, 0);
INSERT INTO `wk_crm_customer_pool_field_setting` VALUES (439855, 34552, NULL, '创建人', 'createUserName', 1, 0);

-- ----------------------------
-- Table structure for wk_crm_customer_pool_field_sort
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_customer_pool_field_sort`;
CREATE TABLE `wk_crm_customer_pool_field_sort`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pool_id` int(11) NOT NULL COMMENT '公海id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `field_id` int(11) NULL DEFAULT NULL COMMENT '字段id',
  `field_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段中文名称',
  `type` int(2) NOT NULL COMMENT '字段类型 1 单行文本 2 多行文本 3 单选 4日期 5 数字 6 小数 7 手机  8 文件 9 多选 10 人员 11 附件 12 部门 13 日期时间 14 邮箱 15客户 16 商机 17 联系人 18 地图 19 产品类型 20 合同 21 回款计划',
  `sort` int(5) NOT NULL COMMENT '字段排序',
  `is_hidden` int(1) NOT NULL COMMENT '是否隐藏 0、不隐藏 1、隐藏',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公海列表页字段排序表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_customer_pool_field_sort
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_customer_pool_field_style
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_customer_pool_field_style`;
CREATE TABLE `wk_crm_customer_pool_field_style`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pool_id` int(11) NOT NULL COMMENT '公海id',
  `style` int(5) NOT NULL COMMENT '字段宽度',
  `field_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公海列表页字段样式表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_customer_pool_field_style
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_customer_pool_relation
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_customer_pool_relation`;
CREATE TABLE `wk_crm_customer_pool_relation`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) NOT NULL COMMENT '客户id',
  `pool_id` int(11) NOT NULL COMMENT '公海id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `pool_id`(`pool_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '客户公海关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_customer_pool_relation
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_customer_pool_rule
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_customer_pool_rule`;
CREATE TABLE `wk_crm_customer_pool_rule`  (
  `rule_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '收回规则id',
  `pool_id` int(11) NOT NULL COMMENT '公海id',
  `type` int(2) NOT NULL COMMENT '收回规则判断类型 1跟进记录 2商机 3成交状态',
  `deal_handle` int(2) NULL DEFAULT NULL COMMENT '已成交客户是否进入公海 0不进入 1进入',
  `business_handle` int(2) NULL DEFAULT NULL COMMENT '有商机客户是否进入公海 0不进入 1进入',
  `customer_level_setting` int(2) NOT NULL COMMENT '客户级别设置 1全部 2根据级别分别设置',
  `level` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户级别 1全部',
  `limit_day` int(4) NOT NULL COMMENT '公海规则限制天数',
  PRIMARY KEY (`rule_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公海收回规则表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_customer_pool_rule
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_customer_setting
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_customer_setting`;
CREATE TABLE `wk_crm_customer_setting`  (
  `setting_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `setting_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则名称',
  `customer_num` int(11) NULL DEFAULT NULL COMMENT '可拥有客户数量',
  `customer_deal` int(1) NULL DEFAULT 0 COMMENT '成交客户是否占用数量 0 不占用 1 占用',
  `type` int(1) NULL DEFAULT NULL COMMENT '类型 1 拥有客户数限制 2 锁定客户数限制',
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`setting_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '员工拥有以及锁定客户数限制' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_customer_setting
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_customer_setting_user
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_customer_setting_user`;
CREATE TABLE `wk_crm_customer_setting_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `setting_id` int(11) NOT NULL COMMENT '客户规则限制ID',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `type` int(1) NULL DEFAULT NULL COMMENT '1 员工 2 部门',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '员工拥有以及锁定客户员工关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_customer_setting_user
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_customer_stats_2021
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_customer_stats_2021`;
CREATE TABLE `wk_crm_customer_stats_2021`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `customer_num` bigint(20) NULL DEFAULT NULL COMMENT '客户数量',
  `deal_status` int(2) NULL DEFAULT NULL COMMENT '成交状态 0 未成交 1 已成交',
  `owner_user_id` int(11) NULL DEFAULT NULL COMMENT '负责人ID',
  `create_date` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间 年月日',
  `deal_date` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间 年月日',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id`(`customer_num`, `deal_status`, `owner_user_id`, `create_date`, `deal_date`) USING BTREE,
  INDEX `create_date`(`create_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_customer_stats_2021
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_customer_stats_info
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_customer_stats_info`;
CREATE TABLE `wk_crm_customer_stats_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `last_customer_id` int(11) NOT NULL COMMENT '最后同步客户ID',
  `create_time` datetime(0) NOT NULL COMMENT '同步时间',
  `sync_num` int(11) NULL DEFAULT NULL COMMENT '同步数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 130 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '客户数量统计汇总表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_customer_stats_info
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_customer_user_star
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_customer_user_star`;
CREATE TABLE `wk_crm_customer_user_star`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `customer_id` int(11) NOT NULL COMMENT '客户id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id`, `customer_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户客户标星关系表 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_customer_user_star
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_examine
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_examine`;
CREATE TABLE `wk_crm_examine`  (
  `examine_id` int(11) NOT NULL AUTO_INCREMENT,
  `category_type` int(1) NOT NULL DEFAULT 1 COMMENT '1 合同 2 回款 3发票 4薪资 5 采购审核 6采购退货审核 7销售审核 8 销售退货审核 9付款单审核10 回款单审核11盘点审核12调拨审核',
  `examine_type` int(1) NULL DEFAULT NULL COMMENT '审核类型 1 固定审批 2 授权审批',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批流名称',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标',
  `dept_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '部门ID（0为全部）',
  `user_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '员工ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `update_user_id` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
  `status` int(1) NULL DEFAULT NULL COMMENT '状态 1 启用 0 禁用 2 删除',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程说明',
  PRIMARY KEY (`examine_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25378 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '审批流程表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_examine
-- ----------------------------
INSERT INTO `wk_crm_examine` VALUES (25375, 2, 2, '回款审批流程', NULL, NULL, NULL, NULL, 3, NULL, 3, 1, '');
INSERT INTO `wk_crm_examine` VALUES (25376, 1, 2, '合同审批流程', NULL, NULL, NULL, NULL, 3, NULL, 3, 1, '说明');
INSERT INTO `wk_crm_examine` VALUES (25377, 3, 2, '发票审批流程', NULL, NULL, NULL, NULL, 0, NULL, 0, 1, '');

-- ----------------------------
-- Table structure for wk_crm_examine_log
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_examine_log`;
CREATE TABLE `wk_crm_examine_log`  (
  `log_id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `record_id` int(11) NULL DEFAULT NULL COMMENT '审批记录ID',
  `examine_step_id` bigint(20) NULL DEFAULT NULL COMMENT '审核步骤ID',
  `examine_status` int(1) NULL DEFAULT NULL COMMENT '审核状态 0 未审核 1 审核通过 2 审核拒绝3 撤回审核',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `examine_user` bigint(20) NULL DEFAULT NULL COMMENT '审核人',
  `examine_time` datetime(0) NULL DEFAULT NULL COMMENT '审核时间',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核备注',
  `is_recheck` int(1) NULL DEFAULT 0 COMMENT '是否是撤回之前的日志 0或者null为新数据 1：撤回之前的数据',
  `order_id` int(30) NULL DEFAULT NULL,
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '审核日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_examine_log
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_examine_record
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_examine_record`;
CREATE TABLE `wk_crm_examine_record`  (
  `record_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '审核记录ID',
  `examine_id` int(11) NULL DEFAULT NULL COMMENT '审批ID',
  `examine_step_id` bigint(20) NULL DEFAULT NULL COMMENT '当前进行的审批步骤ID',
  `examine_status` int(1) NULL DEFAULT NULL COMMENT '审核状态 0 未审核 1 审核通过 2 审核拒绝 3 审核中 4 已撤回',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核备注',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '审核记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_examine_record
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_examine_step
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_examine_step`;
CREATE TABLE `wk_crm_examine_step`  (
  `step_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `step_type` int(1) NULL DEFAULT NULL COMMENT '步骤类型1、负责人主管，2、指定用户（任意一人），3、指定用户（多人会签）,4、上一级审批人主管',
  `examine_id` int(11) NOT NULL COMMENT '审批ID',
  `check_user_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批人ID (使用逗号隔开) ,1,2,',
  `step_num` int(2) NULL DEFAULT 1 COMMENT '排序',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`step_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '审批步骤表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_examine_step
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_field
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_field`;
CREATE TABLE `wk_crm_field`  (
  `field_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `field_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '自定义字段英文标识',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '字段名称',
  `type` int(2) NOT NULL DEFAULT 1 COMMENT '字段类型 1 单行文本 2 多行文本 3 单选 4日期 5 数字 6 小数 7 手机  8 文件 9 多选 10 人员 11 附件 12 部门 13 日期时间 14 邮箱 15客户 16 商机 17 联系人 18 地图 19 产品类型 20 合同 21 回款计划',
  `label` int(2) NOT NULL COMMENT '标签 1 线索 2 客户 3 联系人 4 产品 5 商机 6 合同 7回款8.回款计划',
  `remark` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段说明',
  `input_tips` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '输入提示',
  `max_length` int(12) NULL DEFAULT NULL COMMENT '最大长度',
  `default_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '默认值',
  `is_unique` int(1) NULL DEFAULT 0 COMMENT '是否唯一 1 是 0 否',
  `is_null` int(1) NULL DEFAULT 0 COMMENT '是否必填 1 是 0 否',
  `sorting` int(5) NULL DEFAULT 1 COMMENT '排序 从小到大',
  `options` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '如果类型是选项，此处不能为空，多个选项以，隔开',
  `operating` int(1) NULL DEFAULT 0 COMMENT '是否可以删除修改 0 改删 1 改 2 删 3 无 4 不可修改必填',
  `is_hidden` int(1) NOT NULL DEFAULT 0 COMMENT '是否隐藏  0不隐藏 1隐藏',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后修改时间',
  `field_type` int(2) NOT NULL DEFAULT 0 COMMENT '字段来源  0.自定义 1.原始固定 2原始字段但值存在扩展表中',
  `relevant` int(11) NULL DEFAULT NULL COMMENT '只有线索需要，转换客户的自定义字段ID',
  PRIMARY KEY (`field_id`) USING BTREE,
  INDEX `label`(`label`) USING BTREE,
  INDEX `update_time`(`update_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1101904 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '自定义字段表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_field
-- ----------------------------
INSERT INTO `wk_crm_field` VALUES (1101827, 'customer_name', '客户名称', 1, 2, NULL, NULL, 255, '', 1, 1, 0, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101828, 'source', '客户来源', 3, 2, NULL, NULL, NULL, '', 0, 0, 1, '促销,搜索引擎,广告,转介绍,线上注册,线上询价,预约上门,陌拜,电话咨询,邮件咨询', 1, 0, NULL, 2, NULL);
INSERT INTO `wk_crm_field` VALUES (1101829, 'mobile', '手机', 7, 2, NULL, NULL, 255, '', 0, 0, 2, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101830, 'telephone', '电话', 1, 2, NULL, NULL, 255, '', 0, 0, 3, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101831, 'website', '网址', 1, 2, NULL, NULL, 255, '', 0, 0, 4, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101832, 'industry', '客户行业', 3, 2, NULL, NULL, NULL, '', 0, 0, 5, 'IT,金融业,房地产,商业服务,运输/物流,生产,政府,文化传媒', 1, 0, NULL, 2, NULL);
INSERT INTO `wk_crm_field` VALUES (1101833, 'level', '客户级别', 3, 2, NULL, NULL, NULL, '', 0, 0, 6, 'A（重点客户）,B（普通客户）,C（非优先客户）', 1, 0, NULL, 2, NULL);
INSERT INTO `wk_crm_field` VALUES (1101834, 'next_time', '下次联系时间', 13, 2, NULL, NULL, NULL, '', 0, 0, 7, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101835, 'remark', '备注', 2, 2, NULL, NULL, 255, '', 0, 0, 8, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101836, 'email', '邮箱', 14, 2, NULL, NULL, 255, '', 0, 0, 4, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101837, 'leads_name', '线索名称', 1, 1, NULL, NULL, 255, '', 0, 1, 0, NULL, 1, 0, '2020-08-22 15:33:40', 1, 1101827);
INSERT INTO `wk_crm_field` VALUES (1101838, 'email', '邮箱', 14, 1, NULL, NULL, 255, '', 0, 0, 1, NULL, 1, 0, '2020-08-22 15:33:40', 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101839, 'source', '线索来源', 3, 1, NULL, NULL, NULL, '', 0, 0, 2, '促销,搜索引擎,广告,转介绍,线上注册,线上询价,预约上门,陌拜,电话咨询,邮件咨询', 1, 0, '2020-08-22 15:33:40', 2, 1101828);
INSERT INTO `wk_crm_field` VALUES (1101840, 'mobile', '手机', 7, 1, NULL, NULL, 255, '', 0, 0, 3, NULL, 1, 0, '2020-08-22 15:33:40', 1, 1101829);
INSERT INTO `wk_crm_field` VALUES (1101841, 'telephone', '电话', 1, 1, NULL, NULL, 255, '', 0, 0, 4, NULL, 1, 0, '2020-08-22 15:33:40', 1, 1101830);
INSERT INTO `wk_crm_field` VALUES (1101842, 'address', '地址', 1, 1, NULL, NULL, 255, '', 0, 0, 5, NULL, 1, 0, '2020-08-22 15:33:40', 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101843, 'industry', '客户行业', 3, 1, NULL, NULL, NULL, '', 0, 0, 6, 'IT,金融业,房地产,商业服务,运输/物流,生产,政府,文化传媒', 1, 0, '2020-08-22 15:33:40', 2, 1101832);
INSERT INTO `wk_crm_field` VALUES (1101844, 'level', '客户级别', 3, 1, NULL, NULL, NULL, '', 0, 0, 7, 'A（重点客户）,B（普通客户）,C（非优先客户）', 1, 0, '2020-08-22 15:33:40', 2, 1101833);
INSERT INTO `wk_crm_field` VALUES (1101845, 'next_time', '下次联系时间', 13, 1, NULL, NULL, NULL, '', 0, 0, 8, NULL, 1, 0, '2020-08-22 15:33:40', 1, 1101834);
INSERT INTO `wk_crm_field` VALUES (1101846, 'remark', '备注', 2, 1, NULL, NULL, 255, '', 0, 0, 9, NULL, 1, 0, '2020-08-22 15:33:40', 1, 1101835);
INSERT INTO `wk_crm_field` VALUES (1101847, 'name', '姓名', 1, 3, NULL, NULL, 255, '', 0, 1, 0, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101848, 'customer_id', '客户名称', 15, 3, NULL, NULL, NULL, '', 0, 1, 1, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101849, 'mobile', '手机', 7, 3, NULL, NULL, 255, '', 0, 0, 2, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101850, 'telephone', '电话', 1, 3, NULL, NULL, 255, '', 0, 0, 3, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101851, 'email', '邮箱', 14, 3, NULL, NULL, 255, '', 0, 0, 4, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101852, 'post', '职务', 1, 3, NULL, NULL, 255, '', 0, 0, 5, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101853, 'policymakers', '是否关键决策人', 3, 3, NULL, NULL, NULL, '', 0, 0, 6, '是,否', 1, 0, NULL, 2, NULL);
INSERT INTO `wk_crm_field` VALUES (1101854, 'address', '地址', 1, 3, NULL, NULL, 255, '', 0, 0, 7, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101855, 'next_time', '下次联系时间', 13, 3, NULL, NULL, NULL, '', 0, 0, 8, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101856, 'remark', '备注', 2, 3, NULL, NULL, 255, '', 0, 0, 9, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101857, 'sex', '性别', 3, 3, NULL, NULL, NULL, '', 0, 0, 10, '男,女', 1, 0, NULL, 2, NULL);
INSERT INTO `wk_crm_field` VALUES (1101858, 'name', '产品名称', 1, 4, NULL, NULL, 255, '', 0, 1, 0, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101859, 'category_id', '产品类型', 19, 4, NULL, NULL, NULL, '', 0, 1, 1, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101860, 'unit', '产品单位', 3, 4, NULL, NULL, NULL, '', 0, 0, 2, '个,块,只,把,枚,瓶,盒,台,吨,千克,米,箱,套', 1, 0, NULL, 2, NULL);
INSERT INTO `wk_crm_field` VALUES (1101861, 'num', '产品编码', 1, 4, NULL, NULL, 255, '', 1, 1, 3, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101862, 'price', '价格', 6, 4, NULL, NULL, 255, '', 0, 1, 4, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101863, 'description', '产品描述', 1, 4, NULL, NULL, 255, '', 0, 0, 6, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101864, 'business_name', '商机名称', 1, 5, NULL, NULL, 255, '', 0, 1, 0, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101865, 'customer_id', '客户名称', 15, 5, NULL, NULL, NULL, '', 0, 1, 1, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101866, 'money', '商机金额', 6, 5, NULL, NULL, 255, '', 0, 0, 2, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101867, 'deal_date', '预计成交日期', 13, 5, NULL, NULL, NULL, '', 0, 0, 3, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101868, 'remark', '备注', 2, 5, NULL, NULL, 255, '', 0, 0, 4, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101869, 'name', '合同名称', 1, 6, NULL, NULL, 255, '', 0, 1, 1, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101870, 'num', '合同编号', 1, 6, NULL, NULL, 255, '', 1, 1, 0, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101871, 'customer_id', '客户名称', 15, 6, NULL, NULL, NULL, '', 0, 1, 2, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101872, 'business_id', '商机名称', 16, 6, NULL, NULL, NULL, '', 0, 0, 3, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101873, 'money', '合同金额', 6, 6, NULL, NULL, 255, '', 0, 1, 4, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101874, 'order_date', '下单时间', 4, 6, NULL, NULL, NULL, '', 0, 1, 5, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101875, 'start_time', '合同开始时间', 4, 6, NULL, NULL, NULL, '', 0, 0, 6, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101876, 'end_time', '合同结束时间', 4, 6, NULL, NULL, NULL, '', 0, 0, 7, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101877, 'contacts_id', '客户签约人', 17, 6, NULL, NULL, NULL, '', 0, 0, 8, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101878, 'company_user_id', '公司签约人', 10, 6, NULL, NULL, NULL, '', 0, 0, 9, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101879, 'remark', '备注', 2, 6, NULL, NULL, 255, '', 0, 0, 10, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101880, 'flied_xucqai', '合同类型', 3, 6, NULL, NULL, 255, '', 0, 0, 11, '直销合同,代理合同,服务合同,快销合同', 0, 0, NULL, 0, NULL);
INSERT INTO `wk_crm_field` VALUES (1101881, 'number', '回款编号', 1, 7, NULL, NULL, 255, '', 1, 1, 0, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101882, 'customer_id', '客户名称', 15, 7, NULL, NULL, NULL, '', 0, 1, 1, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101883, 'contract_id', '合同编号', 20, 7, NULL, NULL, NULL, '', 0, 1, 2, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101884, 'plan_id', '期数', 21, 7, NULL, NULL, NULL, '', 0, 0, 3, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101885, 'return_time', '回款日期', 4, 7, NULL, NULL, NULL, '', 0, 1, 4, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101886, 'money', '回款金额', 6, 7, NULL, NULL, 255, '', 0, 1, 5, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101887, 'return_type', '回款方式', 3, 7, NULL, NULL, NULL, '', 0, 0, 6, '支票,现金,邮政汇款,电汇,网上转账,支付宝,微信支付,其他', 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101888, 'remark', '备注', 2, 7, NULL, NULL, 255, '', 0, 0, 7, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101889, 'customer_id', '客户名称', 15, 8, NULL, NULL, NULL, '', 0, 0, 1, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101890, 'contract_id', '合同编号', 20, 8, NULL, NULL, 11, '', 0, 0, 2, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101891, 'money', '计划回款金额', 6, 8, NULL, NULL, NULL, '', 0, 0, 3, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101892, 'return_date', '计划回款日期', 4, 8, NULL, NULL, NULL, '', 0, 0, 4, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101893, 'remind', '提前几天提醒', 5, 8, NULL, NULL, 11, '', 0, 0, 5, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101894, 'remark', '备注', 2, 8, NULL, NULL, 1000, '', 0, 0, 6, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101895, 'visit_number', '回访编号', 1, 17, NULL, NULL, NULL, '', 1, 1, 0, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101896, 'visit_time', '回访时间', 13, 17, NULL, NULL, NULL, '', 0, 1, 1, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101897, 'owner_user_id', '回访人', 28, 17, NULL, NULL, NULL, '', 0, 1, 2, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101898, 'return_visit_type', '回访形式', 3, 17, NULL, NULL, NULL, '', 0, 0, 3, '见面拜访,电话,短信,邮件,微信', 1, 0, NULL, 2, NULL);
INSERT INTO `wk_crm_field` VALUES (1101899, 'customer_id', '客户名称', 15, 17, NULL, NULL, NULL, '', 0, 1, 4, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101900, 'contacts_id', '联系人', 17, 17, NULL, NULL, NULL, '', 0, 0, 5, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101901, 'contract_id', '合同编号', 20, 17, NULL, NULL, NULL, '', 0, 1, 6, NULL, 1, 0, NULL, 1, NULL);
INSERT INTO `wk_crm_field` VALUES (1101902, 'satisficing', '客户满意度', 3, 17, NULL, NULL, NULL, '', 0, 0, 7, '很满意,满意,一般,不满意,很不满意', 1, 0, NULL, 2, NULL);
INSERT INTO `wk_crm_field` VALUES (1101903, 'flied_itvzix', '客户反馈', 2, 17, NULL, NULL, 1000, '', 0, 0, 8, NULL, 0, 0, NULL, 0, NULL);

-- ----------------------------
-- Table structure for wk_crm_field_config
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_field_config`;
CREATE TABLE `wk_crm_field_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `field_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '字段名称',
  `field_type` int(2) NOT NULL DEFAULT 1 COMMENT '字段类型 1 keyword 2 date 3 number 4 nested 5 datetime',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `label` int(2) NOT NULL COMMENT 'label',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `field_name`(`field_name`, `label`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100294 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '字段配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_field_config
-- ----------------------------
INSERT INTO `wk_crm_field_config` VALUES (100257, 'flied_dzmbcn', 4, '2020-08-03 18:46:43', 6);
INSERT INTO `wk_crm_field_config` VALUES (100258, 'flied_ivcdhc', 4, '2020-08-03 18:47:30', 3);
INSERT INTO `wk_crm_field_config` VALUES (100259, 'flied_pyrnyn', 1, '2020-08-03 19:12:42', 1);
INSERT INTO `wk_crm_field_config` VALUES (100260, 'flied_dknbbe', 4, '2020-08-03 19:12:42', 1);
INSERT INTO `wk_crm_field_config` VALUES (100261, 'flied_jhsivt', 1, '2020-08-03 19:15:51', 2);
INSERT INTO `wk_crm_field_config` VALUES (100262, 'flied_bcethz', 3, '2020-08-04 17:17:04', 2);
INSERT INTO `wk_crm_field_config` VALUES (100263, 'flied_jeqgso', 1, '2020-08-04 17:17:04', 2);
INSERT INTO `wk_crm_field_config` VALUES (100264, 'flied_mjrdbe', 1, '2020-08-05 11:17:16', 2);
INSERT INTO `wk_crm_field_config` VALUES (100265, 'flied_mtfnrf', 1, '2020-08-05 11:17:17', 2);
INSERT INTO `wk_crm_field_config` VALUES (100266, 'flied_dlyjjb', 2, '2020-08-06 15:27:17', 1);
INSERT INTO `wk_crm_field_config` VALUES (100267, 'flied_wxpcbx', 3, '2020-08-06 15:27:18', 1);
INSERT INTO `wk_crm_field_config` VALUES (100268, 'flied_kjhmgc', 4, '2020-08-06 15:27:18', 2);
INSERT INTO `wk_crm_field_config` VALUES (100269, 'flied_gdcrxx', 2, '2020-08-06 15:27:18', 2);
INSERT INTO `wk_crm_field_config` VALUES (100270, 'flied_xfhonw', 1, '2020-08-06 15:27:18', 3);
INSERT INTO `wk_crm_field_config` VALUES (100271, 'flied_fdncyr', 2, '2020-08-06 15:27:18', 3);
INSERT INTO `wk_crm_field_config` VALUES (100272, 'flied_ijtnfc', 3, '2020-08-06 15:27:18', 3);
INSERT INTO `wk_crm_field_config` VALUES (100273, 'flied_wuggiv', 1, '2020-08-06 15:27:18', 4);
INSERT INTO `wk_crm_field_config` VALUES (100274, 'flied_mswlgq', 4, '2020-08-06 15:27:18', 4);
INSERT INTO `wk_crm_field_config` VALUES (100275, 'flied_nmkltw', 2, '2020-08-06 15:27:18', 4);
INSERT INTO `wk_crm_field_config` VALUES (100276, 'flied_jokwgt', 3, '2020-08-06 15:27:19', 4);
INSERT INTO `wk_crm_field_config` VALUES (100277, 'flied_drfhhl', 1, '2020-08-06 15:27:19', 5);
INSERT INTO `wk_crm_field_config` VALUES (100278, 'flied_uvqlpy', 4, '2020-08-06 15:27:19', 5);
INSERT INTO `wk_crm_field_config` VALUES (100279, 'flied_temgvq', 2, '2020-08-06 15:27:19', 5);
INSERT INTO `wk_crm_field_config` VALUES (100280, 'flied_lxujya', 3, '2020-08-06 15:27:19', 5);
INSERT INTO `wk_crm_field_config` VALUES (100281, 'flied_kixhfg', 1, '2020-08-06 15:27:19', 6);
INSERT INTO `wk_crm_field_config` VALUES (100282, 'flied_lzwnik', 2, '2020-08-06 15:27:19', 6);
INSERT INTO `wk_crm_field_config` VALUES (100283, 'flied_dununn', 3, '2020-08-06 15:27:19', 6);
INSERT INTO `wk_crm_field_config` VALUES (100284, 'flied_cqlfka', 1, '2020-08-06 15:27:19', 7);
INSERT INTO `wk_crm_field_config` VALUES (100285, 'flied_ylgnov', 4, '2020-08-06 15:27:19', 7);
INSERT INTO `wk_crm_field_config` VALUES (100286, 'flied_umnxvp', 2, '2020-08-06 15:27:19', 7);
INSERT INTO `wk_crm_field_config` VALUES (100287, 'flied_mhbkno', 3, '2020-08-06 15:27:19', 7);
INSERT INTO `wk_crm_field_config` VALUES (100288, 'flied_bthxmi', 1, '2020-08-06 15:27:20', 17);
INSERT INTO `wk_crm_field_config` VALUES (100289, 'flied_xqimlp', 4, '2020-08-06 15:27:20', 17);
INSERT INTO `wk_crm_field_config` VALUES (100290, 'flied_oojrlh', 2, '2020-08-06 15:27:20', 17);
INSERT INTO `wk_crm_field_config` VALUES (100291, 'flied_tmboyd', 3, '2020-08-06 15:27:20', 17);
INSERT INTO `wk_crm_field_config` VALUES (100292, 'flied_grasid', 1, '2020-08-12 18:14:51', 2);
INSERT INTO `wk_crm_field_config` VALUES (100293, 'flied_ilvojx', 1, '2020-08-19 17:17:04', 1);

-- ----------------------------
-- Table structure for wk_crm_field_sort
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_field_sort`;
CREATE TABLE `wk_crm_field_sort`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `field_id` int(11) NULL DEFAULT NULL COMMENT '字段ID',
  `field_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段名称',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `label` int(2) NOT NULL COMMENT '标签 1 线索 2 客户 3 联系人 4 产品 5 商机 6 合同 7回款8.回款计划',
  `type` int(2) NULL DEFAULT NULL COMMENT '字段类型',
  `style` int(8) NULL DEFAULT NULL COMMENT '字段宽度',
  `sort` int(5) NOT NULL DEFAULT 0 COMMENT '字段排序',
  `user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '用户id',
  `is_hide` int(1) NOT NULL DEFAULT 1 COMMENT '是否隐藏 0、不隐藏 1、隐藏',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `label`(`user_id`, `field_name`, `label`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1961 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字段排序表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_field_sort
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_instrument_sort
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_instrument_sort`;
CREATE TABLE `wk_crm_instrument_sort`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `model_id` int(11) NOT NULL COMMENT '模块id 1、合同金额目标及完成情况 2、数据汇总 3、回款金额目标及完成情况 4、业绩指标完成率 5、销售漏斗 6、遗忘提醒 7、排行榜',
  `list` int(4) NOT NULL COMMENT '列 1左侧 2右侧',
  `sort` int(4) NOT NULL COMMENT '排序',
  `is_hidden` int(4) NOT NULL DEFAULT 0 COMMENT '是否隐藏 0显示 1隐藏',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '仪表盘排序表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_instrument_sort
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_invoice
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_invoice`;
CREATE TABLE `wk_crm_invoice`  (
  `invoice_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '发票id',
  `invoice_apply_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发票申请编号',
  `customer_id` int(11) NOT NULL COMMENT '客户id',
  `contract_id` int(11) NULL DEFAULT NULL COMMENT '合同id',
  `invoice_money` decimal(10, 2) NOT NULL COMMENT '开票金额',
  `invoice_date` date NULL DEFAULT NULL COMMENT '开票日期',
  `invoice_type` int(2) NOT NULL COMMENT '开票类型',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `title_type` int(2) NULL DEFAULT NULL COMMENT '抬头类型 1单位 2个人',
  `invoice_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开票抬头',
  `tax_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '纳税识别号',
  `deposit_bank` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开户行',
  `deposit_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开户账户',
  `deposit_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开票地址',
  `telephone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话',
  `contacts_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人名称',
  `contacts_mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系方式',
  `contacts_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮寄地址',
  `examine_record_id` int(11) NULL DEFAULT NULL COMMENT '审批记录id',
  `check_status` int(2) NULL DEFAULT NULL COMMENT '审核状态 0待审核、1通过、2拒绝、3审核中、4撤回',
  `owner_user_id` bigint(20) NOT NULL COMMENT '负责人id',
  `invoice_number` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发票号码',
  `real_invoice_date` date NULL DEFAULT NULL COMMENT '实际开票日期',
  `logistics_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流单号',
  `invoice_status` int(2) NOT NULL DEFAULT 0 COMMENT '开票状态 0 未开票， 1 已开票',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '批次id',
  PRIMARY KEY (`invoice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '发票表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_invoice
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_invoice_info
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_invoice_info`;
CREATE TABLE `wk_crm_invoice_info`  (
  `info_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '发票信息id',
  `customer_id` int(11) NOT NULL COMMENT '客户id',
  `title_type` int(2) NULL DEFAULT NULL COMMENT '抬头类型 1单位 2个人',
  `invoice_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开票抬头',
  `tax_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '纳税识别号',
  `deposit_bank` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开户行',
  `deposit_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开户账户',
  `deposit_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开票地址',
  `telephone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`info_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '发票详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_invoice_info
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_leads
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_leads`;
CREATE TABLE `wk_crm_leads`  (
  `leads_id` int(11) NOT NULL AUTO_INCREMENT,
  `is_transform` int(1) NULL DEFAULT 0 COMMENT '1已转化 0 未转化',
  `followup` int(11) NULL DEFAULT NULL COMMENT '跟进状态 0未跟进1已跟进',
  `leads_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '线索名称',
  `customer_id` int(11) NULL DEFAULT NULL COMMENT '客户id',
  `next_time` datetime(0) NULL DEFAULT NULL COMMENT '下次联系时间',
  `telephone` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `mobile` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '地址',
  `remark` varchar(800) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '备注',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `owner_user_id` bigint(20) NULL DEFAULT NULL COMMENT '负责人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `batch_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '批次 比如附件批次',
  `is_receive` int(1) NULL DEFAULT NULL COMMENT '1 分配',
  `last_time` datetime(0) NULL DEFAULT NULL COMMENT '最后跟进时间',
  `last_content` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后一条跟进记录',
  PRIMARY KEY (`leads_id`) USING BTREE,
  INDEX `owner_user_id`(`owner_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '线索表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_leads
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_leads_data
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_leads_data`;
CREATE TABLE `wk_crm_leads_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NOT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '线索自定义字段存值表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_leads_data
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_leads_user_star
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_leads_user_star`;
CREATE TABLE `wk_crm_leads_user_star`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `leads_id` int(11) NOT NULL COMMENT '客户id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id`, `leads_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户线索标星关系表 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_leads_user_star
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_marketing
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_marketing`;
CREATE TABLE `wk_crm_marketing`  (
  `marketing_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '营销id',
  `marketing_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '营销名称',
  `crm_type` int(1) NOT NULL DEFAULT 1 COMMENT '1线索  2客户',
  `end_time` datetime(0) NOT NULL COMMENT '截止时间',
  `relation_user_id` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '关联人员ID',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '1启用  0禁用',
  `second` int(1) NOT NULL DEFAULT 0 COMMENT '每个客户只能填写次数 0 1',
  `field_data_id` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '营销内容填写字段',
  `browse` int(10) NULL DEFAULT 0 COMMENT '浏览数',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `share_num` int(11) NULL DEFAULT 0 COMMENT '分享数',
  `submit_num` int(11) NULL DEFAULT 0 COMMENT '提交数',
  `synopsis` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '简介',
  `main_file_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '首图id',
  `detail_file_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动地址',
  `marketing_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动类型',
  `marketing_money` decimal(11, 2) NULL DEFAULT NULL COMMENT '活动金额',
  PRIMARY KEY (`marketing_id`) USING BTREE,
  INDEX `status`(`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '营销表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_marketing
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_marketing_field
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_marketing_field`;
CREATE TABLE `wk_crm_marketing_field`  (
  `field_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `field_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '自定义字段英文标识',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '字段名称',
  `type` int(2) NOT NULL DEFAULT 1 COMMENT '字段类型 1 单行文本 2 多行文本 3 单选 4日期 5 数字 6 小数 7 手机  8 文件 9 多选 10 人员 11 附件 12 部门 13 日期时间 14 邮箱 15客户 16 商机 17 联系人 18 地图 19 产品类型 20 合同 21 回款计划',
  `remark` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段说明',
  `input_tips` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '输入提示',
  `max_length` int(12) NULL DEFAULT NULL COMMENT '最大长度',
  `default_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '默认值',
  `is_unique` int(1) NULL DEFAULT 0 COMMENT '是否唯一 1 是 0 否',
  `is_null` int(1) NULL DEFAULT 0 COMMENT '是否必填 1 是 0 否',
  `sorting` int(5) NULL DEFAULT 1 COMMENT '排序 从小到大',
  `options` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '如果类型是选项，此处不能为空，多个选项以，隔开',
  `operating` int(1) NULL DEFAULT 0 COMMENT '是否可以删除修改 0 改删 1 改 2 删 3 无',
  `is_hidden` int(1) NOT NULL DEFAULT 0 COMMENT '是否隐藏  0不隐藏 1隐藏',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后修改时间',
  `form_id` int(11) NULL DEFAULT NULL COMMENT '表单Id',
  `field_type` int(2) NOT NULL DEFAULT 0 COMMENT '字段来源  0.自定义 1.原始固定 2原始字段但值存在扩展表中',
  PRIMARY KEY (`field_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '市场活动字段表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_marketing_field
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_marketing_form
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_marketing_form`;
CREATE TABLE `wk_crm_marketing_form`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `status` int(1) NULL DEFAULT 1 COMMENT '1启用，0禁用',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int(1) NULL DEFAULT 0 COMMENT '1已删除',
  `delete_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
  `delete_user_id` bigint(20) NULL DEFAULT NULL COMMENT '删除人ID',
  `update_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '市场活动表单信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_marketing_form
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_marketing_info
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_marketing_info`;
CREATE TABLE `wk_crm_marketing_info`  (
  `r_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `marketing_id` int(11) NOT NULL COMMENT '关联ID',
  `status` int(1) NOT NULL DEFAULT 0 COMMENT '0未同步  1同步成功  2同步失败',
  `field_info` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '营销内容填写字段内容',
  `device` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '设备号',
  `owner_user_id` bigint(20) NOT NULL COMMENT '关联ID',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`r_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '营销数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_marketing_info
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_number_setting
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_number_setting`;
CREATE TABLE `wk_crm_number_setting`  (
  `setting_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '设置id',
  `pid` int(11) NOT NULL COMMENT '父级设置id',
  `sort` int(2) NOT NULL COMMENT '编号顺序',
  `type` int(2) NOT NULL COMMENT '编号类型 1文本 2日期 3数字',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文本内容或日期格式或起始编号',
  `increase_number` int(11) NULL DEFAULT NULL COMMENT '递增数',
  `reset_type` int(2) NULL DEFAULT NULL COMMENT '重新编号周期 1每天 2每月 3每年 4从不',
  `last_number` int(10) NULL DEFAULT NULL COMMENT '上次生成的编号',
  `last_date` date NULL DEFAULT NULL COMMENT '上次生成的时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  PRIMARY KEY (`setting_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统自动生成编号设置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_number_setting
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_owner_record
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_owner_record`;
CREATE TABLE `wk_crm_owner_record`  (
  `record_id` int(11) NOT NULL AUTO_INCREMENT,
  `type_id` int(11) NOT NULL COMMENT '对象id',
  `type` int(4) NOT NULL COMMENT '对象类型',
  `pre_owner_user_id` bigint(20) NULL DEFAULT NULL COMMENT '上一负责人',
  `post_owner_user_id` bigint(20) NULL DEFAULT NULL COMMENT '接手负责人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '负责人变更记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_owner_record
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_print_record
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_print_record`;
CREATE TABLE `wk_crm_print_record`  (
  `record_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '记录id',
  `crm_type` int(4) NOT NULL,
  `type_id` int(11) NOT NULL,
  `template_id` int(11) NOT NULL COMMENT '模板id',
  `record_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '打印记录',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '打印记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_print_record
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_print_template
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_print_template`;
CREATE TABLE `wk_crm_print_template`  (
  `template_id` int(11) NOT NULL AUTO_INCREMENT,
  `template_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板名称',
  `type` int(2) NOT NULL COMMENT '关联对象',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '模板',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`template_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '打印模板表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_print_template
-- ----------------------------
INSERT INTO `wk_crm_print_template` VALUES (20, '合同条款打印模板', 6, '<p style=\"text-align: center; line-height: 1; margin-bottom: 15px;\"><span style=\"font-size: 36px; font-family: simsun, serif;\">***有限公司</span></p><p style=\"text-align: center; line-height: 1; margin-bottom: 15px;\"><span style=\"font-size: 36px; font-family: simsun, serif;\">销售合同</span></p><p style=\"text-align: right;\"><span style=\"font-size: 14px; font-family: simsun, serif; color: #525151;\">合同编号：<span class=\"wk-print-tag-wukong wk-tiny-color--contract\" contenteditable=\"true\" data-wk-tag=\"contract.num\">{合同编号}</span></span></p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">甲方：<span class=\"wk-print-tag-wukong wk-tiny-color--customer\" contenteditable=\"true\" data-wk-tag=\"customer.customer_name\">{客户名称}</span><u></u></span></p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">乙方：郑州卡卡罗特软件科技有限公司</span></p><p style=\"line-height: 1.75;\">&nbsp;</p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">甲乙双方本着相互信任，真诚合作的原则，经双方友好协商，就乙方为甲方提供特定服务达成一致意见，特签订本合同。</span></p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\"><strong>一、服务内容</strong></span></p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">1、乙方同意向甲方提供的特定服务。服务的内容的标准见附件A。</span></p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">2、如果乙方在工作中因自身过错而发生任何错误或遗漏，乙方应无条件更正，而不另外收费，并对因此而对甲方造成的损失承担赔偿责任，赔偿以附件A所载明的该项服务内容对应之服务费为限。若因甲方原因造成工作的延误，将由甲方承担相应的损失。</span></p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">3、乙方的服务承诺：</span></p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">&nbsp; &nbsp; 1）乙方接到甲方通过电话、信函传真、电子邮件、网上提交等方式提出关于附件A所列服务的请求后，在两个有效工作日内给予响应并提供服务。</span></p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">&nbsp; &nbsp; 2）乙方提供给甲方的服务，必须按照合同附件A规定的标准进行。</span></p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">4、产品明细：</span></p><table style=\"border-collapse: collapse; width: 100%; float: right;\" border=\"1\" data-wk-table-tag=\"table\"><tbody><tr data-wk-table-tr-tag=\"header\"><td style=\"background-color: #ffffff;\" data-wk-table-td-tag=\"name\"><strong><span style=\"font-size: 14px; font-family: simsun, serif;\">产品名称</span></strong></td><td style=\"background-color: #ffffff;\" data-wk-table-td-tag=\"name\"><strong><span style=\"font-size: 14px; font-family: simsun, serif;\">产品类别</span></strong></td><td style=\"background-color: #ffffff;\" data-wk-table-td-tag=\"name\"><strong><span style=\"font-size: 14px; font-family: simsun, serif;\">单位</span></strong></td><td style=\"background-color: #ffffff;\" data-wk-table-td-tag=\"name\"><strong><span style=\"font-size: 14px; font-family: simsun, serif;\">价格</span></strong></td><td style=\"background-color: #ffffff;\" data-wk-table-td-tag=\"name\"><strong><span style=\"font-size: 14px; font-family: simsun, serif;\">售价</span></strong></td><td style=\"background-color: #ffffff;\" data-wk-table-td-tag=\"name\"><strong><span style=\"font-size: 14px; font-family: simsun, serif;\">数量</span></strong></td><td style=\"background-color: #ffffff;\" data-wk-table-td-tag=\"name\"><strong><span style=\"font-size: 14px; font-family: simsun, serif;\">折扣</span></strong></td><td style=\"background-color: #ffffff;\" data-wk-table-td-tag=\"name\"><strong><span style=\"font-size: 14px; font-family: simsun, serif;\">合计</span></strong></td></tr><tr data-wk-table-tr-tag=\"value\"><td data-wk-table-td-tag=\"value\"><span class=\"wk-print-tag-wukong wk-tiny-color--product\" contenteditable=\"true\" data-wk-table-value-tag=\"product.name\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{产品名称}</span></span></td><td data-wk-table-td-tag=\"value\"><span class=\"wk-print-tag-wukong wk-tiny-color--product\" contenteditable=\"true\" data-wk-table-value-tag=\"product.category_name\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{产品类别}</span></span></td><td data-wk-table-td-tag=\"value\"><span class=\"wk-print-tag-wukong wk-tiny-color--product\" contenteditable=\"true\" data-wk-table-value-tag=\"product.单位\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{单位}</span></span></td><td data-wk-table-td-tag=\"value\"><span class=\"wk-print-tag-wukong wk-tiny-color--product\" contenteditable=\"true\" data-wk-table-value-tag=\"product.price\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{价格}</span></span></td><td data-wk-table-td-tag=\"value\"><span class=\"wk-print-tag-wukong wk-tiny-color--product\" contenteditable=\"true\" data-wk-table-value-tag=\"product.sales_price\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{售价}</span></span></td><td data-wk-table-td-tag=\"value\"><span class=\"wk-print-tag-wukong wk-tiny-color--product\" contenteditable=\"true\" data-wk-table-value-tag=\"product.sales_num\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{数量}</span></span></td><td data-wk-table-td-tag=\"value\"><span class=\"wk-print-tag-wukong wk-tiny-color--product\" contenteditable=\"true\" data-wk-table-value-tag=\"product.discount\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{折扣}</span></span></td><td data-wk-table-td-tag=\"value\"><span class=\"wk-print-tag-wukong wk-tiny-color--product\" contenteditable=\"true\" data-wk-table-value-tag=\"product.subtotal\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{合计}</span></span></td></tr></tbody></table><p style=\"line-height: 1.75;\"><span style=\"font-family: simsun, serif;\"><span style=\"font-size: 14px;\">整单折扣：<span class=\"wk-print-tag-wukong wk-tiny-color--product\" contenteditable=\"true\" data-wk-tag=\"contract.discount_rate\">{整单折扣}</span></span><span style=\"font-size: 14px;\">&nbsp; &nbsp;产品总金额（元）：<span class=\"wk-print-tag-wukong wk-tiny-color--product\" contenteditable=\"true\" data-wk-tag=\"contract.total_price\">{产品总金额}</span></span></span></p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\"><strong>二、服务费的支付</strong></span></p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">1、服务费总金额为<u>&nbsp; &nbsp;&nbsp;<span class=\"wk-print-tag-wukong wk-tiny-color--contract\" contenteditable=\"true\" data-wk-tag=\"contract.money\">{合同金额}</span>&nbsp; &nbsp;</u>元人民币(人民币大写：<u>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</u>元整)。</span></p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">2、本费用结构仅限於附件A中列明的工作。如果甲方要求扩大项目范围，或因甲方改变已经议定的项目内容导致乙方需重复进行项目步骤，乙方将需要重新评估上述费用结构。</span></p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">3、甲乙双方一致同意项目服务费按一次性以人民币形式支付。服务完成后，甲方将在验收确认服务完成合格，并且乙方发出该阶段工作的费用账单及正式有效的税务发票后3个工作日内，向乙方支付约定的费用。</span></p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">4、有关发票方面的任何问题，甲方应在收到发票后及时书面通知乙方，便乙方及时作出解释或解决问题，以使甲方能按时付款。</span></p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">5、乙方将自行承担项目实施范围内合理的差旅费用。</span></p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">6、乙方同意免除项目杂费。</span></p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">7、本协议有效期为：&nbsp; <u>&nbsp; &nbsp;<span class=\"wk-print-tag-wukong wk-tiny-color--contract\" contenteditable=\"true\" data-wk-tag=\"contract.start_time\">{合同开始时间}</span></u><u>&nbsp; &nbsp; </u>&nbsp; 起&nbsp; <u>&nbsp; &nbsp;&nbsp;<span class=\"wk-print-tag-wukong wk-tiny-color--contract\" contenteditable=\"true\" data-wk-tag=\"contract.end_time\">{合同结束时间}</span></u><u>&nbsp; &nbsp; </u>&nbsp;止</span></p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\"><strong>三、服务的变更</strong></span></p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">甲方可以提前个工作日以书面形式要求变更或增加所提供的服务。该等变更最终应由双方互相商定认可，其中包括与该等变更有关的任何费用调整。</span></p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\"><strong>四、争议处理</strong></span></p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">甲乙双方如对协议条款规定的理解有异议，或者对与协议有关的事项发生争议，双方应本着友好合作的精神进行协商。协商不能解决的，任何一方可向仲裁委员会提起仲裁。</span></p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\"><strong>五、其他</strong></span></p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">1、本合同中所用的标题仅为方便而设，而不影响对本合同的解释。</span></p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">2、附件A是本合同不可分割的组成部分，与本合同具有同等法律效力。</span></p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">3、本合同未尽事宜，由甲乙双方协商后产生书面文件，作为本合同的补充条款，具备与本合同同等法律效力。</span></p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">4、对本合同内容的任何修改和变更需要，用书面形式，并经双方确认后生效。</span></p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">(以下无正文)</span></p><p style=\"line-height: 1.75;\">&nbsp;</p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">甲方（签章）&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;乙方（签章）</span></p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">代表签字：&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;代表签字：</span></p><p style=\"line-height: 1.75;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">日期：&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;日期：</span></p>', 0, '2020-08-22 11:40:42', NULL);
INSERT INTO `wk_crm_print_template` VALUES (21, '合同订单打印模板', 6, '<p style=\"text-align: center; line-height: 2;\"><span style=\"font-size: 36px; font-family: simsun, serif;\">合同订单</span></p><p style=\"line-height: 2; text-align: right;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">合同编号：<span class=\"wk-print-tag-wukong wk-tiny-color--contract\" contenteditable=\"true\" data-wk-tag=\"contract.num\">{合同编号}</span></span></p><table style=\"border-collapse: collapse; width: 102.185%; height: 147px;\" border=\"1\"><tbody><tr style=\"height: 21px;\"><td style=\"width: 50%; height: 21px;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">合同名称：</span><span class=\"wk-print-tag-wukong wk-tiny-color--contract\" contenteditable=\"true\" data-wk-tag=\"contract.name\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{合同名称}</span></span><span style=\"font-size: 14px; font-family: simsun, serif;\">&nbsp;</span></td><td style=\"width: 52.3571%; height: 21px;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">客户名称：</span><span class=\"wk-print-tag-wukong wk-tiny-color--customer\" contenteditable=\"true\" data-wk-tag=\"customer.customer_name\"><span style=\"font-family: simsun, serif; font-size: 14px;\">{客户名称}</span></span></td></tr><tr style=\"height: 21px;\"><td style=\"width: 50%; height: 21px;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">合同总金额：</span><span class=\"wk-print-tag-wukong wk-tiny-color--contract\" contenteditable=\"true\" data-wk-tag=\"contract.money\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{合同金额}</span></span></td><td style=\"width: 52.3571%; height: 21px;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">负责人：</span><span class=\"wk-print-tag-wukong wk-tiny-color--contract\" contenteditable=\"true\" data-wk-tag=\"contract.owner_user_name\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{负责人}</span></span></td></tr><tr style=\"height: 21px;\"><td style=\"width: 50%; height: 21px;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">相关商机：</span><span class=\"wk-print-tag-wukong wk-tiny-color--contract\" contenteditable=\"true\" data-wk-tag=\"contract.business_name\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{商机名称}</span></span><span style=\"font-size: 14px; font-family: simsun, serif;\">&nbsp;</span></td><td style=\"width: 52.3571%; height: 21px;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">签订时间：</span><span class=\"wk-print-tag-wukong wk-tiny-color--contract\" contenteditable=\"true\" data-wk-tag=\"contract.order_date\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{下单时间}</span></span></td></tr><tr style=\"height: 21px;\"><td style=\"width: 50%; height: 21px;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">开始时间：</span><span class=\"wk-print-tag-wukong wk-tiny-color--contract\" contenteditable=\"true\" data-wk-tag=\"contract.start_time\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{合同开始时间}</span></span></td><td style=\"width: 52.3571%; height: 21px;\"><span style=\"font-family: simsun, serif; font-size: 14px;\">结束时间：</span><span class=\"wk-print-tag-wukong wk-tiny-color--contract\" contenteditable=\"true\" data-wk-tag=\"contract.end_time\"><span style=\"font-family: simsun, serif; font-size: 14px;\">{合同结束时间}</span></span></td></tr><tr style=\"height: 21px;\"><td style=\"width: 50%; height: 21px;\"><span style=\"font-family: simsun, serif; font-size: 14px;\">备注：<span class=\"wk-print-tag-wukong wk-tiny-color--contract\" contenteditable=\"false\" data-wk-tag=\"contract.remark\">{备注}</span></span></td><td style=\"width: 52.3571%; height: 21px;\">&nbsp;</td></tr></tbody></table><p style=\"line-height: 2;\">&nbsp;</p><p style=\"line-height: 2;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">产品明细：</span></p><table style=\"border-collapse: collapse; width: 102.185%; height: 60px; line-height: 2;\" border=\"1\" data-wk-table-tag=\"table\"><tbody><tr data-wk-table-tr-tag=\"header\"><td style=\"width: 16.2207%;\" data-wk-table-td-tag=\"name\"><strong><span style=\"font-size: 14px; font-family: simsun, serif;\">产品名称</span></strong></td><td style=\"width: 16.2207%;\" data-wk-table-td-tag=\"name\"><strong><span style=\"font-size: 14px; font-family: simsun, serif;\">产品类别</span></strong></td><td style=\"width: 9.53177%;\" data-wk-table-td-tag=\"name\"><strong><span style=\"font-size: 14px; font-family: simsun, serif;\">单位</span></strong></td><td style=\"width: 13.8796%;\" data-wk-table-td-tag=\"name\"><strong><span style=\"font-size: 14px; font-family: simsun, serif;\">标准价格</span></strong></td><td style=\"width: 13.8796%;\" data-wk-table-td-tag=\"name\"><strong><span style=\"font-size: 14px; font-family: simsun, serif;\">销售单价</span></strong></td><td style=\"width: 9.699%;\" data-wk-table-td-tag=\"name\"><strong><span style=\"font-size: 14px; font-family: simsun, serif;\">数量</span></strong></td><td style=\"width: 9.699%;\" data-wk-table-td-tag=\"name\"><strong><span style=\"font-size: 14px; font-family: simsun, serif;\">折扣</span></strong></td><td style=\"width: 9.69447%;\" data-wk-table-td-tag=\"name\"><strong><span style=\"font-size: 14px; font-family: simsun, serif;\">合计</span></strong></td></tr><tr data-wk-table-tr-tag=\"value\"><td style=\"width: 16.2207%;\" data-wk-table-td-tag=\"value\"><span class=\"wk-print-tag-wukong wk-tiny-color--product\" contenteditable=\"true\" data-wk-table-value-tag=\"product.name\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{产品名称}</span></span></td><td style=\"width: 16.2207%;\" data-wk-table-td-tag=\"value\"><span class=\"wk-print-tag-wukong wk-tiny-color--product\" contenteditable=\"true\" data-wk-table-value-tag=\"product.category_name\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{产品类别}</span></span></td><td style=\"width: 9.53177%;\" data-wk-table-td-tag=\"value\"><span class=\"wk-print-tag-wukong wk-tiny-color--product\" contenteditable=\"true\" data-wk-table-value-tag=\"product.单位\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{单位}</span></span></td><td style=\"width: 13.8796%;\" data-wk-table-td-tag=\"value\"><span class=\"wk-print-tag-wukong wk-tiny-color--product\" contenteditable=\"true\" data-wk-table-value-tag=\"product.price\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{价格}</span></span></td><td style=\"width: 13.8796%;\" data-wk-table-td-tag=\"value\"><span class=\"wk-print-tag-wukong wk-tiny-color--product\" contenteditable=\"true\" data-wk-table-value-tag=\"product.sales_price\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{售价}</span></span></td><td style=\"width: 9.699%;\" data-wk-table-td-tag=\"value\"><span class=\"wk-print-tag-wukong wk-tiny-color--product\" contenteditable=\"true\" data-wk-table-value-tag=\"product.sales_num\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{数量}</span></span></td><td style=\"width: 9.699%;\" data-wk-table-td-tag=\"value\"><span class=\"wk-print-tag-wukong wk-tiny-color--product\" contenteditable=\"true\" data-wk-table-value-tag=\"product.discount\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{折扣}</span></span></td><td style=\"width: 9.69447%;\" data-wk-table-td-tag=\"value\"><span class=\"wk-print-tag-wukong wk-tiny-color--product\" contenteditable=\"true\" data-wk-table-value-tag=\"product.subtotal\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{合计}</span></span></td></tr></tbody></table><p style=\"text-align: right; line-height: 2;\"><span style=\"font-family: simsun, serif;\">产品总金额：</span><span class=\"wk-print-tag-wukong wk-tiny-color--product\" contenteditable=\"true\" data-wk-tag=\"contract.total_price\"><span style=\"font-family: simsun, serif;\">{产品总金额}</span></span></p><p>&nbsp;</p><p>&nbsp;</p>', 0, '2020-08-22 11:40:42', NULL);
INSERT INTO `wk_crm_print_template` VALUES (22, '商机打印模板', 5, '<p style=\"text-align: center; line-height: 1; margin-bottom: 15px;\"><span style=\"font-size: 36px; font-family: simsun, serif;\">***有限公司</span></p><p style=\"text-align: center; line-height: 1; margin-bottom: 15px;\"><span style=\"font-size: 36px; font-family: simsun, serif;\">商机</span></p><table style=\"border-collapse: collapse; width: 100.337%; height: 138px;\" border=\"1\"><tbody><tr style=\"height: 21px;\"><td style=\"width: 50%; height: 21px;\"><span style=\"font-family: simsun, serif; font-size: 14px;\">商机名称：</span><span class=\"wk-print-tag-wukong wk-tiny-color--business\" contenteditable=\"true\" data-wk-tag=\"business.business_name\"><span style=\"font-family: simsun, serif; font-size: 14px;\">{商机名称}</span></span></td><td style=\"width: 50%; height: 21px;\"><span style=\"font-family: simsun, serif; font-size: 14px;\">客户名称：</span><span class=\"wk-print-tag-wukong wk-tiny-color--customer\" contenteditable=\"true\" data-wk-tag=\"customer.customer_name\"><span style=\"font-family: simsun, serif; font-size: 14px;\">{客户名称}</span></span></td></tr><tr style=\"height: 21px;\"><td style=\"width: 50%; height: 21px;\"><span style=\"font-family: simsun, serif; font-size: 14px;\">商机状态组：</span><span class=\"wk-print-tag-wukong wk-tiny-color--business\" contenteditable=\"true\" data-wk-tag=\"business.type_name\"><span style=\"font-family: simsun, serif; font-size: 14px;\">{商机状态组}</span></span></td><td style=\"width: 50%; height: 21px;\"><span style=\"font-family: simsun, serif; font-size: 14px;\">商机阶段：</span><span class=\"wk-print-tag-wukong wk-tiny-color--business\" contenteditable=\"true\" data-wk-tag=\"business.status_name\"><span style=\"font-family: simsun, serif; font-size: 14px;\">{商机阶段}</span></span></td></tr><tr style=\"height: 21px;\"><td style=\"width: 50%; height: 21px;\"><span style=\"font-family: simsun, serif; font-size: 14px;\">预计成交时间：</span><span class=\"wk-print-tag-wukong wk-tiny-color--business\" contenteditable=\"true\" data-wk-tag=\"business.deal_date\"><span style=\"font-family: simsun, serif; font-size: 14px;\">{预计成交日期}</span></span></td><td style=\"width: 50%; height: 21px;\"><span style=\"font-family: simsun, serif; font-size: 14px;\">商机金额（元）：</span><span class=\"wk-print-tag-wukong wk-tiny-color--business\" contenteditable=\"true\" data-wk-tag=\"business.money\"><span style=\"font-family: simsun, serif; font-size: 14px;\">{商机金额}</span></span></td></tr><tr style=\"height: 21px;\"><td style=\"width: 50%; height: 21px;\"><span style=\"font-family: simsun, serif; font-size: 14px;\">负责人：</span><span class=\"wk-print-tag-wukong wk-tiny-color--business\" contenteditable=\"true\" data-wk-tag=\"business.owner_user_name\"><span style=\"font-family: simsun, serif; font-size: 14px;\">{负责人}</span></span></td><td style=\"width: 50%; height: 21px;\">&nbsp;</td></tr></tbody></table><p style=\"text-align: left; line-height: 2;\">&nbsp;</p><p style=\"text-align: left; line-height: 2;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">产品明细：</span></p><table style=\"border-collapse: collapse; width: 100%; height: 65px; line-height: 2;\" border=\"1\" data-wk-table-tag=\"table\"><tbody><tr style=\"height: 31px;\" data-wk-table-tr-tag=\"header\"><td style=\"height: 31px;\" data-wk-table-td-tag=\"name\"><span style=\"font-size: 14px;\"><strong><span style=\"font-family: simsun, serif;\">产品名称</span></strong></span></td><td style=\"height: 31px;\" data-wk-table-td-tag=\"name\"><span style=\"font-size: 14px;\"><strong><span style=\"font-family: simsun, serif;\">产品类型</span></strong></span></td><td style=\"height: 31px;\" data-wk-table-td-tag=\"name\"><span style=\"font-size: 14px;\"><strong><span style=\"font-family: simsun, serif;\">单位</span></strong></span></td><td style=\"height: 31px;\" data-wk-table-td-tag=\"name\"><span style=\"font-size: 14px;\"><strong><span style=\"font-family: simsun, serif;\">价格</span></strong></span></td><td style=\"height: 31px;\" data-wk-table-td-tag=\"name\"><span style=\"font-size: 14px;\"><strong><span style=\"font-family: simsun, serif;\">售价</span></strong></span></td><td style=\"height: 31px;\" data-wk-table-td-tag=\"name\"><span style=\"font-size: 14px;\"><strong><span style=\"font-family: simsun, serif;\">数量</span></strong></span></td><td style=\"height: 31px;\" data-wk-table-td-tag=\"name\"><span style=\"font-size: 14px;\"><strong><span style=\"font-family: simsun, serif;\">折扣</span></strong></span></td><td style=\"height: 31px;\" data-wk-table-td-tag=\"name\"><span style=\"font-size: 14px;\"><strong><span style=\"font-family: simsun, serif;\">合计</span></strong></span></td></tr><tr style=\"height: 38px;\" data-wk-table-tr-tag=\"value\"><td style=\"height: 34px;\" data-wk-table-td-tag=\"value\"><span class=\"wk-print-tag-wukong wk-tiny-color--product\" contenteditable=\"true\" data-wk-table-value-tag=\"product.name\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{产品名称}</span></span></td><td style=\"height: 34px;\" data-wk-table-td-tag=\"value\"><span class=\"wk-print-tag-wukong wk-tiny-color--product\" contenteditable=\"true\" data-wk-table-value-tag=\"product.category_id\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{产品类型}</span></span></td><td style=\"height: 34px;\" data-wk-table-td-tag=\"value\"><span class=\"wk-print-tag-wukong wk-tiny-color--product\" contenteditable=\"true\" data-wk-table-value-tag=\"product.单位\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{单位}</span></span></td><td style=\"height: 34px;\" data-wk-table-td-tag=\"value\"><span class=\"wk-print-tag-wukong wk-tiny-color--product\" contenteditable=\"true\" data-wk-table-value-tag=\"product.price\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{价格}</span></span></td><td style=\"height: 34px;\" data-wk-table-td-tag=\"value\"><span class=\"wk-print-tag-wukong wk-tiny-color--product\" contenteditable=\"true\" data-wk-table-value-tag=\"product.sales_price\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{售价}</span></span></td><td style=\"height: 34px;\" data-wk-table-td-tag=\"value\"><span class=\"wk-print-tag-wukong wk-tiny-color--product\" contenteditable=\"true\" data-wk-table-value-tag=\"product.sales_num\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{数量}</span></span></td><td style=\"height: 34px;\" data-wk-table-td-tag=\"value\"><span class=\"wk-print-tag-wukong wk-tiny-color--product\" contenteditable=\"true\" data-wk-table-value-tag=\"product.discount\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{折扣}</span></span></td><td style=\"height: 34px;\" data-wk-table-td-tag=\"value\"><span class=\"wk-print-tag-wukong wk-tiny-color--product\" contenteditable=\"true\" data-wk-table-value-tag=\"product.subtotal\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{合计}</span></span></td></tr></tbody></table><p style=\"text-align: left; line-height: 2;\"><span style=\"font-size: 14px; font-family: simsun, serif;\">整单折扣：</span><span class=\"wk-print-tag-wukong wk-tiny-color--product\" contenteditable=\"true\" data-wk-tag=\"business.discount_rate\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{整单折扣}</span></span><span style=\"font-size: 14px; font-family: simsun, serif;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;产品总金额（元）：</span><span class=\"wk-print-tag-wukong wk-tiny-color--product\" contenteditable=\"true\" data-wk-tag=\"business.total_price\"><span style=\"font-size: 14px; font-family: simsun, serif;\">{产品总金额}</span></span></p>', 0, '2020-08-22 11:40:42', NULL);
INSERT INTO `wk_crm_print_template` VALUES (23, '回款打印模板', 7, '<p style=\"text-align: center; line-height: 1; margin-bottom: 15px;\"><span style=\"font-size: 36px; font-family: simsun, serif;\">***有限公司</span></p><p style=\"text-align: center; line-height: 1; margin-bottom: 15px;\"><span style=\"font-size: 36px; font-family: simsun, serif;\">回款单</span></p><table style=\"border-collapse: collapse; width: 100.842%; height: 146px;\" border=\"1\"><tbody><tr style=\"height: 18px;\"><td style=\"width: 50%; height: 18px;\"><span style=\"font-family: simsun, serif; font-size: 14px;\">客户名称：</span><span class=\"wk-print-tag-wukong wk-tiny-color--receivables\" contenteditable=\"true\" data-wk-tag=\"receivables.customer_name\"><span style=\"font-family: simsun, serif; font-size: 14px;\">{客户名称}</span></span></td><td style=\"width: 50%; height: 18px;\"><span style=\"font-family: simsun, serif; font-size: 14px;\">回款编号：</span><span class=\"wk-print-tag-wukong wk-tiny-color--receivables\" contenteditable=\"true\" data-wk-tag=\"receivables.number\"><span style=\"font-family: simsun, serif; font-size: 14px;\">{回款编号}</span></span></td></tr><tr style=\"height: 18px;\"><td style=\"width: 50%; height: 18px;\"><span style=\"font-family: simsun, serif; font-size: 14px;\">回款日期：</span><span class=\"wk-print-tag-wukong wk-tiny-color--receivables\" contenteditable=\"true\" data-wk-tag=\"receivables.return_time\"><span style=\"font-family: simsun, serif; font-size: 14px;\">{回款日期}</span></span></td><td style=\"width: 50%; height: 18px;\"><span style=\"font-family: simsun, serif; font-size: 14px;\">回款方式：</span><span class=\"wk-print-tag-wukong wk-tiny-color--receivables\" contenteditable=\"true\" data-wk-tag=\"receivables.return_type\"><span style=\"font-family: simsun, serif; font-size: 14px;\">{回款方式}</span></span></td></tr><tr style=\"height: 18px;\"><td style=\"width: 50%; height: 18px;\"><span style=\"font-family: simsun, serif; font-size: 14px;\">回款期数：</span><span class=\"wk-print-tag-wukong wk-tiny-color--receivables\" contenteditable=\"true\" data-wk-tag=\"receivables.plan_num\"><span style=\"font-family: simsun, serif; font-size: 14px;\">{期数}</span></span></td><td style=\"width: 50%; height: 18px;\"><span style=\"font-family: simsun, serif; font-size: 14px;\">回款金额（元）：</span><span class=\"wk-print-tag-wukong wk-tiny-color--receivables\" contenteditable=\"true\" data-wk-tag=\"receivables.money\"><span style=\"font-family: simsun, serif; font-size: 14px;\">{回款金额}</span></span></td></tr><tr style=\"height: 18px;\"><td style=\"width: 50%; height: 18px;\"><span style=\"font-family: simsun, serif; font-size: 14px;\">负责人：</span><span class=\"wk-print-tag-wukong wk-tiny-color--receivables\" contenteditable=\"true\" data-wk-tag=\"receivables.owner_user_name\"><span style=\"font-family: simsun, serif; font-size: 14px;\">{负责人}</span></span></td><td style=\"width: 50%; height: 18px;\">&nbsp;</td></tr><tr style=\"height: 18px;\"><td style=\"width: 50%; height: 18px;\" colspan=\"2\"><span style=\"font-family: simsun, serif; font-size: 14px;\">备注：</span><span class=\"wk-print-tag-wukong wk-tiny-color--receivables\" contenteditable=\"true\" data-wk-tag=\"receivables.remark\"><span style=\"font-family: simsun, serif; font-size: 14px;\">{备注}</span></span></td></tr></tbody></table>', 0, '2020-08-22 11:40:42', NULL);

-- ----------------------------
-- Table structure for wk_crm_product
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_product`;
CREATE TABLE `wk_crm_product`  (
  `product_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '产品名称',
  `num` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品编码',
  `unit` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单位',
  `price` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '价格',
  `status` int(1) NULL DEFAULT NULL COMMENT '状态 1 上架 0 下架 3 删除',
  `category_id` int(11) NULL DEFAULT NULL COMMENT '产品分类ID',
  `description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '产品描述',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `owner_user_id` bigint(20) NULL DEFAULT NULL COMMENT '负责人ID',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `batch_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '批次',
  `old_product_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`product_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '产品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_product
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_product_category
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_product_category`;
CREATE TABLE `wk_crm_product_category`  (
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `pid` int(11) NULL DEFAULT 0,
  PRIMARY KEY (`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14768 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '产品分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_product_category
-- ----------------------------
INSERT INTO `wk_crm_product_category` VALUES (14767, '默认', 0);

-- ----------------------------
-- Table structure for wk_crm_product_data
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_product_data`;
CREATE TABLE `wk_crm_product_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NOT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '产品自定义字段存值表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_product_data
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_product_detail_img
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_product_detail_img`;
CREATE TABLE `wk_crm_product_detail_img`  (
  `img_id` int(11) NOT NULL AUTO_INCREMENT,
  `product_id` int(11) NULL DEFAULT NULL COMMENT '产品id',
  `remarks` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `main_file_ids` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主图',
  `detail_file_ids` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`img_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '产品详情图片' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_product_detail_img
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_product_user
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_product_user`;
CREATE TABLE `wk_crm_product_user`  (
  `product_user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`product_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '产品员工小程序显示关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_product_user
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_receivables
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_receivables`;
CREATE TABLE `wk_crm_receivables`  (
  `receivables_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '回款ID',
  `number` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回款编号',
  `plan_id` int(11) NULL DEFAULT NULL COMMENT '回款计划ID',
  `customer_id` int(11) NULL DEFAULT NULL COMMENT '客户ID',
  `contract_id` int(11) NULL DEFAULT NULL COMMENT '合同ID',
  `check_status` int(4) NULL DEFAULT NULL COMMENT '0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交',
  `examine_record_id` int(11) NULL DEFAULT NULL COMMENT '审核记录ID',
  `return_time` date NULL DEFAULT NULL COMMENT '回款日期',
  `return_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回款方式',
  `money` decimal(17, 2) NULL DEFAULT NULL COMMENT '回款金额',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `owner_user_id` bigint(20) NULL DEFAULT NULL COMMENT '负责人ID',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `batch_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '批次',
  PRIMARY KEY (`receivables_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '回款表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_receivables
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_receivables_data
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_receivables_data`;
CREATE TABLE `wk_crm_receivables_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NOT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '回款自定义字段存值表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_receivables_data
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_receivables_plan
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_receivables_plan`;
CREATE TABLE `wk_crm_receivables_plan`  (
  `plan_id` int(11) NOT NULL AUTO_INCREMENT,
  `num` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '期数',
  `receivables_id` int(11) NULL DEFAULT NULL COMMENT '回款ID',
  `status` int(4) NULL DEFAULT NULL COMMENT '1完成 0 未完成',
  `money` decimal(18, 2) NULL DEFAULT NULL COMMENT '计划回款金额',
  `return_date` datetime(0) NULL DEFAULT NULL COMMENT '计划回款日期',
  `return_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计划回款方式',
  `remind` int(4) NULL DEFAULT NULL COMMENT '提前几天提醒',
  `remind_date` datetime(0) NULL DEFAULT NULL COMMENT '提醒日期',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `owner_user_id` bigint(20) NULL DEFAULT NULL COMMENT '负责人ID',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `file_batch` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件批次ID',
  `contract_id` int(11) NOT NULL COMMENT '合同ID',
  `customer_id` int(11) NULL DEFAULT NULL COMMENT '客户ID',
  `old_plan_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`plan_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '回款计划表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_receivables_plan
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_return_visit
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_return_visit`;
CREATE TABLE `wk_crm_return_visit`  (
  `visit_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '回访id',
  `visit_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '回访编号',
  `visit_time` datetime(0) NULL DEFAULT NULL COMMENT '回访时间',
  `owner_user_id` bigint(20) NULL DEFAULT NULL COMMENT '回访人id',
  `customer_id` int(11) NULL DEFAULT NULL COMMENT '客户id',
  `contract_id` int(11) NULL DEFAULT NULL COMMENT '合同id',
  `contacts_id` int(11) NULL DEFAULT NULL COMMENT '联系人id',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '批次id',
  PRIMARY KEY (`visit_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '回访表\r\n' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_return_visit
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_return_visit_data
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_return_visit_data`;
CREATE TABLE `wk_crm_return_visit_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NOT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '回访扩展数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_return_visit_data
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_role_field
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_role_field`;
CREATE TABLE `wk_crm_role_field`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `label` int(4) NOT NULL COMMENT 'crm模块',
  `field_id` int(11) NULL DEFAULT NULL COMMENT '字段id',
  `field_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段标识',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段名称',
  `auth_level` int(2) NOT NULL COMMENT '权限 1不可编辑不可查看 2可查看不可编辑 3可编辑可查看',
  `operate_type` int(2) NOT NULL COMMENT '操作权限 1都可以设置 2只有查看权限可设置 3只有编辑权限可设置 4都不能设置',
  `field_type` int(4) NULL DEFAULT NULL COMMENT '  0自定义字段 1原始字段 2原始字段但值在data表 3关联表的字段 4系统字段',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色字段授权表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_role_field
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_scene
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_scene`;
CREATE TABLE `wk_crm_scene`  (
  `scene_id` int(10) NOT NULL AUTO_INCREMENT,
  `type` int(5) NOT NULL COMMENT '分类',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '场景名称',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `sort` int(5) NOT NULL COMMENT '排序ID',
  `data` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '属性值',
  `is_hide` int(1) NOT NULL COMMENT '1隐藏',
  `is_system` int(1) NOT NULL COMMENT '1系统0自定义',
  `bydata` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统参数',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`scene_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 621 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '场景' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_scene
-- ----------------------------

-- ----------------------------
-- Table structure for wk_crm_scene_default
-- ----------------------------
DROP TABLE IF EXISTS `wk_crm_scene_default`;
CREATE TABLE `wk_crm_scene_default`  (
  `default_id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(5) NOT NULL COMMENT '类型',
  `user_id` bigint(20) NOT NULL COMMENT '人员ID',
  `scene_id` int(11) NOT NULL COMMENT '场景ID',
  PRIMARY KEY (`default_id`) USING BTREE,
  UNIQUE INDEX `default_id`(`default_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '场景默认关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_crm_scene_default
-- ----------------------------

-- ----------------------------
-- Table structure for wk_email_account
-- ----------------------------
DROP TABLE IF EXISTS `wk_email_account`;
CREATE TABLE `wk_email_account`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email_account` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '邮箱账号',
  `email_password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '邮箱密码',
  `send_nick` varchar(225) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '发送昵称',
  `configuration_mode` int(2) NULL DEFAULT 1 COMMENT '配置邮箱方式 1：自动配置 2：手动配置',
  `service_type` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '收信服务类型 :POP3、IMAP',
  `receiving_server` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '收信服务器地址',
  `is_receiving` int(2) NULL DEFAULT 1 COMMENT '收信服务器是否启用ssl 代理 0：未启用 1：已启用',
  `receiving_ssl` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '收信服务器 SSL 端口',
  `smtp_server` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'SMTP服务器',
  `is_smtp` int(2) NULL DEFAULT 1 COMMENT 'smtp服务器是否启用ssl 代理 0：未启用 1：已启用',
  `smtp_ssl` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'smtp端口号',
  `signature` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '个性签名',
  `email_count` int(11) NULL DEFAULT NULL COMMENT '邮件总数量',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '邮箱账号' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_email_account
-- ----------------------------

-- ----------------------------
-- Table structure for wk_email_file
-- ----------------------------
DROP TABLE IF EXISTS `wk_email_file`;
CREATE TABLE `wk_email_file`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件名称',
  `file_size` bigint(20) NULL DEFAULT NULL COMMENT '文件大小',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '文件批次ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '邮箱附件名称表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_email_file
-- ----------------------------

-- ----------------------------
-- Table structure for wk_email_lately
-- ----------------------------
DROP TABLE IF EXISTS `wk_email_lately`;
CREATE TABLE `wk_email_lately`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) NULL DEFAULT NULL COMMENT '客户id 为空时表示不是列表客户 不为空时表示客户',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `email` varchar(225) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '邮箱',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '邮箱最近联系人' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_email_lately
-- ----------------------------

-- ----------------------------
-- Table structure for wk_email_record
-- ----------------------------
DROP TABLE IF EXISTS `wk_email_record`;
CREATE TABLE `wk_email_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email_account_id` int(11) NULL DEFAULT NULL COMMENT '发送账号id',
  `sender` varchar(225) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '发送人昵称',
  `sender_email` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '发送人邮箱',
  `receipt_name` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL,
  `receipt_emails` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '收件人邮箱集合 逗号分隔',
  `cc_name` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL,
  `cc_emails` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '抄送邮箱 逗号隔开',
  `theme` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '主题',
  `attachment` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '附件',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '内容',
  `email_type` int(2) NULL DEFAULT NULL COMMENT '邮箱类型 1：收件箱 2：发件箱 3：草稿箱 4：删除箱 5：垃圾箱',
  `is_read` int(2) NULL DEFAULT 0 COMMENT '是否已读取 0：未读 1：已读',
  `is_start` int(2) NULL DEFAULT 0 COMMENT '是否标星 0：未标星  1：已标星',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `record_type` int(2) NULL DEFAULT NULL COMMENT '记录类型 1：发送 2：保存草稿 3：删除邮件 4：星标邮件 5：垃圾邮件 ',
  `message_id` int(2) NULL DEFAULT NULL COMMENT '邮件id',
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `is_del` int(2) NULL DEFAULT 0 COMMENT '0:未删除 1：已删除',
  `email_uid` bigint(32) NULL DEFAULT NULL COMMENT '邮件uid',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '邮件记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_email_record
-- ----------------------------

-- ----------------------------
-- Table structure for wk_examine
-- ----------------------------
DROP TABLE IF EXISTS `wk_examine`;
CREATE TABLE `wk_examine`  (
  `examine_id` bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '审批ID',
  `label` int(2) UNSIGNED NULL DEFAULT NULL COMMENT '0 OA 1 合同 2 回款 3发票 4薪资 5 采购审核 6采购退货审核 7销售审核 8 销售退货审核 9付款单审核10 回款单审核11盘点审核12调拨审核',
  `examine_icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图标',
  `examine_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '审批名称',
  `recheck_type` int(1) NULL DEFAULT NULL COMMENT '撤回之后重新审核操作 1 从第一层开始 2 从拒绝的层级开始',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `status` int(1) NULL DEFAULT NULL COMMENT '1 正常 2 停用 3 删除 ',
  `batch_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '批次ID',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `update_user_id` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
  `user_ids` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '可见范围（员工）',
  `dept_ids` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '可见范围（部门）',
  `oa_type` int(1) NULL DEFAULT 0 COMMENT '1 普通审批 2 请假审批 3 出差审批 4 加班审批 5 差旅报销 6 借款申请 0 自定义审批',
  PRIMARY KEY (`examine_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1164178 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '审批表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_examine
-- ----------------------------
INSERT INTO `wk_examine` VALUES (25375, 2, NULL, '回款审批流程', 1, NULL, 3, 1, '38e4ecd1525111ebbe7418c04d26d688', '', '2021-01-09 16:03:54', 3, NULL, NULL, 0);
INSERT INTO `wk_examine` VALUES (25376, 1, NULL, '合同审批流程', 1, NULL, 3, 1, '38e4f6e4525111ebbe7418c04d26d688', '说明', '2021-01-09 16:03:54', 3, NULL, NULL, 0);
INSERT INTO `wk_examine` VALUES (25377, 3, NULL, '发票审批流程', 1, NULL, 0, 1, '38e4f798525111ebbe7418c04d26d688', '', '2021-01-09 16:03:54', 0, NULL, NULL, 0);
INSERT INTO `wk_examine` VALUES (1072979, 0, 'wk wk-l-record,#3ABCFB', '普通审批', 1, '2019-04-26 15:06:34', 3, 1, '38efbcd2525111ebbe7418c04d26d688', '普通审批', '2021-01-09 16:03:54', 3, '', '', 1);
INSERT INTO `wk_examine` VALUES (1072980, 0, 'wk wk-leave,#00CAAB', '请假审批', 1, '2019-04-17 18:52:44', 3, 1, '38efbdd4525111ebbe7418c04d26d688', '请假审批', '2021-01-09 16:03:54', 3, '', '', 2);
INSERT INTO `wk_examine` VALUES (1072981, 0, 'wk wk-trip,#3ABCFB', '出差审批', 1, '2019-04-17 18:52:50', 3, 1, '38efbe57525111ebbe7418c04d26d688', '出差审批', '2021-01-09 16:03:54', 3, '', '', 3);
INSERT INTO `wk_examine` VALUES (1072982, 0, 'wk wk-overtime,#FAAD14', '加班审批', 1, '2019-04-17 18:52:59', 3, 1, '38efbe9f525111ebbe7418c04d26d688', '加班审批', '2021-01-09 16:03:54', 3, '', '', 4);
INSERT INTO `wk_examine` VALUES (1072983, 0, 'wk wk-reimbursement,#3ABCFB', '差旅报销', 1, '2019-04-17 18:53:13', 3, 1, '38efbee2525111ebbe7418c04d26d688', '差旅报销', '2021-01-09 16:03:54', 3, '', '', 5);
INSERT INTO `wk_examine` VALUES (1072984, 0, 'wk wk-go-out,#FF6033', '借款申请', 1, '2019-04-17 18:54:44', 3, 1, '38efbf24525111ebbe7418c04d26d688', '借款申请', '2021-01-09 16:03:54', 3, '', '', 6);

-- ----------------------------
-- Table structure for wk_examine_condition
-- ----------------------------
DROP TABLE IF EXISTS `wk_examine_condition`;
CREATE TABLE `wk_examine_condition`  (
  `condition_id` int(11) NOT NULL AUTO_INCREMENT,
  `condition_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '条件名称',
  `flow_id` int(11) NOT NULL COMMENT '审批流程ID',
  `priority` int(4) NOT NULL COMMENT '优先级 数字越低优先级越高',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `batch_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '批次ID',
  PRIMARY KEY (`condition_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1490 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '审批条件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_examine_condition
-- ----------------------------

-- ----------------------------
-- Table structure for wk_examine_condition_data
-- ----------------------------
DROP TABLE IF EXISTS `wk_examine_condition_data`;
CREATE TABLE `wk_examine_condition_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `condition_id` int(11) NOT NULL COMMENT '条件ID',
  `flow_id` int(11) NOT NULL COMMENT '审批流程ID',
  `field_id` int(11) NULL DEFAULT NULL COMMENT '字段ID',
  `field_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '字段名称',
  `condition_type` int(2) NULL DEFAULT NULL COMMENT '连接条件 1 等于 2 大于 3 小于 4 大于等于 5 小于等于 6 两者之间 7 包含 8 员工 9 部门 10 角色',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '值，json数组格式',
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '批次ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '字段中文名称',
  `type` int(2) NULL DEFAULT NULL COMMENT '字段类型 1 单行文本 2 多行文本 3 单选 4日期 5 数字 6 小数 7 手机  8 文件 9 多选 10 人员 11 附件 12 部门 13 日期时间 14 邮箱 15客户 16 商机 17 联系人 18 地图 19 产品类型 20 合同 21 回款计划',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1506 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '审批条件扩展字段表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_examine_condition_data
-- ----------------------------

-- ----------------------------
-- Table structure for wk_examine_flow
-- ----------------------------
DROP TABLE IF EXISTS `wk_examine_flow`;
CREATE TABLE `wk_examine_flow`  (
  `flow_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '审核流程ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '名称',
  `examine_id` bigint(10) UNSIGNED NULL DEFAULT NULL COMMENT '审批ID',
  `examine_type` int(2) NOT NULL COMMENT '0 条件 1 指定成员 2 主管 3 角色 4 发起人自选 5 连续多级主管',
  `examine_error_handling` int(1) NOT NULL DEFAULT 1 COMMENT '审批找不到用户或者条件均不满足时怎么处理 1 自动通过 2 管理员审批',
  `condition_id` int(11) NOT NULL DEFAULT 0 COMMENT '条件ID',
  `sort` int(11) NOT NULL COMMENT '执行顺序，不可为空',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `batch_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '批次ID',
  PRIMARY KEY (`flow_id`) USING BTREE,
  INDEX `examine_id`(`examine_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1163342 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '审批流程表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_examine_flow
-- ----------------------------
INSERT INTO `wk_examine_flow` VALUES (1163333, '业务审批6375', 25375, 4, 2, 0, 1, NULL, 3, '38e4ecd1525111ebbe7418c04d26d688');
INSERT INTO `wk_examine_flow` VALUES (1163334, '业务审批3841', 25376, 4, 2, 0, 1, NULL, 3, '38e4f6e4525111ebbe7418c04d26d688');
INSERT INTO `wk_examine_flow` VALUES (1163335, '业务审批3682', 25377, 4, 2, 0, 1, NULL, 0, '38e4f798525111ebbe7418c04d26d688');
INSERT INTO `wk_examine_flow` VALUES (1163336, '办公审批3621', 1072979, 4, 2, 0, 1, '2019-04-26 15:06:34', 3, '38efbcd2525111ebbe7418c04d26d688');
INSERT INTO `wk_examine_flow` VALUES (1163337, '办公审批1325', 1072980, 4, 2, 0, 1, '2019-04-17 18:52:44', 3, '38efbdd4525111ebbe7418c04d26d688');
INSERT INTO `wk_examine_flow` VALUES (1163338, '办公审批6681', 1072981, 4, 2, 0, 1, '2019-04-17 18:52:50', 3, '38efbe57525111ebbe7418c04d26d688');
INSERT INTO `wk_examine_flow` VALUES (1163339, '办公审批2494', 1072982, 4, 2, 0, 1, '2019-04-17 18:52:59', 3, '38efbe9f525111ebbe7418c04d26d688');
INSERT INTO `wk_examine_flow` VALUES (1163340, '办公审批8739', 1072983, 4, 2, 0, 1, '2019-04-17 18:53:13', 3, '38efbee2525111ebbe7418c04d26d688');
INSERT INTO `wk_examine_flow` VALUES (1163341, '办公审批8180', 1072984, 4, 2, 0, 1, '2019-04-17 18:54:44', 3, '38efbf24525111ebbe7418c04d26d688');

-- ----------------------------
-- Table structure for wk_examine_flow_continuous_superior
-- ----------------------------
DROP TABLE IF EXISTS `wk_examine_flow_continuous_superior`;
CREATE TABLE `wk_examine_flow_continuous_superior`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `flow_id` int(11) NOT NULL COMMENT '审批流程ID',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色ID',
  `max_level` int(2) NULL DEFAULT NULL COMMENT '角色审批的最高级别或者组织架构的第N级',
  `type` int(1) NULL DEFAULT NULL COMMENT '1 指定角色 2 组织架构的最上级',
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '批次ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 143 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '审批流程连续多级主管审批记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_examine_flow_continuous_superior
-- ----------------------------

-- ----------------------------
-- Table structure for wk_examine_flow_member
-- ----------------------------
DROP TABLE IF EXISTS `wk_examine_flow_member`;
CREATE TABLE `wk_examine_flow_member`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `flow_id` int(11) NOT NULL COMMENT '审批流程ID',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '审批人ID',
  `type` int(1) NULL DEFAULT NULL COMMENT '1 依次审批 2 会签 3 或签',
  `sort` int(1) NOT NULL DEFAULT 0 COMMENT '排序规则',
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '批次ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4448 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '审批流程指定成员记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_examine_flow_member
-- ----------------------------

-- ----------------------------
-- Table structure for wk_examine_flow_optional
-- ----------------------------
DROP TABLE IF EXISTS `wk_examine_flow_optional`;
CREATE TABLE `wk_examine_flow_optional`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `flow_id` int(11) NOT NULL COMMENT '审核流程ID',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '审批人ID',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色ID',
  `choose_type` int(1) NULL DEFAULT NULL COMMENT '选择类型 1 自选一人 2 自选多人',
  `type` int(1) NULL DEFAULT NULL COMMENT '1 依次审批 2 会签 3 或签',
  `sort` int(1) NOT NULL DEFAULT 0 COMMENT '排序规则',
  `batch_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '批次ID',
  `range_type` int(1) NULL DEFAULT NULL COMMENT '选择范围 1 全公司 2 指定成员 3 指定角色 ',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `flow_id`(`flow_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1313998 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '审批流程自选成员记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_examine_flow_optional
-- ----------------------------
INSERT INTO `wk_examine_flow_optional` VALUES (1313980, 1163333, NULL, NULL, 2, 1, 0, '38e4ecd1525111ebbe7418c04d26d688', 1);
INSERT INTO `wk_examine_flow_optional` VALUES (1313981, 1163334, NULL, NULL, 2, 1, 0, '38e4f6e4525111ebbe7418c04d26d688', 1);
INSERT INTO `wk_examine_flow_optional` VALUES (1313982, 1163335, NULL, NULL, 2, 1, 0, '38e4f798525111ebbe7418c04d26d688', 1);
INSERT INTO `wk_examine_flow_optional` VALUES (1313983, 1163333, NULL, NULL, 2, 1, 0, '38e4ecd1525111ebbe7418c04d26d688', 1);
INSERT INTO `wk_examine_flow_optional` VALUES (1313984, 1163334, NULL, NULL, 2, 1, 0, '38e4f6e4525111ebbe7418c04d26d688', 1);
INSERT INTO `wk_examine_flow_optional` VALUES (1313985, 1163335, NULL, NULL, 2, 1, 0, '38e4f798525111ebbe7418c04d26d688', 1);
INSERT INTO `wk_examine_flow_optional` VALUES (1313986, 1163336, NULL, NULL, 2, 1, 0, '38efbcd2525111ebbe7418c04d26d688', 1);
INSERT INTO `wk_examine_flow_optional` VALUES (1313987, 1163337, NULL, NULL, 2, 1, 0, '38efbdd4525111ebbe7418c04d26d688', 1);
INSERT INTO `wk_examine_flow_optional` VALUES (1313988, 1163338, NULL, NULL, 2, 1, 0, '38efbe57525111ebbe7418c04d26d688', 1);
INSERT INTO `wk_examine_flow_optional` VALUES (1313989, 1163339, NULL, NULL, 2, 1, 0, '38efbe9f525111ebbe7418c04d26d688', 1);
INSERT INTO `wk_examine_flow_optional` VALUES (1313990, 1163340, NULL, NULL, 2, 1, 0, '38efbee2525111ebbe7418c04d26d688', 1);
INSERT INTO `wk_examine_flow_optional` VALUES (1313991, 1163341, NULL, NULL, 2, 1, 0, '38efbf24525111ebbe7418c04d26d688', 1);

-- ----------------------------
-- Table structure for wk_examine_flow_role
-- ----------------------------
DROP TABLE IF EXISTS `wk_examine_flow_role`;
CREATE TABLE `wk_examine_flow_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `flow_id` int(11) NOT NULL COMMENT '审核流程ID',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色ID',
  `type` int(1) NULL DEFAULT NULL COMMENT '2 会签 3 或签',
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '批次ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 311 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '审批流程角色审批记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_examine_flow_role
-- ----------------------------

-- ----------------------------
-- Table structure for wk_examine_flow_superior
-- ----------------------------
DROP TABLE IF EXISTS `wk_examine_flow_superior`;
CREATE TABLE `wk_examine_flow_superior`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `flow_id` int(11) NOT NULL COMMENT '审核流程ID',
  `parent_level` int(2) NULL DEFAULT NULL COMMENT '直属上级级别 1 代表直属上级 2 代表 直属上级的上级',
  `type` int(1) NULL DEFAULT NULL COMMENT '找不到上级时，是否由上一级上级代审批 0 否 1 是',
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '批次ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2478 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '审批流程主管审批记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_examine_flow_superior
-- ----------------------------

-- ----------------------------
-- Table structure for wk_examine_manager_user
-- ----------------------------
DROP TABLE IF EXISTS `wk_examine_manager_user`;
CREATE TABLE `wk_examine_manager_user`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `examine_id` bigint(10) UNSIGNED NOT NULL COMMENT '审批ID',
  `user_id` bigint(20) NOT NULL COMMENT '管理员ID',
  `sort` int(5) NOT NULL DEFAULT 0 COMMENT '从小到大',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `examine_id`(`examine_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 527852 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '审批管理员设置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_examine_manager_user
-- ----------------------------
INSERT INTO `wk_examine_manager_user` VALUES (527837, 25375, 14773, 0);
INSERT INTO `wk_examine_manager_user` VALUES (527838, 25376, 14773, 0);
INSERT INTO `wk_examine_manager_user` VALUES (527839, 25377, 14773, 0);
INSERT INTO `wk_examine_manager_user` VALUES (527840, 1072979, 14773, 0);
INSERT INTO `wk_examine_manager_user` VALUES (527841, 1072980, 14773, 0);
INSERT INTO `wk_examine_manager_user` VALUES (527842, 1072981, 14773, 0);
INSERT INTO `wk_examine_manager_user` VALUES (527843, 1072982, 14773, 0);
INSERT INTO `wk_examine_manager_user` VALUES (527844, 1072983, 14773, 0);
INSERT INTO `wk_examine_manager_user` VALUES (527845, 1072984, 14773, 0);

-- ----------------------------
-- Table structure for wk_examine_record
-- ----------------------------
DROP TABLE IF EXISTS `wk_examine_record`;
CREATE TABLE `wk_examine_record`  (
  `record_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '审核记录ID',
  `examine_id` bigint(11) NOT NULL COMMENT '审核ID',
  `label` int(1) NULL DEFAULT NULL COMMENT '业务类型',
  `flow_id` int(11) NOT NULL COMMENT '流程ID',
  `type_id` int(11) NULL DEFAULT NULL COMMENT '关联业务主键ID',
  `examine_status` int(1) NULL DEFAULT NULL COMMENT '审核状态 0 未审核 1 审核通过 2 审核拒绝 3 审核中 4 已撤回',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  `update_user_id` bigint(20) NOT NULL COMMENT '修改人',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1004974 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '审核记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_examine_record
-- ----------------------------

-- ----------------------------
-- Table structure for wk_examine_record_log
-- ----------------------------
DROP TABLE IF EXISTS `wk_examine_record_log`;
CREATE TABLE `wk_examine_record_log`  (
  `log_id` int(11) NOT NULL AUTO_INCREMENT,
  `examine_id` bigint(11) NOT NULL COMMENT '审批ID',
  `flow_id` int(11) NOT NULL COMMENT '审批流程ID',
  `record_id` int(11) NOT NULL COMMENT '审批记录ID',
  `type` int(1) NULL DEFAULT NULL COMMENT '1 依次审批 2 会签 3 或签',
  `sort` int(6) NULL DEFAULT NULL COMMENT '排序',
  `examine_status` int(1) NOT NULL COMMENT '审核状态0待审核、1通过、2拒绝、3审核中 4:撤回 5 未提交 6 创建 7 已删除 8 作废',
  `examine_user_id` bigint(20) NULL DEFAULT 0 COMMENT '审核人ID',
  `examine_role_id` int(11) NULL DEFAULT 0 COMMENT '审核角色ID',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '批次ID',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核备注',
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1137829 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '审核日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_examine_record_log
-- ----------------------------

-- ----------------------------
-- Table structure for wk_examine_record_optional
-- ----------------------------
DROP TABLE IF EXISTS `wk_examine_record_optional`;
CREATE TABLE `wk_examine_record_optional`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `flow_id` int(11) NOT NULL COMMENT '流程ID',
  `record_id` int(11) NOT NULL COMMENT '审核记录ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `sort` int(2) NOT NULL DEFAULT 1 COMMENT '排序。从小到大',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 326 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '审核自选成员选择成员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_examine_record_optional
-- ----------------------------

-- ----------------------------
-- Table structure for wk_km_action_record
-- ----------------------------
DROP TABLE IF EXISTS `wk_km_action_record`;
CREATE TABLE `wk_km_action_record`  (
  `record_id` int(11) NOT NULL AUTO_INCREMENT,
  `status` int(1) NULL DEFAULT NULL COMMENT '1 浏览 2 删除',
  `type` int(1) NOT NULL COMMENT '1 知识库 2 文件夹 3 文档 4 文件 ',
  `type_id` int(11) NOT NULL,
  `create_user_id` bigint(20) NOT NULL,
  `create_time` datetime(0) NOT NULL,
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '知识库操作记录（最近使用）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_km_action_record
-- ----------------------------

-- ----------------------------
-- Table structure for wk_km_auth
-- ----------------------------
DROP TABLE IF EXISTS `wk_km_auth`;
CREATE TABLE `wk_km_auth`  (
  `auth_id` int(11) NOT NULL AUTO_INCREMENT,
  `is_open` int(1) NULL DEFAULT NULL COMMENT '是否公开 0 私有 1 公开',
  `open_auth` int(1) NULL DEFAULT NULL COMMENT '公开权限 2 均可编辑 3 均可见，不可编辑',
  PRIMARY KEY (`auth_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 92 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '文档文件夹权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_km_auth
-- ----------------------------
INSERT INTO `wk_km_auth` VALUES (87, 1, 2);
INSERT INTO `wk_km_auth` VALUES (88, 1, 2);
INSERT INTO `wk_km_auth` VALUES (89, 1, 2);
INSERT INTO `wk_km_auth` VALUES (90, 1, 2);
INSERT INTO `wk_km_auth` VALUES (91, 1, 2);

-- ----------------------------
-- Table structure for wk_km_auth_user
-- ----------------------------
DROP TABLE IF EXISTS `wk_km_auth_user`;
CREATE TABLE `wk_km_auth_user`  (
  `r_id` int(11) NOT NULL AUTO_INCREMENT,
  `auth_id` int(11) NULL DEFAULT NULL,
  `user_id` bigint(20) NULL DEFAULT NULL,
  `auth` int(1) NULL DEFAULT NULL COMMENT '私有权限 1 所有权限 2 编辑权限 3只读权限',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `create_user_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`r_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 147 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '知识库权限用户关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_km_auth_user
-- ----------------------------
INSERT INTO `wk_km_auth_user` VALUES (142, 87, 14773, 1, '2020-08-22 16:11:27', 14773);
INSERT INTO `wk_km_auth_user` VALUES (143, 88, 14773, 1, '2020-08-22 16:11:27', 14773);
INSERT INTO `wk_km_auth_user` VALUES (144, 89, 14773, 1, '2020-08-22 16:11:27', 14773);
INSERT INTO `wk_km_auth_user` VALUES (145, 90, 14773, 1, '2020-08-22 16:11:27', 14773);
INSERT INTO `wk_km_auth_user` VALUES (146, 91, 14773, 1, '2020-08-22 16:11:27', 14773);

-- ----------------------------
-- Table structure for wk_km_collect
-- ----------------------------
DROP TABLE IF EXISTS `wk_km_collect`;
CREATE TABLE `wk_km_collect`  (
  `collect_id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(1) NOT NULL COMMENT '1 知识库 2 文件夹 3 文件',
  `type_id` int(11) NOT NULL,
  `create_time` datetime(0) NOT NULL,
  `create_user_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`collect_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '知识库收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_km_collect
-- ----------------------------

-- ----------------------------
-- Table structure for wk_km_document
-- ----------------------------
DROP TABLE IF EXISTS `wk_km_document`;
CREATE TABLE `wk_km_document`  (
  `document_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '文档标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL,
  `type` int(1) NOT NULL COMMENT '3 富文本 4 文件',
  `parent_id` int(20) NULL DEFAULT 0,
  `status` int(1) NULL DEFAULT 1 COMMENT '-1 删除 0 草稿 1 正常 2 模板',
  `library_id` int(20) NULL DEFAULT NULL,
  `folder_id` int(20) NOT NULL COMMENT '文件夹id',
  `auth_id` int(20) NULL DEFAULT NULL,
  `label_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标签id',
  `create_time` datetime(0) NOT NULL,
  `create_user_id` bigint(20) NOT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `delete_user_id` bigint(20) NULL DEFAULT NULL,
  `delete_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`document_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 90 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '知识库文档表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_km_document
-- ----------------------------
INSERT INTO `wk_km_document` VALUES (85, '产品需求文档', '<div id=\"wk-knowledge-title-wukong\">\n<p id=\"wk-knowledge-content-wukong\">产品需求文档</p>\n</div>\n<h1 style=\"mso-pagination: widow-orphan;\">基本信息</h1>\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">目标上线时间：请输入时间</p>\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\"><span lang=\"EN-US\">&nbsp;</span></p>\n<h1 style=\"mso-pagination: widow-orphan;\">项目人员</h1>\n<p class=\"MsoNormal\" style=\"margin-left: 21.0pt; text-align: left; text-indent: -21.0pt; mso-pagination: widow-orphan; mso-list: l0 level1 lfo1;\" align=\"left\"><!-- [if !supportLists]--><span lang=\"EN-US\"><span style=\"mso-list: Ignore;\">l<span style=\"font-style: normal; font-variant: normal; font-stretch: normal; line-height: normal;\">&nbsp; </span></span></span><!--[endif]-->产品：请输入人员<a name=\"_GoBack\"></a></p>\n<p class=\"MsoNormal\" style=\"margin-left: 21.0pt; text-align: left; text-indent: -21.0pt; mso-pagination: widow-orphan; mso-list: l0 level1 lfo1;\" align=\"left\"><!-- [if !supportLists]--><span lang=\"EN-US\"><span style=\"mso-list: Ignore;\">l<span style=\"font-style: normal; font-variant: normal; font-stretch: normal; line-height: normal;\">&nbsp; </span></span></span><!--[endif]-->设计：请输入人员</p>\n<p class=\"MsoNormal\" style=\"margin-left: 21.0pt; text-align: left; text-indent: -21.0pt; mso-pagination: widow-orphan; mso-list: l0 level1 lfo1;\" align=\"left\"><!-- [if !supportLists]--><span lang=\"EN-US\"><span style=\"mso-list: Ignore;\">l<span style=\"font-style: normal; font-variant: normal; font-stretch: normal; line-height: normal;\">&nbsp; </span></span></span><!--[endif]-->研发：请输入人员</p>\n<p class=\"MsoNormal\" style=\"margin-left: 21.0pt; text-align: left; text-indent: -21.0pt; mso-pagination: widow-orphan; mso-list: l0 level1 lfo1;\" align=\"left\"><!-- [if !supportLists]--><span lang=\"EN-US\"><span style=\"mso-list: Ignore;\">l<span style=\"font-style: normal; font-variant: normal; font-stretch: normal; line-height: normal;\">&nbsp; </span></span></span><!--[endif]-->测试：请输入人员</p>\n<p class=\"MsoNormal\" style=\"margin-left: 36.0pt; text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">&nbsp;</span></p>\n<h1 style=\"mso-pagination: widow-orphan;\">需求背景</h1>\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">描述产品需求诞生的背景；我们为什么要做这件事情，和战略方向的契合点。</p>\n<h1 style=\"mso-pagination: widow-orphan;\">产品目标</h1>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\">描述产品的目标用户，以及期望通过产品实现的核心目标。</p>\n<p class=\"MsoNormal\" style=\"margin-left: 36.0pt; text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">&nbsp;</span></p>\n<h1 style=\"mso-pagination: widow-orphan;\">衡量指标</h1>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\">用于衡量产品成功的关键指标</p>\n<p class=\"MsoNormal\" style=\"margin-left: 36.0pt; text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">&nbsp;</span></p>\n<p class=\"MsoNormal\" style=\"margin-left: 36.0pt; text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">&nbsp;</span></p>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\">产品需求</p>\n<table class=\"MsoTableGrid\" style=\"border-collapse: collapse; border: none; mso-border-alt: solid windowtext .5pt; mso-yfti-tbllook: 1184; mso-padding-alt: 0cm 5.4pt 0cm 5.4pt;\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\">\n<tbody>\n<tr style=\"mso-yfti-irow: 0; mso-yfti-firstrow: yes;\">\n<td style=\"width: 106.5pt; border: solid windowtext 1.0pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"142\">\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">&nbsp;</span></p>\n</td>\n<td style=\"width: 106.5pt; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"142\">\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">&nbsp;</span></p>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\">作为</p>\n</td>\n<td style=\"width: 106.55pt; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"142\">\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">&nbsp;</span></p>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\">我想要的</p>\n</td>\n<td style=\"width: 106.55pt; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"142\">\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">&nbsp;</span></p>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\">以便于</p>\n</td>\n</tr>\n<tr style=\"mso-yfti-irow: 1;\">\n<td style=\"width: 106.5pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"142\">\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">1</span></p>\n</td>\n<td style=\"width: 106.5pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"142\">\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">&nbsp;</span></p>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\">产品经理</p>\n</td>\n<td style=\"width: 106.55pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"142\">\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\">从模版直接创建产品需求文档的功能</p>\n</td>\n<td style=\"width: 106.55pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"142\">\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\">快速并有条理的进行需求文档的撰写</p>\n</td>\n</tr>\n<tr style=\"mso-yfti-irow: 2; mso-yfti-lastrow: yes;\">\n<td style=\"width: 106.5pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"142\">\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">2</span></p>\n</td>\n<td style=\"width: 106.5pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"142\">\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">&nbsp;</span></p>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\">团队负责人</p>\n</td>\n<td style=\"width: 106.55pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"142\">\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\">自定义文档模版的能力</p>\n</td>\n<td style=\"width: 106.55pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"142\">\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\">规范不同场景下的撰写文档的格式和要求</p>\n</td>\n</tr>\n</tbody>\n</table>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">&nbsp;</span></p>\n<h1 style=\"mso-pagination: widow-orphan;\">功能及界面设计</h1>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\">描述详细功能设计，以及相关的线框设计、视觉设计图。</p>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">+</span>上传图片</p>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">+</span>上传附件</p>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">&nbsp;</span></p>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\">问题</p>\n<table class=\"MsoTableGrid\" style=\"border-collapse: collapse; border: none; mso-border-alt: solid windowtext .5pt; mso-yfti-tbllook: 1184; mso-padding-alt: 0cm 5.4pt 0cm 5.4pt;\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\">\n<tbody>\n<tr style=\"mso-yfti-irow: 0; mso-yfti-firstrow: yes;\">\n<td style=\"width: 47.95pt; border: solid windowtext 1.0pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"64\">\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">#</span></p>\n</td>\n<td style=\"width: 224.75pt; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"300\">\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\">问题</p>\n</td>\n<td style=\"width: 153.0pt; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"204\">\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\">结论</p>\n</td>\n</tr>\n<tr style=\"mso-yfti-irow: 1;\">\n<td style=\"width: 47.95pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"64\">\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">1</span></p>\n</td>\n<td style=\"width: 224.75pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"300\">\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\">描述评审、研发等过程中发现的问题</p>\n</td>\n<td style=\"width: 153.0pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"204\">\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\">描述最终讨论得出的决定</p>\n</td>\n</tr>\n<tr style=\"mso-yfti-irow: 2; mso-yfti-lastrow: yes; height: 22.15pt;\">\n<td style=\"width: 47.95pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt; height: 22.15pt;\" valign=\"top\" width=\"64\">\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">2</span></p>\n</td>\n<td style=\"width: 224.75pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt; height: 22.15pt;\" valign=\"top\" width=\"300\">\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">&nbsp;</span></p>\n</td>\n<td style=\"width: 153.0pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt; height: 22.15pt;\" valign=\"top\" width=\"204\">\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">&nbsp;</span></p>\n</td>\n</tr>\n</tbody>\n</table>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">&nbsp;</span></p>\n<h1 style=\"mso-pagination: widow-orphan;\">暂不支持</h1>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\">由于受限团队目标、技术、时间等不支持的功能</p>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">&nbsp;</span></p>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">&nbsp;</span></p>\n<h1 style=\"mso-pagination: widow-orphan;\">附录</h1>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">+</span>上传附件</p>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">&nbsp;</span></p>', 3, 0, 1, 25, 0, 87, NULL, '2020-08-22 16:11:27', 14773, '2020-08-22 16:11:27', NULL, NULL);
INSERT INTO `wk_km_document` VALUES (86, '会议纪要', '<div id=\"wk-knowledge-title-wukong\">\n<p id=\"wk-knowledge-content-wukong\" style=\"font-size: 24pt; font-weight: bold;\">会议纪要</p>\n</div>\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">产品研发团队的周会纪要</p>\n<p class=\"MsoNormal\"><span lang=\"EN-US\">&nbsp;</span></p>\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">日期：请输入日期</p>\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">与会人员：请输入人员</p>\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\"><span lang=\"EN-US\">&nbsp;</span></p>\n<h1 style=\"mso-pagination: widow-orphan;\">目标</h1>\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">制定本次会议的目标或者描述会议的上下文。</p>\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\"><span lang=\"EN-US\">&nbsp;</span></p>\n<h1 style=\"mso-pagination: widow-orphan;\">议程</h1>\n<table class=\"MsoTableGrid\" style=\"border-collapse: collapse; border: none; mso-border-alt: solid windowtext .5pt; mso-yfti-tbllook: 1184; mso-padding-alt: 0cm 5.4pt 0cm 5.4pt;\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\">\n<tbody>\n<tr style=\"mso-yfti-irow: 0; mso-yfti-firstrow: yes;\">\n<td style=\"width: 47.95pt; border: solid windowtext 1.0pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"64\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\"><span lang=\"EN-US\">#</span></p>\n</td>\n<td style=\"width: 165.05pt; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"220\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">议题</p>\n</td>\n<td style=\"width: 106.55pt; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"142\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">负责人</p>\n</td>\n<td style=\"width: 106.55pt; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"142\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">时间</p>\n</td>\n</tr>\n<tr style=\"mso-yfti-irow: 1; mso-yfti-lastrow: yes;\">\n<td style=\"width: 47.95pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"64\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\"><span lang=\"EN-US\">1</span></p>\n</td>\n<td style=\"width: 165.05pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"220\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">议题内容描述</p>\n</td>\n<td style=\"width: 106.55pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"142\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">人员姓名</p>\n</td>\n<td style=\"width: 106.55pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"142\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">持续时间</p>\n</td>\n</tr>\n</tbody>\n</table>\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\"><span lang=\"EN-US\">&nbsp;</span></p>\n<p class=\"MsoNormal\"><span lang=\"EN-US\">&nbsp;</span></p>\n<h1 style=\"mso-pagination: widow-orphan;\">记录</h1>\n<p class=\"MsoListParagraph\" style=\"margin-left: 21.0pt; text-align: left; text-indent: -21.0pt; mso-char-indent-count: 0; mso-pagination: widow-orphan; mso-list: l0 level1 lfo1;\" align=\"left\"><!-- [if !supportLists]--><span lang=\"EN-US\"><span style=\"mso-list: Ignore;\">l<span style=\"font-style: normal; font-variant: normal; font-stretch: normal; line-height: normal;\">&nbsp; </span></span></span><!--[endif]-->会议要点<span lang=\"EN-US\">1</span></p>\n<p class=\"MsoListParagraph\" style=\"margin-left: 21.0pt; text-align: left; text-indent: -21.0pt; mso-char-indent-count: 0; mso-pagination: widow-orphan; mso-list: l0 level1 lfo1;\" align=\"left\"><!-- [if !supportLists]--><span lang=\"EN-US\"><span style=\"mso-list: Ignore;\">l<span style=\"font-style: normal; font-variant: normal; font-stretch: normal; line-height: normal;\">&nbsp; </span></span></span><!--[endif]-->会议要点<span lang=\"EN-US\">2</span></p>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">&nbsp;</span></p>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">&nbsp;</span></p>\n<h1 style=\"mso-pagination: widow-orphan;\"><a name=\"_GoBack\"></a>后续工作</h1>\n<table class=\"MsoTableGrid\" style=\"border-collapse: collapse; border: none; mso-border-alt: solid windowtext .5pt; mso-yfti-tbllook: 1184; mso-padding-alt: 0cm 5.4pt 0cm 5.4pt;\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\">\n<tbody>\n<tr style=\"mso-yfti-irow: 0; mso-yfti-firstrow: yes;\">\n<td style=\"width: 62.1pt; border: solid windowtext 1.0pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"83\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">负责人</p>\n</td>\n<td style=\"width: 150.9pt; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"201\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">工作安排</p>\n</td>\n<td style=\"width: 106.55pt; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"142\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">执行人</p>\n</td>\n<td style=\"width: 106.55pt; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"142\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">截止时间</p>\n</td>\n</tr>\n<tr style=\"mso-yfti-irow: 1; mso-yfti-lastrow: yes;\">\n<td style=\"width: 62.1pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"83\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">人员姓名</p>\n</td>\n<td style=\"width: 150.9pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"201\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">工作安排项</p>\n</td>\n<td style=\"width: 106.55pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"142\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">人员姓名</p>\n</td>\n<td style=\"width: 106.55pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"142\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">截止时间</p>\n</td>\n</tr>\n</tbody>\n</table>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">&nbsp;</span></p>\n<p class=\"MsoNormal\"><span lang=\"EN-US\">&nbsp;</span></p>', 3, 0, 1, 25, 0, 88, NULL, '2020-08-22 16:11:27', 14773, '2020-08-22 16:11:27', NULL, NULL);
INSERT INTO `wk_km_document` VALUES (87, '技术文档', '<div id=\"wk-knowledge-title-wukong\">\n<p id=\"wk-knowledge-content-wukong\" style=\"font-size: 24pt; font-weight: bold;\">技术文档</p>\n</div>\n<p class=\"MsoNormal\">标题</p>\n<p class=\"MsoNormal\"><span lang=\"EN-US\">&nbsp;</span></p>\n<p class=\"MsoNormal\">在这里描述</p>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan; tab-stops: 45.8pt 91.6pt 137.4pt 183.2pt 229.0pt 274.8pt 320.6pt 366.4pt 412.2pt 458.0pt 503.8pt 549.6pt 595.4pt 641.2pt 687.0pt 732.8pt;\" align=\"left\"><span lang=\"EN-US\">&nbsp;</span></p>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">1//</span>在这里编写代码</p>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">2</span></p>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">3</span></p>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">4</span></p>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">5</span></p>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">+ </span>上传图片</p>\n<p class=\"MsoNormal\"><span lang=\"EN-US\">&nbsp;</span></p>\n<p class=\"MsoNormal\"><a name=\"_GoBack\"></a><span lang=\"EN-US\">&nbsp;</span></p>\n<p class=\"MsoNormal\">标题</p>\n<p class=\"MsoNormal\"><span lang=\"EN-US\">&nbsp;</span></p>\n<p class=\"MsoNormal\">在这里描述</p>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan; tab-stops: 45.8pt 91.6pt 137.4pt 183.2pt 229.0pt 274.8pt 320.6pt 366.4pt 412.2pt 458.0pt 503.8pt 549.6pt 595.4pt 641.2pt 687.0pt 732.8pt;\" align=\"left\"><span lang=\"EN-US\">&nbsp;</span></p>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">1//</span>在这里编写代码</p>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">2</span></p>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">3</span></p>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">4</span></p>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">5</span></p>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\"><span lang=\"EN-US\">+ </span>上传图片</p>\n<p class=\"MsoNormal\"><span lang=\"EN-US\">&nbsp;</span></p>\n<p class=\"MsoNormal\"><span lang=\"EN-US\">&nbsp;</span></p>', 3, 0, 1, 25, 0, 89, NULL, '2020-08-22 16:11:27', 14773, '2020-08-22 16:11:27', NULL, NULL);
INSERT INTO `wk_km_document` VALUES (88, '竞品分析', '<div id=\"wk-knowledge-title-wukong\">\n<p id=\"wk-knowledge-content-wukong\" style=\"font-size: 24pt; font-weight: bold;\">竞品分析</p>\n</div>\n<h1 style=\"mso-pagination: widow-orphan;\">目的</h1>\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">描述做这次竞品分析的目的，因为不同的目的侧重点也会有所差异。</p>\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">可能是为了寻找可借鉴学习之处；可能是为了摸查竞争对手情况做好应对策略；也有可能是作为融资计划的参考<a name=\"_GoBack\"></a>数据。</p>\n<h1 style=\"mso-pagination: widow-orphan;\">市场分析</h1>\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">描述目前行业市场的相关信息，可以通过以下渠道收集信息：</p>\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">从公司内部市场、运营部门、管理层等收集信息；行业媒体平台新闻及论坛及<span lang=\"EN-US\">QQ</span>群，搜索引擎，比如艾瑞咨询，<span lang=\"EN-US\">AppAnnie</span>；行业专家博客、微博、微信公众账号订阅。</p>\n<h1 style=\"mso-pagination: widow-orphan;\">竞品概况</h1>\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">描述竞品相关的一些信息，比如公司团队状况、产品定位、核心数据分析、盈利模式等等。</p>\n<h1 style=\"mso-pagination: widow-orphan;\">产品分析</h1>\n<h2 style=\"mso-pagination: widow-orphan;\">产品功能</h2>\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">可以从产品基本信息、产品功能逻辑、产品渠道（平台）等方面进行分析。</p>\n<h2 style=\"mso-pagination: widow-orphan;\">产品交互设计</h2>\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">通过截图的方式对比交互和设计内容，可以包含：主要风格、色彩体系、布局结构、品质感、字体、功能页面比较。</p>\n<h2 style=\"mso-pagination: widow-orphan;\">技术分析</h2>\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">以移动<span lang=\"EN-US\">App</span>举例，技术分析包含：应用大小、启动时长、功能页面加载时长、<span lang=\"EN-US\">CPU</span>、内存消耗、崩溃率、版本迭代速度等。 这需要根据不同产品进行不同的技术分析。</p>\n<h1 style=\"mso-pagination: widow-orphan;\">产品优劣势</h1>\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">综合评价所选竞品的优势势，以及各自其中的机会和威胁。</p>\n<h1 style=\"mso-pagination: widow-orphan;\">总结</h1>\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">分析的结论一般可以包括两大部分，一是对研究对象的分析总结，二是对自己的行动建议。</p>\n<p class=\"MsoNormal\"><span lang=\"EN-US\">&nbsp;</span></p>', 3, 0, 1, 25, 0, 90, NULL, '2020-08-22 16:11:27', 14773, '2020-08-22 16:11:27', NULL, NULL);
INSERT INTO `wk_km_document` VALUES (89, '产品规划', '<div id=\"wk-knowledge-title-wukong\">\n<p id=\"wk-knowledge-content-wukong\" style=\"font-size: 24pt; font-weight: bold;\">产品规划</p>\n</div>\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">发布时间：请输入日期</p>\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">产品负责人：请输入人员</p>\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\"><span lang=\"EN-US\">&nbsp;</span></p>\n<h1 style=\"mso-pagination: widow-orphan;\"><span lang=\"EN-US\">Sprint 1</span></h1>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\">时间：请输入起止时间</p>\n<p>工作项：</p>\n<table class=\"MsoTableGrid\" style=\"border-collapse: collapse; border: none; mso-border-alt: solid windowtext .5pt; mso-yfti-tbllook: 1184; mso-padding-alt: 0cm 5.4pt 0cm 5.4pt;\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\">\n<tbody>\n<tr style=\"mso-yfti-irow: 0; mso-yfti-firstrow: yes;\">\n<td style=\"width: 155.7pt; border: solid windowtext 1.0pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"208\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">该冲刺的工作项</p>\n</td>\n<td style=\"width: 77.95pt; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"104\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">执行人</p>\n</td>\n<td style=\"width: 127.6pt; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"170\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">截止时间</p>\n</td>\n</tr>\n<tr style=\"mso-yfti-irow: 1;\">\n<td style=\"width: 155.7pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"208\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">工作项<span lang=\"EN-US\">1</span></p>\n</td>\n<td style=\"width: 77.95pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"104\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">人员姓名</p>\n</td>\n<td style=\"width: 127.6pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"170\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">截止时间</p>\n</td>\n</tr>\n<tr style=\"mso-yfti-irow: 2; mso-yfti-lastrow: yes;\">\n<td style=\"width: 155.7pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"208\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">工作项<span lang=\"EN-US\">2</span></p>\n</td>\n<td style=\"width: 77.95pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"104\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\"><span lang=\"EN-US\">&nbsp;</span></p>\n</td>\n<td style=\"width: 127.6pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"170\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\"><span lang=\"EN-US\">&nbsp;</span></p>\n</td>\n</tr>\n</tbody>\n</table>\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\"><span lang=\"EN-US\">&nbsp;</span></p>\n<h1 style=\"mso-pagination: widow-orphan;\"><span lang=\"EN-US\">Sprint 2</span></h1>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\">时间：请输入起止时间</p>\n<p>工作项：</p>\n<table class=\"MsoTableGrid\" style=\"border-collapse: collapse; border: none; mso-border-alt: solid windowtext .5pt; mso-yfti-tbllook: 1184; mso-padding-alt: 0cm 5.4pt 0cm 5.4pt;\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\">\n<tbody>\n<tr style=\"mso-yfti-irow: 0; mso-yfti-firstrow: yes;\">\n<td style=\"width: 155.7pt; border: solid windowtext 1.0pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"208\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">该冲刺的工作项</p>\n</td>\n<td style=\"width: 77.95pt; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"104\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">执行人</p>\n</td>\n<td style=\"width: 127.6pt; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"170\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">截止时间</p>\n</td>\n</tr>\n<tr style=\"mso-yfti-irow: 1;\">\n<td style=\"width: 155.7pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"208\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">工作项<span lang=\"EN-US\">1</span></p>\n</td>\n<td style=\"width: 77.95pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"104\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">人员姓名</p>\n</td>\n<td style=\"width: 127.6pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"170\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">截止时间</p>\n</td>\n</tr>\n<tr style=\"mso-yfti-irow: 2; mso-yfti-lastrow: yes;\">\n<td style=\"width: 155.7pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"208\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">工作项<span lang=\"EN-US\">2</span></p>\n</td>\n<td style=\"width: 77.95pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"104\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\"><span lang=\"EN-US\">&nbsp;</span></p>\n</td>\n<td style=\"width: 127.6pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"170\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\"><span lang=\"EN-US\">&nbsp;</span></p>\n</td>\n</tr>\n</tbody>\n</table>\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\"><span lang=\"EN-US\">&nbsp;</span></p>\n<h1 style=\"mso-pagination: widow-orphan;\"><span lang=\"EN-US\">Sprint 3<a name=\"_GoBack\"></a></span></h1>\n<p class=\"MsoNormal\" style=\"text-align: left; mso-pagination: widow-orphan;\" align=\"left\">时间：请输入起止时间</p>\n<p>工作项：</p>\n<table class=\"MsoTableGrid\" style=\"border-collapse: collapse; border: none; mso-border-alt: solid windowtext .5pt; mso-yfti-tbllook: 1184; mso-padding-alt: 0cm 5.4pt 0cm 5.4pt;\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\">\n<tbody>\n<tr style=\"mso-yfti-irow: 0; mso-yfti-firstrow: yes;\">\n<td style=\"width: 155.7pt; border: solid windowtext 1.0pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"208\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">该冲刺的工作项</p>\n</td>\n<td style=\"width: 77.95pt; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"104\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">执行人</p>\n</td>\n<td style=\"width: 127.6pt; border: solid windowtext 1.0pt; border-left: none; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"170\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">截止时间</p>\n</td>\n</tr>\n<tr style=\"mso-yfti-irow: 1;\">\n<td style=\"width: 155.7pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"208\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">工作项<span lang=\"EN-US\">1</span></p>\n</td>\n<td style=\"width: 77.95pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"104\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">人员姓名</p>\n</td>\n<td style=\"width: 127.6pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"170\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">截止时间</p>\n</td>\n</tr>\n<tr style=\"mso-yfti-irow: 2; mso-yfti-lastrow: yes;\">\n<td style=\"width: 155.7pt; border: solid windowtext 1.0pt; border-top: none; mso-border-top-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"208\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\">工作项<span lang=\"EN-US\">2</span></p>\n</td>\n<td style=\"width: 77.95pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"104\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\"><span lang=\"EN-US\">&nbsp;</span></p>\n</td>\n<td style=\"width: 127.6pt; border-top: none; border-left: none; border-bottom: solid windowtext 1.0pt; border-right: solid windowtext 1.0pt; mso-border-top-alt: solid windowtext .5pt; mso-border-left-alt: solid windowtext .5pt; mso-border-alt: solid windowtext .5pt; padding: 0cm 5.4pt 0cm 5.4pt;\" valign=\"top\" width=\"170\">\n<p style=\"mso-margin-top-alt: auto; mso-margin-bottom-alt: auto; mso-pagination: widow-orphan;\"><span lang=\"EN-US\">&nbsp;</span></p>\n</td>\n</tr>\n</tbody>\n</table>\n<p class=\"MsoNormal\"><span lang=\"EN-US\">&nbsp;</span></p>', 3, 0, 1, 25, 0, 91, NULL, '2020-08-22 16:11:27', 14773, '2020-08-22 16:11:27', NULL, NULL);

-- ----------------------------
-- Table structure for wk_km_document_favor
-- ----------------------------
DROP TABLE IF EXISTS `wk_km_document_favor`;
CREATE TABLE `wk_km_document_favor`  (
  `favor_id` int(11) NOT NULL AUTO_INCREMENT,
  `document_id` int(11) NULL DEFAULT NULL,
  `create_user_id` bigint(20) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`favor_id`) USING BTREE,
  UNIQUE INDEX `wk_km_document_favor_document_id_create_user_id_uindex`(`document_id`, `create_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '文档点赞表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_km_document_favor
-- ----------------------------

-- ----------------------------
-- Table structure for wk_km_document_label
-- ----------------------------
DROP TABLE IF EXISTS `wk_km_document_label`;
CREATE TABLE `wk_km_document_label`  (
  `label_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `color` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_user_id` bigint(20) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`label_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '文档标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_km_document_label
-- ----------------------------

-- ----------------------------
-- Table structure for wk_km_document_share
-- ----------------------------
DROP TABLE IF EXISTS `wk_km_document_share`;
CREATE TABLE `wk_km_document_share`  (
  `share_id` int(11) NOT NULL AUTO_INCREMENT,
  `document_id` int(11) NULL DEFAULT NULL COMMENT '文档id',
  `share_user_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '分享内部成员id',
  `share_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '外部分享链接',
  `token` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '外部查看文档的唯一标识',
  `status` int(1) NULL DEFAULT NULL COMMENT '1 启用 0 关闭分享',
  `create_user_id` bigint(20) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `close_user_id` bigint(20) NULL DEFAULT NULL,
  `close_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`share_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '文档分享' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_km_document_share
-- ----------------------------

-- ----------------------------
-- Table structure for wk_km_folder
-- ----------------------------
DROP TABLE IF EXISTS `wk_km_folder`;
CREATE TABLE `wk_km_folder`  (
  `folder_id` int(11) NOT NULL AUTO_INCREMENT,
  `library_id` int(11) NULL DEFAULT NULL COMMENT '知识库id',
  `parent_id` int(11) NULL DEFAULT 0 COMMENT '父id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `status` int(1) NULL DEFAULT 1 COMMENT '-1 删除 1 正常',
  `create_user_id` bigint(20) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `auth_id` int(11) NULL DEFAULT NULL,
  `delete_user_id` bigint(20) NULL DEFAULT NULL,
  `delete_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`folder_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '知识库文件夹' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_km_folder
-- ----------------------------

-- ----------------------------
-- Table structure for wk_km_knowledge_library
-- ----------------------------
DROP TABLE IF EXISTS `wk_km_knowledge_library`;
CREATE TABLE `wk_km_knowledge_library`  (
  `library_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '知识库名称',
  `description` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '简介',
  `is_open` int(1) NOT NULL COMMENT '是否公开 1 公开 2 私有',
  `status` int(1) NULL DEFAULT 1 COMMENT '-1 删除 1 正常 2 模板',
  `is_system_cover` int(1) NULL DEFAULT NULL COMMENT '0 否 1 是',
  `cover_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '知识库封面',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `delete_user_id` bigint(20) NULL DEFAULT NULL,
  `delete_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`library_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '知识库' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_km_knowledge_library
-- ----------------------------
INSERT INTO `wk_km_knowledge_library` VALUES (25, '产品研发', '提供完善的产品流程文档', 0, 1, 1, 'https://www.72crm.com/api/uploads/kw/1.png', 14773, '2020-08-22 16:11:27', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for wk_km_knowledge_library_user
-- ----------------------------
DROP TABLE IF EXISTS `wk_km_knowledge_library_user`;
CREATE TABLE `wk_km_knowledge_library_user`  (
  `r_id` int(11) NOT NULL AUTO_INCREMENT,
  `library_id` int(11) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `role` int(1) NULL DEFAULT NULL COMMENT '1 创建人 2 管理员 3 成员',
  PRIMARY KEY (`r_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 56 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '知识库成员' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_km_knowledge_library_user
-- ----------------------------
INSERT INTO `wk_km_knowledge_library_user` VALUES (55, 25, 14773, 1);

-- ----------------------------
-- Table structure for wk_oa_announcement
-- ----------------------------
DROP TABLE IF EXISTS `wk_oa_announcement`;
CREATE TABLE `wk_oa_announcement`  (
  `announcement_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标题',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `dept_ids` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '通知部门',
  `owner_user_ids` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '通知人',
  `read_user_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '已读用户',
  PRIMARY KEY (`announcement_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_oa_announcement
-- ----------------------------

-- ----------------------------
-- Table structure for wk_oa_calendar_type
-- ----------------------------
DROP TABLE IF EXISTS `wk_oa_calendar_type`;
CREATE TABLE `wk_oa_calendar_type`  (
  `type_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '日历类型id',
  `type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型名称',
  `color` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '颜色',
  `type` int(1) NULL DEFAULT NULL COMMENT '1 系统类型 2 自定义类型',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `create_user_id` bigint(20) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `sort` int(2) NULL DEFAULT 1,
  PRIMARY KEY (`type_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 500 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '日历类型' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_oa_calendar_type
-- ----------------------------
INSERT INTO `wk_oa_calendar_type` VALUES (492, '分配的任务', '1', 1, '2020-01-13 09:44:05', 14773, NULL, 1);
INSERT INTO `wk_oa_calendar_type` VALUES (493, '需联系的线索', '5', 1, '2020-01-13 09:44:05', 14773, NULL, 2);
INSERT INTO `wk_oa_calendar_type` VALUES (494, '需联系的客户', '2', 1, '2020-01-13 09:44:05', 14773, NULL, 3);
INSERT INTO `wk_oa_calendar_type` VALUES (495, '需联系的商机', '6', 1, '2020-01-13 09:44:05', 14773, NULL, 4);
INSERT INTO `wk_oa_calendar_type` VALUES (496, '预计成交的商机', '7', 1, '2020-01-13 09:44:05', 14773, NULL, 5);
INSERT INTO `wk_oa_calendar_type` VALUES (497, '即将到期的合同', '3', 1, '2020-01-13 09:44:05', 14773, NULL, 6);
INSERT INTO `wk_oa_calendar_type` VALUES (498, '计划回款', '4', 1, '2020-01-13 09:44:05', 14773, NULL, 7);
INSERT INTO `wk_oa_calendar_type` VALUES (499, '开会', '#53D397', 2, '2020-01-13 09:44:05', 14773, NULL, 1);

-- ----------------------------
-- Table structure for wk_oa_calendar_type_user
-- ----------------------------
DROP TABLE IF EXISTS `wk_oa_calendar_type_user`;
CREATE TABLE `wk_oa_calendar_type_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `type_id` int(11) NOT NULL COMMENT '日历类型id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8738 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户关联日历类型' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_oa_calendar_type_user
-- ----------------------------
INSERT INTO `wk_oa_calendar_type_user` VALUES (8733, 14773, 487);
INSERT INTO `wk_oa_calendar_type_user` VALUES (8734, 14773, 488);
INSERT INTO `wk_oa_calendar_type_user` VALUES (8735, 14773, 489);
INSERT INTO `wk_oa_calendar_type_user` VALUES (8736, 14773, 490);
INSERT INTO `wk_oa_calendar_type_user` VALUES (8737, 14773, 491);

-- ----------------------------
-- Table structure for wk_oa_event
-- ----------------------------
DROP TABLE IF EXISTS `wk_oa_event`;
CREATE TABLE `wk_oa_event`  (
  `event_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '标题',
  `type_id` int(1) NOT NULL COMMENT '日程类型',
  `start_time` datetime(0) NOT NULL COMMENT '开始时间',
  `end_time` datetime(0) NOT NULL COMMENT '结束时间',
  `owner_user_ids` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参与人',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `repetition_type` int(1) NULL DEFAULT 1 COMMENT '重复类型 1从不重复 2每天 3每周 4每月 5每年',
  `repeat_rate` int(10) NULL DEFAULT NULL COMMENT '重复频率',
  `repeat_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '3:周/4:月',
  `end_type` int(1) NULL DEFAULT NULL COMMENT '结束类型 1从不 2重复次数 3结束日期',
  `end_type_config` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '2:次数/3:时间',
  `repeat_start_time` datetime(0) NOT NULL COMMENT '循环开始时间',
  `repeat_end_time` datetime(0) NULL DEFAULT NULL COMMENT '循环结束时间',
  `batch_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`event_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '日程表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_oa_event
-- ----------------------------

-- ----------------------------
-- Table structure for wk_oa_event_notice
-- ----------------------------
DROP TABLE IF EXISTS `wk_oa_event_notice`;
CREATE TABLE `wk_oa_event_notice`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `event_id` int(11) NOT NULL COMMENT '日程ID',
  `type` int(1) NOT NULL COMMENT '1分钟 2小时 3天',
  `value` int(2) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '日程提醒设置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_oa_event_notice
-- ----------------------------

-- ----------------------------
-- Table structure for wk_oa_event_relation
-- ----------------------------
DROP TABLE IF EXISTS `wk_oa_event_relation`;
CREATE TABLE `wk_oa_event_relation`  (
  `eventrelation_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '日程关联业务表',
  `event_id` int(11) NOT NULL COMMENT '日程ID',
  `customer_ids` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户IDs',
  `contacts_ids` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人IDs',
  `business_ids` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商机IDs',
  `contract_ids` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合同IDs',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`eventrelation_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '日程关联业务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_oa_event_relation
-- ----------------------------

-- ----------------------------
-- Table structure for wk_oa_event_update_record
-- ----------------------------
DROP TABLE IF EXISTS `wk_oa_event_update_record`;
CREATE TABLE `wk_oa_event_update_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `event_id` int(11) NOT NULL,
  `time` bigint(20) NOT NULL COMMENT '标题',
  `status` int(1) NULL DEFAULT NULL COMMENT '1 删除本次 2 修改本次 3 修改本次及以后',
  `new_event_id` int(11) NULL DEFAULT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '日程表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_oa_event_update_record
-- ----------------------------

-- ----------------------------
-- Table structure for wk_oa_examine
-- ----------------------------
DROP TABLE IF EXISTS `wk_oa_examine`;
CREATE TABLE `wk_oa_examine`  (
  `examine_id` int(11) NOT NULL AUTO_INCREMENT,
  `category_id` int(11) NOT NULL DEFAULT 1 COMMENT '审批类型',
  `content` varchar(800) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容',
  `remark` varchar(800) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `type_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请假类型',
  `money` decimal(18, 2) NULL DEFAULT NULL COMMENT '差旅、报销总金额',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `duration` decimal(18, 2) NULL DEFAULT NULL COMMENT '时长',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `batch_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件批次id',
  `examine_record_id` int(11) NULL DEFAULT NULL COMMENT '审核记录ID',
  `examine_status` int(1) NULL DEFAULT NULL COMMENT '审核状态 0 未审核 1 审核通过 2 审核拒绝 3 审核中 4 已撤回',
  PRIMARY KEY (`examine_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '审批表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_oa_examine
-- ----------------------------

-- ----------------------------
-- Table structure for wk_oa_examine_category
-- ----------------------------
DROP TABLE IF EXISTS `wk_oa_examine_category`;
CREATE TABLE `wk_oa_examine_category`  (
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `icon` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
  `type` int(1) NULL DEFAULT 0 COMMENT '1 普通审批 2 请假审批 3 出差审批 4 加班审批 5 差旅报销 6 借款申请 0 自定义审批',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `status` int(1) NULL DEFAULT 1 COMMENT '1启用，0禁用',
  `is_sys` int(1) NULL DEFAULT NULL COMMENT '1为系统类型，不能删除',
  `examine_type` int(1) NULL DEFAULT NULL COMMENT '1固定2自选',
  `user_ids` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '可见范围（员工）',
  `dept_ids` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '可见范围（部门）',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int(1) NULL DEFAULT 0 COMMENT '1已删除',
  `delete_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
  `delete_user_id` bigint(20) NULL DEFAULT NULL COMMENT '删除人ID',
  PRIMARY KEY (`category_id`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 72985 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '审批类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_oa_examine_category
-- ----------------------------
INSERT INTO `wk_oa_examine_category` VALUES (72979, '普通审批', '普通审批', 'wk wk-l-record,#3ABCFB', 1, 3, 1, 1, 2, '', '', '2019-04-26 15:06:34', '2019-04-26 15:06:34', 0, NULL, NULL);
INSERT INTO `wk_oa_examine_category` VALUES (72980, '请假审批', '请假审批', 'wk wk-leave,#00CAAB', 2, 3, 1, 1, 2, '', '', '2019-04-17 18:52:44', '2019-04-17 18:52:44', 0, NULL, NULL);
INSERT INTO `wk_oa_examine_category` VALUES (72981, '出差审批', '出差审批', 'wk wk-trip,#3ABCFB', 3, 3, 1, 1, 2, '', '', '2019-04-17 18:52:50', '2019-04-17 18:52:50', 0, NULL, NULL);
INSERT INTO `wk_oa_examine_category` VALUES (72982, '加班审批', '加班审批', 'wk wk-overtime,#FAAD14', 4, 3, 1, 1, 2, '', '', '2019-04-17 18:52:59', '2019-04-17 18:52:59', 0, NULL, NULL);
INSERT INTO `wk_oa_examine_category` VALUES (72983, '差旅报销', '差旅报销', 'wk wk-reimbursement,#3ABCFB', 5, 3, 1, 1, 2, '', '', '2019-04-17 18:53:13', '2019-04-17 18:53:13', 0, NULL, NULL);
INSERT INTO `wk_oa_examine_category` VALUES (72984, '借款申请', '借款申请', 'wk wk-go-out,#FF6033', 6, 3, 1, 1, 2, '', '', '2019-04-17 18:54:44', '2019-04-17 18:54:44', 0, NULL, NULL);

-- ----------------------------
-- Table structure for wk_oa_examine_data
-- ----------------------------
DROP TABLE IF EXISTS `wk_oa_examine_data`;
CREATE TABLE `wk_oa_examine_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL COMMENT '字段id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NOT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'oa审批自定义字段存值表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_oa_examine_data
-- ----------------------------

-- ----------------------------
-- Table structure for wk_oa_examine_field
-- ----------------------------
DROP TABLE IF EXISTS `wk_oa_examine_field`;
CREATE TABLE `wk_oa_examine_field`  (
  `field_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `field_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '自定义字段英文标识',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '字段名称',
  `type` int(2) NOT NULL DEFAULT 1 COMMENT '字段类型 1 单行文本 2 多行文本 3 单选 4日期 5 数字 6 小数 7 手机  8 文件 9 多选 10 人员 11 附件 12 部门 13 日期时间 14 邮箱 15客户 16 商机 17 联系人 18 地图 19 产品类型 20 合同 21 回款计划',
  `remark` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段说明',
  `input_tips` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '输入提示',
  `max_length` int(12) NULL DEFAULT NULL COMMENT '最大长度',
  `default_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '默认值',
  `is_unique` int(1) NULL DEFAULT 0 COMMENT '是否唯一 1 是 0 否',
  `is_null` int(1) NULL DEFAULT 0 COMMENT '是否必填 1 是 0 否',
  `sorting` int(5) NULL DEFAULT 1 COMMENT '排序 从小到大',
  `options` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '如果类型是选项，此处不能为空，多个选项以，隔开',
  `operating` int(1) NULL DEFAULT 0 COMMENT '是否可以删除修改 0 改删 1 改 2 删 3 无',
  `is_hidden` int(1) NOT NULL DEFAULT 0 COMMENT '是否隐藏  0不隐藏 1隐藏',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后修改时间',
  `examine_category_id` int(11) NULL DEFAULT NULL COMMENT '审批ID label为10需要',
  `field_type` int(2) NOT NULL DEFAULT 0 COMMENT '字段来源  0.自定义 1.原始固定 2原始字段但值存在扩展表中',
  PRIMARY KEY (`field_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 572 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '自定义字段表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_oa_examine_field
-- ----------------------------
INSERT INTO `wk_oa_examine_field` VALUES (548, 'content', '审批内容', 1, NULL, '', NULL, '', 0, 1, 0, NULL, 3, 0, '2021-01-09 16:03:54', 1072979, 1);
INSERT INTO `wk_oa_examine_field` VALUES (549, 'remark', '备注', 2, NULL, '', 1000, '', 0, 0, 1, NULL, 3, 0, '2021-01-09 16:03:54', 1072979, 1);
INSERT INTO `wk_oa_examine_field` VALUES (550, 'type_id', '请假类型', 3, NULL, '', NULL, '年假', 0, 1, 0, '年假,事假,病假,产假,调休,婚假,丧假,其他', 3, 0, '2021-01-09 16:03:54', 1072980, 1);
INSERT INTO `wk_oa_examine_field` VALUES (551, 'content', '审批内容', 1, NULL, '', NULL, '', 0, 1, 1, NULL, 3, 0, '2021-01-09 16:03:54', 1072980, 1);
INSERT INTO `wk_oa_examine_field` VALUES (552, 'start_time', '开始时间', 13, NULL, '', NULL, '', 0, 1, 2, NULL, 3, 0, '2021-01-09 16:03:54', 1072980, 1);
INSERT INTO `wk_oa_examine_field` VALUES (553, 'end_time', '结束时间', 13, NULL, '', NULL, '', 0, 1, 3, NULL, 3, 0, '2021-01-09 16:03:54', 1072980, 1);
INSERT INTO `wk_oa_examine_field` VALUES (554, 'duration', '时长（天）', 6, NULL, '', NULL, '', 0, 1, 4, NULL, 3, 0, '2021-01-09 16:03:54', 1072980, 1);
INSERT INTO `wk_oa_examine_field` VALUES (555, 'remark', '备注', 2, NULL, '', 1000, '', 0, 0, 5, NULL, 3, 0, '2021-01-09 16:03:54', 1072980, 1);
INSERT INTO `wk_oa_examine_field` VALUES (556, 'content', '出差事由', 1, NULL, '', NULL, '', 0, 1, 0, NULL, 3, 0, '2021-01-09 16:03:54', 1072981, 1);
INSERT INTO `wk_oa_examine_field` VALUES (557, 'remark', '备注', 2, NULL, '', 1000, '', 0, 0, 1, NULL, 3, 0, '2021-01-09 16:03:54', 1072981, 1);
INSERT INTO `wk_oa_examine_field` VALUES (558, 'duration', '出差总天数', 6, NULL, '', NULL, '', 0, 1, 2, NULL, 3, 0, '2021-01-09 16:03:54', 1072981, 1);
INSERT INTO `wk_oa_examine_field` VALUES (559, 'cause', '行程明细', 22, NULL, '', NULL, '', 0, 1, 2, NULL, 3, 0, '2021-01-09 16:03:54', 1072981, 1);
INSERT INTO `wk_oa_examine_field` VALUES (560, 'content', '加班原因', 1, NULL, '', NULL, '', 0, 1, 0, NULL, 3, 0, '2021-01-09 16:03:54', 1072982, 1);
INSERT INTO `wk_oa_examine_field` VALUES (561, 'start_time', '开始时间', 13, NULL, '', NULL, '', 0, 1, 1, NULL, 3, 0, '2021-01-09 16:03:54', 1072982, 1);
INSERT INTO `wk_oa_examine_field` VALUES (562, 'end_time', '结束时间', 13, NULL, '', NULL, '', 0, 1, 2, NULL, 3, 0, '2021-01-09 16:03:54', 1072982, 1);
INSERT INTO `wk_oa_examine_field` VALUES (563, 'duration', '加班总天数', 6, NULL, '', NULL, '', 0, 1, 3, NULL, 3, 0, '2021-01-09 16:03:54', 1072982, 1);
INSERT INTO `wk_oa_examine_field` VALUES (564, 'remark', '备注', 2, NULL, '', 1000, '', 0, 0, 4, NULL, 3, 0, '2021-01-09 16:03:54', 1072982, 1);
INSERT INTO `wk_oa_examine_field` VALUES (565, 'content', '差旅事由', 1, NULL, '', NULL, '', 0, 1, 0, NULL, 3, 0, '2021-01-09 16:03:54', 1072983, 1);
INSERT INTO `wk_oa_examine_field` VALUES (566, 'money', '报销总金额', 6, NULL, '', 0, '', 0, 1, 1, NULL, 3, 0, '2021-01-09 16:03:54', 1072983, 1);
INSERT INTO `wk_oa_examine_field` VALUES (567, 'remark', '备注', 2, NULL, '', 1000, '', 0, 0, 2, NULL, 3, 0, '2021-01-09 16:03:54', 1072983, 1);
INSERT INTO `wk_oa_examine_field` VALUES (568, 'cause', '费用明细', 23, NULL, '', 1000, '', 0, 0, 2, NULL, 3, 0, '2021-01-09 16:03:54', 1072983, 1);
INSERT INTO `wk_oa_examine_field` VALUES (569, 'content', '借款事由', 1, NULL, '', NULL, '', 0, 1, 0, NULL, 3, 0, '2021-01-09 16:03:54', 1072984, 1);
INSERT INTO `wk_oa_examine_field` VALUES (570, 'money', '借款金额（元）', 6, NULL, '', 0, '', 0, 1, 1, NULL, 3, 0, '2021-01-09 16:03:54', 1072984, 1);
INSERT INTO `wk_oa_examine_field` VALUES (571, 'remark', '备注', 2, NULL, '', 1000, '', 0, 0, 2, NULL, 3, 0, '2021-01-09 16:03:54', 1072984, 1);

-- ----------------------------
-- Table structure for wk_oa_examine_log
-- ----------------------------
DROP TABLE IF EXISTS `wk_oa_examine_log`;
CREATE TABLE `wk_oa_examine_log`  (
  `log_id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `record_id` int(11) NULL DEFAULT NULL COMMENT '审批记录ID',
  `examine_step_id` bigint(20) NULL DEFAULT NULL COMMENT '审核步骤ID',
  `examine_status` int(1) NULL DEFAULT NULL COMMENT '审核状态 0 未审核 1 审核通过 2 审核拒绝4 撤回审核',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `examine_user` bigint(20) NULL DEFAULT NULL COMMENT '审核人',
  `examine_time` datetime(0) NULL DEFAULT NULL COMMENT '审核时间',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核备注',
  `is_recheck` int(1) NULL DEFAULT 0 COMMENT '是否是撤回之前的日志 0或者null为新数据 1：撤回之前的数据',
  `order_id` int(30) NULL DEFAULT NULL COMMENT '排序id',
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '审核日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_oa_examine_log
-- ----------------------------

-- ----------------------------
-- Table structure for wk_oa_examine_record
-- ----------------------------
DROP TABLE IF EXISTS `wk_oa_examine_record`;
CREATE TABLE `wk_oa_examine_record`  (
  `record_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '审核记录ID',
  `examine_id` int(11) NULL DEFAULT NULL COMMENT '审批ID',
  `examine_step_id` bigint(20) NULL DEFAULT NULL COMMENT '当前进行的审批步骤ID',
  `examine_status` int(1) NULL DEFAULT NULL COMMENT '审核状态 0 未审核 1 审核通过 2 审核拒绝 3 审核中 4 已撤回',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核备注',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '审核记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_oa_examine_record
-- ----------------------------

-- ----------------------------
-- Table structure for wk_oa_examine_relation
-- ----------------------------
DROP TABLE IF EXISTS `wk_oa_examine_relation`;
CREATE TABLE `wk_oa_examine_relation`  (
  `r_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '审批关联业务表',
  `examine_id` int(11) NULL DEFAULT NULL COMMENT '审批ID',
  `customer_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户IDs',
  `contacts_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人IDs',
  `business_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商机IDs',
  `contract_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合同IDs',
  `status` int(2) NULL DEFAULT 1 COMMENT '状态1可用',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`r_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '审批关联业务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_oa_examine_relation
-- ----------------------------

-- ----------------------------
-- Table structure for wk_oa_examine_sort
-- ----------------------------
DROP TABLE IF EXISTS `wk_oa_examine_sort`;
CREATE TABLE `wk_oa_examine_sort`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `category_id` int(11) NULL DEFAULT NULL COMMENT '审批类型id',
  `sort` int(6) NULL DEFAULT NULL COMMENT '排序',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '审批类型排序' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_oa_examine_sort
-- ----------------------------

-- ----------------------------
-- Table structure for wk_oa_examine_step
-- ----------------------------
DROP TABLE IF EXISTS `wk_oa_examine_step`;
CREATE TABLE `wk_oa_examine_step`  (
  `step_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `step_type` int(1) NULL DEFAULT NULL COMMENT '步骤类型1、负责人主管，2、指定用户（任意一人），3、指定用户（多人会签）,4、上一级审批人主管',
  `category_id` int(11) NOT NULL COMMENT '审批ID',
  `check_user_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批人ID (使用逗号隔开) ,1,2,',
  `step_num` int(2) NULL DEFAULT 1 COMMENT '排序',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`step_id`) USING BTREE,
  INDEX `category_id`(`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '审批步骤表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_oa_examine_step
-- ----------------------------

-- ----------------------------
-- Table structure for wk_oa_examine_travel
-- ----------------------------
DROP TABLE IF EXISTS `wk_oa_examine_travel`;
CREATE TABLE `wk_oa_examine_travel`  (
  `travel_id` int(11) NOT NULL AUTO_INCREMENT,
  `examine_id` int(11) NOT NULL COMMENT '审批ID',
  `start_address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出发地',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '出发时间',
  `end_address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目的地',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '到达时间',
  `traffic` decimal(18, 2) NULL DEFAULT NULL COMMENT '交通费',
  `stay` decimal(18, 2) NULL DEFAULT NULL COMMENT '住宿费',
  `diet` decimal(18, 2) NULL DEFAULT NULL COMMENT '餐饮费',
  `other` decimal(18, 2) NULL DEFAULT NULL COMMENT '其他费用',
  `money` decimal(18, 2) NULL DEFAULT NULL COMMENT '金额',
  `vehicle` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交通工具',
  `trip` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单程往返（单程、往返）',
  `duration` decimal(18, 2) NULL DEFAULT NULL COMMENT '时长',
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `batch_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次id',
  PRIMARY KEY (`travel_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '差旅行程表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_oa_examine_travel
-- ----------------------------

-- ----------------------------
-- Table structure for wk_oa_log
-- ----------------------------
DROP TABLE IF EXISTS `wk_oa_log`;
CREATE TABLE `wk_oa_log`  (
  `log_id` int(11) NOT NULL AUTO_INCREMENT,
  `category_id` int(2) NOT NULL DEFAULT 1 COMMENT '日志类型（1日报，2周报，3月报）',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '日志内容',
  `tomorrow` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '明日工作内容',
  `question` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '遇到问题',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `send_user_ids` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通知人',
  `send_dept_ids` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通知部门',
  `read_user_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '已读人',
  `batch_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件批次ID',
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '工作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_oa_log
-- ----------------------------

-- ----------------------------
-- Table structure for wk_oa_log_bulletin
-- ----------------------------
DROP TABLE IF EXISTS `wk_oa_log_bulletin`;
CREATE TABLE `wk_oa_log_bulletin`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `log_id` int(11) NULL DEFAULT NULL COMMENT '日志ID',
  `type` int(1) NULL DEFAULT NULL COMMENT '关联类型 1 客户 2 商机 3 合同 4 回款 5 跟进记录 ',
  `type_id` int(11) NULL DEFAULT NULL COMMENT '类型ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '工作日志与业务ID关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_oa_log_bulletin
-- ----------------------------

-- ----------------------------
-- Table structure for wk_oa_log_record
-- ----------------------------
DROP TABLE IF EXISTS `wk_oa_log_record`;
CREATE TABLE `wk_oa_log_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `log_id` int(11) NOT NULL,
  `customer_num` int(5) NULL DEFAULT 0 COMMENT '客户数量',
  `business_num` int(11) NULL DEFAULT 0 COMMENT '商机数量',
  `contract_num` int(11) NULL DEFAULT 0 COMMENT '合同数量',
  `receivables_money` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '回款金额',
  `activity_num` int(11) NULL DEFAULT 0 COMMENT '跟进记录',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '日志关联销售简报表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_oa_log_record
-- ----------------------------

-- ----------------------------
-- Table structure for wk_oa_log_relation
-- ----------------------------
DROP TABLE IF EXISTS `wk_oa_log_relation`;
CREATE TABLE `wk_oa_log_relation`  (
  `r_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '日志关联业务表',
  `log_id` int(11) NULL DEFAULT NULL COMMENT '日志ID',
  `customer_ids` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户IDs',
  `contacts_ids` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人IDs',
  `business_ids` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商机IDs',
  `contract_ids` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合同IDs',
  `status` int(2) NULL DEFAULT NULL COMMENT '状态1可用',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`r_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '日志关联业务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_oa_log_relation
-- ----------------------------

-- ----------------------------
-- Table structure for wk_oa_log_rule
-- ----------------------------
DROP TABLE IF EXISTS `wk_oa_log_rule`;
CREATE TABLE `wk_oa_log_rule`  (
  `rule_id` int(11) NOT NULL AUTO_INCREMENT,
  `status` int(2) NOT NULL DEFAULT 1 COMMENT '状态 0停用 1启用',
  `member_user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '需要提交的员工id，“,”分割',
  `type` int(2) NULL DEFAULT NULL COMMENT '日志类型 1日报 2周报 3月报',
  `effective_day` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '每周需要统计的日期 1-6是周一到周六 7是周日',
  `start_day` int(2) NULL DEFAULT NULL COMMENT '开始日期',
  `start_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开始时间',
  `end_day` int(2) NULL DEFAULT NULL COMMENT '结束日期',
  `end_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结束时间',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`rule_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 268 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'oa日志规则表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_oa_log_rule
-- ----------------------------
INSERT INTO `wk_oa_log_rule` VALUES (265, 1, NULL, 1, '1,2,3,4,5', NULL, '00:00', NULL, '23:00', 0, '2020-08-22 11:40:42');
INSERT INTO `wk_oa_log_rule` VALUES (266, 1, NULL, 2, NULL, 1, NULL, 7, NULL, 0, '2020-08-22 11:40:42');
INSERT INTO `wk_oa_log_rule` VALUES (267, 1, NULL, 3, NULL, 1, NULL, 31, NULL, 0, '2020-08-22 11:40:42');

-- ----------------------------
-- Table structure for wk_work
-- ----------------------------
DROP TABLE IF EXISTS `wk_work`;
CREATE TABLE `wk_work`  (
  `work_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '项目ID',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目名字',
  `status` int(2) NULL DEFAULT 1 COMMENT '状态 1启用 3归档 2 删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `description` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '描述',
  `color` varchar(15) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '颜色',
  `is_open` int(2) NOT NULL DEFAULT 1 COMMENT '是否所有人可见 1 是 0 否',
  `owner_role` int(20) NULL DEFAULT NULL COMMENT '公开项目成员角色id',
  `archive_time` datetime(0) NULL DEFAULT NULL COMMENT '归档时间',
  `delete_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
  `is_system_cover` int(2) NULL DEFAULT NULL COMMENT '是否是系统自带封面 0不是 1是',
  `cover_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目封面路径 仅系统自带封面需要',
  `batch_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `owner_user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目成员',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`work_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '项目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_work
-- ----------------------------

-- ----------------------------
-- Table structure for wk_work_collect
-- ----------------------------
DROP TABLE IF EXISTS `wk_work_collect`;
CREATE TABLE `wk_work_collect`  (
  `collect_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '项目收藏id',
  `work_id` int(11) NOT NULL COMMENT '项目id',
  `user_id` bigint(11) NOT NULL COMMENT '用户id',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`collect_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '项目收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_work_collect
-- ----------------------------

-- ----------------------------
-- Table structure for wk_work_order
-- ----------------------------
DROP TABLE IF EXISTS `wk_work_order`;
CREATE TABLE `wk_work_order`  (
  `order_id` int(11) NOT NULL AUTO_INCREMENT,
  `work_id` int(11) NOT NULL,
  `user_id` bigint(11) NOT NULL,
  `order_num` int(4) NOT NULL DEFAULT 999,
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '项目排序表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_work_order
-- ----------------------------

-- ----------------------------
-- Table structure for wk_work_task
-- ----------------------------
DROP TABLE IF EXISTS `wk_work_task`;
CREATE TABLE `wk_work_task`  (
  `task_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '任务表',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `main_user_id` bigint(20) NULL DEFAULT NULL COMMENT '负责人ID',
  `owner_user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '团队成员ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '新建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `status` int(2) NULL DEFAULT 1 COMMENT '完成状态 1正在进行2延期3归档 5结束',
  `class_id` int(5) NULL DEFAULT -1 COMMENT '分类id',
  `label_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签 ,号拼接',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '描述',
  `pid` int(11) NULL DEFAULT 0 COMMENT '上级ID',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `stop_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `priority` int(2) NULL DEFAULT 0 COMMENT '优先级 从大到小 3高 2中 1低 0无',
  `work_id` int(11) NULL DEFAULT 0 COMMENT '项目ID',
  `is_top` int(2) NULL DEFAULT 0 COMMENT '工作台展示 0收件箱 1今天要做，2下一步要做，3以后要做',
  `is_open` int(2) NULL DEFAULT 1 COMMENT '是否公开',
  `order_num` int(4) NULL DEFAULT 999 COMMENT '排序ID',
  `top_order_num` int(4) NULL DEFAULT 999 COMMENT '我的任务排序ID',
  `archive_time` datetime(0) NULL DEFAULT NULL COMMENT '归档时间',
  `ishidden` int(2) NULL DEFAULT 0 COMMENT '是否删除 0 未删除 1 删除',
  `hidden_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
  `batch_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次',
  `is_archive` int(2) NULL DEFAULT 0 COMMENT '1归档',
  PRIMARY KEY (`task_id`) USING BTREE,
  INDEX `pid`(`pid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '任务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_work_task
-- ----------------------------

-- ----------------------------
-- Table structure for wk_work_task_class
-- ----------------------------
DROP TABLE IF EXISTS `wk_work_task_class`;
CREATE TABLE `wk_work_task_class`  (
  `class_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '任务分类表',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类名',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `status` int(2) NULL DEFAULT 0 COMMENT '状态1正常',
  `work_id` int(11) NULL DEFAULT NULL COMMENT '项目ID',
  `order_num` int(4) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`class_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '任务分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_work_task_class
-- ----------------------------

-- ----------------------------
-- Table structure for wk_work_task_comment
-- ----------------------------
DROP TABLE IF EXISTS `wk_work_task_comment`;
CREATE TABLE `wk_work_task_comment`  (
  `comment_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '评论表',
  `user_id` bigint(20) NOT NULL COMMENT '评论人ID',
  `content` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容(答案)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '新建时间',
  `main_id` int(11) NULL DEFAULT 0 COMMENT '主评论的id',
  `pid` bigint(20) NULL DEFAULT 0 COMMENT '回复对象ID',
  `status` int(2) NULL DEFAULT 1 COMMENT '状态 ',
  `type_id` int(11) NULL DEFAULT 0 COMMENT '评论项目任务ID 或评论其他模块ID',
  `favour` int(7) NULL DEFAULT 0 COMMENT '赞',
  `type` int(2) NULL DEFAULT 0 COMMENT '评论分类 1：任务评论  2：日志评论',
  PRIMARY KEY (`comment_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '任务评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_work_task_comment
-- ----------------------------

-- ----------------------------
-- Table structure for wk_work_task_label
-- ----------------------------
DROP TABLE IF EXISTS `wk_work_task_label`;
CREATE TABLE `wk_work_task_label`  (
  `label_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签名',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `status` int(2) NULL DEFAULT 0 COMMENT '状态',
  `color` varchar(15) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '颜色',
  PRIMARY KEY (`label_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '任务标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_work_task_label
-- ----------------------------

-- ----------------------------
-- Table structure for wk_work_task_label_order
-- ----------------------------
DROP TABLE IF EXISTS `wk_work_task_label_order`;
CREATE TABLE `wk_work_task_label_order`  (
  `order_id` int(11) NOT NULL AUTO_INCREMENT,
  `label_id` int(11) NOT NULL,
  `user_id` bigint(11) NOT NULL,
  `order_num` int(4) NOT NULL DEFAULT 999,
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '项目标签排序表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_work_task_label_order
-- ----------------------------

-- ----------------------------
-- Table structure for wk_work_task_log
-- ----------------------------
DROP TABLE IF EXISTS `wk_work_task_log`;
CREATE TABLE `wk_work_task_log`  (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '项目日志表',
  `user_id` bigint(20) NOT NULL COMMENT '操作人ID',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `status` int(2) NULL DEFAULT 0 COMMENT '状态 4删除',
  `task_id` int(11) NULL DEFAULT 0 COMMENT '任务ID',
  `work_id` int(11) NULL DEFAULT 0 COMMENT '项目ID',
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '任务日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_work_task_log
-- ----------------------------

-- ----------------------------
-- Table structure for wk_work_task_relation
-- ----------------------------
DROP TABLE IF EXISTS `wk_work_task_relation`;
CREATE TABLE `wk_work_task_relation`  (
  `r_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '任务关联业务表',
  `task_id` int(11) NULL DEFAULT NULL COMMENT '任务ID',
  `customer_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户IDs',
  `contacts_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人IDs',
  `business_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商机IDs',
  `contract_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合同IDs',
  `status` int(2) NULL DEFAULT NULL COMMENT '状态1可用',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`r_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '任务关联业务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_work_task_relation
-- ----------------------------

-- ----------------------------
-- Table structure for wk_work_user
-- ----------------------------
DROP TABLE IF EXISTS `wk_work_user`;
CREATE TABLE `wk_work_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `work_id` int(11) NOT NULL COMMENT '项目ID',
  `user_id` bigint(20) NOT NULL COMMENT '成员ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '项目成员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_work_user
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
