CREATE TABLE `eem_energy_efficiencies` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `value` decimal(15,3) NOT NULL,
  `eem_id` bigint(20) DEFAULT NULL,
  `energy_type_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY (`eem_id`),
  KEY (`energy_type_id`),
  FOREIGN KEY (`eem_id`) REFERENCES `eems` (`id`),
  FOREIGN KEY (`energy_type_id`) REFERENCES `energy_types` (`id`)
);