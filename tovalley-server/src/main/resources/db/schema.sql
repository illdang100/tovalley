CREATE TABLE IF NOT EXISTS `member` (
                                        `member_id`	BIGINT	NOT NULL	COMMENT 'member 테이블',
                                        `created_date`	DATETIME(6)	NOT NULL,
    `last_modified_date`	DATETIME(6)	NOT NULL,
    `email`	VARCHAR(25)	NOT NULL,
    `member_name`	VARCHAR(5)	NOT NULL,
    `nickname`	VARCHAR(20)	NULL,
    `username`	VARCHAR(30)	NOT NULL,
    `password`	VARCHAR(60)	NULL,
    `role`	VARCHAR(10)	NOT NULL,
    `store_file_name`	VARCHAR(100)	NULL,
    `store_file_url`	VARCHAR(250)	NULL
    );

CREATE TABLE IF NOT EXISTS `water_place` (
                                             `water_place_id`	BIGINT	NOT NULL,
                                             `province`	VARCHAR(10)	NOT NULL	COMMENT '계곡의 (행정구역) 도를 의미한다.',
    `water_place_name`	VARCHAR(15)	NOT NULL,
    `water_quality`	DOUBLE	NULL,
    `deepest_depth`	DOUBLE	NULL,
    `average_depth`	DOUBLE	NULL,
    `water_place_address`	VARCHAR(50)	NOT NULL,
    `latitude`	VARCHAR(11)	NOT NULL,
    `longitude`	VARCHAR(11)	NOT NULL,
    `created_date`	DATETIME(6)	NOT NULL,
    `last_modified_date`	DATETIME(6)	NOT NULL,
    `water_place_category`	VARCHAR(6)	NOT NULL
    );

CREATE TABLE IF NOT EXISTS `review` (
                                        `review_id`	BIGINT	NOT NULL,
                                        `water_place_id`	BIGINT	NOT NULL,
                                        `member_id`	BIGINT	NOT NULL	COMMENT 'member 테이블',
                                        `review_content`	VARCHAR(256)	NOT NULL,
    `rating`	DOUBLE	NOT NULL	DEFAULT 0.0
    );

CREATE TABLE IF NOT EXISTS `trip_schedule` (
                                               `trip_schedule_id`	BIGINT	NOT NULL,
                                               `member_id`	BIGINT	NOT NULL	COMMENT 'member 테이블',
                                               `water_place_id`	BIGINT	NOT NULL,
                                               `trip_date`	DATETIME(6)	NOT NULL,
    `trip_number`	INT	NOT NULL
    );

CREATE TABLE IF NOT EXISTS `national_weather` (
                                                  `national_weather_id`	BIGINT	NOT NULL,
                                                  `national_region_id`	BIGINT	NOT NULL,
                                                  `climate`	VARCHAR(12)	NOT NULL,
    `lowest_temperature`	DOUBLE	NOT NULL,
    `highest_temperature`	DOUBLE	NOT NULL,
    `created_date`	DATETIME(6)	NOT NULL,
    `last_modified_date`	DATETIME(6)	NOT NULL,
    `weather_date`	DATE	NOT NULL	COMMENT '언제 날씨 정보인지 나타냄.'
    );

CREATE TABLE IF NOT EXISTS `accident` (
                                          `accident_id`	BIGINT	NOT NULL,
                                          `water_place_id`	BIGINT	NOT NULL,
                                          `accident_date`	DATE	NOT NULL	COMMENT '사건 발생 날짜',
                                          `accident_condition`	VARCHAR(13)	NOT NULL	COMMENT '사망, 실종, 부상 중 어떤 상태인지 나타냄',
    `people_num`	INT	NOT NULL
    );

