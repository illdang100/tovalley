CREATE TABLE IF NOT EXISTS `member` (
    `member_id`	BIGINT	NOT NULL AUTO_INCREMENT,
    `created_date`	DATETIME(6)	NOT NULL,
    `last_modified_date`	DATETIME(6)	NOT NULL,
    `email`	VARCHAR(30)	NOT NULL,
    `member_name`	VARCHAR(30)	NOT NULL,
    `nickname`	VARCHAR(20)	NULL,
    `username`	VARCHAR(50)	NOT NULL,
    `password`	VARCHAR(60)	NULL,
    `role`	VARCHAR(10)	NOT NULL,
    `store_file_name`	VARCHAR(100)	NULL,
    `store_file_url`	VARCHAR(250)	NULL,
    primary key (member_id)
    ) engine=InnoDB;

CREATE TABLE IF NOT EXISTS `water_place` (
    `water_place_id`	BIGINT	NOT NULL AUTO_INCREMENT,
    `water_place_name`	VARCHAR(254)	NOT NULL,
    `province`	VARCHAR(20)	NOT NULL,
    `city`	VARCHAR(20)	NOT NULL,
    `town`	VARCHAR(20)	NULL,
    `sub_location`	VARCHAR(254)	NULL,
    `address`	VARCHAR(254)	NOT NULL,
    `water_place_category`	VARCHAR(20)	NULL,
    `latitude`	VARCHAR(20)	NOT NULL,
    `longitude`	VARCHAR(20)	NOT NULL,
    `management_type`	VARCHAR(8)	NOT NULL,
    `rating`	DOUBLE	NOT NULL,
    `review_count` INT NOT NULL,
    `store_file_name`	VARCHAR(100)	NULL,
    `store_file_url`	VARCHAR(250)	NULL,
    primary key (water_place_id)
    ) engine=InnoDB;

CREATE TABLE IF NOT EXISTS `review` (
    `review_id`	BIGINT	NOT NULL AUTO_INCREMENT,
    `trip_schedule_id`	BIGINT	NOT NULL,
    `review_content`	VARCHAR(256)	NOT NULL,
    `created_date`	DATETIME(6)	NOT NULL,
    `last_modified_date`	DATETIME(6)	NOT NULL,
    `rating`	DOUBLE	NOT NULL,
    `water_quality_review` VARCHAR(5) NOT NULL,
    primary key (review_id)
    ) engine=InnoDB;

CREATE TABLE IF NOT EXISTS `trip_schedule` (
    `trip_schedule_id`	BIGINT	NOT NULL AUTO_INCREMENT,
    `member_id`	BIGINT	NOT NULL	COMMENT 'member 테이블',
    `water_place_id`	BIGINT	NOT NULL,
    `trip_date`	DATETIME(6)	NOT NULL,
    `trip_number`	INT	NOT NULL,
    primary key (trip_schedule_id)
    ) engine=InnoDB;

CREATE TABLE IF NOT EXISTS `national_weather` (
    `national_weather_id`	BIGINT	NOT NULL AUTO_INCREMENT,
    `national_region_id`	BIGINT	NOT NULL,
    `climate`	VARCHAR(12)	NOT NULL,
    `lowest_temperature`	DOUBLE	NOT NULL,
    `highest_temperature`	DOUBLE	NOT NULL,
    `weather_date`	DATE	NOT NULL	COMMENT '언제 날씨 정보인지 나타냄.',
    `climate_icon`	VARCHAR(3)	NOT NULL,
    `climate_description`	VARCHAR(20)	NOT NULL,
    `rain_precipitation`	DOUBLE	NOT NULL,
    `humidity`	INT	NOT NULL,
    `wind_speed`	DOUBLE	NOT NULL,
    `clouds`	INT	NOT NULL,
    `day_feels_like`	DOUBLE	NOT NULL,
    primary key (national_weather_id)
    ) engine=InnoDB;

CREATE TABLE IF NOT EXISTS `accident` (
    `accident_id`	BIGINT	NOT NULL AUTO_INCREMENT,
    `water_place_id`	BIGINT	NOT NULL,
    `accident_date`	DATE	NOT NULL	COMMENT '사건 발생 날짜',
    `accident_condition`	VARCHAR(13)	NOT NULL	COMMENT '사망, 실종, 부상 중 어떤 상태인지 나타냄',
    `people_num`	INT	NOT NULL,
    primary key (accident_id)
    ) engine=InnoDB;

