CREATE TABLE `energy_tariffs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `value` decimal(10,3) NOT NULL,
  `energy_type_id` bigint(20) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY (`energy_type_id`),
  KEY (`project_id`),
  FOREIGN KEY (`energy_type_id`) REFERENCES `energy_types` (`id`),
  FOREIGN KEY (`project_id`) REFERENCES `projects` (`id`)
);