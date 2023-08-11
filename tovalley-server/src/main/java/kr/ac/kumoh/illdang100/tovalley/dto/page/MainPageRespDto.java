package kr.ac.kumoh.illdang100.tovalley.dto.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MainPageRespDto {

    @AllArgsConstructor
    @Data
    public static class MainPageAllRespDto {

        private List<NationalWeatherRespDto> nationalWeather; // 전국 지역 날씨 (5일)
        private AlertRespDto weatherAlert; // 기상 특보 정보
        private AccidentCountDto accidentCountDto; // 전국 지역 올해 사고 발생수
        private List<NationalPopularWaterPlacesDto> nationalPopularWaterPlaces; // 전국 인기 물놀이 지역 리스트
    }

    @AllArgsConstructor
    @Data
    public static class NationalWeatherRespDto {

        private LocalDate weatherDate;
        private List<DailyNationalWeatherDto> dailyNationalWeather;
    }

    @AllArgsConstructor
    @Builder
    @Data
    public static class DailyNationalWeatherDto {

        private String region;
        private String weatherIcon;
        private String weatherDesc;
        private Double minTemp;
        private Double maxTemp;
        private Double rainPrecipitation;
    }

    @AllArgsConstructor
    @Data
    public static class AlertRespDto {

        List<WeatherAlertDto> weatherAlerts;
        List<WeatherPreliminaryAlertDto> weatherPreAlerts;
    }

    @AllArgsConstructor
    @Data
    public static class WeatherAlertDto {

        private String weatherAlertType;
        private String title;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime announcementTime;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime effectiveTime;
        private String content;
    }

    @AllArgsConstructor
    @Data
    public static class WeatherPreliminaryAlertDto {

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime announcementTime;
        private String title;
        private String weatherAlertType;
        private List<WeatherPreliminaryAlertContentDto> contents;
    }

    @AllArgsConstructor
    @Data
    public static class WeatherPreliminaryAlertContentDto {
        private String content;
    }

    @AllArgsConstructor
    @Data
    public static class AccidentCountDto {

        private List<AccidentCountPerMonthDto> accidentCountPerMonth;

        private String province;

        private Integer totalDeathCnt;
        private Integer totalDisappearanceCnt;
        private Integer totalInjuryCnt;
    }

    @AllArgsConstructor
    @Data
    public static class AccidentCountPerMonthDto {

        private Integer month;
        private Integer deathCnt;
        private Integer disappearanceCnt;
        private Integer injuryCnt;

        public void incrementDeathCnt(int cnt) {
            this.deathCnt = deathCnt + cnt;
        }

        public void incrementDisappearanceCnt(int cnt) {
            this.disappearanceCnt = disappearanceCnt + cnt;
        }

        public void incrementInjuryCnt(int cnt) {
            this.injuryCnt = injuryCnt + cnt;
        }
    }

    @AllArgsConstructor
    @Data
    public static class NationalPopularWaterPlacesDto {

        // 계곡 이미지
        private Long waterPlaceId; // 물놀이 지역 pk
        private String waterPlaceName; // 물놀이 지역 이름
        private String location; // province + city
        private String rating; // 평점
        private Integer reviewCnt; // 리뷰 개수
    }
}
