CREATE TABLE IF NOT EXISTS `email_authentication` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `email` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `auth_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP NOT NULL,
  `authenticated_at` datetime NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;