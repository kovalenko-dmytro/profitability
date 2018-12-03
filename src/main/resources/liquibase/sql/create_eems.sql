CREATE TABLE `eems` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `annual_om_costs` decimal(12,3) DEFAULT NULL,
  `life_time` int(11) DEFAULT NULL,
  `initial_investment` decimal(15,3) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `init_savings` decimal(15,3) DEFAULT NULL,
  `irr` decimal(15,3) DEFAULT NULL,
  `npv` decimal(15,3) DEFAULT NULL,
  `npvq` decimal(15,3) DEFAULT NULL,
  `net_savings` decimal(15,3) DEFAULT NULL,
  `pay_back` decimal(15,3) DEFAULT NULL,
  `pay_off` decimal(15,3) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY (`project_id`),
  FOREIGN KEY (`project_id`) REFERENCES `projects` (`id`)
);