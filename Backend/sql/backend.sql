/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80405 (8.4.5)
 Source Host           : localhost:3306
 Source Schema         : backend

 Target Server Type    : MySQL
 Target Server Version : 80405 (8.4.5)
 File Encoding         : 65001

 Date: 25/02/2026 21:23:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for community
-- ----------------------------
DROP TABLE IF EXISTS `community`;
CREATE TABLE `community`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `community_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '小区名称',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '小区详细地址',
  `contact_person` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '小区联系人',
  `contact_phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话（手机号）',
  `is_enabled` tinyint NOT NULL DEFAULT 1 COMMENT '是否启用（1=启用，0=禁用）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_community_name`(`community_name` ASC) USING BTREE COMMENT '小区名称唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '小区表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of community
-- ----------------------------
INSERT INTO `community` VALUES (6, '光明小区', '广东省广州市花都区光明小区', '张三', '17875132132', 1, '2025-12-04 23:34:34', '2025-12-26 01:01:11');
INSERT INTO `community` VALUES (7, '碧桂园', '广东省广州市白云区碧桂园', '李四', '17898889101', 1, '2025-12-04 23:35:16', '2025-12-04 23:35:16');

-- ----------------------------
-- Table structure for community_product_type
-- ----------------------------
DROP TABLE IF EXISTS `community_product_type`;
CREATE TABLE `community_product_type`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `community_id` bigint NOT NULL COMMENT '关联小区id',
  `product_type_id` bigint NOT NULL COMMENT '关联商品类型id',
  `is_enabled` tinyint NOT NULL DEFAULT 1 COMMENT '该小区是否启用此服务模块（1=启用，0=禁用）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序序号（控制模块展示顺序）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_community_type`(`community_id` ASC, `product_type_id` ASC) USING BTREE COMMENT '同一小区对同一类型仅能关联一次',
  INDEX `idx_community_id`(`community_id` ASC) USING BTREE COMMENT '小区id索引',
  INDEX `idx_product_type_id`(`product_type_id` ASC) USING BTREE COMMENT '商品类型id索引',
  CONSTRAINT `fk_community` FOREIGN KEY (`community_id`) REFERENCES `community` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_community_type` FOREIGN KEY (`product_type_id`) REFERENCES `product_type` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '小区-商品类型关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of community_product_type
-- ----------------------------
INSERT INTO `community_product_type` VALUES (9, 7, 11, 1, '2025-12-04 23:57:13', '2025-12-04 23:57:13', 0);
INSERT INTO `community_product_type` VALUES (11, 7, 9, 1, '2025-12-04 23:57:20', '2025-12-04 23:57:20', 0);
INSERT INTO `community_product_type` VALUES (12, 7, 8, 1, '2025-12-04 23:57:27', '2025-12-04 23:57:27', 0);
INSERT INTO `community_product_type` VALUES (13, 7, 7, 1, '2025-12-04 23:57:31', '2025-12-04 23:57:31', 0);
INSERT INTO `community_product_type` VALUES (14, 7, 6, 1, '2025-12-04 23:57:34', '2025-12-04 23:57:34', 0);
INSERT INTO `community_product_type` VALUES (16, 7, 4, 1, '2025-12-04 23:57:42', '2025-12-04 23:57:42', 0);
INSERT INTO `community_product_type` VALUES (17, 7, 3, 1, '2025-12-04 23:57:46', '2025-12-04 23:57:46', 0);
INSERT INTO `community_product_type` VALUES (18, 7, 12, 1, '2025-12-08 23:49:40', '2025-12-08 23:49:40', 0);

-- ----------------------------
-- Table structure for convenience_product
-- ----------------------------
DROP TABLE IF EXISTS `convenience_product`;
CREATE TABLE `convenience_product`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `price` bigint NOT NULL COMMENT '商品价格（分）',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品图片URL',
  `product_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '超市商品类型',
  `is_enabled` tinyint NOT NULL DEFAULT 1 COMMENT '是否启用（1=启用，0=禁用）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_product_name`(`product_name` ASC) USING BTREE COMMENT '商品名称唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '便民超市商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of convenience_product
