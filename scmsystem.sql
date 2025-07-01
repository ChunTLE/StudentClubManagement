/*
 Navicat Premium Data Transfer

 Source Server         : mysql8
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : localhost:3306
 Source Schema         : scmsystem

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : 65001

 Date: 01/07/2025 08:23:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for activity
-- ----------------------------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `club_id` bigint(20) NOT NULL,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `start_time` datetime NULL DEFAULT NULL,
  `end_time` datetime NULL DEFAULT NULL,
  `created_by` bigint(20) NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `club_id`(`club_id` ASC) USING BTREE,
  INDEX `created_by`(`created_by` ASC) USING BTREE,
  CONSTRAINT `activity_ibfk_1` FOREIGN KEY (`club_id`) REFERENCES `club` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `activity_ibfk_2` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of activity
-- ----------------------------
INSERT INTO `activity` VALUES (1, 1, '户外摄影采风', '前往西山拍摄日出风景', '西山公园', '2025-07-10 06:00:00', '2025-07-10 10:00:00', 2, '2025-06-30 15:19:11');
INSERT INTO `activity` VALUES (2, 2, '友谊赛', '和隔壁学校进行篮球友谊赛', '体育馆', '2025-07-15 15:00:00', '2025-07-15 17:00:00', 3, '2025-06-30 15:19:11');
INSERT INTO `activity` VALUES (3, 3, 'Python入门讲座', '讲解Python基础语法及应用', '教学楼A301', '2025-07-20 14:00:00', '2025-07-20 16:00:00', 1, '2025-06-30 15:19:11');

-- ----------------------------
-- Table structure for activity_enrollment
-- ----------------------------
DROP TABLE IF EXISTS `activity_enrollment`;
CREATE TABLE `activity_enrollment`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `activity_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `enrolled_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `activity_id`(`activity_id` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `activity_enrollment_ibfk_1` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `activity_enrollment_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of activity_enrollment
-- ----------------------------
INSERT INTO `activity_enrollment` VALUES (1, 1, 1, '2025-06-30 15:19:11');
INSERT INTO `activity_enrollment` VALUES (2, 1, 4, '2025-06-30 15:19:11');
INSERT INTO `activity_enrollment` VALUES (3, 2, 2, '2025-06-30 15:19:11');
INSERT INTO `activity_enrollment` VALUES (4, 3, 3, '2025-06-30 15:19:11');
INSERT INTO `activity_enrollment` VALUES (5, 3, 4, '2025-06-30 15:19:11');

-- ----------------------------
-- Table structure for club
-- ----------------------------
DROP TABLE IF EXISTS `club`;
CREATE TABLE `club`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `leader_id` bigint(20) NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name` ASC) USING BTREE,
  INDEX `leader_id`(`leader_id` ASC) USING BTREE,
  CONSTRAINT `club_ibfk_1` FOREIGN KEY (`leader_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of club
-- ----------------------------
INSERT INTO `club` VALUES (1, '摄影社', '热爱摄影，记录美好瞬间。', 2, '2025-06-30 15:19:11');
INSERT INTO `club` VALUES (2, '篮球队', '校内最强篮球团队。', 3, '2025-06-30 15:19:11');
INSERT INTO `club` VALUES (3, '编程俱乐部', '专注于Web与移动开发。', 1, '2025-06-30 15:19:11');

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `club_id` bigint(20) NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `club_id`(`club_id` ASC) USING BTREE,
  CONSTRAINT `department_ibfk_1` FOREIGN KEY (`club_id`) REFERENCES `club` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO `department` VALUES (1, 1, '外拍部', '组织外出拍摄活动', '2025-06-30 15:19:11');
INSERT INTO `department` VALUES (2, 1, '后期部', '负责照片处理与修图', '2025-06-30 15:19:11');
INSERT INTO `department` VALUES (3, 2, '训练组', '日常训练与体能管理', '2025-06-30 15:19:11');
INSERT INTO `department` VALUES (4, 3, '前端组', '负责网页开发', '2025-06-30 15:19:11');
INSERT INTO `department` VALUES (5, 3, '后端组', '负责服务器逻辑开发', '2025-06-30 15:19:11');

-- ----------------------------
-- Table structure for finance_record
-- ----------------------------
DROP TABLE IF EXISTS `finance_record`;
CREATE TABLE `finance_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `club_id` bigint(20) NOT NULL,
  `type` enum('INCOME','EXPENSE') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `amount` decimal(10, 2) NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `record_time` datetime NOT NULL,
  `recorded_by` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `club_id`(`club_id` ASC) USING BTREE,
  INDEX `recorded_by`(`recorded_by` ASC) USING BTREE,
  CONSTRAINT `finance_record_ibfk_1` FOREIGN KEY (`club_id`) REFERENCES `club` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `finance_record_ibfk_2` FOREIGN KEY (`recorded_by`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of finance_record
-- ----------------------------
INSERT INTO `finance_record` VALUES (1, 1, 'INCOME', 2000.00, '社团赞助费', '2025-06-25 10:00:00', 2);
INSERT INTO `finance_record` VALUES (2, 1, 'EXPENSE', 800.00, '购买相机配件', '2025-06-26 14:00:00', 2);
INSERT INTO `finance_record` VALUES (3, 2, 'INCOME', 1500.00, '比赛奖金', '2025-06-28 11:00:00', 3);
INSERT INTO `finance_record` VALUES (4, 3, 'EXPENSE', 300.00, '打印宣传单', '2025-06-29 09:00:00', 1);

-- ----------------------------
-- Table structure for mail_log
-- ----------------------------
DROP TABLE IF EXISTS `mail_log`;
CREATE TABLE `mail_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `to_email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `subject` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `status` tinyint(4) NULL DEFAULT 0 COMMENT '0失败 1成功',
  `sent_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mail_log
-- ----------------------------
INSERT INTO `mail_log` VALUES (1, 'zhangsan@example.com', '欢迎加入摄影社', '欢迎参加本周六的摄影活动...', 1, '2025-06-30 15:19:11');
INSERT INTO `mail_log` VALUES (2, 'lisi@example.com', '报名成功通知', '您已成功报名篮球队友谊赛，请准时到场...', 1, '2025-06-30 15:19:11');
INSERT INTO `mail_log` VALUES (3, 'wangwu@example.com', '系统通知', '您的账号已被禁用，请联系管理员...', 0, '2025-06-30 15:19:11');

-- ----------------------------
-- Table structure for membership
-- ----------------------------
DROP TABLE IF EXISTS `membership`;
CREATE TABLE `membership`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `club_id` bigint(20) NOT NULL,
  `department_id` bigint(20) NULL DEFAULT NULL,
  `position` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '成员',
  `status` enum('PENDING','APPROVED','REJECTED') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'PENDING',
  `joined_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `club_id`(`club_id` ASC) USING BTREE,
  INDEX `department_id`(`department_id` ASC) USING BTREE,
  CONSTRAINT `membership_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `membership_ibfk_2` FOREIGN KEY (`club_id`) REFERENCES `club` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `membership_ibfk_3` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of membership
-- ----------------------------
INSERT INTO `membership` VALUES (1, 1, 1, 1, '成员', 'APPROVED', '2025-06-01 08:00:00');
INSERT INTO `membership` VALUES (2, 2, 2, 3, '队长', 'APPROVED', '2025-05-20 09:00:00');
INSERT INTO `membership` VALUES (3, 3, 3, 5, '负责人', 'APPROVED', '2025-06-10 10:00:00');
INSERT INTO `membership` VALUES (4, 4, 3, 4, '实习生', 'PENDING', NULL);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `role` enum('ADMIN','LEADER','MEMBER') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '0禁用 1正常',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'zhangsan', '$2a$10$3chH7UynV8p6FJ72XSy0hOFayASz2ermFDeFDfBrFt0nOQkylWplC', 'zhangsan@example.com', '张三', 'MEMBER', 1, '2025-06-30 15:19:11');
INSERT INTO `user` VALUES (2, 'lisi', '$2a$10$ddzMnQCjScvgdyqEL1YfbuL9SWDplHeAR570NxbKFNjS50e93rGWm', 'lisi@example.com', '李四', 'LEADER', 1, '2025-06-30 15:19:11');
INSERT INTO `user` VALUES (3, 'wangwu', '$2a$10$ddzMnQCjScvgdyqEL1YfbuL9SWDplHeAR570NxbKFNjS50e93rGWm', 'wangwu@example.com', '王五', 'ADMIN', 1, '2025-06-30 15:19:11');
INSERT INTO `user` VALUES (4, 'chenliu', '$2a$10$ddzMnQCjScvgdyqEL1YfbuL9SWDplHeAR570NxbKFNjS50e93rGWm', 'chenliu@example.com', '陈六', 'MEMBER', 0, '2025-06-30 15:19:11');

SET FOREIGN_KEY_CHECKS = 1;
