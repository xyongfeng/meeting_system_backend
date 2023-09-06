/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 50743
 Source Host           : localhost:3306
 Source Schema         : meeting

 Target Server Type    : MySQL
 Target Server Version : 50743
 File Encoding         : 65001

 Date: 06/09/2023 12:38:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_admin_log_xq
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_log_xq`;
CREATE TABLE `t_admin_log_xq`  (
  `id_xq` int(11) NOT NULL AUTO_INCREMENT,
  `action_module_xq` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作模块',
  `action_user_id_xq` int(11) NOT NULL COMMENT '操作人id',
  `action_url_xq` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'url',
  `action_content_xq` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '请求参数',
  `action_type_xq` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作类型 查看 编辑 删除 新增',
  `action_ip_xq` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作ip',
  `action_time_xq` datetime NOT NULL COMMENT '操作时间',
  `action_success_xq` tinyint(1) NOT NULL COMMENT '操作是否成功',
  PRIMARY KEY (`id_xq`) USING BTREE,
  INDEX `action_user_id`(`action_user_id_xq`) USING BTREE,
  CONSTRAINT `t_admin_log_xq_ibfk_1` FOREIGN KEY (`action_user_id_xq`) REFERENCES `t_users_xq` (`id_xq`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1548 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_admin_log_xq
-- ----------------------------

-- ----------------------------
-- Table structure for t_chat_filter_xq
-- ----------------------------
DROP TABLE IF EXISTS `t_chat_filter_xq`;
CREATE TABLE `t_chat_filter_xq`  (
  `id_xq` int(11) NOT NULL AUTO_INCREMENT,
  `filter_content_xq` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '过滤内容',
  `filter_rule_xq` int(11) NOT NULL COMMENT '过滤规则 1 直接替换，2 正则表达式替换',
  `replace_content_xq` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '替换内容',
  `appender_id_xq` int(255) NOT NULL COMMENT '添加人的id',
  `append_time_xq` datetime NOT NULL COMMENT '添加时间',
  `enable_xq` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用',
  PRIMARY KEY (`id_xq`) USING BTREE,
  INDEX `appender_id`(`appender_id_xq`) USING BTREE,
  CONSTRAINT `t_chat_filter_xq_ibfk_1` FOREIGN KEY (`appender_id_xq`) REFERENCES `t_users_xq` (`id_xq`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_chat_filter_xq
-- ----------------------------

-- ----------------------------
-- Table structure for t_meeting_application_xq
-- ----------------------------
DROP TABLE IF EXISTS `t_meeting_application_xq`;
CREATE TABLE `t_meeting_application_xq`  (
  `id_xq` int(11) NOT NULL AUTO_INCREMENT,
  `applicant_id_xq` int(11) NOT NULL COMMENT '申请人id',
  `meeting_id_xq` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '会议id',
  `state_xq` tinyint(2) NOT NULL COMMENT '0 未读  1 已读',
  `send_time_xq` datetime NOT NULL,
  PRIMARY KEY (`id_xq`) USING BTREE,
  INDEX `mau`(`applicant_id_xq`) USING BTREE,
  INDEX `mam`(`meeting_id_xq`) USING BTREE,
  CONSTRAINT `mam` FOREIGN KEY (`meeting_id_xq`) REFERENCES `t_meeting_xq` (`id_xq`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `mau` FOREIGN KEY (`applicant_id_xq`) REFERENCES `t_users_xq` (`id_xq`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_meeting_application_xq
-- ----------------------------

-- ----------------------------
-- Table structure for t_meeting_chat_xq
-- ----------------------------
DROP TABLE IF EXISTS `t_meeting_chat_xq`;
CREATE TABLE `t_meeting_chat_xq`  (
  `id_xq` int(11) NOT NULL AUTO_INCREMENT,
  `meeting_id_xq` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_id_xq` int(11) NOT NULL,
  `msg_xq` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `send_time_xq` datetime NOT NULL,
  PRIMARY KEY (`id_xq`) USING BTREE,
  INDEX `user_id`(`user_id_xq`) USING BTREE,
  INDEX `meeting_id`(`meeting_id_xq`) USING BTREE,
  CONSTRAINT `t_meeting_chat_xq_ibfk_1` FOREIGN KEY (`user_id_xq`) REFERENCES `t_users_xq` (`id_xq`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `t_meeting_chat_xq_ibfk_2` FOREIGN KEY (`meeting_id_xq`) REFERENCES `t_meeting_xq` (`id_xq`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_meeting_chat_xq
-- ----------------------------

-- ----------------------------
-- Table structure for t_meeting_notice_users_xq
-- ----------------------------
DROP TABLE IF EXISTS `t_meeting_notice_users_xq`;
CREATE TABLE `t_meeting_notice_users_xq`  (
  `id_xq` int(11) NOT NULL AUTO_INCREMENT,
  `user_id_xq` int(11) NOT NULL COMMENT '接收人id',
  `notice_id_xq` int(11) NOT NULL COMMENT '通知id',
  `state_xq` tinyint(1) UNSIGNED ZEROFILL NOT NULL DEFAULT 0 COMMENT '0 未读 1已读',
  PRIMARY KEY (`id_xq`) USING BTREE,
  INDEX `user_id`(`user_id_xq`) USING BTREE,
  INDEX `notice_id_xq`(`notice_id_xq`) USING BTREE,
  CONSTRAINT `t_meeting_notice_users_xq_ibfk_1` FOREIGN KEY (`user_id_xq`) REFERENCES `t_users_xq` (`id_xq`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `t_meeting_notice_users_xq_ibfk_2` FOREIGN KEY (`notice_id_xq`) REFERENCES `t_meeting_notice_xq` (`id_xq`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_meeting_notice_users_xq
-- ----------------------------

-- ----------------------------
-- Table structure for t_meeting_notice_xq
-- ----------------------------
DROP TABLE IF EXISTS `t_meeting_notice_xq`;
CREATE TABLE `t_meeting_notice_xq`  (
  `id_xq` int(11) NOT NULL AUTO_INCREMENT,
  `meeting_id_xq` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '会议id',
  `sender_id_xq` int(11) NOT NULL COMMENT '发送人id',
  `title_xq` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '通知标题',
  `content_xq` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '通知内容',
  `type_xq` tinyint(1) NOT NULL DEFAULT 1 COMMENT '公告类型 1 普通公告 2 推送公告',
  `send_time_xq` datetime NOT NULL COMMENT '发送时间',
  `update_time_xq` datetime NOT NULL COMMENT '最近修改时间',
  `hidden_xq` tinyint(1) NOT NULL DEFAULT 0 COMMENT '隐藏，不会出现在公告列表里面，通常用来发送私有通知',
  PRIMARY KEY (`id_xq`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 161 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_meeting_notice_xq
-- ----------------------------

-- ----------------------------
-- Table structure for t_meeting_password_xq
-- ----------------------------
DROP TABLE IF EXISTS `t_meeting_password_xq`;
CREATE TABLE `t_meeting_password_xq`  (
  `id_xq` int(11) NOT NULL AUTO_INCREMENT,
  `meeting_id_xq` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password_xq` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '会议密码',
  `enabled_xq` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否启用',
  PRIMARY KEY (`id_xq`) USING BTREE,
  INDEX `meeting_id`(`meeting_id_xq`) USING BTREE,
  CONSTRAINT `t_meeting_password_xq_ibfk_1` FOREIGN KEY (`meeting_id_xq`) REFERENCES `t_meeting_xq` (`id_xq`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_meeting_password_xq
-- ----------------------------

-- ----------------------------
-- Table structure for t_meeting_screenshot_xq
-- ----------------------------
DROP TABLE IF EXISTS `t_meeting_screenshot_xq`;
CREATE TABLE `t_meeting_screenshot_xq`  (
  `id_xq` int(11) NOT NULL AUTO_INCREMENT,
  `user_id_xq` int(11) NOT NULL,
  `meeting_id_xq` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `path_xq` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id_xq`) USING BTREE,
  INDEX `user_id`(`user_id_xq`) USING BTREE,
  INDEX `meeting_id`(`meeting_id_xq`) USING BTREE,
  CONSTRAINT `t_meeting_screenshot_xq_ibfk_1` FOREIGN KEY (`user_id_xq`) REFERENCES `t_users_xq` (`id_xq`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `t_meeting_screenshot_xq_ibfk_2` FOREIGN KEY (`meeting_id_xq`) REFERENCES `t_meeting_xq` (`id_xq`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_meeting_screenshot_xq
-- ----------------------------

-- ----------------------------
-- Table structure for t_meeting_users_xq
-- ----------------------------
DROP TABLE IF EXISTS `t_meeting_users_xq`;
CREATE TABLE `t_meeting_users_xq`  (
  `id_xq` int(11) NOT NULL AUTO_INCREMENT,
  `meeting_id_xq` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `users_id_xq` int(11) NOT NULL,
  `had_sign_in_xq` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否完成签到',
  `had_sign_in_time_xq` datetime NULL DEFAULT NULL COMMENT '签到时间',
  `had_ban_xq` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否被加入黑名单',
  `had_muted_xq` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否被静言',
  `had_banup_xq` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否被禁止投屏',
  `uping_xq` tinyint(1) NOT NULL DEFAULT 0 COMMENT '投屏中',
  `speeching_xq` tinyint(1) NOT NULL DEFAULT 0 COMMENT '语音中',
  `exist_minute_xq` int(11) NOT NULL DEFAULT 0 COMMENT '参会累计时长(分钟)',
  `to_user_hidden_xq` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除了该会议的记录就对他隐藏',
  `is_founder_xq` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否为会议主持人',
  PRIMARY KEY (`id_xq`) USING BTREE,
  INDEX `users_id`(`users_id_xq`) USING BTREE,
  INDEX `meeting_id`(`meeting_id_xq`) USING BTREE,
  CONSTRAINT `t_meeting_users_xq_ibfk_1` FOREIGN KEY (`meeting_id_xq`) REFERENCES `t_meeting_xq` (`id_xq`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `users_id` FOREIGN KEY (`users_id_xq`) REFERENCES `t_users_xq` (`id_xq`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 104 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_meeting_users_xq
-- ----------------------------

-- ----------------------------
-- Table structure for t_meeting_xq
-- ----------------------------
DROP TABLE IF EXISTS `t_meeting_xq`;
CREATE TABLE `t_meeting_xq`  (
  `id_xq` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `name_xq` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '会议名称',
  `user_id_xq` int(11) NOT NULL COMMENT '创建会议的用户id（主持人）',
  `create_date_xq` datetime NOT NULL COMMENT '创建会议的时间',
  `start_date_xq` datetime NOT NULL COMMENT '会议开始时间',
  `end_date_xq` datetime NULL DEFAULT NULL COMMENT '会议结束时间',
  `haveLicence_xq` tinyint(1) NOT NULL DEFAULT 0 COMMENT '进入会议是否需要创建者认可',
  `end_xq` tinyint(1) NOT NULL DEFAULT 0 COMMENT '会议是否已经结束',
  `to_owner_hidden_xq` tinyint(1) NOT NULL DEFAULT 0 COMMENT '主持人删除了该会议的记录就对他隐藏',
  `max_number_xq` int(11) NOT NULL DEFAULT 50 COMMENT '参会最多人数',
  `need_face_xq` tinyint(1) NOT NULL DEFAULT 0 COMMENT '进入房间是否需要人脸验证',
  PRIMARY KEY (`id_xq`) USING BTREE,
  INDEX `userid`(`user_id_xq`) USING BTREE,
  CONSTRAINT `userid` FOREIGN KEY (`user_id_xq`) REFERENCES `t_users_xq` (`id_xq`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_meeting_xq
-- ----------------------------

-- ----------------------------
-- Table structure for t_menu_role_xq
-- ----------------------------
DROP TABLE IF EXISTS `t_menu_role_xq`;
CREATE TABLE `t_menu_role_xq`  (
  `id_xq` int(11) NOT NULL COMMENT 'id',
  `menu_id_xq` int(11) NULL DEFAULT NULL COMMENT 'menu_id',
  `role_id_xq` int(11) NULL DEFAULT NULL COMMENT 'role_id',
  PRIMARY KEY (`id_xq`) USING BTREE,
  INDEX `menu_id2`(`menu_id_xq`) USING BTREE,
  INDEX `role_id2`(`role_id_xq`) USING BTREE,
  CONSTRAINT `menu_id2` FOREIGN KEY (`menu_id_xq`) REFERENCES `t_menu_xq` (`id_xq`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `role_id2` FOREIGN KEY (`role_id_xq`) REFERENCES `t_role_xq` (`id_xq`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_menu_role_xq
-- ----------------------------
INSERT INTO `t_menu_role_xq` VALUES (1, 2, 1);
INSERT INTO `t_menu_role_xq` VALUES (2, 3, 2);
INSERT INTO `t_menu_role_xq` VALUES (3, 4, 1);
INSERT INTO `t_menu_role_xq` VALUES (4, 5, 2);
INSERT INTO `t_menu_role_xq` VALUES (5, 6, 4);
INSERT INTO `t_menu_role_xq` VALUES (6, 7, 4);
INSERT INTO `t_menu_role_xq` VALUES (7, 8, 5);
INSERT INTO `t_menu_role_xq` VALUES (8, 9, 5);
INSERT INTO `t_menu_role_xq` VALUES (9, 10, 6);
INSERT INTO `t_menu_role_xq` VALUES (10, 11, 6);
INSERT INTO `t_menu_role_xq` VALUES (11, 12, 7);
INSERT INTO `t_menu_role_xq` VALUES (12, 13, 7);

-- ----------------------------
-- Table structure for t_menu_xq
-- ----------------------------
DROP TABLE IF EXISTS `t_menu_xq`;
CREATE TABLE `t_menu_xq`  (
  `id_xq` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `url_xq` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'url',
  `path_xq` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'path',
  `component_xq` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件',
  `name_xq` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单名',
  `iconCls_xq` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
  `requireAuth_xq` tinyint(1) NULL DEFAULT NULL COMMENT '是否要求权限',
  `parentid_xq` int(11) NULL DEFAULT NULL COMMENT '父ID',
  `enabled_xq` tinyint(1) NULL DEFAULT NULL COMMENT '是否启用',
  PRIMARY KEY (`id_xq`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_menu_xq
-- ----------------------------
INSERT INTO `t_menu_xq` VALUES (1, '/', '', NULL, '所有', NULL, NULL, NULL, 1);
INSERT INTO `t_menu_xq` VALUES (2, '/', '/home', 'Home', '用户管理', 'User', 1, NULL, 1);
INSERT INTO `t_menu_xq` VALUES (3, '/', '/home', 'Home', '会议管理', 'Comment', 1, NULL, 1);
INSERT INTO `t_menu_xq` VALUES (4, '/', '/userList', 'UserList', '用户列表', '', 1, 2, 1);
INSERT INTO `t_menu_xq` VALUES (5, '/', '/meetingList', 'MeetingList', '会议列表', '', 1, 3, 1);
INSERT INTO `t_menu_xq` VALUES (6, '/', '/home', 'Home', '聊天过滤', 'Filter', 1, NULL, 1);
INSERT INTO `t_menu_xq` VALUES (7, '/', '/chatFilter', 'ChatFilter', '聊天过滤', NULL, 1, 6, 1);
INSERT INTO `t_menu_xq` VALUES (8, '/', '/home', 'Home', '数据统计', 'Histogram', 1, NULL, 1);
INSERT INTO `t_menu_xq` VALUES (9, '/', '/dataStatistics', 'DataStatistics', '数据统计', NULL, 1, 8, 1);
INSERT INTO `t_menu_xq` VALUES (10, '/', '/home', 'Home', '管理日志', 'View', 1, NULL, 1);
INSERT INTO `t_menu_xq` VALUES (11, '/', '/adminLog', 'AdminLog', '管理日志', NULL, 1, 10, 1);
INSERT INTO `t_menu_xq` VALUES (12, '/', '/home', 'Home', '用户意见', 'Postcard', 1, NULL, 1);
INSERT INTO `t_menu_xq` VALUES (13, '/', '/userAdvice', 'UserAdvice', '用户意见', NULL, 1, 12, 1);

-- ----------------------------
-- Table structure for t_role_xq
-- ----------------------------
DROP TABLE IF EXISTS `t_role_xq`;
CREATE TABLE `t_role_xq`  (
  `id_xq` int(11) NOT NULL COMMENT 'id',
  `perms_xq` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限识别',
  `name_xq` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限名',
  `remark_xq` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `hidden_xq` tinyint(1) NULL DEFAULT 0 COMMENT '是否隐藏',
  PRIMARY KEY (`id_xq`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_role_xq
-- ----------------------------
INSERT INTO `t_role_xq` VALUES (1, 'user', '用户管理', '可以对用户增删改查', 0);
INSERT INTO `t_role_xq` VALUES (2, 'meeting', '会议管理', '可以对会议增删改查', 0);
INSERT INTO `t_role_xq` VALUES (3, 'role', '权限管理', '可以对权限增删改查', 1);
INSERT INTO `t_role_xq` VALUES (4, 'chatFilter', '聊天过滤', '管理聊天过滤词', 0);
INSERT INTO `t_role_xq` VALUES (5, 'dataStatistics', '数据统计', '查看数据统计表', 0);
INSERT INTO `t_role_xq` VALUES (6, 'adminLog', '管理日志', '管理操作日志', 0);
INSERT INTO `t_role_xq` VALUES (7, 'userAdvice', '用户建议', '管理用户建议', 0);

-- ----------------------------
-- Table structure for t_user_advice_img_xq
-- ----------------------------
DROP TABLE IF EXISTS `t_user_advice_img_xq`;
CREATE TABLE `t_user_advice_img_xq`  (
  `id_xq` int(11) NOT NULL AUTO_INCREMENT,
  `advice_id_xq` int(11) NOT NULL COMMENT '意见id',
  `img_path_xq` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '图片路径',
  PRIMARY KEY (`id_xq`) USING BTREE,
  INDEX `advice_id_xq`(`advice_id_xq`) USING BTREE,
  CONSTRAINT `t_user_advice_img_xq_ibfk_1` FOREIGN KEY (`advice_id_xq`) REFERENCES `t_user_advice_xq` (`id_xq`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_user_advice_img_xq
-- ----------------------------

-- ----------------------------
-- Table structure for t_user_advice_xq
-- ----------------------------
DROP TABLE IF EXISTS `t_user_advice_xq`;
CREATE TABLE `t_user_advice_xq`  (
  `id_xq` int(11) NOT NULL AUTO_INCREMENT,
  `user_id_xq` int(11) NOT NULL COMMENT '用户id',
  `type_xq` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '意见种类 建议改进  反馈BUG ',
  `title_xq` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '描述标题',
  `content_xq` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '描述内容',
  `time_xq` datetime NOT NULL COMMENT '发起时间',
  PRIMARY KEY (`id_xq`) USING BTREE,
  INDEX `user_id`(`user_id_xq`) USING BTREE,
  CONSTRAINT `t_user_advice_xq_ibfk_1` FOREIGN KEY (`user_id_xq`) REFERENCES `t_users_xq` (`id_xq`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_user_advice_xq
-- ----------------------------

-- ----------------------------
-- Table structure for t_users_face_feature_xq
-- ----------------------------
DROP TABLE IF EXISTS `t_users_face_feature_xq`;
CREATE TABLE `t_users_face_feature_xq`  (
  `id_xq` int(11) NOT NULL AUTO_INCREMENT,
  `user_id_xq` int(11) NOT NULL,
  `face_feature_xq` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '面部特征信息',
  PRIMARY KEY (`id_xq`) USING BTREE,
  INDEX `user_id`(`user_id_xq`) USING BTREE,
  CONSTRAINT `t_users_face_feature_xq_ibfk_1` FOREIGN KEY (`user_id_xq`) REFERENCES `t_users_xq` (`id_xq`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_users_face_feature_xq
-- ----------------------------

-- ----------------------------
-- Table structure for t_users_friend_inform_xq
-- ----------------------------
DROP TABLE IF EXISTS `t_users_friend_inform_xq`;
CREATE TABLE `t_users_friend_inform_xq`  (
  `id_xq` int(11) NOT NULL AUTO_INCREMENT,
  `type_xq` tinyint(2) NOT NULL COMMENT '通知消息类型 0 好友申请 1 好友消息',
  `from_id_xq` int(11) NOT NULL COMMENT '发送消息的userid',
  `to_id_xq` int(11) NOT NULL COMMENT '接收消息的userid',
  `content_xq` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息内容',
  `state_xq` tinyint(2) NOT NULL COMMENT '0 未读 1 已读',
  `send_time_xq` datetime NOT NULL COMMENT '发送时间',
  PRIMARY KEY (`id_xq`) USING BTREE,
  INDEX `to_id`(`to_id_xq`) USING BTREE,
  CONSTRAINT `t_users_friend_inform_xq_ibfk_1` FOREIGN KEY (`to_id_xq`) REFERENCES `t_users_xq` (`id_xq`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 592 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_users_friend_inform_xq
-- ----------------------------

-- ----------------------------
-- Table structure for t_users_friend_xq
-- ----------------------------
DROP TABLE IF EXISTS `t_users_friend_xq`;
CREATE TABLE `t_users_friend_xq`  (
  `id_xq` int(11) NOT NULL AUTO_INCREMENT,
  `user_id1_xq` int(11) NULL DEFAULT NULL,
  `user_id2_xq` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id_xq`) USING BTREE,
  INDEX `t_users_friend_ibfk_1`(`user_id1_xq`) USING BTREE,
  CONSTRAINT `t_users_friend_xq_ibfk_1` FOREIGN KEY (`user_id1_xq`) REFERENCES `t_users_xq` (`id_xq`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 93 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_users_friend_xq
-- ----------------------------

-- ----------------------------
-- Table structure for t_users_role_xq
-- ----------------------------
DROP TABLE IF EXISTS `t_users_role_xq`;
CREATE TABLE `t_users_role_xq`  (
  `id_xq` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id_xq` int(11) NOT NULL,
  `role_id_xq` int(11) NOT NULL,
  PRIMARY KEY (`id_xq`) USING BTREE,
  INDEX `userId2`(`user_id_xq`) USING BTREE,
  INDEX `roleId2`(`role_id_xq`) USING BTREE,
  CONSTRAINT `roleId2` FOREIGN KEY (`role_id_xq`) REFERENCES `t_role_xq` (`id_xq`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `userId2` FOREIGN KEY (`user_id_xq`) REFERENCES `t_users_xq` (`id_xq`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 63 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_users_role_xq
-- ----------------------------
INSERT INTO `t_users_role_xq` VALUES (1, 1, 1);
INSERT INTO `t_users_role_xq` VALUES (2, 1, 2);
INSERT INTO `t_users_role_xq` VALUES (3, 1, 3);
INSERT INTO `t_users_role_xq` VALUES (4, 1, 4);
INSERT INTO `t_users_role_xq` VALUES (5, 1, 5);
INSERT INTO `t_users_role_xq` VALUES (6, 1, 6);
INSERT INTO `t_users_role_xq` VALUES (7, 1, 7);

-- ----------------------------
-- Table structure for t_users_xq
-- ----------------------------
DROP TABLE IF EXISTS `t_users_xq`;
CREATE TABLE `t_users_xq`  (
  `id_xq` int(11) NOT NULL AUTO_INCREMENT,
  `name_xq` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `username_xq` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password_xq` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `telephone_xq` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `email_xq` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱地址',
  `headImage_xq` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '头像路径',
  `faceImage_xq` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '面部照片路径',
  `isAdmin_xq` tinyint(1) NULL DEFAULT 0 COMMENT '是否拥有后台权限',
  PRIMARY KEY (`id_xq`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 53 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_users_xq
-- ----------------------------
INSERT INTO `t_users_xq` VALUES (1, 'root', 'root', '$2a$10$ipQv5C49Y6SETYdtOG01TunR4uyqNNMIVaiUjUgRbt921gJd5Ihwq', NULL, NULL, 'img\\head\\default.jpg', NULL, 1);

SET FOREIGN_KEY_CHECKS = 1;
