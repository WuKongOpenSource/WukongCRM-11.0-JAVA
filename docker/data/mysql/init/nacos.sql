/*
 Navicat Premium Data Transfer

 Source Server         : 47.104.76.88
 Source Server Type    : MySQL
 Source Server Version : 50731
 Source Host           : 47.104.76.88:3306
 Source Schema         : nacos

 Target Server Type    : MySQL
 Target Server Version : 50731
 File Encoding         : 65001

 Date: 13/10/2020 09:44:32
*/
create database nacos character set utf8mb4 collate utf8mb4_general_ci;
use nacos;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `c_use` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `effect` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `c_schema` text CHARACTER SET utf8 COLLATE utf8_bin NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfo_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info` VALUES (1, 'service.vgroupMapping.crm_tx_group', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2020-10-12 09:06:43', '2020-10-12 09:10:53', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (2, 'service.vgroupMapping.admin_tx_group', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2020-10-12 09:06:43', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (3, 'store.mode', 'SEATA_GROUP', 'db', 'd77d5e503ad1439f585ac494268b351b', '2020-10-12 09:06:43', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (4, 'store.db.datasource', 'SEATA_GROUP', 'druid', '3d650fb8a5df01600281d48c47c9fa60', '2020-10-12 09:06:43', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (5, 'store.db.dbType', 'SEATA_GROUP', 'mysql', '81c3b080dad537de7e10e0987a4bf52e', '2020-10-12 09:06:43', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (6, 'store.db.driverClassName', 'SEATA_GROUP', 'com.mysql.jdbc.Driver', '683cf0c3a5a56cec94dfac94ca16d760', '2020-10-12 09:06:43', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (7, 'store.db.url', 'SEATA_GROUP', 'jdbc:mysql://mysql:3306/seata?useUnicode=true', '3c0c8ba10c3daee50a2db1dd0f397282', '2020-10-12 09:06:43', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (8, 'store.db.user', 'SEATA_GROUP', 'root', '63a9f0ea7bb98050796b649e85481845', '2020-10-12 09:06:44', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (9, 'store.db.password', 'SEATA_GROUP', 'password', '5f4dcc3b5aa765d61d8327deb882cf99', '2020-10-12 09:06:44', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (10, 'store.db.minConn', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2020-10-12 09:06:44', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (11, 'store.db.maxConn', 'SEATA_GROUP', '30', '34173cb38f07f89ddbebc2ac9128303f', '2020-10-12 09:06:44', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (12, 'store.db.globalTable', 'SEATA_GROUP', 'global_table', '8b28fb6bb4c4f984df2709381f8eba2b', '2020-10-12 09:06:44', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (13, 'store.db.branchTable', 'SEATA_GROUP', 'branch_table', '54bcdac38cf62e103fe115bcf46a660c', '2020-10-12 09:06:44', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (14, 'store.db.queryLimit', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2020-10-12 09:06:44', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (15, 'store.db.lockTable', 'SEATA_GROUP', 'lock_table', '55e0cae3b6dc6696b768db90098b8f2f', '2020-10-12 09:06:44', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (16, 'store.db.maxWait', 'SEATA_GROUP', '5000', 'a35fe7f7fe8217b4369a0af4244d1fca', '2020-10-12 09:06:44', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (17, 'service.vgroupMapping.oa_tx_group', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2020-10-12 09:06:43', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (18, 'service.vgroupMapping.examine_tx_group', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2020-10-12 09:06:43', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'datum_id',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime(0) NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfoaggr_datagrouptenantdatum`(`data_id`, `group_id`, `tenant_id`, `datum_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '增加租户字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfobeta_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_beta' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_route
-- ----------------------------
DROP TABLE IF EXISTS `config_info_route`;
CREATE TABLE `config_info_route`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `route_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '路由id',
  `uri` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'uri路径',
  `predicates` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '判定器',
  `filters` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '过滤器',
  `orders` int(11) NULL DEFAULT NULL COMMENT '排序',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `intercept` int(1) NOT NULL DEFAULT 1 COMMENT '是否拦截 1 是 0 否',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '状态：Y-有效，N-无效',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ux_gateway_routes_uri`(`uri`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 120 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '网关路由表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_route
-- ----------------------------
INSERT INTO `config_info_route` VALUES (101, 'authorization', 'lb://authorization', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/authorization*/**\"}}]', '[]', 100, '用户认证相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (102, 'admin', 'lb://admin', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/admin*/**\"}}]', '[]', 100, '系统管理相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (103, 'login', 'lb://authorization/login', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/login\"}}]', '[]', 100, '用户登录相关接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (104, 'logout', 'lb://authorization/logout', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/logout\"}}]', '[]', 100, '用户退出相关接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (105, 'reLogin', 'lb://authorization/reLogin', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/reLogin\"}}]', '[]', 100, '用户重新登录相关接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (106, 'crm', 'lb://crm', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/crm*/**\"}}]', '[]', 100, '客户管理相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (109, 'work', 'lb://work', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/work*/**\"}}]', '[]', 100, '项目管理相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (113, 'oa', 'lb://oa', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/oa*/**\"}}]', '[]', 100, 'OA相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (116, 'bi', 'lb://bi', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/bi*/**\"}}]', '[]', 100, '商业智能相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (117, 'file', 'http://127.0.0.1:8012/onlinePreview', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/onlinePreview\"}}]', '[]', 100, '文件预览相关接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (118, 'queryShareUrl', 'lb://km', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/documentShare/queryShareUrl/*\"}}]', '[]', 100, '知识库分享接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (119, 'crmCallUpload', 'lb://crmCall/upload', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"crmCall/upload/*\"}}]', '[]', 100, '呼叫中心上传接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (123, 'permission', 'lb://authorization/permission', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/permission\"}}]', '[]', 100, '用户权限验证接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (124, 'examine', 'lb://examine', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/examine*/**\"}}]', '[]', 100, '审批相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');


-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfotag_datagrouptenanttag`(`data_id`, `group_id`, `tenant_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_tag' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE INDEX `uk_configtagrelation_configidtag`(`id`, `tag_name`, `tag_type`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_tag_relation' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_group_id`(`group_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '集群、各Group容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of group_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`  (
  `id` bigint(64) UNSIGNED NOT NULL,
  `nid` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `op_type` char(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`nid`) USING BTREE,
  INDEX `idx_gmt_create`(`gmt_create`) USING BTREE,
  INDEX `idx_gmt_modified`(`gmt_modified`) USING BTREE,
  INDEX `idx_did`(`data_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '多租户改造' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
INSERT INTO `his_config_info` VALUES (0, 1, 'service.vgroupMapping.crm_tx_group', 'SEATA_GROUP', '', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2020-10-12 09:06:43', '2020-10-12 09:06:43', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 2, 'service.vgroupMapping.admin_tx_group', 'SEATA_GROUP', '', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2020-10-12 09:06:43', '2020-10-12 09:06:43', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 3, 'store.mode', 'SEATA_GROUP', '', 'db', 'd77d5e503ad1439f585ac494268b351b', '2020-10-12 09:06:43', '2020-10-12 09:06:43', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 4, 'store.db.datasource', 'SEATA_GROUP', '', 'druid', '3d650fb8a5df01600281d48c47c9fa60', '2020-10-12 09:06:43', '2020-10-12 09:06:43', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 5, 'store.db.dbType', 'SEATA_GROUP', '', 'mysql', '81c3b080dad537de7e10e0987a4bf52e', '2020-10-12 09:06:43', '2020-10-12 09:06:43', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 6, 'store.db.driverClassName', 'SEATA_GROUP', '', 'com.mysql.jdbc.Driver', '683cf0c3a5a56cec94dfac94ca16d760', '2020-10-12 09:06:43', '2020-10-12 09:06:43', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 7, 'store.db.url', 'SEATA_GROUP', '', 'jdbc:mysql://127.0.0.1:3306/seata?useUnicode=true', 'cbb3bd573704f125fb4f2489208abaec', '2020-10-12 09:06:43', '2020-10-12 09:06:43', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 8, 'store.db.user', 'SEATA_GROUP', '', 'root', '63a9f0ea7bb98050796b649e85481845', '2020-10-12 09:06:43', '2020-10-12 09:06:44', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 9, 'store.db.password', 'SEATA_GROUP', '', 'password', '5f4dcc3b5aa765d61d8327deb882cf99', '2020-10-12 09:06:43', '2020-10-12 09:06:44', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 10, 'store.db.minConn', 'SEATA_GROUP', '', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2020-10-12 09:06:43', '2020-10-12 09:06:44', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 11, 'store.db.maxConn', 'SEATA_GROUP', '', '30', '34173cb38f07f89ddbebc2ac9128303f', '2020-10-12 09:06:43', '2020-10-12 09:06:44', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 12, 'store.db.globalTable', 'SEATA_GROUP', '', 'global_table', '8b28fb6bb4c4f984df2709381f8eba2b', '2020-10-12 09:06:43', '2020-10-12 09:06:44', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 13, 'store.db.branchTable', 'SEATA_GROUP', '', 'branch_table', '54bcdac38cf62e103fe115bcf46a660c', '2020-10-12 09:06:43', '2020-10-12 09:06:44', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 14, 'store.db.queryLimit', 'SEATA_GROUP', '', '100', 'f899139df5e1059396431415e770c6dd', '2020-10-12 09:06:43', '2020-10-12 09:06:44', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 15, 'store.db.lockTable', 'SEATA_GROUP', '', 'lock_table', '55e0cae3b6dc6696b768db90098b8f2f', '2020-10-12 09:06:43', '2020-10-12 09:06:44', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 16, 'store.db.maxWait', 'SEATA_GROUP', '', '5000', 'a35fe7f7fe8217b4369a0af4244d1fca', '2020-10-12 09:06:43', '2020-10-12 09:06:44', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (1, 17, 'service.vgroupMapping.crm_tx_group', 'SEATA_GROUP', '', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2020-10-12 09:10:53', '2020-10-12 09:10:53', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (2, 18, 'service.vgroupMapping.admin_tx_group', 'SEATA_GROUP', '', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2020-10-12 09:10:53', '2020-10-12 09:10:54', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (3, 19, 'store.mode', 'SEATA_GROUP', '', 'db', 'd77d5e503ad1439f585ac494268b351b', '2020-10-12 09:10:53', '2020-10-12 09:10:54', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (4, 20, 'store.db.datasource', 'SEATA_GROUP', '', 'druid', '3d650fb8a5df01600281d48c47c9fa60', '2020-10-12 09:10:53', '2020-10-12 09:10:54', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (5, 21, 'store.db.dbType', 'SEATA_GROUP', '', 'mysql', '81c3b080dad537de7e10e0987a4bf52e', '2020-10-12 09:10:53', '2020-10-12 09:10:54', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (6, 22, 'store.db.driverClassName', 'SEATA_GROUP', '', 'com.mysql.jdbc.Driver', '683cf0c3a5a56cec94dfac94ca16d760', '2020-10-12 09:10:53', '2020-10-12 09:10:54', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (7, 23, 'store.db.url', 'SEATA_GROUP', '', 'jdbc:mysql://127.0.0.1:3306/seata?useUnicode=true', 'cbb3bd573704f125fb4f2489208abaec', '2020-10-12 09:10:53', '2020-10-12 09:10:54', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (8, 24, 'store.db.user', 'SEATA_GROUP', '', 'root', '63a9f0ea7bb98050796b649e85481845', '2020-10-12 09:10:53', '2020-10-12 09:10:54', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (9, 25, 'store.db.password', 'SEATA_GROUP', '', 'password', '5f4dcc3b5aa765d61d8327deb882cf99', '2020-10-12 09:10:53', '2020-10-12 09:10:54', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (10, 26, 'store.db.minConn', 'SEATA_GROUP', '', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2020-10-12 09:10:53', '2020-10-12 09:10:54', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (11, 27, 'store.db.maxConn', 'SEATA_GROUP', '', '30', '34173cb38f07f89ddbebc2ac9128303f', '2020-10-12 09:10:53', '2020-10-12 09:10:54', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (12, 28, 'store.db.globalTable', 'SEATA_GROUP', '', 'global_table', '8b28fb6bb4c4f984df2709381f8eba2b', '2020-10-12 09:10:53', '2020-10-12 09:10:54', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (13, 29, 'store.db.branchTable', 'SEATA_GROUP', '', 'branch_table', '54bcdac38cf62e103fe115bcf46a660c', '2020-10-12 09:10:53', '2020-10-12 09:10:54', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (14, 30, 'store.db.queryLimit', 'SEATA_GROUP', '', '100', 'f899139df5e1059396431415e770c6dd', '2020-10-12 09:10:53', '2020-10-12 09:10:54', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (15, 31, 'store.db.lockTable', 'SEATA_GROUP', '', 'lock_table', '55e0cae3b6dc6696b768db90098b8f2f', '2020-10-12 09:10:54', '2020-10-12 09:10:54', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (16, 32, 'store.db.maxWait', 'SEATA_GROUP', '', '5000', 'a35fe7f7fe8217b4369a0af4244d1fca', '2020-10-12 09:10:54', '2020-10-12 09:10:54', NULL, '127.0.0.1', 'U', '');

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`  (
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `resource` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `action` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE INDEX `uk_role_permission`(`role`, `resource`, `action`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permissions
-- ----------------------------

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE INDEX `idx_user_role`(`username`, `role`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES ('nacos', 'ROLE_ADMIN');

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数',
  `max_aggr_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '租户容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_info_kptenantid`(`kp`, `tenant_id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'tenant_info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tenant_info
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);

SET FOREIGN_KEY_CHECKS = 1;
