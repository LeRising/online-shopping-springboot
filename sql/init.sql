-- ============================================================
-- 网上商城购物系统 - 数据库初始化脚本（简化版）
-- ============================================================

CREATE DATABASE IF NOT EXISTS mall DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE mall;

-- -----------------------------------------------------------
-- 1. 用户表
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `order_item`;
DROP TABLE IF EXISTS `order_info`;
DROP TABLE IF EXISTS `cart`;
DROP TABLE IF EXISTS `product`;
DROP TABLE IF EXISTS `category`;
DROP TABLE IF EXISTS `banner`;
DROP TABLE IF EXISTS `user_address`;
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username`    VARCHAR(50)  NOT NULL COMMENT '用户名',
    `password`    VARCHAR(100) NOT NULL COMMENT '密码（BCrypt加密）',
    `nickname`    VARCHAR(50)  DEFAULT NULL COMMENT '昵称',
    `email`       VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `phone`       VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
    `avatar`      VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `role`        TINYINT      NOT NULL DEFAULT 0 COMMENT '角色：0=普通用户，1=管理员',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- -----------------------------------------------------------
-- 2. 收货地址表
-- -----------------------------------------------------------
CREATE TABLE `user_address` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '地址ID',
    `user_id`     BIGINT       NOT NULL COMMENT '用户ID',
    `name`        VARCHAR(50)  NOT NULL COMMENT '收货人姓名',
    `phone`       VARCHAR(20)  NOT NULL COMMENT '联系电话',
    `province`    VARCHAR(50)  NOT NULL COMMENT '省',
    `city`        VARCHAR(50)  NOT NULL COMMENT '市',
    `district`    VARCHAR(50)  NOT NULL COMMENT '区',
    `detail`      VARCHAR(255) NOT NULL COMMENT '详细地址',
    `is_default`  TINYINT      NOT NULL DEFAULT 0 COMMENT '是否默认：0=否，1=是',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收货地址表';

-- -----------------------------------------------------------
-- 3. 商品分类表
-- -----------------------------------------------------------
CREATE TABLE `category` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `name`        VARCHAR(50)  NOT NULL COMMENT '分类名称',
    `parent_id`   BIGINT       NOT NULL DEFAULT 0 COMMENT '父分类ID，0=顶级分类',
    `sort`        INT          NOT NULL DEFAULT 0 COMMENT '排序',
    `icon`        VARCHAR(255) DEFAULT NULL COMMENT '分类图标',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- -----------------------------------------------------------
-- 4. 商品表
-- -----------------------------------------------------------
CREATE TABLE `product` (
    `id`             BIGINT        NOT NULL AUTO_INCREMENT COMMENT '商品ID',
    `name`           VARCHAR(200)  NOT NULL COMMENT '商品名称',
    `description`    TEXT          DEFAULT NULL COMMENT '商品描述',
    `price`          DECIMAL(10,2) NOT NULL COMMENT '售价',
    `original_price` DECIMAL(10,2) DEFAULT NULL COMMENT '原价',
    `stock`          INT           NOT NULL DEFAULT 0 COMMENT '库存',
    `category_id`    BIGINT        NOT NULL COMMENT '分类ID',
    `image`          VARCHAR(500)  DEFAULT NULL COMMENT '主图URL',
    `images`         TEXT          DEFAULT NULL COMMENT '商品图片（JSON数组）',
    `status`         TINYINT       NOT NULL DEFAULT 1 COMMENT '状态：0=下架，1=上架',
    `sales`          INT           NOT NULL DEFAULT 0 COMMENT '销量',
    `create_time`    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- -----------------------------------------------------------
-- 5. 购物车表
-- -----------------------------------------------------------
CREATE TABLE `cart` (
    `id`          BIGINT   NOT NULL AUTO_INCREMENT COMMENT '购物车项ID',
    `user_id`     BIGINT   NOT NULL COMMENT '用户ID',
    `product_id`  BIGINT   NOT NULL COMMENT '商品ID',
    `quantity`    INT      NOT NULL DEFAULT 1 COMMENT '数量',
    `checked`     TINYINT  NOT NULL DEFAULT 1 COMMENT '是否选中：0=未选中，1=选中',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    UNIQUE KEY `uk_user_product` (`user_id`, `product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- -----------------------------------------------------------
