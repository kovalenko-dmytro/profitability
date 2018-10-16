INSERT INTO roles VALUES (1, 'admin') ON DUPLICATE KEY UPDATE role = 'admin';
INSERT INTO roles VALUES (2, 'user') ON DUPLICATE KEY UPDATE role = 'user';