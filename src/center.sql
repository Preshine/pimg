/*
Navicat MySQL Data Transfer

Source Server         : lh
Source Server Version : 50561
Source Host           : localhost:3306
Source Database       : center

Target Server Type    : MYSQL
Target Server Version : 50561
File Encoding         : 65001

Date: 2018-11-16 19:03:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for resources
-- ----------------------------
DROP TABLE IF EXISTS `resources`;
CREATE TABLE `resources` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `res_code` varchar(255) DEFAULT NULL,
  `res_type` varchar(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `parent_id` int(11) NOT NULL,
  `var1` varchar(255) DEFAULT NULL,
  `var2` varchar(255) DEFAULT NULL,
  `var3` varchar(255) DEFAULT NULL,
  `is_deleted` int(11) DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of resources
-- ----------------------------
INSERT INTO `resources` VALUES ('1', '1', 'element', '国富', 'gofuit', '0', 'ha', 'ha', 'hh', '0', null, null, null);
INSERT INTO `resources` VALUES ('2', null, 'menu', '基础对象', null, '0', null, null, null, '0', '2018-11-14 23:34:30', null, null);
INSERT INTO `resources` VALUES ('3', null, 'menu', '客户', null, '2', null, null, null, '0', null, null, null);
INSERT INTO `resources` VALUES ('6', null, 'menu', '仓库', null, '2', null, null, null, '0', null, null, null);
INSERT INTO `resources` VALUES ('7', null, 'menu', '交易类型', null, '1', null, null, null, '0', null, null, null);
INSERT INTO `resources` VALUES ('8', null, 'menu', '销售订单', null, '7', null, null, null, '0', null, null, null);
INSERT INTO `resources` VALUES ('9', null, 'menu', '物料', null, '2', null, null, null, '0', null, null, null);
INSERT INTO `resources` VALUES ('10', null, 'menu', '供应商', null, '2', null, null, null, '0', null, null, null);
INSERT INTO `resources` VALUES ('11', null, 'menu', '费用', null, '2', null, null, null, '0', null, null, null);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', 'admin', '系统管理员', '1', '2018-11-15 11:03:06');
INSERT INTO `role` VALUES ('2', 'Preshine', '平台工作人员', '1', '2018-11-15 11:10:10');
INSERT INTO `role` VALUES ('3', 'test', 'just for test ...', '0', '2018-11-15 11:29:15');
INSERT INTO `role` VALUES ('5', 'duoduoer', '哈哈哈哈啊哈哈', '1', '2018-11-15 18:36:54');

-- ----------------------------
-- Table structure for role_res
-- ----------------------------
DROP TABLE IF EXISTS `role_res`;
CREATE TABLE `role_res` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL,
  `res_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_res
-- ----------------------------
INSERT INTO `role_res` VALUES ('1', '1', '2');
INSERT INTO `role_res` VALUES ('2', '1', '3');
INSERT INTO `role_res` VALUES ('3', '1', '9');
INSERT INTO `role_res` VALUES ('4', '1', '2');
INSERT INTO `role_res` VALUES ('5', '1', '3');
INSERT INTO `role_res` VALUES ('6', '1', '9');
INSERT INTO `role_res` VALUES ('7', '1', '8');
INSERT INTO `role_res` VALUES ('8', '1', '1');
INSERT INTO `role_res` VALUES ('9', '5', '3');
INSERT INTO `role_res` VALUES ('10', '5', '6');
INSERT INTO `role_res` VALUES ('11', '5', '9');
INSERT INTO `role_res` VALUES ('12', '5', '10');
INSERT INTO `role_res` VALUES ('13', '2', '2');
INSERT INTO `role_res` VALUES ('14', '2', '2');
INSERT INTO `role_res` VALUES ('15', '2', '1');
INSERT INTO `role_res` VALUES ('16', '1', '2');
INSERT INTO `role_res` VALUES ('17', '1', '3');
INSERT INTO `role_res` VALUES ('18', '1', '9');
INSERT INTO `role_res` VALUES ('19', '1', '2');
INSERT INTO `role_res` VALUES ('20', '1', '3');
INSERT INTO `role_res` VALUES ('21', '1', '9');
INSERT INTO `role_res` VALUES ('22', '1', '8');
INSERT INTO `role_res` VALUES ('23', '1', '1');
INSERT INTO `role_res` VALUES ('24', '1', '11');
INSERT INTO `role_res` VALUES ('26', '5', '3');
INSERT INTO `role_res` VALUES ('27', '5', '6');
INSERT INTO `role_res` VALUES ('28', '5', '9');
INSERT INTO `role_res` VALUES ('29', '5', '10');
INSERT INTO `role_res` VALUES ('30', '5', '7');
INSERT INTO `role_res` VALUES ('31', '5', '8');
INSERT INTO `role_res` VALUES ('32', '3', '1');
INSERT INTO `role_res` VALUES ('33', '3', '8');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_account` int(11) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `real_name` varchar(255) DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `sex` int(255) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `is_delete` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '1', 'admin', null, '11', '450082119@qq.com', null, '2', null, null, null, '0', null, null);
INSERT INTO `user` VALUES ('2', '2', 'preshine', null, '666', '666@qq.com', null, '2', null, null, null, '0', null, null);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_account` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('6', '2', '5');
INSERT INTO `user_role` VALUES ('7', '1', '2');
INSERT INTO `user_role` VALUES ('8', '1', '1');
INSERT INTO `user_role` VALUES ('9', '1', '3');
