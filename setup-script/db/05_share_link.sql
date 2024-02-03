CREATE TABLE IF NOT EXISTS `share_link` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `member_id` varchar(64) NOT NULL,
  `og_key` varchar(64) NOT NULL,
  `og_url` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL UNIQUE,
  `deep_link` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;