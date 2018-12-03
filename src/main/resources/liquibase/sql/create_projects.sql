CREATE TABLE `projects` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created` datetime(6) DEFAULT NULL,
  `inflation_rate` decimal(6,3) DEFAULT NULL,
  `nominal_rate` decimal(6,3) DEFAULT NULL,
  `real_rate` decimal(6,3) DEFAULT NULL,
  `title` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`title`),
  KEY (`user_id`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);