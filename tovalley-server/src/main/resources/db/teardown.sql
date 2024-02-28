SET REFERENTIAL_INTEGRITY FALSE; -- 모든 제약 조건 비활성화
truncate table member;
truncate table trip_schedule;
truncate table water_place;
truncate table review;
truncate table review_image;
truncate table accident;
truncate table rescue_supply;
truncate table water_place_weather;
truncate table water_place_detail;
truncate table national_region;
truncate table national_weather;
truncate table special_weather;
truncate table special_weather_detail;
truncate table lost_found_board;
truncate table lost_found_board_image;
truncate table comment;
truncate table chat_room;
truncate table chat_notification;
SET REFERENTIAL_INTEGRITY TRUE; -- 모든 제약 조건 활성화