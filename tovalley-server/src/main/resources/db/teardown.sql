SET REFERENTIAL_INTEGRITY FALSE; -- 모든 제약 조건 비활성화
truncate table member;
truncate table trip_schedule;
truncate table valley;
truncate table review;
truncate table review_image;
truncate table accident;
truncate table rescue_supply;
truncate table valley_weather;
truncate table medical_facility;
truncate table national_weather;
truncate table special_weather;
SET REFERENTIAL_INTEGRITY TRUE; -- 모든 제약 조건 활성화