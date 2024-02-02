CREATE TABLE IF NOT EXISTS `tag` (
  `id` varchar(64) NOT NULL UNIQUE,
  `member_id` varchar(64) NOT NULL,
  `name` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `modified_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `tag` (`id`, `member_id`, `name`, `created_at`, `modified_at`) VALUES
('tag1', 'member1', 'Nature', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tag2', 'member1', 'Cityscape', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tag3', 'member1', 'Nightlife', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tag4', 'member1', 'Wildlife', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
('tag5', 'member1', 'Wildlife', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
