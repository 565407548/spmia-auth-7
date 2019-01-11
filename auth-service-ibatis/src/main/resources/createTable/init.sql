-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: 127.0.0.1    Database: interest
-- ------------------------------------------------------
-- Server version	5.6.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `oauth_client_details`
--

DROP TABLE IF EXISTS `oauth_client_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oauth_client_details` (
  `client_id` varchar(255) NOT NULL,
  `resource_ids` varchar(255) DEFAULT NULL,
  `client_secret` varchar(255) DEFAULT NULL,
  `scope` varchar(255) DEFAULT NULL,
  `authorized_grant_types` varchar(255) DEFAULT NULL,
  `web_server_redirect_uri` varchar(255) DEFAULT NULL,
  `authorities` varchar(256) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` varchar(255) DEFAULT NULL,
  `autoapprove` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oauth_client_details`
--

LOCK TABLES `oauth_client_details` WRITE;
/*!40000 ALTER TABLE `oauth_client_details` DISABLE KEYS */;
INSERT INTO `oauth_client_details` VALUES ('client',NULL,'{noop}secret','all','password,authorization_code,refresh_token,implicit,client_credentials',NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `oauth_client_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `r_user_role`
--

DROP TABLE IF EXISTS `oauth_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oauth_user_role` (
  `user_id` int(10) NOT NULL COMMENT '用户ID',
  `role_id` int(10) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户-角色关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `r_user_role`
--

LOCK TABLES `oauth_user_role` WRITE;
/*!40000 ALTER TABLE `oauth_user_role` DISABLE KEYS */;
INSERT INTO `oauth_user_role` VALUES (1,1),(13,1);
/*!40000 ALTER TABLE `oauth_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oauth_role`
--

DROP TABLE IF EXISTS `oauth_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oauth_role` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role` varchar(50) NOT NULL COMMENT '角色',
  `name` varchar(50) DEFAULT NULL COMMENT '角色名',
  `modules` text COMMENT '权限',
  `describe` text COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oauth_role`
--

LOCK TABLES `oauth_role` WRITE;
/*!40000 ALTER TABLE `oauth_role` DISABLE KEYS */;
INSERT INTO `oauth_role` VALUES (1,'ROLE_ADMIN','超级管理员','4;5;6;7;8;9;10;11;14;1;12;13;','超级管理员，拥有全部权限。');
INSERT INTO `oauth_role` VALUES (2,'ROLE_USER','普通用户','4;5;6;7;8;9;10;11;14;1;12;13;','普通用户，拥有部分权限。');
/*!40000 ALTER TABLE `oauth_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oauth_user`
--

DROP TABLE IF EXISTS `oauth_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oauth_user` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `login_name` varchar(50) DEFAULT NULL COMMENT '登录名',
  `password` varchar(225) DEFAULT NULL COMMENT '密码',
  `email` varchar(225) DEFAULT NULL COMMENT '邮箱',
  `headimg` varchar(225) DEFAULT NULL COMMENT '头像url',
  `url` varchar(225) DEFAULT NULL COMMENT 'GitHub主页',
  `create_time` varchar(225) DEFAULT NULL COMMENT '注册时间',
  `githubid` varchar(225) DEFAULT NULL COMMENT 'github的id',
  `qqid` varchar(225) DEFAULT NULL COMMENT 'qq的openid',
  PRIMARY KEY (`id`),
  UNIQUE KEY `githubid_UNIQUE` (`githubid`),
  UNIQUE KEY `login_name_UNIQUE` (`login_name`),
  UNIQUE KEY `qqid_UNIQUE` (`qqid`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oauth_user`
--

LOCK TABLES `oauth_user` WRITE;
/*!40000 ALTER TABLE `oauth_user` DISABLE KEYS */;
INSERT INTO `oauth_user` VALUES
                                (1,'管理员','admin','{bcrypt}$2a$10$D8E4cuanLviCCe/ASqBC7OZ84JYOH8IT4/y4JLAV/Pm/AdhzPcy2.','123456@qq.com','https://avatars2.githubusercontent.com/u/30545965?v=4','http://www.lovemtt.com','1531704654140',NULL,NULL),
                                (12,'smallsnail-wh',NULL,NULL,'null','https://avatars2.githubusercontent.com/u/30545965?v=4','https://github.com/smallsnail-wh','1542463692126','smallsnail-wh',NULL);
# INSERT INTO `oauth_user` VALUES (2,'jim','jim','{bcrypt}$2a$10$D8E4cuanLviCCe/ASqBC7OZ84JYOH8IT4/y4JLAV/Pm/AdhzPcy2.','123456@qq.com','https://avatars2.githubusercontent.com/u/30545965?v=4','http://www.lovemtt.com','1531704654140',NULL,NULL),(12,'smallsnail-wh',NULL,NULL,'null',0,'https://avatars2.githubusercontent.com/u/30545965?v=4','https://github.com/smallsnail-wh','1542463692126','smallsnail-wh',NULL),(13,'树根',NULL,NULL,NULL,0,'http://thirdqq.qlogo.cn/qqapp/101512648/1C47A2C639D3A89E573AC2BF46FBEF63/40',NULL,'1542463259014',NULL,'1C47A2C639D3A89E573AC2BF46FBEF63');

/*!40000 ALTER TABLE `oauth_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oauth_user_github`
--

DROP TABLE IF EXISTS `oauth_user_github`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oauth_user_github` (
  `login` varchar(225) NOT NULL COMMENT 'github用户名',
  `avatar_url` varchar(225) DEFAULT NULL COMMENT '头像url',
  `html_url` varchar(225) DEFAULT NULL COMMENT 'github主页',
  `email` varchar(225) DEFAULT NULL COMMENT '邮箱',
  `userid` int(10) DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='github用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oauth_user_github`
--

LOCK TABLES `oauth_user_github` WRITE;
/*!40000 ALTER TABLE `oauth_user_github` DISABLE KEYS */;
INSERT INTO `oauth_user_github` VALUES ('smallsnail-wh','https://avatars2.githubusercontent.com/u/30545965?v=4','https://github.com/smallsnail-wh','null',12);
/*!40000 ALTER TABLE `oauth_user_github` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_qq`
--

DROP TABLE IF EXISTS `oauth_user_qq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oauth_user_qq` (
  `openid` varchar(225) NOT NULL COMMENT 'openid',
  `nickname` varchar(225) DEFAULT NULL COMMENT '用户名',
  `figureurl_qq_1` varchar(225) DEFAULT NULL COMMENT '头像url',
  `gender` varchar(225) DEFAULT NULL COMMENT '性别',
  `userid` int(10) DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='qq用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oauth_user_qq`
--

LOCK TABLES `oauth_user_qq` WRITE;
/*!40000 ALTER TABLE `oauth_user_qq` DISABLE KEYS */;
INSERT INTO `oauth_user_qq` VALUES ('1C47A2C639D3A89E573AC2BF46FBEF63','树根','http://thirdqq.qlogo.cn/qqapp/101512648/1C47A2C639D3A89E573AC2BF46FBEF63/40','男',13);
/*!40000 ALTER TABLE `oauth_user_qq` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-11-21 16:50:43
