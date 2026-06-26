-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: mall
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `mall`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `mall` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `mall`;

--
-- Table structure for table `banner`
--

DROP TABLE IF EXISTS `banner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `banner` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '轮播图ID',
  `content` varchar(500) NOT NULL DEFAULT '' COMMENT '公告内容',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='轮播图表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `banner`
--

LOCK TABLES `banner` WRITE;
/*!40000 ALTER TABLE `banner` DISABLE KEYS */;
INSERT INTO `banner` VALUES (12,'全场手机数码类商品限时 8 折优惠，快来抢购！',1,'2026-06-26 14:41:10','2026-06-26 14:46:50'),(13,'新用户注册即送 50 元优惠券，数量有限先到先得！',2,'2026-06-26 14:41:10','2026-06-26 14:46:55'),(14,'全场满 299 元包邮，支持全国配送',3,'2026-06-26 14:41:10','2026-06-26 14:46:59');
/*!40000 ALTER TABLE `banner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '购物车项ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `quantity` int NOT NULL DEFAULT '1' COMMENT '数量',
  `checked` tinyint NOT NULL DEFAULT '1' COMMENT '是否选中：0=未选中，1=选中',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_product` (`user_id`,`product_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='购物车表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
INSERT INTO `cart` VALUES (18,12,9,1,1,'2026-06-24 11:53:00','2026-06-24 11:53:00');
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '父分类ID，0=顶级分类',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  `icon` varchar(255) DEFAULT NULL COMMENT '分类图标',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品分类表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'手机数码',0,1,NULL,'2026-06-22 19:48:13','2026-06-22 19:48:13'),(2,'电脑办公',0,2,NULL,'2026-06-22 19:48:13','2026-06-22 19:48:13'),(3,'家用电器',0,3,NULL,'2026-06-22 19:48:13','2026-06-22 19:48:13');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_info`
--

DROP TABLE IF EXISTS `order_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(50) NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `total_amount` decimal(10,2) NOT NULL COMMENT '总金额',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0待付款/1已付款/2已发货/3已完成/4已取消/5已退货',
  `address_snapshot` text COMMENT '收货地址快照（JSON）',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_info`
--

LOCK TABLES `order_info` WRITE;
/*!40000 ALTER TABLE `order_info` DISABLE KEYS */;
INSERT INTO `order_info` VALUES (24,'2070401344372072448',10,4799.00,5,'{id=1, createTime=2026-06-22T22:37:09, updateTime=2026-06-22T22:37:09, userId=10, name=张书包, phone=1225255544, province=陕西, city=咸阳, district=秦都区, detail=咸阳师范学院秦都校区, isDefault=1}','2026-06-26 14:58:50','2026-06-26 14:58:41','2026-06-26 14:58:41');
/*!40000 ALTER TABLE `order_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_item`
--

DROP TABLE IF EXISTS `order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(200) NOT NULL COMMENT '商品名称快照',
  `product_image` varchar(500) DEFAULT NULL COMMENT '商品图片快照',
  `price` decimal(10,2) NOT NULL COMMENT '单价快照',
  `quantity` int NOT NULL COMMENT '数量',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_item`
--

LOCK TABLES `order_item` WRITE;
/*!40000 ALTER TABLE `order_item` DISABLE KEYS */;
INSERT INTO `order_item` VALUES (26,22,6,'联想小新 Pro 16','/images/upload/2888c4265a664956af67e1f33d1f8430.png',5499.00,1,'2026-06-26 14:22:21','2026-06-26 14:22:21'),(27,23,3,'iPad Air','/images/upload/6d7eec6e1f4346139be802bbd24d4a04.png',4799.00,1,'2026-06-26 14:57:08','2026-06-26 14:57:08'),(28,24,3,'iPad Air','/images/upload/6d7eec6e1f4346139be802bbd24d4a04.png',4799.00,1,'2026-06-26 14:58:41','2026-06-26 14:58:41');
/*!40000 ALTER TABLE `order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `name` varchar(200) NOT NULL COMMENT '商品名称',
  `description` text COMMENT '商品描述',
  `price` decimal(10,2) NOT NULL COMMENT '售价',
  `original_price` decimal(10,2) DEFAULT NULL COMMENT '原价',
  `stock` int NOT NULL DEFAULT '0' COMMENT '库存',
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `image` varchar(500) DEFAULT NULL COMMENT '主图URL',
  `images` text COMMENT '商品图片（JSON数组）',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0=下架，1=上架',
  `sales` int NOT NULL DEFAULT '0' COMMENT '销量',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'iPhone 15 Pro','Apple iPhone 15 Pro 256GB',8999.00,9999.00,98,1,'/images/upload/1e76c25435ff46e38d50bf0ac69ef249.png',NULL,1,522,'2026-06-22 19:48:13','2026-06-26 14:08:31'),(2,'华为 Mate 60','华为 Mate 60 512GB',6999.00,7999.00,76,1,'/images/upload/f10bb5ff128a4b63a27160a886d78d48.png',NULL,1,484,'2026-06-22 19:48:13','2026-06-26 14:00:25'),(3,'iPad Air','Apple iPad Air 256GB',4799.00,5499.00,54,1,'/images/upload/6d7eec6e1f4346139be802bbd24d4a04.png',NULL,1,306,'2026-06-22 19:48:13','2026-06-26 14:58:55'),(4,'MacBook Pro 14','Apple MacBook Pro 14英寸 M3',14999.00,16999.00,26,2,'/images/upload/e84d0d92e10a456dacd01aeab2ba221f.png',NULL,1,204,'2026-06-22 19:48:13','2026-06-26 14:08:59'),(6,'联想小新 Pro 16','联想小新Pro16 R7-7840H',5499.00,6299.00,44,2,'/images/upload/2888c4265a664956af67e1f33d1f8430.png',NULL,1,181,'2026-06-22 19:48:13','2026-06-26 14:22:21'),(7,'Acer9neo','好电脑测试',5999.00,6999.00,100,2,'/images/upload/e298a4538cc449a180bfecd596a21b86.png',NULL,1,0,'2026-06-22 22:14:26','2026-06-26 14:11:54'),(8,'ASUS 16','华硕16英寸轻薄办公笔记本',8999.00,10009.00,99,2,'/images/upload/7930bcf5b52146a9b9c3704855f1d323.png',NULL,1,1,'2026-06-22 22:19:57','2026-06-26 14:09:34'),(9,'Colorful 16 Pro','七彩虹16英寸高性能办公本',5999.00,6999.00,45,2,'/images/upload/6c9d54704b024c64a99021cd0efaa93d.png',NULL,1,5,'2026-06-22 22:24:02','2026-06-26 14:09:26'),(10,'Colorful P15','七彩虹P15办公笔记本',4999.00,5999.00,45,2,'/images/upload/ea068baf75fe4a27bc25f34461555ea7.png',NULL,1,0,'2026-06-22 22:24:02','2026-06-26 14:11:59'),(11,'Dell Pro','戴尔专业办公笔记本电脑',6999.00,7999.00,40,2,'/images/upload/00b1afdb199546a9b36ceb03bf808f03.png',NULL,1,0,'2026-06-22 22:24:02','2026-06-26 14:12:07'),(12,'Honor MagicBook Pro 16','荣耀MagicBook Pro 16英寸轻薄办公本',5499.00,6499.00,55,2,'/images/upload/d8145539a4c94b29b0859b1a22bd4205.png',NULL,1,0,'2026-06-22 22:24:02','2026-06-26 14:12:14'),(13,'Honor X16','荣耀X16办公笔记本',4499.00,5299.00,60,2,'/images/upload/1a18d2b22e554fab8697ff0dedba0ec9.png',NULL,1,0,'2026-06-22 22:24:02','2026-06-26 14:12:19'),(14,'Huawei MateBook 14','华为MateBook 14英寸轻薄办公本',6499.00,7499.00,35,2,'/images/upload/3f688e6f53334c4b9fd1b14e8247e641.png',NULL,1,0,'2026-06-22 22:24:02','2026-06-26 14:12:25'),(15,'Lenovo 7000','联想7000系列办公笔记本',5299.00,6299.00,48,2,'/images/upload/42149db216c143258864892f6f7a17ac.png',NULL,1,0,'2026-06-22 22:24:02','2026-06-26 14:12:34'),(16,'Lenovo G5000','联想G5000高效办公笔记本',5799.00,6799.00,42,2,'/images/upload/5a604b6064ad4a9c9a1efb1bba43756e.png',NULL,1,0,'2026-06-22 22:24:02','2026-06-26 14:12:39'),(17,'Lenovo Y9000P','联想Y9000P高性能办公笔记本',8999.00,9999.00,30,2,'/images/upload/26ef9714e6e54854a007dafa0940db40.png',NULL,1,0,'2026-06-22 22:24:02','2026-06-26 14:12:44'),(18,'MacBook Pro','苹果MacBook Pro专业办公笔记本',14999.00,16999.00,25,2,'/images/upload/741bbc27af7747039c04ddd11d21be76.png',NULL,1,0,'2026-06-22 22:24:02','2026-06-26 14:11:49'),(19,'MECHREVO 15 Pro','机械革命15英寸高性能办公本',5599.00,6599.00,38,2,'/images/upload/802cae77551e40678d2f95c2b1c5e180.png',NULL,1,0,'2026-06-22 22:24:02','2026-06-26 14:13:18'),(20,'MSI 16 Max','微星16英寸Max办公笔记本',7999.00,8999.00,28,2,'/images/upload/bb863e486a8f44818048d96a74ab26c8.png',NULL,1,0,'2026-06-22 22:24:02','2026-06-26 14:15:01'),(21,'RedmiBook Pro','红米Book Pro轻薄办公笔记本',4999.00,5999.00,52,2,'/images/upload/4ca33fc6836f497aa500e6a4d2650c6c.png',NULL,1,0,'2026-06-22 22:24:02','2026-06-26 14:13:23'),(22,'ROG 9 Plus','华硕ROG 9 Plus高性能办公笔记本',9999.00,11999.00,22,2,'/images/upload/25071e4725fe4d18b2c433da36150c1c.png',NULL,1,0,'2026-06-22 22:24:02','2026-06-26 14:13:33'),(23,'ROG 16 Air','华硕ROG 16 Air轻薄高性能办公本',8499.00,9999.00,30,2,'/images/upload/04e46333f31e467fa26715c04862623d.png',NULL,1,0,'2026-06-22 22:24:02','2026-06-26 14:14:05'),(24,'ThinkBook 14+','联想ThinkBook 14+商务办公笔记本',5999.00,6999.00,45,2,'/images/upload/dc8e1be0515d41bf8968ba3595fea33b.png',NULL,1,0,'2026-06-22 22:24:02','2026-06-26 14:14:14'),(25,'ThinkPad X1','联想ThinkPad X1超轻薄商务办公笔记本',12999.00,14999.00,20,2,'/images/upload/97d41b2a3f594102969eb837b79d6149.png',NULL,1,0,'2026-06-22 22:24:02','2026-06-26 14:14:19'),(26,'Honor 600 Pro','荣耀600 Pro智能手机',3999.00,4499.00,80,1,'/images/upload/0c66a316a41d4f52b9880f9b85ca71c8.png',NULL,1,0,'2026-06-22 22:31:59','2026-06-26 14:14:26'),(27,'Honor Magic7 Pro','荣耀Magic7 Pro旗舰智能手机',5999.00,6999.00,60,1,'/images/upload/69abe2a6578a4fcbab9b695951e01774.png',NULL,1,0,'2026-06-22 22:31:59','2026-06-26 14:13:05'),(29,'Honor Win','荣耀Win高性价比智能手机',1999.00,2499.00,120,1,'/images/upload/ef69963f198d4e9f84635c91fdeacb03.png',NULL,1,0,'2026-06-22 22:31:59','2026-06-26 14:10:54'),(30,'Huawei Mate 70','华为Mate 70旗舰智能手机',6999.00,7999.00,50,1,'/images/upload/1850d2f2fb7f456cbc682c7b54c0d953.png',NULL,1,0,'2026-06-22 22:31:59','2026-06-26 14:10:48'),(31,'Huawei Pura X Max','华为Pura X Max影像旗舰手机',8999.00,9999.00,34,1,'/images/upload/430c1bb4d3054c30bb1594b0932d1460.png',NULL,1,1,'2026-06-22 22:31:59','2026-06-26 14:09:47'),(32,'iPhone 16E','苹果iPhone 1E智能手机',5999.00,6299.00,70,1,'/images/upload/8a5486148a5a4fa9bd035fe86e9d432f.png',NULL,1,0,'2026-06-22 22:31:59','2026-06-26 14:10:40'),(33,'iQOO 15','iQOO 15高性能智能手机',4299.00,4999.00,65,1,'/images/upload/549598b7ef8b4c90be18ce4b3db1769d.png',NULL,1,0,'2026-06-22 22:31:59','2026-06-26 14:10:35'),(34,'iQOO Neo 11','iQOO Neo 11游戏智能手机',2999.00,3499.00,85,1,'/images/upload/0a114a7090e94801aef770ae51c80b1c.png',NULL,1,0,'2026-06-22 22:31:59','2026-06-26 14:09:54'),(35,'OnePlus Ace 5','一加Ace 5性能智能手机',3299.00,3999.00,75,1,'/images/upload/46fe3adf645f40ecb6c3f48514ffb406.png',NULL,1,0,'2026-06-22 22:31:59','2026-06-26 14:10:28'),(36,'OPPO Find X8S','OPPO Find X8S影像旗舰手机',5499.00,6299.00,55,1,'/images/upload/d3bcaab131064306a452a7eaf7d7b20b.png',NULL,1,0,'2026-06-22 22:31:59','2026-06-26 14:10:21'),(40,'Samsung W26','三星W26商务折叠智能手机',12999.00,14999.00,25,1,'/images/upload/2c940ffc0c644310a83e870c4fe11d41.png',NULL,1,0,'2026-06-22 22:31:59','2026-06-26 14:11:09'),(41,'vivo S50 Pro Mini','vivo S50 Pro Mini轻薄智能手机',2799.00,3299.00,95,1,'/images/upload/2f58359bde60452393db72c5519ecaa7.png',NULL,1,0,'2026-06-22 22:31:59','2026-06-26 14:11:19'),(42,'vivo X300','vivo X300影像智能手机',4299.00,4999.00,60,1,'/images/upload/525a05f24ac94f42a3b020bfeb8a2977.png',NULL,1,0,'2026-06-22 22:31:59','2026-06-26 14:11:25'),(43,'vivo Y600 Pro','vivo Y600 Pro长续航智能手机',1599.00,1999.00,130,1,'/images/upload/f72c74a4d31d41ed80fe04f790e6c6b2.png',NULL,1,0,'2026-06-22 22:31:59','2026-06-26 14:11:29'),(44,'Xiaomi 17','小米17旗舰智能手机',4999.00,5499.00,70,1,'/images/upload/40968c6704a04888b6ecd18f74803ad9.png',NULL,1,0,'2026-06-22 22:31:59','2026-06-26 14:11:33'),(45,'Xiaomi 17 Pro','小米17 Pro高端智能手机',5999.00,6499.00,50,1,'/images/upload/33b9f06c0cec402b893fdc45b4496a8f.png',NULL,1,0,'2026-06-22 22:31:59','2026-06-26 14:11:38');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码（BCrypt加密）',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `role` tinyint NOT NULL DEFAULT '0' COMMENT '角色：0=普通用户，1=管理员',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (8,'risinglee','$2a$10$M1OGgPHIOZbFHIj9LXlys.SsDZrkKszMYiUwEyPELcldClaKwVT0u','李睿轩','lir428840@gmail.com','12256355855',NULL,1,'2026-06-22 21:45:53','2026-06-22 21:46:14'),(10,'zzz','$2a$10$SKMxIZSa0rtg0fcfe968Ieu1ymmfgud0cB4ty3NlzrmzWUd3KDPEa','张树宝','454544554566@qq.com','1225211144',NULL,0,'2026-06-22 22:35:57','2026-06-22 22:35:57'),(11,'admin','$2a$10$Qt9flq0VA4ctdMmMx7jkkOgxoOxlg2ubXtszySTcA0alKFyxkps3C','管理员1',NULL,NULL,NULL,1,'2026-06-24 11:30:18','2026-06-24 11:30:56'),(12,'zhangsan','$2a$10$XfH7/Qg2sh14ocVIVLZtfOlY8Y78o.cF.tpn/gQwm97L6ZOWrj0oi','张三',NULL,NULL,NULL,0,'2026-06-24 11:31:30','2026-06-24 11:31:30'),(13,'lisi','$2a$10$hbKMnzCcyHk5ZWVRUsXRJeNKGhUO5d0wn5nyBcrT7yXYWiX1vMGIC','李四',NULL,NULL,NULL,0,'2026-06-26 09:45:58','2026-06-26 09:45:58');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_address`
--

DROP TABLE IF EXISTS `user_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_address` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '地址ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `name` varchar(50) NOT NULL COMMENT '收货人姓名',
  `phone` varchar(20) NOT NULL COMMENT '联系电话',
  `province` varchar(50) NOT NULL COMMENT '省',
  `city` varchar(50) NOT NULL COMMENT '市',
  `district` varchar(50) NOT NULL COMMENT '区',
  `detail` varchar(255) NOT NULL COMMENT '详细地址',
  `is_default` tinyint NOT NULL DEFAULT '0' COMMENT '是否默认：0=否，1=是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='收货地址表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_address`
--

LOCK TABLES `user_address` WRITE;
/*!40000 ALTER TABLE `user_address` DISABLE KEYS */;
INSERT INTO `user_address` VALUES (1,10,'张书包','1225255544','陕西','咸阳','秦都区','咸阳师范学院秦都校区',1,'2026-06-22 22:37:09','2026-06-22 22:37:09'),(2,8,'李睿轩','12236555445','四川省','广汉市','xxx区','xxxx地址',1,'2026-06-23 14:48:06','2026-06-23 14:48:06');
/*!40000 ALTER TABLE `user_address` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-26 14:59:31
