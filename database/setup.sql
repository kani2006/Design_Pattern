CREATE DATABASE IF NOT EXISTS restaurant_queue CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'restaurant_app'@'localhost' IDENTIFIED BY 'restaurant_app_password';
GRANT ALL PRIVILEGES ON restaurant_queue.* TO 'restaurant_app'@'localhost';
FLUSH PRIVILEGES;