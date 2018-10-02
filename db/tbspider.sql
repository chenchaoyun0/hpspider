/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1-root
Source Server Version : 50723
Source Host           : 127.0.0.1:3306
Source Database       : tbspider

Target Server Type    : MYSQL
Target Server Version : 50723
File Encoding         : 65001

Date: 2018-10-01 00:51:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(100) DEFAULT NULL COMMENT '用户名',
  `content` longtext COMMENT '回帖内容',
  `post_url` varchar(200) DEFAULT NULL COMMENT '主题帖',
  `user_device` varchar(100) DEFAULT NULL COMMENT '用户设备',
  `time` datetime DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`id`),
  KEY `user_name_idx` (`user_name`),
  KEY `title_idx` (`post_url`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='回帖表';

-- ----------------------------
-- Table structure for post
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(100) DEFAULT NULL COMMENT '用户名',
  `title` varchar(200) DEFAULT NULL COMMENT '标题',
  `post_url` varchar(200) NOT NULL COMMENT '帖子url',
  `content` longtext COMMENT '主题内容',
  `reply_num` int(10) DEFAULT NULL COMMENT '回复数',
  `type` int(1) DEFAULT '1' COMMENT '1-普通帖,2-精华帖',
  `time` datetime DEFAULT NULL COMMENT '发帖时间',
  `year` int(4) DEFAULT NULL,
  `month` int(2) DEFAULT NULL,
  `day` int(2) DEFAULT NULL,
  `hour` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_name_idx` (`user_name`),
  KEY `title_idx` (`post_url`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='帖子表';

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(100) DEFAULT NULL COMMENT '贴吧名',
  `gender` int(1) DEFAULT NULL COMMENT '0-女,1-男',
  `tb_age` double(10,2) DEFAULT '0.00' COMMENT '吧龄',
  `post_count` int(10) DEFAULT '0' COMMENT '发帖量',
  `follow_count` int(10) DEFAULT NULL COMMENT '关注数',
  `fans_count` int(10) DEFAULT NULL COMMENT '粉丝数',
  `user_head_url` varchar(200) DEFAULT NULL COMMENT '用户头像地址',
  `user_home_url` varchar(200) DEFAULT NULL COMMENT '用户贴吧主页地址',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_idx` (`user_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- Table structure for user_tbs
-- ----------------------------
DROP TABLE IF EXISTS `user_tbs`;
CREATE TABLE `user_tbs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(100) DEFAULT NULL,
  `tb_name` varchar(100) DEFAULT NULL COMMENT '贴吧名称',
  `tb_level` int(10) DEFAULT NULL COMMENT '贴吧等级',
  PRIMARY KEY (`id`),
  KEY `user_name_idx` (`user_name`) USING BTREE,
  KEY `tb_name_idx` (`tb_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='用户关注贴吧表';

-- ----------------------------
-- Table structure for word_divide
-- ----------------------------
DROP TABLE IF EXISTS `word_divide`;
CREATE TABLE `word_divide` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `word` varchar(100) DEFAULT NULL,
  `type` int(1) DEFAULT NULL COMMENT '1-用户,2-帖子标题,3-回帖内容',
  PRIMARY KEY (`id`),
  KEY `index1` (`type`,`word`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='分词表';
