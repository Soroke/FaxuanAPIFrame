/*
Navicat MySQL Data Transfer

Source Server         : 虚拟机
Source Server Version : 50636
Source Host           : 192.168.213.130:3306
Source Database       : api

Target Server Type    : MYSQL
Target Server Version : 50636
File Encoding         : 65001

Date: 2017-06-21 10:28:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `request_manager`
-- ----------------------------
DROP TABLE IF EXISTS `request_manager`;
CREATE TABLE `request_manager` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `request_type` varchar(10) NOT NULL,
  `request_url` varchar(1000) DEFAULT NULL,
  `response_body` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=295 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of request_manager
-- ----------------------------