CREATE TABLE IF NOT EXISTS `special_weather` (
    `special_weather_id`	BIGINT	NOT NULL AUTO_INCREMENT,
    `weather_alert_type`	VARCHAR(11)	NOT NULL	COMMENT '기상특보 종류를 나타냄',
    `title`	VARCHAR(20)	NOT NULL,
    `announcement_time`	DATETIME(6)	NOT NULL,
    `effective_time`	DATETIME(6)	NOT NULL,
    `category`	VARCHAR(11)	NOT NULL,
    primary key (special_weather_id)
    ) engine=InnoDB;

CREATE TABLE IF NOT EXISTS `water_place_weather` (
    `water_place_weather_id`	BIGINT	NOT NULL AUTO_INCREMENT,
    `water_place_id`	BIGINT	NOT NULL,
    `climate`	VARCHAR(12)	NOT NULL,
    `lowest_temperature`	DOUBLE	NOT NULL,
    `highest_temperature`	DOUBLE	NOT NULL,
    `weather_date`	DATE	NOT NULL,
    `humidity`	INT	NOT NULL,
    `wind_speed`	DOUBLE	NOT NULL,
    `rain_precipitation`	DOUBLE	NOT NULL	COMMENT '강수 확률',
    `climate_icon`	VARCHAR(3)	NOT NULL,
    `climate_description`	VARCHAR(20)	NOT NULL,
    `created_date`	DATETIME(6)	NOT NULL,
    `last_modified_date`	DATETIME(6)	NOT NULL,
    `clouds`	INT	NOT NULL,
    `day_feels_like`	DOUBLE	NOT NULL,
    primary key (water_place_weather_id)
    ) engine=InnoDB;

CREATE TABLE IF NOT EXISTS `review_image` (
    `review_image_id`	BIGINT	NOT NULL AUTO_INCREMENT,
    `review_id`	BIGINT	NULL,
    `store_file_name`	VARCHAR(100)	NOT NULL,
    `store_file_url`	VARCHAR(250)	NOT NULL,
    primary key (review_image_id)
    ) engine=InnoDB;

CREATE TABLE IF NOT EXISTS `rescue_supply` (
    `rescue_supply_id`	BIGINT	NOT NULL AUTO_INCREMENT,
    `water_place_id`	BIGINT	NOT NULL,
    `life_boat_num`	INT	NULL,
    `portable_stand_num`	INT	NULL,
    `life_jacket_num`	INT	NULL,
    `life_ring_num`	INT	NULL,
    `rescue_rope_num`	INT	NULL,
    `rescue_rod_num`	INT	NULL,
    primary key (rescue_supply_id)
    ) engine=InnoDB;

CREATE TABLE IF NOT EXISTS `national_region` (
    `national_region_id`	BIGINT	NOT NULL AUTO_INCREMENT,
    `region_name`	VARCHAR(6)	NOT NULL,
    `latitude`	VARCHAR(20)	NOT NULL,
    `longitude`	VARCHAR(20)	NOT NULL,
    primary key (national_region_id)
    ) engine=InnoDB;

CREATE TABLE IF NOT EXISTS `special_weather_detail` (
    `special_weather_detail_id`	BIGINT	NOT NULL AUTO_INCREMENT,
    `special_weather_id`	BIGINT	NOT NULL,
    `content`	VARCHAR(500)	NOT NULL,
    primary key (special_weather_detail_id)
    ) engine=InnoDB;

CREATE TABLE IF NOT EXISTS `water_place_detail` (
    `water_place_detail_id`	BIGINT	NOT NULL AUTO_INCREMENT,
    `water_place_id`	BIGINT	NOT NULL,
    `water_place_segment`	VARCHAR(20)	NOT NULL,
    `deepest_depth`	VARCHAR(20)	NOT NULL,
    `avg_depth`	VARCHAR(20)	NOT NULL,
    `annual_visitors`	VARCHAR(20)	NULL,
    `danger_segments`	VARCHAR(20)	NULL,
    `danger_signboards_num`	VARCHAR(20)	NULL,
    `safety_measures`	VARCHAR(254)	NULL,
    `water_temperature` DOUBLE NULL,
    `bod` DOUBLE NULL,
    `turbidity` DOUBLE NULL,
    primary key (water_place_detail_id)
    ) engine=InnoDB;

CREATE TABLE IF NOT EXISTS `email_code` (
    `email_code_id`	BIGINT	NOT NULL AUTO_INCREMENT,
    `email`	VARCHAR(25)	NOT NULL,
    `verify_code`	VARCHAR(7)	NOT NULL,
    primary key (email_code_id)
    ) engine=InnoDB;