-- ----------------------------
INSERT INTO `convenience_product` VALUES (3, '打火机', 100, '/profile/upload/2025/12/05/服务图标_20251205233231A005.png', '日常用品', 1, '2025-12-05 23:32:36', '2025-12-05 23:32:36');
INSERT INTO `convenience_product` VALUES (4, '可口可乐', 300, '/profile/upload/2025/12/05/服务图标_20251205233249A006.png', '饮料', 1, '2025-12-05 23:32:53', '2025-12-05 23:32:53');
INSERT INTO `convenience_product` VALUES (5, '芬达汽水', 300, '/profile/upload/2025/12/05/服务图标_20251205233305A007.png', '饮料', 1, '2025-12-05 23:33:10', '2025-12-05 23:33:10');

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
  `table_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `tpl_web_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '前端模板类型（element-ui模版 element-plus模版）',
  `package_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '代码生成业务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gen_table
-- ----------------------------

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column`  (
  `column_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` bigint NULL DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '字典类型',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '代码生成业务表字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------

-- ----------------------------
-- Table structure for miniapp_config
-- ----------------------------
DROP TABLE IF EXISTS `miniapp_config`;
CREATE TABLE `miniapp_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `withdraw_rate` int NOT NULL COMMENT '提现比率，百分比0-100',
  `carousel_images` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '首页轮播图JSON数组',
  `privacy_policy` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '隐私政策内容',
  `is_enabled` tinyint NULL DEFAULT 1 COMMENT '是否启用（1启用 0禁用）',
  `test_mode` tinyint NULL DEFAULT 0 COMMENT '测试模式（1开启 0关闭）：开启后接单不作限制，可以接自己的订单',
  `dispatch_enabled` tinyint NOT NULL DEFAULT 0 COMMENT '智能派单开关 1开 0关',
  `dispatch_order_types` varchar(50) NOT NULL DEFAULT '1' COMMENT '启用派单的订单类型，逗号分隔（默认仅外卖=1）',
  `dispatch_push_count` int NOT NULL DEFAULT 3 COMMENT '每轮推送人数',
  `dispatch_timeout_sec` int NOT NULL DEFAULT 30 COMMENT '每轮等待秒数',
  `dispatch_max_round` int NOT NULL DEFAULT 3 COMMENT '最大派单轮次',
  `dispatch_weight_json` varchar(500) NULL COMMENT '派单权重与阈值JSON',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `notice_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '公告通知内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '小程序配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of miniapp_config
-- ----------------------------
INSERT INTO `miniapp_config` (`id`, `withdraw_rate`, `carousel_images`, `privacy_policy`, `is_enabled`, `test_mode`, `dispatch_enabled`, `dispatch_order_types`, `dispatch_push_count`, `dispatch_timeout_sec`, `dispatch_max_round`, `dispatch_weight_json`, `create_time`, `update_time`, `notice_content`) VALUES (1, 5, '[{\"imageUrl\":\"/profile/upload/2025/12/08/123_20251208225136A001.png\",\"sortOrder\":1},{\"imageUrl\":\"/profile/upload/2025/12/08/设计跑腿轮播图_20251208225142A002.png\",\"sortOrder\":2},{\"imageUrl\":\"/profile/upload/2025/12/08/设计跑腿轮播图1_20251208225152A003.png\",\"sortOrder\":3}]', '<h1><strong>小谦快快送小程序法律条款及隐私政策</strong></h1><h1>**</h1><h1>生效时间：____年____月____日</h1><h1>更新时间：年____月____日</h1><h1>运营方：（公司 / 个人名称，以下简称 “我们”）</h1><h1>一、法律条款（用之前先看明白）</h1><h1>1. 谁能用，怎么用？</h1><h1>您得是能对自己行为负责的成年人；如果是小朋友，必须让家长同意并帮忙操作。</h1><h1>用小程序时，您填的信息（比如手机号、地址）得是真的，不能用别人的信息冒充。</h1><h1>我们可能会调整服务（比如新增 “代买奶茶”、暂时停 “代扔大件垃圾”），大的调整会提前 7 天在小程序弹通知，您继续用就代表认可调整。</h1><h1>2. 您的权利和要注意的事</h1><h1>您能做的：下单、接单、查订单、算佣金，也能改自己的信息、注销账号。</h1><h1>您要注意的：不能用小程序干违法的事（比如让接单员送危险品、骗接单员钱）；自己的账号要管好，丢了被盗了赶紧联系客服冻结，不然出问题得自己担责。</h1><h1>3. 费用和佣金怎么算？</h1><h1>订单多少钱、接单能赚多少佣金，小程序里会写清楚，您下单 / 接单就代表认这个价格。</h1><h1>付钱支持微信付或线下付，付完钱订单才生效。</h1><h1>接单员的佣金，会在订单完成后 7 个工作日内结算，但得先完成实名认证；如果您违规（比如接了单不送、骗佣金），我们有权扣掉对应的钱。</h1><h1>4. 哪些情况我们不担责？</h1><h1>比如地震、网络断了、微信系统维护这些没法控制的事，导致服务用不了，我们不赔钱，但会尽快恢复并告诉大家。</h1><h1>如果您和接单员有矛盾（比如东西送坏了、送晚了），我们会帮忙协调，但不直接负责；送贵重东西（比如手机、首饰），建议您自己买保险，丢了坏了我们不赔。</h1><h1>5. 有纠纷怎么解决？</h1><h1>先好好商量，商量不好的话，得去我们公司 / 个人所在地的法院解决问题。</h1><h1>二、隐私政策（您的信息我们怎么管）</h1><h1>1. 先说明白</h1><h1>这份政策讲的是我们怎么收、用、存您的信息，还有怎么保护这些信息，适用于您用小程序的所有服务（下单、接单等）。</h1><h1>您同意这份政策，才能用核心功能（比如下单、接单）；要是不同意，只能看看小程序的基本内容，用不了这些功能。</h1><h1>2. 我们会收您哪些信息，用来做什么？</h1><h1>（1）必须要收的信息（不然没法服务）</h1><h1>手机号：从微信授权获取，用来确认您的身份、发订单通知（比如 “有人接您的单了”“东西送到了”）、有急事联系您、给接单员结佣金。只要您还用服务，我们就存着；您注销账号，72 小时内会彻底删掉。</h1><h1>地址：您自己填的小区、门牌号，比如 “XX 小区 3 号楼 2 单元 101”，用来送东西（代拿快递、扔垃圾、送物品）。订单完成后，会存 3 个月（怕有纠纷要查），之后会把地址里的关键信息（比如门牌号）模糊处理。</h1><h1>微信号：只有您用 “代打印” 服务时才收，用来传要打印的文件；服务做完，您可以自己删掉这个号。</h1><h1>（2）自动收的信息（不涉及隐私，只为服务稳定）</h1><h1>您的手机型号、微信版本：用来适配不同手机，保证小程序能正常用。</h1><h1>订单操作记录、IP 地址：比如您什么时候下的单、从哪个地方用的小程序，用来查问题（比如订单没显示）、处理纠纷，存 6 个月后会把能识别您的信息去掉。</h1><h1>3. 您的信息我们怎么存、怎么保护？</h1><h1>所有信息都存在国内的阿里云服务器，不会传到国外。</h1><h1>手机号、地址这些敏感信息，会用加密方式存（就像给信息加了锁），钥匙会定期换，更安全。</h1><h1>只有必要的人（比如客服、技术维护）能看您的信息，而且看的时候都会留记录；要是信息不小心泄露了，我们会马上停止收集，72 小时内告诉受影响的用户。</h1><h1>4. 您的信息会给别人吗？</h1><h1>我们绝不会把您的信息卖掉、租给别人。</h1><h1>只有两种情况会用到您的信息：一是微信支付要知道您的订单金额、付没付钱（不会给敏感信息）；二是警察、监管部门依法要查，我们会给，但会把无关信息模糊处理。</h1><h1>5. 您能管自己的信息吗？</h1><h1>查 / 改信息：进 “我的 - 设置 - 隐私中心”，能看、能改手机号、地址、微信号。</h1><h1>删信息 / 撤回授权：在隐私中心能删掉常用地址、微信号；也能在微信的权限设置里，不让我们用您的手机号。</h1><h1>注销账号：进 “我的 - 设置 - 账号管理” 能注销，注销后所有信息会永久删掉，订单、佣金记录也找不回来了。</h1><h1>6. 小朋友用小程序怎么管？</h1><h1>小朋友用，必须让家长同意并帮忙操作；家长可以联系我们，查、改小朋友填的信息。</h1><h1>要是发现小朋友填错了信息（比如填了家长的手机号），家长能提供身份证明，让我们删掉这些信息。</h1><h1>7. 有问题怎么找我们？</h1><h1>想了解信息保护的事：发邮件到 privacy@xiaoqian-express.com</h1><h1>有客服问题：打____（客服电话），工作日 9:00-18:00 有人接</h1><h1>上门找：去____（联系地址）</h1><h1><br></h1><p class=\"ql-indent-8\"><br></p>', 1, 0, 0, '1', 3, 30, 3, NULL, '2025-12-08 00:50:53', '2025-12-15 01:37:41', '今日不营业，望周知');

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` bigint NOT NULL COMMENT '关联订单主表ID（order_main.id）',
  `item_type` tinyint NOT NULL COMMENT '订单项类型：1=外卖商品，2=超市商品，3=服务套餐',
  `package_id` bigint NULL DEFAULT NULL COMMENT '服务套餐ID（关联service_package.id）',
  `product_id` bigint NULL DEFAULT NULL COMMENT '商品ID（关联product.id）',
  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品/套餐名称快照',
  `quantity` int NOT NULL COMMENT '数量',
  `unit_price` bigint NOT NULL COMMENT '单价（分）',
  `subtotal` bigint NOT NULL COMMENT '小计金额（分）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `store_id` bigint NULL DEFAULT NULL COMMENT '店铺ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE COMMENT '订单ID索引',
  INDEX `idx_package_id`(`package_id` ASC) USING BTREE COMMENT '套餐ID索引',
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE COMMENT '商品ID索引',
  CONSTRAINT `fk_order_item_main` FOREIGN KEY (`order_id`) REFERENCES `order_main` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_order_item_package` FOREIGN KEY (`package_id`) REFERENCES `service_package` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '订单项明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_item
-- ----------------------------
INSERT INTO `order_item` VALUES (22, 44, 2, NULL, 5, '芬达汽水', 3, 300, 900, '2026-01-20 00:55:26', NULL);
INSERT INTO `order_item` VALUES (23, 45, 2, NULL, 4, '可口可乐', 1, 300, 300, '2026-01-20 01:06:29', NULL);
INSERT INTO `order_item` VALUES (24, 45, 2, NULL, 5, '芬达汽水', 1, 300, 300, '2026-01-20 01:06:29', NULL);
INSERT INTO `order_item` VALUES (25, 46, 2, NULL, 5, '芬达汽水', 3, 300, 900, '2026-01-20 01:14:46', NULL);
INSERT INTO `order_item` VALUES (26, 47, 2, NULL, 5, '芬达汽水', 2, 300, 600, '2026-01-20 01:23:16', NULL);
INSERT INTO `order_item` VALUES (27, 48, 2, NULL, 5, '芬达汽水', 5, 300, 1500, '2026-01-20 01:29:27', NULL);
INSERT INTO `order_item` VALUES (28, 49, 2, NULL, 5, '芬达汽水', 5, 300, 1500, '2026-01-20 01:33:28', NULL);
INSERT INTO `order_item` VALUES (29, 51, 1, NULL, 5, '云吞', 2, 1500, 3000, '2026-01-20 01:43:57', 3);
INSERT INTO `order_item` VALUES (30, 52, 2, NULL, 4, '可口可乐', 1, 300, 300, '2026-01-20 02:29:11', NULL);
INSERT INTO `order_item` VALUES (31, 52, 2, NULL, 5, '芬达汽水', 4, 300, 1200, '2026-01-20 02:29:11', NULL);
INSERT INTO `order_item` VALUES (32, 54, 2, NULL, 4, '可口可乐', 1, 300, 300, '2026-01-20 23:43:38', NULL);
INSERT INTO `order_item` VALUES (33, 54, 2, NULL, 5, '芬达汽水', 1, 300, 300, '2026-01-20 23:43:38', NULL);
INSERT INTO `order_item` VALUES (34, 55, 2, NULL, 5, '芬达汽水', 1, 300, 300, '2026-01-21 00:00:42', NULL);
INSERT INTO `order_item` VALUES (35, 56, 2, NULL, 5, '芬达汽水', 1, 300, 300, '2026-01-21 00:04:58', NULL);
INSERT INTO `order_item` VALUES (36, 57, 2, NULL, 5, '芬达汽水', 1, 300, 300, '2026-01-21 00:18:40', NULL);
INSERT INTO `order_item` VALUES (37, 58, 2, NULL, 5, '芬达汽水', 1, 300, 300, '2026-01-21 00:30:31', NULL);
INSERT INTO `order_item` VALUES (38, 67, 1, NULL, 5, '云吞', 1, 1500, 1500, '2026-01-21 00:49:46', 3);

-- ----------------------------
-- Table structure for order_main
-- ----------------------------
DROP TABLE IF EXISTS `order_main`;
CREATE TABLE `order_main`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单编号（唯一）',
  `user_id` bigint NOT NULL COMMENT '下单用户ID（关联wechat_user.id）',
  `runner_id` bigint NULL DEFAULT NULL COMMENT '接单员ID（关联wechat_user.id）',
  `community_id` bigint NOT NULL COMMENT '订单所在小区ID（关联community.id）',
  `detail_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '详细配送地址',
  `order_type` tinyint NOT NULL COMMENT '订单类型：1=外卖订单，2=便民超市订单，3=购买服务套餐订单，4=其他服务订单（不需要关联订单子项表）',
  `service_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '其他服务订单的具体服务类型(代拿快递/代扔垃圾/代买菜等)',
  `start_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '起始地(代拿快递/代送物品)',
  `end_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '目的地(代拿快递/代送物品)',
  `delivery_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '配送地址(代买菜/代买物品/代打印)',
  `item_desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '物品/需求描述(代买菜/万能服务等)',
  `budget` bigint NULL DEFAULT NULL COMMENT '预算金额(分)(代买菜/万能服务)',
  `spec_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '服务规格名称',
  `wechat_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '微信号(代打印)',
  `duration_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '时长类型(小时/天)(物品寄存)',
  `duration_value` int NULL DEFAULT NULL COMMENT '时长数值(物品寄存)',
  `total_amount` bigint NOT NULL COMMENT '订单总金额（分）',
  `discount_amount` bigint NOT NULL DEFAULT 0 COMMENT '优惠金额（分）',
  `actual_amount` bigint NOT NULL COMMENT '实付金额（分）',
  `commission_amount` bigint NOT NULL DEFAULT 0 COMMENT '订单佣金（分）',
  `order_status` tinyint NOT NULL DEFAULT 0 COMMENT '订单状态：0=待支付，1=待接单，2=已接单，3=已完成，4=已取消',
  `pay_status` tinyint NOT NULL DEFAULT 0 COMMENT '支付状态：0=未支付，1=已支付，2=退款中，3=已退款',
  `pay_method` tinyint NULL DEFAULT NULL COMMENT '支付方式：1=微信支付，2=线下支付',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `finish_time` datetime NULL DEFAULT NULL COMMENT '完成时间',
  `cancel_reason` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '取消原因',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_order_no`(`order_no` ASC) USING BTREE COMMENT '订单编号唯一',
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE COMMENT '下单用户索引',
  INDEX `idx_runner_id`(`runner_id` ASC) USING BTREE COMMENT '接单员索引',
  INDEX `idx_order_status`(`order_status` ASC) USING BTREE COMMENT '订单状态索引',
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE COMMENT '创建时间索引',
  INDEX `idx_service_type`(`service_type` ASC) USING BTREE COMMENT '按服务类型查询索引',
  INDEX `fk_order_community`(`community_id` ASC) USING BTREE,
  CONSTRAINT `fk_order_community` FOREIGN KEY (`community_id`) REFERENCES `community` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_order_runner` FOREIGN KEY (`runner_id`) REFERENCES `wechat_user` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT,
  CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `wechat_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 68 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '订单主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_main
-- ----------------------------
INSERT INTO `order_main` VALUES (40, 'OT-20251226012339194', 6, NULL, 7, '无', 4, '物品代送', '401', '601', NULL, NULL, NULL, '3km内', NULL, NULL, NULL, 400, 0, 400, 400, 1, 1, 1, '2025-12-26 01:23:56', NULL, '确定要申请退款吗？', '配送规格：3km内\n需要在2025-12-26 00:00-2025-12-26 01:00内为我配送\n备注：3kg', '2025-12-26 01:23:39', '2026-01-21 00:37:52');
INSERT INTO `order_main` VALUES (41, 'OT-20251226014133560', 6, NULL, 7, '901', 4, '快递代拿', NULL, NULL, NULL, NULL, NULL, '大件', NULL, NULL, NULL, 400, 0, 400, 400, 1, 1, 1, '2025-12-26 01:42:10', NULL, '确定要申请退款吗？', '1.规格：大件，取件码：A1001\n需要在2025-12-26 00:00-2025-12-26 02:00内配送过来\n备注：无', '2025-12-26 01:41:33', '2026-01-21 00:37:52');
INSERT INTO `order_main` VALUES (42, 'OT-20251226014430667', 6, NULL, 7, '无', 4, '物品代送', '901', '801', NULL, NULL, NULL, '3km内', NULL, NULL, NULL, 400, 0, 400, 400, 1, 1, 1, '2025-12-26 01:45:08', NULL, '确定要申请退款吗？', '配送规格：3km内\n需要在2025-12-26 00:00-2025-12-26 01:00内为我配送\n备注：123', '2025-12-26 01:44:30', '2026-01-21 00:37:52');
INSERT INTO `order_main` VALUES (43, 'OT-20251226021425532', 6, 6, 7, '无', 4, '物品代送', '1213', '123', NULL, NULL, NULL, '3km内', NULL, NULL, NULL, 400, 0, 400, 400, 2, 1, 1, '2025-12-26 02:14:40', NULL, '确定要申请退款吗？', '配送规格：3km内\n需要在2025-12-26 00:00-2025-12-26 01:00内为我配送\n备注：123', '2025-12-26 02:14:25', '2026-01-21 00:37:52');
INSERT INTO `order_main` VALUES (44, 'SM-20260120005526898', 6, NULL, 7, '301单元', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 900, 0, 900, 900, 1, 1, 1, NULL, NULL, NULL, '', '2026-01-20 00:55:26', '2026-01-21 00:37:52');
INSERT INTO `order_main` VALUES (45, 'SM-20260120010629222', 6, NULL, 7, '301单元', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 600, 0, 600, 600, 1, 1, 1, NULL, '2026-01-20 01:32:25', '确定要申请退款吗？', '', '2026-01-20 01:06:29', '2026-01-21 00:37:52');
INSERT INTO `order_main` VALUES (46, 'SM-20260120011446795', 6, NULL, 7, '301单元', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 900, 0, 900, 900, 1, 1, 1, NULL, NULL, '确定要申请退款吗？', '', '2026-01-20 01:14:46', '2026-01-21 00:37:52');
INSERT INTO `order_main` VALUES (47, 'SM-20260120012316303', 6, NULL, 7, '301单元', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 600, 0, 600, 600, 1, 1, 1, '2026-01-20 01:23:29', NULL, '确定要申请退款吗？', '', '2026-01-20 01:23:16', '2026-01-21 00:37:52');
INSERT INTO `order_main` VALUES (48, 'SM-20260120012927941', 6, NULL, 7, '301单元', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1500, 0, 1500, 1500, 1, 1, 1, '2026-01-20 01:29:46', NULL, '确定要申请退款吗？', '', '2026-01-20 01:29:27', '2026-01-21 00:37:52');
INSERT INTO `order_main` VALUES (49, 'SM-20260120013328950', 6, 6, 7, '301单元', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1500, 0, 1500, 1500, 2, 1, 1, '2026-01-20 01:33:37', NULL, '退款成功', '', '2026-01-20 01:33:28', '2026-01-21 00:37:52');
INSERT INTO `order_main` VALUES (50, 'OT-20260120013601936', 6, 6, 7, '姐姐', 4, '快递代拿', NULL, NULL, NULL, NULL, NULL, '超大件', NULL, NULL, NULL, 50, 0, 50, 50, 2, 1, 1, NULL, NULL, NULL, '1.规格：超大件，取件码：pj\n需要在2026-01-20 00:00-2026-01-20 18:00内配送过来\n备注：kkk', '2026-01-20 01:36:01', '2026-01-21 00:37:52');
INSERT INTO `order_main` VALUES (51, 'OM-20260120014357437', 6, NULL, 7, '301单元', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3000, 0, 3000, 3000, 0, 0, 1, NULL, NULL, NULL, '', '2026-01-20 01:43:57', '2026-01-21 00:37:52');
INSERT INTO `order_main` VALUES (52, 'SM-20260120022911131', 6, NULL, 7, '301单元', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1500, 0, 1500, 1500, 4, 3, 1, '2026-01-20 02:29:18', NULL, '退款成功', '', '2026-01-20 02:29:11', '2026-01-21 00:37:52');
INSERT INTO `order_main` VALUES (53, 'OT-20260120232519846', 6, NULL, 7, '123', 4, '快递代拿', NULL, NULL, NULL, NULL, NULL, '超大件', NULL, NULL, NULL, 50, 0, 50, 50, 4, 3, 1, '2026-01-20 23:25:47', NULL, '退款成功', '1.规格：超大件，取件码：123123213\n需要在2026-01-20 00:00-2026-01-20 02:00内配送过来\n备注：123', '2026-01-20 23:25:19', '2026-01-21 00:37:52');
INSERT INTO `order_main` VALUES (54, 'SM-20260120234338683', 6, NULL, 7, '301单元', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 600, 0, 600, 600, 4, 3, 1, '2026-01-20 23:43:49', NULL, '退款成功', '', '2026-01-20 23:43:38', '2026-01-21 00:37:52');
INSERT INTO `order_main` VALUES (55, 'SM-20260121000042184', 6, NULL, 7, '301单元', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 300, 0, 300, 300, 4, 3, 1, '2026-01-21 00:00:55', NULL, '退款成功', '', '2026-01-21 00:00:42', '2026-01-21 00:40:35');
INSERT INTO `order_main` VALUES (56, 'SM-20260121000458801', 6, NULL, 7, '301单元', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 300, 0, 300, 300, 4, 3, 1, '2026-01-21 00:05:07', NULL, '退款成功', '', '2026-01-21 00:04:58', '2026-01-21 00:40:33');
INSERT INTO `order_main` VALUES (57, 'SM-20260121001840668', 6, NULL, 7, '301单元', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 300, 0, 300, 300, 4, 3, 1, '2026-01-21 00:18:56', NULL, '退款成功', '', '2026-01-21 00:18:40', '2026-01-21 00:40:31');
INSERT INTO `order_main` VALUES (58, 'SM-20260121003031851', 6, NULL, 7, '301单元', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 300, 0, 300, 300, 4, 3, 1, '2026-01-21 00:30:39', NULL, '退款成功', '', '2026-01-21 00:30:31', '2026-01-21 00:40:16');
INSERT INTO `order_main` VALUES (59, 'OT-20260121003124185', 6, NULL, 7, '111', 4, '快递代拿', NULL, NULL, NULL, NULL, NULL, '大件', NULL, NULL, NULL, 400, 0, 400, 400, 4, 3, 1, '2026-01-21 00:31:34', NULL, '退款成功', '1.规格：大件，取件码：111\n需要在2026-01-21 00:00-2026-01-21 02:00内配送过来\n备注：111', '2026-01-21 00:31:24', '2026-01-21 00:40:14');
INSERT INTO `order_main` VALUES (60, 'OT-20260121003205406', 6, NULL, 7, '111', 4, '垃圾代扔', '111', NULL, NULL, NULL, NULL, '箱子规格', NULL, NULL, NULL, 190, 0, 190, 190, 4, 3, 1, '2026-01-21 00:32:15', NULL, '退款成功', '1.垃圾规格：箱子规格\n需要在2026-01-21 00:00-2026-01-21 03:00内为我处理垃圾\n备注：111', '2026-01-21 00:32:05', '2026-01-21 00:40:12');
INSERT INTO `order_main` VALUES (61, 'OT-20260121003238260', 6, NULL, 7, '无', 4, '物品代送', '11', '11', NULL, NULL, NULL, '3km内', NULL, NULL, NULL, 400, 0, 400, 400, 4, 3, 1, '2026-01-21 00:32:46', NULL, '退款成功', '配送规格：3km内\n需要在2026-01-21 00:00-2026-01-21 02:00内为我配送\n备注：111', '2026-01-21 00:32:38', '2026-01-21 00:40:10');
INSERT INTO `order_main` VALUES (62, 'OT-20260121003309601', 6, NULL, 7, '11', 4, '代购服务', NULL, NULL, NULL, NULL, NULL, '大件', NULL, NULL, NULL, 400, 0, 400, 400, 4, 3, 1, '2026-01-21 00:33:21', NULL, '退款成功', '代购规格：大件\n需要在2026-01-21 00:00-2026-01-21 02:00内为我购买配送\n备注：111', '2026-01-21 00:33:09', '2026-01-21 00:40:08');
INSERT INTO `order_main` VALUES (63, 'OT-20260121003343510', 6, NULL, 7, '11', 4, '万能服务', NULL, NULL, NULL, NULL, NULL, '喂宠物', NULL, NULL, NULL, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, '服务规格：喂宠物-0小时\n需要在2026-01-21 00:00-2026-01-21 02:00内为我服务', '2026-01-21 00:33:43', '2026-01-21 00:37:52');
INSERT INTO `order_main` VALUES (64, 'OT-20260121003426607', 6, NULL, 7, '11', 4, '打印服务', NULL, NULL, NULL, NULL, NULL, '彩色打印', '111', NULL, NULL, 100, 0, 100, 100, 4, 3, 1, '2026-01-21 00:35:02', NULL, '退款成功', '打印规格：彩色打印-A4-1份\n需要在2026-01-21 00:00-2026-01-21 01:00内为我服务\n文件列表：63895374799862.png', '2026-01-21 00:34:26', '2026-01-21 00:40:04');
INSERT INTO `order_main` VALUES (65, 'OT-20260121003531510', 6, 6, 7, '11', 4, '物品寄存', NULL, NULL, NULL, NULL, NULL, '按天寄存', NULL, NULL, NULL, 1000, 0, 1000, 1000, 4, 3, 1, '2026-01-21 00:35:39', '2026-01-21 00:38:03', '退款成功', '寄存类型：按天寄存\n寄存物品：11\n需要在2026-01-21 00:00-2026-01-21 03:00内为我服务\n备注：111', '2026-01-21 00:35:31', '2026-01-21 00:40:03');
INSERT INTO `order_main` VALUES (66, 'OT-20260121004343416', 6, 6, 7, '11', 4, '打印服务', NULL, NULL, NULL, NULL, NULL, '黑白打印', '11', NULL, NULL, 100, 0, 100, 100, 4, 3, 1, '2026-01-21 00:44:27', NULL, '退款成功', '打印规格：黑白打印-A4-1份\n需要在2026-01-21 00:00-2026-01-21 01:00内为我服务\n备注：11，文件列表：63895374799862.png', '2026-01-21 00:43:43', '2026-01-21 01:24:45');
INSERT INTO `order_main` VALUES (67, 'OM-20260121004946303', 6, NULL, 7, '301单元', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1500, 0, 1500, 1500, 4, 3, 1, '2026-01-21 00:49:51', NULL, '退款成功', '', '2026-01-21 00:49:46', '2026-01-21 00:50:23');

-- ----------------------------
-- Table structure for order_print_file
-- ----------------------------
DROP TABLE IF EXISTS `order_print_file`;
CREATE TABLE `order_print_file`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `file_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件URL',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件名',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '订单打印文件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_print_file
-- ----------------------------
INSERT INTO `order_print_file` VALUES (3, 64, 'https://xyz-errand.oss-cn-guangzhou.aliyuncs.com/upload/1768926889959.png', '63895374799862.png', '2026-01-21 00:34:51');
INSERT INTO `order_print_file` VALUES (4, 66, 'https://xyz-errand.oss-cn-guangzhou.aliyuncs.com/upload/1768927446855.png', '63895374799862.png', '2026-01-21 00:44:07');

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type_id` bigint NOT NULL COMMENT '关联商品类型表id',
  `spec_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '规格/服务名称（如：超小件、喂宠物、用户自定义服务）',
  `weight_min` decimal(5, 2) NULL DEFAULT NULL COMMENT '最小重量（kg）',
  `weight_max` decimal(5, 2) NULL DEFAULT NULL COMMENT '最大重量（kg）',
  `distance` decimal(5, 2) NULL DEFAULT NULL COMMENT '距离（公里）',
  `amount_min` bigint NULL DEFAULT NULL COMMENT '金额范围最小值（分）',
  `amount_max` bigint NULL DEFAULT NULL COMMENT '金额范围最大值（分）',
  `duration` int NULL DEFAULT NULL COMMENT '时长（如：1小时填1，1天填24）',
  `duration_unit` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '时长单位（小时/天/月）',
  `price_min` bigint NOT NULL COMMENT '最低价格（分）',
  `price_max` bigint NOT NULL COMMENT '最高价格（分）',
  `is_universal_custom` tinyint NOT NULL DEFAULT 0 COMMENT '是否为“万能服务”下的用户自定义服务（1=是，0=否）',
  `is_enabled` tinyint NOT NULL DEFAULT 1 COMMENT '是否启用（1=启用，0=禁用）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_type_spec_name`(`type_id` ASC, `spec_name` ASC) USING BTREE COMMENT '同一服务模块下规格名称唯一',
  INDEX `idx_type_id`(`type_id` ASC) USING BTREE COMMENT '类型id索引',
  CONSTRAINT `fk_product_type` FOREIGN KEY (`type_id`) REFERENCES `product_type` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '商品规格表（包含所有模块的具体规格和万能服务子项）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (4, 3, '超小件', 0.00, 0.50, 0.00, 0, 0, 0, '0', 0, 90, 0, 1, '2025-12-05 00:00:01', '2025-12-05 00:01:19');
INSERT INTO `product` VALUES (5, 3, '小件', 0.00, 1.50, 0.00, 0, 0, 0, '0', 0, 160, 0, 1, '2025-12-05 00:07:24', '2025-12-05 00:16:57');
INSERT INTO `product` VALUES (6, 3, '中件', 0.00, 2.50, 0.00, 0, 0, 0, '0', 0, 250, 0, 1, '2025-12-05 00:07:57', '2025-12-05 00:16:53');
INSERT INTO `product` VALUES (7, 3, '大件', 0.00, 8.00, 0.00, 0, 0, 0, '0', 0, 400, 0, 1, '2025-12-05 00:09:04', '2025-12-05 00:16:40');
INSERT INTO `product` VALUES (8, 4, '普通垃圾塑料袋', 0.00, 0.00, 0.00, 0, 0, 0, '0', 0, 90, 0, 1, '2025-12-05 00:14:44', '2025-12-05 00:17:22');
INSERT INTO `product` VALUES (9, 4, '箱子规格', 0.00, 0.00, 0.00, 0, 0, 0, '0', 0, 190, 0, 1, '2025-12-05 00:17:56', '2025-12-05 00:17:56');
INSERT INTO `product` VALUES (12, 6, '1km内', 0.00, 3.00, 1.00, 0, 0, 0, '0', 0, 300, 0, 1, '2025-12-05 00:21:04', '2025-12-05 00:21:04');
INSERT INTO `product` VALUES (13, 6, '2km内', 0.00, 3.00, 0.00, 0, 0, 0, '0', 0, 350, 0, 1, '2025-12-05 00:21:52', '2025-12-05 00:21:52');
INSERT INTO `product` VALUES (14, 6, '3km内', 0.00, 3.00, 0.00, 0, 0, 0, '0', 0, 400, 0, 1, '2025-12-05 00:22:09', '2025-12-05 00:22:09');
INSERT INTO `product` VALUES (15, 7, '超小件', 0.00, 0.50, 0.00, 0, 0, 0, '0', 0, 90, 0, 1, '2025-12-05 00:00:01', '2025-12-05 00:01:19');
INSERT INTO `product` VALUES (16, 7, '小件', 0.00, 1.50, 0.00, 0, 0, 0, '0', 0, 160, 0, 1, '2025-12-05 00:07:24', '2025-12-05 00:16:57');
INSERT INTO `product` VALUES (17, 7, '中件', 0.00, 2.50, 0.00, 0, 0, 0, '0', 0, 250, 0, 1, '2025-12-05 00:07:57', '2025-12-05 00:16:53');
INSERT INTO `product` VALUES (18, 7, '大件', 0.00, 8.00, 0.00, 0, 0, 0, '0', 0, 400, 0, 1, '2025-12-05 00:09:04', '2025-12-05 00:16:40');
INSERT INTO `product` VALUES (19, 8, '雨天接送', 0.00, 0.00, 0.00, 0, 0, 0, '0', 0, 0, 1, 1, '2025-12-05 00:24:52', '2025-12-05 00:24:52');
INSERT INTO `product` VALUES (20, 8, '喂宠物', 0.00, 0.00, 0.00, 0, 0, 0, '0', 0, 0, 1, 1, '2025-12-05 00:25:06', '2025-12-05 00:25:06');
INSERT INTO `product` VALUES (21, 8, '家政服务', 0.00, 0.00, 0.00, 0, 0, 3, '00', 0, 10000, 1, 1, '2025-12-05 00:25:24', '2025-12-14 23:02:58');
INSERT INTO `product` VALUES (22, 9, '黑白打印', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 100, 100, 0, 1, '2025-12-05 00:26:15', '2025-12-11 23:16:32');
INSERT INTO `product` VALUES (23, 9, '彩色打印', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 100, 100, 0, 1, '2025-12-05 00:26:26', '2025-12-11 23:16:20');
INSERT INTO `product` VALUES (24, 11, '按天寄存', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 1000, 0, 1, '2025-12-11 23:40:27', '2025-12-11 23:40:27');
INSERT INTO `product` VALUES (25, 11, '按周寄存', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 5000, 0, 1, '2025-12-11 23:40:45', '2025-12-11 23:40:45');
INSERT INTO `product` VALUES (26, 11, '按月寄存', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 10000, 0, 1, '2025-12-11 23:40:56', '2025-12-11 23:41:05');
INSERT INTO `product` VALUES (27, 3, '超大件', 0.00, 5.00, NULL, 0, 0, 0, '0', 0, 50, 0, 1, '2025-12-19 01:18:26', '2025-12-19 01:18:34');

-- ----------------------------
-- Table structure for product_type
-- ----------------------------
DROP TABLE IF EXISTS `product_type`;
CREATE TABLE `product_type`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '模块名称（如：代拿快递、万能服务）',
  `type_desc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '模块描述',
  `icon_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '模块图标URL（用于小程序端展示）',
  `is_manageable` tinyint NOT NULL DEFAULT 1 COMMENT '是否可增删改（1=是，0=否）',
  `is_enabled` tinyint NOT NULL DEFAULT 1 COMMENT '是否启用（1=启用，0=禁用）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_type_name`(`type_name` ASC) USING BTREE COMMENT '模块名称唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '商品类型表（各服务模块）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product_type
-- ----------------------------
INSERT INTO `product_type` VALUES (3, '快递代拿', '可代拿附近快递，备注填写取件码', '/profile/upload/2025/12/08/lajitong_01(2)_20251208234709A014.png', 1, 1, '2025-12-04 23:52:31', '2025-12-09 00:43:27');
INSERT INTO `product_type` VALUES (4, '垃圾代扔', '', '/profile/upload/2025/12/08/LS垃圾分类_20251208234453A012.png', 1, 1, '2025-12-04 23:53:18', '2025-12-09 00:43:20');
INSERT INTO `product_type` VALUES (6, '物品代送', '备注里填写要配送的物品（配送特殊物品超重3kg需备注，远路程可备注）', '/profile/upload/2025/12/08/我要成为骑手_20251208234546A013.png', 1, 1, '2025-12-04 23:54:18', '2025-12-09 00:43:01');
INSERT INTO `product_type` VALUES (7, '代购服务', '备注里填写要买的物品', '/profile/upload/2025/12/08/代购购物车小图标_20251208234006A009.png', 1, 1, '2025-12-04 23:54:38', '2025-12-09 00:42:47');
INSERT INTO `product_type` VALUES (8, '万能服务', '可选择你需要的其他类型服务', '/profile/upload/2025/12/08/服务_20251208233834A007.png', 1, 1, '2025-12-04 23:55:14', '2025-12-08 23:38:35');
INSERT INTO `product_type` VALUES (9, '打印服务', '备注里填写打印规格', '/profile/upload/2025/12/08/打印_20251208234111A010.png', 1, 1, '2025-12-04 23:55:45', '2025-12-09 00:42:41');
INSERT INTO `product_type` VALUES (11, '物品寄存', '可寄存行李箱，衣物等物品', '/profile/upload/2025/12/08/物品寄存_20251208233709A006.png', 1, 1, '2025-12-04 23:56:53', '2025-12-08 23:37:11');
INSERT INTO `product_type` VALUES (12, '便民超市', '超市', '/profile/upload/2025/12/08/附近超市_20251208234859A015.png', 1, 1, '2025-12-08 23:49:01', '2025-12-08 23:49:01');

-- ----------------------------
-- Table structure for service_package
-- ----------------------------
DROP TABLE IF EXISTS `service_package`;
CREATE TABLE `service_package`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type_id` bigint NOT NULL COMMENT '关联商品类型表ID（帮扔垃圾/接送小孩）',
  `package_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '套餐名称',
  `price` bigint NOT NULL COMMENT '套餐价格（分）',
  `first_month_price` bigint NULL DEFAULT 0 COMMENT '首月优惠价（分），仅包月可用（单次填0）',
  `duration_type` tinyint NOT NULL COMMENT '时长类型（1=包月，2=包年）',
  `duration_days` int NOT NULL COMMENT '套餐时长（天），包月填30',
  `include_service` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '包含服务描述',
  `is_enabled` tinyint NOT NULL DEFAULT 1 COMMENT '是否启用（1=启用，0=禁用）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_type_package_name`(`type_id` ASC, `package_name` ASC) USING BTREE,
  INDEX `idx_type_id`(`type_id` ASC) USING BTREE,
  INDEX `idx_duration_type`(`duration_type` ASC) USING BTREE,
  INDEX `idx_is_enabled`(`is_enabled` ASC) USING BTREE,
  CONSTRAINT `fk_package_product_type` FOREIGN KEY (`type_id`) REFERENCES `product_type` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '服务套餐表（帮扔垃圾/接送小孩等套餐）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of service_package
-- ----------------------------
INSERT INTO `service_package` VALUES (2, 4, '代扔垃圾包月', 3000, 2500, 1, 30, '扔垃圾', 1, '2025-12-05 23:33:50', '2025-12-05 23:33:50');
INSERT INTO `service_package` VALUES (3, 4, '代扔垃圾包年', 29900, NULL, 2, 365, '扔垃圾', 1, '2025-12-05 23:34:30', '2025-12-05 23:34:30');

-- ----------------------------
-- Table structure for shopping_cart
-- ----------------------------
DROP TABLE IF EXISTS `shopping_cart`;
CREATE TABLE `shopping_cart`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '关联用户表主键',
  `product_type` tinyint NOT NULL COMMENT '商品类型（1=超市商品，2=外卖商品）',
  `product_id` bigint NOT NULL COMMENT '关联商品表主键',
  `store_id` bigint NULL DEFAULT NULL COMMENT '外卖商品关联店铺表主键（超市商品可空）',
  `quantity` int NOT NULL DEFAULT 1 COMMENT '商品数量',
  `is_selected` tinyint NOT NULL DEFAULT 1 COMMENT '是否选中（1=选中，0=未选中）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_product`(`user_id` ASC, `product_type` ASC, `product_id` ASC, `store_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_product_type`(`product_type` ASC) USING BTREE,
  INDEX `idx_is_selected`(`is_selected` ASC) USING BTREE,
  INDEX `fk_cart_store`(`store_id` ASC) USING BTREE,
  CONSTRAINT `fk_cart_store` FOREIGN KEY (`store_id`) REFERENCES `takeaway_store` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_cart_user` FOREIGN KEY (`user_id`) REFERENCES `wechat_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '超市/外卖共用购物车表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shopping_cart
-- ----------------------------
INSERT INTO `shopping_cart` VALUES (20, 4, 2, 5, 3, 3, 1, '2025-12-17 00:14:32', '2025-12-17 00:14:32');
INSERT INTO `shopping_cart` VALUES (21, 4, 1, 5, NULL, 1, 1, '2025-12-17 00:17:35', '2025-12-17 00:17:35');
INSERT INTO `shopping_cart` VALUES (22, 4, 1, 4, NULL, 1, 1, '2025-12-17 00:17:36', '2025-12-17 00:17:36');

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` int NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '参数配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2025-10-30 17:15:46', '', NULL, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO `sys_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2025-10-30 17:15:46', '', NULL, '初始化密码 123456');
INSERT INTO `sys_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2025-10-30 17:15:46', '', NULL, '深色主题theme-dark，浅色主题theme-light');
INSERT INTO `sys_config` VALUES (4, '账号自助-验证码开关', 'sys.account.captchaEnabled', 'true', 'Y', 'admin', '2025-10-30 17:15:46', '', NULL, '是否开启验证码功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 'admin', '2025-10-30 17:15:46', '', NULL, '是否开启注册用户功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (6, '用户登录-黑名单列表', 'sys.login.blackIPList', '', 'Y', 'admin', '2025-10-30 17:15:46', '', NULL, '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');
INSERT INTO `sys_config` VALUES (7, '用户管理-初始密码修改策略', 'sys.account.initPasswordModify', '1', 'Y', 'admin', '2025-10-30 17:15:46', '', NULL, '0：初始密码修改策略关闭，没有任何提示，1：提醒用户，如果未修改初始密码，则在登录时就会提醒修改密码对话框');
INSERT INTO `sys_config` VALUES (8, '用户管理-账号密码更新周期', 'sys.account.passwordValidateDays', '0', 'Y', 'admin', '2025-10-30 17:15:46', '', NULL, '密码更新周期（填写数字，数据初始化值为0不限制，若修改必须为大于0小于365的正整数），如果超过这个周期登录系统时，则在登录时就会提醒修改密码对话框');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '部门名称',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 200 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, 0, '0', '若依科技', 0, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-10-30 17:15:40', '', NULL);
INSERT INTO `sys_dept` VALUES (101, 100, '0,100', '深圳总公司', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-10-30 17:15:40', '', NULL);
INSERT INTO `sys_dept` VALUES (102, 100, '0,100', '长沙分公司', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-10-30 17:15:40', '', NULL);
INSERT INTO `sys_dept` VALUES (103, 101, '0,100,101', '研发部门', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-10-30 17:15:40', '', NULL);
INSERT INTO `sys_dept` VALUES (104, 101, '0,100,101', '市场部门', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-10-30 17:15:40', '', NULL);
INSERT INTO `sys_dept` VALUES (105, 101, '0,100,101', '测试部门', 3, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-10-30 17:15:40', '', NULL);
INSERT INTO `sys_dept` VALUES (106, 101, '0,100,101', '财务部门', 4, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-10-30 17:15:40', '', NULL);
INSERT INTO `sys_dept` VALUES (107, 101, '0,100,101', '运维部门', 5, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-10-30 17:15:40', '', NULL);
INSERT INTO `sys_dept` VALUES (108, 102, '0,100,102', '市场部门', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-10-30 17:15:40', '', NULL);
INSERT INTO `sys_dept` VALUES (109, 102, '0,100,102', '财务部门', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-10-30 17:15:40', '', NULL);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '字典数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '性别男');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '性别女');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '性别未知');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '显示菜单');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '隐藏菜单');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '默认分组');
INSERT INTO `sys_dict_data` VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '系统分组');
INSERT INTO `sys_dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '系统默认是');
INSERT INTO `sys_dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '系统默认否');
INSERT INTO `sys_dict_data` VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '通知');
INSERT INTO `sys_dict_data` VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '公告');
INSERT INTO `sys_dict_data` VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '关闭状态');
INSERT INTO `sys_dict_data` VALUES (18, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '其他操作');
INSERT INTO `sys_dict_data` VALUES (19, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '新增操作');
INSERT INTO `sys_dict_data` VALUES (20, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '修改操作');
INSERT INTO `sys_dict_data` VALUES (21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '删除操作');
INSERT INTO `sys_dict_data` VALUES (22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '授权操作');
INSERT INTO `sys_dict_data` VALUES (23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '导出操作');
INSERT INTO `sys_dict_data` VALUES (24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '导入操作');
INSERT INTO `sys_dict_data` VALUES (25, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2025-10-30 17:15:46', '', NULL, '强退操作');
INSERT INTO `sys_dict_data` VALUES (26, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2025-10-30 17:15:46', '', NULL, '生成操作');
INSERT INTO `sys_dict_data` VALUES (27, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2025-10-30 17:15:46', '', NULL, '清空操作');
INSERT INTO `sys_dict_data` VALUES (28, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2025-10-30 17:15:46', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (29, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2025-10-30 17:15:46', '', NULL, '停用状态');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '字典类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '任务状态列表');
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '任务分组列表');
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type` VALUES (7, '通知类型', 'sys_notice_type', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '通知类型列表');
INSERT INTO `sys_dict_type` VALUES (8, '通知状态', 'sys_notice_status', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '通知状态列表');
INSERT INTO `sys_dict_type` VALUES (9, '操作类型', 'sys_oper_type', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '操作类型列表');
INSERT INTO `sys_dict_type` VALUES (10, '系统状态', 'sys_common_status', '0', 'admin', '2025-10-30 17:15:45', '', NULL, '登录状态列表');

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
  `job_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '定时任务调度表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO `sys_job` VALUES (1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '0/10 * * * * ?', '3', '1', '1', 'admin', '2025-10-30 17:15:46', '', NULL, '');
INSERT INTO `sys_job` VALUES (2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '0/15 * * * * ?', '3', '1', '1', 'admin', '2025-10-30 17:15:46', '', NULL, '');
INSERT INTO `sys_job` VALUES (3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)', '0/20 * * * * ?', '3', '1', '1', 'admin', '2025-10-30 17:15:46', '', NULL, '');

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log`  (
  `job_log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '日志信息',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '异常信息',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '定时任务调度日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor`  (
  `info_id` bigint NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '操作系统',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '提示消息',
  `login_time` datetime NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`) USING BTREE,
  INDEX `idx_sys_logininfor_s`(`status` ASC) USING BTREE,
  INDEX `idx_sys_logininfor_lt`(`login_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 144 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统访问记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_logininfor
-- ----------------------------
INSERT INTO `sys_logininfor` VALUES (100, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '1', '用户不存在/密码错误', '2025-10-30 17:52:46');
INSERT INTO `sys_logininfor` VALUES (101, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-10-30 17:54:10');
INSERT INTO `sys_logininfor` VALUES (102, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-10-30 18:13:24');
INSERT INTO `sys_logininfor` VALUES (103, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-10-30 18:43:44');
INSERT INTO `sys_logininfor` VALUES (104, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '退出成功', '2025-10-31 01:14:15');
INSERT INTO `sys_logininfor` VALUES (105, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-10-31 01:14:19');
INSERT INTO `sys_logininfor` VALUES (106, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-01 23:16:02');
INSERT INTO `sys_logininfor` VALUES (107, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-18 18:02:36');
INSERT INTO `sys_logininfor` VALUES (108, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-19 00:27:41');
INSERT INTO `sys_logininfor` VALUES (109, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-25 03:15:43');
INSERT INTO `sys_logininfor` VALUES (110, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-26 02:07:23');
INSERT INTO `sys_logininfor` VALUES (111, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '退出成功', '2025-11-26 03:32:24');
INSERT INTO `sys_logininfor` VALUES (112, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-26 03:32:33');
INSERT INTO `sys_logininfor` VALUES (113, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '退出成功', '2025-11-26 03:37:55');
INSERT INTO `sys_logininfor` VALUES (114, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-26 03:38:01');
INSERT INTO `sys_logininfor` VALUES (115, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-11-27 01:57:39');
INSERT INTO `sys_logininfor` VALUES (116, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-12-01 23:51:08');
INSERT INTO `sys_logininfor` VALUES (117, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-12-02 21:23:37');
INSERT INTO `sys_logininfor` VALUES (118, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-12-03 21:49:41');
INSERT INTO `sys_logininfor` VALUES (119, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-12-04 23:34:05');
INSERT INTO `sys_logininfor` VALUES (120, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-12-05 23:29:46');
INSERT INTO `sys_logininfor` VALUES (121, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '1', '验证码错误', '2025-12-07 00:16:45');
INSERT INTO `sys_logininfor` VALUES (122, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-12-07 00:16:49');
INSERT INTO `sys_logininfor` VALUES (123, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-12-07 16:08:16');
INSERT INTO `sys_logininfor` VALUES (124, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-12-07 23:12:24');
INSERT INTO `sys_logininfor` VALUES (125, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-12-07 23:38:12');
INSERT INTO `sys_logininfor` VALUES (126, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-12-08 22:49:47');
INSERT INTO `sys_logininfor` VALUES (127, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-12-09 21:43:18');
INSERT INTO `sys_logininfor` VALUES (128, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-12-10 22:10:11');
INSERT INTO `sys_logininfor` VALUES (129, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-12-11 22:46:25');
INSERT INTO `sys_logininfor` VALUES (130, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-12-12 22:22:20');
INSERT INTO `sys_logininfor` VALUES (131, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-12-14 00:17:39');
INSERT INTO `sys_logininfor` VALUES (132, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-12-14 16:40:52');
INSERT INTO `sys_logininfor` VALUES (133, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-12-15 21:32:46');
INSERT INTO `sys_logininfor` VALUES (134, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-12-16 23:21:06');
INSERT INTO `sys_logininfor` VALUES (135, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-12-17 22:05:36');
INSERT INTO `sys_logininfor` VALUES (136, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-12-18 22:14:50');
INSERT INTO `sys_logininfor` VALUES (137, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-12-20 00:05:34');
INSERT INTO `sys_logininfor` VALUES (138, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-12-23 00:44:56');
INSERT INTO `sys_logininfor` VALUES (139, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-12-23 22:18:00');
INSERT INTO `sys_logininfor` VALUES (140, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-12-24 23:40:58');
INSERT INTO `sys_logininfor` VALUES (141, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-12-25 23:46:17');
INSERT INTO `sys_logininfor` VALUES (142, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2026-01-19 23:50:28');
INSERT INTO `sys_logininfor` VALUES (143, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2026-01-20 23:04:50');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '路由名称',
  `is_frame` int NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` int NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2002 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 1, 'system', NULL, '', '', 1, 0, 'M', '0', '0', '', 'system', 'admin', '2025-10-30 17:15:41', '', NULL, '系统管理目录');
INSERT INTO `sys_menu` VALUES (2, '系统监控', 0, 2, 'monitor', NULL, '', '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', '2025-10-30 17:15:41', '', NULL, '系统监控目录');
INSERT INTO `sys_menu` VALUES (3, '系统工具', 0, 3, 'tool', NULL, '', '', 1, 0, 'M', '0', '0', '', 'tool', 'admin', '2025-10-30 17:15:41', '', NULL, '系统工具目录');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', '', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin', '2025-10-30 17:15:41', '', NULL, '用户管理菜单');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', 'admin', '2025-10-30 17:15:41', '', NULL, '角色管理菜单');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', '2025-10-30 17:15:41', '', NULL, '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree', 'admin', '2025-10-30 17:15:41', '', NULL, '部门管理菜单');
INSERT INTO `sys_menu` VALUES (104, '岗位管理', 1, 5, 'post', 'system/post/index', '', '', 1, 0, 'C', '0', '0', 'system:post:list', 'post', 'admin', '2025-10-30 17:15:41', '', NULL, '岗位管理菜单');
INSERT INTO `sys_menu` VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict', 'admin', '2025-10-30 17:15:41', '', NULL, '字典管理菜单');
INSERT INTO `sys_menu` VALUES (106, '参数设置', 1, 7, 'config', 'system/config/index', '', '', 1, 0, 'C', '0', '0', 'system:config:list', 'edit', 'admin', '2025-10-30 17:15:41', '', NULL, '参数设置菜单');
INSERT INTO `sys_menu` VALUES (107, '通知公告', 1, 8, 'notice', 'system/notice/index', '', '', 1, 0, 'C', '0', '0', 'system:notice:list', 'message', 'admin', '2025-10-30 17:15:41', '', NULL, '通知公告菜单');
INSERT INTO `sys_menu` VALUES (108, '日志管理', 1, 9, 'log', '', '', '', 1, 0, 'M', '0', '0', '', 'log', 'admin', '2025-10-30 17:15:41', '', NULL, '日志管理菜单');
INSERT INTO `sys_menu` VALUES (109, '在线用户', 2, 1, 'online', 'monitor/online/index', '', '', 1, 0, 'C', '0', '0', 'monitor:online:list', 'online', 'admin', '2025-10-30 17:15:41', '', NULL, '在线用户菜单');
INSERT INTO `sys_menu` VALUES (110, '定时任务', 2, 2, 'job', 'monitor/job/index', '', '', 1, 0, 'C', '0', '0', 'monitor:job:list', 'job', 'admin', '2025-10-30 17:15:41', '', NULL, '定时任务菜单');
INSERT INTO `sys_menu` VALUES (111, '数据监控', 2, 3, 'druid', 'monitor/druid/index', '', '', 1, 0, 'C', '0', '0', 'monitor:druid:list', 'druid', 'admin', '2025-10-30 17:15:41', '', NULL, '数据监控菜单');
INSERT INTO `sys_menu` VALUES (112, '服务监控', 2, 4, 'server', 'monitor/server/index', '', '', 1, 0, 'C', '0', '0', 'monitor:server:list', 'server', 'admin', '2025-10-30 17:15:41', '', NULL, '服务监控菜单');
INSERT INTO `sys_menu` VALUES (113, '缓存监控', 2, 5, 'cache', 'monitor/cache/index', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis', 'admin', '2025-10-30 17:15:41', '', NULL, '缓存监控菜单');
INSERT INTO `sys_menu` VALUES (114, '缓存列表', 2, 6, 'cacheList', 'monitor/cache/list', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis-list', 'admin', '2025-10-30 17:15:41', '', NULL, '缓存列表菜单');
INSERT INTO `sys_menu` VALUES (115, '表单构建', 3, 1, 'build', 'tool/build/index', '', '', 1, 0, 'C', '0', '0', 'tool:build:list', 'build', 'admin', '2025-10-30 17:15:41', '', NULL, '表单构建菜单');
INSERT INTO `sys_menu` VALUES (116, '代码生成', 3, 2, 'gen', 'tool/gen/index', '', '', 1, 0, 'C', '0', '0', 'tool:gen:list', 'code', 'admin', '2025-10-30 17:15:41', '', NULL, '代码生成菜单');
INSERT INTO `sys_menu` VALUES (117, '系统接口', 3, 3, 'swagger', 'tool/swagger/index', '', '', 1, 0, 'C', '0', '0', 'tool:swagger:list', 'swagger', 'admin', '2025-10-30 17:15:41', '', NULL, '系统接口菜单');
INSERT INTO `sys_menu` VALUES (500, '操作日志', 108, 1, 'operlog', 'monitor/operlog/index', '', '', 1, 0, 'C', '0', '0', 'monitor:operlog:list', 'form', 'admin', '2025-10-30 17:15:41', '', NULL, '操作日志菜单');
INSERT INTO `sys_menu` VALUES (501, '登录日志', 108, 2, 'logininfor', 'monitor/logininfor/index', '', '', 1, 0, 'C', '0', '0', 'monitor:logininfor:list', 'logininfor', 'admin', '2025-10-30 17:15:41', '', NULL, '登录日志菜单');
INSERT INTO `sys_menu` VALUES (1000, '用户查询', 100, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', '2025-10-30 17:15:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1001, '用户新增', 100, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', '2025-10-30 17:15:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1002, '用户修改', 100, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', '2025-10-30 17:15:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1003, '用户删除', 100, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', '2025-10-30 17:15:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1004, '用户导出', 100, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', '2025-10-30 17:15:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1005, '用户导入', 100, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', '2025-10-30 17:15:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1006, '重置密码', 100, 7, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2025-10-30 17:15:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1007, '角色查询', 101, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', '2025-10-30 17:15:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1008, '角色新增', 101, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', '2025-10-30 17:15:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1009, '角色修改', 101, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', '2025-10-30 17:15:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1010, '角色删除', 101, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1011, '角色导出', 101, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1012, '菜单查询', 102, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1013, '菜单新增', 102, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1014, '菜单修改', 102, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1015, '菜单删除', 102, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1016, '部门查询', 103, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1017, '部门新增', 103, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1018, '部门修改', 103, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1019, '部门删除', 103, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1020, '岗位查询', 104, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1021, '岗位新增', 104, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1022, '岗位修改', 104, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1023, '岗位删除', 104, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1024, '岗位导出', 104, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1025, '字典查询', 105, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1026, '字典新增', 105, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1027, '字典修改', 105, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1028, '字典删除', 105, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1029, '字典导出', 105, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1030, '参数查询', 106, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1031, '参数新增', 106, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1032, '参数修改', 106, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1033, '参数删除', 106, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1034, '参数导出', 106, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1035, '公告查询', 107, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1036, '公告新增', 107, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1037, '公告修改', 107, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1038, '公告删除', 107, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1039, '操作查询', 500, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1040, '操作删除', 500, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1041, '日志导出', 500, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1042, '登录查询', 501, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1043, '登录删除', 501, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1044, '日志导出', 501, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1045, '账户解锁', 501, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:unlock', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1046, '在线查询', 109, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1047, '批量强退', 109, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1048, '单条强退', 109, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1049, '任务查询', 110, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1050, '任务新增', 110, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1051, '任务修改', 110, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1052, '任务删除', 110, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1053, '状态修改', 110, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus', '#', 'admin', '2025-10-30 17:15:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1054, '任务导出', 110, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export', '#', 'admin', '2025-10-30 17:15:43', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1055, '生成查询', 116, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', 'admin', '2025-10-30 17:15:43', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1056, '生成修改', 116, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', 'admin', '2025-10-30 17:15:43', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1057, '生成删除', 116, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', 'admin', '2025-10-30 17:15:43', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1058, '导入代码', 116, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', 'admin', '2025-10-30 17:15:43', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1059, '预览代码', 116, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', 'admin', '2025-10-30 17:15:43', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1060, '生成代码', 116, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', 'admin', '2025-10-30 17:15:43', '', NULL, '');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `notice_id` int NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '公告标题',
  `notice_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob NULL COMMENT '公告内容',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '通知公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (1, '温馨提醒：2018-07-01 若依新版本发布啦', '2', 0xE696B0E78988E69CACE58685E5AEB9, '0', 'admin', '2025-10-30 17:15:46', '', NULL, '管理员');
INSERT INTO `sys_notice` VALUES (2, '维护通知：2018-07-01 若依系统凌晨维护', '1', 0xE7BBB4E68AA4E58685E5AEB9, '0', 'admin', '2025-10-30 17:15:46', '', NULL, '管理员');

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `oper_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '模块标题',
  `business_type` int NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '请求方式',
  `operator_type` int NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '返回参数',
  `status` int NULL DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint NULL DEFAULT 0 COMMENT '消耗时间',
  PRIMARY KEY (`oper_id`) USING BTREE,
  INDEX `idx_sys_oper_log_bt`(`business_type` ASC) USING BTREE,
  INDEX `idx_sys_oper_log_s`(`status` ASC) USING BTREE,
  INDEX `idx_sys_oper_log_ot`(`oper_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 315 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '操作日志记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------
INSERT INTO `sys_oper_log` VALUES (100, '角色管理', 2, 'com.axin.web.controller.system.SysRoleController.changeStatus()', 'PUT', 1, 'admin', '研发部门', '/system/role/changeStatus', '127.0.0.1', '内网IP', '{\"admin\":true,\"deptCheckStrictly\":false,\"flag\":false,\"menuCheckStrictly\":false,\"params\":{},\"roleId\":1,\"status\":\"1\"}', NULL, 1, '不允许操作超级管理员角色', '2025-10-30 17:57:11', 15);
INSERT INTO `sys_oper_log` VALUES (101, '角色管理', 2, 'com.axin.web.controller.system.SysRoleController.changeStatus()', 'PUT', 1, 'admin', '研发部门', '/system/role/changeStatus', '127.0.0.1', '内网IP', '{\"admin\":false,\"deptCheckStrictly\":false,\"flag\":false,\"menuCheckStrictly\":false,\"params\":{},\"roleId\":2,\"status\":\"1\",\"updateBy\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-10-30 17:57:13', 24);
INSERT INTO `sys_oper_log` VALUES (102, '角色管理', 2, 'com.axin.web.controller.system.SysRoleController.changeStatus()', 'PUT', 1, 'admin', '研发部门', '/system/role/changeStatus', '127.0.0.1', '内网IP', '{\"admin\":false,\"deptCheckStrictly\":false,\"flag\":false,\"menuCheckStrictly\":false,\"params\":{},\"roleId\":2,\"status\":\"0\",\"updateBy\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-10-30 17:57:14', 10);
INSERT INTO `sys_oper_log` VALUES (103, '在线用户', 7, 'com.axin.web.controller.monitor.SysUserOnlineController.forceLogout()', 'DELETE', 1, 'admin', '研发部门', '/monitor/online/855bde2f-d735-45b9-a3d3-3eede6bb35d6', '127.0.0.1', '内网IP', '\"855bde2f-d735-45b9-a3d3-3eede6bb35d6\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-10-30 18:13:20', 1);
INSERT INTO `sys_oper_log` VALUES (104, '用户头像', 2, 'com.axin.web.controller.system.SysProfileController.avatar()', 'POST', 1, 'admin', '研发部门', '/system/user/profile/avatar', '127.0.0.1', '内网IP', '', '{\"msg\":\"操作成功\",\"imgUrl\":\"/profile/avatar/2025/10/30/41032c5ebcd5484bb13bac77a17bfce3.png\",\"code\":200}', 0, NULL, '2025-10-30 18:43:58', 174);
INSERT INTO `sys_oper_log` VALUES (105, '用户头像', 2, 'com.axin.web.controller.system.SysProfileController.avatar()', 'POST', 1, 'admin', '研发部门', '/system/user/profile/avatar', '127.0.0.1', '内网IP', '', '{\"msg\":\"操作成功\",\"imgUrl\":\"/profile/avatar/2025/10/30/41c07c211843403489611f6ad4be3939.png\",\"code\":200}', 0, NULL, '2025-10-30 18:51:48', 24);
INSERT INTO `sys_oper_log` VALUES (106, '用户头像', 2, 'com.axin.web.controller.system.SysProfileController.avatar()', 'POST', 1, 'admin', '研发部门', '/system/user/profile/avatar', '127.0.0.1', '内网IP', '', '{\"msg\":\"操作成功\",\"imgUrl\":\"/profile/avatar/2025/10/30/f83c7e0ee3514e53b591ab7e3c65b412.png\",\"code\":200}', 0, NULL, '2025-10-30 18:53:35', 227);
INSERT INTO `sys_oper_log` VALUES (107, '菜单管理', 1, 'com.axin.web.controller.system.SysMenuController.add()', 'POST', 1, 'admin', '研发部门', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"createBy\":\"admin\",\"icon\":\"shopping\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"跑腿管理\",\"menuType\":\"M\",\"orderNum\":4,\"params\":{},\"parentId\":0,\"path\":\"test\",\"status\":\"0\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-11-25 03:18:58', 33);
INSERT INTO `sys_oper_log` VALUES (108, '菜单管理', 2, 'com.axin.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"createTime\":\"2025-11-25 03:18:58\",\"icon\":\"shopping\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2000,\"menuName\":\"跑腿管理\",\"menuType\":\"M\",\"orderNum\":4,\"params\":{},\"parentId\":0,\"path\":\"runservice\",\"perms\":\"\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-11-25 03:19:27', 15);
INSERT INTO `sys_oper_log` VALUES (109, '菜单管理', 1, 'com.axin.web.controller.system.SysMenuController.add()', 'POST', 1, 'admin', '研发部门', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"runservice/wx_user/index\",\"createBy\":\"admin\",\"icon\":\"wechat\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"用户管理\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2000,\"path\":\"wx_user\",\"perms\":\"runservice:wx_user:list\",\"routeName\":\"wx_user\",\"status\":\"0\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-11-25 03:22:19', 14);
INSERT INTO `sys_oper_log` VALUES (110, '小区管理', 1, 'com.axin.web.controller.errand.CommunityController.add()', 'POST', 1, 'admin', '研发部门', '/errand/community', '127.0.0.1', '内网IP', '{\"address\":\"广东省广州市花都区\",\"communityName\":\"测试小区1\",\"contactPerson\":\"张三\",\"contactPhone\":\"17876118798\",\"isEnabled\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-11-27 01:58:17', 65);
INSERT INTO `sys_oper_log` VALUES (111, '小区管理', 1, 'com.axin.web.controller.errand.CommunityController.add()', 'POST', 1, 'admin', '研发部门', '/errand/community', '127.0.0.1', '内网IP', '{\"address\":\"广东省广州市花都区\",\"communityName\":\"测试小区2\",\"contactPerson\":\"张三\",\"contactPhone\":\"17876118798\",\"isEnabled\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-11-27 02:04:09', 16);
INSERT INTO `sys_oper_log` VALUES (112, '小区管理', 1, 'com.axin.web.controller.errand.CommunityController.add()', 'POST', 1, 'admin', '研发部门', '/errand/community', '127.0.0.1', '内网IP', '{\"address\":\"广东省广州市花都区\",\"communityName\":\"测试小区1\",\"contactPerson\":\"张三\",\"contactPhone\":\"17876118798\",\"isEnabled\":1}', '{\"msg\":\"新增小区失败，小区名称已存在\",\"code\":500}', 0, NULL, '2025-11-27 02:04:13', 6);
INSERT INTO `sys_oper_log` VALUES (113, '小区管理', 1, 'com.axin.web.controller.errand.CommunityController.add()', 'POST', 1, 'admin', '研发部门', '/errand/community', '127.0.0.1', '内网IP', '{\"address\":\"广东省广州市花都区1\",\"communityName\":\"测试小区1\",\"contactPerson\":\"张三\",\"contactPhone\":\"17876118798\",\"isEnabled\":1}', '{\"msg\":\"新增小区失败，小区名称已存在\",\"code\":500}', 0, NULL, '2025-11-27 02:04:17', 5);
INSERT INTO `sys_oper_log` VALUES (114, '小区管理', 1, 'com.axin.web.controller.errand.CommunityController.add()', 'POST', 1, 'admin', '研发部门', '/errand/community', '127.0.0.1', '内网IP', '{\"address\":\"广东省广州市花都区1\",\"communityName\":\"测试小区3\",\"contactPerson\":\"张三\",\"contactPhone\":\"17876118798\",\"isEnabled\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-11-27 02:04:21', 19);
INSERT INTO `sys_oper_log` VALUES (115, '小区管理', 1, 'com.axin.web.controller.errand.CommunityController.add()', 'POST', 1, 'admin', '研发部门', '/errand/community', '127.0.0.1', '内网IP', '{\"address\":\"广东省广州市花都区1\",\"communityName\":\"测试小区3\",\"contactPerson\":\"张三\",\"contactPhone\":\"178761187981\",\"isEnabled\":1}', '{\"msg\":\"新增小区失败，小区名称已存在\",\"code\":500}', 0, NULL, '2025-11-27 02:04:23', 3);
INSERT INTO `sys_oper_log` VALUES (116, '小区管理', 1, 'com.axin.web.controller.errand.CommunityController.add()', 'POST', 1, 'admin', '研发部门', '/errand/community', '127.0.0.1', '内网IP', '{\"address\":\"广东省广州市花都区1\",\"communityName\":\"测试小区13\",\"contactPerson\":\"张三\",\"contactPhone\":\"178761187981\",\"isEnabled\":1}', NULL, 1, '\r\n### Error updating database.  Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'contact_phone\' at row 1\r\n### The error may exist in file [D:\\Working\\Framework\\Backend\\Backend-system\\target\\classes\\mapper\\system\\CommunityMapper.xml]\r\n### The error may involve com.axin.system.mapper.CommunityMapper.insertCommunity-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO community (community_name, address, contact_person, contact_phone, is_enabled, create_time, update_time)         VALUES (?, ?, ?, ?, ?, NOW(), NOW())\r\n### Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'contact_phone\' at row 1\n; Data truncation: Data too long for column \'contact_phone\' at row 1', '2025-11-27 02:04:26', 176);
INSERT INTO `sys_oper_log` VALUES (117, '小区管理', 1, 'com.axin.web.controller.errand.CommunityController.add()', 'POST', 1, 'admin', '研发部门', '/errand/community', '127.0.0.1', '内网IP', '{\"address\":\"广东省广州市花都区1\",\"communityName\":\"测试小区13\",\"contactPerson\":\"张三\",\"contactPhone\":\"17876118798\",\"isEnabled\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-11-27 02:04:47', 26);
INSERT INTO `sys_oper_log` VALUES (118, '商品（服务）类型管理', 1, 'com.axin.web.controller.errand.ProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"sss\",\"isEnabled\":0,\"isManageable\":0,\"typeDesc\":\"测试\",\"typeName\":\"测试1\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-01 23:53:23', 48);
INSERT INTO `sys_oper_log` VALUES (119, '商品（服务）类型管理', 3, 'com.axin.web.controller.errand.ProductTypeController.remove()', 'DELETE', 1, 'admin', '研发部门', '/errand/productType/1', '127.0.0.1', '内网IP', '[1]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-02 00:01:03', 32);
INSERT INTO `sys_oper_log` VALUES (120, '商品（服务）类型管理', 1, 'com.axin.web.controller.errand.ProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"sss\",\"isEnabled\":0,\"isManageable\":0,\"typeDesc\":\"测试\",\"typeName\":\"测试1\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-02 00:05:29', 187);
INSERT INTO `sys_oper_log` VALUES (121, '小区管理', 1, 'com.axin.web.controller.errand.CommunityController.add()', 'POST', 1, 'admin', '研发部门', '/errand/community', '127.0.0.1', '内网IP', '{\"address\":\"111\",\"communityName\":\"测试\",\"contactPerson\":\"11\",\"contactPhone\":\"11\",\"isEnabled\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-02 00:05:42', 28);
INSERT INTO `sys_oper_log` VALUES (122, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":2,\"isEnabled\":0,\"productTypeId\":2}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-02 00:05:50', 19);
INSERT INTO `sys_oper_log` VALUES (123, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":6,\"isEnabled\":0,\"productTypeId\":2}', NULL, 1, '\r\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot add or update a child row: a foreign key constraint fails (`backend`.`community_product_type`, CONSTRAINT `fk_community` FOREIGN KEY (`community_id`) REFERENCES `community` (`id`) ON DELETE CASCADE)\r\n### The error may exist in file [D:\\Working\\Framework\\Backend\\Backend-system\\target\\classes\\mapper\\system\\CommunityProductTypeMapper.xml]\r\n### The error may involve com.axin.system.mapper.CommunityProductTypeMapper.insertCommunityProductType-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO community_product_type (community_id, product_type_id, is_enabled, create_time, update_time)         VALUES (?, ?, ?, NOW(), NOW())\r\n### Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot add or update a child row: a foreign key constraint fails (`backend`.`community_product_type`, CONSTRAINT `fk_community` FOREIGN KEY (`community_id`) REFERENCES `community` (`id`) ON DELETE CASCADE)\n; Cannot add or update a child row: a foreign key constraint fails (`backend`.`community_product_type`, CONSTRAINT `fk_community` FOREIGN KEY (`community_id`) REFERENCES `community` (`id`) ON DELETE CASCADE)', '2025-12-02 00:06:25', 147);
INSERT INTO `sys_oper_log` VALUES (124, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":3,\"isEnabled\":0,\"productTypeId\":2}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-02 00:06:37', 20);
INSERT INTO `sys_oper_log` VALUES (125, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":3,\"isEnabled\":0,\"productTypeId\":1}', NULL, 1, '\r\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot add or update a child row: a foreign key constraint fails (`backend`.`community_product_type`, CONSTRAINT `fk_community_type` FOREIGN KEY (`product_type_id`) REFERENCES `product_type` (`id`) ON DELETE CASCADE)\r\n### The error may exist in file [D:\\Working\\Framework\\Backend\\Backend-system\\target\\classes\\mapper\\system\\CommunityProductTypeMapper.xml]\r\n### The error may involve com.axin.system.mapper.CommunityProductTypeMapper.insertCommunityProductType-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO community_product_type (community_id, product_type_id, is_enabled, create_time, update_time)         VALUES (?, ?, ?, NOW(), NOW())\r\n### Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot add or update a child row: a foreign key constraint fails (`backend`.`community_product_type`, CONSTRAINT `fk_community_type` FOREIGN KEY (`product_type_id`) REFERENCES `product_type` (`id`) ON DELETE CASCADE)\n; Cannot add or update a child row: a foreign key constraint fails (`backend`.`community_product_type`, CONSTRAINT `fk_community_type` FOREIGN KEY (`product_type_id`) REFERENCES `product_type` (`id`) ON DELETE CASCADE)', '2025-12-02 00:06:41', 14);
INSERT INTO `sys_oper_log` VALUES (126, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":3,\"isEnabled\":0,\"productTypeId\":2}', '{\"msg\":\"新增失败：该小区与该类型已关联\",\"code\":500}', 0, NULL, '2025-12-02 00:06:46', 5);
INSERT INTO `sys_oper_log` VALUES (127, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":2,\"isEnabled\":0,\"productTypeId\":2}', '{\"msg\":\"新增失败：该小区与该类型已关联\",\"code\":500}', 0, NULL, '2025-12-02 00:06:55', 5);
INSERT INTO `sys_oper_log` VALUES (128, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":1,\"isEnabled\":0,\"productTypeId\":2}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-02 00:07:00', 22);
INSERT INTO `sys_oper_log` VALUES (129, '小区-商品类型关联', 2, 'com.axin.web.controller.errand.CommunityProductTypeController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":1,\"id\":1,\"isEnabled\":1,\"productTypeId\":2}', '{\"msg\":\"修改失败：该小区与该类型已关联\",\"code\":500}', 0, NULL, '2025-12-02 00:07:32', 15);
INSERT INTO `sys_oper_log` VALUES (130, '小区-商品类型关联', 2, 'com.axin.web.controller.errand.CommunityProductTypeController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"id\":1,\"isEnabled\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-02 00:07:45', 32);
INSERT INTO `sys_oper_log` VALUES (131, '小区-商品类型关联', 3, 'com.axin.web.controller.errand.CommunityProductTypeController.remove()', 'DELETE', 1, 'admin', '研发部门', '/errand/communityProductType/1', '127.0.0.1', '内网IP', '[1]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-02 00:08:45', 19);
INSERT INTO `sys_oper_log` VALUES (132, '小区-商品类型关联', 3, 'com.axin.web.controller.errand.CommunityProductTypeController.remove()', 'DELETE', 1, 'admin', '研发部门', '/errand/communityProductType/2', '127.0.0.1', '内网IP', '[2]', '{\"msg\":\"操作失败\",\"code\":500}', 0, NULL, '2025-12-02 00:08:46', 3);
INSERT INTO `sys_oper_log` VALUES (133, '小区-商品类型关联', 3, 'com.axin.web.controller.errand.CommunityProductTypeController.remove()', 'DELETE', 1, 'admin', '研发部门', '/errand/communityProductType/3', '127.0.0.1', '内网IP', '[3]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-02 00:08:48', 16);
INSERT INTO `sys_oper_log` VALUES (134, '小区-商品类型关联', 3, 'com.axin.web.controller.errand.CommunityProductTypeController.remove()', 'DELETE', 1, 'admin', '研发部门', '/errand/communityProductType/5', '127.0.0.1', '内网IP', '[5]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-02 00:08:51', 19);
INSERT INTO `sys_oper_log` VALUES (135, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":1,\"isEnabled\":0,\"productTypeId\":2}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-02 00:08:58', 11);
INSERT INTO `sys_oper_log` VALUES (136, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":2,\"isEnabled\":0,\"productTypeId\":2}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-02 00:09:08', 12);
INSERT INTO `sys_oper_log` VALUES (137, '商品规格管理', 1, 'com.axin.web.controller.errand.ProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"\",\"isEnabled\":0,\"isUniversalCustom\":0,\"priceMax\":0,\"priceMin\":0,\"specName\":\"1\",\"typeId\":1,\"weightMax\":0,\"weightMin\":0}', NULL, 1, '\r\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot add or update a child row: a foreign key constraint fails (`backend`.`product`, CONSTRAINT `fk_product_type` FOREIGN KEY (`type_id`) REFERENCES `product_type` (`id`) ON DELETE CASCADE)\r\n### The error may exist in file [D:\\Working\\Framework\\Backend\\Backend-system\\target\\classes\\mapper\\system\\ProductMapper.xml]\r\n### The error may involve com.axin.system.mapper.ProductMapper.insertProduct-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO product (             type_id, spec_name, weight_min, weight_max, distance, amount_min, amount_max,             duration, duration_unit, price_min, price_max, is_universal_custom, is_enabled, create_time, update_time         ) VALUES (             ?, ?, ?, ?, ?, ?, ?,             ?, ?, ?, ?, ?, ?, NOW(), NOW()         )\r\n### Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot add or update a child row: a foreign key constraint fails (`backend`.`product`, CONSTRAINT `fk_product_type` FOREIGN KEY (`type_id`) REFERENCES `product_type` (`id`) ON DELETE CASCADE)\n; Cannot add or update a child row: a foreign key constraint fails (`backend`.`product`, CONSTRAINT `fk_product_type` FOREIGN KEY (`type_id`) REFERENCES `product_type` (`id`) ON DELETE CASCADE)', '2025-12-02 00:15:54', 16);
INSERT INTO `sys_oper_log` VALUES (138, '商品规格管理', 1, 'com.axin.web.controller.errand.ProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"\",\"isEnabled\":0,\"isUniversalCustom\":0,\"priceMax\":0,\"priceMin\":0,\"specName\":\"1\",\"typeId\":2,\"weightMax\":0,\"weightMin\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-02 00:16:35', 8);
INSERT INTO `sys_oper_log` VALUES (139, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"\",\"id\":1,\"isEnabled\":0,\"isUniversalCustom\":0,\"priceMax\":0,\"priceMin\":0,\"specName\":\"222\",\"weightMax\":0,\"weightMin\":0}', '{\"msg\":\"操作失败\",\"code\":500}', 0, NULL, '2025-12-02 00:16:49', 10);
INSERT INTO `sys_oper_log` VALUES (140, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"\",\"id\":1,\"isEnabled\":0,\"isUniversalCustom\":0,\"priceMax\":0,\"priceMin\":0,\"specName\":\"221112\",\"weightMax\":0,\"weightMin\":0}', '{\"msg\":\"操作失败\",\"code\":500}', 0, NULL, '2025-12-02 00:17:18', 4);
INSERT INTO `sys_oper_log` VALUES (141, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"\",\"id\":1,\"isEnabled\":0,\"isUniversalCustom\":0,\"priceMax\":0,\"priceMin\":0,\"specName\":\"\",\"typeId\":0,\"weightMax\":0,\"weightMin\":0}', '{\"msg\":\"操作失败\",\"code\":500}', 0, NULL, '2025-12-02 00:17:22', 5);
INSERT INTO `sys_oper_log` VALUES (142, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"\",\"id\":1,\"isEnabled\":0,\"isUniversalCustom\":0,\"priceMax\":0,\"priceMin\":0,\"specName\":\"\",\"typeId\":0,\"weightMax\":0,\"weightMin\":0}', '{\"msg\":\"操作失败\",\"code\":500}', 0, NULL, '2025-12-02 00:17:23', 3);
INSERT INTO `sys_oper_log` VALUES (143, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"\",\"id\":1,\"isEnabled\":0,\"isUniversalCustom\":0,\"priceMax\":0,\"priceMin\":0,\"specName\":\"123\",\"typeId\":0,\"weightMax\":0,\"weightMin\":0}', '{\"msg\":\"操作失败\",\"code\":500}', 0, NULL, '2025-12-02 00:17:27', 5);
INSERT INTO `sys_oper_log` VALUES (144, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"\",\"id\":1,\"isEnabled\":0,\"isUniversalCustom\":0,\"priceMax\":0,\"priceMin\":0,\"specName\":\"123\",\"typeId\":0,\"weightMax\":0,\"weightMin\":0}', '{\"msg\":\"操作失败\",\"code\":500}', 0, NULL, '2025-12-02 00:17:27', 5);
INSERT INTO `sys_oper_log` VALUES (145, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"\",\"id\":1,\"isEnabled\":0,\"isUniversalCustom\":0,\"priceMax\":0,\"priceMin\":0,\"specName\":\"123\",\"typeId\":111,\"weightMax\":0,\"weightMin\":0}', '{\"msg\":\"操作失败\",\"code\":500}', 0, NULL, '2025-12-02 00:17:30', 6);
INSERT INTO `sys_oper_log` VALUES (146, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"\",\"id\":1,\"isEnabled\":0,\"isUniversalCustom\":0,\"priceMax\":0,\"priceMin\":0,\"specName\":\"123\",\"typeId\":2,\"weightMax\":0,\"weightMin\":0}', '{\"msg\":\"操作失败\",\"code\":500}', 0, NULL, '2025-12-02 00:17:36', 4);
INSERT INTO `sys_oper_log` VALUES (147, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"\",\"id\":1,\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":0,\"priceMin\":0,\"specName\":\"123\",\"typeId\":2,\"weightMax\":0,\"weightMin\":0}', '{\"msg\":\"操作失败\",\"code\":500}', 0, NULL, '2025-12-02 00:17:41', 4);
INSERT INTO `sys_oper_log` VALUES (148, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"id\":1,\"isEnabled\":1,\"specName\":\"123\",\"typeId\":2}', '{\"msg\":\"操作失败\",\"code\":500}', 0, NULL, '2025-12-02 00:17:44', 5);
INSERT INTO `sys_oper_log` VALUES (149, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"id\":1,\"isEnabled\":1,\"specName\":\"123\",\"typeId\":2}', '{\"msg\":\"操作失败\",\"code\":500}', 0, NULL, '2025-12-02 00:17:50', 5);
INSERT INTO `sys_oper_log` VALUES (150, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"id\":2,\"isEnabled\":1,\"specName\":\"123\",\"typeId\":2}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-02 00:18:28', 20);
INSERT INTO `sys_oper_log` VALUES (151, '外卖店铺管理', 1, 'com.axin.web.controller.errand.TakeawayStoreController.add()', 'POST', 1, 'admin', '研发部门', '/errand/takeawayStore', '127.0.0.1', '内网IP', '{\"address\":\"\",\"avatarUrl\":\"\",\"communityId\":1,\"contact\":\"\",\"isEnabled\":0,\"storeName\":\"21\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-02 00:27:54', 30);
INSERT INTO `sys_oper_log` VALUES (152, '外卖店铺管理', 2, 'com.axin.web.controller.errand.TakeawayStoreController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/takeawayStore', '127.0.0.1', '内网IP', '{\"address\":\"\",\"avatarUrl\":\"\",\"communityId\":3,\"contact\":\"\",\"id\":1,\"isEnabled\":0,\"storeName\":\"\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-02 00:28:03', 12);
INSERT INTO `sys_oper_log` VALUES (153, '外卖店铺管理', 2, 'com.axin.web.controller.errand.TakeawayStoreController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/takeawayStore', '127.0.0.1', '内网IP', '{\"address\":\"\",\"avatarUrl\":\"\",\"communityId\":3,\"contact\":\"\",\"id\":1,\"isEnabled\":0,\"storeName\":\"222\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-02 00:28:21', 13);
INSERT INTO `sys_oper_log` VALUES (154, '外卖店铺管理', 3, 'com.axin.web.controller.errand.TakeawayStoreController.remove()', 'DELETE', 1, 'admin', '研发部门', '/errand/takeawayStore/2', '127.0.0.1', '内网IP', '[2]', '{\"msg\":\"操作失败\",\"code\":500}', 0, NULL, '2025-12-02 00:28:32', 7);
INSERT INTO `sys_oper_log` VALUES (155, '外卖店铺管理', 3, 'com.axin.web.controller.errand.TakeawayStoreController.remove()', 'DELETE', 1, 'admin', '研发部门', '/errand/takeawayStore/1', '127.0.0.1', '内网IP', '[1]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-02 00:28:34', 20);
INSERT INTO `sys_oper_log` VALUES (156, '外卖店铺管理', 1, 'com.axin.web.controller.errand.TakeawayStoreController.add()', 'POST', 1, 'admin', '研发部门', '/errand/takeawayStore', '127.0.0.1', '内网IP', '{\"address\":\"\",\"avatarUrl\":\"\",\"communityId\":1,\"contact\":\"\",\"isEnabled\":0,\"storeName\":\"21\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-02 00:49:13', 22);
INSERT INTO `sys_oper_log` VALUES (157, '外卖商品管理', 1, 'com.axin.web.controller.errand.TakeawayProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/takeawayProduct', '127.0.0.1', '内网IP', '{\"imageUrl\":\"\",\"isEnabled\":0,\"price\":0,\"productName\":\"123\",\"productType\":\"\",\"storeId\":2}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-02 00:49:27', 12);
INSERT INTO `sys_oper_log` VALUES (158, '外卖商品管理', 2, 'com.axin.web.controller.errand.TakeawayProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/takeawayProduct', '127.0.0.1', '内网IP', '{\"id\":1,\"imageUrl\":\"\",\"isEnabled\":0,\"price\":0,\"productName\":\"\",\"productType\":\"\",\"storeId\":0}', NULL, 1, '\r\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot add or update a child row: a foreign key constraint fails (`backend`.`takeaway_product`, CONSTRAINT `fk_takeaway_store` FOREIGN KEY (`store_id`) REFERENCES `takeaway_store` (`id`) ON DELETE CASCADE)\r\n### The error may exist in file [D:\\Working\\Framework\\Backend\\Backend-system\\target\\classes\\mapper\\system\\TakeawayProductMapper.xml]\r\n### The error may involve com.axin.system.mapper.TakeawayProductMapper.updateTakeawayProduct-Inline\r\n### The error occurred while setting parameters\r\n### SQL: UPDATE takeaway_product          SET store_id = ?,             product_name = ?,             price = ?,             image_url = ?,             product_type = ?,             is_enabled = ?,             update_time = NOW()          WHERE id = ?\r\n### Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot add or update a child row: a foreign key constraint fails (`backend`.`takeaway_product`, CONSTRAINT `fk_takeaway_store` FOREIGN KEY (`store_id`) REFERENCES `takeaway_store` (`id`) ON DELETE CASCADE)\n; Cannot add or update a child row: a foreign key constraint fails (`backend`.`takeaway_product`, CONSTRAINT `fk_takeaway_store` FOREIGN KEY (`store_id`) REFERENCES `takeaway_store` (`id`) ON DELETE CASCADE)', '2025-12-02 00:49:37', 21);
INSERT INTO `sys_oper_log` VALUES (159, '外卖商品管理', 2, 'com.axin.web.controller.errand.TakeawayProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/takeawayProduct', '127.0.0.1', '内网IP', '{\"id\":1,\"imageUrl\":\"\",\"isEnabled\":0,\"price\":0,\"productName\":\"333\",\"productType\":\"\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-02 00:49:43', 10);
INSERT INTO `sys_oper_log` VALUES (160, '便民超市商品管理', 1, 'com.axin.web.controller.errand.ConvenienceProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/convenienceProduct', '127.0.0.1', '内网IP', '{\"imageUrl\":\"\",\"isEnabled\":0,\"price\":0,\"productName\":\"111\",\"productType\":\"\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-02 22:15:50', 186);
INSERT INTO `sys_oper_log` VALUES (161, '便民超市商品管理', 2, 'com.axin.web.controller.errand.ConvenienceProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/convenienceProduct', '127.0.0.1', '内网IP', '{\"id\":1,\"imageUrl\":\"\",\"isEnabled\":0,\"price\":10000,\"productName\":\"\",\"productType\":\"\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-02 22:41:37', 52);
INSERT INTO `sys_oper_log` VALUES (162, '服务套餐管理', 1, 'com.axin.web.controller.errand.ServicePackageController.add()', 'POST', 1, 'admin', '研发部门', '/errand/servicePackage', '127.0.0.1', '内网IP', '{\"durationDays\":0,\"durationType\":0,\"firstMonthPrice\":0,\"includeService\":\"\",\"isEnabled\":0,\"packageName\":\"1\",\"price\":12220,\"typeId\":2}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-02 23:31:45', 212);
INSERT INTO `sys_oper_log` VALUES (163, '菜单管理', 3, 'com.axin.web.controller.system.SysMenuController.remove()', 'DELETE', 1, 'admin', '研发部门', '/system/menu/2000', '127.0.0.1', '内网IP', '2000', '{\"msg\":\"存在子菜单,不允许删除\",\"code\":601}', 0, NULL, '2025-12-03 21:59:47', 15);
INSERT INTO `sys_oper_log` VALUES (164, '菜单管理', 3, 'com.axin.web.controller.system.SysMenuController.remove()', 'DELETE', 1, 'admin', '研发部门', '/system/menu/2001', '127.0.0.1', '内网IP', '2001', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-03 21:59:50', 19);
INSERT INTO `sys_oper_log` VALUES (165, '菜单管理', 3, 'com.axin.web.controller.system.SysMenuController.remove()', 'DELETE', 1, 'admin', '研发部门', '/system/menu/2000', '127.0.0.1', '内网IP', '2000', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-03 21:59:52', 27);
INSERT INTO `sys_oper_log` VALUES (166, '小区管理', 2, 'com.axin.web.controller.errand.CommunityController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/community', '127.0.0.1', '内网IP', '{\"address\":\"111\",\"communityName\":\"测试\",\"contactPerson\":\"11\",\"contactPhone\":\"11\",\"id\":5,\"isEnabled\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-03 22:27:11', 16);
INSERT INTO `sys_oper_log` VALUES (167, '小区管理', 2, 'com.axin.web.controller.errand.CommunityController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/community', '127.0.0.1', '内网IP', '{\"address\":\"111\",\"communityName\":\"测试\",\"contactPerson\":\"11\",\"contactPhone\":\"11\",\"id\":5,\"isEnabled\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-03 22:27:17', 15);
INSERT INTO `sys_oper_log` VALUES (168, '小区管理', 3, 'com.axin.web.controller.errand.CommunityController.remove()', 'DELETE', 1, 'admin', '研发部门', '/errand/community/5', '127.0.0.1', '内网IP', '[5]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-03 22:58:10', 23);
INSERT INTO `sys_oper_log` VALUES (169, '商品（服务）类型管理', 2, 'com.axin.web.controller.errand.ProductTypeController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"sss\",\"id\":2,\"isEnabled\":0,\"isManageable\":1,\"typeDesc\":\"测试\",\"typeName\":\"测试1\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-03 23:21:54', 14);
INSERT INTO `sys_oper_log` VALUES (170, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":4,\"isEnabled\":0,\"productTypeId\":2,\"sortOrder\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-03 23:29:34', 32);
INSERT INTO `sys_oper_log` VALUES (171, '小区管理', 2, 'com.axin.web.controller.errand.CommunityController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/community', '127.0.0.1', '内网IP', '{\"address\":\"广东省广州市花都区1\",\"communityName\":\"测试小区13\",\"contactPerson\":\"张三\",\"contactPhone\":\"17876118798\",\"id\":4,\"isEnabled\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-03 23:30:50', 19);
INSERT INTO `sys_oper_log` VALUES (172, '商品（服务）类型管理', 2, 'com.axin.web.controller.errand.ProductTypeController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"http://localhost:8080/profile/upload/2025/12/03/屏幕截图 2025-11-15 230141_20251203233857A001.png\",\"id\":2,\"isEnabled\":1,\"isManageable\":1,\"typeDesc\":\"测试\",\"typeName\":\"测试1\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-03 23:38:59', 12);
INSERT INTO `sys_oper_log` VALUES (173, '商品（服务）类型管理', 2, 'com.axin.web.controller.errand.ProductTypeController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"/profile/upload/2025/12/03/屏幕截图 2025-11-16 181413_20251203234128A002.png\",\"id\":2,\"isEnabled\":1,\"isManageable\":1,\"typeDesc\":\"测试\",\"typeName\":\"测试1\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-03 23:41:30', 21);
INSERT INTO `sys_oper_log` VALUES (174, '商品规格管理', 1, 'com.axin.web.controller.errand.ProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":100,\"amountMin\":100,\"distance\":1,\"duration\":4,\"durationUnit\":\"1\",\"isEnabled\":1,\"isUniversalCustom\":1,\"priceMax\":100,\"priceMin\":100,\"specName\":\"111\",\"typeId\":2,\"weightMax\":1,\"weightMin\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-03 23:54:02', 36);
INSERT INTO `sys_oper_log` VALUES (175, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":100,\"amountMin\":100,\"distance\":1,\"duration\":4,\"durationUnit\":\"1\",\"id\":3,\"isEnabled\":1,\"isUniversalCustom\":1,\"priceMax\":100,\"priceMin\":100,\"specName\":\"111\",\"typeId\":2,\"weightMax\":11,\"weightMin\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-03 23:59:52', 27);
INSERT INTO `sys_oper_log` VALUES (176, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":100,\"amountMin\":100,\"distance\":1,\"duration\":4,\"durationUnit\":\"1\",\"id\":3,\"isEnabled\":0,\"isUniversalCustom\":0,\"priceMax\":100,\"priceMin\":100,\"specName\":\"111\",\"typeId\":2,\"weightMax\":11,\"weightMin\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-03 23:59:59', 20);
INSERT INTO `sys_oper_log` VALUES (177, '外卖店铺管理', 2, 'com.axin.web.controller.errand.TakeawayStoreController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/takeawayStore', '127.0.0.1', '内网IP', '{\"address\":\"1111\",\"avatarUrl\":\"/profile/upload/2025/12/04/屏幕截图 2025-11-15 230141_20251204001455A003.png\",\"communityId\":1,\"contact\":\"1111111\",\"id\":2,\"isEnabled\":1,\"storeName\":\"21\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 00:15:01', 14);
INSERT INTO `sys_oper_log` VALUES (178, '外卖商品管理', 1, 'com.axin.web.controller.errand.TakeawayProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/takeawayProduct', '127.0.0.1', '内网IP', '{\"imageUrl\":\"/profile/upload/2025/12/04/屏幕截图 2025-07-22 102045_20251204001806A004.png\",\"isEnabled\":0,\"price\":11111100,\"productName\":\"111111\",\"productType\":\"111\",\"storeId\":2}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 00:18:11', 10);
INSERT INTO `sys_oper_log` VALUES (179, '外卖商品管理', 2, 'com.axin.web.controller.errand.TakeawayProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/takeawayProduct', '127.0.0.1', '内网IP', '{\"id\":2,\"imageUrl\":\"/profile/upload/2025/12/04/屏幕截图 2025-07-22 102045_20251204001806A004.png\",\"isEnabled\":1,\"price\":11111100,\"productName\":\"111111\",\"productType\":\"111\",\"storeId\":2}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 00:18:16', 19);
INSERT INTO `sys_oper_log` VALUES (180, '外卖商品管理', 2, 'com.axin.web.controller.errand.TakeawayProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/takeawayProduct', '127.0.0.1', '内网IP', '{\"id\":1,\"imageUrl\":\"\",\"isEnabled\":1,\"price\":0,\"productName\":\"333\",\"productType\":\"\",\"storeId\":2}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 00:18:20', 9);
INSERT INTO `sys_oper_log` VALUES (181, '便民超市商品管理', 1, 'com.axin.web.controller.errand.ConvenienceProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/convenienceProduct', '127.0.0.1', '内网IP', '{\"imageUrl\":\"/profile/upload/2025/12/04/屏幕截图 2025-07-21 155331_20251204002301A005.png\",\"isEnabled\":1,\"price\":11100,\"productName\":\"111\",\"productType\":\"11\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 00:23:06', 13);
INSERT INTO `sys_oper_log` VALUES (182, '便民超市商品管理', 2, 'com.axin.web.controller.errand.ConvenienceProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/convenienceProduct', '127.0.0.1', '内网IP', '{\"id\":2,\"imageUrl\":\"/profile/upload/2025/12/04/屏幕截图 2025-07-21 155331_20251204002301A005.png\",\"isEnabled\":1,\"price\":11100,\"productName\":\"111\",\"productType\":\"1111\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 00:23:11', 24);
INSERT INTO `sys_oper_log` VALUES (183, '便民超市商品管理', 2, 'com.axin.web.controller.errand.ConvenienceProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/convenienceProduct', '127.0.0.1', '内网IP', '{\"id\":2,\"imageUrl\":\"/profile/upload/2025/12/04/屏幕截图 2025-07-21 155331_20251204002301A005.png\",\"isEnabled\":1,\"price\":11111100,\"productName\":\"111\",\"productType\":\"1111\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 00:23:15', 13);
INSERT INTO `sys_oper_log` VALUES (184, '便民超市商品管理', 3, 'com.axin.web.controller.errand.ConvenienceProductController.remove()', 'DELETE', 1, 'admin', '研发部门', '/errand/convenienceProduct/2', '127.0.0.1', '内网IP', '[2]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 00:23:17', 11);
INSERT INTO `sys_oper_log` VALUES (185, '服务套餐管理', 2, 'com.axin.web.controller.errand.ServicePackageController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/servicePackage', '127.0.0.1', '内网IP', '{\"durationDays\":0,\"durationType\":1,\"firstMonthPrice\":0,\"id\":1,\"includeService\":\"\",\"isEnabled\":0,\"packageName\":\"1\",\"price\":12220,\"typeId\":2}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 00:24:43', 15);
INSERT INTO `sys_oper_log` VALUES (186, '服务套餐管理', 2, 'com.axin.web.controller.errand.ServicePackageController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/servicePackage', '127.0.0.1', '内网IP', '{\"durationDays\":0,\"durationType\":3,\"firstMonthPrice\":0,\"id\":1,\"includeService\":\"\",\"isEnabled\":0,\"packageName\":\"1\",\"price\":12220,\"typeId\":2}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 00:24:46', 10);
INSERT INTO `sys_oper_log` VALUES (187, '服务套餐管理', 2, 'com.axin.web.controller.errand.ServicePackageController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/servicePackage', '127.0.0.1', '内网IP', '{\"durationDays\":0,\"durationType\":1,\"firstMonthPrice\":0,\"id\":1,\"includeService\":\"\",\"isEnabled\":0,\"packageName\":\"1\",\"price\":12220,\"typeId\":2}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 00:26:12', 21);
INSERT INTO `sys_oper_log` VALUES (188, '服务套餐管理', 2, 'com.axin.web.controller.errand.ServicePackageController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/servicePackage', '127.0.0.1', '内网IP', '{\"durationDays\":0,\"durationType\":2,\"firstMonthPrice\":0,\"id\":1,\"includeService\":\"\",\"isEnabled\":0,\"packageName\":\"1\",\"price\":12220,\"typeId\":2}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 00:26:16', 12);
INSERT INTO `sys_oper_log` VALUES (189, '小区管理', 3, 'com.axin.web.controller.errand.CommunityController.remove()', 'DELETE', 1, 'admin', '研发部门', '/errand/community/4,3,2,1', '127.0.0.1', '内网IP', '[4,3,2,1]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 23:34:12', 24);
INSERT INTO `sys_oper_log` VALUES (190, '小区管理', 1, 'com.axin.web.controller.errand.CommunityController.add()', 'POST', 1, 'admin', '研发部门', '/errand/community', '127.0.0.1', '内网IP', '{\"address\":\"广东省广州市花都区光明小区\",\"communityName\":\"光明小区\",\"contactPerson\":\"张三\",\"contactPhone\":\"17875114193\",\"isEnabled\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 23:34:34', 24);
INSERT INTO `sys_oper_log` VALUES (191, '小区管理', 1, 'com.axin.web.controller.errand.CommunityController.add()', 'POST', 1, 'admin', '研发部门', '/errand/community', '127.0.0.1', '内网IP', '{\"address\":\"广东省广州市白云区碧桂园\",\"communityName\":\"碧桂园\",\"contactPerson\":\"李四\",\"contactPhone\":\"17898889101\",\"isEnabled\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 23:35:16', 25);
INSERT INTO `sys_oper_log` VALUES (192, '小区管理', 2, 'com.axin.web.controller.errand.CommunityController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/community', '127.0.0.1', '内网IP', '{\"address\":\"广东省广州市花都区光明小区\",\"communityName\":\"光明小区\",\"contactPerson\":\"张三\",\"contactPhone\":\"17875132132\",\"id\":6,\"isEnabled\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 23:35:27', 27);
INSERT INTO `sys_oper_log` VALUES (193, '小区管理', 2, 'com.axin.web.controller.errand.CommunityController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/community', '127.0.0.1', '内网IP', '{\"address\":\"广东省广州市花都区光明小区\",\"communityName\":\"光明小区\",\"contactPerson\":\"张三\",\"contactPhone\":\"17875132132\",\"id\":6,\"isEnabled\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 23:35:32', 24);
INSERT INTO `sys_oper_log` VALUES (194, '商品（服务）类型管理', 3, 'com.axin.web.controller.errand.ProductTypeController.remove()', 'DELETE', 1, 'admin', '研发部门', '/errand/productType/2', '127.0.0.1', '内网IP', '[2]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 23:35:50', 25);
INSERT INTO `sys_oper_log` VALUES (195, '商品（服务）类型管理', 1, 'com.axin.web.controller.errand.ProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"/profile/upload/2025/12/04/服务图标_20251204235226A001.png\",\"isEnabled\":1,\"isManageable\":1,\"typeDesc\":\"可代拿附近快递，备注填写取件码\",\"typeName\":\"代拿快递\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 23:52:31', 31);
INSERT INTO `sys_oper_log` VALUES (196, '商品（服务）类型管理', 1, 'com.axin.web.controller.errand.ProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"/profile/upload/2025/12/04/服务图标_20251204235316A002.png\",\"isEnabled\":1,\"isManageable\":1,\"typeDesc\":\"\",\"typeName\":\"代扔垃圾\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 23:53:18', 11);
INSERT INTO `sys_oper_log` VALUES (197, '商品（服务）类型管理', 1, 'com.axin.web.controller.errand.ProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"/profile/upload/2025/12/04/服务图标_20251204235350A003.png\",\"isEnabled\":1,\"isManageable\":1,\"typeDesc\":\"备注里填写你需要买的菜\",\"typeName\":\"代买菜\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 23:53:52', 20);
INSERT INTO `sys_oper_log` VALUES (198, '商品（服务）类型管理', 1, 'com.axin.web.controller.errand.ProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"/profile/upload/2025/12/04/服务图标_20251204235417A004.png\",\"isEnabled\":1,\"isManageable\":1,\"typeDesc\":\"备注里填写要配送的物品\",\"typeName\":\"代送物品\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 23:54:18', 18);
INSERT INTO `sys_oper_log` VALUES (199, '商品（服务）类型管理', 1, 'com.axin.web.controller.errand.ProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"/profile/upload/2025/12/04/服务图标_20251204235437A005.png\",\"isEnabled\":1,\"isManageable\":1,\"typeDesc\":\"备注里填写要买的物品\",\"typeName\":\"代买物品\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 23:54:38', 21);
INSERT INTO `sys_oper_log` VALUES (200, '商品（服务）类型管理', 1, 'com.axin.web.controller.errand.ProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"/profile/upload/2025/12/04/服务图标_20251204235512A006.png\",\"isEnabled\":1,\"isManageable\":1,\"typeDesc\":\"可选择你需要的其他类型服务\",\"typeName\":\"万能服务\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 23:55:14', 12);
INSERT INTO `sys_oper_log` VALUES (201, '商品（服务）类型管理', 1, 'com.axin.web.controller.errand.ProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"/profile/upload/2025/12/04/服务图标_20251204235544A007.png\",\"isEnabled\":1,\"isManageable\":1,\"typeDesc\":\"备注里填写打印规格\",\"typeName\":\"代打印\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 23:55:45', 11);
INSERT INTO `sys_oper_log` VALUES (202, '商品（服务）类型管理', 1, 'com.axin.web.controller.errand.ProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"/profile/upload/2025/12/04/服务图标_20251204235618A008.png\",\"isEnabled\":1,\"isManageable\":1,\"typeDesc\":\"使用自家私家车接送\",\"typeName\":\"代送小孩\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 23:56:20', 22);
INSERT INTO `sys_oper_log` VALUES (203, '商品（服务）类型管理', 1, 'com.axin.web.controller.errand.ProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"/profile/upload/2025/12/04/服务图标_20251204235645A009.png\",\"isEnabled\":1,\"isManageable\":1,\"typeDesc\":\"可寄存行李箱，衣物等物品\",\"typeName\":\"物品寄存\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 23:56:53', 22);
INSERT INTO `sys_oper_log` VALUES (204, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":7,\"isEnabled\":1,\"productTypeId\":11,\"sortOrder\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 23:57:13', 24);
INSERT INTO `sys_oper_log` VALUES (205, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":7,\"isEnabled\":1,\"productTypeId\":10,\"sortOrder\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 23:57:16', 13);
INSERT INTO `sys_oper_log` VALUES (206, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":7,\"isEnabled\":1,\"productTypeId\":9,\"sortOrder\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 23:57:20', 22);
INSERT INTO `sys_oper_log` VALUES (207, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":7,\"isEnabled\":1,\"productTypeId\":11,\"sortOrder\":0}', '{\"msg\":\"新增失败：该小区与该类型已关联\",\"code\":500}', 0, NULL, '2025-12-04 23:57:24', 5);
INSERT INTO `sys_oper_log` VALUES (208, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":7,\"isEnabled\":1,\"productTypeId\":8,\"sortOrder\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 23:57:27', 21);
INSERT INTO `sys_oper_log` VALUES (209, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":7,\"isEnabled\":1,\"productTypeId\":7,\"sortOrder\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 23:57:31', 10);
INSERT INTO `sys_oper_log` VALUES (210, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":7,\"isEnabled\":1,\"productTypeId\":6,\"sortOrder\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 23:57:34', 14);
INSERT INTO `sys_oper_log` VALUES (211, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":7,\"isEnabled\":1,\"productTypeId\":5,\"sortOrder\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 23:57:38', 12);
INSERT INTO `sys_oper_log` VALUES (212, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":7,\"isEnabled\":1,\"productTypeId\":4,\"sortOrder\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 23:57:42', 19);
INSERT INTO `sys_oper_log` VALUES (213, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":7,\"isEnabled\":1,\"productTypeId\":3,\"sortOrder\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-04 23:57:46', 12);
INSERT INTO `sys_oper_log` VALUES (214, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":7,\"isEnabled\":1,\"productTypeId\":10,\"sortOrder\":0}', '{\"msg\":\"新增失败：该小区与该类型已关联\",\"code\":500}', 0, NULL, '2025-12-04 23:57:56', 4);
INSERT INTO `sys_oper_log` VALUES (215, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":7,\"isEnabled\":1,\"productTypeId\":11,\"sortOrder\":0}', '{\"msg\":\"新增失败：该小区与该类型已关联\",\"code\":500}', 0, NULL, '2025-12-04 23:57:58', 5);
INSERT INTO `sys_oper_log` VALUES (216, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":7,\"isEnabled\":1,\"productTypeId\":9,\"sortOrder\":0}', '{\"msg\":\"新增失败：该小区与该类型已关联\",\"code\":500}', 0, NULL, '2025-12-04 23:58:00', 3);
INSERT INTO `sys_oper_log` VALUES (217, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":7,\"isEnabled\":1,\"productTypeId\":8,\"sortOrder\":0}', '{\"msg\":\"新增失败：该小区与该类型已关联\",\"code\":500}', 0, NULL, '2025-12-04 23:58:02', 2);
INSERT INTO `sys_oper_log` VALUES (218, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":7,\"isEnabled\":1,\"productTypeId\":7,\"sortOrder\":0}', '{\"msg\":\"新增失败：该小区与该类型已关联\",\"code\":500}', 0, NULL, '2025-12-04 23:58:04', 4);
INSERT INTO `sys_oper_log` VALUES (219, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":7,\"isEnabled\":1,\"productTypeId\":6,\"sortOrder\":0}', '{\"msg\":\"新增失败：该小区与该类型已关联\",\"code\":500}', 0, NULL, '2025-12-04 23:58:07', 7);
INSERT INTO `sys_oper_log` VALUES (220, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":7,\"isEnabled\":1,\"productTypeId\":5,\"sortOrder\":0}', '{\"msg\":\"新增失败：该小区与该类型已关联\",\"code\":500}', 0, NULL, '2025-12-04 23:58:09', 5);
INSERT INTO `sys_oper_log` VALUES (221, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":7,\"isEnabled\":1,\"productTypeId\":4,\"sortOrder\":0}', '{\"msg\":\"新增失败：该小区与该类型已关联\",\"code\":500}', 0, NULL, '2025-12-04 23:58:11', 3);
INSERT INTO `sys_oper_log` VALUES (222, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":7,\"isEnabled\":1,\"productTypeId\":3,\"sortOrder\":0}', '{\"msg\":\"新增失败：该小区与该类型已关联\",\"code\":500}', 0, NULL, '2025-12-04 23:58:13', 3);
INSERT INTO `sys_oper_log` VALUES (223, '商品规格管理', 1, 'com.axin.web.controller.errand.ProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":90,\"priceMin\":0,\"specName\":\"超小件\",\"typeId\":3,\"weightMax\":0.5,\"weightMin\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 00:00:01', 35);
INSERT INTO `sys_oper_log` VALUES (224, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"id\":4,\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":900,\"priceMin\":0,\"specName\":\"超小件\",\"typeId\":3,\"weightMax\":0.5,\"weightMin\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 00:00:28', 18);
INSERT INTO `sys_oper_log` VALUES (225, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"id\":4,\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":0,\"priceMin\":0,\"specName\":\"超小件\",\"typeId\":3,\"weightMax\":0.5,\"weightMin\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 00:01:01', 23);
INSERT INTO `sys_oper_log` VALUES (226, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"0\",\"id\":4,\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":900,\"priceMin\":0,\"specName\":\"超小件\",\"typeId\":3,\"weightMax\":0.5,\"weightMin\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 00:01:14', 22);
INSERT INTO `sys_oper_log` VALUES (227, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"0\",\"id\":4,\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":90,\"priceMin\":0,\"specName\":\"超小件\",\"typeId\":3,\"weightMax\":0.5,\"weightMin\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 00:01:19', 21);
INSERT INTO `sys_oper_log` VALUES (228, '商品规格管理', 1, 'com.axin.web.controller.errand.ProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"0\",\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":16000,\"priceMin\":9000,\"specName\":\"小件\",\"typeId\":3,\"weightMax\":1.5,\"weightMin\":0.5}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 00:07:24', 25);
INSERT INTO `sys_oper_log` VALUES (229, '商品规格管理', 1, 'com.axin.web.controller.errand.ProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"0\",\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":25000,\"priceMin\":16000,\"specName\":\"中件\",\"typeId\":3,\"weightMax\":2.5,\"weightMin\":1.5}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 00:07:57', 15);
INSERT INTO `sys_oper_log` VALUES (230, '商品规格管理', 1, 'com.axin.web.controller.errand.ProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"0\",\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":40000,\"priceMin\":25000,\"specName\":\"大件\",\"typeId\":3,\"weightMax\":8,\"weightMin\":2.5}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 00:09:04', 24);
INSERT INTO `sys_oper_log` VALUES (231, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"0\",\"id\":7,\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":40000,\"priceMin\":0,\"specName\":\"大件\",\"typeId\":3,\"weightMax\":8,\"weightMin\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 00:13:42', 23);
INSERT INTO `sys_oper_log` VALUES (232, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"0\",\"id\":6,\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":25000,\"priceMin\":0,\"specName\":\"中件\",\"typeId\":3,\"weightMax\":2.5,\"weightMin\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 00:13:52', 14);
INSERT INTO `sys_oper_log` VALUES (233, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"0\",\"id\":5,\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":16000,\"priceMin\":0,\"specName\":\"小件\",\"typeId\":3,\"weightMax\":1.5,\"weightMin\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 00:13:57', 14);
INSERT INTO `sys_oper_log` VALUES (234, '商品规格管理', 1, 'com.axin.web.controller.errand.ProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"0\",\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":9000,\"priceMin\":0,\"specName\":\"普通垃圾塑料袋\",\"typeId\":4,\"weightMax\":0,\"weightMin\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 00:14:44', 20);
INSERT INTO `sys_oper_log` VALUES (235, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"0\",\"id\":7,\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":400,\"priceMin\":0,\"specName\":\"大件\",\"typeId\":3,\"weightMax\":8,\"weightMin\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 00:16:40', 24);
INSERT INTO `sys_oper_log` VALUES (236, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"0\",\"id\":6,\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":250,\"priceMin\":0,\"specName\":\"中件\",\"typeId\":3,\"weightMax\":2.5,\"weightMin\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 00:16:53', 21);
INSERT INTO `sys_oper_log` VALUES (237, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"0\",\"id\":5,\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":160,\"priceMin\":0,\"specName\":\"小件\",\"typeId\":3,\"weightMax\":1.5,\"weightMin\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 00:16:57', 21);
INSERT INTO `sys_oper_log` VALUES (238, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"0\",\"id\":8,\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":90,\"priceMin\":0,\"specName\":\"普通垃圾塑料袋\",\"typeId\":4,\"weightMax\":0,\"weightMin\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 00:17:22', 14);
INSERT INTO `sys_oper_log` VALUES (239, '商品规格管理', 1, 'com.axin.web.controller.errand.ProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"0\",\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":190,\"priceMin\":0,\"specName\":\"箱子规格\",\"typeId\":4,\"weightMax\":0,\"weightMin\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 00:17:56', 22);
INSERT INTO `sys_oper_log` VALUES (240, '商品规格管理', 1, 'com.axin.web.controller.errand.ProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":2000,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"0\",\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":2400,\"priceMin\":0,\"specName\":\"预算20\",\"typeId\":5,\"weightMax\":0,\"weightMin\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 00:19:16', 12);
INSERT INTO `sys_oper_log` VALUES (241, '商品规格管理', 1, 'com.axin.web.controller.errand.ProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":4000,\"amountMin\":2000,\"distance\":0,\"duration\":0,\"durationUnit\":\"0\",\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":4600,\"priceMin\":0,\"specName\":\"预算40\",\"typeId\":5,\"weightMax\":0,\"weightMin\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 00:19:42', 21);
INSERT INTO `sys_oper_log` VALUES (242, '商品规格管理', 1, 'com.axin.web.controller.errand.ProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":1,\"duration\":0,\"durationUnit\":\"0\",\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":300,\"priceMin\":0,\"specName\":\"1km内\",\"typeId\":6,\"weightMax\":3,\"weightMin\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 00:21:04', 16);
INSERT INTO `sys_oper_log` VALUES (243, '商品规格管理', 1, 'com.axin.web.controller.errand.ProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"0\",\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":350,\"priceMin\":0,\"specName\":\"2km内\",\"typeId\":6,\"weightMax\":3,\"weightMin\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 00:21:52', 20);
INSERT INTO `sys_oper_log` VALUES (244, '商品规格管理', 1, 'com.axin.web.controller.errand.ProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"0\",\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":400,\"priceMin\":0,\"specName\":\"3km内\",\"typeId\":6,\"weightMax\":3,\"weightMin\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 00:22:09', 20);
INSERT INTO `sys_oper_log` VALUES (245, '商品规格管理', 1, 'com.axin.web.controller.errand.ProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"0\",\"isEnabled\":1,\"isUniversalCustom\":1,\"priceMax\":0,\"priceMin\":0,\"specName\":\"雨天接送\",\"typeId\":8,\"weightMax\":0,\"weightMin\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 00:24:52', 19);
INSERT INTO `sys_oper_log` VALUES (246, '商品规格管理', 1, 'com.axin.web.controller.errand.ProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"0\",\"isEnabled\":1,\"isUniversalCustom\":1,\"priceMax\":0,\"priceMin\":0,\"specName\":\"喂宠物\",\"typeId\":8,\"weightMax\":0,\"weightMin\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 00:25:06', 19);
INSERT INTO `sys_oper_log` VALUES (247, '商品规格管理', 1, 'com.axin.web.controller.errand.ProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"00\",\"isEnabled\":1,\"isUniversalCustom\":1,\"priceMax\":0,\"priceMin\":0,\"specName\":\"家政服务\",\"typeId\":8,\"weightMax\":0,\"weightMin\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 00:25:24', 20);
INSERT INTO `sys_oper_log` VALUES (248, '商品规格管理', 1, 'com.axin.web.controller.errand.ProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":100,\"priceMin\":100,\"specName\":\"A4打印\",\"typeId\":9}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 00:26:15', 20);
INSERT INTO `sys_oper_log` VALUES (249, '商品规格管理', 1, 'com.axin.web.controller.errand.ProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":100,\"priceMin\":100,\"specName\":\"A3打印\",\"typeId\":9}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 00:26:26', 21);
INSERT INTO `sys_oper_log` VALUES (250, '外卖店铺管理', 3, 'com.axin.web.controller.errand.TakeawayStoreController.remove()', 'DELETE', 1, 'admin', '研发部门', '/errand/takeawayStore/2', '127.0.0.1', '内网IP', '[2]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 23:29:59', 25);
INSERT INTO `sys_oper_log` VALUES (251, '外卖店铺管理', 1, 'com.axin.web.controller.errand.TakeawayStoreController.add()', 'POST', 1, 'admin', '研发部门', '/errand/takeawayStore', '127.0.0.1', '内网IP', '{\"avatarUrl\":\"/profile/upload/2025/12/05/服务图标_20251205233033A001.png\",\"communityId\":7,\"isEnabled\":1,\"storeName\":\"麻辣烫\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 23:30:39', 31);
INSERT INTO `sys_oper_log` VALUES (252, '外卖商品管理', 1, 'com.axin.web.controller.errand.TakeawayProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/takeawayProduct', '127.0.0.1', '内网IP', '{\"imageUrl\":\"/profile/upload/2025/12/05/服务图标_20251205233103A002.png\",\"isEnabled\":1,\"price\":1200,\"productName\":\"小份麻辣烫\",\"productType\":\"超值套餐\",\"storeId\":3}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 23:31:18', 22);
INSERT INTO `sys_oper_log` VALUES (253, '外卖商品管理', 1, 'com.axin.web.controller.errand.TakeawayProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/takeawayProduct', '127.0.0.1', '内网IP', '{\"imageUrl\":\"/profile/upload/2025/12/05/服务图标_20251205233132A003.png\",\"isEnabled\":1,\"price\":1400,\"productName\":\"中份麻辣烫\",\"productType\":\"超值套餐\",\"storeId\":3}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 23:31:37', 19);
INSERT INTO `sys_oper_log` VALUES (254, '外卖商品管理', 1, 'com.axin.web.controller.errand.TakeawayProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/takeawayProduct', '127.0.0.1', '内网IP', '{\"imageUrl\":\"/profile/upload/2025/12/05/服务图标_20251205233154A004.png\",\"isEnabled\":1,\"price\":1500,\"productName\":\"云吞\",\"productType\":\"汤粉类\",\"storeId\":3}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 23:32:04', 15);
INSERT INTO `sys_oper_log` VALUES (255, '便民超市商品管理', 3, 'com.axin.web.controller.errand.ConvenienceProductController.remove()', 'DELETE', 1, 'admin', '研发部门', '/errand/convenienceProduct/1', '127.0.0.1', '内网IP', '[1]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 23:32:14', 19);
INSERT INTO `sys_oper_log` VALUES (256, '便民超市商品管理', 1, 'com.axin.web.controller.errand.ConvenienceProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/convenienceProduct', '127.0.0.1', '内网IP', '{\"imageUrl\":\"/profile/upload/2025/12/05/服务图标_20251205233231A005.png\",\"isEnabled\":1,\"price\":100,\"productName\":\"打火机\",\"productType\":\"日常用品\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 23:32:36', 25);
INSERT INTO `sys_oper_log` VALUES (257, '便民超市商品管理', 1, 'com.axin.web.controller.errand.ConvenienceProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/convenienceProduct', '127.0.0.1', '内网IP', '{\"imageUrl\":\"/profile/upload/2025/12/05/服务图标_20251205233249A006.png\",\"isEnabled\":1,\"price\":300,\"productName\":\"可口可乐\",\"productType\":\"饮料\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 23:32:53', 22);
INSERT INTO `sys_oper_log` VALUES (258, '便民超市商品管理', 1, 'com.axin.web.controller.errand.ConvenienceProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/convenienceProduct', '127.0.0.1', '内网IP', '{\"imageUrl\":\"/profile/upload/2025/12/05/服务图标_20251205233305A007.png\",\"isEnabled\":1,\"price\":300,\"productName\":\"芬达汽水\",\"productType\":\"饮料\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 23:33:10', 26);
INSERT INTO `sys_oper_log` VALUES (259, '服务套餐管理', 1, 'com.axin.web.controller.errand.ServicePackageController.add()', 'POST', 1, 'admin', '研发部门', '/errand/servicePackage', '127.0.0.1', '内网IP', '{\"durationDays\":30,\"durationType\":1,\"firstMonthPrice\":2500,\"includeService\":\"扔垃圾\",\"isEnabled\":1,\"packageName\":\"代扔垃圾包月\",\"price\":3000,\"typeId\":4}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 23:33:50', 25);
INSERT INTO `sys_oper_log` VALUES (260, '服务套餐管理', 1, 'com.axin.web.controller.errand.ServicePackageController.add()', 'POST', 1, 'admin', '研发部门', '/errand/servicePackage', '127.0.0.1', '内网IP', '{\"durationDays\":365,\"durationType\":2,\"includeService\":\"扔垃圾\",\"isEnabled\":1,\"packageName\":\"代扔垃圾包年\",\"price\":29900,\"typeId\":4}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-05 23:34:30', 15);
INSERT INTO `sys_oper_log` VALUES (261, '商品（服务）类型管理', 2, 'com.axin.web.controller.errand.ProductTypeController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"/profile/upload/2025/12/04/服务图标_20251204235417A004.png\",\"id\":6,\"isEnabled\":1,\"isManageable\":1,\"typeDesc\":\"备注里填写要配送的物品（配送特殊物品超重3kg需备注，远路程可备注）\",\"typeName\":\"代送物品\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-06 00:30:55', 19);
INSERT INTO `sys_oper_log` VALUES (262, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":2000,\"amountMin\":0,\"distance\":0,\"duration\":0,\"durationUnit\":\"0\",\"id\":10,\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":400,\"priceMin\":0,\"specName\":\"预算20\",\"typeId\":5,\"weightMax\":0,\"weightMin\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-07 00:41:42', 44);
INSERT INTO `sys_oper_log` VALUES (263, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":4000,\"amountMin\":2000,\"distance\":0,\"duration\":0,\"durationUnit\":\"0\",\"id\":11,\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":600,\"priceMin\":0,\"specName\":\"预算40\",\"typeId\":5,\"weightMax\":0,\"weightMin\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-07 00:41:47', 26);
INSERT INTO `sys_oper_log` VALUES (264, '微信用户管理', 2, 'com.axin.web.controller.errand.WechatUserController.changeStatus()', 'PUT', 1, 'admin', '研发部门', '/errand/wechatUser/status/0/1', '127.0.0.1', '内网IP', '0 [1]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-07 23:12:44', 23);
INSERT INTO `sys_oper_log` VALUES (265, '微信用户管理', 2, 'com.axin.web.controller.errand.WechatUserController.changeStatus()', 'PUT', 1, 'admin', '研发部门', '/errand/wechatUser/status/1/1', '127.0.0.1', '内网IP', '1 [1]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-07 23:13:12', 11);
INSERT INTO `sys_oper_log` VALUES (266, '小程序配置', 2, 'com.axin.web.controller.errand.MiniappConfigController.update()', 'PUT', 1, 'admin', '研发部门', '/errand/miniappConfig/current', '127.0.0.1', '内网IP', '{\"carouselImages\":\"[{\\\"imageUrl\\\":\\\"/profile/upload/2025/12/08/服务图标_20251208005006A001.png\\\",\\\"sortOrder\\\":1},{\\\"imageUrl\\\":\\\"/profile/upload/2025/12/08/服务图标_20251208005024A002.png\\\",\\\"sortOrder\\\":2},{\\\"imageUrl\\\":\\\"/profile/upload/2025/12/08/服务图标_20251208005042A003.png\\\",\\\"sortOrder\\\":3}]\",\"isEnabled\":1,\"privacyPolicy\":\"<p>11111</p>\",\"withdrawRate\":5}', '{\"msg\":\"操作成功\",\"code\":200,\"data\":true}', 0, NULL, '2025-12-08 00:50:53', 167);
INSERT INTO `sys_oper_log` VALUES (267, '小程序配置', 2, 'com.axin.web.controller.errand.MiniappConfigController.update()', 'PUT', 1, 'admin', '研发部门', '/errand/miniappConfig/current', '127.0.0.1', '内网IP', '{\"carouselImages\":\"[{\\\"imageUrl\\\":\\\"/profile/upload/2025/12/08/123_20251208225136A001.png\\\",\\\"sortOrder\\\":1},{\\\"imageUrl\\\":\\\"/profile/upload/2025/12/08/设计跑腿轮播图_20251208225142A002.png\\\",\\\"sortOrder\\\":2},{\\\"imageUrl\\\":\\\"/profile/upload/2025/12/08/设计跑腿轮播图1_20251208225152A003.png\\\",\\\"sortOrder\\\":3}]\",\"isEnabled\":1,\"privacyPolicy\":\"<p class=\\\"ql-indent-8\\\"><strong style=\\\"background-color: rgb(239, 240, 241); color: rgb(31, 35, 41);\\\">小谦快快送跑腿小程序隐私政策</strong></p><p><span style=\\\"color: rgb(31, 35, 41); background-color: rgb(239, 240, 241);\\\">一、引言</span><span style=\\\"color: rgb(31, 35, 41); background-color: rgb(255, 255, 255);\\\">​</span></p><ol><li data-list=\\\"ordered\\\"><span class=\\\"ql-ui\\\" contenteditable=\\\"false\\\"></span><span style=\\\"color: rgb(31, 35, 41); background-color: rgb(239, 240, 241);\\\">本隐私政策旨在明确 “小谦快快送” 跑腿小程序（以下简称 “本小程序”）收集、使用、存储、保护用户个人信息的规则，保障您的信息安全与合法权益。</span><span style=\\\"color: rgb(31, 35, 41); background-color: rgb(255, 255, 255);\\\">​</span></li><li data-list=\\\"ordered\\\"><span class=\\\"ql-ui\\\" contenteditable=\\\"false\\\"></span><span style=\\\"color: rgb(31, 35, 41); background-color: rgb(239, 240, 241);\\\">本小程序严格遵循《中华人民共和国个人信息保护法》《微信小程序平台运营规范》等法律法规，坚持 “合法、正当、必要” 原则处理个人信息。</span><span style=\\\"color: rgb(31, 35, 41); background-color: rgb(255, 255, 255);\\\">​</span></li><li data-list=\\\"ordered\\\"><span class=\\\"ql-ui\\\" contenteditable=\\\"false\\\"></span><span style=\\\"color: rgb(31, 35, 41); background-color: rgb(239, 240, 241);\\\">您首次使用本小程序时，我们将通过弹窗（覆盖屏幕 1/3 以上）提示您阅读本政策，您点击 “同意” 即表示充分理解并接受全部条款；若您拒绝，将无法使用核心跑腿服务，但可浏览小程序基础功能。</span><span style=\\\"color: rgb(31, 35, 41); background-color: rgb(255, 255, 255);\\\">​</span></li></ol><p><span style=\\\"color: rgb(31, 35, 41); background-color: rgb(239, 240, 241);\\\">二、我们收集的个人信息及用途</span><span style=\\\"color: rgb(31, 35, 41); background-color: rgb(255, 255, 255);\\\">​</span></p><p><span style=\\\"color: rgb(31, 35, 41); background-color: rgb(239, 240, 241);\\\">（一）核心服务必需的个人信息</span><span style=\\\"color: rgb(31, 35, 41); background-colo', '{\"msg\":\"操作成功\",\"code\":200,\"data\":true}', 0, NULL, '2025-12-08 22:56:09', 44);
INSERT INTO `sys_oper_log` VALUES (268, '商品（服务）类型管理', 3, 'com.axin.web.controller.errand.ProductTypeController.remove()', 'DELETE', 1, 'admin', '研发部门', '/errand/productType/10', '127.0.0.1', '内网IP', '[10]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-08 23:34:32', 21);
INSERT INTO `sys_oper_log` VALUES (269, '商品（服务）类型管理', 2, 'com.axin.web.controller.errand.ProductTypeController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"/profile/upload/2025/12/08/dayinji_01_20251208233455A005.png\",\"id\":9,\"isEnabled\":1,\"isManageable\":1,\"typeDesc\":\"备注里填写打印规格\",\"typeName\":\"代打印\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-08 23:34:57', 16);
INSERT INTO `sys_oper_log` VALUES (270, '商品（服务）类型管理', 2, 'com.axin.web.controller.errand.ProductTypeController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"/profile/upload/2025/12/08/物品寄存_20251208233709A006.png\",\"id\":11,\"isEnabled\":1,\"isManageable\":1,\"typeDesc\":\"可寄存行李箱，衣物等物品\",\"typeName\":\"物品寄存\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-08 23:37:11', 12);
INSERT INTO `sys_oper_log` VALUES (271, '商品（服务）类型管理', 2, 'com.axin.web.controller.errand.ProductTypeController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"/profile/upload/2025/12/08/服务_20251208233834A007.png\",\"id\":8,\"isEnabled\":1,\"isManageable\":1,\"typeDesc\":\"可选择你需要的其他类型服务\",\"typeName\":\"万能服务\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-08 23:38:35', 11);
INSERT INTO `sys_oper_log` VALUES (272, '商品（服务）类型管理', 2, 'com.axin.web.controller.errand.ProductTypeController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"/profile/upload/2025/12/08/代购购物车小图标_20251208234006A009.png\",\"id\":7,\"isEnabled\":1,\"isManageable\":1,\"typeDesc\":\"备注里填写要买的物品\",\"typeName\":\"代购\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-08 23:40:07', 20);
INSERT INTO `sys_oper_log` VALUES (273, '商品（服务）类型管理', 2, 'com.axin.web.controller.errand.ProductTypeController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"/profile/upload/2025/12/08/打印_20251208234111A010.png\",\"id\":9,\"isEnabled\":1,\"isManageable\":1,\"typeDesc\":\"备注里填写打印规格\",\"typeName\":\"代打印\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-08 23:41:11', 21);
INSERT INTO `sys_oper_log` VALUES (274, '商品（服务）类型管理', 2, 'com.axin.web.controller.errand.ProductTypeController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"/profile/upload/2025/12/08/daina_01_20251208234130A011.png\",\"id\":6,\"isEnabled\":1,\"isManageable\":1,\"typeDesc\":\"备注里填写要配送的物品（配送特殊物品超重3kg需备注，远路程可备注）\",\"typeName\":\"代送物品\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-08 23:41:31', 19);
INSERT INTO `sys_oper_log` VALUES (275, '商品（服务）类型管理', 3, 'com.axin.web.controller.errand.ProductTypeController.remove()', 'DELETE', 1, 'admin', '研发部门', '/errand/productType/5', '127.0.0.1', '内网IP', '[5]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-08 23:41:36', 14);
INSERT INTO `sys_oper_log` VALUES (276, '商品（服务）类型管理', 2, 'com.axin.web.controller.errand.ProductTypeController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"/profile/upload/2025/12/08/LS垃圾分类_20251208234453A012.png\",\"id\":4,\"isEnabled\":1,\"isManageable\":1,\"typeDesc\":\"\",\"typeName\":\"代扔垃圾\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-08 23:44:53', 21);
INSERT INTO `sys_oper_log` VALUES (277, '商品（服务）类型管理', 2, 'com.axin.web.controller.errand.ProductTypeController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"/profile/upload/2025/12/08/我要成为骑手_20251208234546A013.png\",\"id\":6,\"isEnabled\":1,\"isManageable\":1,\"typeDesc\":\"备注里填写要配送的物品（配送特殊物品超重3kg需备注，远路程可备注）\",\"typeName\":\"代送物品\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-08 23:45:47', 14);
INSERT INTO `sys_oper_log` VALUES (278, '商品（服务）类型管理', 2, 'com.axin.web.controller.errand.ProductTypeController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"/profile/upload/2025/12/08/lajitong_01(2)_20251208234709A014.png\",\"id\":3,\"isEnabled\":1,\"isManageable\":1,\"typeDesc\":\"可代拿附近快递，备注填写取件码\",\"typeName\":\"代拿快递\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-08 23:47:10', 9);
INSERT INTO `sys_oper_log` VALUES (279, '商品（服务）类型管理', 1, 'com.axin.web.controller.errand.ProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"/profile/upload/2025/12/08/附近超市_20251208234859A015.png\",\"isEnabled\":1,\"isManageable\":1,\"typeDesc\":\"超市\",\"typeName\":\"便民超市\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-08 23:49:01', 20);
INSERT INTO `sys_oper_log` VALUES (280, '小区-商品类型关联', 1, 'com.axin.web.controller.errand.CommunityProductTypeController.add()', 'POST', 1, 'admin', '研发部门', '/errand/communityProductType', '127.0.0.1', '内网IP', '{\"communityId\":7,\"isEnabled\":1,\"productTypeId\":12,\"sortOrder\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-08 23:49:41', 23);
INSERT INTO `sys_oper_log` VALUES (281, '商品（服务）类型管理', 2, 'com.axin.web.controller.errand.ProductTypeController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"/profile/upload/2025/12/08/打印_20251208234111A010.png\",\"id\":9,\"isEnabled\":1,\"isManageable\":1,\"typeDesc\":\"备注里填写打印规格\",\"typeName\":\"打印服务\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-09 00:42:41', 20);
INSERT INTO `sys_oper_log` VALUES (282, '商品（服务）类型管理', 2, 'com.axin.web.controller.errand.ProductTypeController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"/profile/upload/2025/12/08/代购购物车小图标_20251208234006A009.png\",\"id\":7,\"isEnabled\":1,\"isManageable\":1,\"typeDesc\":\"备注里填写要买的物品\",\"typeName\":\"代购服务\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-09 00:42:47', 11);
INSERT INTO `sys_oper_log` VALUES (283, '商品（服务）类型管理', 2, 'com.axin.web.controller.errand.ProductTypeController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"/profile/upload/2025/12/08/我要成为骑手_20251208234546A013.png\",\"id\":6,\"isEnabled\":1,\"isManageable\":1,\"typeDesc\":\"备注里填写要配送的物品（配送特殊物品超重3kg需备注，远路程可备注）\",\"typeName\":\"物品代送\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-09 00:43:01', 16);
INSERT INTO `sys_oper_log` VALUES (284, '商品（服务）类型管理', 2, 'com.axin.web.controller.errand.ProductTypeController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"/profile/upload/2025/12/08/LS垃圾分类_20251208234453A012.png\",\"id\":4,\"isEnabled\":1,\"isManageable\":1,\"typeDesc\":\"\",\"typeName\":\"垃圾代扔\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-09 00:43:20', 10);
INSERT INTO `sys_oper_log` VALUES (285, '商品（服务）类型管理', 2, 'com.axin.web.controller.errand.ProductTypeController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/productType', '127.0.0.1', '内网IP', '{\"iconUrl\":\"/profile/upload/2025/12/08/lajitong_01(2)_20251208234709A014.png\",\"id\":3,\"isEnabled\":1,\"isManageable\":1,\"typeDesc\":\"可代拿附近快递，备注填写取件码\",\"typeName\":\"快递代拿\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-09 00:43:27', 10);
INSERT INTO `sys_oper_log` VALUES (286, '小程序配置', 2, 'com.axin.web.controller.errand.MiniappConfigController.update()', 'PUT', 1, 'admin', '研发部门', '/errand/miniappConfig/current', '127.0.0.1', '内网IP', '{\"carouselImages\":\"[{\\\"imageUrl\\\":\\\"/profile/upload/2025/12/08/123_20251208225136A001.png\\\",\\\"sortOrder\\\":1},{\\\"imageUrl\\\":\\\"/profile/upload/2025/12/08/设计跑腿轮播图_20251208225142A002.png\\\",\\\"sortOrder\\\":2},{\\\"imageUrl\\\":\\\"/profile/upload/2025/12/08/设计跑腿轮播图1_20251208225152A003.png\\\",\\\"sortOrder\\\":3}]\",\"isEnabled\":1,\"privacyPolicy\":\"<h1>小谦快快送小程序法律条款及隐私政策</h1><p><br></p><p><strong>生效时间</strong>：____年____月____日</p><p><strong>更新时间</strong>：<strong>年____月____日</strong></p><p><strong>运营方：</strong>（公司 / 个人名称，以下简称 “我们”）</p><h2>一、法律条款（用之前先看明白）</h2><h3>1. 谁能用，怎么用？</h3><ol><li data-list=\\\"bullet1\\\"><span class=\\\"ql-ui\\\" contenteditable=\\\"false\\\"></span>您得是能对自己行为负责的成年人；如果是小朋友，必须让家长同意并帮忙操作。</li><li data-list=\\\"bullet1\\\"><span class=\\\"ql-ui\\\" contenteditable=\\\"false\\\"></span>用小程序时，您填的信息（比如手机号、地址）得是真的，不能用别人的信息冒充。</li><li data-list=\\\"bullet1\\\"><span class=\\\"ql-ui\\\" contenteditable=\\\"false\\\"></span>我们可能会调整服务（比如新增 “代买奶茶”、暂时停 “代扔大件垃圾”），大的调整会提前 7 天在小程序弹通知，您继续用就代表认可调整。</li></ol><h3>2. 您的权利和要注意的事</h3><ol><li data-list=\\\"bullet1\\\"><span class=\\\"ql-ui\\\" contenteditable=\\\"false\\\"></span><strong>您能做的</strong>：下单、接单、查订单、算佣金，也能改自己的信息、注销账号。</li><li data-list=\\\"bullet1\\\"><span class=\\\"ql-ui\\\" contenteditable=\\\"false\\\"></span><strong>您要注意的</strong>：不能用小程序干违法的事（比如让接单员送危险品、骗接单员钱）；自己的账号要管好，丢了被盗了赶紧联系客服冻结，不然出问题得自己担责。</li></ol><h3>3. 费用和佣金怎么算？</h3><ol><li data-list=\\\"bullet1\\\"><span class=\\\"ql-ui\\\" contenteditable=\\\"false\\\"></span>订单多少钱、接单能赚多少佣金，小程序里会写清楚，您下单 / 接单就代表认这个价格。</li><li data-list=\\\"bullet1\\\"><span class=\\\"ql-ui\\\" contenteditable=\\\"false\\\"></span>付钱支持微信付或线下付，付完钱订单才生效。</li><li data-list=\\\"bullet1\\\"><span class=\\\"ql-ui\\\" contenteditable=\\\"false\\\"></span>接单员的佣金，会在订单完成后 7 个工作日内结算，但得先完成实名认证；如果您违规（比如接了单不送、骗佣金），我们有权扣掉对应的钱。</li></ol><h3>4. 哪些情况我们不担责？</h3><ol><li data-list=\\\"bullet1\\\"><span class=\\\"ql-ui\\\" contenteditable=\\\"false\\\"></span>比如地震、网络断了、微信系统维护这些没法控制的事，导致服务用不了，我们不赔钱，但会尽快恢复并告诉大家。</li><li data-list=\\\"bullet1\\\"><span class=\\\"ql-ui\\\" contenteditable=\\\"false\\\"></span>如果您和接单员有矛盾（比如东西送坏了、送晚了），我们会帮忙协调，但不', '{\"msg\":\"操作成功\",\"code\":200,\"data\":true}', 0, NULL, '2025-12-09 01:03:29', 36);
INSERT INTO `sys_oper_log` VALUES (287, '小程序配置', 2, 'com.axin.web.controller.errand.MiniappConfigController.update()', 'PUT', 1, 'admin', '研发部门', '/errand/miniappConfig/current', '127.0.0.1', '内网IP', '{\"carouselImages\":\"[{\\\"imageUrl\\\":\\\"/profile/upload/2025/12/08/123_20251208225136A001.png\\\",\\\"sortOrder\\\":1},{\\\"imageUrl\\\":\\\"/profile/upload/2025/12/08/设计跑腿轮播图_20251208225142A002.png\\\",\\\"sortOrder\\\":2},{\\\"imageUrl\\\":\\\"/profile/upload/2025/12/08/设计跑腿轮播图1_20251208225152A003.png\\\",\\\"sortOrder\\\":3}]\",\"isEnabled\":1,\"privacyPolicy\":\"<h1><strong>小谦快快送小程序法律条款及隐私政策</strong></h1><h1>**</h1><h1>生效时间：____年____月____日</h1><h1>更新时间：年____月____日</h1><h1>运营方：（公司 / 个人名称，以下简称 “我们”）</h1><h1>一、法律条款（用之前先看明白）</h1><h1>1. 谁能用，怎么用？</h1><h1>您得是能对自己行为负责的成年人；如果是小朋友，必须让家长同意并帮忙操作。</h1><h1>用小程序时，您填的信息（比如手机号、地址）得是真的，不能用别人的信息冒充。</h1><h1>我们可能会调整服务（比如新增 “代买奶茶”、暂时停 “代扔大件垃圾”），大的调整会提前 7 天在小程序弹通知，您继续用就代表认可调整。</h1><h1>2. 您的权利和要注意的事</h1><h1>您能做的：下单、接单、查订单、算佣金，也能改自己的信息、注销账号。</h1><h1>您要注意的：不能用小程序干违法的事（比如让接单员送危险品、骗接单员钱）；自己的账号要管好，丢了被盗了赶紧联系客服冻结，不然出问题得自己担责。</h1><h1>3. 费用和佣金怎么算？</h1><h1>订单多少钱、接单能赚多少佣金，小程序里会写清楚，您下单 / 接单就代表认这个价格。</h1><h1>付钱支持微信付或线下付，付完钱订单才生效。</h1><h1>接单员的佣金，会在订单完成后 7 个工作日内结算，但得先完成实名认证；如果您违规（比如接了单不送、骗佣金），我们有权扣掉对应的钱。</h1><h1>4. 哪些情况我们不担责？</h1><h1>比如地震、网络断了、微信系统维护这些没法控制的事，导致服务用不了，我们不赔钱，但会尽快恢复并告诉大家。</h1><h1>如果您和接单员有矛盾（比如东西送坏了、送晚了），我们会帮忙协调，但不直接负责；送贵重东西（比如手机、首饰），建议您自己买保险，丢了坏了我们不赔。</h1><h1>5. 有纠纷怎么解决？</h1><h1>先好好商量，商量不好的话，得去我们公司 / 个人所在地的法院解决问题。</h1><h1>二、隐私政策（您的信息我们怎么管）</h1><h1>1. 先说明白</h1><h1>这份政策讲的是我们怎么收、用、存您的信息，还有怎么保护这些信息，适用于您用小程序的所有服务（下单、接单等）。</h1><h1>您同意这份政策，才能用核心功能（比如下单、接单）；要是不同意，只能看看小程序的基本内容，用不了这些功能。</h1><h1>2. 我们会收您哪些信息，用来做什么？</h1><h1>（1）必须要收的信息（不然没法服务）</h1><h1>手机号：从微信授权获取，用来确认您的身份、发订单通知（比如 “有人接您的单了”“东西送到了”）、有急事联系您、给接单员结佣金。只要您还用服务，我们就存着；您注销账号，72 小时内会彻底删掉。</h1><h1>地址：您自己填的小区、门牌号，比如 “XX 小区 3 号楼 2 单元 101”，用来送东西（代拿快递、扔垃圾、送物品）。订单完成后，会存 3 个月（怕有纠纷要查），之后会把地址里的关键信息（比如门牌号）模糊处理。</h1><h1>微信号：只有您用 “代打印” 服务时才收，用来传要打印的文件；服务做完，您可以自己删掉这个号。</h1><h1>（2）自动收的信息（不涉及隐私，只为服务稳定）</h1><h1>您的手机型号、微信版本：用来适配不同手机，保证小程序能正常用。</h1><h1>订单操作记录、IP 地址：比如您什么时候下的单、从哪个地方用的小程序，用来查问题（比如订单没显示）、处理纠纷，存 6 个月后会把能识别您的信息去掉。</h1><h1>3. 您的信息我们怎么存、怎么保护？</h1><h1>所有信息都存在国内的阿里云服务器，不会传到国外。</h1><h1>手机号、地址这些敏感信息，会用加密方式存（就像给信息加了锁），钥匙会定期换，更安全。</h1><h1>只有必要的人（', '{\"msg\":\"操作成功\",\"code\":200,\"data\":true}', 0, NULL, '2025-12-09 01:04:45', 14);
INSERT INTO `sys_oper_log` VALUES (288, '外卖店铺管理', 2, 'com.axin.web.controller.errand.TakeawayStoreController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/takeawayStore', '127.0.0.1', '内网IP', '{\"address\":\"地址111\",\"avatarUrl\":\"/profile/upload/2025/12/05/服务图标_20251205233033A001.png\",\"communityId\":7,\"contact\":\"17875116728\",\"id\":3,\"isEnabled\":1,\"storeName\":\"麻辣烫\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-10 00:59:38', 31);
INSERT INTO `sys_oper_log` VALUES (289, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"id\":23,\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":100,\"priceMin\":100,\"specName\":\"彩色打印\",\"typeId\":9}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-11 23:16:20', 33);
INSERT INTO `sys_oper_log` VALUES (290, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"id\":22,\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":100,\"priceMin\":100,\"specName\":\"彩色打印\",\"typeId\":9}', '{\"msg\":\"修改失败：同一服务模块下规格名称已存在\",\"code\":500}', 0, NULL, '2025-12-11 23:16:27', 3);
INSERT INTO `sys_oper_log` VALUES (291, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"id\":22,\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":100,\"priceMin\":100,\"specName\":\"黑白打印\",\"typeId\":9}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-11 23:16:32', 21);
INSERT INTO `sys_oper_log` VALUES (292, '商品规格管理', 1, 'com.axin.web.controller.errand.ProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":1000,\"priceMin\":0,\"specName\":\"按天寄存\",\"typeId\":11}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-11 23:40:27', 16);
INSERT INTO `sys_oper_log` VALUES (293, '商品规格管理', 1, 'com.axin.web.controller.errand.ProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":5000,\"priceMin\":0,\"specName\":\"按周寄存\",\"typeId\":11}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-11 23:40:45', 21);
INSERT INTO `sys_oper_log` VALUES (294, '商品规格管理', 1, 'com.axin.web.controller.errand.ProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":2000,\"priceMin\":0,\"specName\":\"按月寄存\",\"typeId\":11}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-11 23:40:56', 20);
INSERT INTO `sys_oper_log` VALUES (295, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"id\":26,\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":10000,\"priceMin\":0,\"specName\":\"按月寄存\",\"typeId\":11}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-11 23:41:05', 11);
INSERT INTO `sys_oper_log` VALUES (296, '微信用户管理', 2, 'com.axin.web.controller.errand.WechatUserController.changeAdminStatus()', 'PUT', 1, 'admin', '研发部门', '/errand/wechatUser/admin/1/1', '127.0.0.1', '内网IP', '1 [1]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-14 00:39:17', 26);
INSERT INTO `sys_oper_log` VALUES (297, '微信用户管理', 2, 'com.axin.web.controller.errand.WechatUserController.changeAdminStatus()', 'PUT', 1, 'admin', '研发部门', '/errand/wechatUser/admin/0/1', '127.0.0.1', '内网IP', '0 [1]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-14 00:47:05', 30);
INSERT INTO `sys_oper_log` VALUES (298, '微信用户管理', 2, 'com.axin.web.controller.errand.WechatUserController.changeAdminStatus()', 'PUT', 1, 'admin', '研发部门', '/errand/wechatUser/admin/1/1', '127.0.0.1', '内网IP', '1 [1]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-14 01:22:10', 24);
INSERT INTO `sys_oper_log` VALUES (299, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"distance\":0,\"duration\":3,\"durationUnit\":\"00\",\"id\":21,\"isEnabled\":1,\"isUniversalCustom\":1,\"priceMax\":10000,\"priceMin\":0,\"specName\":\"家政服务\",\"typeId\":8,\"weightMax\":0,\"weightMin\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-14 23:02:58', 23);
INSERT INTO `sys_oper_log` VALUES (300, '小程序配置', 2, 'com.axin.web.controller.errand.MiniappConfigController.update()', 'PUT', 1, 'admin', '研发部门', '/errand/miniappConfig/current', '127.0.0.1', '内网IP', '{\"carouselImages\":\"[{\\\"imageUrl\\\":\\\"/profile/upload/2025/12/08/123_20251208225136A001.png\\\",\\\"sortOrder\\\":1},{\\\"imageUrl\\\":\\\"/profile/upload/2025/12/08/设计跑腿轮播图_20251208225142A002.png\\\",\\\"sortOrder\\\":2},{\\\"imageUrl\\\":\\\"/profile/upload/2025/12/08/设计跑腿轮播图1_20251208225152A003.png\\\",\\\"sortOrder\\\":3}]\",\"isEnabled\":1,\"noticeContent\":\"今日放假，暂不接单，望周知！\",\"privacyPolicy\":\"<h1><strong>小谦快快送小程序法律条款及隐私政策</strong></h1><h1>**</h1><h1>生效时间：____年____月____日</h1><h1>更新时间：年____月____日</h1><h1>运营方：（公司 / 个人名称，以下简称 “我们”）</h1><h1>一、法律条款（用之前先看明白）</h1><h1>1. 谁能用，怎么用？</h1><h1>您得是能对自己行为负责的成年人；如果是小朋友，必须让家长同意并帮忙操作。</h1><h1>用小程序时，您填的信息（比如手机号、地址）得是真的，不能用别人的信息冒充。</h1><h1>我们可能会调整服务（比如新增 “代买奶茶”、暂时停 “代扔大件垃圾”），大的调整会提前 7 天在小程序弹通知，您继续用就代表认可调整。</h1><h1>2. 您的权利和要注意的事</h1><h1>您能做的：下单、接单、查订单、算佣金，也能改自己的信息、注销账号。</h1><h1>您要注意的：不能用小程序干违法的事（比如让接单员送危险品、骗接单员钱）；自己的账号要管好，丢了被盗了赶紧联系客服冻结，不然出问题得自己担责。</h1><h1>3. 费用和佣金怎么算？</h1><h1>订单多少钱、接单能赚多少佣金，小程序里会写清楚，您下单 / 接单就代表认这个价格。</h1><h1>付钱支持微信付或线下付，付完钱订单才生效。</h1><h1>接单员的佣金，会在订单完成后 7 个工作日内结算，但得先完成实名认证；如果您违规（比如接了单不送、骗佣金），我们有权扣掉对应的钱。</h1><h1>4. 哪些情况我们不担责？</h1><h1>比如地震、网络断了、微信系统维护这些没法控制的事，导致服务用不了，我们不赔钱，但会尽快恢复并告诉大家。</h1><h1>如果您和接单员有矛盾（比如东西送坏了、送晚了），我们会帮忙协调，但不直接负责；送贵重东西（比如手机、首饰），建议您自己买保险，丢了坏了我们不赔。</h1><h1>5. 有纠纷怎么解决？</h1><h1>先好好商量，商量不好的话，得去我们公司 / 个人所在地的法院解决问题。</h1><h1>二、隐私政策（您的信息我们怎么管）</h1><h1>1. 先说明白</h1><h1>这份政策讲的是我们怎么收、用、存您的信息，还有怎么保护这些信息，适用于您用小程序的所有服务（下单、接单等）。</h1><h1>您同意这份政策，才能用核心功能（比如下单、接单）；要是不同意，只能看看小程序的基本内容，用不了这些功能。</h1><h1>2. 我们会收您哪些信息，用来做什么？</h1><h1>（1）必须要收的信息（不然没法服务）</h1><h1>手机号：从微信授权获取，用来确认您的身份、发订单通知（比如 “有人接您的单了”“东西送到了”）、有急事联系您、给接单员结佣金。只要您还用服务，我们就存着；您注销账号，72 小时内会彻底删掉。</h1><h1>地址：您自己填的小区、门牌号，比如 “XX 小区 3 号楼 2 单元 101”，用来送东西（代拿快递、扔垃圾、送物品）。订单完成后，会存 3 个月（怕有纠纷要查），之后会把地址里的关键信息（比如门牌号）模糊处理。</h1><h1>微信号：只有您用 “代打印” 服务时才收，用来传要打印的文件；服务做完，您可以自己删掉这个号。</h1><h1>（2）自动收的信息（不涉及隐私，只为服务稳定）</h1><h1>您的手机型号、微信版本：用来适配不同手机，保证小程序能正常用。</h1><h1>订单操作记录、IP 地址：比如您什么时候下的单、从哪个地方用的小程序，用来查问题（比如订单没显示）、处理纠纷，存 6 个月后会把能识别您的信息去掉。</h1><h1>3. 您的信息我们怎么存、怎么保护？</h1><h1>所有信息都存在国内的阿里云服务器，不会传到国外。</h1><h1>手机号、地址这些敏感信息，会用加密方式存（就像给信', '{\"msg\":\"操作成功\",\"code\":200,\"data\":true}', 0, NULL, '2025-12-15 01:32:42', 46);
INSERT INTO `sys_oper_log` VALUES (301, '小程序配置', 2, 'com.axin.web.controller.errand.MiniappConfigController.update()', 'PUT', 1, 'admin', '研发部门', '/errand/miniappConfig/current', '127.0.0.1', '内网IP', '{\"carouselImages\":\"[{\\\"imageUrl\\\":\\\"/profile/upload/2025/12/08/123_20251208225136A001.png\\\",\\\"sortOrder\\\":1},{\\\"imageUrl\\\":\\\"/profile/upload/2025/12/08/设计跑腿轮播图_20251208225142A002.png\\\",\\\"sortOrder\\\":2},{\\\"imageUrl\\\":\\\"/profile/upload/2025/12/08/设计跑腿轮播图1_20251208225152A003.png\\\",\\\"sortOrder\\\":3}]\",\"isEnabled\":1,\"noticeContent\":\"213123\",\"privacyPolicy\":\"<h1><strong>小谦快快送小程序法律条款及隐私政策</strong></h1><h1>**</h1><h1>生效时间：____年____月____日</h1><h1>更新时间：年____月____日</h1><h1>运营方：（公司 / 个人名称，以下简称 “我们”）</h1><h1>一、法律条款（用之前先看明白）</h1><h1>1. 谁能用，怎么用？</h1><h1>您得是能对自己行为负责的成年人；如果是小朋友，必须让家长同意并帮忙操作。</h1><h1>用小程序时，您填的信息（比如手机号、地址）得是真的，不能用别人的信息冒充。</h1><h1>我们可能会调整服务（比如新增 “代买奶茶”、暂时停 “代扔大件垃圾”），大的调整会提前 7 天在小程序弹通知，您继续用就代表认可调整。</h1><h1>2. 您的权利和要注意的事</h1><h1>您能做的：下单、接单、查订单、算佣金，也能改自己的信息、注销账号。</h1><h1>您要注意的：不能用小程序干违法的事（比如让接单员送危险品、骗接单员钱）；自己的账号要管好，丢了被盗了赶紧联系客服冻结，不然出问题得自己担责。</h1><h1>3. 费用和佣金怎么算？</h1><h1>订单多少钱、接单能赚多少佣金，小程序里会写清楚，您下单 / 接单就代表认这个价格。</h1><h1>付钱支持微信付或线下付，付完钱订单才生效。</h1><h1>接单员的佣金，会在订单完成后 7 个工作日内结算，但得先完成实名认证；如果您违规（比如接了单不送、骗佣金），我们有权扣掉对应的钱。</h1><h1>4. 哪些情况我们不担责？</h1><h1>比如地震、网络断了、微信系统维护这些没法控制的事，导致服务用不了，我们不赔钱，但会尽快恢复并告诉大家。</h1><h1>如果您和接单员有矛盾（比如东西送坏了、送晚了），我们会帮忙协调，但不直接负责；送贵重东西（比如手机、首饰），建议您自己买保险，丢了坏了我们不赔。</h1><h1>5. 有纠纷怎么解决？</h1><h1>先好好商量，商量不好的话，得去我们公司 / 个人所在地的法院解决问题。</h1><h1>二、隐私政策（您的信息我们怎么管）</h1><h1>1. 先说明白</h1><h1>这份政策讲的是我们怎么收、用、存您的信息，还有怎么保护这些信息，适用于您用小程序的所有服务（下单、接单等）。</h1><h1>您同意这份政策，才能用核心功能（比如下单、接单）；要是不同意，只能看看小程序的基本内容，用不了这些功能。</h1><h1>2. 我们会收您哪些信息，用来做什么？</h1><h1>（1）必须要收的信息（不然没法服务）</h1><h1>手机号：从微信授权获取，用来确认您的身份、发订单通知（比如 “有人接您的单了”“东西送到了”）、有急事联系您、给接单员结佣金。只要您还用服务，我们就存着；您注销账号，72 小时内会彻底删掉。</h1><h1>地址：您自己填的小区、门牌号，比如 “XX 小区 3 号楼 2 单元 101”，用来送东西（代拿快递、扔垃圾、送物品）。订单完成后，会存 3 个月（怕有纠纷要查），之后会把地址里的关键信息（比如门牌号）模糊处理。</h1><h1>微信号：只有您用 “代打印” 服务时才收，用来传要打印的文件；服务做完，您可以自己删掉这个号。</h1><h1>（2）自动收的信息（不涉及隐私，只为服务稳定）</h1><h1>您的手机型号、微信版本：用来适配不同手机，保证小程序能正常用。</h1><h1>订单操作记录、IP 地址：比如您什么时候下的单、从哪个地方用的小程序，用来查问题（比如订单没显示）、处理纠纷，存 6 个月后会把能识别您的信息去掉。</h1><h1>3. 您的信息我们怎么存、怎么保护？</h1><h1>所有信息都存在国内的阿里云服务器，不会传到国外。</h1><h1>手机号、地址这些敏感信息，会用加密方式存（就像给信息加了锁），钥匙', '{\"msg\":\"操作成功\",\"code\":200,\"data\":true}', 0, NULL, '2025-12-15 01:34:47', 27);
INSERT INTO `sys_oper_log` VALUES (302, '小程序配置', 2, 'com.axin.web.controller.errand.MiniappConfigController.update()', 'PUT', 1, 'admin', '研发部门', '/errand/miniappConfig/current', '127.0.0.1', '内网IP', '{\"carouselImages\":\"[{\\\"imageUrl\\\":\\\"/profile/upload/2025/12/08/123_20251208225136A001.png\\\",\\\"sortOrder\\\":1},{\\\"imageUrl\\\":\\\"/profile/upload/2025/12/08/设计跑腿轮播图_20251208225142A002.png\\\",\\\"sortOrder\\\":2},{\\\"imageUrl\\\":\\\"/profile/upload/2025/12/08/设计跑腿轮播图1_20251208225152A003.png\\\",\\\"sortOrder\\\":3}]\",\"isEnabled\":1,\"noticeContent\":\"1232\",\"privacyPolicy\":\"<h1><strong>小谦快快送小程序法律条款及隐私政策</strong></h1><h1>**</h1><h1>生效时间：____年____月____日</h1><h1>更新时间：年____月____日</h1><h1>运营方：（公司 / 个人名称，以下简称 “我们”）</h1><h1>一、法律条款（用之前先看明白）</h1><h1>1. 谁能用，怎么用？</h1><h1>您得是能对自己行为负责的成年人；如果是小朋友，必须让家长同意并帮忙操作。</h1><h1>用小程序时，您填的信息（比如手机号、地址）得是真的，不能用别人的信息冒充。</h1><h1>我们可能会调整服务（比如新增 “代买奶茶”、暂时停 “代扔大件垃圾”），大的调整会提前 7 天在小程序弹通知，您继续用就代表认可调整。</h1><h1>2. 您的权利和要注意的事</h1><h1>您能做的：下单、接单、查订单、算佣金，也能改自己的信息、注销账号。</h1><h1>您要注意的：不能用小程序干违法的事（比如让接单员送危险品、骗接单员钱）；自己的账号要管好，丢了被盗了赶紧联系客服冻结，不然出问题得自己担责。</h1><h1>3. 费用和佣金怎么算？</h1><h1>订单多少钱、接单能赚多少佣金，小程序里会写清楚，您下单 / 接单就代表认这个价格。</h1><h1>付钱支持微信付或线下付，付完钱订单才生效。</h1><h1>接单员的佣金，会在订单完成后 7 个工作日内结算，但得先完成实名认证；如果您违规（比如接了单不送、骗佣金），我们有权扣掉对应的钱。</h1><h1>4. 哪些情况我们不担责？</h1><h1>比如地震、网络断了、微信系统维护这些没法控制的事，导致服务用不了，我们不赔钱，但会尽快恢复并告诉大家。</h1><h1>如果您和接单员有矛盾（比如东西送坏了、送晚了），我们会帮忙协调，但不直接负责；送贵重东西（比如手机、首饰），建议您自己买保险，丢了坏了我们不赔。</h1><h1>5. 有纠纷怎么解决？</h1><h1>先好好商量，商量不好的话，得去我们公司 / 个人所在地的法院解决问题。</h1><h1>二、隐私政策（您的信息我们怎么管）</h1><h1>1. 先说明白</h1><h1>这份政策讲的是我们怎么收、用、存您的信息，还有怎么保护这些信息，适用于您用小程序的所有服务（下单、接单等）。</h1><h1>您同意这份政策，才能用核心功能（比如下单、接单）；要是不同意，只能看看小程序的基本内容，用不了这些功能。</h1><h1>2. 我们会收您哪些信息，用来做什么？</h1><h1>（1）必须要收的信息（不然没法服务）</h1><h1>手机号：从微信授权获取，用来确认您的身份、发订单通知（比如 “有人接您的单了”“东西送到了”）、有急事联系您、给接单员结佣金。只要您还用服务，我们就存着；您注销账号，72 小时内会彻底删掉。</h1><h1>地址：您自己填的小区、门牌号，比如 “XX 小区 3 号楼 2 单元 101”，用来送东西（代拿快递、扔垃圾、送物品）。订单完成后，会存 3 个月（怕有纠纷要查），之后会把地址里的关键信息（比如门牌号）模糊处理。</h1><h1>微信号：只有您用 “代打印” 服务时才收，用来传要打印的文件；服务做完，您可以自己删掉这个号。</h1><h1>（2）自动收的信息（不涉及隐私，只为服务稳定）</h1><h1>您的手机型号、微信版本：用来适配不同手机，保证小程序能正常用。</h1><h1>订单操作记录、IP 地址：比如您什么时候下的单、从哪个地方用的小程序，用来查问题（比如订单没显示）、处理纠纷，存 6 个月后会把能识别您的信息去掉。</h1><h1>3. 您的信息我们怎么存、怎么保护？</h1><h1>所有信息都存在国内的阿里云服务器，不会传到国外。</h1><h1>手机号、地址这些敏感信息，会用加密方式存（就像给信息加了锁），钥匙会定', '{\"msg\":\"操作成功\",\"code\":200,\"data\":true}', 0, NULL, '2025-12-15 01:36:02', 12);
INSERT INTO `sys_oper_log` VALUES (303, '小程序配置', 2, 'com.axin.web.controller.errand.MiniappConfigController.update()', 'PUT', 1, 'admin', '研发部门', '/errand/miniappConfig/current', '127.0.0.1', '内网IP', '{\"carouselImages\":\"[{\\\"imageUrl\\\":\\\"/profile/upload/2025/12/08/123_20251208225136A001.png\\\",\\\"sortOrder\\\":1},{\\\"imageUrl\\\":\\\"/profile/upload/2025/12/08/设计跑腿轮播图_20251208225142A002.png\\\",\\\"sortOrder\\\":2},{\\\"imageUrl\\\":\\\"/profile/upload/2025/12/08/设计跑腿轮播图1_20251208225152A003.png\\\",\\\"sortOrder\\\":3}]\",\"isEnabled\":1,\"noticeContent\":\"今日不营业，望周知\",\"privacyPolicy\":\"<h1><strong>小谦快快送小程序法律条款及隐私政策</strong></h1><h1>**</h1><h1>生效时间：____年____月____日</h1><h1>更新时间：年____月____日</h1><h1>运营方：（公司 / 个人名称，以下简称 “我们”）</h1><h1>一、法律条款（用之前先看明白）</h1><h1>1. 谁能用，怎么用？</h1><h1>您得是能对自己行为负责的成年人；如果是小朋友，必须让家长同意并帮忙操作。</h1><h1>用小程序时，您填的信息（比如手机号、地址）得是真的，不能用别人的信息冒充。</h1><h1>我们可能会调整服务（比如新增 “代买奶茶”、暂时停 “代扔大件垃圾”），大的调整会提前 7 天在小程序弹通知，您继续用就代表认可调整。</h1><h1>2. 您的权利和要注意的事</h1><h1>您能做的：下单、接单、查订单、算佣金，也能改自己的信息、注销账号。</h1><h1>您要注意的：不能用小程序干违法的事（比如让接单员送危险品、骗接单员钱）；自己的账号要管好，丢了被盗了赶紧联系客服冻结，不然出问题得自己担责。</h1><h1>3. 费用和佣金怎么算？</h1><h1>订单多少钱、接单能赚多少佣金，小程序里会写清楚，您下单 / 接单就代表认这个价格。</h1><h1>付钱支持微信付或线下付，付完钱订单才生效。</h1><h1>接单员的佣金，会在订单完成后 7 个工作日内结算，但得先完成实名认证；如果您违规（比如接了单不送、骗佣金），我们有权扣掉对应的钱。</h1><h1>4. 哪些情况我们不担责？</h1><h1>比如地震、网络断了、微信系统维护这些没法控制的事，导致服务用不了，我们不赔钱，但会尽快恢复并告诉大家。</h1><h1>如果您和接单员有矛盾（比如东西送坏了、送晚了），我们会帮忙协调，但不直接负责；送贵重东西（比如手机、首饰），建议您自己买保险，丢了坏了我们不赔。</h1><h1>5. 有纠纷怎么解决？</h1><h1>先好好商量，商量不好的话，得去我们公司 / 个人所在地的法院解决问题。</h1><h1>二、隐私政策（您的信息我们怎么管）</h1><h1>1. 先说明白</h1><h1>这份政策讲的是我们怎么收、用、存您的信息，还有怎么保护这些信息，适用于您用小程序的所有服务（下单、接单等）。</h1><h1>您同意这份政策，才能用核心功能（比如下单、接单）；要是不同意，只能看看小程序的基本内容，用不了这些功能。</h1><h1>2. 我们会收您哪些信息，用来做什么？</h1><h1>（1）必须要收的信息（不然没法服务）</h1><h1>手机号：从微信授权获取，用来确认您的身份、发订单通知（比如 “有人接您的单了”“东西送到了”）、有急事联系您、给接单员结佣金。只要您还用服务，我们就存着；您注销账号，72 小时内会彻底删掉。</h1><h1>地址：您自己填的小区、门牌号，比如 “XX 小区 3 号楼 2 单元 101”，用来送东西（代拿快递、扔垃圾、送物品）。订单完成后，会存 3 个月（怕有纠纷要查），之后会把地址里的关键信息（比如门牌号）模糊处理。</h1><h1>微信号：只有您用 “代打印” 服务时才收，用来传要打印的文件；服务做完，您可以自己删掉这个号。</h1><h1>（2）自动收的信息（不涉及隐私，只为服务稳定）</h1><h1>您的手机型号、微信版本：用来适配不同手机，保证小程序能正常用。</h1><h1>订单操作记录、IP 地址：比如您什么时候下的单、从哪个地方用的小程序，用来查问题（比如订单没显示）、处理纠纷，存 6 个月后会把能识别您的信息去掉。</h1><h1>3. 您的信息我们怎么存、怎么保护？</h1><h1>所有信息都存在国内的阿里云服务器，不会传到国外。</h1><h1>手机号、地址这些敏感信息，会用加密方式存（就像给信息加了锁）', '{\"msg\":\"操作成功\",\"code\":200,\"data\":true}', 0, NULL, '2025-12-15 01:37:41', 20);
INSERT INTO `sys_oper_log` VALUES (304, '微信用户管理', 2, 'com.axin.web.controller.errand.WechatUserController.changeAdminStatus()', 'PUT', 1, 'admin', '研发部门', '/errand/wechatUser/admin/1/4', '127.0.0.1', '内网IP', '1 [4]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-15 23:25:03', 19);
INSERT INTO `sys_oper_log` VALUES (305, '用户头像', 2, 'com.axin.web.controller.system.SysProfileController.avatar()', 'POST', 1, 'admin', '研发部门', '/system/user/profile/avatar', '127.0.0.1', '内网IP', '', '{\"msg\":\"操作成功\",\"imgUrl\":\"/profile/avatar/2025/12/18/a07c676ee28f44e4b32a23f00283ee11.png\",\"code\":200}', 0, NULL, '2025-12-18 00:31:42', 224);
INSERT INTO `sys_oper_log` VALUES (306, '个人信息', 2, 'com.axin.web.controller.system.SysProfileController.updateProfile()', 'PUT', 1, 'admin', '研发部门', '/system/user/profile', '127.0.0.1', '内网IP', '{\"admin\":false,\"email\":\"ry@163.com\",\"nickName\":\"小玉子\",\"params\":{},\"phonenumber\":\"15888888888\",\"sex\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-18 23:14:01', 31);
INSERT INTO `sys_oper_log` VALUES (307, '商品规格管理', 1, 'com.axin.web.controller.errand.ProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"duration\":0,\"durationUnit\":\"0\",\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":50,\"priceMin\":0,\"specName\":\"超小件\",\"typeId\":3,\"weightMax\":5,\"weightMin\":0}', '{\"msg\":\"新增失败：同一服务模块下规格名称已存在\",\"code\":500}', 0, NULL, '2025-12-19 01:18:19', 14);
INSERT INTO `sys_oper_log` VALUES (308, '商品规格管理', 1, 'com.axin.web.controller.errand.ProductController.add()', 'POST', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"duration\":0,\"durationUnit\":\"0\",\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":50,\"priceMin\":0,\"specName\":\"超小小件\",\"typeId\":3,\"weightMax\":5,\"weightMin\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-19 01:18:26', 19);
INSERT INTO `sys_oper_log` VALUES (309, '商品规格管理', 2, 'com.axin.web.controller.errand.ProductController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/product', '127.0.0.1', '内网IP', '{\"amountMax\":0,\"amountMin\":0,\"duration\":0,\"durationUnit\":\"0\",\"id\":27,\"isEnabled\":1,\"isUniversalCustom\":0,\"priceMax\":50,\"priceMin\":0,\"specName\":\"超大件\",\"typeId\":3,\"weightMax\":5,\"weightMin\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-19 01:18:34', 29);
INSERT INTO `sys_oper_log` VALUES (310, '微信用户管理', 2, 'com.axin.web.controller.errand.WechatUserController.changeAdminStatus()', 'PUT', 1, 'admin', '研发部门', '/errand/wechatUser/admin/1/6', '127.0.0.1', '内网IP', '1 [6]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-20 00:43:08', 26);
INSERT INTO `sys_oper_log` VALUES (311, '提现申请管理', 5, 'com.axin.web.controller.errand.WithdrawApplyController.export()', 'POST', 1, 'admin', '研发部门', '/errand/withdrawApply/export', '127.0.0.1', '内网IP', '', NULL, 0, NULL, '2025-12-25 01:05:53', 631);
INSERT INTO `sys_oper_log` VALUES (312, '提现申请管理', 5, 'com.axin.web.controller.errand.WithdrawApplyController.export()', 'POST', 1, 'admin', '研发部门', '/errand/withdrawApply/export', '127.0.0.1', '内网IP', '', NULL, 0, NULL, '2025-12-25 01:07:10', 40);
INSERT INTO `sys_oper_log` VALUES (313, '小区管理', 2, 'com.axin.web.controller.errand.CommunityController.edit()', 'PUT', 1, 'admin', '研发部门', '/errand/community', '127.0.0.1', '内网IP', '{\"address\":\"广东省广州市花都区光明小区\",\"communityName\":\"光明小区\",\"contactPerson\":\"张三\",\"contactPhone\":\"17875132132\",\"id\":6,\"isEnabled\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-12-26 01:01:11', 31);
INSERT INTO `sys_oper_log` VALUES (314, '用户头像', 2, 'com.axin.web.controller.system.SysProfileController.avatar()', 'POST', 1, 'admin', '研发部门', '/system/user/profile/avatar', '127.0.0.1', '内网IP', '', '{\"msg\":\"操作成功\",\"imgUrl\":\"https://xyz-errand.oss-cn-guangzhou.aliyuncs.com/avatar/1/1768929946878.png\",\"code\":200}', 0, NULL, '2026-01-21 01:25:47', 135);

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `post_id` bigint NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '岗位名称',
  `post_sort` int NOT NULL COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '岗位信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, 'ceo', '董事长', 1, '0', 'admin', '2025-10-30 17:15:40', '', NULL, '');
INSERT INTO `sys_post` VALUES (2, 'se', '项目经理', 2, '0', 'admin', '2025-10-30 17:15:40', '', NULL, '');
INSERT INTO `sys_post` VALUES (3, 'hr', '人力资源', 3, '0', 'admin', '2025-10-30 17:15:40', '', NULL, '');
INSERT INTO `sys_post` VALUES (4, 'user', '普通员工', 4, '0', 'admin', '2025-10-30 17:15:41', '', NULL, '');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色权限字符串',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', 'admin', '2025-10-30 17:15:41', '', NULL, '超级管理员');
INSERT INTO `sys_role` VALUES (2, '普通角色', 'common', 2, '2', 0, 0, '0', '0', 'admin', '2025-10-30 17:15:41', 'admin', '2025-10-30 17:57:14', '普通角色');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色和部门关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_dept` VALUES (2, 100);
INSERT INTO `sys_role_dept` VALUES (2, 101);
INSERT INTO `sys_role_dept` VALUES (2, 105);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (2, 1);
INSERT INTO `sys_role_menu` VALUES (2, 2);
INSERT INTO `sys_role_menu` VALUES (2, 3);
INSERT INTO `sys_role_menu` VALUES (2, 4);
INSERT INTO `sys_role_menu` VALUES (2, 100);
INSERT INTO `sys_role_menu` VALUES (2, 101);
INSERT INTO `sys_role_menu` VALUES (2, 102);
INSERT INTO `sys_role_menu` VALUES (2, 103);
INSERT INTO `sys_role_menu` VALUES (2, 104);
INSERT INTO `sys_role_menu` VALUES (2, 105);
INSERT INTO `sys_role_menu` VALUES (2, 106);
INSERT INTO `sys_role_menu` VALUES (2, 107);
INSERT INTO `sys_role_menu` VALUES (2, 108);
INSERT INTO `sys_role_menu` VALUES (2, 109);
INSERT INTO `sys_role_menu` VALUES (2, 110);
INSERT INTO `sys_role_menu` VALUES (2, 111);
INSERT INTO `sys_role_menu` VALUES (2, 112);
INSERT INTO `sys_role_menu` VALUES (2, 113);
INSERT INTO `sys_role_menu` VALUES (2, 114);
INSERT INTO `sys_role_menu` VALUES (2, 115);
INSERT INTO `sys_role_menu` VALUES (2, 116);
INSERT INTO `sys_role_menu` VALUES (2, 117);
INSERT INTO `sys_role_menu` VALUES (2, 500);
INSERT INTO `sys_role_menu` VALUES (2, 501);
INSERT INTO `sys_role_menu` VALUES (2, 1000);
INSERT INTO `sys_role_menu` VALUES (2, 1001);
INSERT INTO `sys_role_menu` VALUES (2, 1002);
INSERT INTO `sys_role_menu` VALUES (2, 1003);
INSERT INTO `sys_role_menu` VALUES (2, 1004);
INSERT INTO `sys_role_menu` VALUES (2, 1005);
INSERT INTO `sys_role_menu` VALUES (2, 1006);
INSERT INTO `sys_role_menu` VALUES (2, 1007);
INSERT INTO `sys_role_menu` VALUES (2, 1008);
INSERT INTO `sys_role_menu` VALUES (2, 1009);
INSERT INTO `sys_role_menu` VALUES (2, 1010);
INSERT INTO `sys_role_menu` VALUES (2, 1011);
INSERT INTO `sys_role_menu` VALUES (2, 1012);
INSERT INTO `sys_role_menu` VALUES (2, 1013);
INSERT INTO `sys_role_menu` VALUES (2, 1014);
INSERT INTO `sys_role_menu` VALUES (2, 1015);
INSERT INTO `sys_role_menu` VALUES (2, 1016);
INSERT INTO `sys_role_menu` VALUES (2, 1017);
INSERT INTO `sys_role_menu` VALUES (2, 1018);
INSERT INTO `sys_role_menu` VALUES (2, 1019);
INSERT INTO `sys_role_menu` VALUES (2, 1020);
INSERT INTO `sys_role_menu` VALUES (2, 1021);
INSERT INTO `sys_role_menu` VALUES (2, 1022);
INSERT INTO `sys_role_menu` VALUES (2, 1023);
INSERT INTO `sys_role_menu` VALUES (2, 1024);
INSERT INTO `sys_role_menu` VALUES (2, 1025);
INSERT INTO `sys_role_menu` VALUES (2, 1026);
INSERT INTO `sys_role_menu` VALUES (2, 1027);
INSERT INTO `sys_role_menu` VALUES (2, 1028);
INSERT INTO `sys_role_menu` VALUES (2, 1029);
INSERT INTO `sys_role_menu` VALUES (2, 1030);
INSERT INTO `sys_role_menu` VALUES (2, 1031);
INSERT INTO `sys_role_menu` VALUES (2, 1032);
INSERT INTO `sys_role_menu` VALUES (2, 1033);
INSERT INTO `sys_role_menu` VALUES (2, 1034);
INSERT INTO `sys_role_menu` VALUES (2, 1035);
INSERT INTO `sys_role_menu` VALUES (2, 1036);
INSERT INTO `sys_role_menu` VALUES (2, 1037);
INSERT INTO `sys_role_menu` VALUES (2, 1038);
INSERT INTO `sys_role_menu` VALUES (2, 1039);
INSERT INTO `sys_role_menu` VALUES (2, 1040);
INSERT INTO `sys_role_menu` VALUES (2, 1041);
INSERT INTO `sys_role_menu` VALUES (2, 1042);
INSERT INTO `sys_role_menu` VALUES (2, 1043);
INSERT INTO `sys_role_menu` VALUES (2, 1044);
INSERT INTO `sys_role_menu` VALUES (2, 1045);
INSERT INTO `sys_role_menu` VALUES (2, 1046);
INSERT INTO `sys_role_menu` VALUES (2, 1047);
INSERT INTO `sys_role_menu` VALUES (2, 1048);
INSERT INTO `sys_role_menu` VALUES (2, 1049);
INSERT INTO `sys_role_menu` VALUES (2, 1050);
INSERT INTO `sys_role_menu` VALUES (2, 1051);
INSERT INTO `sys_role_menu` VALUES (2, 1052);
INSERT INTO `sys_role_menu` VALUES (2, 1053);
INSERT INTO `sys_role_menu` VALUES (2, 1054);
INSERT INTO `sys_role_menu` VALUES (2, 1055);
INSERT INTO `sys_role_menu` VALUES (2, 1056);
INSERT INTO `sys_role_menu` VALUES (2, 1057);
INSERT INTO `sys_role_menu` VALUES (2, 1058);
INSERT INTO `sys_role_menu` VALUES (2, 1059);
INSERT INTO `sys_role_menu` VALUES (2, 1060);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `pwd_update_date` datetime NULL DEFAULT NULL COMMENT '密码最后更新时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 103, 'admin', '小玉子', '00', 'ry@163.com', '15888888888', '0', 'https://xyz-errand.oss-cn-guangzhou.aliyuncs.com/avatar/1/1768929946878.png', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', '2026-01-20 23:04:50', '2025-10-30 17:15:40', 'admin', '2025-10-30 17:15:40', '', '2026-01-21 01:25:47', '管理员');
INSERT INTO `sys_user` VALUES (2, 105, 'ry', '若依', '00', 'ry@qq.com', '15666666666', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', '2025-10-30 17:15:40', '2025-10-30 17:15:40', 'admin', '2025-10-30 17:15:40', '', NULL, '测试员');

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户与岗位关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES (1, 1);
INSERT INTO `sys_user_post` VALUES (2, 2);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户和角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2);

-- ----------------------------
-- Table structure for takeaway_product
-- ----------------------------
DROP TABLE IF EXISTS `takeaway_product`;
CREATE TABLE `takeaway_product`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `store_id` bigint NOT NULL COMMENT '关联外卖店铺表id',
  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `price` bigint NOT NULL COMMENT '商品价格（分）',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品图片URL',
  `product_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '外卖商品类型',
  `is_enabled` tinyint NOT NULL DEFAULT 1 COMMENT '是否启用（1=启用，0=禁用）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_store_id`(`store_id` ASC) USING BTREE COMMENT '店铺id索引',
  CONSTRAINT `fk_takeaway_store` FOREIGN KEY (`store_id`) REFERENCES `takeaway_store` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '外卖商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of takeaway_product
-- ----------------------------
INSERT INTO `takeaway_product` VALUES (3, 3, '小份麻辣烫', 1200, '/profile/upload/2025/12/05/服务图标_20251205233103A002.png', '超值套餐', 1, '2025-12-05 23:31:18', '2025-12-05 23:31:18');
INSERT INTO `takeaway_product` VALUES (4, 3, '中份麻辣烫', 1400, '/profile/upload/2025/12/05/服务图标_20251205233132A003.png', '超值套餐', 1, '2025-12-05 23:31:37', '2025-12-05 23:31:37');
INSERT INTO `takeaway_product` VALUES (5, 3, '云吞', 1500, '/profile/upload/2025/12/05/服务图标_20251205233154A004.png', '汤粉类', 1, '2025-12-05 23:32:04', '2025-12-05 23:32:04');

-- ----------------------------
-- Table structure for takeaway_store
-- ----------------------------
DROP TABLE IF EXISTS `takeaway_store`;
CREATE TABLE `takeaway_store`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `community_id` bigint NULL DEFAULT NULL COMMENT '关联小区id',
  `store_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '店铺名称',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '店铺头像URL',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '店铺地址',
  `contact` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话（手机号）',
  `is_enabled` tinyint NOT NULL DEFAULT 1 COMMENT '是否启用（1=启用，0=禁用）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_store_name`(`store_name` ASC) USING BTREE COMMENT '店铺名称唯一',
  INDEX `idx_community_id`(`community_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '外卖店铺表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of takeaway_store
-- ----------------------------
INSERT INTO `takeaway_store` VALUES (3, 7, '麻辣烫', '/profile/upload/2025/12/05/服务图标_20251205233033A001.png', '地址111', '17875116728', 1, '2025-12-05 23:30:38', '2025-12-10 00:59:38');

-- ----------------------------
-- Table structure for wechat_user
-- ----------------------------
DROP TABLE IF EXISTS `wechat_user`;
CREATE TABLE `wechat_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `openid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户在小程序中的唯一标识',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户头像URL',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户手机号',
  `community_id` bigint NULL DEFAULT NULL COMMENT '关联小区ID',
  `total_commission` bigint NULL DEFAULT 0 COMMENT '累计佣金(分)',
  `pending_commission` bigint NULL DEFAULT 0 COMMENT '待结算佣金(分)',
  `settled_commission` bigint NULL DEFAULT 0 COMMENT '已结算佣金(分)',
  `is_enabled` tinyint NOT NULL DEFAULT 1 COMMENT '账号是否启用(1=启用, 0=禁用)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `detail_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '详细地址（小区和门牌号等）',
  `is_admin` tinyint(1) NULL DEFAULT 0 COMMENT '是否为管理员（1是 0否）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_openid`(`openid` ASC) USING BTREE COMMENT 'openid唯一',
  INDEX `idx_community_id`(`community_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '小程序用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wechat_user
-- ----------------------------
INSERT INTO `wechat_user` VALUES (1, 'oZIa06iUqoN-SriKCPaD5n8LIRJ4', 'Axin', 'http://localhost:8080/profile/upload/2025/12/14/xCAhlquREUTNd051c443dd8ea1e00f378b51dc968c13_20251214012134A001.jpeg', '17875114193', 7, 400, 400, NULL, 1, '2025-12-07 17:55:38', '2025-12-16 00:23:47', '1235653', 1);
INSERT INTO `wechat_user` VALUES (4, 'oZIa06gp0o5DI0pvl89T9nL6CAh0', '牛爱花', 'http://localhost:8080/profile/upload/2025/12/16/ib0VgsauGf3Uce84ce1f68407aac3376a9ccef511f5a_20251216002534A001.jpeg', '18455758943', 7, NULL, NULL, NULL, 1, '2025-12-15 23:14:49', '2025-12-17 00:23:53', 'asdsad', 1);
INSERT INTO `wechat_user` VALUES (5, 'oD2lk1x6Ccj8UePKn9ychfL3Mzj0', '牛爱花', 'http://localhost:8080/profile/upload/2025/12/20/oTOvEKR5sTXffd482ea7c86117aa421506924a333a40_20251220000733A001.jpeg', '18455758943', 7, NULL, NULL, NULL, 1, '2025-12-17 23:07:14', '2025-12-20 00:08:39', '501', NULL);
INSERT INTO `wechat_user` VALUES (6, 'oD2lk113x7Zx4t4TKPAOxFWUNQ04', 'Axin', 'https://xyz-errand.oss-cn-guangzhou.aliyuncs.com/upload/1768842186369.jpeg', '17875114193', 7, 1400, 0, NULL, 1, '2025-12-20 00:25:27', '2026-01-21 01:23:22', '301单元', 1);

-- ----------------------------
-- Table structure for withdraw_apply
-- ----------------------------
DROP TABLE IF EXISTS `withdraw_apply`;
CREATE TABLE `withdraw_apply`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `amount` bigint NOT NULL COMMENT '提现金额（分）',
  `credited_amount` bigint NOT NULL COMMENT '预计到账金额（分）',
  `rate` int NOT NULL COMMENT '提现比率（百分比）',
  `status` int NOT NULL DEFAULT 0 COMMENT '状态（0申请中 1审核通过 2审核拒绝 3已打款）',
  `pay_status` tinyint NOT NULL DEFAULT 0 COMMENT '打款状态：0未打款 1已打款',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '提现申请表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of withdraw_apply
-- ----------------------------
INSERT INTO `withdraw_apply` VALUES (1, 6, 400, 380, 5, 2, 0, NULL, '2025-12-24 01:26:45', '2025-12-24 01:47:01');
INSERT INTO `withdraw_apply` VALUES (2, 6, 400, 380, 5, 2, 0, NULL, '2025-12-24 01:46:54', '2025-12-24 01:47:20');
INSERT INTO `withdraw_apply` VALUES (3, 6, 400, 380, 5, 2, 0, NULL, '2025-12-25 00:36:07', '2025-12-25 00:38:27');
INSERT INTO `withdraw_apply` VALUES (4, 6, 400, 380, 5, 3, 1, NULL, '2025-12-25 00:51:56', '2025-12-25 01:07:28');
INSERT INTO `withdraw_apply` VALUES (5, 6, 1000, 950, 5, 3, 1, NULL, '2026-01-21 00:38:17', '2026-01-21 00:39:04');

-- ----------------------------
-- Table structure for runner_profile
-- ----------------------------
DROP TABLE IF EXISTS `runner_profile`;
CREATE TABLE `runner_profile`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '关联 wechat_user.id',
  `is_runner` tinyint NOT NULL DEFAULT 0 COMMENT '是否接单员 1是 0否',
  `online_status` tinyint NOT NULL DEFAULT 0 COMMENT '接单状态 1接单中 0休息中',
  `is_all_community` tinyint NOT NULL DEFAULT 0 COMMENT '是否服务全部小区 1是 0否',
  `max_concurrent` int NOT NULL DEFAULT 3 COMMENT '最大同时进行单数',
  `skills` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '可接类型，逗号分隔',
  `weight_boost` int NOT NULL DEFAULT 0 COMMENT '运营人工加权',
  `reject_streak` int NOT NULL DEFAULT 0 COMMENT '连续拒绝超时次数',
  `dispatch_miss_rounds` int NOT NULL DEFAULT 0 COMMENT '连续未入选轮次',
  `last_assigned_time` datetime NULL DEFAULT NULL COMMENT '最近被派单时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_runner_user`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '接单员档案表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for runner_community
-- ----------------------------
DROP TABLE IF EXISTS `runner_community`;
CREATE TABLE `runner_community`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `runner_user_id` bigint NOT NULL COMMENT '关联 wechat_user.id',
  `community_id` bigint NOT NULL COMMENT '关联 community.id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_runner_community`(`runner_user_id` ASC, `community_id` ASC) USING BTREE,
  INDEX `idx_rc_community`(`community_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '接单员服务小区表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_dispatch_log
-- ----------------------------
DROP TABLE IF EXISTS `order_dispatch_log`;
CREATE TABLE `order_dispatch_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` bigint NOT NULL COMMENT '关联 order_main.id',
  `runner_user_id` bigint NOT NULL COMMENT '被派/被推送的接单员 user_id',
  `score` decimal(5, 2) NULL DEFAULT NULL COMMENT '该轮得分',
  `dispatch_round` int NOT NULL DEFAULT 1 COMMENT '第几轮',
  `dispatch_type` tinyint NOT NULL COMMENT '1=推送抢单 2=系统指派',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '1=已推送 2=已接受 3=超时未接 4=已拒绝 5=被指派 9=他人已接',
  `is_read` tinyint NOT NULL DEFAULT 0 COMMENT '接单员是否已读提醒 0=未读 1=已读',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_dispatch_order`(`order_id` ASC) USING BTREE,
  INDEX `idx_dispatch_runner`(`runner_user_id` ASC) USING BTREE,
  INDEX `idx_dispatch_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '派单记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_job（智能派单兜底扫描：每 15 秒推进超时未接的外卖订单）
-- ----------------------------
INSERT INTO `sys_job` (`job_name`, `job_group`, `invoke_target`, `cron_expression`, `misfire_policy`, `concurrent`, `status`, `create_by`, `create_time`, `remark`)
VALUES ('智能派单兜底扫描', 'DEFAULT', 'dispatchTask.scan()', '0/15 * * * * ?', '3', '1', '0', 'admin', NOW(), '每15秒扫描超时未接的外卖订单，推进下一轮或自动指派');

SET FOREIGN_KEY_CHECKS = 1;
