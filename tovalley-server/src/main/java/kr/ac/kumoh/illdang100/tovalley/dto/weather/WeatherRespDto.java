package kr.ac.kumoh.illdang100.tovalley.dto.weather;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

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
    public static class WaterPlaceWeatherRespDto {
        private String climate; // 날씨 상태 (예: 맑음, 흐림, 비, 눈 등)
        private double lowestTemperature; // 최저 온도 (단위: 섭씨)
        private double highestTemperature; // 최고 온도 (단위: 섭씨)
        private double humidity; // 습도 (단위: %)
        private String windDirection; // 풍향 (예: 북풍, 서풍 등)
        private double windSpeed; // 풍속 (단위: m/s)
    }

}
