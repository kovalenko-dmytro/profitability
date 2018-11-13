INSERT INTO roles VALUES (1, 'admin') ON DUPLICATE KEY UPDATE role = 'admin';
INSERT INTO roles VALUES (2, 'user') ON DUPLICATE KEY UPDATE role = 'user';

INSERT INTO energy_types VALUES (1, 'electricity') ON DUPLICATE KEY UPDATE name = 'electricity';
INSERT INTO energy_types VALUES (2, 'fuel') ON DUPLICATE KEY UPDATE name = 'fuel';
INSERT INTO energy_types VALUES (3, 'district heating') ON DUPLICATE KEY UPDATE name = 'district heating';