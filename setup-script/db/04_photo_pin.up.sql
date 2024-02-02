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
  `photo_pin_id` varchar(64) NOT NULL,
  `tag_id` varchar(64) NOT NULL,
  PRIMARY KEY (`photo_pin_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


INSERT INTO `photo_pin` (`id`, `member_id`, `photo_url`, `photo_date_time`, `latitude`, `longitude`, `created_at`, `modified_at`) VALUES
    ('photopin1', 'member1', 'https://dummyimage.com/300.png/01f/fff', '2024-01-01 00:00:00', -34.603722, -58.381592, '2024-01-01 00:00:00', '2024-01-01 00:00:00'),
    ('photopin2', 'member1', 'https://dummyimage.com/300.png/02f/fff', '2024-01-02 00:00:00', 34.052234, -118.243685, '2024-01-02 00:00:00', '2024-01-02 00:00:00'),
    ('photopin3', 'member1', 'https://dummyimage.com/300.png/03f/fff', '2024-01-02 00:00:00', 34.052234, -118.243685, '2024-01-02 00:00:00', '2024-01-02 00:00:00'),
    ('photopin4', 'member1', 'https://dummyimage.com/300.png/04f/fff', '2024-01-02 00:00:00', 34.052234, -118.243685, '2024-01-02 00:00:00', '2024-01-02 00:00:00'),
    ('photopin5', 'member1', 'https://dummyimage.com/300.png/05f/fff', '2024-01-14 00:00:00', 34.052234, -118.243685, '2024-01-14 00:00:00', '2024-01-02 00:00:00'),
    ('photopin6', 'member1', 'https://dummyimage.com/300.png/06f/fff', '2024-01-16 00:00:00', 34.052234, -118.243685, '2024-01-16 00:00:00', '2024-01-02 00:00:00'),
    ('photopin7', 'member1', 'https://dummyimage.com/300.png/07f/fff', '2024-01-02 00:00:00', 34.052234, -118.243685, '2024-01-13 00:00:00', '2024-01-02 00:00:00'),
    ('photopin8', 'member1', 'https://dummyimage.com/300.png/08f/fff', '2024-01-14 00:00:00', 34.052234, -118.243685, '2024-01-02 00:00:00', '2024-01-02 00:00:00'),
    ('photopin9', 'member1', 'https://dummyimage.com/300.png/09f/fff', '2024-01-02 00:00:00', 34.052234, -118.243685, '2024-01-17 00:00:00', '2024-01-02 00:00:00'),
    ('photopin10', 'member1', 'https://dummyimage.com/300.png/10f/fff', '2024-01-17 00:00:00', 34.052234, -118.243685, '2024-01-02 00:00:00', '2024-01-02 00:00:00'),
    ('photopin11', 'member1', 'https://dummyimage.com/300.png/11f/fff', '2024-01-21 00:00:00', 34.052234, -118.243685, '2024-01-12 00:00:00', '2024-01-02 00:00:00'),
    ('photopin12', 'member1', 'https://dummyimage.com/300.png/12f/fff', '2024-01-02 00:00:00', 34.052234, -118.243685, '2024-01-14 00:00:00', '2024-01-02 00:00:00'),
    ('photopin14', 'member1', 'https://dummyimage.com/300.png/132f/fff', '2024-01-30 00:00:00', 34.052234, -118.243685, '2024-01-16 00:00:00', '2024-01-02 00:00:00'),
    ('photopin15', 'member1', 'https://dummyimage.com/300.png/132f/fff', '2024-01-30 00:00:00', 34.052234, -118.243685, '2024-01-28 00:00:00', '2024-01-02 00:00:00'),
    ('photopin16', 'member1', 'https://dummyimage.com/300.png/132f/fff', '2024-01-30 00:00:00', 34.052234, -118.243685, '2024-01-14 00:00:00', '2024-01-02 00:00:00'),


INSERT INTO `photo_pin_tag_ids` (`photo_pin_id`, `tag_id`) VALUES
    ('photopin1', 'tag1'),
    ('photopin1', 'tag2'),
    ('photopin2', 'tag3'),
    ('photopin2', 'tag2'),
    ('photopin3', 'tag2'),
    ('photopin4', 'tag1'),
    ('photopin4', 'tag3'),
    ('photopin5', 'tag2'),
    ('photopin6', 'tag1'),
    ('photopin7', 'tag3'),
    ('photopin8', 'tag2'),
    ('photopin8', 'tag3'),
    ('photopin9', 'tag1'),
    ('photopin9', 'tag2'),
    ('photopin10', 'tag1'),
    ('photopin10', 'tag2'),
    ('photopin11', 'tag1'),
    ('photopin11', 'tag3'),
    ('photopin12', 'tag1'),
    ('photopin12', 'tag3'),
    ('photopin13', 'tag1'),
    ('photopin13', 'tag2'),
    ('photopin14', 'tag1'),
    ('photopin14', 'tag4'),
    ('photopin15', 'tag1'),
    ('photopin16', 'tag1'),
    ('photopin16', 'tag2'),

