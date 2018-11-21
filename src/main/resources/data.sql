INSERT INTO roles VALUES (1, 'admin') ON DUPLICATE KEY UPDATE role = 'admin';
INSERT INTO roles VALUES (2, 'user') ON DUPLICATE KEY UPDATE role = 'user';

INSERT INTO energy_types VALUES (1, 'Electricity') ON DUPLICATE KEY UPDATE name = 'Electricity';
INSERT INTO energy_types VALUES (2, 'Fuel') ON DUPLICATE KEY UPDATE name = 'Fuel';
INSERT INTO energy_types VALUES (3, 'District heating') ON DUPLICATE KEY UPDATE name = 'District heating';