CREATE TABLE IF NOT EXISTS `chat_room` (
    `chat_room_id`	BIGINT	NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `sender_id`	BIGINT,
    `recipient_id`	BIGINT,
    `created_date`	DATETIME(6)	NOT NULL,
    `last_modified_date`	DATETIME(6)	NOT NULL,
    FOREIGN KEY (sender_id) REFERENCES member(member_id),
    FOREIGN KEY (recipient_id) REFERENCES member(member_id)
    ) engine=InnoDB;

CREATE TABLE IF NOT EXISTS `chat_notification` (
    `chat_notification_id` BIGINT NOT NULL AUTO_INCREMENT,
    `sender_id` BIGINT NOT NULL,
    `recipient_id` BIGINT NOT NULL,
    `chat_room_id` BIGINT NOT NULL,
    `content` VARCHAR(1000) NOT NULL,
    `has_read` BIT NOT NULL,
    `created_date` datetime(6) NOT NULL,
    `last_modified_date` datetime(6) NOT NULL,
    PRIMARY KEY (`chat_notification_id`),
    FOREIGN KEY (`sender_id`) REFERENCES `member` (`member_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `lost_found_board` (
    `lost_found_board_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `water_place_id` BIGINT,
    `member_id` BIGINT,
    `title` VARCHAR(20) NOT NULL,
    `content` VARCHAR(256) NOT NULL,
    `is_posting` BOOLEAN,
    `is_resolved` BOOLEAN,
    `lost_found_enum` VARCHAR(255),
    `created_date` TIMESTAMP,
    `last_modified_date` TIMESTAMP,

    FOREIGN KEY (water_place_id) REFERENCES water_place(water_place_id),
    FOREIGN KEY (member_id) REFERENCES member(member_id)
    ) engine=InnoDB;

CREATE TABLE IF NOT EXISTS `comment` (
    `comment_id`	BIGINT  NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '댓글 아이디',
    `lost_found_board_id`	BIGINT	NOT NULL COMMENT '분실물 찾기 게시글 아이디',
    `member_id` BIGINT COMMENT '댓글 작성자',
    `content`	VARCHAR(256)	NOT NULL COMMENT '댓글 내용',
    `created_date` TIMESTAMP,
    `last_modified_date` TIMESTAMP
    ) engine=InnoDB;

CREATE TABLE IF NOT EXISTS `lost_found_board_image` (
    `lost_found_board_image_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `store_file_name`     VARCHAR(100) NOT NULL,
    `store_file_url`      VARCHAR(250) NOT NULL,
    `lost_found_board_id` BIGINT       NOT NULL
    ) engine=InnoDB;


alter table member
    add constraint UK_mbmcqelty0fbrvxp1q58dn57t unique (email);

alter table member
    add constraint UK_hh9kg6jti4n1eoiertn2k6qsc unique (nickname);

alter table member
    add constraint UK_gc3jmn7c2abyo3wf6syln5t2i unique (username);

alter table accident
    add constraint FKecacv92y2xtul7k0bnsuhyrpa
        foreign key (water_place_id)
            references water_place (water_place_id);

alter table national_weather
    add constraint FK76g76mn84hqg6xnbv6ydoq9wy
        foreign key (national_region_id)
            references national_region (national_region_id);

alter table rescue_supply
    add constraint FKgvq5flqhwyvb5a19vrtne350k
        foreign key (water_place_id)
            references water_place (water_place_id);

alter table review
    add constraint FKayy598ogf3wajw1eyweu3js23
        foreign key (trip_schedule_id)
            references trip_schedule (trip_schedule_id)

alter table review_image
    add constraint FK16wp089tx9nm0obc217gvdd6l
        foreign key (review_id)
            references review (review_id);

alter table special_weather_detail
    add constraint FKn9ursr1scnjp3b7x7qgh7b2gk
        foreign key (special_weather_id)
            references special_weather (special_weather_id);

alter table trip_schedule
    add constraint FKl7bjsaf8xxk4jkmbd751y6mwr
        foreign key (member_id)
            references member (member_id);

alter table trip_schedule
    add constraint FK99xetyq1fqbsj8suxhvmsj6w0
        foreign key (water_place_id)
            references water_place (water_place_id);

alter table water_place_detail
    add constraint FKjpw5m09wg2gca4q8im49wlmlg
        foreign key (water_place_id)
            references water_place (water_place_id);

alter table water_place_weather
    add constraint FKdwubbp5esw94ysb1rulos56kb
        foreign key (water_place_id)
            references water_place (water_place_id);