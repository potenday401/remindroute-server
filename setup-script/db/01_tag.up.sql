CREATE TABLE IF NOT EXISTS `tag` (
  `id` varchar(64) NOT NULL UNIQUE,
  `member_id` varchar(64) NOT NULL,
  `name` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `modified_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `tag` (`id`, `member_id`, `name`, `created_at`, `modified_at`) VALUES
('tag1', 'member1', 'tagA', '2024-01-01 00:00:00', '2024-01-01 00:00:00'),
('tag2', 'member1', 'tagB', '2024-01-01 00:00:00', '2024-01-01 00:00:00'),
('tag3', 'member1', 'tagC','2024-01-01 00:00:00', '2024-01-01 00:00:00'),
('tag4', 'member1', 'tagD', '2024-01-01 00:00:00', '2024-01-01 00:00:00'),
('tag5', 'member1', 'tagE', '2024-01-01 00:00:00', '2024-01-01 00:00:00');
