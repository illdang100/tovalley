SET REFERENTIAL_INTEGRITY FALSE; -- 모든 제약 조건 비활성화
truncate table member;
truncate table trip_schedule;
truncate table water_place;
truncate table review;
truncate table review_image;
truncate table accident;
truncate table rescue_supply;
truncate table water_place_weather;
truncate table medical_facility;
truncate table national_weather;
truncate table special_weather;
SET REFERENTIAL_INTEGRITY TRUE; -- 모든 제약 조건 활성화