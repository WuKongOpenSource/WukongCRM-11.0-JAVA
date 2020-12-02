SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
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


ALTER TABLE `wk_work` ADD COLUMN `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间' AFTER `owner_user_id`;

SET FOREIGN_KEY_CHECKS = 1;
