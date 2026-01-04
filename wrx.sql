/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80020 (8.0.20)
 Source Host           : localhost:3306
 Source Schema         : wrx

 Target Server Type    : MySQL
 Target Server Version : 80020 (8.0.20)
 File Encoding         : 65001

 Date: 04/01/2026 16:47:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for check_in
-- ----------------------------
DROP TABLE IF EXISTS `check_in`;
CREATE TABLE `check_in`  (
  `check_in_id` int NOT NULL AUTO_INCREMENT COMMENT '入住记录ID',
  `customer_id` int NOT NULL COMMENT '关联客户ID',
  `room_id` int NOT NULL COMMENT '关联客房ID',
  `actual_check_in` datetime NOT NULL COMMENT '实际入住时间',
  `actual_check_out` datetime NULL DEFAULT NULL COMMENT '退房时间',
  PRIMARY KEY (`check_in_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '入住登记表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of check_in
-- ----------------------------
INSERT INTO `check_in` VALUES (1, 1, 3, '2025-11-23 21:34:31', '2025-11-05 11:58:50');
INSERT INTO `check_in` VALUES (2, 2, 5, '2025-11-02 23:15:00', '2025-11-02 23:47:31');
INSERT INTO `check_in` VALUES (4, 2, 5, '2025-11-03 11:47:03', '2025-11-05 11:58:56');
INSERT INTO `check_in` VALUES (5, 2, 2, '2025-11-03 16:41:21', '2025-11-03 16:41:51');
INSERT INTO `check_in` VALUES (6, 2, 2, '2025-11-03 16:44:23', '2025-11-03 16:44:37');
INSERT INTO `check_in` VALUES (7, 2, 2, '2025-11-05 11:58:04', '2025-11-05 16:08:44');
INSERT INTO `check_in` VALUES (8, 2, 3, '2025-11-05 16:08:19', '2025-11-25 09:24:13');
INSERT INTO `check_in` VALUES (9, 2, 2, '2025-11-24 22:13:48', NULL);
INSERT INTO `check_in` VALUES (11, 2, 9, '2025-11-25 10:02:45', '2025-11-25 10:12:19');

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `customer_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '客户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of customer
-- ----------------------------
INSERT INTO `customer` VALUES (1, '刘开明', 'lkm', '$2a$10$WO/lBpPk4by7udalLMZ3w.IR8VHFGqcChoo/Nb6qwntRVUpasXRzu', '12345678910');
INSERT INTO `customer` VALUES (2, '王五', 'wangwu', '$2a$10$YQzoYziE13dJm4XOsIhmC.CAc0Osp2R0iphV2Wag1vCYiiuzHsnvi', '13654545439');
INSERT INTO `customer` VALUES (16, '李四', 'lisi', '$2a$10$TuY5ffhxjmxHF5bqDhczIuA2FGu/emXWE2.quOLWMuHT.HuwUhcOu', '13245678766');
INSERT INTO `customer` VALUES (17, '黄果树', 'hgs', '$2a$10$BBjVZO2VeIDia6Vpf8GNauyYHU05pScS5DA/b.L514b6tsIbHy8xG', '14739287463');

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `employee_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '员工表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of employee
-- ----------------------------
INSERT INTO `employee` VALUES (1, '张三', '202201530329', '$2a$10$Z7w.cTR5R7WBvea49KgYt.jp.pjRhzGJP2aSabw/ugs8XkBYhQ/7K', '16673237083');
INSERT INTO `employee` VALUES (2, '李四', '202201530330', '$2a$10$./6gJw3TI5zz6FDIvFvy0.31tFW0HULb4Yt8CsHEbUKEDnoJT/1MG', '15526367822');

-- ----------------------------
-- Table structure for employee_role
-- ----------------------------
DROP TABLE IF EXISTS `employee_role`;
CREATE TABLE `employee_role`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `employee_id` int NOT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`id` DESC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '员工-角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of employee_role
-- ----------------------------
INSERT INTO `employee_role` VALUES (17, 1, 1);
INSERT INTO `employee_role` VALUES (2, 2, 2);

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `order_id` int NOT NULL AUTO_INCREMENT COMMENT '订单唯一ID',
  `customer_id` int NOT NULL COMMENT '关联客户ID',
  `room_id` int NOT NULL COMMENT '关联客房ID',
  `check_in_date` datetime NOT NULL COMMENT '预计入住日期',
  `check_out_date` datetime NOT NULL COMMENT '预计退房日期',
  `amount` decimal(10, 2) NOT NULL COMMENT '订单总金额',
  `create_time` datetime NOT NULL COMMENT '下单时间',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (20, 2, 1, '2025-11-25 00:00:00', '2025-11-26 00:00:00', 100.00, '2025-11-25 09:25:04');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `role_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '经理', 'manager');
INSERT INTO `role` VALUES (2, '前台', 'reception');
INSERT INTO `role` VALUES (3, '客房管理员', 'roomattendant');

-- ----------------------------
-- Table structure for room
-- ----------------------------
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room`  (
  `room_id` int NOT NULL AUTO_INCREMENT COMMENT '客房唯一ID',
  `room_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '客房编号（如301）',
  `room_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '客房类型（单人间/双人间）',
  `price` decimal(10, 2) NOT NULL COMMENT '单价（元/天）',
  `status` int NOT NULL COMMENT '状态：1=空闲，2=已预订，3=已入住，4=待清洁',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '客房描述',
  PRIMARY KEY (`room_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '客房表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of room
-- ----------------------------
INSERT INTO `room` VALUES (1, '201', '单人间', 100.00, 2, '大床2.0m, 免费WiFi, 浴缸, 空调, 迷你吧');
INSERT INTO `room` VALUES (2, '202', '单人间', 100.00, 3, '大床2.0m, 免费WiFi, 浴缸, 空调, 迷你吧');
INSERT INTO `room` VALUES (3, '203', '双人间', 200.00, 1, '大床2.0m, 免费WiFi, 浴缸, 空调, 迷你吧');
INSERT INTO `room` VALUES (4, '204', '大床房', 300.00, 1, '大床2.0m, 免费WiFi, 浴缸, 空调, 迷你吧');
INSERT INTO `room` VALUES (5, '205', '豪华间', 400.00, 1, '大床2.0m, 免费WiFi, 浴缸, 空调, 迷你吧');
INSERT INTO `room` VALUES (7, '206', '大床房', 150.00, 1, '大床2.0m, 免费WiFi, 浴缸, 空调, 迷你吧');
INSERT INTO `room` VALUES (8, '207', '豪华间', 300.00, 1, '大床2.0m, 免费WiFi, 浴缸, 空调, 迷你吧');
INSERT INTO `room` VALUES (9, '209', '单人间', 100.00, 4, '大床2.0m, 免费WiFi, 浴缸, 空调, 迷你吧');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `account` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', 'admin', '$2a$10$hHISp/jV5OxYwFL27N3k3.rGt1GSqfxjWaoPoNl5/tc6HN4cktX9G', 'admin');

SET FOREIGN_KEY_CHECKS = 1;