CREATE TABLE IF NOT EXISTS `special_weather` (
                                                 `special_weather_id`	BIGINT	NOT NULL,
                                                 `special_weather_time_id`	BIGINT	NOT NULL,
                                                 `type`	VARCHAR(10)	NOT NULL	COMMENT '기상특보 종류를 나타냄',
    `title`	VARCHAR(20)	NOT NULL,
    `content`	VARCHAR(256)	NOT NULL
    );

CREATE TABLE IF NOT EXISTS `water_place_weather` (
                                                     `water_place_weather_id`	BIGINT	NOT NULL,
                                                     `water_place_id`	BIGINT	NOT NULL,
                                                     `climate`	VARCHAR(12)	NOT NULL,
    `lowest_temperature`	DOUBLE	NOT NULL,
    `highest_temperature`	DOUBLE	NOT NULL,
    `weather_date`	DATE	NOT NULL,
    `humidity`	INT	NOT NULL,
    `wind_speed`	DOUBLE	NOT NULL,
    `precipitation`	DOUBLE	NOT NULL	COMMENT '강수 확률',
    `climate_icon`	VARCHAR(3)	NOT NULL,
    `climate_description`	VARCHAR(20)	NOT NULL
    );

CREATE TABLE IF NOT EXISTS `review_image` (
                                              `review_image_id`	BIGINT	NOT NULL,
                                              `review_id`	BIGINT	NULL,
                                              `store_file_name`	VARCHAR(100)	NOT NULL,
    `store_file_url`	VARCHAR(250)	NOT NULL
    );

CREATE TABLE IF NOT EXISTS `rescue_supply` (
                                               `rescue_supply_id`	BIGINT	NOT NULL,
                                               `water_place_id`	BIGINT	NOT NULL,
                                               `life_boat_num`	INT	NULL,
                                               `life_jacket_num`	INT	NULL,
                                               `life_rope_num`	INT	NULL,
                                               `life_ring_num`	INT	NULL,
                                               `life_buoy_num`	INT	NULL	COMMENT '구조봉',
                                               `movable_cradle_num`	INT	NULL	COMMENT '이동식 거치대'
);

CREATE TABLE IF NOT EXISTS `national_region` (
                                                 `national_region_id`	BIGINT	NOT NULL,
                                                 `region_name`	VARCHAR(6)	NOT NULL,
    `latitude`	VARCHAR(11)	NOT NULL,
    `longitude`	VARCHAR(11)	NOT NULL
    );

CREATE TABLE IF NOT EXISTS `special_weather_time` (
                                                      `special_weather_time_id`	BIGINT	NOT NULL,
                                                      `announcement_time`	DATETIME(6)	NOT NULL,
    `effective_time`	DATETIME(6)	NOT NULL
    );

ALTER TABLE `member` ADD CONSTRAINT `PK_MEMBER` PRIMARY KEY (
                                                             `member_id`
    );

alter table member
    add constraint UK_mbmcqelty0fbrvxp1q58dn57t unique (email);

alter table member
    add constraint UK_hh9kg6jti4n1eoiertn2k6qsc unique (nickname);

alter table member
    add constraint UK_gc3jmn7c2abyo3wf6syln5t2i unique (username);

ALTER TABLE `water_place` ADD CONSTRAINT `PK_WATER_PLACE` PRIMARY KEY (
                                                                       `water_place_id`
    );

alter table water_place
    add constraint UK_traufdoq4e8aox5g804i7nlmr unique (water_place_name);

ALTER TABLE `review` ADD CONSTRAINT `PK_REVIEW` PRIMARY KEY (
                                                             `review_id`,
                                                             `water_place_id`,
                                                             `member_id`
    );

ALTER TABLE `trip_schedule` ADD CONSTRAINT `PK_TRIP_SCHEDULE` PRIMARY KEY (
                                                                           `trip_schedule_id`,
                                                                           `member_id`,
                                                                           `water_place_id`
    );

ALTER TABLE `national_weather` ADD CONSTRAINT `PK_NATIONAL_WEATHER` PRIMARY KEY (
                                                                                 `national_weather_id`,
                                                                                 `national_region_id`
    );

