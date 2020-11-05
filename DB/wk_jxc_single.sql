/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50720
 Source Host           : localhost:3306
 Source Schema         : wk_jxc_single

 Target Server Type    : MySQL
 Target Server Version : 50720
 File Encoding         : 65001

 Date: 24/09/2020 14:06:55
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for wk_jxc_allocation
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_allocation`;
CREATE TABLE `wk_jxc_allocation`  (
  `allocation_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `allocation_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '调拨名称',
  `original_warehouse_id` int(11) NULL DEFAULT NULL COMMENT '原仓库',
  `current_warehouse_id` int(255) NULL DEFAULT NULL COMMENT '现仓库',
  `allocation_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `allocation_time` datetime(0) NULL DEFAULT NULL,
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '1' COMMENT '0 删除 1 不删除 2正在审核 3 审核完成',
  `allocation_state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '1' COMMENT '0作废 1调拨中 2已调拨',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `owner_user_id` int(11) NULL DEFAULT NULL COMMENT '负责人',
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注',
  `record_id` int(11) NULL DEFAULT NULL COMMENT '审核记录Id',
  PRIMARY KEY (`allocation_id`) USING BTREE,
  UNIQUE INDEX `allocation_id`(`allocation_id`) USING BTREE,
  UNIQUE INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `original_warehouse_id`(`original_warehouse_id`) USING BTREE,
  INDEX `current_warehouse_id`(`current_warehouse_id`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '调拨表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_allocation
-- ----------------------------

-- ----------------------------
-- Table structure for wk_jxc_allocation_data
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_allocation_data`;
CREATE TABLE `wk_jxc_allocation_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '调拨扩展字段数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_allocation_data
-- ----------------------------

-- ----------------------------
-- Table structure for wk_jxc_allocation_product
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_allocation_product`;
CREATE TABLE `wk_jxc_allocation_product`  (
  `allocation_product_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `product_id` int(11) NULL DEFAULT NULL COMMENT '仓库规格id',
  `allocation_number` int(11) NULL DEFAULT NULL COMMENT '调拨数量',
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `remarks` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注',
  PRIMARY KEY (`allocation_product_id`) USING BTREE,
  UNIQUE INDEX `allocation_product_id`(`allocation_product_id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `product_id`(`product_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '调拨产品关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_allocation_product
-- ----------------------------
INSERT INTO `wk_jxc_allocation_product` VALUES (21, 130, 7, '9f60ac15458d48629d6cf08d7b6ddb4f', '');
INSERT INTO `wk_jxc_allocation_product` VALUES (23, 130, 12, 'a64697495a7b4280a28dbe5c854bc8a3', '');

-- ----------------------------
-- Table structure for wk_jxc_bookkeeping
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_bookkeeping`;
CREATE TABLE `wk_jxc_bookkeeping`  (
  `bookkeeping_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '0 付款 1 回款',
  `amount_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '记账金额',
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '1' COMMENT '0 删除 1不删',
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注',
  PRIMARY KEY (`bookkeeping_id`) USING BTREE,
  UNIQUE INDEX `bookkeeping_id`(`bookkeeping_id`) USING BTREE,
  INDEX `type`(`type`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '记账表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_bookkeeping
-- ----------------------------
INSERT INTO `wk_jxc_bookkeeping` VALUES (21, '1', 3213.00, '1', '0268bf87be5e4d87a6932ca1e7ab0585', '2020-09-17 11:04:24', NULL, NULL);
INSERT INTO `wk_jxc_bookkeeping` VALUES (22, '0', 3213.00, '1', 'd20bff49f3de4416820b424031c3fd3d', '2020-09-17 11:04:51', NULL, NULL);
INSERT INTO `wk_jxc_bookkeeping` VALUES (23, '0', 3213.00, '1', '9bb353db78f74e908b8d464509617e81', '2020-09-17 11:05:13', NULL, NULL);

-- ----------------------------
-- Table structure for wk_jxc_collection
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_collection`;
CREATE TABLE `wk_jxc_collection`  (
  `collection_note_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `collection_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '回款编号',
  `collection_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '回款类型',
  `related_id` int(11) NULL DEFAULT NULL COMMENT '关联单号',
  `collection_amount` decimal(11, 2) NULL DEFAULT NULL COMMENT '回款金额',
  `collection_object` int(11) NULL DEFAULT NULL COMMENT '回款供应商',
  `collection_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '回款方式',
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '1' COMMENT '0 删除 1不删除2正在审核3审核完成',
  `order_time` datetime(0) NULL DEFAULT NULL COMMENT '下单时间',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注',
  `owner_user_id` int(11) NULL DEFAULT NULL,
  `record_id` int(11) NULL DEFAULT NULL COMMENT '审核记录Id',
  PRIMARY KEY (`collection_note_id`) USING BTREE,
  UNIQUE INDEX `collection_note_id`(`collection_note_id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '回款表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_collection
-- ----------------------------
INSERT INTO `wk_jxc_collection` VALUES (36, '555555555555555555', '采购退货', 17, 3213.00, 26, '邮政汇款', '3', '2020-09-22 00:00:00', 14773, 14773, '0268bf87be5e4d87a6932ca1e7ab0585', '2020-09-17 10:47:59', '2020-09-17 11:04:18', '4565', 14773, 16);
INSERT INTO `wk_jxc_collection` VALUES (37, '456', '其他', NULL, 0.00, NULL, '现金', '2', '2020-09-01 00:00:00', 14773, NULL, 'c7176411f9434259a36b72d85de02abb', '2020-09-17 11:03:58', '2020-09-17 11:03:58', '546', 14773, 14);
INSERT INTO `wk_jxc_collection` VALUES (38, '546', '销售', 26, 232.00, 138, '现金', '2', '2020-09-28 00:00:00', 14773, NULL, '1836ba9ce1f14676b0baef79bac3417d', '2020-09-17 11:06:40', '2020-09-17 11:06:40', '', 14773, 21);

-- ----------------------------
-- Table structure for wk_jxc_collection_data
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_collection_data`;
CREATE TABLE `wk_jxc_collection_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '回款扩展字段数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_collection_data
-- ----------------------------

-- ----------------------------
-- Table structure for wk_jxc_detailed
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_detailed`;
CREATE TABLE `wk_jxc_detailed`  (
  `detailed_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `product_id` int(11) NULL DEFAULT NULL COMMENT '产品Id',
  `warehouse_id` int(11) NULL DEFAULT NULL COMMENT '仓库ID',
  `detailed_time` datetime(0) NULL DEFAULT NULL COMMENT '出入库时间',
  `order_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `op_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '操作数量',
  `current_number` int(11) NULL DEFAULT 0 COMMENT '当前数量',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '1.初始化库存入库 2 采购入库 3 销售退货入库 4 其他入库 5 入库作废出库 6 销售出库 7采购退货出库 9 其他出库 10  出库作废入库  11 盘点人库 12 盘点出库 13 盘点作废入库 14 盘点作废出库 15 调拨入库 16 调拨出库 17调拨作废入库 18  调拨作废出库',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `owner_user_id` int(11) NULL DEFAULT NULL COMMENT '负责人',
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注',
  PRIMARY KEY (`detailed_id`) USING BTREE,
  UNIQUE INDEX `detailed_id`(`detailed_id`) USING BTREE,
  INDEX `warehouse_id`(`warehouse_id`) USING BTREE,
  INDEX `product_id`(`product_id`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 389 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '出入库明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_detailed
-- ----------------------------
INSERT INTO `wk_jxc_detailed` VALUES (373, 130, 25, '2020-09-01 00:00:00', '5464', '+1', 1, '2', NULL, NULL, 14773, 'eb2c73e1dbc64e79aebf1c86e60dac4b', '2020-09-17 10:45:47', NULL, NULL);
INSERT INTO `wk_jxc_detailed` VALUES (374, 130, 25, '2020-09-09 00:00:00', '5555554646', '-1', 0, '7', NULL, NULL, 14773, '2d86cf1c14624e2fa43fb9585b9e04d5', '2020-09-17 10:47:23', NULL, NULL);
INSERT INTO `wk_jxc_detailed` VALUES (375, 125, 25, '2020-08-31 00:00:00', '546', '+2', 2, '11', NULL, NULL, 14773, '4b8c990774314dc4a4b707f12cc80f61', '2020-09-17 10:48:58', NULL, NULL);
INSERT INTO `wk_jxc_detailed` VALUES (376, 126, 25, '2020-08-31 00:00:00', '546', '+2', 2, '11', NULL, NULL, 14773, '4b8c990774314dc4a4b707f12cc80f61', '2020-09-17 10:48:58', NULL, NULL);
INSERT INTO `wk_jxc_detailed` VALUES (377, 125, 25, '2020-09-17 10:49:02', '546', '-2', 0, '13', NULL, NULL, 14773, '4b8c990774314dc4a4b707f12cc80f61', '2020-09-17 10:49:02', NULL, NULL);
INSERT INTO `wk_jxc_detailed` VALUES (378, 126, 25, '2020-09-17 10:49:02', '546', '-2', 0, '13', NULL, NULL, 14773, '4b8c990774314dc4a4b707f12cc80f61', '2020-09-17 10:49:02', NULL, NULL);
INSERT INTO `wk_jxc_detailed` VALUES (379, 125, 25, '2020-09-15 00:00:00', '12', '+2', 2, '11', NULL, NULL, 14773, '48ebc7983a7043d4995dc23c3b72b26b', '2020-09-17 10:59:24', NULL, NULL);
INSERT INTO `wk_jxc_detailed` VALUES (380, 126, 25, '2020-09-15 00:00:00', '12', '+2', 2, '11', NULL, NULL, 14773, '48ebc7983a7043d4995dc23c3b72b26b', '2020-09-17 10:59:24', NULL, NULL);
INSERT INTO `wk_jxc_detailed` VALUES (381, 125, 25, '2020-09-17 10:59:31', '12', '-2', 0, '13', NULL, NULL, 14773, '48ebc7983a7043d4995dc23c3b72b26b', '2020-09-17 10:59:31', NULL, NULL);
INSERT INTO `wk_jxc_detailed` VALUES (382, 126, 25, '2020-09-17 10:59:31', '12', '-2', 0, '13', NULL, NULL, 14773, '48ebc7983a7043d4995dc23c3b72b26b', '2020-09-17 10:59:31', NULL, NULL);
INSERT INTO `wk_jxc_detailed` VALUES (383, 130, 25, '2020-09-09 00:00:00', '4564', '+2000', 2000, '4', NULL, NULL, 14773, 'a611ae87671646b1b3ece94a25ccf736', '2020-09-17 11:01:22', NULL, NULL);
INSERT INTO `wk_jxc_detailed` VALUES (384, 130, 25, '2020-09-14 00:00:00', '5465465', '-20', 1980, '16', NULL, NULL, 14773, '9f60ac15458d48629d6cf08d7b6ddb4f', '2020-09-17 11:01:53', NULL, NULL);
INSERT INTO `wk_jxc_detailed` VALUES (385, 130, 26, '2020-09-17 11:02:44', '5465465', '+7', 7, '15', NULL, NULL, 14773, '9f60ac15458d48629d6cf08d7b6ddb4f', '2020-09-17 11:02:44', NULL, NULL);
INSERT INTO `wk_jxc_detailed` VALUES (386, 130, 25, '2020-09-15 00:00:00', '415214', '-10', 1970, '12', NULL, NULL, 14773, '20a514daafe445bdbdd7e7d82ffe9b00', '2020-09-17 11:13:45', NULL, NULL);
INSERT INTO `wk_jxc_detailed` VALUES (387, 130, 25, '2020-09-17 11:13:49', '415214', '+10', 1980, '14', NULL, NULL, 14773, '20a514daafe445bdbdd7e7d82ffe9b00', '2020-09-17 11:13:49', NULL, NULL);
INSERT INTO `wk_jxc_detailed` VALUES (388, 130, 25, '2020-09-08 00:00:00', '23121', '-20', 1960, '16', NULL, NULL, 14773, 'a64697495a7b4280a28dbe5c854bc8a3', '2020-09-17 11:14:51', NULL, NULL);

-- ----------------------------
-- Table structure for wk_jxc_field
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_field`;
CREATE TABLE `wk_jxc_field`  (
  `field_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `field_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '自定义字段英文标识',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '字段名称',
  `type` int(2) NOT NULL DEFAULT 1 COMMENT '字段类型 1 单行文本 2 多行文本 3 单选 4日期 5 数字 6 小数 7 手机  8 文件 9 多选 10 人员 11 附件 12 部门 13 日期时间 14 邮箱 29图片',
  `label` int(2) NOT NULL COMMENT '标签 1 产品 2 供应商 3 采购订单 4 采购退货单 5 销售订单 6 销售退货单 7入库单 8 出库单 9 付款单 10 回款单 11 盘点 12调拨',
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
  `examine_category_id` int(11) NULL DEFAULT NULL,
  `field_type` int(2) NOT NULL DEFAULT 0 COMMENT '字段来源  0.自定义 1.原始固定 2原始字段但值存在扩展表中',
  `relevant` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`field_id`) USING BTREE,
  UNIQUE INDEX `field_id`(`field_id`) USING BTREE,
  INDEX `label`(`label`) USING BTREE,
  INDEX `update_time`(`update_time`) USING BTREE,
  INDEX `sorting`(`sorting`) USING BTREE,
  INDEX `is_hidden`(`is_hidden`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1676 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '自定义字段表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_field
-- ----------------------------
INSERT INTO `wk_jxc_field` VALUES (1597, 'product_name', '产品名称', 1, 1, NULL, '', NULL, '', 1, 1, 2, NULL, 3, 0, '2020-06-03 21:15:08', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1598, 'sp_data_value', '产品规格', 1, 1, NULL, '', NULL, '', 0, 0, 1, NULL, 3, 0, '2020-06-03 21:15:08', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1599, 'product_code', '产品编码', 1, 1, NULL, '', NULL, '', 1, 1, 0, NULL, 3, 0, '2020-09-24 10:45:21', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1600, 'product_company', '产品单位', 3, 1, NULL, '', NULL, '', 0, 0, 3, '个,块,只,把,枚,瓶,盒,台,吨,千克,米,箱', 1, 0, '2020-06-03 21:15:08', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1601, 'product_cost_price', '成本价', 6, 1, NULL, '', NULL, '', 0, 0, 4, NULL, 1, 0, '2020-06-03 21:15:08', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1602, 'product_market_price', '市场价', 6, 1, NULL, '', NULL, '', 0, 0, 5, NULL, 1, 0, '2020-06-03 21:15:08', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1603, 'remark', '备注', 2, 1, NULL, '', 1000, '', 0, 0, 6, NULL, 1, 0, '2020-06-03 21:15:08', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1604, 'supplier_name', '供应商名称', 1, 2, NULL, '', NULL, '', 0, 1, 0, NULL, 1, 0, '2020-09-17 11:17:08', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1605, 'supplier_level', '供应商级别', 3, 2, NULL, '', NULL, 'A', 0, 0, 1, 'A,B,C,D', 1, 0, '2020-09-17 11:17:08', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1606, 'contacts', '联系人姓名', 1, 2, NULL, '', NULL, '', 0, 0, 2, NULL, 1, 0, '2020-09-17 11:17:08', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1607, 'contact_number', '联系电话', 7, 2, NULL, '', NULL, '', 0, 0, 3, NULL, 1, 0, '2020-09-17 11:17:08', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1608, 'remark', '备注', 2, 2, NULL, '', 1000, '', 0, 0, 5, NULL, 1, 0, '2020-09-17 11:17:08', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1609, 'detail_address', '地址', 1, 2, NULL, '', NULL, '', 0, 0, 4, NULL, 1, 0, '2020-09-17 11:17:08', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1610, 'supplier_id', '供应商名称', 30, 3, NULL, '', NULL, '', 0, 1, 2, NULL, 1, 0, '2020-06-03 21:15:08', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1611, 'order_number', '采购单编号', 1, 3, NULL, '', NULL, '', 1, 1, 0, NULL, 1, 0, '2020-06-03 21:15:08', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1612, 'total_price', '采购金额', 6, 3, NULL, '', NULL, '', 0, 1, 3, NULL, 1, 0, '2020-06-11 18:32:58', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1613, 'purchase_date', '采购日期', 4, 3, NULL, '', NULL, '', 0, 0, 1, NULL, 1, 0, '2020-06-03 21:15:08', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1614, 'remark', '备注', 2, 3, NULL, '', 1000, '', 0, 0, 4, NULL, 1, 0, '2020-06-03 21:15:08', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1615, 'order_number', '采购退货单编号', 1, 4, NULL, '', NULL, '', 1, 1, 0, NULL, 1, 0, '2020-06-03 21:15:08', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1616, 'supplier_id', '供应商名称', 30, 4, NULL, '', NULL, '', 0, 1, 2, NULL, 1, 0, '2020-06-11 15:51:52', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1617, 'purchase_id', '采购单编号', 31, 4, NULL, '', NULL, '', 0, 1, 1, NULL, 1, 0, '2020-06-11 15:51:49', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1618, 'total_price', '退货金额', 6, 4, NULL, '', NULL, '', 0, 1, 3, NULL, 1, 0, '2020-06-11 18:33:06', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1619, 'retreat_date', '退货日期', 4, 4, NULL, '', NULL, '', 0, 0, 4, NULL, 1, 0, '2020-06-03 21:15:08', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1620, 'reason', '退货原因', 1, 4, NULL, '', NULL, '', 0, 0, 5, NULL, 1, 0, '2020-06-03 21:15:08', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1621, 'remark', '备注', 2, 4, NULL, '', 1000, '', 0, 0, 6, NULL, 1, 0, '2020-06-03 21:15:08', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1622, 'order_number', '销售单编号', 1, 5, NULL, '', NULL, '', 0, 1, 0, NULL, 1, 0, '2020-06-12 14:50:31', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1623, 'sale_name', '销售单名称', 1, 5, NULL, '', NULL, '', 0, 1, 1, NULL, 1, 0, '2020-06-09 14:05:16', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1624, 'customer_name', '客户名称', 15, 5, NULL, '', NULL, '', 0, 1, 2, NULL, 1, 0, '2020-06-13 09:30:55', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1625, 'sale_price', '销售单金额', 6, 5, NULL, '', NULL, '', 0, 1, 3, NULL, 1, 0, '2020-06-11 18:33:14', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1626, 'sale_time', '下单时间', 4, 5, NULL, '', NULL, '', 0, 0, 4, NULL, 1, 0, '2020-06-09 14:05:16', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1627, 'delivery_time', '交货时间', 4, 5, NULL, '', NULL, '', 0, 0, 5, NULL, 1, 0, '2020-06-09 14:05:16', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1628, 'remark', '备注', 2, 5, NULL, '', 1000, '', 0, 0, 6, NULL, 1, 0, '2020-06-09 14:05:16', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1629, 'order_number', '销售退货单编号', 1, 6, NULL, '', NULL, '', 1, 1, 0, NULL, 1, 0, '2020-06-09 14:07:14', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1630, 'sale_id', '销售订单编号', 32, 6, NULL, '', NULL, '', 0, 1, 1, NULL, 1, 0, '2020-06-09 14:07:21', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1631, 'customer_name', '客户名称', 15, 6, NULL, '', NULL, '', 0, 1, 2, NULL, 1, 0, '2020-06-13 16:31:39', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1632, 'total_price', '退款金额', 6, 6, NULL, '', NULL, '', 0, 1, 3, NULL, 1, 0, '2020-06-11 18:33:19', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1633, 'retreat_date', '退货时间', 4, 6, NULL, '', NULL, '', 0, 0, 4, NULL, 1, 0, '2020-06-09 14:08:01', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1634, 'remark', '备注', 2, 6, NULL, '', 1000, '', 0, 0, 5, NULL, 1, 0, '2020-06-09 14:08:08', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1635, 'order_number', '入库单号', 1, 7, NULL, '', NULL, '', 1, 1, 1, NULL, 1, 0, '2020-06-10 09:56:48', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1636, 'receipt_type', '入库类型', 3, 7, NULL, '', NULL, '', 0, 1, 0, '初始化入库,采购入库,销售退货入库,其他入库', 1, 0, '2020-06-10 09:56:57', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1637, 'order_id', '关联单', 1, 7, NULL, '', NULL, '', 0, 1, 2, NULL, 1, 0, '2020-06-10 09:57:03', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1638, 'warehouse_id', '入库仓库', 33, 7, NULL, '', NULL, '', 0, 1, 3, NULL, 1, 0, '2020-06-10 09:57:22', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1639, 'receipt_time', '入库时间', 4, 7, NULL, '', NULL, '', 0, 0, 4, NULL, 1, 0, '2020-06-10 09:57:33', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1640, 'remark', '备注', 2, 7, NULL, '', 1000, '', 0, 0, 5, NULL, 1, 0, '2020-06-10 09:57:39', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1641, 'order_number', '出库单号', 1, 8, NULL, '', NULL, '', 1, 1, 1, NULL, 1, 0, '2020-06-10 10:18:49', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1642, 'outbound_type', '出库类型', 3, 8, NULL, '', NULL, '其他出库', 0, 1, 0, '销售出库,采购退货出库,其他出库', 1, 0, '2020-06-10 10:18:54', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1643, 'order_id', '关联单号', 1, 8, NULL, '', NULL, '', 0, 1, 2, NULL, 1, 0, '2020-06-10 10:19:18', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1644, 'warehouse_id', '出库仓库', 33, 8, NULL, '', NULL, '', 0, 1, 3, NULL, 1, 0, '2020-06-13 14:19:36', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1645, 'outbound_time', '出库时间', 4, 8, NULL, '', NULL, '', 0, 0, 4, NULL, 1, 0, '2020-06-10 10:19:36', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1646, 'remark', '备注', 2, 8, NULL, '', 1000, '', 0, 0, 5, NULL, 1, 0, '2020-06-10 10:19:41', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1647, 'payment_type', '付款类型', 3, 9, NULL, '', NULL, '其他', 0, 1, 0, '采购,销售退货,其他', 1, 0, '2020-06-11 09:53:55', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1648, 'payment_no', '付款编号', 1, 9, NULL, '', NULL, '', 1, 1, 1, NULL, 1, 0, '2020-06-11 09:54:02', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1649, 'related_id', '关联单', 34, 9, NULL, '', NULL, '', 0, 1, 2, NULL, 1, 0, '2020-06-11 09:54:22', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1650, 'collection_object', '关联对象', 35, 9, NULL, '', NULL, '', 0, 1, 3, NULL, 1, 0, '2020-06-11 09:57:03', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1651, 'payment_amount', '付款金额', 6, 9, NULL, '', NULL, '', 0, 1, 4, NULL, 1, 0, '2020-06-11 18:33:27', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1652, 'payment_method', '付款方式', 3, 9, NULL, '', NULL, '', 0, 1, 5, '支票,现金,邮政汇款,电汇,网上转账,支付宝,微信,其他', 1, 0, '2020-06-11 09:57:17', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1653, 'order_time', '付款时间', 4, 9, NULL, '', NULL, '', 0, 1, 6, NULL, 1, 0, '2020-06-11 09:57:23', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1654, 'remark', '备注', 2, 9, NULL, '', 1000, '', 0, 0, 7, NULL, 1, 0, '2020-06-11 09:57:29', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1655, 'collection_type', '回款类型', 3, 10, NULL, '', NULL, '其他', 0, 1, 0, '销售,采购退货,其他', 1, 0, '2020-06-11 09:59:20', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1656, 'collection_no', '回款编号', 1, 10, NULL, '', NULL, '', 1, 1, 1, NULL, 1, 0, '2020-06-11 09:59:25', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1657, 'related_id', '关联单', 34, 10, NULL, '', NULL, '', 0, 1, 2, NULL, 1, 0, '2020-06-11 10:00:34', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1658, 'collection_object', '关联对象', 35, 10, NULL, '', NULL, '', 0, 1, 3, NULL, 1, 0, '2020-06-11 10:00:28', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1659, 'collection_amount', '回款金额', 6, 10, NULL, '', NULL, '', 0, 1, 4, NULL, 1, 0, '2020-06-11 18:33:32', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1660, 'collection_method', '回款方式', 3, 10, NULL, '', NULL, '', 0, 1, 5, '支票,现金,邮政汇款,电汇,网上转账,支付宝,微信,其他', 1, 0, '2020-06-11 10:03:57', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1661, 'order_time', '回款时间', 4, 10, NULL, '', NULL, '', 0, 1, 6, NULL, 1, 0, '2020-06-11 10:04:02', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1662, 'remark', '备注', 2, 10, NULL, '', 1000, '', 0, 0, 7, NULL, 1, 0, '2020-06-11 10:04:08', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1663, 'inventory_number', '盘点编号', 1, 11, NULL, '', NULL, '', 1, 1, 0, NULL, 1, 0, '2020-06-13 17:32:45', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1664, 'warehouse_id', '盘点仓库', 33, 11, NULL, '', NULL, '', 0, 1, 1, NULL, 1, 0, '2020-06-17 16:25:08', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1665, 'owner_user_id', '盘点人', 28, 11, NULL, '', NULL, '', 0, 0, 3, NULL, 1, 0, '2020-06-18 14:43:50', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1666, 'inventory_time', '盘点时间', 4, 11, NULL, '', NULL, '', 0, 1, 4, NULL, 1, 0, '2020-06-13 17:33:01', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1667, 'remark', '备注', 2, 11, NULL, '', 1000, '', 0, 0, 5, NULL, 1, 0, '2020-06-13 17:33:23', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1668, 'inventory_name', '盘点名字', 1, 11, NULL, '', NULL, '', 0, 0, 2, NULL, 1, 0, '2020-06-13 17:32:54', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1669, 'allocation_number', '调拨编号', 1, 12, NULL, '', NULL, '', 0, 1, 0, NULL, 1, 0, '2020-06-13 17:35:48', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1670, 'allocation_name', '调拨名称', 1, 12, NULL, '', NULL, '', 0, 0, 1, NULL, 1, 0, '2020-06-13 17:33:40', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1671, 'owner_user_id', '负责人', 28, 12, NULL, '', NULL, '', 0, 0, 2, NULL, 1, 0, '2020-06-18 14:43:55', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1672, 'original_warehouse_id', '调出仓库', 33, 12, NULL, '', NULL, '', 0, 1, 4, NULL, 1, 0, '2020-06-17 13:49:59', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1673, 'current_warehouse_id', '调入仓库', 33, 12, NULL, '', NULL, '', 0, 1, 5, NULL, 1, 0, '2020-06-17 13:50:01', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1674, 'allocation_time', '调拨时间', 4, 12, NULL, '', NULL, '', 0, 1, 3, NULL, 1, 0, '2020-06-17 13:50:06', NULL, 1, NULL);
INSERT INTO `wk_jxc_field` VALUES (1675, 'remark', '备注', 2, 12, NULL, '', 1000, '', 0, 0, 6, NULL, 1, 0, '2020-06-13 17:36:00', NULL, 1, NULL);

-- ----------------------------
-- Table structure for wk_jxc_field_sort
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_field_sort`;
CREATE TABLE `wk_jxc_field_sort`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '字段id',
  `label` int(2) NOT NULL COMMENT '标签 1 产品 2 供应单 3 采购订单 4 采购退货单 5 销售订单 6 销售退货单 7入库单 8 出库单 9 付款单 10 回款单 11 盘点 12调拨',
  `field_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `form_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段中文名称',
  `type` int(2) NULL DEFAULT NULL COMMENT '字段类型 1 单行文本 2 多行文本 3 单选 4日期 5 数字 6 小数 7 手机  8 文件 9 多选 10 人员 11 附件 12 部门 13 日期时间 14 邮箱 15客户 16 商机 17 联系人 18 地图 19 产品类型 20 合同 21 回款计划',
  `sort` int(5) NOT NULL DEFAULT 0 COMMENT '字段排序',
  `width` int(11) NULL DEFAULT NULL COMMENT '宽度',
  `user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '用户id',
  `is_hide` int(1) NOT NULL DEFAULT 1 COMMENT '是否隐藏 0、不隐藏 1、隐藏',
  `field_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id`(`id`) USING BTREE,
  INDEX `label`(`label`, `user_id`) USING BTREE,
  INDEX `sort`(`sort`) USING BTREE,
  INDEX `is_hide`(`is_hide`) USING BTREE,
  INDEX `field_id`(`field_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3364 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字段排序表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_field_sort
-- ----------------------------
INSERT INTO `wk_jxc_field_sort` VALUES (3226, 2, 'supplierName', NULL, '供应商名称', 1, 0, NULL, 14773, 0, 1604);
INSERT INTO `wk_jxc_field_sort` VALUES (3227, 2, 'supplierLevel', NULL, '供应商级别', 3, 1, NULL, 14773, 0, 1605);
INSERT INTO `wk_jxc_field_sort` VALUES (3228, 2, 'contacts', NULL, '联系人姓名', 1, 2, NULL, 14773, 0, 1606);
INSERT INTO `wk_jxc_field_sort` VALUES (3229, 2, 'contactNumber', NULL, '联系电话', 7, 3, NULL, 14773, 0, 1607);
INSERT INTO `wk_jxc_field_sort` VALUES (3230, 2, 'detailAddress', NULL, '地址', 1, 4, NULL, 14773, 0, 1609);
INSERT INTO `wk_jxc_field_sort` VALUES (3231, 2, 'remark', NULL, '备注', 2, 5, NULL, 14773, 0, 1608);
INSERT INTO `wk_jxc_field_sort` VALUES (3232, 2, 'ownerUserId', NULL, '负责人', 10, 7, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3233, 2, 'createTime', NULL, '创建时间', 13, 8, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3234, 2, 'createUserId', NULL, '创建人', 10, 9, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3235, 2, 'updateTime', NULL, '更新时间', 13, 10, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3236, 1, 'productPicture', NULL, '产品图片', 29, 0, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3237, 1, 'productCode', NULL, '产品编码', 1, 0, NULL, 14773, 0, 1599);
INSERT INTO `wk_jxc_field_sort` VALUES (3238, 1, 'spDataValue', NULL, '产品规格', 1, 1, NULL, 14773, 0, 1598);
INSERT INTO `wk_jxc_field_sort` VALUES (3239, 1, 'productName', NULL, '产品名称', 1, 2, NULL, 14773, 0, 1597);
INSERT INTO `wk_jxc_field_sort` VALUES (3240, 1, 'productCompany', NULL, '产品单位', 3, 3, NULL, 14773, 0, 1600);
INSERT INTO `wk_jxc_field_sort` VALUES (3241, 1, 'productCostPrice', NULL, '成本价', 6, 4, NULL, 14773, 0, 1601);
INSERT INTO `wk_jxc_field_sort` VALUES (3242, 1, 'productMarketPrice', NULL, '市场价', 6, 5, NULL, 14773, 0, 1602);
INSERT INTO `wk_jxc_field_sort` VALUES (3243, 1, 'remark', NULL, '备注', 2, 6, NULL, 14773, 0, 1603);
INSERT INTO `wk_jxc_field_sort` VALUES (3244, 1, 'productType', NULL, '产品类型', 19, 1, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3245, 1, 'productIshelf', NULL, '是否上架', 3, 1, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3246, 1, 'createTime', NULL, '创建时间', 13, 11, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3247, 1, 'createUserId', NULL, '创建人', 10, 12, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3248, 1, 'updateTime', NULL, '更新时间', 13, 13, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3249, 5, 'orderNumber', NULL, '销售单编号', 1, 0, NULL, 14773, 0, 1622);
INSERT INTO `wk_jxc_field_sort` VALUES (3250, 5, 'saleName', NULL, '销售单名称', 1, 1, NULL, 14773, 0, 1623);
INSERT INTO `wk_jxc_field_sort` VALUES (3251, 5, 'customerName', NULL, '客户名称', 15, 2, NULL, 14773, 0, 1624);
INSERT INTO `wk_jxc_field_sort` VALUES (3252, 5, 'salePrice', NULL, '销售单金额', 6, 3, NULL, 14773, 0, 1625);
INSERT INTO `wk_jxc_field_sort` VALUES (3253, 5, 'saleTime', NULL, '下单时间', 4, 4, NULL, 14773, 0, 1626);
INSERT INTO `wk_jxc_field_sort` VALUES (3254, 5, 'deliveryTime', NULL, '交货时间', 4, 5, NULL, 14773, 0, 1627);
INSERT INTO `wk_jxc_field_sort` VALUES (3255, 5, 'remark', NULL, '备注', 2, 6, NULL, 14773, 0, 1628);
INSERT INTO `wk_jxc_field_sort` VALUES (3256, 5, 'ownerUserId', NULL, '负责人', 10, 8, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3257, 5, 'state', NULL, '状态', 39, 9, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3258, 5, 'createTime', NULL, '创建时间', 13, 10, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3259, 5, 'createUserId', NULL, '创建人', 10, 11, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3260, 5, 'updateTime', NULL, '更新时间', 13, 12, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3261, 4, 'orderNumber', NULL, '采购退货单编号', 1, 0, NULL, 14773, 0, 1615);
INSERT INTO `wk_jxc_field_sort` VALUES (3262, 4, 'purchaseId', NULL, '采购单编号', 31, 1, NULL, 14773, 0, 1617);
INSERT INTO `wk_jxc_field_sort` VALUES (3263, 4, 'supplierId', NULL, '供应商名称', 30, 2, NULL, 14773, 0, 1616);
INSERT INTO `wk_jxc_field_sort` VALUES (3264, 4, 'totalPrice', NULL, '退货金额', 6, 3, NULL, 14773, 0, 1618);
INSERT INTO `wk_jxc_field_sort` VALUES (3265, 4, 'retreatDate', NULL, '退货日期', 4, 4, NULL, 14773, 0, 1619);
INSERT INTO `wk_jxc_field_sort` VALUES (3266, 4, 'reason', NULL, '退货原因', 1, 5, NULL, 14773, 0, 1620);
INSERT INTO `wk_jxc_field_sort` VALUES (3267, 4, 'remark', NULL, '备注', 2, 6, NULL, 14773, 0, 1621);
INSERT INTO `wk_jxc_field_sort` VALUES (3268, 4, 'ownerUserId', NULL, '负责人', 10, 8, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3269, 4, 'state', NULL, '状态', 39, 9, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3270, 4, 'createTime', NULL, '创建时间', 13, 10, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3271, 4, 'createUserId', NULL, '创建人', 10, 11, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3272, 4, 'updateTime', NULL, '更新时间', 13, 12, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3273, 3, 'orderNumber', NULL, '采购单编号', 1, 0, NULL, 14773, 0, 1611);
INSERT INTO `wk_jxc_field_sort` VALUES (3274, 3, 'purchaseDate', NULL, '采购日期', 4, 1, NULL, 14773, 0, 1613);
INSERT INTO `wk_jxc_field_sort` VALUES (3275, 3, 'supplierId', NULL, '供应商名称', 30, 2, NULL, 14773, 0, 1610);
INSERT INTO `wk_jxc_field_sort` VALUES (3276, 3, 'totalPrice', NULL, '采购金额', 6, 3, NULL, 14773, 0, 1612);
INSERT INTO `wk_jxc_field_sort` VALUES (3277, 3, 'remark', NULL, '备注', 2, 4, NULL, 14773, 0, 1614);
INSERT INTO `wk_jxc_field_sort` VALUES (3278, 3, 'ownerUserId', NULL, '负责人', 10, 6, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3279, 3, 'state', NULL, '状态', 39, 7, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3280, 3, 'createTime', NULL, '创建时间', 13, 8, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3281, 3, 'createUserId', NULL, '创建人', 10, 9, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3282, 3, 'updateTime', NULL, '更新时间', 13, 10, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3283, 9, 'paymentType', NULL, '付款类型', 3, 0, NULL, 14773, 0, 1647);
INSERT INTO `wk_jxc_field_sort` VALUES (3284, 9, 'paymentNo', NULL, '付款编号', 1, 1, NULL, 14773, 0, 1648);
INSERT INTO `wk_jxc_field_sort` VALUES (3285, 9, 'relatedId', NULL, '关联单', 34, 2, NULL, 14773, 0, 1649);
INSERT INTO `wk_jxc_field_sort` VALUES (3286, 9, 'collectionObject', NULL, '关联对象', 35, 3, NULL, 14773, 0, 1650);
INSERT INTO `wk_jxc_field_sort` VALUES (3287, 9, 'paymentAmount', NULL, '付款金额', 6, 4, NULL, 14773, 0, 1651);
INSERT INTO `wk_jxc_field_sort` VALUES (3288, 9, 'paymentMethod', NULL, '付款方式', 3, 5, NULL, 14773, 0, 1652);
INSERT INTO `wk_jxc_field_sort` VALUES (3289, 9, 'orderTime', NULL, '付款时间', 4, 6, NULL, 14773, 0, 1653);
INSERT INTO `wk_jxc_field_sort` VALUES (3290, 9, 'remark', NULL, '备注', 2, 7, NULL, 14773, 0, 1654);
INSERT INTO `wk_jxc_field_sort` VALUES (3291, 9, 'ownerUserId', NULL, '负责人', 10, 9, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3292, 9, 'state', NULL, '状态', 39, 10, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3293, 9, 'createTime', NULL, '创建时间', 13, 11, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3294, 9, 'createUserId', NULL, '创建人', 10, 12, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3295, 9, 'updateTime', NULL, '更新时间', 13, 13, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3296, 10, 'collectionType', NULL, '回款类型', 3, 0, NULL, 14773, 0, 1655);
INSERT INTO `wk_jxc_field_sort` VALUES (3297, 10, 'collectionNo', NULL, '回款编号', 1, 1, NULL, 14773, 0, 1656);
INSERT INTO `wk_jxc_field_sort` VALUES (3298, 10, 'relatedId', NULL, '关联单', 34, 2, NULL, 14773, 0, 1657);
INSERT INTO `wk_jxc_field_sort` VALUES (3299, 10, 'collectionObject', NULL, '关联对象', 35, 3, NULL, 14773, 0, 1658);
INSERT INTO `wk_jxc_field_sort` VALUES (3300, 10, 'collectionAmount', NULL, '回款金额', 6, 4, NULL, 14773, 0, 1659);
INSERT INTO `wk_jxc_field_sort` VALUES (3301, 10, 'collectionMethod', NULL, '回款方式', 3, 5, NULL, 14773, 0, 1660);
INSERT INTO `wk_jxc_field_sort` VALUES (3302, 10, 'orderTime', NULL, '回款时间', 4, 6, NULL, 14773, 0, 1661);
INSERT INTO `wk_jxc_field_sort` VALUES (3303, 10, 'remark', NULL, '备注', 2, 7, NULL, 14773, 0, 1662);
INSERT INTO `wk_jxc_field_sort` VALUES (3304, 10, 'ownerUserId', NULL, '负责人', 10, 9, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3305, 10, 'state', NULL, '状态', 39, 10, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3306, 10, 'createTime', NULL, '创建时间', 13, 11, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3307, 10, 'createUserId', NULL, '创建人', 10, 12, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3308, 10, 'updateTime', NULL, '更新时间', 13, 13, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3309, 7, 'receiptType', NULL, '入库类型', 3, 0, NULL, 14773, 0, 1636);
INSERT INTO `wk_jxc_field_sort` VALUES (3310, 7, 'orderNumber', NULL, '入库单号', 1, 1, NULL, 14773, 0, 1635);
INSERT INTO `wk_jxc_field_sort` VALUES (3311, 7, 'orderId', NULL, '关联单', 1, 2, NULL, 14773, 0, 1637);
INSERT INTO `wk_jxc_field_sort` VALUES (3312, 7, 'warehouseId', NULL, '入库仓库', 33, 3, NULL, 14773, 0, 1638);
INSERT INTO `wk_jxc_field_sort` VALUES (3313, 7, 'receiptTime', NULL, '入库时间', 4, 4, NULL, 14773, 0, 1639);
INSERT INTO `wk_jxc_field_sort` VALUES (3314, 7, 'remark', NULL, '备注', 2, 5, NULL, 14773, 0, 1640);
INSERT INTO `wk_jxc_field_sort` VALUES (3315, 7, 'ownerUserId', NULL, '负责人', 10, 7, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3316, 7, 'state', NULL, '状态', 39, 8, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3317, 7, 'createTime', NULL, '创建时间', 13, 9, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3318, 7, 'createUserId', NULL, '创建人', 10, 10, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3319, 7, 'updateTime', NULL, '更新时间', 13, 11, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3320, 11, 'inventoryNumber', NULL, '盘点编号', 1, 0, NULL, 14773, 0, 1663);
INSERT INTO `wk_jxc_field_sort` VALUES (3321, 11, 'warehouseId', NULL, '盘点仓库', 33, 1, NULL, 14773, 0, 1664);
INSERT INTO `wk_jxc_field_sort` VALUES (3322, 11, 'inventoryName', NULL, '盘点名字', 1, 2, NULL, 14773, 0, 1668);
INSERT INTO `wk_jxc_field_sort` VALUES (3323, 11, 'ownerUserId', NULL, '盘点人', 28, 3, NULL, 14773, 0, 1665);
INSERT INTO `wk_jxc_field_sort` VALUES (3324, 11, 'inventoryTime', NULL, '盘点时间', 4, 4, NULL, 14773, 0, 1666);
INSERT INTO `wk_jxc_field_sort` VALUES (3325, 11, 'remark', NULL, '备注', 2, 5, NULL, 14773, 0, 1667);
INSERT INTO `wk_jxc_field_sort` VALUES (3326, 11, 'productType', NULL, '产品类型', 19, 1, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3327, 11, 'state', NULL, '状态', 39, 8, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3328, 11, 'createTime', NULL, '创建时间', 13, 9, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3329, 11, 'createUserId', NULL, '创建人', 10, 10, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3330, 11, 'updateTime', NULL, '更新时间', 13, 11, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3331, 12, 'allocationNumber', NULL, '调拨编号', 1, 0, NULL, 14773, 0, 1669);
INSERT INTO `wk_jxc_field_sort` VALUES (3332, 12, 'allocationName', NULL, '调拨名称', 1, 1, NULL, 14773, 0, 1670);
INSERT INTO `wk_jxc_field_sort` VALUES (3333, 12, 'ownerUserId', NULL, '负责人', 28, 2, NULL, 14773, 0, 1671);
INSERT INTO `wk_jxc_field_sort` VALUES (3334, 12, 'allocationTime', NULL, '调拨时间', 4, 3, NULL, 14773, 0, 1674);
INSERT INTO `wk_jxc_field_sort` VALUES (3335, 12, 'originalWarehouseId', NULL, '调出仓库', 33, 4, NULL, 14773, 0, 1672);
INSERT INTO `wk_jxc_field_sort` VALUES (3336, 12, 'currentWarehouseId', NULL, '调入仓库', 33, 5, NULL, 14773, 0, 1673);
INSERT INTO `wk_jxc_field_sort` VALUES (3337, 12, 'remark', NULL, '备注', 2, 6, NULL, 14773, 0, 1675);
INSERT INTO `wk_jxc_field_sort` VALUES (3338, 12, 'state', NULL, '状态', 39, 8, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3339, 12, 'createTime', NULL, '创建时间', 13, 9, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3340, 12, 'createUserId', NULL, '创建人', 10, 10, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3341, 12, 'updateTime', NULL, '更新时间', 13, 11, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3342, 8, 'outboundType', NULL, '出库类型', 3, 0, NULL, 14773, 0, 1642);
INSERT INTO `wk_jxc_field_sort` VALUES (3343, 8, 'orderNumber', NULL, '出库单号', 1, 1, NULL, 14773, 0, 1641);
INSERT INTO `wk_jxc_field_sort` VALUES (3344, 8, 'orderId', NULL, '关联单号', 1, 2, NULL, 14773, 0, 1643);
INSERT INTO `wk_jxc_field_sort` VALUES (3345, 8, 'warehouseId', NULL, '出库仓库', 33, 3, NULL, 14773, 0, 1644);
INSERT INTO `wk_jxc_field_sort` VALUES (3346, 8, 'outboundTime', NULL, '出库时间', 4, 4, NULL, 14773, 0, 1645);
INSERT INTO `wk_jxc_field_sort` VALUES (3347, 8, 'remark', NULL, '备注', 2, 5, NULL, 14773, 0, 1646);
INSERT INTO `wk_jxc_field_sort` VALUES (3348, 8, 'ownerUserId', NULL, '负责人', 10, 7, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3349, 8, 'state', NULL, '状态', 39, 8, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3350, 8, 'createTime', NULL, '创建时间', 13, 9, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3351, 8, 'createUserId', NULL, '创建人', 10, 10, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3352, 8, 'updateTime', NULL, '更新时间', 13, 11, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3353, 6, 'orderNumber', NULL, '销售退货单编号', 1, 0, NULL, 14773, 0, 1629);
INSERT INTO `wk_jxc_field_sort` VALUES (3354, 6, 'saleId', NULL, '销售订单编号', 32, 1, NULL, 14773, 0, 1630);
INSERT INTO `wk_jxc_field_sort` VALUES (3355, 6, 'customerName', NULL, '客户名称', 15, 2, NULL, 14773, 0, 1631);
INSERT INTO `wk_jxc_field_sort` VALUES (3356, 6, 'totalPrice', NULL, '退款金额', 6, 3, NULL, 14773, 0, 1632);
INSERT INTO `wk_jxc_field_sort` VALUES (3357, 6, 'retreatDate', NULL, '退货时间', 4, 4, NULL, 14773, 0, 1633);
INSERT INTO `wk_jxc_field_sort` VALUES (3358, 6, 'remark', NULL, '备注', 2, 5, NULL, 14773, 0, 1634);
INSERT INTO `wk_jxc_field_sort` VALUES (3359, 6, 'ownerUserId', NULL, '负责人', 10, 7, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3360, 6, 'state', NULL, '状态', 39, 8, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3361, 6, 'createTime', NULL, '创建时间', 13, 9, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3362, 6, 'createUserId', NULL, '创建人', 10, 10, NULL, 14773, 0, NULL);
INSERT INTO `wk_jxc_field_sort` VALUES (3363, 6, 'updateTime', NULL, '更新时间', 13, 11, NULL, 14773, 0, NULL);

-- ----------------------------
-- Table structure for wk_jxc_field_style
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_field_style`;
CREATE TABLE `wk_jxc_field_style`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '样式表id',
  `style` int(5) NULL DEFAULT NULL COMMENT '字段宽度',
  `type` int(2) NULL DEFAULT NULL COMMENT '标签 1 产品 2 供应单 3 采购订单 4 采购退货单 5 销售订单 6 销售退货单 7入库单 8 出库单 9 付款单 10 回款单 11 盘点 12调拨',
  `field_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段名称',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `type`(`type`, `user_id`) USING BTREE,
  INDEX `id`(`id`) USING BTREE,
  INDEX `field_name`(`field_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '字段样式表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_field_style
-- ----------------------------

-- ----------------------------
-- Table structure for wk_jxc_file
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_file`;
CREATE TABLE `wk_jxc_file`  (
  `file_id` int(11) NOT NULL AUTO_INCREMENT,
  `jxc_batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '列表的批次ID',
  `label` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签 1 产品 2 供应单 3 采购订单 4 采购退货单 5 销售订单 6 销售退货单 7入库单 8 出库单 9 付款单 10 回款单 11 盘点 12调拨',
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '文件批次id',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`file_id`) USING BTREE,
  UNIQUE INDEX `file_id`(`file_id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `jxc_batch_id`(`jxc_batch_id`) USING BTREE,
  INDEX `label`(`label`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '附件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_file
-- ----------------------------

-- ----------------------------
-- Table structure for wk_jxc_follow_record
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_follow_record`;
CREATE TABLE `wk_jxc_follow_record`  (
  `follow_record_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标签 1 产品 2 供应单 3 采购订单 4 采购退货单 5 销售订单 6 销售退货单 7入库单 8 出库单 9 付款单 10 回款单 11 盘点 12调拨',
  `follow_record_role` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '跟进记录内容',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注',
  PRIMARY KEY (`follow_record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '跟进记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_follow_record
-- ----------------------------

-- ----------------------------
-- Table structure for wk_jxc_follow_record_set
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_follow_record_set`;
CREATE TABLE `wk_jxc_follow_record_set`  (
  `follow_record_set_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `follow_record_set_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '跟进记录名',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注',
  PRIMARY KEY (`follow_record_set_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_follow_record_set
-- ----------------------------

-- ----------------------------
-- Table structure for wk_jxc_follow_record_set_role
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_follow_record_set_role`;
CREATE TABLE `wk_jxc_follow_record_set_role`  (
  `follow_record_set_role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `follow_record_set_role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '常用语',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注',
  PRIMARY KEY (`follow_record_set_role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_follow_record_set_role
-- ----------------------------

-- ----------------------------
-- Table structure for wk_jxc_inventory
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_inventory`;
CREATE TABLE `wk_jxc_inventory`  (
  `inventory_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `inventory_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `inventory_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '盘点名称',
  `inventory_time` datetime(0) NULL DEFAULT NULL COMMENT '盘点时间',
  `warehouse_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '仓库Id',
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '1' COMMENT '0 删除 1 不删除 2正在审核 3 审核完成',
  `inventory_state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '0 未盘点 1盘点中 2 已入库',
  `owner_user_id` int(11) NULL DEFAULT NULL COMMENT '负责人',
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注',
  `product_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '产品类别',
  `record_id` int(11) NULL DEFAULT NULL COMMENT '审核记录Id',
  PRIMARY KEY (`inventory_id`) USING BTREE,
  UNIQUE INDEX `inventory_id`(`inventory_id`) USING BTREE,
  UNIQUE INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `warehouse_id`(`warehouse_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 55 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '进销存盘点表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_inventory
-- ----------------------------
INSERT INTO `wk_jxc_inventory` VALUES (53, '546546', '5464', '2020-09-09 00:00:00', '25', '0', '2', 14773, 'fe60bb5ec9cb4400adc139e3a1e5fcf1', 14773, NULL, '2020-09-17 10:48:39', '2020-09-17 10:48:39', '', '35', 9);
INSERT INTO `wk_jxc_inventory` VALUES (54, '12512', '1212', '2020-08-31 00:00:00', '25', '3', '0', 14773, 'bc93d8c471c246869aee77ed10824a1b', 14773, NULL, '2020-09-17 11:13:27', '2020-09-17 11:13:27', '121', '35', 22);

-- ----------------------------
-- Table structure for wk_jxc_inventory_data
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_inventory_data`;
CREATE TABLE `wk_jxc_inventory_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '盘点扩展字段数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_inventory_data
-- ----------------------------

-- ----------------------------
-- Table structure for wk_jxc_inventory_product
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_inventory_product`;
CREATE TABLE `wk_jxc_inventory_product`  (
  `inventory_product_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `warehouse_id` int(11) NULL DEFAULT NULL COMMENT '仓库规格Id',
  `actual_number` int(11) NULL DEFAULT NULL COMMENT '实际数量',
  `product_id` int(11) NULL DEFAULT NULL COMMENT '商品Id',
  `loss_number` int(11) NULL DEFAULT NULL COMMENT '盈亏数量',
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`inventory_product_id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `warehouse_id`(`warehouse_id`) USING BTREE,
  INDEX `product_id`(`product_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 91 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '盘点产品关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_inventory_product
-- ----------------------------
INSERT INTO `wk_jxc_inventory_product` VALUES (88, 25, 2, 125, 2, 'fe60bb5ec9cb4400adc139e3a1e5fcf1', '');
INSERT INTO `wk_jxc_inventory_product` VALUES (89, 25, 2, 126, 2, 'fe60bb5ec9cb4400adc139e3a1e5fcf1', '');
INSERT INTO `wk_jxc_inventory_product` VALUES (90, 25, 1970, 130, -10, 'bc93d8c471c246869aee77ed10824a1b', '');

-- ----------------------------
-- Table structure for wk_jxc_inventory_record
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_inventory_record`;
CREATE TABLE `wk_jxc_inventory_record`  (
  `inventory_record_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '订单号',
  `inventory_product_id` int(11) NULL DEFAULT NULL,
  `product_id` int(11) NULL DEFAULT NULL COMMENT '商品Id',
  `warehouse_id` int(11) NULL DEFAULT NULL COMMENT '仓库Id',
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '5' COMMENT '5 生效 6作废',
  `inventory_record_time` datetime(0) NULL DEFAULT NULL COMMENT '入库时间',
  `inventory_record_number` int(11) NULL DEFAULT NULL COMMENT '操作数量',
  `b_batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '本次',
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `b_remark` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '本次备注',
  `remark` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注',
  `owner_user_id` int(11) NULL DEFAULT NULL COMMENT '负责人',
  PRIMARY KEY (`inventory_record_id`) USING BTREE,
  UNIQUE INDEX `inventory_record_id`(`inventory_record_id`) USING BTREE,
  INDEX `inventory_product_id`(`inventory_product_id`) USING BTREE,
  INDEX `product_id`(`product_id`) USING BTREE,
  INDEX `warehouse_id`(`warehouse_id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 76 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_inventory_record
-- ----------------------------
INSERT INTO `wk_jxc_inventory_record` VALUES (71, '546', 88, 125, 25, '6', '2020-08-31 00:00:00', 2, '4b8c990774314dc4a4b707f12cc80f61', 'fe60bb5ec9cb4400adc139e3a1e5fcf1', '2020-09-17 10:48:58', '2020-09-17 10:49:02', '456', '', 14773);
INSERT INTO `wk_jxc_inventory_record` VALUES (72, '546', 89, 126, 25, '6', '2020-08-31 00:00:00', 2, '4b8c990774314dc4a4b707f12cc80f61', 'fe60bb5ec9cb4400adc139e3a1e5fcf1', '2020-09-17 10:48:58', '2020-09-17 10:49:02', '456', '', 14773);
INSERT INTO `wk_jxc_inventory_record` VALUES (73, '12', 88, 125, 25, '5', '2020-09-15 00:00:00', 2, '48ebc7983a7043d4995dc23c3b72b26b', 'fe60bb5ec9cb4400adc139e3a1e5fcf1', '2020-09-17 10:59:24', NULL, '123', '', 14773);
INSERT INTO `wk_jxc_inventory_record` VALUES (74, '12', 89, 126, 25, '5', '2020-09-15 00:00:00', 2, '48ebc7983a7043d4995dc23c3b72b26b', 'fe60bb5ec9cb4400adc139e3a1e5fcf1', '2020-09-17 10:59:24', NULL, '123', '', 14773);
INSERT INTO `wk_jxc_inventory_record` VALUES (75, '415214', 90, 130, 25, '6', '2020-09-15 00:00:00', -10, '20a514daafe445bdbdd7e7d82ffe9b00', 'bc93d8c471c246869aee77ed10824a1b', '2020-09-17 11:13:45', '2020-09-17 11:13:49', '454', '', 14773);

-- ----------------------------
-- Table structure for wk_jxc_number_rules
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_number_rules`;
CREATE TABLE `wk_jxc_number_rules`  (
  `number_rules_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '设置id',
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
  PRIMARY KEY (`number_rules_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 148 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '进销存自动编号规则表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_number_rules
-- ----------------------------
INSERT INTO `wk_jxc_number_rules` VALUES (146, 241, 0, 1, 'dddd', NULL, NULL, NULL, NULL, '2020-09-17 11:17:54', 14773);
INSERT INTO `wk_jxc_number_rules` VALUES (147, 241, 1, 3, '1', 1, 4, 1, '2020-09-17', '2020-09-17 11:17:54', 14773);

-- ----------------------------
-- Table structure for wk_jxc_number_setting
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_number_setting`;
CREATE TABLE `wk_jxc_number_setting`  (
  `setting_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主鍵',
  `status` int(1) NOT NULL DEFAULT 0 COMMENT '状态，0:不启用 1 ： 启用',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '设置名称',
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '值',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`setting_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 253 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '进销存自动编号表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_number_setting
-- ----------------------------
INSERT INTO `wk_jxc_number_setting` VALUES (241, 1, 'FIELDNUMBE', '1', '产品');
INSERT INTO `wk_jxc_number_setting` VALUES (242, 0, 'FIELDNUMBE', '3', '采购订单');
INSERT INTO `wk_jxc_number_setting` VALUES (243, 0, 'FIELDNUMBE', '4', '采购退货单');
INSERT INTO `wk_jxc_number_setting` VALUES (244, 0, 'FIELDNUMBE', '5', '销售订单');
INSERT INTO `wk_jxc_number_setting` VALUES (245, 0, 'FIELDNUMBE', '6', '销售退货单');
INSERT INTO `wk_jxc_number_setting` VALUES (246, 0, 'FIELDNUMBE', '7', '入库单');
INSERT INTO `wk_jxc_number_setting` VALUES (247, 0, 'FIELDNUMBE', '8', '出库单');
INSERT INTO `wk_jxc_number_setting` VALUES (248, 0, 'FIELDNUMBE', '9', '付款单');
INSERT INTO `wk_jxc_number_setting` VALUES (249, 0, 'FIELDNUMBE', '10', '回款单');
INSERT INTO `wk_jxc_number_setting` VALUES (250, 0, 'FIELDNUMBE', '11', '盘点');
INSERT INTO `wk_jxc_number_setting` VALUES (251, 0, 'FIELDNUMBE', '12', '调拨');
INSERT INTO `wk_jxc_number_setting` VALUES (252, 0, 'FIELDNUMBE', '13', '盘点录入');

-- ----------------------------
-- Table structure for wk_jxc_operation_record
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_operation_record`;
CREATE TABLE `wk_jxc_operation_record`  (
  `operation_record_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标签 1 产品 2 供应单 3 采购订单 4 采购退货单 5 销售订单 6 销售退货单 7入库单 8 出库单 9 付款单 10 回款单 11 盘点 12调拨',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '操作子类型',
  `object_role` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '操作内容',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注',
  PRIMARY KEY (`operation_record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 721 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_operation_record
-- ----------------------------
INSERT INTO `wk_jxc_operation_record` VALUES (676, '1', NULL, '新建了产品', 14773, NULL, 'f3090696d55a49d8a6097d7dfad5a663', '2020-09-17 10:07:08', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (677, '1', NULL, '新建了产品', 14773, NULL, '6af31aa9453848868c163d518bf6d9a5', '2020-09-17 10:08:02', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (678, '1', NULL, '新建了产品', 14773, NULL, '21396c23640a4b91af1cc2eaef3dbf80', '2020-09-17 10:08:02', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (679, '1', NULL, '新建了产品', 14773, NULL, '03137d9ac7cb482eb8c3267bd1d7df8a', '2020-09-17 10:08:02', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (680, '1', NULL, '新建了产品', 14773, NULL, 'de0889614993443da6f86149f0c632b4', '2020-09-17 10:08:02', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (681, '1', NULL, '新建了产品', 14773, NULL, '5d68353531fa4b4880120bdb1d011abb', '2020-09-17 10:22:54', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (682, '1', NULL, '新建了产品', 14773, NULL, '5a174e42e2ed43eca870f3662c464996', '2020-09-17 10:22:54', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (683, '1', NULL, '新建了产品', 14773, NULL, '1aeeea24e8734bfb999d428f614ac49f', '2020-09-17 10:22:54', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (684, '1', NULL, '新建了产品', 14773, NULL, '2c133081b91b4e798e354e5c52d7f042', '2020-09-17 10:22:54', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (685, '1', NULL, '新建了产品', 14773, NULL, '6c2060777e544860afa19abaa8dbf8a2', '2020-09-17 10:24:18', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (686, '1', NULL, '新建了产品', 14773, NULL, 'b17f41f09b8e47f2a27e59ecc1953c76', '2020-09-17 10:35:16', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (687, '1', NULL, '新建了产品', 14773, NULL, 'afc0ac82942f47c287e8c62fd066fc3c', '2020-09-17 10:35:16', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (688, '1', NULL, '新建了产品', 14773, NULL, '3afa56fcc08b425aa20bfdc766e9a4b1', '2020-09-17 10:35:16', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (689, '1', NULL, '新建了产品', 14773, NULL, '0197ac616c9f4b27bdff8fc246677586', '2020-09-17 10:35:16', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (690, '1', NULL, '产品:将产品单位 由个改为枚', 14773, NULL, 'b17f41f09b8e47f2a27e59ecc1953c76', '2020-09-17 10:37:22', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (691, '2', NULL, '新建了供应商', 14773, NULL, 'ddca35409ecf4537b6fd3af1b0a81499', '2020-09-17 10:39:55', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (692, '3', NULL, '新建了采购订单', 14773, NULL, '8ca4c96ab7e645c5b4e291f61b71bcbe', '2020-09-17 10:42:12', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (693, '3', NULL, '采购订单:将状态 采购订单的撤回状态改为审核中状态', 14773, NULL, '8ca4c96ab7e645c5b4e291f61b71bcbe', '2020-09-17 10:42:30', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (694, '3', NULL, '新建了采购订单', 14773, NULL, 'fee2ed93a5104fe8b56b6660bed4c7da', '2020-09-17 10:43:03', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (695, '3', NULL, '采购订单:将状态 采购订单的撤回状态改为审核中状态', 14773, NULL, 'fee2ed93a5104fe8b56b6660bed4c7da', '2020-09-17 10:43:18', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (696, '3', NULL, '采购订单:将状态 采购订单的拒绝状态改为审核中状态', 14773, NULL, 'fee2ed93a5104fe8b56b6660bed4c7da', '2020-09-17 10:43:27', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (697, '3', NULL, '将采购订单的审核完成状态改为作废状态', 14773, NULL, 'fee2ed93a5104fe8b56b6660bed4c7da', '2020-09-17 10:43:40', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (698, '3', NULL, '新建了采购订单', 14773, NULL, '7171563c6dac4aa08d225782bfc65776', '2020-09-17 10:44:01', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (699, '7', NULL, '新建了入库单', 14773, NULL, 'eb2c73e1dbc64e79aebf1c86e60dac4b', '2020-09-17 10:45:46', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (700, '4', NULL, '新建了采购退货单', 14773, NULL, '61c1ce3ea3f64ceca505ade69005a52e', '2020-09-17 10:46:03', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (701, '4', NULL, '采购退货单:将状态 采购退货单的撤回状态改为审核中状态', 14773, NULL, '61c1ce3ea3f64ceca505ade69005a52e', '2020-09-17 10:46:17', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (702, '4', NULL, '将采购退货单的审核完成状态改为作废状态', 14773, NULL, '61c1ce3ea3f64ceca505ade69005a52e', '2020-09-17 10:46:25', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (703, '4', NULL, '新建了采购退货单', 14773, NULL, '2bea5795c3ec4383904a126ecbf5e60c', '2020-09-17 10:46:51', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (704, '8', NULL, '新建了出库单', 14773, NULL, '2d86cf1c14624e2fa43fb9585b9e04d5', '2020-09-17 10:47:23', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (705, '9', NULL, '新建了付款单', 14773, NULL, '9bb353db78f74e908b8d464509617e81', '2020-09-17 10:47:39', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (706, '11', NULL, '将盘点的作废状态改为作废状态', 14773, NULL, 'fe60bb5ec9cb4400adc139e3a1e5fcf1', '2020-09-17 10:59:31', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (707, '7', NULL, '新建了入库单', 14773, NULL, 'a611ae87671646b1b3ece94a25ccf736', '2020-09-17 11:01:21', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (708, '12', NULL, '新建了调拨', 14773, NULL, '9f60ac15458d48629d6cf08d7b6ddb4f', '2020-09-17 11:01:53', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (709, '12', NULL, '调拨:将状态 调拨的撤回状态改为审核中状态', 14773, NULL, '9f60ac15458d48629d6cf08d7b6ddb4f', '2020-09-17 11:02:10', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (710, '12', NULL, '调拨:将状态 调拨的撤回状态改为审核中状态', 14773, NULL, '9f60ac15458d48629d6cf08d7b6ddb4f', '2020-09-17 11:02:27', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (711, '12', NULL, '调拨:将状态 调拨的撤回状态改为审核中状态', 14773, NULL, '9f60ac15458d48629d6cf08d7b6ddb4f', '2020-09-17 11:02:40', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (712, '10', NULL, '新建了回款单', 14773, NULL, 'c7176411f9434259a36b72d85de02abb', '2020-09-17 11:03:58', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (713, '10', NULL, '回款单:将状态 回款单的撤回状态改为审核中状态', 14773, NULL, '0268bf87be5e4d87a6932ca1e7ab0585', '2020-09-17 11:04:09', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (714, '10', NULL, '回款单:将状态 回款单的撤回状态改为审核中状态', 14773, NULL, '0268bf87be5e4d87a6932ca1e7ab0585', '2020-09-17 11:04:18', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (715, '9', NULL, '新建了付款单', 14773, NULL, 'd20bff49f3de4416820b424031c3fd3d', '2020-09-17 11:04:42', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (716, '9', NULL, '付款单:将状态 付款单的撤回状态改为审核中状态', 14773, NULL, '9bb353db78f74e908b8d464509617e81', '2020-09-17 11:05:00', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (717, '9', NULL, '付款单:将状态 付款单的拒绝状态改为审核中状态', 14773, NULL, '9bb353db78f74e908b8d464509617e81', '2020-09-17 11:05:08', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (718, '5', NULL, '新建了销售订单', 14773, NULL, 'd37c74f5536c4490bdec0084199dd610', '2020-09-17 11:06:11', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (719, '10', NULL, '新建了回款单', 14773, NULL, '1836ba9ce1f14676b0baef79bac3417d', '2020-09-17 11:06:39', NULL, NULL);
INSERT INTO `wk_jxc_operation_record` VALUES (720, '11', NULL, '新建了盘点', 14773, NULL, 'bc93d8c471c246869aee77ed10824a1b', '2020-09-17 11:13:26', NULL, NULL);

-- ----------------------------
-- Table structure for wk_jxc_outbound
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_outbound`;
CREATE TABLE `wk_jxc_outbound`  (
  `outbound_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `outbound_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '出库类型',
  `order_id` int(11) NULL DEFAULT NULL COMMENT '关联单号',
  `order_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '单号',
  `warehouse_id` int(11) NULL DEFAULT NULL COMMENT '仓库Id',
  `outbound_time` datetime(0) NULL DEFAULT NULL COMMENT '出库时间',
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '4' COMMENT '4 已出库 6作废',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `owner_user_id` int(11) NULL DEFAULT NULL COMMENT '负责人',
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注',
  PRIMARY KEY (`outbound_id`) USING BTREE,
  UNIQUE INDEX `outbound_id`(`outbound_id`) USING BTREE,
  UNIQUE INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `warehouse_id`(`warehouse_id`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 56 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '进销存出库表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_outbound
-- ----------------------------
INSERT INTO `wk_jxc_outbound` VALUES (55, '采购退货出库', 17, '5555554646', 25, '2020-09-09 00:00:00', '4', 14773, NULL, 14773, '2d86cf1c14624e2fa43fb9585b9e04d5', '2020-09-17 10:47:23', '2020-09-17 10:47:23', '');

-- ----------------------------
-- Table structure for wk_jxc_outbound_data
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_outbound_data`;
CREATE TABLE `wk_jxc_outbound_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '出库扩展字段数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_outbound_data
-- ----------------------------

-- ----------------------------
-- Table structure for wk_jxc_outbound_product
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_outbound_product`;
CREATE TABLE `wk_jxc_outbound_product`  (
  `outbound_product_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `outbound_product_number` int(11) NULL DEFAULT NULL COMMENT '出库数量',
  `relation_id` int(11) NULL DEFAULT NULL COMMENT '关联ID',
  `product_id` int(11) NULL DEFAULT NULL COMMENT '规格Id',
  `warehouse_id` int(11) NULL DEFAULT NULL COMMENT '仓库Id',
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `remarks` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注',
  PRIMARY KEY (`outbound_product_id`) USING BTREE,
  INDEX `warehouse_id`(`warehouse_id`) USING BTREE,
  INDEX `product_id`(`product_id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 57 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '出库产品关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_outbound_product
-- ----------------------------
INSERT INTO `wk_jxc_outbound_product` VALUES (56, 1, 22, 130, 25, '2d86cf1c14624e2fa43fb9585b9e04d5', '');

-- ----------------------------
-- Table structure for wk_jxc_payment
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_payment`;
CREATE TABLE `wk_jxc_payment`  (
  `payment_note_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `payment_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '付款编号',
  `payment_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '付款类型',
  `related_id` int(11) NULL DEFAULT NULL COMMENT '关联单号',
  `payment_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '付款金额',
  `collection_object` int(11) NULL DEFAULT NULL COMMENT '收款对象',
  `payment_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '付款方式',
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '0 删除 1不删除2正在审核3审核完成',
  `order_time` datetime(0) NULL DEFAULT NULL COMMENT '下单时间',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注',
  `owner_user_id` int(11) NULL DEFAULT NULL,
  `record_id` int(11) NULL DEFAULT NULL COMMENT '审核记录Id',
  PRIMARY KEY (`payment_note_id`) USING BTREE,
  UNIQUE INDEX `payment_note_id`(`payment_note_id`) USING BTREE,
  UNIQUE INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '进销存付款单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_payment
-- ----------------------------
INSERT INTO `wk_jxc_payment` VALUES (26, '5466', '采购', 115, 3213.00, 26, '现金', '3', '2020-09-23 00:00:00', 14773, 14773, '9bb353db78f74e908b8d464509617e81', '2020-09-17 10:47:40', '2020-09-17 11:05:09', '54654', 14773, 19);
INSERT INTO `wk_jxc_payment` VALUES (27, '5464', '采购', 115, 3213.00, 26, '邮政汇款', '3', '2020-09-21 00:00:00', 14773, NULL, 'd20bff49f3de4416820b424031c3fd3d', '2020-09-17 11:04:42', '2020-09-17 11:04:42', '46546', 14773, 17);

-- ----------------------------
-- Table structure for wk_jxc_payment_data
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_payment_data`;
CREATE TABLE `wk_jxc_payment_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '付款单扩展字段数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_payment_data
-- ----------------------------

-- ----------------------------
-- Table structure for wk_jxc_product
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_product`;
CREATE TABLE `wk_jxc_product`  (
  `product_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '产品主建',
  `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '产品名称',
  `product_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '产品类别',
  `product_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '产品编码',
  `product_company` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '产品单位',
  `product_cost_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '产品成本价格',
  `product_market_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '产品市场价',
  `product_ishelf` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '1' COMMENT '是否上架 0下架 1上架 2删除',
  `product_picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '图片',
  `product_picture_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '详情图',
  `product_isp` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '1' COMMENT '是否开启多规格 0否 1是',
  `product_isn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '1' COMMENT '是否开启sn 0 否 1是',
  `sp_data_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '产品规格',
  `supplier_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '供应商Id',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `owner_user_id` int(11) NULL DEFAULT NULL COMMENT '负责人',
  `sp_batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注',
  `is_del` int(2) NULL DEFAULT 0 COMMENT '是否删除 0 否 1 是',
  PRIMARY KEY (`product_id`) USING BTREE,
  UNIQUE INDEX `product_id`(`product_id`) USING BTREE,
  UNIQUE INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `supplier_id`(`supplier_id`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 132 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '进销存产品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_product
-- ----------------------------
INSERT INTO `wk_jxc_product` VALUES (125, '123213', '35', '123', '块', 0.00, 0.00, '1', '', '', '0', '1', '123', ',26,', 14773, 14773, 14773, 'd29967697b514a60b0602989e3c8e088', 'f3090696d55a49d8a6097d7dfad5a663', '2020-09-17 10:07:09', '2020-09-17 10:36:51', '1231', 0);
INSERT INTO `wk_jxc_product` VALUES (126, '2133', '35', '', '枚', 3213.00, 232.00, '1', NULL, '', '1', NULL, '12312:2313;131232:12312;', ',26,', 14773, 14773, 14773, 'ee86cfd5ee584994a10a9e6b8ea11ce8', 'b17f41f09b8e47f2a27e59ecc1953c76', '2020-09-17 10:35:16', '2020-09-17 10:37:23', '213', 0);
INSERT INTO `wk_jxc_product` VALUES (127, '2133', '35', '', '枚', 3213.00, 232.00, '1', NULL, '', '1', NULL, '12312:2313;131232:1233;', ',26,', 14773, NULL, 14773, 'ee86cfd5ee584994a10a9e6b8ea11ce8', 'afc0ac82942f47c287e8c62fd066fc3c', '2020-09-17 10:35:16', '2020-09-17 10:35:16', '213', 0);
INSERT INTO `wk_jxc_product` VALUES (128, '2133', '35', '', '枚', 3213.00, 232.00, '1', NULL, '', '1', NULL, '12312:123213;131232:12312;', ',26,', 14773, NULL, 14773, 'ee86cfd5ee584994a10a9e6b8ea11ce8', '3afa56fcc08b425aa20bfdc766e9a4b1', '2020-09-17 10:35:16', '2020-09-17 10:35:16', '213', 0);
INSERT INTO `wk_jxc_product` VALUES (129, '2133', '35', '', '枚', 3213.00, 232.00, '1', NULL, '', '1', NULL, '12312:123213;131232:1233;', ',26,', 14773, NULL, 14773, 'ee86cfd5ee584994a10a9e6b8ea11ce8', '0197ac616c9f4b27bdff8fc246677586', '2020-09-17 10:35:16', '2020-09-17 10:35:16', '213', 0);
INSERT INTO `wk_jxc_product` VALUES (130, '2133', '35', '1231', '枚', 3213.00, 232.00, '1', '', '', '1', '1', '12312:123123 131232:12313 ', ',26,', 14773, 14773, 14773, 'ee86cfd5ee584994a10a9e6b8ea11ce8', '1bb07d042f084ed097945c9cf73cb0c4', '2020-09-17 10:35:55', '2020-09-17 10:36:58', '213', 0);
INSERT INTO `wk_jxc_product` VALUES (131, '23', '37', 'dddd-1', '个', 0.00, 0.00, '1', '', '', '0', '1', '12323', NULL, 14773, NULL, 14773, '0b80247ce7eb4ab1923c2e56c8fa2a4e', 'b2cf379c97674ced94e5d461986b253a', '2020-09-17 11:18:24', '2020-09-17 11:18:24', '3123', 0);

-- ----------------------------
-- Table structure for wk_jxc_product_data
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_product_data`;
CREATE TABLE `wk_jxc_product_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '产品扩展字段数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_product_data
-- ----------------------------

-- ----------------------------
-- Table structure for wk_jxc_product_specifications
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_product_specifications`;
CREATE TABLE `wk_jxc_product_specifications`  (
  `specifications_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '规格Id',
  `specifications_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '规格名称',
  `specifications_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '规格值可以,隔开',
  `specifications_sort` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '规格排序',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注',
  PRIMARY KEY (`specifications_id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `specifications_sort`(`specifications_sort`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 79 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '产品规格表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_product_specifications
-- ----------------------------
INSERT INTO `wk_jxc_product_specifications` VALUES (77, '12312', '2313,123213', '0', 14773, NULL, 'ee86cfd5ee584994a10a9e6b8ea11ce8', '2020-09-17 10:35:16', '2020-09-17 10:35:16', NULL);
INSERT INTO `wk_jxc_product_specifications` VALUES (78, '131232', '12312,1233', '1', 14773, NULL, 'ee86cfd5ee584994a10a9e6b8ea11ce8', '2020-09-17 10:35:16', '2020-09-17 10:35:16', NULL);

-- ----------------------------
-- Table structure for wk_jxc_product_specifications_data
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_product_specifications_data`;
CREATE TABLE `wk_jxc_product_specifications_data`  (
  `sp_data_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键Id',
  `sp_data_picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '规格数据图片 ，分割',
  `sp_data_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '规格数据值已,号分割',
  `sp_data_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '编码',
  `sp_data_tcode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '条形码',
  `sp_data_sort` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '规格数据排序',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '1',
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '1',
  `pd_batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注',
  PRIMARY KEY (`sp_data_id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `pd_batch_id`(`pd_batch_id`) USING BTREE,
  INDEX `sp_data_sort`(`sp_data_sort`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 131 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '产品规格扩展字段表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_product_specifications_data
-- ----------------------------
INSERT INTO `wk_jxc_product_specifications_data` VALUES (124, NULL, '123', '123', NULL, '0', 14773, 14773, '1', '1', 'f3090696d55a49d8a6097d7dfad5a663', '2020-09-17 10:07:09', '2020-09-17 10:36:51', '1231');
INSERT INTO `wk_jxc_product_specifications_data` VALUES (125, NULL, '12312:2313;131232:12312;', '', NULL, '0', 14773, 14773, 'ee86cfd5ee584994a10a9e6b8ea11ce8', '1', 'b17f41f09b8e47f2a27e59ecc1953c76', '2020-09-17 10:35:16', '2020-09-17 10:37:23', '213');
INSERT INTO `wk_jxc_product_specifications_data` VALUES (126, NULL, '12312:2313;131232:1233;', '', NULL, '1', 14773, NULL, 'ee86cfd5ee584994a10a9e6b8ea11ce8', '1', 'afc0ac82942f47c287e8c62fd066fc3c', '2020-09-17 10:35:16', '2020-09-17 10:35:16', NULL);
INSERT INTO `wk_jxc_product_specifications_data` VALUES (127, NULL, '12312:123213;131232:12312;', '', NULL, '2', 14773, NULL, 'ee86cfd5ee584994a10a9e6b8ea11ce8', '1', '3afa56fcc08b425aa20bfdc766e9a4b1', '2020-09-17 10:35:16', '2020-09-17 10:35:16', NULL);
INSERT INTO `wk_jxc_product_specifications_data` VALUES (128, NULL, '12312:123213;131232:1233;', '', NULL, '3', 14773, NULL, 'ee86cfd5ee584994a10a9e6b8ea11ce8', '1', '0197ac616c9f4b27bdff8fc246677586', '2020-09-17 10:35:16', '2020-09-17 10:35:16', NULL);
INSERT INTO `wk_jxc_product_specifications_data` VALUES (129, NULL, '12312:123123 131232:12313 ', '1231', NULL, '0', 14773, 14773, 'ee86cfd5ee584994a10a9e6b8ea11ce8', '1', '1bb07d042f084ed097945c9cf73cb0c4', '2020-09-17 10:35:55', '2020-09-17 10:36:58', '213');
INSERT INTO `wk_jxc_product_specifications_data` VALUES (130, NULL, '12323', 'dddd-1', NULL, '0', 14773, NULL, '1', '1', 'b2cf379c97674ced94e5d461986b253a', '2020-09-17 11:18:24', '2020-09-17 11:18:24', NULL);

-- ----------------------------
-- Table structure for wk_jxc_product_type
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_product_type`;
CREATE TABLE `wk_jxc_product_type`  (
  `product_type_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '产品类型别Id',
  `product_type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '产品类别名称',
  `product_type_paid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '父级Id',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注',
  PRIMARY KEY (`product_type_id`) USING BTREE,
  UNIQUE INDEX `product_type_id`(`product_type_id`) USING BTREE,
  INDEX `product_type_paid`(`product_type_paid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '产品类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_product_type
-- ----------------------------
INSERT INTO `wk_jxc_product_type` VALUES (35, '默认', NULL, NULL, NULL, '2020-07-22 09:47:26', NULL, NULL);
INSERT INTO `wk_jxc_product_type` VALUES (36, '2132', NULL, NULL, NULL, '2020-09-17 11:17:19', NULL, NULL);
INSERT INTO `wk_jxc_product_type` VALUES (37, '123', '35', NULL, NULL, '2020-09-17 11:17:32', NULL, NULL);

-- ----------------------------
-- Table structure for wk_jxc_purchase
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_purchase`;
CREATE TABLE `wk_jxc_purchase`  (
  `purchase_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `supplier_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '供应商Id',
  `order_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '订单编号',
  `product_number` int(11) NULL DEFAULT NULL COMMENT '产品数量',
  `discount` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '折扣',
  `total_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '总价格',
  `buyer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '采购人',
  `purchase_date` datetime(0) NULL DEFAULT NULL,
  `owner_user_id` int(11) NULL DEFAULT NULL,
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '1' COMMENT '0 删除 1 不删除 2正在审核 3 审核完成',
  `warehousing_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '0正在入库 1全部入库',
  `return_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '1' COMMENT '1准备退货 2 已全部退货',
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注',
  `record_id` int(11) NULL DEFAULT NULL COMMENT '审核记录Id',
  PRIMARY KEY (`purchase_id`) USING BTREE,
  UNIQUE INDEX `purchase_id`(`purchase_id`) USING BTREE,
  UNIQUE INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `supplier_id`(`supplier_id`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 116 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '采购单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_purchase
-- ----------------------------
INSERT INTO `wk_jxc_purchase` VALUES (113, '26', '6546', 1, '0', 3213.00, NULL, '2020-09-08 00:00:00', 14773, 14773, 14773, '0', '0', '1', '8ca4c96ab7e645c5b4e291f61b71bcbe', '2020-09-17 10:42:13', '2020-09-17 10:42:31', '4564', 1);
INSERT INTO `wk_jxc_purchase` VALUES (114, '26', '546', 1, '0', 3213.00, NULL, '2020-09-07 00:00:00', 14773, 14773, 14773, '6', '0', '1', 'fee2ed93a5104fe8b56b6660bed4c7da', '2020-09-17 10:43:04', '2020-09-17 10:43:28', '', 2);
INSERT INTO `wk_jxc_purchase` VALUES (115, '26', '54654', 1, '0', 3213.00, NULL, '2020-09-08 00:00:00', 14773, 14773, NULL, '3', '1', '2', '7171563c6dac4aa08d225782bfc65776', '2020-09-17 10:44:02', '2020-09-17 10:44:02', '546', 3);

-- ----------------------------
-- Table structure for wk_jxc_purchase_data
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_purchase_data`;
CREATE TABLE `wk_jxc_purchase_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '采购单扩展字段数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_purchase_data
-- ----------------------------

-- ----------------------------
-- Table structure for wk_jxc_purchases_product
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_purchases_product`;
CREATE TABLE `wk_jxc_purchases_product`  (
  `purchase_product_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '采购单产品Id',
  `product_id` int(11) NULL DEFAULT NULL COMMENT '产品ID',
  `purchases_number` int(11) NULL DEFAULT NULL COMMENT '采购数量',
  `purchases_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '单价',
  `purchases_subtotal` decimal(10, 2) NULL DEFAULT NULL COMMENT '小计',
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `discount` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '折扣',
  PRIMARY KEY (`purchase_product_id`) USING BTREE,
  INDEX `product_id`(`product_id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 226 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '采购单产品关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_purchases_product
-- ----------------------------
INSERT INTO `wk_jxc_purchases_product` VALUES (221, 130, 1, 3213.00, 3213.00, '8ca4c96ab7e645c5b4e291f61b71bcbe', '0');
INSERT INTO `wk_jxc_purchases_product` VALUES (224, 130, 1, 3213.00, 3213.00, 'fee2ed93a5104fe8b56b6660bed4c7da', '0');
INSERT INTO `wk_jxc_purchases_product` VALUES (225, 130, 1, 3213.00, 3213.00, '7171563c6dac4aa08d225782bfc65776', '0');

-- ----------------------------
-- Table structure for wk_jxc_receipt
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_receipt`;
CREATE TABLE `wk_jxc_receipt`  (
  `receipt_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` int(11) NULL DEFAULT NULL COMMENT '关联单号',
  `order_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '单号',
  `receipt_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '入库类型',
  `warehouse_id` int(11) NULL DEFAULT NULL COMMENT '仓库id',
  `receipt_time` datetime(0) NULL DEFAULT NULL COMMENT '入库时间',
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '5' COMMENT '5已入库 6作废',
  `owner_user_id` int(11) NULL DEFAULT NULL COMMENT '负责人',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注',
  PRIMARY KEY (`receipt_id`) USING BTREE,
  UNIQUE INDEX `receipt_id`(`receipt_id`) USING BTREE,
  UNIQUE INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `warehouse_id`(`warehouse_id`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 68 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '进销存入库表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_receipt
-- ----------------------------
INSERT INTO `wk_jxc_receipt` VALUES (66, 115, '5464', '采购入库', 25, '2020-09-01 00:00:00', '5', 14773, 14773, NULL, 'eb2c73e1dbc64e79aebf1c86e60dac4b', '2020-09-17 10:45:47', '2020-09-17 10:45:47', '5464');
INSERT INTO `wk_jxc_receipt` VALUES (67, NULL, '4564', '其他入库', 25, '2020-09-09 00:00:00', '5', 14773, 14773, NULL, 'a611ae87671646b1b3ece94a25ccf736', '2020-09-17 11:01:22', '2020-09-17 11:01:22', '546');

-- ----------------------------
-- Table structure for wk_jxc_receipt_data
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_receipt_data`;
CREATE TABLE `wk_jxc_receipt_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '入库单扩展字段数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_receipt_data
-- ----------------------------

-- ----------------------------
-- Table structure for wk_jxc_receipt_product
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_receipt_product`;
CREATE TABLE `wk_jxc_receipt_product`  (
  `receipt_product_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `product_id` int(11) NULL DEFAULT NULL COMMENT '规格Id',
  `relation_id` int(11) NULL DEFAULT NULL COMMENT '关联ID',
  `receipt_number` int(11) NULL DEFAULT NULL COMMENT '入库数量',
  `warehouse_id` int(11) NULL DEFAULT NULL COMMENT '仓库Id',
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`receipt_product_id`) USING BTREE,
  INDEX `product_id`(`product_id`) USING BTREE,
  INDEX `warehouse_id`(`warehouse_id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 93 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '入库单产品关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_receipt_product
-- ----------------------------
INSERT INTO `wk_jxc_receipt_product` VALUES (91, 130, 225, 1, 25, 'eb2c73e1dbc64e79aebf1c86e60dac4b', '');
INSERT INTO `wk_jxc_receipt_product` VALUES (92, 130, NULL, 2000, 25, 'a611ae87671646b1b3ece94a25ccf736', '');

-- ----------------------------
-- Table structure for wk_jxc_retreat
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_retreat`;
CREATE TABLE `wk_jxc_retreat`  (
  `retreat_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `supplier_id` int(11) NULL DEFAULT NULL COMMENT '供应商Id',
  `purchase_id` int(11) NULL DEFAULT NULL COMMENT '采购ID',
  `order_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '退款编号',
  `discount` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '折扣',
  `total_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '总价格',
  `reason` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '退货原因',
  `product_number` int(11) NULL DEFAULT NULL COMMENT '产品数量',
  `retreat_date` datetime(0) NULL DEFAULT NULL COMMENT '退货日期',
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '1' COMMENT '0删 1不删 2正在审核 3审核完成',
  `warehousing_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '0正在出库 1全部出库',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `owner_user_id` int(11) NULL DEFAULT NULL COMMENT '负责人',
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注',
  `record_id` int(11) NULL DEFAULT NULL COMMENT '审核记录Id',
  PRIMARY KEY (`retreat_id`) USING BTREE,
  UNIQUE INDEX `retreat_id`(`retreat_id`) USING BTREE,
  UNIQUE INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `supplier_id`(`supplier_id`) USING BTREE,
  INDEX `purchase_id`(`purchase_id`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '采购单退货表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_retreat
-- ----------------------------
INSERT INTO `wk_jxc_retreat` VALUES (16, 26, 115, '456456', NULL, 3213.00, '', 1, '2020-09-02 00:00:00', '0', '0', 14773, 14773, 14773, '61c1ce3ea3f64ceca505ade69005a52e', '2020-09-17 10:46:04', '2020-09-17 10:46:17', '5465', 5);
INSERT INTO `wk_jxc_retreat` VALUES (17, 26, 115, '54646465465', NULL, 3213.00, '456', 1, '2020-09-05 00:00:00', '3', '1', 14773, NULL, 14773, '2bea5795c3ec4383904a126ecbf5e60c', '2020-09-17 10:46:51', '2020-09-17 10:46:51', '456', 6);

-- ----------------------------
-- Table structure for wk_jxc_retreat_data
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_retreat_data`;
CREATE TABLE `wk_jxc_retreat_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '采购单退货扩展字段数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_retreat_data
-- ----------------------------

-- ----------------------------
-- Table structure for wk_jxc_retreat_product
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_retreat_product`;
CREATE TABLE `wk_jxc_retreat_product`  (
  `retreat_product_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `purchase_product_id` int(11) NULL DEFAULT NULL COMMENT '采购产品的Id',
  `retreat_number` int(11) NULL DEFAULT NULL COMMENT '退货数量',
  `retreat_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '退货单价',
  `retreat_subtotal` decimal(10, 2) NULL DEFAULT NULL COMMENT '小计',
  `warehouse_id` int(11) NULL DEFAULT NULL COMMENT '退货仓库Id',
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`retreat_product_id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `warehouse_id`(`warehouse_id`) USING BTREE,
  INDEX `purchase_product_id`(`purchase_product_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '采购单退货关联产品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_retreat_product
-- ----------------------------
INSERT INTO `wk_jxc_retreat_product` VALUES (21, 225, 1, 3213.00, 3213.00, NULL, '61c1ce3ea3f64ceca505ade69005a52e');
INSERT INTO `wk_jxc_retreat_product` VALUES (22, 225, 1, 3213.00, 3213.00, NULL, '2bea5795c3ec4383904a126ecbf5e60c');

-- ----------------------------
-- Table structure for wk_jxc_sale
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_sale`;
CREATE TABLE `wk_jxc_sale`  (
  `sale_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sale_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '合同/订单编号',
  `order_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '合同/订单名称',
  `customer_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '客户名称',
  `opportunity_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '商机名称',
  `sale_time` datetime(0) NULL DEFAULT NULL COMMENT '下单时间',
  `sale_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '合同/订单金额',
  `customer_signer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '客户签约人',
  `company_signer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公司签约人',
  `delivery_time` datetime(0) NULL DEFAULT NULL COMMENT '发货时间',
  `telephone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '电话',
  `contacts` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '联系人',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '1' COMMENT '0 删除 1不删除2正在审核3审核完成',
  `warehousing_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '0 正在出库 1 全部出库',
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '省市区',
  `location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '定位信息',
  `lng` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '地理位置经度',
  `lat` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '地理位置维度',
  `detail_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '详细地址',
  `return_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '1' COMMENT '1准备退货 2 已全部退货',
  `discount` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '折扣',
  `owner_user_id` int(11) NULL DEFAULT NULL,
  `record_id` int(11) NULL DEFAULT NULL COMMENT '审核记录Id',
  PRIMARY KEY (`sale_id`) USING BTREE,
  UNIQUE INDEX `sale_id`(`sale_id`) USING BTREE,
  UNIQUE INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '进销存销售订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_sale
-- ----------------------------
INSERT INTO `wk_jxc_sale` VALUES (26, '45645', '+5+', '138', NULL, '2020-09-08 00:00:00', 232.00, NULL, NULL, '2020-09-16 00:00:00', '18888888888', '546', 14773, NULL, '3', '0', 'd37c74f5536c4490bdec0084199dd610', '2020-09-17 11:06:12', '2020-09-17 11:06:12', '456456', '', '', '', '', '', '1', '0', 14773, 20);

-- ----------------------------
-- Table structure for wk_jxc_sale_data
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_sale_data`;
CREATE TABLE `wk_jxc_sale_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '销售订单扩展字段数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_sale_data
-- ----------------------------

-- ----------------------------
-- Table structure for wk_jxc_sale_product
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_sale_product`;
CREATE TABLE `wk_jxc_sale_product`  (
  `sale_product_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sale_product_number` int(11) NULL DEFAULT NULL COMMENT '销售数量',
  `sale_product_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '单价',
  `sale_product_subtotal` decimal(10, 2) NULL DEFAULT NULL COMMENT '小计',
  `product_id` int(11) NULL DEFAULT NULL COMMENT '产品Id',
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `discount` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '折扣',
  PRIMARY KEY (`sale_product_id`) USING BTREE,
  INDEX `product_id`(`product_id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '销售订单产品关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_sale_product
-- ----------------------------
INSERT INTO `wk_jxc_sale_product` VALUES (35, 1, 232.00, 232.00, 130, 'd37c74f5536c4490bdec0084199dd610', '0');

-- ----------------------------
-- Table structure for wk_jxc_salereturn
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_salereturn`;
CREATE TABLE `wk_jxc_salereturn`  (
  `salereturn_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sale_id` int(11) NULL DEFAULT NULL COMMENT '销售单ID',
  `customer_name` int(11) NULL DEFAULT NULL COMMENT '客户',
  `order_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '退款编号',
  `discount` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '折扣',
  `total_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '总价格',
  `product_number` int(11) NULL DEFAULT NULL COMMENT '产品数量',
  `retreat_date` datetime(0) NULL DEFAULT NULL COMMENT '退货日期',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `owner_user_id` int(11) NULL DEFAULT NULL COMMENT '负责人',
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '1' COMMENT '0 删除 1不删除 2正在审核 3审核完成',
  `warehousing_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '0正在入库 1全部入库',
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注',
  `record_id` int(11) NULL DEFAULT NULL COMMENT '审核记录Id',
  PRIMARY KEY (`salereturn_id`) USING BTREE,
  UNIQUE INDEX `salereturn_id`(`salereturn_id`) USING BTREE,
  UNIQUE INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `sale_id`(`sale_id`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '销售退货表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_salereturn
-- ----------------------------

-- ----------------------------
-- Table structure for wk_jxc_salereturn_data
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_salereturn_data`;
CREATE TABLE `wk_jxc_salereturn_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '销售退货扩展字段数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_salereturn_data
-- ----------------------------

-- ----------------------------
-- Table structure for wk_jxc_salereturn_product
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_salereturn_product`;
CREATE TABLE `wk_jxc_salereturn_product`  (
  `salereturn_product_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sale_product_id` int(11) NULL DEFAULT NULL COMMENT '销售商品Id',
  `salereturn_number` int(11) NULL DEFAULT NULL COMMENT '退货数量',
  `salereturn_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '退货单价',
  `salereturn_subtotal` decimal(10, 2) NULL DEFAULT NULL COMMENT '小计',
  `warehouse_id` int(11) NULL DEFAULT NULL COMMENT '退货仓库Id',
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`salereturn_product_id`) USING BTREE,
  INDEX `sale_product_id`(`sale_product_id`) USING BTREE,
  INDEX `warehouse_id`(`warehouse_id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '销售退货产品关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_salereturn_product
-- ----------------------------

-- ----------------------------
-- Table structure for wk_jxc_scene
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_scene`;
CREATE TABLE `wk_jxc_scene`  (
  `scene_id` int(255) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '场景名',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '查询数据',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '添加用户',
  `type` int(255) NULL DEFAULT NULL,
  `is_default` int(11) NULL DEFAULT 0 COMMENT '0自定义 1隐藏 2 系统',
  `default_scene` int(2) NULL DEFAULT 0 COMMENT '是否默认 0 否 1 是',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注',
  PRIMARY KEY (`scene_id`) USING BTREE,
  UNIQUE INDEX `scene_id`(`scene_id`) USING BTREE,
  INDEX `user_id`(`user_id`, `type`) USING BTREE,
  INDEX `is_default`(`is_default`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 690 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '进销存产品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_scene
-- ----------------------------
INSERT INTO `wk_jxc_scene` VALUES (654, '全部供应商', '[{\"type\":19}]', 14773, 2, 2, 0, 14773, NULL, '2020-09-17 10:06:30', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (655, '我负责的供应商', '[{\"values\":[\"14773\"],\"type\":17,\"name\":\"owner_user_id\"}]', 14773, 2, 2, 0, 14773, NULL, '2020-09-17 10:06:30', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (656, '下属负责的供应商', '[{\"values\":[\"14773\"],\"type\":18,\"name\":\"owner_user_id\"}]', 14773, 2, 2, 0, 14773, NULL, '2020-09-17 10:06:30', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (657, '全部产品', '[{\"type\":19}]', 14773, 1, 2, 0, 14773, NULL, '2020-09-17 10:06:34', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (658, '上架产品', '[{\"values\":[\"1\"],\"type\":1,\"name\":\"product_ishelf\"}]', 14773, 1, 2, 0, 14773, NULL, '2020-09-17 10:06:34', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (659, '下架产品', '[{\"values\":[\"0\"],\"type\":1,\"name\":\"product_ishelf\"}]', 14773, 1, 2, 0, 14773, NULL, '2020-09-17 10:06:34', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (660, '全部销售订单', '[{\"type\":19}]', 14773, 5, 2, 0, 14773, NULL, '2020-09-17 10:38:00', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (661, '我负责的销售订单', '[{\"values\":[\"14773\"],\"type\":1,\"name\":\"owner_user_id\"}]', 14773, 5, 2, 0, 14773, NULL, '2020-09-17 10:38:00', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (662, '下属负责的销售订单', '[{\"values\":[\"1\"],\"type\":1,\"name\":\"owner_user_id\"}]', 14773, 5, 2, 0, 14773, NULL, '2020-09-17 10:38:00', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (663, '全部采购退货', '[{\"type\":19}]', 14773, 4, 2, 0, 14773, NULL, '2020-09-17 10:41:49', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (664, '我负责的采购退货', '[{\"values\":[\"14773\"],\"type\":17,\"name\":\"owner_user_id\"}]', 14773, 4, 2, 0, 14773, NULL, '2020-09-17 10:41:49', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (665, '下属负责的采购退货', '[{\"values\":[\"14773\"],\"type\":18,\"name\":\"owner_user_id\"}]', 14773, 4, 2, 0, 14773, NULL, '2020-09-17 10:41:49', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (666, '全部采购订单', '[{\"type\":19}]', 14773, 3, 2, 0, 14773, NULL, '2020-09-17 10:41:50', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (667, '我负责的采购订单', '[{\"values\":[\"14773\"],\"type\":17,\"name\":\"owner_user_id\"}]', 14773, 3, 2, 0, 14773, NULL, '2020-09-17 10:41:50', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (668, '下属负责的采购订单', '[{\"values\":[\"14773\"],\"type\":18,\"name\":\"owner_user_id\"}]', 14773, 3, 2, 0, 14773, NULL, '2020-09-17 10:41:50', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (669, '全部入库', '[{\"type\":19}]', 14773, 7, 2, 0, 14773, NULL, '2020-09-17 10:48:11', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (670, '我负责的入库', '[{\"values\":[\"14773\"],\"type\":17,\"name\":\"owner_user_id\"}]', 14773, 7, 2, 0, 14773, NULL, '2020-09-17 10:48:11', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (671, '我下属负责的入库', '[{\"values\":[\"14773\"],\"type\":18,\"name\":\"owner_user_id\"}]', 14773, 7, 2, 0, 14773, NULL, '2020-09-17 10:48:11', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (672, '全部盘点', '[{\"type\":19}]', 14773, 11, 2, 0, 14773, NULL, '2020-09-17 10:48:43', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (673, '我负责的盘点', '[{\"values\":[\"14773\"],\"type\":17,\"name\":\"owner_user_id\"}]', 14773, 11, 2, 0, 14773, NULL, '2020-09-17 10:48:43', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (674, '下属负责的盘点', '[{\"values\":[\"14773\"],\"type\":18,\"name\":\"owner_user_id\"}]', 14773, 11, 2, 0, 14773, NULL, '2020-09-17 10:48:43', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (675, '全部调拨', '[{\"type\":19}]', 14773, 12, 2, 0, 14773, NULL, '2020-09-17 10:59:40', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (676, '我负责的调拨', '[{\"values\":[\"14773\"],\"type\":17,\"name\":\"owner_user_id\"}]', 14773, 12, 2, 0, 14773, NULL, '2020-09-17 10:59:40', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (677, '下属负责的调拨', '[{\"values\":[\"14773\"],\"type\":18,\"name\":\"owner_user_id\"}]', 14773, 12, 2, 0, 14773, NULL, '2020-09-17 10:59:41', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (678, '全部出库', '[{\"type\":19}]', 14773, 8, 2, 0, 14773, NULL, '2020-09-17 11:03:39', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (679, '我负责的出库', '[{\"values\":[\"14773\"],\"type\":17,\"name\":\"owner_user_id\"}]', 14773, 8, 2, 0, 14773, NULL, '2020-09-17 11:03:39', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (680, '我下属负责的出库', '[{\"values\":[\"14773\"],\"type\":18,\"name\":\"owner_user_id\"}]', 14773, 8, 2, 0, 14773, NULL, '2020-09-17 11:03:39', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (681, '全部回款', '[{\"type\":19}]', 14773, 10, 2, 0, 14773, NULL, '2020-09-17 11:03:49', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (682, '我负责的回款', '[{\"values\":[\"14773\"],\"type\":17,\"name\":\"owner_user_id\"}]', 14773, 10, 2, 0, 14773, NULL, '2020-09-17 11:03:49', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (683, '下属负责的回款', '[{\"values\":[\"14773\"],\"type\":18,\"name\":\"ownerUserId\"}]', 14773, 10, 2, 0, 14773, NULL, '2020-09-17 11:03:49', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (684, '全部付款', '[{\"type\":19}]', 14773, 9, 2, 0, 14773, NULL, '2020-09-17 11:04:26', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (685, '我负责的付款', '[{\"values\":[\"14773\"],\"type\":17,\"name\":\"owner_user_id\"}]', 14773, 9, 2, 0, 14773, NULL, '2020-09-17 11:04:26', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (686, '下属负责的付款', '[{\"values\":[\"14773\"],\"type\":18,\"name\":\"owner_user_id\"}]', 14773, 9, 2, 0, 14773, NULL, '2020-09-17 11:04:26', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (687, '全部销售退货', '[{\"type\":19}]', 14773, 6, 2, 0, 14773, NULL, '2020-09-17 11:05:45', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (688, '我负责的销售退货', '[{\"values\":[\"1\"],\"type\":17,\"name\":\"owner_user_id\"}]', 14773, 6, 2, 0, 14773, NULL, '2020-09-17 11:05:45', NULL, NULL);
INSERT INTO `wk_jxc_scene` VALUES (689, '下属负责的销售退货', '[{\"values\":[\"14773\"],\"type\":18,\"name\":\"owner_user_id\"}]', 14773, 6, 2, 0, 14773, NULL, '2020-09-17 11:05:45', NULL, NULL);

-- ----------------------------
-- Table structure for wk_jxc_supplier
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_supplier`;
CREATE TABLE `wk_jxc_supplier`  (
  `supplier_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `supplier_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '供应商名称',
  `supplier_level` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '供应商级别',
  `contacts` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '联系人',
  `contact_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '联系人电话',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '省市区',
  `location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '定位信息',
  `lng` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '地理位置经度',
  `lat` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '地理位置维度',
  `detail_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '详细地址',
  `owner_user_id` int(11) NULL DEFAULT NULL COMMENT '负责人',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '1' COMMENT '0 删除 1不删除',
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注',
  PRIMARY KEY (`supplier_id`) USING BTREE,
  UNIQUE INDEX `supplier_id`(`supplier_id`) USING BTREE,
  UNIQUE INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '供应商表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_supplier
-- ----------------------------
INSERT INTO `wk_jxc_supplier` VALUES (26, '546546', 'A', '54654', '13939884969', '广东省,梅州市,五华县', '五华县五华县', '115.78248934924261', '23.9384094154491', '五华县五华县', 14774, 14773, 14773, '1', 'ddca35409ecf4537b6fd3af1b0a81499', '2020-09-17 10:39:55', '2020-09-17 10:40:02', '546');

-- ----------------------------
-- Table structure for wk_jxc_supplier_data
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_supplier_data`;
CREATE TABLE `wk_jxc_supplier_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `batch_id`(`batch_id`) USING BTREE,
  INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '供应商扩展字段数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_supplier_data
-- ----------------------------

-- ----------------------------
-- Table structure for wk_jxc_warehouse
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_warehouse`;
CREATE TABLE `wk_jxc_warehouse`  (
  `warehouse_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `warehouse_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '仓库名称',
  `warehouse_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '仓库编码',
  `warehouse_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '仓库地址',
  `warehouse_isuse` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '1' COMMENT '是否使用0 否 1是',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `owner_user_id` int(11) NULL DEFAULT NULL COMMENT '负责人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '备注',
  PRIMARY KEY (`warehouse_id`) USING BTREE,
  UNIQUE INDEX `warehouse_id`(`warehouse_id`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '进销存仓库表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_warehouse
-- ----------------------------
INSERT INTO `wk_jxc_warehouse` VALUES (25, '仓库A', '456', '456', '1', 14773, NULL, NULL, NULL, '2020-09-17 10:45:29', '2020-09-17 10:45:29', NULL);
INSERT INTO `wk_jxc_warehouse` VALUES (26, '仓库B', '456546', '546546', '1', 14773, NULL, NULL, NULL, '2020-09-17 11:00:11', '2020-09-17 11:00:11', NULL);

-- ----------------------------
-- Table structure for wk_jxc_warehouse_product
-- ----------------------------
DROP TABLE IF EXISTS `wk_jxc_warehouse_product`;
CREATE TABLE `wk_jxc_warehouse_product`  (
  `warehouse_product_id` int(255) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `warehouse_id` int(11) NULL DEFAULT NULL COMMENT '仓库Id',
  `product_id` int(11) NULL DEFAULT NULL COMMENT '规格ID',
  `original_quantity` int(11) NULL DEFAULT 0 COMMENT '总数量',
  `remaining_quantity` int(11) NULL DEFAULT 0 COMMENT '剩余数量',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`warehouse_product_id`) USING BTREE,
  UNIQUE INDEX `warehouse_product_id`(`warehouse_id`, `product_id`) USING BTREE,
  INDEX `warehouse_id`(`warehouse_id`) USING BTREE,
  INDEX `product_id`(`product_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 339 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '进销存产品仓库关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wk_jxc_warehouse_product
-- ----------------------------
INSERT INTO `wk_jxc_warehouse_product` VALUES (325, 25, 125, 0, 0, NULL, '2020-09-17 10:45:29');
INSERT INTO `wk_jxc_warehouse_product` VALUES (326, 25, 126, 0, 0, NULL, '2020-09-17 10:45:29');
INSERT INTO `wk_jxc_warehouse_product` VALUES (327, 25, 127, 0, 0, NULL, '2020-09-17 10:45:29');
INSERT INTO `wk_jxc_warehouse_product` VALUES (328, 25, 128, 0, 0, NULL, '2020-09-17 10:45:29');
INSERT INTO `wk_jxc_warehouse_product` VALUES (329, 25, 129, 0, 0, NULL, '2020-09-17 10:45:29');
INSERT INTO `wk_jxc_warehouse_product` VALUES (330, 25, 130, 2001, 1960, NULL, '2020-09-17 10:45:29');
INSERT INTO `wk_jxc_warehouse_product` VALUES (331, 26, 125, 0, 0, NULL, '2020-09-17 11:00:11');
INSERT INTO `wk_jxc_warehouse_product` VALUES (332, 26, 126, 0, 0, NULL, '2020-09-17 11:00:11');
INSERT INTO `wk_jxc_warehouse_product` VALUES (333, 26, 127, 0, 0, NULL, '2020-09-17 11:00:11');
INSERT INTO `wk_jxc_warehouse_product` VALUES (334, 26, 128, 0, 0, NULL, '2020-09-17 11:00:11');
INSERT INTO `wk_jxc_warehouse_product` VALUES (335, 26, 129, 0, 0, NULL, '2020-09-17 11:00:11');
INSERT INTO `wk_jxc_warehouse_product` VALUES (336, 26, 130, 0, 7, NULL, '2020-09-17 11:00:11');
INSERT INTO `wk_jxc_warehouse_product` VALUES (337, 25, 131, 0, 0, NULL, '2020-09-17 11:18:24');
INSERT INTO `wk_jxc_warehouse_product` VALUES (338, 26, 131, 0, 0, NULL, '2020-09-17 11:18:24');

SET FOREIGN_KEY_CHECKS = 1;
