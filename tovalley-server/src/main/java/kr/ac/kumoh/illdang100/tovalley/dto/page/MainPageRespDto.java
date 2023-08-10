package kr.ac.kumoh.illdang100.tovalley.dto.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MainPageRespDto {

    @AllArgsConstructor
    @Data
    public static class MainPageAllRespDto {

        private List<NationalWeatherRespDto> nationalWeather; // 전국 지역 날씨 (5일)
        private AlertDto weatherAlert; // 기상 특보 정보
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
    private static class AlertDto {

        List<WeatherAlertDto> weatherAlerts;
        List<WeatherPreliminaryAlertDto> weatherPreAlerts;
    }

    @AllArgsConstructor
    @Data
    private static class WeatherAlertDto {

        private String weatherAlertType;
        private String title;
        private LocalDateTime announcementTime;
        private LocalDateTime effectiveTime;
        private String content;
    }

    @AllArgsConstructor
    @Data
    private static class WeatherPreliminaryAlertDto {

        private String weatherAlertType;
        private LocalDateTime announcementTime;
        private String title;
        private String content;
    }

    @AllArgsConstructor
    @Data
    public static class AccidentCountDto {

        private List<AccidentCountPerMonthDto> accidentCountPerMonth;

        private ProvinceEnum province;

        private Integer totalDeathCnt;
        private Integer totalDisappearanceCnt;
        private Integer totalInjuryCnt;
    }

    @AllArgsConstructor
    @Getter
    private enum ProvinceEnum {

        ULSAN("울산"), DAEJEON("대전"), GWANGJU("광주"), BUSAN("부산"),
        SEOUL("서울"), JEJU("제주"), SEJONG("세종"),
        JEOLLA("전라"), GYEONGGI("경기"), GYEONGSANG("경상"), CHUNGCHEONG("충청"), GANGWON("강원");
        private String value;
    }

    @AllArgsConstructor
    @Data
    private static class AccidentCountPerMonthDto {

        private Integer month;
        private Integer deathCnt;
        private Integer disappearanceCnt;
        private Integer injuryCnt;
    }

    @AllArgsConstructor
    @Data
    public static class NationalPopularWaterPlacesDto {

        // 계곡 이미지
        private Long waterPlaceId; // 물놀이 지역 pk
        private String waterPlaceName; // 물놀이 지역 이름
        private String location; // province + city
        private Double rating; // 평점
        private Integer reviewCnt; // 리뷰 개수
    }
}