ALTER TABLE `accident` ADD CONSTRAINT `PK_ACCIDENT` PRIMARY KEY (
                                                                 `accident_id`,
                                                                 `water_place_id`
    );

ALTER TABLE `special_weather` ADD CONSTRAINT `PK_SPECIAL_WEATHER` PRIMARY KEY (
                                                                               `special_weather_id`,
                                                                               `special_weather_time_id`
    );

ALTER TABLE `water_place_weather` ADD CONSTRAINT `PK_WATER_PLACE_WEATHER` PRIMARY KEY (
                                                                                       `water_place_weather_id`,
                                                                                       `water_place_id`
    );

ALTER TABLE `review_image` ADD CONSTRAINT `PK_REVIEW_IMAGE` PRIMARY KEY (
                                                                         `review_image_id`,
                                                                         `review_id`
    );

ALTER TABLE `rescue_supply` ADD CONSTRAINT `PK_RESCUE_SUPPLY` PRIMARY KEY (
                                                                           `rescue_supply_id`,
                                                                           `water_place_id`
    );

ALTER TABLE `national_region` ADD CONSTRAINT `PK_NATIONAL_REGION` PRIMARY KEY (
                                                                               `national_region_id`
    );

ALTER TABLE `special_weather_time` ADD CONSTRAINT `PK_SPECIAL_WEATHER_TIME` PRIMARY KEY (
                                                                                         `special_weather_time_id`
    );

ALTER TABLE `review` ADD CONSTRAINT `FK_water_place_TO_review_1` FOREIGN KEY (
                                                                              `water_place_id`
    )
    REFERENCES `water_place` (
                              `water_place_id`
        );

ALTER TABLE `review` ADD CONSTRAINT `FK_member_TO_review_1` FOREIGN KEY (
                                                                         `member_id`
    )
    REFERENCES `member` (
                         `member_id`
        );

ALTER TABLE `trip_schedule` ADD CONSTRAINT `FK_member_TO_trip_schedule_1` FOREIGN KEY (
                                                                                       `member_id`
    )
    REFERENCES `member` (
                         `member_id`
        );

ALTER TABLE `trip_schedule` ADD CONSTRAINT `FK_water_place_TO_trip_schedule_1` FOREIGN KEY (
                                                                                            `water_place_id`
    )
    REFERENCES `water_place` (
                              `water_place_id`
        );

ALTER TABLE `national_weather` ADD CONSTRAINT `FK_national_region_TO_national_weather_1` FOREIGN KEY (
                                                                                                      `national_region_id`
    )
    REFERENCES `national_region` (
                                  `national_region_id`
        );

ALTER TABLE `accident` ADD CONSTRAINT `FK_water_place_TO_accident_1` FOREIGN KEY (
                                                                                  `water_place_id`
    )
    REFERENCES `water_place` (
                              `water_place_id`
        );

ALTER TABLE `special_weather` ADD CONSTRAINT `FK_special_weather_time_TO_special_weather_1` FOREIGN KEY (
                                                                                                         `special_weather_time_id`
    )
    REFERENCES `special_weather_time` (
                                       `special_weather_time_id`
        );

ALTER TABLE `water_place_weather` ADD CONSTRAINT `FK_water_place_TO_water_place_weather_1` FOREIGN KEY (
                                                                                                        `water_place_id`
    )
    REFERENCES `water_place` (
                              `water_place_id`
        );

ALTER TABLE `review_image` ADD CONSTRAINT `FK_review_TO_review_image_1` FOREIGN KEY (
                                                                                     `review_id`
    )
    REFERENCES `review` (
                         `review_id`
        );

ALTER TABLE `rescue_supply` ADD CONSTRAINT `FK_water_place_TO_rescue_supply_1` FOREIGN KEY (
                                                                                            `water_place_id`
    )
    REFERENCES `water_place` (
                              `water_place_id`
        );

