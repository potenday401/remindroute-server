CREATE TABLE IF NOT EXISTS `photo_pin` (
  `id` varchar(64) NOT NULL UNIQUE,
  `member_id` varchar(64) NOT NULL,
  `photo_url` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL,
  `photo_date_time` datetime,
  `latitude` double,
  `longitude` double,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `modified_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `photo_pin_tag_ids` (
  `photo_pin_id` varchar(64) NOT NULL UNIQUE,
  `tag_id` varchar(64) NOT NULL,
  PRIMARY KEY (`photo_pin_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;