-- 6. 订单表
-- -----------------------------------------------------------
CREATE TABLE `order_info` (
    `id`               BIGINT        NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no`         VARCHAR(50)   NOT NULL COMMENT '订单号',
    `user_id`          BIGINT        NOT NULL COMMENT '用户ID',
    `total_amount`     DECIMAL(10,2) NOT NULL COMMENT '总金额',
    `status`           TINYINT       NOT NULL DEFAULT 0 COMMENT '状态：0待付款/1已付款/2已发货/3已完成/4已取消/5已退货',
    `pay_method`       TINYINT       NOT NULL DEFAULT 0 COMMENT '支付方式：0微信/1支付宝/2云闪付',
    `address_snapshot` TEXT          DEFAULT NULL COMMENT '收货地址快照（JSON）',
    `pay_time`         DATETIME      DEFAULT NULL COMMENT '支付时间',
    `create_time`      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- -----------------------------------------------------------
-- 7. 订单明细表
-- -----------------------------------------------------------
CREATE TABLE `order_item` (
    `id`            BIGINT        NOT NULL AUTO_INCREMENT COMMENT '明细ID',
    `order_id`      BIGINT        NOT NULL COMMENT '订单ID',
    `product_id`    BIGINT        NOT NULL COMMENT '商品ID',
    `product_name`  VARCHAR(200)  NOT NULL COMMENT '商品名称快照',
    `product_image` VARCHAR(500)  DEFAULT NULL COMMENT '商品图片快照',
    `price`         DECIMAL(10,2) NOT NULL COMMENT '单价快照',
    `quantity`      INT           NOT NULL COMMENT '数量',
    `create_time`   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- -----------------------------------------------------------
-- 8. 轮播图表
-- -----------------------------------------------------------
CREATE TABLE `banner` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '轮播图ID',
    `image`       VARCHAR(500) NOT NULL COMMENT '图片URL',
    `url`         VARCHAR(500) DEFAULT NULL COMMENT '跳转链接',
    `sort`        INT          NOT NULL DEFAULT 0 COMMENT '排序',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='轮播图表';

-- -----------------------------------------------------------
-- 初始数据
-- -----------------------------------------------------------

-- 管理员账号（密码: admin123，BCrypt加密）
INSERT INTO `user` (`username`, `password`, `nickname`, `role`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '管理员', 1);

-- 普通用户（密码: user123）
INSERT INTO `user` (`username`, `password`, `nickname`, `role`) VALUES
('zhangsan', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '张三', 0);

-- 商品分类（只保留顶级分类）
INSERT INTO `category` (`id`, `name`, `parent_id`, `sort`) VALUES
(1, '手机数码', 0, 1),
(2, '电脑办公', 0, 2),
(3, '家用电器', 0, 3);

-- 商品
INSERT INTO `product` (`name`, `description`, `price`, `original_price`, `stock`, `category_id`, `image`, `status`, `sales`) VALUES
('iPhone 15 Pro', 'Apple iPhone 15 Pro 256GB', 8999.00, 9999.00, 100, 1, '/images/iphone15pro.png', 1, 520),
('华为 Mate 60', '华为 Mate 60 Pro 512GB', 6999.00, 7999.00, 80, 1, '/images/mate60pro.png', 1, 480),
('iPad Air', 'Apple iPad Air M2 256GB', 4799.00, 5499.00, 60, 1, '/images/ipad_air_m2.png', 1, 300),
('MacBook Pro 14', 'Apple MacBook Pro 14英寸 M3', 14999.00, 16999.00, 30, 2, '/images/macbook_pro14.png', 1, 200),
('小米14', '小米14 5G手机', 3999.00, 4299.00, 50, 3, '/images/xiaomi14.png', 1, 150),
('联想小新 Pro 16', '联想小新Pro16 R7-7840H', 5499.00, 6299.00, 45, 2, '/images/xiaoxin_pro16.png', 1, 180);

-- 轮播图
INSERT INTO `banner` (`image`, `url`, `sort`) VALUES
('/images/banner_1.png', '/product/1', 1),
('/images/banner_2.png', '/product/2', 2),
('/images/banner_3.png', '/product/3', 3);
