CREATE TABLE `blog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `blog_title` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '标题',
  `blog_content` varchar(512) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '内容',
  `blog_author` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '作者',
  `state` int(10) DEFAULT NULL COMMENT '状态',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='博客';