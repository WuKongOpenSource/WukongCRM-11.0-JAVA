SET FOREIGN_KEY_CHECKS=0;

ALTER TABLE `wk_crm_business` MODIFY COLUMN `customer_id` int(11) NULL DEFAULT NULL COMMENT '客户ID' AFTER `next_time`;

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
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '市场活动字段表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '市场活动表单信息' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '审批表' ROW_FORMAT = Dynamic;

CREATE TABLE `wk_examine_condition`  (
  `condition_id` int(11) NOT NULL AUTO_INCREMENT,
  `condition_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '条件名称',
  `flow_id` int(11) NOT NULL COMMENT '审批流程ID',
  `priority` int(4) NOT NULL COMMENT '优先级 数字越低优先级越高',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `batch_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '批次ID',
  PRIMARY KEY (`condition_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '审批条件表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '审批条件扩展字段表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '审批流程表' ROW_FORMAT = Dynamic;

CREATE TABLE `wk_examine_flow_continuous_superior`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `flow_id` int(11) NOT NULL COMMENT '审批流程ID',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色ID',
  `max_level` int(2) NULL DEFAULT NULL COMMENT '角色审批的最高级别或者组织架构的第N级',
  `type` int(1) NULL DEFAULT NULL COMMENT '1 指定角色 2 组织架构的最上级',
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '批次ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '审批流程连续多级主管审批记录表' ROW_FORMAT = Dynamic;

CREATE TABLE `wk_examine_flow_member`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `flow_id` int(11) NOT NULL COMMENT '审批流程ID',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '审批人ID',
  `type` int(1) NULL DEFAULT NULL COMMENT '1 依次审批 2 会签 3 或签',
  `sort` int(1) NOT NULL DEFAULT 0 COMMENT '排序规则',
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '批次ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '审批流程指定成员记录表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '审批流程自选成员记录表' ROW_FORMAT = Dynamic;

CREATE TABLE `wk_examine_flow_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `flow_id` int(11) NOT NULL COMMENT '审核流程ID',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色ID',
  `type` int(1) NULL DEFAULT NULL COMMENT '2 会签 3 或签',
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '批次ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '审批流程角色审批记录表' ROW_FORMAT = Dynamic;

CREATE TABLE `wk_examine_flow_superior`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `flow_id` int(11) NOT NULL COMMENT '审核流程ID',
  `parent_level` int(2) NULL DEFAULT NULL COMMENT '直属上级级别 1 代表直属上级 2 代表 直属上级的上级',
  `type` int(1) NULL DEFAULT NULL COMMENT '找不到上级时，是否由上一级上级代审批 0 否 1 是',
  `batch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '批次ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '审批流程主管审批记录表' ROW_FORMAT = Dynamic;

CREATE TABLE `wk_examine_manager_user`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `examine_id` bigint(10) UNSIGNED NOT NULL COMMENT '审批ID',
  `user_id` bigint(20) NOT NULL COMMENT '管理员ID',
  `sort` int(5) NOT NULL DEFAULT 0 COMMENT '从小到大',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `examine_id`(`examine_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '审批管理员设置表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '审核记录表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '审核日志表' ROW_FORMAT = Dynamic;

CREATE TABLE `wk_examine_record_optional`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `flow_id` int(11) NOT NULL COMMENT '流程ID',
  `record_id` int(11) NOT NULL COMMENT '审核记录ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `sort` int(2) NOT NULL DEFAULT 1 COMMENT '排序。从小到大',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '审核自选成员选择成员表' ROW_FORMAT = Dynamic;

ALTER TABLE `wk_oa_examine` ADD COLUMN `examine_record_id` int(11) NULL DEFAULT NULL COMMENT '审核记录ID' AFTER `batch_id`;

ALTER TABLE `wk_oa_examine` ADD COLUMN `examine_status` int(1) NULL DEFAULT NULL COMMENT '审核状态 0 未审核 1 审核通过 2 审核拒绝 3 审核中 4 已撤回' AFTER `examine_record_id`;

SET FOREIGN_KEY_CHECKS=1;