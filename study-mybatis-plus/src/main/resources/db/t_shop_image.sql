CREATE TABLE `t_shop_image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `vendor_id` varchar(20) NOT NULL COMMENT '商家ID',
  `name` varchar(64) DEFAULT NULL COMMENT '图片名称',
  `out_id` varchar(64) NOT NULL COMMENT '用于存储外部标识，如存储在阿里云oss上对应的图片标识',
  `size` smallint(6) NOT NULL COMMENT '图片大小',
  `length` smallint(6) DEFAULT NULL COMMENT '图片长度',
  `width` smallint(6) DEFAULT NULL COMMENT '图片宽度',
  `img_url` varchar(255) DEFAULT NULL COMMENT '图片链接',
  `is_deleted` smallint(6) DEFAULT '0' COMMENT '逻辑删除标志 0:正常 1:删除',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `index_vendor_id` (`vendor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COMMENT='商家图片空间配置';

