package kr.ac.kumoh.illdang100.tovalley.dto.weather;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class WeatherRespDto {

    @AllArgsConstructor
    @Data
    public static class SpecialWeatherRespDto {

        private String type;
        private String title;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime announcementDate;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime effectiveDate;
        private String content;
    }

    @AllArgsConstructor
    @Data
    public static class DailyWaterPlaceWeatherDto {

        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate weatherDate;
        private String climateIcon; // 날씨 아이콘 ID
        private String climateDescription; // 날씨 설명
        private Double lowestTemperature; // 최소 일일 온도
        private Double highestTemperature; // 최고 일일 온도
        private Integer humidity; // 습도(%)
        private Double windSpeed; // 풍속(m/s)
        private Double rainPrecipitation; // 강수량(mm)
        private Integer clouds; // 흐림(%)
        private Double dayFeelsLike; // 주간 체감 온도
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
}
